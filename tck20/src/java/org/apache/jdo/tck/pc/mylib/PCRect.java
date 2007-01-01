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

import java.io.Serializable;
import java.util.Date;

/**
* Test for instances with application identity containing instances of 
* a user-defined class.
*
* @author Marina Vatkina
*/
public class PCRect {
    
    private static long counter = new Date().getTime();
    private static long newId() {
        synchronized (PCRect.class) {
            return counter++;
        }
    }

    public long id = newId();

    public PCPoint upperLeft;
    public PCPoint lowerRight;

    public PCRect() { }

    public PCRect(long id, PCPoint ul, PCPoint lr) {
        upperLeft = ul;
        lowerRight = lr;
    }

    public PCRect(PCPoint ul, PCPoint lr) {
        upperLeft = ul;
        lowerRight = lr;
    }

    public PCPoint getUpperLeft() {
        return upperLeft;
    }

    public PCPoint getLowerRight() {
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
            //rc = Util.getClassName(this)
	     rc = obj.getClass().getName()
                + " ul: " + getUpperLeft().name()
                + " lr: " + getLowerRight().name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCRect's values";
        }
        return rc;
    }

    public static class Oid implements Serializable {
        public long id;

        public Oid() {
        }

        public Oid(String s) { id = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + id;}

        public int hashCode() { return (int)id ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.id == this.id;
            }
            return false;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

    }

}
