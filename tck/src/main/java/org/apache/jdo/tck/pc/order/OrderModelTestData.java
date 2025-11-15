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

import static org.apache.jdo.tck.util.DataSourceUtil.toSet;

import org.apache.jdo.tck.util.DataSource;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

/** Utility class to create a graph of order model instances. */
public class OrderModelTestData implements DataSource<OrderFactory> {

  @Override
  public void init(OrderFactory factory, DefaultListableInstanceFactory registry) {
    Order order1 = factory.newOrder(1, 3);
    OrderItem item1 = factory.newOrderItem(order1, 1, "SunRay", 15);
    OrderItem item2 = factory.newOrderItem(order1, 1, "Sun Ultra 40", 3);
    order1.setItems(toSet(item1, item2));
    registry.register("order1", order1);
  }
}
