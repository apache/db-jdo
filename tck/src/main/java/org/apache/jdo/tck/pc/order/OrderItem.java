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

package org.apache.jdo.tck.pc.order;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

public class OrderItem
    implements Serializable, Comparable<OrderItem>, Comparator<OrderItem>, DeepEquality {

  private static final long serialVersionUID = 1L;

  Order order;
  long item;
  String description;
  int quantity;

  public OrderItem() {}

  public OrderItem(Order order, long item, String description, int quantity) {
    this.order = order;
    this.item = item;
    this.description = description;
    this.quantity = quantity;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public long getItem() {
    return item;
  }

  public void setItem(long item) {
    this.item = item;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified Person.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    OrderItem otherOrderItem = (OrderItem) other;
    String where = "OrderItem<" + order.getOrderId() + "," + item + ">";
    return helper.equals(order, otherOrderItem.getOrder(), where + ".order")
        & helper.equals(item, otherOrderItem.getItem(), where + ".item");
  }

  /**
   * Compares this object with the specified Company object for order. Returns a negative integer,
   * zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The Company object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified Company object.
   */
  public int compareTo(OrderItem other) {
    return compare(this, other);
  }

  /**
   * Compares its two OrderItem arguments for order. Returns a negative integer, zero, or a positive
   * integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first OrderItem object to be compared.
   * @param o2 the second OrderItem object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public int compare(OrderItem o1, OrderItem o2) {
    int retval = o1.getOrder().compareTo(o2.getOrder());
    if (retval != 0) {
      return retval;
    }
    return EqualityHelper.compare(o1.getItem(), o2.getItem());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof OrderItem) {
      return compareTo((OrderItem) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) (order.orderId + item);
  }

  /**
   * The class to be used as the application identifier for the <code>OrderItem</code> class. It
   * consists of both the order and the item fields.
   */
  public static class OrderItemOid implements Serializable, Comparable<OrderItemOid> {

    private static final long serialVersionUID = 1L;

    public Order.OrderOid order; // matches order field name and OrderId type
    public long item; // matches item field name and type

    /** The required public no-arg constructor. */
    public OrderItemOid() {}

    public OrderItemOid(String s) {
      order = new Order.OrderOid();
      order.orderId = Long.parseLong(justTheOrder(s));
      item = Long.parseLong(justTheItem(s));
    }

    public String toString() {
      return this.getClass().getName() + "order:" + order.orderId + ",item:" + item;
    }

    /** */
    public boolean equals(Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return false;
      OrderItemOid o = (OrderItemOid) obj;
      if (!this.order.equals(o.order) || (this.item != o.item)) {
        return false;
      }
      return true;
    }

    /** */
    public int hashCode() {
      return (int) (item + order.orderId);
    }

    protected static String justTheOrder(String str) {
      return str.substring(str.indexOf("order:") + 6, str.indexOf(",item:"));
    }

    protected static String justTheItem(String str) {
      return str.substring(str.indexOf("item:") + 5);
    }

    /** */
    public int compareTo(OrderItemOid obj) {
      if (order.orderId < obj.order.orderId) return -1;
      if (order.orderId > obj.order.orderId) return 1;
      if (item < obj.item) return -1;
      if (item > obj.item) return 1;
      return 0;
    }
  }
}
