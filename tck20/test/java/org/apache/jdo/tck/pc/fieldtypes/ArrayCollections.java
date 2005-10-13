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
import java.util.*;
import java.math.*;

public class ArrayCollections {
  public int identifier;
  public Object [] ArrayOfObject0;
  public Object [] ArrayOfObject1;
  public SimpleClass [] ArrayOfSimpleClass2;
  public SimpleClass [] ArrayOfSimpleClass3;
  public SimpleInterface [] ArrayOfSimpleInterface4;
  public SimpleInterface [] ArrayOfSimpleInterface5;
  public String [] ArrayOfString6;
  public String [] ArrayOfString7;
  public Date [] ArrayOfDate8;
  public Date [] ArrayOfDate9;
  public Locale [] ArrayOfLocale10;
  public Locale [] ArrayOfLocale11;
  public BigDecimal [] ArrayOfBigDecimal12;
  public BigDecimal [] ArrayOfBigDecimal13;
  public BigInteger [] ArrayOfBigInteger14;
  public BigInteger [] ArrayOfBigInteger15;
  public Byte [] ArrayOfByte16;
  public Byte [] ArrayOfByte17;
  public Double [] ArrayOfDouble18;
  public Double [] ArrayOfDouble19;
  public Float [] ArrayOfFloat20;
  public Float [] ArrayOfFloat21;
  public Integer [] ArrayOfInteger22;
  public Integer [] ArrayOfInteger23;
  public Long [] ArrayOfLong24;
  public Long [] ArrayOfLong25;
  public Short [] ArrayOfShort26;
  public Short [] ArrayOfShort27;

  public static final String [] fieldSpecs = { 
  "serialized=true public Object [] ArrayOfObject0",
  "embedded-element=false public Object [] ArrayOfObject1",
  "serialized=true public SimpleClass [] ArrayOfSimpleClass2",
  "embedded-element=false public SimpleClass [] ArrayOfSimpleClass3",
  "serialized=true public SimpleInterface [] ArrayOfSimpleInterface4",
  "embedded-element=false public SimpleInterface [] ArrayOfSimpleInterface5",
  "embedded-element=false public String [] ArrayOfString6",
  "embedded-element=true public String [] ArrayOfString7",
  "embedded-element=false public Date [] ArrayOfDate8",
  "embedded-element=true public Date [] ArrayOfDate9",
  "embedded-element=false public Locale [] ArrayOfLocale10",
  "embedded-element=true public Locale [] ArrayOfLocale11",
  "embedded-element=false public BigDecimal [] ArrayOfBigDecimal12",
  "embedded-element=true public BigDecimal [] ArrayOfBigDecimal13",
  "embedded-element=false public BigInteger [] ArrayOfBigInteger14",
  "embedded-element=true public BigInteger [] ArrayOfBigInteger15",
  "embedded-element=false public Byte [] ArrayOfByte16",
  "embedded-element=true public Byte [] ArrayOfByte17",
  "embedded-element=false public Double [] ArrayOfDouble18",
  "embedded-element=true public Double [] ArrayOfDouble19",
  "embedded-element=false public Float [] ArrayOfFloat20",
  "embedded-element=true public Float [] ArrayOfFloat21",
  "embedded-element=false public Integer [] ArrayOfInteger22",
  "embedded-element=true public Integer [] ArrayOfInteger23",
  "embedded-element=false public Long [] ArrayOfLong24",
  "embedded-element=true public Long [] ArrayOfLong25",
  "embedded-element=false public Short [] ArrayOfShort26",
  "embedded-element=true public Short [] ArrayOfShort27"
  };
  public int getLength()
  {
    return fieldSpecs.length;
  }
  public Object []  get(int index)
  {
    switch (index)
    {
      case(0):
        return ArrayOfObject0;
      case(1):
        return ArrayOfObject1;
      case(2):
        return ArrayOfSimpleClass2;
      case(3):
        return ArrayOfSimpleClass3;
      case(4):
        return ArrayOfSimpleInterface4;
      case(5):
        return ArrayOfSimpleInterface5;
      case(6):
        return ArrayOfString6;
      case(7):
        return ArrayOfString7;
      case(8):
        return ArrayOfDate8;
      case(9):
        return ArrayOfDate9;
      case(10):
        return ArrayOfLocale10;
      case(11):
        return ArrayOfLocale11;
      case(12):
        return ArrayOfBigDecimal12;
      case(13):
        return ArrayOfBigDecimal13;
      case(14):
        return ArrayOfBigInteger14;
      case(15):
        return ArrayOfBigInteger15;
      case(16):
        return ArrayOfByte16;
      case(17):
        return ArrayOfByte17;
      case(18):
        return ArrayOfDouble18;
      case(19):
        return ArrayOfDouble19;
      case(20):
        return ArrayOfFloat20;
      case(21):
        return ArrayOfFloat21;
      case(22):
        return ArrayOfInteger22;
      case(23):
        return ArrayOfInteger23;
      case(24):
        return ArrayOfLong24;
      case(25):
        return ArrayOfLong25;
      case(26):
        return ArrayOfShort26;
      case(27):
        return ArrayOfShort27;
      default:
        throw new IndexOutOfBoundsException();
    }
  }
  public boolean set(int index,Object []  value)
  {
    if(fieldSpecs[index].indexOf("final") != -1)
      return false;
    switch (index)
    {
      case(0):
        ArrayOfObject0= (Object []) value ;
         break;
      case(1):
        ArrayOfObject1= (Object []) value ;
         break;
      case(2):
        ArrayOfSimpleClass2= (SimpleClass []) value ;
         break;
      case(3):
        ArrayOfSimpleClass3= (SimpleClass []) value ;
         break;
      case(4):
        ArrayOfSimpleInterface4= (SimpleInterface []) value ;
         break;
      case(5):
        ArrayOfSimpleInterface5= (SimpleInterface []) value ;
         break;
      case(6):
        ArrayOfString6= (String []) value ;
         break;
      case(7):
        ArrayOfString7= (String []) value ;
         break;
      case(8):
        ArrayOfDate8= (Date []) value ;
         break;
      case(9):
        ArrayOfDate9= (Date []) value ;
         break;
      case(10):
        ArrayOfLocale10= (Locale []) value ;
         break;
      case(11):
        ArrayOfLocale11= (Locale []) value ;
         break;
      case(12):
        ArrayOfBigDecimal12= (BigDecimal []) value ;
         break;
      case(13):
        ArrayOfBigDecimal13= (BigDecimal []) value ;
         break;
      case(14):
        ArrayOfBigInteger14= (BigInteger []) value ;
         break;
      case(15):
        ArrayOfBigInteger15= (BigInteger []) value ;
         break;
      case(16):
        ArrayOfByte16= (Byte []) value ;
         break;
      case(17):
        ArrayOfByte17= (Byte []) value ;
         break;
      case(18):
        ArrayOfDouble18= (Double []) value ;
         break;
      case(19):
        ArrayOfDouble19= (Double []) value ;
         break;
      case(20):
        ArrayOfFloat20= (Float []) value ;
         break;
      case(21):
        ArrayOfFloat21= (Float []) value ;
         break;
      case(22):
        ArrayOfInteger22= (Integer []) value ;
         break;
      case(23):
        ArrayOfInteger23= (Integer []) value ;
         break;
      case(24):
        ArrayOfLong24= (Long []) value ;
         break;
      case(25):
        ArrayOfLong25= (Long []) value ;
         break;
      case(26):
        ArrayOfShort26= (Short []) value ;
         break;
      case(27):
        ArrayOfShort27= (Short []) value ;
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
