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
 * ClearLifecycleListener.java
 *
 */

package javax.jdo.listener;

/**
 * This interface is implemented by listeners to be notified of
 * clear events.
 * @version 2.0
 * @since 2.0
 */
public interface ClearLifecycleListener
    extends InstanceLifecycleListener {

    /**
     * This method is called before the implementation calls the instance
     *  method {@link ClearCallback#jdoPreClear} and before it clears the values 
     *  in the instance to their Java default values. This happens during 
     *  an application call to evict, and in afterCompletion for commit 
     *  with RetainValues false and rollback with RestoreValues false. 
     *  <P>The method is called during any state transition to hollow.
     *  Non-persistent, non-transactional fields should be cleared 
     *  in this method. Associations between this instance and others 
     *  in the runtime environment should be cleared. 
     *  <P>This method is not modified by the enhancer, so access to fields 
     *  is not mediated.
     * @param event the clear event.
     * @since 2.0
     */
    void preClear (InstanceLifecycleEvent event);
    
    /**
     * This method is called after the {@link ClearCallback#jdoPreClear} 
     * method is invoked on the instance and the fields have been cleared 
     * by the JDO implementation.
     * @param event the clear event.
     * @since 2.0
     */
    void postClear (InstanceLifecycleEvent event);
}