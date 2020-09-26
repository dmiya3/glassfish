/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.resource;

import com.sun.enterprise.resource.allocator.ResourceAllocator;
import com.sun.enterprise.resource.allocator.LocalTxConnectorAllocator;
import com.sun.enterprise.connectors.ConnectorRuntime;
import com.sun.logging.LogDomains;
import com.sun.appserv.connectors.internal.api.PoolingException;
import com.sun.enterprise.transaction.spi.TransactionalResource;

import jakarta.resource.spi.ConnectionEventListener;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import jakarta.transaction.Transaction;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * ResourceHandle encapsulates a resource connection.
 * Equality on the handle is based on the id field
 *
 * @author Tony Ng
 */
public class ResourceHandle implements
        com.sun.appserv.connectors.internal.api.ResourceHandle, TransactionalResource {

    // unique ID for resource handles
    static private long idSequence;

    private long id;
    private ClientSecurityInfo info;
    private Object resource;  // represents MC
    private ResourceSpec spec;
    private XAResource xares;
    private Object usercon;   // represents connection-handle to user
    private ResourceAllocator alloc;
    private Object instance;  // the component instance holding this resource
    private int shareCount;   // sharing within a component (XA only)
    private boolean supportsXAResource = false;

    private volatile boolean busy;

    private Subject subject = null;

    private ResourceState state = null;
    private ConnectionEventListener listener = null;

    private boolean enlistmentSuspended = false;

    private boolean supportsLazyEnlistment_ = false;
    private boolean supportsLazyAssoc_ = false;

    private static Logger logger = LogDomains.getLogger(ResourceHandle.class, LogDomains.RSR_LOGGER);

    public final Object lock = new Object();
    private long lastValidated; //holds the latest time at which the connection was validated.
    private int usageCount; //holds the no. of times the handle(connection) is used so far.
    private int partition;
    private boolean isDestroyByLeakTimeOut = false;

    static private long getNextId() {
        synchronized (ResourceHandle.class) {
            idSequence++;
            return idSequence;
        }
    }
    private boolean markedReclaim = false;
    
    public ResourceHandle(Object resource,
                          ResourceSpec spec,
                          ResourceAllocator alloc,
                          ClientSecurityInfo info) {
        this.id = getNextId();
        this.spec = spec;
        this.info = info;
        this.resource = resource;
        this.alloc = alloc;

        if (alloc instanceof LocalTxConnectorAllocator)
            supportsXAResource = false;
        else
            supportsXAResource = true;

        if (resource instanceof
                jakarta.resource.spi.LazyEnlistableManagedConnection) {
            supportsLazyEnlistment_ = true;
        }

        if (resource instanceof
                jakarta.resource.spi.DissociatableManagedConnection) {
            supportsLazyAssoc_ = true;
        }
    }

/*    public ResourceHandle(Object resource,
                          ResourceSpec spec,
                          ResourceAllocator alloc,
                          ClientSecurityInfo info,
                          boolean supportsXA) {
        this.id = getNextId();
        this.spec = spec;
        this.info = info;
        this.resource = resource;
        this.alloc = alloc;

        supportsXAResource = supportsXA;

        if (resource instanceof
                jakarta.resource.spi.LazyEnlistableManagedConnection) {
            supportsLazyEnlistment_ = true;
        }

        if (resource instanceof
                jakarta.resource.spi.DissociatableManagedConnection) {
            supportsLazyAssoc_ = true;
        }
    } */


    /**
     * Does this resource need enlistment to transaction manager?
     */
    public boolean isTransactional() {
        return alloc.isTransactional();
    }

    /**
     * To check whether lazy enlistment is suspended or not.<br>
     * If true, TM will not do enlist/lazy enlist.
     *
     * @return boolean
     */
    public boolean isEnlistmentSuspended() {
        return enlistmentSuspended;
    }

    public void setEnlistmentSuspended(boolean enlistmentSuspended) {
        this.enlistmentSuspended = enlistmentSuspended;
    }

    public void markForReclaim(boolean reclaim) {
        this.markedReclaim = reclaim;
    }

    /**
     * To check if the resourceHandle is marked for leak reclaim or not. <br>
     * 
     * @return boolean
     */
    public boolean isMarkedForReclaim() {
        return markedReclaim;
    }

    public boolean supportsXA() {
        return supportsXAResource;
    }

    public ResourceAllocator getResourceAllocator() {
        return alloc;
    }

    public Object getResource() {
        return resource;
    }

    public ClientSecurityInfo getClientSecurityInfo() {
        return info;
    }

    public void setResourceSpec(ResourceSpec spec) {
        this.spec = spec;
    }

    public ResourceSpec getResourceSpec() {
        return spec;
    }

    public XAResource getXAResource() {
        return xares;
    }

    public Object getUserConnection() {
        return usercon;
    }

    public void setComponentInstance(Object instance) {
        this.instance = instance;
    }

    public void closeUserConnection() throws PoolingException {
        getResourceAllocator().closeUserConnection(this);
    }

    public Object getComponentInstance() {
        return instance;
    }

    public long getId() {
        return id;
    }

    public void fillInResourceObjects(Object userConnection,
                                      XAResource xaRes) {
        if (userConnection != null) usercon = userConnection;

        if (xaRes != null) {
            if (logger.isLoggable(Level.FINEST)) {
                //When Log level is Finest, XAResourceWrapper is used to log
                //all XA interactions - Don't wrap XAResourceWrapper if it is
                //already wrapped
                if ((xaRes instanceof XAResourceWrapper) ||
                        (xaRes instanceof ConnectorXAResource)) {
                    this.xares = xaRes;
                } else {
                    this.xares = new XAResourceWrapper(xaRes);
                }
            } else {
                this.xares = xaRes;
            }
        }

    }

    // For XA-capable connections, multiple connections within a
    // component are collapsed into one. shareCount keeps track of
    // the number of additional shared connections
    public void incrementCount() {
        shareCount++;
    }

    public void decrementCount() {
        if (shareCount == 0) {
            throw new IllegalStateException("shareCount cannot be negative");
        } else {
            shareCount--;
        }
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof ResourceHandle) {
            return this.id == (((ResourceHandle) other).id);
        }
        return false;
    }

    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    public String toString() {
        return String.valueOf(id);
    }

    private boolean connectionErrorOccurred = false;

    public void setConnectionErrorOccurred() {
        connectionErrorOccurred = true;
    }

    public boolean hasConnectionErrorOccurred() {
        return connectionErrorOccurred;
    }

    public void setResourceState(ResourceState state) {
        this.state = state;
    }

    public ResourceState getResourceState() {
        return state;
    }

    public void setListener(ConnectionEventListener l) {
        this.listener = l;
    }

    public ConnectionEventListener getListener() {
        return listener;
    }

    public boolean isShareable() {
        return alloc.shareableWithinComponent();
    }

    public void destroyResource() {
        throw new UnsupportedOperationException("Transaction is not supported yet");
    }

    public boolean isEnlisted() {
        return state != null && state.isEnlisted();
    }

    public long getLastValidated() {
        return lastValidated;
    }

    public void setLastValidated(long lastValidated) {
        this.lastValidated = lastValidated;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void incrementUsageCount() {
        usageCount++;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getName() {
        return spec.getResourceId();
    }

    public boolean supportsLazyEnlistment() {
        return supportsLazyEnlistment_;
    }

    public boolean supportsLazyAssociation() {
        return supportsLazyAssoc_;
    }

    public void enlistedInTransaction(Transaction tran) throws IllegalStateException {
        ConnectorRuntime.getRuntime().getPoolManager().resourceEnlisted(tran, this);
    }

    public void setBusy(boolean isBusy){
        busy = isBusy;
    }

    public boolean isBusy(){
        return busy;
    }

    public boolean getDestroyByLeakTimeOut(){
        return isDestroyByLeakTimeOut;
    }

    public void setDestroyByLeakTimeOut(boolean isDestroyByLeakTimeOut){
        this.isDestroyByLeakTimeOut = isDestroyByLeakTimeOut;
    }
}
