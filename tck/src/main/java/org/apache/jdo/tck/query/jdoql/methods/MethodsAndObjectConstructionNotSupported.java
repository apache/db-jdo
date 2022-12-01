/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql.methods;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Methods and Object Construction not Supported <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-8. <br>
 * <B>Assertion Description: </B> Methods, including object construction, are not supported in a
 * <code>Query</code> filter.
 */
public class MethodsAndObjectConstructionNotSupported extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-8 (MethodsAndObjectConstructionNotSupported) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MethodsAndObjectConstructionNotSupported.class);
  }

  /** */
  public void testNegative() {
    PersistenceManager pm = getPM();

    runTestUnsupportedOperators01(pm, Employee.class, "this.team.add(this)");
    runTestUnsupportedOperators01(pm, Employee.class, "this.team.remove(this)");
    runTestUnsupportedOperators01(pm, PCPoint.class, "y == Integer.valueOf(1)");
  }

  /** */
  <T> void runTestUnsupportedOperators01(
      PersistenceManager pm, Class<T> candidateClass, String filter) {
    String expectedMsg = "setFilter: Invalid method call ....";
    Query<T> query = pm.newQuery(candidateClass);

    try {
      query.setFilter(filter);
      query.compile();

      fail(ASSERTION_FAILED, "Missing JDOUserException(" + expectedMsg + ") for filter " + filter);
    } catch (JDOUserException ex) {
      if (debug) logger.debug("expected exception " + ex);
    }
  }
}
