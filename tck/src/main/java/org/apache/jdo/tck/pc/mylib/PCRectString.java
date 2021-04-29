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
package org.apache.jdo.tck.pc.mylib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jdo.AttributeConverter;
import java.io.Serializable;
import java.util.Date;

/**
 * PersistenceCapable class to test JDO AttributeConverter interface.
 * It's fields of type Point are converted to strings in the datastore.
 *
 * @author Michael Bouschen
 */
public class PCRectString {
    private static long counter = new Date().getTime();

    private static synchronized long newId() {
        return counter++;
    }

    public long id = newId();

    private Point upperLeft;
    private Point lowerRight;

    public PCRectString() {
    }

    public PCRectString(long id, Point ul, Point lr) {
        upperLeft = ul;
        lowerRight = lr;
    }

    public PCRectString(Point ul, Point lr) {
        upperLeft = ul;
        lowerRight = lr;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        String rc = null;
        Object obj = this;
        try {
            rc = obj.getClass().getName()
                    + " ul: " + getUpperLeft().name()
                    + " lr: " + getLowerRight().name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCRectString's values";
        }
        return rc;
    }

    /**
     * PCRectString'S ObjectId class.
     */
    public static class Oid implements Serializable {
        public long id;

        public Oid() {
        }

        public Oid(String s) { id = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + id;}

        public int hashCode() { return (int)id ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof PCRectString.Oid)) {
                PCRectString.Oid k = (PCRectString.Oid)other;
                return k.id == this.id;
            }
            return false;
        }

        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

    }

    /**
     * AttributeConverter implementation mapping a Point instance to a string of the form x:y.
     */
    public static class PointToStringConverter implements AttributeConverter<Point, String> {

        private static int nrOfConvertToDatastoreCalls = 0;
        private static int nrOfConvertToAttributeCalls = 0;

        // Character to separate x and y value of teh Pont instance.
        private static final String SEPARATOR = ":";

        private Log logger = LogFactory.getFactory().getInstance("org.apache.jdo.tck");

        /**
         * Converts the given Point attribute value to its string representation in the datastore.
         * @param attributeValue the attribute value of type Point to be converted
         * @return the string representation of the Point instance
         */
        @Override
        public String convertToDatastore(Point attributeValue) {
            nrOfConvertToDatastoreCalls++;
            String datastoreValue = null;
            if (attributeValue != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(attributeValue.getX());
                builder.append(SEPARATOR);
                builder.append(attributeValue.getY() == null ? Integer.valueOf(0) : attributeValue.getY());
                datastoreValue = builder.toString();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("PointToStringConverter.convertToDatastore " +
                        "attributeValue=" + attributeValue + " datastoreValue=" + datastoreValue);
            }
            return datastoreValue;
        }

        /**
         * Converts the given string datastore value to its representation as a persistent attribute of type Point.
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
                logger.debug("PointToStringConverter.convertToAttribute " +
                        "datastoreValue=" + datastoreValue + " attributeValue=" + attributeValue);
            }
            return attributeValue;
        }

        public static int getNrOfConvertToDatastoreCalls() {
            return nrOfConvertToDatastoreCalls;
        }
        public static int getNrOfConvertToAttributeCalls() {
            return nrOfConvertToAttributeCalls;
        }
    }
}
