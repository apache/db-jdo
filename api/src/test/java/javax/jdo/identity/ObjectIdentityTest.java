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
 * ObjectIdentityTest.java
 *
 */
package javax.jdo.identity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDONullIdentityException;
import javax.jdo.JDOUserException;
import javax.jdo.LegacyJava;
import javax.jdo.spi.JDOImplHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** */
class ObjectIdentityTest extends SingleFieldIdentityTest {

  /** The JDOImplHelper instance used for Date formatting. */
  private static final JDOImplHelper helper = doPrivileged(JDOImplHelper::getInstance);

  @SuppressWarnings("unchecked")
  private static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
    try {
      return (T) LegacyJava.doPrivilegedAction.invoke(null, privilegedAction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new JDOFatalInternalException(e.getMessage());
    }
  }

  /** Creates a new instance of ObjectIdentityTest */
  public ObjectIdentityTest() {}

  @Test
  void testConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, new IdClass(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new IdClass(2));
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testIntegerConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Integer.valueOf(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Integer.valueOf(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Integer.valueOf(2));
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testLongConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Long.valueOf(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Long.valueOf(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Long.valueOf(2));
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testDateConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new Date(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, new Date(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new Date(2));
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testLocaleConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Locale.US);
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Locale.US);
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Locale.GERMANY);
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testCurrencyConstructor() {
    if (!isClassLoadable("java.util.Currency")) return;
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.GERMANY));
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testStringConstructor() {
    ObjectIdentity c1 =
        new ObjectIdentity(Object.class, "javax.jdo.identity.ObjectIdentityTest$IdClass:1");
    ObjectIdentity c2 =
        new ObjectIdentity(Object.class, "javax.jdo.identity.ObjectIdentityTest$IdClass:1");
    ObjectIdentity c3 =
        new ObjectIdentity(Object.class, "javax.jdo.identity.ObjectIdentityTest$IdClass:2");
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, c3, "Not equal ObjectIdentity instances compare equal");
  }

  @Test
  void testToStringConstructor() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, c1.toString());
    Assertions.assertEquals(c1, c2, "Equal ObjectIdentity instances compare not equal.");
  }

  @Test
  void testDateCompareTo() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new Date(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, new Date(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new Date(2));
    ObjectIdentity c4 = new ObjectIdentity(Class.class, new Date(1));
    Assertions.assertEquals(
        0, c1.compareTo(c2), "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertTrue(
        c1.compareTo(c3) < 0, "Not equal ObjectIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c3.compareTo(c1) > 0, "Not equal ObjectIdentity instances have wrong compareTo result");
    Assertions.assertTrue(
        c1.compareTo(c4) > 0, "Not equal ObjectIdentity instances have wrong compareTo result");
  }

  @Test
  void testBadStringConstructorNullClass() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new ObjectIdentity(null, "1"),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadStringConstructorNullParam() {
    Assertions.assertThrows(
        JDONullIdentityException.class,
        () -> new ObjectIdentity(Object.class, null),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadStringConstructorTooShort() {
    Assertions.assertThrows(
        JDOUserException.class,
        () -> new ObjectIdentity(Object.class, "xx"),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadStringConstructorNoDelimiter() {
    Assertions.assertThrows(
        JDOUserException.class,
        () -> new ObjectIdentity(Object.class, "xxxxxxxxx"),
        "Failed to catch expected exception.");
  }

  @Test
  void testBadStringConstructorBadClassName() {
    JDOUserException ex =
        Assertions.assertThrows(
            JDOUserException.class,
            () -> new ObjectIdentity(Object.class, "xx:yy"),
            "Failed to catch expected ClassNotFoundException.");
    validateNestedException(ex, ClassNotFoundException.class);
  }

  @Test
  void testBadStringConstructorNoStringConstructor() {
    JDOUserException ex =
        Assertions.assertThrows(
            JDOUserException.class,
            () ->
                new ObjectIdentity(
                    Object.class,
                    "javax.jdo.identity.ObjectIdentityTest$BadIdClassNoStringConstructor:yy"),
            "Failed to catch expected NoSuchMethodException.");
    validateNestedException(ex, NoSuchMethodException.class);
  }

  @Test
  void testBadStringConstructorNoPublicStringConstructor() {
    JDOUserException ex =
        Assertions.assertThrows(
            JDOUserException.class,
            () ->
                new ObjectIdentity(
                    Object.class,
                    "javax.jdo.identity.ObjectIdentityTest$BadIdClassNoPublicStringConstructor:yy"),
            "Failed to catch expected NoSuchMethodException.");
    validateNestedException(ex, NoSuchMethodException.class);
  }

  @Test
  void testBadStringConstructorIllegalArgument() {
    JDOUserException ex =
        Assertions.assertThrows(
            JDOUserException.class,
            () ->
                new ObjectIdentity(
                    Object.class, "javax.jdo.identity.ObjectIdentityTest$IdClass:yy"),
            "Failed to catch expected InvocationTargetException.");
    validateNestedException(ex, InvocationTargetException.class);
  }

  @Test
  void testStringDateConstructor() {
    SimpleDateFormat usDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
    helper.registerDateFormat(usDateFormat);

    // construct date instance for 1.1.1970 00:00:00
    Calendar cal = Calendar.getInstance(Locale.US);
    cal.clear();
    cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
    Date date = cal.getTime();

    ObjectIdentity c1 = new ObjectIdentity(Object.class, "java.util.Date:Jan 01, 1970 00:00:00 AM");
    ObjectIdentity c2 = new ObjectIdentity(Object.class, date);
    Assertions.assertEquals(
        0, c1.compareTo(c2), "Equal ObjectIdentity instances compare not equal.");

    helper.registerDateFormat(DateFormat.getDateTimeInstance());
  }

  @Test
  void testStringDefaultDateConstructor() {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    Calendar cal = Calendar.getInstance();
    // nullify millisecond field since dateFormat does not take milliseconds into account
    cal.clear(Calendar.MILLISECOND);
    Date date = cal.getTime();
    String dateAsString = dateFormat.format(date);

    ObjectIdentity c1 = new ObjectIdentity(Object.class, "java.util.Date:" + dateAsString);
    ObjectIdentity c2 = new ObjectIdentity(Object.class, date);
    Assertions.assertEquals(
        0, c1.compareTo(c2), "Equal ObjectIdentity instances compare not equal.");
  }

  @Test
  void testBadStringDateConstructor() {
    Assertions.assertThrows(
        JDOUserException.class,
        () -> new ObjectIdentity(Object.class, "java.util.Date:Jop 1, 1970 00:00:00"),
        "Failed to catch expected Exception.");
  }

  @Test
  void testStringLocaleConstructorLanguage() {
    if (!isClassLoadable("java.util.Currency")) return;
    SingleFieldIdentity<ObjectIdentity> c1 =
        new ObjectIdentity(Object.class, "java.util.Locale:en");
    Assertions.assertEquals(new Locale("en"), c1.getKeyAsObject());
  }

  @Test
  void testStringLocaleConstructorCountry() {
    SingleFieldIdentity<ObjectIdentity> c1 =
        new ObjectIdentity(Object.class, "java.util.Locale:_US");
    Assertions.assertEquals(new Locale("", "US"), c1.getKeyAsObject());
  }

  @Test
  void testStringLocaleConstructorLanguageCountry() {
    SingleFieldIdentity<ObjectIdentity> c1 =
        new ObjectIdentity(Object.class, "java.util.Locale:en_US");
    Assertions.assertEquals(new Locale("en", "US"), c1.getKeyAsObject());
  }

  @Test
  void testStringLocaleConstructorLanguageCountryVariant() {
    SingleFieldIdentity<ObjectIdentity> c1 =
        new ObjectIdentity(Object.class, "java.util.Locale:en_US_MAC");
    Assertions.assertEquals(new Locale("en", "US", "MAC"), c1.getKeyAsObject());
  }

  @Test
  void testStringCurrencyConstructor() {
    if (!isClassLoadable("java.util.Currency")) return;
    ObjectIdentity c1 = new ObjectIdentity(Object.class, "java.util.Currency:USD");
    Assertions.assertEquals(
        Currency.getInstance("USD"), c1.getKeyAsObject(), "Expected USD currency");
  }

  @Test
  void testBadStringCurrencyConstructor() {
    if (!isClassLoadable("java.util.Currency")) return;
    JDOUserException ex =
        Assertions.assertThrows(
            JDOUserException.class,
            () -> new ObjectIdentity(Object.class, "java.util.Currency:NowhereInTheWorld"),
            "Failed to catch expected IllegalArgumentException.");
    validateNestedException(ex, IllegalArgumentException.class);
  }

  @Test
  void testSerializedIdClass() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, new IdClass(1));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new IdClass(2));
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ObjectIdentity instances compare equal.");
  }

  @Test
  void testSerializedBigDecimal() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new BigDecimal("123456789.012"));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, new BigDecimal("123456789.012"));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new BigDecimal("123456789.01"));
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ObjectIdentity instances compare equal.");
  }

  @Test
  void testSerializedCurrency() {
    if (!isClassLoadable("java.util.Currency")) return;
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.GERMANY));
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ObjectIdentity instances compare equal.");
  }

  @Test
  void testSerializedDate() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new Date(1));
    ObjectIdentity c2 = new ObjectIdentity(Object.class, "java.util.Date:1");
    ObjectIdentity c3 = new ObjectIdentity(Object.class, new Date(2));
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ObjectIdentity instances compare equal.");
  }

  @Test
  void testSerializedLocale() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, Locale.US);
    ObjectIdentity c2 = new ObjectIdentity(Object.class, Locale.US);
    ObjectIdentity c3 = new ObjectIdentity(Object.class, Locale.GERMANY);
    Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
    Object sc1 = scis[0];
    Object sc2 = scis[1];
    Object sc3 = scis[2];
    Assertions.assertEquals(c1, sc1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(c2, sc2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc1, c2, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertEquals(sc2, c1, "Equal ObjectIdentity instances compare not equal.");
    Assertions.assertNotEquals(c1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, c3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc1, sc3, "Not equal ObjectIdentity instances compare equal.");
    Assertions.assertNotEquals(sc3, sc1, "Not equal ObjectIdentity instances compare equal.");
  }

  @Test
  void testGetKeyAsObject() {
    ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
    Assertions.assertEquals(c1.getKeyAsObject(), new IdClass(1), "keyAsObject doesn't match.");
  }

  private <T> void validateNestedException(JDOUserException ex, Class<T> expected) {
    Throwable[] nesteds = ex.getNestedExceptions();
    if (nesteds == null || nesteds.length != 1) {
      Assertions.fail("Nested exception is null or length 0");
    }
    Throwable nested = nesteds[0];
    if (nested != ex.getCause()) {
      Assertions.fail("Nested exception is not == getCause()");
    }
    if (!(expected.isAssignableFrom(nested.getClass()))) {
      Assertions.fail(
          "Wrong nested exception. Expected " + expected.getName() + ", got " + nested.toString());
    }
    return;
  }

  public static class IdClass implements Serializable {
    private static final long serialVersionUID = 5718122068872969580L;

    public final int value;

    public IdClass() {
      value = 0;
    }

    public IdClass(int value) {
      this.value = value;
    }

    public IdClass(String str) {
      this.value = Integer.parseInt(str);
    }

    public String toString() {
      return Integer.toString(value);
    }

    public int hashCode() {
      return value;
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else {
        IdClass other = (IdClass) obj;
        return value == other.value;
      }
    }
  }

  public static class BadIdClassNoStringConstructor {}

  public static class BadIdClassNoPublicStringConstructor {
    private BadIdClassNoPublicStringConstructor(String str) {}
  }
}
