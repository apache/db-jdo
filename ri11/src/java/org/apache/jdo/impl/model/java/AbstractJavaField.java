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

package org.apache.jdo.impl.model.java;

import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOField;


/**
 * Abstract super class for JavaField implementations. 
 * It provides getters for the name and declaringClass properties which are
 * initialized in the constructor. The implementation of method getJDOField
 * always returns <code>null</code>.
 * <p>
 * A non-abstract subclass must implement methods
 * {@link #getModifiers()} and {@link #getType()}. Note, this
 * implementation of method {@link #getJDOField()} always returns
 * <code>null</code>, so a subclass may want to override this method.
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1 
 */
abstract public class AbstractJavaField
    implements JavaField 
{
    /** The Field name. */
    private String name;

    /** The declaring class. */
    private JavaType declaringClass;

    /** 
     * Constructor setting the name and declaringClass property.
     * @param name field name
     * @param declaringClass the JavaType of the class or interface that
     * declares this JavaField.
     */
    public AbstractJavaField(String name, JavaType declaringClass)
    {
        this.name = name;
        this.declaringClass = declaringClass;
    }

    /**
     * Returns the name of the field. 
     * @return field name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the Java language modifiers for the field represented by
     * this JavaField, as an integer. The java.lang.reflect.Modifier class
     * should be used to decode the modifiers. 
     * @return the Java language modifiers for this JavaField
     * @see java.lang.reflect.Modifier
     */
    abstract public int getModifiers();
    
    /**
     * Returns the JavaType representation of the field type.
     * @return field type
     */
    abstract public JavaType getType();

    /**
     * Returns the JavaType instance representing the class or interface
     * that declares the field represented by this JavaField instance.
     * @return the JavaType instance of the declaring class.
     */
    public JavaType getDeclaringClass()
    {
        return declaringClass;
    }
    
    /**
     * Returns the corresponding JDOField instance, if the JDOModel
     * provides any JDO metadata for the field represented by this
     * JavaField. If there is no corresponding JDOField representation, the
     * method returns <code>null</code>. 
     * <p>
     * This implementation always returns <code>null</code>.
     * @return the corresponding JDOField instance (if available);
     * <code>null</code> otherwise.
     */
    public JDOField getJDOField()
    {
        return null;
    }
    
    // ===== Methods not defined in JavaField =====

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare. 
     * <p>
     * This implementation matches the declaring class and the name of the
     * specified object to the declaring class and the name of this
     * JavaField. 
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj)
    {
        // return true if obj is this
        if (obj == this) return  true;
        // return false if obj does not have the correct type
        if ((obj == null) || !(obj instanceof JavaField)) return false;

        JavaField other = (JavaField)obj;
        // compare declaringClass and field names
        return (getDeclaringClass() == other.getDeclaringClass())
            && (getName().equals(other.getName()));
    }
    
    /**
     * Returns a hash code value for the object. 
     * <p>
     * This is computed as the exclusive-or of the hashcodes for the
     * underlying field's declaring class name and its name.
     * @return a hash code value for this object.
     */
    public int hashCode()
    {
        return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
    }
    
    /**
     * Returns a string representation of the object. 
     * @return a string representation of the object.
     */
    public String toString()
    {
        return getDeclaringClass().getName() + "." + getName();
    }
}

