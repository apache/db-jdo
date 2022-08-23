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

import javax.jdo.Extent;

import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get PersistenceManager <br>
 * <B>Keywords:</B> extent <br>
 * <B>Assertion ID:</B> A15.3-8. <br>
 * <B>Assertion Description: </B> <code>Extent.getPersistenceManager()</code> returns the <code>
 * PersistenceManager</code> which created this <code>Extent</code>.
 */
public class GetPersistenceManager extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A15.3-8 (GetPersistenceManager) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPersistenceManager.class);
  }

  /** */
  public void test() {
    Extent ex = getPM().getExtent(Employee.class, true);
    if (pm != ex.getPersistenceManager()) {
      fail(
          ASSERTION_FAILED,
          "unexpected pm of extent, expected " + pm + ", got " + ex.getPersistenceManager());
    }
  }
}
