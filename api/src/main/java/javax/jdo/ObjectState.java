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
 * ObjectState.java
 *
 */
package javax.jdo;

/**
 * This class defines the object states for JDO instances.
 *
 * @version 2.1
 */
public enum ObjectState {
  TRANSIENT("transient"),
  TRANSIENT_CLEAN("transient-clean"),
  TRANSIENT_DIRTY("transient-dirty"),
  PERSISTENT_NEW("persistent-new"),
  HOLLOW_PERSISTENT_NONTRANSACTIONAL("hollow/persistent-nontransactional"),
  PERSISTENT_NONTRANSACTIONAL_DIRTY("persistent-nontransactional-dirty"),
  PERSISTENT_CLEAN("persistent-clean"),
  PERSISTENT_DIRTY("persistent-dirty"),
  PERSISTENT_DELETED("persistent-deleted"),
  PERSISTENT_NEW_DELETED("persistent-new-deleted"),
  DETACHED_CLEAN("detached-clean"),
  DETACHED_DIRTY("detached-dirty");

  private final String value;

  private ObjectState(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
