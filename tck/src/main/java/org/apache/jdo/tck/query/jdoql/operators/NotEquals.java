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

package org.apache.jdo.tck.query.jdoql.operators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Not Equals Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-15. <br>
 * <B>Assertion Description: </B> The not equal operator (<code>!=</code>) is supported for the
 * following types:
 *
 * <UL>
 *   <LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code>
 *   <LI><code>float, double, Float, Double</code>
 *   <LI><code>BigDecimal, BigInteger</code>
 *   <LI><code>Boolean, boolean</code>
 *   <LI>any class instance or array
 *   <LI><code>Date, String</code>
 * </UL>
 *
 * The operation on object-valued fields of wrapper types (<code>Boolean, Byte,
 * Short, Integer, Long, Float, and Double</code>), and numeric types (<code>BigDecimal</code> and
 * <code>BigInteger</code>) use the wrapped values as operands. Inequality comparison of
 * object-valued fields of <code>PersistenceCapable</code> types use the JDO Identity comparison of
 * the references. Thus, two objects will compare not equal if they have different JDO Identity.
 * Equality comparison of object-valued fields of non-<code>PersistenceCapable</code> types uses the
 * <code>equals</code> method of the field type.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotEquals extends ComparisonTests {
  private static final String boolean_filterL = "value != fld_boolean";
  private static final String boolean_filterR = "fld_boolean != value";
  private static final String boolean_filterT = "fld_boolean != true";
  private static final String boolean_filterF = "false != fld_boolean";
  private static final String boolean_filterObj = "value.fld_boolean != fld_boolean";

  private static final String byte_filterL = "value != fld_byte";
  private static final String byte_filterR = "fld_byte != value";
  private static final String byte_filterObj = "value.fld_byte != fld_byte";
  private static final String byte_filterVal = "fld_byte != 100";

  private static final String char_filterL = "value != fld_char";
  private static final String char_filterR = "fld_char != value";
  private static final String char_filterObj = "value.fld_char != fld_char";
  private static final String char_filterVal = "'M' != fld_char";

  private static final String double_filterL = "value != fld_double";
  private static final String double_filterR = "fld_double != value";
  private static final String double_filterObj = "value.fld_double != fld_double";
  private static final String double_filterVal = "fld_double != 100.0";

  private static final String float_filterL = "value != fld_float";
  private static final String float_filterR = "fld_float != value";
  private static final String float_filterObj = "fld_float != value.fld_float";
  private static final String float_filterVal = "fld_float != 100.0";

  private static final String int_filterL = "value != fld_int";
  private static final String int_filterR = "fld_int != value";
  private static final String int_filterObj = "value.fld_int != fld_int";
  private static final String int_filterVal = "fld_int != 1000";

  private static final String long_filterL = "value != fld_long";
  private static final String long_filterR = "fld_long != value";
  private static final String long_filterObj = "fld_long != value.fld_long";
  private static final String long_filterVal = "fld_long != 1000000";

  private static final String short_filterL = "value != fld_short";
  private static final String short_filterR = "fld_short != value";
  private static final String short_filterObj = "value.fld_short != fld_short";
  private static final String short_filterVal = "1000 != fld_short";

  private static final String Boolean_filterL = "value != fld_Boolean";
  private static final String Boolean_filterR = "fld_Boolean != value";
  private static final String Boolean_filterT = "fld_Boolean != true";
  private static final String Boolean_filterF = "false != fld_Boolean";
  private static final String Boolean_filterObj = "value.fld_Boolean != fld_Boolean";
  private static final String Boolean_filterVal = "fld_Boolean != false";

  private static final String Byte_filterL = "value != fld_Byte";
  private static final String Byte_filterR = "fld_Byte != value";
  private static final String Byte_filterObj = "fld_Byte != value.fld_Byte";
  private static final String Byte_filterVal = "100 != fld_Byte";

  private static final String Character_filterL = "value != fld_Character";
  private static final String Character_filterR = "fld_Character != value";
  private static final String Character_filterObj = "value.fld_Character != fld_Character";
  private static final String Character_filterVal = "fld_Character != 'z'";

  private static final String Double_filterL = "value != fld_Double";
  private static final String Double_filterR = "fld_Double != value";
  private static final String Double_filterObj = "value.fld_Double != fld_Double";
  private static final String Double_filterVal = "fld_Double != 100.0";

  private static final String Float_filterL = "value != fld_Float";
  private static final String Float_filterR = "fld_Float != value";
  private static final String Float_filterObj = "fld_Float != value.fld_Float";
  private static final String Float_filterVal = "100.0f != fld_Float";

  private static final String Integer_filterL = "value != fld_Integer";
  private static final String Integer_filterR = "fld_Integer != value";
  private static final String Integer_filterObj = "fld_Integer != value.fld_Integer";
  private static final String Integer_filterVal = "fld_Integer != 100";

  private static final String Long_filterL = "value != fld_Long";
  private static final String Long_filterR = "fld_Long != value";
  private static final String Long_filterObj = "value.fld_Long != fld_Long";
  private static final String Long_filterVal = "-1000 != fld_Long";

  private static final String Short_filterL = "value != fld_Short";
  private static final String Short_filterR = "fld_Short != value";
  private static final String Short_filterObj = "fld_Short != value.fld_Short";
  private static final String Short_filterVal = "-1000 != fld_Short";

  private static final String String_filterL = "value != fld_String";
  private static final String String_filterR = "fld_String != value";
  private static final String String_filterObj = "value.fld_String != fld_String";
  private static final String String_filterVal1 = "fld_String != \"Java\"";
  private static final String String_filterVal2 = "fld_String != \"\"";

  private static final String Locale_filterL = "value != fld_Locale";
  private static final String Locale_filterR = "fld_Locale != value";
  private static final String Locale_filterObj = "value.fld_Locale != fld_Locale";

  private static final String Date_filterL = "value != fld_Date";
  private static final String Date_filterR = "fld_Date != value";
  private static final String Date_filterObj = "fld_Date != value.fld_Date";

  private static final String BigDecimal_filterL = "value != fld_BigDecimal";
  private static final String BigDecimal_filterR = "fld_BigDecimal != value";
  private static final String BigDecimal_filterObj = "value.fld_BigDecimal != fld_BigDecimal";

  private static final String BigInteger_filterL = "value != fld_BigInteger";
  private static final String BigInteger_filterR = "fld_BigInteger != value";
  private static final String BigInteger_filterObj = "fld_BigInteger != value.fld_BigInteger";

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-15 (NotEquals) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBooleanPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testBooleanPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Boolean trueBoolean = Boolean.TRUE;
      Boolean falseBoolean = Boolean.FALSE;
      run_booleanQuery(pm, boolean_filterL, booleanParameter, trueBoolean, true, 6);
      run_booleanQuery(pm, boolean_filterL, booleanParameter, falseBoolean, false, 4);
      run_booleanQuery(pm, boolean_filterR, booleanParameter, trueBoolean, true, 6);
      run_booleanQuery(pm, boolean_filterR, booleanParameter, falseBoolean, false, 4);
      run_booleanQuery(pm, boolean_filterL, BooleanParameter, trueBoolean, true, 6);
      run_booleanQuery(pm, boolean_filterL, BooleanParameter, falseBoolean, false, 4);
      run_booleanQuery(pm, boolean_filterR, BooleanParameter, trueBoolean, true, 6);
      run_booleanQuery(pm, boolean_filterR, BooleanParameter, falseBoolean, false, 4);
      run_booleanQuery(pm, boolean_filterT, null, null, true, 6);
      run_booleanQuery(pm, boolean_filterF, null, null, false, 4);
      AllTypes alltypes = new AllTypes();
      alltypes.setboolean(true);
      run_booleanQuery(pm, boolean_filterObj, AllTypesParameter, alltypes, true, 6);
      alltypes.setboolean(false);
      run_booleanQuery(pm, boolean_filterObj, AllTypesParameter, alltypes, false, 4);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBytePrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testBytePrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_byteQuery(pm, byte_filterL, byteParameter, Byte.valueOf((byte) 50), (byte) 50, 8);
      run_byteQuery(
          pm, byte_filterR, byteParameter, Byte.valueOf(Byte.MIN_VALUE), Byte.MIN_VALUE, 9);
      run_byteQuery(pm, byte_filterL, ByteParameter, Byte.valueOf((byte) 20), (byte) 20, 10);
      run_byteQuery(
          pm, byte_filterR, ByteParameter, Byte.valueOf(Byte.MAX_VALUE), Byte.MAX_VALUE, 9);
      run_byteQuery(pm, byte_filterL, shortParameter, Short.valueOf((short) 50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, shortParameter, Short.valueOf((short) 50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterL, ShortParameter, Short.valueOf((short) -100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterR, ShortParameter, Short.valueOf((short) -100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, charParameter, Character.valueOf((char) 50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, charParameter, Character.valueOf((char) 50), (byte) 50, 8);
      run_byteQuery(
          pm, byte_filterL, CharacterParameter, Character.valueOf((char) 50), (byte) 50, 8);
      run_byteQuery(
          pm, byte_filterR, CharacterParameter, Character.valueOf((char) 100), (byte) 100, 9);
      run_byteQuery(pm, byte_filterL, intParameter, Integer.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, intParameter, Integer.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterL, IntegerParameter, Integer.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterR, IntegerParameter, Integer.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, longParameter, Long.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, longParameter, Long.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterL, LongParameter, Long.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterR, LongParameter, Long.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, floatParameter, Float.valueOf((float) 50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, floatParameter, Float.valueOf((float) 50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterL, FloatParameter, Float.valueOf((float) -100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterR, FloatParameter, Float.valueOf((float) -100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, doubleParameter, Double.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, doubleParameter, Double.valueOf(50), (byte) 50, 8);
      run_byteQuery(pm, byte_filterL, DoubleParameter, Double.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterR, DoubleParameter, Double.valueOf(-100), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, BigIntegerParameter, new BigInteger("50"), (byte) 50, 8);
      run_byteQuery(pm, byte_filterR, BigIntegerParameter, new BigInteger("-100"), (byte) -100, 9);
      run_byteQuery(pm, byte_filterL, BigDecimalParameter, new BigDecimal("100.0"), (byte) 100, 9);
      run_byteQuery(pm, byte_filterR, BigDecimalParameter, new BigDecimal("10.0"), (byte) 10, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setbyte((byte) 50);
      run_byteQuery(pm, byte_filterObj, AllTypesParameter, alltypes, (byte) 50, 8);
      alltypes.setbyte((byte) 45);
      run_byteQuery(pm, byte_filterObj, AllTypesParameter, alltypes, (byte) 45, 10);
      run_byteQuery(pm, byte_filterVal, null, null, (byte) 100, 9);
    } finally {
      cleanupPM(pm);
    }
  }
  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testShortPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testShortPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_shortQuery(pm, short_filterL, shortParameter, Short.valueOf((short) 100), (short) 100, 8);
      run_shortQuery(pm, short_filterR, shortParameter, Short.valueOf((short) 100), (short) 100, 8);
      run_shortQuery(
          pm, short_filterL, ShortParameter, Short.valueOf(Short.MIN_VALUE), Short.MIN_VALUE, 9);
      run_shortQuery(
          pm, short_filterR, ShortParameter, Short.valueOf((short) 253), (short) 253, 10);
      run_shortQuery(
          pm, short_filterR, shortParameter, Short.valueOf((short) 1000), (short) 1000, 9);
      run_shortQuery(pm, short_filterL, byteParameter, Byte.valueOf((byte) 75), (short) 75, 10);
      run_shortQuery(pm, short_filterR, byteParameter, Byte.valueOf((byte) 75), (short) 75, 10);
      run_shortQuery(pm, short_filterL, ByteParameter, Byte.valueOf((byte) 100), (short) 100, 8);
      run_shortQuery(pm, short_filterR, ByteParameter, Byte.valueOf((byte) 100), (short) 100, 8);
      run_shortQuery(
          pm, short_filterL, charParameter, Character.valueOf((char) 75), (short) 75, 10);
      run_shortQuery(
          pm, short_filterR, charParameter, Character.valueOf((char) 75), (short) 75, 10);
      run_shortQuery(
          pm, short_filterL, CharacterParameter, Character.valueOf((char) 100), (short) 100, 8);
      run_shortQuery(
          pm, short_filterR, CharacterParameter, Character.valueOf((char) 100), (short) 100, 8);
      run_shortQuery(pm, short_filterL, intParameter, Integer.valueOf(-10000), (short) -10000, 9);
      run_shortQuery(pm, short_filterR, intParameter, Integer.valueOf(-10000), (short) -10000, 9);
      run_shortQuery(pm, short_filterL, IntegerParameter, Integer.valueOf(10000), (short) 10000, 9);
      run_shortQuery(pm, short_filterR, IntegerParameter, Integer.valueOf(10000), (short) 10000, 9);
      run_shortQuery(pm, short_filterL, longParameter, Long.valueOf(10000), (short) 10000, 9);
      run_shortQuery(pm, short_filterR, longParameter, Long.valueOf(10000), (short) 10000, 9);
      run_shortQuery(pm, short_filterL, LongParameter, Long.valueOf(100), (short) 100, 8);
      run_shortQuery(pm, short_filterR, LongParameter, Long.valueOf(100), (short) 100, 8);
      run_shortQuery(
          pm, short_filterL, floatParameter, Float.valueOf((float) 23000), (short) 23000, 10);
      run_shortQuery(
          pm, short_filterR, floatParameter, Float.valueOf((float) 23000), (short) 23000, 10);
      run_shortQuery(pm, short_filterL, FloatParameter, Float.valueOf((float) 100), (short) 100, 8);
      run_shortQuery(pm, short_filterR, FloatParameter, Float.valueOf((float) 100), (short) 100, 8);
      run_shortQuery(pm, short_filterL, doubleParameter, Double.valueOf(-10000), (short) -10000, 9);
      run_shortQuery(pm, short_filterR, doubleParameter, Double.valueOf(-10000), (short) -10000, 9);
      run_shortQuery(pm, short_filterL, DoubleParameter, Double.valueOf(23), (short) 23, 10);
      run_shortQuery(pm, short_filterR, DoubleParameter, Double.valueOf(23), (short) 23, 10);
      run_shortQuery(
          pm, short_filterL, BigIntegerParameter, new BigInteger("999"), (short) 999, 10);
      run_shortQuery(
          pm, short_filterR, BigIntegerParameter, new BigInteger("-1000"), (short) -1000, 9);
      run_shortQuery(
          pm, short_filterL, BigDecimalParameter, new BigDecimal("100.0"), (short) 100, 8);
      run_shortQuery(pm, short_filterR, BigDecimalParameter, new BigDecimal("0.0"), (short) 0, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setshort((short) 100);
      run_shortQuery(pm, short_filterObj, AllTypesParameter, alltypes, (short) 100, 8);
      alltypes.setshort((short) 23);
      run_shortQuery(pm, short_filterObj, AllTypesParameter, alltypes, (short) 23, 10);
      run_shortQuery(pm, short_filterVal, null, null, (short) 1000, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCharPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testCharPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_charQuery(
          pm,
          char_filterL,
          charParameter,
          Character.valueOf(Character.MIN_VALUE),
          Character.MIN_VALUE,
          9);
      run_charQuery(
          pm,
          char_filterR,
          charParameter,
          Character.valueOf(Character.MAX_VALUE),
          Character.MAX_VALUE,
          9);
      run_charQuery(pm, char_filterL, charParameter, Character.valueOf('C'), 'C', 10);
      run_charQuery(pm, char_filterR, charParameter, Character.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterL, CharacterParameter, Character.valueOf(' '), ' ', 9);
      run_charQuery(pm, char_filterR, CharacterParameter, Character.valueOf('f'), 'f', 10);
      run_charQuery(
          pm,
          char_filterL,
          byteParameter,
          Byte.valueOf((byte) Character.MIN_VALUE),
          Character.MIN_VALUE,
          9);
      run_charQuery(pm, char_filterR, byteParameter, Byte.valueOf((byte) 122), 'z', 8);
      run_charQuery(pm, char_filterL, ByteParameter, Byte.valueOf((byte) 'a'), 'a', 10);
      run_charQuery(pm, char_filterR, ByteParameter, Byte.valueOf((byte) 'a'), 'a', 10);
      run_charQuery(pm, char_filterL, shortParameter, Short.valueOf((short) 'M'), 'M', 8);
      run_charQuery(pm, char_filterR, shortParameter, Short.valueOf((short) 'M'), 'M', 8);
      run_charQuery(pm, char_filterL, ShortParameter, Short.valueOf((short) 'A'), 'A', 9);
      run_charQuery(pm, char_filterR, ShortParameter, Short.valueOf((short) 'A'), 'A', 9);
      run_charQuery(pm, char_filterL, intParameter, Integer.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterR, intParameter, Integer.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterL, IntegerParameter, Integer.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterR, IntegerParameter, Integer.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterL, longParameter, Long.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterR, longParameter, Long.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterL, LongParameter, Long.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterR, LongParameter, Long.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterL, floatParameter, Float.valueOf((float) 123.222), 'x', 10);
      run_charQuery(pm, char_filterR, floatParameter, Float.valueOf((float) 123.222), 'x', 10);
      run_charQuery(pm, char_filterL, FloatParameter, Float.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterR, FloatParameter, Float.valueOf('z'), 'z', 8);
      run_charQuery(pm, char_filterL, doubleParameter, Double.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterR, doubleParameter, Double.valueOf('B'), 'B', 9);
      run_charQuery(pm, char_filterL, DoubleParameter, Double.valueOf('A'), 'A', 9);
      run_charQuery(pm, char_filterR, DoubleParameter, Double.valueOf('A'), 'A', 9);
      run_charQuery(
          pm, char_filterL, BigIntegerParameter, new BigInteger("65"), 'A', 9); // 'A' == 65
      run_charQuery(
          pm, char_filterR, BigIntegerParameter, new BigInteger("122"), 'z', 8); // 'z' == 122
      run_charQuery(pm, char_filterL, BigDecimalParameter, new BigDecimal("65.0"), 'A', 9);
      run_charQuery(
          pm, char_filterR, BigDecimalParameter, new BigDecimal("77.0"), 'M', 8); // 'M' == 77
      AllTypes alltypes = new AllTypes();
      alltypes.setchar('A');
      run_charQuery(pm, char_filterObj, AllTypesParameter, alltypes, 'A', 9);
      alltypes.setchar('b');
      run_charQuery(pm, char_filterObj, AllTypesParameter, alltypes, 'b', 10);
      run_charQuery(pm, char_filterVal, null, null, 'M', 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testIntPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testIntPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_intQuery(
          pm,
          int_filterL,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          9);
      run_intQuery(
          pm,
          int_filterR,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          9);
      run_intQuery(pm, int_filterR, intParameter, Integer.valueOf(23), 23, 10);
      run_intQuery(pm, int_filterL, IntegerParameter, Integer.valueOf(1000000), 1000000, 9);
      run_intQuery(pm, int_filterR, IntegerParameter, Integer.valueOf(1000), 1000, 9);
      run_intQuery(pm, int_filterL, byteParameter, Byte.valueOf((byte) 100), 100, 8);
      run_intQuery(pm, int_filterR, byteParameter, Byte.valueOf((byte) 0), 0, 9);
      run_intQuery(pm, int_filterL, ByteParameter, Byte.valueOf((byte) 100), 100, 8);
      run_intQuery(pm, int_filterR, ByteParameter, Byte.valueOf((byte) 0), 0, 9);
      run_intQuery(pm, int_filterL, shortParameter, Short.valueOf((short) 10000), 10000, 9);
      run_intQuery(pm, int_filterR, shortParameter, Short.valueOf((short) -1000), -1000, 9);
      run_intQuery(pm, int_filterL, ShortParameter, Short.valueOf((short) -1000), -1000, 9);
      run_intQuery(pm, int_filterR, ShortParameter, Short.valueOf((short) 10000), 10000, 9);
      run_intQuery(pm, int_filterL, charParameter, Character.valueOf((char) 10000), 10000, 9);
      run_intQuery(pm, int_filterR, charParameter, Character.valueOf((char) 0), 0, 9);
      run_intQuery(pm, int_filterL, CharacterParameter, Character.valueOf((char) 100), 100, 8);
      run_intQuery(pm, int_filterR, CharacterParameter, Character.valueOf((char) 10000), 10000, 9);
      run_intQuery(
          pm,
          int_filterL,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          9);
      run_intQuery(
          pm,
          int_filterR,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          9);
      run_intQuery(pm, int_filterL, LongParameter, Long.valueOf(10000), 10000, 9);
      run_intQuery(pm, int_filterR, LongParameter, Long.valueOf(43), 43, 10);
      run_intQuery(
          pm,
          int_filterL,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          9);
      run_intQuery(
          pm,
          int_filterR,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          9);
      run_intQuery(pm, int_filterL, FloatParameter, Float.valueOf((float) 10000), 10000, 9);
      run_intQuery(pm, int_filterR, FloatParameter, Float.valueOf((float) 43), 43, 10);
      run_intQuery(
          pm,
          int_filterL,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          9);
      run_intQuery(
          pm,
          int_filterR,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          9);
      run_intQuery(pm, int_filterL, DoubleParameter, Double.valueOf(10000), 10000, 9);
      run_intQuery(pm, int_filterR, DoubleParameter, Double.valueOf(43), 43, 10);
      run_intQuery(pm, int_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, 9);
      run_intQuery(pm, int_filterR, BigIntegerParameter, new BigInteger("-1000000"), -1000000, 9);
      run_intQuery(pm, int_filterL, BigDecimalParameter, new BigDecimal("1000000.0"), 1000000, 9);
      run_intQuery(pm, int_filterR, BigDecimalParameter, new BigDecimal("-1000000.0"), -1000000, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setint(100);
      run_intQuery(pm, int_filterObj, AllTypesParameter, alltypes, 100, 8);
      run_intQuery(pm, int_filterVal, null, null, 1000, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testLongPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testLongPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_longQuery(
          pm, long_filterL, longParameter, Long.valueOf(Long.MIN_VALUE), Long.MIN_VALUE, 9);
      run_longQuery(
          pm, long_filterR, longParameter, Long.valueOf(Long.MAX_VALUE), Long.MAX_VALUE, 9);
      run_longQuery(pm, long_filterL, LongParameter, Long.valueOf(100), 100, 8);
      run_longQuery(pm, long_filterR, LongParameter, Long.valueOf(23), 23, 10);
      run_longQuery(pm, long_filterL, byteParameter, Byte.valueOf((byte) 100), 100, 8);
      run_longQuery(pm, long_filterR, byteParameter, Byte.valueOf((byte) 0), 0, 9);
      run_longQuery(pm, long_filterL, ByteParameter, Byte.valueOf((byte) 100), 100, 8);
      run_longQuery(pm, long_filterR, ByteParameter, Byte.valueOf((byte) 0), 0, 9);
      run_longQuery(pm, long_filterL, shortParameter, Short.valueOf((short) -1000), -1000, 8);
      run_longQuery(pm, long_filterR, shortParameter, Short.valueOf((short) 1000), 1000, 9);
      run_longQuery(pm, long_filterL, ShortParameter, Short.valueOf((short) 100), 100, 8);
      run_longQuery(pm, long_filterR, ShortParameter, Short.valueOf((short) 32), 32, 10);
      run_longQuery(pm, long_filterL, charParameter, Character.valueOf((char) 0), 0, 9);
      run_longQuery(pm, long_filterR, charParameter, Character.valueOf((char) 100), 100, 8);
      run_longQuery(pm, long_filterL, CharacterParameter, Character.valueOf((char) 23), 23, 10);
      run_longQuery(pm, long_filterR, CharacterParameter, Character.valueOf((char) 0), 0, 9);
      run_longQuery(pm, long_filterL, intParameter, Integer.valueOf(100), 100, 8);
      run_longQuery(pm, long_filterR, intParameter, Integer.valueOf(0), 0, 9);
      run_longQuery(pm, long_filterL, IntegerParameter, Integer.valueOf(23), 23, 10);
      run_longQuery(pm, long_filterR, IntegerParameter, Integer.valueOf(1000000), 1000000, 9);
      run_longQuery(
          pm, long_filterL, floatParameter, Float.valueOf((float) -1000000.0), -1000000, 9);
      //    run_longQuery(pm, long_filterR, floatParameter, Float.valueOf((float)Long.MAX_VALUE),
      // Long.MAX_VALUE, 9);
      run_longQuery(pm, long_filterL, FloatParameter, Float.valueOf((float) 100), 100, 8);
      run_longQuery(pm, long_filterR, FloatParameter, Float.valueOf((float) 32), 32, 10);
      run_longQuery(pm, long_filterL, doubleParameter, Double.valueOf(-1000000.0), -1000000, 9);
      //    run_longQuery(pm, long_filterR, doubleParameter, Double.valueOf((double)Long.MAX_VALUE),
      // Long.MAX_VALUE, 9);
      run_longQuery(pm, long_filterL, DoubleParameter, Double.valueOf(100), 100, 8);
      run_longQuery(pm, long_filterR, DoubleParameter, Double.valueOf(32), 32, 10);
      run_longQuery(pm, long_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, 9);
      run_longQuery(pm, long_filterR, BigIntegerParameter, new BigInteger("-1000000"), -1000000, 9);
      run_longQuery(pm, long_filterL, BigDecimalParameter, new BigDecimal("10000.0"), 10000, 10);
      run_longQuery(
          pm, long_filterR, BigDecimalParameter, new BigDecimal("-1000000.0"), -1000000, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setlong(100);
      run_longQuery(pm, long_filterObj, AllTypesParameter, alltypes, 100, 8);
      run_longQuery(pm, long_filterVal, null, null, 1000000, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFloatPrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testFloatPrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_floatQuery(pm, float_filterL, floatParameter, Float.valueOf(100.0f), 100.0f, 8);
      run_floatQuery(pm, float_filterR, floatParameter, Float.valueOf(100.0f), 100.0f, 8);
      run_floatQuery(pm, float_filterL, FloatParameter, Float.valueOf((float) 0.0), 0.0f, 9);
      run_floatQuery(pm, float_filterR, FloatParameter, Float.valueOf((float) 4.0), 4.0f, 10);
      run_floatQuery(pm, float_filterL, byteParameter, Byte.valueOf((byte) 0), 0.0f, 9);
      run_floatQuery(pm, float_filterR, byteParameter, Byte.valueOf((byte) 23), 23.0f, 10);
      run_floatQuery(pm, float_filterL, ByteParameter, Byte.valueOf((byte) 34), 34.0f, 10);
      run_floatQuery(pm, float_filterR, ByteParameter, Byte.valueOf((byte) 100), 100.0f, 8);
      run_floatQuery(pm, float_filterL, shortParameter, Short.valueOf((short) 0), 0.0f, 9);
      run_floatQuery(pm, float_filterR, shortParameter, Short.valueOf((short) 23), 23.0f, 10);
      run_floatQuery(pm, float_filterL, ShortParameter, Short.valueOf((short) 34), 34.0f, 10);
      run_floatQuery(pm, float_filterR, ShortParameter, Short.valueOf((short) 100), 100.0f, 8);
      run_floatQuery(pm, float_filterL, charParameter, Character.valueOf((char) 0), 0.0f, 9);
      run_floatQuery(pm, float_filterR, charParameter, Character.valueOf((char) 23), 23.0f, 10);
      run_floatQuery(
          pm, float_filterL, CharacterParameter, Character.valueOf((char) 34), 34.0f, 10);
      run_floatQuery(
          pm, float_filterR, CharacterParameter, Character.valueOf((char) 100), 100.0f, 8);
      run_floatQuery(pm, float_filterL, intParameter, Integer.valueOf(50000000), 50000000.0f, 9);
      run_floatQuery(pm, float_filterR, intParameter, Integer.valueOf(23), 23.0f, 10);
      run_floatQuery(pm, float_filterL, IntegerParameter, Integer.valueOf(34), 34.0f, 10);
      run_floatQuery(pm, float_filterR, IntegerParameter, Integer.valueOf(100), 100.0f, 8);
      run_floatQuery(pm, float_filterL, longParameter, Long.valueOf(50000000), 50000000.0f, 9);
      run_floatQuery(pm, float_filterR, longParameter, Long.valueOf(23), 23.0f, 10);
      run_floatQuery(pm, float_filterL, LongParameter, Long.valueOf(34), 34.0f, 10);
      run_floatQuery(pm, float_filterR, LongParameter, Long.valueOf(100), 100.0f, 8);
      run_floatQuery(
          pm, float_filterL, doubleParameter, Double.valueOf(50000000.0f), 50000000.0f, 9);
      run_floatQuery(pm, float_filterR, doubleParameter, Double.valueOf(0.0f), 0.0f, 9);
      run_floatQuery(pm, float_filterL, DoubleParameter, Double.valueOf(0.0f), 0.0f, 9);
      run_floatQuery(pm, float_filterR, DoubleParameter, Double.valueOf(100.0f), 100.0f, 8);
      run_floatQuery(pm, float_filterL, BigIntegerParameter, new BigInteger("0"), 0.0f, 9);
      run_floatQuery(
          pm, float_filterR, BigIntegerParameter, new BigInteger("1000000000"), 1000000000.0f, 9);
      run_floatQuery(
          pm, float_filterL, BigDecimalParameter, new BigDecimal("-234.23"), -234.23f, 9);
      run_floatQuery(pm, float_filterR, BigDecimalParameter, new BigDecimal("100.0"), 100.0f, 8);
      AllTypes alltypes = new AllTypes();
      alltypes.setfloat(23.23f);
      run_floatQuery(pm, float_filterObj, AllTypesParameter, alltypes, 23.23f, 10);
      run_floatQuery(pm, float_filterVal, null, null, 100.0f, 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDoublePrimitiveQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testDoublePrimitiveQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_doubleQuery(pm, double_filterL, doubleParameter, Double.valueOf(350.5), 350.5, 9);
      run_doubleQuery(pm, double_filterR, doubleParameter, Double.valueOf(350.5), 350.5, 9);
      run_doubleQuery(pm, double_filterL, DoubleParameter, Double.valueOf(0.0), 0.0, 9);
      run_doubleQuery(pm, double_filterR, DoubleParameter, Double.valueOf(23.34), 23.34, 10);
      run_doubleQuery(pm, double_filterL, byteParameter, Byte.valueOf((byte) 100), 100.0, 8);
      run_doubleQuery(pm, double_filterR, byteParameter, Byte.valueOf((byte) 0), 0.0, 9);
      run_doubleQuery(pm, double_filterL, ByteParameter, Byte.valueOf((byte) 23), 23.0, 10);
      run_doubleQuery(pm, double_filterR, ByteParameter, Byte.valueOf((byte) 100), 100.0, 8);
      run_doubleQuery(pm, double_filterL, shortParameter, Short.valueOf((short) 100), 100.0, 8);
      run_doubleQuery(pm, double_filterR, shortParameter, Short.valueOf((short) 0), 0.0, 9);
      run_doubleQuery(pm, double_filterL, ShortParameter, Short.valueOf((short) 23), 23.0, 10);
      run_doubleQuery(pm, double_filterR, ShortParameter, Short.valueOf((short) 100), 100.0, 8);
      run_doubleQuery(pm, double_filterL, intParameter, Integer.valueOf(100), 100.0, 8);
      run_doubleQuery(pm, double_filterR, intParameter, Integer.valueOf(0), 0.0, 9);
      run_doubleQuery(pm, double_filterL, IntegerParameter, Integer.valueOf(23), 23.0, 10);
      run_doubleQuery(pm, double_filterR, IntegerParameter, Integer.valueOf(100), 100.0, 8);
      run_doubleQuery(pm, double_filterL, longParameter, Long.valueOf(100), 100.0, 8);
      run_doubleQuery(pm, double_filterR, longParameter, Long.valueOf(0), 0.0, 9);
      run_doubleQuery(pm, double_filterL, LongParameter, Long.valueOf(23), 23.0, 10);
      run_doubleQuery(pm, double_filterR, LongParameter, Long.valueOf(100), 100.0, 8);
      run_doubleQuery(pm, double_filterL, floatParameter, Float.valueOf(0.0f), 0.0, 9);
      run_doubleQuery(pm, double_filterR, floatParameter, Float.valueOf(100.0f), 100.0, 8);
      run_doubleQuery(pm, double_filterL, FloatParameter, Float.valueOf(100.0f), 100.0, 8);
      run_doubleQuery(pm, double_filterR, FloatParameter, Float.valueOf(69.96f), 69.96, 10);
      run_doubleQuery(
          pm, double_filterL, BigIntegerParameter, new BigInteger("50000000"), 50000000.0, 9);
      run_doubleQuery(
          pm, double_filterR, BigIntegerParameter, new BigInteger("1000000000"), 1000000000.0, 9);
      run_doubleQuery(pm, double_filterL, BigDecimalParameter, new BigDecimal("350.5"), 350.5, 9);
      run_doubleQuery(
          pm, double_filterR, BigDecimalParameter, new BigDecimal("-234234.234"), -234234.234, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setdouble(0.0);
      run_doubleQuery(pm, double_filterObj, AllTypesParameter, alltypes, 0.0, 9);
      run_doubleQuery(pm, double_filterVal, null, null, 100.0, 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBooleanQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testBooleanQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Boolean trueBoolean = Boolean.TRUE;
    Boolean falseBoolean = Boolean.FALSE;
    try {
      run_BooleanQuery(pm, Boolean_filterL, booleanParameter, trueBoolean, trueBoolean, 6);
      run_BooleanQuery(pm, Boolean_filterL, booleanParameter, falseBoolean, falseBoolean, 4);
      run_BooleanQuery(pm, Boolean_filterR, booleanParameter, trueBoolean, trueBoolean, 6);
      run_BooleanQuery(pm, Boolean_filterR, booleanParameter, falseBoolean, falseBoolean, 4);
      run_BooleanQuery(pm, Boolean_filterL, BooleanParameter, trueBoolean, trueBoolean, 6);
      run_BooleanQuery(pm, Boolean_filterL, BooleanParameter, falseBoolean, falseBoolean, 4);
      run_BooleanQuery(pm, Boolean_filterR, BooleanParameter, trueBoolean, trueBoolean, 6);
      run_BooleanQuery(pm, Boolean_filterR, BooleanParameter, falseBoolean, falseBoolean, 4);
      run_BooleanQuery(pm, Boolean_filterT, null, trueBoolean, trueBoolean, 6);
      run_BooleanQuery(pm, Boolean_filterF, null, falseBoolean, falseBoolean, 4);
      AllTypes alltypes = new AllTypes();
      alltypes.setBoolean(trueBoolean);
      run_BooleanQuery(pm, Boolean_filterObj, AllTypesParameter, alltypes, trueBoolean, 6);
      alltypes.setBoolean(falseBoolean);
      run_BooleanQuery(pm, Boolean_filterObj, AllTypesParameter, alltypes, falseBoolean, 4);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testByteQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testByteQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_ByteQuery(
          pm, Byte_filterL, byteParameter, Byte.valueOf((byte) 50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm,
          Byte_filterR,
          byteParameter,
          Byte.valueOf(Byte.MIN_VALUE),
          Byte.valueOf(Byte.MIN_VALUE),
          9);
      run_ByteQuery(
          pm, Byte_filterL, ByteParameter, Byte.valueOf((byte) 20), Byte.valueOf((byte) 20), 10);
      run_ByteQuery(
          pm,
          Byte_filterR,
          ByteParameter,
          Byte.valueOf(Byte.MAX_VALUE),
          Byte.valueOf(Byte.MAX_VALUE),
          9);
      run_ByteQuery(
          pm, Byte_filterL, shortParameter, Short.valueOf((short) 50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterR, shortParameter, Short.valueOf((short) 50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm,
          Byte_filterL,
          ShortParameter,
          Short.valueOf((short) -100),
          Byte.valueOf((byte) -100),
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          ShortParameter,
          Short.valueOf((short) -100),
          Byte.valueOf((byte) -100),
          9);
      run_ByteQuery(
          pm,
          Byte_filterL,
          charParameter,
          Character.valueOf((char) 50),
          Byte.valueOf((byte) 50),
          8);
      run_ByteQuery(
          pm,
          Byte_filterR,
          charParameter,
          Character.valueOf((char) 50),
          Byte.valueOf((byte) 50),
          8);
      run_ByteQuery(
          pm,
          Byte_filterL,
          CharacterParameter,
          Character.valueOf((char) 50),
          Byte.valueOf((byte) 50),
          8);
      run_ByteQuery(
          pm,
          Byte_filterR,
          CharacterParameter,
          Character.valueOf((char) 75),
          Byte.valueOf((byte) 75),
          9);
      run_ByteQuery(
          pm, Byte_filterL, intParameter, Integer.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterR, intParameter, Integer.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterL, IntegerParameter, Integer.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterR, IntegerParameter, Integer.valueOf(75), Byte.valueOf((byte) 75), 9);
      run_ByteQuery(pm, Byte_filterL, longParameter, Long.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(pm, Byte_filterR, longParameter, Long.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterL, LongParameter, Long.valueOf(-100), Byte.valueOf((byte) -100), 9);
      run_ByteQuery(
          pm, Byte_filterR, LongParameter, Long.valueOf(-100), Byte.valueOf((byte) -100), 9);
      run_ByteQuery(
          pm, Byte_filterL, floatParameter, Float.valueOf((float) 50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterR, floatParameter, Float.valueOf((float) 50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm,
          Byte_filterL,
          FloatParameter,
          Float.valueOf((float) -100),
          Byte.valueOf((byte) -100),
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          FloatParameter,
          Float.valueOf((float) -100),
          Byte.valueOf((byte) -100),
          9);
      run_ByteQuery(
          pm, Byte_filterL, doubleParameter, Double.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterR, doubleParameter, Double.valueOf(50), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm, Byte_filterL, DoubleParameter, Double.valueOf(-100), Byte.valueOf((byte) -100), 9);
      run_ByteQuery(
          pm, Byte_filterR, DoubleParameter, Double.valueOf(-100), Byte.valueOf((byte) -100), 9);
      run_ByteQuery(
          pm, Byte_filterL, BigIntegerParameter, new BigInteger("50"), Byte.valueOf((byte) 50), 8);
      run_ByteQuery(
          pm,
          Byte_filterR,
          BigIntegerParameter,
          new BigInteger("-100"),
          Byte.valueOf((byte) -100),
          9);
      run_ByteQuery(
          pm,
          Byte_filterL,
          BigDecimalParameter,
          new BigDecimal("100.0"),
          Byte.valueOf((byte) 100),
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          BigDecimalParameter,
          new BigDecimal("10.0"),
          Byte.valueOf((byte) 10),
          9);
      Byte val = Byte.valueOf((byte) 50);
      AllTypes alltypes = new AllTypes();
      alltypes.setByte(val);
      run_ByteQuery(pm, Byte_filterObj, AllTypesParameter, alltypes, val, 8);
      val = Byte.valueOf((byte) 45);
      alltypes.setByte(val);
      run_ByteQuery(pm, Byte_filterObj, AllTypesParameter, alltypes, val, 10);
      run_ByteQuery(pm, Byte_filterVal, null, null, Byte.valueOf((byte) 100), 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testShortQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testShortQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_ShortQuery(
          pm,
          Short_filterL,
          shortParameter,
          Short.valueOf((short) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterR,
          shortParameter,
          Short.valueOf((short) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterL,
          ShortParameter,
          Short.valueOf(Short.MIN_VALUE),
          Short.valueOf(Short.MIN_VALUE),
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          ShortParameter,
          Short.valueOf((short) 253),
          Short.valueOf((short) 253),
          10);
      run_ShortQuery(
          pm,
          Short_filterR,
          shortParameter,
          Short.valueOf((short) 1000),
          Short.valueOf((short) 1000),
          9);
      run_ShortQuery(
          pm, Short_filterL, byteParameter, Byte.valueOf((byte) 75), Short.valueOf((short) 75), 10);
      run_ShortQuery(
          pm, Short_filterR, byteParameter, Byte.valueOf((byte) 75), Short.valueOf((short) 75), 10);
      run_ShortQuery(
          pm,
          Short_filterL,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterR,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterL,
          charParameter,
          Character.valueOf((char) 75),
          Short.valueOf((short) 75),
          10);
      run_ShortQuery(
          pm,
          Short_filterR,
          charParameter,
          Character.valueOf((char) 75),
          Short.valueOf((short) 75),
          10);
      run_ShortQuery(
          pm,
          Short_filterL,
          CharacterParameter,
          Character.valueOf((char) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterR,
          CharacterParameter,
          Character.valueOf((char) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterL,
          intParameter,
          Integer.valueOf(-10000),
          Short.valueOf((short) -10000),
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          intParameter,
          Integer.valueOf(-10000),
          Short.valueOf((short) -10000),
          9);
      run_ShortQuery(
          pm,
          Short_filterL,
          IntegerParameter,
          Integer.valueOf(10000),
          Short.valueOf((short) 10000),
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          IntegerParameter,
          Integer.valueOf(10000),
          Short.valueOf((short) 10000),
          9);
      run_ShortQuery(
          pm, Short_filterL, longParameter, Long.valueOf(10000), Short.valueOf((short) 10000), 9);
      run_ShortQuery(
          pm, Short_filterR, longParameter, Long.valueOf(10000), Short.valueOf((short) 10000), 9);
      run_ShortQuery(
          pm, Short_filterL, LongParameter, Long.valueOf(100), Short.valueOf((short) 100), 8);
      run_ShortQuery(
          pm, Short_filterR, LongParameter, Long.valueOf(100), Short.valueOf((short) 100), 8);
      run_ShortQuery(
          pm,
          Short_filterL,
          floatParameter,
          Float.valueOf((float) 23000),
          Short.valueOf((short) 23000),
          10);
      run_ShortQuery(
          pm,
          Short_filterR,
          floatParameter,
          Float.valueOf((float) 23000),
          Short.valueOf((short) 23000),
          10);
      run_ShortQuery(
          pm,
          Short_filterL,
          FloatParameter,
          Float.valueOf((float) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterR,
          FloatParameter,
          Float.valueOf((float) 100),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterL,
          doubleParameter,
          Double.valueOf(-10000),
          Short.valueOf((short) -10000),
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          doubleParameter,
          Double.valueOf(-10000),
          Short.valueOf((short) -10000),
          9);
      run_ShortQuery(
          pm, Short_filterL, DoubleParameter, Double.valueOf(23), Short.valueOf((short) 23), 10);
      run_ShortQuery(
          pm, Short_filterR, DoubleParameter, Double.valueOf(23), Short.valueOf((short) 23), 10);
      run_ShortQuery(
          pm,
          Short_filterL,
          BigIntegerParameter,
          new BigInteger("999"),
          Short.valueOf((short) 999),
          10);
      run_ShortQuery(
          pm,
          Short_filterR,
          BigIntegerParameter,
          new BigInteger("-1000"),
          Short.valueOf((short) -1000),
          9);
      run_ShortQuery(
          pm,
          Short_filterL,
          BigDecimalParameter,
          new BigDecimal("100.0"),
          Short.valueOf((short) 100),
          8);
      run_ShortQuery(
          pm,
          Short_filterR,
          BigDecimalParameter,
          new BigDecimal("10000.0"),
          Short.valueOf((short) 10000),
          9);
      Short sval = Short.valueOf((short) 100);
      AllTypes alltypes = new AllTypes();
      alltypes.setShort(sval);
      run_ShortQuery(pm, Short_filterObj, AllTypesParameter, alltypes, sval, 8);
      sval = Short.valueOf((short) 23);
      alltypes.setShort(sval);
      run_ShortQuery(pm, Short_filterObj, AllTypesParameter, alltypes, sval, 10);
      run_ShortQuery(pm, Short_filterVal, null, null, Short.valueOf((short) -1000), 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCharacterQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testCharacterQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_CharacterQuery(
          pm,
          Character_filterL,
          charParameter,
          Character.valueOf(Character.MIN_VALUE),
          Character.valueOf(Character.MIN_VALUE),
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          charParameter,
          Character.valueOf(Character.MAX_VALUE),
          Character.valueOf(Character.MAX_VALUE),
          9);
      run_CharacterQuery(
          pm, Character_filterL, charParameter, Character.valueOf('C'), Character.valueOf('C'), 10);
      run_CharacterQuery(
          pm, Character_filterR, charParameter, Character.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm,
          Character_filterL,
          CharacterParameter,
          Character.valueOf(' '),
          Character.valueOf(' '),
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          CharacterParameter,
          Character.valueOf('f'),
          Character.valueOf('f'),
          10);
      run_CharacterQuery(
          pm,
          Character_filterL,
          byteParameter,
          Byte.valueOf((byte) Character.MIN_VALUE),
          Character.valueOf(Character.MIN_VALUE),
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          ByteParameter,
          Byte.valueOf((byte) 'a'),
          Character.valueOf('a'),
          10);
      run_CharacterQuery(
          pm,
          Character_filterL,
          shortParameter,
          Short.valueOf((short) 'M'),
          Character.valueOf('M'),
          8);
      run_CharacterQuery(
          pm,
          Character_filterR,
          shortParameter,
          Short.valueOf((short) 'F'),
          Character.valueOf('F'),
          9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          ShortParameter,
          Short.valueOf((short) 'A'),
          Character.valueOf('A'),
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          ShortParameter,
          Short.valueOf((short) 'A'),
          Character.valueOf('A'),
          9);
      run_CharacterQuery(
          pm, Character_filterL, intParameter, Integer.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterR, intParameter, Integer.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterL, IntegerParameter, Integer.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm, Character_filterR, IntegerParameter, Integer.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm, Character_filterL, longParameter, Long.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterR, longParameter, Long.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterL, LongParameter, Long.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm, Character_filterR, LongParameter, Long.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          floatParameter,
          Float.valueOf((float) 123.222),
          Character.valueOf('x'),
          10);
      run_CharacterQuery(
          pm,
          Character_filterR,
          floatParameter,
          Float.valueOf((float) 123.222),
          Character.valueOf('x'),
          10);
      run_CharacterQuery(
          pm, Character_filterL, FloatParameter, Float.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterR, FloatParameter, Float.valueOf('z'), Character.valueOf('z'), 8);
      run_CharacterQuery(
          pm, Character_filterL, doubleParameter, Double.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm, Character_filterR, doubleParameter, Double.valueOf('B'), Character.valueOf('B'), 9);
      run_CharacterQuery(
          pm, Character_filterL, DoubleParameter, Double.valueOf('A'), Character.valueOf('A'), 9);
      run_CharacterQuery(
          pm, Character_filterR, DoubleParameter, Double.valueOf('A'), Character.valueOf('A'), 9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          BigIntegerParameter,
          new BigInteger("65"),
          Character.valueOf('A'),
          9); // 'A' == 65
      run_CharacterQuery(
          pm,
          Character_filterR,
          BigIntegerParameter,
          new BigInteger("122"),
          Character.valueOf('z'),
          8); // 'z' == 122
      run_CharacterQuery(
          pm,
          Character_filterL,
          BigDecimalParameter,
          new BigDecimal("65.000000"),
          Character.valueOf('A'),
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          BigDecimalParameter,
          new BigDecimal("77.0"),
          Character.valueOf('M'),
          8); // 'M' == 77
      AllTypes alltypes = new AllTypes();
      alltypes.setCharacter(Character.valueOf('A'));
      run_CharacterQuery(
          pm, Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('A'), 9);
      alltypes.setCharacter(Character.valueOf('b'));
      run_CharacterQuery(
          pm, Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('b'), 10);
      run_CharacterQuery(pm, Character_filterVal, null, null, Character.valueOf('z'), 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testIntegerQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testIntegerQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_IntegerQuery(
          pm,
          Integer_filterL,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          9);
      run_IntegerQuery(
          pm, Integer_filterR, intParameter, Integer.valueOf(23), Integer.valueOf(23), 10);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          IntegerParameter,
          Integer.valueOf(1000000),
          Integer.valueOf(1000000),
          9);
      run_IntegerQuery(
          pm, Integer_filterR, IntegerParameter, Integer.valueOf(1000), Integer.valueOf(1000), 9);
      run_IntegerQuery(
          pm, Integer_filterL, byteParameter, Byte.valueOf((byte) 100), Integer.valueOf(100), 8);
      run_IntegerQuery(
          pm, Integer_filterR, byteParameter, Byte.valueOf((byte) 0), Integer.valueOf(0), 9);
      run_IntegerQuery(
          pm, Integer_filterL, ByteParameter, Byte.valueOf((byte) 100), Integer.valueOf(100), 8);
      run_IntegerQuery(
          pm, Integer_filterR, ByteParameter, Byte.valueOf((byte) 0), Integer.valueOf(0), 9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          shortParameter,
          Short.valueOf((short) 10000),
          Integer.valueOf(10000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          shortParameter,
          Short.valueOf((short) -1000),
          Integer.valueOf(-1000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          ShortParameter,
          Short.valueOf((short) -1000),
          Integer.valueOf(-1000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          charParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          charParameter,
          Character.valueOf((char) 100),
          Integer.valueOf(100),
          8);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          CharacterParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          CharacterParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          9);
      run_IntegerQuery(
          pm, Integer_filterL, LongParameter, Long.valueOf(10000), Integer.valueOf(10000), 9);
      run_IntegerQuery(
          pm, Integer_filterR, LongParameter, Long.valueOf(43), Integer.valueOf(43), 10);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          FloatParameter,
          Float.valueOf((float) 10000),
          Integer.valueOf(10000),
          9);
      run_IntegerQuery(
          pm, Integer_filterR, FloatParameter, Float.valueOf((float) 43), Integer.valueOf(43), 10);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          9);
      run_IntegerQuery(
          pm, Integer_filterL, DoubleParameter, Double.valueOf(10000), Integer.valueOf(10000), 9);
      run_IntegerQuery(
          pm, Integer_filterR, DoubleParameter, Double.valueOf(43), Integer.valueOf(43), 10);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          BigIntegerParameter,
          new BigInteger("1000000"),
          Integer.valueOf(1000000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          BigIntegerParameter,
          new BigInteger("-1000000"),
          Integer.valueOf(-1000000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          BigDecimalParameter,
          new BigDecimal("1000000.0"),
          Integer.valueOf(1000000),
          9);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          BigDecimalParameter,
          new BigDecimal("-100000.0"),
          Integer.valueOf(-100000),
          10);
      AllTypes alltypes = new AllTypes();
      alltypes.setInteger(Integer.valueOf(100));
      run_IntegerQuery(pm, Integer_filterObj, AllTypesParameter, alltypes, Integer.valueOf(100), 8);
      run_IntegerQuery(pm, Integer_filterVal, null, null, Integer.valueOf(100), 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testLongQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testLongQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_LongQuery(
          pm,
          Long_filterL,
          longParameter,
          Long.valueOf(Long.MIN_VALUE),
          Long.valueOf(Long.MIN_VALUE),
          9);
      run_LongQuery(
          pm,
          Long_filterR,
          longParameter,
          Long.valueOf(Long.MAX_VALUE),
          Long.valueOf(Long.MAX_VALUE),
          9);
      run_LongQuery(pm, Long_filterL, LongParameter, Long.valueOf(100), Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterR, LongParameter, Long.valueOf(23), Long.valueOf(23), 10);
      run_LongQuery(
          pm, Long_filterL, byteParameter, Byte.valueOf((byte) 100), Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterR, byteParameter, Byte.valueOf((byte) 0), Long.valueOf(0), 9);
      run_LongQuery(
          pm, Long_filterL, ByteParameter, Byte.valueOf((byte) 100), Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterR, ByteParameter, Byte.valueOf((byte) 0), Long.valueOf(0), 9);
      run_LongQuery(
          pm, Long_filterL, shortParameter, Short.valueOf((short) -1000), Long.valueOf(-1000), 8);
      run_LongQuery(
          pm, Long_filterR, shortParameter, Short.valueOf((short) 1000), Long.valueOf(1000), 9);
      run_LongQuery(
          pm, Long_filterL, ShortParameter, Short.valueOf((short) 100), Long.valueOf(100), 8);
      run_LongQuery(
          pm, Long_filterR, ShortParameter, Short.valueOf((short) 32), Long.valueOf(32), 10);
      run_LongQuery(
          pm, Long_filterL, charParameter, Character.valueOf((char) 0), Long.valueOf(0), 9);
      run_LongQuery(
          pm, Long_filterR, charParameter, Character.valueOf((char) 100), Long.valueOf(100), 8);
      run_LongQuery(
          pm, Long_filterL, CharacterParameter, Character.valueOf((char) 23), Long.valueOf(23), 10);
      run_LongQuery(
          pm, Long_filterR, CharacterParameter, Character.valueOf((char) 0), Long.valueOf(0), 9);
      run_LongQuery(pm, Long_filterL, intParameter, Integer.valueOf(100), Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterR, intParameter, Integer.valueOf(0), Long.valueOf(0), 9);
      run_LongQuery(pm, Long_filterL, IntegerParameter, Integer.valueOf(23), Long.valueOf(23), 10);
      run_LongQuery(
          pm, Long_filterR, IntegerParameter, Integer.valueOf(1000000), Long.valueOf(1000000), 9);
      run_LongQuery(
          pm,
          Long_filterL,
          floatParameter,
          Float.valueOf((float) -1000000.0),
          Long.valueOf(-1000000),
          9);
      //    run_LongQuery(pm, Long_filterR, floatParameter, Float.valueOf((float)Long.MAX_VALUE),
      // Long.valueOf(Long.MAX_VALUE), 9);
      run_LongQuery(
          pm, Long_filterL, FloatParameter, Float.valueOf((float) 100), Long.valueOf(100), 8);
      run_LongQuery(
          pm, Long_filterR, FloatParameter, Float.valueOf((float) 32), Long.valueOf(32), 10);
      run_LongQuery(
          pm, Long_filterL, doubleParameter, Double.valueOf(-1000000.0), Long.valueOf(-1000000), 9);
      //    run_LongQuery(pm, Long_filterR, doubleParameter, Double.valueOf((double)Long.MAX_VALUE),
      // Long.valueOf(Long.MAX_VALUE), 9);
      run_LongQuery(pm, Long_filterL, DoubleParameter, Double.valueOf(100), Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterR, DoubleParameter, Double.valueOf(32), Long.valueOf(32), 10);
      run_LongQuery(
          pm,
          Long_filterL,
          BigIntegerParameter,
          new BigInteger("1000000"),
          Long.valueOf(1000000),
          9);
      run_LongQuery(
          pm,
          Long_filterR,
          BigIntegerParameter,
          new BigInteger("-1000000"),
          Long.valueOf(-1000000),
          9);
      run_LongQuery(
          pm, Long_filterL, BigDecimalParameter, new BigDecimal("-1000.0"), Long.valueOf(-1000), 8);
      run_LongQuery(
          pm,
          Long_filterR,
          BigDecimalParameter,
          new BigDecimal("-1000000.0"),
          Long.valueOf(-1000000),
          9);
      AllTypes alltypes = new AllTypes();
      alltypes.setLong(Long.valueOf(100));
      run_LongQuery(pm, Long_filterObj, AllTypesParameter, alltypes, Long.valueOf(100), 8);
      run_LongQuery(pm, Long_filterVal, null, null, Long.valueOf(-1000), 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFloatQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testFloatQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_FloatQuery(
          pm, Float_filterL, floatParameter, Float.valueOf(100.0f), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterR, floatParameter, Float.valueOf(100.0f), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterL, FloatParameter, Float.valueOf((float) 0.0), Float.valueOf(0.0f), 9);
      run_FloatQuery(
          pm, Float_filterR, FloatParameter, Float.valueOf((float) 4.0), Float.valueOf(4.0f), 10);
      run_FloatQuery(
          pm, Float_filterL, byteParameter, Byte.valueOf((byte) 0), Float.valueOf(0.0f), 9);
      run_FloatQuery(
          pm, Float_filterR, byteParameter, Byte.valueOf((byte) 23), Float.valueOf(23.0f), 10);
      run_FloatQuery(
          pm, Float_filterL, ByteParameter, Byte.valueOf((byte) 34), Float.valueOf(34.0f), 10);
      run_FloatQuery(
          pm, Float_filterR, ByteParameter, Byte.valueOf((byte) 100), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterL, shortParameter, Short.valueOf((short) 0), Float.valueOf(0.0f), 9);
      run_FloatQuery(
          pm, Float_filterR, shortParameter, Short.valueOf((short) 23), Float.valueOf(23.0f), 10);
      run_FloatQuery(
          pm, Float_filterL, ShortParameter, Short.valueOf((short) 34), Float.valueOf(34.0f), 10);
      run_FloatQuery(
          pm, Float_filterR, ShortParameter, Short.valueOf((short) 100), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterL, charParameter, Character.valueOf((char) 0), Float.valueOf(0.0f), 9);
      run_FloatQuery(
          pm, Float_filterR, charParameter, Character.valueOf((char) 23), Float.valueOf(23.0f), 10);
      run_FloatQuery(
          pm,
          Float_filterL,
          CharacterParameter,
          Character.valueOf((char) 34),
          Float.valueOf(34.0f),
          10);
      run_FloatQuery(
          pm,
          Float_filterR,
          CharacterParameter,
          Character.valueOf((char) 100),
          Float.valueOf(100.0f),
          8);
      run_FloatQuery(
          pm,
          Float_filterL,
          intParameter,
          Integer.valueOf(50000000),
          Float.valueOf(50000000.0f),
          9);
      run_FloatQuery(
          pm, Float_filterR, intParameter, Integer.valueOf(23), Float.valueOf(23.0f), 10);
      run_FloatQuery(
          pm, Float_filterL, IntegerParameter, Integer.valueOf(34), Float.valueOf(34.0f), 10);
      run_FloatQuery(
          pm, Float_filterR, IntegerParameter, Integer.valueOf(100), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterL, longParameter, Long.valueOf(50000000), Float.valueOf(50000000.0f), 9);
      run_FloatQuery(pm, Float_filterR, longParameter, Long.valueOf(23), Float.valueOf(23.0f), 10);
      run_FloatQuery(pm, Float_filterL, LongParameter, Long.valueOf(34), Float.valueOf(34.0f), 10);
      run_FloatQuery(pm, Float_filterR, LongParameter, Long.valueOf(100), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm,
          Float_filterL,
          doubleParameter,
          Double.valueOf(50000000.0f),
          Float.valueOf(50000000.0f),
          9);
      run_FloatQuery(
          pm, Float_filterR, doubleParameter, Double.valueOf(100.0f), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm, Float_filterL, DoubleParameter, Double.valueOf(0.0f), Float.valueOf(0.0f), 9);
      run_FloatQuery(
          pm, Float_filterR, DoubleParameter, Double.valueOf(100.0f), Float.valueOf(100.0f), 8);
      run_FloatQuery(
          pm,
          Float_filterL,
          BigIntegerParameter,
          new BigInteger("50000000"),
          Float.valueOf(50000000.0f),
          9);
      run_FloatQuery(
          pm,
          Float_filterR,
          BigIntegerParameter,
          new BigInteger("1000000000"),
          Float.valueOf(1000000000.0f),
          9);
      run_FloatQuery(
          pm,
          Float_filterL,
          BigDecimalParameter,
          new BigDecimal("350.5"),
          Float.valueOf(350.5f),
          9);
      run_FloatQuery(
          pm,
          Float_filterR,
          BigDecimalParameter,
          new BigDecimal("50000000.0"),
          Float.valueOf(50000000.0f),
          9);
      AllTypes alltypes = new AllTypes();
      alltypes.setFloat(Float.valueOf(23.23f));
      run_FloatQuery(pm, Float_filterObj, AllTypesParameter, alltypes, Float.valueOf(23.23f), 10);
      run_FloatQuery(pm, Float_filterVal, null, null, Float.valueOf(100.0f), 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDoubleQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testDoubleQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_DoubleQuery(
          pm, Double_filterL, doubleParameter, Double.valueOf(-25.5), Double.valueOf(-25.5), 9);
      run_DoubleQuery(
          pm, Double_filterR, doubleParameter, Double.valueOf(-25.5), Double.valueOf(-25.5), 9);
      run_DoubleQuery(
          pm, Double_filterL, DoubleParameter, Double.valueOf(0.0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterR, DoubleParameter, Double.valueOf(23.34), Double.valueOf(23.34), 10);
      run_DoubleQuery(
          pm, Double_filterL, byteParameter, Byte.valueOf((byte) 100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterR, byteParameter, Byte.valueOf((byte) 0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterL, ByteParameter, Byte.valueOf((byte) 23), Double.valueOf(23.0), 10);
      run_DoubleQuery(
          pm, Double_filterR, ByteParameter, Byte.valueOf((byte) 100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterL, shortParameter, Short.valueOf((short) 100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterR, shortParameter, Short.valueOf((short) 0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterL, ShortParameter, Short.valueOf((short) 23), Double.valueOf(23.0), 10);
      run_DoubleQuery(
          pm, Double_filterR, ShortParameter, Short.valueOf((short) 100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm,
          Double_filterL,
          charParameter,
          Character.valueOf((char) 100),
          Double.valueOf(100.0),
          8);
      run_DoubleQuery(
          pm, Double_filterR, charParameter, Character.valueOf((char) 0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm,
          Double_filterL,
          CharacterParameter,
          Character.valueOf((char) 23),
          Double.valueOf(23.0),
          10);
      run_DoubleQuery(
          pm,
          Double_filterR,
          CharacterParameter,
          Character.valueOf((char) 100),
          Double.valueOf(100.0),
          8);
      run_DoubleQuery(
          pm, Double_filterL, intParameter, Integer.valueOf(100), Double.valueOf(100.0), 8);
      run_DoubleQuery(pm, Double_filterR, intParameter, Integer.valueOf(0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterL, IntegerParameter, Integer.valueOf(23), Double.valueOf(23.0), 10);
      run_DoubleQuery(
          pm, Double_filterR, IntegerParameter, Integer.valueOf(100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterL, longParameter, Long.valueOf(100), Double.valueOf(100.0), 8);
      run_DoubleQuery(pm, Double_filterR, longParameter, Long.valueOf(0), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterL, LongParameter, Long.valueOf(23), Double.valueOf(23.0), 10);
      run_DoubleQuery(
          pm, Double_filterR, LongParameter, Long.valueOf(100), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterL, floatParameter, Float.valueOf(0.0f), Double.valueOf(0.0), 9);
      run_DoubleQuery(
          pm, Double_filterR, floatParameter, Float.valueOf(100.0f), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterL, FloatParameter, Float.valueOf(100.0f), Double.valueOf(100.0), 8);
      run_DoubleQuery(
          pm, Double_filterR, FloatParameter, Float.valueOf(69.96f), Double.valueOf(69.96), 10);
      run_DoubleQuery(
          pm,
          Double_filterL,
          BigIntegerParameter,
          new BigInteger("50000000"),
          Double.valueOf(50000000.0f),
          9);
      run_DoubleQuery(
          pm,
          Double_filterR,
          BigIntegerParameter,
          new BigInteger("1000000000"),
          Double.valueOf(1000000000.0f),
          9);
      run_DoubleQuery(
          pm,
          Double_filterL,
          BigDecimalParameter,
          new BigDecimal("350.5"),
          Double.valueOf(350.5f),
          9);
      run_DoubleQuery(
          pm,
          Double_filterR,
          BigDecimalParameter,
          new BigDecimal("50000000.0"),
          Double.valueOf(50000000.0f),
          9);
      AllTypes alltypes = new AllTypes();
      alltypes.setDouble(Double.valueOf(0.0));
      run_DoubleQuery(pm, Double_filterObj, AllTypesParameter, alltypes, Double.valueOf(0.0), 9);
      run_DoubleQuery(pm, Double_filterVal, null, null, Double.valueOf(100.0), 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testStringQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testStringQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_StringQuery(pm, String_filterL, StringParameter, "JDO", "JDO", 9);
      run_StringQuery(pm, String_filterR, StringParameter, "JDBC", "JDBC", 10);
      AllTypes alltypes = new AllTypes();
      alltypes.setString("abcde");
      run_StringQuery(pm, String_filterObj, AllTypesParameter, alltypes, "abcde", 9);
      run_StringQuery(pm, String_filterVal1, null, null, "Java", 8);
      run_StringQuery(pm, String_filterVal2, null, null, "", 9);

    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testLocaleQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testLocaleQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_LocaleQuery(
          pm, Locale_filterL, LocaleParameter, Locale.CANADA_FRENCH, Locale.CANADA_FRENCH, 10);
      run_LocaleQuery(pm, Locale_filterR, LocaleParameter, Locale.US, Locale.US, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setLocale(Locale.UK);
      run_LocaleQuery(pm, Locale_filterObj, AllTypesParameter, alltypes, Locale.UK, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBigDecimalQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testBigDecimalQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      BigDecimal bd = new BigDecimal("100.0");
      run_BigDecimalQuery(pm, BigDecimal_filterL, BigDecimalParameter, bd, bd, 8);
      bd = new BigDecimal("-234234.23333");
      run_BigDecimalQuery(pm, BigDecimal_filterR, BigDecimalParameter, bd, bd, 9);
      bd = new BigDecimal("989899.33304953");
      run_BigDecimalQuery(pm, BigDecimal_filterL, BigDecimalParameter, bd, bd, 10);
      bd = new BigDecimal("-1123123.22");
      run_BigDecimalQuery(pm, BigDecimal_filterR, BigDecimalParameter, bd, bd, 9);
      AllTypes alltypes = new AllTypes();
      alltypes.setBigDecimal(bd);
      run_BigDecimalQuery(pm, BigDecimal_filterObj, AllTypesParameter, alltypes, bd, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBigIntegerQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testBigIntegerQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      BigInteger bi = new BigInteger("987034534985043985");
      run_BigIntegerQuery(pm, BigInteger_filterL, BigIntegerParameter, bi, bi, 9);
      bi = new BigInteger("-999999999999999999");
      run_BigIntegerQuery(pm, BigInteger_filterR, BigIntegerParameter, bi, bi, 9);
      bi = new BigInteger("-99999999999999999");
      run_BigIntegerQuery(pm, BigInteger_filterR, BigIntegerParameter, bi, bi, 10);
      bi = new BigInteger("1333330");
      AllTypes alltypes = new AllTypes();
      alltypes.setBigInteger(bi);
      run_BigIntegerQuery(pm, BigInteger_filterObj, AllTypesParameter, alltypes, bi, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDateQueries() {
    if (logger.isDebugEnabled()) {
      logger.debug("testDateQueries " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.UK);
      gc.clear();
      gc.set(1999, Calendar.DECEMBER, 31, 9, 0, 0);
      Date d = gc.getTime();
      run_DateQuery(pm, Date_filterL, DateParameter, d, d, 9);
      gc.set(1992, Calendar.NOVEMBER, 22, 9, 0, 0);
      d = gc.getTime();
      run_DateQuery(pm, Date_filterR, DateParameter, d, d, 9);
      gc.set(1959, Calendar.OCTOBER, 9, 9, 0, 0);
      d = gc.getTime();
      run_DateQuery(pm, Date_filterR, DateParameter, d, d, 10);
      gc.set(1995, Calendar.JUNE, 14, 9, 0, 0);
      d = gc.getTime();
      AllTypes alltypes = new AllTypes();
      alltypes.setDate(d);
      run_DateQuery(pm, Date_filterObj, AllTypesParameter, alltypes, d, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  private void runQueries() {}

  private void run_booleanQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      boolean value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      boolean val = obj.getboolean();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns object with incorrect value",
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_byteQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      byte value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      byte val = obj.getbyte();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", byte parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_shortQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      short value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      short val = obj.getshort();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", short parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_charQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      char value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      char val = obj.getchar();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", char parameter value: "
                + value
                + ", char integer value: "
                + (int) value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_intQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      int value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      int val = obj.getint();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", int parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_longQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      long value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      long val = obj.getlong();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", long parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_floatQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      float value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      float val = obj.getfloat();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", float parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_doubleQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      double value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      double val = obj.getdouble();
      if (val == value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", double parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_BooleanQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Boolean value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Boolean val = obj.getBoolean();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns object with incorrect value, retrieved value: "
                + val
                + ", Boolean parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count)
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_ByteQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Byte value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Byte val = obj.getByte();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Byte parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_ShortQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Short value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Short val = obj.getShort();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Short parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_CharacterQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Character value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Character val = obj.getCharacter();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Character parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_IntegerQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Integer value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Integer val = obj.getInteger();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Integer parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_LongQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Long value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Long val = obj.getLong();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Long parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_FloatQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Float value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Float val = obj.getFloat();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Float parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_DoubleQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Double value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Double val = obj.getDouble();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", Double parameter value: "
                + value
                + ", Object parameter value: "
                + parameterValue,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_StringQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      String value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      String val = obj.getString();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_LocaleQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Locale value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Locale val = obj.getLocale();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_BigDecimalQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      BigDecimal value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      BigDecimal val = obj.getBigDecimal();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_BigIntegerQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      BigInteger value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      BigInteger val = obj.getBigInteger();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_DateQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Date value,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<?> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = (AllTypes) iter.next();
      Date val = obj.getDate();
      if (val.equals(value)) {
        fail(
            ASSERTION_FAILED,
            "JDOQL NotEquals test returns incorrect value, retrieved value: "
                + val
                + ", parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL NotEquals test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
  }

  /** */
  protected void localSetUp() {
    addTearDownClass(AllTypes.class);
    AllTypes.load(getPM());
  }
}
