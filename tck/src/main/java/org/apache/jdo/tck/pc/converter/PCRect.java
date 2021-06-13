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
package org.apache.jdo.tck.pc.converter;

import org.apache.jdo.tck.pc.mylib.Point;

import java.util.Date;

/**
 * PersistenceCapable class to test JDO AttributeConverter interface.
 * Its fields of type Point are converted to strings in the datastore.
 */
public class PCRect implements IPCRect {
    private static long counter = new Date().getTime();

    private static synchronized long newId() {
        return counter++;
    }

    private long id = newId();
    private Point upperLeft;
    private Point lowerRight;

    public PCRect() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }
    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }
    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
    }

    public String toString() {
        String rc = null;
        Object obj = this;
        try {
            rc = obj.getClass().getName()
                    + " ul: " + getUpperLeft().name()
                    + " lr: " + getLowerRight().name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCRect's values";
        }
        return rc;
    }
}
