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
 * JDOConnection.java
 *
 */
 
package javax.jdo.datastore;

/**
 * The underlying connection that is being used by a 
 * {@link javax.jdo.PersistenceManager}. 
 * @version 2.0
 * @since 2.0
 */
public interface JDOConnection {

    /**
     * Returns the native, datastore-specific connection that this
     * connection wraps. In general, it is not recommended that this
     * native connection be used directly, since the JDO
     * implementation has no way to intercept calls to it, so it is
     * quite possible to put the <code>PersistenceManager</code>'s
     * connection into an invalid state.
     * @return the native connection
     * @since 2.0
     */
    Object getNativeConnection ();
    
    /**
     * Returns this connection to the JDO implementation.
     * The object must be returned to the JDO implementation prior to
     * calling any JDO method or performing any action on any persistent
     * instance that might require the JDO implementation to use a
     * connection. If the object has not been returned and the JDO
     * implementation needs a connection, a <code>JDOUserException</code>
     * is thrown.
     * @since 2.0
     */
    void close();
}
