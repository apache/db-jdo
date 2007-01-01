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

import java.util.Vector;

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

      String valueTypeWithNumber = fieldTypeWithValueType.substring(indexOfUnderScore + 1);
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


}
