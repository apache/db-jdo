/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class VectorCollections {
  public int identifier;
  public Vector<Object> VectorOfObject0;
  public Vector<Object> VectorOfObject1;
  public Vector<Object> VectorOfObject2;
  public Vector<SimpleClass> VectorOfSimpleClass3;
  public Vector<SimpleClass> VectorOfSimpleClass4;
  public Vector<SimpleClass> VectorOfSimpleClass5;
  public Vector<SimpleInterface> VectorOfSimpleInterface6;
  public Vector<SimpleInterface> VectorOfSimpleInterface7;
  public Vector<SimpleInterface> VectorOfSimpleInterface8;
  public Vector<String> VectorOfString9;
  public Vector<String> VectorOfString10;
  public Vector<String> VectorOfString11;
  public Vector<Date> VectorOfDate12;
  public Vector<Date> VectorOfDate13;
  public Vector<Date> VectorOfDate14;
  public Vector<Locale> VectorOfLocale15;
  public Vector<Locale> VectorOfLocale16;
  public Vector<Locale> VectorOfLocale17;
  public Vector<BigDecimal> VectorOfBigDecimal18;
  public Vector<BigDecimal> VectorOfBigDecimal19;
  public Vector<BigDecimal> VectorOfBigDecimal20;
  public Vector<BigInteger> VectorOfBigInteger21;
  public Vector<BigInteger> VectorOfBigInteger22;
  public Vector<BigInteger> VectorOfBigInteger23;
  public Vector<Byte> VectorOfByte24;
  public Vector<Byte> VectorOfByte25;
  public Vector<Byte> VectorOfByte26;
  public Vector<Double> VectorOfDouble27;
  public Vector<Double> VectorOfDouble28;
  public Vector<Double> VectorOfDouble29;
  public Vector<Float> VectorOfFloat30;
  public Vector<Float> VectorOfFloat31;
  public Vector<Float> VectorOfFloat32;
  public Vector<Integer> VectorOfInteger33;
  public Vector<Integer> VectorOfInteger34;
  public Vector<Integer> VectorOfInteger35;
  public Vector<Long> VectorOfLong36;
  public Vector<Long> VectorOfLong37;
  public Vector<Long> VectorOfLong38;
  public Vector<Short> VectorOfShort39;
  public Vector<Short> VectorOfShort40;
  public Vector<Short> VectorOfShort41;
  public Vector<SimpleClass> VectorOfSimpleClass42;

  public static final String[] fieldSpecs = {
    "public Vector VectorOfObject0",
    "embedded-element=true public Vector VectorOfObject1",
    "embedded-element=false public Vector VectorOfObject2",
    "public Vector VectorOfSimpleClass3",
    "embedded-element=true public Vector VectorOfSimpleClass4",
    "embedded-element=false public Vector VectorOfSimpleClass5",
    "public Vector VectorOfSimpleInterface6",
    "embedded-element=true public Vector VectorOfSimpleInterface7",
    "embedded-element=false public Vector VectorOfSimpleInterface8",
    "public Vector VectorOfString9",
    "embedded-element=true public Vector VectorOfString10",
    "embedded-element=false public Vector VectorOfString11",
    "public Vector VectorOfDate12",
    "embedded-element=true public Vector VectorOfDate13",
    "embedded-element=false public Vector VectorOfDate14",
    "public Vector VectorOfLocale15",
    "embedded-element=true public Vector VectorOfLocale16",
    "embedded-element=false public Vector VectorOfLocale17",
    "public Vector VectorOfBigDecimal18",
    "embedded-element=true public Vector VectorOfBigDecimal19",
    "embedded-element=false public Vector VectorOfBigDecimal20",
    "public Vector VectorOfBigInteger21",
    "embedded-element=true public Vector VectorOfBigInteger22",
    "embedded-element=false public Vector VectorOfBigInteger23",
    "public Vector VectorOfByte24",
    "embedded-element=true public Vector VectorOfByte25",
    "embedded-element=false public Vector VectorOfByte26",
    "public Vector VectorOfDouble27",
    "embedded-element=true public Vector VectorOfDouble28",
    "embedded-element=false public Vector VectorOfDouble29",
    "public Vector VectorOfFloat30",
    "embedded-element=true public Vector VectorOfFloat31",
    "embedded-element=false public Vector VectorOfFloat32",
    "public Vector VectorOfInteger33",
    "embedded-element=true public Vector VectorOfInteger34",
    "embedded-element=false public Vector VectorOfInteger35",
    "public Vector VectorOfLong36",
    "embedded-element=true public Vector VectorOfLong37",
    "embedded-element=false public Vector VectorOfLong38",
    "public Vector VectorOfShort39",
    "embedded-element=true public Vector VectorOfShort40",
    "embedded-element=false public Vector VectorOfShort41",
    "serialized=true public Vector VectorOfSimpleClass42"
  };

  public int getLength() {
    return fieldSpecs.length;
  }

  public Vector<?> get(int index) {
    switch (index) {
      case (0):
        return VectorOfObject0;
      case (1):
        return VectorOfObject1;
      case (2):
        return VectorOfObject2;
      case (3):
        return VectorOfSimpleClass3;
      case (4):
        return VectorOfSimpleClass4;
      case (5):
        return VectorOfSimpleClass5;
      case (6):
        return VectorOfSimpleInterface6;
      case (7):
        return VectorOfSimpleInterface7;
      case (8):
        return VectorOfSimpleInterface8;
      case (9):
        return VectorOfString9;
      case (10):
        return VectorOfString10;
      case (11):
        return VectorOfString11;
      case (12):
        return VectorOfDate12;
      case (13):
        return VectorOfDate13;
      case (14):
        return VectorOfDate14;
      case (15):
        return VectorOfLocale15;
      case (16):
        return VectorOfLocale16;
      case (17):
        return VectorOfLocale17;
      case (18):
        return VectorOfBigDecimal18;
      case (19):
        return VectorOfBigDecimal19;
      case (20):
        return VectorOfBigDecimal20;
      case (21):
        return VectorOfBigInteger21;
      case (22):
        return VectorOfBigInteger22;
      case (23):
        return VectorOfBigInteger23;
      case (24):
        return VectorOfByte24;
      case (25):
        return VectorOfByte25;
      case (26):
        return VectorOfByte26;
      case (27):
        return VectorOfDouble27;
      case (28):
        return VectorOfDouble28;
      case (29):
        return VectorOfDouble29;
      case (30):
        return VectorOfFloat30;
      case (31):
        return VectorOfFloat31;
      case (32):
        return VectorOfFloat32;
      case (33):
        return VectorOfInteger33;
      case (34):
        return VectorOfInteger34;
      case (35):
        return VectorOfInteger35;
      case (36):
        return VectorOfLong36;
      case (37):
        return VectorOfLong37;
      case (38):
        return VectorOfLong38;
      case (39):
        return VectorOfShort39;
      case (40):
        return VectorOfShort40;
      case (41):
        return VectorOfShort41;
      case (42):
        return VectorOfSimpleClass42;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") Vector value) {
    if (fieldSpecs[index].indexOf("final") != -1) return false;
    switch (index) {
      case (0):
        VectorOfObject0 = value;
        break;
      case (1):
        VectorOfObject1 = value;
        break;
      case (2):
        VectorOfObject2 = value;
        break;
      case (3):
        VectorOfSimpleClass3 = value;
        break;
      case (4):
        VectorOfSimpleClass4 = value;
        break;
      case (5):
        VectorOfSimpleClass5 = value;
        break;
      case (6):
        VectorOfSimpleInterface6 = value;
        break;
      case (7):
        VectorOfSimpleInterface7 = value;
        break;
      case (8):
        VectorOfSimpleInterface8 = value;
        break;
      case (9):
        VectorOfString9 = value;
        break;
      case (10):
        VectorOfString10 = value;
        break;
      case (11):
        VectorOfString11 = value;
        break;
      case (12):
        VectorOfDate12 = value;
        break;
      case (13):
        VectorOfDate13 = value;
        break;
      case (14):
        VectorOfDate14 = value;
        break;
      case (15):
        VectorOfLocale15 = value;
        break;
      case (16):
        VectorOfLocale16 = value;
        break;
      case (17):
        VectorOfLocale17 = value;
        break;
      case (18):
        VectorOfBigDecimal18 = value;
        break;
      case (19):
        VectorOfBigDecimal19 = value;
        break;
      case (20):
        VectorOfBigDecimal20 = value;
        break;
      case (21):
        VectorOfBigInteger21 = value;
        break;
      case (22):
        VectorOfBigInteger22 = value;
        break;
      case (23):
        VectorOfBigInteger23 = value;
        break;
      case (24):
        VectorOfByte24 = value;
        break;
      case (25):
        VectorOfByte25 = value;
        break;
      case (26):
        VectorOfByte26 = value;
        break;
      case (27):
        VectorOfDouble27 = value;
        break;
      case (28):
        VectorOfDouble28 = value;
        break;
      case (29):
        VectorOfDouble29 = value;
        break;
      case (30):
        VectorOfFloat30 = value;
        break;
      case (31):
        VectorOfFloat31 = value;
        break;
      case (32):
        VectorOfFloat32 = value;
        break;
      case (33):
        VectorOfInteger33 = value;
        break;
      case (34):
        VectorOfInteger34 = value;
        break;
      case (35):
        VectorOfInteger35 = value;
        break;
      case (36):
        VectorOfLong36 = value;
        break;
      case (37):
        VectorOfLong37 = value;
        break;
      case (38):
        VectorOfLong38 = value;
        break;
      case (39):
        VectorOfShort39 = value;
        break;
      case (40):
        VectorOfShort40 = value;
        break;
      case (41):
        VectorOfShort41 = value;
        break;
      case (42):
        VectorOfSimpleClass42 = value;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
    return true;
  }

  public static class Oid implements Serializable {

    private static final long serialVersionUID = 1L;

    public int identifier;

    public Oid() {}

    public Oid(String s) {
      identifier = Integer.parseInt(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + identifier;
    }

    public int hashCode() {
      return identifier;
    }

    public boolean equals(Object other) {
      if (other != null && (other instanceof Oid)) {
        Oid k = (Oid) other;
        return k.identifier == this.identifier;
      }
      return false;
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }
  }
}
