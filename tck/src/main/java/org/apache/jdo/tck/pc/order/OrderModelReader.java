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

package org.apache.jdo.tck.pc.order;

import java.util.Date;
import java.util.Locale;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/** Utility class to create a graph of order model instances from an xml representation. */
public class OrderModelReader extends DefaultListableInstanceFactory {

  private static final long serialVersionUID = 1L;

  /** The order factory instance. */
  private OrderFactory orderFactory;

  /**
   * Create a OrderModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public OrderModelReader(String resourceName) {
    super();
    configureFactory();
    init();
  }

  private void init() {
    Order order1 = orderFactory.newOrder(1, 3);
    OrderItem item1 = orderFactory.newOrderItem(order1, 1, "SunRay", 15);
    OrderItem item2 = orderFactory.newOrderItem(order1, 1, "Sun Ultra 40", 3);
    order1.addItem(item1);
    order1.addItem(item2);
    register("order1", order1);
  }

  /**
   * Configure the OrderModelReader, e.g. register CustomEditor classes to convert the string
   * representation of a property into an instance of the right type.
   */
  private void configureFactory() {
    // registerCustomEditor(Date.class, JDOCustomDateEditor.class);
    orderFactory = OrderFactoryRegistry.getInstance();
    // addSingleton(BEAN_FACTORY_NAME, orderFactory);
  }

  // Convenience methods

  /**
   * Convenience method returning a Order instance for the specified name. The method returns <code>
   * null</code> if there is no Order bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Order bean.
   */
  public Order getOrder(String name) {
    return getBean(name, Order.class);
  }

  /**
   * Convenience method returning a OrderItem instance for the specified name. The method returns
   * <code>null</code> if there is no Department bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Department bean.
   */
  public OrderItem getOrderItem(String name) {
    return getBean(name, OrderItem.class);
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public Class<?>[] getTearDownClassesFromFactory() {
    return orderFactory.getTearDownClasses();
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public static Class<?>[] getTearDownClasses() {
    return OrderFactoryConcreteClass.tearDownClasses;
  }

  public static Date stringToUtilDate(String value) {
    return ConversionHelper.toUtilDate(JDOCustomDateEditor.DATE_PATTERN, Locale.US, value);
  }
}
