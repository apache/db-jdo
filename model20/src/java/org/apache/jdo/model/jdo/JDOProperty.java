/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.model.jdo;

/**
 * A JDOProperty instance represents the JDO metadata of a property of a
 * persistence-capable class. 
 *
 * @author Michael Bouschen
 */
public interface JDOProperty
    extends JDOField
{
    /** 
     * Return the JDOField instance associated with this property, if
     * available. If there is no JDOField instance associated, then the method
     * returns <code>null</code>.
     * @return associated JDOField instance or <code>null</code> if not
     * available.
     */
    public JDOField getAssociatedJDOField();
}
