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

import org.apache.jdo.tck.util.DataSource;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

/** Utility class to create a mylib instances for unit tests. */
public class MylibReaderTestData implements DataSource<DefaultListableInstanceFactory> {

  @Override
  public void init(DefaultListableInstanceFactory factory, DefaultListableInstanceFactory registry) {
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

    registry.register("primitiveTypesPositive", primitiveTypesPositive);
    registry.register("primitiveTypesNegative", primitiveTypesNegative);
    registry.register("pcClass1", pcClass1);
    registry.register("pcClass2", pcClass2);
    registry.register("primitiveTypesCharacterStringLiterals", primitiveTypesCharacterStringLiterals);
  }
}
