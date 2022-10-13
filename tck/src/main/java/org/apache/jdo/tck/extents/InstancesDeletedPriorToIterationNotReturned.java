/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Instances Deleted Prior to Iteration not Returned <br>
 * <B>Keywords:</B> extent <br>
 * <B>Assertion ID:</B> A15.3-3. <br>
 * <B>Assertion Description: </B> If instances were deleted in the transaction prior to the
 * execution of <code>Extent.iterator()</code>, the returned <code>Iterator</code> will not contain
 * the instances.
 */
public class InstancesDeletedPriorToIterationNotReturned extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A15.3-3 (InstancesDeletedPriorToIterationNotReturned) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(InstancesDeletedPriorToIterationNotReturned.class);
  }

  /** */
  public void test() {
    beginTransaction();
    getPM().setIgnoreCache(false);
    Extent<Employee> ex = getPM().getExtent(Employee.class, true);
    Iterator<Employee> it1 = ex.iterator();
    deleteEmployee(it1.next());
    Iterator<Employee> it2 = ex.iterator();
    int count = countIterator(it2);
    rollbackTransaction();

    beginTransaction();
    Iterator<Employee> it3 = ex.iterator();
    int count3 = countIterator(it3);
    commitTransaction();

    if (count != 1) {
      fail(ASSERTION_FAILED, "counted " + count + " after delete; should be 1");
    }
    if (count3 != 2) {
      fail(ASSERTION_FAILED, "counted " + count3 + "after rollback; should be 2");
    }
  }
}
