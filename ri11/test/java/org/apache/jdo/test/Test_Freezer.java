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
 * Test_Freezer.java
 *
 * Created on April 12, 2003, 4:15 PM
 */

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jdo.impl.sco.Freezer;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 *
 * @author  Craig Russell
 * @version 1.0.1
 */
public class Test_Freezer extends AbstractTest {
    
    /** */
    static private final short s33 = 33;

    /** */
    private boolean verbose;
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Freezer.class);
    }

    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }
    
    /** */
    public void testSet() {
        Set ts = new HashSet();
        ts.add(new Integer(12));
        ts.add(new Integer(12));
        ts.add(new Integer(12));
        ts.add(new Integer(12));
        ts.add(new java.util.Date(2000));
        ts.add(new Float(2000));
        ts.add(new Float(2020));
        ts.add(new Float(2000.1));
        ts.add(new Integer(112));
        ts.add(new Integer(212));
        ts.add(new Float(2000.001));
        ts.add(new Float(2000.00001));
        ts.add(new Short(s33));
        ts.add(new Integer(2));
        ts.add(new Long(3456789L));
        ts.add(new java.util.Date(1000));
        List expected = new ArrayList();
        expected.add(new Float(2000));
        expected.add(new Float(2000.001));
        expected.add(new Float(2000.1));
        expected.add(new Float(2020));
        expected.add(new Integer(2));
        expected.add(new Integer(12));
        expected.add(new Integer(112));
        expected.add(new Integer(212));
        expected.add(new Long(3456789L));
        expected.add(new Short(s33));
        expected.add(new java.util.Date(1000));
        expected.add(new java.util.Date(2000));

        // now freeze the set
        Object[] ordered = Freezer.freeze(ts, ts.size());
        List al = Arrays.asList(ordered);
        if (verbose) 
            for (Iterator it=al.iterator(); it.hasNext();) {
                Object o = it.next();
                System.out.println(o.getClass().getName() + ":" + o.toString());
            }
        assertEquals("freezing a Set returned unexpected result", expected, al);
    }
    
    /** */
    public void testMap() {
        Map hm = new HashMap();
        hm.put(new Integer(22), new Integer(1000));
        hm.put(new java.sql.Time(2004), new Integer(6));
        hm.put(new Integer(22), new Integer(1000));
        hm.put(new Integer(22), new Integer(1000));
        hm.put(new Integer(22), new Integer(16));
        hm.put(new java.sql.Date(2001), new Integer(2));
        hm.put(new java.util.Date(2000), new Integer(1));
        hm.put(new Float(2000.), new Integer(10));
        hm.put(new Float(2020), new Integer(14));
        hm.put(new org.apache.jdo.impl.sco.SqlTime(3000), new Integer(7));
        hm.put(new java.sql.Date(2002), new Integer(3));
        hm.put(new Float(2000.1), new Integer(13));
        hm.put(new Integer(112), new Integer(17));
        hm.put(new org.apache.jdo.impl.sco.SqlDate(3004), new Integer(5));
        hm.put(new Integer(212), new Integer(18));
        hm.put(new java.sql.Date(2003), new Integer(4));
        hm.put(new Float(2000.001), new Integer(11));
        hm.put(new Float(2000.01), new Integer(12));
        hm.put(new java.sql.Timestamp(2004), new Integer(8));
        hm.put(new org.apache.jdo.impl.sco.SqlTimestamp(3002), new Integer(9));
        hm.put(new Short(s33), new Integer(20));
        hm.put(new Integer(2), new Integer(15));
        hm.put(new Long(3456789L), new Integer(19));
        hm.put(new java.util.Date(1000), new Integer(0));

        List expected = new ArrayList();
        expected.add(new SimpleEntry(new Float(2000.), new Integer(10)));
        expected.add(new SimpleEntry(new Float(2000.001), new Integer(11)));
        expected.add(new SimpleEntry(new Float(2000.01), new Integer(12)));
        expected.add(new SimpleEntry(new Float(2000.1), new Integer(13)));
        expected.add(new SimpleEntry(new Float(2020), new Integer(14)));
        expected.add(new SimpleEntry(new Integer(2), new Integer(15)));
        expected.add(new SimpleEntry(new Integer(22), new Integer(16)));
        expected.add(new SimpleEntry(new Integer(112), new Integer(17)));
        expected.add(new SimpleEntry(new Integer(212), new Integer(18)));
        expected.add(new SimpleEntry(new Long(3456789L), new Integer(19)));
        expected.add(new SimpleEntry(new Short(s33), new Integer(20)));
        expected.add(new SimpleEntry(new java.util.Date(1000), new Integer(0)));
        expected.add(new SimpleEntry(new java.util.Date(2000), new Integer(1)));
        expected.add(new SimpleEntry(new java.sql.Date(2001), new Integer(2)));
        expected.add(new SimpleEntry(new java.sql.Date(2002), new Integer(3)));
        expected.add(new SimpleEntry(new java.sql.Date(2003), new Integer(4)));
        expected.add(new SimpleEntry(new org.apache.jdo.impl.sco.SqlDate(3004), new Integer(5)));
        expected.add(new SimpleEntry(new java.sql.Time(2004), new Integer(6)));
        expected.add(new SimpleEntry(new org.apache.jdo.impl.sco.SqlTime(3000), new Integer(7)));
        expected.add(new SimpleEntry(new java.sql.Timestamp(2004), new Integer(8)));
        expected.add(new SimpleEntry(new org.apache.jdo.impl.sco.SqlTimestamp(3002), new Integer(9)));
        
        Object[] ordered = Freezer.freeze(hm, hm.size());
        List al = Arrays.asList(ordered);
        if (verbose)
            for (Iterator it=al.iterator(); it.hasNext();) {
                Map.Entry o = (Map.Entry)it.next();
                Object key = o.getKey();
                Object value = o.getValue();
                System.out.println(" " + value + " " + key.getClass().getName() + ":" + key.toString());
            }
        assertEquals("freezing a Map returned unexpected result", expected, al);
    }

    /** */
    static class SimpleEntry implements Map.Entry {
        Object key;
        Object value;

        public SimpleEntry(Object key, Object value) {
            this.key   = key;
            this.value = value;
        }
        
        public SimpleEntry(Map.Entry e) {
            this.key   = e.getKey();
            this.value = e.getValue();
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
        
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry)o;
            return eq(key, e.getKey()) &&  eq(value, e.getValue());
        }
        
        public int hashCode() {
            Object v;
            return ((key   == null)   ? 0 :   key.hashCode()) ^
                ((value == null)   ? 0 : value.hashCode());
        }
        
        public String toString() {
            return key + "=" + value;
        }
        
        private static boolean eq(Object o1, Object o2) {
            return (o1 == null ? o2 == null : o1.equals(o2));
        }
    }

}
