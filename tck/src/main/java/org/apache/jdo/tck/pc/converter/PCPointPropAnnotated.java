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

import org.apache.jdo.tck.util.IntegerToStringConverter;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Convert;
import javax.jdo.annotations.PersistenceCapable;
import java.util.Date;

/**
 * PersistenceCapable class to test JDO AttributeConverter interface.
 * Its fields of type int and Integer are converted to strings in the datastore.
 */
@PersistenceCapable(table="PCPointConv")
public class PCPointPropAnnotated implements IPCPoint {
    private static long counter = new Date().getTime();

    private static synchronized long newId() {
        return counter++;
    }

    private long id = newId();
    private int x;
    private Integer y;

    public PCPointPropAnnotated() {}

    @Column(name="ID")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="X")
    @Convert(value = IntegerToStringConverter.class)
    public int getX() {
        return x;
    }
    public void setX(int x ) {
        this.x = x;
    }

    @Column(name="Y")
    @Convert(value = IntegerToStringConverter.class)
    public Integer getY() {
        return y;
    }
    public void setY(Integer y) {
        this.y = y;
    }

    public String toString() {
        return this.getClass().getName() + "(x: " + x + " / y: " + y + ")";
    }
}
