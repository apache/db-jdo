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

import javax.jdo.PersistenceManager;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * This class provides an implementation of OrderFactory that sets all
 * of the properties of the instance and defines abstract methods to
 * construct the instance itself. It is intended to be subclassed by
 * classes that implement only the methods to construct the instance.
 */
public abstract class OrderFactoryAbstractImpl implements OrderFactory {
    
    protected final PersistenceManager pm;
    
    /** Logger */
    protected final Log logger =
        LogFactory.getFactory().getInstance("org.apache.jdo.tck");

    /** true if debug logging is enabled. */
    protected final boolean debug = logger.isDebugEnabled();
    
    /**
     * Creates a new instance of OrderFactoryAbstractImpl
     * @param pm the PersistnceManager
     */
    public OrderFactoryAbstractImpl(PersistenceManager pm) {
        this.pm = pm;
    }

    abstract Order newOrder();
    abstract OrderItem newOrderItem();

    public Order newOrder(long orderId, long customerId) {
        Order result = newOrder();
        result.setOrderId(orderId);
        result.setCustomerId(customerId);
        if (debug) logger.debug("newOrder returned" + result);
        return result;
    }

    public Order newOrder(long orderId, Set<OrderItem> items, long customerId) {
        Order result = newOrder();
        result.setOrderId(orderId);
        result.setItems(items);
        result.setCustomerId(customerId);
        if (debug) logger.debug("newOrder returned" + result);
        return result;
    }

    public OrderItem newOrderItem(Order order, long item, String description,
            int quantity) {
        OrderItem result = newOrderItem();
        result.setOrder(order);
        result.setItem(item);
        result.setDescription(description);
        result.setQuantity(quantity);
        if (debug) logger.debug("newOrderItem returned" + result);
        return result;
    }
}
