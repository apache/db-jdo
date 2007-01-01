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
import org.apache.jdo.model.java.JavaType;


/**
 * A JDOMap instance represents the JDO relationship metadata 
 * (the treatment of keys and values) of a map relationship field. 
 *
 * @author Michael Bouschen
 */
public interface JDOMap
    extends JDORelationship 
{
    /**
     * Determines whether the keys of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the keys are stored as part of this instance;
     * <code>false</code> otherwise
     */
    public boolean isEmbeddedKey();
    
    /**
     * Set whether the keys of the map should be stored if possible as part 
     * of the instance instead of as their own instances in the datastore.
     * @param embeddedKey <code>true</code> if the keys are stored as part of
     * this instance; <code>false</code> otherwise
     * @exception ModelException if impossible
     */
    public void setEmbeddedKey(boolean embeddedKey)
        throws ModelException;

    /**
     * Get the type representation of the keys for this JDOMap.
     * @return the type of the keys of this JDOMap  
     */
    public JavaType getKeyType();

    /**
     * Set the type representation of the keys for this JDOMap.
     * @param keyType the type representation of the keys
     * @exception ModelException if impossible
     */
    public void setKeyType(JavaType keyType)
        throws ModelException;

    /**
     * Get the string representation of the type of the keys for this JDOMap.
     * @return the key type as string
     */
    public String getKeyTypeName();

    /**
     * Set string representation of the type of the keys for this JDOMap.
     * @param keyTypeName the name of the key type
     * @exception ModelException if impossible
     */
    public void setKeyTypeName(String keyTypeName)
        throws ModelException;

    /**
     * Determines whether the values of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the values are stored as part of this 
     * instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedValue();
    
    /**
     * Set whether the values of the map should be stored if possible as part 
     * of the instance instead of as their own instances in the datastore.
     * @param embeddedValue <code>true</code> if the values are stored as part 
     * of this instance; <code>false</code> otherwise
     * @exception ModelException if impossible
     */
    public void setEmbeddedValue(boolean embeddedValue)
        throws ModelException;

    /**
     * Get the type representation of the values for this JDOMap.
     * @return the type of the values of this JDOMap  
     */
    public JavaType getValueType();

    /**
     * Set the type representation of the values for this JDOMap.
     * @param valueType the type representation of the values
     * @exception ModelException if impossible
     */
    public void setValueType(JavaType valueType)
        throws ModelException;

    /**
     * Get the string representation of the type of the values for this JDOMap.
     * @return the key value as string
     */
    public String getValueTypeName();

    /**
     * Set string representation of the type of the values for this JDOMap.
     * @param valueTypeName the name of the value type
     * @exception ModelException if impossible
     */
    public void setValueTypeName(String valueTypeName)
        throws ModelException;

}
