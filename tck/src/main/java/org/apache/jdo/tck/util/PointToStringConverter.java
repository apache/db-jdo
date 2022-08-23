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
import org.apache.jdo.tck.pc.mylib.Point;

import javax.jdo.AttributeConverter;

/** AttributeConverter implementation mapping a Point instance to a string of the form x:y. */
public class PointToStringConverter implements AttributeConverter<Point, String> {

  private static int nrOfConvertToDatastoreCalls = 0;
  private static int nrOfConvertToAttributeCalls = 0;

  // Character to separate x and y value of the Point instance.
  private static final String SEPARATOR = ":";

  private final Log logger = LogFactory.getFactory().getInstance("org.apache.jdo.tck");

  /**
   * Converts the given Point attribute value to its string representation in the datastore.
   *
   * @param attributeValue the attribute value of type Point to be converted
   * @return the string representation of the Point instance
   */
  @Override
  public String convertToDatastore(Point attributeValue) {
    nrOfConvertToDatastoreCalls++;
    String datastoreValue = null;
    if (attributeValue != null) {
      datastoreValue =
          attributeValue.getX()
              + SEPARATOR
              + (attributeValue.getY() == null ? Integer.valueOf(0) : attributeValue.getY());
    }
    if (logger.isDebugEnabled()) {
      logger.debug(
          "PointToStringConverter.convertToDatastore "
              + "attributeValue="
              + attributeValue
              + " datastoreValue="
              + datastoreValue);
    }
    return datastoreValue;
  }

  /**
   * Converts the given string datastore value to its representation as a persistent attribute of
   * type Point.
   *
   * @param datastoreValue the string value in the datastore
   * @return the attribute value as Point instance
   */
  @Override
  public Point convertToAttribute(String datastoreValue) {
    nrOfConvertToAttributeCalls++;
    Point attributeValue = null;
    if (datastoreValue != null) {
      String[] parts = datastoreValue.split(SEPARATOR);
      if (parts.length == 2) {
        Integer x = Integer.valueOf(parts[0]);
        Integer y = Integer.valueOf(parts[1]);
        attributeValue = new Point(x == null ? 0 : x.intValue(), y);
      }
    }
    if (logger.isDebugEnabled()) {
      logger.debug(
          "PointToStringConverter.convertToAttribute "
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
