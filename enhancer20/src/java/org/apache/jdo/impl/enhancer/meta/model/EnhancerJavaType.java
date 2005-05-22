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

package org.apache.jdo.impl.enhancer.meta.model;

import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataFatalError;
import org.apache.jdo.impl.model.java.reflection.ReflectionJavaType;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOModel;



/**
 * Provides some basic Java type information based on JVM descriptors.
 * 
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class EnhancerJavaType
    extends ReflectionJavaType
{
    /** The declaring EnhancerJavaModel instance. */
    private JavaModel javaModel = null;

    /**
     * Creates an instance.
     */
    public EnhancerJavaType(Class clazz, JDOModel jdoModel, JavaModel javaModel)
    {
        super(clazz, jdoModel);
        this.javaModel = javaModel;
    }
    
    // ===== Methods not defined in JavaType =====

    /** 
     * Returns a JavaType instance for the specified Class object. 
     * <p>
     * This implementation delegates the call to the javaModel.
     * @param clazz the Class instance representing the type
     * @return a JavaType instance for the name of the specified class
     * object or <code>null</code> if not present in this model instance.
     */
    protected JavaType getJavaTypeInternal(Class clazz)
    {
        return javaModel.getJavaType(clazz);
    }
    
    
}
