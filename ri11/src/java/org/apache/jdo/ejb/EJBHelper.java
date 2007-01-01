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
 * EJBHelper.java
 *
 * Created on December 15, 2000, 10:06 AM
 */

package org.apache.jdo.ejb;

import javax.transaction.Transaction;
import javax.transaction.UserTransaction;

import javax.jdo.PersistenceManagerFactory;

/** Provide a Forte for Java implementation with information about the distributed
 * transaction environment.  This is an interface that a helper class
 * implements that is specific to a managed environment.
 * <P><B>This interface is specific to Forte for Java, version 3.0,
 * and is subject to change without notice.  In particular, as additional
 * experience is gained with specific application servers, this interface
 * may have methods added and removed, even with patch releases.  
 * Therefore, this interface should be considered very volatile, and 
 * any class that implements it might have to be reimplemented whenever 
 * an upgrade to either the application server or Forte for Java occurs.</B></P>
 * The class that implements this interface must register itself
 * by a static method at class initialization time.  For example,
 * <blockquote><pre>
 * import org.apache.jdo.*;
 * class blackHerringEJBImplHelper implements EJBHelper {
 *    static EJBHelper.register(new blackHerringEJBImplHelper());
 *    ...
 * }
 * </pre></blockquote>
 *
 * @author Marina Vatkina
 */  
public interface EJBHelper {

    /** Returns the UserTransaction associated with the calling thread.  If there
     * is no transaction currently in progress, this method returns null.
     * @return the UserTransaction instance for the calling thread
     */  
    UserTransaction getUserTransaction();

    /** Identify the Transaction context for the calling thread, and return a
     * Transaction instance that can be used to register synchronizations,
     * and used as the key for HashMaps. The returned Transaction must implement
     * <code>equals()</code> and <code>hashCode()</code> based on the global transaction id.
     * <P>All Transaction instances returned by this method called in the same
     * Transaction context must compare equal and return the same hashCode.
     * The Transaction instance returned will be held as the key to an
     * internal HashMap until the Transaction completes. If there is no transaction 
     * associated with the current thread, this method returns null.
     * @return the Transaction instance for the calling thread
     */  
    Transaction getTransaction();

    /** Translate local representation of the Transaction Status to
     * javax.transaction.Status value if necessary. Otherwise this method
     * should return the value passed to it as an argument.
     * <P>This method is used during afterCompletion callbacks to translate
     * the parameter value passed by the application server to the 
     * afterCompletion method.  The return value must be one of:
     * <code>javax.transaction.Status.STATUS_COMMITTED</code> or
     * <code>javax.transaction.Status.STATUS_ROLLED_BACK</code>.
     * @param 	st 	local Status value
     * @return the javax.transaction.Status value of the status
     */
    int translateStatus(int st);

    /** Replace newly created instance of internal PersistenceManagerFactory
     * with the hashed one if it exists. The replacement is necessary only if 
     * the JNDI lookup always returns a new instance. Otherwise this method 
     * returns the object passed to it as an argument.
     *
     * PersistenceManagerFactory is uniquely identified by 
     * ConnectionFactory.hashCode() if ConnectionFactory is 
     * not null; otherwise by ConnectionFactoryName.hashCode() if 
     * ConnectionFactoryName is not null; otherwise 
     * by the combination of URL.hashCode() + userName.hashCode() + 
     * password.hashCode() + driverName.hashCode();
     *
     * @param 	pmf 	PersistenceManagerFactory instance to be replaced
     * @return 	the PersistenceManagerFactory known to the runtime
     */
    PersistenceManagerFactory replacePersistenceManagerFactory(
        PersistenceManagerFactory pmf) ;

    /** Called at the beginning of the Transaction.beforeCompletion() to
     * register the component with the app server if necessary.
     * The component argument is an array of Objects. 
     * The first element is javax.jdo.Transaction object responsible for 
     * transaction completion.
     * The second element is javax.jdo.PersistenceManager object that has 
     * been associated with the Transaction context for the calling thread.
     * The third element is javax.transaction.Transaction object that has been 
     * associated with the given instance of PersistenceManager.
     * The return value is passed unchanged to the postInvoke method.
     *
     * @param 	component 	an array of Objects
     * @return 	implementation-specific Object
     */
    Object enlistBeforeCompletion(Object component) ;

    /** Called at the end of the Transaction.beforeCompletion() to
     * de-register the component with the app server if necessary.
     * The parameter is the return value from preInvoke, and can be any
     * Object.
     *
     * @param 	im 	implementation-specific Object
     */
    void delistBeforeCompletion(Object im) ;

}

