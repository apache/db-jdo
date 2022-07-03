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
package org.apache.jdo.tck.pc.shoppingcart;

import java.io.Serializable;

/**
 * This class is a token undetachable class. It is only present to be
 * persistence-capable but not detachable so as to test that a detachable class
 * must be declared so.
 */
public class Undetachable implements Serializable {

    protected static long nextId = System.currentTimeMillis();

    public static synchronized long nextId() {
        return nextId++;
    }

    /** Identity field for use with application identity */
    protected long id;
    protected int foo;
    protected int bar;

    public Undetachable() {
        this(nextId());
    }

    public Undetachable(long id) {
        this.id = id;
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

    public int getFoo() {
        return foo;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

    public int getBar() {
        return bar;
    }

    public void setBar(int bar) {
        this.bar = bar;
    }
}
