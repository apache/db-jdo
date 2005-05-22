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

package org.apache.jdo.impl.model.java.runtime;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.jdo.impl.model.java.reflection.ReflectionJavaType;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaModelFactory;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.util.I18NHelper;



/**
/**
 * A reflection based JavaType implementation used at runtime.  
 * The implementation takes <code>java.lang.Class</code> and
 * <code>java.lang.reflect.Field</code> instances to get Java related
 * metadata about types and fields. 
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class RuntimeJavaType
    extends ReflectionJavaType
{
    /** JavaModelFactory */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    /** Constructor. */
    public RuntimeJavaType(Class clazz, JDOModel jdoModel)
    {
        super(clazz, jdoModel);
    }

    /** 
     * Returns a JavaType instance for the specified Class object. 
     * This method provides a hook such that RuntimeJavaType subclasses can
     * implement their own mapping of Class objects to JavaType instances. 
     * <p>
     * This implementation delegates the call to the javaModelFactory. 
     * @param clazz the Class instance representing the type
     * @return a JavaType instance for the name of the specified class
     * object or <code>null</code> if not present in this model instance.
     */
    protected JavaType getJavaTypeInternal(Class clazz)
    {
        return javaModelFactory.getJavaType(clazz);
    }
    
}
