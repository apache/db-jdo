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

import java.util.Map;
import java.util.HashMap;
import java.security.AccessController;
import java.security.PrivilegedAction;

import java.lang.reflect.Field;

import org.apache.jdo.impl.model.java.PredefinedType;
import org.apache.jdo.impl.model.java.BaseReflectionJavaField;
import org.apache.jdo.impl.model.java.BaseReflectionJavaType;
import org.apache.jdo.model.ModelFatalException;
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
 * @since JDO 1.1
 */
public abstract class ReflectionJavaType
    extends BaseReflectionJavaType
{
    /** The JDOModel instance to lookup JDO metadata. */
    private JDOModel jdoModel;

    /** Flag indicating whether the superclass is checked already. */
    private boolean superclassUnchecked = true;

    /** Flag indicating whether the JDOClass info is retrieved already. */
    private boolean jdoClassUnchecked = true;

    /** The JDO metadata, if this type represents a pc class. */
    private JDOClass jdoClass;

    /** Map of JavaField instances, key is the field name. */
    private Map javaFields = new HashMap();

    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N

    /** Constructor. */
    public ReflectionJavaType(Class clazz, JDOModel jdoModel)
    {
        // Pass null as the superclass to the super call. This allows lazy
        // evaluation of the superclass (see getSuperclass implementation).
        super(clazz, null); 
        this.jdoModel = jdoModel;
    }

    /**
     * Determines if this JavaType object represents an array type.
     * @return <code>true</code> if this object represents an array type; 
     * <code>false</code> otherwise.
     */
    public boolean isArray()
    {
        return clazz.isArray();
    }

    /** 
     * Returns <code>true</code> if this JavaType represents a persistence
     * capable class.
     * <p>
     * A {@link org.apache.jdo.model.ModelFatalException} indicates a
     * problem accessing the JDO meta data for this JavaType.
     * @return <code>true</code> if this JavaType represents a persistence
     * capable class; <code>false</code> otherwise.
     * @exception ModelFatalException if there is a problem accessing the
     * JDO metadata
     */
    public boolean isPersistenceCapable()
        throws ModelFatalException
    {
        return (getJDOClass() != null);
    }

    /** 
     * Returns the JavaType representing the superclass of the entity
     * represented by this JavaType. If this JavaType represents either the 
     * Object class, an interface, a primitive type, or <code>void</code>, 
     * then <code>null</code> is returned. If this object represents an
     * array class then the JavaType instance representing the Object class
     * is returned.  
     * @return the superclass of the class represented by this JavaType.
     */
    public synchronized JavaType getSuperclass()
    {
        if (superclassUnchecked) {
            superclassUnchecked = false;
            superclass = getJavaTypeInternal(clazz.getSuperclass());
        }
        return superclass;
    }

    /**
     * Returns the JDOClass instance if this JavaType represents a
     * persistence capable class. The method returns <code>null</code>, 
     * if this JavaType does not represent a persistence capable class.
     * <p>
     * A {@link org.apache.jdo.model.ModelFatalException} indicates a
     * problem accessing the JDO meta data for this JavaType.
     * @return the JDOClass instance if this JavaType represents a
     * persistence capable class; <code>null</code> otherwise.
     * @exception ModelFatalException if there is a problem accessing the
     * JDO metadata
     */
    public synchronized JDOClass getJDOClass()
        throws ModelFatalException
    {
        if (jdoClassUnchecked) {
            jdoClassUnchecked = false;
            jdoClass = jdoModel.getJDOClass(getName());
        }
        return jdoClass;
    }
 
    /** 
     * Returns the JavaType representing the component type of an array. 
     * If this JavaType does not represent an array type this method
     * returns <code>null</code>.
     * @return the JavaType representing the component type of this
     * JavaType if this class is an array; <code>null</code> otherwise. 
     */ 
    public JavaType getArrayComponentType()
    {
        JavaType componentType = null;
        if (isArray()) {
            Class componentClass = clazz.getComponentType();
            if (componentClass != null)
                componentType = getJavaTypeInternal(componentClass);
        }
        return componentType;
    }

    /**
     * Returns a JavaField instance that reflects the field with the
     * specified name of the class or interface represented by this
     * JavaType instance. The method returns <code>null</code>, if the
     * class or interface (or one of its superclasses) does not have a
     * field with that name.
     * @param fieldName the name of the field 
     * @return the JavaField instance for the specified field in this class
     * or <code>null</code> if there is no such field.
     */
    public JavaField getJavaField(String fieldName) 
    { 
        JavaField javaField = getDeclaredJavaField(fieldName);
        if (javaField == null) {
            // check superclass
            JavaType superclass = getSuperclass();
            if ((superclass != null) &&
                (superclass != PredefinedType.objectType)) {
                javaField = superclass.getJavaField(fieldName);
            }
        }
        return javaField;
    }

    // ===== Methods not defined in JavaType =====

    /**
     * RegisterClassListener calls this method to create a ReflectionJavaField
     * instance when processing the enhancer generated metadata.
     * @param jdoField the JDO field metadata
     * @param type the type of the field
     * @return the ReflectionJavaField representation
     */
    public JavaField createJavaField(JDOField jdoField, JavaType type)
    {
        String name = jdoField.getName();
        synchronized(javaFields) {
            JavaField javaField = (JavaField)javaFields.get(name);
            if (javaField != null) {
                throw new ModelFatalException(msg.msg(
                    "ERR_MultipleJavaField", //NOI18N
                    "ReflectionJavaType.createJavaField", name, getName())); //NOI18N
            }
            javaField = new ReflectionJavaField(jdoField, type, this);
            javaFields.put(name, javaField);
            return javaField;
        }
    }

    /**
     * Returns a JavaField instance that reflects the declared field with
     * the specified name of the class or interface represented by this
     * JavaType instance. The method returns <code>null</code>, if the 
     * class or interface does not declared a field with that name. It does
     * not check whether one of its superclasses declared such a field.
     * @param fieldName the name of the field 
     * @return the JavaField instance for the specified field in this class
     */
    public JavaField getDeclaredJavaField(String fieldName)
    {
        synchronized (javaFields) {
            JavaField javaField = (JavaField)javaFields.get(fieldName);
            if (javaField == null) {
                JDOClass jdoClass = getJDOClass();
                if (jdoClass != null) {
                    // pc class => look for JDOField first
                    JDOField jdoField = jdoClass.getDeclaredField(fieldName);
                    if (jdoField != null) {
                        javaField = new ReflectionJavaField(jdoField, this);
                        javaFields.put(fieldName, javaField);
                    }
                }
                
                // if no field info check reflection
                if (javaField == null) {
                    Field field = ReflectionJavaField.getDeclaredFieldPrivileged(
                        clazz, fieldName);
                    if (field != null) {
                        javaField = new ReflectionJavaField(field, this);
                        javaFields.put(fieldName, javaField);
                    }
                }
            }
            return javaField;   
        }
    }

    /** 
     * Returns a JavaType instance for the specified Class object. 
     * This method provides a hook such that ReflectionJavaType subclasses can
     * implement their own mapping of Class objects to JavaType instances. 
     */
    protected abstract JavaType getJavaTypeInternal(Class clazz);
    
}
