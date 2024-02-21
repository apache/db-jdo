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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
 * <B>Title:</B> Non-correlated Subqueries <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-55. <br>
 * <B>Assertion Description: </B> If the subquery has no references to expressions in the outer
 * query the subquery is noncorrelated.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NonCorrelatedSubqueries extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-55 (NonCorrelatedSubqueries) failed: ";

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestSubqueries01() {
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

      // API query
      Query<Employee> sub = pm.newQuery(Employee.class);
      sub.setResult("avg(this.weeklyhours)");
      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
      apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
      executeJDOQuery(
          ASSERTION_FAILED, pm, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

      // API query against memory model
      List<Employee> allEmployees = getAllEmployees(pm);
      apiQuery.setCandidates(allEmployees);
      executeJDOQuery(
          ASSERTION_FAILED, pm, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

      // single String JDOQL
      Query<Employee> singleStringQuery = pm.newQuery(singleStringJDOQL);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          singleStringQuery,
          singleStringJDOQL,
          false,
          null,
          expectedResult,
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestSubqueries02() {
    List<IEmployee> expectedResult =
        getTransientCompanyModelInstancesAsList(IEmployee.class, "emp2", "emp5", "emp10");

    // Select employees hired after a particular date who work more
    // than the average of all employees
    String singleStringJDOQL =
        "SELECT FROM "
            + Employee.class.getName()
            + " WHERE this.hiredate > :hired && "
            + "this.weeklyhours> (SELECT AVG(e.weeklyhours) FROM "
            + Employee.class.getName()
            + " e)";

    Calendar cal = Calendar.getInstance(Locale.US);
    cal.set(2002, Calendar.SEPTEMBER, 1, 0, 0, 0);
    Date hired = cal.getTime();

    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      // API query
      Query<Employee> sub = pm.newQuery(Employee.class);
      sub.setResult("avg(this.weeklyhours)");
      Query<Employee> apiQuery = pm.newQuery(Employee.class);
      apiQuery.setFilter("this.hiredate > :hired && this.weeklyhours > averageWeeklyhours");
      apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          apiQuery,
          singleStringJDOQL,
          false,
          new Object[] {hired},
          expectedResult,
          true);

      // API query against memory model
      List<Employee> allEmployees = getAllEmployees(pm);
      apiQuery.setCandidates(allEmployees);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          apiQuery,
          singleStringJDOQL,
          false,
          new Object[] {hired},
          expectedResult,
          true);

      // single String JDOQL
      Query<Employee> singleStringQuery = pm.newQuery(singleStringJDOQL);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          singleStringQuery,
          singleStringJDOQL,
          false,
          new Object[] {hired},
          expectedResult,
          true);
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
