/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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

import java.io.Serializable;

import org.apache.jdo.test.util.Util;


/**
* A simple class with two fields for application identity
*
* @author Marina Vatkina
*/
public class PCPoint1 implements Serializable {
    public int x;
    public Integer y;

    public PCPoint1() { }

    public PCPoint1(int x, int y) {
        this.x = x;
        this.y = new Integer(y);
    }

    public PCPoint1(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        String rc = null;
        try {
            rc = Util.getClassName(this) + name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCPoint1's values";
        }
        return rc;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }
    
    public String name() {
        return " x: " + getX() + ", y: " + getY().intValue();
    }
}
