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

import java.util.Iterator;
import javax.jdo.Extent;
import org.apache.jdo.tck.pc.company.Employee;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Instances Persisted Prior to Iteration Returned <br>
 * <B>Keywords:</B> extent <br>
 * <B>Assertion ID:</B> A15.3-2. <br>
 * <B>Assertion Description: </B> If instances were made persistent in the transaction prior to the
 * execution of <code>Extent.iterator()</code>, the returned <code>Iterator</code> will contain the
 * instances.
 */
public class InstancesPersistedPriorToIterationReturned extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A15.3-2 (InstancesPersistedPriorToIterationReturned) failed: ";

  /** */
  @Test
  public void test() {

    beginTransaction();
    getPM().setIgnoreCache(false);
    Extent<Employee> ex = getPM().getExtent(Employee.class, true);
    addEmployee();
    Iterator<Employee> it = ex.iterator();
    int count = countIterator(it);
    rollbackTransaction();

    beginTransaction();
    Iterator<Employee> it2 = ex.iterator();
    int count2 = countIterator(it2);
    commitTransaction();

    if (count != 3) {
      fail(ASSERTION_FAILED, "Iterator: " + count + " should be 3");
    }
    if (count2 != 2) {
      fail(ASSERTION_FAILED, "Iterator2: " + count2 + "should be 2");
    }
  }
}
