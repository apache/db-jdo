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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a utility class to support equality checking. An EqualityHelper
 * object defines the context of a deepEquals call, because it keeps track
 * of objects that have already been processed. This avoids endless
 * recursion when comparing cyclic data structures for deep equality.
 * <p>
 * Furthermore, EqualityHelper provides convenience methods for checking
 * deep equality, equality and close enough (for floating point values). 
 *
 * @author Michael Bouschen
 * @since 1.1 
 */
public class EqualityHelper {
    
    /** Logger */
    protected Log logger = 
        LogFactory.getFactory().getInstance("org.apache.jdo.tck");

    /** true if debug logging in enabled. */
    protected boolean debug = logger.isDebugEnabled();
    
    /** Used when comparing float values close enough. */
    public static final float FLOAT_EPSILON = (float)Math.pow(2.0, -20.0);

    /** Used when comparing double values close enough. */
    public static final double DOUBLE_EPSILON = Math.pow(2.0, -52.0);

    /** Message for null vs. not null */
    static final String MSG_ME_NULL = "\nExpected null, actual not null";
    
    /** Message for not null vs. null */
    static final String MSG_OTHER_NULL = "\nExpected not null, actual null";
    
    /** Message for incompatible types */
    static final String MSG_INCOMPATIBLE_TYPES = "\nIncompatible types for comparison";
    
    /** Message for wrong class for counting via iterator */
    static final String MSG_PARAMETER_MUST_BE_COLLECTION_OR_MAP =
            "Parameter must be a Collection or Map.";
    
    /** Comparator used in method deepEquals comparing maps. This comparator
     * is used to order Maps whose keys are Comparable so the entries can be 
     * compared using deepCompareFields.
     */
    private static Comparator<Map.Entry<?, ?>> entryKeyComparator = (o1, o2) -> {
        Object key1 = o1.getKey();
        Object key2 = o2.getKey();
        return ((Comparable<Object>)key1).compareTo(key2);
    };
        
    /** 
     * Utility counter for maps and collections
     */
    static int countIterator(Object o) {
        int result = 0;
        Iterator<?> it;
        if (o instanceof Collection) {
            it = ((Collection<?>)o).iterator();
        } else if (o instanceof Map) {
            it = ((Map<?,?>)o).entrySet().iterator();
        } else {
            throw new ClassCastException(MSG_PARAMETER_MUST_BE_COLLECTION_OR_MAP);
        }
        while (it.hasNext()) {
            it.next();
            result++;
        }
        return result;
    }
    /** Comparator used in method deepEquals comparing maps of
     * DeepEquality.
     */
    private static class DeepEqualityEntryKeyComparator<K,V>
    implements Comparator<Map.Entry<K,V>> {
        Comparator<K> comparator;
        DeepEqualityEntryKeyComparator(Comparator<K> comp) {
            this.comparator = comp;
        }
        public int compare(Map.Entry<K,V>  o1, Map.Entry<K,V>  o2) {
            K key1 = o1.getKey();
            K key2 = o2.getKey();
            return comparator.compare(key1, key2);
        }
    }

    /** Collection of instances that have been processed already in the
     * context of this EqualityHelper instance 
     */
    private Collection<Object> processed = new HashSet<>();
    
    /** StringBuffer of logged differences.
     */
    StringBuffer unequalBuffer = new StringBuffer();
    
    /**
     * Context is a stack of navigational paths.
     */
    Stack<String> contextStack = new Stack<>();

    // Methods to support keeping track of instances that have been
    // processed already.

    /** Returns <code>true</code> if the specified instance has been
     * processed already in the context of this
     * <code>EqualityHelper</code>. 
     * @param obj the instance to be checked.
     * @return <code>true</code> if the instance has been processed
     * already; <code>false</code> otherwise.
     */
    public boolean isProcessed(Object obj) {
        return processed.contains(obj);
    }
    
    /** Marks the specified instance as processed in the context of this
     * <code>EqualityHelper</code>. This means the instance is added to the 
     * collection of processed instances. 
     * @param obj instance marked as processed
     */
    public void markProcessed(Object obj) {
        processed.add(obj);
    }
    
    /** Clears the collection of processed instances of this
     * <code>EqualityHelper</code>. No instance is marked as processed in
     * the context of this <code>EqualityHelper</code> after calling this
     * method. 
     */
    public void clearProcessed() {
        processed.clear();
    }

    // Deep equality support methods

    /** Returns <code>true</code> if the specified instances are "deep
     * equal". 
     * @param me one object to be tested for deep equality
     * @param other the other object to be tested for deep equality
     * @return <code>true</code> if the objects are deep equal.
     */
    public boolean deepEquals(DeepEquality me, DeepEquality other) {
        if (me == other)
            return true;
        if ((me == null) || (other == null))
            return false;
        if (isProcessed(me))
            return true;
        markProcessed(me);
        return me.deepCompareFields(other, this);
    }

    /** Returns <code>true</code> if the specified instances are "deep
     * equal". The method compares the two instances via the deepEquals
     * method if they implement DeepEquals; compares the two instances via 
     * deepEquals if they implement Collection or Map, and otherwise
     * compares the instances using equals. 
     * @param me one object to be tested for deep equality
     * @param other the other object to be tested for deep equality
     * @return <code>true</code> if the objects are deep equal.
     */
    public  boolean deepEquals(Object me, Object other) {
        if (me == other)
            return true;
        if ((me == null) || (other == null))
            return false;
        if ((me instanceof DeepEquality) && (other instanceof DeepEquality))
            return deepEquals((DeepEquality)me, (DeepEquality)other);
        if ((me instanceof Collection<?>) && (other instanceof Collection<?>))
            return deepEquals((Collection)me, (Collection)other);
        if ((me instanceof Map<?, ?>) && (other instanceof Map<?, ?>))
            return deepEquals((Map)me, (Map)other);
        return me.equals(other);
    }

    /** Returns <code>true</code> if the specified collections are "deep 
     * equal". Two collections are deep equal, if they have the same size
     * and their corresponding elements are deep equal after sorting 
     * using the natural ordering of the elements. The method throws a
     * <code>ClassCastException</code> if the elements are not Comparable
     * or if they are not mutually comparable.
     * @param mine one collection to be tested for deep equality
     * @param other the other collection to be tested for deep equality
     * @return <code>true</code> if the collections are deep equal.
     * @throws ClassCastException if the collections contain elements that
     * are not mutually comparable.
     */
    public <T extends Comparable<T>> boolean deepEquals(Collection<T> mine, Collection<T> other) {
        if (mine == other)
            return true;
        if ((mine == null) || (other == null))
            return false;

        // Return false, if the size differs
        if (mine.size() != other.size())
            return false;

        if (mine.size() == 0) 
            return true;

        // Now check the elements 
        List<T> myList = new ArrayList<>(mine);
        Collections.sort(myList);
        List<T> otherList = new ArrayList<>(other);
        /* Any collection of elements to be compared must implement Comparator
         * to avoid the other side having to implement Comparable. */
        Comparator<T> comparator =
                (Comparator<T>)myList.get(0);
        Collections.sort(otherList, comparator);
        for (int i = 0; i < myList.size(); i++) {
            if (!deepEquals(myList.get(i), otherList.get(i)))
                return false;
        }
        return true;
    }

    /** Returns <code>true</code> if the specified maps are "deep
     * equal". Two maps are deep equal, if they have the same size and the 
     * values of the corresponding keys compare deep equal. The method
     * throws a <code>ClassCastException</code> if keys or values are not
     * Comparable or if they are not mutually comparable.
     * @param mine one map to be tested for deep equality
     * @param other the other map to be tested for deep equality
     * @return <code>true</code> if the maps are deep equal.
     * @throws ClassCastException if the maps contain keys or values that 
     * are not mutually comparable.
     */
    public <K,V> boolean deepEquals(Map<K,V> mine, Map<K,V> other) {
        if (mine == other)
            return true;
        if ((mine == null) || (other == null))
            return false;

        // Return false, if the size differs
        if (mine.size() != other.size())
            return false;

        if (mine.size() == 0) 
            return true;

        // Now check the elements 
        List<Map.Entry<K,V>> myList = new ArrayList<>(mine.entrySet());
        Collections.sort(myList, entryKeyComparator);
        List<Map.Entry<K,V>> otherList = new ArrayList<>(other.entrySet());
        /* Any collection of elements to be compared must implement Comparator
         * to avoid the other side having to implement Comparable. */
        Comparator<K> comparator =
                (Comparator<K>)((Map.Entry<V, K>)myList.get(0)).getKey();
        Collections.sort(otherList, 
                new DeepEqualityEntryKeyComparator<>(comparator));

        for (int i = 0; i < myList.size(); i++) {
            Map.Entry<K,V> entry1 = myList.get(i);
            Map.Entry<K,V> entry2 = otherList.get(i);
            // compare the keys
            if (!deepEquals(entry1.getKey(), entry2.getKey()))
                return false;
            // compare the values
            if (!deepEquals(entry1.getValue(), entry2.getValue()))
                return false;
        }
        return true;
    }

    // Shallow equality support methods

    /** Returns <code>true</code> if the specified collections are "shallow
     * equal". Two collections are shallow equal, if they have the same size 
     * and their corresponding elements are equal after sorting using the
     * natural ordering. 
     * @param mine one collection to be tested for shallow equality
     * @param other the other collection to be tested for shallow equality
     * @return <code>true</code> if the collections are deep equal.
     */
    public <T extends Comparable<T>> boolean shallowEquals(Collection<T> mine, Collection<T> other) {
        if (mine == other)
            return true;
        if ((mine == null) || (other == null))
            return false;

        // Return false, if the size differs
        if (mine.size() != other.size())
            return false;

        if (mine.size() == 0) 
            return true;

        // Now check the elements 
        List<T> myList = new ArrayList<>(mine);
        Collections.sort(myList);
        List<T> otherList = new ArrayList<>(other);
        /* Any collection of elements to be compared must implement Comparator
         * to avoid the other side having to implement Comparable. */
        Comparator<T> comparator =
                (Comparator<T>)myList.get(0);
        Collections.sort(otherList, comparator);
        return myList.equals(otherList);
    }

    // Deep equality support methods with logging
    
    public String getUnequalBuffer() {
        return unequalBuffer.toString();
    }
    
    /**
     * Context is nested via navigation through relationships.
     */
    void pushContext(String ctx) {
        contextStack.push(ctx);
    }
    
    String popContext() {
        return contextStack.pop();
    }
    
    /** Log differences between objects that don't compare equal.
     * @param o1 the first object
     * @param o2 the second object
     * @param where the field where the objects are found
     */
    void logUnequal(Object o1, Object o2, String where) {
        unequalBuffer.append("Context: ");
        Iterator<String> it = contextStack.iterator();
        StringBuffer offset = new StringBuffer("\n");
        while (it.hasNext()) {
            unequalBuffer.append(it.next());
            unequalBuffer.append("-> ");
            offset.append("    ");
        }
        unequalBuffer.append(where);
        unequalBuffer.append(offset.toString());
        unequalBuffer.append("expected '");
        unequalBuffer.append(o1);
        unequalBuffer.append("'");
        unequalBuffer.append(offset.toString());
        unequalBuffer.append("  actual '");
        unequalBuffer.append(o2);
        unequalBuffer.append("'\n");
    }

    /** Returns <code>true</code> if the specified instances are "deep
     * equal". If unequal, log the location of the inequality.
     * @param me one object to be tested for deep equality
     * @param other the other object to be tested for deep equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the objects are deep equal.
     */
    public boolean deepEquals(DeepEquality me, Object other,
            String where) {
        if (me == other)
            return true;
        if (me == null) {
            logUnequal(me, other, where + MSG_ME_NULL);
            return false;
        }
        if (other == null) {
            logUnequal(me, other, where + MSG_OTHER_NULL);
            return false;
        }
        if (isProcessed(me))
            return true;
        markProcessed(me);
        pushContext(where);
        boolean result = true;
        if (!me.deepCompareFields(other, this)) {
            //logUnequal(me, other, where);
            result = false;
        }
        popContext();
        return result;
    }

    /** Returns <code>true</code> if the specified instances are "deep
     * equal". The method compares the two instances via the deepEquals
     * method if they implement DeepEquals; compares the two instances via 
     * deepEquals if they implement Collection or Map, and otherwise
     * compares the instances using equals. 
     * @param me one object to be tested for deep equality
     * @param other the other object to be tested for deep equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the objects are deep equal.
     */
    public boolean deepEquals(Object me, Object other, String where) {
        if (me == other)
            return true;
        if (me == null) {
            logUnequal(me, other, where + MSG_ME_NULL);
            return false;
        }
        if (other == null) {
            logUnequal(me, other, where + MSG_OTHER_NULL);
            return false;
        }
        if (me instanceof DeepEquality) {
            return deepEquals((DeepEquality)me, other, where);
        } else if ((me instanceof Collection<?>) && (other instanceof Collection<?>)) {
            return deepEqualsCollection((Collection)me, (Collection)other, where);
        } else if ((me instanceof Map<?, ?>) && (other instanceof Map<?, ?>)) {
            return deepEqualsMap((Map)me, (Map)other, where);
        } else {
            return equals(me, other, where);
        }
    }

    /** Returns <code>true</code> if the specified collections are "deep 
     * equal". Two collections are deep equal, if they have the same size
     * and their corresponding elements are deep equal after sorting 
     * using the natural ordering of the elements. The method throws a
     * <code>ClassCastException</code> if the elements are not Comparable
     * or if they are not mutually comparable.
     * @param me one collection to be tested for deep equality
     * @param other the other collection to be tested for deep equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the collections are deep equal.
     * @throws ClassCastException if the collections contain elements that
     * are not mutually comparable.
     */
    public <S extends Comparable<S>, T> boolean deepEqualsCollection(Collection<S> me, Collection<T> other,
            String where) {
        if (me == other)
            return true;
        if (me == null) {
            logUnequal(me, other, where + MSG_ME_NULL);
            return false;
        }
        if (other == null) {
            logUnequal(me, other, where + MSG_OTHER_NULL);
            return false;
        }
        int mysize = me.size();
        int othersize = other.size();
        // Return false, if the size differs
        if (mysize != othersize) {
            int count = countIterator(other); 
            logUnequal(me, other, 
                    where + "\nSize mismatch: expected size= " + me.size() + 
                    ", original size= " + othersize +
                    ", current size= " + other.size() +
                    ", counted size= " + count);
            return false;
        }
        
        if (mysize == 0) 
            return true;
        
        // Now check each element for equality or deep equality
        List<S> myList = new ArrayList<>(me);
        // Use the natural ordering of me; must implement Comparable
        Collections.sort(myList);
        List<T> otherList = new ArrayList<>(other);
        /* Any collection of elements to be compared must implement Comparator
         * to avoid the other side having to implement Comparable. */
        Comparator<T> comparator =
                (Comparator<T>)myList.get(0);
        otherList.sort(comparator);
        boolean result = true;
        for (int i = 0; i < myList.size(); i++) {
            DeepEquality o1 = (DeepEquality)myList.get(i);
            Object o2 = otherList.get(i);
            /* Compare corresponding elements of the ordered list. */
            if (!deepEquals(o1, o2, where + "[" + i + "]")) {
                result = false;
            }
        }
        return result;
    }

    /** Returns <code>true</code> if the specified maps are "deep
     * equal". Two maps are deep equal, if they have the same size and the 
     * values of the corresponding keys compare deep equal. The method
     * throws a <code>ClassCastException</code> if keys or values are not
     * Comparable or if they are not mutually comparable.
     * @param me one map to be tested for deep equality
     * @param other the other map to be tested for deep equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the maps are deep equal.
     * @throws ClassCastException if the maps contain keys or values that 
     * are not mutually comparable.
     */
    public <K,V> boolean deepEqualsMap(Map<K,V> me, Map<K,V> other, String where) {
        if (me == other)
            return true;
        if (me == null) {
            logUnequal(me, other, where + MSG_ME_NULL);
            return false;
        }
        if (other == null) {
            logUnequal(me, other, where + MSG_OTHER_NULL);
            return false;
        }

        // Return false, if the size differs
        int mysize = me.size();
        int othersize = other.size();
        if (mysize != othersize) {
            int count = countIterator(other);
            logUnequal(me, other, 
                    where + "\nSize mismatch: expected size= " + me.size() + 
                    ", original size= " + othersize +
                    ", current size= " + other.size() +
                    ", counted size= " + count);
            return false;
        }

        if (mysize == 0) 
            return true;
        
        // Now check the elements 
        List<Map.Entry<K,V>> myList = new ArrayList<>(me.entrySet());
        // Use the natural ordering of me; must implement Comparable
        Collections.sort(myList, entryKeyComparator);
        List<Map.Entry<K,V>> otherList = new ArrayList<>(other.entrySet());
        Comparator<Map.Entry<?, ?>> comparator = entryKeyComparator;
        // Use the Comparator to avoid the other side implementing Comparable
        Object key = myList.get(0).getKey();
        if (key instanceof Comparator) {
            comparator = new DeepEqualityEntryKeyComparator((Comparator)key);
        }
        Collections.sort(otherList, comparator);

        boolean result = true;
        for (int i = 0; i < myList.size(); i++) {
            Map.Entry<K,V> entry1 = myList.get(i);
            Object key1 = entry1.getKey();
            Object value1 = entry1.getValue();
            Map.Entry<K,V> entry2 = otherList.get(i);
            K key2 = entry2.getKey();
            V value2 = entry2.getValue();
            // compare the keys
            if (!deepEquals(key1, key2, where + "[" + i + "].key")) {
                result = false;
            }
            // compare the values
            if (!deepEquals(value1, value2, where + "[" + i + "].value")) {
                result = false;
            }
        }
        return result;
    }

    // Shallow equality support methods

    /** Returns <code>true</code> if the specified collections are "shallow
     * equal". Two collections are shallow equal, if they have the same size 
     * and their corresponding elements are equal after sorting using the
     * natural ordering. 
     * @param me one collection to be tested for shallow equality
     * @param other the other collection to be tested for shallow equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the collections are deep equal.
     */
    public <T extends Comparable<T>> boolean shallowEquals(Collection<T> me, Collection<T> other,
            String where) {
        if (me == other)
            return true;
        if (me == null) {
            logUnequal(me, other, where + MSG_ME_NULL);
            return false;
        }
        if (other == null) {
            logUnequal(me, other, where + MSG_OTHER_NULL);
            return false;
        }

        // Return false, if the size differs
        int mysize = me.size();
        int othersize = other.size();
        if (mysize != othersize) {
            // debug size...
            Iterator<T> it = other.iterator();
            int count = 0; 
            while (it.hasNext()) { it.next(); ++count; }
                logUnequal(me, other, 
                    where + "\nSize mismatch: expected size= " + me.size() + 
                    ", original size= " + othersize +
                    ", current size= " + other.size() +
                    ", counted size= " + count);
            return false;
        }

        if (me.size() == 0) 
            return true;

        // Now check the elements 
        List<T> myList = new ArrayList<>(me);
        Collections.sort(myList);
        List<T> otherList = new ArrayList<>(other);
        /* Any collection of elements to be compared must implement Comparator
         * to avoid the other side having to implement Comparable. */
        Comparator<T> comparator =
                (Comparator<T>)myList.get(0);
        Collections.sort(otherList, comparator);
        boolean result = myList.equals(otherList);
        if (!result) 
            logUnequal(me,  other, 
                    where + "\nCollections do not compare equal");
        return result;
    }

    // Equality support methods

    /** Returns <code>true</code> if the specified objects are equal. 
     * This is a helper method checking for identical and <code>null</code>
     * objects before delegating to the regular equals method.
     * @param o1 one object to be tested for equality
     * @param o2 the other object to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the specified objects are equal.
     */
    public boolean equals(Object o1, Object o2, String where) {
        if (o1 == o2)
            return true;
        if (o1 == null) {
            logUnequal(o1, o2, where + MSG_ME_NULL);
            return false;
        }
        if (o2 == null) {
            logUnequal(o1, o2, where + MSG_OTHER_NULL);
            return false;
        }
        if (!o1.equals(o2)) {
            logUnequal(o1, o2, where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if compare called for the specified
     * BigDecimal objects returns <code>0</code>. Please note, two
     * BigDecimal instances are not equal (using equals) if their scale
     * differs, and this method compares the values, ignoring scale. 
     * @param o1 one object to be tested for equality
     * @param o2 the other object to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the specified BigDecimal objects are
     * equal.
     */
    public boolean equals(BigDecimal o1, BigDecimal o2, String where) {
        if (o1 == o2)
            return true;
        if ((o1 == null) || (o2 == null)) {
            logUnequal(o1, o2, where);
            return false;
        }
        boolean result = o1.equals(o2);
        if (!result) 
            logUnequal(o1, o2, where);
        return result;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(boolean p1, boolean p2, String where) {
        if (p1 != p2) {
            logUnequal(Boolean.toString(p1), Boolean.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(char p1, char p2, String where) {
        if (p1 != p2) {
            logUnequal(Character.toString(p1), Character.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(byte p1, byte p2, String where) {
        if (p1 != p2) {
            logUnequal(Byte.toString(p1), Byte.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(short p1, short p2, String where) {
        if (p1 != p2) {
            logUnequal(Short.toString(p1), Short.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(int p1, int p2, String where) {
        if (p1 != p2) {
            logUnequal(Integer.toString(p1), Integer.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code>, if two parameters are equal.  
     * @param p1 one to be tested for equality
     * @param p2 the other to be tested for equality
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the parameters are equal.
     */
    public boolean equals(long p1, long p2, String where) {
        if (p1 != p2) {
            logUnequal(Long.toString(p1), Long.toString(p2), where);
            return false;
        }
        return true;
    }

    /** Returns <code>true</code> if the specified objects are equal. 
     * This is a helper method checking for identical and <code>null</code>
     * objects before delegating to the regular equals method.
     * @param o1 one object to be tested for equality
     * @param o2 the other object to be tested for equality
     * @return <code>true</code> if the specified objects are equal.
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }
    
    // Methods to support "close enough" comparison

    /** Returns <code>true</code> if the specified objects are close
     * enough to be considered to be equal for a deep equals
     * comparison. The method delegates to the method taking double
     * or float values if the specified objects are Float or Double
     * wrappers. Otherwise it delegates to equals. 
     * @param o1 one object to be tested for close enough 
     * @param o2 the other object to be tested for close enough 
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the specified values are close enough.
     */
    public boolean closeEnough(Object o1, Object o2, String where) {
        if (o1 == o2)
            return true;
        if ((o1 == null) || (o2 == null)) {
            logUnequal(o1, o2, where);
            return false;
        }
        boolean result = true;
        if ((o1 instanceof Double) && (o2 instanceof Double)) {
            return closeEnough(((Double)o1).doubleValue(), 
                               ((Double)o2).doubleValue(), where);
        }
        else if ((o1 instanceof Float) && (o2 instanceof Float)) {
            return closeEnough(((Float)o1).floatValue(), 
                               ((Float)o2).floatValue(), where); 
        }
        else if ((o1 instanceof BigDecimal) && (o2 instanceof BigDecimal)) {
            return ((BigDecimal)o1).compareTo((BigDecimal)o2) == 0;
        }
        else {
            result = o1.equals(o2);
        }
        if (!result) 
            logUnequal(o1, o2, where);
        return result;
    }

    /** Returns <code>true</code> if the specified float values are close
     * enough to be considered to be equal for a deep equals
     * comparison. Floating point values are not exact, so comparing them
     * using <code>==</code> might not return useful results. This method
     * checks that both double values are within some percent of each
     * other. 
     * @param d1 one double to be tested for close enough 
     * @param d2 the other double to be tested for close enough 
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the specified values are close enough.
     */
    public boolean closeEnough(double d1, double d2, String where) {
        if (d1 == d2)
            return true;

        double diff = Math.abs(d1 - d2);
        boolean result = diff < Math.abs((d1 + d2) * DOUBLE_EPSILON);
        if (!result) 
            logUnequal(Double.toString(d1), Double.toString(d2), where);
        return result;
    }

    /**
     * Returns <code>true</code> if the specified float values are close
     * enough to be considered to be equal for a deep equals
     * comparison. Floating point values are not exact, so comparing them 
     * using <code>==</code> might not return useful results. This method
     * checks that both float values are within some percent of each
     * other. 
     * @param f1 one float to be tested for close enough 
     * @param f2 the other float to be tested for close enough 
     * @param where the location of the inequality (provided by the caller)
     * @return <code>true</code> if the specified values are close enough.
     */
    public boolean closeEnough(float f1, float f2, String where) {
        if (f1 == f2)
            return true;

        float diff = Math.abs(f1 - f2);
        boolean result = diff < Math.abs((f1 + f2) * FLOAT_EPSILON);
        if (!result) 
            logUnequal(Float.toString(f1), Float.toString(f2), where);
        return result;
    }

    // Methods to support compare methods as specified in Comparator 
    
    /** 
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second. 
     * @param l1 the first long to be compared
     * @param l2 the second long to be compared
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second. 
     */
    public static int compare (long l1, long l2) {
        return (Long.compare(l1, l2));
    }
}
