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

package org.apache.jdo.tck.api.persistencemanagerfactory.config;

import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.StoreLifecycleListener;

/**
 * A StoreLivecycleListener implementation that provides a public no-args constructor that may be
 * invoked by a JDO implementation, but NO getInstance() method.
 */
public class StoreListener implements StoreLifecycleListener {

  private static boolean preStore = false;
  private static boolean postStore = false;
  private static int instanceCount = 0;

  public StoreListener() {
    instanceCount++;
  }

  public static boolean isPreStore() {
    return preStore;
  }

  public static boolean isPostStore() {
    return postStore;
  }

  public static void resetValues() {
    preStore = false;
    postStore = false;
  }

  public void preStore(InstanceLifecycleEvent event) {
    //        System.out.println("preStore: event is " + event);
    preStore = true;
  }

  public void postStore(InstanceLifecycleEvent event) {
    //        System.out.println("postStore: event is " + event);
    postStore = true;
  }
}
