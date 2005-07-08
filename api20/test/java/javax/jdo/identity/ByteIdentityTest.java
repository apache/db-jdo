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
 * ByteIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;

import javax.jdo.util.BatchTestRunner;

/**
 *
 */
public class ByteIdentityTest extends SingleFieldIdentityTest {
    
    /** Creates a new instance of ByteIdentityTest */
    public ByteIdentityTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ByteIdentityTest.class);
    }
    
    public void testConstructor() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c2 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c3 = new ByteIdentity(Object.class, (byte)2);
        assertEquals("Equal ByteIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }

    public void testByteConstructor() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c2 = new ByteIdentity(Object.class, new Byte((byte)1));
        ByteIdentity c3 = new ByteIdentity(Object.class, new Byte((byte)2));
        assertEquals ("Equal ByteIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }

    public void testToStringConstructor() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c2 = new ByteIdentity(Object.class, c1.toString());
        assertEquals ("Equal ByteIdentity instances compare not equal.", c1, c2);
    }

    public void testStringConstructor() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c2 = new ByteIdentity(Object.class, "1");
        ByteIdentity c3 = new ByteIdentity(Object.class, "2");
        assertEquals ("Equal ByteIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testIllegalStringConstructor() {
        try {
            ByteIdentity c1 = new ByteIdentity(Object.class, "b");
        } catch (IllegalArgumentException iae) {
            return; // good
        }
        fail ("No exception caught for illegal String.");
    }
    
    public void testSerialized() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        ByteIdentity c2 = new ByteIdentity(Object.class, "1");
        ByteIdentity c3 = new ByteIdentity(Object.class, "2");
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ByteIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ByteIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ByteIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ByteIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ByteIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ByteIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ByteIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ByteIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testGetKeyAsObjectPrimitive() {
        ByteIdentity c1 = new ByteIdentity(Object.class, (byte)1);
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), new Byte((byte)1));
    }

    public void testGetKeyAsObject() {
        ByteIdentity c1 = new ByteIdentity(Object.class, new Byte((byte)1));
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), new Byte((byte)1));
    }

    public void testBadConstructorNullByteParam() {
        try {
            ByteIdentity c1 = new ByteIdentity(Object.class, (Byte)null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }

    public void testBadConstructorNullStringParam() {
        try {
            ByteIdentity c1 = new ByteIdentity(Object.class, (String)null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }

}
