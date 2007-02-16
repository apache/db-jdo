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
import java.util.Set;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

public class Order implements Serializable, Comparable, Comparator, DeepEquality {
    long orderId;
    Set items;
    long customerId;

    public Order() {}

    public Order(long orderId, long customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public Order(long orderId, Set items, long customerId) {
        this.orderId = orderId;
        this.items = items;
        this.customerId = customerId;
    }

    public long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    
    public Set getItems() {
        return items;
    }
    
    public void setItems(Set items) {
        this.items = items;
    }

    public long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    
    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the specified Person.
     * @param other the object with which to compare.
     * @param helper EqualityHelper to keep track of instances that have
     * already been processed. 
     * @return <code>true</code> if all the fields are deep equal;
     * <code>false</code> otherwise.  
     * @throws ClassCastException if the specified instances' type prevents
     * it from being compared to this instance. 
     */
    public boolean deepCompareFields(Object other, 
                                     EqualityHelper helper) {
        Order otherOrder = (Order)other;
        String where = "Order<" + orderId + ">";
        return 
            helper.equals(orderId, otherOrder.getOrderId(), where + ".order") &
            helper.equals(items, otherOrder.getItems(), where + ".items") &
            helper.equals(customerId, otherOrder.getCustomerId(),
                    where + ".customerId");
    }
    
    /** 
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object. 
     * @param o The Object to be compared. 
     * @return a negative integer, zero, or a positive integer as this 
     * object is less than, equal to, or greater than the specified object. 
     * @throws ClassCastException - if the specified object's type prevents
     * it from being compared to this Object. 
     */
    public int compareTo(Object o) {
        return compareTo((Order)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((Order)o1, (Order)o2);
    }

    /** 
     * Compares this object with the specified Company object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The Company object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified Company
     * object. 
     */
    public int compareTo(Order other) {
        return compare(this, other);
    }

    /**
     * Compares its two Order arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first Order object to be compared. 
     * @param o2 the second Order object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(Order o1, Order o2) {
        return EqualityHelper.compare(o1.getOrderId(), o2.getOrderId());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof Order) {
            return compareTo((Order)obj) == 0;
        }
        return false;
    }

    /**
     * The class to be used as the application identifier
     * for the <code>Order</code> class. It consists of both the orderId 
     * field.
     */
    public static class OrderOid implements Serializable, Comparable {
        public long orderId;

        /** The required public no-arg constructor. */
        public OrderOid() { }

        public OrderOid(String s) {
            orderId = Long.parseLong(justTheOrder(s)); 
        }

        public String toString() {
            return this.getClass().getName() + "order:" 
                + String.valueOf(orderId);
        }
        
        /** */
        public boolean equals(Object obj) {
            if (obj == null || !this.getClass().equals(obj.getClass())) 
                return false;
            OrderOid o = (OrderOid) obj;
            if (this.orderId != o.orderId) {
                return false;
            }
            return true;
        }

        /** */
        public int hashCode() {
            return (int)orderId;
        }
        
        protected static String justTheOrder(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            OrderOid other = (OrderOid) obj;
            if( orderId < other.orderId ) return -1;
            if( orderId > other.orderId ) return 1;
            return 0;
        }

    }
}

