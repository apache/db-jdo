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
 
package org.apache.jdo.tck.pc.mylib;

import java.io.Serializable;
import java.util.Date;

/**
* A simple class with two fields for application identity
*
* @author Marina Vatkina
*/
public class PCPoint implements Serializable {
    
    
    private static long counter = new Date().getTime();
    private static long newId() {
        synchronized (PCPoint.class) {
            return counter++;
        }
    }
    private long id = newId();
    public int x;
    public Integer y;

    public PCPoint() {
    }

    public PCPoint(int x, int y) {
        this.x = x;
        this.y = Integer.valueOf(y);
    }

    public PCPoint(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        String rc = null;
	Object obj = this;
        try {
	    rc = obj.getClass().getName();
            //rc = Util.getClassName(this) + name();
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
