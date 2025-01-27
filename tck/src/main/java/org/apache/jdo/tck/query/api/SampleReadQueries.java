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
package org.apache.jdo.tck.query.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.JDOQLTypedSubquery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.query.CollectionExpression;
import javax.jdo.query.NumericExpression;
import javax.jdo.query.StringExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.EqualityHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> SampleReadQueries <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> This test class runs the example read queries from the JDO
 * specification.
 *
 * <p>There are up to six test methods per test case:
 *
 * <ul>
 *   <li>testQueryxxa: runtime constructed JDO query using execute to run the query
 *   <li>testQueryxxb: runtime constructed JDO query using setNamedParameters to specify the
 *       parameter values and executeList/executeResultList/executeResultUnique to run the query
 *   <li>testQueryxxc: runtime constructed JDO query using setParameters to specify the parameter
 *       values and executeList/executeResultList/executeResultUnique to run the query
 *   <li>testQueryxxd: single string version of the JDO query
 *   <li>testQueryxxe: named query version of the JDO query
 *   <li>testQueryxxf: JDOQLTypedQuery version
 * </ul>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SampleReadQueries extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (SampleReadQueries) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";

  private static final String SINGLE_STRING_QUERY_01 =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee where salary > 30000";

  private static final String SINGLE_STRING_QUERY_02 =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where salary > 30000 order by salary ascending";

  private static final String SINGLE_STRING_QUERY_03 =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where salary > :sal && firstname.startsWith(:begin)";

  private static final String SINGLE_STRING_QUERY_04 =
      "select from org.apache.jdo.tck.pc.company.Employee where department.name == :dep";

  private static final String SINGLE_STRING_QUERY_05 =
      "select from org.apache.jdo.tck.pc.company.Department "
          + "where employees.contains(emp) && emp.weeklyhours > :hours variables Employee emp";

  private static final String SINGLE_STRING_QUERY_06 =
      "select from org.apache.jdo.tck.pc.company.Department where :depts.contains(name)";

  private static final String SINGLE_STRING_QUERY_07 =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where languages.contains(:lang)";

  private static final String SINGLE_STRING_QUERY_08 =
      "select firstname from org.apache.jdo.tck.pc.company.Employee where department.name == :deptName";

  private static final String SINGLE_STRING_QUERY_09 =
      "select firstname, salary, manager as reportsTo into org.apache.jdo.tck.query.api.SampleReadQueries$Info "
          + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where department.name == :deptName";

  private static final String SINGLE_STRING_QUERY_10 =
      "select new org.apache.jdo.tck.query.api.SampleReadQueries$Info (firstname, salary, manager) "
          + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where department.name == :deptName";

  private static final String SINGLE_STRING_QUERY_11 =
      "select avg(salary) from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where department.name == :deptName";

  private static final String SINGLE_STRING_QUERY_12 =
      "select avg(salary), sum(salary) from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where department.name == :deptName";

  private static final String SINGLE_STRING_QUERY_13 =
      "select avg(salary), sum(salary), department.name "
          + "from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "group by department.name having count(department.name) > 1";

  private static final String SINGLE_STRING_QUERY_14 =
      "select unique this from org.apache.jdo.tck.pc.company.Employee where firstname == :empName";

  private static final String SINGLE_STRING_QUERY_15 =
      "select unique salary from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where firstname == :empName";

  private static final String SINGLE_STRING_QUERY_16 =
      "select into org.apache.jdo.tck.query.api.SampleReadQueries$EmpWrapper "
          + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where salary > :sal";

  private static final String SINGLE_STRING_QUERY_17 =
      "select into org.apache.jdo.tck.query.api.SampleReadQueries$EmpInfo "
          + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where salary > :sal";

  private static final String SINGLE_STRING_QUERY_18 =
      "select e.firstname from org.apache.jdo.tck.pc.company.Department "
          + "where name.startsWith('R&D') && employees.contains(e) "
          + "variables org.apache.jdo.tck.pc.company.Employee e";

  private static final String SINGLE_STRING_QUERY_19 =
      "select firstname from org.apache.jdo.tck.pc.company.Employee "
          + "where this.weeklyhours > (select avg(e.weeklyhours) from org.apache.jdo.tck.pc.company.Employee e)";

  private static final String SINGLE_STRING_QUERY_20 =
      "select firstname from org.apache.jdo.tck.pc.company.Employee "
          + "where this.weeklyhours > "
          + " (select AVG(e.weeklyhours) from this.department.employees e where e.manager == this.manager)";

  /**
   * Basic query.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000. Note that the float value for salary is unwrapped for the
   * comparison with the literal int value, which is promoted to float using numeric promotion. If
   * the value for the salary field in a candidate instance isnull, then it cannot be unwrapped for
   * the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery01a() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery01a " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000")) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_01, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000. Note that the float value for salary is unwrapped for the
   * comparison with the literal int value, which is promoted to float using numeric promotion. If
   * the value for the salary field in a candidate instance isnull, then it cannot be unwrapped for
   * the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery01b() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery01b " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000")) {
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_01, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000. Note that the float value for salary is unwrapped for the
   * comparison with the literal int value, which is promoted to float using numeric promotion. If
   * the value for the salary field in a candidate instance isnull, then it cannot be unwrapped for
   * the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery01d() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery01d " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_01)) {
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_01, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000. Note that the float value for salary is unwrapped for the
   * comparison with the literal int value, which is promoted to float using numeric promotion. If
   * the value for the salary field in a candidate instance isnull, then it cannot be unwrapped for
   * the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery01f() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery01f " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        q.filter(cand.salary.gt(30000.));
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_01, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query with ordering.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000, and returns a Collection ordered based on employee salary.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery02a() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery02a " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5", "emp2");
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000")) {
        q.setOrdering("salary ascending");
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute();
        checkQueryResultWithOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_02, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query with ordering.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000, and returns a Collection ordered based on employee salary.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery02b() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery02b " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5", "emp2");
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000")) {
        q.setOrdering("salary ascending");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_02, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query with ordering.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000, and returns a Collection ordered based on employee salary.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery02d() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery02d " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5", "emp2");
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_02)) {
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_02, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Basic query with ordering.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the constant 30000, and returns a Collection ordered based on employee salary.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery02f() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery02f " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5", "emp2");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        q.filter(cand.salary.gt(30000.)).orderBy(cand.salary.asc());
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_02, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Parameter passing.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the value passed as a parameter and the name starts with the value passed as a
   * second parameter. If the value for the salary field in a candidate instance is null, then it
   * cannot be unwrapped for the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery03a() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery03a " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "salary > sal && firstname.startsWith(begin)")) {
        q.declareParameters("Double sal, String begin");
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute(30000., "M");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_03, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Parameter passing.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the value passed as a parameter and the name starts with the value passed as a
   * second parameter. If the value for the salary field in a candidate instance is null, then it
   * cannot be unwrapped for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery03b() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery03b " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "salary > sal && firstname.startsWith(begin)")) {
        q.declareParameters("Double sal, String begin");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        paramValues.put("begin", "M");
        q.setNamedParameters(paramValues);
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_03, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Parameter passing.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the value passed as a parameter and the name starts with the value passed as a
   * second parameter. If the value for the salary field in a candidate instance is null, then it
   * cannot be unwrapped for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery03c() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery03c " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "salary > sal && firstname.startsWith(begin)")) {
        q.declareParameters("Double sal, String begin");
        q.setParameters(30000., "M");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_03, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Parameter passing.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the value passed as a parameter and the name starts with the value passed as a
   * second parameter. If the value for the salary field in a candidate instance is null, then it
   * cannot be unwrapped for the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery03d() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery03d " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_03)) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute(30000., "M");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_03, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Parameter passing.
   *
   * <p>This query selects all Employee instances from the candidate collection where the salary is
   * greater than the value passed as a parameter and the name starts with the value passed as a
   * second parameter. If the value for the salary field in a candidate instance is null, then it
   * cannot be unwrapped for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery03f() {
    if (logger.isDebugEnabled()) {
      logger.debug("testQuery03f " + Thread.currentThread().getName());
    }
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        StringExpression begin = q.stringParameter("begin");
        q.filter(cand.salary.gt(sal).and(cand.firstname.startsWith(begin)));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        paramValues.put("begin", "M");
        q.setParameters(paramValues);
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_03, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through single-valued field.
   *
   * <p>This query selects all Employee instances from the candidate collection where the value of
   * the name field in the Department instance associated with the Employee instance is equal to the
   * value passed as a parameter. If the value for the dept field in a candidate instance is null,
   * then it cannot be navigated for the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery04a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == dep")) {
        q.declareParameters("String dep");
        List<Employee> emps = (List<Employee>) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_04, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through single-valued field.
   *
   * <p>This query selects all Employee instances from the candidate collection where the value of
   * the name field in the Department instance associated with the Employee instance is equal to the
   * value passed as a parameter. If the value for the dept field in a candidate instance is null,
   * then it cannot be navigated for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery04b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == dep")) {
        q.declareParameters("String dep");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("dep", "R&D");
        q.setNamedParameters(paramValues);
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_04, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through single-valued field.
   *
   * <p>This query selects all Employee instances from the candidate collection where the value of
   * the name field in the Department instance associated with the Employee instance is equal to the
   * value passed as a parameter. If the value for the dept field in a candidate instance is null,
   * then it cannot be navigated for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery04c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == dep")) {
        q.declareParameters("String dep");
        q.setParameters("R&D");
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_04, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through single-valued field.
   *
   * <p>This query selects all Employee instances from the candidate collection where the value of
   * the name field in the Department instance associated with the Employee instance is equal to the
   * value passed as a parameter. If the value for the dept field in a candidate instance is null,
   * then it cannot be navigated for the comparison, and the candidate instance is rejected.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery04d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
      try (Query<Employee> q = pm.newQuery(SINGLE_STRING_QUERY_04)) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("dep", "R&D");
        q.setNamedParameters(paramValues);
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_04, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through single-valued field.
   *
   * <p>This query selects all Employee instances from the candidate collection where the value of
   * the name field in the Department instance associated with the Employee instance is equal to the
   * value passed as a parameter. If the value for the dept field in a candidate instance is null,
   * then it cannot be navigated for the comparison, and the candidate instance is rejected.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery04f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate("this");
        StringExpression dep = q.stringParameter("dep");
        q.filter(cand.department.name.eq(dep));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("dep", "R&D");
        q.setParameters(paramValues);
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_04, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all Department instances from the candidate collection where the
   * collection of Employee instances contains at least one Employee instance having a salary
   * greater than the value passed as a parameter.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery05a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1");
      try (Query<Department> q =
          pm.newQuery(Department.class, "employees.contains (emp) && emp.weeklyhours > hours")) {
        q.declareVariables("Employee emp");
        q.declareParameters("double hours");
        List<Department> deps = (List<Department>) q.execute(30.);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_05, deps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all Department instances from the candidate collection where the
   * collection of Employee instances contains at least one Employee instance having a salary
   * greater than the value passed as a parameter.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery05b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1");
      try (Query<Department> q =
          pm.newQuery(Department.class, "employees.contains (emp) && emp.weeklyhours > hours")) {
        q.declareVariables("Employee emp");
        q.declareParameters("double hours");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("hours", 30.);
        q.setNamedParameters(paramValues);
        List<Department> deps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_05, deps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all Department instances from the candidate collection where the
   * collection of Employee instances contains at least one Employee instance having a salary
   * greater than the value passed as a parameter.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery05c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1");
      try (Query<Department> q =
          pm.newQuery(Department.class, "employees.contains (emp) && emp.weeklyhours > hours")) {
        q.declareVariables("Employee emp");
        q.declareParameters("double hours");
        q.setParameters(30.);
        List<Department> deps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_05, deps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all Department instances from the candidate collection where the
   * collection of Employee instances contains at least one Employee instance having a salary
   * greater than the value passed as a parameter.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery05d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1");
      try (Query<Department> q = pm.newQuery(SINGLE_STRING_QUERY_05)) {
        List<Department> deps = (List<Department>) q.execute(30.);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_05, deps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all Department instances from the candidate collection where the
   * collection of Employee instances contains at least one Employee instance having a salary
   * greater than the value passed as a parameter.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery05f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1");
      try (JDOQLTypedQuery<Department> q = pm.newJDOQLTypedQuery(Department.class)) {
        QDepartment cand = QDepartment.candidate("this");
        QEmployee emp = QEmployee.variable("emp");
        NumericExpression<Double> hours = q.numericParameter("hours", double.class);
        q.filter(cand.employees.contains(emp).and(emp.weeklyhours.gt(hours)));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("hours", 30.);
        q.setParameters(paramValues);
        List<Department> deps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_05, deps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Membership in a collection.
   *
   * <p>This query selects all Department instances where the name field is contained in a parameter
   * collection, which in this example consists of three department names.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery06a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2", "dept3");
      try (Query<Department> q = pm.newQuery(Department.class, "depts.contains(name)")) {
        q.declareParameters("java.util.Collection depts");
        List<String> deptNames = Arrays.asList("R&D", "Sales", "Marketing");
        List<Department> result = (List<Department>) q.execute(deptNames);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_06, result, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Membership in a collection.
   *
   * <p>This query selects all Department instances where the name field is contained in a parameter
   * collection, which in this example consists of three department names.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery06b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2", "dept3");
      try (Query<Department> q = pm.newQuery(Department.class, "depts.contains(name)")) {
        q.declareParameters("java.util.Collection depts");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("depts", Arrays.asList("R&D", "Sales", "Marketing"));
        q.setNamedParameters(paramValues);
        List<Department> result = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_06, result, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Membership in a collection.
   *
   * <p>This query selects all Department instances where the name field is contained in a parameter
   * collection, which in this example consists of three department names.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery06c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2", "dept3");
      try (Query<Department> q = pm.newQuery(Department.class, "depts.contains(name)")) {
        q.declareParameters("java.util.Collection depts");
        q.setParameters(Arrays.asList("R&D", "Sales", "Marketing"));
        List<Department> result = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_06, result, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Membership in a collection.
   *
   * <p>This query selects all Department instances where the name field is contained in a parameter
   * collection, which in this example consists of three department names.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery06d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2", "dept3");
      try (Query<Department> q = pm.newQuery(SINGLE_STRING_QUERY_06)) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("depts", Arrays.asList("R&D", "Sales", "Marketing"));
        q.setNamedParameters(paramValues);
        List<Department> result = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_06, result, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Membership in a collection.
   *
   * <p>This query selects all Department instances where the name field is contained in a parameter
   * collection, which in this example consists of three department names.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery06f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Department> expected =
          getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2", "dept3");
      try (JDOQLTypedQuery<Department> q = pm.newJDOQLTypedQuery(Department.class)) {
        QDepartment cand = QDepartment.candidate("this");
        CollectionExpression<Collection<String>, String> depts =
            q.collectionParameter("depts", String.class);
        q.filter(depts.contains(cand.name));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("depts", Arrays.asList("R&D", "Sales", "Marketing"));
        q.setParameters(paramValues);
        List<Department> result = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_06, result, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery07a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "languages.contains(:lang)")) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute("German");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_07, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery07b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "languages.contains(:lang)")) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("lang", "German");
        q.setNamedParameters(paramValues);
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_07, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery07c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "languages.contains(:lang)")) {
        q.setParameters("German");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_07, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery07d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_07)) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute("German");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_07, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery07f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression lang = q.stringParameter("lang");
        q.filter(cand.languages.contains(lang));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("lang", "German");
        q.setParameters(paramValues);
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_07, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of a Single Field.
   *
   * <p>This query selects names of all Employees who work in the parameter department.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery08a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName")) {
        q.setResult("firstname");
        q.declareParameters("String deptName");
        List<String> names = (List<String>) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_08, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of a Single Field.
   *
   * <p>This query selects names of all Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery08b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName")) {
        q.setResult("firstname");
        q.declareParameters("String deptName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_08, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of a Single Field.
   *
   * <p>This query selects names of all Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery08c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
      try (Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName")) {
        q.setResult("firstname");
        q.declareParameters("String deptName");
        q.setParameters("R&D");
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_08, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of a Single Field.
   *
   * <p>This query selects names of all Employees who work in the parameter department.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery08d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
      try (Query<Employee> q = pm.newQuery(SINGLE_STRING_QUERY_08)) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_08, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of a Single Field.
   *
   * <p>This query selects names of all Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery08f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate("this");
        StringExpression deptName = q.stringParameter("deptName");
        q.filter(cand.department.name.eq(deptName)).result(false, cand.firstname);
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setParameters(paramValues);
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_08, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery09a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<SampleReadQueries.Info> expected = testQuery09Helper();
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("firstname, salary, manager as reportsTo");
        q.setResultClass(Info.class);
        q.declareParameters("String deptName");
        List<Info> infos = (List<Info>) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_09, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery09b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected = testQuery09Helper();
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("firstname, salary, manager as reportsTo");
        q.setResultClass(Info.class);
        q.declareParameters("String deptName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_09, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery09c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected = testQuery09Helper();
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("firstname, salary, manager as reportsTo");
        q.setResultClass(Info.class);
        q.declareParameters("String deptName");
        q.setParameters("R&D");
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_09, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery09d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected = testQuery09Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_09)) {
        q.setParameters("R&D");
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_09, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery09f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected = testQuery09Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression deptName = q.stringParameter("deptName");
        q.result(false, cand.firstname, cand.salary, cand.manager.as("reportsTo"))
            .filter(cand.department.name.eq(deptName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setParameters(paramValues);
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_09, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult(
            "new org.apache.jdo.tck.query.api.SampleReadQueries$Info(firstname, salary, manager)");
        q.declareParameters("String deptName");
        List<Info> infos = (List<Info>) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult(
            "new org.apache.jdo.tck.query.api.SampleReadQueries$Info(firstname, salary, manager)");
        q.declareParameters("String deptName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult(
            "new org.apache.jdo.tck.query.api.SampleReadQueries$Info(firstname, salary, manager)");
        q.declareParameters("String deptName");
        q.setParameters("R&D");
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_10)) {
        q.setParameters("R&D");
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10e() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (Query<FullTimeEmployee> q = pm.newNamedQuery(FullTimeEmployee.class, "constructor")) {
        q.setParameters("R&D");
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of Multiple Fields and Expressions into a Constructed instance.
   *
   * <p>This query selects names, salaries, and bosses of Employees who work in the parameter
   * department, and uses the constructor for the result class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery10f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Info> expected =
          Arrays.asList(
              new Info("Michael", 40000., getTransientCompanyModelInstance(Employee.class, "emp2")),
              new Info("Craig", 50000., null));
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression deptName = q.stringParameter("deptName");
        q.result(false, cand.firstname, cand.salary, cand.manager)
            .filter(cand.department.name.eq(deptName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setParameters(paramValues);
        List<Info> infos = q.executeResultList(Info.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_10, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of a single Field.
   *
   * <p>This query averages the salaries of Employees who work in the parameter department and
   * returns a single value.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery11a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expected = 45000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary)");
        q.declareParameters("String deptName");
        Double avgSalary = (Double) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_11, avgSalary, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of a single Field.
   *
   * <p>This query averages the salaries of Employees who work in the parameter department and
   * returns a single value.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery11b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expected = 45000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary)");
        q.declareParameters("String deptName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        Double avgSalary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_11, avgSalary, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of a single Field.
   *
   * <p>This query averages the salaries of Employees who work in the parameter department and
   * returns a single value.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery11c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expected = 45000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary)");
        q.declareParameters("String deptName");
        q.setParameters("R&D");
        Double avgSalary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_11, avgSalary, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of a single Field.
   *
   * <p>This query averages the salaries of Employees who work in the parameter department and
   * returns a single value.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery11d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expected = 45000.;
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_11)) {
        q.setParameters("R&D");
        Double avgSalary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_11, avgSalary, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of a single Field.
   *
   * <p>This query averages the salaries of Employees who work in the parameter department and
   * returns a single value.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery11f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expected = 45000.;
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression deptName = q.stringParameter("deptName");
        q.result(false, cand.salary.avg()).filter(cand.department.name.eq(deptName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setParameters(paramValues);
        Double avgSalary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_11, avgSalary, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple Fields and Expressions.
   *
   * <p>This query averages and sums the salaries of Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery12a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double[] expected = new Double[] {45000., 90000.};
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary), sum(salary)");
        q.declareParameters("String deptName");
        Object[] avgSum = (Object[]) q.execute("R&D");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_12, avgSum, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple Fields and Expressions.
   *
   * <p>This query averages and sums the salaries of Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery12b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double[] expected = new Double[] {45000., 90000.};
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary), sum(salary)");
        q.declareParameters("String deptName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setNamedParameters(paramValues);
        Object[] avgSum = q.executeResultUnique(Object[].class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_12, avgSum, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple Fields and Expressions.
   *
   * <p>This query averages and sums the salaries of Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery12c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double[] expected = new Double[] {45000., 90000.};
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "department.name == deptName")) {
        q.setResult("avg(salary), sum(salary)");
        q.declareParameters("String deptName");
        q.setParameters("R&D");
        Object[] avgSum = q.executeResultUnique(Object[].class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_12, avgSum, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple Fields and Expressions.
   *
   * <p>This query averages and sums the salaries of Employees who work in the parameter department.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery12d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double[] expected = new Double[] {45000., 90000.};
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_12)) {
        q.setParameters("R&D");
        Object[] avgSum = q.executeResultUnique(Object[].class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_12, avgSum, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple Fields and Expressions.
   *
   * <p>This query averages and sums the salaries of Employees who work in the parameter department.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery12f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double[] expected = new Double[] {45000., 90000.};
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression deptName = q.stringParameter("deptName");
        q.result(false, cand.salary.avg(), cand.salary.sum())
            .filter(cand.department.name.eq(deptName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("deptName", "R&D");
        q.setParameters(paramValues);
        Object[] avgSum = q.executeResultUnique(Object[].class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_12, avgSum, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple fields with Grouping.
   *
   * <p>This query averages and sums the salaries of Employees who work in all departments having
   * more than one employee and aggregates by department name.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery13a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Object[] expectedRow = new Object[] {45000., 90000., "R&D"};
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class)) {
        q.setResult("avg(salary), sum(salary), department.name");
        q.setGrouping("department.name having count(department.name) > 1");
        List<Object[]> results = (List<Object[]>) q.execute();
        if (results.size() != 1) {
          fail(
              ASSERTION_FAILED,
              "Query result has size " + results.size() + ", expected query result of size 1");
        }
        Object[] row = results.get(0);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_13, row, expectedRow);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple fields with Grouping.
   *
   * <p>This query averages and sums the salaries of Employees who work in all departments having
   * more than one employee and aggregates by department name.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery13b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Object[] expectedRow = new Object[] {45000., 90000., "R&D"};
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class)) {
        q.setResult("avg(salary), sum(salary), department.name");
        q.setGrouping("department.name having count(department.name) > 1");
        List<Object[]> results = q.executeResultList(Object[].class);
        if (results.size() != 1) {
          fail(
              ASSERTION_FAILED,
              "Query result has size " + results.size() + ", expected query result of size 1");
        }
        Object[] row = results.get(0);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_13, row, expectedRow);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple fields with Grouping.
   *
   * <p>This query averages and sums the salaries of Employees who work in all departments having
   * more than one employee and aggregates by department name.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery13d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Object[] expectedRow = new Object[] {45000., 90000., "R&D"};
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_13)) {
        List<Object[]> results = q.executeResultList(Object[].class);
        if (results.size() != 1) {
          fail(
              ASSERTION_FAILED,
              "Query result has size " + results.size() + ", expected query result of size 1");
        }
        Object[] row = results.get(0);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_13, row, expectedRow);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple fields with Grouping.
   *
   * <p>This query averages and sums the salaries of Employees who work in all departments having
   * more than one employee and aggregates by department name.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery13e() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Object[] expectedRow = new Object[] {45000., 90000., "R&D"};
      try (Query<FullTimeEmployee> q = pm.newNamedQuery(FullTimeEmployee.class, "grouping")) {
        List<Object[]> results = q.executeResultList(Object[].class);
        if (results.size() != 1) {
          fail(
              ASSERTION_FAILED,
              "Query result has size " + results.size() + ", expected query result of size 1");
        }
        Object[] row = results.get(0);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_13, row, expectedRow);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Aggregation of Multiple fields with Grouping.
   *
   * <p>This query averages and sums the salaries of Employees who work in all departments having
   * more than one employee and aggregates by department name.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery13f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Object[] expectedRow = new Object[] {45000., 90000., "R&D"};
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        q.result(false, cand.salary.avg(), cand.salary.sum(), cand.department.name)
            .groupBy(cand.department.name)
            .having(cand.department.name.count().gt(1L));
        List<Object[]> results = q.executeResultList(Object[].class);
        if (results.size() != 1) {
          fail(
              ASSERTION_FAILED,
              "Query result has size " + results.size() + ", expected query result of size 1");
        }
        Object[] row = results.get(0);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_13, row, expectedRow);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Instance.
   *
   * <p>This query returns a single instance of Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery14a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Employee expectedEmp = getTransientCompanyModelInstance(Employee.class, "emp1");
      try (Query<Employee> q = pm.newQuery(Employee.class, "firstname == empName")) {
        q.setUnique(true);
        q.declareParameters("String empName");
        Employee emp = (Employee) q.execute("Michael");
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_14, emp, expectedEmp);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Instance.
   *
   * <p>This query returns a single instance of Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery14b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Employee expectedEmp = getTransientCompanyModelInstance(Employee.class, "emp1");
      try (Query<Employee> q = pm.newQuery(Employee.class, "firstname == empName")) {
        q.setUnique(true);
        q.declareParameters("String empName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("empName", "Michael");
        q.setNamedParameters(paramValues);
        Employee emp = q.executeUnique();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_14, emp, expectedEmp);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Instance.
   *
   * <p>This query returns a single instance of Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery14c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Employee expectedEmp = getTransientCompanyModelInstance(Employee.class, "emp1");
      try (Query<Employee> q = pm.newQuery(Employee.class, "firstname == empName")) {
        q.setUnique(true);
        q.declareParameters("String empName");
        q.setParameters("Michael");
        Employee emp = q.executeUnique();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_14, emp, expectedEmp);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Instance.
   *
   * <p>This query returns a single instance of Employee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery14d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Employee expectedEmp = getTransientCompanyModelInstance(Employee.class, "emp1");
      try (Query<Employee> q = pm.newQuery(SINGLE_STRING_QUERY_14)) {
        q.setParameters("Michael");
        Employee emp = q.executeResultUnique(Employee.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_14, emp, expectedEmp);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Instance.
   *
   * <p>This query returns a single instance of Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery14f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Employee expectedEmp = getTransientCompanyModelInstance(Employee.class, "emp1");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate("this");
        StringExpression empName = q.stringParameter("empName");
        q.filter(cand.firstname.eq(empName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("empName", "Michael");
        q.setParameters(paramValues);
        Employee emp = q.executeUnique();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_14, emp, expectedEmp);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Field.
   *
   * <p>This query returns a single field of a single Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery15a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expectedSalary = 40000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "firstname == empName")) {
        q.setResult("salary");
        q.setResultClass(Double.class);
        q.setUnique(true);
        q.declareParameters("String empName");
        Double salary = (Double) q.execute("Michael");
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_15, salary, expectedSalary);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Field.
   *
   * <p>This query returns a single field of a single Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery15b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expectedSalary = 40000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "firstname == empName")) {
        q.setResult("salary");
        q.setResultClass(Double.class);
        q.declareParameters("String empName");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("empName", "Michael");
        q.setNamedParameters(paramValues);
        Double salary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_15, salary, expectedSalary);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Field.
   *
   * <p>This query returns a single field of a single Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery15c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expectedSalary = 40000.;
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "firstname == empName")) {
        q.setResult("salary");
        q.setResultClass(Double.class);
        q.declareParameters("String empName");
        q.setParameters("Michael");
        Double salary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_15, salary, expectedSalary);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Field.
   *
   * <p>This query returns a single field of a single Employee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery15d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expectedSalary = 40000.;
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_15)) {
        q.setParameters("Michael");
        Double salary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_15, salary, expectedSalary);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Selection of a Single Field.
   *
   * <p>This query returns a single field of a single Employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery15f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Double expectedSalary = 40000.;
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        StringExpression empName = q.stringParameter("empName");
        q.result(false, cand.salary).filter(cand.firstname.eq(empName));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("empName", "Michael");
        q.setParameters(paramValues);
        Double salary = q.executeResultUnique(Double.class);
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_15, salary, expectedSalary);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery16a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpWrapper> expected = testQuery16Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpWrapper.class);
        q.declareParameters("Double sal");
        List<EmpWrapper> infos = (List<EmpWrapper>) q.execute(30000.);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery16b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpWrapper> expected = testQuery16Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpWrapper.class);
        q.declareParameters("Double sal");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setNamedParameters(paramValues);
        List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery16c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpWrapper> expected = testQuery16Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpWrapper.class);
        q.declareParameters("Double sal");
        q.setParameters(30000.);
        List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery16d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpWrapper> expected = testQuery16Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_16)) {
        q.setParameters(30000.);
        List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery16f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpWrapper> expected = testQuery16Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery17a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpInfo> expected = testQuery17Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpInfo.class);
        q.declareParameters("Double sal");
        List<EmpInfo> infos = (List<EmpInfo>) q.execute(30000.);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_17, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery17b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpInfo> expected = testQuery17Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpInfo.class);
        q.declareParameters("Double sal");
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setNamedParameters(paramValues);
        List<EmpInfo> infos = q.executeResultList(EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_17, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery17c() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpInfo> expected = testQuery17Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal")) {
        q.setResultClass(EmpInfo.class);
        q.declareParameters("Double sal");
        q.setParameters(30000.);
        List<EmpInfo> infos = q.executeResultList(EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_17, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery17d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpInfo> expected = testQuery17Helper();
      try (Query<FullTimeEmployee> q = pm.newQuery(SINGLE_STRING_QUERY_17)) {
        q.setParameters(30000.);
        List<EmpInfo> infos = q.executeResultList(EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_17, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery17f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<EmpInfo> expected = testQuery17Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<EmpInfo> infos = q.executeResultList(EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_17, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of variables.
   *
   * <p>This query returns the names of all Employees of all "Research" departments.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery18a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
      try (Query<Department> q = pm.newQuery(Department.class)) {
        q.declareVariables("org.apache.jdo.tck.pc.company.Employee e");
        q.setFilter("name.startsWith('R&D') && employees.contains(e)");
        q.setResult("e.firstname");
        List<String> names = (List<String>) q.execute();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_18, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of variables.
   *
   * <p>This query returns the names of all Employees of all "Research" departments.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery18b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
      try (Query<Department> q = pm.newQuery(Department.class)) {
        q.declareVariables("org.apache.jdo.tck.pc.company.Employee e");
        q.setFilter("name.startsWith('R&D') && employees.contains(e)");
        q.setResult("e.firstname");
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_18, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of variables.
   *
   * <p>This query returns the names of all Employees of all "Research" departments.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery18d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
      try (Query<Department> q = pm.newQuery(SINGLE_STRING_QUERY_18)) {
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_18, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of variables.
   *
   * <p>This query returns the names of all Employees of all "Research" departments.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery18e() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
      try (Query<Department> q = pm.newNamedQuery(Department.class, "projectingVariables")) {
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_18, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of variables.
   *
   * <p>This query returns the names of all Employees of all "Research" departments.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery18f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
      try (JDOQLTypedQuery<Department> q = pm.newJDOQLTypedQuery(Department.class)) {
        QDepartment cand = QDepartment.candidate("this");
        QEmployee e = QEmployee.variable("e");
        q.filter(cand.name.startsWith("R&D").and(cand.employees.contains(e)))
            .result(false, e.firstname);
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_18, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Non-correlated subquery
   *
   * <p>This query returns names of employees who work more than the average of all employees.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery19a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig");
      try (Query<Employee> q = pm.newQuery(Employee.class)) {
        Query<Employee> subq = pm.newQuery(Employee.class);
        subq.setResult("avg(weeklyhours)");
        q.setFilter("this.weeklyhours > average_hours");
        q.setResult("this.firstname");
        q.addSubquery(subq, "double average_hours", null);
        List<String> names = (List<String>) q.execute();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_19, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Non-correlated subquery
   *
   * <p>This query returns names of employees who work more than the average of all employees.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery19b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig");
      try (Query<Employee> q = pm.newQuery(Employee.class)) {
        Query<Employee> subq = pm.newQuery(Employee.class);
        subq.setResult("avg(weeklyhours)");
        q.setFilter("this.weeklyhours > average_hours");
        q.setResult("this.firstname");
        q.addSubquery(subq, "double average_hours", null);
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_19, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Non-correlated subquery
   *
   * <p>This query returns names of employees who work more than the average of all employees.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery19d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig");
      try (Query<Employee> q = pm.newQuery(SINGLE_STRING_QUERY_19)) {
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_19, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Non-correlated subquery
   *
   * <p>This query returns names of employees who work more than the average of all employees.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery19f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael", "Craig");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate("this");
        JDOQLTypedSubquery<Employee> subquery = q.subquery("e");
        QEmployee candsub = QEmployee.candidate("e");
        q.result(false, cand.firstname)
            .filter(cand.weeklyhours.gt(subquery.selectUnique(candsub.weeklyhours.avg())));
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_19, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Correlated subquery.
   *
   * <p>This query returns names of employees who work more than the average of employees in the
   * same department having the same manager. The candidate collection of the subquery is the
   * collection of employees in the department of the candidate employee and the parameter passed to
   * the subquery is the manager of the candidate employee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery20a() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael");
      try (Query<Employee> q = pm.newQuery(Employee.class)) {
        Query<Employee> subq = pm.newQuery(Employee.class);
        subq.setFilter("this.manager == :manager");
        subq.setResult("avg(weeklyhours)");
        q.setFilter("this.weeklyhours > average_hours");
        q.setResult("firstname");
        q.addSubquery(subq, "double average_hours", "this.department.employees", "this.manager");
        List<String> names = (List<String>) q.execute();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_20, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Correlated subquery.
   *
   * <p>This query returns names of employees who work more than the average of employees in the
   * same department having the same manager. The candidate collection of the subquery is the
   * collection of employees in the department of the candidate employee and the parameter passed to
   * the subquery is the manager of the candidate employee.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery20b() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael");
      try (Query<Employee> q = pm.newQuery(Employee.class)) {
        Query<Employee> subq = pm.newQuery(Employee.class);
        subq.setFilter("this.manager == :manager");
        subq.setResult("avg(weeklyhours)");
        q.setFilter("this.weeklyhours > average_hours");
        q.setResult("firstname");
        q.addSubquery(subq, "double average_hours", "this.department.employees", "this.manager");
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_20, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Correlated subquery.
   *
   * <p>This query returns names of employees who work more than the average of employees in the
   * same department having the same manager. The candidate collection of the subquery is the
   * collection of employees in the department of the candidate employee and the parameter passed to
   * the subquery is the manager of the candidate employee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery20d() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael");
      try (Query<Employee> q = pm.newQuery(SINGLE_STRING_QUERY_20)) {
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_20, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Correlated subquery.
   *
   * <p>This query returns names of employees who work more than the average of employees in the
   * same department having the same manager. The candidate collection of the subquery is the
   * collection of employees in the department of the candidate employee and the parameter passed to
   * the subquery is the manager of the candidate employee.
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQuery20f() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<String> expected = Arrays.asList("Michael");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate("this");
        JDOQLTypedSubquery<Employee> subquery =
            q.subquery(cand.department.employees, Employee.class, "e");
        QEmployee candsub = QEmployee.candidate("e");
        subquery.filter(candsub.manager.eq(cand.manager));
        q.result(false, cand.firstname)
            .filter(cand.weeklyhours.gt(subquery.selectUnique(candsub.weeklyhours.avg())));
        List<String> names = q.executeResultList(String.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_20, names, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  private List<Info> testQuery09Helper() {
    Info info1 = new Info();
    info1.firstname = "Michael";
    info1.salary = 40000.;
    info1.reportsTo = getTransientCompanyModelInstance(Employee.class, "emp2");
    Info info2 = new Info();
    info2.firstname = "Craig";
    info2.salary = 50000.;
    info2.reportsTo = null;
    return Arrays.asList(info1, info2);
  }

  private List<EmpWrapper> testQuery16Helper() {
    EmpWrapper wrapper1 = new EmpWrapper();
    wrapper1.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp1");
    EmpWrapper wrapper2 = new EmpWrapper();
    wrapper2.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp2");
    EmpWrapper wrapper3 = new EmpWrapper();
    wrapper3.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp5");
    return Arrays.asList(wrapper1, wrapper2, wrapper3);
  }

  private List<EmpInfo> testQuery17Helper() {
    EmpInfo info1 = new EmpInfo();
    info1.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp1"));
    EmpInfo info2 = new EmpInfo();
    info2.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp2"));
    EmpInfo info3 = new EmpInfo();
    info3.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp5"));
    return Arrays.asList(info1, info2, info3);
  }

  public static class Info {
    public String firstname;
    public Double salary;
    public Person reportsTo;

    public Info() {}

    public Info(String firstname, Double salary, Person reportsTo) {
      this.firstname = firstname;
      this.salary = salary;
      this.reportsTo = reportsTo;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof Info)) {
        return false;
      }
      Info other = (Info) obj;
      if (!EqualityHelper.equals(firstname, other.firstname)) {
        return false;
      }
      if (!EqualityHelper.equals(salary, other.salary)) {
        return false;
      }
      if (!EqualityHelper.equals(reportsTo, other.reportsTo)) {
        return false;
      }
      return true;
    }

    public int hashCode() {
      int hashCode = 0;
      hashCode += firstname == null ? 0 : firstname.hashCode();
      hashCode += salary == null ? 0 : salary.hashCode();
      hashCode += reportsTo == null ? 0 : reportsTo.hashCode();
      return hashCode;
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Info(");
      builder.append("firstname:").append(firstname);
      builder.append(", salary:").append(salary);
      builder.append(", reportsTo:").append(reportsTo == null ? "null" : reportsTo.getFirstname());
      builder.append(")");
      return builder.toString();
    }
  }

  public static class EmpWrapper {
    public FullTimeEmployee FullTimeEmployee;

    public EmpWrapper() {}

    // Need constructor to prevent
    // java.lang.NullPointerException
    // at
    // org.datanucleus.query.QueryUtils.createResultObjectUsingDefaultConstructorAndSetters(QueryUtils.java:293)
    public EmpWrapper(FullTimeEmployee FullTimeEmployee) {
      this.FullTimeEmployee = FullTimeEmployee;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof EmpWrapper)) {
        return false;
      }
      EmpWrapper other = (EmpWrapper) obj;
      if (!EqualityHelper.equals(FullTimeEmployee, other.FullTimeEmployee)) {
        return false;
      }
      return true;
    }

    public int hashCode() {
      int hashCode = 0;
      hashCode += FullTimeEmployee == null ? 0 : FullTimeEmployee.hashCode();
      return hashCode;
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("EmpWrapper(");
      builder
          .append("FullTimeEmployee:")
          .append(FullTimeEmployee == null ? "null" : "name = " + FullTimeEmployee.getFirstname());
      builder.append(")");
      return builder.toString();
    }
  }

  public static class EmpInfo {
    private FullTimeEmployee worker;

    public EmpInfo() {}

    // Need constructor to prevent
    // java.lang.NullPointerException
    // at
    // org.datanucleus.query.QueryUtils.createResultObjectUsingDefaultConstructorAndSetters(QueryUtils.java:293)
    public EmpInfo(FullTimeEmployee worker) {
      this.worker = worker;
    }

    public FullTimeEmployee getWorker() {
      return worker;
    }

    public void setFullTimeEmployee(FullTimeEmployee e) {
      worker = e;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof EmpInfo)) {
        return false;
      }
      EmpInfo other = (EmpInfo) obj;
      if (!EqualityHelper.equals(worker, other.worker)) {
        return false;
      }
      return true;
    }

    public int hashCode() {
      int hashCode = 0;
      hashCode += worker == null ? 0 : worker.hashCode();
      return hashCode;
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("EmpInfo(");
      builder.append("worker:").append(worker == null ? "null" : worker.getFirstname());
      builder.append(")");
      return builder.toString();
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

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
  }
}
