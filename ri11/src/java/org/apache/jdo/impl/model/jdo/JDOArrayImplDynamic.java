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

package org.apache.jdo.impl.model.jdo;

import org.apache.jdo.impl.model.jdo.util.TypeSupport;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOArray;
import org.apache.jdo.model.jdo.JDOField;

/**
 * An instance of this class represents the JDO relationship metadata 
 * of a array relationship field. This dynamic implementation only
 * stores property values explicitly set by setter method. 
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOArrayImplDynamic extends JDORelationshipImpl 
    implements JDOArray {
    
    /** Property embeddedElement. */
    protected Boolean embeddedElement;

    /**
     * Determines whether the values of the elements should be stored 
     * if possible as part of the instance instead of as their own instances 
     * in the datastore.
     * @return <code>true</code> if the elements should be stored as part of 
     * the instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedElement() {
        if (embeddedElement != null) {
            // return embeddedElement, if explicitly set by the setter
            return embeddedElement.booleanValue();
        }
        
        // not set => calculate
        JavaType elementType = getElementType();
        return (elementType != null) ? 
            TypeSupport.isEmbeddedElementType(elementType) : false;
    }
    
    /**
     * Set whether the values of the elements should be stored 
     * if possible as part of the instance instead of as their own instances 
     * in the datastore.
     * @param embeddedElement flag indicating whether the elements should be 
     * stored as part of the instance
     */
    public void setEmbeddedElement(boolean embeddedElement) {
        this.embeddedElement = (embeddedElement ? Boolean.TRUE : Boolean.FALSE);
    }

    /** 
     * Get the type representation of the array component type. 
     * @return the array component type
     */
    public JavaType getElementType() {
        JDOField jdoField = getDeclaringField();
        JavaType fieldType = jdoField.getType();
        return (fieldType != null) ? fieldType.getArrayComponentType() : null;
    }

}

