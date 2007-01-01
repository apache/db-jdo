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
 


package org.apache.jdo.tck.models.fieldtypes;

import java.util.*;
import java.math.*;

import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;

public class TestUtil {

  public TestUtil() {
  }

  //gets the value type for fields of a collection class
  public static String getFieldSpecs(String field) {
    //sample field =public Collection CollectionOfObject0
    //look for last space and get the String before the  numbers
    String valueType = "";

    int indexOfLastSpace = field.lastIndexOf(" ");
    String fieldName = field.substring(indexOfLastSpace);
    int indexOfValueType = fieldName.indexOf("Of") + 2;
    String valueTypeWithNumber = fieldName.substring(indexOfValueType);
    int lastIndexOfValueType = 0;
    for (int i=valueTypeWithNumber.length() -1; i>=0; i--) {
      if (Character.isDigit(valueTypeWithNumber.charAt(i))) {
        continue;
      } else {
        lastIndexOfValueType = i;
        break;
      }
    }
    valueType =  valueTypeWithNumber.substring(0, lastIndexOfValueType+1);

    return valueType;
  }


    //gets the key type and value type for fields of a Map class
  public static Vector getFieldSpecsForMap(String field) {
      //sample field =public HashMap HashMapOfObject_Object0
      //fieldType -- look for the last space and get the value between Of and _
      //valueType -- look for last _ and get the String before the  numbers
      String fieldType = "";
      String valueType = "";

      int indexOfLastSpace = field.lastIndexOf(" ");
      String fieldName = field.substring(indexOfLastSpace);
      int indexOfFieldType = fieldName.indexOf("Of") + 2;
      String fieldTypeWithValueType = fieldName.substring(indexOfFieldType);
      int indexOfUnderScore = fieldTypeWithValueType.indexOf("_");
      fieldType = fieldTypeWithValueType.substring(0, indexOfUnderScore);

      String valueTypeWithNumber =
              fieldTypeWithValueType.substring(indexOfUnderScore + 1);
      int lastIndexOfValueType = 0;
      for (int i=valueTypeWithNumber.length() -1; i>=0; i--) {
        if (Character.isDigit(valueTypeWithNumber.charAt(i))) {
          continue;
        } else {
          lastIndexOfValueType = i;
          break;
        }
      }
      valueType =  valueTypeWithNumber.substring(0, lastIndexOfValueType+1);

      Vector fieldSpecs = new Vector();
      fieldSpecs.add(fieldType);
      fieldSpecs.add(valueType);

      return fieldSpecs;
  }

  public static String [] elementTypes = new String[]
            {"Object", "SimpleClass", "SimpleInterface",
            "String", "Date", "Locale", "BigDecimal",
            "BigInteger", "Byte", "Double", "Float",
            "Integer", "Long", "Short"};

  private static int getIndexOf(String type) {
      for (int i=0; i < elementTypes.length; i++) {
          if (type.equals(elementTypes[i]))
              return i;
      }
      return 9999;
  }

  public static Vector makeNewVectorInstance(String type, int order) {

      Vector vec = new Vector();

      switch (order) {
          case(1):
              switch (getIndexOf(type)) {
                  case(0):
                      vec.add(0, new SimpleClass(1, "Hello World"));
                      vec.add(1, new SimpleClass(2, "Java Data Objects"));
                      vec.add(2, new SimpleClass(2, "Java"));
                      vec.add(3, new SimpleClass(4, "Origami"));
                      vec.add(4, new SimpleClass(5, "watch"));
                      break;
                  case(1):
                  case(2):
                      vec.add(0, new SimpleClass(1, "Welcome"));
                      vec.add(1, new SimpleClass(2, "To"));
                      vec.add(2, new SimpleClass(3, "The"));
                      vec.add(3, new SimpleClass(4, "Beautiful"));
                      vec.add(4, new SimpleClass(5, "World"));
                      break;
                  case(3):
                      vec.add(0, new String("Hello"));
                      vec.add(1, new String("Welcome"));
                      vec.add(2, new String("To The"));
                      vec.add(3, new String("Beautiful"));
                      vec.add(4, new String("World"));
                      break;
                  case(4):
                      vec.add(0, new Date(2007908));
                      vec.add(1, new Date(89067382l));
                      vec.add(2, new Date(890673822));
                      vec.add(3, new Date(890673823));
                      vec.add(4, new Date(890673824));
                      break;
                  case(5):
                      vec.add(0, Locale.CHINA);
                      vec.add(1, Locale.FRANCE);
                      vec.add(2, Locale.GERMANY);
                      vec.add(3, Locale.JAPAN);
                      vec.add(4, Locale.ITALY);
                      break;
                  case(6):
                      vec.add(0, new BigDecimal("2007908.54548"));
                      vec.add(1, new BigDecimal("0.544"));
                      vec.add(2, new BigDecimal("3002323232.545454"));
                      vec.add(3, new BigDecimal("64564645656.78657"));
                      vec.add(4, new BigDecimal("4564565465.2342"));
                      break;
                  case(7):
                      vec.add(0, new BigInteger("2007908"));
                      vec.add(1, new BigInteger("767575"));
                      vec.add(2, new BigInteger("3002323232"));
                      vec.add(3, new BigInteger("64564645656"));
                      vec.add(4, new BigInteger("456445645"));
                      break;
                  case(8):
                      vec.add(0, new Byte((byte)Byte.MIN_VALUE));
                      vec.add(1, new Byte((byte)Byte.MAX_VALUE));
                      vec.add(2, new Byte((byte)(Byte.MAX_VALUE- 20)));
                      vec.add(3, new Byte((byte)(Byte.MAX_VALUE - 50)));
                      vec.add(4, new Byte((byte)(Byte.MAX_VALUE - 75)));
                      break;
                  case(9):
                      vec.add(0, new Double(AllTypes.DOUBLE_SMALLEST));
                      vec.add(1, new Double(AllTypes.DOUBLE_LARGEST));
                      vec.add(2, new Double(AllTypes.DOUBLE_LARGEST - 20000));
                      vec.add(3, new Double(AllTypes.DOUBLE_LARGEST
                                  - 454545.436664));
                      vec.add(4, new Double(AllTypes.DOUBLE_LARGEST
                                  - 2323235.76764677));
                      break;
                  case(10):
                      vec.add(0, new Float(AllTypes.FLOAT_SMALLEST));
                      vec.add(1, new Float(AllTypes.FLOAT_LARGEST));
                      vec.add(2, new Float(AllTypes.FLOAT_LARGEST - 20000));
                      vec.add(3, new Float(AllTypes.FLOAT_LARGEST
                                  - 454545.434));
                      vec.add(4, new Float(AllTypes.FLOAT_LARGEST
                                  - 565656.43434));
                      break;
                  case(11):
                      vec.add(0, new Integer(Integer.MIN_VALUE));
                      vec.add(1, new Integer(Integer.MAX_VALUE));
                      vec.add(2, new Integer(Integer.MAX_VALUE - 20000));
                      vec.add(3, new Integer(Integer.MAX_VALUE - 343434343));
                      vec.add(4, new Integer(Integer.MAX_VALUE - 565656));
                      break;
                  case(12):
                      vec.add(0, new Long(Long.MIN_VALUE));
                      vec.add(1, new Long(Long.MAX_VALUE));
                      vec.add(2, new Long(Long.MAX_VALUE - 20000));
                      vec.add(3, new Long(Long.MAX_VALUE - 343434343));
                      vec.add(4, new Long(Long.MAX_VALUE - 565656));
                      break;
                  case(13):
                      vec.add(0, new Short(Short.MIN_VALUE));
                      vec.add(1, new Short(Short.MAX_VALUE));
                      vec.add(2, new Short((short)(Short.MAX_VALUE - 20000)));
                      vec.add(3, new Short((short)(Short.MAX_VALUE - 343)));
                      vec.add(4, new Short((short)(Short.MAX_VALUE - 5656)));
                      break;
                 default:
                      throw new IndexOutOfBoundsException();
              }
                  return vec;
          case(2):
              switch (getIndexOf(type)) {
                  case(0):
                    vec.add(0, new SimpleClass(1, "Hi There"));
                    vec.add(1, new SimpleClass(1, "Hi"));
                    vec.add(2, new SimpleClass(2, "Object"));
                    vec.add(3, new SimpleClass(0, "Relational"));
                    vec.add(4, new SimpleClass(3, "Hi There"));
                    break;
                  case(1):
                    vec.add(0, new SimpleClass(1, "Peaches"));
                    vec.add(1, new SimpleClass(2, "Oranges"));
                    vec.add(2, new SimpleClass(3, "Blue Berries"));
                    vec.add(3, new SimpleClass(4, "Apples"));
                    vec.add(4, new SimpleClass(5, "Strawberries"));
                    break;
                  case(2):
                    vec.add(0, new SimpleClass(1, "Peaches"));
                    vec.add(1, new SimpleClass(2, "Oranges"));
                    vec.add(2, new SimpleClass(3, "Blue Berries"));
                    vec.add(3, new SimpleClass(4, "Apples"));
                    vec.add(4, new SimpleClass(5, "Strawberries"));
                    break;
                  case(3):
                    vec.add(0, new String("Peaches"));
                    vec.add(1, new String("Oranges"));
                    vec.add(2, new String("Blue Berries"));
                    vec.add(3, new String("Apples"));
                    vec.add(4, new String("Strawberries"));
                    break;
                  case(4):
                    vec.add(0, new Date(54545));
                    vec.add(1, new Date(8905454l));
                    vec.add(2, new Date(323545445));
                    vec.add(3, new Date(890748967382l));
                    vec.add(4, new Date(954545));
                    break;
                  case(5):
                    vec.add(0, Locale.ENGLISH);
                    vec.add(1, Locale.JAPANESE);
                    vec.add(2, Locale.CANADA_FRENCH);
                    vec.add(3, Locale.KOREA);
                    vec.add(4, Locale.UK);
                    break;
                  case(6):
                    vec.add(0, new BigDecimal("434238.5454898989"));
                    vec.add(1, new BigDecimal("6.544"));
                    vec.add(2, new BigDecimal("55552323232.545454"));
                    vec.add(3, new BigDecimal("6456456.7543543534865785"));
                    vec.add(4, new BigDecimal("456456.4353452342"));
                    break;
                  case(7):
                    vec.add(0, new BigInteger("345345345345345345345345"));
                    vec.add(1, new BigInteger("543543543543544"));
                    vec.add(2, new BigInteger("65323423432423423"));
                    vec.add(3, new BigInteger("87845634534543"));
                    vec.add(4, new BigInteger("53452567766657567"));
                    break;
                  case(8):
                    vec.add(0, new Byte((byte)(Byte.MAX_VALUE-34)));
                    vec.add(1, new Byte((byte)Byte.MIN_VALUE));
                    vec.add(2, new Byte((byte)(Byte.MAX_VALUE- 76)));
                    vec.add(3, new Byte((byte)Byte.MAX_VALUE));
                    vec.add(4, new Byte((byte)(Byte.MAX_VALUE - 12)));
                    break;
                  case(9):
                    vec.add(0, new Double(AllTypes.DOUBLE_LARGEST - 343434));
                    vec.add(1, new Double(AllTypes.DOUBLE_SMALLEST));
                    vec.add(2, new Double(AllTypes.DOUBLE_LARGEST));
                    vec.add(3, new Double(AllTypes.DOUBLE_LARGEST
                                - 65423445.436664));
                    vec.add(4, new Double(AllTypes.DOUBLE_LARGEST
                                - 7235.236764677));
                    break;
                  case(10):
                    vec.add(0, new Float(AllTypes.FLOAT_LARGEST - 5452));
                    vec.add(1, new Float(AllTypes.FLOAT_SMALLEST));
                    vec.add(2, new Float(AllTypes.FLOAT_LARGEST - 6564560.54));
                    vec.add(3, new Float(AllTypes.FLOAT_LARGEST));
                    vec.add(4, new Float(AllTypes.FLOAT_LARGEST - 9756.634));
                    break;
                  case(11):
                    vec.add(0, new Integer(Integer.MAX_VALUE - 54454));
                    vec.add(1, new Integer(Integer.MIN_VALUE));
                    vec.add(2, new Integer(Integer.MAX_VALUE));
                    vec.add(3, new Integer(Integer.MAX_VALUE - 767234));
                    vec.add(4, new Integer(Integer.MAX_VALUE - 23673446));
                    break;
                  case(12):
                    vec.add(0, new Long(Long.MAX_VALUE - 545345454));
                    vec.add(1, new Long(Long.MIN_VALUE));
                    vec.add(2, new Long(Long.MAX_VALUE));
                    vec.add(3, new Long(Long.MAX_VALUE - 3543343));
                    vec.add(4, new Long(Long.MAX_VALUE - 556));
                    break;
                  case(13):
                    vec.add(0, new Short((short)(Short.MAX_VALUE - 3434)));
                    vec.add(1, new Short(Short.MIN_VALUE));
                    vec.add(2, new Short((short)(Short.MAX_VALUE)));
                    vec.add(3, new Short((short)(Short.MAX_VALUE - 23344)));
                    vec.add(4, new Short((short)(Short.MAX_VALUE - 723)));
                    break;
                 default:
                    throw new IndexOutOfBoundsException();
              }
              return vec;
          default:
               throw new IndexOutOfBoundsException();
          }
    }

    protected static boolean containsBigDecimalKey(BigDecimal keyValue,
                                                Set bigDecimalKeySet)
    {
        Iterator iter = bigDecimalKeySet.iterator();
        while (iter.hasNext()) {
            BigDecimal nextVal = (BigDecimal) iter.next();
            if (keyValue.compareTo(nextVal) == 0) {
                return true;
            }
        }
        return false;
    }

    protected static BigDecimal getBigDecimalKey(BigDecimal keyValue,
                                                Set bigDecimalKeySet)
    {
        Iterator iter = bigDecimalKeySet.iterator();
        while (iter.hasNext()) {
            BigDecimal nextVal = (BigDecimal) iter.next();
            if (keyValue.compareTo(nextVal) == 0) {
                return nextVal;
            }
        }
        return null;
    }
}
