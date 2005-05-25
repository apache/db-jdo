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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaMethod;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.util.I18NHelper;

/** 
 * Helper clas to intropsect a ReflectionJavaType representing a class for
 * properties. 
 *
 * @author Michael Bouschen
 * @since JDO 2.0
 */
public class ReflectionJavaTypeIntrospector
{
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance("org.apache.jdo.impl.model.java.Bundle"); //NOI18N
    
    /** 
     * Returns a map of properties for the specified class. The key is the
     * property name, value is the JavaProperty instance.
     * @param beanClass the class to be introspected
     * @return a map of JavaProperty instances
     */
    public void addDeclaredJavaProperties(ReflectionJavaType beanClass) 
    {
        Class clazz = beanClass.getJavaClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(
                clazz, clazz.getSuperclass());
            PropertyDescriptor[] descrs = 
                beanInfo.getPropertyDescriptors();
            if (descrs != null) {
                for (int i = 0; i < descrs.length; i++) {
                    PropertyDescriptor descr = descrs[i];
                    if (descr == null) continue;
                    String name = descr.getName();
                    JavaType type = 
                        beanClass.getJavaTypeForClass(descr.getPropertyType());
                    Method getter = descr.getReadMethod();
                    JavaMethod javaGetter = (getter == null) ? null : 
                        beanClass.createJavaMethod(getter);
                    Method setter = descr.getWriteMethod();
                    JavaMethod javaSetter = (setter == null) ? null : 
                        beanClass.createJavaMethod(setter);
                    beanClass.createJavaProperty(name, javaGetter, 
                                                 javaSetter, type);
                }
            }
        }
        catch (IntrospectionException ex) {
            throw new ModelFatalException(msg.msg(
                "ERR_CannotIntrospectClass", beanClass.getName()), ex); //NOI18N
        }
    }
}

