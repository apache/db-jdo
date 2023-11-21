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
 * StringIdentityTest.java
 *
 */

package javax.jdo.identity;

import javax.jdo.JDONullIdentityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** */
class StringIdentityTest extends SingleFieldIdentityTest {

  /** Creates a new instance of StringIdentityTest */
  public StringIdentityTest() {}

  @Test
  void testConstructor() {
    StringIdentity c1 = new StringIdentity(Object.class, "1");
    StringIdentity c2 = new StringIdentity(Object.class, "1");
    StringIdentity c3 = new StringIdentity(Object.class, "2");
    Assertions.assertEquals(c1, c2, "Equal StringIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal StringIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    StringIdentity c1 = new StringIdentity(Object.class, "Now who's talking!");
    StringIdentity c2 = new StringIdentity(Object.class, c1.toString());
    Assertions.assertEquals(c1, c2, "Equal StringIdentity instances compare not equal.");
  }

  @Test
  void testSerialized() {
    StringIdentity c1 = new StringIdentity(Object.class, "1");
    StringIdentity c2 = new StringIdentity(Object.class, "1");
    StringIdentity c3 = new StringIdentity(Object.class, "2");
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal StringIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal StringIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal StringIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal StringIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal StringIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal StringIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal StringIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal StringIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObject() {
    StringIdentity c1 = new StringIdentity(Object.class, "1");
    Assertions.assertEquals("1", c1.getKeyAsObject(), "keyAsObject doesn't match.");
  }

  @Test
  void testBadConstructorNullParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new StringIdentity(Object.class, null),
        "Failed to catch expected exception.");
  }

  @Test
  void testCompareTo() {
    StringIdentity c1 = new StringIdentity(Object.class, "1");
    StringIdentity c2 = new StringIdentity(Object.class, "1");
    StringIdentity c3 = new StringIdentity(Object.class, "2");
    StringIdentity c4 = new StringIdentity(Class.class, "1");
    Assertions.assertEquals(
        0, c1.compareTo(c2), "Equal StringIdentity instances compare not equal.");
    Assertions.assertTrue(
        c1.compareTo(c3) < 0, "Not equal StringIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c3.compareTo(c1) > 0, "Not equal StringIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c1.compareTo(c4) > 0, "Not equal StringIdentity instances have wrong compareTo result");
  }
}
