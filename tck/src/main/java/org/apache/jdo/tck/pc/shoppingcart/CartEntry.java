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
package org.apache.jdo.tck.pc.shoppingcart;

import java.io.Serializable;

/** This class represents an entry in an online shopping cart that has a quantity of a product. */
public class CartEntry implements Serializable {

  private static final long serialVersionUID = 1L;

  protected static long nextId = System.currentTimeMillis();

  public static synchronized long nextId() {
    return nextId++;
  }

  /** Identity field for use with application identity */
  protected long id;

  protected Cart cart;
  protected Product product;
  protected int quantity = 1;

  /** No-arg constructor required by JDO; not for public consumption */
  protected CartEntry() {}

  public CartEntry(Cart cart, Product product) {
    this(cart, nextId(), product);
  }

  public CartEntry(Cart cart, long id, Product product) {
    this(cart, id, product, 1);
  }

  public CartEntry(Cart cart, long id, Product product, int quantity) {
    setId(id);
    setProduct(product);
    setQuantity(quantity);
    cart.addCartEntry(this);
  }

  public long getId() {
    return id;
  }

  protected void setId(long id) {
    this.id = id;
  }

  public Cart getCart() {
    return cart;
  }

  protected void setCart(Cart cart) {
    if (this.cart != null) {
      throw new IllegalStateException("Cart already set");
    }
    this.cart = cart;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    if (quantity < 1) {
      throw new IllegalArgumentException("quantity can't be less than one");
    }
    this.quantity = quantity;
  }

  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (!(that instanceof CartEntry)) {
      return false;
    }
    return equals((CartEntry) that);
  }

  public boolean equals(CartEntry that) {
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
