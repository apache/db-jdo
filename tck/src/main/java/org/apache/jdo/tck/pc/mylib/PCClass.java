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

import javax.jdo.listener.LoadCallback;

public class PCClass implements LoadCallback {

    private long id;
    private int number1;
    private int number2;
    
    private int transientNumber1;
    private int transientNumber2;

    public PCClass() {
    }

    /**
     * @see LoadCallback#jdoPostLoad()
     */
    public void jdoPostLoad() {
        transientNumber1 = number1;
        transientNumber2 = number2;
    }

    /**
     * @see Object#toString()
     */
    public String toString() { 
        return "PCClass(" + id + ")";
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() { return (int)id ; }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (other != null && (other instanceof PCClass)) {
            PCClass k = (PCClass)other;
            return k.id == this.id;
        }
        return false;
    }
    
    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Returns the number.
     */
    public int getNumber1() {
        return number1;
    }

    /**
     * @param number The number to set.
     */
    public void setNumber1(int number) {
        this.number1 = number;
    }

    /**
     * @return Returns the number2.
     */
    public int getNumber2() {
        return number2;
    }

    /**
     * @param number2 The number2 to set.
     */
    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    /**
     * @return Returns the transientNumber1.
     */
    public int getTransientNumber1() {
        return transientNumber1;
    }

    /**
     * @param transientNumber1 The transientNumber1 to set.
     */
    public void setTransientNumber1(int transientNumber1) {
        this.transientNumber1 = transientNumber1;
    }

    /**
     * @return Returns the transientNumber2.
     */
    public int getTransientNumber2() {
        return transientNumber2;
    }

    /**
     * @param transientNumber2 The transientNumber2 to set.
     */
    public void setTransientNumber2(int transientNumber2) {
        this.transientNumber2 = transientNumber2;
    }

    /**
     * The objectid class for this class in case of application identity. 
     */
    public static class Oid implements Serializable, Comparable {

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

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherId = ((Oid)o).id;
            if (id == otherId)
                return 0;
            else if (id < otherId)
                return -1;
            return 1;
        }
    }
}

