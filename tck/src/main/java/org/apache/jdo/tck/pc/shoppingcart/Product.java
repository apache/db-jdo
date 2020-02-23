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

/** This class represents a product that you would buy from an online store.
 */
public class Product implements Serializable {

    /** Identity field when used with application identity */
    protected String sku;

    protected String description;

    /** No-arg constructor required by JDO; not for public consumption */
    protected Product() {}

    /**
     * Domain model constructor
     * @param sku identity field
     */
    public Product(String sku) {
        setSku(sku);
    }

    /**
     * Constructor with sku and description
     * @param sku identity field
     * @param description description
     */
    public Product(String sku, String description) {
        this(sku);
        setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        if (sku == null || (sku = sku.trim()).length() == 0) {
            throw new IllegalArgumentException("no sku given");
        }
        this.sku = sku.toUpperCase();
    }

    public static class Oid implements Serializable {
        public String sku;

        public Oid() {
        }

        public Oid(String s) {
            sku = justTheSku(s);
        }

        public String toString() {
            return this.getClass().getName() + ":" + sku;
        }

        public int hashCode() {
            return sku.hashCode();
        }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid that = (Oid) other;
                return that.sku.equals(this.sku);
            }
            return false;
        }

        protected static String justTheSku(String str) {
            return str.substring(str.indexOf(':') + 1);
        }
    }
}
