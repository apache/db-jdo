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
 * AttachLifecycleListener.java
 *
 */
 
package javax.jdo.listener;

import javax.jdo.PersistenceManager;

/**
 * This interface is implemented by listeners to be notified of
 * attach events.
 * @version 2.0
 * @since 2.0
 */
public interface AttachLifecycleListener
    extends InstanceLifecycleListener {
    
    /**
     * This method is called during the execution of
     * {@link PersistenceManager#attachCopy} before the copy is made.
     * It is called before the method {@link AttachCallback#jdoPreAttach}
     * is invoked on the instance to be attached.
     * @param event the attach event.
     * @since 2.0
	 */
    void preAttach (InstanceLifecycleEvent event);
    
    /**
     * This method is called during the execution of
     * {@link PersistenceManager#attachCopy} on the persistent
     * instance after the copy is made.
     * @param event the attach event.
     * @since 2.0
     */
    void postAttach (InstanceLifecycleEvent event);
}
