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
 * StringIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;
import javax.jdo.JDOUserException;

import javax.jdo.util.BatchTestRunner;

/**
 *
 */
public class StringIdentityTest extends SingleFieldIdentityTest {
    
    /** Creates a new instance of StringIdentityTest */
    public StringIdentityTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(StringIdentityTest.class);
    }
    
    public void testConstructor() {
        StringIdentity c1 = new StringIdentity(Object.class, "1");
        StringIdentity c2 = new StringIdentity(Object.class, "1");
        StringIdentity c3 = new StringIdentity(Object.class, "2");
        assertEquals("Equal StringIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal StringIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testToStringConstructor() {
        StringIdentity c1 = new StringIdentity(Object.class, "Now who's talking!");
        StringIdentity c2 = new StringIdentity(Object.class, c1.toString());
        assertEquals ("Equal StringIdentity instances compare not equal.", c1, c2);
    }

    public void testSerialized() {
        StringIdentity c1 = new StringIdentity(Object.class, "1");
        StringIdentity c2 = new StringIdentity(Object.class, "1");
        StringIdentity c3 = new StringIdentity(Object.class, "2");
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal StringIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal StringIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal StringIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal StringIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal StringIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal StringIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal StringIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal StringIdentity instances compare equal.", sc3.equals(sc1));
    }

    public void testGetKeyAsObject() {
        StringIdentity c1 = new StringIdentity(Object.class, "1");
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), "1");
    }

    public void testBadConstructorNullParam() {
        try {
            StringIdentity c1 = new StringIdentity(Object.class, null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }

    public void testCompareTo() {
    	StringIdentity c1 = new StringIdentity(Object.class, "1");
    	StringIdentity c2 = new StringIdentity(Object.class, "1");
    	StringIdentity c3 = new StringIdentity(Object.class, "2");
        assertEquals("Equal StringIdentity instances compare not equal.", 0, c1.compareTo(c2));
        assertTrue("Not equal StringIdentity instances have wrong compareTo result", c1.compareTo(c3) < 0);
        assertTrue("Not equal StringIdentity instances have wrong compareTo result", c3.compareTo(c1) > 0); 
    }
}
