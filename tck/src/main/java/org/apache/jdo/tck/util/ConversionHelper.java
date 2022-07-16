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

package org.apache.jdo.tck.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.jdo.JDOFatalException;

/**
 *  Provides consersion functionality.
 */
public class ConversionHelper {

    /**
     * Converts the given <code>value</code> to a {@link java.util.Date}.
     * @param pattern the pattern
     * @param timezone the timezone
     * @param locale the locale
     * @param value the value
     * @return the date
     * @throws JDOFatalException if the conversion fails
     */
    public static Date toUtilDate(String pattern, 
            String timezone, Locale locale, String value) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            throw new JDOFatalException("", e);
        }
    }

    public static Date toUtilDate(String pattern, Locale locale, String value) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            throw new JDOFatalException("", e);
        }
    }

    /**
     * Converts the given array into a {@link Map}. 
     * The first dimension represents the map entries,
     * the second dimension holds the keys and values, e.g.
     * { {"key1", "value1"}, {"key2", "value2"} }.
     * @param array the array
     * @return the map
     */
    public static Map<Object, Object> arrayToMap(Object[][] array) {
        Map<Object, Object> map = new HashMap<>();
        for (Object[] objects : array) {
            map.put(objects[0], objects[1]);
        }
        return map;
    }
    
    /**
     * Returns a collection containing all elements 
     * in the given <code>collection</code>.
     * Recursively converts all elements of type <code>Object[]</code>
     * in the given <code>collection</code> to collections 
     * in the returned collection.
     * @param collection the collection
     * @return the converted collection
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> convertsElementsOfTypeObjectArray(Collection<T> collection) {
        Collection<T>  result = new ArrayList<>();
        for (T t : collection) {
            T current = (T) convertObjectArrayElements(t);
            result.add(current);
        }
        return result;
    }

    /**
     * Returns a map containing all entries 
     * in the given <code>map</code>.
     * Recursively converts all entries having keys and/or values
     * of type <code>Object[]</code> in the given <code>map</code> to collections 
     * in the returned map.
     * @param map the map
     * @return the converted map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> convertsElementsOfTypeObjectArray(Map<K, V> map) {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Object key = convertObjectArrayElements(entry.getKey());
            Object value = convertObjectArrayElements(entry.getValue());
            result.put((K) key, (V) value);
        }
        return result;
    }

    /**
     * Recursively converts all elements of type <code>Object[]</code>
     * Collection, or Map.
     * @see #convertObjectArrayElements(Object[])
     * @see #convertsElementsOfTypeObjectArray(Collection)
     * @see #convertsElementsOfTypeObjectArray(Map)
     * @param object the object to convert
     * @return the converted parameter
     */
    public static Object convertObjectArrayElements(Object object) {
        Object result;
        if (object instanceof Object[]) {
            result = Arrays.asList(
                    convertObjectArrayElements((Object[])object));
        } else if (object instanceof Collection) {
            result = convertsElementsOfTypeObjectArray((Collection<?>)object);
        } else if (object instanceof Map) {
            result = convertsElementsOfTypeObjectArray((Map<?, ?>)object);
        } else {
            result = object;
        }
        return result;
    }
    
    /**
     * Recursively converts all elements of type <code>Object[]</code>
     * in the given <code>array</code> and retuns that array.
     * @param array the array
     * @return the converted array
     */
    public static Object[] convertObjectArrayElements(Object[] array) {
        for (int i = 0; i < array.length; i++ ) {
            array[i] = convertObjectArrayElements(array[i]);
        }
        return array;
    }

    /**
     * Converts the given <code>array</code> to a string array.
     * @param array the object array
     * @return the string array
     */
    public static String[] toStringArray(Object[] array) {
        String[] result = new String[array.length];
        System.arraycopy(array, 0, result, 0, result.length);
        return result;
    }

}
