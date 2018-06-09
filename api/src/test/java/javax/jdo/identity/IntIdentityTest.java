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
 * IntIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;

import javax.jdo.util.BatchTestRunner;

/**
 *
 */
public class IntIdentityTest extends SingleFieldIdentityTest {
    
    /** Creates a new instance of IntIdentityTest */
    public IntIdentityTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IntIdentityTest.class);
    }
    
    public void testConstructor() {
        IntIdentity c1 = new IntIdentity(Object.class, (int)1);
        IntIdentity c2 = new IntIdentity(Object.class, (int)1);
        IntIdentity c3 = new IntIdentity(Object.class, (int)2);
        assertEquals("Equal IntIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal IntIdentity instances compare equal", c1.equals(c3));
    }

    public void testIntegerConstructor() {
        IntIdentity c1 = new IntIdentity(Object.class, (int)1);
        IntIdentity c2 = new IntIdentity(Object.class, new Integer((int)1));
        IntIdentity c3 = new IntIdentity(Object.class, new Integer((int)2));
        assertEquals ("Equal intIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal IntIdentity instances compare equal", c1.equals(c3));
    }

    public void testToStringConstructor() {
        IntIdentity c1 = new IntIdentity(Object.class, (int)1);
        IntIdentity c2 = new IntIdentity(Object.class, c1.toString());
        assertEquals ("Equal IntIdentity instances compare not equal.", c1, c2);
    }

    public void testStringConstructor() {
        IntIdentity c1 = new IntIdentity(Object.class, (int)1);
        IntIdentity c2 = new IntIdentity(Object.class, "1");
        IntIdentity c3 = new IntIdentity(Object.class, "2");
        assertEquals ("Equal IntIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal IntIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testIllegalStringConstructor() {
        try {
            new IntIdentity(Object.class, "b");
        } catch (IllegalArgumentException iae) {
            return; // good
        }
        fail ("No exception caught for illegal String.");
    }
    
    public void testSerialized() {
        IntIdentity c1 = new IntIdentity(Object.class, (int)1);
        IntIdentity c2 = new IntIdentity(Object.class, "1");
        IntIdentity c3 = new IntIdentity(Object.class, "2");
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal IntIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal IntIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal IntIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal IntIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal InrIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal IntIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal IntIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal IntIdentity instances compare equal.", sc3.equals(sc1));
    }
    public void testGetKeyAsObjectPrimitive() {
        IntIdentity c1 = new IntIdentity(Object.class, 1);
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), new Integer(1));
    }

    public void testGetKeyAsObject() {
        IntIdentity c1 = new IntIdentity(Object.class, new Integer(1));
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), new Integer(1));
    }

    public void testBadConstructorNullIntegerParam() {
        try {
            new IntIdentity(Object.class, (Integer)null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }

    public void testBadConstructorNullStringParam() {
        try {
            new IntIdentity(Object.class, (String)null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }

    public void testCompareTo() {
    	IntIdentity c1 = new IntIdentity(Object.class, 1);
    	IntIdentity c2 = new IntIdentity(Object.class, 1);
    	IntIdentity c3 = new IntIdentity(Object.class, 2);
        IntIdentity c4 = new IntIdentity(Class.class, 1);
        assertEquals("Equal IntIdentity instances compare not equal.", 0, c1.compareTo(c2));
        assertTrue("Not equal IntIdentity instances have wrong compareTo result", c1.compareTo(c3) < 0);
        assertTrue("Not equal IntIdentity instances have wrong compareTo result", c3.compareTo(c1) > 0); 
        assertTrue("Not equal IntIdentity instances have wrong compareTo result", c1.compareTo(c4) > 0);
    }
}
