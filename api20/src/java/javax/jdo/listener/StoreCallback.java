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
 * StoreCallback.java
 *
 */
 
package javax.jdo.listener;

/**
 * This interface is used to notify instances of store events.
 * @version 2.0
 * @since 2.0
 */
public interface StoreCallback {
    
    /**
     * Called before the values are stored from this instance to the
     * data store.
     *
     * <P>Data store fields that might have been affected by modified
     * non-persistent fields should be updated in this method.
     *
     * <P>This method is modified by the enhancer so that changes to 
     * persistent fields will be reflected in the data store. 
     * The context in which this call is made allows access to the 
     * <code>PersistenceManager</code> and other persistent JDO instances.
     */
    void jdoPreStore();
}
