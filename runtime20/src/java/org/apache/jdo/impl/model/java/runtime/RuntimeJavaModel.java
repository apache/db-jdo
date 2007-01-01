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

package org.apache.jdo.impl.model.java.runtime;

import org.apache.jdo.impl.model.java.PredefinedType; // for javadoc
import org.apache.jdo.impl.model.java.reflection.ReflectionJavaModel;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.util.I18NHelper;

/**
 * A reflection based JavaModel implementation used at runtime.  
 * The implementation takes <code>java.lang.Class</code> and
 * <code>java.lang.reflect.Field</code> instances to get Java related
 * metadata about types and fields. 
 * <p>
 * The RuntimeJavaModelFactory caches JavaModel instances per ClassLoader.
 * The RuntimeJavaModel implementation will use this ClassLoader to lookup
 * any type by name. This makes sure that the type name is unique.
 * <p>
 * Any JavaType instance bound to a RuntimeJavaModel instance wraps a class
 * instance that is loaded by the ClassLoader that corresponds to this
 * RuntimeJavaModel. The only exception are PredefinedType instances 
 * (see {@link PredefinedType.getPredefinedTypes()} which are present in every
 * RuntimeJavaModel instance.
 *
 * @since 1.0.1
 * @version 2.0
 */
public class RuntimeJavaModel
    extends ReflectionJavaModel
{
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.runtime.Bundle"); //NOI18N

    /** Constructor. */
    public RuntimeJavaModel(ClassLoader classLoader, 
                            RuntimeJavaModelFactory declaringJavaModelFactory) 
    {
        super(classLoader, declaringJavaModelFactory);
    }

    /** 
     * The method returns the JavaType instance for the specified type
     * name. A type name is unique within one JavaModel instance. The
     * method returns <code>null</code> if this model instance does not
     * know a type with the specified name.
     * <p>
     * Note, this method calls Class.forName with the wrapped ClassLoader,
     * if it cannot find a JavaType with the specified name in the cache.
     * @param name the name of the type
     * @return a JavaType instance for the specified name or
     * <code>null</code> if not present in this model instance.
     */
    public JavaType getJavaType(String name) 
    {
        synchronized (types) {
            JavaType javaType = (JavaType)types.get(name);
            if (javaType == null) {
                try {
                    // Note, if name denotes a pc class that has not been
                    // loaded, Class.forName will load the class which will
                    // register the runtime metadata at the JDOImplHelper.
                    // This will create a new JavaType entry in the cache.
                    final boolean initialize = true; 
                    Class clazz = RuntimeJavaModelFactory.forNamePrivileged(
                        name, initialize, getClassLoader());
                    // Get the class loader of the Class instance
                    ClassLoader loader = RuntimeJavaModelFactory.
                        getClassLoaderPrivileged(clazz);
                    // and get the JavaModel instance for the class loader. 
                    RuntimeJavaModel javaModel = (RuntimeJavaModel)
                        declaringJavaModelFactory.getJavaModel(loader);
                    // Delegate the JavaType lookup to the JavaModel instance.
                    javaType = javaModel.getJavaTypeInternal(clazz);
                }
                catch (ClassNotFoundException ex) {
                    // cannot find class => return null
                }
                catch (LinkageError ex) {
                    throw new ModelFatalException(msg.msg(
                        "EXC_ClassLoadingError", name, ex.toString())); //NOI18N
                }
            }
            return javaType;
        }
    }

    /** 
     * The method returns the JavaType instance for the type name of the
     * specified class object. This is a convenience method for 
     * <code>getJavaType(clazz.getName())</code>. The major difference
     * between this method and getJavaType taking a type name is that this 
     * method is supposed to return a non-<code>null<code> value. The
     * specified class object describes an existing type.
     * <p>
     * Note, this implementation does not call the overloaded getJavaType
     * method taking a String, because this would retrieve the Class
     * instance for the specified type again. Instead, it checks the cache 
     * directly. If not available it creates a new ReflectionJavaType using
     * the specified Class instance.
     * @param clazz the Class instance representing the type
     * @return a JavaType instance for the name of the specified class
     * object or <code>null</code> if not present in this model instance.
     */
    public JavaType getJavaType(Class clazz)
    {
        if (clazz == null)
            return null;
        
        String name = clazz.getName();
        synchronized (types) {
            JavaType javaType = (JavaType)types.get(name);
            if (javaType == null) {
                // Check whether the specified class object is loaded by the
                // class loader bound to this JavaModel. Note, we never
                // execute this check for any PredefinedType instance, because
                // the cache lookup will always find a JavaType (the cache is
                // initialized with all PredefinedTypes). The check would
                // potentially fail for a PredefinedType, because it might be
                // loaded by a different class loader.
                ClassLoader loader = 
                    RuntimeJavaModelFactory.getClassLoaderPrivileged(clazz);
                if (loader != getClassLoader()) {
                    throw new ModelFatalException(msg.msg(
                        "ERR_UnexpectedClassLoader", //NOI18N
                        clazz.getName(), loader, getClassLoader()));
                }
                try {
                    // Make sure the class is initialized, because this will
                    // register the runtime metadata at the JDOImplHelper. 
                    final boolean initialize = true; 
                    RuntimeJavaModelFactory.forNamePrivileged(
                        clazz.getName(), initialize, loader);
                }
                catch (ClassNotFoundException ex) {
                    // ignore, since class has already been loaded 
                }
                javaType = getJavaTypeInternal(clazz);
            }
            return javaType;
        }
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
        return new RuntimeJavaType(clazz, this);
    }
    
}
