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
 * LongIdentityTest.java
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
class LongIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of LongIdentityTest */
  public LongIdentityTest() {}

  @Test
  void testConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, 1);
    LongIdentity c3 = new LongIdentity(Object.class, 2);
    LongIdentity c4 = new LongIdentity(Object.class, 0x100000001L);
    assertEquals(c1, c2, "Equal LongIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal LongIdentity instances compare equal");
    assertNotEquals(c4, c1, "Not equal LongIdentity instances compare equal");
  }

  @Test
  void testLongConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, Long.valueOf(1));
    LongIdentity c3 = new LongIdentity(Object.class, Long.valueOf(2));
    assertEquals(c1, c2, "Equal LongIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal LongIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, Long.MAX_VALUE);
    LongIdentity c2 = new LongIdentity(Object.class, c1.toString());
    assertEquals(c1, c2, "Equal LongIdentity instances compare not equal.");
  }

  @Test
  void testStringConstructor() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, "1");
    LongIdentity c3 = new LongIdentity(Object.class, "2");
    assertEquals(c1, c2, "Equal LongIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal LongIdentity instances compare equal");
  }

  @Test
  void testIllegalStringConstructor() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new LongIdentity(Object.class, "b"),
        "No exception caught for illegal String.");
  }

  @Test
  void testSerialized() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, "1");
    LongIdentity c3 = new LongIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    assertEquals(c1, sc1, "Equal LongIdentity instances compare not equal.");
    assertEquals(c2, sc2, "Equal LongIdentity instances compare not equal.");
    assertEquals(sc1, c2, "Equal LongIdentity instances compare not equal.");
    assertEquals(sc2, c1, "Equal LongIdentity instances compare not equal.");
    assertNotEquals(c1, sc3, "Not equal LongIdentity instances compare equal.");
    assertNotEquals(sc1, c3, "Not equal LongIdentity instances compare equal.");
    assertNotEquals(sc1, sc3, "Not equal LongIdentity instances compare equal.");
    assertNotEquals(sc3, sc1, "Not equal LongIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObjectPrimitive() {
    LongIdentity c1 = new LongIdentity(Object.class, 1L);
    assertEquals(c1.getKeyAsObject(), Long.valueOf(1L), "keyAsObject doesn't match.");
  }

  @Test
  void testGetKeyAsObject() {
    LongIdentity c1 = new LongIdentity(Object.class, Long.valueOf(1L));
    assertEquals(c1.getKeyAsObject(), Long.valueOf(1L), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullShortParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new LongIdentity(Object.class, (Long) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadConstructorNullStringParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new LongIdentity(Object.class, (String) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    LongIdentity c1 = new LongIdentity(Object.class, 1);
    LongIdentity c2 = new LongIdentity(Object.class, 1);
    LongIdentity c3 = new LongIdentity(Object.class, 2);
    LongIdentity c4 = new LongIdentity(Class.class, 1);
    LongIdentity c5 = new LongIdentity(Object.class, 0x100000001L);
    assertEquals(0, c1.compareTo(c2), "Equal LongIdentity instances compare not equal.");
    assertTrue(
        c1.compareTo(c3) < 0, "Not equal LongIdentity instances have wrong compareTo result");
    assertTrue(
        c3.compareTo(c1) > 0, "Not equal LongIdentity instances have wrong compareTo result");
    assertTrue(
        c1.compareTo(c4) > 0, "Not equal LongIdentity instances have wrong compareTo result");
    assertTrue(
        c5.compareTo(c1) > 0, "Not equal LongIdentity instances have wrong compareTo result");
  }
}
