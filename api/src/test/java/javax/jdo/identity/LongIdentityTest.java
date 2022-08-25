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
 * LongIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;
import javax.jdo.util.BatchTestRunner;

/** */
public class LongIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of LongIdentityTest */
  public LongIdentityTest() {}

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
    LongIdentity c4 = new LongIdentity(Object.class, 0x100000001L);
    assertEquals("Equal LongIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal LongIdentity instances compare equal", c1.equals(c3));
    assertFalse("Not equal LongIdentity instances compare equal", c4.equals(c1));
  }

  public void testLongConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, Long.valueOf(1));
    LongIdentity c3 = new LongIdentity(Object.class, Long.valueOf(2));
    assertEquals("Equal LongIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal LongIdentity instances compare equal", c1.equals(c3));
  }

  public void testToStringConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, Long.MAX_VALUE);
    LongIdentity c2 = new LongIdentity(Object.class, c1.toString());
    assertEquals("Equal LongIdentity instances compare not equal.", c1, c2);
  }

  public void testStringConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, "1");
    LongIdentity c3 = new LongIdentity(Object.class, "2");
    assertEquals("Equal LongIdentity instances compare not equal.", c1, c2);
    assertFalse("Not equal LongIdentity instances compare equal", c1.equals(c3));
  }

  public void testIllegalStringConstructor() {
    try {
      new LongIdentity(Object.class, "b");
    } catch (IllegalArgumentException iae) {
      return; // good
    }
    fail("No exception caught for illegal String.");
  }

  public void testSerialized() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, "1");
    LongIdentity c3 = new LongIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    assertEquals("Equal LongIdentity instances compare not equal.", c1, sc1);
    assertEquals("Equal LongIdentity instances compare not equal.", c2, sc2);
    assertEquals("Equal LongIdentity instances compare not equal.", sc1, c2);
    assertEquals("Equal LongIdentity instances compare not equal.", sc2, c1);
    assertFalse("Not equal LongIdentity instances compare equal.", c1.equals(sc3));
    assertFalse("Not equal LongIdentity instances compare equal.", sc1.equals(c3));
    assertFalse("Not equal LongIdentity instances compare equal.", sc1.equals(sc3));
    assertFalse("Not equal LongIdentity instances compare equal.", sc3.equals(sc1));
  }

  public void testGetKeyAsObjectPrimitive() {
    LongIdentity c1 = new LongIdentity(Object.class, 1L);
    assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), Long.valueOf(1L));
  }

  public void testGetKeyAsObject() {
    LongIdentity c1 = new LongIdentity(Object.class, Long.valueOf(1L));
    assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), Long.valueOf(1L));
  }

  public void testBadConstructorNullShortParam() {
    try {
      new LongIdentity(Object.class, (Long) null);
    } catch (JDONullIdentityException ex) {
      return;
    }
    fail("Failed to catch expected exception.");
  }

  public void testBadConstructorNullStringParam() {
    try {
      new LongIdentity(Object.class, (String) null);
    } catch (JDONullIdentityException ex) {
      return;
    }
    fail("Failed to catch expected exception.");
  }

  public void testCompareTo() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, 1);
    LongIdentity c3 = new LongIdentity(Object.class, 2);
    LongIdentity c4 = new LongIdentity(Class.class, 1);
    LongIdentity c5 = new LongIdentity(Object.class, 0x100000001L);
    assertEquals("Equal LongIdentity instances compare not equal.", 0, c1.compareTo(c2));
    assertTrue(
        "Not equal LongIdentity instances have wrong compareTo result", c1.compareTo(c3) < 0);
    assertTrue(
        "Not equal LongIdentity instances have wrong compareTo result", c3.compareTo(c1) > 0);
    assertTrue(
        "Not equal LongIdentity instances have wrong compareTo result", c1.compareTo(c4) > 0);
    assertTrue(
        "Not equal LongIdentity instances have wrong compareTo result", c5.compareTo(c1) > 0);
  }
}
