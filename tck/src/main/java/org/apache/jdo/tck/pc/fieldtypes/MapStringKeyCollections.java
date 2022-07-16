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
import java.util.Locale;
import java.util.Map;

public class MapStringKeyCollections {
  public int identifier;
  public Map<String, Object> MapOfString_Object0;
  public Map<String, Object> MapOfString_Object1;
  public Map<String, Object> MapOfString_Object2;
  public Map<String, SimpleClass> MapOfString_SimpleClass3;
  public Map<String, SimpleClass> MapOfString_SimpleClass4;
  public Map<String, SimpleClass> MapOfString_SimpleClass5;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface6;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface7;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface8;
  public Map<String, String> MapOfString_String9;
  public Map<String, String> MapOfString_String10;
  public Map<String, String> MapOfString_String11;
  public Map<String, Date> MapOfString_Date12;
  public Map<String, Date> MapOfString_Date13;
  public Map<String, Date> MapOfString_Date14;
  public Map<String, Locale> MapOfString_Locale15;
  public Map<String, Locale> MapOfString_Locale16;
  public Map<String, Locale> MapOfString_Locale17;
  public Map<String, BigDecimal> MapOfString_BigDecimal18;
  public Map<String, BigDecimal> MapOfString_BigDecimal19;
  public Map<String, BigDecimal> MapOfString_BigDecimal20;
  public Map<String, BigInteger> MapOfString_BigInteger21;
  public Map<String, BigInteger> MapOfString_BigInteger22;
  public Map<String, BigInteger> MapOfString_BigInteger23;
  public Map<String, Byte> MapOfString_Byte24;
  public Map<String, Byte> MapOfString_Byte25;
  public Map<String, Byte> MapOfString_Byte26;
  public Map<String, Double> MapOfString_Double27;
  public Map<String, Double> MapOfString_Double28;
  public Map<String, Double> MapOfString_Double29;
  public Map<String, Float> MapOfString_Float30;
  public Map<String, Float> MapOfString_Float31;
  public Map<String, Float> MapOfString_Float32;
  public Map<String, Integer> MapOfString_Integer33;
  public Map<String, Integer> MapOfString_Integer34;
  public Map<String, Integer> MapOfString_Integer35;
  public Map<String, Long> MapOfString_Long36;
  public Map<String, Long> MapOfString_Long37;
  public Map<String, Long> MapOfString_Long38;
  public Map<String, Short> MapOfString_Short39;
  public Map<String, Short> MapOfString_Short40;
  public Map<String, Short> MapOfString_Short41;
  public Map<String, Object> MapOfString_Object42;
  public Map<String, Object> MapOfString_Object43;
  public Map<String, SimpleClass> MapOfString_SimpleClass45;
  public Map<String, SimpleClass> MapOfString_SimpleClass46;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface48;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface49;
  public Map<String, String> MapOfString_String51;
  public Map<String, String> MapOfString_String52;
  public Map<String, Date> MapOfString_Date54;
  public Map<String, Date> MapOfString_Date55;
  public Map<String, Locale> MapOfString_Locale57;
  public Map<String, Locale> MapOfString_Locale58;
  public Map<String, BigDecimal> MapOfString_BigDecimal60;
  public Map<String, BigDecimal> MapOfString_BigDecimal61;
  public Map<String, BigInteger> MapOfString_BigInteger63;
  public Map<String, BigInteger> MapOfString_BigInteger64;
  public Map<String, Byte> MapOfString_Byte66;
  public Map<String, Byte> MapOfString_Byte67;
  public Map<String, Double> MapOfString_Double69;
  public Map<String, Double> MapOfString_Double70;
  public Map<String, Float> MapOfString_Float72;
  public Map<String, Float> MapOfString_Float73;
  public Map<String, Integer> MapOfString_Integer75;
  public Map<String, Integer> MapOfString_Integer76;
  public Map<String, Long> MapOfString_Long78;
  public Map<String, Long> MapOfString_Long79;
  public Map<String, Short> MapOfString_Short81;
  public Map<String, Short> MapOfString_Short82;
  public Map<String, Object> MapOfString_Object84;
  public Map<String, Object> MapOfString_Object86;
  public Map<String, SimpleClass> MapOfString_SimpleClass87;
  public Map<String, SimpleClass> MapOfString_SimpleClass89;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface90;
  public Map<String, SimpleInterface> MapOfString_SimpleInterface92;
  public Map<String, String> MapOfString_String93;
  public Map<String, String> MapOfString_String95;
  public Map<String, Date> MapOfString_Date96;
  public Map<String, Date> MapOfString_Date98;
  public Map<String, Locale> MapOfString_Locale99;
  public Map<String, Locale> MapOfString_Locale101;
  public Map<String, BigDecimal> MapOfString_BigDecimal102;
  public Map<String, BigDecimal> MapOfString_BigDecimal104;
  public Map<String, BigInteger> MapOfString_BigInteger105;
  public Map<String, BigInteger> MapOfString_BigInteger107;
  public Map<String, Byte> MapOfString_Byte108;
  public Map<String, Byte> MapOfString_Byte110;
  public Map<String, Double> MapOfString_Double111;
  public Map<String, Double> MapOfString_Double113;
  public Map<String, Float> MapOfString_Float114;
  public Map<String, Float> MapOfString_Float116;
  public Map<String, Integer> MapOfString_Integer117;
  public Map<String, Integer> MapOfString_Integer119;
  public Map<String, Long> MapOfString_Long120;
  public Map<String, Long> MapOfString_Long122;
  public Map<String, Short> MapOfString_Short123;
  public Map<String, Short> MapOfString_Short125;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum126;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum127;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum128;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum129;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum130;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum131;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum132;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum133;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum134;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum135;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum136;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum137;
  public Map<String, SimpleEnum> MapOfString_SimpleEnum138;

  public static final String [] fieldSpecs = { 
  "public Map MapOfString_Object0",
  "embedded-value=true public Map MapOfString_Object1",
  "embedded-value=false public Map MapOfString_Object2",
  "public Map MapOfString_SimpleClass3",
  "embedded-value=true public Map MapOfString_SimpleClass4",
  "embedded-value=false public Map MapOfString_SimpleClass5",
  "public Map MapOfString_SimpleInterface6",
  "embedded-value=true public Map MapOfString_SimpleInterface7",
  "embedded-value=false public Map MapOfString_SimpleInterface8",
  "public Map MapOfString_String9",
  "embedded-value=true public Map MapOfString_String10",
  "embedded-value=false public Map MapOfString_String11",
  "public Map MapOfString_Date12",
  "embedded-value=true public Map MapOfString_Date13",
  "embedded-value=false public Map MapOfString_Date14",
  "public Map MapOfString_Locale15",
  "embedded-value=true public Map MapOfString_Locale16",
  "embedded-value=false public Map MapOfString_Locale17",
  "public Map MapOfString_BigDecimal18",
  "embedded-value=true public Map MapOfString_BigDecimal19",
  "embedded-value=false public Map MapOfString_BigDecimal20",
  "public Map MapOfString_BigInteger21",
  "embedded-value=true public Map MapOfString_BigInteger22",
  "embedded-value=false public Map MapOfString_BigInteger23",
  "public Map MapOfString_Byte24",
  "embedded-value=true public Map MapOfString_Byte25",
  "embedded-value=false public Map MapOfString_Byte26",
  "public Map MapOfString_Double27",
  "embedded-value=true public Map MapOfString_Double28",
  "embedded-value=false public Map MapOfString_Double29",
  "public Map MapOfString_Float30",
  "embedded-value=true public Map MapOfString_Float31",
  "embedded-value=false public Map MapOfString_Float32",
  "public Map MapOfString_Integer33",
  "embedded-value=true public Map MapOfString_Integer34",
  "embedded-value=false public Map MapOfString_Integer35",
  "public Map MapOfString_Long36",
  "embedded-value=true public Map MapOfString_Long37",
  "embedded-value=false public Map MapOfString_Long38",
  "public Map MapOfString_Short39",
  "embedded-value=true public Map MapOfString_Short40",
  "embedded-value=false public Map MapOfString_Short41",
  "embedded-key=true  public Map MapOfString_Object42",
  "embedded-key=true embedded-value=true public Map MapOfString_Object43",
  "embedded-key=true  public Map MapOfString_SimpleClass45",
  "embedded-key=true embedded-value=true public Map MapOfString_SimpleClass46",
  "embedded-key=true  public Map MapOfString_SimpleInterface48",
  "embedded-key=true embedded-value=true public Map MapOfString_SimpleInterface49",
  "embedded-key=true  public Map MapOfString_String51",
  "embedded-key=true embedded-value=true public Map MapOfString_String52",
  "embedded-key=true  public Map MapOfString_Date54",
  "embedded-key=true embedded-value=true public Map MapOfString_Date55",
  "embedded-key=true  public Map MapOfString_Locale57",
  "embedded-key=true embedded-value=true public Map MapOfString_Locale58",
  "embedded-key=true  public Map MapOfString_BigDecimal60",
  "embedded-key=true embedded-value=true public Map MapOfString_BigDecimal61",
  "embedded-key=true  public Map MapOfString_BigInteger63",
  "embedded-key=true embedded-value=true public Map MapOfString_BigInteger64",
  "embedded-key=true  public Map MapOfString_Byte66",
  "embedded-key=true embedded-value=true public Map MapOfString_Byte67",
  "embedded-key=true  public Map MapOfString_Double69",
  "embedded-key=true embedded-value=true public Map MapOfString_Double70",
  "embedded-key=true  public Map MapOfString_Float72",
  "embedded-key=true embedded-value=true public Map MapOfString_Float73",
  "embedded-key=true  public Map MapOfString_Integer75",
  "embedded-key=true embedded-value=true public Map MapOfString_Integer76",
  "embedded-key=true  public Map MapOfString_Long78",
  "embedded-key=true embedded-value=true public Map MapOfString_Long79",
  "embedded-key=true  public Map MapOfString_Short81",
  "embedded-key=true embedded-value=true public Map MapOfString_Short82",
  "embedded-key=false  public Map MapOfString_Object84",
  "embedded-key=false embedded-value=false public Map MapOfString_Object86",
  "embedded-key=false  public Map MapOfString_SimpleClass87",
  "embedded-key=false embedded-value=false public Map MapOfString_SimpleClass89",
  "embedded-key=false  public Map MapOfString_SimpleInterface90",
  "embedded-key=false embedded-value=false public Map MapOfString_SimpleInterface92",
  "embedded-key=false  public Map MapOfString_String93",
  "embedded-key=false embedded-value=false public Map MapOfString_String95",
  "embedded-key=false  public Map MapOfString_Date96",
  "embedded-key=false embedded-value=false public Map MapOfString_Date98",
  "embedded-key=false  public Map MapOfString_Locale99",
  "embedded-key=false embedded-value=false public Map MapOfString_Locale101",
  "embedded-key=false  public Map MapOfString_BigDecimal102",
  "embedded-key=false embedded-value=false public Map MapOfString_BigDecimal104",
  "embedded-key=false  public Map MapOfString_BigInteger105",
  "embedded-key=false embedded-value=false public Map MapOfString_BigInteger107",
  "embedded-key=false  public Map MapOfString_Byte108",
  "embedded-key=false embedded-value=false public Map MapOfString_Byte110",
  "embedded-key=false  public Map MapOfString_Double111",
  "embedded-key=false embedded-value=false public Map MapOfString_Double113",
  "embedded-key=false  public Map MapOfString_Float114",
  "embedded-key=false embedded-value=false public Map MapOfString_Float116",
  "embedded-key=false  public Map MapOfString_Integer117",
  "embedded-key=false embedded-value=false public Map MapOfString_Integer119",
  "embedded-key=false  public Map MapOfString_Long120",
  "embedded-key=false embedded-value=false public Map MapOfString_Long122",
  "embedded-key=false  public Map MapOfString_Short123",
  "embedded-key=false embedded-value=false public Map MapOfString_Short125",
  "public Map MapOfString_SimpleEnum126",
  "embedded-value=true public Map MapOfString_SimpleEnum127",
  "embedded-value=false public Map MapOfString_SimpleEnum128",
  "embedded-key=true  public Map MapOfString_SimpleEnum129",
  "embedded-key=true embedded-value=true public Map MapOfString_SimpleEnum130",
  "embedded-key=false  public Map MapOfString_SimpleEnum131",
  "embedded-key=false embedded-value=false public Map MapOfString_SimpleEnum132",
  "public Map MapOfString_SimpleEnum133",
  "embedded-value=true public Map MapOfString_SimpleEnum134",
  "embedded-value=false public Map MapOfString_SimpleEnum135",
  "embedded-key=true  public Map MapOfString_SimpleEnum136",
  "embedded-key=true embedded-value=true public Map MapOfString_SimpleEnum137",
  "embedded-key=false  public Map MapOfString_SimpleEnum138"
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
        return MapOfString_Object0;
      case(1):
        return MapOfString_Object1;
      case(2):
        return MapOfString_Object2;
      case(3):
        return MapOfString_SimpleClass3;
      case(4):
        return MapOfString_SimpleClass4;
      case(5):
        return MapOfString_SimpleClass5;
      case(6):
        return MapOfString_SimpleInterface6;
      case(7):
        return MapOfString_SimpleInterface7;
      case(8):
        return MapOfString_SimpleInterface8;
      case(9):
        return MapOfString_String9;
      case(10):
        return MapOfString_String10;
      case(11):
        return MapOfString_String11;
      case(12):
        return MapOfString_Date12;
      case(13):
        return MapOfString_Date13;
      case(14):
        return MapOfString_Date14;
      case(15):
        return MapOfString_Locale15;
      case(16):
        return MapOfString_Locale16;
      case(17):
        return MapOfString_Locale17;
      case(18):
        return MapOfString_BigDecimal18;
      case(19):
        return MapOfString_BigDecimal19;
      case(20):
        return MapOfString_BigDecimal20;
      case(21):
        return MapOfString_BigInteger21;
      case(22):
        return MapOfString_BigInteger22;
      case(23):
        return MapOfString_BigInteger23;
      case(24):
        return MapOfString_Byte24;
      case(25):
        return MapOfString_Byte25;
      case(26):
        return MapOfString_Byte26;
      case(27):
        return MapOfString_Double27;
      case(28):
        return MapOfString_Double28;
      case(29):
        return MapOfString_Double29;
      case(30):
        return MapOfString_Float30;
      case(31):
        return MapOfString_Float31;
      case(32):
        return MapOfString_Float32;
      case(33):
        return MapOfString_Integer33;
      case(34):
        return MapOfString_Integer34;
      case(35):
        return MapOfString_Integer35;
      case(36):
        return MapOfString_Long36;
      case(37):
        return MapOfString_Long37;
      case(38):
        return MapOfString_Long38;
      case(39):
        return MapOfString_Short39;
      case(40):
        return MapOfString_Short40;
      case(41):
        return MapOfString_Short41;
      case(42):
        return MapOfString_Object42;
      case(43):
        return MapOfString_Object43;
      case(44):
        return MapOfString_SimpleClass45;
      case(45):
        return MapOfString_SimpleClass46;
      case(46):
        return MapOfString_SimpleInterface48;
      case(47):
        return MapOfString_SimpleInterface49;
      case(48):
        return MapOfString_String51;
      case(49):
        return MapOfString_String52;
      case(50):
        return MapOfString_Date54;
      case(51):
        return MapOfString_Date55;
      case(52):
        return MapOfString_Locale57;
      case(53):
        return MapOfString_Locale58;
      case(54):
        return MapOfString_BigDecimal60;
      case(55):
        return MapOfString_BigDecimal61;
      case(56):
        return MapOfString_BigInteger63;
      case(57):
        return MapOfString_BigInteger64;
      case(58):
        return MapOfString_Byte66;
      case(59):
        return MapOfString_Byte67;
      case(60):
        return MapOfString_Double69;
      case(61):
        return MapOfString_Double70;
      case(62):
        return MapOfString_Float72;
      case(63):
        return MapOfString_Float73;
      case(64):
        return MapOfString_Integer75;
      case(65):
        return MapOfString_Integer76;
      case(66):
        return MapOfString_Long78;
      case(67):
        return MapOfString_Long79;
      case(68):
        return MapOfString_Short81;
      case(69):
        return MapOfString_Short82;
      case(70):
        return MapOfString_Object84;
      case(71):
        return MapOfString_Object86;
      case(72):
        return MapOfString_SimpleClass87;
      case(73):
        return MapOfString_SimpleClass89;
      case(74):
        return MapOfString_SimpleInterface90;
      case(75):
        return MapOfString_SimpleInterface92;
      case(76):
        return MapOfString_String93;
      case(77):
        return MapOfString_String95;
      case(78):
        return MapOfString_Date96;
      case(79):
        return MapOfString_Date98;
      case(80):
        return MapOfString_Locale99;
      case(81):
        return MapOfString_Locale101;
      case(82):
        return MapOfString_BigDecimal102;
      case(83):
        return MapOfString_BigDecimal104;
      case(84):
        return MapOfString_BigInteger105;
      case(85):
        return MapOfString_BigInteger107;
      case(86):
        return MapOfString_Byte108;
      case(87):
        return MapOfString_Byte110;
      case(88):
        return MapOfString_Double111;
      case(89):
        return MapOfString_Double113;
      case(90):
        return MapOfString_Float114;
      case(91):
        return MapOfString_Float116;
      case(92):
        return MapOfString_Integer117;
      case(93):
        return MapOfString_Integer119;
      case(94):
        return MapOfString_Long120;
      case(95):
        return MapOfString_Long122;
      case(96):
        return MapOfString_Short123;
      case(97):
        return MapOfString_Short125;
      case(98):
        return MapOfString_SimpleEnum126;
      case(99):
        return MapOfString_SimpleEnum127;
      case(100):
        return MapOfString_SimpleEnum128;
      case(101):
        return MapOfString_SimpleEnum129;
      case(102):
        return MapOfString_SimpleEnum130;
      case(103):
        return MapOfString_SimpleEnum131;
      case(104):
        return MapOfString_SimpleEnum132;
      case(105):
        return MapOfString_SimpleEnum133;
      case(106):
        return MapOfString_SimpleEnum134;
      case(107):
        return MapOfString_SimpleEnum135;
      case(108):
        return MapOfString_SimpleEnum136;
      case(109):
        return MapOfString_SimpleEnum137;
      case(110):
        return MapOfString_SimpleEnum138;
      default:
        throw new IndexOutOfBoundsException();
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
        MapOfString_Object0= value;
         break;
      case(1):
        MapOfString_Object1= value;
         break;
      case(2):
        MapOfString_Object2= value;
         break;
      case(3):
        MapOfString_SimpleClass3= value;
         break;
      case(4):
        MapOfString_SimpleClass4= value;
         break;
      case(5):
        MapOfString_SimpleClass5= value;
         break;
      case(6):
        MapOfString_SimpleInterface6= value;
         break;
      case(7):
        MapOfString_SimpleInterface7= value;
         break;
      case(8):
        MapOfString_SimpleInterface8= value;
         break;
      case(9):
        MapOfString_String9= value;
         break;
      case(10):
        MapOfString_String10= value;
         break;
      case(11):
        MapOfString_String11= value;
         break;
      case(12):
        MapOfString_Date12= value;
         break;
      case(13):
        MapOfString_Date13= value;
         break;
      case(14):
        MapOfString_Date14= value;
         break;
      case(15):
        MapOfString_Locale15= value;
         break;
      case(16):
        MapOfString_Locale16= value;
         break;
      case(17):
        MapOfString_Locale17= value;
         break;
      case(18):
        MapOfString_BigDecimal18= value;
         break;
      case(19):
        MapOfString_BigDecimal19= value;
         break;
      case(20):
        MapOfString_BigDecimal20= value;
         break;
      case(21):
        MapOfString_BigInteger21= value;
         break;
      case(22):
        MapOfString_BigInteger22= value;
         break;
      case(23):
        MapOfString_BigInteger23= value;
         break;
      case(24):
        MapOfString_Byte24= value;
         break;
      case(25):
        MapOfString_Byte25= value;
         break;
      case(26):
        MapOfString_Byte26= value;
         break;
      case(27):
        MapOfString_Double27= value;
         break;
      case(28):
        MapOfString_Double28= value;
         break;
      case(29):
        MapOfString_Double29= value;
         break;
      case(30):
        MapOfString_Float30= value;
         break;
      case(31):
        MapOfString_Float31= value;
         break;
      case(32):
        MapOfString_Float32= value;
         break;
      case(33):
        MapOfString_Integer33= value;
         break;
      case(34):
        MapOfString_Integer34= value;
         break;
      case(35):
        MapOfString_Integer35= value;
         break;
      case(36):
        MapOfString_Long36= value;
         break;
      case(37):
        MapOfString_Long37= value;
         break;
      case(38):
        MapOfString_Long38= value;
         break;
      case(39):
        MapOfString_Short39= value;
         break;
      case(40):
        MapOfString_Short40= value;
         break;
      case(41):
        MapOfString_Short41= value;
         break;
      case(42):
        MapOfString_Object42= value;
         break;
      case(43):
        MapOfString_Object43= value;
         break;
      case(44):
        MapOfString_SimpleClass45= value;
         break;
      case(45):
        MapOfString_SimpleClass46= value;
         break;
      case(46):
        MapOfString_SimpleInterface48= value;
         break;
      case(47):
        MapOfString_SimpleInterface49= value;
         break;
      case(48):
        MapOfString_String51= value;
         break;
      case(49):
        MapOfString_String52= value;
         break;
      case(50):
        MapOfString_Date54= value;
         break;
      case(51):
        MapOfString_Date55= value;
         break;
      case(52):
        MapOfString_Locale57= value;
         break;
      case(53):
        MapOfString_Locale58= value;
         break;
      case(54):
        MapOfString_BigDecimal60= value;
         break;
      case(55):
        MapOfString_BigDecimal61= value;
         break;
      case(56):
        MapOfString_BigInteger63= value;
         break;
      case(57):
        MapOfString_BigInteger64= value;
         break;
      case(58):
        MapOfString_Byte66= value;
         break;
      case(59):
        MapOfString_Byte67= value;
         break;
      case(60):
        MapOfString_Double69= value;
         break;
      case(61):
        MapOfString_Double70= value;
         break;
      case(62):
        MapOfString_Float72= value;
         break;
      case(63):
        MapOfString_Float73= value;
         break;
      case(64):
        MapOfString_Integer75= value;
         break;
      case(65):
        MapOfString_Integer76= value;
         break;
      case(66):
        MapOfString_Long78= value;
         break;
      case(67):
        MapOfString_Long79= value;
         break;
      case(68):
        MapOfString_Short81= value;
         break;
      case(69):
        MapOfString_Short82= value;
         break;
      case(70):
        MapOfString_Object84= value;
         break;
      case(71):
        MapOfString_Object86= value;
         break;
      case(72):
        MapOfString_SimpleClass87= value;
         break;
      case(73):
        MapOfString_SimpleClass89= value;
         break;
      case(74):
        MapOfString_SimpleInterface90= value;
         break;
      case(75):
        MapOfString_SimpleInterface92= value;
         break;
      case(76):
        MapOfString_String93= value;
         break;
      case(77):
        MapOfString_String95= value;
         break;
      case(78):
        MapOfString_Date96= value;
         break;
      case(79):
        MapOfString_Date98= value;
         break;
      case(80):
        MapOfString_Locale99= value;
         break;
      case(81):
        MapOfString_Locale101= value;
         break;
      case(82):
        MapOfString_BigDecimal102= value;
         break;
      case(83):
        MapOfString_BigDecimal104= value;
         break;
      case(84):
        MapOfString_BigInteger105= value;
         break;
      case(85):
        MapOfString_BigInteger107= value;
         break;
      case(86):
        MapOfString_Byte108= value;
         break;
      case(87):
        MapOfString_Byte110= value;
         break;
      case(88):
        MapOfString_Double111= value;
         break;
      case(89):
        MapOfString_Double113= value;
         break;
      case(90):
        MapOfString_Float114= value;
         break;
      case(91):
        MapOfString_Float116= value;
         break;
      case(92):
        MapOfString_Integer117= value;
         break;
      case(93):
        MapOfString_Integer119= value;
         break;
      case(94):
        MapOfString_Long120= value;
         break;
      case(95):
        MapOfString_Long122= value;
         break;
      case(96):
        MapOfString_Short123= value;
         break;
      case(97):
        MapOfString_Short125= value;
         break;
      case(98):
        MapOfString_SimpleEnum126= value;
         break;
      case(99):
        MapOfString_SimpleEnum127= value;
         break;
      case(100):
        MapOfString_SimpleEnum128= value;
         break;
      case(101):
        MapOfString_SimpleEnum129= value;
         break;
      case(102):
        MapOfString_SimpleEnum130= value;
         break;
      case(103):
        MapOfString_SimpleEnum131= value;
         break;
      case(104):
        MapOfString_SimpleEnum132= value;
         break;
      case(105):
        MapOfString_SimpleEnum133= value;
         break;
      case(106):
        MapOfString_SimpleEnum134= value;
         break;
      case(107):
        MapOfString_SimpleEnum135= value;
         break;
      case(108):
        MapOfString_SimpleEnum136= value;
         break;
      case(109):
        MapOfString_SimpleEnum137= value;
         break;
      case(110):
        MapOfString_SimpleEnum138= value;
         break;
      default:
        throw new IndexOutOfBoundsException();
    }
    return true;
  }

    public static class Oid implements Serializable {
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
