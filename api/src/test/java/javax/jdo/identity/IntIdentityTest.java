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
 * IntIdentityTest.java
 *
 */

package javax.jdo.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.jdo.JDONullIdentityException;
import org.junit.jupiter.api.Test;

/** */
class IntIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of IntIdentityTest */
  public IntIdentityTest() {}

  @Test
  void testConstructor() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, 1);
    IntIdentity c3 = new IntIdentity(Object.class, 2);
    assertEquals(c1, c2, "Equal IntIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal IntIdentity instances compare equal");
  }

  @Test
  void testIntegerConstructor() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, Integer.valueOf(1));
    IntIdentity c3 = new IntIdentity(Object.class, Integer.valueOf(2));
    assertEquals(c1, c2, "Equal intIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal IntIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, c1.toString());
    assertEquals(c1, c2, "Equal IntIdentity instances compare not equal.");
  }

  @Test
  void testStringConstructor() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, "1");
    IntIdentity c3 = new IntIdentity(Object.class, "2");
    assertEquals(c1, c2, "Equal IntIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal IntIdentity instances compare equal");
  }

  @Test
  void testIllegalStringConstructor() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new IntIdentity(Object.class, "b"),
        "No exception caught for illegal String.");
  }

  @Test
  void testSerialized() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, "1");
    IntIdentity c3 = new IntIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    assertEquals(c1, sc1, "Equal IntIdentity instances compare not equal.");
    assertEquals(c2, sc2, "Equal IntIdentity instances compare not equal.");
    assertEquals(sc1, c2, "Equal IntIdentity instances compare not equal.");
    assertEquals(sc2, c1, "Equal IntIdentity instances compare not equal.");
    assertNotEquals(c1, sc3, "Not equal InrIdentity instances compare equal.");
    assertNotEquals(sc1, c3, "Not equal IntIdentity instances compare equal.");
    assertNotEquals(sc1, sc3, "Not equal IntIdentity instances compare equal.");
    assertNotEquals(sc3, sc1, "Not equal IntIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObjectPrimitive() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    assertEquals(c1.getKeyAsObject(), Integer.valueOf(1), "keyAsObject doesn't match.");
  }

  @Test
  void testGetKeyAsObject() {
    IntIdentity c1 = new IntIdentity(Object.class, Integer.valueOf(1));
    assertEquals(c1.getKeyAsObject(), Integer.valueOf(1), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullIntegerParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new IntIdentity(Object.class, (Integer) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadConstructorNullStringParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new IntIdentity(Object.class, (String) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    IntIdentity c1 = new IntIdentity(Object.class, 1);
    IntIdentity c2 = new IntIdentity(Object.class, 1);
    IntIdentity c3 = new IntIdentity(Object.class, 2);
    IntIdentity c4 = new IntIdentity(Class.class, 1);
    assertEquals(0, c1.compareTo(c2), "Equal IntIdentity instances compare not equal.");
    assertTrue(c1.compareTo(c3) < 0, "Not equal IntIdentity instances have wrong compareTo result");
    assertTrue(c3.compareTo(c1) > 0, "Not equal IntIdentity instances have wrong compareTo result");
    assertTrue(c1.compareTo(c4) > 0, "Not equal IntIdentity instances have wrong compareTo result");
  }
}
