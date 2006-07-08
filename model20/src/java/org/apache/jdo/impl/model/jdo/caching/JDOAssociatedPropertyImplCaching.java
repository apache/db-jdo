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

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaProperty;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.impl.model.jdo.JDOAssociatedPropertyImplDynamic;

/**
 * An instance of this class represents the JDO metadata of a managed property
 * of a persistence capable class. This JDOProperty implementation is used for
 * persistent properties with an associated JDOField. All JDOField getter
 * methods delegate to the associated JDOField, except methods getName,
 * getDeclaringClass and getJavaField. All JDOField setter method throw a
 * ModelException to avoid changing the associated JDOField through this
 * JDOProperty instance. This caching implementation caches any calculated
 * value to avoid re-calculating it if it is requested again. 
 *
 * @author Michael Bouschen
 * @since 2.0
 * @version 2.0
 */
public class JDOAssociatedPropertyImplCaching
    extends JDOAssociatedPropertyImplDynamic
{
    /** Constructor. */
    protected JDOAssociatedPropertyImplCaching(
        String name, JDOClass declaringClass, JDOField associatedJDOField)
        throws ModelException {
        super(name, declaringClass, associatedJDOField);
    }
    
    // ===== Methods specified in JDOField =====

    /**
     * Get the corresponding JavaProperty representation for this JDOProperty.
     * @return the corresponding JavaProperty representation
     */
    public JavaField getJavaField() {
        if (javaProperty == null) {
            javaProperty = (JavaProperty)super.getJavaField();
        }
        return javaProperty;
    }
}
