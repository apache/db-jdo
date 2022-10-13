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

package javax.jdo.spi;

/**
 * This interface is implemented by classes that can be detached from the persistence context and
 * later attached. The interface includes the contract by which the StateManager can set the object
 * id, version, BitSet of loaded fields, and BitSet of modified fields so they are preserved while
 * outside the persistence environment.
 *
 * <p>The detached state is stored as a field in each instance of Detachable. The field is
 * serialized so as to maintain the state of the instance while detached. While detached, only the
 * BitSet of modified fields will be modified. The structure of the Object[] jdoDetachedState is as
 * follows:
 *
 * <ul>
 *   <li>jdoDetachedState[0]: the Object Id of the instance
 *   <li>jdoDetachedState[1]: the Version of the instance
 *   <li>jdoDetachedState[2]: a BitSet of loaded fields
 *   <li>jdoDetachedState[3]: a BitSet of modified fields
 * </ul>
 *
 * @version 2.0
 */
public interface Detachable {

  /**
   * This method calls the StateManager with the current detached state instance as a parameter and
   * replaces the current detached state instance with the value provided by the StateManager.
   *
   * @since 2.0
   */
  public void jdoReplaceDetachedState();
}
