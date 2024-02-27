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

package org.apache.jdo.tck.extents;

import javax.jdo.Extent;
import org.apache.jdo.tck.pc.company.Employee;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Has Subclasses is True <br>
 * <B>Keywords:</B> extent inheritance <br>
 * <B>Assertion ID:</B> A15.3-6. <br>
 * <B>Assertion Description: </B> <code>Extent.hasSubclasses()</code> returns <code>true</code> if
 * the extent includes subclasses.
 */
public class HasSubclassesTrue extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A15.3-6 (HasSubclassesTrue) failed: ";

  /** */
  @Test
  public void test() {
    Extent<Employee> ex = getPM().getExtent(Employee.class, true);
    if (!ex.hasSubclasses()) {
      fail(
          ASSERTION_FAILED,
          "ex.hasSubclasses() returned false, but extent was created with subclasses=true.");
    }
  }
}
