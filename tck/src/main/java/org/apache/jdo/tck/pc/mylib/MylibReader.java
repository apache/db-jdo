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

package org.apache.jdo.tck.pc.mylib;

import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

/** Utility class to create mylib test data instances. */
public class MylibReader extends DefaultListableInstanceFactory {

  /** Teardown classes */
  private static final Class<?>[] tearDownClasses =
      new Class[] {PrimitiveTypes.class, PCClass.class};

  /**
   * Create a MylibReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public MylibReader(String resourceName) {
    super();
    new MylibTestData().init(this, this);
  }

  // Convenience methods

  /**
   * Convenience method returning an Address instance for the specified name. The method returns
   * <code>null</code> if there is no Address bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there is no Address bean.
   */
  public PrimitiveTypes getPrimitiveTypes(String name) {
    return getBean(name, PrimitiveTypes.class);
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public static Class<?>[] getTearDownClasses() {
    return tearDownClasses;
  }
}
