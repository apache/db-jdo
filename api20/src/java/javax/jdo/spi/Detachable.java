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
 * Detachable.java
 *
 */

package javax.jdo.spi;

/**
 * This interface is implemented by classes that can be detached from the
 * persistence context and later attached. The interface includes the 
 * contract by which the StateManager can set the object id and version
 * so they are preserved while outside the persistence environment.
 * @version 2.0
 * @since 2.0
 */
public interface Detachable {
    
    /** Replace the object id in the detached object.
     */
    void jdoReplaceObjectId();
    
    /** Replace the version in the detached object.
     */
    void jdoReplaceVersion();
    
    /** Provide the loaded field list in the detached object.
     */
    void jdoProvideLoadedFieldList();
    
    /** Replace the loaded field list in the detached object.
     */
    void jdoReplaceLoadedFieldList();
    
    /** Provide the modified field list in the detached object.
     */
    void jdoProvideModifiedFieldList();
    
    /** Replace the modified field list in the detached object.
     */
    void jdoReplaceModifiedFieldList();
}
