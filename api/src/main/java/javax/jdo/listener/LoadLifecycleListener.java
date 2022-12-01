/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
 * LoadLifecycleListener.java
 *
 */

package javax.jdo.listener;

/**
 * This interface is implemented by listeners to be notified of load events.
 *
 * @version 2.0
 * @since 2.0
 */
public interface LoadLifecycleListener extends InstanceLifecycleListener {

  /**
   * Invoked whenever a persistent instance is loaded from the data store. It is called after the
   * method {@link LoadCallback#jdoPostLoad} is invoked on the persistent instance.
   *
   * @param event the load event.
   * @since 2.0
   */
  void postLoad(InstanceLifecycleEvent event);
}
