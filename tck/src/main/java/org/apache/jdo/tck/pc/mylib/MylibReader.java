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

/** Utility class to create a mylib instances from an xml representation. */
public class MylibReader extends DefaultListableInstanceFactory {

  private static final long serialVersionUID = 1L;

  /** Teardown classes */
  @SuppressWarnings("rawtypes")
  private static final Class<?>[] tearDownClasses =
      new Class[] {PrimitiveTypes.class, PCClass.class};

  /**
   * Create a MylibReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public MylibReader(String resourceName) {
    super();
    init();
  }

  private void init() {
    System.err.println("MyLibReader2.init()");
    PrimitiveTypes primitiveTypesPositive =
        new PrimitiveTypes(
            1L,
            false,
            null,
            (byte) 0,
            null,
            (short) 0,
            null,
            4,
            4,
            4,
            Long.valueOf(4),
            4.0f,
            4.0f,
            4.0,
            4.0,
            (char) 0,
            null,
            null,
            null,
            null,
            null,
            null);
    PrimitiveTypes primitiveTypesNegative =
        new PrimitiveTypes(
            2L, false, null, (byte) 0, null, (short) 0, null, -4, -4, -4, -4L, -4.0f, -4.0f, -4.0,
            -4.0, (char) 0, null, null, null, null, null, null);
    PCClass pcClass1 = new PCClass(1, 10, 10, 0, 0);
    PCClass pcClass2 = new PCClass(2, 20, 20, 0, 0);
    PrimitiveTypes primitiveTypesCharacterStringLiterals =
        new PrimitiveTypes(
            3, false, null, (byte) 0, null, (short) 0, null, 0, null, 0, null, 0, null, 0, null,
            (char) 0, (char) 0, null, "Even", null, null, null);

    register("primitiveTypesPositive", primitiveTypesPositive);
    register("primitiveTypesNegative", primitiveTypesNegative);
    register("pcClass1", pcClass1);
    register("pcClass2", pcClass2);
    register("primitiveTypesCharacterStringLiterals", primitiveTypesCharacterStringLiterals);
  }

  // Convenience methods

  /**
   * Convenience method returning an Address instance for the specified name. The method returns
   * <code>null</code> if there is no Address bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Address bean.
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
