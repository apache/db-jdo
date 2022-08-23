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
package org.apache.jdo.tck.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jdo.AttributeConverter;

/** AttributeConverter implementation mapping a Integer instance to a string. */
public class IntegerToStringConverter implements AttributeConverter<Integer, String> {

  private static int nrOfConvertToDatastoreCalls = 0;
  private static int nrOfConvertToAttributeCalls = 0;

  private final Log logger = LogFactory.getFactory().getInstance("org.apache.jdo.tck");

  /**
   * Converts the given Integer attribute value to its string representation in the datastore.
   *
   * @param attributeValue the attribute value of type Integer to be converted
   * @return the string representation of the Integer instance
   */
  @Override
  public String convertToDatastore(Integer attributeValue) {
    nrOfConvertToDatastoreCalls++;
    String datastoreValue = attributeValue != null ? attributeValue.toString() : null;
    if (logger.isDebugEnabled()) {
      logger.debug(
          "IntegerToStringConverter.convertToDatastore "
              + "attributeValue="
              + attributeValue
              + " datastoreValue="
              + datastoreValue);
    }
    return datastoreValue;
  }

  /**
   * Converts the given string datastore value to its representation as a persistent attribute of
   * type Integer.
   *
   * @param datastoreValue the string value in the datastore
   * @return the attribute value as Integer instance
   */
  @Override
  public Integer convertToAttribute(String datastoreValue) {
    nrOfConvertToAttributeCalls++;
    Integer attributeValue = datastoreValue != null ? Integer.valueOf(datastoreValue) : null;
    if (logger.isDebugEnabled()) {
      logger.debug(
          "IntegerToStringConverter.convertToAttribute "
              + "datastoreValue="
              + datastoreValue
              + " attributeValue="
              + attributeValue);
    }
    return attributeValue;
  }

  /**
   * Method returning the current number of convertToDatastore method calls.
   *
   * @return number of convertToDatastore method calls
   */
  public static int getNrOfConvertToDatastoreCalls() {
    return nrOfConvertToDatastoreCalls;
  }

  /**
   * Method returning the current number of convertToAttribute method calls.
   *
   * @return number of convertToAttribute method calls
   */
  public static int getNrOfConvertToAttributeCalls() {
    return nrOfConvertToAttributeCalls;
  }
}
