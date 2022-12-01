/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */
package org.apache.jdo.tck.pc.shoppingcart;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** This class represents an online shopping cart. It has a list of entries of type CartEntry. */
public class Cart implements Serializable {

  private static final long serialVersionUID = 1L;

  protected static long nextId = System.currentTimeMillis();

  public static synchronized long nextId() {
    return nextId++;
  }

  /** Identity field for use with application identity */
  protected long id;

  /** The identity of the customer whose shopping cart this is. */
  protected String customerId;

  /** The list of entries in this cart */
  protected Set<CartEntry> entries = new HashSet<>(); // element-type CartEntry

  /** For JDO compliance only; not for public consumption. */
  protected Cart() {}

  public Cart(String customerId) {
    this(nextId(), customerId);
  }

  protected Cart(long id, String customerId) {
    setId(id);
    setCustomerId(customerId);
  }

  public long getId() {
    return id;
  }

  protected void setId(long id) {
    this.id = id;
  }

  public CartEntry newCartEntry(Product product, int quantity) {
    CartEntry ce = new CartEntry(this, CartEntry.nextId(), product, quantity);
    entries.add(ce);
    return ce;
  }

  public void addCartEntry(CartEntry ce) {
    if (ce == null) {
      throw new IllegalArgumentException("no CartEntry given");
    }
    ce.setCart(this);
    entries.add(ce);
  }

  public Iterator<CartEntry> getEntries() {
    return entries.iterator();
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (!(that instanceof Cart)) {
      return false;
    }
    return equals((Cart) that);
  }

  public boolean equals(Cart that) {
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

    private static final long serialVersionUID = 1L;

    public long id;

    public Oid() {}

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
