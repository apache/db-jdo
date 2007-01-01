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

package org.apache.jdo.pc.appid;

/**
* Test for instances with application identity containing instances of 
* a user-defined class.
*
* @author Marina Vatkina
*/
public class PCRect {

    private int zid;

    private PCPoint upperLeft;
    private PCPoint lowerRight;

    public PCRect() { }

    public PCRect(int zid, PCPoint ul, PCPoint lr) {
        this.zid = zid;
        upperLeft = ul;
        lowerRight = lr;
    }

    public PCPoint getUpperLeft() {
        return upperLeft;
    }

    public PCPoint getLowerRight() {
        return lowerRight;
    }

    public int getId() {
        return zid;
    }

    public void setId(int zid) {
        this.zid = zid;
    }

    public String toString() {
        String rc = null;
        try {
            rc = org.apache.jdo.test.util.Util.getClassName(this)
                + " id: " + getId()
                + " ul: " + getUpperLeft().name()
                + " lr: " + getLowerRight().name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCRect's values";
        }
        return rc;
    }

    public static class Oid {
        public int zid;

        public Oid() {
        }

        public Oid(String s) { zid = Integer.parseInt(s); }

        public String toString() { return this.getClass().getName() + ": "  + zid;}

        public int hashCode() { return zid ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.zid == this.zid;
            }
            return false;
        }

    }

}
