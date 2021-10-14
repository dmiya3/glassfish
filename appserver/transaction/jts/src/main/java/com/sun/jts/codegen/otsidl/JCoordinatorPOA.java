/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.jts.codegen.otsidl;


/**
* com/sun/jts/codegen/otsidl/JCoordinatorPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from com/sun/jts/ots.idl
* Tuesday, February 5, 2002 12:57:23 PM PST
*/


//#-----------------------------------------------------------------------------
public abstract class JCoordinatorPOA extends org.omg.PortableServer.Servant
 implements com.sun.jts.codegen.otsidl.JCoordinatorOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("getGlobalTID", 0);
    _methods.put ("getLocalTID", 1);
    _methods.put ("getAncestors", 2);
    _methods.put ("isRollbackOnly", 3);
    _methods.put ("get_status", 4);
    _methods.put ("get_parent_status", 5);
    _methods.put ("get_top_level_status", 6);
    _methods.put ("is_same_transaction", 7);
    _methods.put ("is_related_transaction", 8);
    _methods.put ("is_ancestor_transaction", 9);
    _methods.put ("is_descendant_transaction", 10);
    _methods.put ("is_top_level_transaction", 11);
    _methods.put ("hash_transaction", 12);
    _methods.put ("hash_top_level_tran", 13);
    _methods.put ("register_resource", 14);
    _methods.put ("register_synchronization", 15);
    _methods.put ("register_subtran_aware", 16);
    _methods.put ("rollback_only", 17);
    _methods.put ("get_transaction_name", 18);
    _methods.put ("create_subtransaction", 19);
    _methods.put ("get_txcontext", 20);
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // otsidl/JCoordinator/getGlobalTID
       {
         org.omg.CosTransactions.otid_t $result = null;
         $result = this.getGlobalTID ();
         out = $rh.createReply();
         org.omg.CosTransactions.otid_tHelper.write (out, $result);
         break;
       }


  // Returns the global identifier that represents the Coordinator's transaction.
       case 1:  // otsidl/JCoordinator/getLocalTID
       {
         long $result = (long)0;
         $result = this.getLocalTID ();
         out = $rh.createReply();
         out.write_longlong ($result);
         break;
       }


  // Returns the local identifier that represents the Coordinator's transaction.
       case 2:  // otsidl/JCoordinator/getAncestors
       {
         org.omg.CosTransactions.TransIdentity $result[] = null;
         $result = this.getAncestors ();
         out = $rh.createReply();
         com.sun.jts.codegen.otsidl.TransAncestryHelper.write (out, $result);
         break;
       }


  // freeing the sequence storage.
       case 3:  // otsidl/JCoordinator/isRollbackOnly
       {
         boolean $result = false;
         $result = this.isRollbackOnly ();
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 4:  // CosTransactions/Coordinator/get_status
       {
         org.omg.CosTransactions.Status $result = null;
         $result = this.get_status ();
         out = $rh.createReply();
         org.omg.CosTransactions.StatusHelper.write (out, $result);
         break;
       }

       case 5:  // CosTransactions/Coordinator/get_parent_status
       {
         org.omg.CosTransactions.Status $result = null;
         $result = this.get_parent_status ();
         out = $rh.createReply();
         org.omg.CosTransactions.StatusHelper.write (out, $result);
         break;
       }

       case 6:  // CosTransactions/Coordinator/get_top_level_status
       {
         org.omg.CosTransactions.Status $result = null;
         $result = this.get_top_level_status ();
         out = $rh.createReply();
         org.omg.CosTransactions.StatusHelper.write (out, $result);
         break;
       }

       case 7:  // CosTransactions/Coordinator/is_same_transaction
       {
         org.omg.CosTransactions.Coordinator tc = org.omg.CosTransactions.CoordinatorHelper.read (in);
         boolean $result = false;
         $result = this.is_same_transaction (tc);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 8:  // CosTransactions/Coordinator/is_related_transaction
       {
         org.omg.CosTransactions.Coordinator tc = org.omg.CosTransactions.CoordinatorHelper.read (in);
         boolean $result = false;
         $result = this.is_related_transaction (tc);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 9:  // CosTransactions/Coordinator/is_ancestor_transaction
       {
         org.omg.CosTransactions.Coordinator tc = org.omg.CosTransactions.CoordinatorHelper.read (in);
         boolean $result = false;
         $result = this.is_ancestor_transaction (tc);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 10:  // CosTransactions/Coordinator/is_descendant_transaction
       {
         org.omg.CosTransactions.Coordinator tc = org.omg.CosTransactions.CoordinatorHelper.read (in);
         boolean $result = false;
         $result = this.is_descendant_transaction (tc);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 11:  // CosTransactions/Coordinator/is_top_level_transaction
       {
         boolean $result = false;
         $result = this.is_top_level_transaction ();
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 12:  // CosTransactions/Coordinator/hash_transaction
       {
         int $result = (int)0;
         $result = this.hash_transaction ();
         out = $rh.createReply();
         out.write_ulong ($result);
         break;
       }

       case 13:  // CosTransactions/Coordinator/hash_top_level_tran
       {
         int $result = (int)0;
         $result = this.hash_top_level_tran ();
         out = $rh.createReply();
         out.write_ulong ($result);
         break;
       }

       case 14:  // CosTransactions/Coordinator/register_resource
       {
         try {
           org.omg.CosTransactions.Resource r = org.omg.CosTransactions.ResourceHelper.read (in);
           org.omg.CosTransactions.RecoveryCoordinator $result = null;
           $result = this.register_resource (r);
           out = $rh.createReply();
           org.omg.CosTransactions.RecoveryCoordinatorHelper.write (out, $result);
         } catch (org.omg.CosTransactions.Inactive $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.InactiveHelper.write (out, $ex);
         }
         break;
       }

       case 15:  // CosTransactions/Coordinator/register_synchronization
       {
         try {
           org.omg.CosTransactions.Synchronization sync = org.omg.CosTransactions.SynchronizationHelper.read (in);
           this.register_synchronization (sync);
           out = $rh.createReply();
         } catch (org.omg.CosTransactions.Inactive $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.InactiveHelper.write (out, $ex);
         } catch (org.omg.CosTransactions.SynchronizationUnavailable $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.SynchronizationUnavailableHelper.write (out, $ex);
         }
         break;
       }

       case 16:  // CosTransactions/Coordinator/register_subtran_aware
       {
         try {
           org.omg.CosTransactions.SubtransactionAwareResource r = org.omg.CosTransactions.SubtransactionAwareResourceHelper.read (in);
           this.register_subtran_aware (r);
           out = $rh.createReply();
         } catch (org.omg.CosTransactions.Inactive $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.InactiveHelper.write (out, $ex);
         } catch (org.omg.CosTransactions.NotSubtransaction $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.NotSubtransactionHelper.write (out, $ex);
         }
         break;
       }

       case 17:  // CosTransactions/Coordinator/rollback_only
       {
         try {
           this.rollback_only ();
           out = $rh.createReply();
         } catch (org.omg.CosTransactions.Inactive $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.InactiveHelper.write (out, $ex);
         }
         break;
       }

       case 18:  // CosTransactions/Coordinator/get_transaction_name
       {
         String $result = null;
         $result = this.get_transaction_name ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 19:  // CosTransactions/Coordinator/create_subtransaction
       {
         try {
           org.omg.CosTransactions.Control $result = null;
           $result = this.create_subtransaction ();
           out = $rh.createReply();
           org.omg.CosTransactions.ControlHelper.write (out, $result);
         } catch (org.omg.CosTransactions.SubtransactionsUnavailable $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.SubtransactionsUnavailableHelper.write (out, $ex);
         } catch (org.omg.CosTransactions.Inactive $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.InactiveHelper.write (out, $ex);
         }
         break;
       }

       case 20:  // CosTransactions/Coordinator/get_txcontext
       {
         try {
           org.omg.CosTransactions.PropagationContext $result = null;
           $result = this.get_txcontext ();
           out = $rh.createReply();
           org.omg.CosTransactions.PropagationContextHelper.write (out, $result);
         } catch (org.omg.CosTransactions.Unavailable $ex) {
           out = $rh.createExceptionReply ();
           org.omg.CosTransactions.UnavailableHelper.write (out, $ex);
         }
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:otsidl/JCoordinator:1.0",
    "IDL:omg.org/CosTransactions/Coordinator:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public JCoordinator _this()
  {
    return JCoordinatorHelper.narrow(
    super._this_object());
  }

  public JCoordinator _this(org.omg.CORBA.ORB orb)
  {
    return JCoordinatorHelper.narrow(
    super._this_object(orb));
  }


} // class JCoordinatorPOA