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
package org.apache.jdo.tck.pc.converter;

import java.util.Date;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.jdo.tck.pc.mylib.ConvertiblePoint;
import org.apache.jdo.tck.pc.mylib.Point;

/**
 * PersistenceCapable class to test JDO AttributeConverter interface. Its fields of type Point are
 * converted to strings in the datastore. The conversion is declared directly on the type
 * ConvertiblePoint.
 */
@PersistenceCapable(table = "PCRectConv")
public class PCRectPointTypeAnnotated implements IPCRect {
  private static long counter = new Date().getTime();

  private static synchronized long newId() {
    return counter++;
  }

  @Column(name = "ID")
  private long id = newId();

  @Column(name = "UPPER_LEFT")
  private ConvertiblePoint upperLeft;

  @Column(name = "LOWER_RIGHT")
  private ConvertiblePoint lowerRight;

  public PCRectPointTypeAnnotated() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Point getUpperLeft() {
    return new Point(upperLeft.getX(), upperLeft.getY());
  }

  public void setUpperLeft(Point upperLeft) {
    this.upperLeft = new ConvertiblePoint(upperLeft.getX(), upperLeft.getY());
  }

  public Point getLowerRight() {
    return new Point(lowerRight.getX(), lowerRight.getY());
  }

  public void setLowerRight(Point lowerRight) {
    this.lowerRight = new ConvertiblePoint(lowerRight.getX(), lowerRight.getY());
  }

  public String toString() {
    String rc = null;
    Object obj = this;
    try {
      rc =
          obj.getClass().getName()
              + " ul: "
              + getUpperLeft().name()
              + " lr: "
              + getLowerRight().name();
    } catch (NullPointerException ex) {
      rc = "NPE getting PCRectPointTypeAnnotated's values";
    }
    return rc;
  }
}
