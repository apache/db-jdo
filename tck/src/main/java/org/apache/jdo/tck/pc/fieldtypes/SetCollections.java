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
import java.util.Set;

public class SetCollections { 
  public int identifier;
  public Set<Object> SetOfObject0;
  public Set<Object> SetOfObject1;
  public Set<Object> SetOfObject2;
  public Set<SimpleClass> SetOfSimpleClass3;
  public Set<SimpleClass> SetOfSimpleClass4;
  public Set<SimpleClass> SetOfSimpleClass5;
  public Set<SimpleInterface> SetOfSimpleInterface6;
  public Set<SimpleInterface> SetOfSimpleInterface7;
  public Set<SimpleInterface> SetOfSimpleInterface8;
  public Set<String> SetOfString9;
  public Set<String> SetOfString10;
  public Set<String> SetOfString11;
  public Set<Date> SetOfDate12;
  public Set<Date> SetOfDate13;
  public Set<Date> SetOfDate14;
  public Set<Locale> SetOfLocale15;
  public Set<Locale> SetOfLocale16;
  public Set<Locale> SetOfLocale17;
  public Set<BigDecimal> SetOfBigDecimal18;
  public Set<BigDecimal> SetOfBigDecimal19;
  public Set<BigDecimal> SetOfBigDecimal20;
  public Set<BigInteger> SetOfBigInteger21;
  public Set<BigInteger> SetOfBigInteger22;
  public Set<BigInteger> SetOfBigInteger23;
  public Set<Byte> SetOfByte24;
  public Set<Byte> SetOfByte25;
  public Set<Byte> SetOfByte26;
  public Set<Double> SetOfDouble27;
  public Set<Double> SetOfDouble28;
  public Set<Double> SetOfDouble29;
  public Set<Float> SetOfFloat30;
  public Set<Float> SetOfFloat31;
  public Set<Float> SetOfFloat32;
  public Set<Integer> SetOfInteger33;
  public Set<Integer> SetOfInteger34;
  public Set<Integer> SetOfInteger35;
  public Set<Long> SetOfLong36;
  public Set<Long> SetOfLong37;
  public Set<Long> SetOfLong38;
  public Set<Short> SetOfShort39;
  public Set<Short> SetOfShort40;
  public Set<Short> SetOfShort41;
  public Set<SimpleClass> SetOfSimpleClass42;

  public static final String [] fieldSpecs = { 
  "public Set SetOfObject0",
  "embedded-element=true public Set SetOfObject1",
  "embedded-element=false public Set SetOfObject2",
  "public Set SetOfSimpleClass3",
  "embedded-element=true public Set SetOfSimpleClass4",
  "embedded-element=false public Set SetOfSimpleClass5",
  "public Set SetOfSimpleInterface6",
  "embedded-element=true public Set SetOfSimpleInterface7",
  "embedded-element=false public Set SetOfSimpleInterface8",
  "public Set SetOfString9",
  "embedded-element=true public Set SetOfString10",
  "embedded-element=false public Set SetOfString11",
  "public Set SetOfDate12",
  "embedded-element=true public Set SetOfDate13",
  "embedded-element=false public Set SetOfDate14",
  "public Set SetOfLocale15",
  "embedded-element=true public Set SetOfLocale16",
  "embedded-element=false public Set SetOfLocale17",
  "public Set SetOfBigDecimal18",
  "embedded-element=true public Set SetOfBigDecimal19",
  "embedded-element=false public Set SetOfBigDecimal20",
  "public Set SetOfBigInteger21",
  "embedded-element=true public Set SetOfBigInteger22",
  "embedded-element=false public Set SetOfBigInteger23",
  "public Set SetOfByte24",
  "embedded-element=true public Set SetOfByte25",
  "embedded-element=false public Set SetOfByte26",
  "public Set SetOfDouble27",
  "embedded-element=true public Set SetOfDouble28",
  "embedded-element=false public Set SetOfDouble29",
  "public Set SetOfFloat30",
  "embedded-element=true public Set SetOfFloat31",
  "embedded-element=false public Set SetOfFloat32",
  "public Set SetOfInteger33",
  "embedded-element=true public Set SetOfInteger34",
  "embedded-element=false public Set SetOfInteger35",
  "public Set SetOfLong36",
  "embedded-element=true public Set SetOfLong37",
  "embedded-element=false public Set SetOfLong38",
  "public Set SetOfShort39",
  "embedded-element=true public Set SetOfShort40",
  "embedded-element=false public Set SetOfShort41",
  "serialized=true public Set SetOfSimpleClass42"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public Set<?> get(int index)
  {
    switch (index)
    {
      case(0):
        return SetOfObject0;
      case(1):
        return SetOfObject1;
      case(2):
        return SetOfObject2;
      case(3):
        return SetOfSimpleClass3;
      case(4):
        return SetOfSimpleClass4;
      case(5):
        return SetOfSimpleClass5;
      case(6):
        return SetOfSimpleInterface6;
      case(7):
        return SetOfSimpleInterface7;
      case(8):
        return SetOfSimpleInterface8;
      case(9):
        return SetOfString9;
      case(10):
        return SetOfString10;
      case(11):
        return SetOfString11;
      case(12):
        return SetOfDate12;
      case(13):
        return SetOfDate13;
      case(14):
        return SetOfDate14;
      case(15):
        return SetOfLocale15;
      case(16):
        return SetOfLocale16;
      case(17):
        return SetOfLocale17;
      case(18):
        return SetOfBigDecimal18;
      case(19):
        return SetOfBigDecimal19;
      case(20):
        return SetOfBigDecimal20;
      case(21):
        return SetOfBigInteger21;
      case(22):
        return SetOfBigInteger22;
      case(23):
        return SetOfBigInteger23;
      case(24):
        return SetOfByte24;
      case(25):
        return SetOfByte25;
      case(26):
        return SetOfByte26;
      case(27):
        return SetOfDouble27;
      case(28):
        return SetOfDouble28;
      case(29):
        return SetOfDouble29;
      case(30):
        return SetOfFloat30;
      case(31):
        return SetOfFloat31;
      case(32):
        return SetOfFloat32;
      case(33):
        return SetOfInteger33;
      case(34):
        return SetOfInteger34;
      case(35):
        return SetOfInteger35;
      case(36):
        return SetOfLong36;
      case(37):
        return SetOfLong37;
      case(38):
        return SetOfLong38;
      case(39):
        return SetOfShort39;
      case(40):
        return SetOfShort40;
      case(41):
        return SetOfShort41;
      case(42):
        return SetOfSimpleClass42;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") Set value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        SetOfObject0= value;
         break;
      case(1):
        SetOfObject1= value;
         break;
      case(2):
        SetOfObject2= value;
         break;
      case(3):
        SetOfSimpleClass3= value;
         break;
      case(4):
        SetOfSimpleClass4= value;
         break;
      case(5):
        SetOfSimpleClass5= value;
         break;
      case(6):
        SetOfSimpleInterface6= value;
         break;
      case(7):
        SetOfSimpleInterface7= value;
         break;
      case(8):
        SetOfSimpleInterface8= value;
         break;
      case(9):
        SetOfString9= value;
         break;
      case(10):
        SetOfString10= value;
         break;
      case(11):
        SetOfString11= value;
         break;
      case(12):
        SetOfDate12= value;
         break;
      case(13):
        SetOfDate13= value;
         break;
      case(14):
        SetOfDate14= value;
         break;
      case(15):
        SetOfLocale15= value;
         break;
      case(16):
        SetOfLocale16= value;
         break;
      case(17):
        SetOfLocale17= value;
         break;
      case(18):
        SetOfBigDecimal18= value;
         break;
      case(19):
        SetOfBigDecimal19= value;
         break;
      case(20):
        SetOfBigDecimal20= value;
         break;
      case(21):
        SetOfBigInteger21= value;
         break;
      case(22):
        SetOfBigInteger22= value;
         break;
      case(23):
        SetOfBigInteger23= value;
         break;
      case(24):
        SetOfByte24= value;
         break;
      case(25):
        SetOfByte25= value;
         break;
      case(26):
        SetOfByte26= value;
         break;
      case(27):
        SetOfDouble27= value;
         break;
      case(28):
        SetOfDouble28= value;
         break;
      case(29):
        SetOfDouble29= value;
         break;
      case(30):
        SetOfFloat30= value;
         break;
      case(31):
        SetOfFloat31= value;
         break;
      case(32):
        SetOfFloat32= value;
         break;
      case(33):
        SetOfInteger33= value;
         break;
      case(34):
        SetOfInteger34= value;
         break;
      case(35):
        SetOfInteger35= value;
         break;
      case(36):
        SetOfLong36= value;
         break;
      case(37):
        SetOfLong37= value;
         break;
      case(38):
        SetOfLong38= value;
         break;
      case(39):
        SetOfShort39= value;
         break;
      case(40):
        SetOfShort40= value;
         break;
      case(41):
        SetOfShort41= value;
         break;
      case(42):
        SetOfSimpleClass42= value;
         break;
      default:
        throw new IndexOutOfBoundsException();
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
