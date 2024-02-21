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

package org.apache.jdo.tck.query.jdoql.subqueries;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.Employee;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Null Variable Declaration in addSubquery <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-53. <br>
 * <B>Assertion Description: </B> If the trimmed value is the empty String, or the parameter is
 * null, then JDOUserException is thrown.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NullVariableDeclaration extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-53 (NullVariableDeclaration) failed: ";

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestNullVariable() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      try {
        apiQuery.addSubquery(null, null, null);
        apiQuery.compile();
        fail(
            ASSERTION_FAILED,
            "addSubquery called with a null varible declaration must throw a JDOUserException.");
      } catch (JDOException ex) {
        // expected JDOException
      }
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestEmptyVariable() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      try {
        apiQuery.addSubquery(null, " ", null);
        apiQuery.compile();
        fail(
            ASSERTION_FAILED,
            "addSubquery called with an empty varible declaration must throw a JDOUserException.");
      } catch (JDOException ex) {
        // expected JDOException
      }
    } finally {
      cleanupPM(pm);
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
