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
import java.util.TreeMap;

public class TreeMapStringKeyCollections { 
  public int identifier;
  public TreeMap<String, Object> TreeMapOfString_Object0;
  public TreeMap<String, Object> TreeMapOfString_Object1;
  public TreeMap<String, Object> TreeMapOfString_Object2;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass3;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass4;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass5;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface6;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface7;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface8;
  public TreeMap<String, String> TreeMapOfString_String9;
  public TreeMap<String, String> TreeMapOfString_String10;
  public TreeMap<String, String> TreeMapOfString_String11;
  public TreeMap<String, Date> TreeMapOfString_Date12;
  public TreeMap<String, Date> TreeMapOfString_Date13;
  public TreeMap<String, Date> TreeMapOfString_Date14;
  public TreeMap<String, Locale> TreeMapOfString_Locale15;
  public TreeMap<String, Locale> TreeMapOfString_Locale16;
  public TreeMap<String, Locale> TreeMapOfString_Locale17;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal18;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal19;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal20;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger21;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger22;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger23;
  public TreeMap<String, Byte> TreeMapOfString_Byte24;
  public TreeMap<String, Byte> TreeMapOfString_Byte25;
  public TreeMap<String, Byte> TreeMapOfString_Byte26;
  public TreeMap<String, Double> TreeMapOfString_Double27;
  public TreeMap<String, Double> TreeMapOfString_Double28;
  public TreeMap<String, Double> TreeMapOfString_Double29;
  public TreeMap<String, Float> TreeMapOfString_Float30;
  public TreeMap<String, Float> TreeMapOfString_Float31;
  public TreeMap<String, Float> TreeMapOfString_Float32;
  public TreeMap<String, Integer> TreeMapOfString_Integer33;
  public TreeMap<String, Integer> TreeMapOfString_Integer34;
  public TreeMap<String, Integer> TreeMapOfString_Integer35;
  public TreeMap<String, Long> TreeMapOfString_Long36;
  public TreeMap<String, Long> TreeMapOfString_Long37;
  public TreeMap<String, Long> TreeMapOfString_Long38;
  public TreeMap<String, Short> TreeMapOfString_Short39;
  public TreeMap<String, Short> TreeMapOfString_Short40;
  public TreeMap<String, Short> TreeMapOfString_Short41;
  public TreeMap<String, Object> TreeMapOfString_Object42;
  public TreeMap<String, Object> TreeMapOfString_Object43;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass45;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass46;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface48;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface49;
  public TreeMap<String, String> TreeMapOfString_String51;
  public TreeMap<String, String> TreeMapOfString_String52;
  public TreeMap<String, Date> TreeMapOfString_Date54;
  public TreeMap<String, Date> TreeMapOfString_Date55;
  public TreeMap<String, Locale> TreeMapOfString_Locale57;
  public TreeMap<String, Locale> TreeMapOfString_Locale58;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal60;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal61;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger63;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger64;
  public TreeMap<String, Byte> TreeMapOfString_Byte66;
  public TreeMap<String, Byte> TreeMapOfString_Byte67;
  public TreeMap<String, Double> TreeMapOfString_Double69;
  public TreeMap<String, Double> TreeMapOfString_Double70;
  public TreeMap<String, Float> TreeMapOfString_Float72;
  public TreeMap<String, Float> TreeMapOfString_Float73;
  public TreeMap<String, Integer> TreeMapOfString_Integer75;
  public TreeMap<String, Integer> TreeMapOfString_Integer76;
  public TreeMap<String, Long> TreeMapOfString_Long78;
  public TreeMap<String, Long> TreeMapOfString_Long79;
  public TreeMap<String, Short> TreeMapOfString_Short81;
  public TreeMap<String, Short> TreeMapOfString_Short82;
  public TreeMap<String, Object> TreeMapOfString_Object84;
  public TreeMap<String, Object> TreeMapOfString_Object86;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass87;
  public TreeMap<String, SimpleClass> TreeMapOfString_SimpleClass89;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface90;
  public TreeMap<String, SimpleInterface> TreeMapOfString_SimpleInterface92;
  public TreeMap<String, String> TreeMapOfString_String93;
  public TreeMap<String, String> TreeMapOfString_String95;
  public TreeMap<String, Date> TreeMapOfString_Date96;
  public TreeMap<String, Date> TreeMapOfString_Date98;
  public TreeMap<String, Locale> TreeMapOfString_Locale99;
  public TreeMap<String, Locale> TreeMapOfString_Locale101;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal102;
  public TreeMap<String, BigDecimal> TreeMapOfString_BigDecimal104;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger105;
  public TreeMap<String, BigInteger> TreeMapOfString_BigInteger107;
  public TreeMap<String, Byte> TreeMapOfString_Byte108;
  public TreeMap<String, Byte> TreeMapOfString_Byte110;
  public TreeMap<String, Double> TreeMapOfString_Double111;
  public TreeMap<String, Double> TreeMapOfString_Double113;
  public TreeMap<String, Float> TreeMapOfString_Float114;
  public TreeMap<String, Float> TreeMapOfString_Float116;
  public TreeMap<String, Integer> TreeMapOfString_Integer117;
  public TreeMap<String, Integer> TreeMapOfString_Integer119;
  public TreeMap<String, Long> TreeMapOfString_Long120;
  public TreeMap<String, Long> TreeMapOfString_Long122;
  public TreeMap<String, Short> TreeMapOfString_Short123;
  public TreeMap<String, Short> TreeMapOfString_Short125;

  public static final String [] fieldSpecs = { 
  "public TreeMap TreeMapOfString_Object0",
  "embedded-value=true public TreeMap TreeMapOfString_Object1",
  "embedded-value=false public TreeMap TreeMapOfString_Object2",
  "public TreeMap TreeMapOfString_SimpleClass3",
  "embedded-value=true public TreeMap TreeMapOfString_SimpleClass4",
  "embedded-value=false public TreeMap TreeMapOfString_SimpleClass5",
  "public TreeMap TreeMapOfString_SimpleInterface6",
  "embedded-value=true public TreeMap TreeMapOfString_SimpleInterface7",
  "embedded-value=false public TreeMap TreeMapOfString_SimpleInterface8",
  "public TreeMap TreeMapOfString_String9",
  "embedded-value=true public TreeMap TreeMapOfString_String10",
  "embedded-value=false public TreeMap TreeMapOfString_String11",
  "public TreeMap TreeMapOfString_Date12",
  "embedded-value=true public TreeMap TreeMapOfString_Date13",
  "embedded-value=false public TreeMap TreeMapOfString_Date14",
  "public TreeMap TreeMapOfString_Locale15",
  "embedded-value=true public TreeMap TreeMapOfString_Locale16",
  "embedded-value=false public TreeMap TreeMapOfString_Locale17",
  "public TreeMap TreeMapOfString_BigDecimal18",
  "embedded-value=true public TreeMap TreeMapOfString_BigDecimal19",
  "embedded-value=false public TreeMap TreeMapOfString_BigDecimal20",
  "public TreeMap TreeMapOfString_BigInteger21",
  "embedded-value=true public TreeMap TreeMapOfString_BigInteger22",
  "embedded-value=false public TreeMap TreeMapOfString_BigInteger23",
  "public TreeMap TreeMapOfString_Byte24",
  "embedded-value=true public TreeMap TreeMapOfString_Byte25",
  "embedded-value=false public TreeMap TreeMapOfString_Byte26",
  "public TreeMap TreeMapOfString_Double27",
  "embedded-value=true public TreeMap TreeMapOfString_Double28",
  "embedded-value=false public TreeMap TreeMapOfString_Double29",
  "public TreeMap TreeMapOfString_Float30",
  "embedded-value=true public TreeMap TreeMapOfString_Float31",
  "embedded-value=false public TreeMap TreeMapOfString_Float32",
  "public TreeMap TreeMapOfString_Integer33",
  "embedded-value=true public TreeMap TreeMapOfString_Integer34",
  "embedded-value=false public TreeMap TreeMapOfString_Integer35",
  "public TreeMap TreeMapOfString_Long36",
  "embedded-value=true public TreeMap TreeMapOfString_Long37",
  "embedded-value=false public TreeMap TreeMapOfString_Long38",
  "public TreeMap TreeMapOfString_Short39",
  "embedded-value=true public TreeMap TreeMapOfString_Short40",
  "embedded-value=false public TreeMap TreeMapOfString_Short41",
  "embedded-key=true  public TreeMap TreeMapOfString_Object42",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Object43",
  "embedded-key=true  public TreeMap TreeMapOfString_SimpleClass45",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_SimpleClass46",
  "embedded-key=true  public TreeMap TreeMapOfString_SimpleInterface48",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_SimpleInterface49",
  "embedded-key=true  public TreeMap TreeMapOfString_String51",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_String52",
  "embedded-key=true  public TreeMap TreeMapOfString_Date54",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Date55",
  "embedded-key=true  public TreeMap TreeMapOfString_Locale57",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Locale58",
  "embedded-key=true  public TreeMap TreeMapOfString_BigDecimal60",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_BigDecimal61",
  "embedded-key=true  public TreeMap TreeMapOfString_BigInteger63",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_BigInteger64",
  "embedded-key=true  public TreeMap TreeMapOfString_Byte66",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Byte67",
  "embedded-key=true  public TreeMap TreeMapOfString_Double69",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Double70",
  "embedded-key=true  public TreeMap TreeMapOfString_Float72",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Float73",
  "embedded-key=true  public TreeMap TreeMapOfString_Integer75",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Integer76",
  "embedded-key=true  public TreeMap TreeMapOfString_Long78",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Long79",
  "embedded-key=true  public TreeMap TreeMapOfString_Short81",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_Short82",
  "embedded-key=false  public TreeMap TreeMapOfString_Object84",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Object86",
  "embedded-key=false  public TreeMap TreeMapOfString_SimpleClass87",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_SimpleClass89",
  "embedded-key=false  public TreeMap TreeMapOfString_SimpleInterface90",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_SimpleInterface92",
  "embedded-key=false  public TreeMap TreeMapOfString_String93",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_String95",
  "embedded-key=false  public TreeMap TreeMapOfString_Date96",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Date98",
  "embedded-key=false  public TreeMap TreeMapOfString_Locale99",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Locale101",
  "embedded-key=false  public TreeMap TreeMapOfString_BigDecimal102",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_BigDecimal104",
  "embedded-key=false  public TreeMap TreeMapOfString_BigInteger105",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_BigInteger107",
  "embedded-key=false  public TreeMap TreeMapOfString_Byte108",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Byte110",
  "embedded-key=false  public TreeMap TreeMapOfString_Double111",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Double113",
  "embedded-key=false  public TreeMap TreeMapOfString_Float114",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Float116",
  "embedded-key=false  public TreeMap TreeMapOfString_Integer117",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Integer119",
  "embedded-key=false  public TreeMap TreeMapOfString_Long120",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Long122",
  "embedded-key=false  public TreeMap TreeMapOfString_Short123",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_Short125"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public TreeMap<?, ?> get(int index)
  {
    switch (index)
    {
      case(0):
        return TreeMapOfString_Object0;
      case(1):
        return TreeMapOfString_Object1;
      case(2):
        return TreeMapOfString_Object2;
      case(3):
        return TreeMapOfString_SimpleClass3;
      case(4):
        return TreeMapOfString_SimpleClass4;
      case(5):
        return TreeMapOfString_SimpleClass5;
      case(6):
        return TreeMapOfString_SimpleInterface6;
      case(7):
        return TreeMapOfString_SimpleInterface7;
      case(8):
        return TreeMapOfString_SimpleInterface8;
      case(9):
        return TreeMapOfString_String9;
      case(10):
        return TreeMapOfString_String10;
      case(11):
        return TreeMapOfString_String11;
      case(12):
        return TreeMapOfString_Date12;
      case(13):
        return TreeMapOfString_Date13;
      case(14):
        return TreeMapOfString_Date14;
      case(15):
        return TreeMapOfString_Locale15;
      case(16):
        return TreeMapOfString_Locale16;
      case(17):
        return TreeMapOfString_Locale17;
      case(18):
        return TreeMapOfString_BigDecimal18;
      case(19):
        return TreeMapOfString_BigDecimal19;
      case(20):
        return TreeMapOfString_BigDecimal20;
      case(21):
        return TreeMapOfString_BigInteger21;
      case(22):
        return TreeMapOfString_BigInteger22;
      case(23):
        return TreeMapOfString_BigInteger23;
      case(24):
        return TreeMapOfString_Byte24;
      case(25):
        return TreeMapOfString_Byte25;
      case(26):
        return TreeMapOfString_Byte26;
      case(27):
        return TreeMapOfString_Double27;
      case(28):
        return TreeMapOfString_Double28;
      case(29):
        return TreeMapOfString_Double29;
      case(30):
        return TreeMapOfString_Float30;
      case(31):
        return TreeMapOfString_Float31;
      case(32):
        return TreeMapOfString_Float32;
      case(33):
        return TreeMapOfString_Integer33;
      case(34):
        return TreeMapOfString_Integer34;
      case(35):
        return TreeMapOfString_Integer35;
      case(36):
        return TreeMapOfString_Long36;
      case(37):
        return TreeMapOfString_Long37;
      case(38):
        return TreeMapOfString_Long38;
      case(39):
        return TreeMapOfString_Short39;
      case(40):
        return TreeMapOfString_Short40;
      case(41):
        return TreeMapOfString_Short41;
      case(42):
        return TreeMapOfString_Object42;
      case(43):
        return TreeMapOfString_Object43;
      case(44):
        return TreeMapOfString_SimpleClass45;
      case(45):
        return TreeMapOfString_SimpleClass46;
      case(46):
        return TreeMapOfString_SimpleInterface48;
      case(47):
        return TreeMapOfString_SimpleInterface49;
      case(48):
        return TreeMapOfString_String51;
      case(49):
        return TreeMapOfString_String52;
      case(50):
        return TreeMapOfString_Date54;
      case(51):
        return TreeMapOfString_Date55;
      case(52):
        return TreeMapOfString_Locale57;
      case(53):
        return TreeMapOfString_Locale58;
      case(54):
        return TreeMapOfString_BigDecimal60;
      case(55):
        return TreeMapOfString_BigDecimal61;
      case(56):
        return TreeMapOfString_BigInteger63;
      case(57):
        return TreeMapOfString_BigInteger64;
      case(58):
        return TreeMapOfString_Byte66;
      case(59):
        return TreeMapOfString_Byte67;
      case(60):
        return TreeMapOfString_Double69;
      case(61):
        return TreeMapOfString_Double70;
      case(62):
        return TreeMapOfString_Float72;
      case(63):
        return TreeMapOfString_Float73;
      case(64):
        return TreeMapOfString_Integer75;
      case(65):
        return TreeMapOfString_Integer76;
      case(66):
        return TreeMapOfString_Long78;
      case(67):
        return TreeMapOfString_Long79;
      case(68):
        return TreeMapOfString_Short81;
      case(69):
        return TreeMapOfString_Short82;
      case(70):
        return TreeMapOfString_Object84;
      case(71):
        return TreeMapOfString_Object86;
      case(72):
        return TreeMapOfString_SimpleClass87;
      case(73):
        return TreeMapOfString_SimpleClass89;
      case(74):
        return TreeMapOfString_SimpleInterface90;
      case(75):
        return TreeMapOfString_SimpleInterface92;
      case(76):
        return TreeMapOfString_String93;
      case(77):
        return TreeMapOfString_String95;
      case(78):
        return TreeMapOfString_Date96;
      case(79):
        return TreeMapOfString_Date98;
      case(80):
        return TreeMapOfString_Locale99;
      case(81):
        return TreeMapOfString_Locale101;
      case(82):
        return TreeMapOfString_BigDecimal102;
      case(83):
        return TreeMapOfString_BigDecimal104;
      case(84):
        return TreeMapOfString_BigInteger105;
      case(85):
        return TreeMapOfString_BigInteger107;
      case(86):
        return TreeMapOfString_Byte108;
      case(87):
        return TreeMapOfString_Byte110;
      case(88):
        return TreeMapOfString_Double111;
      case(89):
        return TreeMapOfString_Double113;
      case(90):
        return TreeMapOfString_Float114;
      case(91):
        return TreeMapOfString_Float116;
      case(92):
        return TreeMapOfString_Integer117;
      case(93):
        return TreeMapOfString_Integer119;
      case(94):
        return TreeMapOfString_Long120;
      case(95):
        return TreeMapOfString_Long122;
      case(96):
        return TreeMapOfString_Short123;
      case(97):
        return TreeMapOfString_Short125;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index,TreeMap value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        TreeMapOfString_Object0= value;
         break;
      case(1):
        TreeMapOfString_Object1= value;
         break;
      case(2):
        TreeMapOfString_Object2= value;
         break;
      case(3):
        TreeMapOfString_SimpleClass3= value;
         break;
      case(4):
        TreeMapOfString_SimpleClass4= value;
         break;
      case(5):
        TreeMapOfString_SimpleClass5= value;
         break;
      case(6):
        TreeMapOfString_SimpleInterface6= value;
         break;
      case(7):
        TreeMapOfString_SimpleInterface7= value;
         break;
      case(8):
        TreeMapOfString_SimpleInterface8= value;
         break;
      case(9):
        TreeMapOfString_String9= value;
         break;
      case(10):
        TreeMapOfString_String10= value;
         break;
      case(11):
        TreeMapOfString_String11= value;
         break;
      case(12):
        TreeMapOfString_Date12= value;
         break;
      case(13):
        TreeMapOfString_Date13= value;
         break;
      case(14):
        TreeMapOfString_Date14= value;
         break;
      case(15):
        TreeMapOfString_Locale15= value;
         break;
      case(16):
        TreeMapOfString_Locale16= value;
         break;
      case(17):
        TreeMapOfString_Locale17= value;
         break;
      case(18):
        TreeMapOfString_BigDecimal18= value;
         break;
      case(19):
        TreeMapOfString_BigDecimal19= value;
         break;
      case(20):
        TreeMapOfString_BigDecimal20= value;
         break;
      case(21):
        TreeMapOfString_BigInteger21= value;
         break;
      case(22):
        TreeMapOfString_BigInteger22= value;
         break;
      case(23):
        TreeMapOfString_BigInteger23= value;
         break;
      case(24):
        TreeMapOfString_Byte24= value;
         break;
      case(25):
        TreeMapOfString_Byte25= value;
         break;
      case(26):
        TreeMapOfString_Byte26= value;
         break;
      case(27):
        TreeMapOfString_Double27= value;
         break;
      case(28):
        TreeMapOfString_Double28= value;
         break;
      case(29):
        TreeMapOfString_Double29= value;
         break;
      case(30):
        TreeMapOfString_Float30= value;
         break;
      case(31):
        TreeMapOfString_Float31= value;
         break;
      case(32):
        TreeMapOfString_Float32= value;
         break;
      case(33):
        TreeMapOfString_Integer33= value;
         break;
      case(34):
        TreeMapOfString_Integer34= value;
         break;
      case(35):
        TreeMapOfString_Integer35= value;
         break;
      case(36):
        TreeMapOfString_Long36= value;
         break;
      case(37):
        TreeMapOfString_Long37= value;
         break;
      case(38):
        TreeMapOfString_Long38= value;
         break;
      case(39):
        TreeMapOfString_Short39= value;
         break;
      case(40):
        TreeMapOfString_Short40= value;
         break;
      case(41):
        TreeMapOfString_Short41= value;
         break;
      case(42):
        TreeMapOfString_Object42= value;
         break;
      case(43):
        TreeMapOfString_Object43= value;
         break;
      case(44):
        TreeMapOfString_SimpleClass45= value;
         break;
      case(45):
        TreeMapOfString_SimpleClass46= value;
         break;
      case(46):
        TreeMapOfString_SimpleInterface48= value;
         break;
      case(47):
        TreeMapOfString_SimpleInterface49= value;
         break;
      case(48):
        TreeMapOfString_String51= value;
         break;
      case(49):
        TreeMapOfString_String52= value;
         break;
      case(50):
        TreeMapOfString_Date54= value;
         break;
      case(51):
        TreeMapOfString_Date55= value;
         break;
      case(52):
        TreeMapOfString_Locale57= value;
         break;
      case(53):
        TreeMapOfString_Locale58= value;
         break;
      case(54):
        TreeMapOfString_BigDecimal60= value;
         break;
      case(55):
        TreeMapOfString_BigDecimal61= value;
         break;
      case(56):
        TreeMapOfString_BigInteger63= value;
         break;
      case(57):
        TreeMapOfString_BigInteger64= value;
         break;
      case(58):
        TreeMapOfString_Byte66= value;
         break;
      case(59):
        TreeMapOfString_Byte67= value;
         break;
      case(60):
        TreeMapOfString_Double69= value;
         break;
      case(61):
        TreeMapOfString_Double70= value;
         break;
      case(62):
        TreeMapOfString_Float72= value;
         break;
      case(63):
        TreeMapOfString_Float73= value;
         break;
      case(64):
        TreeMapOfString_Integer75= value;
         break;
      case(65):
        TreeMapOfString_Integer76= value;
         break;
      case(66):
        TreeMapOfString_Long78= value;
         break;
      case(67):
        TreeMapOfString_Long79= value;
         break;
      case(68):
        TreeMapOfString_Short81= value;
         break;
      case(69):
        TreeMapOfString_Short82= value;
         break;
      case(70):
        TreeMapOfString_Object84= value;
         break;
      case(71):
        TreeMapOfString_Object86= value;
         break;
      case(72):
        TreeMapOfString_SimpleClass87= value;
         break;
      case(73):
        TreeMapOfString_SimpleClass89= value;
         break;
      case(74):
        TreeMapOfString_SimpleInterface90= value;
         break;
      case(75):
        TreeMapOfString_SimpleInterface92= value;
         break;
      case(76):
        TreeMapOfString_String93= value;
         break;
      case(77):
        TreeMapOfString_String95= value;
         break;
      case(78):
        TreeMapOfString_Date96= value;
         break;
      case(79):
        TreeMapOfString_Date98= value;
         break;
      case(80):
        TreeMapOfString_Locale99= value;
         break;
      case(81):
        TreeMapOfString_Locale101= value;
         break;
      case(82):
        TreeMapOfString_BigDecimal102= value;
         break;
      case(83):
        TreeMapOfString_BigDecimal104= value;
         break;
      case(84):
        TreeMapOfString_BigInteger105= value;
         break;
      case(85):
        TreeMapOfString_BigInteger107= value;
         break;
      case(86):
        TreeMapOfString_Byte108= value;
         break;
      case(87):
        TreeMapOfString_Byte110= value;
         break;
      case(88):
        TreeMapOfString_Double111= value;
         break;
      case(89):
        TreeMapOfString_Double113= value;
         break;
      case(90):
        TreeMapOfString_Float114= value;
         break;
      case(91):
        TreeMapOfString_Float116= value;
         break;
      case(92):
        TreeMapOfString_Integer117= value;
         break;
      case(93):
        TreeMapOfString_Integer119= value;
         break;
      case(94):
        TreeMapOfString_Long120= value;
         break;
      case(95):
        TreeMapOfString_Long122= value;
         break;
      case(96):
        TreeMapOfString_Short123= value;
         break;
      case(97):
        TreeMapOfString_Short125= value;
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
