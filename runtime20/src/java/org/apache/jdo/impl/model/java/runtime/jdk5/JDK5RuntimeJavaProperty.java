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

package org.apache.jdo.impl.model.java.runtime.jdk5;

import org.apache.jdo.impl.model.java.JavaPropertyImpl;
import org.apache.jdo.model.java.JavaMethod;
import org.apache.jdo.model.java.JavaType;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.jdo.impl.model.java.runtime.*;

/**
 * Default implementation for the JavaProperty interfaces. A JavaProperty
 * represents a JavaBeans property.
 *
 * @author Michael Bouschen
 */
public class JDK5RuntimeJavaProperty 
        extends JavaPropertyImpl
{
    /** */
    public JDK5RuntimeJavaProperty(String name, 
        JavaMethod getter, JavaMethod setter,
        JavaType type, JavaType declaringClass)
    {
        super(name, getter, setter, type, declaringClass);
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
    
    // ===== Methods not specified in JavaProperty =====
    
    /** 
     * Returns a JavaType instance for the specified Class object. 
     * This method provides a hook such that ReflectionJavaField subclasses can
     * implement their own mapping of Class objects to JavaType instances. 
     */
    public JavaType getJavaTypeForClass(Class clazz)
    {
        return ((JDK5RuntimeJavaType) getDeclaringClass()).
                getJavaTypeForClass(clazz);
    }
}
