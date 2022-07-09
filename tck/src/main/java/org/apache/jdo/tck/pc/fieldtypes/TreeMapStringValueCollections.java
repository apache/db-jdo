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
import java.util.TreeMap;

public class TreeMapStringValueCollections { 
  public int identifier;
  public TreeMap<Object, String> TreeMapOfObject_String0;
  public TreeMap<Object, String> TreeMapOfObject_String1;
  public TreeMap<Object, String> TreeMapOfObject_String2;
  public TreeMap<Object, String> TreeMapOfObject_String3;
  public TreeMap<Object, String> TreeMapOfObject_String4;
  public TreeMap<Object, String> TreeMapOfObject_String6;
  public TreeMap<Object, String> TreeMapOfObject_String8;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String9;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String10;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String11;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String12;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String13;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String15;
  public TreeMap<SimpleClass, String> TreeMapOfSimpleClass_String17;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String18;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String19;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String20;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String21;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String22;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String24;
  public TreeMap<SimpleInterface, String> TreeMapOfSimpleInterface_String26;
  public TreeMap<String, String> TreeMapOfString_String27;
  public TreeMap<String, String> TreeMapOfString_String28;
  public TreeMap<String, String> TreeMapOfString_String29;
  public TreeMap<String, String> TreeMapOfString_String30;
  public TreeMap<String, String> TreeMapOfString_String31;
  public TreeMap<String, String> TreeMapOfString_String33;
  public TreeMap<String, String> TreeMapOfString_String35;
  public TreeMap<Date, String> TreeMapOfDate_String36;
  public TreeMap<Date, String> TreeMapOfDate_String37;
  public TreeMap<Date, String> TreeMapOfDate_String38;
  public TreeMap<Date, String> TreeMapOfDate_String39;
  public TreeMap<Date, String> TreeMapOfDate_String40;
  public TreeMap<Date, String> TreeMapOfDate_String42;
  public TreeMap<Date, String> TreeMapOfDate_String44;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String45;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String46;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String47;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String48;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String49;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String51;
  public TreeMap<BigDecimal, String> TreeMapOfBigDecimal_String53;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String54;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String55;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String56;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String57;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String58;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String60;
  public TreeMap<BigInteger, String> TreeMapOfBigInteger_String62;
  public TreeMap<Byte, String> TreeMapOfByte_String63;
  public TreeMap<Byte, String> TreeMapOfByte_String64;
  public TreeMap<Byte, String> TreeMapOfByte_String65;
  public TreeMap<Byte, String> TreeMapOfByte_String66;
  public TreeMap<Byte, String> TreeMapOfByte_String67;
  public TreeMap<Byte, String> TreeMapOfByte_String69;
  public TreeMap<Byte, String> TreeMapOfByte_String71;
  public TreeMap<Double, String> TreeMapOfDouble_String72;
  public TreeMap<Double, String> TreeMapOfDouble_String73;
  public TreeMap<Double, String> TreeMapOfDouble_String74;
  public TreeMap<Double, String> TreeMapOfDouble_String75;
  public TreeMap<Double, String> TreeMapOfDouble_String76;
  public TreeMap<Double, String> TreeMapOfDouble_String78;
  public TreeMap<Double, String> TreeMapOfDouble_String80;
  public TreeMap<Float, String> TreeMapOfFloat_String81;
  public TreeMap<Float, String> TreeMapOfFloat_String82;
  public TreeMap<Float, String> TreeMapOfFloat_String83;
  public TreeMap<Float, String> TreeMapOfFloat_String84;
  public TreeMap<Float, String> TreeMapOfFloat_String85;
  public TreeMap<Float, String> TreeMapOfFloat_String87;
  public TreeMap<Float, String> TreeMapOfFloat_String89;
  public TreeMap<Integer, String> TreeMapOfInteger_String90;
  public TreeMap<Integer, String> TreeMapOfInteger_String91;
  public TreeMap<Integer, String> TreeMapOfInteger_String92;
  public TreeMap<Integer, String> TreeMapOfInteger_String93;
  public TreeMap<Integer, String> TreeMapOfInteger_String94;
  public TreeMap<Integer, String> TreeMapOfInteger_String96;
  public TreeMap<Integer, String> TreeMapOfInteger_String98;
  public TreeMap<Long, String> TreeMapOfLong_String99;
  public TreeMap<Long, String> TreeMapOfLong_String100;
  public TreeMap<Long, String> TreeMapOfLong_String101;
  public TreeMap<Long, String> TreeMapOfLong_String102;
  public TreeMap<Long, String> TreeMapOfLong_String103;
  public TreeMap<Long, String> TreeMapOfLong_String105;
  public TreeMap<Long, String> TreeMapOfLong_String107;
  public TreeMap<Short, String> TreeMapOfShort_String108;
  public TreeMap<Short, String> TreeMapOfShort_String109;
  public TreeMap<Short, String> TreeMapOfShort_String110;
  public TreeMap<Short, String> TreeMapOfShort_String111;
  public TreeMap<Short, String> TreeMapOfShort_String112;
  public TreeMap<Short, String> TreeMapOfShort_String114;
  public TreeMap<Short, String> TreeMapOfShort_String116;

  public static final String [] fieldSpecs = { 
  "public TreeMap TreeMapOfObject_String0",
  "embedded-value=true public TreeMap TreeMapOfObject_String1",
  "embedded-value=false public TreeMap TreeMapOfObject_String2",
  "embedded-key=true  public TreeMap TreeMapOfObject_String3",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfObject_String4",
  "embedded-key=false  public TreeMap TreeMapOfObject_String6",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfObject_String8",
  "public TreeMap TreeMapOfSimpleClass_String9",
  "embedded-value=true public TreeMap TreeMapOfSimpleClass_String10",
  "embedded-value=false public TreeMap TreeMapOfSimpleClass_String11",
  "embedded-key=true  public TreeMap TreeMapOfSimpleClass_String12",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfSimpleClass_String13",
  "embedded-key=false  public TreeMap TreeMapOfSimpleClass_String15",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfSimpleClass_String17",
  "public TreeMap TreeMapOfSimpleInterface_String18",
  "embedded-value=true public TreeMap TreeMapOfSimpleInterface_String19",
  "embedded-value=false public TreeMap TreeMapOfSimpleInterface_String20",
  "embedded-key=true  public TreeMap TreeMapOfSimpleInterface_String21",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfSimpleInterface_String22",
  "embedded-key=false  public TreeMap TreeMapOfSimpleInterface_String24",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfSimpleInterface_String26",
  "public TreeMap TreeMapOfString_String27",
  "embedded-value=true public TreeMap TreeMapOfString_String28",
  "embedded-value=false public TreeMap TreeMapOfString_String29",
  "embedded-key=true  public TreeMap TreeMapOfString_String30",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfString_String31",
  "embedded-key=false  public TreeMap TreeMapOfString_String33",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfString_String35",
  "public TreeMap TreeMapOfDate_String36",
  "embedded-value=true public TreeMap TreeMapOfDate_String37",
  "embedded-value=false public TreeMap TreeMapOfDate_String38",
  "embedded-key=true  public TreeMap TreeMapOfDate_String39",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfDate_String40",
  "embedded-key=false  public TreeMap TreeMapOfDate_String42",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfDate_String44",
  "public TreeMap TreeMapOfBigDecimal_String45",
  "embedded-value=true public TreeMap TreeMapOfBigDecimal_String46",
  "embedded-value=false public TreeMap TreeMapOfBigDecimal_String47",
  "embedded-key=true  public TreeMap TreeMapOfBigDecimal_String48",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfBigDecimal_String49",
  "embedded-key=false  public TreeMap TreeMapOfBigDecimal_String51",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfBigDecimal_String53",
  "public TreeMap TreeMapOfBigInteger_String54",
  "embedded-value=true public TreeMap TreeMapOfBigInteger_String55",
  "embedded-value=false public TreeMap TreeMapOfBigInteger_String56",
  "embedded-key=true  public TreeMap TreeMapOfBigInteger_String57",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfBigInteger_String58",
  "embedded-key=false  public TreeMap TreeMapOfBigInteger_String60",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfBigInteger_String62",
  "public TreeMap TreeMapOfByte_String63",
  "embedded-value=true public TreeMap TreeMapOfByte_String64",
  "embedded-value=false public TreeMap TreeMapOfByte_String65",
  "embedded-key=true  public TreeMap TreeMapOfByte_String66",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfByte_String67",
  "embedded-key=false  public TreeMap TreeMapOfByte_String69",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfByte_String71",
  "public TreeMap TreeMapOfDouble_String72",
  "embedded-value=true public TreeMap TreeMapOfDouble_String73",
  "embedded-value=false public TreeMap TreeMapOfDouble_String74",
  "embedded-key=true  public TreeMap TreeMapOfDouble_String75",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfDouble_String76",
  "embedded-key=false  public TreeMap TreeMapOfDouble_String78",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfDouble_String80",
  "public TreeMap TreeMapOfFloat_String81",
  "embedded-value=true public TreeMap TreeMapOfFloat_String82",
  "embedded-value=false public TreeMap TreeMapOfFloat_String83",
  "embedded-key=true  public TreeMap TreeMapOfFloat_String84",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfFloat_String85",
  "embedded-key=false  public TreeMap TreeMapOfFloat_String87",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfFloat_String89",
  "public TreeMap TreeMapOfInteger_String90",
  "embedded-value=true public TreeMap TreeMapOfInteger_String91",
  "embedded-value=false public TreeMap TreeMapOfInteger_String92",
  "embedded-key=true  public TreeMap TreeMapOfInteger_String93",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfInteger_String94",
  "embedded-key=false  public TreeMap TreeMapOfInteger_String96",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfInteger_String98",
  "public TreeMap TreeMapOfLong_String99",
  "embedded-value=true public TreeMap TreeMapOfLong_String100",
  "embedded-value=false public TreeMap TreeMapOfLong_String101",
  "embedded-key=true  public TreeMap TreeMapOfLong_String102",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfLong_String103",
  "embedded-key=false  public TreeMap TreeMapOfLong_String105",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfLong_String107",
  "public TreeMap TreeMapOfShort_String108",
  "embedded-value=true public TreeMap TreeMapOfShort_String109",
  "embedded-value=false public TreeMap TreeMapOfShort_String110",
  "embedded-key=true  public TreeMap TreeMapOfShort_String111",
  "embedded-key=true embedded-value=true public TreeMap TreeMapOfShort_String112",
  "embedded-key=false  public TreeMap TreeMapOfShort_String114",
  "embedded-key=false embedded-value=false public TreeMap TreeMapOfShort_String116"
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
        return TreeMapOfObject_String0;
      case(1):
        return TreeMapOfObject_String1;
      case(2):
        return TreeMapOfObject_String2;
      case(3):
        return TreeMapOfObject_String3;
      case(4):
        return TreeMapOfObject_String4;
      case(5):
        return TreeMapOfObject_String6;
      case(6):
        return TreeMapOfObject_String8;
      case(7):
        return TreeMapOfSimpleClass_String9;
      case(8):
        return TreeMapOfSimpleClass_String10;
      case(9):
        return TreeMapOfSimpleClass_String11;
      case(10):
        return TreeMapOfSimpleClass_String12;
      case(11):
        return TreeMapOfSimpleClass_String13;
      case(12):
        return TreeMapOfSimpleClass_String15;
      case(13):
        return TreeMapOfSimpleClass_String17;
      case(14):
        return TreeMapOfSimpleInterface_String18;
      case(15):
        return TreeMapOfSimpleInterface_String19;
      case(16):
        return TreeMapOfSimpleInterface_String20;
      case(17):
        return TreeMapOfSimpleInterface_String21;
      case(18):
        return TreeMapOfSimpleInterface_String22;
      case(19):
        return TreeMapOfSimpleInterface_String24;
      case(20):
        return TreeMapOfSimpleInterface_String26;
      case(21):
        return TreeMapOfString_String27;
      case(22):
        return TreeMapOfString_String28;
      case(23):
        return TreeMapOfString_String29;
      case(24):
        return TreeMapOfString_String30;
      case(25):
        return TreeMapOfString_String31;
      case(26):
        return TreeMapOfString_String33;
      case(27):
        return TreeMapOfString_String35;
      case(28):
        return TreeMapOfDate_String36;
      case(29):
        return TreeMapOfDate_String37;
      case(30):
        return TreeMapOfDate_String38;
      case(31):
        return TreeMapOfDate_String39;
      case(32):
        return TreeMapOfDate_String40;
      case(33):
        return TreeMapOfDate_String42;
      case(34):
        return TreeMapOfDate_String44;
      case(35):
        return TreeMapOfBigDecimal_String45;
      case(36):
        return TreeMapOfBigDecimal_String46;
      case(37):
        return TreeMapOfBigDecimal_String47;
      case(38):
        return TreeMapOfBigDecimal_String48;
      case(39):
        return TreeMapOfBigDecimal_String49;
      case(40):
        return TreeMapOfBigDecimal_String51;
      case(41):
        return TreeMapOfBigDecimal_String53;
      case(42):
        return TreeMapOfBigInteger_String54;
      case(43):
        return TreeMapOfBigInteger_String55;
      case(44):
        return TreeMapOfBigInteger_String56;
      case(45):
        return TreeMapOfBigInteger_String57;
      case(46):
        return TreeMapOfBigInteger_String58;
      case(47):
        return TreeMapOfBigInteger_String60;
      case(48):
        return TreeMapOfBigInteger_String62;
      case(49):
        return TreeMapOfByte_String63;
      case(50):
        return TreeMapOfByte_String64;
      case(51):
        return TreeMapOfByte_String65;
      case(52):
        return TreeMapOfByte_String66;
      case(53):
        return TreeMapOfByte_String67;
      case(54):
        return TreeMapOfByte_String69;
      case(55):
        return TreeMapOfByte_String71;
      case(56):
        return TreeMapOfDouble_String72;
      case(57):
        return TreeMapOfDouble_String73;
      case(58):
        return TreeMapOfDouble_String74;
      case(59):
        return TreeMapOfDouble_String75;
      case(60):
        return TreeMapOfDouble_String76;
      case(61):
        return TreeMapOfDouble_String78;
      case(62):
        return TreeMapOfDouble_String80;
      case(63):
        return TreeMapOfFloat_String81;
      case(64):
        return TreeMapOfFloat_String82;
      case(65):
        return TreeMapOfFloat_String83;
      case(66):
        return TreeMapOfFloat_String84;
      case(67):
        return TreeMapOfFloat_String85;
      case(68):
        return TreeMapOfFloat_String87;
      case(69):
        return TreeMapOfFloat_String89;
      case(70):
        return TreeMapOfInteger_String90;
      case(71):
        return TreeMapOfInteger_String91;
      case(72):
        return TreeMapOfInteger_String92;
      case(73):
        return TreeMapOfInteger_String93;
      case(74):
        return TreeMapOfInteger_String94;
      case(75):
        return TreeMapOfInteger_String96;
      case(76):
        return TreeMapOfInteger_String98;
      case(77):
        return TreeMapOfLong_String99;
      case(78):
        return TreeMapOfLong_String100;
      case(79):
        return TreeMapOfLong_String101;
      case(80):
        return TreeMapOfLong_String102;
      case(81):
        return TreeMapOfLong_String103;
      case(82):
        return TreeMapOfLong_String105;
      case(83):
        return TreeMapOfLong_String107;
      case(84):
        return TreeMapOfShort_String108;
      case(85):
        return TreeMapOfShort_String109;
      case(86):
        return TreeMapOfShort_String110;
      case(87):
        return TreeMapOfShort_String111;
      case(88):
        return TreeMapOfShort_String112;
      case(89):
        return TreeMapOfShort_String114;
      case(90):
        return TreeMapOfShort_String116;
      default:
        throw new IndexOutOfBoundsException();
    }
  }
  public boolean set(int index,TreeMap value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        TreeMapOfObject_String0= value;
         break;
      case(1):
        TreeMapOfObject_String1= value;
         break;
      case(2):
        TreeMapOfObject_String2= value;
         break;
      case(3):
        TreeMapOfObject_String3= value;
         break;
      case(4):
        TreeMapOfObject_String4= value;
         break;
      case(5):
        TreeMapOfObject_String6= value;
         break;
      case(6):
        TreeMapOfObject_String8= value;
         break;
      case(7):
        TreeMapOfSimpleClass_String9= value;
         break;
      case(8):
        TreeMapOfSimpleClass_String10= value;
         break;
      case(9):
        TreeMapOfSimpleClass_String11= value;
         break;
      case(10):
        TreeMapOfSimpleClass_String12= value;
         break;
      case(11):
        TreeMapOfSimpleClass_String13= value;
         break;
      case(12):
        TreeMapOfSimpleClass_String15= value;
         break;
      case(13):
        TreeMapOfSimpleClass_String17= value;
         break;
      case(14):
        TreeMapOfSimpleInterface_String18= value;
         break;
      case(15):
        TreeMapOfSimpleInterface_String19= value;
         break;
      case(16):
        TreeMapOfSimpleInterface_String20= value;
         break;
      case(17):
        TreeMapOfSimpleInterface_String21= value;
         break;
      case(18):
        TreeMapOfSimpleInterface_String22= value;
         break;
      case(19):
        TreeMapOfSimpleInterface_String24= value;
         break;
      case(20):
        TreeMapOfSimpleInterface_String26= value;
         break;
      case(21):
        TreeMapOfString_String27= value;
         break;
      case(22):
        TreeMapOfString_String28= value;
         break;
      case(23):
        TreeMapOfString_String29= value;
         break;
      case(24):
        TreeMapOfString_String30= value;
         break;
      case(25):
        TreeMapOfString_String31= value;
         break;
      case(26):
        TreeMapOfString_String33= value;
         break;
      case(27):
        TreeMapOfString_String35= value;
         break;
      case(28):
        TreeMapOfDate_String36= value;
         break;
      case(29):
        TreeMapOfDate_String37= value;
         break;
      case(30):
        TreeMapOfDate_String38= value;
         break;
      case(31):
        TreeMapOfDate_String39= value;
         break;
      case(32):
        TreeMapOfDate_String40= value;
         break;
      case(33):
        TreeMapOfDate_String42= value;
         break;
      case(34):
        TreeMapOfDate_String44= value;
         break;
      case(35):
        TreeMapOfBigDecimal_String45= value;
         break;
      case(36):
        TreeMapOfBigDecimal_String46= value;
         break;
      case(37):
        TreeMapOfBigDecimal_String47= value;
         break;
      case(38):
        TreeMapOfBigDecimal_String48= value;
         break;
      case(39):
        TreeMapOfBigDecimal_String49= value;
         break;
      case(40):
        TreeMapOfBigDecimal_String51= value;
         break;
      case(41):
        TreeMapOfBigDecimal_String53= value;
         break;
      case(42):
        TreeMapOfBigInteger_String54= value;
         break;
      case(43):
        TreeMapOfBigInteger_String55= value;
         break;
      case(44):
        TreeMapOfBigInteger_String56= value;
         break;
      case(45):
        TreeMapOfBigInteger_String57= value;
         break;
      case(46):
        TreeMapOfBigInteger_String58= value;
         break;
      case(47):
        TreeMapOfBigInteger_String60= value;
         break;
      case(48):
        TreeMapOfBigInteger_String62= value;
         break;
      case(49):
        TreeMapOfByte_String63= value;
         break;
      case(50):
        TreeMapOfByte_String64= value;
         break;
      case(51):
        TreeMapOfByte_String65= value;
         break;
      case(52):
        TreeMapOfByte_String66= value;
         break;
      case(53):
        TreeMapOfByte_String67= value;
         break;
      case(54):
        TreeMapOfByte_String69= value;
         break;
      case(55):
        TreeMapOfByte_String71= value;
         break;
      case(56):
        TreeMapOfDouble_String72= value;
         break;
      case(57):
        TreeMapOfDouble_String73= value;
         break;
      case(58):
        TreeMapOfDouble_String74= value;
         break;
      case(59):
        TreeMapOfDouble_String75= value;
         break;
      case(60):
        TreeMapOfDouble_String76= value;
         break;
      case(61):
        TreeMapOfDouble_String78= value;
         break;
      case(62):
        TreeMapOfDouble_String80= value;
         break;
      case(63):
        TreeMapOfFloat_String81= value;
         break;
      case(64):
        TreeMapOfFloat_String82= value;
         break;
      case(65):
        TreeMapOfFloat_String83= value;
         break;
      case(66):
        TreeMapOfFloat_String84= value;
         break;
      case(67):
        TreeMapOfFloat_String85= value;
         break;
      case(68):
        TreeMapOfFloat_String87= value;
         break;
      case(69):
        TreeMapOfFloat_String89= value;
         break;
      case(70):
        TreeMapOfInteger_String90= value;
         break;
      case(71):
        TreeMapOfInteger_String91= value;
         break;
      case(72):
        TreeMapOfInteger_String92= value;
         break;
      case(73):
        TreeMapOfInteger_String93= value;
         break;
      case(74):
        TreeMapOfInteger_String94= value;
         break;
      case(75):
        TreeMapOfInteger_String96= value;
         break;
      case(76):
        TreeMapOfInteger_String98= value;
         break;
      case(77):
        TreeMapOfLong_String99= value;
         break;
      case(78):
        TreeMapOfLong_String100= value;
         break;
      case(79):
        TreeMapOfLong_String101= value;
         break;
      case(80):
        TreeMapOfLong_String102= value;
         break;
      case(81):
        TreeMapOfLong_String103= value;
         break;
      case(82):
        TreeMapOfLong_String105= value;
         break;
      case(83):
        TreeMapOfLong_String107= value;
         break;
      case(84):
        TreeMapOfShort_String108= value;
         break;
      case(85):
        TreeMapOfShort_String109= value;
         break;
      case(86):
        TreeMapOfShort_String110= value;
         break;
      case(87):
        TreeMapOfShort_String111= value;
         break;
      case(88):
        TreeMapOfShort_String112= value;
         break;
      case(89):
        TreeMapOfShort_String114= value;
         break;
      case(90):
        TreeMapOfShort_String116= value;
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
