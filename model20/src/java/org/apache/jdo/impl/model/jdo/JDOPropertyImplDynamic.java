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

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaProperty;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOProperty;

/**
 * An instance of this class represents the JDO metadata of a managed property
 * of a persistence capable class. This dynamic implementation only
 * stores values explicitly set by setter method. 
 *
 * @author Michael Bouschen
 * @since 2.0
 * @version 2.0
 */
public class JDOPropertyImplDynamic
    extends JDOFieldImplDynamic
    implements JDOProperty
{
    /** Constructor. */
    protected JDOPropertyImplDynamic(String name, JDOClass declaringClass) {
        super(name, declaringClass);
    }

    // ===== methods specified in JDOField =====

    /**
     * Get the corresponding JavaField representation for this JDOProperty.
     * @return the corresponding JavaProperty representation
     */
    public JavaField getJavaField() {
        if (javaField != null) {
            // return java field, if explicitly set by the setter
            return javaField;
        }
        
        // not set => calculate
        JavaType javaType = getDeclaringClass().getJavaType();
        return javaType.getJavaProperty(getName());
    }

    /**
     * Sets the corresponding JavaProperty representation for this JDOProperty.
     * @param javaField the corresponding JavaProperty representation
     * @throws ModelException if impossible
     */
    public void setJavaField(JavaField javaField) throws ModelException {
        if (javaField instanceof JavaProperty) {
            this.javaField = javaField;
        }
        else {
            throw new ModelException(msg.msg(
                "EXC_InvalidJavaFieldForJDOProperty", javaField)); //NOI18N
        }
    }

    /**
     * Convenience method to check whether this field represents a property.
     * @return <code>true</code> if this field represents a property; 
     * <code>false</code> otherwise
     */
    public boolean isProperty() {
        return true;
    }

    // ===== methods specified in JDOProperty =====

    /** 
     * Return the JDOField instance associated with this property, if
     * available. If there is no JDOField instance associated, then the method
     * returns <code>null</code>.
     * <p>
     * This implementation always retruns <code>null</code>.
     * @return associated JDOField instance or <code>null</code> if not
     * available.
     */
    public JDOField getAssociatedJDOField() {
        return null;
    }
}
