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

package org.apache.jdo.impl.model.jdo.caching;

import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.impl.model.jdo.JDOMapImplDynamic;

/**
 * An instance of this class represents the JDO relationship metadata 
 * (the treatment of keys and values) of a map relationship field.  
 * This caching implementation caches any calulated value to avoid
 * re-calculating it if it is requested again. 
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOMapImplCaching extends JDOMapImplDynamic {
    
    /**
     * Determines whether the keys of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the keys are stored as part of this instance;
     * <code>false</code> otherwise
     */
    public boolean isEmbeddedKey() {
        if (embeddedKey == null) {
            embeddedKey =
                super.isEmbeddedKey() ? Boolean.TRUE : Boolean.FALSE;
        }
        return embeddedKey.booleanValue();
    }
    
    /**
     * Get the type representation of the keys for this JDOMap.
     * @return the type of the keys of this JDOMap  
     */
    public JavaType getKeyType() {
        if (keyType == null) {
            keyType = super.getKeyType();
        }
        return keyType;
    }

    /**
     * Determines whether the values of the map should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @return <code>true</code> if the values are stored as part of this 
     * instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedValue() {
        if (embeddedValue == null) {
            embeddedValue = 
                super.isEmbeddedValue() ? Boolean.TRUE : Boolean.FALSE;
        }
        return embeddedValue.booleanValue();
    }
    
    /**
     * Get the type representation of the values for this JDOMap.
     * @return the type of the values of this JDOMap  
     */
    public JavaType getValueType() {
        if (valueType == null) {
            valueType = super.getValueType();
        }
        return valueType;
    }

}
