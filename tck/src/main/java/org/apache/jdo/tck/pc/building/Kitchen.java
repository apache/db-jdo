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
package org.apache.jdo.tck.pc.building;

import java.io.Serializable;

/** This class represents an online shopping cart.  It has a list of entries of
 * type CartEntry.
 */
public class Kitchen implements Serializable {

    protected static long nextId = System.currentTimeMillis();

    public synchronized static long nextId() {
        return nextId++;
    }

    /** Identity field for use with application identity */
    protected long id;

    /** The Oven we are using. */
    protected Oven oven;

    public Kitchen() {
    	this(nextId());
    }

    public Kitchen(long id) {
        setId(id);
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public void setOven(Oven ov) {
    	this.oven = ov;
    }

    public Oven getOven() {
    	return oven;
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof Kitchen)) {
            return false;
        }
        return equals((Kitchen) that);
    }

    public boolean equals(Kitchen that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        return this.id == that.id;
    }

    public int hashCode() {
        return (int) id;
    }

    public static class Oid implements Serializable {
        public long id;

        public Oid() {
        }

        public Oid(String s) {
            id = Long.parseLong(justTheId(s));
        }

        public String toString() {
            return this.getClass().getName() + ":" + id;
        }

        public int hashCode() {
            return (int) id;
        }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid that = (Oid) other;
                return that.id == this.id;
            }
            return false;
        }

        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }
    }
}
