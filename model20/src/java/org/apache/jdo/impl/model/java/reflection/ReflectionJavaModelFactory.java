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

package org.apache.jdo.impl.model.java.reflection;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.impl.model.java.AbstractJavaModelFactory;
import org.apache.jdo.impl.model.java.BaseReflectionJavaType;
import org.apache.jdo.util.I18NHelper;

/**
 * A reflection based JavaModelFactory implementation. 
 * The implementation takes <code>java.lang.Class</code> and
 * <code>java.lang.reflect.Field</code> instances to get Java related
 * metadata about types and fields. This implementation caches JavaModel
 * instances per ClassLoader.
 * 
 * @since 1.1
 */
public abstract class ReflectionJavaModelFactory
    extends AbstractJavaModelFactory
{    
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N

    /**
     * Creates a new empty JavaModel instance. A factory implementation may
     * use the specified key when caching the new JavaModel instance. 
     * <p>
     * This implementation only accepts <code>java.lang.ClassLoader</code>
     * instances as key. A ModelException indicates an invalid key.
     * <p>
     * The method automatically sets the parent/child relationship for the
     * created JavaModel according to the parent/child relationship of the 
     * ClassLoader passed as key. 
     * @param key the key that may be used to cache the returned JavaModel
     * instance. 
     * @return a new JavaModel instance.
     * @exception ModelException if impossible; the key is of an
     * inappropriate type.
     */
    public JavaModel createJavaModel(Object key)
        throws ModelException
    {
        if ((key != null) && (!(key instanceof ClassLoader)))
            throw new ModelException(msg.msg("EXC_InvalidJavaModelKey", //NOI18N
                                             key.getClass().getName()));
        
        ClassLoader classLoader = (ClassLoader)key;
        JavaModel javaModel = newJavaModelInstance(classLoader);

        // check parent <-> child relationship
        if (classLoader != null) {
            // if the specified classLoader is not null,
            // try to get the parent class loader and update the parent property
            try {
                ClassLoader parentClassLoader = classLoader.getParent();
                if (parentClassLoader != null) {
                    javaModel.setParent(getJavaModel(parentClassLoader));
                }
            }
            catch (SecurityException ex) {
                // ignore => parentClassLoader and parent JavaModel are null
            }
        }

        return javaModel;
    }

    /**
     * Returns a JavaType instance for the specified type description
     * (optional operation). This method is a convenience method and a
     * short cut for <code>getJavaModel(key).getJavaType(typeName)</code>.
     * <p>
     * The ReflectionJavaModelFactory supports this short cut and accepts
     * <code>java.lang.Class</code> instances as valid arguments for this
     * method. The method throws a 
     * {@link org.apache.jdo.model.ModelFatalException}, if the specified
     * type descriptor is not a <code>java.lang.Class</code> instance. 
     * @param typeDesc the type description
     * @return a JavaType instance for the specified type.
     * @exception ModelFatalException the specified type description is not
     * a <code>java.lang.Class</code> instance.
     */
    public JavaType getJavaType(Object typeDesc)
    {
        if (typeDesc == null)
            return null;

        try {
            Class clazz = (Class)typeDesc;
            ClassLoader classLoader = getClassLoaderPrivileged(clazz);
            return getJavaModel(classLoader).getJavaType(clazz);
        }
        catch (ClassCastException ex) {
            throw new ModelFatalException(msg.msg("EXC_InvalidTypeDesc", //NOI18N
                typeDesc.getClass().getName()));
        }
    }

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
                msg.msg("EXC_CannotGetClassInstance", name, loader), ex);
        }
    }

    /**
     * Returns the <code>java.lang.Class</code> wrapped in the specified 
     * JavaType. 
     * @return the <code>java.lang.Class</code> for the specified
     * JavaType. 
     * @exception ModelFatalException the specified JavaType does
     * not wrap a <code>java.lang.Class</code> instance.
     */
    public Class getJavaClass(JavaType javaType) 
    {
        if (javaType == null)
            return null;
        
        try {
            return ((BaseReflectionJavaType)javaType).getJavaClass();
        }
        catch (ClassCastException ex) {
            throw new ModelFatalException(msg.msg(
                "EXC_InvalidJavaType", javaType.getClass())); //NOI18N
        }
    }

    //========= Internal helper methods ==========
    
    /** 
     * Creates a new instance of the JavaModel implementation class.
     * <p>
     * This implementation returns a <code>ReflectionJavaModel</code>
     * instance.
     * @return a new JavaModel instance.
     */
    protected JavaModel newJavaModelInstance(ClassLoader classLoader) {
        return new ReflectionJavaModel(classLoader, this);
    }

}
