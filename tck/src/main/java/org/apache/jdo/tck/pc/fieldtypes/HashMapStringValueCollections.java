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
import java.util.HashMap;

public class HashMapStringValueCollections { 
  public int identifier;
  public HashMap<Object, String> HashMapOfObject_String0;
  public HashMap<Object, String> HashMapOfObject_String1;
  public HashMap<Object, String> HashMapOfObject_String2;
  public HashMap<Object, String> HashMapOfObject_String3;
  public HashMap<Object, String> HashMapOfObject_String4;
  public HashMap<Object, String> HashMapOfObject_String6;
  public HashMap<Object, String> HashMapOfObject_String8;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String9;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String10;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String11;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String12;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String13;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String15;
  public HashMap<SimpleClass, String> HashMapOfSimpleClass_String17;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String18;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String19;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String20;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String21;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String22;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String24;
  public HashMap<SimpleInterface, String> HashMapOfSimpleInterface_String26;
  public HashMap<String, String> HashMapOfString_String27;
  public HashMap<String, String> HashMapOfString_String28;
  public HashMap<String, String> HashMapOfString_String29;
  public HashMap<String, String> HashMapOfString_String30;
  public HashMap<String, String> HashMapOfString_String31;
  public HashMap<String, String> HashMapOfString_String33;
  public HashMap<String, String> HashMapOfString_String35;
  public HashMap<Date, String> HashMapOfDate_String36;
  public HashMap<Date, String> HashMapOfDate_String37;
  public HashMap<Date, String> HashMapOfDate_String38;
  public HashMap<Date, String> HashMapOfDate_String39;
  public HashMap<Date, String> HashMapOfDate_String40;
  public HashMap<Date, String> HashMapOfDate_String42;
  public HashMap<Date, String> HashMapOfDate_String44;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String45;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String46;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String47;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String48;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String49;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String51;
  public HashMap<BigDecimal, String> HashMapOfBigDecimal_String53;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String54;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String55;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String56;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String57;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String58;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String60;
  public HashMap<BigInteger, String> HashMapOfBigInteger_String62;
  public HashMap<Byte, String> HashMapOfByte_String63;
  public HashMap<Byte, String> HashMapOfByte_String64;
  public HashMap<Byte, String> HashMapOfByte_String65;
  public HashMap<Byte, String> HashMapOfByte_String66;
  public HashMap<Byte, String> HashMapOfByte_String67;
  public HashMap<Byte, String> HashMapOfByte_String69;
  public HashMap<Byte, String> HashMapOfByte_String71;
  public HashMap<Double, String> HashMapOfDouble_String72;
  public HashMap<Double, String> HashMapOfDouble_String73;
  public HashMap<Double, String> HashMapOfDouble_String74;
  public HashMap<Double, String> HashMapOfDouble_String75;
  public HashMap<Double, String> HashMapOfDouble_String76;
  public HashMap<Double, String> HashMapOfDouble_String78;
  public HashMap<Double, String> HashMapOfDouble_String80;
  public HashMap<Float, String> HashMapOfFloat_String81;
  public HashMap<Float, String> HashMapOfFloat_String82;
  public HashMap<Float, String> HashMapOfFloat_String83;
  public HashMap<Float, String> HashMapOfFloat_String84;
  public HashMap<Float, String> HashMapOfFloat_String85;
  public HashMap<Float, String> HashMapOfFloat_String87;
  public HashMap<Float, String> HashMapOfFloat_String89;
  public HashMap<Integer, String> HashMapOfInteger_String90;
  public HashMap<Integer, String> HashMapOfInteger_String91;
  public HashMap<Integer, String> HashMapOfInteger_String92;
  public HashMap<Integer, String> HashMapOfInteger_String93;
  public HashMap<Integer, String> HashMapOfInteger_String94;
  public HashMap<Integer, String> HashMapOfInteger_String96;
  public HashMap<Integer, String> HashMapOfInteger_String98;
  public HashMap<Long, String> HashMapOfLong_String99;
  public HashMap<Long, String> HashMapOfLong_String100;
  public HashMap<Long, String> HashMapOfLong_String101;
  public HashMap<Long, String> HashMapOfLong_String102;
  public HashMap<Long, String> HashMapOfLong_String103;
  public HashMap<Long, String> HashMapOfLong_String105;
  public HashMap<Long, String> HashMapOfLong_String107;
  public HashMap<Short, String> HashMapOfShort_String108;
  public HashMap<Short, String> HashMapOfShort_String109;
  public HashMap<Short, String> HashMapOfShort_String110;
  public HashMap<Short, String> HashMapOfShort_String111;
  public HashMap<Short, String> HashMapOfShort_String112;
  public HashMap<Short, String> HashMapOfShort_String114;
  public HashMap<Short, String> HashMapOfShort_String116;

  public static final String [] fieldSpecs = { 
  "public HashMap HashMapOfObject_String0",
  "embedded-value=true public HashMap HashMapOfObject_String1",
  "embedded-value=false public HashMap HashMapOfObject_String2",
  "embedded-key=true  public HashMap HashMapOfObject_String3",
  "embedded-key=true embedded-value=true public HashMap HashMapOfObject_String4",
  "embedded-key=false  public HashMap HashMapOfObject_String6",
  "embedded-key=false embedded-value=false public HashMap HashMapOfObject_String8",
  "public HashMap HashMapOfSimpleClass_String9",
  "embedded-value=true public HashMap HashMapOfSimpleClass_String10",
  "embedded-value=false public HashMap HashMapOfSimpleClass_String11",
  "embedded-key=true  public HashMap HashMapOfSimpleClass_String12",
  "embedded-key=true embedded-value=true public HashMap HashMapOfSimpleClass_String13",
  "embedded-key=false  public HashMap HashMapOfSimpleClass_String15",
  "embedded-key=false embedded-value=false public HashMap HashMapOfSimpleClass_String17",
  "public HashMap HashMapOfSimpleInterface_String18",
  "embedded-value=true public HashMap HashMapOfSimpleInterface_String19",
  "embedded-value=false public HashMap HashMapOfSimpleInterface_String20",
  "embedded-key=true  public HashMap HashMapOfSimpleInterface_String21",
  "embedded-key=true embedded-value=true public HashMap HashMapOfSimpleInterface_String22",
  "embedded-key=false  public HashMap HashMapOfSimpleInterface_String24",
  "embedded-key=false embedded-value=false public HashMap HashMapOfSimpleInterface_String26",
  "public HashMap HashMapOfString_String27",
  "embedded-value=true public HashMap HashMapOfString_String28",
  "embedded-value=false public HashMap HashMapOfString_String29",
  "embedded-key=true  public HashMap HashMapOfString_String30",
  "embedded-key=true embedded-value=true public HashMap HashMapOfString_String31",
  "embedded-key=false  public HashMap HashMapOfString_String33",
  "embedded-key=false embedded-value=false public HashMap HashMapOfString_String35",
  "public HashMap HashMapOfDate_String36",
  "embedded-value=true public HashMap HashMapOfDate_String37",
  "embedded-value=false public HashMap HashMapOfDate_String38",
  "embedded-key=true  public HashMap HashMapOfDate_String39",
  "embedded-key=true embedded-value=true public HashMap HashMapOfDate_String40",
  "embedded-key=false  public HashMap HashMapOfDate_String42",
  "embedded-key=false embedded-value=false public HashMap HashMapOfDate_String44",
  "public HashMap HashMapOfBigDecimal_String45",
  "embedded-value=true public HashMap HashMapOfBigDecimal_String46",
  "embedded-value=false public HashMap HashMapOfBigDecimal_String47",
  "embedded-key=true  public HashMap HashMapOfBigDecimal_String48",
  "embedded-key=true embedded-value=true public HashMap HashMapOfBigDecimal_String49",
  "embedded-key=false  public HashMap HashMapOfBigDecimal_String51",
  "embedded-key=false embedded-value=false public HashMap HashMapOfBigDecimal_String53",
  "public HashMap HashMapOfBigInteger_String54",
  "embedded-value=true public HashMap HashMapOfBigInteger_String55",
  "embedded-value=false public HashMap HashMapOfBigInteger_String56",
  "embedded-key=true  public HashMap HashMapOfBigInteger_String57",
  "embedded-key=true embedded-value=true public HashMap HashMapOfBigInteger_String58",
  "embedded-key=false  public HashMap HashMapOfBigInteger_String60",
  "embedded-key=false embedded-value=false public HashMap HashMapOfBigInteger_String62",
  "public HashMap HashMapOfByte_String63",
  "embedded-value=true public HashMap HashMapOfByte_String64",
  "embedded-value=false public HashMap HashMapOfByte_String65",
  "embedded-key=true  public HashMap HashMapOfByte_String66",
  "embedded-key=true embedded-value=true public HashMap HashMapOfByte_String67",
  "embedded-key=false  public HashMap HashMapOfByte_String69",
  "embedded-key=false embedded-value=false public HashMap HashMapOfByte_String71",
  "public HashMap HashMapOfDouble_String72",
  "embedded-value=true public HashMap HashMapOfDouble_String73",
  "embedded-value=false public HashMap HashMapOfDouble_String74",
  "embedded-key=true  public HashMap HashMapOfDouble_String75",
  "embedded-key=true embedded-value=true public HashMap HashMapOfDouble_String76",
  "embedded-key=false  public HashMap HashMapOfDouble_String78",
  "embedded-key=false embedded-value=false public HashMap HashMapOfDouble_String80",
  "public HashMap HashMapOfFloat_String81",
  "embedded-value=true public HashMap HashMapOfFloat_String82",
  "embedded-value=false public HashMap HashMapOfFloat_String83",
  "embedded-key=true  public HashMap HashMapOfFloat_String84",
  "embedded-key=true embedded-value=true public HashMap HashMapOfFloat_String85",
  "embedded-key=false  public HashMap HashMapOfFloat_String87",
  "embedded-key=false embedded-value=false public HashMap HashMapOfFloat_String89",
  "public HashMap HashMapOfInteger_String90",
  "embedded-value=true public HashMap HashMapOfInteger_String91",
  "embedded-value=false public HashMap HashMapOfInteger_String92",
  "embedded-key=true  public HashMap HashMapOfInteger_String93",
  "embedded-key=true embedded-value=true public HashMap HashMapOfInteger_String94",
  "embedded-key=false  public HashMap HashMapOfInteger_String96",
  "embedded-key=false embedded-value=false public HashMap HashMapOfInteger_String98",
  "public HashMap HashMapOfLong_String99",
  "embedded-value=true public HashMap HashMapOfLong_String100",
  "embedded-value=false public HashMap HashMapOfLong_String101",
  "embedded-key=true  public HashMap HashMapOfLong_String102",
  "embedded-key=true embedded-value=true public HashMap HashMapOfLong_String103",
  "embedded-key=false  public HashMap HashMapOfLong_String105",
  "embedded-key=false embedded-value=false public HashMap HashMapOfLong_String107",
  "public HashMap HashMapOfShort_String108",
  "embedded-value=true public HashMap HashMapOfShort_String109",
  "embedded-value=false public HashMap HashMapOfShort_String110",
  "embedded-key=true  public HashMap HashMapOfShort_String111",
  "embedded-key=true embedded-value=true public HashMap HashMapOfShort_String112",
  "embedded-key=false  public HashMap HashMapOfShort_String114",
  "embedded-key=false embedded-value=false public HashMap HashMapOfShort_String116"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public HashMap<?, ?> get(int index)
  {
    switch (index)
    {
      case(0):
        return HashMapOfObject_String0;
      case(1):
        return HashMapOfObject_String1;
      case(2):
        return HashMapOfObject_String2;
      case(3):
        return HashMapOfObject_String3;
      case(4):
        return HashMapOfObject_String4;
      case(5):
        return HashMapOfObject_String6;
      case(6):
        return HashMapOfObject_String8;
      case(7):
        return HashMapOfSimpleClass_String9;
      case(8):
        return HashMapOfSimpleClass_String10;
      case(9):
        return HashMapOfSimpleClass_String11;
      case(10):
        return HashMapOfSimpleClass_String12;
      case(11):
        return HashMapOfSimpleClass_String13;
      case(12):
        return HashMapOfSimpleClass_String15;
      case(13):
        return HashMapOfSimpleClass_String17;
      case(14):
        return HashMapOfSimpleInterface_String18;
      case(15):
        return HashMapOfSimpleInterface_String19;
      case(16):
        return HashMapOfSimpleInterface_String20;
      case(17):
        return HashMapOfSimpleInterface_String21;
      case(18):
        return HashMapOfSimpleInterface_String22;
      case(19):
        return HashMapOfSimpleInterface_String24;
      case(20):
        return HashMapOfSimpleInterface_String26;
      case(21):
        return HashMapOfString_String27;
      case(22):
        return HashMapOfString_String28;
      case(23):
        return HashMapOfString_String29;
      case(24):
        return HashMapOfString_String30;
      case(25):
        return HashMapOfString_String31;
      case(26):
        return HashMapOfString_String33;
      case(27):
        return HashMapOfString_String35;
      case(28):
        return HashMapOfDate_String36;
      case(29):
        return HashMapOfDate_String37;
      case(30):
        return HashMapOfDate_String38;
      case(31):
        return HashMapOfDate_String39;
      case(32):
        return HashMapOfDate_String40;
      case(33):
        return HashMapOfDate_String42;
      case(34):
        return HashMapOfDate_String44;
      case(35):
        return HashMapOfBigDecimal_String45;
      case(36):
        return HashMapOfBigDecimal_String46;
      case(37):
        return HashMapOfBigDecimal_String47;
      case(38):
        return HashMapOfBigDecimal_String48;
      case(39):
        return HashMapOfBigDecimal_String49;
      case(40):
        return HashMapOfBigDecimal_String51;
      case(41):
        return HashMapOfBigDecimal_String53;
      case(42):
        return HashMapOfBigInteger_String54;
      case(43):
        return HashMapOfBigInteger_String55;
      case(44):
        return HashMapOfBigInteger_String56;
      case(45):
        return HashMapOfBigInteger_String57;
      case(46):
        return HashMapOfBigInteger_String58;
      case(47):
        return HashMapOfBigInteger_String60;
      case(48):
        return HashMapOfBigInteger_String62;
      case(49):
        return HashMapOfByte_String63;
      case(50):
        return HashMapOfByte_String64;
      case(51):
        return HashMapOfByte_String65;
      case(52):
        return HashMapOfByte_String66;
      case(53):
        return HashMapOfByte_String67;
      case(54):
        return HashMapOfByte_String69;
      case(55):
        return HashMapOfByte_String71;
      case(56):
        return HashMapOfDouble_String72;
      case(57):
        return HashMapOfDouble_String73;
      case(58):
        return HashMapOfDouble_String74;
      case(59):
        return HashMapOfDouble_String75;
      case(60):
        return HashMapOfDouble_String76;
      case(61):
        return HashMapOfDouble_String78;
      case(62):
        return HashMapOfDouble_String80;
      case(63):
        return HashMapOfFloat_String81;
      case(64):
        return HashMapOfFloat_String82;
      case(65):
        return HashMapOfFloat_String83;
      case(66):
        return HashMapOfFloat_String84;
      case(67):
        return HashMapOfFloat_String85;
      case(68):
        return HashMapOfFloat_String87;
      case(69):
        return HashMapOfFloat_String89;
      case(70):
        return HashMapOfInteger_String90;
      case(71):
        return HashMapOfInteger_String91;
      case(72):
        return HashMapOfInteger_String92;
      case(73):
        return HashMapOfInteger_String93;
      case(74):
        return HashMapOfInteger_String94;
      case(75):
        return HashMapOfInteger_String96;
      case(76):
        return HashMapOfInteger_String98;
      case(77):
        return HashMapOfLong_String99;
      case(78):
        return HashMapOfLong_String100;
      case(79):
        return HashMapOfLong_String101;
      case(80):
        return HashMapOfLong_String102;
      case(81):
        return HashMapOfLong_String103;
      case(82):
        return HashMapOfLong_String105;
      case(83):
        return HashMapOfLong_String107;
      case(84):
        return HashMapOfShort_String108;
      case(85):
        return HashMapOfShort_String109;
      case(86):
        return HashMapOfShort_String110;
      case(87):
        return HashMapOfShort_String111;
      case(88):
        return HashMapOfShort_String112;
      case(89):
        return HashMapOfShort_String114;
      case(90):
        return HashMapOfShort_String116;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") HashMap value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        HashMapOfObject_String0= value;
         break;
      case(1):
        HashMapOfObject_String1= value;
         break;
      case(2):
        HashMapOfObject_String2= value;
         break;
      case(3):
        HashMapOfObject_String3= value;
         break;
      case(4):
        HashMapOfObject_String4= value;
         break;
      case(5):
        HashMapOfObject_String6= value;
         break;
      case(6):
        HashMapOfObject_String8= value;
         break;
      case(7):
        HashMapOfSimpleClass_String9= value;
         break;
      case(8):
        HashMapOfSimpleClass_String10= value;
         break;
      case(9):
        HashMapOfSimpleClass_String11= value;
         break;
      case(10):
        HashMapOfSimpleClass_String12= value;
         break;
      case(11):
        HashMapOfSimpleClass_String13= value;
         break;
      case(12):
        HashMapOfSimpleClass_String15= value;
         break;
      case(13):
        HashMapOfSimpleClass_String17= value;
         break;
      case(14):
        HashMapOfSimpleInterface_String18= value;
         break;
      case(15):
        HashMapOfSimpleInterface_String19= value;
         break;
      case(16):
        HashMapOfSimpleInterface_String20= value;
         break;
      case(17):
        HashMapOfSimpleInterface_String21= value;
         break;
      case(18):
        HashMapOfSimpleInterface_String22= value;
         break;
      case(19):
        HashMapOfSimpleInterface_String24= value;
         break;
      case(20):
        HashMapOfSimpleInterface_String26= value;
         break;
      case(21):
        HashMapOfString_String27= value;
         break;
      case(22):
        HashMapOfString_String28= value;
         break;
      case(23):
        HashMapOfString_String29= value;
         break;
      case(24):
        HashMapOfString_String30= value;
         break;
      case(25):
        HashMapOfString_String31= value;
         break;
      case(26):
        HashMapOfString_String33= value;
         break;
      case(27):
        HashMapOfString_String35= value;
         break;
      case(28):
        HashMapOfDate_String36= value;
         break;
      case(29):
        HashMapOfDate_String37= value;
         break;
      case(30):
        HashMapOfDate_String38= value;
         break;
      case(31):
        HashMapOfDate_String39= value;
         break;
      case(32):
        HashMapOfDate_String40= value;
         break;
      case(33):
        HashMapOfDate_String42= value;
         break;
      case(34):
        HashMapOfDate_String44= value;
         break;
      case(35):
        HashMapOfBigDecimal_String45= value;
         break;
      case(36):
        HashMapOfBigDecimal_String46= value;
         break;
      case(37):
        HashMapOfBigDecimal_String47= value;
         break;
      case(38):
        HashMapOfBigDecimal_String48= value;
         break;
      case(39):
        HashMapOfBigDecimal_String49= value;
         break;
      case(40):
        HashMapOfBigDecimal_String51= value;
         break;
      case(41):
        HashMapOfBigDecimal_String53= value;
         break;
      case(42):
        HashMapOfBigInteger_String54= value;
         break;
      case(43):
        HashMapOfBigInteger_String55= value;
         break;
      case(44):
        HashMapOfBigInteger_String56= value;
         break;
      case(45):
        HashMapOfBigInteger_String57= value;
         break;
      case(46):
        HashMapOfBigInteger_String58= value;
         break;
      case(47):
        HashMapOfBigInteger_String60= value;
         break;
      case(48):
        HashMapOfBigInteger_String62= value;
         break;
      case(49):
        HashMapOfByte_String63= value;
         break;
      case(50):
        HashMapOfByte_String64= value;
         break;
      case(51):
        HashMapOfByte_String65= value;
         break;
      case(52):
        HashMapOfByte_String66= value;
         break;
      case(53):
        HashMapOfByte_String67= value;
         break;
      case(54):
        HashMapOfByte_String69= value;
         break;
      case(55):
        HashMapOfByte_String71= value;
         break;
      case(56):
        HashMapOfDouble_String72= value;
         break;
      case(57):
        HashMapOfDouble_String73= value;
         break;
      case(58):
        HashMapOfDouble_String74= value;
         break;
      case(59):
        HashMapOfDouble_String75= value;
         break;
      case(60):
        HashMapOfDouble_String76= value;
         break;
      case(61):
        HashMapOfDouble_String78= value;
         break;
      case(62):
        HashMapOfDouble_String80= value;
         break;
      case(63):
        HashMapOfFloat_String81= value;
         break;
      case(64):
        HashMapOfFloat_String82= value;
         break;
      case(65):
        HashMapOfFloat_String83= value;
         break;
      case(66):
        HashMapOfFloat_String84= value;
         break;
      case(67):
        HashMapOfFloat_String85= value;
         break;
      case(68):
        HashMapOfFloat_String87= value;
         break;
      case(69):
        HashMapOfFloat_String89= value;
         break;
      case(70):
        HashMapOfInteger_String90= value;
         break;
      case(71):
        HashMapOfInteger_String91= value;
         break;
      case(72):
        HashMapOfInteger_String92= value;
         break;
      case(73):
        HashMapOfInteger_String93= value;
         break;
      case(74):
        HashMapOfInteger_String94= value;
         break;
      case(75):
        HashMapOfInteger_String96= value;
         break;
      case(76):
        HashMapOfInteger_String98= value;
         break;
      case(77):
        HashMapOfLong_String99= value;
         break;
      case(78):
        HashMapOfLong_String100= value;
         break;
      case(79):
        HashMapOfLong_String101= value;
         break;
      case(80):
        HashMapOfLong_String102= value;
         break;
      case(81):
        HashMapOfLong_String103= value;
         break;
      case(82):
        HashMapOfLong_String105= value;
         break;
      case(83):
        HashMapOfLong_String107= value;
         break;
      case(84):
        HashMapOfShort_String108= value;
         break;
      case(85):
        HashMapOfShort_String109= value;
         break;
      case(86):
        HashMapOfShort_String110= value;
         break;
      case(87):
        HashMapOfShort_String111= value;
         break;
      case(88):
        HashMapOfShort_String112= value;
         break;
      case(89):
        HashMapOfShort_String114= value;
         break;
      case(90):
        HashMapOfShort_String116= value;
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
