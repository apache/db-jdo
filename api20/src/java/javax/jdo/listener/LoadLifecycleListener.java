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
 * LoadLifecycleListener.java
 *
 */

package javax.jdo.listener;

/**
 * This interface is implemented by listeners to be notified of
 * load events.
 * @version 2.0
 * @since 2.0
 */
public interface LoadLifecycleListener
    extends InstanceLifecycleListener {

    /**
     * Invoked whenever a persistent instance is loaded from the data
     * store. It is called after the method {@link LoadCallback#jdoPostLoad}
     * is invoked on the persistent instance.
     * @param event the load event.
     * @since 2.0
     */
    void postLoad (InstanceLifecycleEvent event);
}
