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
 * <B>Title:</B> Less Than or Equal Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-19. <br>
 * <B>Assertion Description: </B> The less than or equal operator (<code>&lt;=</code>) is supported
 * for all types as they are defined in the Java language. This includes the following types:
 *
 * <UL>
 *   <LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code>
 *   <LI><code>float, double, Float, Double</code>
 *   <LI><code>BigDecimal, BigInteger</code>
 *   <LI><code>Date, String</code>
 * </UL>
 *
 * The operation on object-valued fields of wrapper types (<code>Boolean, Byte,
 * Short, Integer, Long, Float</code>, and <code>Double</code>), and numeric types (<code>BigDecimal
 * </code> and <code>BigInteger</code>) use the wrapped values as operands.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LessThanOrEqual extends ComparisonTests {
  private static final String boolean_filterL = "value <= fld_boolean";
  private static final String boolean_filterR = "fld_boolean <= value";
  private static final String boolean_filterT = "fld_boolean <= true";
  private static final String boolean_filterF = "false <= fld_boolean";
  private static final String boolean_filterObj = "value.fld_boolean <= fld_boolean";

  private static final String byte_filterL = "value <= fld_byte";
  private static final String byte_filterR = "fld_byte <= value";
  private static final String byte_filterObj = "value.fld_byte <= fld_byte";
  private static final String byte_filterVal = "fld_byte <= 100";

  private static final String char_filterL = "value <= fld_char";
  private static final String char_filterR = "fld_char <= value";
  private static final String char_filterObj = "value.fld_char <= fld_char";
  private static final String char_filterVal = "'M' <= fld_char";

  private static final String double_filterL = "value <= fld_double";
  private static final String double_filterR = "fld_double <= value";
  private static final String double_filterObj = "value.fld_double <= fld_double";
  private static final String double_filterVal = "fld_double <= 100.0";

  private static final String float_filterL = "value <= fld_float";
  private static final String float_filterR = "fld_float <= value";
  private static final String float_filterObj = "fld_float <= value.fld_float";
  private static final String float_filterVal = "fld_float <= 100.0";

  private static final String int_filterL = "value <= fld_int";
  private static final String int_filterR = "fld_int <= value";
  private static final String int_filterObj = "value.fld_int <= fld_int";
  private static final String int_filterVal = "fld_int <= 1000";

  private static final String long_filterL = "value <= fld_long";
  private static final String long_filterR = "fld_long <= value";
  private static final String long_filterObj = "fld_long <= value.fld_long";
  private static final String long_filterVal = "fld_long <= 1000000";

  private static final String short_filterL = "value <= fld_short";
  private static final String short_filterR = "fld_short <= value";
  private static final String short_filterObj = "value.fld_short <= fld_short";
  private static final String short_filterVal = "1000 <= fld_short";

  private static final String Boolean_filterL = "value <= fld_Boolean";
  private static final String Boolean_filterR = "fld_Boolean <= value";
  private static final String Boolean_filterT = "fld_Boolean <= true";
  private static final String Boolean_filterF = "false <= fld_Boolean";
  private static final String Boolean_filterObj = "value.fld_Boolean <= fld_Boolean";
  private static final String Boolean_filterVal = "fld_Boolean <= false";

  private static final String Byte_filterL = "value <= fld_Byte";
  private static final String Byte_filterR = "fld_Byte <= value";
  private static final String Byte_filterObj = "fld_Byte <= value.fld_Byte";
  private static final String Byte_filterVal = "100 <= fld_Byte";

  private static final String Character_filterL = "value <= fld_Character";
  private static final String Character_filterR = "fld_Character <= value";
  private static final String Character_filterObj = "value.fld_Character <= fld_Character";
  private static final String Character_filterVal = "fld_Character <= 'z'";

  private static final String Double_filterL = "value <= fld_Double";
  private static final String Double_filterR = "fld_Double <= value";
  private static final String Double_filterObj = "value.fld_Double <= fld_Double";
  private static final String Double_filterVal = "fld_Double <= 100.0";

  private static final String Float_filterL = "value <= fld_Float";
  private static final String Float_filterR = "fld_Float <= value";
  private static final String Float_filterObj = "fld_Float <= value.fld_Float";
  private static final String Float_filterVal = "100.0f <= fld_Float";

  private static final String Integer_filterL = "value <= fld_Integer";
  private static final String Integer_filterR = "fld_Integer <= value";
  private static final String Integer_filterObj = "fld_Integer <= value.fld_Integer";
  private static final String Integer_filterVal = "fld_Integer <= 100";

  private static final String Long_filterL = "value <= fld_Long";
  private static final String Long_filterR = "fld_Long <= value";
  private static final String Long_filterObj = "value.fld_Long <= fld_Long";
  private static final String Long_filterVal = "-1000 <= fld_Long";

  private static final String Short_filterL = "value <= fld_Short";
  private static final String Short_filterR = "fld_Short <= value";
  private static final String Short_filterObj = "fld_Short <= value.fld_Short";
  private static final String Short_filterVal = "-1000 <= fld_Short";

  private static final String String_filterL = "value <= fld_String";
  private static final String String_filterR = "fld_String <= value";
  private static final String String_filterObj = "value.fld_String <= fld_String";
  private static final String String_filterVal1 = "fld_String <= \"Java\"";
  private static final String String_filterVal2 = "fld_String <= \"\"";

  private static final String Date_filterL = "value <= fld_Date";
  private static final String Date_filterR = "fld_Date <= value";
  private static final String Date_filterObj = "fld_Date <= value.fld_Date";

  private static final String BigDecimal_filterL = "value <= fld_BigDecimal";
  private static final String BigDecimal_filterR = "fld_BigDecimal <= value";
  private static final String BigDecimal_filterObj = "value.fld_BigDecimal <= fld_BigDecimal";

  private static final String BigInteger_filterL = "value <= fld_BigInteger";
  private static final String BigInteger_filterR = "fld_BigInteger <= value";
  private static final String BigInteger_filterObj = "fld_BigInteger <= value.fld_BigInteger";

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-19 (LessThanOrEqual) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBytePrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      AllTypes alltypes = new AllTypes();
      run_byteQuery(pm, byte_filterL, byteParameter, Byte.valueOf((byte) 0), (byte) 0, true, 7);
      run_byteQuery(
          pm, byte_filterR, byteParameter, Byte.valueOf(Byte.MIN_VALUE), Byte.MIN_VALUE, false, 1);
      run_byteQuery(pm, byte_filterL, ByteParameter, Byte.valueOf((byte) 50), (byte) 50, true, 5);
      run_byteQuery(
          pm, byte_filterR, ByteParameter, Byte.valueOf(Byte.MAX_VALUE), Byte.MAX_VALUE, false, 10);
      run_byteQuery(
          pm, byte_filterL, shortParameter, Short.valueOf((short) 75), (byte) 75, true, 3);
      run_byteQuery(
          pm, byte_filterR, shortParameter, Short.valueOf((short) 75), (byte) 75, false, 8);
      run_byteQuery(
          pm, byte_filterL, ShortParameter, Short.valueOf((short) 10), (byte) 10, true, 6);
      run_byteQuery(
          pm, byte_filterR, ShortParameter, Short.valueOf((short) 25), (byte) 25, false, 5);
      run_byteQuery(
          pm, byte_filterL, charParameter, Character.valueOf((char) 101), (byte) 101, true, 1);
      run_byteQuery(
          pm, byte_filterR, charParameter, Character.valueOf((char) 50), (byte) 50, false, 7);
      run_byteQuery(
          pm, byte_filterL, CharacterParameter, Character.valueOf((char) 0), (byte) 0, true, 7);
      run_byteQuery(
          pm, byte_filterR, CharacterParameter, Character.valueOf((char) 0), (byte) 0, false, 4);
      run_byteQuery(pm, byte_filterL, intParameter, Integer.valueOf(25), (byte) 25, true, 5);
      run_byteQuery(pm, byte_filterR, intParameter, Integer.valueOf(50), (byte) 50, false, 7);
      run_byteQuery(pm, byte_filterL, IntegerParameter, Integer.valueOf(-10), (byte) -10, true, 8);
      run_byteQuery(
          pm, byte_filterR, IntegerParameter, Integer.valueOf(-100), (byte) -100, false, 2);
      run_byteQuery(pm, byte_filterL, longParameter, Long.valueOf(50), (byte) 50, true, 5);
      run_byteQuery(pm, byte_filterR, longParameter, Long.valueOf(60), (byte) 60, false, 7);
      run_byteQuery(pm, byte_filterL, LongParameter, Long.valueOf(-100), (byte) -100, true, 9);
      run_byteQuery(pm, byte_filterR, LongParameter, Long.valueOf(-100), (byte) -100, false, 2);
      run_byteQuery(
          pm, byte_filterL, floatParameter, Float.valueOf((float) 51), (byte) 51, true, 3);
      run_byteQuery(
          pm, byte_filterR, floatParameter, Float.valueOf((float) -20), (byte) -20, false, 2);
      run_byteQuery(
          pm, byte_filterL, FloatParameter, Float.valueOf((float) -99), (byte) -99, true, 8);
      run_byteQuery(
          pm, byte_filterR, FloatParameter, Float.valueOf((float) -100), (byte) -100, false, 2);
      run_byteQuery(pm, byte_filterL, doubleParameter, Double.valueOf(50), (byte) 50, true, 5);
      run_byteQuery(pm, byte_filterR, doubleParameter, Double.valueOf(60), (byte) 60, false, 7);
      run_byteQuery(
          pm,
          byte_filterL,
          DoubleParameter,
          Double.valueOf(Byte.MAX_VALUE),
          Byte.MAX_VALUE,
          true,
          1);
      run_byteQuery(pm, byte_filterR, DoubleParameter, Double.valueOf(25), (byte) 25, false, 5);
      run_byteQuery(
          pm, byte_filterL, BigIntegerParameter, new BigInteger("50"), (byte) 50, true, 5);
      run_byteQuery(
          pm, byte_filterR, BigIntegerParameter, new BigInteger("-100"), (byte) -100, false, 2);
      run_byteQuery(
          pm, byte_filterL, BigDecimalParameter, new BigDecimal("50.000000"), (byte) 50, true, 5);
      run_byteQuery(
          pm, byte_filterR, BigDecimalParameter, new BigDecimal("10.00000"), (byte) 10, false, 5);
      alltypes.setbyte((byte) 50);
      run_byteQuery(pm, byte_filterObj, AllTypesParameter, alltypes, (byte) 50, true, 5);
      alltypes.setbyte((byte) 55);
      run_byteQuery(pm, byte_filterObj, AllTypesParameter, alltypes, (byte) 55, true, 3);
      run_byteQuery(pm, byte_filterVal, null, null, (byte) 100, false, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testShortPrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_shortQuery(
          pm, short_filterL, shortParameter, Short.valueOf((short) 100), (short) 100, true, 5);
      run_shortQuery(
          pm, short_filterR, shortParameter, Short.valueOf((short) 100), (short) 100, false, 7);
      run_shortQuery(
          pm,
          short_filterL,
          ShortParameter,
          Short.valueOf(Short.MIN_VALUE),
          Short.MIN_VALUE,
          true,
          10);
      run_shortQuery(
          pm, short_filterR, ShortParameter, Short.valueOf((short) 253), (short) 253, false, 7);
      run_shortQuery(
          pm, short_filterR, shortParameter, Short.valueOf((short) 1000), (short) 1000, false, 8);
      run_shortQuery(
          pm, short_filterL, byteParameter, Byte.valueOf((byte) 75), (short) 75, true, 5);
      run_shortQuery(
          pm, short_filterR, byteParameter, Byte.valueOf((byte) -75), (short) -75, false, 4);
      run_shortQuery(
          pm, short_filterL, ByteParameter, Byte.valueOf((byte) 100), (short) 100, true, 5);
      run_shortQuery(
          pm, short_filterR, ByteParameter, Byte.valueOf((byte) 100), (short) 100, false, 7);
      run_shortQuery(
          pm, short_filterL, charParameter, Character.valueOf((char) 75), (short) 75, true, 5);
      run_shortQuery(
          pm, short_filterR, charParameter, Character.valueOf((char) 9999), (short) 9999, false, 8);
      run_shortQuery(
          pm,
          short_filterL,
          CharacterParameter,
          Character.valueOf((char) 1000),
          (short) 1000,
          true,
          3);
      run_shortQuery(
          pm,
          short_filterR,
          CharacterParameter,
          Character.valueOf((char) 10000),
          (short) 10000,
          false,
          9);
      run_shortQuery(
          pm, short_filterL, intParameter, Integer.valueOf(-10000), (short) -10000, true, 9);
      run_shortQuery(
          pm, short_filterR, intParameter, Integer.valueOf(-10000), (short) -10000, false, 2);
      run_shortQuery(
          pm, short_filterL, IntegerParameter, Integer.valueOf(10000), (short) 10000, true, 2);
      run_shortQuery(
          pm, short_filterR, IntegerParameter, Integer.valueOf(30000), (short) 30000, false, 9);
      run_shortQuery(pm, short_filterL, longParameter, Long.valueOf(10000), (short) 10000, true, 2);
      run_shortQuery(
          pm,
          short_filterR,
          longParameter,
          Long.valueOf(Short.MAX_VALUE),
          Short.MAX_VALUE,
          false,
          10);
      run_shortQuery(
          pm,
          short_filterL,
          LongParameter,
          Long.valueOf(Short.MAX_VALUE),
          Short.MAX_VALUE,
          true,
          1);
      run_shortQuery(pm, short_filterR, LongParameter, Long.valueOf(100), (short) 100, false, 7);
      run_shortQuery(
          pm, short_filterL, floatParameter, Float.valueOf((float) 23000), (short) 23000, true, 1);
      run_shortQuery(
          pm, short_filterR, floatParameter, Float.valueOf((float) 23000), (short) 23000, false, 9);
      run_shortQuery(
          pm, short_filterL, FloatParameter, Float.valueOf((float) -1000), (short) -1000, true, 8);
      run_shortQuery(
          pm, short_filterR, FloatParameter, Float.valueOf((float) 100), (short) 100, false, 7);
      run_shortQuery(
          pm, short_filterL, doubleParameter, Double.valueOf(-10000.0), (short) -10000, true, 9);
      run_shortQuery(
          pm, short_filterR, doubleParameter, Double.valueOf(9999.0), (short) 9999, false, 8);
      run_shortQuery(pm, short_filterL, DoubleParameter, Double.valueOf(23.0), (short) 23, true, 5);
      run_shortQuery(
          pm, short_filterR, DoubleParameter, Double.valueOf(23.0), (short) 23, false, 5);
      run_shortQuery(
          pm, short_filterL, BigIntegerParameter, new BigInteger("10000"), (short) 10000, true, 2);
      run_shortQuery(
          pm, short_filterR, BigIntegerParameter, new BigInteger("30000"), (short) 30000, false, 9);
      run_shortQuery(
          pm, short_filterL, BigDecimalParameter, new BigDecimal("23.0"), (short) 23, true, 5);
      run_shortQuery(
          pm, short_filterR, BigDecimalParameter, new BigDecimal("23.0"), (short) 23, false, 5);
      AllTypes alltypes = new AllTypes();
      alltypes.setshort((short) 100);
      run_shortQuery(pm, short_filterObj, AllTypesParameter, alltypes, (short) 100, true, 5);
      alltypes.setshort((short) 23);
      run_shortQuery(pm, short_filterObj, AllTypesParameter, alltypes, (short) 23, true, 5);
      run_shortQuery(pm, short_filterVal, null, null, (short) 1000, true, 3);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCharPrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_charQuery(
          pm,
          char_filterL,
          charParameter,
          Character.valueOf(Character.MIN_VALUE),
          Character.MIN_VALUE,
          true,
          10);
      run_charQuery(
          pm,
          char_filterR,
          charParameter,
          Character.valueOf(Character.MAX_VALUE),
          Character.MAX_VALUE,
          false,
          10);
      run_charQuery(pm, char_filterL, charParameter, Character.valueOf('C'), 'C', true, 6);
      run_charQuery(pm, char_filterR, charParameter, Character.valueOf('z'), 'z', false, 9);
      run_charQuery(pm, char_filterL, CharacterParameter, Character.valueOf(' '), ' ', true, 9);
      run_charQuery(pm, char_filterR, CharacterParameter, Character.valueOf('f'), 'f', false, 7);
      run_charQuery(
          pm,
          char_filterL,
          byteParameter,
          Byte.valueOf((byte) Character.MIN_VALUE),
          Character.MIN_VALUE,
          true,
          10);
      run_charQuery(pm, char_filterR, ByteParameter, Byte.valueOf((byte) 'a'), 'a', false, 7);
      run_charQuery(pm, char_filterL, shortParameter, Short.valueOf((short) 'M'), 'M', true, 5);
      run_charQuery(pm, char_filterR, shortParameter, Short.valueOf((short) 'M'), 'M', false, 7);
      run_charQuery(pm, char_filterL, ShortParameter, Short.valueOf((short) 'A'), 'A', true, 8);
      run_charQuery(pm, char_filterR, ShortParameter, Short.valueOf((short) 'A'), 'A', false, 3);
      run_charQuery(pm, char_filterL, intParameter, Integer.valueOf('z'), 'z', true, 3);
      run_charQuery(pm, char_filterR, intParameter, Integer.valueOf('z'), 'z', false, 9);
      run_charQuery(pm, char_filterL, IntegerParameter, Integer.valueOf('B'), 'B', true, 7);
      run_charQuery(pm, char_filterR, IntegerParameter, Integer.valueOf('B'), 'B', false, 4);
      run_charQuery(pm, char_filterL, longParameter, Long.valueOf('z'), 'z', true, 3);
      run_charQuery(pm, char_filterR, longParameter, Long.valueOf('z'), 'z', false, 9);
      run_charQuery(pm, char_filterL, LongParameter, Long.valueOf('B'), 'B', true, 7);
      run_charQuery(pm, char_filterR, LongParameter, Long.valueOf('B'), 'B', false, 4);
      run_charQuery(pm, char_filterL, floatParameter, Float.valueOf('f'), 'f', true, 3);
      run_charQuery(pm, char_filterR, floatParameter, Float.valueOf(' '), ' ', false, 2);
      run_charQuery(pm, char_filterL, FloatParameter, Float.valueOf('z'), 'z', true, 3);
      run_charQuery(pm, char_filterR, FloatParameter, Float.valueOf('z'), 'z', false, 9);
      run_charQuery(pm, char_filterL, doubleParameter, Double.valueOf('B'), 'B', true, 7);
      run_charQuery(pm, char_filterR, doubleParameter, Double.valueOf('B'), 'B', false, 4);
      run_charQuery(pm, char_filterL, DoubleParameter, Double.valueOf('A'), 'A', true, 8);
      run_charQuery(pm, char_filterR, DoubleParameter, Double.valueOf('A'), 'A', false, 3);
      run_charQuery(
          pm, char_filterL, BigIntegerParameter, new BigInteger("65"), 'A', true, 8); // 'A' == 65
      run_charQuery(
          pm,
          char_filterR,
          BigIntegerParameter,
          new BigInteger("122"),
          'z',
          false,
          9); // 'z' == 122
      run_charQuery(
          pm, char_filterL, BigDecimalParameter, new BigDecimal("65.00000"), 'A', true, 8);
      run_charQuery(
          pm,
          char_filterR,
          BigDecimalParameter,
          new BigDecimal("77.0000"),
          'M',
          false,
          7); // 'M' == 77
      AllTypes alltypes = new AllTypes();
      alltypes.setchar('A');
      run_charQuery(pm, char_filterObj, AllTypesParameter, alltypes, 'A', true, 8);
      alltypes.setchar('b');
      run_charQuery(pm, char_filterObj, AllTypesParameter, alltypes, 'b', true, 3);
      run_charQuery(pm, char_filterVal, null, null, 'M', true, 5);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testIntPrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_intQuery(
          pm,
          int_filterL,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          true,
          10);
      run_intQuery(
          pm,
          int_filterR,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          false,
          10);
      run_intQuery(pm, int_filterR, intParameter, Integer.valueOf(23), 23, false, 4);
      run_intQuery(pm, int_filterL, IntegerParameter, Integer.valueOf(1000000), 1000000, true, 2);
      run_intQuery(pm, int_filterR, IntegerParameter, Integer.valueOf(1000), 1000, false, 7);
      run_intQuery(pm, int_filterL, byteParameter, Byte.valueOf((byte) 100), 100, true, 6);
      run_intQuery(pm, int_filterR, byteParameter, Byte.valueOf((byte) 0), 0, false, 4);
      run_intQuery(pm, int_filterL, ByteParameter, Byte.valueOf((byte) 100), 100, true, 6);
      run_intQuery(pm, int_filterR, ByteParameter, Byte.valueOf((byte) 0), 0, false, 4);
      run_intQuery(pm, int_filterL, shortParameter, Short.valueOf((short) 10000), 10000, true, 3);
      run_intQuery(pm, int_filterR, shortParameter, Short.valueOf((short) -1000), -1000, false, 3);
      run_intQuery(pm, int_filterL, ShortParameter, Short.valueOf((short) -1000), -1000, true, 8);
      run_intQuery(pm, int_filterR, ShortParameter, Short.valueOf((short) 10000), 10000, false, 8);
      run_intQuery(pm, int_filterL, charParameter, Character.valueOf((char) 100), 100, true, 6);
      run_intQuery(pm, int_filterR, charParameter, Character.valueOf((char) 0), 0, false, 4);
      run_intQuery(
          pm, int_filterL, CharacterParameter, Character.valueOf((char) 100), 100, true, 6);
      run_intQuery(
          pm, int_filterR, CharacterParameter, Character.valueOf((char) 10000), 10000, false, 8);
      run_intQuery(
          pm,
          int_filterL,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          true,
          1);
      run_intQuery(
          pm,
          int_filterR,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          false,
          1);
      run_intQuery(pm, int_filterL, LongParameter, Long.valueOf(10000), 10000, true, 3);
      run_intQuery(pm, int_filterR, LongParameter, Long.valueOf(43), 43, false, 4);
      run_intQuery(
          pm,
          int_filterL,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          true,
          1);
      run_intQuery(
          pm,
          int_filterR,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          false,
          1);
      run_intQuery(pm, int_filterL, FloatParameter, Float.valueOf((float) 10000), 10000, true, 3);
      run_intQuery(pm, int_filterR, FloatParameter, Float.valueOf((float) 43), 43, false, 4);
      run_intQuery(
          pm,
          int_filterL,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          AllTypes.VERY_LARGE_POSITIVE_INT,
          true,
          1);
      run_intQuery(
          pm,
          int_filterR,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          AllTypes.VERY_LARGE_NEGATIVE_INT,
          false,
          1);
      run_intQuery(pm, int_filterL, DoubleParameter, Double.valueOf(10000), 10000, true, 3);
      run_intQuery(pm, int_filterR, DoubleParameter, Double.valueOf(43), 43, false, 4);
      run_intQuery(
          pm, int_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, true, 2);
      run_intQuery(pm, int_filterR, BigIntegerParameter, new BigInteger("1000"), 1000, false, 7);
      run_intQuery(pm, int_filterL, BigDecimalParameter, new BigDecimal("10000.0"), 10000, true, 3);
      run_intQuery(pm, int_filterR, BigDecimalParameter, new BigDecimal("43.0"), 43, false, 4);
      AllTypes alltypes = new AllTypes();
      alltypes.setint(100);
      run_intQuery(pm, int_filterObj, AllTypesParameter, alltypes, 100, true, 6);
      run_intQuery(pm, int_filterVal, null, null, 1000, false, 7);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testLongPrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_longQuery(
          pm, long_filterL, longParameter, Long.valueOf(Long.MIN_VALUE), Long.MIN_VALUE, true, 10);
      run_longQuery(
          pm, long_filterR, longParameter, Long.valueOf(Long.MAX_VALUE), Long.MAX_VALUE, false, 10);
      run_longQuery(pm, long_filterL, LongParameter, Long.valueOf(100), 100, true, 5);
      run_longQuery(pm, long_filterR, LongParameter, Long.valueOf(23), 23, false, 5);
      run_longQuery(pm, long_filterL, byteParameter, Byte.valueOf((byte) 100), 100, true, 5);
      run_longQuery(pm, long_filterR, byteParameter, Byte.valueOf((byte) 0), 0, false, 5);
      run_longQuery(pm, long_filterL, ByteParameter, Byte.valueOf((byte) 100), 100, true, 5);
      run_longQuery(pm, long_filterR, ByteParameter, Byte.valueOf((byte) 0), 0, false, 5);
      run_longQuery(pm, long_filterL, shortParameter, Short.valueOf((short) -1000), -1000, true, 8);
      run_longQuery(pm, long_filterR, shortParameter, Short.valueOf((short) 1000), 1000, false, 8);
      run_longQuery(pm, long_filterL, ShortParameter, Short.valueOf((short) 100), 100, true, 5);
      run_longQuery(pm, long_filterR, ShortParameter, Short.valueOf((short) 32), 32, false, 5);
      run_longQuery(pm, long_filterL, charParameter, Character.valueOf((char) 0), 0, true, 6);
      run_longQuery(pm, long_filterR, charParameter, Character.valueOf((char) 100), 100, false, 7);
      run_longQuery(
          pm, long_filterL, CharacterParameter, Character.valueOf((char) 23), 23, true, 5);
      run_longQuery(pm, long_filterR, CharacterParameter, Character.valueOf((char) 0), 0, false, 5);
      run_longQuery(pm, long_filterL, intParameter, Integer.valueOf(100), 100, true, 5);
      run_longQuery(pm, long_filterR, intParameter, Integer.valueOf(0), 0, false, 5);
      run_longQuery(pm, long_filterL, IntegerParameter, Integer.valueOf(23), 23, true, 5);
      run_longQuery(
          pm, long_filterR, IntegerParameter, Integer.valueOf(1000000), 1000000, false, 9);
      run_longQuery(
          pm, long_filterL, floatParameter, Float.valueOf((float) -1000000.0), -1000000, true, 9);
      run_longQuery(
          pm,
          long_filterR,
          floatParameter,
          Float.valueOf((float) Long.MAX_VALUE),
          Long.MAX_VALUE,
          false,
          10);
      run_longQuery(pm, long_filterL, FloatParameter, Float.valueOf((float) 100.0), 100, true, 5);
      run_longQuery(pm, long_filterR, FloatParameter, Float.valueOf((float) 32.0), 32, false, 5);
      run_longQuery(
          pm, long_filterL, doubleParameter, Double.valueOf(-1000000.0), -1000000, true, 9);
      run_longQuery(
          pm,
          long_filterR,
          doubleParameter,
          Double.valueOf((double) Long.MAX_VALUE),
          Long.MAX_VALUE,
          false,
          10);
      run_longQuery(pm, long_filterL, DoubleParameter, Double.valueOf(100.0), 100, true, 5);
      run_longQuery(pm, long_filterR, DoubleParameter, Double.valueOf(32.0), 32, false, 5);
      run_longQuery(pm, long_filterL, BigIntegerParameter, new BigInteger("23"), 23, true, 5);
      run_longQuery(
          pm, long_filterR, BigIntegerParameter, new BigInteger("1000000"), 1000000, false, 9);
      run_longQuery(pm, long_filterL, BigDecimalParameter, new BigDecimal("100.0"), 100, true, 5);
      run_longQuery(pm, long_filterR, BigDecimalParameter, new BigDecimal("32.0"), 32, false, 5);
      AllTypes alltypes = new AllTypes();
      alltypes.setlong(100);
      run_longQuery(pm, long_filterObj, AllTypesParameter, alltypes, 100, false, 7);
      run_longQuery(pm, long_filterVal, null, null, 1000000, false, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFloatPrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_floatQuery(
          pm,
          float_filterL,
          floatParameter,
          Float.valueOf(AllTypes.FLOAT_SMALLEST),
          AllTypes.FLOAT_SMALLEST,
          true,
          10);
      run_floatQuery(
          pm,
          float_filterR,
          floatParameter,
          Float.valueOf(AllTypes.FLOAT_LARGEST),
          AllTypes.FLOAT_LARGEST,
          false,
          10);
      run_floatQuery(pm, float_filterL, FloatParameter, Float.valueOf(4.0f), 4.0f, true, 6);
      run_floatQuery(pm, float_filterR, FloatParameter, Float.valueOf(400.0f), 400.0f, false, 7);
      run_floatQuery(pm, float_filterL, byteParameter, Byte.valueOf((byte) 4), 4.0f, true, 6);
      run_floatQuery(pm, float_filterR, byteParameter, Byte.valueOf((byte) 23), 23.0f, false, 4);
      run_floatQuery(pm, float_filterL, ByteParameter, Byte.valueOf((byte) 34), 34.0f, true, 6);
      run_floatQuery(pm, float_filterR, ByteParameter, Byte.valueOf((byte) 100), 100.0f, false, 6);
      run_floatQuery(pm, float_filterL, shortParameter, Short.valueOf((short) 10), 10.0f, true, 6);
      run_floatQuery(pm, float_filterR, shortParameter, Short.valueOf((short) 23), 23.0f, false, 4);
      run_floatQuery(pm, float_filterL, ShortParameter, Short.valueOf((short) 34), 34.0f, true, 6);
      run_floatQuery(
          pm, float_filterR, ShortParameter, Short.valueOf((short) 100), 100.0f, false, 6);
      run_floatQuery(
          pm, float_filterL, charParameter, Character.valueOf((char) 20), 20.0f, true, 6);
      run_floatQuery(
          pm, float_filterR, charParameter, Character.valueOf((char) 23), 23.0f, false, 4);
      run_floatQuery(
          pm, float_filterL, CharacterParameter, Character.valueOf((char) 34), 34.0f, true, 6);
      run_floatQuery(
          pm, float_filterR, CharacterParameter, Character.valueOf((char) 100), 100.0f, false, 6);
      run_floatQuery(
          pm, float_filterL, intParameter, Integer.valueOf(55000000), 55000000.0f, true, 2);
      run_floatQuery(pm, float_filterR, intParameter, Integer.valueOf(23), 23.0f, false, 4);
      run_floatQuery(pm, float_filterL, IntegerParameter, Integer.valueOf(34), 34.0f, true, 6);
      run_floatQuery(pm, float_filterR, IntegerParameter, Integer.valueOf(100), 100.0f, false, 6);
      run_floatQuery(
          pm, float_filterL, longParameter, Long.valueOf(55000000), 55000000.0f, true, 2);
      run_floatQuery(pm, float_filterR, longParameter, Long.valueOf(23), 23.0f, false, 4);
      run_floatQuery(pm, float_filterL, LongParameter, Long.valueOf(34), 34.0f, true, 6);
      run_floatQuery(pm, float_filterR, LongParameter, Long.valueOf(100), 100.0f, false, 6);
      run_floatQuery(
          pm, float_filterL, doubleParameter, Double.valueOf(55000000.0), 55000000.0f, true, 2);
      run_floatQuery(pm, float_filterR, doubleParameter, Double.valueOf(-20.5), -20.5f, false, 3);
      run_floatQuery(pm, float_filterL, DoubleParameter, Double.valueOf(2.0), 2.0f, true, 6);
      run_floatQuery(pm, float_filterR, DoubleParameter, Double.valueOf(100.0), 100.0f, false, 6);
      run_floatQuery(
          pm, float_filterL, BigIntegerParameter, new BigInteger("55000000"), 55000000.0f, true, 2);
      run_floatQuery(pm, float_filterR, BigIntegerParameter, new BigInteger("23"), 23.0f, false, 4);
      run_floatQuery(
          pm,
          float_filterL,
          BigDecimalParameter,
          new BigDecimal("55000000.0"),
          55000000.0f,
          true,
          2);
      run_floatQuery(
          pm, float_filterR, BigDecimalParameter, new BigDecimal("-20.5"), -20.5f, false, 3);
      AllTypes alltypes = new AllTypes();
      alltypes.setfloat(23.23f);
      run_floatQuery(pm, float_filterObj, AllTypesParameter, alltypes, 23.23f, false, 4);
      run_floatQuery(pm, float_filterVal, null, null, 100.0f, false, 6);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDoublePrimitiveQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_doubleQuery(
          pm,
          double_filterL,
          doubleParameter,
          Double.valueOf(AllTypes.DOUBLE_SMALLEST),
          AllTypes.DOUBLE_SMALLEST,
          true,
          10);
      run_doubleQuery(
          pm,
          double_filterR,
          doubleParameter,
          Double.valueOf(AllTypes.DOUBLE_LARGEST),
          AllTypes.DOUBLE_LARGEST,
          false,
          10);
      run_doubleQuery(pm, double_filterL, DoubleParameter, Double.valueOf(0.0), 0.0, true, 7);
      run_doubleQuery(pm, double_filterR, DoubleParameter, Double.valueOf(23.34), 23.34, false, 4);
      run_doubleQuery(pm, double_filterL, byteParameter, Byte.valueOf((byte) 100), 100.0, true, 6);
      run_doubleQuery(pm, double_filterR, byteParameter, Byte.valueOf((byte) 0), 0.0, false, 4);
      run_doubleQuery(pm, double_filterL, ByteParameter, Byte.valueOf((byte) 23), 23.0, true, 6);
      run_doubleQuery(pm, double_filterR, ByteParameter, Byte.valueOf((byte) 100), 100.0, false, 6);
      run_doubleQuery(
          pm, double_filterL, charParameter, Character.valueOf((char) 100), 100.0, true, 6);
      run_doubleQuery(
          pm, double_filterR, charParameter, Character.valueOf((char) 0), 0.0, false, 4);
      run_doubleQuery(
          pm, double_filterL, CharacterParameter, Character.valueOf((char) 23), 23.0, true, 6);
      run_doubleQuery(
          pm, double_filterR, CharacterParameter, Character.valueOf((char) 100), 100.0, false, 6);
      run_doubleQuery(
          pm, double_filterL, shortParameter, Short.valueOf((short) 100), 100.0, true, 6);
      run_doubleQuery(pm, double_filterR, shortParameter, Short.valueOf((short) 0), 0.0, false, 4);
      run_doubleQuery(pm, double_filterL, ShortParameter, Short.valueOf((short) 23), 23.0, true, 6);
      run_doubleQuery(
          pm, double_filterR, ShortParameter, Short.valueOf((short) 100), 100.0, false, 6);
      run_doubleQuery(pm, double_filterL, intParameter, Integer.valueOf(100), 100.0, true, 6);
      run_doubleQuery(pm, double_filterR, intParameter, Integer.valueOf(0), 0.0, false, 4);
      run_doubleQuery(pm, double_filterL, IntegerParameter, Integer.valueOf(5000), 5000.0, true, 3);
      run_doubleQuery(pm, double_filterR, IntegerParameter, Integer.valueOf(-20), -20.0, false, 3);
      run_doubleQuery(pm, double_filterL, longParameter, Long.valueOf(100), 100.0, true, 6);
      run_doubleQuery(pm, double_filterR, longParameter, Long.valueOf(0), 0.0, false, 4);
      run_doubleQuery(pm, double_filterL, LongParameter, Long.valueOf(5000), 5000.0, true, 3);
      run_doubleQuery(pm, double_filterR, LongParameter, Long.valueOf(-20), -20.0, false, 3);
      run_doubleQuery(pm, double_filterL, floatParameter, Float.valueOf(0.0f), 0.0f, true, 7);
      run_doubleQuery(pm, double_filterR, floatParameter, Float.valueOf(100.0f), 100.0f, false, 6);
      run_doubleQuery(pm, double_filterL, FloatParameter, Float.valueOf(100.0f), 100.0, true, 6);
      run_doubleQuery(pm, double_filterR, FloatParameter, Float.valueOf(69.96f), 69.96, false, 4);
      run_doubleQuery(
          pm, double_filterL, BigIntegerParameter, new BigInteger("5000"), 5000.0, true, 3);
      run_doubleQuery(
          pm, double_filterR, BigIntegerParameter, new BigInteger("-20"), -20.0, false, 3);
      run_doubleQuery(
          pm, double_filterL, BigDecimalParameter, new BigDecimal("100.0"), 100.0, true, 6);
      run_doubleQuery(
          pm, double_filterR, BigDecimalParameter, new BigDecimal("69.96"), 69.96, false, 4);
      AllTypes alltypes = new AllTypes();
      alltypes.setdouble(-25.5);
      run_doubleQuery(pm, double_filterObj, AllTypesParameter, alltypes, -25.5, true, 8);
      run_doubleQuery(pm, double_filterVal, null, null, 100.0, false, 6);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testByteQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_ByteQuery(
          pm,
          Byte_filterL,
          byteParameter,
          Byte.valueOf((byte) 50),
          Byte.valueOf((byte) 50),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          byteParameter,
          Byte.valueOf(Byte.MIN_VALUE),
          Byte.valueOf(Byte.MIN_VALUE),
          false,
          1);
      run_ByteQuery(
          pm,
          Byte_filterL,
          ByteParameter,
          Byte.valueOf((byte) 20),
          Byte.valueOf((byte) 20),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          ByteParameter,
          Byte.valueOf(Byte.MAX_VALUE),
          Byte.valueOf(Byte.MAX_VALUE),
          false,
          10);
      run_ByteQuery(
          pm,
          Byte_filterL,
          shortParameter,
          Short.valueOf((short) 60),
          Byte.valueOf((byte) 60),
          true,
          3);
      run_ByteQuery(
          pm,
          Byte_filterR,
          shortParameter,
          Short.valueOf((short) 51),
          Byte.valueOf((byte) 51),
          false,
          7);
      run_ByteQuery(
          pm,
          Byte_filterL,
          ShortParameter,
          Short.valueOf((short) -100),
          Byte.valueOf((byte) -100),
          true,
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          ShortParameter,
          Short.valueOf((short) -100),
          Byte.valueOf((byte) -100),
          false,
          2);
      run_ByteQuery(
          pm,
          Byte_filterL,
          charParameter,
          Character.valueOf((char) 101),
          Byte.valueOf((byte) 101),
          true,
          1);
      run_ByteQuery(
          pm,
          Byte_filterR,
          charParameter,
          Character.valueOf((char) 10),
          Byte.valueOf((byte) 10),
          false,
          5);
      run_ByteQuery(
          pm,
          Byte_filterL,
          CharacterParameter,
          Character.valueOf((char) 50),
          Byte.valueOf((byte) 50),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          CharacterParameter,
          Character.valueOf((char) 75),
          Byte.valueOf((byte) 75),
          false,
          8);
      run_ByteQuery(
          pm, Byte_filterL, intParameter, Integer.valueOf(77), Byte.valueOf((byte) 77), true, 2);
      run_ByteQuery(
          pm, Byte_filterR, intParameter, Integer.valueOf(60), Byte.valueOf((byte) 60), false, 7);
      run_ByteQuery(
          pm,
          Byte_filterL,
          IntegerParameter,
          Integer.valueOf(40),
          Byte.valueOf((byte) 40),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          IntegerParameter,
          Integer.valueOf(75),
          Byte.valueOf((byte) 75),
          false,
          8);
      run_byteQuery(
          pm, byte_filterL, longParameter, Long.valueOf(50), Byte.valueOf((byte) 50), true, 5);
      run_byteQuery(
          pm, byte_filterR, longParameter, Long.valueOf(50), Byte.valueOf((byte) 50), false, 7);
      run_ByteQuery(
          pm, Byte_filterL, LongParameter, Long.valueOf(-100), Byte.valueOf((byte) -100), true, 9);
      run_ByteQuery(
          pm, Byte_filterR, LongParameter, Long.valueOf(-100), Byte.valueOf((byte) -100), false, 2);
      run_ByteQuery(
          pm,
          Byte_filterL,
          floatParameter,
          Float.valueOf((float) 50),
          Byte.valueOf((byte) 50),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          floatParameter,
          Float.valueOf((float) 50),
          Byte.valueOf((byte) 50),
          false,
          7);
      run_ByteQuery(
          pm,
          Byte_filterL,
          FloatParameter,
          Float.valueOf((float) -100),
          Byte.valueOf((byte) -100),
          true,
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          FloatParameter,
          Float.valueOf((float) -100),
          Byte.valueOf((byte) -100),
          false,
          2);
      run_ByteQuery(
          pm, Byte_filterL, doubleParameter, Double.valueOf(50), Byte.valueOf((byte) 50), true, 5);
      run_ByteQuery(
          pm, Byte_filterR, doubleParameter, Double.valueOf(50), Byte.valueOf((byte) 50), false, 7);
      run_ByteQuery(
          pm,
          Byte_filterL,
          DoubleParameter,
          Double.valueOf(-100),
          Byte.valueOf((byte) -100),
          true,
          9);
      run_ByteQuery(
          pm,
          Byte_filterR,
          DoubleParameter,
          Double.valueOf(-100),
          Byte.valueOf((byte) -100),
          false,
          2);
      run_ByteQuery(
          pm,
          Byte_filterL,
          BigIntegerParameter,
          new BigInteger("50"),
          Byte.valueOf((byte) 50),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          BigIntegerParameter,
          new BigInteger("-100"),
          Byte.valueOf((byte) -100),
          false,
          2);
      run_ByteQuery(
          pm,
          Byte_filterL,
          BigDecimalParameter,
          new BigDecimal("50.000000"),
          Byte.valueOf((byte) 50),
          true,
          5);
      run_ByteQuery(
          pm,
          Byte_filterR,
          BigDecimalParameter,
          new BigDecimal("10.00000"),
          Byte.valueOf((byte) 10),
          false,
          5);
      Byte val = Byte.valueOf((byte) 50);
      AllTypes alltypes = new AllTypes();
      alltypes.setByte(val);
      run_ByteQuery(pm, Byte_filterObj, AllTypesParameter, alltypes, val, false, 7);
      val = Byte.valueOf((byte) 51);
      alltypes.setByte(val);
      run_ByteQuery(pm, Byte_filterObj, AllTypesParameter, alltypes, val, false, 7);
      run_ByteQuery(pm, Byte_filterVal, null, null, Byte.valueOf((byte) 100), true, 2);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testShortQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_ShortQuery(
          pm,
          Short_filterL,
          shortParameter,
          Short.valueOf((short) 100),
          Short.valueOf((short) 100),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          shortParameter,
          Short.valueOf((short) 100),
          Short.valueOf((short) 100),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterL,
          ShortParameter,
          Short.valueOf(Short.MIN_VALUE),
          Short.valueOf(Short.MIN_VALUE),
          true,
          10);
      run_ShortQuery(
          pm,
          Short_filterR,
          ShortParameter,
          Short.valueOf((short) 253),
          Short.valueOf((short) 253),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterR,
          shortParameter,
          Short.valueOf((short) 1000),
          Short.valueOf((short) 1000),
          false,
          8);
      run_ShortQuery(
          pm,
          Short_filterL,
          byteParameter,
          Byte.valueOf((byte) 75),
          Short.valueOf((short) 75),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          byteParameter,
          Byte.valueOf((byte) 75),
          Short.valueOf((short) 75),
          false,
          5);
      run_ShortQuery(
          pm,
          Short_filterL,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Short.valueOf((short) 100),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Short.valueOf((short) 100),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterL,
          charParameter,
          Character.valueOf((char) 75),
          Short.valueOf((short) 75),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          charParameter,
          Character.valueOf((char) 75),
          Short.valueOf((short) 75),
          false,
          5);
      run_ShortQuery(
          pm,
          Short_filterL,
          CharacterParameter,
          Character.valueOf((char) 100),
          Short.valueOf((short) 100),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          CharacterParameter,
          Character.valueOf((char) 200),
          Short.valueOf((short) 200),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterL,
          intParameter,
          Integer.valueOf(-10000),
          Short.valueOf((short) -10000),
          true,
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          intParameter,
          Integer.valueOf(-10000),
          Short.valueOf((short) -10000),
          false,
          2);
      run_ShortQuery(
          pm,
          Short_filterL,
          IntegerParameter,
          Integer.valueOf(10000),
          Short.valueOf((short) 10000),
          true,
          2);
      run_ShortQuery(
          pm,
          Short_filterR,
          IntegerParameter,
          Integer.valueOf(10000),
          Short.valueOf((short) 10000),
          false,
          9);
      run_ShortQuery(
          pm,
          Short_filterL,
          longParameter,
          Long.valueOf(20000),
          Short.valueOf((short) 20000),
          true,
          1);
      run_ShortQuery(
          pm,
          Short_filterR,
          longParameter,
          Long.valueOf(5000),
          Short.valueOf((short) 5000),
          false,
          8);
      run_ShortQuery(
          pm, Short_filterL, LongParameter, Long.valueOf(200), Short.valueOf((short) 200), true, 3);
      run_ShortQuery(
          pm,
          Short_filterR,
          LongParameter,
          Long.valueOf(500),
          Short.valueOf((short) 500),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterL,
          floatParameter,
          Float.valueOf(23000.0f),
          Short.valueOf((short) 23000),
          true,
          1);
      run_ShortQuery(
          pm,
          Short_filterR,
          floatParameter,
          Float.valueOf(23000.0f),
          Short.valueOf((short) 23000),
          false,
          9);
      run_ShortQuery(
          pm,
          Short_filterL,
          FloatParameter,
          Float.valueOf(10.0f),
          Short.valueOf((short) 10),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          FloatParameter,
          Float.valueOf(101.0f),
          Short.valueOf((short) 101),
          false,
          7);
      run_ShortQuery(
          pm,
          Short_filterL,
          doubleParameter,
          Double.valueOf(-10000.0),
          Short.valueOf((short) -10000),
          true,
          9);
      run_ShortQuery(
          pm,
          Short_filterR,
          doubleParameter,
          Double.valueOf(-10000.0),
          Short.valueOf((short) -10000),
          false,
          2);
      run_ShortQuery(
          pm,
          Short_filterL,
          DoubleParameter,
          Double.valueOf(101.0),
          Short.valueOf((short) 101),
          true,
          3);
      run_ShortQuery(
          pm,
          Short_filterR,
          DoubleParameter,
          Double.valueOf(23.0),
          Short.valueOf((short) 23),
          false,
          5);
      run_ShortQuery(
          pm,
          Short_filterL,
          BigIntegerParameter,
          new BigInteger("10000"),
          Short.valueOf((short) 10000),
          true,
          2);
      run_ShortQuery(
          pm,
          Short_filterR,
          BigIntegerParameter,
          new BigInteger("30000"),
          Short.valueOf((short) 30000),
          false,
          9);
      run_ShortQuery(
          pm,
          Short_filterL,
          BigDecimalParameter,
          new BigDecimal("23.0"),
          Short.valueOf((short) 23),
          true,
          5);
      run_ShortQuery(
          pm,
          Short_filterR,
          BigDecimalParameter,
          new BigDecimal("23.0"),
          Short.valueOf((short) 23),
          false,
          5);
      Short sval = Short.valueOf((short) 100);
      AllTypes alltypes = new AllTypes();
      alltypes.setShort(sval);
      run_ShortQuery(pm, Short_filterObj, AllTypesParameter, alltypes, sval, false, 7);
      sval = Short.valueOf((short) 23);
      alltypes.setShort(sval);
      run_ShortQuery(pm, Short_filterObj, AllTypesParameter, alltypes, sval, false, 5);
      run_ShortQuery(pm, Short_filterVal, null, null, Short.valueOf((short) -1000), true, 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCharacterQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_CharacterQuery(
          pm,
          Character_filterL,
          charParameter,
          Character.valueOf(Character.MIN_VALUE),
          Character.valueOf(Character.MIN_VALUE),
          true,
          10);
      run_CharacterQuery(
          pm,
          Character_filterR,
          charParameter,
          Character.valueOf(Character.MAX_VALUE),
          Character.valueOf(Character.MAX_VALUE),
          false,
          10);
      run_CharacterQuery(
          pm,
          Character_filterL,
          charParameter,
          Character.valueOf('C'),
          Character.valueOf('C'),
          true,
          6);
      run_CharacterQuery(
          pm,
          Character_filterR,
          charParameter,
          Character.valueOf('z'),
          Character.valueOf('z'),
          false,
          9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          CharacterParameter,
          Character.valueOf(' '),
          Character.valueOf(' '),
          true,
          9);
      run_CharacterQuery(
          pm,
          Character_filterR,
          CharacterParameter,
          Character.valueOf('f'),
          Character.valueOf('f'),
          false,
          7);
      run_CharacterQuery(
          pm,
          Character_filterL,
          byteParameter,
          Byte.valueOf((byte) Character.MIN_VALUE),
          Character.valueOf(Character.MIN_VALUE),
          true,
          10);
      run_CharacterQuery(
          pm,
          Character_filterR,
          ByteParameter,
          Byte.valueOf((byte) 'a'),
          Character.valueOf('a'),
          false,
          7);
      run_CharacterQuery(
          pm,
          Character_filterL,
          shortParameter,
          Short.valueOf((short) 'M'),
          Character.valueOf('M'),
          true,
          5);
      run_CharacterQuery(
          pm,
          Character_filterR,
          shortParameter,
          Short.valueOf((short) 'F'),
          Character.valueOf('F'),
          false,
          5);
      run_CharacterQuery(
          pm,
          Character_filterL,
          ShortParameter,
          Short.valueOf((short) 'A'),
          Character.valueOf('A'),
          true,
          8);
      run_CharacterQuery(
          pm,
          Character_filterR,
          ShortParameter,
          Short.valueOf((short) 'A'),
          Character.valueOf('A'),
          false,
          3);
      run_CharacterQuery(
          pm,
          Character_filterL,
          intParameter,
          Integer.valueOf('z'),
          Character.valueOf('z'),
          true,
          3);
      run_CharacterQuery(
          pm,
          Character_filterR,
          intParameter,
          Integer.valueOf('z'),
          Character.valueOf('z'),
          false,
          9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          IntegerParameter,
          Integer.valueOf('B'),
          Character.valueOf('B'),
          true,
          7);
      run_CharacterQuery(
          pm,
          Character_filterR,
          IntegerParameter,
          Integer.valueOf('B'),
          Character.valueOf('B'),
          false,
          4);
      run_CharacterQuery(
          pm, Character_filterL, longParameter, Long.valueOf('z'), Character.valueOf('z'), true, 3);
      run_CharacterQuery(
          pm,
          Character_filterR,
          longParameter,
          Long.valueOf('z'),
          Character.valueOf('z'),
          false,
          9);
      run_CharacterQuery(
          pm, Character_filterL, LongParameter, Long.valueOf('B'), Character.valueOf('B'), true, 7);
      run_CharacterQuery(
          pm,
          Character_filterR,
          LongParameter,
          Long.valueOf('B'),
          Character.valueOf('B'),
          false,
          4);
      run_CharacterQuery(
          pm,
          Character_filterL,
          floatParameter,
          Float.valueOf('z'),
          Character.valueOf('z'),
          true,
          3);
      run_CharacterQuery(
          pm,
          Character_filterR,
          floatParameter,
          Float.valueOf('z'),
          Character.valueOf('z'),
          false,
          9);
      run_CharacterQuery(
          pm,
          Character_filterL,
          FloatParameter,
          Float.valueOf('M'),
          Character.valueOf('M'),
          true,
          5);
      run_CharacterQuery(
          pm,
          Character_filterR,
          FloatParameter,
          Float.valueOf('X'),
          Character.valueOf('X'),
          false,
          7);
      run_CharacterQuery(
          pm,
          Character_filterL,
          doubleParameter,
          Double.valueOf('B'),
          Character.valueOf('B'),
          true,
          7);
      run_CharacterQuery(
          pm,
          Character_filterR,
          doubleParameter,
          Double.valueOf('B'),
          Character.valueOf('B'),
          false,
          4);
      run_CharacterQuery(
          pm,
          Character_filterL,
          DoubleParameter,
          Double.valueOf('A'),
          Character.valueOf('A'),
          true,
          8);
      run_CharacterQuery(
          pm,
          Character_filterR,
          DoubleParameter,
          Double.valueOf('A'),
          Character.valueOf('A'),
          false,
          3);
      run_CharacterQuery(
          pm,
          Character_filterL,
          BigIntegerParameter,
          new BigInteger("65"),
          Character.valueOf('A'),
          true,
          8); // 'A' == 65
      run_CharacterQuery(
          pm,
          Character_filterR,
          BigIntegerParameter,
          new BigInteger("122"),
          Character.valueOf('z'),
          false,
          9); // 'z' == 122
      run_CharacterQuery(
          pm,
          Character_filterL,
          BigDecimalParameter,
          new BigDecimal("65.00000"),
          Character.valueOf('A'),
          true,
          8);
      run_CharacterQuery(
          pm,
          Character_filterR,
          BigDecimalParameter,
          new BigDecimal("77.0000"),
          Character.valueOf('M'),
          false,
          7); // 'M' == 77
      AllTypes alltypes = new AllTypes();
      alltypes.setCharacter(Character.valueOf('A'));
      run_CharacterQuery(
          pm, Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('A'), true, 8);
      alltypes.setCharacter(Character.valueOf('b'));
      run_CharacterQuery(
          pm, Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('b'), true, 3);
      run_CharacterQuery(pm, Character_filterVal, null, null, Character.valueOf('z'), false, 9);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testIntegerQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_IntegerQuery(
          pm,
          Integer_filterL,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          true,
          10);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          intParameter,
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          false,
          10);
      run_IntegerQuery(
          pm, Integer_filterR, intParameter, Integer.valueOf(23), Integer.valueOf(23), false, 4);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          IntegerParameter,
          Integer.valueOf(1000000),
          Integer.valueOf(1000000),
          true,
          2);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          IntegerParameter,
          Integer.valueOf(1000),
          Integer.valueOf(1000),
          false,
          7);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          byteParameter,
          Byte.valueOf((byte) 100),
          Integer.valueOf(100),
          true,
          6);
      run_IntegerQuery(
          pm, Integer_filterR, byteParameter, Byte.valueOf((byte) 0), Integer.valueOf(0), false, 4);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Integer.valueOf(100),
          true,
          6);
      run_IntegerQuery(
          pm, Integer_filterR, ByteParameter, Byte.valueOf((byte) 0), Integer.valueOf(0), false, 4);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          shortParameter,
          Short.valueOf((short) 10000),
          Integer.valueOf(10000),
          true,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          shortParameter,
          Short.valueOf((short) -1000),
          Integer.valueOf(-1000),
          false,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          ShortParameter,
          Short.valueOf((short) -1000),
          Integer.valueOf(-1000),
          true,
          8);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          ShortParameter,
          Short.valueOf((short) -999),
          Integer.valueOf(-999),
          false,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          charParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          true,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          charParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          false,
          8);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          CharacterParameter,
          Character.valueOf((char) 100),
          Integer.valueOf(100),
          true,
          6);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          CharacterParameter,
          Character.valueOf((char) 10000),
          Integer.valueOf(10000),
          false,
          8);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          true,
          1);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          longParameter,
          Long.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          false,
          1);
      run_IntegerQuery(
          pm, Integer_filterL, LongParameter, Long.valueOf(10000), Integer.valueOf(10000), true, 3);
      run_IntegerQuery(
          pm, Integer_filterR, LongParameter, Long.valueOf(43), Integer.valueOf(43), false, 4);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          true,
          1);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          floatParameter,
          Float.valueOf((float) AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          false,
          1);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          FloatParameter,
          Float.valueOf((float) 10000),
          Integer.valueOf(10000),
          true,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          FloatParameter,
          Float.valueOf((float) 43),
          Integer.valueOf(43),
          false,
          4);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_POSITIVE_INT),
          true,
          1);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          doubleParameter,
          Double.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          Integer.valueOf(AllTypes.VERY_LARGE_NEGATIVE_INT),
          false,
          1);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          DoubleParameter,
          Double.valueOf(10000),
          Integer.valueOf(10000),
          true,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          DoubleParameter,
          Double.valueOf(1000001.0),
          Integer.valueOf(1000001),
          false,
          9);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          BigIntegerParameter,
          new BigInteger("1000000"),
          Integer.valueOf(1000000),
          true,
          2);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          BigIntegerParameter,
          new BigInteger("1000"),
          Integer.valueOf(1000),
          false,
          7);
      run_IntegerQuery(
          pm,
          Integer_filterL,
          BigDecimalParameter,
          new BigDecimal("10000.0"),
          Integer.valueOf(10000),
          true,
          3);
      run_IntegerQuery(
          pm,
          Integer_filterR,
          BigDecimalParameter,
          new BigDecimal("43.0"),
          Integer.valueOf(43),
          false,
          4);
      AllTypes alltypes = new AllTypes();
      alltypes.setInteger(Integer.valueOf(100));
      run_IntegerQuery(
          pm, Integer_filterObj, AllTypesParameter, alltypes, Integer.valueOf(100), false, 6);
      run_IntegerQuery(pm, Integer_filterVal, null, null, Integer.valueOf(100), false, 6);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testLongQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_LongQuery(
          pm,
          Long_filterL,
          longParameter,
          Long.valueOf(Long.MIN_VALUE),
          Long.valueOf(Long.MIN_VALUE),
          true,
          10);
      run_LongQuery(
          pm,
          Long_filterR,
          longParameter,
          Long.valueOf(Long.MAX_VALUE),
          Long.valueOf(Long.MAX_VALUE),
          false,
          10);
      run_LongQuery(pm, Long_filterL, LongParameter, Long.valueOf(100), Long.valueOf(100), true, 5);
      run_LongQuery(pm, Long_filterR, LongParameter, Long.valueOf(23), Long.valueOf(23), false, 5);
      run_LongQuery(
          pm, Long_filterL, byteParameter, Byte.valueOf((byte) 100), Long.valueOf(100), true, 5);
      run_LongQuery(
          pm, Long_filterR, byteParameter, Byte.valueOf((byte) 0), Long.valueOf(0), false, 5);
      run_LongQuery(
          pm, Long_filterL, ByteParameter, Byte.valueOf((byte) 100), Long.valueOf(100), true, 5);
      run_LongQuery(
          pm, Long_filterR, ByteParameter, Byte.valueOf((byte) 0), Long.valueOf(0), false, 5);
      run_LongQuery(
          pm,
          Long_filterL,
          shortParameter,
          Short.valueOf((short) -1000),
          Long.valueOf(-1000),
          true,
          8);
      run_LongQuery(
          pm,
          Long_filterR,
          shortParameter,
          Short.valueOf((short) 1000),
          Long.valueOf(1000),
          false,
          8);
      run_LongQuery(
          pm, Long_filterL, ShortParameter, Short.valueOf((short) 101), Long.valueOf(101), true, 3);
      run_LongQuery(
          pm, Long_filterR, ShortParameter, Short.valueOf((short) 32), Long.valueOf(32), false, 5);
      run_LongQuery(
          pm, Long_filterL, charParameter, Character.valueOf((char) 0), Long.valueOf(0), true, 6);
      run_LongQuery(
          pm,
          Long_filterR,
          charParameter,
          Character.valueOf((char) 100),
          Long.valueOf(100),
          false,
          7);
      run_LongQuery(
          pm,
          Long_filterL,
          CharacterParameter,
          Character.valueOf((char) 23),
          Long.valueOf(23),
          true,
          5);
      run_LongQuery(
          pm,
          Long_filterR,
          CharacterParameter,
          Character.valueOf((char) 1110),
          Long.valueOf(1110),
          false,
          8);
      run_LongQuery(
          pm, Long_filterL, intParameter, Integer.valueOf(100), Long.valueOf(100), true, 5);
      run_LongQuery(pm, Long_filterR, intParameter, Integer.valueOf(0), Long.valueOf(0), false, 5);
      run_LongQuery(
          pm, Long_filterL, IntegerParameter, Integer.valueOf(23), Long.valueOf(23), true, 5);
      run_LongQuery(
          pm,
          Long_filterR,
          IntegerParameter,
          Integer.valueOf(1000000),
          Long.valueOf(1000000),
          false,
          9);
      run_LongQuery(
          pm,
          Long_filterL,
          floatParameter,
          Float.valueOf((float) -1000000.0),
          Long.valueOf(-1000000),
          true,
          9);
      run_LongQuery(
          pm,
          Long_filterR,
          floatParameter,
          Float.valueOf((float) Long.MAX_VALUE),
          Long.valueOf(Long.MAX_VALUE),
          false,
          10);
      run_LongQuery(
          pm, Long_filterL, FloatParameter, Float.valueOf(100.0f), Long.valueOf(100), true, 5);
      run_LongQuery(
          pm, Long_filterR, FloatParameter, Float.valueOf(32.0f), Long.valueOf(32), false, 5);
      run_LongQuery(
          pm,
          Long_filterL,
          doubleParameter,
          Double.valueOf(-1000000.0),
          Long.valueOf(-1000000),
          true,
          9);
      run_LongQuery(
          pm,
          Long_filterR,
          doubleParameter,
          Double.valueOf((double) Long.MAX_VALUE),
          Long.valueOf(Long.MAX_VALUE),
          false,
          10);
      run_LongQuery(
          pm, Long_filterL, DoubleParameter, Double.valueOf(100.0), Long.valueOf(100), true, 5);
      run_LongQuery(
          pm, Long_filterR, DoubleParameter, Double.valueOf(32.0), Long.valueOf(32), false, 5);
      run_LongQuery(
          pm, Long_filterL, BigIntegerParameter, new BigInteger("23"), Long.valueOf(23), true, 5);
      run_LongQuery(
          pm,
          Long_filterR,
          BigIntegerParameter,
          new BigInteger("1000000"),
          Long.valueOf(1000000),
          false,
          9);
      run_LongQuery(
          pm,
          Long_filterL,
          BigDecimalParameter,
          new BigDecimal("100.0"),
          Long.valueOf(100),
          true,
          5);
      run_LongQuery(
          pm,
          Long_filterR,
          BigDecimalParameter,
          new BigDecimal("32.0"),
          Long.valueOf(32),
          false,
          5);
      AllTypes alltypes = new AllTypes();
      alltypes.setLong(Long.valueOf(100));
      run_LongQuery(pm, Long_filterObj, AllTypesParameter, alltypes, Long.valueOf(100), true, 5);
      run_LongQuery(pm, Long_filterVal, null, null, Long.valueOf(-1000), true, 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFloatQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_FloatQuery(
          pm,
          Float_filterL,
          floatParameter,
          Float.valueOf(-1000000000.0f),
          Float.valueOf(-1000000000.0f),
          true,
          9);
      run_FloatQuery(
          pm,
          Float_filterR,
          floatParameter,
          Float.valueOf(100.0f),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          FloatParameter,
          Float.valueOf((float) 0.0),
          Float.valueOf(0.0f),
          true,
          8);
      run_FloatQuery(
          pm,
          Float_filterR,
          FloatParameter,
          Float.valueOf((float) 4.0),
          Float.valueOf(4.0f),
          false,
          3);
      run_FloatQuery(
          pm, Float_filterL, byteParameter, Byte.valueOf((byte) 0), Float.valueOf(0.0f), true, 8);
      run_FloatQuery(
          pm,
          Float_filterR,
          byteParameter,
          Byte.valueOf((byte) 23),
          Float.valueOf(23.0f),
          false,
          3);
      run_FloatQuery(
          pm, Float_filterL, ByteParameter, Byte.valueOf((byte) 34), Float.valueOf(34.0f), true, 7);
      run_FloatQuery(
          pm,
          Float_filterR,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          shortParameter,
          Short.valueOf((short) 0),
          Float.valueOf(0.0f),
          true,
          8);
      run_FloatQuery(
          pm,
          Float_filterR,
          shortParameter,
          Short.valueOf((short) 23),
          Float.valueOf(23.0f),
          false,
          3);
      run_FloatQuery(
          pm,
          Float_filterL,
          ShortParameter,
          Short.valueOf((short) 34),
          Float.valueOf(34.0f),
          true,
          7);
      run_FloatQuery(
          pm,
          Float_filterR,
          ShortParameter,
          Short.valueOf((short) 100),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          charParameter,
          Character.valueOf((char) 0),
          Float.valueOf(0.0f),
          true,
          8);
      run_FloatQuery(
          pm,
          Float_filterR,
          charParameter,
          Character.valueOf((char) 23),
          Float.valueOf(23.0f),
          false,
          3);
      run_FloatQuery(
          pm,
          Float_filterL,
          CharacterParameter,
          Character.valueOf((char) 34),
          Float.valueOf(34.0f),
          true,
          7);
      run_FloatQuery(
          pm,
          Float_filterR,
          CharacterParameter,
          Character.valueOf((char) 100),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          intParameter,
          Integer.valueOf(50000000),
          Float.valueOf(50000000.0f),
          true,
          3);
      run_FloatQuery(
          pm, Float_filterR, intParameter, Integer.valueOf(23), Float.valueOf(23.0f), false, 3);
      run_FloatQuery(
          pm, Float_filterL, IntegerParameter, Integer.valueOf(34), Float.valueOf(34.0f), true, 7);
      run_FloatQuery(
          pm,
          Float_filterR,
          IntegerParameter,
          Integer.valueOf(100),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          longParameter,
          Long.valueOf(50000000),
          Float.valueOf(50000000.0f),
          true,
          3);
      run_FloatQuery(
          pm, Float_filterR, longParameter, Long.valueOf(23), Float.valueOf(23.0f), false, 3);
      run_FloatQuery(
          pm, Float_filterL, LongParameter, Long.valueOf(34), Float.valueOf(34.0f), true, 7);
      run_FloatQuery(
          pm, Float_filterR, LongParameter, Long.valueOf(100), Float.valueOf(100.0f), false, 5);
      run_FloatQuery(
          pm,
          Float_filterL,
          doubleParameter,
          Double.valueOf(50000000.0f),
          Float.valueOf(50000000.0f),
          true,
          3);
      run_FloatQuery(
          pm,
          Float_filterR,
          doubleParameter,
          Double.valueOf(100.0f),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm, Float_filterL, DoubleParameter, Double.valueOf(0.0f), Float.valueOf(0.0f), true, 8);
      run_FloatQuery(
          pm,
          Float_filterR,
          DoubleParameter,
          Double.valueOf(100.0f),
          Float.valueOf(100.0f),
          false,
          5);
      run_FloatQuery(
          pm,
          Float_filterL,
          BigIntegerParameter,
          new BigInteger("55000000"),
          Float.valueOf(55000000.0f),
          true,
          2);
      run_FloatQuery(
          pm,
          Float_filterR,
          BigIntegerParameter,
          new BigInteger("23"),
          Float.valueOf(23.0f),
          false,
          3);
      run_FloatQuery(
          pm,
          Float_filterL,
          BigDecimalParameter,
          new BigDecimal("55000000.0"),
          Float.valueOf(55000000.0f),
          true,
          2);
      run_FloatQuery(
          pm,
          Float_filterR,
          BigDecimalParameter,
          new BigDecimal("-20.5"),
          Float.valueOf(-20.5f),
          false,
          2);
      AllTypes alltypes = new AllTypes();
      alltypes.setFloat(Float.valueOf(23.23f));
      run_FloatQuery(
          pm, Float_filterObj, AllTypesParameter, alltypes, Float.valueOf(23.23f), false, 3);
      run_FloatQuery(pm, Float_filterVal, null, null, Float.valueOf(100.0f), true, 7);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDoubleQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_DoubleQuery(
          pm,
          Double_filterL,
          doubleParameter,
          Double.valueOf(-999999999999.0),
          Double.valueOf(-999999999999.0),
          true,
          9);
      run_DoubleQuery(
          pm,
          Double_filterR,
          doubleParameter,
          Double.valueOf(9999999999999.0),
          Double.valueOf(9999999999999.0),
          false,
          9);
      run_DoubleQuery(
          pm, Double_filterL, DoubleParameter, Double.valueOf(0.0), Double.valueOf(0.0), true, 7);
      run_DoubleQuery(
          pm,
          Double_filterR,
          DoubleParameter,
          Double.valueOf(23.34),
          Double.valueOf(23.34),
          false,
          4);
      run_DoubleQuery(
          pm,
          Double_filterL,
          byteParameter,
          Byte.valueOf((byte) 100),
          Double.valueOf(100.0),
          true,
          6);
      run_DoubleQuery(
          pm, Double_filterR, byteParameter, Byte.valueOf((byte) 0), Double.valueOf(0.0), false, 4);
      run_DoubleQuery(
          pm,
          Double_filterL,
          ByteParameter,
          Byte.valueOf((byte) -23),
          Double.valueOf(-23.0),
          true,
          7);
      run_DoubleQuery(
          pm,
          Double_filterR,
          ByteParameter,
          Byte.valueOf((byte) 100),
          Double.valueOf(100.0),
          false,
          6);
      run_DoubleQuery(
          pm,
          Double_filterL,
          shortParameter,
          Short.valueOf((short) 100),
          Double.valueOf(100.0),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          shortParameter,
          Short.valueOf((short) 0),
          Double.valueOf(0.0),
          false,
          4);
      run_DoubleQuery(
          pm,
          Double_filterL,
          ShortParameter,
          Short.valueOf((short) 23),
          Double.valueOf(23.0),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          ShortParameter,
          Short.valueOf((short) 400),
          Double.valueOf(400.0),
          false,
          7);
      run_DoubleQuery(
          pm,
          Double_filterL,
          charParameter,
          Character.valueOf((char) 100),
          Double.valueOf(100.0),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          charParameter,
          Character.valueOf((char) 0),
          Double.valueOf(0.0),
          false,
          4);
      run_DoubleQuery(
          pm,
          Double_filterL,
          CharacterParameter,
          Character.valueOf((char) 23),
          Double.valueOf(23.0),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          CharacterParameter,
          Character.valueOf((char) 400),
          Double.valueOf(400.0),
          false,
          7);
      run_DoubleQuery(
          pm, Double_filterL, intParameter, Integer.valueOf(100), Double.valueOf(100.0), true, 6);
      run_DoubleQuery(
          pm, Double_filterR, intParameter, Integer.valueOf(0), Double.valueOf(0.0), false, 4);
      run_DoubleQuery(
          pm, Double_filterL, IntegerParameter, Integer.valueOf(23), Double.valueOf(23.0), true, 6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          IntegerParameter,
          Integer.valueOf(100),
          Double.valueOf(100.0),
          false,
          6);
      run_DoubleQuery(
          pm, Double_filterL, longParameter, Long.valueOf(100), Double.valueOf(100.0), true, 6);
      run_DoubleQuery(
          pm, Double_filterR, longParameter, Long.valueOf(0), Double.valueOf(0.0), false, 4);
      run_DoubleQuery(
          pm, Double_filterL, LongParameter, Long.valueOf(23), Double.valueOf(23.0), true, 6);
      run_DoubleQuery(
          pm, Double_filterR, LongParameter, Long.valueOf(100), Double.valueOf(100.0), false, 6);
      run_DoubleQuery(
          pm, Double_filterL, floatParameter, Float.valueOf(0.0f), Double.valueOf(0.0f), true, 7);
      run_DoubleQuery(
          pm,
          Double_filterR,
          floatParameter,
          Float.valueOf(100.0f),
          Double.valueOf(100.0f),
          false,
          6);
      run_DoubleQuery(
          pm,
          Double_filterL,
          FloatParameter,
          Float.valueOf(100.0f),
          Double.valueOf(100.0f),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          FloatParameter,
          Float.valueOf(69.96f),
          Double.valueOf(69.96),
          false,
          4);
      run_DoubleQuery(
          pm,
          Double_filterL,
          BigIntegerParameter,
          new BigInteger("5000"),
          Double.valueOf(5000.0),
          true,
          3);
      run_DoubleQuery(
          pm,
          Double_filterR,
          BigIntegerParameter,
          new BigInteger("-20"),
          Double.valueOf(-20.0),
          false,
          3);
      run_DoubleQuery(
          pm,
          Double_filterL,
          BigDecimalParameter,
          new BigDecimal("100.0"),
          Double.valueOf(100.0),
          true,
          6);
      run_DoubleQuery(
          pm,
          Double_filterR,
          BigDecimalParameter,
          new BigDecimal("69.96"),
          Double.valueOf(69.96),
          false,
          4);
      AllTypes alltypes = new AllTypes();
      alltypes.setDouble(Double.valueOf(-999999999999.0));
      run_DoubleQuery(
          pm,
          Double_filterObj,
          AllTypesParameter,
          alltypes,
          Double.valueOf(-999999999999.0),
          true,
          9);
      run_DoubleQuery(pm, Double_filterVal, null, null, Double.valueOf(100.0), false, 6);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testStringQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      run_StringQuery(pm, String_filterL, StringParameter, "JDO", "JDO", true, 9);
      run_StringQuery(pm, String_filterR, StringParameter, "JDBC", "JDBC", false, 1);
      AllTypes alltypes = new AllTypes();
      alltypes.setString("abcde");
      run_StringQuery(pm, String_filterObj, AllTypesParameter, alltypes, "abcde", true, 3);
      run_StringQuery(pm, String_filterVal1, null, null, "Java", false, 6);
      run_StringQuery(pm, String_filterVal2, null, null, "", false, 1);

    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBigDecimalQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      BigDecimal bd = new BigDecimal("100.0");
      run_BigDecimalQuery(pm, BigDecimal_filterL, BigDecimalParameter, bd, bd, true, 6);
      bd = new BigDecimal("-234234.23333");
      run_BigDecimalQuery(pm, BigDecimal_filterR, BigDecimalParameter, bd, bd, false, 2);
      bd = new BigDecimal("989899.33304953");
      run_BigDecimalQuery(pm, BigDecimal_filterL, BigDecimalParameter, bd, bd, true, 3);
      bd = new BigDecimal("-1123123.22");
      run_BigDecimalQuery(pm, BigDecimal_filterR, BigDecimalParameter, bd, bd, false, 1);
      AllTypes alltypes = new AllTypes();
      alltypes.setBigDecimal(bd);
      run_BigDecimalQuery(pm, BigDecimal_filterObj, AllTypesParameter, alltypes, bd, true, 10);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testBigIntegerQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      BigInteger bi = new BigInteger("987034534985043985");
      run_BigIntegerQuery(pm, BigInteger_filterL, BigIntegerParameter, bi, bi, true, 1);
      bi = new BigInteger("-999999999999999999");
      run_BigIntegerQuery(pm, BigInteger_filterR, BigIntegerParameter, bi, bi, false, 1);
      bi = new BigInteger("-9999999999999999999");
      run_BigIntegerQuery(pm, BigInteger_filterR, BigIntegerParameter, bi, bi, false, 0);
      bi = new BigInteger("1333330");
      AllTypes alltypes = new AllTypes();
      alltypes.setBigInteger(bi);
      run_BigIntegerQuery(pm, BigInteger_filterObj, AllTypesParameter, alltypes, bi, false, 8);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDateQueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.UK);
      gc.clear();
      gc.set(1999, Calendar.DECEMBER, 31, 9, 0, 0);
      Date d = gc.getTime();
      run_DateQuery(pm, Date_filterL, DateParameter, d, d, true, 3);
      gc.set(1992, Calendar.NOVEMBER, 22, 9, 0, 0);
      d = gc.getTime();
      run_DateQuery(pm, Date_filterR, DateParameter, d, d, false, 6);
      gc.set(1959, Calendar.OCTOBER, 9, 9, 0, 0);
      d = gc.getTime();
      run_DateQuery(pm, Date_filterR, DateParameter, d, d, false, 3);
      gc.set(1995, Calendar.JUNE, 14, 9, 0, 0);
      d = gc.getTime();
      AllTypes alltypes = new AllTypes();
      alltypes.setDate(d);
      run_DateQuery(pm, Date_filterObj, AllTypesParameter, alltypes, d, false, 7);
    } finally {
      cleanupPM(pm);
    }
  }

  private void run_byteQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      byte value,
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      byte val = obj.getbyte();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      short val = obj.getshort();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      char val = obj.getchar();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      int val = obj.getint();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      long val = obj.getlong();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      float val = obj.getfloat();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      double val = obj.getdouble();
      boolean correct_value = valueOnLeft ? value <= val : val <= value;
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
              + expected_count
              + ", got "
              + cnt,
          filter,
          parameter);
    }
    queryInfo.query.close(queryInfo.queryResult);
    pm.currentTransaction().rollback();
  }

  private void run_ByteQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      Byte value,
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Byte val = obj.getByte();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Short val = obj.getShort();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Character val = obj.getCharacter();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Integer val = obj.getInteger();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Long val = obj.getLong();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Float val = obj.getFloat();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Double val = obj.getDouble();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      String val = obj.getString();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      BigDecimal val = obj.getBigDecimal();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      BigInteger val = obj.getBigInteger();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
      boolean valueOnLeft,
      int expected_count) {
    pm.currentTransaction().begin();
    QueryInfo queryInfo = runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
    if (queryInfo.queryResult == null) return;
    Iterator<AllTypes> iter = queryInfo.queryResult.iterator();
    int cnt = 0;
    while (iter.hasNext()) {
      AllTypes obj = iter.next();
      Date val = obj.getDate();
      boolean correct_value =
          valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
      if (!correct_value) {
        fail(
            ASSERTION_FAILED,
            "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: "
                + val
                + ", with parameter value: "
                + value,
            filter,
            parameter);
      }
      cnt++;
    }
    if (cnt != expected_count) {
      fail(
          ASSERTION_FAILED,
          "JDOQL LessThanOrEqual test returns wrong number of instances, expected "
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
