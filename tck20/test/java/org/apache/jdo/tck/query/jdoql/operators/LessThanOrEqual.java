/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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
 *<B>Title:</B> Less Than or Equal Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-19.
 *<BR>
 *<B>Assertion Description: </B>
The less than or equal operator (<code>&lt;=</code>) is supported for all types
as they are defined in the Java language. This includes the following types:
<UL>
<LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code></LI>
<LI><code>float, double, Float, Double</code></LI>
<LI><code>BigDecimal, BigInteger</code></LI>
<LI><code>Date, String</code></LI>
</UL>
The operation on object-valued fields of wrapper types (<code>Boolean, Byte,
Short, Integer, Long, Float</code>, and <code>Double</code>), and numeric types
(<code>BigDecimal</code> and <code>BigInteger</code>)
use the wrapped values as operands.

 */

public class LessThanOrEqual extends ComparisonTests {
    private static      String      boolean_filterL     = "value <= fld_boolean";
    private static      String      boolean_filterR     = "fld_boolean <= value";
    private static      String      boolean_filterT     = "fld_boolean <= true";
    private static      String      boolean_filterF     = "false <= fld_boolean";
    private static      String      boolean_filterObj   = "value.fld_boolean <= fld_boolean";
    
    private static      String      byte_filterL        = "value <= fld_byte";
    private static      String      byte_filterR        = "fld_byte <= value";
    private static      String      byte_filterObj      = "value.fld_byte <= fld_byte";
    private static      String      byte_filterVal      = "fld_byte <= 100";
    
    private static      String      char_filterL        = "value <= fld_char";
    private static      String      char_filterR        = "fld_char <= value";
    private static      String      char_filterObj      = "value.fld_char <= fld_char";
    private static      String      char_filterVal      = "'M' <= fld_char";
    
    private static      String      double_filterL      = "value <= fld_double";
    private static      String      double_filterR      = "fld_double <= value";
    private static      String      double_filterObj    = "value.fld_double <= fld_double";
    private static      String      double_filterVal    = "fld_double <= 100.0";
    
    private static      String      float_filterL       = "value <= fld_float";
    private static      String      float_filterR       = "fld_float <= value";
    private static      String      float_filterObj     = "fld_float <= value.fld_float";
    private static      String      float_filterVal     = "fld_float <= 100.0";
    
    private static      String      int_filterL         = "value <= fld_int";
    private static      String      int_filterR         = "fld_int <= value";
    private static      String      int_filterObj       = "value.fld_int <= fld_int";
    private static      String      int_filterVal       = "fld_int <= 1000";
    
    private static      String      long_filterL        = "value <= fld_long";
    private static      String      long_filterR        = "fld_long <= value";
    private static      String      long_filterObj      = "fld_long <= value.fld_long";
    private static      String      long_filterVal      = "fld_long <= 1000000";
    
    private static      String      short_filterL       = "value <= fld_short";
    private static      String      short_filterR       = "fld_short <= value";
    private static      String      short_filterObj     = "value.fld_short <= fld_short";
    private static      String      short_filterVal     = "1000 <= fld_short";
    
    private static      String      Boolean_filterL     = "value <= fld_Boolean";
    private static      String      Boolean_filterR     = "fld_Boolean <= value";
    private static      String      Boolean_filterT     = "fld_Boolean <= true";
    private static      String      Boolean_filterF     = "false <= fld_Boolean";
    private static      String      Boolean_filterObj   = "value.fld_Boolean <= fld_Boolean";
    private static      String      Boolean_filterVal   = "fld_Boolean <= false";
    
    private static      String      Byte_filterL        = "value <= fld_Byte";
    private static      String      Byte_filterR        = "fld_Byte <= value";
    private static      String      Byte_filterObj      = "fld_Byte <= value.fld_Byte";
    private static      String      Byte_filterVal      = "100 <= fld_Byte";
    
    private static      String      Character_filterL   = "value <= fld_Character";
    private static      String      Character_filterR   = "fld_Character <= value";
    private static      String      Character_filterObj = "value.fld_Character <= fld_Character";
    private static      String      Character_filterVal = "fld_Character <= 'z'";
    
    private static      String      Double_filterL      = "value <= fld_Double";
    private static      String      Double_filterR      = "fld_Double <= value";
    private static      String      Double_filterObj    = "value.fld_Double <= fld_Double";
    private static      String      Double_filterVal    = "fld_Double <= 100.0";
    
    private static      String      Float_filterL       = "value <= fld_Float";
    private static      String      Float_filterR       = "fld_Float <= value";
    private static      String      Float_filterObj     = "fld_Float <= value.fld_Float";
    private static      String      Float_filterVal     = "100.0f <= fld_Float";
    
    private static      String      Integer_filterL     = "value <= fld_Integer";
    private static      String      Integer_filterR     = "fld_Integer <= value";
    private static      String      Integer_filterObj   = "fld_Integer <= value.fld_Integer";
    private static      String      Integer_filterVal   = "fld_Integer <= 100";
    
    private static      String      Long_filterL        = "value <= fld_Long";
    private static      String      Long_filterR        = "fld_Long <= value";
    private static      String      Long_filterObj      = "value.fld_Long <= fld_Long";
    private static      String      Long_filterVal      = "-1000 <= fld_Long";
    
    private static      String      Short_filterL       = "value <= fld_Short";
    private static      String      Short_filterR       = "fld_Short <= value";
    private static      String      Short_filterObj     = "fld_Short <= value.fld_Short";
    private static      String      Short_filterVal     = "-1000 <= fld_Short";
    
    private static      String      String_filterL      = "value <= fld_String";
    private static      String      String_filterR      = "fld_String <= value";
    private static      String      String_filterObj    = "value.fld_String <= fld_String";
    private static      String      String_filterVal1   = "fld_String <= \"Java\"";
    private static      String      String_filterVal2   = "fld_String <= \"\"";
    
    private static      String      Date_filterL        = "value <= fld_Date";
    private static      String      Date_filterR        = "fld_Date <= value";
    private static      String      Date_filterObj      = "fld_Date <= value.fld_Date";
    
    private static      String      BigDecimal_filterL  = "value <= fld_BigDecimal";
    private static      String      BigDecimal_filterR  = "fld_BigDecimal <= value";
    private static      String      BigDecimal_filterObj = "value.fld_BigDecimal <= fld_BigDecimal";
    
    private static      String      BigInteger_filterL  = "value <= fld_BigInteger";
    private static      String      BigInteger_filterR  = "fld_BigInteger <= value";
    private static      String      BigInteger_filterObj = "fld_BigInteger <= value.fld_BigInteger";

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-19 (LessThanOrEqual) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(LessThanOrEqual.class);
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
        AllTypes alltypes = new AllTypes();
        run_byteQuery(byte_filterL, byteParameter, new Byte((byte)0), (byte)0, true, 7);
        run_byteQuery(byte_filterR, byteParameter, new Byte(Byte.MIN_VALUE), Byte.MIN_VALUE, false, 1);
        run_byteQuery(byte_filterL, ByteParameter, new Byte((byte)50), (byte)50, true, 5);
        run_byteQuery(byte_filterR, ByteParameter, new Byte(Byte.MAX_VALUE), Byte.MAX_VALUE, false, 10);    
        run_byteQuery(byte_filterL, shortParameter, new Short((short)75), (byte)75, true, 3);
        run_byteQuery(byte_filterR, shortParameter, new Short((short)75), (byte)75, false, 8);
        run_byteQuery(byte_filterL, ShortParameter, new Short((short)10), (byte)10, true, 6);
        run_byteQuery(byte_filterR, ShortParameter, new Short((short)25), (byte)25, false, 5);
        run_byteQuery(byte_filterL, charParameter, new Character((char)101), (byte)101, true, 1);
        run_byteQuery(byte_filterR, charParameter, new Character((char)50), (byte)50, false, 7);
        run_byteQuery(byte_filterL, CharacterParameter, new Character((char)0), (byte)0, true, 7);
        run_byteQuery(byte_filterR, CharacterParameter, new Character((char)0), (byte)0, false, 4);    
        run_byteQuery(byte_filterL, intParameter, new Integer(25), (byte)25, true, 5);
        run_byteQuery(byte_filterR, intParameter, new Integer(50), (byte)50, false, 7);
        run_byteQuery(byte_filterL, IntegerParameter, new Integer(-10), (byte)-10, true, 8);
        run_byteQuery(byte_filterR, IntegerParameter, new Integer(-100), (byte)-100, false, 2);
        run_byteQuery(byte_filterL, longParameter, new Long(50), (byte)50, true, 5);
        run_byteQuery(byte_filterR, longParameter, new Long(60), (byte)60, false, 7);
        run_byteQuery(byte_filterL, LongParameter, new Long(-100), (byte)-100, true, 9);
        run_byteQuery(byte_filterR, LongParameter, new Long(-100), (byte)-100, false, 2);
        run_byteQuery(byte_filterL, floatParameter, new Float(51), (byte)51, true, 3);
        run_byteQuery(byte_filterR, floatParameter, new Float(-20), (byte)-20, false, 2);
        run_byteQuery(byte_filterL, FloatParameter, new Float(-99), (byte)-99, true, 8);
        run_byteQuery(byte_filterR, FloatParameter, new Float(-100), (byte)-100, false, 2);
        run_byteQuery(byte_filterL, doubleParameter, new Double(50), (byte)50, true, 5);
        run_byteQuery(byte_filterR, doubleParameter, new Double(60), (byte)60, false, 7);
        run_byteQuery(byte_filterL, DoubleParameter, new Double(Byte.MAX_VALUE), Byte.MAX_VALUE, true, 1);
        run_byteQuery(byte_filterR, DoubleParameter, new Double(25), (byte)25, false, 5);
        run_byteQuery(byte_filterL, BigIntegerParameter, new BigInteger("50"), (byte)50, true, 5);
        run_byteQuery(byte_filterR, BigIntegerParameter, new BigInteger("-100"), (byte)-100, false, 2);
        run_byteQuery(byte_filterL, BigDecimalParameter, new BigDecimal("50.000000"), (byte)50, true, 5);
        run_byteQuery(byte_filterR, BigDecimalParameter, new BigDecimal("10.00000"), (byte)10, false, 5);    
        alltypes.setbyte((byte)50);
        run_byteQuery(byte_filterObj, AllTypesParameter, alltypes, (byte)50, true, 5);
        alltypes.setbyte((byte)55);
        run_byteQuery(byte_filterObj, AllTypesParameter, alltypes, (byte)55, true, 3);
        run_byteQuery(byte_filterVal, null, null, (byte)100, false, 9);

        run_shortQuery(short_filterL, shortParameter, new Short((short)100), (short)100, true, 5);
        run_shortQuery(short_filterR, shortParameter, new Short((short)100), (short)100, false, 7);
        run_shortQuery(short_filterL, ShortParameter, new Short(Short.MIN_VALUE), Short.MIN_VALUE, true, 10);
        run_shortQuery(short_filterR, ShortParameter, new Short((short)253), (short)253, false, 7);
        run_shortQuery(short_filterR, shortParameter, new Short((short)1000), (short)1000, false, 8);
        run_shortQuery(short_filterL, byteParameter, new Byte((byte)75), (short)75, true, 5);
        run_shortQuery(short_filterR, byteParameter, new Byte((byte)-75), (short)-75, false, 4);
        run_shortQuery(short_filterL, ByteParameter, new Byte((byte)100), (short)100, true, 5);
        run_shortQuery(short_filterR, ByteParameter, new Byte((byte)100), (short)100, false, 7);
        run_shortQuery(short_filterL, charParameter, new Character((char)75), (short)75, true, 5);
        run_shortQuery(short_filterR, charParameter, new Character((char)9999), (short)9999, false, 8);
        run_shortQuery(short_filterL, CharacterParameter, new Character((char)1000), (short)1000, true, 3);
        run_shortQuery(short_filterR, CharacterParameter, new Character((char)10000), (short)10000, false, 9);
        run_shortQuery(short_filterL, intParameter, new Integer(-10000), (short)-10000, true, 9);
        run_shortQuery(short_filterR, intParameter, new Integer(-10000), (short)-10000, false, 2);
        run_shortQuery(short_filterL, IntegerParameter, new Integer(10000), (short)10000, true, 2);
        run_shortQuery(short_filterR, IntegerParameter, new Integer(30000), (short)30000, false, 9);
        run_shortQuery(short_filterL, longParameter, new Long(10000), (short) 10000, true, 2);
        run_shortQuery(short_filterR, longParameter, new Long(Short.MAX_VALUE), Short.MAX_VALUE, false, 10);
        run_shortQuery(short_filterL, LongParameter, new Long(Short.MAX_VALUE), Short.MAX_VALUE, true, 1);
        run_shortQuery(short_filterR, LongParameter, new Long(100), (short)100, false, 7);
        run_shortQuery(short_filterL, floatParameter, new Float(23000), (short)23000, true, 1);
        run_shortQuery(short_filterR, floatParameter, new Float(23000), (short)23000, false, 9);
        run_shortQuery(short_filterL, FloatParameter, new Float(-1000), (short)-1000, true, 8);
        run_shortQuery(short_filterR, FloatParameter, new Float(100), (short)100, false, 7);
        run_shortQuery(short_filterL, doubleParameter, new Double(-10000.0), (short)-10000, true, 9);
        run_shortQuery(short_filterR, doubleParameter, new Double(9999.0), (short)9999, false, 8);
        run_shortQuery(short_filterL, DoubleParameter, new Double(23.0), (short)23, true, 5);
        run_shortQuery(short_filterR, DoubleParameter, new Double(23.0), (short)23, false, 5);
        run_shortQuery(short_filterL, BigIntegerParameter, new BigInteger("10000"), (short)10000, true, 2);
        run_shortQuery(short_filterR, BigIntegerParameter, new BigInteger("30000"), (short)30000, false, 9);
        run_shortQuery(short_filterL, BigDecimalParameter, new BigDecimal("23.0"), (short)23, true, 5);
        run_shortQuery(short_filterR, BigDecimalParameter, new BigDecimal("23.0"), (short)23, false, 5);
        alltypes.setshort((short)100);
        run_shortQuery(short_filterObj, AllTypesParameter, alltypes, (short)100, true, 5);
        alltypes.setshort((short)23);
        run_shortQuery(short_filterObj, AllTypesParameter, alltypes, (short)23, true, 5);
        run_shortQuery(short_filterVal, null, null, (short)1000, true, 3);
    
        run_charQuery(char_filterL, charParameter, new Character(Character.MIN_VALUE), Character.MIN_VALUE, true, 10);
        run_charQuery(char_filterR, charParameter, new Character(Character.MAX_VALUE), Character.MAX_VALUE, false, 10);
        run_charQuery(char_filterL, charParameter, new Character('C'), 'C', true, 6);
        run_charQuery(char_filterR, charParameter, new Character('z'), 'z', false, 9);
        run_charQuery(char_filterL, CharacterParameter, new Character(' '), ' ', true, 9);
        run_charQuery(char_filterR, CharacterParameter, new Character('f'), 'f', false, 7);
        run_charQuery(char_filterL, byteParameter, new Byte((byte)Character.MIN_VALUE), (char)Character.MIN_VALUE, true, 10);
        run_charQuery(char_filterR, ByteParameter, new Byte((byte)'a'), 'a', false, 7);
        run_charQuery(char_filterL, shortParameter, new Short((short)'M'), 'M', true, 5);
        run_charQuery(char_filterR, shortParameter, new Short((short)'M'), 'M', false, 7);
        run_charQuery(char_filterL, ShortParameter, new Short((short)'A'), 'A', true, 8);
        run_charQuery(char_filterR, ShortParameter, new Short((short)'A'), 'A', false, 3);
        run_charQuery(char_filterL, intParameter, new Integer('z'), 'z', true, 3);
        run_charQuery(char_filterR, intParameter, new Integer('z'), 'z', false, 9);
        run_charQuery(char_filterL, IntegerParameter, new Integer('B'), 'B', true, 7);
        run_charQuery(char_filterR, IntegerParameter, new Integer('B'), 'B', false, 4);
        run_charQuery(char_filterL, longParameter, new Long('z'), 'z', true, 3);
        run_charQuery(char_filterR, longParameter, new Long('z'), 'z', false, 9);
        run_charQuery(char_filterL, LongParameter, new Long('B'), 'B', true, 7);
        run_charQuery(char_filterR, LongParameter, new Long('B'), 'B', false, 4);
        run_charQuery(char_filterL, floatParameter, new Float('f'), 'f', true, 3);
        run_charQuery(char_filterR, floatParameter, new Float(' '), ' ', false, 2);
        run_charQuery(char_filterL, FloatParameter, new Float('z'), 'z', true, 3);
        run_charQuery(char_filterR, FloatParameter, new Float('z'), 'z', false, 9);
        run_charQuery(char_filterL, doubleParameter, new Double('B'), 'B', true, 7);
        run_charQuery(char_filterR, doubleParameter, new Double('B'), 'B', false, 4);
        run_charQuery(char_filterL, DoubleParameter, new Double('A'), 'A', true, 8);
        run_charQuery(char_filterR, DoubleParameter, new Double('A'), 'A', false, 3);
        run_charQuery(char_filterL, BigIntegerParameter, new BigInteger("65"), 'A', true, 8);  // 'A' == 65
        run_charQuery(char_filterR, BigIntegerParameter, new BigInteger("122"), 'z', false, 9);  // 'z' == 122
        run_charQuery(char_filterL, BigDecimalParameter, new BigDecimal("65.00000"), 'A', true, 8);
        run_charQuery(char_filterR, BigDecimalParameter, new BigDecimal("77.0000"), 'M', false, 7); // 'M' == 77
        alltypes.setchar('A');
        run_charQuery(char_filterObj, AllTypesParameter, alltypes, 'A', true, 8);
        alltypes.setchar('b');
        run_charQuery(char_filterObj, AllTypesParameter, alltypes, 'b', true, 3);
        run_charQuery(char_filterVal, null, null, 'M', true, 5);
    
        run_intQuery(int_filterL, intParameter, new Integer(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, true, 10);
        run_intQuery(int_filterR, intParameter, new Integer(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, false, 10);
        run_intQuery(int_filterR, intParameter, new Integer(23), 23, false, 4);
        run_intQuery(int_filterL, IntegerParameter, new Integer(1000000), 1000000, true, 2);
        run_intQuery(int_filterR, IntegerParameter, new Integer(1000), 1000, false, 7);
        run_intQuery(int_filterL, byteParameter, new Byte((byte)100), 100, true, 6);
        run_intQuery(int_filterR, byteParameter, new Byte((byte)0), 0, false, 4);
        run_intQuery(int_filterL, ByteParameter, new Byte((byte)100), 100, true, 6);
        run_intQuery(int_filterR, ByteParameter, new Byte((byte)0), 0, false, 4);
        run_intQuery(int_filterL, shortParameter, new Short((short)10000), 10000, true, 3);
        run_intQuery(int_filterR, shortParameter, new Short((short)-1000), -1000, false, 3);
        run_intQuery(int_filterL, ShortParameter, new Short((short)-1000), -1000, true, 8);
        run_intQuery(int_filterR, ShortParameter, new Short((short)10000), 10000, false, 8);
        run_intQuery(int_filterL, charParameter, new Character((char)100), 100, true, 6);
        run_intQuery(int_filterR, charParameter, new Character((char)0), 0, false, 4);
        run_intQuery(int_filterL, CharacterParameter, new Character((char)100), 100, true, 6);
        run_intQuery(int_filterR, CharacterParameter, new Character((char)10000), 10000, false, 8);
        run_intQuery(int_filterL, longParameter, new Long(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, true, 1);
        run_intQuery(int_filterR, longParameter, new Long(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, false, 1);
        run_intQuery(int_filterL, LongParameter, new Long(10000), 10000, true, 3);
        run_intQuery(int_filterR, LongParameter, new Long(43), 43, false, 4);
        run_intQuery(int_filterL, floatParameter, new Float(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, true, 1);
        run_intQuery(int_filterR, floatParameter, new Float(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, false, 1);
        run_intQuery(int_filterL, FloatParameter, new Float(10000), 10000, true, 3);
        run_intQuery(int_filterR, FloatParameter, new Float(43), 43, false, 4);
        run_intQuery(int_filterL, doubleParameter, new Double(AllTypes.veryLargePositiveInt), AllTypes.veryLargePositiveInt, true, 1);
        run_intQuery(int_filterR, doubleParameter, new Double(AllTypes.veryLargeNegativeInt), AllTypes.veryLargeNegativeInt, false, 1);
        run_intQuery(int_filterL, DoubleParameter, new Double(10000), 10000, true, 3);
        run_intQuery(int_filterR, DoubleParameter, new Double(43), 43, false, 4);
        run_intQuery(int_filterL, BigIntegerParameter, new BigInteger("1000000"), 1000000, true, 2);
        run_intQuery(int_filterR, BigIntegerParameter, new BigInteger("1000"), 1000, false, 7);
        run_intQuery(int_filterL, BigDecimalParameter, new BigDecimal("10000.0"), 10000, true, 3);
        run_intQuery(int_filterR, BigDecimalParameter, new BigDecimal("43.0"), 43, false, 4);
        alltypes.setint(100);
        run_intQuery(int_filterObj, AllTypesParameter, alltypes, 100, true, 6);
        run_intQuery(int_filterVal, null, null, 1000, false, 7);
    
        run_longQuery(long_filterL, longParameter, new Long(Long.MIN_VALUE), Long.MIN_VALUE, true, 10);
        run_longQuery(long_filterR, longParameter, new Long(Long.MAX_VALUE), Long.MAX_VALUE, false, 10);
        run_longQuery(long_filterL, LongParameter, new Long(100), 100, true, 5);
        run_longQuery(long_filterR, LongParameter, new Long(23), 23, false, 5);
        run_longQuery(long_filterL, byteParameter, new Byte((byte)100), 100, true, 5);
        run_longQuery(long_filterR, byteParameter, new Byte((byte)0), 0, false, 5);
        run_longQuery(long_filterL, ByteParameter, new Byte((byte)100), 100, true, 5);
        run_longQuery(long_filterR, ByteParameter, new Byte((byte)0), 0, false, 5);
        run_longQuery(long_filterL, shortParameter, new Short((short)-1000), -1000, true, 8);
        run_longQuery(long_filterR, shortParameter, new Short((short)1000), 1000, false, 8);
        run_longQuery(long_filterL, ShortParameter, new Short((short)100), 100, true, 5);
        run_longQuery(long_filterR, ShortParameter, new Short((short)32), 32, false, 5);
        run_longQuery(long_filterL, charParameter, new Character((char)0), 0, true, 6);
        run_longQuery(long_filterR, charParameter, new Character((char)100), 100, false, 7);
        run_longQuery(long_filterL, CharacterParameter, new Character((char)23), 23, true, 5);
        run_longQuery(long_filterR, CharacterParameter, new Character((char)0), 0, false, 5);
        run_longQuery(long_filterL, intParameter, new Integer(100), 100, true, 5);
        run_longQuery(long_filterR, intParameter, new Integer(0), 0, false, 5);
        run_longQuery(long_filterL, IntegerParameter, new Integer(23), 23, true, 5);
        run_longQuery(long_filterR, IntegerParameter, new Integer(1000000), 1000000, false, 9);
        run_longQuery(long_filterL, floatParameter, new Float(-1000000.0), -1000000, true, 9);
        run_longQuery(long_filterR, floatParameter, new Float((float)Long.MAX_VALUE), Long.MAX_VALUE, false, 10);
        run_longQuery(long_filterL, FloatParameter, new Float(100.0), 100, true, 5);
        run_longQuery(long_filterR, FloatParameter, new Float(32.0), 32, false, 5);
        run_longQuery(long_filterL, doubleParameter, new Double(-1000000.0), -1000000, true, 9);
        run_longQuery(long_filterR, doubleParameter, new Double((double)Long.MAX_VALUE), Long.MAX_VALUE, false, 10);
        run_longQuery(long_filterL, DoubleParameter, new Double(100.0), 100, true, 5);
        run_longQuery(long_filterR, DoubleParameter, new Double(32.0), 32, false, 5);
        run_longQuery(long_filterL, BigIntegerParameter, new BigInteger("23"), 23, true, 5);
        run_longQuery(long_filterR, BigIntegerParameter, new BigInteger("1000000"), 1000000, false, 9);
        run_longQuery(long_filterL, BigDecimalParameter, new BigDecimal("100.0"), 100, true, 5);
        run_longQuery(long_filterR, BigDecimalParameter, new BigDecimal("32.0"), 32, false, 5);
        alltypes.setlong(100);
        run_longQuery(long_filterObj, AllTypesParameter, alltypes, 100, false, 7);
        run_longQuery(long_filterVal, null, null, 1000000, false, 9);
    
        run_floatQuery(float_filterL, floatParameter, new Float(AllTypes.FLOAT_SMALLEST), AllTypes.FLOAT_SMALLEST, true, 10);
        run_floatQuery(float_filterR, floatParameter, new Float(AllTypes.FLOAT_LARGEST), AllTypes.FLOAT_LARGEST, false, 10);
        run_floatQuery(float_filterL, FloatParameter, new Float(4.0f), 4.0f, true, 6);
        run_floatQuery(float_filterR, FloatParameter, new Float(400.0f), 400.0f, false, 7);
        run_floatQuery(float_filterL, byteParameter, new Byte((byte)4), 4.0f, true, 6);
        run_floatQuery(float_filterR, byteParameter, new Byte((byte)23), 23.0f, false, 4);
        run_floatQuery(float_filterL, ByteParameter, new Byte((byte)34), 34.0f, true, 6);
        run_floatQuery(float_filterR, ByteParameter, new Byte((byte)100), 100.0f, false, 6);
        run_floatQuery(float_filterL, shortParameter, new Short((short)10), 10.0f, true, 6);
        run_floatQuery(float_filterR, shortParameter, new Short((short)23), 23.0f, false, 4);
        run_floatQuery(float_filterL, ShortParameter, new Short((short)34), 34.0f, true, 6);
        run_floatQuery(float_filterR, ShortParameter, new Short((short)100), 100.0f, false, 6);
        run_floatQuery(float_filterL, charParameter, new Character((char)20), 20.0f, true, 6);
        run_floatQuery(float_filterR, charParameter, new Character((char)23), 23.0f, false, 4);
        run_floatQuery(float_filterL, CharacterParameter, new Character((char)34), 34.0f, true, 6);
        run_floatQuery(float_filterR, CharacterParameter, new Character((char)100), 100.0f, false, 6);
        run_floatQuery(float_filterL, intParameter, new Integer(55000000), 55000000.0f, true, 2);
        run_floatQuery(float_filterR, intParameter, new Integer(23), 23.0f, false, 4);
        run_floatQuery(float_filterL, IntegerParameter, new Integer(34), 34.0f, true, 6);
        run_floatQuery(float_filterR, IntegerParameter, new Integer(100), 100.0f, false, 6);
        run_floatQuery(float_filterL, longParameter, new Long(55000000), 55000000.0f, true, 2);
        run_floatQuery(float_filterR, longParameter, new Long(23), 23.0f, false, 4);
        run_floatQuery(float_filterL, LongParameter, new Long(34), 34.0f, true, 6);
        run_floatQuery(float_filterR, LongParameter, new Long(100), 100.0f, false, 6);
        run_floatQuery(float_filterL, doubleParameter, new Double(55000000.0), 55000000.0f, true, 2);
        run_floatQuery(float_filterR, doubleParameter, new Double(-20.5), -20.5f, false, 3);
        run_floatQuery(float_filterL, DoubleParameter, new Double(2.0), 2.0f, true, 6);
        run_floatQuery(float_filterR, DoubleParameter, new Double(100.0), 100.0f, false, 6);
        run_floatQuery(float_filterL, BigIntegerParameter, new BigInteger("55000000"), 55000000.0f, true, 2);
        run_floatQuery(float_filterR, BigIntegerParameter, new BigInteger("23"), 23.0f, false, 4);
        run_floatQuery(float_filterL, BigDecimalParameter, new BigDecimal("55000000.0"), 55000000.0f, true, 2);
        run_floatQuery(float_filterR, BigDecimalParameter, new BigDecimal("-20.5"), -20.5f, false, 3);
        alltypes.setfloat(23.23f);
        run_floatQuery(float_filterObj, AllTypesParameter, alltypes, 23.23f, false, 4);
        run_floatQuery(float_filterVal, null, null, 100.0f, false, 6);
    
        run_doubleQuery(double_filterL, doubleParameter, new Double(AllTypes.DOUBLE_SMALLEST), AllTypes.DOUBLE_SMALLEST, true, 10);
        run_doubleQuery(double_filterR, doubleParameter, new Double(AllTypes.DOUBLE_LARGEST), AllTypes.DOUBLE_LARGEST, false, 10);
        run_doubleQuery(double_filterL, DoubleParameter, new Double(0.0), 0.0, true, 7);
        run_doubleQuery(double_filterR, DoubleParameter, new Double(23.34), 23.34, false, 4);
        run_doubleQuery(double_filterL, byteParameter, new Byte((byte)100), 100.0, true, 6);
        run_doubleQuery(double_filterR, byteParameter, new Byte((byte)0), 0.0, false, 4);
        run_doubleQuery(double_filterL, ByteParameter, new Byte((byte)23), 23.0, true, 6);
        run_doubleQuery(double_filterR, ByteParameter, new Byte((byte)100), 100.0, false, 6);
        run_doubleQuery(double_filterL, charParameter, new Character((char)100), 100.0, true, 6);
        run_doubleQuery(double_filterR, charParameter, new Character((char)0), 0.0, false, 4);
        run_doubleQuery(double_filterL, CharacterParameter, new Character((char)23), 23.0, true, 6);
        run_doubleQuery(double_filterR, CharacterParameter, new Character((char)100), 100.0, false, 6);
        run_doubleQuery(double_filterL, shortParameter, new Short((short)100), 100.0, true, 6);
        run_doubleQuery(double_filterR, shortParameter, new Short((short)0), 0.0, false, 4);
        run_doubleQuery(double_filterL, ShortParameter, new Short((short)23), 23.0, true, 6);
        run_doubleQuery(double_filterR, ShortParameter, new Short((short)100), 100.0, false, 6);
        run_doubleQuery(double_filterL, intParameter, new Integer(100), 100.0, true, 6);
        run_doubleQuery(double_filterR, intParameter, new Integer(0), 0.0, false, 4);
        run_doubleQuery(double_filterL, IntegerParameter, new Integer(5000), 5000.0, true, 3);
        run_doubleQuery(double_filterR, IntegerParameter, new Integer(-20), -20.0, false, 3);
        run_doubleQuery(double_filterL, longParameter, new Long(100), 100.0, true, 6);
        run_doubleQuery(double_filterR, longParameter, new Long(0), 0.0, false, 4);
        run_doubleQuery(double_filterL, LongParameter, new Long(5000), 5000.0, true, 3);
        run_doubleQuery(double_filterR, LongParameter, new Long(-20), -20.0, false, 3);
        run_doubleQuery(double_filterL, floatParameter, new Float(0.0f), 0.0f, true, 7);
        run_doubleQuery(double_filterR, floatParameter, new Float(100.0f), 100.0f, false, 6);
        run_doubleQuery(double_filterL, FloatParameter, new Float(100.0f), 100.0, true, 6);
        run_doubleQuery(double_filterR, FloatParameter, new Float(69.96f), 69.96, false, 4);
        run_doubleQuery(double_filterL, BigIntegerParameter, new BigInteger("5000"), 5000.0, true, 3);
        run_doubleQuery(double_filterR, BigIntegerParameter, new BigInteger("-20"), -20.0, false, 3);
        run_doubleQuery(double_filterL, BigDecimalParameter, new BigDecimal("100.0"), 100.0, true, 6);
        run_doubleQuery(double_filterR, BigDecimalParameter, new BigDecimal("69.96"), 69.96, false, 4);
        alltypes.setdouble(-25.5);
        run_doubleQuery(double_filterObj, AllTypesParameter, alltypes, -25.5, true, 8);
        run_doubleQuery(double_filterVal, null, null, 100.0, false, 6);
    
        run_ByteQuery(Byte_filterL, byteParameter, new Byte((byte)50), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, byteParameter, new Byte(Byte.MIN_VALUE), new Byte(Byte.MIN_VALUE), false, 1);
        run_ByteQuery(Byte_filterL, ByteParameter, new Byte((byte)20), new Byte((byte)20), true, 5);
        run_ByteQuery(Byte_filterR, ByteParameter, new Byte(Byte.MAX_VALUE), new Byte(Byte.MAX_VALUE), false, 10);    
        run_ByteQuery(Byte_filterL, shortParameter, new Short((short)60), new Byte((byte)60), true, 3);
        run_ByteQuery(Byte_filterR, shortParameter, new Short((short)51), new Byte((byte)51), false, 7);
        run_ByteQuery(Byte_filterL, ShortParameter, new Short((short)-100), new Byte((byte)-100), true, 9);
        run_ByteQuery(Byte_filterR, ShortParameter, new Short((short)-100), new Byte((byte)-100), false, 2);
        run_ByteQuery(Byte_filterL, charParameter, new Character((char)101), new Byte((byte)101), true, 1);
        run_ByteQuery(Byte_filterR, charParameter, new Character((char)10), new Byte((byte)10), false, 5);
        run_ByteQuery(Byte_filterL, CharacterParameter, new Character((char)50), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, CharacterParameter, new Character((char)75), new Byte((byte)75), false, 8);    
        run_ByteQuery(Byte_filterL, intParameter, new Integer(77), new Byte((byte)77), true, 2);
        run_ByteQuery(Byte_filterR, intParameter, new Integer(60), new Byte((byte)60), false, 7);
        run_ByteQuery(Byte_filterL, IntegerParameter, new Integer(40), new Byte((byte)40), true, 5);
        run_ByteQuery(Byte_filterR, IntegerParameter, new Integer(75), new Byte((byte)75), false, 8);
        run_ByteQuery(Byte_filterL, longParameter, new Long(50), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, longParameter, new Long(50), new Byte((byte)50), false, 7);
        run_ByteQuery(Byte_filterL, LongParameter, new Long(-100), new Byte((byte)-100), true, 9);
        run_ByteQuery(Byte_filterR, LongParameter, new Long(-100), new Byte((byte)-100), false, 2);
        run_ByteQuery(Byte_filterL, floatParameter, new Float(50), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, floatParameter, new Float(50), new Byte((byte)50), false, 7);
        run_ByteQuery(Byte_filterL, FloatParameter, new Float(-100), new Byte((byte)-100), true, 9);
        run_ByteQuery(Byte_filterR, FloatParameter, new Float(-100), new Byte((byte)-100), false, 2);
        run_ByteQuery(Byte_filterL, doubleParameter, new Double(50), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, doubleParameter, new Double(50), new Byte((byte)50), false, 7);
        run_ByteQuery(Byte_filterL, DoubleParameter, new Double(-100), new Byte((byte)-100), true, 9);
        run_ByteQuery(Byte_filterR, DoubleParameter, new Double(-100), new Byte((byte)-100), false, 2);
        run_ByteQuery(Byte_filterL, BigIntegerParameter, new BigInteger("50"), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, BigIntegerParameter, new BigInteger("-100"), new Byte((byte)-100), false, 2);
        run_ByteQuery(Byte_filterL, BigDecimalParameter, new BigDecimal("50.000000"), new Byte((byte)50), true, 5);
        run_ByteQuery(Byte_filterR, BigDecimalParameter, new BigDecimal("10.00000"), new Byte((byte)10), false, 5);    
        Byte val = new Byte((byte)50);
        alltypes.setByte(val);
        run_ByteQuery(Byte_filterObj, AllTypesParameter, alltypes, val, false, 7);
        val = new Byte((byte)51);
        alltypes.setByte(val);
        run_ByteQuery(Byte_filterObj, AllTypesParameter, alltypes, val, false, 7);
        run_ByteQuery(Byte_filterVal, null, null, new Byte((byte)100), true, 2);
    
        run_ShortQuery(Short_filterL, shortParameter, new Short((short)100), new Short((short)100), true, 5);
        run_ShortQuery(Short_filterR, shortParameter, new Short((short)100), new Short((short)100), false, 7);
        run_ShortQuery(Short_filterL, ShortParameter, new Short(Short.MIN_VALUE), new Short(Short.MIN_VALUE), true, 10);
        run_ShortQuery(Short_filterR, ShortParameter, new Short((short)253), new Short((short)253), false, 7);
        run_ShortQuery(Short_filterR, shortParameter, new Short((short)1000), new Short((short)1000), false, 8);
        run_ShortQuery(Short_filterL, byteParameter, new Byte((byte)75), new Short((short)75), true, 5);
        run_ShortQuery(Short_filterR, byteParameter, new Byte((byte)75), new Short((short)75), false, 5);
        run_ShortQuery(Short_filterL, ByteParameter, new Byte((byte)100), new Short((short)100), true, 5);
        run_ShortQuery(Short_filterR, ByteParameter, new Byte((byte)100), new Short((short)100), false, 7);
        run_ShortQuery(Short_filterL, charParameter, new Character((char)75), new Short((short)75), true, 5);
        run_ShortQuery(Short_filterR, charParameter, new Character((char)75), new Short((short)75), false, 5);
        run_ShortQuery(Short_filterL, CharacterParameter, new Character((char)100), new Short((short)100), true, 5);
        run_ShortQuery(Short_filterR, CharacterParameter, new Character((char)200), new Short((short)200), false, 7);
        run_ShortQuery(Short_filterL, intParameter, new Integer(-10000), new Short((short)-10000), true, 9);
        run_ShortQuery(Short_filterR, intParameter, new Integer(-10000), new Short((short)-10000), false, 2);
        run_ShortQuery(Short_filterL, IntegerParameter, new Integer(10000), new Short((short)10000), true, 2);
        run_ShortQuery(Short_filterR, IntegerParameter, new Integer(10000), new Short((short)10000), false, 9);
        run_ShortQuery(Short_filterL, longParameter, new Long(20000), new Short((short) 20000), true, 1);
        run_ShortQuery(Short_filterR, longParameter, new Long(5000), new Short((short) 5000), false, 8);
        run_ShortQuery(Short_filterL, LongParameter, new Long(200), new Short((short)200), true, 3);
        run_ShortQuery(Short_filterR, LongParameter, new Long(500), new Short((short)500), false, 7);
        run_ShortQuery(Short_filterL, floatParameter, new Float(23000.0f), new Short((short)23000), true, 1);
        run_ShortQuery(Short_filterR, floatParameter, new Float(23000.0f), new Short((short)23000), false, 9);
        run_ShortQuery(Short_filterL, FloatParameter, new Float(10.0f), new Short((short)10), true, 5);
        run_ShortQuery(Short_filterR, FloatParameter, new Float(101.0f), new Short((short)101), false, 7);
        run_ShortQuery(Short_filterL, doubleParameter, new Double(-10000.0), new Short((short)-10000), true, 9);
        run_ShortQuery(Short_filterR, doubleParameter, new Double(-10000.0), new Short((short)-10000), false, 2);
        run_ShortQuery(Short_filterL, DoubleParameter, new Double(101.0), new Short((short)101), true, 3);
        run_ShortQuery(Short_filterR, DoubleParameter, new Double(23.0), new Short((short)23), false, 5);
        run_ShortQuery(Short_filterL, BigIntegerParameter, new BigInteger("10000"), new Short((short)10000), true, 2);
        run_ShortQuery(Short_filterR, BigIntegerParameter, new BigInteger("30000"), new Short((short)30000), false, 9);
        run_ShortQuery(Short_filterL, BigDecimalParameter, new BigDecimal("23.0"), new Short((short)23), true, 5);
        run_ShortQuery(Short_filterR, BigDecimalParameter, new BigDecimal("23.0"), new Short((short)23), false, 5);
        Short sval = new Short((short)100);
        alltypes.setShort(sval);
        run_ShortQuery(Short_filterObj, AllTypesParameter, alltypes, sval, false, 7);
        sval = new Short((short)23);
        alltypes.setShort(sval);
        run_ShortQuery(Short_filterObj, AllTypesParameter, alltypes, sval, false, 5);
        run_ShortQuery(Short_filterVal, null, null, new Short((short)-1000), true, 8);
    
        run_CharacterQuery(Character_filterL, charParameter, new Character(Character.MIN_VALUE), new Character(Character.MIN_VALUE), true, 10);
        run_CharacterQuery(Character_filterR, charParameter, new Character(Character.MAX_VALUE), new Character(Character.MAX_VALUE), false, 10);
        run_CharacterQuery(Character_filterL, charParameter, new Character('C'), new Character('C'), true, 6);
        run_CharacterQuery(Character_filterR, charParameter, new Character('z'), new Character('z'), false, 9);
        run_CharacterQuery(Character_filterL, CharacterParameter, new Character(' '), new Character(' '), true, 9);
        run_CharacterQuery(Character_filterR, CharacterParameter, new Character('f'), new Character('f'), false, 7);
        run_CharacterQuery(Character_filterL, byteParameter, new Byte((byte)Character.MIN_VALUE), new Character((char)Character.MIN_VALUE), true, 10);
        run_CharacterQuery(Character_filterR, ByteParameter, new Byte((byte)'a'), new Character('a'), false, 7);
        run_CharacterQuery(Character_filterL, shortParameter, new Short((short)'M'), new Character('M'), true, 5);
        run_CharacterQuery(Character_filterR, shortParameter, new Short((short)'F'), new Character('F'), false, 5);
        run_CharacterQuery(Character_filterL, ShortParameter, new Short((short)'A'), new Character('A'), true, 8);
        run_CharacterQuery(Character_filterR, ShortParameter, new Short((short)'A'), new Character('A'), false, 3);
        run_CharacterQuery(Character_filterL, intParameter, new Integer('z'), new Character('z'), true, 3);
        run_CharacterQuery(Character_filterR, intParameter, new Integer('z'), new Character('z'), false, 9);
        run_CharacterQuery(Character_filterL, IntegerParameter, new Integer('B'), new Character('B'), true, 7);
        run_CharacterQuery(Character_filterR, IntegerParameter, new Integer('B'), new Character('B'), false, 4);
        run_CharacterQuery(Character_filterL, longParameter, new Long('z'), new Character('z'), true, 3);
        run_CharacterQuery(Character_filterR, longParameter, new Long('z'), new Character('z'), false, 9);
        run_CharacterQuery(Character_filterL, LongParameter, new Long('B'), new Character('B'), true, 7);
        run_CharacterQuery(Character_filterR, LongParameter, new Long('B'), new Character('B'), false, 4);
        run_CharacterQuery(Character_filterL, floatParameter, new Float('z'), new Character('z'), true, 3);
        run_CharacterQuery(Character_filterR, floatParameter, new Float('z'), new Character('z'), false, 9);
        run_CharacterQuery(Character_filterL, FloatParameter, new Float('M'), new Character('M'), true, 5);
        run_CharacterQuery(Character_filterR, FloatParameter, new Float('X'), new Character('X'), false, 7);
        run_CharacterQuery(Character_filterL, doubleParameter, new Double('B'), new Character('B'), true, 7);
        run_CharacterQuery(Character_filterR, doubleParameter, new Double('B'), new Character('B'), false, 4);
        run_CharacterQuery(Character_filterL, DoubleParameter, new Double('A'), new Character('A'), true, 8);
        run_CharacterQuery(Character_filterR, DoubleParameter, new Double('A'), new Character('A'), false, 3);
        run_CharacterQuery(Character_filterL, BigIntegerParameter, new BigInteger("65"), new Character('A'), true, 8);  // 'A' == 65
        run_CharacterQuery(Character_filterR, BigIntegerParameter, new BigInteger("122"), new Character('z'), false, 9);  // 'z' == 122
        run_CharacterQuery(Character_filterL, BigDecimalParameter, new BigDecimal("65.00000"), new Character('A'), true, 8);
        run_CharacterQuery(Character_filterR, BigDecimalParameter, new BigDecimal("77.0000"), new Character('M'), false, 7); // 'M' == 77
        alltypes.setCharacter(new Character('A'));
        run_CharacterQuery(Character_filterObj, AllTypesParameter, alltypes, new Character('A'), true, 8);
        alltypes.setCharacter(new Character('b'));
        run_CharacterQuery(Character_filterObj, AllTypesParameter, alltypes, new Character('b'), true, 3);
        run_CharacterQuery(Character_filterVal, null, null, new Character('z'), false, 9);
    
        run_IntegerQuery(Integer_filterL, intParameter, new Integer(AllTypes.veryLargeNegativeInt), new Integer(AllTypes.veryLargeNegativeInt), true, 10);
        run_IntegerQuery(Integer_filterR, intParameter, new Integer(AllTypes.veryLargePositiveInt), new Integer(AllTypes.veryLargePositiveInt), false, 10);
        run_IntegerQuery(Integer_filterR, intParameter, new Integer(23), new Integer(23), false, 4);
        run_IntegerQuery(Integer_filterL, IntegerParameter, new Integer(1000000), new Integer(1000000), true, 2);
        run_IntegerQuery(Integer_filterR, IntegerParameter, new Integer(1000), new Integer(1000), false, 7);
        run_IntegerQuery(Integer_filterL, byteParameter, new Byte((byte)100), new Integer(100), true, 6);
        run_IntegerQuery(Integer_filterR, byteParameter, new Byte((byte)0), new Integer(0), false, 4);
        run_IntegerQuery(Integer_filterL, ByteParameter, new Byte((byte)100), new Integer(100), true, 6);
        run_IntegerQuery(Integer_filterR, ByteParameter, new Byte((byte)0), new Integer(0), false, 4);
        run_IntegerQuery(Integer_filterL, shortParameter, new Short((short)10000), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, shortParameter, new Short((short)-1000), new Integer(-1000), false, 3);
        run_IntegerQuery(Integer_filterL, ShortParameter, new Short((short)-1000), new Integer(-1000), true, 8);
        run_IntegerQuery(Integer_filterR, ShortParameter, new Short((short)-999), new Integer(-999), false, 3);
        run_IntegerQuery(Integer_filterL, charParameter, new Character((char)10000), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, charParameter, new Character((char)10000), new Integer(10000), false, 8);
        run_IntegerQuery(Integer_filterL, CharacterParameter, new Character((char)100), new Integer(100), true, 6);
        run_IntegerQuery(Integer_filterR, CharacterParameter, new Character((char)10000), new Integer(10000), false, 8);
        run_IntegerQuery(Integer_filterL, longParameter, new Long(AllTypes.veryLargePositiveInt), new Integer(AllTypes.veryLargePositiveInt), true, 1);
        run_IntegerQuery(Integer_filterR, longParameter, new Long(AllTypes.veryLargeNegativeInt), new Integer(AllTypes.veryLargeNegativeInt), false, 1);
        run_IntegerQuery(Integer_filterL, LongParameter, new Long(10000), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, LongParameter, new Long(43), new Integer(43), false, 4);
        run_IntegerQuery(Integer_filterL, floatParameter, new Float(AllTypes.veryLargePositiveInt), new Integer(AllTypes.veryLargePositiveInt), true, 1);
        run_IntegerQuery(Integer_filterR, floatParameter, new Float(AllTypes.veryLargeNegativeInt), new Integer(AllTypes.veryLargeNegativeInt), false, 1);
        run_IntegerQuery(Integer_filterL, FloatParameter, new Float(10000), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, FloatParameter, new Float(43), new Integer(43), false, 4);
        run_IntegerQuery(Integer_filterL, doubleParameter, new Double(AllTypes.veryLargePositiveInt), new Integer(AllTypes.veryLargePositiveInt), true, 1);
        run_IntegerQuery(Integer_filterR, doubleParameter, new Double(AllTypes.veryLargeNegativeInt), new Integer(AllTypes.veryLargeNegativeInt), false, 1);
        run_IntegerQuery(Integer_filterL, DoubleParameter, new Double(10000), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, DoubleParameter, new Double(1000001.0), new Integer(1000001), false, 9);
        run_IntegerQuery(Integer_filterL, BigIntegerParameter, new BigInteger("1000000"), new Integer(1000000), true, 2);
        run_IntegerQuery(Integer_filterR, BigIntegerParameter, new BigInteger("1000"), new Integer(1000), false, 7);
        run_IntegerQuery(Integer_filterL, BigDecimalParameter, new BigDecimal("10000.0"), new Integer(10000), true, 3);
        run_IntegerQuery(Integer_filterR, BigDecimalParameter, new BigDecimal("43.0"), new Integer(43), false, 4);
        alltypes.setInteger(new Integer(100));
        run_IntegerQuery(Integer_filterObj, AllTypesParameter, alltypes, new Integer(100), false, 6);
        run_IntegerQuery(Integer_filterVal, null, null, new Integer(100), false, 6);
    
        run_LongQuery(Long_filterL, longParameter, new Long(Long.MIN_VALUE), new Long(Long.MIN_VALUE), true, 10);
        run_LongQuery(Long_filterR, longParameter, new Long(Long.MAX_VALUE), new Long(Long.MAX_VALUE), false, 10);
        run_LongQuery(Long_filterL, LongParameter, new Long(100), new Long(100), true, 5);
        run_LongQuery(Long_filterR, LongParameter, new Long(23), new Long(23), false, 5);
        run_LongQuery(Long_filterL, byteParameter, new Byte((byte)100), new Long(100), true, 5);
        run_LongQuery(Long_filterR, byteParameter, new Byte((byte)0), new Long(0), false, 5);
        run_LongQuery(Long_filterL, ByteParameter, new Byte((byte)100), new Long(100), true, 5);
        run_LongQuery(Long_filterR, ByteParameter, new Byte((byte)0), new Long(0), false, 5);
        run_LongQuery(Long_filterL, shortParameter, new Short((short)-1000 ), new Long(-1000), true, 8);
        run_LongQuery(Long_filterR, shortParameter, new Short((short)1000), new Long(1000), false, 8);
        run_LongQuery(Long_filterL, ShortParameter, new Short((short)101), new Long(101), true, 3);
        run_LongQuery(Long_filterR, ShortParameter, new Short((short)32), new Long(32), false, 5);
        run_LongQuery(Long_filterL, charParameter, new Character((char)0), new Long(0), true, 6);
        run_LongQuery(Long_filterR, charParameter, new Character((char)100), new Long(100), false, 7);
        run_LongQuery(Long_filterL, CharacterParameter, new Character((char)23), new Long(23), true, 5);
        run_LongQuery(Long_filterR, CharacterParameter, new Character((char)1110), new Long(1110), false, 8);
        run_LongQuery(Long_filterL, intParameter, new Integer(100), new Long(100), true, 5);
        run_LongQuery(Long_filterR, intParameter, new Integer(0), new Long(0), false, 5);
        run_LongQuery(Long_filterL, IntegerParameter, new Integer(23), new Long(23), true, 5);
        run_LongQuery(Long_filterR, IntegerParameter, new Integer(1000000), new Long(1000000), false, 9);
        run_LongQuery(Long_filterL, floatParameter, new Float(-1000000.0), new Long(-1000000), true, 9);
        run_LongQuery(Long_filterR, floatParameter, new Float((float)Long.MAX_VALUE), new Long(Long.MAX_VALUE), false, 10);
        run_LongQuery(Long_filterL, FloatParameter, new Float(100.0f), new Long(100), true, 5);
        run_LongQuery(Long_filterR, FloatParameter, new Float(32.0f), new Long(32), false, 5);
        run_LongQuery(Long_filterL, doubleParameter, new Double(-1000000.0), new Long(-1000000), true, 9);
        run_LongQuery(Long_filterR, doubleParameter, new Double((double)Long.MAX_VALUE), new Long(Long.MAX_VALUE), false, 10);
        run_LongQuery(Long_filterL, DoubleParameter, new Double(100.0), new Long(100), true, 5);
        run_LongQuery(Long_filterR, DoubleParameter, new Double(32.0), new Long(32), false, 5);
        run_LongQuery(Long_filterL, BigIntegerParameter, new BigInteger("23"),  new Long(23), true, 5);
        run_LongQuery(Long_filterR, BigIntegerParameter, new BigInteger("1000000"), new Long(1000000), false, 9);
        run_LongQuery(Long_filterL, BigDecimalParameter, new BigDecimal("100.0"), new Long(100), true, 5);
        run_LongQuery(Long_filterR, BigDecimalParameter, new BigDecimal("32.0"), new Long(32), false, 5);
        alltypes.setLong(new Long(100));
        run_LongQuery(Long_filterObj, AllTypesParameter, alltypes, new Long(100), true, 5);
        run_LongQuery(Long_filterVal, null, null, new Long(-1000), true, 8);
    
        run_FloatQuery(Float_filterL, floatParameter, new Float(-1000000000.0f), new Float(-1000000000.0f), true, 9);
        run_FloatQuery(Float_filterR, floatParameter, new Float(100.0f), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, FloatParameter, new Float(0.0), new Float(0.0f), true, 8);
        run_FloatQuery(Float_filterR, FloatParameter, new Float(4.0), new Float(4.0f), false, 3);
        run_FloatQuery(Float_filterL, byteParameter, new Byte((byte)0), new Float(0.0f), true, 8);
        run_FloatQuery(Float_filterR, byteParameter, new Byte((byte)23), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, ByteParameter, new Byte((byte)34), new Float(34.0f), true, 7);
        run_FloatQuery(Float_filterR, ByteParameter, new Byte((byte)100), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, shortParameter, new Short((short)0), new Float(0.0f), true, 8);
        run_FloatQuery(Float_filterR, shortParameter, new Short((short)23), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, ShortParameter, new Short((short)34), new Float(34.0f), true, 7);
        run_FloatQuery(Float_filterR, ShortParameter, new Short((short)100), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, charParameter, new Character((char)0), new Float(0.0f), true, 8);
        run_FloatQuery(Float_filterR, charParameter, new Character((char)23), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, CharacterParameter, new Character((char)34), new Float(34.0f), true, 7);
        run_FloatQuery(Float_filterR, CharacterParameter, new Character((char)100), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, intParameter, new Integer(50000000), new Float(50000000.0f), true, 3);
        run_FloatQuery(Float_filterR, intParameter, new Integer(23), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, IntegerParameter, new Integer(34), new Float(34.0f), true, 7);
        run_FloatQuery(Float_filterR, IntegerParameter, new Integer(100), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, longParameter, new Long(50000000), new Float(50000000.0f), true, 3);
        run_FloatQuery(Float_filterR, longParameter, new Long(23), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, LongParameter, new Long(34), new Float(34.0f), true, 7);
        run_FloatQuery(Float_filterR, LongParameter, new Long(100), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, doubleParameter, new Double(50000000.0f), new Float(50000000.0f), true, 3);
        run_FloatQuery(Float_filterR, doubleParameter, new Double(100.0f), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, DoubleParameter, new Double(0.0f), new Float(0.0f), true, 8);
        run_FloatQuery(Float_filterR, DoubleParameter, new Double(100.0f), new Float(100.0f), false, 5);
        run_FloatQuery(Float_filterL, BigIntegerParameter, new BigInteger("55000000"), new Float(55000000.0f), true, 2);
        run_FloatQuery(Float_filterR, BigIntegerParameter, new BigInteger("23"), new Float(23.0f), false, 3);
        run_FloatQuery(Float_filterL, BigDecimalParameter, new BigDecimal("55000000.0"), new Float(55000000.0f), true, 2);
        run_FloatQuery(Float_filterR, BigDecimalParameter, new BigDecimal("-20.5"), new Float(-20.5f), false, 2);
        alltypes.setFloat(new Float(23.23f));
        run_FloatQuery(Float_filterObj, AllTypesParameter, alltypes, new Float(23.23f), false, 3);
        run_FloatQuery(Float_filterVal, null, null, new Float(100.0f), true, 7);
   
        run_DoubleQuery(Double_filterL, doubleParameter, new Double(-999999999999.0), new Double(-999999999999.0), true, 9);
        run_DoubleQuery(Double_filterR, doubleParameter, new Double(9999999999999.0), new Double(9999999999999.0), false, 9);
        run_DoubleQuery(Double_filterL, DoubleParameter, new Double(0.0), new Double(0.0), true, 7);
        run_DoubleQuery(Double_filterR, DoubleParameter, new Double(23.34), new Double(23.34), false, 4);
        run_DoubleQuery(Double_filterL, byteParameter, new Byte((byte)100), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, byteParameter, new Byte((byte)0), new Double(0.0), false, 4);
        run_DoubleQuery(Double_filterL, ByteParameter, new Byte((byte)-23), new Double(-23.0), true, 7);
        run_DoubleQuery(Double_filterR, ByteParameter, new Byte((byte)100), new Double(100.0), false, 6);
        run_DoubleQuery(Double_filterL, shortParameter, new Short((short)100), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, shortParameter, new Short((short)0), new Double(0.0), false, 4);
        run_DoubleQuery(Double_filterL, ShortParameter, new Short((short)23), new Double(23.0), true, 6);
        run_DoubleQuery(Double_filterR, ShortParameter, new Short((short)400), new Double(400.0), false, 7);
        run_DoubleQuery(Double_filterL, charParameter, new Character((char)100), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, charParameter, new Character((char)0), new Double(0.0), false, 4);
        run_DoubleQuery(Double_filterL, CharacterParameter, new Character((char)23), new Double(23.0), true, 6);
        run_DoubleQuery(Double_filterR, CharacterParameter, new Character((char)400), new Double(400.0), false, 7);
        run_DoubleQuery(Double_filterL, intParameter, new Integer(100), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, intParameter, new Integer(0), new Double(0.0), false, 4);
        run_DoubleQuery(Double_filterL, IntegerParameter, new Integer(23), new Double(23.0), true, 6);
        run_DoubleQuery(Double_filterR, IntegerParameter, new Integer(100), new Double(100.0), false, 6);
        run_DoubleQuery(Double_filterL, longParameter, new Long(100), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, longParameter, new Long(0), new Double(0.0), false, 4);
        run_DoubleQuery(Double_filterL, LongParameter, new Long(23), new Double(23.0), true, 6);
        run_DoubleQuery(Double_filterR, LongParameter, new Long(100), new Double(100.0), false, 6);
        run_DoubleQuery(Double_filterL, floatParameter, new Float(0.0f), new Double(0.0f), true, 7);
        run_DoubleQuery(Double_filterR, floatParameter, new Float(100.0f), new Double(100.0f), false, 6);
        run_DoubleQuery(Double_filterL, FloatParameter, new Float(100.0f), new Double(100.0f), true, 6);
        run_DoubleQuery(Double_filterR, FloatParameter, new Float(69.96f), new Double(69.96), false, 4);
        run_DoubleQuery(Double_filterL, BigIntegerParameter, new BigInteger("5000"), new Double(5000.0), true, 3);
        run_DoubleQuery(Double_filterR, BigIntegerParameter, new BigInteger("-20"), new Double(-20.0), false, 3);
        run_DoubleQuery(Double_filterL, BigDecimalParameter, new BigDecimal("100.0"), new Double(100.0), true, 6);
        run_DoubleQuery(Double_filterR, BigDecimalParameter, new BigDecimal("69.96"), new Double(69.96), false, 4);
        alltypes.setDouble(new Double(-999999999999.0));
        run_DoubleQuery(Double_filterObj, AllTypesParameter, alltypes, new Double(-999999999999.0), true, 9);
        run_DoubleQuery(Double_filterVal, null, null, new Double(100.0), false, 6);

        run_StringQuery(String_filterL, StringParameter, new String("JDO"), new String("JDO"), true, 9);
        run_StringQuery(String_filterR, StringParameter, new String("JDBC"), new String("JDBC"), false, 1);
        alltypes.setString(new String("abcde"));
        run_StringQuery(String_filterObj, AllTypesParameter, alltypes, new String("abcde"), true, 3);
        run_StringQuery(String_filterVal1, null, null, new String("Java"), false, 6);
        run_StringQuery(String_filterVal2, null, null, new String(""), false, 1);

        BigDecimal bd = new BigDecimal("100.0");
        run_BigDecimalQuery(BigDecimal_filterL, BigDecimalParameter, bd, bd, true, 6);
        bd = new BigDecimal("-234234.23333");
        run_BigDecimalQuery(BigDecimal_filterR, BigDecimalParameter, bd, bd, false, 2);
        bd = new BigDecimal("989899.33304953");
        run_BigDecimalQuery(BigDecimal_filterL, BigDecimalParameter, bd, bd, true, 3);
        bd = new BigDecimal("-1123123.22");
        run_BigDecimalQuery(BigDecimal_filterR, BigDecimalParameter, bd, bd, false, 1);
        alltypes.setBigDecimal(bd);
        run_BigDecimalQuery(BigDecimal_filterObj, AllTypesParameter, alltypes, bd, true, 10);

        BigInteger bi = new BigInteger("987034534985043985");
        run_BigIntegerQuery(BigInteger_filterL, BigIntegerParameter, bi, bi, true, 1);
        bi = new BigInteger("-999999999999999999");
        run_BigIntegerQuery(BigInteger_filterR, BigIntegerParameter, bi, bi, false, 1);
        bi = new BigInteger("-9999999999999999999");
        run_BigIntegerQuery(BigInteger_filterR, BigIntegerParameter, bi, bi, false, 0);
        bi = new BigInteger("1333330");
        alltypes.setBigInteger(bi);
        run_BigIntegerQuery(BigInteger_filterObj, AllTypesParameter, alltypes, bi, false, 8);
    
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.UK);
        gc.clear();
        gc.set(1999, Calendar.DECEMBER, 31, 9, 0, 0);
        Date d = gc.getTime();
        run_DateQuery(Date_filterL, DateParameter, d, d, true, 3);
        gc.set(1992, Calendar.NOVEMBER, 22, 9, 0, 0);
        d = gc.getTime();
        run_DateQuery(Date_filterR, DateParameter, d, d, false, 6);
        gc.set(1959, Calendar.OCTOBER, 9, 9, 0, 0);
        d = gc.getTime();
        run_DateQuery(Date_filterR, DateParameter, d, d, false, 3);
        gc.set(1995, Calendar.JUNE, 14, 9, 0, 0);
        d = gc.getTime();
        alltypes.setDate(d);
        run_DateQuery(Date_filterObj, AllTypesParameter, alltypes, d, false, 7);

    }

    private void run_byteQuery(String filter, String parameter, Object parameterValue,
                               byte value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_shortQuery(String filter, String parameter, Object parameterValue,
                                short value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_charQuery(String filter, String parameter, Object parameterValue,
                               char value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_intQuery(String filter, String parameter, Object parameterValue,
                              int value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_longQuery(String filter, String parameter, Object parameterValue,
                               long value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_floatQuery(String filter, String parameter, Object parameterValue,
                                float value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_doubleQuery(String filter, String parameter, Object parameterValue,
                                 double value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? value <= val : val <= value;
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_ByteQuery(String filter, String parameter, Object parameterValue,
                               Byte value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_ShortQuery(String filter, String parameter, Object parameterValue,
                                Short value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_CharacterQuery(String filter, String parameter, Object parameterValue,
                                    Character value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_IntegerQuery(String filter, String parameter, Object parameterValue,
                                  Integer value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_LongQuery(String filter, String parameter, Object parameterValue,
                               Long value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_FloatQuery(String filter, String parameter, Object parameterValue,
                                Float value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_DoubleQuery(String filter, String parameter, Object parameterValue,
                                 Double value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_StringQuery(String filter, String parameter, Object parameterValue,
                                 String value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_BigDecimalQuery(String filter, String parameter, Object parameterValue,
                                     BigDecimal value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_BigIntegerQuery(String filter, String parameter, Object parameterValue,
                                     BigInteger value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    private void run_DateQuery(String filter, String parameter, Object parameterValue,
                               Date value, boolean valueOnLeft, int expected_count)
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
            boolean correct_value = valueOnLeft ? (value.compareTo(val) <= 0) : (val.compareTo(value) <= 0);
            if( !correct_value ){
                fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns incorrect value, retrieved value: " + val + ", with parameter value: " + value, filter, parameter);
            }
            cnt++;
        }
        if( cnt != expected_count ){
            fail(ASSERTION_FAILED, "JDOQL LessThanOrEqual test returns wrong number of instances, expected " + expected_count + ", got " + cnt, filter, parameter);
        }
        query.close(query_result);
        tx.rollback();
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(AllTypes.class);
        AllTypes.load(getPM());
    }
}
