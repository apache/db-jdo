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
 * LoadCallback.java
 *
 */
 
package javax.jdo.listener;

/**
 * This interface is used to notify instances of load events.
 * @version 2.0
 * @since 2.0
 */
public interface LoadCallback {
    
    /**
     * Called after the values are loaded from the data store into
     * this instance.
     *
     * <P>This method is not modified by the enhancer.
     * <P>Derived fields should be initialized in this method.
     * The context in which this call is made does not allow access to 
     * other persistent JDO instances.
     */
    void jdoPostLoad();
}
