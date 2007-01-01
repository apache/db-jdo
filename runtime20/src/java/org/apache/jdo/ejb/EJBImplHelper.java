/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * EJBImplHelper.java
 *
 * Created on December 15, 2000, 10:15 AM
 */
package org.apache.jdo.ejb;

import javax.transaction.*;

import javax.jdo.PersistenceManagerFactory;

/** Provides helper methods for a Forte for Java implementation with the
 * application server specific information in the distributed transaction
 * environment. Calls corresponding methods on the registered class which
 * implements EJBHelper interface.
 * 
 * @author Marina Vatkina
 */
public class EJBImplHelper {
    
   /** Reference to a class that implements EJBHelper interface for this
    * particular application server
    */
    static EJBHelper myHelper;
 
   /** Register class that implements EJBHelper interface
    * Should be called by a static method at class initialization time.
    *
    * @param h application server specific implemetation of the
    * EJBHelper interface.
    */
    public static void registerEJBHelper (EJBHelper h) {
        myHelper = h;
    }

   /** Returns Transaction instance that can be used to register
    * synchronizations. In a non-managed environment or if there is no
    * transaction associated with the current thread, this method
    * returns null.
    *
    * @see EJBHelper#getTransaction()
    * @return the Transaction instance for the calling thread
    */
    public static Transaction getTransaction() {
        return myHelper == null ? null : myHelper.getTransaction();
    }

   /** Returns the UserTransaction associated with the calling thread.
    * In a non-managed environment or if there is no transaction
    * currently in progress, this method returns null.
    *
    * @see EJBHelper#getUserTransaction()
    * @return the UserTransaction instance for the calling thread
    */
    public static UserTransaction getUserTransaction() {
        return myHelper == null ? null : myHelper.getUserTransaction();
    }

    /** Identifies the managed environment behavior.
     * @return true if there is a helper class registered. 
     */
    public static boolean isManaged() {
        return myHelper != null;
    }

   /** Translates local representation of the Transaction Status to 
    * javax.transaction.Status value. In a non-managed environment
    * returns the value passed to it as an argument.
    *
    * @see EJBHelper#translateStatus(int st)
    * @param 	st 	Status value
    * @return 	the javax.transaction.Status value of the status 
    */ 
    public static int translateStatus(int st) {
        return myHelper == null ? st : myHelper.translateStatus(st);
    }

   /** Returns the hashed instance of internal PersistenceManagerFactory 
    * that compares equal to the newly created instance or the instance 
    * itself if it is not found. In a non-managed environment returns
    * the value passed to it as an argument.
    *
    * @see EJBHelper#replacePersistenceManagerFactory(
    * 	PersistenceManagerFactory pmf)
    * @param 	pmf 	PersistenceManagerFactory instance to be replaced
    * @return 	the PersistenceManagerFactory known to the runtime
    */
    public static PersistenceManagerFactory replacePersistenceManagerFactory(
        PersistenceManagerFactory pmf) {
        return myHelper == null ? pmf : 
            myHelper.replacePersistenceManagerFactory(pmf);
    }

   /** Called at the beginning of the Transaction.beforeCompletion()
    * to register the component with the app server if necessary. In a
    * non-managed environment or if the delistBeforeCompletion method
    * does not use the value, this method returns null. 
    *
    * @see EJBHelper#enlistBeforeCompletion(Object component)
    * @param 	component 	an array of Objects
    * @return implementation-specific Object
    */
    public static Object enlistBeforeCompletion(Object component) {
        return myHelper == null ? null : 
            myHelper.enlistBeforeCompletion(component);
    }

   /** Called a non-managed environment at the end of the
    * Transaction.beforeCompletion() to de-register the component with
    * the app server if necessary.
    *
    * @see EJBHelper#delistBeforeCompletion(Object im)
    * @param im implementation-specific Object
    */
    public static void delistBeforeCompletion(Object im) {
        if (myHelper != null) {
            myHelper.delistBeforeCompletion(im);
        }
    }

}

