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

package org.apache.jdo.tck.util.signature;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

/**
 * A helper class for translating between Java user type names and
 * reflection type names.
 */
public class TypeHelper {
    /** Error message for format errors with a user type name. */
    static private final String MSG_ILLEGAL_USR_TYPE
        = "illegal user type name: ";
    
    /** Error message for format errors with a reflection type name. */
    static private final String MSG_ILLEGAL_RFL_TYPE
        = "illegal reflection type name: ";
    
    /** Throws an IllegalArgumentException if a given condition is violated. */
    static private void check(boolean cond, String msg) {
        if (!cond) throw new IllegalArgumentException(msg);
    }

    /** Maps primitive reflection type names to (Java) user names. */
    static private final Map userTypeNames = new HashMap();

    /** Maps primitive (Java) user type names to reflection names. */
    static private final Map reflectionTypeNames = new HashMap();

    /** Maps primitive type names to class objects. */
    static private final Map primitiveClasses = new HashMap();

    // initializes the type maps
    static {
        userTypeNames.put("B", "byte");
        userTypeNames.put("C", "char");
        userTypeNames.put("D", "double");
        userTypeNames.put("F", "float");
        userTypeNames.put("I", "int");
        userTypeNames.put("J", "long");
        userTypeNames.put("S", "short");
        userTypeNames.put("Z", "boolean");
        userTypeNames.put("V", "void");

        for (Iterator i = userTypeNames.entrySet().iterator();
             i.hasNext();) {
            final Map.Entry e = (Map.Entry)i.next();
            reflectionTypeNames.put(e.getValue(), e.getKey());
        }

        primitiveClasses.put("byte", byte.class);
        primitiveClasses.put("char", char.class);
        primitiveClasses.put("double", double.class);
        primitiveClasses.put("float", float.class);
        primitiveClasses.put("int", int.class);
        primitiveClasses.put("long", long.class);
        primitiveClasses.put("short", short.class);
        primitiveClasses.put("boolean", boolean.class);
        primitiveClasses.put("void", void.class);
    }

    /** Returns the (Java) user name for a reflection type name. */
    static public String userTypeName(String name) {
        check(name != null, MSG_ILLEGAL_RFL_TYPE + name);

        // count array dimensions from start
        final int n = name.length();
        check(n > 0, MSG_ILLEGAL_RFL_TYPE + name);
        int i = 0;
        final StringBuffer sb = new StringBuffer();
        while (name.charAt(i) == '[') {
            sb.append("[]");
            i++;
            check(i < n, MSG_ILLEGAL_RFL_TYPE + name);
        }

        // no translation of primitive type names if not an array type
        if (i == 0) {
            return name;
        }

        // translate and recompose name
        final String s;
        if (name.charAt(i) == 'L') {
            check(name.endsWith(";"), MSG_ILLEGAL_RFL_TYPE + name);
            s = name.substring(i + 1, n - 1);
        } else {
            s = (String)userTypeNames.get(name.substring(i));
            check(s != null, MSG_ILLEGAL_RFL_TYPE + name);
        }
        return (s + sb.toString());
    }
    
    /** Returns the (Java) user names for reflection type names. */
    static public String[] userTypeNames(String[] names) {
        final String[] u = new String[names.length];
        for (int i = names.length - 1; i >= 0; i--) {
            u[i] = userTypeName(names[i]);
        }
        return u;
    }
    
    /** Returns the reflection name for a (Java) user type name. */
    static public String reflectionTypeName(String name) {
        check(name != null, MSG_ILLEGAL_USR_TYPE + name);

        // count array dimensions from end
        final int n = name.length();
        check(n > 0, MSG_ILLEGAL_USR_TYPE + name);
        int i = n - 1;
        final StringBuffer sb = new StringBuffer();
        while (name.charAt(i) == ']') {
            i--;
            check(name.charAt(i) == '[', MSG_ILLEGAL_USR_TYPE + name);
            sb.append("[");
            i--;
            check(i >= 0, MSG_ILLEGAL_USR_TYPE + name);
        }

        // no translation of primitive type names if not an array type
        if (++i == n) {
            return name;
        }

        // translate and recompose name
        final String s = name.substring(0, i);
        final String p = (String)reflectionTypeNames.get(s);
        return sb.append(p != null ? p : "L" + s + ";").toString();
    }

    /** Returns the (Java) user names for reflection type names. */
    static public String[] reflectionTypeNames(String[] names) {
        final String[] r = new String[names.length];
        for (int i = names.length - 1; i >= 0; i--) {
            r[i] = reflectionTypeName(names[i]);
        }
        return r;
    }
    
    /**
     * Returns the class object for a primitive type name, or
     * <code>null</code> if the name does not denote a primitive type
     * (class objects of primitive types cannot be loaded with reflection).
     */
    static public Class primitiveClass(String name) {
        return (Class)primitiveClasses.get(name);
    }

    /** Tests if a name denotes a primitive type. */
    static public boolean isPrimitive(String name) {
        return primitiveClasses.containsKey(name);
    }

    /** Returns the component type name of a (Java) user type name. */
    static public String componentUserTypeName(String name) {
        check(name != null, MSG_ILLEGAL_USR_TYPE + name);
        final int n = name.length();
        check(n > 0, MSG_ILLEGAL_USR_TYPE + name);
        final int i = name.indexOf('[');
        if (i >= 0) {
            check(i > 0, MSG_ILLEGAL_USR_TYPE + name);
            name = name.substring(0, i);
        }
        return name;
    }

    /**
     * Returns the <code>java.lang.</code>-qualified name for a given
     * unqualified (Java) user type name.
     */
    static public String qualifiedUserTypeName(String name) {
        final String c = componentUserTypeName(name);
        return ((isPrimitive(c) || c.indexOf('.') >= 0)
                ? name : "java.lang." + name);
    }
    
    /**
     * Returns the <code>java.lang.</code>-qualified names for given
     * unqualified (Java) user type names.
     */
    static public String[] qualifiedUserTypeNames(String[] names) {
        final String[] q = new String[names.length];
        for (int i = names.length - 1; i >= 0; i--) {
            q[i] = qualifiedUserTypeName(names[i]);
        }
        return q;
    }

    /**
     * Compares a type name with a class objects for equality in the name.
     */
    static public boolean isNameMatch(String userTypeName, Class cls) {
        final String c = (cls == null ? null : userTypeName(cls.getName()));
        return (userTypeName == null ? (c == null) : userTypeName.equals(c));
    }

    /**
     * Compares an array of type names with an array of class objects
     * for set-equality in the names (i.e., ignoring order).
     */
    static public boolean isNameMatch(String[] userTypeName, Class[] cls) {
        final Set s = new HashSet(Arrays.asList(userTypeName));
        for (int i = cls.length - 1; i >= 0; i--) {
            if (!s.remove(userTypeName(cls[i].getName()))) {
                return false;
            }
        }
        return s.isEmpty();
    }
}
