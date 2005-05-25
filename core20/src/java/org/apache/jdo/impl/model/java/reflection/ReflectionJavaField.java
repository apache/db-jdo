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

import java.lang.reflect.Field;

import org.apache.jdo.impl.model.java.BaseReflectionJavaField;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.util.I18NHelper;

/**
 * A reflection based JavaField implementation used at runtime.  
 * The implementation takes <code>java.lang.reflect.Field</code> instances
 * to get Java related metadata about fields. 
 * 
 * @author Michael Bouschen
 * @since JDO 1.1
 * @version JDO 2.0
 */
public class ReflectionJavaField
    extends BaseReflectionJavaField
{
    /** The corresponding JDO metadata. */
    protected JDOField jdoField;

    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N

    /** 
     * Constructor for fields w/o JDO metadata. 
     * @param field the reflection field representation.
     * @param declaringClass the JavaType of the class that declares the field.
     */
    public ReflectionJavaField(Field field, JavaType declaringClass)
    {
        super(field, declaringClass);
        this.type = getJavaTypeForClass(field.getType());
    }
    
    /** 
     * Constructor for fields having JDO metadata.
     * @param jdoField the JDO field metadata.
     * @param type the field type.
     * @param declaringClass the JavaType of the class that declares the field.
     */
    public ReflectionJavaField(JDOField jdoField, JavaType type, JavaType declaringClass)
    {
        super((jdoField == null) ? null : jdoField.getName(), declaringClass);
        if (jdoField == null)
            throw new ModelFatalException(msg.msg(
                "ERR_InvalidNullFieldInstance", //NOI18N
                "ReflectionJavaField.<init>")); //NOI18N
        this.jdoField = jdoField;
        this.type = type;
    }
    
    /**
     * Returns the JavaType representation of the field type.
     * @return field type
     */
    public JavaType getType()
    {
        if (type == null) {
            type = getJavaTypeForClass(getField().getType());
        }
        return type;
    }

    /**
     * Returns the JDOField instance if this JavaField represents a
     * managed field of a persistence capable class. The method returns
     * <code>null</code>, if this JavaField does not represent a managed
     * field. 
     * <p>
     * The method throws a {@link org.apache.jdo.model.ModelFatalException},
     * if there is a problem accessing the JDO meta data for this JavaField. 
     * @return the JDOField instance if this JavaField represents a managed
     * field; <code>null</code> otherwise.
     */
    public JDOField getJDOField()
    {
        return jdoField;
    }

    // ===== Methods not defined in JavaField =====

    /** 
     * Returns a JavaType instance for the specified Class object. 
     * This method provides a hook such that ReflectionJavaField subclasses can
     * implement their own mapping of Class objects to JavaType instances. 
     */
    public JavaType getJavaTypeForClass(Class clazz)
    {
        return ((ReflectionJavaType)getDeclaringClass()).getJavaTypeForClass(clazz);
    }
}
