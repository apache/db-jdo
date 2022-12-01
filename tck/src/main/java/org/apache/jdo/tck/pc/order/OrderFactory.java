/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

/*
 * OrderFactory.java
 *
 */

package org.apache.jdo.tck.pc.order;

import java.util.Set;

/**
 * This interface is implemented by a factory class that can create Order model instances. The
 * factory instance is registered with OrderFactoryRegistry.
 */
public interface OrderFactory {
  Order newOrder(long orderId, Set<OrderItem> items, long customerId);

  Order newOrder(long orderId, long customerId);

  OrderItem newOrderItem(Order order, long item, String description, int quantity);

  Class<?>[] getTearDownClasses();
}
