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
import java.io.InputStream;

import org.apache.jdo.impl.model.java.AbstractJavaModel;
import org.apache.jdo.impl.model.jdo.caching.JDOModelFactoryImplCaching;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.JDOModelFactory;

/**
 * A reflection based JavaModel implementation used at runtime.  
 * The implementation takes <code>java.lang.Class</code> and
 * <code>java.lang.reflect.Field</code> instances to get Java related
 * metadata about types and fields. 
 * <p>
 * The ReflectionJavaModel implementation will use this ClassLoader to lookup
 * any type by name. This makes sure that the type name is unique.
 *
 * @since 1.1
 * @version 2.0
 */
public class ReflectionJavaModel
    extends AbstractJavaModel
{
    /** The ClassLoader instance used as key to cache this JavaModel. */
    private final ClassLoader classLoader;

    /** The declaring JavaModelFactory. */
    protected final ReflectionJavaModelFactory declaringJavaModelFactory;

    /** Constructor. */
    protected ReflectionJavaModel(ClassLoader classLoader,
        ReflectionJavaModelFactory declaringJavaModelFactory)
    {
        super();
        this.classLoader = classLoader;
        this.declaringJavaModelFactory = declaringJavaModelFactory;
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
                    final boolean initialize = false; 
                    Class clazz = ReflectionJavaModelFactory.forNamePrivileged(
                        name, initialize, classLoader);
                    javaType = getJavaTypeInternal(clazz);
                }
                catch (ClassNotFoundException ex) {
                    // cannot find class => return null
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
     * @param clazz the Class instance representing the type
     * @return a JavaType instance for the name of the specified class
     * object.
     */
    public JavaType getJavaType(Class clazz)
    {
        if (clazz == null)
            return null;
        
        return getJavaTypeInternal(clazz);
    }

    /**
     * Finds a resource with a given name. A resource is some data that can
     * be accessed by class code in a way that is independent of the
     * location of the code. The name of a resource is a "/"-separated path
     * name that identifies the resource. The method method opens the
     * resource for reading and returns an InputStream. It returns 
     * <code>null</code> if no resource with this name is found or if the
     * caller doesn't have adequate privileges to get the resource.  
     * <p>
     * This implementation delegates the request to the wrapped
     * ClassLoader. 
     * @param resourceName the resource name
     * @return an input stream for reading the resource, or <code>null</code> 
     * if the resource could not be found or if the caller doesn't have
     * adequate privileges to get the resource. 
     */
    public InputStream getInputStreamForResource(final String resourceName)
    {
        return (InputStream) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    ClassLoader loader = (classLoader == null) ?
                        ClassLoader.getSystemClassLoader() : classLoader;
                    return loader.getResourceAsStream(resourceName);
                }
            }
            );
    }

    /**
     * Returns the corresponding JDOModel instance.
     * @return the corresponding JDOModel.
     */
    public JDOModel getJDOModel()
    {
        if (jdoModel == null) {
            JDOModelFactory factory = JDOModelFactoryImplCaching.getInstance();
            jdoModel = factory.getJDOModel(this);
        }
        return jdoModel;
    }

    // ===== Methods not defined in JavaModel =====

    /** 
     * Returns the ClassLoader wrapped by this ReflectionJavaModel instance.
     * @return the ClassLoader
     */
    public ClassLoader getClassLoader()
    {
        return classLoader;
    }

    /**
     * The method returns the JavaType instance for the type name of the
     * specified class object. It first checks the cache and if there is no
     * entry for the type name in the cache then it creates a new JavaType
     * instance for the specified Class object.
     * @param clazz the Class instance representing the type
     * @return a JavaType instance for the name of the specified class
     * object or <code>null</code> if not present in this model instance.
     */
    public JavaType getJavaTypeInternal(Class clazz)
    {
        String name = clazz.getName();
        synchronized (types) {
            JavaType javaType = (JavaType)types.get(name);
            if (javaType == null) {
                javaType = newJavaTypeInstance(clazz);
                types.put(name, javaType);
            }
            return javaType;
        }
    }

    /**
     * Returns the declaring ReflectionJavaModelFactory of this
     * ReflectionJavaModel.
     * @return the declaring ReflectionJavaModelFactory
     */
    public ReflectionJavaModelFactory getDeclaringJavaModelFactory()
    {
        return declaringJavaModelFactory;
    }

    /** 
     * Creates a new instance of the JavaType implementation class.
     * <p>
     * This implementation returns a ReflectionJavaType instance.
     * @param clazz the Class instance representing the type
     * @return a new JavaType instance
     */
    protected JavaType newJavaTypeInstance(Class clazz)
    {
        return new ReflectionJavaType(clazz, this);
    }
    
}
