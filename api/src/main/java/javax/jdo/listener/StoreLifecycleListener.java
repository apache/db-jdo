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

/*
 * StoreLifecycleListener.java
 *
 */

package javax.jdo.listener;

/**
 * This interface is implemented by listeners to be notified of store events.
 *
 * @version 2.0
 * @since 2.0
 */
public interface StoreLifecycleListener extends InstanceLifecycleListener {

  /**
   * Invoked whenever a persistent instance is stored, for example during {@link
   * javax.jdo.PersistenceManager#flush} or {@link javax.jdo.Transaction#commit}. It is called
   * before the method {@link StoreCallback#jdoPreStore} is invoked.
   *
   * @param event the store event.
   * @since 2.0
   */
  void preStore(InstanceLifecycleEvent event);

  /**
   * Invoked whenever a persistent instance is stored, for example during {@link
   * javax.jdo.PersistenceManager#flush} or {@link javax.jdo.Transaction#commit}. It is called after
   * the field values have been stored.
   *
   * @param event the store event.
   * @since 2.0
   */
  void postStore(InstanceLifecycleEvent event);
}
