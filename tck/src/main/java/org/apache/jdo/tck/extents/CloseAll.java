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
 * <B>Title:</B> CloseAll <br>
 * <B>Keywords:</B> extent <br>
 * <B>Assertion ID:</B> A15.3-12. <br>
 * <B>Assertion Description: </B> After a call to <code>Extent.closeAll()</code>, all iterators
 * acquired from this <code>Extent</code> will return <code>false</code> to <code>hasNext()</code>.
 */
public class CloseAll extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A15.3-12 (CloseAll) failed: ";

  /** */
  @Test
  public void test() {
    try {
      beginTransaction();
      Extent<Employee> ex = getPM().getExtent(Employee.class, true);
      Iterator<Employee> it1 = ex.iterator();
      deleteEmployee(it1.next());
      Iterator<Employee> it2 = ex.iterator();
      addEmployee();
      Iterator<Employee> it3 = ex.iterator();
      ex.closeAll();
      if (it1.hasNext()) {
        fail(ASSERTION_FAILED, "After extent.closeAll() iterator1.hasNext(): " + it1.hasNext());
      }
      if (it2.hasNext()) {
        fail(ASSERTION_FAILED, "After extent.closeAll() iterator2.hasNext(): " + it2.hasNext());
      }
      if (it3.hasNext()) {
        fail(ASSERTION_FAILED, "After extent.closeAll() iterator3.hasNext(): " + it3.hasNext());
      }
      if (debug) logger.debug("Assertion A15.3-12 passed");
    } finally {
      rollbackTransaction();
    }
  }
}
