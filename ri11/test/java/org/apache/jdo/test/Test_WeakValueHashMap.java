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

/*
 * TestWeakHashMap.java
 *
 */

package org.apache.jdo.test;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.util.WeakValueHashMap;

/**
 *
 * @author Markus Fuchs
 */
public class Test_WeakValueHashMap extends AbstractTest {

    static boolean verbose = Boolean.getBoolean("verbose");

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_WeakValueHashMap.class);
    }
    
    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }

    /** */
    public void testContainsKey() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);
        y11 = null;
        y12 = null;

        if (verbose) System.out.println("\nTest ContainsKey:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertTrue("FAILURE: map does not contain x11", map.containsKey(new X("x11", y11)));
        assertTrue("FAILURE: map does not contain x12", map.containsKey(new X("x12")));
        assertTrue("FAILURE: map does not contain x13", map.containsKey(new X("x13")));
        assertTrue("FAILURE: map does not contain x14", map.containsKey(new X("x14")));
        assertTrue("FAILURE: map does not contain null", map.containsKey(null));

        // now gc
        gc();

        if (verbose) System.out.println("After GC:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertTrue("FAILURE: map does not contain x11", map.containsKey(new X("x11")));
        assertTrue("FAILURE: map contains x12", !map.containsKey(new X("x12")));
        assertTrue("FAILURE: map contains x13", !map.containsKey(new X("x13")));
        assertTrue("FAILURE: map does not contain x14", map.containsKey(new X("x14")));
        assertTrue("FAILURE: map does not contain null", map.containsKey(null));

        map.clear();
    }

    /** */
    public void testContainsValue() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);
        y11 = null;
        y12 = null;

        if (verbose) System.out.println("\nTest ContainsValue:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertTrue("FAILURE: map does not contain y11", map.containsValue(new Y("y11")));
        assertTrue("FAILURE: map does not contain y12", map.containsValue(new Y("y12", x12)));
        assertTrue("FAILURE: map does not contain y13", map.containsValue(new Y("y13")));
        assertTrue("FAILURE: map does not contain y14", map.containsValue(new Y("y14")));
        assertTrue("FAILURE: map does not contain null", map.containsValue(new Y("y14")));

        // now gc
        gc();

        if (verbose) System.out.println("After GC:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertTrue("FAILURE: map does not contain y11", map.containsValue(new Y("y11")));
        assertTrue("FAILURE: map contains y12", !map.containsValue(new Y("y12", x12)));
        assertTrue("FAILURE: map contains y13", !map.containsValue(new Y("y13")));
        assertTrue("FAILURE: map does not contain y14", map.containsValue(new Y("y14")));
        assertTrue("FAILURE: map does not contain null", map.containsValue(null));

        map.clear();
    }

    /** */
    public void testGet() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);

        if (verbose) System.out.println("\nTest Get:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertEquals("Get (x11) returns wrong value", y11, map.get(new X("x11")));
        assertEquals("Get (x12) returns wrong value", y12, map.get(new X("x12")));
        assertEquals("Get (x13) returns wrong value", new Y("y13"), map.get(new X("x13")));
        assertNull("Get (x14) returns wrong value", map.get(new X("x14")));
        assertEquals("Get (null) returns wrong value", y14, map.get(null));

        y11 = null;
        y12 = null;

        // now gc
        gc();

        if (verbose) System.out.println("After GC:");
        if (verbose) printEntries(map);

        // check whether instances are included in map
        assertNotNull("Get (x11) returns wrong value", map.get(new X("x11")));
        assertNull("Get (x12) returns wrong value",map.get(new X("x12")));
        assertNull("Get (x13) returns wrong value", map.get(new X("x13")));
        assertNull("Get (x14) returns wrong value", map.get(new X("x14")));
        assertNotNull("Get (null) returns wrong value", map.get(null));

        map.clear();
    }

    /** */
    public void testSize() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);
        y11 = null;
        y12 = null;

        int size1, size2, size3;

        if (verbose) System.out.println("\nTest Size:");
        if (verbose) printEntries(map);

        size1 = map.size();
        size2 = map.entrySet().size();
        size3 = map.values().size();

        if (verbose) System.out.println("Size: " + size1);

        assertEquals("FAILURE: unexpected size of map", 5, size1); 
        assertEquals("FAILURE: unexpected size of entrySet", 5, size2); 
        assertEquals("FAILURE: unexpected size of values", 5, size3); 

        // now gc
        gc();

        if (verbose) System.out.println("After GC:");
        if (verbose) printEntries(map);

        size1 = map.size();
        size2 = map.entrySet().size();
        size3 = map.values().size();

        if (verbose) System.out.println("After GC, size: " + size1);

        assertEquals("FAILURE: unexpected size of map", 3, size1); 
        assertEquals("FAILURE: unexpected size of entrySet", 3, size2); 
        assertEquals("FAILURE: unexpected size of values", 3, size3); 

        map.clear();
    }

    /** */
    public void testEntrySet() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);
        y11 = null;
        y12 = null;

        if (verbose) System.out.println("\nTest EntrySet:");
        if (verbose) printEntries(map);

        Set entrySet = map.entrySet();

        if (verbose) System.out.println("size: " + entrySet.size());
        assertEquals("Unexpected size of entrySet", 5, entrySet.size());
        if (verbose) System.out.println("isEmpty: " + entrySet.isEmpty());
        assertFalse("Unexpected empty entrySet", entrySet.isEmpty());
        if (verbose) System.out.println("hashCode: " + entrySet.hashCode());

        entrySet.remove(new Entry((Object) x12, (Object) new Y("y12", x12)));

        if (verbose) System.out.println("After remove(x12, y12):");
        if (verbose) printEntries(map);

        if (verbose) System.out.println("size: " + entrySet.size());
        assertEquals("Unexpected size of entrySet", 4, entrySet.size());
        if (verbose) System.out.println("isEmpty: " + entrySet.isEmpty());
        assertFalse("Unexpected empty entrySet", entrySet.isEmpty());
        if (verbose) System.out.println("hashCode: " + entrySet.hashCode());

        // Remove a specific entry, then print the map; the entry must be
        // gone from there.
        for (Iterator i = entrySet.iterator(); i.hasNext();) {
            Map.Entry ent = (Map.Entry) i.next();
            if (ent.getKey() == x11) {
                i.remove();
            }                             
        }
        
        if (verbose) System.out.println("After Iterator.remove:");
        if (verbose) printEntries(map);

        if (verbose) System.out.println("size: " + entrySet.size());
        assertEquals("Unexpected size of entrySet", 3, entrySet.size());
        if (verbose) System.out.println("isEmpty: " + entrySet.isEmpty());
        assertFalse("Unexpected empty entrySet", entrySet.isEmpty());
        if (verbose) System.out.println("hashCode: " + entrySet.hashCode());

        entrySet.clear();
        if (verbose) System.out.println("After clear:");

        if (verbose) System.out.println("size: " + entrySet.size());
        assertEquals("Unexpected size of entrySet", 0, entrySet.size());
        if (verbose) System.out.println("isEmpty: " + entrySet.isEmpty());
        assertTrue("Unexpected non empty entrySet", entrySet.isEmpty());
        if (verbose) System.out.println("hashCode: " + entrySet.hashCode());
    }

    /** */
    public void testValues() {
        Map map = new WeakValueHashMap();

        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);
        y11 = null;
        y12 = null;

        Collection values = map.values();

        if (verbose) System.out.println("\nTest Values:");
        if (verbose) printValues(map);
        assertEquals("Unexpected size of values", 5, values.size());

        // now gc
        gc();

        if (verbose) System.out.println("After GC:");
        if (verbose) printValues(map);
        assertEquals("Unexpected size of values", 3, values.size());

        values.remove(new Y("y11"));

        if (verbose) System.out.println("After remove(y11):");
        if (verbose) printValues(map);
        assertEquals("Unexpected size of values", 2, values.size());

        map.clear();
    }

    /** */
    public void testAll() {
        Map weakMap = new WeakValueHashMap();

        // WeakHashMap
        Y y11 = new Y("y11");
        X x11 = new X("x11", y11);
        X x12 = new X("x12");
        Y y12 = new Y("y12", x12);
        X x14 = new X("x14");
        Y y14 = new Y("y14");
        weakMap.put(x11, y11);
        weakMap.put(x12, y12);
        weakMap.put(new X("x13"), new Y("y13"));
        weakMap.put(x14, null);
        weakMap.put(null, y14);
 
        // Regular HashMap
        Map map = new HashMap();
        map.put(x11, y11);
        map.put(x12, y12);
        map.put(new X("x13"), new Y("y13"));
        map.put(x14, null);
        map.put(null, y14);

        if (verbose) System.out.println("\nTest equals:");
        if (verbose) System.out.println("Weak map Map1:"); if (verbose) printEntries(weakMap);
        if (verbose) System.out.println("Regular map Map2:"); if (verbose) printEntries(map);
        if (verbose) System.out.println("Map2.equals(Map1): " + map.equals(weakMap));
        assertTrue("map.equals(weakMap) should return true", map.equals(weakMap));
        if (verbose) System.out.println("Map1.equals(Map2): " + weakMap.equals(map));
        assertTrue("weakMap.equals(map) should return true", weakMap.equals(map));

        weakMap.put(new X("x15"), new Y("y15"));
        map.put(new X("x16"), new Y("y16"));

        if (verbose) System.out.println("\nTest equals:");
        if (verbose) System.out.println("Weak map Map1:"); if (verbose) printEntries(weakMap);
        if (verbose) System.out.println("Regular map Map2:"); if (verbose) printEntries(map);
        if (verbose) System.out.println("Map2.equals(Map1): " + map.equals(weakMap));
        assertFalse("map.equals(weakMap) should return false", map.equals(weakMap));
        if (verbose) System.out.println("Map1.equals(Map2): " + weakMap.equals(map));
        assertFalse("weakMap.equals(map) should return false", weakMap.equals(map));

        weakMap.remove(new X("x15"));
        map.clear();
        map.put(new X("x15"), new Y("y15"));
        map.put(new X("x16"), new Y("y16"));

        if (verbose) System.out.println("\nTest putAll:");
        if (verbose) System.out.println("Weak map Map1:"); if (verbose) printEntries(weakMap);
        if (verbose) System.out.println("Regular map Map2:"); if (verbose) printEntries(map);
        weakMap.putAll(map);
        if (verbose) System.out.println("Map1.putAll(Map2):"); if (verbose) printEntries(weakMap);
        assertEquals("Unexpected size of Weak map", 7, weakMap.size());
        weakMap.remove(new X("x15"));
        weakMap.remove(new X("x16"));

        if (verbose) System.out.println("\nTest putAll:");
        if (verbose) System.out.println("Weak map Map1:"); if (verbose) printEntries(weakMap);
        if (verbose) System.out.println("Regular map Map2:"); if (verbose) printEntries(map);
        map.putAll(weakMap);
        if (verbose) System.out.println("Map2.putAll(Map1):"); if (verbose) printEntries(map);
        assertEquals("Unexpected size of map", 7, map.size());

        weakMap.clear();
    }

    /* Internal class for entries */
    private static class Entry implements Map.Entry {
        private Object key;
        private Object value;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        private static boolean valEquals(Object o1, Object o2) {
            return (o1 == null) ? (o2 == null) : o1.equals(o2);
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry e = (Map.Entry) o;
            return (valEquals(key, e.getKey())
                    && valEquals(value, e.getValue()));
        }

        public int hashCode() {
            Object k;
            return (((key == null) ? 0 : key.hashCode())
                    ^ ((value == null) ? 0 : value.hashCode()));
        }

    }

    private static class X {
        private String name;
        private Y y;
        public X(String name) { this.name = name; }
        public X(String name, Y y) { this.name = name; this.y = y; }
        public String toString() { return "(" + this.name + "|" + y + ")"; }
        public int hashCode() { return (name == null) ? 0 : name.hashCode(); }
        public boolean equals(Object o)
        {
            // compare by name only
            if ((o == null) || !(o instanceof X))
                return false;
            else if (name == null)
                return ((X)o).name == null;
            else
                return name.equals(((X)o).name);
        }
    }

    private static class Y implements Comparable {
        private String name;
        private X x;
        public Y(String name) { this.name = name; }
        public Y(String name, X x) { this.name = name; this.x = x; }
        public String toString() { return "(" + this.name + "|" + x + ")"; }
        public int hashCode() { return (name == null) ? 0 : name.hashCode(); }
        public int compareTo(Object o) {
            if (o != null
                && o instanceof Y) {
                return toString().compareTo(o.toString());
            } else {
                return 1;
            }
        }            
        public boolean equals(Object o)
        {
            // compare by name only
            if ((o == null) || !(o instanceof Y))
                return false;
            else if (name == null)
                return ((Y)o).name == null;
            else
                return name.equals(((Y)o).name);
        }
    }

    /**
     * A sorted set of instances of map entries.
     */
    private static class XTreeSet extends TreeSet {
        private static final Comparator c = new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (o1 == null) {
                        return -1;
                    } else if (o2 == null) {
                        return 1;
                    } else {
                        Object s1 = ((Map.Entry)o1).getKey();
                        Object s2 = ((Map.Entry)o2).getKey();
                        if (s1 == null) {
                            return -1;
                        } else if (s2 == null) {
                            return 1;
                        } else {
                            return (s1.toString()).compareTo(s2.toString());
                        }
                    }
                }
                
                public boolean equals(Object obj) {
                    return obj.equals(this);
                }
            };
        
        XTreeSet(Collection col) {
            super(c);
            this.addAll(col);
        }
    }

    private static void printEntries(Map map) {
        Set entrySet = map.entrySet();
        TreeSet sorted = new XTreeSet(entrySet);
        for (Iterator i = sorted.iterator(); i.hasNext();) {
            Map.Entry ent = (Map.Entry) i.next();
            System.out.println("Key: " + ent.getKey() +
                    " Val: " + ent.getValue());
        }
    }

    private static void printValues(Map map) {
        TreeSet values = new TreeSet(map.values());
        for (Iterator i = values.iterator(); i.hasNext();) {
            System.out.println("Val: " + i.next());
        }
    }

    private static void gc() {
        //System.out.print("gc: ");
        Runtime rt = Runtime.getRuntime();
        long oldfree;
        long newfree = rt.freeMemory();
        do {
            oldfree = newfree;
            rt.runFinalization(); rt.gc();
            newfree = rt.freeMemory();
            //System.out.print('.');
        } while (newfree > oldfree);
        //System.out.println();
    }

}


