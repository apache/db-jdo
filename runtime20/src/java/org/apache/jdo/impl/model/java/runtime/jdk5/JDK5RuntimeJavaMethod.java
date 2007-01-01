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

import org.apache.jdo.impl.model.java.reflection.ReflectionJavaMethod;
import java.lang.reflect.Method;
import org.apache.jdo.model.java.JavaType;

/**
 * A reflection based JavaMethod implementation used at runtime in a
 * J2SE5.0 environment. 
 *
 * @author Michael Bouschen
 */
public class JDK5RuntimeJavaMethod 
        extends ReflectionJavaMethod
{
    /** 
     * Constructor.
     * @param method the reflection method representation.
     * @param declaringClass the JavaType of the class that declares the
     * method. 
     */
    public JDK5RuntimeJavaMethod(Method method, JavaType declaringClass)
    {
        super(method, declaringClass);
    }
        
    // ===== methods specified in JavaMember =====
    
     /**
     * Returns the JavaType representation of the component type of the return
     * type of the method, if the method return type is an array or
     * collection. The method returns <code>null</code>, if the property type
     * is not an array or collection.
     * @return the component type of the method return type in case of an
     * array or collection. 
     */
    public JavaType getComponentType() 
    {
        Class componentClass = ComponentTypeHelper.getComponentClass(this);
        return (componentClass == null) ? null : 
            getJavaTypeForClass(componentClass);
   }
}
