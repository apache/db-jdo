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

package org.apache.jdo.impl.model.java.runtime.jdk5;

import java.lang.reflect.Field;

import org.apache.jdo.impl.model.java.reflection.ReflectionJavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOField;

/**
 * A reflection based JavaField implementation used at runtime in a
 * J2SE5.0 environment. 
 *
 * @author Michael Bouschen
 */
public class JDK5RuntimeJavaField 
        extends ReflectionJavaField
{
    /** 
     * Constructor for fields w/o JDO metadata. 
     * @param field the reflection field representation.
     * @param declaringClass the JavaType of the class that declares the field.
     */
    public JDK5RuntimeJavaField(Field field, JavaType declaringClass)
    {
        super(field, declaringClass);
    }
        
    /** 
     * Constructor for fields having JDO metadata.
     * @param jdoField the JDO field metadata.
     * @param type the field type.
     * @param declaringClass the JavaType of the class that declares the field.
     */
    public JDK5RuntimeJavaField(JDOField jdoField, JavaType type, 
            JavaType declaringClass)
    {
        super(jdoField, type, declaringClass);
    }
    
    // ===== methods specified in JavaMember =====
    
     /**
     * Returns the JavaType representation of the component type of the type
     * of the property, if the property type is an array or collection. The
     * method returns <code>null</code>, if the property type is not an array
     * or collection.
     * @return the component type of the property type in case of an array or
     * collection.
     */
    public JavaType getComponentType() 
    {
        Class componentClass = ComponentTypeHelper.getComponentClass(this);
        return (componentClass == null) ? null : 
            getJavaTypeForClass(componentClass);
    }
}
