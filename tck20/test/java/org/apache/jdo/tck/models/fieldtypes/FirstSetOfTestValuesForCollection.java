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
 
package org.apache.jdo.tck.models.fieldtypes;

import java.util.*;
import java.math.*;

import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;

public class FirstSetOfTestValuesForCollection extends Hashtable {

  public FirstSetOfTestValuesForCollection() {
    String [] elementTypes  = {"Object", "SimpleClass", "SimpleInterface",
                                             "String", "Date", "Locale", "BigDecimal",
                                             "BigInteger", "Byte", "Double", "Float",
                                             "Integer", "Long", "Short"};

    Vector objectVector = new Vector();
    objectVector.add(0, new SimpleClass(1, "Hello World"));
    objectVector.add(1, new SimpleClass(2, "Java Data Objects"));
    objectVector.add(2, new SimpleClass(2, "Java"));
    objectVector.add(3, new SimpleClass(4, "Origami"));
    objectVector.add(4, new SimpleClass(5, "watch"));
    put("Object", objectVector);

    Vector simpleClassVector = new Vector();
    simpleClassVector.add(0, new SimpleClass(1, "Welcome"));
    simpleClassVector.add(1, new SimpleClass(2, "To"));
    simpleClassVector.add(2, new SimpleClass(3, "The"));
    simpleClassVector.add(3, new SimpleClass(4, "Beautiful"));
    simpleClassVector.add(4, new SimpleClass(5, "World"));
    put("SimpleClass", simpleClassVector);
    put("SimpleInterface", simpleClassVector);


    Vector stringVector = new Vector();
    stringVector.add(0, new String("Hello"));
    stringVector.add(1, new String("Welcome"));
    stringVector.add(2, new String("To The"));
    stringVector.add(3, new String("Beautiful"));
    stringVector.add(4, new String("World"));
    put("String", stringVector);

    Vector dateVector = new Vector();
    dateVector.add(0, new Date(2007908));
    dateVector.add(1, new Date(89067382l));
    dateVector.add(2, new Date(890673822));
    dateVector.add(3, new Date(890673823));
    dateVector.add(4, new Date(890673824));
    put("Date", dateVector);

    Vector localeVector = new Vector();
    localeVector.add(0, Locale.CHINA);
    localeVector.add(1, Locale.FRANCE);
    localeVector.add(2, Locale.GERMANY);
    localeVector.add(3, Locale.JAPAN);
    localeVector.add(4, Locale.ITALY);
    put("Locale", localeVector);


    Vector bigDecVector = new Vector();
    bigDecVector.add(0, new BigDecimal("2007908.54548"));
    bigDecVector.add(1, new BigDecimal("0.544"));
    bigDecVector.add(2, new BigDecimal("3002323232.545454"));
    bigDecVector.add(3, new BigDecimal("64564645656.78657"));
    bigDecVector.add(4, new BigDecimal("4564565465.2342"));
    put("BigDecimal", bigDecVector);

    Vector bigIntVector = new Vector();
    bigIntVector.add(0, new BigInteger("2007908"));
    bigIntVector.add(1, new BigInteger("767575"));
    bigIntVector.add(2, new BigInteger("3002323232"));
    bigIntVector.add(3, new BigInteger("64564645656"));
    bigIntVector.add(4, new BigInteger("456445645"));
    put("BigInteger", bigIntVector);

    Vector byteVector = new Vector();
    byteVector.add(0, new Byte((byte)Byte.MIN_VALUE));
    byteVector.add(1, new Byte((byte)Byte.MAX_VALUE));
    byteVector.add(2, new Byte((byte)(Byte.MAX_VALUE- 20)));
    byteVector.add(3, new Byte((byte)(Byte.MAX_VALUE - 50)));
    byteVector.add(4, new Byte((byte)(Byte.MAX_VALUE - 75)));
    put("Byte", byteVector);

    Vector doubleVector = new Vector();
    doubleVector.add(0, new Double(AllTypes.DOUBLE_SMALLEST));
    doubleVector.add(1, new Double(AllTypes.DOUBLE_LARGEST));
    doubleVector.add(2, new Double(AllTypes.DOUBLE_LARGEST - 20000));
    doubleVector.add(3, new Double(AllTypes.DOUBLE_LARGEST - 454545.436664));
    doubleVector.add(4, new Double(AllTypes.DOUBLE_LARGEST - 2323235.76764677));
    put("Double", doubleVector);

    Vector floatVector = new Vector();
    floatVector.add(0, new Float(AllTypes.FLOAT_SMALLEST));
    floatVector.add(1, new Float(AllTypes.FLOAT_LARGEST));
    floatVector.add(2, new Float(AllTypes.FLOAT_LARGEST - 20000));
    floatVector.add(3, new Float(AllTypes.FLOAT_LARGEST - 454545.434));
    floatVector.add(4, new Float(AllTypes.FLOAT_LARGEST - 565656.43434));
    put("Float", floatVector);

    Vector integerVector = new Vector();
    integerVector.add(0, new Integer(Integer.MIN_VALUE));
    integerVector.add(1, new Integer(Integer.MAX_VALUE));
    integerVector.add(2, new Integer(Integer.MAX_VALUE - 20000));
    integerVector.add(3, new Integer(Integer.MAX_VALUE - 343434343));
    integerVector.add(4, new Integer(Integer.MAX_VALUE - 565656));
    put("Integer", integerVector);


    Vector longVector = new Vector();
    longVector.add(0, new Long(Long.MIN_VALUE));
    longVector.add(1, new Long(Long.MAX_VALUE));
    longVector.add(2, new Long(Long.MAX_VALUE - 20000));
    longVector.add(3, new Long(Long.MAX_VALUE - 343434343));
    longVector.add(4, new Long(Long.MAX_VALUE - 565656));
    put("Long", longVector);

    Vector shortVector = new Vector();
    shortVector.add(0, new Short(Short.MIN_VALUE));
    shortVector.add(1, new Short(Short.MAX_VALUE));
    shortVector.add(2, new Short((short)(Short.MAX_VALUE - 20000)));
    shortVector.add(3, new Short((short)(Short.MAX_VALUE - 343)));
    shortVector.add(4, new Short((short)(Short.MAX_VALUE - 5656)));
    put("Short", shortVector);
  }

}
