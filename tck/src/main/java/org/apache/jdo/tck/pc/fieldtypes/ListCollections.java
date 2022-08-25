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
import java.util.List;

public class ListCollections { 
  public int identifier;
  public List ListOfObject0;
  public List ListOfObject1;
  public List ListOfObject2;
  public List ListOfSimpleClass3;
  public List ListOfSimpleClass4;
  public List ListOfSimpleClass5;
  public List ListOfSimpleInterface6;
  public List ListOfSimpleInterface7;
  public List ListOfSimpleInterface8;
  public List ListOfString9;
  public List ListOfString10;
  public List ListOfString11;
  public List ListOfDate12;
  public List ListOfDate13;
  public List ListOfDate14;
  public List ListOfLocale15;
  public List ListOfLocale16;
  public List ListOfLocale17;
  public List ListOfBigDecimal18;
  public List ListOfBigDecimal19;
  public List ListOfBigDecimal20;
  public List ListOfBigInteger21;
  public List ListOfBigInteger22;
  public List ListOfBigInteger23;
  public List ListOfByte24;
  public List ListOfByte25;
  public List ListOfByte26;
  public List ListOfDouble27;
  public List ListOfDouble28;
  public List ListOfDouble29;
  public List ListOfFloat30;
  public List ListOfFloat31;
  public List ListOfFloat32;
  public List ListOfInteger33;
  public List ListOfInteger34;
  public List ListOfInteger35;
  public List ListOfLong36;
  public List ListOfLong37;
  public List ListOfLong38;
  public List ListOfShort39;
  public List ListOfShort40;
  public List ListOfShort41;
  public List ListOfSimpleClass42;

  public static final String [] fieldSpecs = { 
  "public List ListOfObject0",
  "embedded-element=true public List ListOfObject1",
  "embedded-element=false public List ListOfObject2",
  "public List ListOfSimpleClass3",
  "embedded-element=true public List ListOfSimpleClass4",
  "embedded-element=false public List ListOfSimpleClass5",
  "public List ListOfSimpleInterface6",
  "embedded-element=true public List ListOfSimpleInterface7",
  "embedded-element=false public List ListOfSimpleInterface8",
  "public List ListOfString9",
  "embedded-element=true public List ListOfString10",
  "embedded-element=false public List ListOfString11",
  "public List ListOfDate12",
  "embedded-element=true public List ListOfDate13",
  "embedded-element=false public List ListOfDate14",
  "public List ListOfLocale15",
  "embedded-element=true public List ListOfLocale16",
  "embedded-element=false public List ListOfLocale17",
  "public List ListOfBigDecimal18",
  "embedded-element=true public List ListOfBigDecimal19",
  "embedded-element=false public List ListOfBigDecimal20",
  "public List ListOfBigInteger21",
  "embedded-element=true public List ListOfBigInteger22",
  "embedded-element=false public List ListOfBigInteger23",
  "public List ListOfByte24",
  "embedded-element=true public List ListOfByte25",
  "embedded-element=false public List ListOfByte26",
  "public List ListOfDouble27",
  "embedded-element=true public List ListOfDouble28",
  "embedded-element=false public List ListOfDouble29",
  "public List ListOfFloat30",
  "embedded-element=true public List ListOfFloat31",
  "embedded-element=false public List ListOfFloat32",
  "public List ListOfInteger33",
  "embedded-element=true public List ListOfInteger34",
  "embedded-element=false public List ListOfInteger35",
  "public List ListOfLong36",
  "embedded-element=true public List ListOfLong37",
  "embedded-element=false public List ListOfLong38",
  "public List ListOfShort39",
  "embedded-element=true public List ListOfShort40",
  "embedded-element=false public List ListOfShort41",
  "serialized=true public List ListOfSimpleClass42"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public List get(int index)
  {
    switch (index)
    {
      case(0):
        return ListOfObject0;
      case(1):
        return ListOfObject1;
      case(2):
        return ListOfObject2;
      case(3):
        return ListOfSimpleClass3;
      case(4):
        return ListOfSimpleClass4;
      case(5):
        return ListOfSimpleClass5;
      case(6):
        return ListOfSimpleInterface6;
      case(7):
        return ListOfSimpleInterface7;
      case(8):
        return ListOfSimpleInterface8;
      case(9):
        return ListOfString9;
      case(10):
        return ListOfString10;
      case(11):
        return ListOfString11;
      case(12):
        return ListOfDate12;
      case(13):
        return ListOfDate13;
      case(14):
        return ListOfDate14;
      case(15):
        return ListOfLocale15;
      case(16):
        return ListOfLocale16;
      case(17):
        return ListOfLocale17;
      case(18):
        return ListOfBigDecimal18;
      case(19):
        return ListOfBigDecimal19;
      case(20):
        return ListOfBigDecimal20;
      case(21):
        return ListOfBigInteger21;
      case(22):
        return ListOfBigInteger22;
      case(23):
        return ListOfBigInteger23;
      case(24):
        return ListOfByte24;
      case(25):
        return ListOfByte25;
      case(26):
        return ListOfByte26;
      case(27):
        return ListOfDouble27;
      case(28):
        return ListOfDouble28;
      case(29):
        return ListOfDouble29;
      case(30):
        return ListOfFloat30;
      case(31):
        return ListOfFloat31;
      case(32):
        return ListOfFloat32;
      case(33):
        return ListOfInteger33;
      case(34):
        return ListOfInteger34;
      case(35):
        return ListOfInteger35;
      case(36):
        return ListOfLong36;
      case(37):
        return ListOfLong37;
      case(38):
        return ListOfLong38;
      case(39):
        return ListOfShort39;
      case(40):
        return ListOfShort40;
      case(41):
        return ListOfShort41;
      case(42):
        return ListOfSimpleClass42;
      default:
        throw new IndexOutOfBoundsException();
    }
  }
  public boolean set(int index, List value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        ListOfObject0= value;
         break;
      case(1):
        ListOfObject1= value;
         break;
      case(2):
        ListOfObject2= value;
         break;
      case(3):
        ListOfSimpleClass3= value;
         break;
      case(4):
        ListOfSimpleClass4= value;
         break;
      case(5):
        ListOfSimpleClass5= value;
         break;
      case(6):
        ListOfSimpleInterface6= value;
         break;
      case(7):
        ListOfSimpleInterface7= value;
         break;
      case(8):
        ListOfSimpleInterface8= value;
         break;
      case(9):
        ListOfString9= value;
         break;
      case(10):
        ListOfString10= value;
         break;
      case(11):
        ListOfString11= value;
         break;
      case(12):
        ListOfDate12= value;
         break;
      case(13):
        ListOfDate13= value;
         break;
      case(14):
        ListOfDate14= value;
         break;
      case(15):
        ListOfLocale15= value;
         break;
      case(16):
        ListOfLocale16= value;
         break;
      case(17):
        ListOfLocale17= value;
         break;
      case(18):
        ListOfBigDecimal18= value;
         break;
      case(19):
        ListOfBigDecimal19= value;
         break;
      case(20):
        ListOfBigDecimal20= value;
         break;
      case(21):
        ListOfBigInteger21= value;
         break;
      case(22):
        ListOfBigInteger22= value;
         break;
      case(23):
        ListOfBigInteger23= value;
         break;
      case(24):
        ListOfByte24= value;
         break;
      case(25):
        ListOfByte25= value;
         break;
      case(26):
        ListOfByte26= value;
         break;
      case(27):
        ListOfDouble27= value;
         break;
      case(28):
        ListOfDouble28= value;
         break;
      case(29):
        ListOfDouble29= value;
         break;
      case(30):
        ListOfFloat30= value;
         break;
      case(31):
        ListOfFloat31= value;
         break;
      case(32):
        ListOfFloat32= value;
         break;
      case(33):
        ListOfInteger33= value;
         break;
      case(34):
        ListOfInteger34= value;
         break;
      case(35):
        ListOfInteger35= value;
         break;
      case(36):
        ListOfLong36= value;
         break;
      case(37):
        ListOfLong37= value;
         break;
      case(38):
        ListOfLong38= value;
         break;
      case(39):
        ListOfShort39= value;
         break;
      case(40):
        ListOfShort40= value;
         break;
      case(41):
        ListOfShort41= value;
         break;
      case(42):
        ListOfSimpleClass42= value;
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
