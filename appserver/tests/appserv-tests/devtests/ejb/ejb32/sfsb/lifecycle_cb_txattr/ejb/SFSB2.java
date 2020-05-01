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

package com.acme;

import jakarta.ejb.*;
import javax.persistence.*;
import jakarta.annotation.*;

import javax.naming.InitialContext;

import javax.management.j2ee.ManagementHome;

@Stateful
@LocalBean
public class SFSB2 implements Hello2 {

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void init() {
        System.out.println("In SFSB2::init()");
    }
    
    public String test() throws EJBException {
	System.out.println("In SFSB2::test()");
        return "SFSB2";
    }

    @PreDestroy
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void destroy() {
        System.out.println("In SFSB2::destroy()");
    }
}
