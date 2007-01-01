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
 * ObjectIdentityTest.java
 *
 */

package javax.jdo.identity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;

import java.math.BigDecimal;

import java.security.AccessController;
import java.security.PrivilegedAction;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.jdo.JDOUserException;
import javax.jdo.JDONullIdentityException;

import javax.jdo.spi.JDOImplHelper;

import javax.jdo.util.BatchTestRunner;

/**
 *
 */
public class ObjectIdentityTest extends SingleFieldIdentityTest {
    
    /** The JDOImplHelper instance used for Date formatting.
     */
    private static JDOImplHelper helper = (JDOImplHelper)
        AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return JDOImplHelper.getInstance();
                }
            }
        );
    
    /** Creates a new instance of ObjectIdentityTest */
    public ObjectIdentityTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ObjectIdentityTest.class);
    }
    
    public void testConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new IdClass(1));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new IdClass(2));
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testIntegerConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new Integer(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new Integer(1));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new Integer(2));
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testLongConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new Long(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new Long(1));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new Long(2));
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testDateConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new Date(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new Date(1));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new Date(2));
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testLocaleConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, Locale.US);
        ObjectIdentity c2 = new ObjectIdentity(Object.class, Locale.US);
        ObjectIdentity c3 = new ObjectIdentity(Object.class, Locale.GERMANY);
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testCurrencyConstructor() {
        if (!isClassLoadable("java.util.Currency")) return;
        ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                Currency.getInstance(Locale.US));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, 
                Currency.getInstance(Locale.US));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, 
                Currency.getInstance(Locale.GERMANY));
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testStringConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                "javax.jdo.identity.ObjectIdentityTest$IdClass:1");        
        ObjectIdentity c2 = new ObjectIdentity(Object.class, 
                "javax.jdo.identity.ObjectIdentityTest$IdClass:1");        
        ObjectIdentity c3 = new ObjectIdentity(Object.class, 
                "javax.jdo.identity.ObjectIdentityTest$IdClass:2");        
        assertEquals("Equal ObjectIdentity instances compare not equal.", c1, c2);
        assertFalse ("Not equal ObjectIdentity instances compare equal", c1.equals(c3));
    }
    
    public void testToStringConstructor() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, c1.toString());
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, c2);
    }

    public void testBadStringConstructorNullClass() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(null, "1");
        } catch (NullPointerException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }
    
    public void testBadStringConstructorNullParam() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, null);
        } catch (JDONullIdentityException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }
    
    public void testBadStringConstructorTooShort() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, "xx");
        } catch (JDOUserException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }
    
    public void testBadStringConstructorNoDelimiter() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, "xxxxxxxxx");
        } catch (JDOUserException ex) {
            return;
        }
        fail ("Failed to catch expected exception.");
    }
    
    public void testBadStringConstructorBadClassName() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, "xx:yy");
        } catch (JDOUserException ex) {
            validateNestedException(ex, ClassNotFoundException.class);
            return;
        }
        fail ("Failed to catch expected ClassNotFoundException.");
    }
    
    public void testBadStringConstructorNoStringConstructor() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                    "javax.jdo.identity.ObjectIdentityTest$BadIdClassNoStringConstructor:yy");
        } catch (JDOUserException ex) {
            validateNestedException(ex, NoSuchMethodException.class);
            return;
        }
        fail ("Failed to catch expected NoSuchMethodException.");
    }
    
    public void testBadStringConstructorNoPublicStringConstructor() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                    "javax.jdo.identity.ObjectIdentityTest$BadIdClassNoPublicStringConstructor:yy");
        } catch (JDOUserException ex) {
            validateNestedException(ex, NoSuchMethodException.class);
            return;
        }
        fail ("Failed to catch expected NoSuchMethodException.");
    }
    
    public void testBadStringConstructorIllegalArgument() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                    "javax.jdo.identity.ObjectIdentityTest$IdClass:yy");
        } catch (JDOUserException ex) {
            validateNestedException(ex, InvocationTargetException.class);
            return;
        }
        fail ("Failed to catch expected InvocationTargetException.");
    }

    public void testStringDateConstructor() {
        SimpleDateFormat usDateFormat = new SimpleDateFormat
                ("MMM dd, yyyy hh:mm:ss a", Locale.US);
        helper.registerDateFormat(usDateFormat);
        Object c1 = new ObjectIdentity(Object.class, 
            "java.util.Date:Jan 01, 1970 00:00:00 AM");
        helper.registerDateFormat(DateFormat.getDateTimeInstance());
    }

    public void testStringDefaultDateConstructor() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String rightNow = dateFormat.format(new Date());
        Object c1 = new ObjectIdentity(Object.class, 
            "java.util.Date:" + rightNow);
    }

    public void testBadStringDateConstructor() {
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                "java.util.Date:Jop 1, 1970 00:00:00");
        } catch (JDOUserException ex) {
            return;
        }
        fail ("Failed to catch expected Exception.");
    }

    public void testStringLocaleConstructorLanguage() {
        if (!isClassLoadable("java.util.Currency")) return;
        SingleFieldIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Locale:en");
        assertEquals(new Locale("en"), c1.getKeyAsObject());
    }

    public void testStringLocaleConstructorCountry() {
        SingleFieldIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Locale:_US");
        assertEquals(new Locale("","US"), c1.getKeyAsObject());
    }

    public void testStringLocaleConstructorLanguageCountry() {
        SingleFieldIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Locale:en_US");
        assertEquals(new Locale("en","US"), c1.getKeyAsObject());
    }

    public void testStringLocaleConstructorLanguageCountryVariant() {
        SingleFieldIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Locale:en_US_MAC");
        assertEquals(new Locale("en","US","MAC"), c1.getKeyAsObject());
    }

    public void testStringCurrencyConstructor() {
        if (!isClassLoadable("java.util.Currency")) return;
        SingleFieldIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Currency:USD");
    }

    public void testBadStringCurrencyConstructor() {
        if (!isClassLoadable("java.util.Currency")) return;
        try {
            ObjectIdentity c1 = new ObjectIdentity(Object.class, 
                    "java.util.Currency:NowhereInTheWorld");
        } catch (JDOUserException ex) {
            validateNestedException(ex, IllegalArgumentException.class);
            return;
        }
        fail ("Failed to catch expected IllegalArgumentException.");
    }

    public void testSerializedIdClass() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new IdClass(1));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new IdClass(2));
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ObjectIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testSerializedBigDecimal() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new BigDecimal("123456789.012"));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, new BigDecimal("123456789.012"));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new BigDecimal("123456789.01"));
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ObjectIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testSerializedCurrency() {
        if (!isClassLoadable("java.util.Currency")) return;
        ObjectIdentity c1 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.US));
        ObjectIdentity c3 = new ObjectIdentity(Object.class, Currency.getInstance(Locale.GERMANY));
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ObjectIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testSerializedDate() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new Date(1));
        ObjectIdentity c2 = new ObjectIdentity(Object.class, "java.util.Date:1");
        ObjectIdentity c3 = new ObjectIdentity(Object.class, new Date(2));
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ObjectIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testSerializedLocale() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, Locale.US);
        ObjectIdentity c2 = new ObjectIdentity(Object.class, Locale.US);
        ObjectIdentity c3 = new ObjectIdentity(Object.class, Locale.GERMANY);
        Object[] scis = writeReadSerialized(new Object[] {c1, c2, c3});
        Object sc1 = scis[0];
        Object sc2 = scis[1];
        Object sc3 = scis[2];
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c1, sc1);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", c2, sc2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc1, c2);
        assertEquals ("Equal ObjectIdentity instances compare not equal.", sc2, c1);
        assertFalse ("Not equal ObjectIdentity instances compare equal.", c1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(c3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc1.equals(sc3));
        assertFalse ("Not equal ObjectIdentity instances compare equal.", sc3.equals(sc1));
    }
    
    public void testGetKeyAsObject() {
        ObjectIdentity c1 = new ObjectIdentity(Object.class, new IdClass(1));
        assertEquals("keyAsObject doesn't match.", c1.getKeyAsObject(), new IdClass(1));
    }

    private void validateNestedException(JDOUserException ex, Class expected) {
        Throwable[] nesteds = ex.getNestedExceptions();
        if (nesteds == null || nesteds.length == 0) {
            fail ("Nested exception is null or length 0");
        }
        Throwable nested = nesteds[0];
        if (!(expected.isAssignableFrom(nested.getClass()))) {
            fail ("Wrong nested exception. Expected ClassNotFoundException, got "
                    + nested.toString());
        }
        return;
    }
    public static class IdClass implements Serializable {
        public int value;
        public IdClass() {value = 0;}
        public IdClass(int value) {this.value = value;}
        public IdClass(String str) {this.value = Integer.parseInt(str);}
        public String toString() {return Integer.toString(value);}
        public int hashCode() {
            return value;
        }
        public boolean equals (Object obj) {
            if (this == obj) {
                return true;
            } else {
                IdClass other = (IdClass) obj;
                return value == other.value;
            }
        }
    }
    
    public static class BadIdClassNoStringConstructor {
    }
    
    public static class BadIdClassNoPublicStringConstructor {
        private BadIdClassNoPublicStringConstructor(String str) {}
    }
}
