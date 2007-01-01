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

package org.apache.jdo.jdoql.tree;

/**
 * A ValueTable manages values for declared identifiers (such as variables 
 * or parameters) of a query. It provides methods to declare an identifier, 
 * to set and get its value and check a value to be compaitible with the 
 * identifier's type.
 *
 * @author Michael Bouschen
 */
public interface ValueTable
{
    /**
     * This method declares an identifier. The methods takes the name of the 
     * declared identifier from the specified declaration node.
     * @param decl the declaration node
     */
    public void declare(Declaration decl);

    /**
     * Sets the value of the specified identifier for later retrieval using 
     * method {@link #getValue}). The identifier must be declared (see method 
     * {@link #declare}) prior to a call of this method.
     * @param name the name of the identifier
     * @param value the new value of the identifier
     */
    public void setValue(String name, Object value);

    
    /**
     * Returns the current value of the specified identifier. The identifier 
     * must be declared using method {@link #declare}) and its value must be 
     * set using method {@link #getValue}) prior to a call of this method.
     * @param name the name of the identifier
     * @return the current value of the identifier
     */
    public Object getValue(String name);

    /**
     * Checks whether the type of the specified value is compatible with the 
     * type of the identifier.
     * @return <code>true</code> if the type of the value is compatible with 
     * the type of the identifier; <code>false</code> otherwise
     */
    public boolean isCompatibleValue(String name, Object value);
}

