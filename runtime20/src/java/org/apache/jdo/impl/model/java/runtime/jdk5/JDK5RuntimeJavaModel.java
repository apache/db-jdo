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

import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.impl.model.java.runtime.*;

/**
 * A reflection based JavaModel implementation used at runtime in a J2SE5.0
 * environment. 
 * The implementation takes <code>java.lang.Class</code> and
 * <code>java.lang.reflect.Field</code> instances to get Java related
 * metadata about types and fields. 
 * <p>
 * The RuntimeJavaModelFactory caches JavaModel instances per ClassLoader.
 * The RuntimeJavaModel implementation will use this ClassLoader to lookup
 * any type by name. This makes sure that the type name is unique.
 *
 * @author Michael Bouschen
 * @since JDO 2.0
 */
public class JDK5RuntimeJavaModel
    extends RuntimeJavaModel
{
    /** Constructor. */
    public JDK5RuntimeJavaModel(ClassLoader classLoader, 
        JDK5RuntimeJavaModelFactory declaringJavaModelFactory) 
    {
        super(classLoader, declaringJavaModelFactory);
    }
    
    /** 
     * Creates a new instance of the JavaType implementation class.
     * <p>
     * This implementation returns a RuntimeJavaType instance.
     * @param clazz the Class instance representing the type
     * @return a new JavaType instance
     */
    protected JavaType newJavaTypeInstance(Class clazz)
    {
        return new JDK5RuntimeJavaType(clazz, this);
    }
    
}
