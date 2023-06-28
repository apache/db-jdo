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
 * CharIdentityTest.java
 *
 */

package javax.jdo.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.jdo.JDONullIdentityException;
import org.junit.jupiter.api.Test;

/**
 * @author clr
 */
class CharIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of CharIdentityTest */
  public CharIdentityTest() {}

  @Test
  void testConstructor() {
    CharIdentity c1 = new CharIdentity(Object.class, 'a');
    CharIdentity c2 = new CharIdentity(Object.class, 'a');
    CharIdentity c3 = new CharIdentity(Object.class, 'b');
    assertEquals(c1, c2, "Equal CharIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal CharIdentity instances compare equal");
  }

  @Test
  void testCharacterConstructor() {
    CharIdentity c1 = new CharIdentity(Object.class, 'a');
    CharIdentity c2 = new CharIdentity(Object.class, Character.valueOf('a'));
    CharIdentity c3 = new CharIdentity(Object.class, Character.valueOf('b'));
    assertEquals(c1, c2, "Equal CharIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal CharIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    CharIdentity c1 = new CharIdentity(Object.class, 'a');
    CharIdentity c2 = new CharIdentity(Object.class, c1.toString());
    assertEquals(c1, c2, "Equal CharIdentity instances compare not equal.");
  }

  @Test
  void testStringConstructor() {
    CharIdentity c1 = new CharIdentity(Object.class, 'a');
    CharIdentity c2 = new CharIdentity(Object.class, "a");
    CharIdentity c3 = new CharIdentity(Object.class, "b");
    assertEquals(c1, c2, "Equal CharIdentity instances compare not equal.");
    assertNotEquals(c1, c3, "Not equal CharIdentity instances compare equal");
  }

  @Test
  void testStringConstructorTooLong() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new CharIdentity(Object.class, "ab"),
        "No exception caught for String too long.");
  }

  @Test
  void testStringConstructorTooShort() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new CharIdentity(Object.class, ""),
        "No exception caught for String too short.");
  }

  @Test
  void testSerialized() {
    CharIdentity c1 = new CharIdentity(Object.class, 'a');
    CharIdentity c2 = new CharIdentity(Object.class, "a");
    CharIdentity c3 = new CharIdentity(Object.class, "b");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    assertEquals(c1, sc1, "Equal CharIdentity instances compare not equal.");
    assertEquals(c2, sc2, "Equal CharIdentity instances compare not equal.");
    assertEquals(sc1, c2, "Equal CharIdentity instances compare not equal.");
    assertEquals(sc2, c1, "Equal CharIdentity instances compare not equal.");
    assertNotEquals(c1, sc3, "Not equal CharIdentity instances compare equal.");
    assertNotEquals(sc1, c3, "Not equal CharIdentity instances compare equal.");
    assertNotEquals(sc1, sc3, "Not equal CharIdentity instances compare equal.");
    assertNotEquals(sc3, sc1, "Not equal CharIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObjectPrimitive() {
    CharIdentity c1 = new CharIdentity(Object.class, '1');
    assertEquals(c1.getKeyAsObject(), Character.valueOf('1'), "keyAsObject doesn't match.");
  }

  @Test
  void testGetKeyAsObject() {
    CharIdentity c1 = new CharIdentity(Object.class, Character.valueOf('1'));
    assertEquals(c1.getKeyAsObject(), Character.valueOf('1'), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullCharacterParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new CharIdentity(Object.class, (Character) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadConstructorNullStringParam() {
    assertThrows(
        JDONullIdentityException.class,
        () -> new CharIdentity(Object.class, (String) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    CharIdentity c1 = new CharIdentity(Object.class, '1');
    CharIdentity c2 = new CharIdentity(Object.class, '1');
    CharIdentity c3 = new CharIdentity(Object.class, '2');
    CharIdentity c4 = new CharIdentity(Class.class, '1');
    assertEquals(0, c1.compareTo(c2), "Equal CharIdentity instances compare not equal.");
    assertTrue(
        c1.compareTo(c3) < 0, "Not equal CharIdentity instances have wrong compareTo result");
    assertTrue(
        c3.compareTo(c1) > 0, "Not equal CharIdentity instances have wrong compareTo result");
    assertTrue(
        c1.compareTo(c4) > 0, "Not equal CharIdentity instances have wrong compareTo result");
  }
}
