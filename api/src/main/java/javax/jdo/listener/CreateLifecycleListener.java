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
 * CreateLifecycleListener.java
 *
 */

package javax.jdo.listener;

/**
 * This interface is implemented by listeners to be notified of create events.
 *
 * @version 2.0
 * @since 2.0
 */
public interface CreateLifecycleListener extends InstanceLifecycleListener {

  /**
   * Invoked whenever an instance is made persistent via a call to {@link
   * javax.jdo.PersistenceManager#makePersistent} or during persistence by reachability.
   *
   * @param event the create event.
   * @since 2.0
   */
  void postCreate(InstanceLifecycleEvent event);
}
