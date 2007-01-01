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

/*
 * TypeTable.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import java.util.*;
import java.lang.Class;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.JDOHelper;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.impl.model.java.ErrorType;
import org.apache.jdo.impl.model.java.PredefinedType;
import org.apache.jdo.impl.model.java.WrapperClassType;
import org.apache.jdo.impl.model.java.reflection.ReflectionJavaField;
import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOCollection;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDORelationship;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.FieldManager;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;


/** 
 * Provides query convenience methods to deal with Java/JDO metadata.
 *
 * @author  Michael Bouschen
 * @since JDO 1.0.1
 */
public class TypeSupport
{
    /** The JavaModel for the class loader of the candidate class. */
    protected JavaModel applicationJavaModel;

    /** The runtime JavaModel factory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    /** I18N support */
    private static final I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", TypeSupport.class.getClassLoader()); //NOI18N

    /**
     * This methods sets the application JavaModel to the JavaModel
     * instance for the specified ClassLoader.
     * @param classLoader the class loader of the candidate class.
     */
    public void initApplicationJavaModel(ClassLoader classLoader)
    {
        this.applicationJavaModel = javaModelFactory.getJavaModel(classLoader);
    }

    /**
     * Returns the JavaType representation for the type with the specified
     * name. This method uses the application JavaModel for the type
     * lookup. This means any type is checked in the context of the
     * JavaModel for the class loader of the candidate class.
     * @param  name the name of the type to be checked. 
     * @return the JavaType object representing the type with the 
     *         specified name or null when the type was not found.
     */
    public JavaType checkType(String name)
    {
        return applicationJavaModel.getJavaType(name);
    }
    
    /**
     * Checks for the Java Type with the specified class object. 
     * @param  clazz the clazz object of the type to be checked. 
     * @return the TypeDescriptor object representing the type
     */
    public JavaType checkType(Class clazz)
    {
        if (clazz == null)
            return null;
        return javaModelFactory.getJavaType(clazz);
    }

    /**
     * Implements binary numeric promotion as defined in the 
     * Java Language Specification section 5.6.2
     */
    public static JavaType binaryNumericPromotion(JavaType left, JavaType right)
    {
        if ((left == null) || (right == null))
            return ErrorType.errorType;

        if (isNumericType(left) && isNumericType(right)) {
            if (left.equals(PredefinedType.doubleType) || 
                right.equals(PredefinedType.doubleType)) {
                return PredefinedType.doubleType;
            }
            else if (left.equals(PredefinedType.floatType) || 
                     right.equals(PredefinedType.floatType)) {
                return PredefinedType.floatType;
            }
            else if (left.equals(PredefinedType.longType) || 
                     right.equals(PredefinedType.longType)) {
                return PredefinedType.longType;
            }
            else {
                return PredefinedType.intType;
            }
        }

        return ErrorType.errorType;
    }

    /**
     * Implements unray numeric promotion as defined in the 
     * Java Language Specification section 5.6.1
     */
    public static JavaType unaryNumericPromotion(JavaType type)
    {
        if (type == null)
            return ErrorType.errorType;

        if (isNumericType(type)) {
            if (type.equals(PredefinedType.byteType) || 
                type.equals(PredefinedType.shortType) || 
                type.equals(PredefinedType.charType)) {
                return PredefinedType.intType;
            }
            else {
                return type;
            }
        }

        return ErrorType.errorType;
    }

    /** 
     * Returns the java.lang.Class instance for the specified type.
     * @param type the type to be checked
     * @return the corresponding class instance.
     */
    public static Class getJavaClass(JavaType type)
    {
        return javaModelFactory.getJavaClass(type);
    }

    //========= Interrogative methods ==========

    /** 
     * Returns <code>true</code> if the specified type is 
     * boolean or java.lang.Boolean.
     * @param type the type to be checked
     * @return <code>true</code> if type is boolean or java.lang.Boolean; 
     * <code>false</code> otherwise.
     */
    public static boolean isBooleanType(JavaType type)
    {
        return type.equals(PredefinedType.booleanType) || 
            type.equals(PredefinedType.booleanClassType);
    }

    /** 
     * Returns <code>true</code> if the specified type is 
     * char or java.lang.Character 
     * @param type the type to be checked
     * @return <code>true</code> if type is char or java.lang.Character 
     * <code>false</code> otherwise.
     */
    public static boolean isCharType(JavaType type)
    {
        return type.equals(PredefinedType.charType) ||
            type.equals(PredefinedType.characterClassType);
    }

    /** 
     * Returns <code>true</code> if the specified type is an interal type
     * or a Java wrapper class for an interal type.
     * @param type the type to be checked
     * @return <code>true</code> if type is an integral type or a Java
     * wrapper for an integral type; <code>false</code> otherwise.
     */
    public static boolean isIntegralType(JavaType type)
    {
        return type.isIntegral() ||
            (type.isWrapperClass() && 
             ((WrapperClassType)type).getWrappedPrimitiveType().isIntegral());
    }

    /**
     * Returns <code>true</code> if specified type is a number type:
     * <br>
     * a numeric primitive
     * <br>
     * a numeric wrapper class 
     * <br>
     * java.math.BigDecimal, java.math.BigInteger.
     * @param type the type to be checked
     * @return <code>true</code> if type is a number type;
     * <code>false</code> otherwise.
     */
    public static boolean isNumberType(JavaType type)
    {
        return isNumericType(type) || 
            isNumericWrapperClassType(type) ||
            isMathType(type);
    }

    /** 
     * Returns <code>true</code> if the specified type is a Java wrapper
     * class type for a numeric primitive type. 
     * @param type the type to be checked
     * @return <code>true</code> if type is a numeric wrapper class type;
     * <code>false</code> otherwise.
     */
    public static boolean isNumericWrapperClassType(JavaType type)
    {
        return type.isWrapperClass() && 
            isNumericType(((WrapperClassType)type).getWrappedPrimitiveType());
    }

    /**
     * Returns <code>true</code> if the specified type is a either a
     * integral or a floating point type.
     * @param type the type to be checked
     * @return <code>true</code> if type is a numeric type;
     * <code>false</code> otherwise.
     */
    public static boolean isNumericType(JavaType type)
    {
        return type.isIntegral() || type.isFloatingPoint();
    }
    
    /** 
     * Returns <code>true</code> if the specified type is either
     * java.math.BigDecimal or java.math.BigInteger. 
     * @param type the type to be checked
     * @return <code>true</code> if type is BigDecimal or BigInteger;
     * <code>false</code> otherwise.
     */
    public static boolean isMathType(JavaType type)
    {
        return PredefinedType.bigDecimalType.equals(type) ||
            PredefinedType.bigIntegerType.equals(type);
    }

    //========= Field access methods ==========

    /** */
    public static boolean isStaticField(JavaField field)
    {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers);
    }
    
    /** */
    public static JDOField getJDOField(JavaField javaField)
    {
        JDOField jdoField = null;
        JDOClass jdoClass = javaField.getDeclaringClass().getJDOClass();
        if (jdoClass != null) {
            jdoField = jdoClass.getField(javaField.getName());
        }
        return jdoField;
    }

    /** */
    public static JavaType getElementType(JavaField field)
    {
        JDOField jdoField = getJDOField(field);
        if (jdoField != null) {
            // check relationship
            try {
                JDORelationship jdoRelationship = jdoField.getRelationship();
                if (jdoRelationship instanceof JDOCollection) {
                    return ((JDOCollection)jdoRelationship).getElementType();
                } 
            }
            catch (ModelFatalException ex) {
                throw new JDOQueryException(ex.getMessage());
            }
        }
        JavaType fieldType = field.getType();
        if (fieldType.isJDOSupportedCollection())
            return PredefinedType.objectType;

        // If this code is reached the field is not specified as collection
        // in the JDO metadata and does not have a JDO supported collection
        // type => internal error. 
        String fieldName = field.getName();
        String className = field.getDeclaringClass().getName();
        throw new JDOFatalInternalException(
                msg.msg("ERR_CollectionFieldExpected", //NOI18N
                        fieldName, className, fieldType.getName()));
    }

    /**
     * field value of a managed field. Not using reflection.
     */
    public static Object getFieldValue(int fieldNumber, 
                                       PersistenceManagerInternal pm, 
                                       Object object)
    {
        PersistenceCapable pc = (PersistenceCapable)object;
        StateManagerInternal sm = pm.findStateManager(pc);
        FieldManager fm = new SimpleFieldManager();
        // Call isLoaded to ensure the field with the specified
        // field number is loaded.
        // NOTE, this code is not StateManager implementation neutral,
        // because it relies on the fact that isLoaded actually loads
        // the field. A neutral implementation would check the returns
        // value of isLoaded and if it returns false it would call a
        // jdoGetXXX to load the field.
        sm.isLoaded(pc, fieldNumber);
        sm.provideField(fieldNumber, fm , false);
        // NOTE, this call assumes that fetchObjectField return the 
        // field value regardless which type the field has. Only 
        // SimpleFieldManager from common.query.util.types has this 
        // behavior. This is a workaround and needs to be changed!
        return fm.fetchObjectField(fieldNumber);
    }

    /**
     * Get field value via reflection 
     */
    public static Object getFieldValue(Field field, Object object)
    {
        try {
            return field.get(object);
        }
        catch (IllegalAccessException ex) {
            String fieldName = field.getName();
            String className = field.getDeclaringClass().getName();
            throw new JDOQueryException(
                msg.msg("EXC_CannotAccessField", //NOI18N
                        fieldName, className, ex));
        }
    }
    
    /** 
     * Returns the fieldNumber of the specified field.
     * @return field number 
     */
    public static int getFieldNumber(JavaField javaField,
                                     PersistenceManager pm, 
                                     Object object)
    {
        JDOField jdoField = getJDOField(javaField);
        if ((object == null) || // null object means static field access
            (jdoField == null) || // no jdo field info
            !jdoField.isManaged()) { // field is not managed
            return -1; // use reflection
        }
        
        PersistenceManager instancePM =
            JDOHelper.getPersistenceManager(object);
        if (instancePM == null) { // object is a transient instance 
            return -1; // use reflection
        }
        else if (!instancePM.equals(pm)) {
            // instancePM is NOT the one from the query => exception
            throw new JDOException(
                msg.msg("EXC_InstanceBoundToDifferentPM", object)); //NOI18N
        }

        // return the field number from the JDOField
        return jdoField.getFieldNumber();
    }

    /**
     * Returns a accessible java.lang.reflect.Field instance for the
     * specified JavaField. Note, this method gets a new Field instance
     * from reflection and sets the accessibility. The method requires the
     * caller to have the permission ReflectPermission("suppressAccessChecks").
     * @param javaField the JavaField 
     * @return accessible Field instance
     */
    public static Field getAccessibleField(JavaField javaField)
    {
        // Get a new java.lang.reflect.Field instance. The query runtime
        // needs an accessible Field instance.
        Class clazz = javaModelFactory.getJavaClass(javaField.getDeclaringClass());
        String fieldName = javaField.getName();
        final Field field = 
            ReflectionJavaField.getDeclaredFieldPrivileged(clazz, fieldName);
        if (field == null) {
            throw new JDOQueryException( 
                msg.msg("EXC_CannotFindField", //NOI18N
                        fieldName, clazz.getName()));
        }

        // if the field is not accessible, try to set the accessible flag.
        if (!field.isAccessible()) {
            try {
                field.setAccessible(true);
            }
            catch (SecurityException ex) {
                throw new JDOQueryException(
                    msg.msg("EXC_CannotChangeAccessibility", //NOI18N
                            fieldName, clazz.getName()));
            }
        }
        return field;
    }
    
}
