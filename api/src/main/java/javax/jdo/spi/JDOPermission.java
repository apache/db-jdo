/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package javax.jdo.spi;

/**
 * The <code>JDOPermission</code> class is for operations that are reserved for 
 * JDO implementations and should not be called by other code.  A
 * <code>JDOPermission</code> is a <em>named permission</em> and has no
 * actions.  There are two names currently defined.  Each named permission
 * has a corresponding public static final field which contains an instance
 * of the named permission.
 * <P>
 * The following table
 * provides a summary description of what each named permission allows,
 * and discusses the risks of granting code the permission.
 *
 * <table border=1 cellpadding=5 summary="">
 * <tr>
 * <th>Permission Target Name</th>
 * <th>What the Permission Allows</th>
 * <th>Risks of Allowing this Permission</th>
 * </tr>
 * <tr>
 *   <td><code>setStateManager</code></td>
 *   <td>This allows setting the <code>StateManager</code> for an instance of 
 *   <code>PersistenceCapable</code>. The <code>StateManager</code>
 *   has unlimited access to get and set persistent and transactional fields of
 *   the <code>PersistenceCapable</code> instance.</td>
 *   <td>This is dangerous in that information (possibly confidential) 
 *   normally unavailable would be accessible to malicious code.</td>
 * </tr>
 * <tr>
 *   <td><code>getMetadata</code></td>
 *   <td>This allows getting metadata for any <code>PersistenceCapable</code> 
 *   class that has registered with <code>JDOImplHelper</code>.</td>
 *   <td>This is dangerous in that metadata information (possibly confidential) 
 *   normally unavailable would be accessible to malicious code.</td>
 * </tr>
 * <tr>
 *   <td><code>manageMetadata</code></td>
 *   <td>This allows managing metadata for any <code>PersistenceCapable</code> 
 *   class that has registered with <code>JDOImplHelper</code>.</td>
 *   <td>This is dangerous in that metadata information (possibly confidential) 
 *   normally unavailable would be manageable (modifiable) by malicious code.
 *   </td>
 * </tr>
 * <tr>
 *   <td><code>closePersistenceManagerFactory</code></td>
 *   <td>This allows closing a <code>PersistenceManagerFactory</code>,
 *       thereby releasing resources.</td> 
 *   <td>This is dangerous in that resources bound to the
 *       <code>PersistenceManagerFactory</code> would be releaseable by
 *       malicious code.</td>  
 * </tr>
 * </table>
 *
 * @see java.security.Permission
 * @see java.security.BasicPermission
 * @see javax.jdo.spi.JDOImplHelper
 * @see javax.jdo.spi.PersistenceCapable
 * @version 1.0.2
 */
public final class JDOPermission extends java.security.BasicPermission {

	private static final long serialVersionUID = 3087132628681636890L;

	/**
     * Constructs a <code>JDOPermission</code> with the specified name.
     *
     * @param name the name of the <code>JDOPermission</code>
     */
    public JDOPermission(String name) {
        super(name);
    }

    /**
     * Constructs a <code>JDOPermission</code> with the specified name and 
     * actions.  The actions should be <code>null</code>; they are ignored. 
     * This constructor exists for use by the <code>Policy</code> object
     * to instantiate new <code>Permission</code> objects.
     *
     * @param name the name of the <code>JDOPermission</code>
     * @param actions should be <code>null</code>.
     */
    public JDOPermission(String name, String actions) {
        super(name, actions);
    }

    /** An instance of <code>JDOPermission</code> to be used for
     * <code>getMetadata</code> permission checking.
     */
    public final static JDOPermission GET_METADATA = 
        new JDOPermission("getMetadata"); // NOI18N
    
    /** An instance of <code>JDOPermission</code> to be used for
     * <code>manageMetadata</code> permission checking.
     * @since 1.0.2
     */
    public final static JDOPermission MANAGE_METADATA = 
        new JDOPermission("manageMetadata"); // NOI18N
    
    /** An instance of <code>JDOPermission</code> to be used for
     * <code>setStateManager</code> permission checking.
     */
    public final static JDOPermission SET_STATE_MANAGER = 
        new JDOPermission("setStateManager"); // NOI18N
    
    /** An instance of <code>JDOPermission</code> to be used for
     * <code>closePersistenceManagerFactory</code> permission checking.
     * @since 1.0.1
     */
    public final static JDOPermission CLOSE_PERSISTENCE_MANAGER_FACTORY = 
        new JDOPermission("closePersistenceManagerFactory"); // NOI18N
    
}
