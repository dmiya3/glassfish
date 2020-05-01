/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.s1asdev.ejb.allowedmethods.remove;

import java.util.Enumeration;
import java.io.Serializable;
import java.rmi.RemoteException; 
import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;
import jakarta.ejb.EJBException;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;


public class HereLocalBean implements SessionBean {

    private SessionContext sc;

    public HereLocalBean() {}

    public void ejbCreate() throws RemoteException {
	System.out.println("In HereLocalBean::ejbCreate !!");
    }

    public void setSessionContext(SessionContext sc) {
	this.sc = sc;
    }

    public void test() {
	System.out.println("In HereLocalBean::test !!");
    }

    public void ejbRemove()
        throws RemoteException
    {
	System.out.println("In HereLocalBean::ejbRemove !!");
    }

    public void ejbActivate() {}

    public void ejbPassivate() {}
}
