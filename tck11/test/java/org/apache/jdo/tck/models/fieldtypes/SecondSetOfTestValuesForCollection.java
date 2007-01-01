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

import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;

public class SecondSetOfTestValuesForCollection extends Hashtable {

  public SecondSetOfTestValuesForCollection() {
    String [] elementTypes  = {"Object", "SimpleClass", "SimpleInterface",
                                             "String", "Date", "Locale", "BigDecimal",
                                             "BigInteger", "Byte", "Double", "Float",
                                             "Integer", "Long", "Short"};

    Vector objectVector = new Vector();
    objectVector.add(0, new SimpleClass(1, "Hi There"));
    objectVector.add(1, new SimpleClass(1, "Hi"));
    objectVector.add(2, new SimpleClass(2, "Object"));
    objectVector.add(3, new SimpleClass(0, "Relational"));
    objectVector.add(4, new SimpleClass(3, "Hi There"));
    put("Object", objectVector);

    Vector simpleClassVector = new Vector();
    simpleClassVector.add(0, new SimpleClass(1, "Peaches"));
    simpleClassVector.add(1, new SimpleClass(2, "Oranges"));
    simpleClassVector.add(2, new SimpleClass(3, "Blue Berries"));
    simpleClassVector.add(3, new SimpleClass(4, "Apples"));
    simpleClassVector.add(4, new SimpleClass(5, "Strawberries"));
    put("SimpleClass", simpleClassVector);
    put("SimpleInterface", simpleClassVector);


    Vector stringVector = new Vector();
    stringVector.add(0, new String("Peaches"));
    stringVector.add(1, new String("Oranges"));
    stringVector.add(2, new String("Blue Berries"));
    stringVector.add(3, new String("Apples"));
    stringVector.add(4, new String("Strawberries"));
    put("String", stringVector);

    Vector dateVector = new Vector();
    dateVector.add(0, new Date(54545));
    dateVector.add(1, new Date(8905454l));
    dateVector.add(2, new Date(323545445));
    dateVector.add(3, new Date(890748967382l));
    dateVector.add(4, new Date(954545));
    put("Date", dateVector);

    Vector localeVector = new Vector();
    localeVector.add(0, Locale.ENGLISH);
    localeVector.add(1, Locale.JAPANESE);
    localeVector.add(2, Locale.CANADA_FRENCH);
    localeVector.add(3, Locale.KOREA);
    localeVector.add(4, Locale.UK);
    put("Locale", localeVector);


    Vector bigDecVector = new Vector();
    bigDecVector.add(0, new BigDecimal("434238.5454898989"));
//    bigDecVector.add(1, new BigDecimal("8348967382l.544"));
    bigDecVector.add(1, new BigDecimal("6.544"));
    bigDecVector.add(2, new BigDecimal("55552323232.545454"));
    bigDecVector.add(3, new BigDecimal("6456456.7543543534865785"));
    bigDecVector.add(4, new BigDecimal("456456.4353452342"));
    put("BigDecimal", bigDecVector);

    Vector bigIntVector = new Vector();
    bigIntVector.add(0, new BigInteger("345345345"));
    bigIntVector.add(1, new BigInteger("543543543543544"));
    bigIntVector.add(2, new BigInteger("65323423432423423"));
    bigIntVector.add(3, new BigInteger("87845634534543"));
    bigIntVector.add(4, new BigInteger("53452567766657567"));
    put("BigInteger", bigIntVector);

    Vector byteVector = new Vector();
    byteVector.add(0, new Byte((byte)(Byte.MAX_VALUE-34)));
    byteVector.add(1, new Byte((byte)Byte.MIN_VALUE));
    byteVector.add(2, new Byte((byte)(Byte.MAX_VALUE- 76)));
    byteVector.add(3, new Byte((byte)Byte.MAX_VALUE));
    byteVector.add(4, new Byte((byte)(Byte.MAX_VALUE - 12)));
    put("Byte", byteVector);

    Vector doubleVector = new Vector();
    doubleVector.add(0, new Double(Double.MAX_VALUE - 343434));
    doubleVector.add(1, new Double(Double.MIN_VALUE));
    doubleVector.add(2, new Double(Double.MAX_VALUE));
    doubleVector.add(3, new Double(Double.MAX_VALUE - 65423445.436664));
    doubleVector.add(4, new Double(Double.MAX_VALUE - 7235.236764677));
    put("Double", doubleVector);

    Vector floatVector = new Vector();
    floatVector.add(0, new Float(Float.MAX_VALUE - 5452));
    floatVector.add(1, new Float(Float.MIN_VALUE));
    floatVector.add(2, new Float(Float.MAX_VALUE - 6564560.54));
    floatVector.add(3, new Float(Float.MAX_VALUE));
    floatVector.add(4, new Float(Float.MAX_VALUE - 9756.634));
    put("Float", floatVector);

    Vector integerVector = new Vector();
    integerVector.add(0, new Integer(Integer.MAX_VALUE - 54454));
    integerVector.add(1, new Integer(Integer.MIN_VALUE));
    integerVector.add(2, new Integer(Integer.MAX_VALUE));
    integerVector.add(3, new Integer(Integer.MAX_VALUE - 767234));
    integerVector.add(4, new Integer(Integer.MAX_VALUE - 23673446));
    put("Integer", integerVector);


    Vector longVector = new Vector();
    longVector.add(0, new Long(Long.MAX_VALUE - 545345454));
    longVector.add(1, new Long(Long.MIN_VALUE));
    longVector.add(2, new Long(Long.MAX_VALUE));
    longVector.add(3, new Long(Long.MAX_VALUE - 3543343));
    longVector.add(4, new Long(Long.MAX_VALUE - 556));
    put("Long", longVector);

    Vector shortVector = new Vector();
    shortVector.add(0, new Short((short)(Short.MAX_VALUE - 3434)));
    shortVector.add(1, new Short(Short.MIN_VALUE));
    shortVector.add(2, new Short((short)(Short.MAX_VALUE)));
    shortVector.add(3, new Short((short)(Short.MAX_VALUE - 23344)));
    shortVector.add(4, new Short((short)(Short.MAX_VALUE - 723)));
    put("Short", shortVector);
  }
}
