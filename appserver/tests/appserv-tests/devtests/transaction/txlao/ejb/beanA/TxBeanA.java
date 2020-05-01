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

package com.sun.s1peqe.transaction.txlao.ejb.beanA;

import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.transaction.UserTransaction;
import java.rmi.RemoteException;
import com.sun.s1peqe.transaction.txlao.ejb.beanB.*;

public class TxBeanA implements SessionBean {

    private TxRemoteHomeB home = null;
    private UserTransaction tx = null;
    private SessionContext context = null;

    // ------------------------------------------------------------------------
    // Container Required Methods
    // ------------------------------------------------------------------------
    public void ejbCreate() throws RemoteException {
        Class homeClass = TxRemoteHomeB.class;
        System.out.println("ejbCreate in BeanA");
        try {
            Context ic = new InitialContext();
            java.lang.Object obj = ic.lookup("java:comp/env/ejb/TxBeanB");
            home = (TxRemoteHomeB) PortableRemoteObject.narrow(obj, homeClass);
         } catch (Exception ex) {
            System.out.println("Exception in ejbCreate: " + ex.toString());
            ex.printStackTrace();
        }
    }

    public void setSessionContext(SessionContext sc) {
        System.out.println("setSessionContext in BeanA");
        this.context = sc;
    }

    public void ejbRemove() {
        System.out.println("ejbRemove in BeanA");
    }
  
    public void ejbDestroy() {
        System.out.println("ejbDestroy in BeanA");
    }

    public void ejbActivate() {
        System.out.println("ejbActivate in BeanA");
    }

    public void ejbPassivate() {
        System.out.println("ejbPassivate in BeanA");
    }


    // ------------------------------------------------------------------------
    // Business Logic Methods
    // ------------------------------------------------------------------------
    public boolean firstXAJDBCSecondNonXAJDBC() throws RemoteException {
        boolean result = false;
        System.out.println("firstXAJDBCSecondNonXAJDBC in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }
            tx.begin();
            beanB.firstXAJDBCSecondNonXAJDBC("A1001", 3000);
            tx.commit();

            tx.begin();
            result = beanB.verifyResults("A1001", "DB1", "XA");
            result = result && beanB.verifyResults("A1001", "DB2", "NonXA");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstXAJDBCSecondNonXAJDBC: " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }

    public boolean firstNonXAJDBCSecondXAJDBC() throws RemoteException {
        boolean result = false;
        System.out.println("firstNonXAJDBCSecondXAJDBC in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.firstNonXAJDBCSecondXAJDBC("A1002", 3000);
            tx.commit();

            tx.begin();
            result = beanB.verifyResults("A1002", "DB1", "NonXA");
            result = result && beanB.verifyResults("A1002", "DB2", "XA");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstNonXAJDBCSecondXAJDBC: " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }

    public boolean firstXAJDBCSecondXAJDBC() throws RemoteException {
        boolean result = false;
        System.out.println("firstXAJDBCSecondXAJDBC in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.firstXAJDBCSecondXAJDBC("A1003", 3000);
            tx.commit();

            tx.begin();
            result = beanB.verifyResults("A1003", "DB1", "XA");
            result = result && beanB.verifyResults("A1003", "DB2", "XA");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstXAJDBCSecondXAJDBC: " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }

    public boolean firstNonXAJDBCSecondNonXAJDBC() throws RemoteException {
       boolean result = false;
        System.out.println("firstNonXAJDBCSecondNonXAJDBC in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.firstNonXAJDBCSecondNonXAJDBC("A1004", 3000);
            tx.commit();

            tx.begin();
            result = beanB.verifyResults("A1004", "DB1", "NonXA");
            result = result && beanB.verifyResults("A1004", "DB2", "NonXA");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstXAJDBCSecondNonXAJDBC: " + ex.toString());
            ex.printStackTrace();
            result = true;
        }
        return result;
    }

    public boolean firstXAJMSSecondNonXAJDBC() throws RemoteException {
        boolean result = false;
        System.out.println("firstXAJMSSecondNonXAJDBC in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.firstXAJMSSecondNonXAJDBC("JMS Message-1","A1005", 3000);
            tx.commit();
             System.out.println("beanA:firstXAJMSSecondNonXAJDBC:verifying..");
            tx.begin();
            result = beanB.verifyResults("A1005", "DB2", "NonXA");
                System.out.println("beanA:firstXAJMSSecondNonXAJDBC:A1005+result.."+result);
            result = result && beanB.verifyResults("JMS Message-1", "JMS", "");
                System.out.println("beanA:firstXAJMSSecondNonXAJDBC:JMS+result.."+result);
            tx.commit();

            System.out.println("beanA:firstXAJMSSecondNonXAJDBC:verification over");
            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstXAJMSSecondNonXAJDBC: " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }

    public boolean firstNonXAJDBCOnly() throws RemoteException {
        boolean result = false;
        System.out.println("firstNonXAJDBCOnly in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.firstNonXAJDBCOnly("A1006", 3000);
            tx.commit();

            tx.begin();
            result = beanB.verifyResults("A1006", "DB1", "NonXA");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in firstNonXAJDBCOnly: " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }
    public void cleanup() throws RemoteException {
        System.out.println("cleanup in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.delete("A1001");
            tx.commit();
            tx.begin();
            beanB.delete("A1002");
            tx.commit();
            tx.begin();
            beanB.delete("A1003");
            tx.commit();
            tx.begin();
            beanB.delete("A1004");
            tx.commit();
            tx.begin();
            beanB.delete("A1005");
            tx.commit();
            tx.begin();
            beanB.delete("A1006");
            tx.commit();

            beanB.remove();
        } catch (Exception ex) {
            try{
                if(tx != null)
                tx.rollback();
            }catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Exception in cleanup: " + ex.toString());
            ex.printStackTrace();
        }
    }

    public boolean rollbackXAJDBCNonXAJDBC()  throws RemoteException{
      boolean result = true;
        System.out.println("txRollback in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.rollbackXAJDBCNonXAJDBC("A1007", 8000);
            tx.commit();

            result = !beanB.verifyResults("A1007", "DB1", "XA");
            beanB.remove();
        } catch (Throwable ex) {
            System.out.println("Exception in txRollback: " + ex.toString());
            ex.printStackTrace();
            try{
                if(tx != null)
                tx.rollback();
            }catch(Throwable e) {
                e.printStackTrace();
            }
            result = true;
        }
        return result;
    }
    public boolean rollbackNonXAJDBCXAJDBC() throws RemoteException {
        boolean result = true;
        System.out.println("txRollback in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.rollbackXAJDBCNonXAJDBC("A1008", 8000);
            tx.commit();

            result = !beanB.verifyResults("A1008", "DB1", "NonXA");
            beanB.remove();
        } catch (Throwable ex) {
            System.out.println("Exception in txRollback: " + ex.toString());
            ex.printStackTrace();
            try{
                if(tx != null)
                tx.rollback();
            }catch(Throwable e) {
                e.printStackTrace();
            }
            result = true;
        }
        return result;
    }

    public boolean txCommit() throws RemoteException {
        boolean result = true;;
        /*System.out.println("txCommit in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }

            tx.begin();
            beanB.delete("A1000");
            beanB.insert("A1001", 3000);
            beanB.sendJMSMessage("JMS Message-1");
            beanB.insert("A1002", 5000);
            tx.commit();

            result = beanB.verifyResults("A1002", "DB1");
            result = result && beanB.verifyResults("A1002", "DB2");
            result = result && beanB.verifyResults("JMS Message-1", "JMS");

            beanB.remove();
        } catch (Exception ex) {
            System.out.println("Exception in txCommit: " + ex.toString());
            ex.printStackTrace();
        }       */
        return result;
    }

    public boolean txRollback() throws RemoteException {
        boolean result = true;
        /*System.out.println("txRollback in BeanA");
        try {
            TxRemoteB beanB = home.create();
            tx = context.getUserTransaction();

            if ( (beanB == null) || (tx == null) ) {
                throw new NullPointerException();
            }
     
            tx.begin();
            beanB.delete("A1001");
            beanB.insert("A1003", 8000);
            beanB.sendJMSMessage("JMS Message-2");
            tx.rollback();

            result = !beanB.verifyResults("A1003", "DB1");
            result = result && !beanB.verifyResults("A1003", "DB2");
            result = result && !beanB.verifyResults("JMS Message-2", "JMS");

            beanB.remove();
        } catch (Exception ex) {
            System.out.println("Exception in txCommit: " + ex.toString());
            ex.printStackTrace();
        }  */
        return result;
    }
}

