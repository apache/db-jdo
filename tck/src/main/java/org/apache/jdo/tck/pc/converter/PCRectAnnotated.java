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
import javax.jdo.annotations.Convert;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.PointToStringConverter;

/**
 * PersistenceCapable class to test JDO AttributeConverter interface. Its fields of type Point are
 * converted to strings in the datastore.
 */
@PersistenceCapable(table = "PCRectConv")
public class PCRectAnnotated implements IPCRect {
  private static long counter = new Date().getTime();

  private static synchronized long newId() {
    return counter++;
  }

  @Column(name = "ID")
  private long id = newId();

  @Column(name = "UPPER_LEFT")
  @Convert(value = PointToStringConverter.class)
  private Point upperLeft;

  @Column(name = "LOWER_RIGHT")
  @Convert(value = PointToStringConverter.class)
  private Point lowerRight;

  public PCRectAnnotated() {}

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
      rc =
          obj.getClass().getName()
              + " ul: "
              + getUpperLeft().name()
              + " lr: "
              + getLowerRight().name();
    } catch (NullPointerException ex) {
      rc = "NPE getting PCRectAnnotated's values";
    }
    return rc;
  }
}
