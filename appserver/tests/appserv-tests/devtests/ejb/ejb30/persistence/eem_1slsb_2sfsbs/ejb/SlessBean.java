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

package com.sun.s1asdev.ejb.ejb30.persistence.eem_1slsb_2sfsbs;

import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityNotFoundException;

import javax.persistence.EntityManager;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
public class SlessBean
    implements Sless {

    @EJB
    private SfulPeer sfulPeer;

    public SfulPeer getSfulPeer() {
        return sfulPeer;
    }

}
