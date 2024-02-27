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

package org.apache.jdo.tck.query.jdoql.methods;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Methods and Object Construction not Supported <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-8. <br>
 * <B>Assertion Description: </B> Methods, including object construction, are not supported in a
 * <code>Query</code> filter.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MethodsAndObjectConstructionNotSupported extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-8 (MethodsAndObjectConstructionNotSupported) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative1() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      runTestUnsupportedOperators01(pm, Employee.class, "this.team.add(this)");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative2() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      runTestUnsupportedOperators01(pm, Employee.class, "this.team.remove(this)");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative3() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      runTestUnsupportedOperators01(pm, PCPoint.class, "y == Integer.valueOf(1)");
    } finally {
      cleanupPM(pm);
    }
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

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
  }
}
