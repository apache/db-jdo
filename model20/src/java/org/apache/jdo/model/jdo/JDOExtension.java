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

import org.apache.jdo.model.ModelException;

/**
 * A JDOExtension instance represents a JDO vendor specific extension.
 * 
 * @author Michael Bouschen
 */
public interface JDOExtension 
{
    /**
     * Returns the vendor name of this vendor extension.
     */
    public String getVendorName();

    /**
     * Sets the vendor name for this vendor extension.
     * @exception ModelException if impossible
     */
    public void setVendorName(String vendorName)
        throws ModelException;
    
    /**
     * Returns the key of this vendor extension.
     */
    public String getKey();

    /**
     * Sets the key for this vendor extension.
     * @exception ModelException if impossible
     */
    public void setKey(String key)
        throws ModelException;
    
    /**
     * Returns the value of this vendor extension.
     */
    public Object getValue();

    /**
     * Sets the value for this vendor extension.
     * @exception ModelException if impossible
     */
    public void setValue(Object value)
        throws ModelException;
}
