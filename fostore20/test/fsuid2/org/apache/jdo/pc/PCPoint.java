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

package org.apache.jdo.pc;

/**
* A simple class with two fields
*
* @author Dave Bristor
*/
public class PCPoint {
    public int x;
    public Integer y;
    float z;

    public PCPoint() { }

    public PCPoint(int x, int y, float z) {
        this.x = x;
        this.y = new Integer(y);
        this.z = z;
    }

    public PCPoint(int x, Integer y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        String rc = null;
        try {
            rc = this.getClass().getName() + name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCPoint's values";
        }
        return rc;
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof PCPoint))
            return false;
        PCPoint other = (PCPoint)o;
        if (x != other.x)
            return false;
        // Caution: comparing floats
        if (z != other.z)
            return false;
        if (y == null)
            return other.y == null;
        else if (other.y == null)
            return y == null;
        else 
            return y.intValue() == other.y.intValue();
    }

    void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }

    void setZ(float z) {
        this.z = z;
    }

    float getZ() {
        return z;
    }
    
    public String name() {
        return " x: " + getX() + ", y: " + getY().intValue() + ", z: " + z;
    }
}
