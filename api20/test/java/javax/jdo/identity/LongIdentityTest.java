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
 * LongIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.util.BatchTestRunner;

/**
 *
 */
public class LongIdentityTest extends SingleFieldIdentityTest {
    
    /** Creates a new instance of LongIdentityTest */
    public LongIdentityTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(LongIdentityTest.class);
    }
    
    public void testConstructor() {
        LongIdentity c1 = new LongIdentity(Object.class, 1);
        LongIdentity c2 = new LongIdentity(Object.class, 1);
        LongIdentity c3 = new LongIdentity(Object.class, 2);
        assertEquals("Equal LongIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }

    public void testLongConstructor() {
        LongIdentity c1 = new LongIdentity(Object.class, 1);
        LongIdentity c2 = new LongIdentity(Object.class, new Long(1));
        LongIdentity c3 = new LongIdentity(Object.class, new Long(2));
        assertEquals ("Equal LongIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }

    public void testStringConstructor() {
        LongIdentity c1 = new LongIdentity(Object.class, 1);
        LongIdentity c2 = new LongIdentity(Object.class, "1");
        LongIdentity c3 = new LongIdentity(Object.class, "2");
        assertEquals ("Equal LongIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ByteIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testIllegalStringConstructor() {
        try {
            LongIdentity c1 = new LongIdentity(Object.class, "b");
        } catch (IllegalArgumentException iae) {
            return; // good
        }
        fail ("No exception caught for illegal String.");
    }
    
    public void testSerialized() {
        LongIdentity c1 = new LongIdentity(Object.class, 1);
        LongIdentity c2 = new LongIdentity(Object.class, "1");
        LongIdentity c3 = new LongIdentity(Object.class, "2");
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal LongIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal LongIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal LongIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal LongIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal LongIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal LongIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal LongIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal LongIdentity instances compare equal.", sc3.equals(sc1));
    }
}
