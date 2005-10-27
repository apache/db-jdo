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
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.impl.model.java.AbstractJavaModelFactory;
import org.apache.jdo.util.I18NHelper;

/**
 * A reflection based JavaModelFactory implementation. 
 * 
 * @since 1.1
 */
public abstract class ReflectionJavaModelFactory
    extends AbstractJavaModelFactory
{    
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N

    // ===== Methods not defined in JavaModelFactory =====

    /**
     * Calls getClassLoader on the specified Class instance in a
     * doPrivileged block. Any SecurityException is wrapped into a
     * ModelFatalException. 
     * @param clazz the class to get the ClassLoader from.
     * @return the class loader that loaded the specified Class instance.
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

    /**
     * Calls Class.forName in a doPrivileged block. Any SecurityException is
     * wrapped into a ModelFatalException.
     * @param name fully qualified name of the desired class
     * @param initialize whether the class must be initialized
     * @param loader class loader from which the class must be loaded
     * @return class object representing the desired class.
     * @exception ModelFatalException wraps the SecurityException thrown by
     * getClassLoader.
     * @exception ClassNotFoundException if the class cannot be located by the
     * specified class loader.
     */
    public static Class forNamePrivileged(final String name, 
                                          final boolean initialize, 
                                          final ClassLoader loader)
        throws ClassNotFoundException
    {
        try { 
            return (Class) AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws ClassNotFoundException {
                        return Class.forName(name, initialize, loader);
                    }
                }
                );
        }
        catch (PrivilegedActionException pae) {
            throw (ClassNotFoundException) pae.getException();
        }
        catch (SecurityException ex) {
            throw new ModelFatalException(
                msg.msg("CannotGetClassInstance", name, loader), ex);
        }
    }

}
