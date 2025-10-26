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

package org.apache.jdo.tck.util;

/**
 * Common interface for classes that generate data for tests.
 *
 * @param <F> Factory class.
 */
public interface DataSource<F> {
  /**
   * Implementations should generate test data objects and register them with the registry.
   *
   * <p>The "init()" Methods are usually structured as follows:<br>
   * - Creation of all instances, using the factory if one is available.<br>
   * - Setting of all properties of all instances.<br>
   * - Registration of instance with names (root names and bean names) insofar as it is required by
   * the tests.<br>
   *
   * <p>The test class name is usually given as argument to the constructor of the factory class.
   * Often the name is read from the "org.tck.testdata" property in the config files.
   *
   * @param factory Factory instance. May be "null" if no factory is used.
   * @param registry Registry for named objects (formerly "roots" and "beans").
   */
  void init(F factory, DefaultListableInstanceFactory registry);
}
