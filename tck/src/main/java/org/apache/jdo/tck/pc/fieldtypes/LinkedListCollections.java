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
import java.util.LinkedList;
import java.util.Locale;

public class LinkedListCollections { 
  public int identifier;
  public LinkedList<Object> LinkedListOfObject0;
  public LinkedList<Object> LinkedListOfObject1;
  public LinkedList<Object> LinkedListOfObject2;
  public LinkedList<SimpleClass> LinkedListOfSimpleClass3;
  public LinkedList<SimpleClass> LinkedListOfSimpleClass4;
  public LinkedList<SimpleClass> LinkedListOfSimpleClass5;
  public LinkedList<SimpleInterface> LinkedListOfSimpleInterface6;
  public LinkedList<SimpleInterface> LinkedListOfSimpleInterface7;
  public LinkedList<SimpleInterface> LinkedListOfSimpleInterface8;
  public LinkedList<String> LinkedListOfString9;
  public LinkedList<String> LinkedListOfString10;
  public LinkedList<String> LinkedListOfString11;
  public LinkedList<Date> LinkedListOfDate12;
  public LinkedList<Date> LinkedListOfDate13;
  public LinkedList<Date> LinkedListOfDate14;
  public LinkedList<Locale> LinkedListOfLocale15;
  public LinkedList<Locale> LinkedListOfLocale16;
  public LinkedList<Locale> LinkedListOfLocale17;
  public LinkedList<BigDecimal> LinkedListOfBigDecimal18;
  public LinkedList<BigDecimal> LinkedListOfBigDecimal19;
  public LinkedList<BigDecimal> LinkedListOfBigDecimal20;
  public LinkedList<BigInteger> LinkedListOfBigInteger21;
  public LinkedList<BigInteger> LinkedListOfBigInteger22;
  public LinkedList<BigInteger> LinkedListOfBigInteger23;
  public LinkedList<Byte> LinkedListOfByte24;
  public LinkedList<Byte> LinkedListOfByte25;
  public LinkedList<Byte> LinkedListOfByte26;
  public LinkedList<Double> LinkedListOfDouble27;
  public LinkedList<Double> LinkedListOfDouble28;
  public LinkedList<Double> LinkedListOfDouble29;
  public LinkedList<Float> LinkedListOfFloat30;
  public LinkedList<Float> LinkedListOfFloat31;
  public LinkedList<Float> LinkedListOfFloat32;
  public LinkedList<Integer> LinkedListOfInteger33;
  public LinkedList<Integer> LinkedListOfInteger34;
  public LinkedList<Integer> LinkedListOfInteger35;
  public LinkedList<Long> LinkedListOfLong36;
  public LinkedList<Long> LinkedListOfLong37;
  public LinkedList<Long> LinkedListOfLong38;
  public LinkedList<Short> LinkedListOfShort39;
  public LinkedList<Short> LinkedListOfShort40;
  public LinkedList<Short> LinkedListOfShort41;
  public LinkedList<SimpleClass> LinkedListOfSimpleClass42;

  public static final String [] fieldSpecs = { 
  "public LinkedList LinkedListOfObject0",
  "embedded-element=true public LinkedList LinkedListOfObject1",
  "embedded-element=false public LinkedList LinkedListOfObject2",
  "public LinkedList LinkedListOfSimpleClass3",
  "embedded-element=true public LinkedList LinkedListOfSimpleClass4",
  "embedded-element=false public LinkedList LinkedListOfSimpleClass5",
  "public LinkedList LinkedListOfSimpleInterface6",
  "embedded-element=true public LinkedList LinkedListOfSimpleInterface7",
  "embedded-element=false public LinkedList LinkedListOfSimpleInterface8",
  "public LinkedList LinkedListOfString9",
  "embedded-element=true public LinkedList LinkedListOfString10",
  "embedded-element=false public LinkedList LinkedListOfString11",
  "public LinkedList LinkedListOfDate12",
  "embedded-element=true public LinkedList LinkedListOfDate13",
  "embedded-element=false public LinkedList LinkedListOfDate14",
  "public LinkedList LinkedListOfLocale15",
  "embedded-element=true public LinkedList LinkedListOfLocale16",
  "embedded-element=false public LinkedList LinkedListOfLocale17",
  "public LinkedList LinkedListOfBigDecimal18",
  "embedded-element=true public LinkedList LinkedListOfBigDecimal19",
  "embedded-element=false public LinkedList LinkedListOfBigDecimal20",
  "public LinkedList LinkedListOfBigInteger21",
  "embedded-element=true public LinkedList LinkedListOfBigInteger22",
  "embedded-element=false public LinkedList LinkedListOfBigInteger23",
  "public LinkedList LinkedListOfByte24",
  "embedded-element=true public LinkedList LinkedListOfByte25",
  "embedded-element=false public LinkedList LinkedListOfByte26",
  "public LinkedList LinkedListOfDouble27",
  "embedded-element=true public LinkedList LinkedListOfDouble28",
  "embedded-element=false public LinkedList LinkedListOfDouble29",
  "public LinkedList LinkedListOfFloat30",
  "embedded-element=true public LinkedList LinkedListOfFloat31",
  "embedded-element=false public LinkedList LinkedListOfFloat32",
  "public LinkedList LinkedListOfInteger33",
  "embedded-element=true public LinkedList LinkedListOfInteger34",
  "embedded-element=false public LinkedList LinkedListOfInteger35",
  "public LinkedList LinkedListOfLong36",
  "embedded-element=true public LinkedList LinkedListOfLong37",
  "embedded-element=false public LinkedList LinkedListOfLong38",
  "public LinkedList LinkedListOfShort39",
  "embedded-element=true public LinkedList LinkedListOfShort40",
  "embedded-element=false public LinkedList LinkedListOfShort41",
  "serialized=true public LinkedList LinkedListOfSimpleClass42"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public LinkedList<?> get(int index)
  {
    switch (index)
    {
      case(0):
        return LinkedListOfObject0;
      case(1):
        return LinkedListOfObject1;
      case(2):
        return LinkedListOfObject2;
      case(3):
        return LinkedListOfSimpleClass3;
      case(4):
        return LinkedListOfSimpleClass4;
      case(5):
        return LinkedListOfSimpleClass5;
      case(6):
        return LinkedListOfSimpleInterface6;
      case(7):
        return LinkedListOfSimpleInterface7;
      case(8):
        return LinkedListOfSimpleInterface8;
      case(9):
        return LinkedListOfString9;
      case(10):
        return LinkedListOfString10;
      case(11):
        return LinkedListOfString11;
      case(12):
        return LinkedListOfDate12;
      case(13):
        return LinkedListOfDate13;
      case(14):
        return LinkedListOfDate14;
      case(15):
        return LinkedListOfLocale15;
      case(16):
        return LinkedListOfLocale16;
      case(17):
        return LinkedListOfLocale17;
      case(18):
        return LinkedListOfBigDecimal18;
      case(19):
        return LinkedListOfBigDecimal19;
      case(20):
        return LinkedListOfBigDecimal20;
      case(21):
        return LinkedListOfBigInteger21;
      case(22):
        return LinkedListOfBigInteger22;
      case(23):
        return LinkedListOfBigInteger23;
      case(24):
        return LinkedListOfByte24;
      case(25):
        return LinkedListOfByte25;
      case(26):
        return LinkedListOfByte26;
      case(27):
        return LinkedListOfDouble27;
      case(28):
        return LinkedListOfDouble28;
      case(29):
        return LinkedListOfDouble29;
      case(30):
        return LinkedListOfFloat30;
      case(31):
        return LinkedListOfFloat31;
      case(32):
        return LinkedListOfFloat32;
      case(33):
        return LinkedListOfInteger33;
      case(34):
        return LinkedListOfInteger34;
      case(35):
        return LinkedListOfInteger35;
      case(36):
        return LinkedListOfLong36;
      case(37):
        return LinkedListOfLong37;
      case(38):
        return LinkedListOfLong38;
      case(39):
        return LinkedListOfShort39;
      case(40):
        return LinkedListOfShort40;
      case(41):
        return LinkedListOfShort41;
      case(42):
        return LinkedListOfSimpleClass42;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") LinkedList value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        LinkedListOfObject0= value;
         break;
      case(1):
        LinkedListOfObject1= value;
         break;
      case(2):
        LinkedListOfObject2= value;
         break;
      case(3):
        LinkedListOfSimpleClass3= value;
         break;
      case(4):
        LinkedListOfSimpleClass4= value;
         break;
      case(5):
        LinkedListOfSimpleClass5= value;
         break;
      case(6):
        LinkedListOfSimpleInterface6= value;
         break;
      case(7):
        LinkedListOfSimpleInterface7= value;
         break;
      case(8):
        LinkedListOfSimpleInterface8= value;
         break;
      case(9):
        LinkedListOfString9= value;
         break;
      case(10):
        LinkedListOfString10= value;
         break;
      case(11):
        LinkedListOfString11= value;
         break;
      case(12):
        LinkedListOfDate12= value;
         break;
      case(13):
        LinkedListOfDate13= value;
         break;
      case(14):
        LinkedListOfDate14= value;
         break;
      case(15):
        LinkedListOfLocale15= value;
         break;
      case(16):
        LinkedListOfLocale16= value;
         break;
      case(17):
        LinkedListOfLocale17= value;
         break;
      case(18):
        LinkedListOfBigDecimal18= value;
         break;
      case(19):
        LinkedListOfBigDecimal19= value;
         break;
      case(20):
        LinkedListOfBigDecimal20= value;
         break;
      case(21):
        LinkedListOfBigInteger21= value;
         break;
      case(22):
        LinkedListOfBigInteger22= value;
         break;
      case(23):
        LinkedListOfBigInteger23= value;
         break;
      case(24):
        LinkedListOfByte24= value;
         break;
      case(25):
        LinkedListOfByte25= value;
         break;
      case(26):
        LinkedListOfByte26= value;
         break;
      case(27):
        LinkedListOfDouble27= value;
         break;
      case(28):
        LinkedListOfDouble28= value;
         break;
      case(29):
        LinkedListOfDouble29= value;
         break;
      case(30):
        LinkedListOfFloat30= value;
         break;
      case(31):
        LinkedListOfFloat31= value;
         break;
      case(32):
        LinkedListOfFloat32= value;
         break;
      case(33):
        LinkedListOfInteger33= value;
         break;
      case(34):
        LinkedListOfInteger34= value;
         break;
      case(35):
        LinkedListOfInteger35= value;
         break;
      case(36):
        LinkedListOfLong36= value;
         break;
      case(37):
        LinkedListOfLong37= value;
         break;
      case(38):
        LinkedListOfLong38= value;
         break;
      case(39):
        LinkedListOfShort39= value;
         break;
      case(40):
        LinkedListOfShort40= value;
         break;
      case(41):
        LinkedListOfShort41= value;
         break;
      case(42):
        LinkedListOfSimpleClass42= value;
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
