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
 * TestWeakHashSet.java
 *
 */

package org.apache.jdo.test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.util.WeakHashSet;


/**
 *
 * @author  Michael Bouschen
 * @author  Markus Fuchs
 */
public class Test_WeakHashSet extends AbstractTest {

    static boolean verbose = Boolean.getBoolean("verbose");

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_WeakHashSet.class);
    }
    
    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }

    /** */
    public void testContains() {
        // WeakHashSet
        Set weakSet = new WeakHashSet();

        X x11 = new X("x11");
        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);

        // Regular HashSet
        Set set = new HashSet();
        X x21 = new X("x21");
        set.add(x21);
        set.add(new X("x22"));
        set.add(null);

        // check whether instance is included in set
        assertTrue("FAILURE: weakSet does not contain x11", weakSet.contains(x11));
        assertTrue("FAILURE: weakSet does not contain x12", weakSet.contains(new X("x12")));
        assertTrue("FAILURE: weakSet does not contain null", weakSet.contains(null));
        assertTrue("FAILURE: set does not contain x21", set.contains(x21));
        assertTrue("FAILURE: set does not contain x22", set.contains(new X("x22")));
        assertTrue("FAILURE: set does not contain null", set.contains(null));

        if (verbose) System.out.println("\nTest GC:");
        if (verbose) System.out.println("Before GC: " + new XTreeSet(weakSet)); 

        // now gc
        gc();

        if (verbose) System.out.println("After GC: " + new XTreeSet(weakSet)); 

        // x11 should still be in weakSet
        assertTrue("FAILURE: weakSet does not contain x11 after gc", weakSet.contains(x11));
        // x12 should not be in weakSet
        assertTrue("FAILURE: weakSet still contains x12 after gc", !weakSet.contains(new X("x12")));
        assertTrue("FAILURE: weakSet does not contain null", weakSet.contains(null));

        // set should not be affected by gc
        assertEquals("FAILURE: set has wrong size ", 3, set.size());
        assertTrue("FAILURE: set does not contain x21 after gc", set.contains(x21));
        assertTrue("FAILURE: set does not contain x22 after gc", set.contains(new X("x22")));
        assertTrue("FAILURE: set does not contain null", set.contains(null));

        weakSet.clear();
        if (verbose) System.out.println("After Clear: " + new XTreeSet(weakSet)); 
    }
    
    /** */
    public void testRemove() {
        // WeakHashSet
        gc();
        Set weakSet = new WeakHashSet();

        X x11 = new X("x11");
        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);

        // check whether instance is included in set
        assertTrue("FAILURE: weakSet does not contain x11", weakSet.contains(x11));
        assertTrue("FAILURE: weakSet does not contain x12", weakSet.contains(new X("x12")));
        assertTrue("FAILURE: weakSet does not contain null", weakSet.contains(null));

        if (verbose) System.out.println("\nTest Remove: " + new XTreeSet(weakSet)); 
        if (verbose) System.out.println("Size = " + weakSet.size());
        assertEquals("Unexpected size of weakSet", 3, weakSet.size());

        weakSet.remove(x11);
        if (verbose) System.out.println("After Remove(x11): " + new XTreeSet(weakSet)); 
        if (verbose) System.out.println("Size = " + weakSet.size());
        assertEquals("Unexpected size of weakSet", 2, weakSet.size());

        weakSet.remove(new X("x12"));
        if (verbose) System.out.println("After Remove(x12): " + new XTreeSet(weakSet)); 
        if (verbose) System.out.println("Size = " + weakSet.size());
        assertEquals("Unexpected size of weakSet", 1, weakSet.size());

        weakSet.remove(null);
        if (verbose) System.out.println("After Remove(null): " + new XTreeSet(weakSet)); 
        if (verbose) System.out.println("Size = " + weakSet.size());
        assertEquals("Unexpected size of weakSet", 0, weakSet.size());

        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);

        XTreeSet xts = new XTreeSet(weakSet);
        if (verbose) System.out.println("\nTest Iterator: " + xts);
        if (verbose) System.out.println("Size = " + weakSet.size());
        assertEquals("Unexpected size of weakSet", 3, weakSet.size());

        int size = 3;
        for (Iterator i = weakSet.iterator(); i.hasNext();) {
            Object o = i.next();
            if (verbose) System.out.println("iterator.remove: " + o);
            i.remove();
            size--;
            if (verbose) System.out.println("After remove: " + new XTreeSet(weakSet));
            if (verbose) System.out.println("Size = " + weakSet.size());
            assertEquals("Unexpected size of weakSet", size, weakSet.size());
        }

        weakSet.clear();
    }

    /** */
    public void testAll() {
        // WeakHashSet
        gc();
        Set weakSet = new WeakHashSet();

        X x11 = new X("x11");
        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);

        // Regular HashSet
        Set set = new HashSet();
        set.add(x11);
        set.add(new X("x12"));
        set.add(null);

        if (verbose) System.out.println("\nTest containsAll: ");
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        if (verbose) System.out.println("Regular set Set2: " + new XTreeSet(set));
        if (verbose) System.out.println("Set1.containsAll(Set2): " + weakSet.containsAll(set));
        assertTrue("Set1.containsAll(Set2) should return true", weakSet.containsAll(set));
        if (verbose) System.out.println("Set2.containsAll(Set1): " + set.containsAll(weakSet));
        assertTrue("Set2.containsAll(Set1) should return true", set.containsAll(weakSet));
        if (verbose) System.out.println("Set2.equals(Set1): " + set.equals(weakSet));
        assertTrue("Set2.equals(Set1) should return true", set.equals(weakSet));
        if (verbose) System.out.println("Set1.equals(Set2): " + weakSet.equals(set));
        assertTrue("Set1.equals(Set2) should return true", weakSet.equals(set));

        weakSet.add(new X("x13"));
        set.add(new X("x14"));

        if (verbose) System.out.println("\nTest containsAll: ");
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        if (verbose) System.out.println("Regular set Set2: " + new XTreeSet(set));
        if (verbose) System.out.println("Set1.containsAll(Set2): " + weakSet.containsAll(set));
        assertFalse("Set1.containsAll(Set2) should return false", weakSet.containsAll(set));
        if (verbose) System.out.println("Set2.containsAll(Set1): " + set.containsAll(weakSet));
        assertFalse("Set2.containsAll(Set1) should return false", set.containsAll(weakSet));
        if (verbose) System.out.println("Set2.equals(Set1): " + set.equals(weakSet));
        assertFalse("Set2.equals(Set1) should return false", set.equals(weakSet));
        if (verbose) System.out.println("Set1.equals(Set2): " + weakSet.equals(set));
        assertFalse("Set1.equals(Set2) should return false", weakSet.equals(set));

        weakSet.remove(new X("x13"));
        set.remove(new X("x14"));

        boolean result;
        if (verbose) System.out.println("\nTest removeAll: ");
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        if (verbose) System.out.println("Regular set Set2: " + new XTreeSet(set));
        result = weakSet.removeAll(set);
        if (verbose) System.out.println("Set1.removeAll(Set2): " + result);
        assertTrue("weakSet.removeAll(set) should change the weakSet", result);
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        assertTrue("weakSet should be empty", weakSet.isEmpty());
        gc();

        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);

        result = set.removeAll(weakSet);
        if (verbose) System.out.println("Set2.removeAll(Set1): " + result);
        assertTrue("set.removeAll(weakSet) should change the set", result);
        if (verbose) System.out.println("Regular set Set2: " +new XTreeSet( set));
        assertTrue("set should be empty", set.isEmpty());

        set.add(x11);
        set.add(new X("x12"));
        set.add(null);
        set.add(new X("x14"));
        weakSet.add(new X("x13"));

        if (verbose) System.out.println("\nTest removeAll: ");
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        if (verbose) System.out.println("Regular set Set2: " + new XTreeSet(set));
        result = weakSet.removeAll(set);
        if (verbose) System.out.println("Set1.removeAll(Set2): " + result);
        assertTrue("weakSet.removeAll(set) should change the weakSet", result);
        if (verbose) System.out.println("Weak set Set1: " + new XTreeSet(weakSet));
        assertEquals("Unexpected size of weakSet", 1, weakSet.size());

        weakSet.add(x11);
        weakSet.add(new X("x12"));
        weakSet.add(null);
        weakSet.add(new X("x13"));

        result = set.removeAll(weakSet);
        if (verbose) System.out.println("Set2.removeAll(Set1): " + result);
        assertTrue("set.removeAll(weakSet) should change the set", result);
        if (verbose) System.out.println("Regular set Set2: " + new XTreeSet(set));
        assertEquals("Unexpected size of set", 1, set.size());

        weakSet.clear();
    }

    private static class X {
        private String name;
        public X(String name) { this.name = name; }
        public String toString() { return this.name; }
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

    /**
     * A Sorted set of X instances.
     */
    private static class XTreeSet extends TreeSet {
        private static final Comparator c = new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (o1 == null) {
                        return -1;
                    } else if (o2 == null) {
                        return 1;
                    } else {
                        String s1 = ((X)o1).toString();
                        String s2 = ((X)o2).toString();
                        if (s1 == null) {
                            return -1;
                        } else if (s2 == null) {
                            return 1;
                        } else {
                            return s1.compareTo(s2);
                        }
                    }
                }
                
                public boolean equals(Object obj) {
                    return true;
                }
            };
        
        XTreeSet(Set s) {
            super(c);
            this.addAll(s);
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


