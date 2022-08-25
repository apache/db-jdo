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
 
package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public class MapStringValueCollections { 
  public int identifier;
  public Map<Object, String> MapOfObject_String0;
  public Map<Object, String> MapOfObject_String1;
  public Map<Object, String> MapOfObject_String2;
  public Map<Object, String> MapOfObject_String3;
  public Map<Object, String> MapOfObject_String4;
  public Map<Object, String> MapOfObject_String6;
  public Map<Object, String> MapOfObject_String8;
  public Map<SimpleClass, String> MapOfSimpleClass_String9;
  public Map<SimpleClass, String> MapOfSimpleClass_String10;
  public Map<SimpleClass, String> MapOfSimpleClass_String11;
  public Map<SimpleClass, String> MapOfSimpleClass_String12;
  public Map<SimpleClass, String> MapOfSimpleClass_String13;
  public Map<SimpleClass, String> MapOfSimpleClass_String15;
  public Map<SimpleClass, String> MapOfSimpleClass_String17;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String18;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String19;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String20;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String21;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String22;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String24;
  public Map<SimpleInterface, String> MapOfSimpleInterface_String26;
  public Map<String, String> MapOfString_String27;
  public Map<String, String> MapOfString_String28;
  public Map<String, String> MapOfString_String29;
  public Map<String, String> MapOfString_String30;
  public Map<String, String> MapOfString_String31;
  public Map<String, String> MapOfString_String33;
  public Map<String, String> MapOfString_String35;
  public Map<Date, String> MapOfDate_String36;
  public Map<Date, String> MapOfDate_String37;
  public Map<Date, String> MapOfDate_String38;
  public Map<Date, String> MapOfDate_String39;
  public Map<Date, String> MapOfDate_String40;
  public Map<Date, String> MapOfDate_String42;
  public Map<Date, String> MapOfDate_String44;
  public Map<BigDecimal, String> MapOfBigDecimal_String45;
  public Map<BigDecimal, String> MapOfBigDecimal_String46;
  public Map<BigDecimal, String> MapOfBigDecimal_String47;
  public Map<BigDecimal, String> MapOfBigDecimal_String48;
  public Map<BigDecimal, String> MapOfBigDecimal_String49;
  public Map<BigDecimal, String> MapOfBigDecimal_String51;
  public Map<BigDecimal, String> MapOfBigDecimal_String53;
  public Map<BigInteger, String> MapOfBigInteger_String54;
  public Map<BigInteger, String> MapOfBigInteger_String55;
  public Map<BigInteger, String> MapOfBigInteger_String56;
  public Map<BigInteger, String> MapOfBigInteger_String57;
  public Map<BigInteger, String> MapOfBigInteger_String58;
  public Map<BigInteger, String> MapOfBigInteger_String60;
  public Map<BigInteger, String> MapOfBigInteger_String62;
  public Map<Byte, String> MapOfByte_String63;
  public Map<Byte, String> MapOfByte_String64;
  public Map<Byte, String> MapOfByte_String65;
  public Map<Byte, String> MapOfByte_String66;
  public Map<Byte, String> MapOfByte_String67;
  public Map<Byte, String> MapOfByte_String69;
  public Map<Byte, String> MapOfByte_String71;
  public Map<Double, String> MapOfDouble_String72;
  public Map<Double, String> MapOfDouble_String73;
  public Map<Double, String> MapOfDouble_String74;
  public Map<Double, String> MapOfDouble_String75;
  public Map<Double, String> MapOfDouble_String76;
  public Map<Double, String> MapOfDouble_String78;
  public Map<Double, String> MapOfDouble_String80;
  public Map<Float, String> MapOfFloat_String81;
  public Map<Float, String> MapOfFloat_String82;
  public Map<Float, String> MapOfFloat_String83;
  public Map<Float, String> MapOfFloat_String84;
  public Map<Float, String> MapOfFloat_String85;
  public Map<Float, String> MapOfFloat_String87;
  public Map<Float, String> MapOfFloat_String89;
  public Map<Integer, String> MapOfInteger_String90;
  public Map<Integer, String> MapOfInteger_String91;
  public Map<Integer, String> MapOfInteger_String92;
  public Map<Integer, String> MapOfInteger_String93;
  public Map<Integer, String> MapOfInteger_String94;
  public Map<Integer, String> MapOfInteger_String96;
  public Map<Integer, String> MapOfInteger_String98;
  public Map<Long, String> MapOfLong_String99;
  public Map<Long, String> MapOfLong_String100;
  public Map<Long, String> MapOfLong_String101;
  public Map<Long, String> MapOfLong_String102;
  public Map<Long, String> MapOfLong_String103;
  public Map<Long, String> MapOfLong_String105;
  public Map<Long, String> MapOfLong_String107;
  public Map<Short, String> MapOfShort_String108;
  public Map<Short, String> MapOfShort_String109;
  public Map<Short, String> MapOfShort_String110;
  public Map<Short, String> MapOfShort_String111;
  public Map<Short, String> MapOfShort_String112;
  public Map<Short, String> MapOfShort_String114;
  public Map<Short, String> MapOfShort_String116;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String117;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String118;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String119;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String120;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String121;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String122;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String123;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String124;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String125;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String126;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String127;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String128;
  public Map<SimpleEnum, String> MapOfSimpleEnum_String129;

  public static final String [] fieldSpecs = { 
  "public Map MapOfObject_String0",
  "embedded-value=true public Map MapOfObject_String1",
  "embedded-value=false public Map MapOfObject_String2",
  "embedded-key=true  public Map MapOfObject_String3",
  "embedded-key=true embedded-value=true public Map MapOfObject_String4",
  "embedded-key=false  public Map MapOfObject_String6",
  "embedded-key=false embedded-value=false public Map MapOfObject_String8",
  "public Map MapOfSimpleClass_String9",
  "embedded-value=true public Map MapOfSimpleClass_String10",
  "embedded-value=false public Map MapOfSimpleClass_String11",
  "embedded-key=true  public Map MapOfSimpleClass_String12",
  "embedded-key=true embedded-value=true public Map MapOfSimpleClass_String13",
  "embedded-key=false  public Map MapOfSimpleClass_String15",
  "embedded-key=false embedded-value=false public Map MapOfSimpleClass_String17",
  "public Map MapOfSimpleInterface_String18",
  "embedded-value=true public Map MapOfSimpleInterface_String19",
  "embedded-value=false public Map MapOfSimpleInterface_String20",
  "embedded-key=true  public Map MapOfSimpleInterface_String21",
  "embedded-key=true embedded-value=true public Map MapOfSimpleInterface_String22",
  "embedded-key=false  public Map MapOfSimpleInterface_String24",
  "embedded-key=false embedded-value=false public Map MapOfSimpleInterface_String26",
  "public Map MapOfString_String27",
  "embedded-value=true public Map MapOfString_String28",
  "embedded-value=false public Map MapOfString_String29",
  "embedded-key=true  public Map MapOfString_String30",
  "embedded-key=true embedded-value=true public Map MapOfString_String31",
  "embedded-key=false  public Map MapOfString_String33",
  "embedded-key=false embedded-value=false public Map MapOfString_String35",
  "public Map MapOfDate_String36",
  "embedded-value=true public Map MapOfDate_String37",
  "embedded-value=false public Map MapOfDate_String38",
  "embedded-key=true  public Map MapOfDate_String39",
  "embedded-key=true embedded-value=true public Map MapOfDate_String40",
  "embedded-key=false  public Map MapOfDate_String42",
  "embedded-key=false embedded-value=false public Map MapOfDate_String44",
  "public Map MapOfBigDecimal_String45",
  "embedded-value=true public Map MapOfBigDecimal_String46",
  "embedded-value=false public Map MapOfBigDecimal_String47",
  "embedded-key=true  public Map MapOfBigDecimal_String48",
  "embedded-key=true embedded-value=true public Map MapOfBigDecimal_String49",
  "embedded-key=false  public Map MapOfBigDecimal_String51",
  "embedded-key=false embedded-value=false public Map MapOfBigDecimal_String53",
  "public Map MapOfBigInteger_String54",
  "embedded-value=true public Map MapOfBigInteger_String55",
  "embedded-value=false public Map MapOfBigInteger_String56",
  "embedded-key=true  public Map MapOfBigInteger_String57",
  "embedded-key=true embedded-value=true public Map MapOfBigInteger_String58",
  "embedded-key=false  public Map MapOfBigInteger_String60",
  "embedded-key=false embedded-value=false public Map MapOfBigInteger_String62",
  "public Map MapOfByte_String63",
  "embedded-value=true public Map MapOfByte_String64",
  "embedded-value=false public Map MapOfByte_String65",
  "embedded-key=true  public Map MapOfByte_String66",
  "embedded-key=true embedded-value=true public Map MapOfByte_String67",
  "embedded-key=false  public Map MapOfByte_String69",
  "embedded-key=false embedded-value=false public Map MapOfByte_String71",
  "public Map MapOfDouble_String72",
  "embedded-value=true public Map MapOfDouble_String73",
  "embedded-value=false public Map MapOfDouble_String74",
  "embedded-key=true  public Map MapOfDouble_String75",
  "embedded-key=true embedded-value=true public Map MapOfDouble_String76",
  "embedded-key=false  public Map MapOfDouble_String78",
  "embedded-key=false embedded-value=false public Map MapOfDouble_String80",
  "public Map MapOfFloat_String81",
  "embedded-value=true public Map MapOfFloat_String82",
  "embedded-value=false public Map MapOfFloat_String83",
  "embedded-key=true  public Map MapOfFloat_String84",
  "embedded-key=true embedded-value=true public Map MapOfFloat_String85",
  "embedded-key=false  public Map MapOfFloat_String87",
  "embedded-key=false embedded-value=false public Map MapOfFloat_String89",
  "public Map MapOfInteger_String90",
  "embedded-value=true public Map MapOfInteger_String91",
  "embedded-value=false public Map MapOfInteger_String92",
  "embedded-key=true  public Map MapOfInteger_String93",
  "embedded-key=true embedded-value=true public Map MapOfInteger_String94",
  "embedded-key=false  public Map MapOfInteger_String96",
  "embedded-key=false embedded-value=false public Map MapOfInteger_String98",
  "public Map MapOfLong_String99",
  "embedded-value=true public Map MapOfLong_String100",
  "embedded-value=false public Map MapOfLong_String101",
  "embedded-key=true  public Map MapOfLong_String102",
  "embedded-key=true embedded-value=true public Map MapOfLong_String103",
  "embedded-key=false  public Map MapOfLong_String105",
  "embedded-key=false embedded-value=false public Map MapOfLong_String107",
  "public Map MapOfShort_String108",
  "embedded-value=true public Map MapOfShort_String109",
  "embedded-value=false public Map MapOfShort_String110",
  "embedded-key=true  public Map MapOfShort_String111",
  "embedded-key=true embedded-value=true public Map MapOfShort_String112",
  "embedded-key=false  public Map MapOfShort_String114",
  "embedded-key=false embedded-value=false public Map MapOfShort_String116",
  "public Map MapOfSimpleEnum_String117",
  "embedded-value=true public Map MapOfSimpleEnum_String118",
  "embedded-value=false public Map MapOfSimpleEnum_String119",
  "embedded-key=true  public Map MapOfSimpleEnum_String120",
  "embedded-key=true embedded-value=true public Map MapOfSimpleEnum_String121",
  "embedded-key=false  public Map MapOfSimpleEnum_String122",
  "embedded-key=false embedded-value=false public Map MapOfSimpleEnum_String123",
  "public Map MapOfSimpleEnum_String124",
  "embedded-value=true public Map MapOfSimpleEnum_String124",
  "embedded-value=false public Map MapOfSimpleEnum_String126",
  "embedded-key=true  public Map MapOfSimpleEnum_String127",
  "embedded-key=true embedded-value=true public Map MapOfSimpleEnum_String128",
  "embedded-key=false  public Map MapOfSimpleEnum_String129"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public Map<?, ?> get(int index)
  {
    switch (index)
    {
      case(0):
        return MapOfObject_String0;
      case(1):
        return MapOfObject_String1;
      case(2):
        return MapOfObject_String2;
      case(3):
        return MapOfObject_String3;
      case(4):
        return MapOfObject_String4;
      case(5):
        return MapOfObject_String6;
      case(6):
        return MapOfObject_String8;
      case(7):
        return MapOfSimpleClass_String9;
      case(8):
        return MapOfSimpleClass_String10;
      case(9):
        return MapOfSimpleClass_String11;
      case(10):
        return MapOfSimpleClass_String12;
      case(11):
        return MapOfSimpleClass_String13;
      case(12):
        return MapOfSimpleClass_String15;
      case(13):
        return MapOfSimpleClass_String17;
      case(14):
        return MapOfSimpleInterface_String18;
      case(15):
        return MapOfSimpleInterface_String19;
      case(16):
        return MapOfSimpleInterface_String20;
      case(17):
        return MapOfSimpleInterface_String21;
      case(18):
        return MapOfSimpleInterface_String22;
      case(19):
        return MapOfSimpleInterface_String24;
      case(20):
        return MapOfSimpleInterface_String26;
      case(21):
        return MapOfString_String27;
      case(22):
        return MapOfString_String28;
      case(23):
        return MapOfString_String29;
      case(24):
        return MapOfString_String30;
      case(25):
        return MapOfString_String31;
      case(26):
        return MapOfString_String33;
      case(27):
        return MapOfString_String35;
      case(28):
        return MapOfDate_String36;
      case(29):
        return MapOfDate_String37;
      case(30):
        return MapOfDate_String38;
      case(31):
        return MapOfDate_String39;
      case(32):
        return MapOfDate_String40;
      case(33):
        return MapOfDate_String42;
      case(34):
        return MapOfDate_String44;
      case(35):
        return MapOfBigDecimal_String45;
      case(36):
        return MapOfBigDecimal_String46;
      case(37):
        return MapOfBigDecimal_String47;
      case(38):
        return MapOfBigDecimal_String48;
      case(39):
        return MapOfBigDecimal_String49;
      case(40):
        return MapOfBigDecimal_String51;
      case(41):
        return MapOfBigDecimal_String53;
      case(42):
        return MapOfBigInteger_String54;
      case(43):
        return MapOfBigInteger_String55;
      case(44):
        return MapOfBigInteger_String56;
      case(45):
        return MapOfBigInteger_String57;
      case(46):
        return MapOfBigInteger_String58;
      case(47):
        return MapOfBigInteger_String60;
      case(48):
        return MapOfBigInteger_String62;
      case(49):
        return MapOfByte_String63;
      case(50):
        return MapOfByte_String64;
      case(51):
        return MapOfByte_String65;
      case(52):
        return MapOfByte_String66;
      case(53):
        return MapOfByte_String67;
      case(54):
        return MapOfByte_String69;
      case(55):
        return MapOfByte_String71;
      case(56):
        return MapOfDouble_String72;
      case(57):
        return MapOfDouble_String73;
      case(58):
        return MapOfDouble_String74;
      case(59):
        return MapOfDouble_String75;
      case(60):
        return MapOfDouble_String76;
      case(61):
        return MapOfDouble_String78;
      case(62):
        return MapOfDouble_String80;
      case(63):
        return MapOfFloat_String81;
      case(64):
        return MapOfFloat_String82;
      case(65):
        return MapOfFloat_String83;
      case(66):
        return MapOfFloat_String84;
      case(67):
        return MapOfFloat_String85;
      case(68):
        return MapOfFloat_String87;
      case(69):
        return MapOfFloat_String89;
      case(70):
        return MapOfInteger_String90;
      case(71):
        return MapOfInteger_String91;
      case(72):
        return MapOfInteger_String92;
      case(73):
        return MapOfInteger_String93;
      case(74):
        return MapOfInteger_String94;
      case(75):
        return MapOfInteger_String96;
      case(76):
        return MapOfInteger_String98;
      case(77):
        return MapOfLong_String99;
      case(78):
        return MapOfLong_String100;
      case(79):
        return MapOfLong_String101;
      case(80):
        return MapOfLong_String102;
      case(81):
        return MapOfLong_String103;
      case(82):
        return MapOfLong_String105;
      case(83):
        return MapOfLong_String107;
      case(84):
        return MapOfShort_String108;
      case(85):
        return MapOfShort_String109;
      case(86):
        return MapOfShort_String110;
      case(87):
        return MapOfShort_String111;
      case(88):
        return MapOfShort_String112;
      case(89):
        return MapOfShort_String114;
      case(90):
        return MapOfShort_String116;
      case(91):
        return MapOfSimpleEnum_String117;
      case(92):
        return MapOfSimpleEnum_String118;
      case(93):
        return MapOfSimpleEnum_String119;
      case(94):
        return MapOfSimpleEnum_String120;
      case(95):
        return MapOfSimpleEnum_String121;
      case(96):
        return MapOfSimpleEnum_String122;
      case(97):
        return MapOfSimpleEnum_String123;
      case(98):
        return MapOfSimpleEnum_String124;
      case(99):
        return MapOfSimpleEnum_String125;
      case(100):
        return MapOfSimpleEnum_String126;
      case(101):
        return MapOfSimpleEnum_String127;
      case(102):
        return MapOfSimpleEnum_String128;
      case(103):
        return MapOfSimpleEnum_String129;
      default:
        throw new IndexOutOfBoundsException("Bad index " + index);
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") Map value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        MapOfObject_String0= value;
         break;
      case(1):
        MapOfObject_String1= value;
         break;
      case(2):
        MapOfObject_String2= value;
         break;
      case(3):
        MapOfObject_String3= value;
         break;
      case(4):
        MapOfObject_String4= value;
         break;
      case(5):
        MapOfObject_String6= value;
         break;
      case(6):
        MapOfObject_String8= value;
         break;
      case(7):
        MapOfSimpleClass_String9= value;
         break;
      case(8):
        MapOfSimpleClass_String10= value;
         break;
      case(9):
        MapOfSimpleClass_String11= value;
         break;
      case(10):
        MapOfSimpleClass_String12= value;
         break;
      case(11):
        MapOfSimpleClass_String13= value;
         break;
      case(12):
        MapOfSimpleClass_String15= value;
         break;
      case(13):
        MapOfSimpleClass_String17= value;
         break;
      case(14):
        MapOfSimpleInterface_String18= value;
         break;
      case(15):
        MapOfSimpleInterface_String19= value;
         break;
      case(16):
        MapOfSimpleInterface_String20= value;
         break;
      case(17):
        MapOfSimpleInterface_String21= value;
         break;
      case(18):
        MapOfSimpleInterface_String22= value;
         break;
      case(19):
        MapOfSimpleInterface_String24= value;
         break;
      case(20):
        MapOfSimpleInterface_String26= value;
         break;
      case(21):
        MapOfString_String27= value;
         break;
      case(22):
        MapOfString_String28= value;
         break;
      case(23):
        MapOfString_String29= value;
         break;
      case(24):
        MapOfString_String30= value;
         break;
      case(25):
        MapOfString_String31= value;
         break;
      case(26):
        MapOfString_String33= value;
         break;
      case(27):
        MapOfString_String35= value;
         break;
      case(28):
        MapOfDate_String36= value;
         break;
      case(29):
        MapOfDate_String37= value;
         break;
      case(30):
        MapOfDate_String38= value;
         break;
      case(31):
        MapOfDate_String39= value;
         break;
      case(32):
        MapOfDate_String40= value;
         break;
      case(33):
        MapOfDate_String42= value;
         break;
      case(34):
        MapOfDate_String44= value;
         break;
      case(35):
        MapOfBigDecimal_String45= value;
         break;
      case(36):
        MapOfBigDecimal_String46= value;
         break;
      case(37):
        MapOfBigDecimal_String47= value;
         break;
      case(38):
        MapOfBigDecimal_String48= value;
         break;
      case(39):
        MapOfBigDecimal_String49= value;
         break;
      case(40):
        MapOfBigDecimal_String51= value;
         break;
      case(41):
        MapOfBigDecimal_String53= value;
         break;
      case(42):
        MapOfBigInteger_String54= value;
         break;
      case(43):
        MapOfBigInteger_String55= value;
         break;
      case(44):
        MapOfBigInteger_String56= value;
         break;
      case(45):
        MapOfBigInteger_String57= value;
         break;
      case(46):
        MapOfBigInteger_String58= value;
         break;
      case(47):
        MapOfBigInteger_String60= value;
         break;
      case(48):
        MapOfBigInteger_String62= value;
         break;
      case(49):
        MapOfByte_String63= value;
         break;
      case(50):
        MapOfByte_String64= value;
         break;
      case(51):
        MapOfByte_String65= value;
         break;
      case(52):
        MapOfByte_String66= value;
         break;
      case(53):
        MapOfByte_String67= value;
         break;
      case(54):
        MapOfByte_String69= value;
         break;
      case(55):
        MapOfByte_String71= value;
         break;
      case(56):
        MapOfDouble_String72= value;
         break;
      case(57):
        MapOfDouble_String73= value;
         break;
      case(58):
        MapOfDouble_String74= value;
         break;
      case(59):
        MapOfDouble_String75= value;
         break;
      case(60):
        MapOfDouble_String76= value;
         break;
      case(61):
        MapOfDouble_String78= value;
         break;
      case(62):
        MapOfDouble_String80= value;
         break;
      case(63):
        MapOfFloat_String81= value;
         break;
      case(64):
        MapOfFloat_String82= value;
         break;
      case(65):
        MapOfFloat_String83= value;
         break;
      case(66):
        MapOfFloat_String84= value;
         break;
      case(67):
        MapOfFloat_String85= value;
         break;
      case(68):
        MapOfFloat_String87= value;
         break;
      case(69):
        MapOfFloat_String89= value;
         break;
      case(70):
        MapOfInteger_String90= value;
         break;
      case(71):
        MapOfInteger_String91= value;
         break;
      case(72):
        MapOfInteger_String92= value;
         break;
      case(73):
        MapOfInteger_String93= value;
         break;
      case(74):
        MapOfInteger_String94= value;
         break;
      case(75):
        MapOfInteger_String96= value;
         break;
      case(76):
        MapOfInteger_String98= value;
         break;
      case(77):
        MapOfLong_String99= value;
         break;
      case(78):
        MapOfLong_String100= value;
         break;
      case(79):
        MapOfLong_String101= value;
         break;
      case(80):
        MapOfLong_String102= value;
         break;
      case(81):
        MapOfLong_String103= value;
         break;
      case(82):
        MapOfLong_String105= value;
         break;
      case(83):
        MapOfLong_String107= value;
         break;
      case(84):
        MapOfShort_String108= value;
         break;
      case(85):
        MapOfShort_String109= value;
         break;
      case(86):
        MapOfShort_String110= value;
         break;
      case(87):
        MapOfShort_String111= value;
         break;
      case(88):
        MapOfShort_String112= value;
         break;
      case(89):
        MapOfShort_String114= value;
         break;
      case(90):
        MapOfShort_String116= value;
         break;
      case(91):
        MapOfSimpleEnum_String117= value;
         break;
      case(92):
        MapOfSimpleEnum_String118= value;
         break;
      case(93):
        MapOfSimpleEnum_String119= value;
         break;
      case(94):
        MapOfSimpleEnum_String120= value;
         break;
      case(95):
        MapOfSimpleEnum_String121= value;
         break;
      case(96):
        MapOfSimpleEnum_String122= value;
         break;
      case(97):
        MapOfSimpleEnum_String123= value;
         break;
      case(98):
        MapOfSimpleEnum_String124= value;
         break;
      case(99):
        MapOfSimpleEnum_String125= value;
         break;
      case(100):
        MapOfSimpleEnum_String126= value;
         break;
      case(101):
        MapOfSimpleEnum_String127= value;
         break;
      case(102):
        MapOfSimpleEnum_String128= value;
         break;
      case(103):
        MapOfSimpleEnum_String129= value;
         break;
      default:
        throw new IndexOutOfBoundsException("Bad index " + index);
    }
    return true;
  }

    public static class Oid implements Serializable {

        private static final long serialVersionUID = 1L;

        public int identifier;

        public Oid() {
        }

        public Oid(String s) { identifier = Integer.parseInt(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + identifier;}

        public int hashCode() { return identifier; }

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
