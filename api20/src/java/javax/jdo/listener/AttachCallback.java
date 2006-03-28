/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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
 * AttachCallback.java
 *
 */
 
package javax.jdo.listener;

/**
 * This interface is used to notify instances of attach events.
 * @version 2.0
 * @since 2.0
 */
public interface AttachCallback {
    
    /**
     * This method is called during the execution of
     * {@link javax.jdo.PersistenceManager#makePersistent} on the detached 
     * instance before the copy is made.
     * @since 2.0
     */
    public void jdoPreAttach();

    /**
     * This method is called during the execution of
     * {@link javax.jdo.PersistenceManager#makePersistent} on the persistent
     * instance after the copy is made.
     * @param attached	The corresponding (non-attached) instance that was
     * attached in the call to 
     * {@link javax.jdo.PersistenceManager#makePersistent}.
     * @since 2.0
     */
    public void jdoPostAttach(Object attached);
}
