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
 * ByteIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** */
class ByteIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of ByteIdentityTest */
  public ByteIdentityTest() {}

  @Test
  void testConstructor() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c3 = new ByteIdentity(Object.class, (byte) 2);
    Assertions.assertEquals(c1, c2, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ByteIdentity instances compare equal");
  }

  @Test
  void testByteConstructor() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, Byte.valueOf((byte) 1));
    ByteIdentity c3 = new ByteIdentity(Object.class, Byte.valueOf((byte) 2));
    Assertions.assertEquals(c1, c2, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ByteIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, c1.toString());
    Assertions.assertEquals(c1, c2, "Equal ByteIdentity instances compare not equal.");
  }

  @Test
  void testStringConstructor() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, "1");
    ByteIdentity c3 = new ByteIdentity(Object.class, "2");
    Assertions.assertEquals(c1, c2, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ByteIdentity instances compare equal");
  }

  @Test
  void testIllegalStringConstructor() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ByteIdentity(Object.class, "b");
        },
        "No exception caught for illegal String.");
  }

  @Test
  void testSerialized() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, "1");
    ByteIdentity c3 = new ByteIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ByteIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ByteIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ByteIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ByteIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ByteIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObjectPrimitive() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    Assertions.assertEquals(
        c1.getKeyAsObject(), Byte.valueOf((byte) 1), "keyAsObject doesn't match.");
  }

  @Test
  void testGetKeyAsObject() {
    ByteIdentity c1 = new ByteIdentity(Object.class, Byte.valueOf((byte) 1));
    Assertions.assertEquals(
        c1.getKeyAsObject(), Byte.valueOf((byte) 1), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullByteParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new ByteIdentity(Object.class, (Byte) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadConstructorNullStringParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new ByteIdentity(Object.class, (String) null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    ByteIdentity c1 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c2 = new ByteIdentity(Object.class, (byte) 1);
    ByteIdentity c3 = new ByteIdentity(Object.class, (byte) 2);
    ByteIdentity c4 = new ByteIdentity(Class.class, (byte) 1);
    Assertions.assertEquals(0, c1.compareTo(c2), "Equal ByteIdentity instances compare not equal.");
    Assertions.assertTrue(
        c1.compareTo(c3) < 0, "Not equal ByteIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c3.compareTo(c1) > 0, "Not equal ByteIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c1.compareTo(c4) > 0, "Not equal ByteIdentity instances have wrong compareTo result");
  }
}
