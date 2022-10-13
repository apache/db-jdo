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
import java.util.Date;
import java.util.TreeSet;

public class TreeSetCollections {
  public int identifier;
  public TreeSet<Object> TreeSetOfObject0;
  public TreeSet<Object> TreeSetOfObject1;
  public TreeSet<Object> TreeSetOfObject2;
  public TreeSet<SimpleClass> TreeSetOfSimpleClass3;
  public TreeSet<SimpleClass> TreeSetOfSimpleClass4;
  public TreeSet<SimpleClass> TreeSetOfSimpleClass5;
  public TreeSet<SimpleInterface> TreeSetOfSimpleInterface6;
  public TreeSet<SimpleInterface> TreeSetOfSimpleInterface7;
  public TreeSet<SimpleInterface> TreeSetOfSimpleInterface8;
  public TreeSet<String> TreeSetOfString9;
  public TreeSet<String> TreeSetOfString10;
  public TreeSet<String> TreeSetOfString11;
  public TreeSet<Date> TreeSetOfDate12;
  public TreeSet<Date> TreeSetOfDate13;
  public TreeSet<Date> TreeSetOfDate14;
  public TreeSet<BigDecimal> TreeSetOfBigDecimal15;
  public TreeSet<BigDecimal> TreeSetOfBigDecimal16;
  public TreeSet<BigDecimal> TreeSetOfBigDecimal17;
  public TreeSet<BigInteger> TreeSetOfBigInteger18;
  public TreeSet<BigInteger> TreeSetOfBigInteger19;
  public TreeSet<BigInteger> TreeSetOfBigInteger20;
  public TreeSet<Byte> TreeSetOfByte21;
  public TreeSet<Byte> TreeSetOfByte22;
  public TreeSet<Byte> TreeSetOfByte23;
  public TreeSet<Double> TreeSetOfDouble24;
  public TreeSet<Double> TreeSetOfDouble25;
  public TreeSet<Double> TreeSetOfDouble26;
  public TreeSet<Float> TreeSetOfFloat27;
  public TreeSet<Float> TreeSetOfFloat28;
  public TreeSet<Float> TreeSetOfFloat29;
  public TreeSet<Integer> TreeSetOfInteger30;
  public TreeSet<Integer> TreeSetOfInteger31;
  public TreeSet<Integer> TreeSetOfInteger32;
  public TreeSet<Long> TreeSetOfLong33;
  public TreeSet<Long> TreeSetOfLong34;
  public TreeSet<Long> TreeSetOfLong35;
  public TreeSet<Short> TreeSetOfShort36;
  public TreeSet<Short> TreeSetOfShort37;
  public TreeSet<Short> TreeSetOfShort38;

  public static final String[] fieldSpecs = {
    "public TreeSet TreeSetOfObject0",
    "embedded-element=true public TreeSet TreeSetOfObject1",
    "embedded-element=false public TreeSet TreeSetOfObject2",
    "public TreeSet TreeSetOfSimpleClass3",
    "embedded-element=true public TreeSet TreeSetOfSimpleClass4",
    "embedded-element=false public TreeSet TreeSetOfSimpleClass5",
    "public TreeSet TreeSetOfSimpleInterface6",
    "embedded-element=true public TreeSet TreeSetOfSimpleInterface7",
    "embedded-element=false public TreeSet TreeSetOfSimpleInterface8",
    "public TreeSet TreeSetOfString9",
    "embedded-element=true public TreeSet TreeSetOfString10",
    "embedded-element=false public TreeSet TreeSetOfString11",
    "public TreeSet TreeSetOfDate12",
    "embedded-element=true public TreeSet TreeSetOfDate13",
    "embedded-element=false public TreeSet TreeSetOfDate14",
    "public TreeSet TreeSetOfBigDecimal15",
    "embedded-element=true public TreeSet TreeSetOfBigDecimal16",
    "embedded-element=false public TreeSet TreeSetOfBigDecimal17",
    "public TreeSet TreeSetOfBigInteger18",
    "embedded-element=true public TreeSet TreeSetOfBigInteger19",
    "embedded-element=false public TreeSet TreeSetOfBigInteger20",
    "public TreeSet TreeSetOfByte21",
    "embedded-element=true public TreeSet TreeSetOfByte22",
    "embedded-element=false public TreeSet TreeSetOfByte23",
    "public TreeSet TreeSetOfDouble24",
    "embedded-element=true public TreeSet TreeSetOfDouble25",
    "embedded-element=false public TreeSet TreeSetOfDouble26",
    "public TreeSet TreeSetOfFloat27",
    "embedded-element=true public TreeSet TreeSetOfFloat28",
    "embedded-element=false public TreeSet TreeSetOfFloat29",
    "public TreeSet TreeSetOfInteger30",
    "embedded-element=true public TreeSet TreeSetOfInteger31",
    "embedded-element=false public TreeSet TreeSetOfInteger32",
    "public TreeSet TreeSetOfLong33",
    "embedded-element=true public TreeSet TreeSetOfLong34",
    "embedded-element=false public TreeSet TreeSetOfLong35",
    "public TreeSet TreeSetOfShort36",
    "embedded-element=true public TreeSet TreeSetOfShort37",
    "embedded-element=false public TreeSet TreeSetOfShort38"
  };

  public int getLength() {
    return fieldSpecs.length;
  }

  @SuppressWarnings("rawtypes")
  public TreeSet get(int index) {
    switch (index) {
      case (0):
        return TreeSetOfObject0;
      case (1):
        return TreeSetOfObject1;
      case (2):
        return TreeSetOfObject2;
      case (3):
        return TreeSetOfSimpleClass3;
      case (4):
        return TreeSetOfSimpleClass4;
      case (5):
        return TreeSetOfSimpleClass5;
      case (6):
        return TreeSetOfSimpleInterface6;
      case (7):
        return TreeSetOfSimpleInterface7;
      case (8):
        return TreeSetOfSimpleInterface8;
      case (9):
        return TreeSetOfString9;
      case (10):
        return TreeSetOfString10;
      case (11):
        return TreeSetOfString11;
      case (12):
        return TreeSetOfDate12;
      case (13):
        return TreeSetOfDate13;
      case (14):
        return TreeSetOfDate14;
      case (15):
        return TreeSetOfBigDecimal15;
      case (16):
        return TreeSetOfBigDecimal16;
      case (17):
        return TreeSetOfBigDecimal17;
      case (18):
        return TreeSetOfBigInteger18;
      case (19):
        return TreeSetOfBigInteger19;
      case (20):
        return TreeSetOfBigInteger20;
      case (21):
        return TreeSetOfByte21;
      case (22):
        return TreeSetOfByte22;
      case (23):
        return TreeSetOfByte23;
      case (24):
        return TreeSetOfDouble24;
      case (25):
        return TreeSetOfDouble25;
      case (26):
        return TreeSetOfDouble26;
      case (27):
        return TreeSetOfFloat27;
      case (28):
        return TreeSetOfFloat28;
      case (29):
        return TreeSetOfFloat29;
      case (30):
        return TreeSetOfInteger30;
      case (31):
        return TreeSetOfInteger31;
      case (32):
        return TreeSetOfInteger32;
      case (33):
        return TreeSetOfLong33;
      case (34):
        return TreeSetOfLong34;
      case (35):
        return TreeSetOfLong35;
      case (36):
        return TreeSetOfShort36;
      case (37):
        return TreeSetOfShort37;
      case (38):
        return TreeSetOfShort38;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  public boolean set(int index, @SuppressWarnings("rawtypes") TreeSet value) {
    if (fieldSpecs[index].indexOf("final") != -1) return false;
    switch (index) {
      case (0):
        TreeSetOfObject0 = value;
        break;
      case (1):
        TreeSetOfObject1 = value;
        break;
      case (2):
        TreeSetOfObject2 = value;
        break;
      case (3):
        TreeSetOfSimpleClass3 = value;
        break;
      case (4):
        TreeSetOfSimpleClass4 = value;
        break;
      case (5):
        TreeSetOfSimpleClass5 = value;
        break;
      case (6):
        TreeSetOfSimpleInterface6 = value;
        break;
      case (7):
        TreeSetOfSimpleInterface7 = value;
        break;
      case (8):
        TreeSetOfSimpleInterface8 = value;
        break;
      case (9):
        TreeSetOfString9 = value;
        break;
      case (10):
        TreeSetOfString10 = value;
        break;
      case (11):
        TreeSetOfString11 = value;
        break;
      case (12):
        TreeSetOfDate12 = value;
        break;
      case (13):
        TreeSetOfDate13 = value;
        break;
      case (14):
        TreeSetOfDate14 = value;
        break;
      case (15):
        TreeSetOfBigDecimal15 = value;
        break;
      case (16):
        TreeSetOfBigDecimal16 = value;
        break;
      case (17):
        TreeSetOfBigDecimal17 = value;
        break;
      case (18):
        TreeSetOfBigInteger18 = value;
        break;
      case (19):
        TreeSetOfBigInteger19 = value;
        break;
      case (20):
        TreeSetOfBigInteger20 = value;
        break;
      case (21):
        TreeSetOfByte21 = value;
        break;
      case (22):
        TreeSetOfByte22 = value;
        break;
      case (23):
        TreeSetOfByte23 = value;
        break;
      case (24):
        TreeSetOfDouble24 = value;
        break;
      case (25):
        TreeSetOfDouble25 = value;
        break;
      case (26):
        TreeSetOfDouble26 = value;
        break;
      case (27):
        TreeSetOfFloat27 = value;
        break;
      case (28):
        TreeSetOfFloat28 = value;
        break;
      case (29):
        TreeSetOfFloat29 = value;
        break;
      case (30):
        TreeSetOfInteger30 = value;
        break;
      case (31):
        TreeSetOfInteger31 = value;
        break;
      case (32):
        TreeSetOfInteger32 = value;
        break;
      case (33):
        TreeSetOfLong33 = value;
        break;
      case (34):
        TreeSetOfLong34 = value;
        break;
      case (35):
        TreeSetOfLong35 = value;
        break;
      case (36):
        TreeSetOfShort36 = value;
        break;
      case (37):
        TreeSetOfShort37 = value;
        break;
      case (38):
        TreeSetOfShort38 = value;
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
