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
 
package org.apache.jdo.tck.query.jdoql.operators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Equality Operator Support
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-14.
 *<BR>
 *<B>Assertion Description: </B>
The equal operator (<code>==</code>) is supported for the following types:
<UL>
<LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code></LI>
<LI><code>float, double, Float, Double</code></LI>
<LI><code>BigDecimal, BigInteger</code></LI>
<LI><code>Boolean, boolean</code></LI>
<LI>any class instance or array</LI>
<LI><code>Date, String</code></LI>
</UL>
The operation on object-valued fields of wrapper types (<code>Boolean, Byte,
Short, Integer, Long, Float, and Double</code>), and numeric types
(<code>BigDecimal</code> and <code>BigInteger</code>) use the wrapped values
as operands. Equality comparison of object-valued fields of
<code>PersistenceCapable</code> types use the JDO Identity comparison
of the references. Thus, two objects will compare equal if they have the same
JDO Identity. Equality comparison of object-valued fields of
non-<code>PersistenceCapable</code> types uses the <code>equals</code>
method of the field type.

 */

public class Equality extends ComparisonTests {

    private static      String      boolean_filterL     = "value == fld_boolean";
    private static      String      boolean_filterR     = "fld_boolean == value";
    private static      String      boolean_filterT     = "fld_boolean == true";
    private static      String      boolean_filterF     = "false == fld_boolean";
    private static      String      boolean_filterObj   = "value.fld_boolean == fld_boolean";
    
    private static      String      byte_filterL        = "value == fld_byte";
    private static      String      byte_filterR        = "fld_byte == value";
    private static      String      byte_filterObj      = "value.fld_byte == fld_byte";
    private static      String      byte_filterVal      = "fld_byte == 100";
    
    private static      String      char_filterL        = "value == fld_char";
    private static      String      char_filterR        = "fld_char == value";
    private static      String      char_filterObj      = "value.fld_char == fld_char";
    private static      String      char_filterVal      = "'M' == fld_char";
    
    private static      String      double_filterL      = "value == fld_double";
    private static      String      double_filterR      = "fld_double == value";
    private static      String      double_filterObj    = "value.fld_double == fld_double";
    private static      String      double_filterVal    = "fld_double == 100.0";
    
    private static      String      float_filterL       = "value == fld_float";
    private static      String      float_filterR       = "fld_float == value";
    private static      String      float_filterObj     = "fld_float == value.fld_float";
    private static      String      float_filterVal     = "fld_float == 100.0";
    
    private static      String      int_filterL         = "value == fld_int";
    private static      String      int_filterR         = "fld_int == value";
    private static      String      int_filterObj       = "value.fld_int == fld_int";
    private static      String      int_filterVal       = "fld_int == 1000";
    
    private static      String      long_filterL        = "value == fld_long";
    private static      String      long_filterR        = "fld_long == value";
    private static      String      long_filterObj      = "fld_long == value.fld_long";
    private static      String      long_filterVal      = "fld_long == 1000000";
    
    private static      String      short_filterL       = "value == fld_short";
    private static      String      short_filterR       = "fld_short == value";
    private static      String      short_filterObj     = "value.fld_short == fld_short";
    private static      String      short_filterVal     = "1000 == fld_short";
    
    private static      String      Boolean_filterL     = "value == fld_Boolean";
    private static      String      Boolean_filterR     = "fld_Boolean == value";
    private static      String      Boolean_filterT     = "fld_Boolean == true";
    private static      String      Boolean_filterF     = "false == fld_Boolean";
    private static      String      Boolean_filterObj   = "value.fld_Boolean == fld_Boolean";
    private static      String      Boolean_filterVal   = "fld_Boolean == false";
    
    private static      String      Byte_filterL        = "value == fld_Byte";
    private static      String      Byte_filterR        = "fld_Byte == value";
    private static      String      Byte_filterObj      = "fld_Byte == value.fld_Byte";
    private static      String      Byte_filterVal      = "100 == fld_Byte";
    
    private static      String      Character_filterL   = "value == fld_Character";
    private static      String      Character_filterR   = "fld_Character == value";
    private static      String      Character_filterObj = "value.fld_Character == fld_Character";
    private static      String      Character_filterVal = "fld_Character == 'z'";
    
    private static      String      Double_filterL      = "value == fld_Double";
    private static      String      Double_filterR      = "fld_Double == value";
    private static      String      Double_filterObj    = "value.fld_Double == fld_Double";
    private static      String      Double_filterVal    = "fld_Double == -25.5";
    
    private static      String      Float_filterL       = "value == fld_Float";
    private static      String      Float_filterR       = "fld_Float == value";
    private static      String      Float_filterObj     = "fld_Float == value.fld_Float";
    private static      String      Float_filterVal     = "100.0f == fld_Float";
    
    private static      String      Integer_filterL     = "value == fld_Integer";
    private static      String      Integer_filterR     = "fld_Integer == value";
    private static      String      Integer_filterObj   = "fld_Integer == value.fld_Integer";
    private static      String      Integer_filterVal   = "fld_Integer == 100";
    
    private static      String      Long_filterL        = "value == fld_Long";
    private static      String      Long_filterR        = "fld_Long == value";
    private static      String      Long_filterObj      = "value.fld_Long == fld_Long";
    private static      String      Long_filterVal      = "-1000 == fld_Long";
    
    private static      String      Short_filterL       = "value == fld_Short";
    private static      String      Short_filterR       = "fld_Short == value";
    private static      String      Short_filterObj     = "fld_Short == value.fld_Short";
    private static      String      Short_filterVal     = "-1000 == fld_Short";
    
    private static      String      String_filterL      = "value == fld_String";
    private static      String      String_filterR      = "fld_String == value";
    private static      String      String_filterObj    = "value.fld_String == fld_String";
    private static      String      String_filterVal1   = "fld_String == \"Java\"";
    private static      String      String_filterVal2   = "fld_String == \"\"";
    
    private static      String      Locale_filterL      = "value == fld_Locale";
    private static      String      Locale_filterR      = "fld_Locale == value";
    private static      String      Locale_filterObj    = "value.fld_Locale == fld_Locale";

    private static      String      Date_filterL        = "value == fld_Date";
    private static      String      Date_filterR        = "fld_Date == value";
    private static      String      Date_filterObj      = "fld_Date == value.fld_Date";
    
    private static      String      BigDecimal_filterL  = "value == fld_BigDecimal";
    private static      String      BigDecimal_filterR  = "fld_BigDecimal == value";
    private static      String      BigDecimal_filterObj = "value.fld_BigDecimal == fld_BigDecimal";
    
    private static      String      BigInteger_filterL  = "value == fld_BigInteger";
    private static      String      BigInteger_filterR  = "fld_BigInteger == value";
    private static      String      BigInteger_filterObj = "fld_BigInteger == value.fld_BigInteger";

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-14 (Equality) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Equality.class);
    }

    /** */
    public void test() {
        pm = getPM();
        tx = pm.currentTransaction();
        runQueries();
    }

    /** */
    private void runQueries()
    {
        run_booleanQuery(boolean_filterL, booleanParameter, Boolean.TRUE, true, 4);
        run_booleanQuery(boolean_filterL, booleanParameter, Boolean.FALSE, false, 6);
        run_booleanQuery(boolean_filterR, booleanParameter, Boolean.TRUE, true, 4);
        run_booleanQuery(boolean_filterR, booleanParameter, Boolean.FALSE, false, 6);
        run_booleanQuery(boolean_filterL, BooleanParameter, Boolean.TRUE, true, 4);
        run_booleanQuery(boolean_filterL, BooleanParameter, Boolean.FALSE, false, 6);
        run_booleanQuery(boolean_filterR, BooleanParameter, Boolean.TRUE, true, 4);
        run_booleanQuery(boolean_filterR, BooleanParameter, Boolean.FALSE, false, 6);
        run_booleanQuery(boolean_filterT, null, null, true, 4);
        run_booleanQuery(boolean_filterF, null, null, false, 6);
        AllTypes alltypes = new AllTypes();
        alltypes.setboolean(true);
        run_booleanQuery(boolean_filterObj, AllTypesParameter, alltypes, true, 4);
        alltypes.setboolean(false);
        run_booleanQuery(boolean_filterObj, AllTypesParameter, alltypes, false, 6);

        run_byteQuery(byte_filterL, byteParameter, Byte.valueOf((byte)50), (byte)50, 2);
        run_byteQuery(byte_filterR, byteParameter, Byte.valueOf(Byte.MIN_VALUE), Byte.MIN_VALUE, 1);
        run_byteQuery(byte_filterL, ByteParameter, Byte.valueOf((byte)20), (byte)20, 0);
        run_byteQuery(byte_filterR, ByteParameter, Byte.valueOf(Byte.MAX_VALUE), Byte.MAX_VALUE, 1);    
        run_byteQuery(byte_filterL, shortParameter, Short.valueOf((short)50), (byte)50, 2);
        run_byteQuery(byte_filterR, shortParameter, Short.valueOf((short)50), (byte)50, 2);
        run_byteQuery(byte_filterL, ShortParameter, Short.valueOf((short)-100), (byte)-100, 1);
        run_byteQuery(byte_filterR, ShortParameter, Short.valueOf((short)-100), (byte)-100, 1);
        run_byteQuery(byte_filterL, charParameter, Character.valueOf((char)50), (byte)50, 2);
        run_byteQuery(byte_filterR, charParameter, Character.valueOf((char)50), (byte)50, 2);
        run_byteQuery(byte_filterL, CharacterParameter, Character.valueOf((char)50), (byte)50, 2);
        run_byteQuery(byte_filterR, CharacterParameter, Character.valueOf((char)100), (byte)100, 1);
        run_byteQuery(byte_filterL, intParameter, Integer.valueOf(50), (byte)50, 2);
        run_byteQuery(byte_filterR, intParameter, Integer.valueOf(50), (byte)50, 2);
        run_byteQuery(byte_filterL, IntegerParameter, Integer.valueOf(-100), (byte)-100, 1);
        run_byteQuery(byte_filterR, IntegerParameter, Integer.valueOf(-100), (byte)-100, 1);
        run_byteQuery(byte_filterL, longParameter, Long.valueOf(50), (byte)50, 2);
        run_byteQuery(byte_filterR, longParameter, Long.valueOf(50), (byte)50, 2);
        run_byteQuery(byte_filterL, LongParameter, Long.valueOf(-100), (byte)-100, 1);
        run_byteQuery(byte_filterR, LongParameter, Long.valueOf(-100), (byte)-100, 1);
        run_byteQuery(byte_filterL, floatParameter, Float.valueOf(50.0f), (byte)50, 2);
        run_byteQuery(byte_filterR, floatParameter, Float.valueOf(50.0f), (byte)50, 2);
        run_byteQuery(byte_filterL, FloatParameter, Float.valueOf(-100.0f), (byte)-100, 1);
        run_byteQuery(byte_filterR, FloatParameter, Float.valueOf(-100.0f), (byte)-100, 1);
        run_byteQuery(byte_filterL, doubleParameter, Double.valueOf(50.0), (byte)50, 2);
        run_byteQuery(byte_filterR, doubleParameter, Double.valueOf(50.0), (byte)50, 2);
        run_byteQuery(byte_filterL, DoubleParameter, Double.valueOf(-100.0), (byte)-100, 1);
        run_byteQuery(byte_filterR, DoubleParameter, Double.valueOf(-100.0), (byte)-100, 1);
        run_byteQuery(byte_filterL, BigIntegerParameter, new BigInteger("50"), (byte)50, 2);
        run_byteQuery(byte_filterR, BigIntegerParameter, new BigInteger("-100"), (byte)-100, 1);
        run_byteQuery(byte_filterL, BigDecimalParameter, new BigDecimal("100.0"), (byte)100, 1);
        run_byteQuery(byte_filterR, BigDecimalParameter, new BigDecimal("10.0"), (byte)10, 1);    
        alltypes.setbyte((byte)50);
        run_byteQuery(byte_filterObj, AllTypesParameter, alltypes, (byte)50, 2);
        alltypes.setbyte((byte)45);
        run_byteQuery(byte_filterObj, AllTypesParameter, alltypes, (byte)45, 0);
        run_byteQuery(byte_filterVal, null, null, (byte)100, 1);

        run_shortQuery(short_filterL, shortParameter, Short.valueOf((short)100), (short)100, 2);
        run_shortQuery(short_filterR, shortParameter, Short.valueOf((short)100), (short)100, 2);
        run_shortQuery(short_filterL, ShortParameter, Short.valueOf(Short.MIN_VALUE), Short.MIN_VALUE, 1);
        run_shortQuery(short_filterR, ShortParameter, Short.valueOf((short)253), (short)253, 0);
        run_shortQuery(short_filterR, shortParameter, Short.valueOf((short)1000), (short)1000, 1);
        run_shortQuery(short_filterL, byteParameter, Byte.valueOf((byte)75), (short)75, 0);
        run_shortQuery(short_filterR, byteParameter, Byte.valueOf((byte)75), (short)75, 0);
        run_shortQuery(short_filterL, ByteParameter, Byte.valueOf((byte)100), (short)100, 2);
        run_shortQuery(short_filterR, ByteParameter, Byte.valueOf((byte)100), (short)100, 2);
        run_shortQuery(short_filterL, charParameter, Character.valueOf((char)75), (short)75, 0);
        run_shortQuery(short_filterR, charParameter, Character.valueOf((char)75), (short)75, 0);
        run_shortQuery(short_filterL, CharacterParameter, Character.valueOf((char)100), (short)100, 2);
        run_shortQuery(short_filterR, CharacterParameter, Character.valueOf((char)100), (short)100, 2);
        run_shortQuery(short_filterL, intParameter, Integer.valueOf(-10000), (short)-10000, 1);
        run_shortQuery(short_filterR, intParameter, Integer.valueOf(-10000), (short)-10000, 1);
        run_shortQuery(short_filterL, IntegerParameter, Integer.valueOf(10000), (short)10000, 1);
        run_shortQuery(short_filterR, IntegerParameter, Integer.valueOf(10000), (short)10000, 1);
        run_shortQuery(short_filterL, longParameter, Long.valueOf(10000), (short) 10000, 1);
        run_shortQuery(short_filterR, longParameter, Long.valueOf(10000), (short) 10000, 1);
        run_shortQuery(short_filterL, LongParameter, Long.valueOf(100), (short)100, 2);
        run_shortQuery(short_filterR, LongParameter, Long.valueOf(100), (short)100, 2);
        run_shortQuery(short_filterL, floatParameter, Float.valueOf((float)23000), (short)23000, 0);
        run_shortQuery(short_filterR, floatParameter, Float.valueOf((float)23000), (short)23000, 0);
        run_shortQuery(short_filterL, FloatParameter, Float.valueOf((float)100), (short)100, 2);
        run_shortQuery(short_filterR, FloatParameter, Float.valueOf((float)100), (short)100, 2);
        run_shortQuery(short_filterL, doubleParameter, Double.valueOf(-10000), (short)-10000, 1);
        run_shortQuery(short_filterR, doubleParameter, Double.valueOf(-10000), (short)-10000, 1);
        run_shortQuery(short_filterL, DoubleParameter, Double.valueOf(23), (short)23, 0);
        run_shortQuery(short_filterR, DoubleParameter, Double.valueOf(23), (short)23, 0);
        run_shortQuery(short_filterL, BigIntegerParameter, new BigInteger("999"), (short)999, 0);
        run_shortQuery(short_filterR, BigIntegerParameter, new BigInteger("-1000"), (short)-1000, 1);
        run_shortQuery(short_filterL, BigDecimalParameter, new BigDecimal("100.0"), (short)100, 2);
        run_shortQuery(short_filterR, BigDecimalParameter, new BigDecimal("100.0001"), (short)100, 0);    
        alltypes.setshort((short)100);
        run_shortQuery(short_filterObj, AllTypesParameter, alltypes, (short)100, 2);
        alltypes.setshort((short)23);
        run_shortQuery(short_filterObj, AllTypesParameter, alltypes, (short)23, 0);
        run_shortQuery(short_filterVal, null, null, (short)1000, 1);
    
        run_charQuery(char_filterL, charParameter, Character.valueOf(Character.MIN_VALUE), Character.MIN_VALUE, 1);
        run_charQuery(char_filterR, charParameter, Character.valueOf(Character.MAX_VALUE), Character.MAX_VALUE, 1);
        run_charQuery(char_filterL, charParameter, Character.valueOf('C'), 'C', 0);
        run_charQuery(char_filterR, charParameter, Character.valueOf('z'), 'z', 2);
        run_charQuery(char_filterL, CharacterParameter, Character.valueOf(' '), ' ', 1);
        run_charQuery(char_filterR, CharacterParameter, Character.valueOf('f'), 'f', 0);
        run_charQuery(char_filterL, byteParameter, Byte.valueOf((byte)Character.MIN_VALUE), (char)Character.MIN_VALUE, 1);
        run_charQuery(char_filterR, byteParameter, Byte.valueOf((byte)122), 'z', 2);
        run_charQuery(char_filterL, ByteParameter, Byte.valueOf((byte)'a'), 'a', 0);
        run_charQuery(char_filterR, ByteParameter, Byte.valueOf((byte)'a'), 'a', 0);
        run_charQuery(char_filterL, shortParameter, Short.valueOf((short)'M'), 'M', 2);
        run_charQuery(char_filterR, shortParameter, Short.valueOf((short)'M'), 'M', 2);
        run_charQuery(char_filterL, ShortParameter, Short.valueOf((short)'A'), 'A', 1);
        run_charQuery(char_filterR, ShortParameter, Short.valueOf((short)'A'), 'A', 1);
        run_charQuery(char_filterL, intParameter, Integer.valueOf('z'), 'z', 2);
        run_charQuery(char_filterR, intParameter, Integer.valueOf('z'), 'z', 2);
        run_charQuery(char_filterL, IntegerParameter, Integer.valueOf('B'), 'B', 1);
        run_charQuery(char_filterR, IntegerParameter, Integer.valueOf('B'), 'B', 1);
        run_charQuery(char_filterL, longParameter, Long.valueOf('z'), 'z', 2);
        run_charQuery(char_filterR, longParameter, Long.valueOf('z'), 'z', 2);
        run_charQuery(char_filterL, LongParameter, Long.valueOf('B'), 'B', 1);
        run_charQuery(char_filterR, LongParameter, Long.valueOf('B'), 'B', 1);
        run_charQuery(char_filterL, floatParameter, Float.valueOf((float)123.222), 'x', 0);
        run_charQuery(char_filterR, floatParameter, Float.valueOf((float)123.222), 'x', 0);
        run_charQuery(char_filterL, FloatParameter, Float.valueOf((float)'z'), 'z', 2);
        run_charQuery(char_filterR, FloatParameter, Float.valueOf((float)'z'), 'z', 2);
        run_charQuery(char_filterL, doubleParameter, Double.valueOf('B'), 'B', 1);
        run_charQuery(char_filterR, doubleParameter, Double.valueOf('B'), 'B', 1);
        run_charQuery(char_filterL, DoubleParameter, Double.valueOf('A'), 'A', 1);
        run_charQuery(char_filterR, DoubleParameter, Double.valueOf('A'), 'A', 1);
        run_charQuery(char_filterL, BigIntegerParameter, new BigInteger("65"), 'A', 1);   // 'A' == 65
        run_charQuery(char_filterR, BigIntegerParameter, new BigInteger("122"), 'z', 2);  // 'z' == 122
        run_charQuery(char_filterL, BigDecimalParameter, new BigDecimal("65.0"), 'A', 1);
        run_charQuery(char_filterR, BigDecimalParameter, new BigDecimal("77.0"), 'M', 2); // 'M' == 77
        alltypes.setchar('A');
        run_charQuery(char_filterObj, AllTypesParameter, alltypes, 'A', 1);
        alltypes.setchar('b');
        run_charQuery(char_filterObj, AllTypesParameter, alltypes, 'b', 0);
        run_charQuery(char_filterVal, null, null, 'M', 2);
    
        run_intQuery(int_filterL, intParameter, Integer.valueOf(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, 1);
        run_intQuery(int_filterR, intParameter, Integer.valueOf(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, 1);
        run_intQuery(int_filterR, intParameter, Integer.valueOf(23), 23, 0);
        run_intQuery(int_filterL, IntegerParameter, Integer.valueOf(1000000), 1000000, 1);
        run_intQuery(int_filterR, IntegerParameter, Integer.valueOf(1000), 1000, 1);
        run_intQuery(int_filterL, byteParameter, Byte.valueOf((byte)100), 100, 2);
        run_intQuery(int_filterR, byteParameter, Byte.valueOf((byte)0), 0, 1);
        run_intQuery(int_filterL, ByteParameter, Byte.valueOf((byte)100), 100, 2);
        run_intQuery(int_filterR, ByteParameter, Byte.valueOf((byte)0), 0, 1);
        run_intQuery(int_filterL, shortParameter, Short.valueOf((short)10000), 10000, 1);
        run_intQuery(int_filterR, shortParameter, Short.valueOf((short)-1000), -1000, 1);
        run_intQuery(int_filterL, ShortParameter, Short.valueOf((short)-1000), -1000, 1);
        run_intQuery(int_filterR, ShortParameter, Short.valueOf((short)10000), 10000, 1);
        run_intQuery(int_filterL, charParameter, Character.valueOf((char)10000), 10000, 1);
        run_intQuery(int_filterR, charParameter, Character.valueOf((char)0), 0, 1);
        run_intQuery(int_filterL, CharacterParameter, Character.valueOf((char)100), 100, 2);
        run_intQuery(int_filterR, CharacterParameter, Character.valueOf((char)10000), 10000, 1);
        run_intQuery(int_filterL, longParameter, Long.valueOf(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, 1);
        run_intQuery(int_filterR, longParameter, Long.valueOf(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, 1);
        run_intQuery(int_filterL, LongParameter, Long.valueOf(10000), 10000, 1);
        run_intQuery(int_filterR, LongParameter, Long.valueOf(43), 43, 0);
        run_intQuery(int_filterL, floatParameter, Float.valueOf((float)AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, 1);
        run_intQuery(int_filterR, floatParameter, Float.valueOf((float)AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, 1);
        run_intQuery(int_filterL, FloatParameter, Float.valueOf((float)10000), 10000, 1);
        run_intQuery(int_filterR, FloatParameter, Float.valueOf((float)43), 43, 0);
        run_intQuery(int_filterL, doubleParameter, Double.valueOf(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, 1);
        run_intQuery(int_filterR, doubleParameter, Double.valueOf(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, 1);
        run_intQuery(int_filterL, DoubleParameter, Double.valueOf(10000), 10000, 1);
        run_intQuery(int_filterR, DoubleParameter, Double.valueOf(43), 43, 0);
        run_intQuery(int_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, 1);
        run_intQuery(int_filterR, BigIntegerParameter, new BigInteger("-1000000"), -1000000, 1);
        run_intQuery(int_filterL, BigDecimalParameter, new BigDecimal("1000000.0"), 1000000, 1);
        run_intQuery(int_filterR, BigDecimalParameter, new BigDecimal("-1000000.00001"), -1000000, 0);
        alltypes.setint(100);
        run_intQuery(int_filterObj, AllTypesParameter, alltypes, 100, 2);
        run_intQuery(int_filterVal, null, null, 1000, 1);
    
        run_longQuery(long_filterL, longParameter, Long.valueOf(Long.MIN_VALUE), Long.MIN_VALUE, 1);
        run_longQuery(long_filterR, longParameter, Long.valueOf(Long.MAX_VALUE), Long.MAX_VALUE, 1);
        run_longQuery(long_filterL, LongParameter, Long.valueOf(100), 100, 2);
        run_longQuery(long_filterR, LongParameter, Long.valueOf(23), 23, 0);
        run_longQuery(long_filterL, byteParameter, Byte.valueOf((byte)100), 100, 2);
        run_longQuery(long_filterR, byteParameter, Byte.valueOf((byte)0), 0, 1);
        run_longQuery(long_filterL, ByteParameter, Byte.valueOf((byte)100), 100, 2);
        run_longQuery(long_filterR, ByteParameter, Byte.valueOf((byte)0), 0, 1);
        run_longQuery(long_filterL, shortParameter, Short.valueOf((short)-1000 ), -1000, 2);
        run_longQuery(long_filterR, shortParameter, Short.valueOf((short)1000), 1000, 1);
        run_longQuery(long_filterL, ShortParameter, Short.valueOf((short)100), 100, 2);
        run_longQuery(long_filterR, ShortParameter, Short.valueOf((short)32), 32, 0);
        run_longQuery(long_filterL, charParameter, Character.valueOf((char)0), 0, 1);
        run_longQuery(long_filterR, charParameter, Character.valueOf((char)100), 100, 2);
        run_longQuery(long_filterL, CharacterParameter, Character.valueOf((char)23), 23, 0);
        run_longQuery(long_filterR, CharacterParameter, Character.valueOf((char)0), 0, 1);
        run_longQuery(long_filterL, intParameter, Integer.valueOf(100), 100, 2);
        run_longQuery(long_filterR, intParameter, Integer.valueOf(0), 0, 1);
        run_longQuery(long_filterL, IntegerParameter, Integer.valueOf(23), 23, 0);
        run_longQuery(long_filterR, IntegerParameter, Integer.valueOf(1000000), 1000000, 1);
        run_longQuery(long_filterL, floatParameter, Float.valueOf((float)-1000000.0), -1000000, 1);
//    run_longQuery(long_filterR, floatParameter, Float.valueOf((float)Long.MAX_VALUE), Long.MAX_VALUE, 1);
        run_longQuery(long_filterL, FloatParameter, Float.valueOf((float)100), 100, 2);
        run_longQuery(long_filterR, FloatParameter, Float.valueOf((float)32), 32, 0);
        run_longQuery(long_filterL, doubleParameter, Double.valueOf(-1000000.0), -1000000, 1);
//    run_longQuery(long_filterR, doubleParameter, Double.valueOf((double)Long.MAX_VALUE), Long.MAX_VALUE, 1);
        run_longQuery(long_filterL, DoubleParameter, Double.valueOf(100.0), 100, 2);
        run_longQuery(long_filterR, DoubleParameter, Double.valueOf(32.0), 32, 0);
        run_longQuery(long_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, 1);
        run_longQuery(long_filterR, BigIntegerParameter, new BigInteger("-1000000"), -1000000, 1);
        run_longQuery(long_filterL, BigDecimalParameter, new BigDecimal("100.0"), 100, 2);
        run_longQuery(long_filterR, BigDecimalParameter, new BigDecimal("-1000000.0001"), -1000000, 0);
        alltypes.setlong(100);
        run_longQuery(long_filterObj, AllTypesParameter, alltypes, 100, 2);
        run_longQuery(long_filterVal, null, null, 1000000, 1);
    
        run_floatQuery(float_filterL, floatParameter, Float.valueOf(-234.23f), -234.23f, 1);
        run_floatQuery(float_filterR, floatParameter, Float.valueOf(-234.23f), -234.23f, 1);
        run_floatQuery(float_filterL, FloatParameter, Float.valueOf((float)0.0), 0.0f, 1);
        run_floatQuery(float_filterR, FloatParameter, Float.valueOf((float)4.0), 4.0f, 0);
        run_floatQuery(float_filterL, byteParameter, Byte.valueOf((byte)0), 0.0f, 1);
        run_floatQuery(float_filterR, byteParameter, Byte.valueOf((byte)23), 23.0f, 0);
        run_floatQuery(float_filterL, ByteParameter, Byte.valueOf((byte)34), 34.0f, 0);
        run_floatQuery(float_filterR, ByteParameter, Byte.valueOf((byte)100), 100.0f, 2);
        run_floatQuery(float_filterL, shortParameter, Short.valueOf((short)0), 0.0f, 1);
        run_floatQuery(float_filterR, shortParameter, Short.valueOf((short)23), 23.0f, 0);
        run_floatQuery(float_filterL, ShortParameter, Short.valueOf((short)34), 34.0f, 0);
        run_floatQuery(float_filterR, ShortParameter, Short.valueOf((short)100), 100.0f, 2);
        run_floatQuery(float_filterL, charParameter, Character.valueOf((char)0), 0.0f, 1);
        run_floatQuery(float_filterR, charParameter, Character.valueOf((char)23), 23.0f, 0);
        run_floatQuery(float_filterL, CharacterParameter, Character.valueOf((char)34), 34.0f, 0);
        run_floatQuery(float_filterR, CharacterParameter, Character.valueOf((char)100), 100.0f, 2);
        run_floatQuery(float_filterL, intParameter, Integer.valueOf(50000000), 50000000.0f, 1);
        run_floatQuery(float_filterR, intParameter, Integer.valueOf(23), 23.0f, 0);
        run_floatQuery(float_filterL, IntegerParameter, Integer.valueOf(34), 34.0f, 0);
        run_floatQuery(float_filterR, IntegerParameter, Integer.valueOf(100), 100.0f, 2);
        run_floatQuery(float_filterL, longParameter, Long.valueOf(50000000), 50000000.0f, 1);
        run_floatQuery(float_filterR, longParameter, Long.valueOf(23), 23.0f, 0);
        run_floatQuery(float_filterL, LongParameter, Long.valueOf(34), 34.0f, 0);
        run_floatQuery(float_filterR, LongParameter, Long.valueOf(100), 100.0f, 2);
        run_floatQuery(float_filterL, doubleParameter, Double.valueOf(50000000.0), 50000000.0f, 1);
        run_floatQuery(float_filterR, doubleParameter, Double.valueOf(-25.5), -25.5f, 1);
        run_floatQuery(float_filterL, DoubleParameter, Double.valueOf(0.0), 0.0f, 1);
        run_floatQuery(float_filterR, DoubleParameter, Double.valueOf(100.0), 100.0f, 2);
        run_floatQuery(float_filterL, BigIntegerParameter, new BigInteger("0"), 0.0f, 1);
        run_floatQuery(float_filterR, BigIntegerParameter, new BigInteger("1000000000"), 1000000000.0f, 1);
        run_floatQuery(float_filterL, BigDecimalParameter, new BigDecimal("350.5"), 350.5f, 1);
        run_floatQuery(float_filterR, BigDecimalParameter, new BigDecimal("50000000.0"), 50000000.0f, 1);
        alltypes.setfloat(23.23f);
        run_floatQuery(float_filterObj, AllTypesParameter, alltypes, 23.23f, 0);
        run_floatQuery(float_filterVal, null, null, 100.0f, 2);
    
        run_doubleQuery(double_filterL, doubleParameter, Double.valueOf(-234234.234), -234234.234, 1);
        run_doubleQuery(double_filterR, doubleParameter, Double.valueOf(-234234.234), -234234.234, 1);
        run_doubleQuery(double_filterL, DoubleParameter, Double.valueOf(0.0), 0.0, 1);
        run_doubleQuery(double_filterR, DoubleParameter, Double.valueOf(23.34), 23.34, 0);
        run_doubleQuery(double_filterL, byteParameter, Byte.valueOf((byte)100), 100.0, 2);
        run_doubleQuery(double_filterR, byteParameter, Byte.valueOf((byte)0), 0.0, 1);
        run_doubleQuery(double_filterL, ByteParameter, Byte.valueOf((byte)23), 23.0, 0);
        run_doubleQuery(double_filterR, ByteParameter, Byte.valueOf((byte)100), 100.0, 2);
        run_doubleQuery(double_filterL, shortParameter, Short.valueOf((short)100), 100.0, 2);
        run_doubleQuery(double_filterR, shortParameter, Short.valueOf((short)0), 0.0, 1);
        run_doubleQuery(double_filterL, ShortParameter, Short.valueOf((short)23), 23.0, 0);
        run_doubleQuery(double_filterR, ShortParameter, Short.valueOf((short)100), 100.0, 2);
        run_doubleQuery(double_filterL, intParameter, Integer.valueOf(100), 100.0, 2);
        run_doubleQuery(double_filterR, intParameter, Integer.valueOf(0), 0.0, 1);
        run_doubleQuery(double_filterL, IntegerParameter, Integer.valueOf(23), 23.0, 0);
        run_doubleQuery(double_filterR, IntegerParameter, Integer.valueOf(100), 100.0, 2);
        run_doubleQuery(double_filterL, longParameter, Long.valueOf(100), 100.0, 2);
        run_doubleQuery(double_filterR, longParameter, Long.valueOf(0), 0.0, 1);
        run_doubleQuery(double_filterL, LongParameter, Long.valueOf(23), 23.0, 0);
        run_doubleQuery(double_filterR, LongParameter, Long.valueOf(100), 100.0, 2);
        run_doubleQuery(double_filterL, floatParameter, Float.valueOf(0.0f),0.0f, 1);
        run_doubleQuery(double_filterR, floatParameter, Float.valueOf(100.0f), 100.0f, 2);
        run_doubleQuery(double_filterL, FloatParameter, Float.valueOf(100.0f), 100.0, 2);
        run_doubleQuery(double_filterR, FloatParameter, Float.valueOf(69.96f), 69.96, 0);
        run_doubleQuery(double_filterL, BigIntegerParameter, new BigInteger("50000000"), 50000000.0f, 1);
        run_doubleQuery(double_filterR, BigIntegerParameter, new BigInteger("1000000000"), 1000000000.0f, 1);
        run_doubleQuery(double_filterL, BigDecimalParameter, new BigDecimal("350.5"), 350.5f, 1);
        run_doubleQuery(double_filterR, BigDecimalParameter, new BigDecimal("50000000.0"), 50000000.0f, 1);
        alltypes.setdouble(-25.5);
        run_doubleQuery(double_filterObj, AllTypesParameter, alltypes, -25.5, 1);
        run_doubleQuery(double_filterVal, null, null, 100.0, 2);
    
        run_BooleanQuery(Boolean_filterL, booleanParameter, Boolean.TRUE, Boolean.TRUE, 4);
        run_BooleanQuery(Boolean_filterL, booleanParameter, Boolean.FALSE, Boolean.FALSE, 6);
        run_BooleanQuery(Boolean_filterR, booleanParameter, Boolean.TRUE, Boolean.TRUE, 4);
        run_BooleanQuery(Boolean_filterR, booleanParameter, Boolean.FALSE, Boolean.FALSE, 6);
        run_BooleanQuery(Boolean_filterL, BooleanParameter, Boolean.TRUE, Boolean.TRUE, 4);
        run_BooleanQuery(Boolean_filterL, BooleanParameter, Boolean.FALSE, Boolean.FALSE, 6);
        run_BooleanQuery(Boolean_filterR, BooleanParameter, Boolean.TRUE, Boolean.TRUE, 4);
        run_BooleanQuery(Boolean_filterR, BooleanParameter, Boolean.FALSE, Boolean.FALSE, 6);
        run_BooleanQuery(Boolean_filterT, null, Boolean.TRUE, Boolean.TRUE, 4);
        run_BooleanQuery(Boolean_filterF, null, Boolean.FALSE, Boolean.FALSE, 6);
        alltypes.setBoolean(Boolean.TRUE);
        run_BooleanQuery(Boolean_filterObj, AllTypesParameter, alltypes, Boolean.TRUE, 4);
        alltypes.setBoolean(Boolean.FALSE);
        run_BooleanQuery(Boolean_filterObj, AllTypesParameter, alltypes, Boolean.FALSE, 6);

        run_ByteQuery(Byte_filterL, byteParameter, Byte.valueOf((byte)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, byteParameter, Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MIN_VALUE), 1);
        run_ByteQuery(Byte_filterL, ByteParameter, Byte.valueOf((byte)20), Byte.valueOf((byte)20), 0);
        run_ByteQuery(Byte_filterR, ByteParameter, Byte.valueOf(Byte.MAX_VALUE), Byte.valueOf(Byte.MAX_VALUE), 1);    
        run_ByteQuery(Byte_filterL, shortParameter, Short.valueOf((short)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, shortParameter, Short.valueOf((short)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, ShortParameter, Short.valueOf((short)-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterR, ShortParameter, Short.valueOf((short)-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterL, charParameter, Character.valueOf((char)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, charParameter, Character.valueOf((char)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, CharacterParameter, Character.valueOf((char)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, CharacterParameter, Character.valueOf((char)75), Byte.valueOf((byte)75), 1);
        run_ByteQuery(Byte_filterL, intParameter, Integer.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, intParameter, Integer.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, IntegerParameter, Integer.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, IntegerParameter, Integer.valueOf(75), Byte.valueOf((byte)75), 1);
        run_ByteQuery(Byte_filterL, longParameter, Long.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, longParameter, Long.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, LongParameter, Long.valueOf(-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterR, LongParameter, Long.valueOf(-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterL, floatParameter, Float.valueOf((float)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, floatParameter, Float.valueOf((float)50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, FloatParameter, Float.valueOf((float)-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterR, FloatParameter, Float.valueOf((float)-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterL, doubleParameter, Double.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, doubleParameter, Double.valueOf(50), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterL, DoubleParameter, Double.valueOf(-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterR, DoubleParameter, Double.valueOf(-100), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterL, BigIntegerParameter, new BigInteger("50"), Byte.valueOf((byte)50), 2);
        run_ByteQuery(Byte_filterR, BigIntegerParameter, new BigInteger("-100"), Byte.valueOf((byte)-100), 1);
        run_ByteQuery(Byte_filterL, BigDecimalParameter, new BigDecimal("100.0"), Byte.valueOf((byte)100), 1);
        run_ByteQuery(Byte_filterR, BigDecimalParameter, new BigDecimal("10.0001"), Byte.valueOf((byte)10), 0);    
        Byte val = Byte.valueOf((byte)50);
        alltypes.setByte(val);
        run_ByteQuery(Byte_filterObj, AllTypesParameter, alltypes, val, 2);
        val = Byte.valueOf((byte)45);
        alltypes.setByte(val);
        run_ByteQuery(Byte_filterObj, AllTypesParameter, alltypes, val, 0);
        run_ByteQuery(Byte_filterVal, null, null, Byte.valueOf((byte)100), 1);
    
        run_ShortQuery(Short_filterL, shortParameter, Short.valueOf((short)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, shortParameter, Short.valueOf((short)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterL, ShortParameter, Short.valueOf(Short.MIN_VALUE), Short.valueOf(Short.MIN_VALUE), 1);
        run_ShortQuery(Short_filterR, ShortParameter, Short.valueOf((short)253), Short.valueOf((short)253), 0);
        run_ShortQuery(Short_filterR, shortParameter, Short.valueOf((short)1000), Short.valueOf((short)1000), 1);
        run_ShortQuery(Short_filterL, byteParameter, Byte.valueOf((byte)75), Short.valueOf((short)75), 0);
        run_ShortQuery(Short_filterR, byteParameter, Byte.valueOf((byte)75), Short.valueOf((short)75), 0);
        run_ShortQuery(Short_filterL, ByteParameter, Byte.valueOf((byte)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, ByteParameter, Byte.valueOf((byte)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterL, charParameter, Character.valueOf((char)75), Short.valueOf((short)75), 0);
        run_ShortQuery(Short_filterR, charParameter, Character.valueOf((char)75), Short.valueOf((short)75), 0);
        run_ShortQuery(Short_filterL, CharacterParameter, Character.valueOf((char)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, CharacterParameter, Character.valueOf((char)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterL, intParameter, Integer.valueOf(-10000), Short.valueOf((short)-10000), 1);
        run_ShortQuery(Short_filterR, intParameter, Integer.valueOf(-10000), Short.valueOf((short)-10000), 1);
        run_ShortQuery(Short_filterL, IntegerParameter, Integer.valueOf(10000), Short.valueOf((short)10000), 1);
        run_ShortQuery(Short_filterR, IntegerParameter, Integer.valueOf(10000), Short.valueOf((short)10000), 1);
        run_ShortQuery(Short_filterL, longParameter, Long.valueOf(10000), Short.valueOf((short) 10000), 1);
        run_ShortQuery(Short_filterR, longParameter, Long.valueOf(10000), Short.valueOf((short) 10000), 1);
        run_ShortQuery(Short_filterL, LongParameter, Long.valueOf(100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, LongParameter, Long.valueOf(100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterL, floatParameter, Float.valueOf((float)23000), Short.valueOf((short)23000), 0);
        run_ShortQuery(Short_filterR, floatParameter, Float.valueOf((float)23000), Short.valueOf((short)23000), 0);
        run_ShortQuery(Short_filterL, FloatParameter, Float.valueOf((float)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, FloatParameter, Float.valueOf((float)100), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterL, doubleParameter, Double.valueOf(-10000), Short.valueOf((short)-10000), 1);
        run_ShortQuery(Short_filterR, doubleParameter, Double.valueOf(-10000), Short.valueOf((short)-10000), 1);
        run_ShortQuery(Short_filterL, DoubleParameter, Double.valueOf(23), Short.valueOf((short)23), 0);
        run_ShortQuery(Short_filterR, DoubleParameter, Double.valueOf(23), Short.valueOf((short)23), 0);
        run_ShortQuery(Short_filterL, BigIntegerParameter, new BigInteger("999"), Short.valueOf((short)999), 0);
        run_ShortQuery(Short_filterR, BigIntegerParameter, new BigInteger("-1000"), Short.valueOf((short)-1000), 1);
        run_ShortQuery(Short_filterL, BigDecimalParameter, new BigDecimal("100.0"), Short.valueOf((short)100), 2);
        run_ShortQuery(Short_filterR, BigDecimalParameter, new BigDecimal("101.0"), Short.valueOf((short)100), 0);    
        Short sval = Short.valueOf((short)100);
        alltypes.setShort(sval);
        run_ShortQuery(Short_filterObj, AllTypesParameter, alltypes, sval, 2);
        sval = Short.valueOf((short)23);
        alltypes.setShort(sval);
        run_ShortQuery(Short_filterObj, AllTypesParameter, alltypes, sval, 0);
        run_ShortQuery(Short_filterVal, null, null, Short.valueOf((short)-1000), 1);
    
        run_CharacterQuery(Character_filterL, charParameter, Character.valueOf(Character.MIN_VALUE), Character.valueOf(Character.MIN_VALUE), 1);
        run_CharacterQuery(Character_filterR, charParameter, Character.valueOf(Character.MAX_VALUE), Character.valueOf(Character.MAX_VALUE), 1);
        run_CharacterQuery(Character_filterL, charParameter, Character.valueOf('C'), Character.valueOf('C'), 0);
        run_CharacterQuery(Character_filterR, charParameter, Character.valueOf('z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterL, CharacterParameter, Character.valueOf(' '), Character.valueOf(' '), 1);
        run_CharacterQuery(Character_filterR, CharacterParameter, Character.valueOf('f'), Character.valueOf('f'), 0);
        run_CharacterQuery(Character_filterL, byteParameter, Byte.valueOf((byte)Character.MIN_VALUE), Character.valueOf((char)Character.MIN_VALUE), 1);
        run_CharacterQuery(Character_filterR, ByteParameter, Byte.valueOf((byte)'a'), Character.valueOf('a'), 0);
        run_CharacterQuery(Character_filterL, shortParameter, Short.valueOf((short)'M'), Character.valueOf('M'), 2);
        run_CharacterQuery(Character_filterR, shortParameter, Short.valueOf((short)'F'), Character.valueOf('F'), 1);
        run_CharacterQuery(Character_filterL, ShortParameter, Short.valueOf((short)'A'), Character.valueOf('A'), 1);
        run_CharacterQuery(Character_filterR, ShortParameter, Short.valueOf((short)'A'), Character.valueOf('A'), 1);
        run_CharacterQuery(Character_filterL, intParameter, Integer.valueOf('z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterR, intParameter, Integer.valueOf('z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterL, IntegerParameter, Integer.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterR, IntegerParameter, Integer.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterL, longParameter, Long.valueOf('z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterR, longParameter, Long.valueOf('z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterL, LongParameter, Long.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterR, LongParameter, Long.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterL, floatParameter, Float.valueOf((float)123.222), Character.valueOf('x'), 0);
        run_CharacterQuery(Character_filterR, floatParameter, Float.valueOf((float)123.222), Character.valueOf('x'), 0);
        run_CharacterQuery(Character_filterL, FloatParameter, Float.valueOf((float)'z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterR, FloatParameter, Float.valueOf((float)'z'), Character.valueOf('z'), 2);
        run_CharacterQuery(Character_filterL, doubleParameter, Double.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterR, doubleParameter, Double.valueOf('B'), Character.valueOf('B'), 1);
        run_CharacterQuery(Character_filterL, DoubleParameter, Double.valueOf('A'), Character.valueOf('A'), 1);
        run_CharacterQuery(Character_filterR, DoubleParameter, Double.valueOf('A'), Character.valueOf('A'), 1);
        run_CharacterQuery(Character_filterL, BigIntegerParameter, new BigInteger("65"), Character.valueOf('A'), 1);   // 'A' == 65
        run_CharacterQuery(Character_filterR, BigIntegerParameter, new BigInteger("122"), Character.valueOf('z'), 2);  // 'z' == 122
        run_CharacterQuery(Character_filterL, BigDecimalParameter, new BigDecimal("65.0"), Character.valueOf('A'), 1);
        run_CharacterQuery(Character_filterR, BigDecimalParameter, new BigDecimal("77.0"), Character.valueOf('M'), 2); // 'M' == 77
        alltypes.setCharacter(Character.valueOf('A'));
        run_CharacterQuery(Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('A'), 1);
        alltypes.setCharacter(Character.valueOf('b'));
        run_CharacterQuery(Character_filterObj, AllTypesParameter, alltypes, Character.valueOf('b'), 0);
        run_CharacterQuery(Character_filterVal, null, null, Character.valueOf('z'), 2);
    
        run_IntegerQuery(Integer_filterL, intParameter, Integer.valueOf(AllTypes.veryLargeNegativeInt), Integer.valueOf(AllTypes.veryLargeNegativeInt), 1);
        run_IntegerQuery(Integer_filterR, intParameter, Integer.valueOf(AllTypes.veryLargePositiveInt), Integer.valueOf(AllTypes.veryLargePositiveInt), 1);
        run_IntegerQuery(Integer_filterR, intParameter, Integer.valueOf(23), Integer.valueOf(23), 0);
        run_IntegerQuery(Integer_filterL, IntegerParameter, Integer.valueOf(1000000), Integer.valueOf(1000000), 1);
        run_IntegerQuery(Integer_filterR, IntegerParameter, Integer.valueOf(1000), Integer.valueOf(1000), 1);
        run_IntegerQuery(Integer_filterL, byteParameter, Byte.valueOf((byte)100), Integer.valueOf(100), 2);
        run_IntegerQuery(Integer_filterR, byteParameter, Byte.valueOf((byte)0), Integer.valueOf(0), 1);
        run_IntegerQuery(Integer_filterL, ByteParameter, Byte.valueOf((byte)100), Integer.valueOf(100), 2);
        run_IntegerQuery(Integer_filterR, ByteParameter, Byte.valueOf((byte)0), Integer.valueOf(0), 1);
        run_IntegerQuery(Integer_filterL, shortParameter, Short.valueOf((short)10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterR, shortParameter, Short.valueOf((short)-1000), Integer.valueOf(-1000), 1);
        run_IntegerQuery(Integer_filterL, ShortParameter, Short.valueOf((short)-1000), Integer.valueOf(-1000), 1);
        run_IntegerQuery(Integer_filterL, charParameter, Character.valueOf((char)10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterR, charParameter, Character.valueOf((char)10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterL, CharacterParameter, Character.valueOf((char)100), Integer.valueOf(100), 2);
        run_IntegerQuery(Integer_filterR, CharacterParameter, Character.valueOf((char)10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterL, longParameter, Long.valueOf(AllTypes.veryLargePositiveInt), Integer.valueOf(AllTypes.veryLargePositiveInt), 1);
        run_IntegerQuery(Integer_filterR, longParameter, Long.valueOf(AllTypes.veryLargeNegativeInt), Integer.valueOf(AllTypes.veryLargeNegativeInt), 1);
        run_IntegerQuery(Integer_filterL, LongParameter, Long.valueOf(10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterR, LongParameter, Long.valueOf(43), Integer.valueOf(43), 0);
        run_IntegerQuery(Integer_filterL, floatParameter, Float.valueOf((float)AllTypes.veryLargePositiveInt), Integer.valueOf(AllTypes.veryLargePositiveInt), 1);
        run_IntegerQuery(Integer_filterR, floatParameter, Float.valueOf((float)AllTypes.veryLargeNegativeInt), Integer.valueOf(AllTypes.veryLargeNegativeInt), 1);
        run_IntegerQuery(Integer_filterL, FloatParameter, Float.valueOf((float)10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterR, FloatParameter, Float.valueOf((float)43), Integer.valueOf(43), 0);
        run_IntegerQuery(Integer_filterL, doubleParameter, Double.valueOf(AllTypes.veryLargePositiveInt), Integer.valueOf(AllTypes.veryLargePositiveInt), 1);
        run_IntegerQuery(Integer_filterR, doubleParameter, Double.valueOf(AllTypes.veryLargeNegativeInt), Integer.valueOf(AllTypes.veryLargeNegativeInt), 1);
        run_IntegerQuery(Integer_filterL, DoubleParameter, Double.valueOf(10000), Integer.valueOf(10000), 1);
        run_IntegerQuery(Integer_filterR, DoubleParameter, Double.valueOf(43), Integer.valueOf(43), 0);
        run_IntegerQuery(Integer_filterL, BigIntegerParameter, new BigInteger("1000000"), Integer.valueOf(1000000), 1);
        run_IntegerQuery(Integer_filterR, BigIntegerParameter, new BigInteger("-1000000"), Integer.valueOf(-1000000), 1);
        run_IntegerQuery(Integer_filterL, BigDecimalParameter, new BigDecimal("1000000.0"), Integer.valueOf(1000000), 1);
        run_IntegerQuery(Integer_filterR, BigDecimalParameter, new BigDecimal("-1000000.01"), Integer.valueOf(-1000000), 0);
        alltypes.setInteger(Integer.valueOf(100));
        run_IntegerQuery(Integer_filterObj, AllTypesParameter, alltypes, Integer.valueOf(100), 2);
        run_IntegerQuery(Integer_filterVal, null, null, Integer.valueOf(100), 2);
    
        run_LongQuery(Long_filterL, longParameter, Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MIN_VALUE), 1);
        run_LongQuery(Long_filterR, longParameter, Long.valueOf(Long.MAX_VALUE), Long.valueOf(Long.MAX_VALUE), 1);
        run_LongQuery(Long_filterL, LongParameter, Long.valueOf(100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, LongParameter, Long.valueOf(23), Long.valueOf(23), 0);
        run_LongQuery(Long_filterL, byteParameter, Byte.valueOf((byte)100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, byteParameter, Byte.valueOf((byte)0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterL, ByteParameter, Byte.valueOf((byte)100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, ByteParameter, Byte.valueOf((byte)0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterL, shortParameter, Short.valueOf((short)-1000 ), Long.valueOf(-1000), 2);
        run_LongQuery(Long_filterR, shortParameter, Short.valueOf((short)1000), Long.valueOf(1000), 1);
        run_LongQuery(Long_filterL, ShortParameter, Short.valueOf((short)100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, ShortParameter, Short.valueOf((short)32), Long.valueOf(32), 0);
        run_LongQuery(Long_filterL, charParameter, Character.valueOf((char)0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterR, charParameter, Character.valueOf((char)100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterL, CharacterParameter, Character.valueOf((char)23), Long.valueOf(23), 0);
        run_LongQuery(Long_filterR, CharacterParameter, Character.valueOf((char)0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterL, intParameter, Integer.valueOf(100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, intParameter, Integer.valueOf(0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterL, IntegerParameter, Integer.valueOf(23), Long.valueOf(23), 0);
        run_LongQuery(Long_filterR, IntegerParameter, Integer.valueOf(1000000), Long.valueOf(1000000), 1);
        run_LongQuery(Long_filterL, longParameter, Long.valueOf(100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, longParameter, Long.valueOf(0), Long.valueOf(0), 1);
        run_LongQuery(Long_filterL, LongParameter, Long.valueOf(23), Long.valueOf(23), 0);
        run_LongQuery(Long_filterR, LongParameter, Long.valueOf(1000000), Long.valueOf(1000000), 1);
        run_LongQuery(Long_filterL, floatParameter, Float.valueOf((float)-1000000.0), Long.valueOf(-1000000), 1);
//    run_LongQuery(Long_filterR, floatParameter, Float.valueOf((float)Long.MAX_VALUE), Long.valueOf(Long.MAX_VALUE), 1);
        run_LongQuery(Long_filterL, FloatParameter, Float.valueOf((float)100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, FloatParameter, Float.valueOf((float)32), Long.valueOf(32), 0);
        run_LongQuery(Long_filterL, doubleParameter, Double.valueOf(-1000000.0), Long.valueOf(-1000000), 1);
//    run_LongQuery(Long_filterR, doubleParameter, Double.valueOf((double)Long.MAX_VALUE), Long.valueOf(Long.MAX_VALUE), 1);
        run_LongQuery(Long_filterL, DoubleParameter, Double.valueOf(100), Long.valueOf(100), 2);
        run_LongQuery(Long_filterR, DoubleParameter, Double.valueOf(32), Long.valueOf(32), 0);
        run_LongQuery(Long_filterL, BigIntegerParameter, new BigInteger("1000000"), Long.valueOf(1000000), 1);
        run_LongQuery(Long_filterR, BigIntegerParameter, new BigInteger("-1000000"), Long.valueOf(-1000000), 1);
        run_LongQuery(Long_filterL, BigDecimalParameter, new BigDecimal("1000000.0"), Long.valueOf(1000000), 1);
        run_LongQuery(Long_filterR, BigDecimalParameter, new BigDecimal("-1000000.0001"), Long.valueOf(-1000000), 0);
        alltypes.setLong(Long.valueOf(100));
        run_LongQuery(Long_filterObj, AllTypesParameter, alltypes, Long.valueOf(100), 2);
        run_LongQuery(Long_filterVal, null, null, Long.valueOf(-1000), 2);
    
        run_FloatQuery(Float_filterL, floatParameter, Float.valueOf(350.5f), Float.valueOf(350.5f), 1);
        run_FloatQuery(Float_filterR, floatParameter, Float.valueOf(350.5f), Float.valueOf(350.5f), 1);
        run_FloatQuery(Float_filterL, FloatParameter, Float.valueOf((float)0.0), Float.valueOf(0.0f), 1);
        run_FloatQuery(Float_filterR, FloatParameter, Float.valueOf((float)4.0), Float.valueOf(4.0f), 0);
        run_FloatQuery(Float_filterL, byteParameter, Byte.valueOf((byte)0), Float.valueOf(0.0f), 1);
        run_FloatQuery(Float_filterR, byteParameter, Byte.valueOf((byte)23), Float.valueOf(23.0f), 0);
        run_FloatQuery(Float_filterL, ByteParameter, Byte.valueOf((byte)34), Float.valueOf(34.0f), 0);
        run_FloatQuery(Float_filterR, ByteParameter, Byte.valueOf((byte)100), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, shortParameter, Short.valueOf((short)0), Float.valueOf(0.0f), 1);
        run_FloatQuery(Float_filterR, shortParameter, Short.valueOf((short)23), Float.valueOf(23.0f), 0);
        run_FloatQuery(Float_filterL, ShortParameter, Short.valueOf((short)34), Float.valueOf(34.0f), 0);
        run_FloatQuery(Float_filterR, ShortParameter, Short.valueOf((short)100), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, charParameter, Character.valueOf((char)0), Float.valueOf(0.0f), 1);
        run_FloatQuery(Float_filterR, charParameter, Character.valueOf((char)23), Float.valueOf(23.0f), 0);
        run_FloatQuery(Float_filterL, CharacterParameter, Character.valueOf((char)34), Float.valueOf(34.0f), 0);
        run_FloatQuery(Float_filterR, CharacterParameter, Character.valueOf((char)100), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, intParameter, Integer.valueOf(50000000), Float.valueOf(50000000.0f), 1);
        run_FloatQuery(Float_filterR, intParameter, Integer.valueOf(23), Float.valueOf(23.0f), 0);
        run_FloatQuery(Float_filterL, IntegerParameter, Integer.valueOf(34), Float.valueOf(34.0f), 0);
        run_FloatQuery(Float_filterR, IntegerParameter, Integer.valueOf(100), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, longParameter, Long.valueOf(50000000), Float.valueOf(50000000.0f), 1);
        run_FloatQuery(Float_filterR, longParameter, Long.valueOf(23), Float.valueOf(23.0f), 0);
        run_FloatQuery(Float_filterL, LongParameter, Long.valueOf(34), Float.valueOf(34.0f), 0);
        run_FloatQuery(Float_filterR, LongParameter, Long.valueOf(100), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, doubleParameter, Double.valueOf(50000000.0f), Float.valueOf(50000000.0f), 1);
        run_FloatQuery(Float_filterR, doubleParameter, Double.valueOf(100.0f), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, DoubleParameter, Double.valueOf(0.0f), Float.valueOf(0.0f), 1);
        run_FloatQuery(Float_filterR, DoubleParameter, Double.valueOf(100.0f), Float.valueOf(100.0f), 2);
        run_FloatQuery(Float_filterL, BigIntegerParameter, new BigInteger("50000000"), Float.valueOf(50000000.0f), 1);
        run_FloatQuery(Float_filterR, BigIntegerParameter, new BigInteger("1000000000"), Float.valueOf(1000000000.0f), 1);
        run_FloatQuery(Float_filterL, BigDecimalParameter, new BigDecimal("350.5"), Float.valueOf(350.5f), 1);
        run_FloatQuery(Float_filterR, BigDecimalParameter, new BigDecimal("50000000.0"), Float.valueOf(50000000.0f), 1);
        alltypes.setFloat(Float.valueOf(23.23f));
        run_FloatQuery(Float_filterObj, AllTypesParameter, alltypes, Float.valueOf(23.23f), 0);
        run_FloatQuery(Float_filterVal, null, null, Float.valueOf(100.0f), 2);
    
        run_DoubleQuery(Double_filterL, doubleParameter, Double.valueOf(350.5), Double.valueOf(350.5), 1);
        run_DoubleQuery(Double_filterR, doubleParameter, Double.valueOf(350.5), Double.valueOf(350.5), 1);
        run_DoubleQuery(Double_filterL, DoubleParameter, Double.valueOf(0.0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterR, DoubleParameter, Double.valueOf(23.34), Double.valueOf(23.34), 0);
        run_DoubleQuery(Double_filterL, byteParameter, Byte.valueOf((byte)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterR, byteParameter, Byte.valueOf((byte)0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterL, ByteParameter, Byte.valueOf((byte)23), Double.valueOf(23.0), 0);
        run_DoubleQuery(Double_filterR, ByteParameter, Byte.valueOf((byte)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterL, shortParameter, Short.valueOf((short)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterR, shortParameter, Short.valueOf((short)0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterL, ShortParameter, Short.valueOf((short)23), Double.valueOf(23.0), 0);
        run_DoubleQuery(Double_filterR, ShortParameter, Short.valueOf((short)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterL, charParameter, Character.valueOf((char)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterR, charParameter, Character.valueOf((char)0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterL, CharacterParameter, Character.valueOf((char)23), Double.valueOf(23.0), 0);
        run_DoubleQuery(Double_filterR, CharacterParameter, Character.valueOf((char)100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterL, intParameter, Integer.valueOf(100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterR, intParameter, Integer.valueOf(0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterL, IntegerParameter, Integer.valueOf(23), Double.valueOf(23.0), 0);
        run_DoubleQuery(Double_filterR, IntegerParameter, Integer.valueOf(100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterL, longParameter, Long.valueOf(100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterR, longParameter, Long.valueOf(0), Double.valueOf(0.0), 1);
        run_DoubleQuery(Double_filterL, LongParameter, Long.valueOf(23), Double.valueOf(23.0), 0);
        run_DoubleQuery(Double_filterR, LongParameter, Long.valueOf(100), Double.valueOf(100.0), 2);
        run_DoubleQuery(Double_filterL, floatParameter, Float.valueOf(0.0f), Double.valueOf(0.0f), 1);
        run_DoubleQuery(Double_filterR, floatParameter, Float.valueOf(100.0f), Double.valueOf(100.0f), 2);
        run_DoubleQuery(Double_filterL, FloatParameter, Float.valueOf(100.0f), Double.valueOf(100.0f), 2);
        run_DoubleQuery(Double_filterR, FloatParameter, Float.valueOf(69.96f), Double.valueOf(69.96), 0);
        run_DoubleQuery(Double_filterL, BigIntegerParameter, new BigInteger("50000000"), Double.valueOf(50000000.0f), 1);
        run_DoubleQuery(Double_filterR, BigIntegerParameter, new BigInteger("1000000000"), Double.valueOf(1000000000.0f), 1);
        run_DoubleQuery(Double_filterL, BigDecimalParameter, new BigDecimal("350.5"), Double.valueOf(350.5f), 1);
        run_DoubleQuery(Double_filterR, BigDecimalParameter, new BigDecimal("50000000.0"), Double.valueOf(50000000.0f), 1);
        alltypes.setDouble(Double.valueOf(-25.5));
        run_DoubleQuery(Double_filterObj, AllTypesParameter, alltypes, Double.valueOf(-25.5), 1);
        run_DoubleQuery(Double_filterVal, null, null, Double.valueOf(-25.5), 1);

        run_StringQuery(String_filterL, StringParameter, new String("JDO"), new String("JDO"), 1);
        run_StringQuery(String_filterR, StringParameter, new String("JDBC"), new String("JDBC"), 0);
        alltypes.setString(new String("abcde"));
        run_StringQuery(String_filterObj, AllTypesParameter, alltypes, new String("abcde"), 1);
        run_StringQuery(String_filterVal1, null, null, new String("Java"), 2);
        run_StringQuery(String_filterVal2, null, null, new String(""), 1);

        run_LocaleQuery(Locale_filterL, LocaleParameter, Locale.CANADA_FRENCH, Locale.CANADA_FRENCH, 0);
        run_LocaleQuery(Locale_filterR, LocaleParameter, Locale.US, Locale.US, 1);
        alltypes.setLocale(Locale.UK);
        run_LocaleQuery(Locale_filterObj, AllTypesParameter, alltypes, Locale.UK, 1);
    
        BigDecimal bd = new BigDecimal("100.0");
        run_BigDecimalQuery(BigDecimal_filterL, BigDecimalParameter, bd, bd, 2);
        bd = new BigDecimal("-234234.23333");
        run_BigDecimalQuery(BigDecimal_filterR, BigDecimalParameter, bd, bd, 1);
        bd = new BigDecimal("989899.33304953");
        run_BigDecimalQuery(BigDecimal_filterL, BigDecimalParameter, bd, bd, 0);
        bd = new BigDecimal("-1123123.22");
        run_BigDecimalQuery(BigDecimal_filterR, BigDecimalParameter, bd, bd, 1);
        alltypes.setBigDecimal(bd);
        run_BigDecimalQuery(BigDecimal_filterObj, AllTypesParameter, alltypes, bd, 1);

        BigInteger bi = new BigInteger("987034534985043985");
        run_BigIntegerQuery(BigInteger_filterL, BigIntegerParameter, bi, bi, 1);
        bi = new BigInteger("-999999999999999999");
        run_BigIntegerQuery(BigInteger_filterR, BigIntegerParameter, bi, bi, 1);
        bi = new BigInteger("-99999999999999999");
        run_BigIntegerQuery(BigInteger_filterR, BigIntegerParameter, bi, bi, 0);
        bi = new BigInteger("1333330");
        alltypes.setBigInteger(bi);
        run_BigIntegerQuery(BigInteger_filterObj, AllTypesParameter, alltypes, bi, 1);
    
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.UK);
        gc.clear();
        gc.set(1999, Calendar.DECEMBER, 31, 9, 0, 0);
        Date d = gc.getTime();
        run_DateQuery(Date_filterL, DateParameter, d, d, 1);
        gc.set(1992, Calendar.NOVEMBER, 22, 9, 0, 0);
        d = gc.getTime();
        run_DateQuery(Date_filterR, DateParameter, d, d, 1);
        gc.set(1959, Calendar.OCTOBER, 9, 9, 0, 0);
        d = gc.getTime();
        run_DateQuery(Date_filterR, DateParameter, d, d, 0);
        gc.set(1995, Calendar.JUNE, 14, 9, 0, 0);
        d = gc.getTime();
        alltypes.setDate(d);
        run_DateQuery(Date_filterObj, AllTypesParameter, alltypes, d, 1);
    }

    private void run_booleanQuery(String filter, String parameter, Object parameterValue, boolean value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while(iter.hasNext()){
            AllTypes obj = (AllTypes) iter.next();
            boolean val = obj.getboolean();
            if (val != value) {
                fail(ASSERTION_FAILED, "JDOQL Equality test returns object with incorrect value, retrieved value: " + val + ", expected value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_byteQuery(String filter, String parameter, Object parameterValue, byte value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            byte val = obj.getbyte();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_shortQuery(String filter, String parameter, Object parameterValue, short value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            short val = obj.getshort();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_charQuery(String filter, String parameter, Object parameterValue, char value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            char val = obj.getchar();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_intQuery(String filter, String parameter, Object parameterValue, int value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            int val = obj.getint();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_longQuery(String filter, String parameter, Object parameterValue, long value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            long val = obj.getlong();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_floatQuery(String filter, String parameter, Object parameterValue, float value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            float val = obj.getfloat();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_doubleQuery(String filter, String parameter, Object parameterValue, double value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            double val = obj.getdouble();
            if( val != value ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_BooleanQuery(String filter, String parameter, Object parameterValue, Boolean value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while(iter.hasNext()){
            AllTypes obj = (AllTypes) iter.next();
            Boolean val = obj.getBoolean();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns object with incorrect value", filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_ByteQuery(String filter, String parameter, Object parameterValue, Byte value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Byte val = obj.getByte();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_ShortQuery(String filter, String parameter, Object parameterValue, Short value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Short val = obj.getShort();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_CharacterQuery(String filter, String parameter, Object parameterValue, Character value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Character val = obj.getCharacter();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_IntegerQuery(String filter, String parameter, Object parameterValue, Integer value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Integer val = obj.getInteger();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_LongQuery(String filter, String parameter, Object parameterValue, Long value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Long val = obj.getLong();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_FloatQuery(String filter, String parameter, Object parameterValue, Float value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Float val = obj.getFloat();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_DoubleQuery(String filter, String parameter, Object parameterValue, Double value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Double val = obj.getDouble();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_StringQuery(String filter, String parameter, Object parameterValue, String value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            String val = obj.getString();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_LocaleQuery(String filter, String parameter, Object parameterValue, Locale value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Locale val = obj.getLocale();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);

            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_BigDecimalQuery(String filter, String parameter, Object parameterValue, BigDecimal value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            BigDecimal val = obj.getBigDecimal();
//        if( !val.equals(value) ){
            if( val.compareTo(value) != 0 ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_BigIntegerQuery(String filter, String parameter, Object parameterValue, BigInteger value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            BigInteger val = obj.getBigInteger();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_DateQuery(String filter, String parameter, Object parameterValue, Date value, int expected_count)
    {
        tx.begin();
        runQuery(pm, filter, parameter, parameterValue, ASSERTION_FAILED);
        if( query_result == null )
            return;
        Iterator iter = query_result.iterator();
        int cnt = 0;
        while( iter.hasNext() ){
            AllTypes obj = (AllTypes) iter.next();
            Date val = obj.getDate();
            if( !val.equals(value) ){
                fail(ASSERTION_FAILED, "JDOQL Equality test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL Equality test returns wrong number of instances", filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    /** */
    protected void localSetUp() {
        addTearDownClass(AllTypes.class);
        AllTypes.load(getPM());
    }
}
