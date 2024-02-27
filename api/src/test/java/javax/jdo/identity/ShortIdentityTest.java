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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** */
class ShortIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of ShortIdentityTest */
  public ShortIdentityTest() {}

  @Test
  void testConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c3 = new ShortIdentity(Object.class, (short) 2);
    Assertions.assertEquals(c1, c2, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ShortIdentity instances compare equal");
  }

  @Test
  void testShortConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, Short.valueOf((short) 1));
    ShortIdentity c3 = new ShortIdentity(Object.class, Short.valueOf((short) 2));
    Assertions.assertEquals(c1, c2, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ShortIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, Short.MAX_VALUE);
    ShortIdentity c2 = new ShortIdentity(Object.class, c1.toString());
    Assertions.assertEquals(c1, c2, "Equal ShortIdentity instances compare not equal.");
  }

  @Test
  void testStringConstructor() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, "1");
    ShortIdentity c3 = new ShortIdentity(Object.class, "2");
    Assertions.assertEquals(c1, c2, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ShortIdentity instances compare equal");
  }

  @Test
  void testIllegalStringConstructor() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new ShortIdentity(Object.class, "b"),
        "No exception caught for illegal String.");
  }

  @Test
  void testSerialized() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, "1");
    ShortIdentity c3 = new ShortIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ShortIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ShortIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ShortIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ShortIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ShortIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObjectPrimitive() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    Assertions.assertEquals(
        c1.getKeyAsObject(), Short.valueOf((short) 1), "keyAsObject doesn't match.");
  }

  @Test
  void testGetKeyAsObject() {
    ShortIdentity c1 = new ShortIdentity(Object.class, Short.valueOf((short) 1));
    Assertions.assertEquals(
        c1.getKeyAsObject(), Short.valueOf((short) 1), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullShortParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new ShortIdentity(Object.class, (Short) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadConstructorNullStringParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new ShortIdentity(Object.class, (String) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    ShortIdentity c1 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c2 = new ShortIdentity(Object.class, (short) 1);
    ShortIdentity c3 = new ShortIdentity(Object.class, (short) 2);
    ShortIdentity c4 = new ShortIdentity(Class.class, (short) 1);
    Assertions.assertEquals(
        0, c1.compareTo(c2), "Equal ShortIdentity instances compare not equal.");
    Assertions.assertTrue(
        c1.compareTo(c3) < 0, "Not equal ShortIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c3.compareTo(c1) > 0, "Not equal ShortIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c1.compareTo(c4) > 0, "Not equal ShortIdentity instances have wrong compareTo result");
  }
}
