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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArrayListCollections {
  public int identifier;
  public ArrayList<Object> ArrayListOfObject0;
  public ArrayList<Object> ArrayListOfObject1;
  public ArrayList<Object> ArrayListOfObject2;
  public ArrayList<SimpleClass> ArrayListOfSimpleClass3;
  public ArrayList<SimpleClass> ArrayListOfSimpleClass4;
  public ArrayList<SimpleClass> ArrayListOfSimpleClass5;
  public ArrayList<SimpleInterface> ArrayListOfSimpleInterface6;
  public ArrayList<SimpleInterface> ArrayListOfSimpleInterface7;
  public ArrayList<SimpleInterface> ArrayListOfSimpleInterface8;
  public ArrayList<String> ArrayListOfString9;
  public ArrayList<String> ArrayListOfString10;
  public ArrayList<String> ArrayListOfString11;
  public ArrayList<Date> ArrayListOfDate12;
  public ArrayList<Date> ArrayListOfDate13;
  public ArrayList<Date> ArrayListOfDate14;
  public ArrayList<Locale> ArrayListOfLocale15;
  public ArrayList<Locale> ArrayListOfLocale16;
  public ArrayList<Locale> ArrayListOfLocale17;
  public ArrayList<BigDecimal> ArrayListOfBigDecimal18;
  public ArrayList<BigDecimal> ArrayListOfBigDecimal19;
  public ArrayList<BigDecimal> ArrayListOfBigDecimal20;
  public ArrayList<BigInteger> ArrayListOfBigInteger21;
  public ArrayList<BigInteger> ArrayListOfBigInteger22;
  public ArrayList<BigInteger> ArrayListOfBigInteger23;
  public ArrayList<Byte> ArrayListOfByte24;
  public ArrayList<Byte> ArrayListOfByte25;
  public ArrayList<Byte> ArrayListOfByte26;
  public ArrayList<Double> ArrayListOfDouble27;
  public ArrayList<Double> ArrayListOfDouble28;
  public ArrayList<Double> ArrayListOfDouble29;
  public ArrayList<Float> ArrayListOfFloat30;
  public ArrayList<Float> ArrayListOfFloat31;
  public ArrayList<Float> ArrayListOfFloat32;
  public ArrayList<Integer> ArrayListOfInteger33;
  public ArrayList<Integer> ArrayListOfInteger34;
  public ArrayList<Integer> ArrayListOfInteger35;
  public ArrayList<Long> ArrayListOfLong36;
  public ArrayList<Long> ArrayListOfLong37;
  public ArrayList<Long> ArrayListOfLong38;
  public ArrayList<Short> ArrayListOfShort39;
  public ArrayList<Short> ArrayListOfShort40;
  public ArrayList<Short> ArrayListOfShort41;
  public ArrayList<SimpleEnum> ArrayListOfSimpleEnum42;
  public ArrayList<SimpleEnum> ArrayListOfSimpleEnum43;
  public ArrayList<SimpleEnum> ArrayListOfSimpleEnum44;
  public ArrayList<SimpleEnum> ArrayListOfSimpleEnum45;
  public ArrayList<SimpleEnum> ArrayListOfSimpleEnum46;

  public static final String[] fieldSpecs = {
    "public ArrayList ArrayListOfObject0",
    "embedded-element=true public ArrayList ArrayListOfObject1",
    "embedded-element=false public ArrayList ArrayListOfObject2",
    "public ArrayList ArrayListOfSimpleClass3",
    "embedded-element=true public ArrayList ArrayListOfSimpleClass4",
    "embedded-element=false public ArrayList ArrayListOfSimpleClass5",
    "public ArrayList ArrayListOfSimpleInterface6",
    "embedded-element=true public ArrayList ArrayListOfSimpleInterface7",
    "embedded-element=false public ArrayList ArrayListOfSimpleInterface8",
    "public ArrayList ArrayListOfString9",
    "embedded-element=true public ArrayList ArrayListOfString10",
    "embedded-element=false public ArrayList ArrayListOfString11",
    "public ArrayList ArrayListOfDate12",
    "embedded-element=true public ArrayList ArrayListOfDate13",
    "embedded-element=false public ArrayList ArrayListOfDate14",
    "public ArrayList ArrayListOfLocale15",
    "embedded-element=true public ArrayList ArrayListOfLocale16",
    "embedded-element=false public ArrayList ArrayListOfLocale17",
    "public ArrayList ArrayListOfBigDecimal18",
    "embedded-element=true public ArrayList ArrayListOfBigDecimal19",
    "embedded-element=false public ArrayList ArrayListOfBigDecimal20",
    "public ArrayList ArrayListOfBigInteger21",
    "embedded-element=true public ArrayList ArrayListOfBigInteger22",
    "embedded-element=false public ArrayList ArrayListOfBigInteger23",
    "public ArrayList ArrayListOfByte24",
    "embedded-element=true public ArrayList ArrayListOfByte25",
    "embedded-element=false public ArrayList ArrayListOfByte26",
    "public ArrayList ArrayListOfDouble27",
    "embedded-element=true public ArrayList ArrayListOfDouble28",
    "embedded-element=false public ArrayList ArrayListOfDouble29",
    "public ArrayList ArrayListOfFloat30",
    "embedded-element=true public ArrayList ArrayListOfFloat31",
    "embedded-element=false public ArrayList ArrayListOfFloat32",
    "public ArrayList ArrayListOfInteger33",
    "embedded-element=true public ArrayList ArrayListOfInteger34",
    "embedded-element=false public ArrayList ArrayListOfInteger35",
    "public ArrayList ArrayListOfLong36",
    "embedded-element=true public ArrayList ArrayListOfLong37",
    "embedded-element=false public ArrayList ArrayListOfLong38",
    "public ArrayList ArrayListOfShort39",
    "embedded-element=true public ArrayList ArrayListOfShort40",
    "embedded-element=false public ArrayList ArrayListOfShort41",
    "public ArrayList ArrayListOfSimpleEnum42",
    "embedded-element=true public ArrayList ArrayListOfSimpleEnum43",
    "embedded-element=false public ArrayList ArrayListOfSimpleEnum44",
    "public ArrayList ArrayListOfSimpleEnum45",
    "embedded-element=false public ArrayList ArrayListOfSimpleEnum46"
  };

  public int getLength() {
    return fieldSpecs.length;
  }

  public ArrayList<?> get(int index) {
    switch (index) {
      case (0):
        return ArrayListOfObject0;
      case (1):
        return ArrayListOfObject1;
      case (2):
        return ArrayListOfObject2;
      case (3):
        return ArrayListOfSimpleClass3;
      case (4):
        return ArrayListOfSimpleClass4;
      case (5):
        return ArrayListOfSimpleClass5;
      case (6):
        return ArrayListOfSimpleInterface6;
      case (7):
        return ArrayListOfSimpleInterface7;
      case (8):
        return ArrayListOfSimpleInterface8;
      case (9):
        return ArrayListOfString9;
      case (10):
        return ArrayListOfString10;
      case (11):
        return ArrayListOfString11;
      case (12):
        return ArrayListOfDate12;
      case (13):
        return ArrayListOfDate13;
      case (14):
        return ArrayListOfDate14;
      case (15):
        return ArrayListOfLocale15;
      case (16):
        return ArrayListOfLocale16;
      case (17):
        return ArrayListOfLocale17;
      case (18):
        return ArrayListOfBigDecimal18;
      case (19):
        return ArrayListOfBigDecimal19;
      case (20):
        return ArrayListOfBigDecimal20;
      case (21):
        return ArrayListOfBigInteger21;
      case (22):
        return ArrayListOfBigInteger22;
      case (23):
        return ArrayListOfBigInteger23;
      case (24):
        return ArrayListOfByte24;
      case (25):
        return ArrayListOfByte25;
      case (26):
        return ArrayListOfByte26;
      case (27):
        return ArrayListOfDouble27;
      case (28):
        return ArrayListOfDouble28;
      case (29):
        return ArrayListOfDouble29;
      case (30):
        return ArrayListOfFloat30;
      case (31):
        return ArrayListOfFloat31;
      case (32):
        return ArrayListOfFloat32;
      case (33):
        return ArrayListOfInteger33;
      case (34):
        return ArrayListOfInteger34;
      case (35):
        return ArrayListOfInteger35;
      case (36):
        return ArrayListOfLong36;
      case (37):
        return ArrayListOfLong37;
      case (38):
        return ArrayListOfLong38;
      case (39):
        return ArrayListOfShort39;
      case (40):
        return ArrayListOfShort40;
      case (41):
        return ArrayListOfShort41;
      case (42):
        return ArrayListOfSimpleEnum42;
      case (43):
        return ArrayListOfSimpleEnum43;
      case (44):
        return ArrayListOfSimpleEnum44;
      case (45):
        return ArrayListOfSimpleEnum45;
      case (46):
        return ArrayListOfSimpleEnum46;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") ArrayList value) {
    if (fieldSpecs[index].indexOf("final") != -1) return false;
    switch (index) {
      case (0):
        ArrayListOfObject0 = value;
        break;
      case (1):
        ArrayListOfObject1 = value;
        break;
      case (2):
        ArrayListOfObject2 = value;
        break;
      case (3):
        ArrayListOfSimpleClass3 = value;
        break;
      case (4):
        ArrayListOfSimpleClass4 = value;
        break;
      case (5):
        ArrayListOfSimpleClass5 = value;
        break;
      case (6):
        ArrayListOfSimpleInterface6 = value;
        break;
      case (7):
        ArrayListOfSimpleInterface7 = value;
        break;
      case (8):
        ArrayListOfSimpleInterface8 = value;
        break;
      case (9):
        ArrayListOfString9 = value;
        break;
      case (10):
        ArrayListOfString10 = value;
        break;
      case (11):
        ArrayListOfString11 = value;
        break;
      case (12):
        ArrayListOfDate12 = value;
        break;
      case (13):
        ArrayListOfDate13 = value;
        break;
      case (14):
        ArrayListOfDate14 = value;
        break;
      case (15):
        ArrayListOfLocale15 = value;
        break;
      case (16):
        ArrayListOfLocale16 = value;
        break;
      case (17):
        ArrayListOfLocale17 = value;
        break;
      case (18):
        ArrayListOfBigDecimal18 = value;
        break;
      case (19):
        ArrayListOfBigDecimal19 = value;
        break;
      case (20):
        ArrayListOfBigDecimal20 = value;
        break;
      case (21):
        ArrayListOfBigInteger21 = value;
        break;
      case (22):
        ArrayListOfBigInteger22 = value;
        break;
      case (23):
        ArrayListOfBigInteger23 = value;
        break;
      case (24):
        ArrayListOfByte24 = value;
        break;
      case (25):
        ArrayListOfByte25 = value;
        break;
      case (26):
        ArrayListOfByte26 = value;
        break;
      case (27):
        ArrayListOfDouble27 = value;
        break;
      case (28):
        ArrayListOfDouble28 = value;
        break;
      case (29):
        ArrayListOfDouble29 = value;
        break;
      case (30):
        ArrayListOfFloat30 = value;
        break;
      case (31):
        ArrayListOfFloat31 = value;
        break;
      case (32):
        ArrayListOfFloat32 = value;
        break;
      case (33):
        ArrayListOfInteger33 = value;
        break;
      case (34):
        ArrayListOfInteger34 = value;
        break;
      case (35):
        ArrayListOfInteger35 = value;
        break;
      case (36):
        ArrayListOfLong36 = value;
        break;
      case (37):
        ArrayListOfLong37 = value;
        break;
      case (38):
        ArrayListOfLong38 = value;
        break;
      case (39):
        ArrayListOfShort39 = value;
        break;
      case (40):
        ArrayListOfShort40 = value;
        break;
      case (41):
        ArrayListOfShort41 = value;
        break;
      case (42):
        ArrayListOfSimpleEnum42 = value;
        break;
      case (43):
        ArrayListOfSimpleEnum43 = value;
        break;
      case (44):
        ArrayListOfSimpleEnum44 = value;
        break;
      case (45):
        ArrayListOfSimpleEnum45 = value;
        break;
      case (46):
        ArrayListOfSimpleEnum46 = value;
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
