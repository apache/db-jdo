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

/*
 * ClearCallback.java
 *
 */
 
package javax.jdo.listener;


/**
 * This interface is used to notify instances of clear events.
 * @version 2.0
 * @since 2.0
 */
public interface ClearCallback {
    
    /**
     * Called before the values in the instance are cleared.
     *
     * <P>Transient fields should be cleared in this method.  
     * Associations between this
     * instance and others in the runtime environment should be cleared.
     *
     * <P>This method is not modified by the enhancer.
     */
    void jdoPreClear();
}
