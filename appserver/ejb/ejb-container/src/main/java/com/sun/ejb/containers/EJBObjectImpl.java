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

package com.sun.ejb.containers;

import java.rmi.RemoteException;
import java.lang.reflect.Method;

import jakarta.ejb.*;
import com.sun.ejb.portable.*;

import com.sun.enterprise.util.LocalStringManagerImpl;

import java.util.Map;
import java.util.HashMap;

import java.util.logging.*;

/**
 * EJBObjectImpl implements EJBObject methods for EJBs.
 * It is extended by the generated concrete type-specific EJBObject
 * implementation (e.g. Hello_EJBObject).
 * Instances of this class are NEVER given to beans or clients.
 * Beans and clients get only stubs (instances of the stub class
 * generated by rmic).
 *
 */

public abstract class EJBObjectImpl
    extends EJBLocalRemoteObject
    implements EJBObject
{
    private static Class[] NO_PARAMS = new Class[] {};
    private static LocalStringManagerImpl localStrings =
        new LocalStringManagerImpl(EJBObjectImpl.class);
    private static Method REMOVE_METHOD = null;

    static {

        try {
            REMOVE_METHOD = EJBObject.class.getMethod("remove", NO_PARAMS);
        } catch ( NoSuchMethodException e ) {
            _logger.log(Level.FINE, "Exception retrieving remove method", e);
        }
    }

    transient private java.rmi.Remote stub;
    transient private java.rmi.Remote ejbObject;
    transient private Map businessStubs = new HashMap();
    transient private Map businessEJBObjects = new HashMap();
    transient private boolean beingCreated=false;

    // True if this object instance represents the RemoteHomeview
    // False if this object instance represents the RemoteBusiness view
    private boolean isRemoteHomeView;

    protected EJBObjectImpl() throws RemoteException {
    }

    final void setStub(java.rmi.Remote stub) {
        this.stub = stub;
    }

    /**
     * Stubs are keyed by the name of generated RMI-IIOP version of
     * each remote business interface.
     */
    final void setStub(String generatedBusinessInterface,
                       java.rmi.Remote stub) {
        businessStubs.put(generatedBusinessInterface, stub);
    }

    public final java.rmi.Remote getStub() {
        return stub;
    }

    public final java.rmi.Remote getStub(String generatedBusinessInterface) {
        return (java.rmi.Remote) businessStubs.get(generatedBusinessInterface);
    }

    void setIsRemoteHomeView(boolean flag) {
        isRemoteHomeView = flag;
    }

    boolean isRemoteHomeView() {
        return isRemoteHomeView;
    }

    /**
     * Get the Remote object corresponding to an EJBObjectImpl for
     * the RemoteHome view.
     */
    public java.rmi.Remote getEJBObject() {
        return ejbObject;
    }

    /**
     * Get the Remote object corresponding to an EJBObjectImpl for
     * the RemoteBusiness view.
     */
    public java.rmi.Remote getEJBObject(String generatedBusinessInterface) {
        return (java.rmi.Remote) businessEJBObjects.get
            (generatedBusinessInterface);
    }

    public void setEJBObject(java.rmi.Remote ejbObject) {
        this.ejbObject = ejbObject;
    }

    public void setEJBObject(String generatedBusinessInterface,
                             java.rmi.Remote ejbObject) {
        businessEJBObjects.put(generatedBusinessInterface, ejbObject);
    }

    final void setBeingCreated(boolean b) {
        beingCreated = b;
    }

    final boolean isBeingCreated() {
        return beingCreated;
    }

    /**************************************************************************
    The following are implementations of EJBObject methods.
     **************************************************************************/
    /**
     */
    public boolean isIdentical(EJBObject ejbo) throws RemoteException {
        container.authorizeRemoteMethod(BaseContainer.EJBObject_isIdentical);

        return container.isIdentical(this, ejbo);
    }


    /**
     */
    public Object getPrimaryKey() throws RemoteException {
        container.authorizeRemoteGetPrimaryKey(this);
        return primaryKey;
    }

    /**
     *
     */
    public final EJBHome getEJBHome() throws RemoteException {
        container.authorizeRemoteMethod(BaseContainer.EJBObject_getEJBHome);

        return container.getEJBHomeStub();
    }

    /**
     * This is called when the EJB client does ejbref.remove().
     * or EJBHome/LocalHome.remove(primaryKey).
     * Since there is no generated code in the *_EJBObjectImpl class
     * for remove, we need to call preInvoke, postInvoke etc here.
     */
    public final void remove() throws RemoteException, RemoveException {

        // authorization is performed within container

        container.removeBean(this, REMOVE_METHOD, false);
    }

    /**
     * This is called when the EJB client does ejbref.getHandle().
     * Return a serializable implementation of jakarta.ejb.Handle.
     */
    public final Handle getHandle() throws RemoteException {
        container.authorizeRemoteMethod(BaseContainer.EJBObject_getHandle);

        // We can assume the stub an EJBObject since getHandle() is only
        // visible through the RemoteHome view.
        return new HandleImpl((EJBObject)stub);
    }
}