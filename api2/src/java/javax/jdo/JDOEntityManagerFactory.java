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

package javax.jdo;

import javax.persistence.EntityManagerFactory;

/*
 * JDOEntityManagerFactory.java
 *
 * @since 2.1
 *
 */
public interface JDOEntityManagerFactory 
    extends EntityManagerFactory, PersistenceManagerFactory {

    /** Get an instance of <code>JDOEntityManager</code> from this factory.  
     * The instance has default values for options. This method overrides
     * the getPersistenceManager method from PersistenceManagerFactory.
     *
     * <P>After the first use of <code>getPersistenceManager</code>, no "set" 
     * methods will succeed.
     *
     * @return a <code>JDOEntityManager</code> instance with default options.
     */
    JDOEntityManager getPersistenceManager();

    /** Get a thread-safe instance of a proxy that dynamically binds 
     * on each method call to an instance of <code>JDOEntityManager</code>.
     * <P>When used with a <code>JDOEntityManagerFactory</code>
     * that uses TransactionType JTA,
     * the proxy can be used in a server to dynamically bind to an instance 
     * from this factory associated with the thread's current transaction.
     * In this case, the close method is ignored, as the 
     * <code>PersistenceManager</code> is automatically closed when the
     * transaction completes.
     * <P>When used with a <code>JDOEntityManagerFactory</code>
     * that uses TransactionType RESOURCE_LOCAL, the proxy uses an inheritable
     * ThreadLocal to bind to an instance of <code>JDOEntityManager</code>
     * associated with the thread. In this case, the close method executed
     * on the proxy closes the <code>JDOEntityManager</code> and then
     * clears the ThreadLocal.
     * Use of this method does not affect the configurability of the
     * <code>JDOEntityManagerFactory</code>.
     *
     * @since 2.1
     * @return a <code>PersistenceManager</code> proxy.
     */
    JDOEntityManager getPersistenceManagerProxy();

    /** Get an instance of <code>JDOEntityManager</code> from this factory.  
     * The instance has default values for options.  
     * The parameters <code>userid</code> and <code>password</code> are used 
     * when obtaining datastore connections from the connection pool.
     *
     * <P>After the first use of <code>getPersistenceManager</code>, no "set" 
     * methods will succeed.
     *
     * @return a <code>JDOEntityManager</code> instance with default options.
     * @param userid the userid for the connection
     * @param password the password for the connection
     */
    JDOEntityManager getPersistenceManager(String userid, String password);

}
