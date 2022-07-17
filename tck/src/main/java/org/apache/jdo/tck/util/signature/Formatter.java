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

package org.apache.jdo.tck.util.signature;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * A helper class for building exhaustive string descriptions of
 * class, field, constructor, or method declarations.
 *
 * @author Martin Zaun
 */
class Formatter {
    /**
     * Returns a string formatting an array of names as
     * comma-separated list.
     */
    public static String toString(String prefix, String[] names) {
        final StringBuilder s = new StringBuilder();
        if (names != null && names.length > 0) { 
            s.append(prefix == null ? "" : prefix).append(names[0]);
            for (int i = 1; i < names.length; i++) {
                s.append(", ").append(names[i]);
            }
        }
        return s.toString();
    }
    
    /**
     * Returns a string formatting an array of class objects as
     * comma-separated list of (Java) user type names.
     */
    public static String toString(String prefix, Class<?>[] cls) {
        final StringBuilder s = new StringBuilder();
        if (cls != null && cls.length > 0) {
            String n = TypeHelper.userTypeName(cls[0].getName());
            s.append(prefix == null ? "" : prefix).append(n);
            for (int i = 1; i < cls.length; i++) {
                n = TypeHelper.userTypeName(cls[i].getName());
                s.append(", ").append(n);
            }
        }
        return s.toString();
    }
    
    /**
     * Returns an exhaustive string description of a <code>Field</code>
     * presenting types as (Java) user type names.
     */
    public static String toString(Field field, Object value) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(field.getModifiers())).append(" ");
        s.append(TypeHelper.userTypeName(field.getType().getName()));
        s.append(" ");
        s.append(field.getName());
        s.append(value == null ? "" : " = " + value);
        return s.toString();
    }

    /**
     * Returns an combined string description of a field declaration.
     */
    public static String toString(int mods, String type, String name,
                                  String value) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(mods)).append(" ");
        s.append(type).append(" ");
        s.append(name);
        s.append(value == null ? "" : " = " + value);
        return s.toString();
    }

    /**
     * Returns an exhaustive string description of a
     * <code>Constructor</code> presenting types as (Java) user type names.

     */
    public static String toString(Constructor<?> ctor) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(ctor.getModifiers())).append(" ");
        s.append(ctor.getName()).append("(");
        s.append(toString("", ctor.getParameterTypes())).append(")");
        s.append(toString(" throws ", ctor.getExceptionTypes()));
        return s.toString();
    }

    /**
     * Returns an exhaustive string description of a <code>Method</code>
     * presenting types as (Java) user type names.
     */
    public static String toString(Method method) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(method.getModifiers())).append(" ");
        final String r = method.getReturnType().getName();
        s.append(TypeHelper.userTypeName(r)).append(" ");
        s.append(method.getName()).append("(");
        s.append(toString("", method.getParameterTypes())).append(")");
        s.append(toString(" throws ", method.getExceptionTypes()));
        if (method.getDeclaringClass().isAnnotation() && method.getDefaultValue() != null) {
            s.append(" default \"" + method.getDefaultValue() + "\"");
        }
        return s.toString();
    }

    /**
     * Returns an combined string description of a constructor or
     * method declaration.
     */
    public static String toString(int mods, String result, String name,
                                  String[] params, String[] excepts) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(mods)).append(" ");
        s.append(result == null ? "" : result).append(" ");
        s.append(name).append("(").append(toString("", params)).append(")");
        s.append(toString(" throws ", excepts));
        return s.toString();
    }

    /**
     * Returns an exhaustive string description of a <code>Class</code>
     * presenting types as (Java) user type names.
     */
    public static String toString(Class<?> cls) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(cls.getModifiers()));
        s.append(cls.isInterface() ? " " : " class ").append(cls.getName());
        final Class<?> superc = cls.getSuperclass();
        final Class<?>[] interf = cls.getInterfaces();
        if (cls.isInterface()) {
            s.append(toString(" extends ", interf));
        } else {
            s.append(superc == null ? "" : " extends " + superc.getName());
            s.append(toString(" implements ", interf));
        }
        return s.toString();
    }

    /**
     * Returns an combined string description of a class header declaration.
     */
    public static String toString(int mods, String name,
                                  String[] ext, String[] impl) {
        final StringBuilder s = new StringBuilder();
        s.append(Modifier.toString(mods));
        final boolean isInterface = ((mods & Modifier.INTERFACE) != 0);
        s.append(isInterface ? " " : " class ").append(name);
        s.append(toString(" extends ", ext));
        s.append(toString(" implements ", impl));
        return s.toString();
    }

    /**
     * Returns an exhaustive string description of a <code>Member</code>
     * presenting types as (Java) user type names.
     */
    public static String toString(Member member) {
        final String s;
        if (member instanceof Field) {
            s = toString((Field)member, null);
        } else if (member instanceof Constructor) {
            s = toString((Constructor<?>)member);
        } else if (member instanceof Method) {
            s = toString((Method)member);
        } else {
            s = null;
        }
        return s;
    }
}
