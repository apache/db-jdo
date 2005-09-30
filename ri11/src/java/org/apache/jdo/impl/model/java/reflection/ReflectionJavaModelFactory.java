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

package org.apache.jdo.impl.model.java.reflection;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.impl.model.java.AbstractJavaModelFactory;
import org.apache.jdo.impl.model.java.BaseReflectionJavaType;
import org.apache.jdo.util.I18NHelper;

/**
 * A reflection based JavaModelFactory implementation. 
 * 
 * @since JDO 1.1
 */
public abstract class ReflectionJavaModelFactory
    extends AbstractJavaModelFactory
{    
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N

    // ===== Methods not defined in JavaModelFactory =====

    /**
     * Calls getClassLoader on the specified class instance in a
     * doPrivileged block. Any SecurityException is wrapped into a
     * ModelFatalException. 
     * @return the class loader that loaded the specified class instance.
     * @exception ModelFatalException wraps the SecurityException thrown by
     * getClassLoader.
     */
    public static ClassLoader getClassLoaderPrivileged(final Class clazz)
    {
        if (clazz == null)
            return null;

        try { 
            return (ClassLoader) AccessController.doPrivileged(
                new PrivilegedAction () {
                    public Object run () {
                        return clazz.getClassLoader();
                    }
                }
                );
        }
        catch (SecurityException ex) {
            throw new ModelFatalException(
                msg.msg("EXC_CannotGetClassLoader", clazz), ex); //NOI18N
        }
    }

}
