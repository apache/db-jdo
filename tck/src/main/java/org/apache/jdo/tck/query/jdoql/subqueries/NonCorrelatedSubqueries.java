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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Non-correlated Subqueries <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-55. <br>
 * <B>Assertion Description: </B> If the subquery has no references to expressions in the outer
 * query the subquery is noncorrelated.
 */
public class NonCorrelatedSubqueries extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-55 (NonCorrelatedSubqueries) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NonCorrelatedSubqueries.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    runTestSubqueries01(pm);
    runTestSubqueries02(pm);
  }

  /** */
  @SuppressWarnings("unchecked")
  void runTestSubqueries01(PersistenceManager pm) {
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

    // API query
    Query<Employee> sub = pm.newQuery(Employee.class);
    sub.setResult("avg(this.weeklyhours)");
    Query<Employee> apiQuery = pm.newQuery(Employee.class);
    apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
    apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

    // API query against memory model
    List<Employee> allEmployees = getAllEmployees(pm);
    apiQuery.setCandidates(allEmployees);
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

    // single String JDOQL
    Query<Employee> singleStringQuery = pm.newQuery(singleStringJDOQL);
    executeJDOQuery(
        ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, false, null, expectedResult, true);
  }

  /** */
  @SuppressWarnings("unchecked")
  void runTestSubqueries02(PersistenceManager pm) {
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

    // API query
    Query<Employee> sub = pm.newQuery(Employee.class);
    sub.setResult("avg(this.weeklyhours)");
    Query<Employee> apiQuery = pm.newQuery(Employee.class);
    apiQuery.setFilter("this.hiredate > :hired && this.weeklyhours > averageWeeklyhours");
    apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
    executeJDOQuery(
        ASSERTION_FAILED,
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
        singleStringQuery,
        singleStringJDOQL,
        false,
        new Object[] {hired},
        expectedResult,
        true);
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
