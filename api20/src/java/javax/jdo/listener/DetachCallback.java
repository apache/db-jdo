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
 * DetachCallback.java
 *
 */
 
package javax.jdo.listener;

import javax.jdo.PersistenceManager;

/**
 * This interface is used to notify instances of detach events.
 * @version 2.0
 * @since 2.0
 */
public interface DetachCallback {
    
    /**
     * This method is called during the execution of
     * {@link PersistenceManager#detachCopy} on the
     * persistent instance before the copy is made.
     * @since 2.0
     */
    public void jdoPreDetach();

    /**
     * This method is called during the execution of
     * {@link PersistenceManager#detachCopy} on the
     * detached instance after the copy is made.
     * @param detached The corresponding (attached) persistent instance.
     * @since 2.0
     */
    public void jdoPostDetach(Object detached);
}
