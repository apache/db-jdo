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
 
package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Hashtable;

public class HashtableStringValueCollections { 
  public int identifier;
  public Hashtable<Object, String> HashtableOfObject_String0;
  public Hashtable<Object, String> HashtableOfObject_String1;
  public Hashtable<Object, String> HashtableOfObject_String2;
  public Hashtable<Object, String> HashtableOfObject_String3;
  public Hashtable<Object, String> HashtableOfObject_String4;
  public Hashtable<Object, String> HashtableOfObject_String6;
  public Hashtable<Object, String> HashtableOfObject_String8;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String9;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String10;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String11;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String12;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String13;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String15;
  public Hashtable<SimpleClass, String> HashtableOfSimpleClass_String17;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String18;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String19;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String20;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String21;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String22;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String24;
  public Hashtable<SimpleInterface, String> HashtableOfSimpleInterface_String26;
  public Hashtable<String, String> HashtableOfString_String27;
  public Hashtable<String, String> HashtableOfString_String28;
  public Hashtable<String, String> HashtableOfString_String29;
  public Hashtable<String, String> HashtableOfString_String30;
  public Hashtable<String, String> HashtableOfString_String31;
  public Hashtable<String, String> HashtableOfString_String33;
  public Hashtable<String, String> HashtableOfString_String35;
  public Hashtable<Date, String> HashtableOfDate_String36;
  public Hashtable<Date, String> HashtableOfDate_String37;
  public Hashtable<Date, String> HashtableOfDate_String38;
  public Hashtable<Date, String> HashtableOfDate_String39;
  public Hashtable<Date, String> HashtableOfDate_String40;
  public Hashtable<Date, String> HashtableOfDate_String42;
  public Hashtable<Date, String> HashtableOfDate_String44;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String45;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String46;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String47;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String48;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String49;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String51;
  public Hashtable<BigDecimal, String> HashtableOfBigDecimal_String53;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String54;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String55;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String56;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String57;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String58;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String60;
  public Hashtable<BigInteger, String> HashtableOfBigInteger_String62;
  public Hashtable<Byte, String> HashtableOfByte_String63;
  public Hashtable<Byte, String> HashtableOfByte_String64;
  public Hashtable<Byte, String> HashtableOfByte_String65;
  public Hashtable<Byte, String> HashtableOfByte_String66;
  public Hashtable<Byte, String> HashtableOfByte_String67;
  public Hashtable<Byte, String> HashtableOfByte_String69;
  public Hashtable<Byte, String> HashtableOfByte_String71;
  public Hashtable<Double, String> HashtableOfDouble_String72;
  public Hashtable<Double, String> HashtableOfDouble_String73;
  public Hashtable<Double, String> HashtableOfDouble_String74;
  public Hashtable<Double, String> HashtableOfDouble_String75;
  public Hashtable<Double, String> HashtableOfDouble_String76;
  public Hashtable<Double, String> HashtableOfDouble_String78;
  public Hashtable<Double, String> HashtableOfDouble_String80;
  public Hashtable<Float, String> HashtableOfFloat_String81;
  public Hashtable<Float, String> HashtableOfFloat_String82;
  public Hashtable<Float, String> HashtableOfFloat_String83;
  public Hashtable<Float, String> HashtableOfFloat_String84;
  public Hashtable<Float, String> HashtableOfFloat_String85;
  public Hashtable<Float, String> HashtableOfFloat_String87;
  public Hashtable<Float, String> HashtableOfFloat_String89;
  public Hashtable<Integer, String> HashtableOfInteger_String90;
  public Hashtable<Integer, String> HashtableOfInteger_String91;
  public Hashtable<Integer, String> HashtableOfInteger_String92;
  public Hashtable<Integer, String> HashtableOfInteger_String93;
  public Hashtable<Integer, String> HashtableOfInteger_String94;
  public Hashtable<Integer, String> HashtableOfInteger_String96;
  public Hashtable<Integer, String> HashtableOfInteger_String98;
  public Hashtable<Long, String> HashtableOfLong_String99;
  public Hashtable<Long, String> HashtableOfLong_String100;
  public Hashtable<Long, String> HashtableOfLong_String101;
  public Hashtable<Long, String> HashtableOfLong_String102;
  public Hashtable<Long, String> HashtableOfLong_String103;
  public Hashtable<Long, String> HashtableOfLong_String105;
  public Hashtable<Long, String> HashtableOfLong_String107;
  public Hashtable<Short, String> HashtableOfShort_String108;
  public Hashtable<Short, String> HashtableOfShort_String109;
  public Hashtable<Short, String> HashtableOfShort_String110;
  public Hashtable<Short, String> HashtableOfShort_String111;
  public Hashtable<Short, String> HashtableOfShort_String112;
  public Hashtable<Short, String> HashtableOfShort_String114;
  public Hashtable<Short, String> HashtableOfShort_String116;

  public static final String [] fieldSpecs = { 
  "public Hashtable HashtableOfObject_String0",
  "embedded-value=true public Hashtable HashtableOfObject_String1",
  "embedded-value=false public Hashtable HashtableOfObject_String2",
  "embedded-key=true  public Hashtable HashtableOfObject_String3",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfObject_String4",
  "embedded-key=false  public Hashtable HashtableOfObject_String6",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfObject_String8",
  "public Hashtable HashtableOfSimpleClass_String9",
  "embedded-value=true public Hashtable HashtableOfSimpleClass_String10",
  "embedded-value=false public Hashtable HashtableOfSimpleClass_String11",
  "embedded-key=true  public Hashtable HashtableOfSimpleClass_String12",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfSimpleClass_String13",
  "embedded-key=false  public Hashtable HashtableOfSimpleClass_String15",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfSimpleClass_String17",
  "public Hashtable HashtableOfSimpleInterface_String18",
  "embedded-value=true public Hashtable HashtableOfSimpleInterface_String19",
  "embedded-value=false public Hashtable HashtableOfSimpleInterface_String20",
  "embedded-key=true  public Hashtable HashtableOfSimpleInterface_String21",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfSimpleInterface_String22",
  "embedded-key=false  public Hashtable HashtableOfSimpleInterface_String24",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfSimpleInterface_String26",
  "public Hashtable HashtableOfString_String27",
  "embedded-value=true public Hashtable HashtableOfString_String28",
  "embedded-value=false public Hashtable HashtableOfString_String29",
  "embedded-key=true  public Hashtable HashtableOfString_String30",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfString_String31",
  "embedded-key=false  public Hashtable HashtableOfString_String33",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfString_String35",
  "public Hashtable HashtableOfDate_String36",
  "embedded-value=true public Hashtable HashtableOfDate_String37",
  "embedded-value=false public Hashtable HashtableOfDate_String38",
  "embedded-key=true  public Hashtable HashtableOfDate_String39",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfDate_String40",
  "embedded-key=false  public Hashtable HashtableOfDate_String42",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfDate_String44",
  "public Hashtable HashtableOfBigDecimal_String45",
  "embedded-value=true public Hashtable HashtableOfBigDecimal_String46",
  "embedded-value=false public Hashtable HashtableOfBigDecimal_String47",
  "embedded-key=true  public Hashtable HashtableOfBigDecimal_String48",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfBigDecimal_String49",
  "embedded-key=false  public Hashtable HashtableOfBigDecimal_String51",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfBigDecimal_String53",
  "public Hashtable HashtableOfBigInteger_String54",
  "embedded-value=true public Hashtable HashtableOfBigInteger_String55",
  "embedded-value=false public Hashtable HashtableOfBigInteger_String56",
  "embedded-key=true  public Hashtable HashtableOfBigInteger_String57",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfBigInteger_String58",
  "embedded-key=false  public Hashtable HashtableOfBigInteger_String60",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfBigInteger_String62",
  "public Hashtable HashtableOfByte_String63",
  "embedded-value=true public Hashtable HashtableOfByte_String64",
  "embedded-value=false public Hashtable HashtableOfByte_String65",
  "embedded-key=true  public Hashtable HashtableOfByte_String66",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfByte_String67",
  "embedded-key=false  public Hashtable HashtableOfByte_String69",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfByte_String71",
  "public Hashtable HashtableOfDouble_String72",
  "embedded-value=true public Hashtable HashtableOfDouble_String73",
  "embedded-value=false public Hashtable HashtableOfDouble_String74",
  "embedded-key=true  public Hashtable HashtableOfDouble_String75",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfDouble_String76",
  "embedded-key=false  public Hashtable HashtableOfDouble_String78",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfDouble_String80",
  "public Hashtable HashtableOfFloat_String81",
  "embedded-value=true public Hashtable HashtableOfFloat_String82",
  "embedded-value=false public Hashtable HashtableOfFloat_String83",
  "embedded-key=true  public Hashtable HashtableOfFloat_String84",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfFloat_String85",
  "embedded-key=false  public Hashtable HashtableOfFloat_String87",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfFloat_String89",
  "public Hashtable HashtableOfInteger_String90",
  "embedded-value=true public Hashtable HashtableOfInteger_String91",
  "embedded-value=false public Hashtable HashtableOfInteger_String92",
  "embedded-key=true  public Hashtable HashtableOfInteger_String93",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfInteger_String94",
  "embedded-key=false  public Hashtable HashtableOfInteger_String96",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfInteger_String98",
  "public Hashtable HashtableOfLong_String99",
  "embedded-value=true public Hashtable HashtableOfLong_String100",
  "embedded-value=false public Hashtable HashtableOfLong_String101",
  "embedded-key=true  public Hashtable HashtableOfLong_String102",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfLong_String103",
  "embedded-key=false  public Hashtable HashtableOfLong_String105",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfLong_String107",
  "public Hashtable HashtableOfShort_String108",
  "embedded-value=true public Hashtable HashtableOfShort_String109",
  "embedded-value=false public Hashtable HashtableOfShort_String110",
  "embedded-key=true  public Hashtable HashtableOfShort_String111",
  "embedded-key=true embedded-value=true public Hashtable HashtableOfShort_String112",
  "embedded-key=false  public Hashtable HashtableOfShort_String114",
  "embedded-key=false embedded-value=false public Hashtable HashtableOfShort_String116"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public Hashtable<?, ?> get(int index)
  {
    switch (index)
    {
      case(0):
        return HashtableOfObject_String0;
      case(1):
        return HashtableOfObject_String1;
      case(2):
        return HashtableOfObject_String2;
      case(3):
        return HashtableOfObject_String3;
      case(4):
        return HashtableOfObject_String4;
      case(5):
        return HashtableOfObject_String6;
      case(6):
        return HashtableOfObject_String8;
      case(7):
        return HashtableOfSimpleClass_String9;
      case(8):
        return HashtableOfSimpleClass_String10;
      case(9):
        return HashtableOfSimpleClass_String11;
      case(10):
        return HashtableOfSimpleClass_String12;
      case(11):
        return HashtableOfSimpleClass_String13;
      case(12):
        return HashtableOfSimpleClass_String15;
      case(13):
        return HashtableOfSimpleClass_String17;
      case(14):
        return HashtableOfSimpleInterface_String18;
      case(15):
        return HashtableOfSimpleInterface_String19;
      case(16):
        return HashtableOfSimpleInterface_String20;
      case(17):
        return HashtableOfSimpleInterface_String21;
      case(18):
        return HashtableOfSimpleInterface_String22;
      case(19):
        return HashtableOfSimpleInterface_String24;
      case(20):
        return HashtableOfSimpleInterface_String26;
      case(21):
        return HashtableOfString_String27;
      case(22):
        return HashtableOfString_String28;
      case(23):
        return HashtableOfString_String29;
      case(24):
        return HashtableOfString_String30;
      case(25):
        return HashtableOfString_String31;
      case(26):
        return HashtableOfString_String33;
      case(27):
        return HashtableOfString_String35;
      case(28):
        return HashtableOfDate_String36;
      case(29):
        return HashtableOfDate_String37;
      case(30):
        return HashtableOfDate_String38;
      case(31):
        return HashtableOfDate_String39;
      case(32):
        return HashtableOfDate_String40;
      case(33):
        return HashtableOfDate_String42;
      case(34):
        return HashtableOfDate_String44;
      case(35):
        return HashtableOfBigDecimal_String45;
      case(36):
        return HashtableOfBigDecimal_String46;
      case(37):
        return HashtableOfBigDecimal_String47;
      case(38):
        return HashtableOfBigDecimal_String48;
      case(39):
        return HashtableOfBigDecimal_String49;
      case(40):
        return HashtableOfBigDecimal_String51;
      case(41):
        return HashtableOfBigDecimal_String53;
      case(42):
        return HashtableOfBigInteger_String54;
      case(43):
        return HashtableOfBigInteger_String55;
      case(44):
        return HashtableOfBigInteger_String56;
      case(45):
        return HashtableOfBigInteger_String57;
      case(46):
        return HashtableOfBigInteger_String58;
      case(47):
        return HashtableOfBigInteger_String60;
      case(48):
        return HashtableOfBigInteger_String62;
      case(49):
        return HashtableOfByte_String63;
      case(50):
        return HashtableOfByte_String64;
      case(51):
        return HashtableOfByte_String65;
      case(52):
        return HashtableOfByte_String66;
      case(53):
        return HashtableOfByte_String67;
      case(54):
        return HashtableOfByte_String69;
      case(55):
        return HashtableOfByte_String71;
      case(56):
        return HashtableOfDouble_String72;
      case(57):
        return HashtableOfDouble_String73;
      case(58):
        return HashtableOfDouble_String74;
      case(59):
        return HashtableOfDouble_String75;
      case(60):
        return HashtableOfDouble_String76;
      case(61):
        return HashtableOfDouble_String78;
      case(62):
        return HashtableOfDouble_String80;
      case(63):
        return HashtableOfFloat_String81;
      case(64):
        return HashtableOfFloat_String82;
      case(65):
        return HashtableOfFloat_String83;
      case(66):
        return HashtableOfFloat_String84;
      case(67):
        return HashtableOfFloat_String85;
      case(68):
        return HashtableOfFloat_String87;
      case(69):
        return HashtableOfFloat_String89;
      case(70):
        return HashtableOfInteger_String90;
      case(71):
        return HashtableOfInteger_String91;
      case(72):
        return HashtableOfInteger_String92;
      case(73):
        return HashtableOfInteger_String93;
      case(74):
        return HashtableOfInteger_String94;
      case(75):
        return HashtableOfInteger_String96;
      case(76):
        return HashtableOfInteger_String98;
      case(77):
        return HashtableOfLong_String99;
      case(78):
        return HashtableOfLong_String100;
      case(79):
        return HashtableOfLong_String101;
      case(80):
        return HashtableOfLong_String102;
      case(81):
        return HashtableOfLong_String103;
      case(82):
        return HashtableOfLong_String105;
      case(83):
        return HashtableOfLong_String107;
      case(84):
        return HashtableOfShort_String108;
      case(85):
        return HashtableOfShort_String109;
      case(86):
        return HashtableOfShort_String110;
      case(87):
        return HashtableOfShort_String111;
      case(88):
        return HashtableOfShort_String112;
      case(89):
        return HashtableOfShort_String114;
      case(90):
        return HashtableOfShort_String116;
      default:
        throw new IndexOutOfBoundsException("Bad index " + index);
    }
  }
  public boolean set(int index,Hashtable value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        HashtableOfObject_String0= value;
         break;
      case(1):
        HashtableOfObject_String1= value;
         break;
      case(2):
        HashtableOfObject_String2= value;
         break;
      case(3):
        HashtableOfObject_String3= value;
         break;
      case(4):
        HashtableOfObject_String4= value;
         break;
      case(5):
        HashtableOfObject_String6= value;
         break;
      case(6):
        HashtableOfObject_String8= value;
         break;
      case(7):
        HashtableOfSimpleClass_String9= value;
         break;
      case(8):
        HashtableOfSimpleClass_String10= value;
         break;
      case(9):
        HashtableOfSimpleClass_String11= value;
         break;
      case(10):
        HashtableOfSimpleClass_String12= value;
         break;
      case(11):
        HashtableOfSimpleClass_String13= value;
         break;
      case(12):
        HashtableOfSimpleClass_String15= value;
         break;
      case(13):
        HashtableOfSimpleClass_String17= value;
         break;
      case(14):
        HashtableOfSimpleInterface_String18= value;
         break;
      case(15):
        HashtableOfSimpleInterface_String19= value;
         break;
      case(16):
        HashtableOfSimpleInterface_String20= value;
         break;
      case(17):
        HashtableOfSimpleInterface_String21= value;
         break;
      case(18):
        HashtableOfSimpleInterface_String22= value;
         break;
      case(19):
        HashtableOfSimpleInterface_String24= value;
         break;
      case(20):
        HashtableOfSimpleInterface_String26= value;
         break;
      case(21):
        HashtableOfString_String27= value;
         break;
      case(22):
        HashtableOfString_String28= value;
         break;
      case(23):
        HashtableOfString_String29= value;
         break;
      case(24):
        HashtableOfString_String30= value;
         break;
      case(25):
        HashtableOfString_String31= value;
         break;
      case(26):
        HashtableOfString_String33= value;
         break;
      case(27):
        HashtableOfString_String35= value;
         break;
      case(28):
        HashtableOfDate_String36= value;
         break;
      case(29):
        HashtableOfDate_String37= value;
         break;
      case(30):
        HashtableOfDate_String38= value;
         break;
      case(31):
        HashtableOfDate_String39= value;
         break;
      case(32):
        HashtableOfDate_String40= value;
         break;
      case(33):
        HashtableOfDate_String42= value;
         break;
      case(34):
        HashtableOfDate_String44= value;
         break;
      case(35):
        HashtableOfBigDecimal_String45= value;
         break;
      case(36):
        HashtableOfBigDecimal_String46= value;
         break;
      case(37):
        HashtableOfBigDecimal_String47= value;
         break;
      case(38):
        HashtableOfBigDecimal_String48= value;
         break;
      case(39):
        HashtableOfBigDecimal_String49= value;
         break;
      case(40):
        HashtableOfBigDecimal_String51= value;
         break;
      case(41):
        HashtableOfBigDecimal_String53= value;
         break;
      case(42):
        HashtableOfBigInteger_String54= value;
         break;
      case(43):
        HashtableOfBigInteger_String55= value;
         break;
      case(44):
        HashtableOfBigInteger_String56= value;
         break;
      case(45):
        HashtableOfBigInteger_String57= value;
         break;
      case(46):
        HashtableOfBigInteger_String58= value;
         break;
      case(47):
        HashtableOfBigInteger_String60= value;
         break;
      case(48):
        HashtableOfBigInteger_String62= value;
         break;
      case(49):
        HashtableOfByte_String63= value;
         break;
      case(50):
        HashtableOfByte_String64= value;
         break;
      case(51):
        HashtableOfByte_String65= value;
         break;
      case(52):
        HashtableOfByte_String66= value;
         break;
      case(53):
        HashtableOfByte_String67= value;
         break;
      case(54):
        HashtableOfByte_String69= value;
         break;
      case(55):
        HashtableOfByte_String71= value;
         break;
      case(56):
        HashtableOfDouble_String72= value;
         break;
      case(57):
        HashtableOfDouble_String73= value;
         break;
      case(58):
        HashtableOfDouble_String74= value;
         break;
      case(59):
        HashtableOfDouble_String75= value;
         break;
      case(60):
        HashtableOfDouble_String76= value;
         break;
      case(61):
        HashtableOfDouble_String78= value;
         break;
      case(62):
        HashtableOfDouble_String80= value;
         break;
      case(63):
        HashtableOfFloat_String81= value;
         break;
      case(64):
        HashtableOfFloat_String82= value;
         break;
      case(65):
        HashtableOfFloat_String83= value;
         break;
      case(66):
        HashtableOfFloat_String84= value;
         break;
      case(67):
        HashtableOfFloat_String85= value;
         break;
      case(68):
        HashtableOfFloat_String87= value;
         break;
      case(69):
        HashtableOfFloat_String89= value;
         break;
      case(70):
        HashtableOfInteger_String90= value;
         break;
      case(71):
        HashtableOfInteger_String91= value;
         break;
      case(72):
        HashtableOfInteger_String92= value;
         break;
      case(73):
        HashtableOfInteger_String93= value;
         break;
      case(74):
        HashtableOfInteger_String94= value;
         break;
      case(75):
        HashtableOfInteger_String96= value;
         break;
      case(76):
        HashtableOfInteger_String98= value;
         break;
      case(77):
        HashtableOfLong_String99= value;
         break;
      case(78):
        HashtableOfLong_String100= value;
         break;
      case(79):
        HashtableOfLong_String101= value;
         break;
      case(80):
        HashtableOfLong_String102= value;
         break;
      case(81):
        HashtableOfLong_String103= value;
         break;
      case(82):
        HashtableOfLong_String105= value;
         break;
      case(83):
        HashtableOfLong_String107= value;
         break;
      case(84):
        HashtableOfShort_String108= value;
         break;
      case(85):
        HashtableOfShort_String109= value;
         break;
      case(86):
        HashtableOfShort_String110= value;
         break;
      case(87):
        HashtableOfShort_String111= value;
         break;
      case(88):
        HashtableOfShort_String112= value;
         break;
      case(89):
        HashtableOfShort_String114= value;
         break;
      case(90):
        HashtableOfShort_String116= value;
         break;
      default:
        throw new IndexOutOfBoundsException("Bad index " + index);
    }
    return true;
  }

    public static class Oid implements Serializable {
        public int identifier;

        public Oid() {
        }

        public Oid(String s) { identifier = Integer.parseInt(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + identifier;}

        public int hashCode() { return (int)identifier ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.identifier == this.identifier;
            }
            return false;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

    }   
}
