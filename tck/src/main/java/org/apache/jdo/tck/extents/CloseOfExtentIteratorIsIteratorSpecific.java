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

import java.util.Collection;
import java.util.Iterator;
import javax.jdo.Extent;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.Employee;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Close of Extent Iterator is Iterator Specific <br>
 * <B>Keywords:</B> extent <br>
 * <B>Assertion ID:</B> A15.3-11. <br>
 * <B>Assertion Description: </B> After a call to <code>Extent.close(Iterator i)</code>, the <code>
 * Extent</code> itself can still be used to acquire other iterators and can be used as the <code>
 * Extent</code> for queries.
 */
public class CloseOfExtentIteratorIsIteratorSpecific extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A15.3-11 (CloseOfExtentIteratorIsIteratorSpecific) failed: ";

  /** */
  @Test
  public void test() {
    Extent<Employee> ex = getExtent();
    beginTransaction();
    Iterator<Employee> it1 = ex.iterator();
    ex.close(it1);
    int count = countIterator(ex.iterator());
    if (count != 2) {
      fail(
          ASSERTION_FAILED,
          "iterating Employees after close of first iterator; counted "
              + count
              + " instances; should be 2");
    }
    Query<?> q = getPM().newQuery(ex);
    Collection<?> c = (Collection<?>) q.execute();
    int count2 = countIterator(c.iterator());
    commitTransaction();
    if (count2 != 2) {
      fail(
          ASSERTION_FAILED,
          "in query after closing iterator; counted " + count2 + " instances; should be 2");
    }
    if (debug) logger.debug("Assertion A15.3-11 passed");
  }
}
