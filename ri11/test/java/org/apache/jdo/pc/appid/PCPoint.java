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

package org.apache.jdo.pc.appid;

/**
* A simple class with two fields for application identity
*
* @author Marina Vatkina
*/
public class PCPoint {
    public int x;
    public Integer y;

    public PCPoint() { }

    public PCPoint(int x, int y) {
        this.x = x;
        this.y = new Integer(y);
    }

    public PCPoint(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        String rc = null;
        try {
            rc = org.apache.jdo.test.util.Util.getClassName(this) + name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCPoint's values";
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

    public static class Oid {
        public int x;

        public Oid() {
        }

        public Oid(String s) { x = Integer.parseInt(s); }

        public String toString() { return this.getClass().getName() + ": "  + x;}

        public int hashCode() { return x ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.x == this.x;
            }
            return false;
        }

    }   
}
