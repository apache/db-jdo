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
 
package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;
import java.util.Collection;

public class CollectionCollections { 
  public int identifier;
  public Collection CollectionOfObject0;
  public Collection CollectionOfObject1;
  public Collection CollectionOfObject2;
  public Collection CollectionOfSimpleClass3;
  public Collection CollectionOfSimpleClass4;
  public Collection CollectionOfSimpleClass5;
  public Collection CollectionOfSimpleInterface6;
  public Collection CollectionOfSimpleInterface7;
  public Collection CollectionOfSimpleInterface8;
  public Collection CollectionOfString9;
  public Collection CollectionOfString10;
  public Collection CollectionOfString11;
  public Collection CollectionOfDate12;
  public Collection CollectionOfDate13;
  public Collection CollectionOfDate14;
  public Collection CollectionOfLocale15;
  public Collection CollectionOfLocale16;
  public Collection CollectionOfLocale17;
  public Collection CollectionOfBigDecimal18;
  public Collection CollectionOfBigDecimal19;
  public Collection CollectionOfBigDecimal20;
  public Collection CollectionOfBigInteger21;
  public Collection CollectionOfBigInteger22;
  public Collection CollectionOfBigInteger23;
  public Collection CollectionOfByte24;
  public Collection CollectionOfByte25;
  public Collection CollectionOfByte26;
  public Collection CollectionOfDouble27;
  public Collection CollectionOfDouble28;
  public Collection CollectionOfDouble29;
  public Collection CollectionOfFloat30;
  public Collection CollectionOfFloat31;
  public Collection CollectionOfFloat32;
  public Collection CollectionOfInteger33;
  public Collection CollectionOfInteger34;
  public Collection CollectionOfInteger35;
  public Collection CollectionOfLong36;
  public Collection CollectionOfLong37;
  public Collection CollectionOfLong38;
  public Collection CollectionOfShort39;
  public Collection CollectionOfShort40;
  public Collection CollectionOfShort41;

  public static final String [] fieldSpecs = { 
  "public Collection CollectionOfObject0",
  "embedded-element=true public Collection CollectionOfObject1",
  "embedded-element=false public Collection CollectionOfObject2",
  "public Collection CollectionOfSimpleClass3",
  "embedded-element=true public Collection CollectionOfSimpleClass4",
  "embedded-element=false public Collection CollectionOfSimpleClass5",
  "public Collection CollectionOfSimpleInterface6",
  "embedded-element=true public Collection CollectionOfSimpleInterface7",
  "embedded-element=false public Collection CollectionOfSimpleInterface8",
  "public Collection CollectionOfString9",
  "embedded-element=true public Collection CollectionOfString10",
  "embedded-element=false public Collection CollectionOfString11",
  "public Collection CollectionOfDate12",
  "embedded-element=true public Collection CollectionOfDate13",
  "embedded-element=false public Collection CollectionOfDate14",
  "public Collection CollectionOfLocale15",
  "embedded-element=true public Collection CollectionOfLocale16",
  "embedded-element=false public Collection CollectionOfLocale17",
  "public Collection CollectionOfBigDecimal18",
  "embedded-element=true public Collection CollectionOfBigDecimal19",
  "embedded-element=false public Collection CollectionOfBigDecimal20",
  "public Collection CollectionOfBigInteger21",
  "embedded-element=true public Collection CollectionOfBigInteger22",
  "embedded-element=false public Collection CollectionOfBigInteger23",
  "public Collection CollectionOfByte24",
  "embedded-element=true public Collection CollectionOfByte25",
  "embedded-element=false public Collection CollectionOfByte26",
  "public Collection CollectionOfDouble27",
  "embedded-element=true public Collection CollectionOfDouble28",
  "embedded-element=false public Collection CollectionOfDouble29",
  "public Collection CollectionOfFloat30",
  "embedded-element=true public Collection CollectionOfFloat31",
  "embedded-element=false public Collection CollectionOfFloat32",
  "public Collection CollectionOfInteger33",
  "embedded-element=true public Collection CollectionOfInteger34",
  "embedded-element=false public Collection CollectionOfInteger35",
  "public Collection CollectionOfLong36",
  "embedded-element=true public Collection CollectionOfLong37",
  "embedded-element=false public Collection CollectionOfLong38",
  "public Collection CollectionOfShort39",
  "embedded-element=true public Collection CollectionOfShort40",
  "embedded-element=false public Collection CollectionOfShort41"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public Collection get(int index)
  {
    switch (index)
    {
      case(0):
        return CollectionOfObject0;
      case(1):
        return CollectionOfObject1;
      case(2):
        return CollectionOfObject2;
      case(3):
        return CollectionOfSimpleClass3;
      case(4):
        return CollectionOfSimpleClass4;
      case(5):
        return CollectionOfSimpleClass5;
      case(6):
        return CollectionOfSimpleInterface6;
      case(7):
        return CollectionOfSimpleInterface7;
      case(8):
        return CollectionOfSimpleInterface8;
      case(9):
        return CollectionOfString9;
      case(10):
        return CollectionOfString10;
      case(11):
        return CollectionOfString11;
      case(12):
        return CollectionOfDate12;
      case(13):
        return CollectionOfDate13;
      case(14):
        return CollectionOfDate14;
      case(15):
        return CollectionOfLocale15;
      case(16):
        return CollectionOfLocale16;
      case(17):
        return CollectionOfLocale17;
      case(18):
        return CollectionOfBigDecimal18;
      case(19):
        return CollectionOfBigDecimal19;
      case(20):
        return CollectionOfBigDecimal20;
      case(21):
        return CollectionOfBigInteger21;
      case(22):
        return CollectionOfBigInteger22;
      case(23):
        return CollectionOfBigInteger23;
      case(24):
        return CollectionOfByte24;
      case(25):
        return CollectionOfByte25;
      case(26):
        return CollectionOfByte26;
      case(27):
        return CollectionOfDouble27;
      case(28):
        return CollectionOfDouble28;
      case(29):
        return CollectionOfDouble29;
      case(30):
        return CollectionOfFloat30;
      case(31):
        return CollectionOfFloat31;
      case(32):
        return CollectionOfFloat32;
      case(33):
        return CollectionOfInteger33;
      case(34):
        return CollectionOfInteger34;
      case(35):
        return CollectionOfInteger35;
      case(36):
        return CollectionOfLong36;
      case(37):
        return CollectionOfLong37;
      case(38):
        return CollectionOfLong38;
      case(39):
        return CollectionOfShort39;
      case(40):
        return CollectionOfShort40;
      case(41):
        return CollectionOfShort41;
      default:
        throw new IndexOutOfBoundsException();
    }
  }
  public boolean set(int index,Collection value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        CollectionOfObject0= value;
         break;
      case(1):
        CollectionOfObject1= value;
         break;
      case(2):
        CollectionOfObject2= value;
         break;
      case(3):
        CollectionOfSimpleClass3= value;
         break;
      case(4):
        CollectionOfSimpleClass4= value;
         break;
      case(5):
        CollectionOfSimpleClass5= value;
         break;
      case(6):
        CollectionOfSimpleInterface6= value;
         break;
      case(7):
        CollectionOfSimpleInterface7= value;
         break;
      case(8):
        CollectionOfSimpleInterface8= value;
         break;
      case(9):
        CollectionOfString9= value;
         break;
      case(10):
        CollectionOfString10= value;
         break;
      case(11):
        CollectionOfString11= value;
         break;
      case(12):
        CollectionOfDate12= value;
         break;
      case(13):
        CollectionOfDate13= value;
         break;
      case(14):
        CollectionOfDate14= value;
         break;
      case(15):
        CollectionOfLocale15= value;
         break;
      case(16):
        CollectionOfLocale16= value;
         break;
      case(17):
        CollectionOfLocale17= value;
         break;
      case(18):
        CollectionOfBigDecimal18= value;
         break;
      case(19):
        CollectionOfBigDecimal19= value;
         break;
      case(20):
        CollectionOfBigDecimal20= value;
         break;
      case(21):
        CollectionOfBigInteger21= value;
         break;
      case(22):
        CollectionOfBigInteger22= value;
         break;
      case(23):
        CollectionOfBigInteger23= value;
         break;
      case(24):
        CollectionOfByte24= value;
         break;
      case(25):
        CollectionOfByte25= value;
         break;
      case(26):
        CollectionOfByte26= value;
         break;
      case(27):
        CollectionOfDouble27= value;
         break;
      case(28):
        CollectionOfDouble28= value;
         break;
      case(29):
        CollectionOfDouble29= value;
         break;
      case(30):
        CollectionOfFloat30= value;
         break;
      case(31):
        CollectionOfFloat31= value;
         break;
      case(32):
        CollectionOfFloat32= value;
         break;
      case(33):
        CollectionOfInteger33= value;
         break;
      case(34):
        CollectionOfInteger34= value;
         break;
      case(35):
        CollectionOfInteger35= value;
         break;
      case(36):
        CollectionOfLong36= value;
         break;
      case(37):
        CollectionOfLong37= value;
         break;
      case(38):
        CollectionOfLong38= value;
         break;
      case(39):
        CollectionOfShort39= value;
         break;
      case(40):
        CollectionOfShort40= value;
         break;
      case(41):
        CollectionOfShort41= value;
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
