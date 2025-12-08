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

import java.util.*;
import javax.jdo.*;
import javax.jdo.query.NumericExpression;
import javax.jdo.query.StringExpression;
import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.EqualityHelper;
import org.junit.jupiter.api.*;
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
public class SampleReadQueries5 extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (SampleReadQueries) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org.apache.jdo.tck.pc.company.data.SampleQueryTestData";

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
    logger.debug("setUP 1 " + Thread.currentThread().getName());
    super.setUp();
    logger.debug("setUP 2" + Thread.currentThread().getName());
  }

  @AfterAll
  @Override
  protected void tearDown() {
    logger.debug("tearDown 1 " + Thread.currentThread().getName());
    super.tearDown();
    logger.debug("tearDown 2 " + Thread.currentThread().getName());
  }

  @BeforeEach
  void beforeEach() {
    logger.debug("beforeEach " + Thread.currentThread().getName());
  }

  @AfterEach
  void afterEach() {
    logger.debug("afterEach " + Thread.currentThread().getName());
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
