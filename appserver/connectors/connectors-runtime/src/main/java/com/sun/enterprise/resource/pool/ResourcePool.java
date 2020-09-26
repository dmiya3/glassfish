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

package com.sun.enterprise.resource.pool;


import com.sun.appserv.connectors.internal.api.PoolingException;
import com.sun.enterprise.connectors.ConnectorConnectionPool;
import com.sun.enterprise.resource.ResourceHandle;
import com.sun.enterprise.resource.ResourceSpec;
import com.sun.enterprise.resource.allocator.ResourceAllocator;
import com.sun.enterprise.resource.listener.PoolLifeCycleListener;
import com.sun.enterprise.resource.pool.waitqueue.PoolWaitQueue;
import org.glassfish.resourcebase.resources.api.PoolInfo;

import jakarta.resource.spi.RetryableUnavailableException;
import jakarta.transaction.Transaction;

/**
 * @author Tony Ng
 */
public interface ResourcePool {

    // start IASRI 4649256
    // Modify getResource() to throw PoolingException
    public ResourceHandle getResource(ResourceSpec spec,
                                      ResourceAllocator alloc,
                                      Transaction tran) throws PoolingException, RetryableUnavailableException;
    // end IASRI 4649256

    public void resourceClosed(ResourceHandle resource);

    public void resourceErrorOccurred(ResourceHandle resource);

    public void resourceEnlisted(Transaction tran, ResourceHandle resource);

    //Get status of pool
    public PoolStatus getPoolStatus();

    public void transactionCompleted(Transaction tran, int status);

    public void resizePool(boolean forced);

    // forcefully destroy all connections in the pool even if
    // connections have transactions in progress
    public void emptyPool();


    //reconfig the pool's properties
    public void reconfigurePool(ConnectorConnectionPool ccp)
            throws PoolingException;


    //cancel the resizer task in the pool
    public void cancelResizerTask();

    public void switchOnMatching();

    public PoolInfo getPoolInfo();

    public void emptyFreeConnectionsInPool();

    //accessors for self mgmt
    /**
     * Gets the max-pool-size attribute of this pool. Envisaged to be
     * used by the Self management framework to query the pool size
     * attribute for tweaking it using the setMaxPoolSize method
     *
     * @return max-pool-size value for this pool
     * @see setMaxPoolSize
     */
    public int getMaxPoolSize();

    /**
     * Gets the steady-pool-size attribute of this pool. Envisaged to be
     * used by the Self management framework to query the pool size
     * attribute for tweaking it using the setSteadyPoolSize method
     *
     * @return steady-pool-size value for this pool
     * @see setSteadyPoolSize
     */
    public int getSteadyPoolSize();

    //mutators for self mgmt
    /**
     * Sets the max-pool-size value for this pool. This attribute is
     * expected to be set by the self-management framework for an optimum
     * max-pool-size. The corresponding accessor gets this value.
     *
     * @param size - The new max-pool-size value
     * @see getMaxPoolSize
     */
    public void setMaxPoolSize(int size);

    /**
     * Sets the steady-pool-size value for this pool. This attribute is
     * expected to be set by the self-management framework for an optimum
     * steady-pool-size. The corresponding accessor gets this value.
     *
     * @param size - The new steady-pool-size value
     * @see getSteadyPoolSize
     */
    public void setSteadyPoolSize(int size);

    /**
     * Sets/Resets the flag indicating if this pool is self managed.
     * This method would be typically called by the self management
     * framework to indicate to the world (and this pool) that this
     * pool is self managed. Its very important that the self mgmt
     * framework properly sets this flag to control the dynamic reconfig
     * behavior of this pool. If this flag is set to true, all dynamic
     * reconfigs affecting the max/steady pool size of this pool will
     * be ignored.
     *
     * @param selfManaged - true to switch on self management, false otherwise
     */
    public void setSelfManaged(boolean selfManaged);

    /**
     * set pool life cycle listener
     * @param listener
     */
    public void setPoolLifeCycleListener(PoolLifeCycleListener listener);

    /**
     * remove pool life cycle listener
     */
    public void removePoolLifeCycleListener();

    /**
     * Flush Connection pool by reinitializing the connections 
     * established in the pool.
     * @return boolean indicating whether flush operation was successful or not
     * @throws com.sun.appserv.connectors.internal.api.PoolingException
     */
    public boolean flushConnectionPool() throws PoolingException;

    /**
     * block any new requests to the pool
     * Used for transparent dynamic reconfiguration of the pool
     * @param waitTimeout time for which the new requests will wait
     */
    public void blockRequests(long waitTimeout);

    /**
     * returns pool-wait-queue
     * @return wait-queue
     */
    public PoolWaitQueue getPoolWaitQueue();

    /**
     * returns wait-queue used during transparent dynamic reconfiguration
     * @return PoolWaitQueue
     */
    public PoolWaitQueue getReconfigWaitQueue();

    /**
     * returns the reconfig-wait-time
     * @return long
     */
    public long getReconfigWaitTime();
}


