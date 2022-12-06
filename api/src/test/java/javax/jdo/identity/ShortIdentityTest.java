/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * ShortIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;
import javax.jdo.util.BatchTestRunner;

/** */
public class ShortIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of ShortIdentityTest */
  public ShortIdentityTest() {}

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ShortIdentityTest.class);
  }

  public void testConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c3 = new ShortIdentity(Object.class, (short) 2);
    assertEquals("Equal ShortIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal ShortIdentity instances compare equal", c1.equals(c3));
  }

  public void testShortConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, Short.valueOf((short) 1));
    ShortIdentity c3 = new ShortIdentity(Object.class, Short.valueOf((short) 2));
    assertEquals("Equal ShortIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal ShortIdentity instances compare equal", c1.equals(c3));
  }

  public void testToStringConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, Short.MAX_VALUE);
    ShortIdentity c2 = new ShortIdentity(Object.class, c1.toString());
    assertEquals("Equal ShortIdentity instances compare not equal.", c1, c2);
  }

  public void testStringConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, "1");
    ShortIdentity c3 = new ShortIdentity(Object.class, "2");
    assertEquals("Equal ShortIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal ShortIdentity instances compare equal", c1.equals(c3));
  }

  public void testIllegalStringConstructor() {
    try {
      new ShortIdentity(Object.class, "b");
    } catch (IllegalArgumentException iae) {
      return; // good
    }
    fail("No exception caught for illegal String.");
  }

  public void testSerialized() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, "1");
    ShortIdentity c3 = new ShortIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    assertEquals("Equal ShortIdentity instances compare not equal.", c1, sc1);
    assertEquals("Equal ShortIdentity instances compare not equal.", c2, sc2);
    assertEquals("Equal ShortIdentity instances compare not equal.", sc1, c2);
    assertEquals("Equal ShortIdentity instances compare not equal.", sc2, c1);
    assertFalse("Not equal ShortIdentity instances compare equal.", c1.equals(sc3));
    assertFalse("Not equal ShortIdentity instances compare equal.", sc1.equals(c3));
    assertFalse("Not equal ShortIdentity instances compare equal.", sc1.equals(sc3));
    assertFalse("Not equal ShortIdentity instances compare equal.", sc3.equals(sc1));
  }

  public void testGetKeyAsObjectPrimitive() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), Short.valueOf((short) 1));
  }

  public void testGetKeyAsObject() {
    ShortIdentity c1 = new ShortIdentity(Object.class, Short.valueOf((short) 1));
    assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), Short.valueOf((short) 1));
  }

  public void testBadConstructorNullShortParam() {
    try {
      new ShortIdentity(Object.class, (Short) null);
    } catch (JDONullIdentityException ex) {
      return;
    }
    fail("Failed to catch expected exception.");
  }

  public void testBadConstructorNullStringParam() {
    try {
      new ShortIdentity(Object.class, (String) null);
    } catch (JDONullIdentityException ex) {
      return;
    }
    fail("Failed to catch expected exception.");
  }

  public void testCompareTo() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c3 = new ShortIdentity(Object.class, (short) 2);
    ShortIdentity c4 = new ShortIdentity(Class.class, (short) 1);
    assertEquals("Equal ShortIdentity instances compare not equal.", 0, c1.compareTo(c2));
    assertTrue(
        "Not equal ShortIdentity instances have wrong compareTo result", c1.compareTo(c3) < 0);
    assertTrue(
        "Not equal ShortIdentity instances have wrong compareTo result", c3.compareTo(c1) > 0);
    assertTrue(
        "Not equal ShortIdentity instances have wrong compareTo result", c1.compareTo(c4) > 0);
  }
}
