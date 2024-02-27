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

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Unmodified Subquery Instance. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-50. <br>
 * <B>Assertion Description: </B> The Query parameter instance is unmodified as a result of the
 * addSubquery or subsequent execution of the outer Query. Only some of the parameter query parts
 * are copied for use as the subquery. The parts copied include the candidate class, filter,
 * parameter declarations, variable declarations, imports, ordering specification, uniqueness,
 * result specification, and grouping specification. The association with a PersistenceManager, the
 * candidate collection or extent, result class, and range limits are not used.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnmodifiedSubqueryInstance extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-50 (UnmodifiedSubqueryInstance) failed: ";

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestUnmodifiedSubquery() {
    List<IEmployee> expectedResult =
        getTransientCompanyModelInstancesAsList(
            IEmployee.class, "emp1", "emp2", "emp4", "emp5", "emp6", "emp7", "emp10");
    Double averageWeeklyHours = Double.valueOf(33.5);

    // select average weeklyhours of all employees
    String singleStringJDOQLSubquery = "SELECT AVG(e.weeklyhours) FROM Employee e";
    // select employees who work more than the average of all employees
    String singleStringJDOQL =
        "SELECT FROM "
            + Employee.class.getName()
            + " WHERE this.weeklyhours > "
            + "("
            + singleStringJDOQLSubquery
            + ")";

    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      // execute subquery
      Query<Employee> sub = pm.newQuery(Employee.class);
      sub.setResult("avg(this.weeklyhours)");
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          sub,
          singleStringJDOQLSubquery,
          false,
          null,
          averageWeeklyHours,
          true);

      // execute API query
      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
      apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
      executeJDOQuery(
          ASSERTION_FAILED, pm, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

      // execute subquery again
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          sub,
          singleStringJDOQLSubquery,
          false,
          null,
          averageWeeklyHours,
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDifferentPM() {
    List<IEmployee> expectedResult =
        getTransientCompanyModelInstancesAsList(
            IEmployee.class, "emp1", "emp2", "emp4", "emp5", "emp6", "emp7", "emp10");

    // select employees who work more than the average of all employees
    String singleStringJDOQL =
        "SELECT FROM "
            + Employee.class.getName()
            + " WHERE this.weeklyhours > "
            + "(SELECT AVG(e.weeklyhours) FROM "
            + Employee.class.getName()
            + " e)";
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      // create subquery instance using different pm
      PersistenceManager newPM = pm.getPersistenceManagerFactory().getPersistenceManager();
      Query<Employee> sub = newPM.newQuery(Employee.class);
      sub.setResult("avg(this.weeklyhours)");

      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
      apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
      executeJDOQuery(
          ASSERTION_FAILED, pm, apiQuery, singleStringJDOQL, false, null, expectedResult, true);
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

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }
}
