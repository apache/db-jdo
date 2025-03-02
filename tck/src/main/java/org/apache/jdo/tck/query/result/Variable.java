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

package org.apache.jdo.tck.query.result;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QCompany;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Variable in Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.9-3. <br>
 * <B>Assertion Description: </B> If a variable or a field of a variable is included in the result,
 * either directly or via navigation through the variable, then the semantics of the contains clause
 * that include the variable change. In this case, all values of the variable that satisfy the
 * filter are included in the result. Result expressions begin with either an instance of the
 * candidate class (with an explicit or implicit "this") or an instance of a variable (using the
 * variable name). The candidate tuples are the cartesian product of the candidate class and all
 * variables used in the result. The result tuples are the tuples of the candidate class and all
 * variables used in the result that satisfy the filter. The result is the collection of result
 * expressions projected from the result tuples.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Variable extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_3a =
      "Assertion A14.6.9-3 (VariableInResult) failed: ";

  /** */
  private static final String ASSERTION_FAILED_3b =
      "Assertion A14.6.9-3 (VariableInResultNavigation) failed: ";

  /** */
  private static final String ASSERTION_FAILED_3c =
      "Assertion A14.6.9-3 (VariableInResult) failed: ";

  /** The expected results of valid queries. */
  private final Object company1 = getTransientCompanyModelInstance(Company.class, "company1");

  private final Object emp1 = getTransientCompanyModelInstance(Employee.class, "emp1");
  private final Object emp2 = getTransientCompanyModelInstance(Employee.class, "emp2");
  private final Object emp3 = getTransientCompanyModelInstance(Employee.class, "emp3");
  private final Object emp4 = getTransientCompanyModelInstance(Employee.class, "emp4");
  private final Object emp5 = getTransientCompanyModelInstance(Employee.class, "emp5");
  private final Object proj1 = getTransientCompanyModelInstance(Project.class, "proj1");
  private final Object proj2 = getTransientCompanyModelInstance(Project.class, "proj2");
  private final Object proj3 = getTransientCompanyModelInstance(Project.class, "proj3");
  private final Object dept1 = getTransientCompanyModelInstance(Department.class, "dept1");
  private final Object dept2 = getTransientCompanyModelInstance(Department.class, "dept2");

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDistinctNoNavigation() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      query.filter(cand.employees.contains(e));
      query.result(true, e);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "distinct e",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e)",
              /*VARIABLES*/ "Employee e",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3a, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDistinctNavigation() {
    Object elem = new Object[] {Long.valueOf(1), "orange"};
    Object expected = Collections.singletonList(elem);
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      QProject p = QProject.variable("p");
      query.filter(cand.projects.contains(p).and(p.name.eq("orange")));
      query.result(true, p.projid, p.name);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "distinct p.projid, p.name",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "projects.contains(p) & p.name == 'orange'",
              /*VARIABLES*/ "Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3a, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigation() {
    Object expected =
        Arrays.asList(
            new Object[] {Long.valueOf(1), "orange"},
            new Object[] {Long.valueOf(1), "orange"},
            new Object[] {Long.valueOf(1), "orange"});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      QProject p = QProject.variable("p");
      query.filter(cand.projects.contains(p).and(p.name.eq("orange")));
      query.result(false, p.projid, p.name);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "p.projid, p.name",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "projects.contains(p) & p.name == 'orange'",
              /*VARIABLES*/ "Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3a, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  //@Execution(ExecutionMode.CONCURRENT)
  public void testNoNavigation() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      query.filter(cand.employees.contains(e));
      query.result(false, e);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e)",
              /*VARIABLES*/ "Employee e",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3a, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleProjectionWithConstraints() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      query.filter(cand.deptid.eq(2L).and(cand.employees.contains(e)));
      query.result(false, e);

      // SELECT e FROM Department WHERE deptid==2 & employees.contains(e) VARIABLES Employee e
      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "deptid == 2 & employees.contains(e)",
              /*VARIABLES*/ "Employee e",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3a, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3a, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  //@Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithCompanyAndDepartmentAndEmployeeAndProject() {
    Object expected =
        Arrays.asList(
            new Object[] {company1, dept1, emp1, proj1},
            new Object[] {company1, dept1, emp2, proj1},
            new Object[] {company1, dept1, emp3, proj1},
            new Object[] {company1, dept1, emp2, proj2},
            new Object[] {company1, dept1, emp3, proj2},
            new Object[] {company1, dept2, emp4, proj3},
            new Object[] {company1, dept2, emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Company> query = pm.newJDOQLTypedQuery(Company.class);
      QCompany cand = QCompany.candidate("this");
      QDepartment d = QDepartment.variable("d");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(
          cand.name
              .eq("Sun Microsystems, Inc.")
              .and(cand.departments.contains(d))
              .and(d.employees.contains(e))
              .and(e.projects.contains(p)));
      query.result(false, cand, d, e, p);

      QueryElementHolder<Company> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this, d, e, p",
              /*INTO*/ null,
              /*FROM*/ Company.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "name == \"Sun Microsystems, Inc.\" && "
                  + "departments.contains(d) && d.employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Department d; Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3b, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  //@Execution(ExecutionMode.CONCURRENT) // TODO this fixed it
  public void testNavigationWithCompanyAndEmployeeAndProject() {
    Object expected =
        Arrays.asList(
            new Object[] {company1, emp1, proj1},
            new Object[] {company1, emp2, proj1},
            new Object[] {company1, emp3, proj1},
            new Object[] {company1, emp2, proj2},
            new Object[] {company1, emp3, proj2},
            new Object[] {company1, emp4, proj3},
            new Object[] {company1, emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Company> query = pm.newJDOQLTypedQuery(Company.class);
      QCompany cand = QCompany.candidate("this");
      QDepartment d = QDepartment.variable("d");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(
          cand.name
              .eq("Sun Microsystems, Inc.")
              .and(cand.departments.contains(d))
              .and(d.employees.contains(e))
              .and(e.projects.contains(p)));
      query.result(false, cand, e, p);

      QueryElementHolder<Company> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this, e, p",
              /*INTO*/ null,
              /*FROM*/ Company.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "name == \"Sun Microsystems, Inc.\" && "
                  + "departments.contains(d) && d.employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Department d; Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3b, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithDepartmentAndEmployeeAndProject() {
    Object expected =
        Arrays.asList(
            new Object[] {dept1, emp1, proj1},
            new Object[] {dept1, emp2, proj1},
            new Object[] {dept1, emp3, proj1},
            new Object[] {dept1, emp2, proj2},
            new Object[] {dept1, emp3, proj2},
            new Object[] {dept2, emp4, proj3},
            new Object[] {dept2, emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Company> query = pm.newJDOQLTypedQuery(Company.class);
      QCompany cand = QCompany.candidate("this");
      QDepartment d = QDepartment.variable("d");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(
          cand.name
              .eq("Sun Microsystems, Inc.")
              .and(cand.departments.contains(d))
              .and(d.employees.contains(e))
              .and(e.projects.contains(p)));
      query.result(false, d, e, p);

      QueryElementHolder<Company> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "d, e, p",
              /*INTO*/ null,
              /*FROM*/ Company.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "name == \"Sun Microsystems, Inc.\" && "
                  + "departments.contains(d) && d.employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Department d; Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3b, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3b, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithConstraint() {
    Object expected =
        Arrays.asList(
            new Object[] {emp1, proj1}, new Object[] {emp2, proj1}, new Object[] {emp3, proj1});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(cand.employees.contains(e).and(e.projects.contains(p)).and(p.name.eq("orange")));
      query.result(false, e, p);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e, p",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e) && e.projects.contains(p) && p.name == 'orange'",
              /*VARIABLES*/ "Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3c, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithoutConstraint() {
    Object expected =
        Arrays.asList(
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2},
            new Object[] {emp4, proj3},
            new Object[] {emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(cand.employees.contains(e).and(e.projects.contains(p)));
      query.result(false, e, p);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e, p",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3c, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithThis() {
    Object expected =
        Arrays.asList(
            new Object[] {dept1, emp1, proj1},
            new Object[] {dept1, emp2, proj1},
            new Object[] {dept1, emp3, proj1},
            new Object[] {dept1, emp2, proj2},
            new Object[] {dept1, emp3, proj2},
            new Object[] {dept2, emp4, proj3},
            new Object[] {dept2, emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(cand.employees.contains(e).and(e.projects.contains(p)));
      query.result(false, cand, e, p);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this, e, p",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3c, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithThisConstraint() {
    Object expected =
        Arrays.asList(
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(cand.deptid.eq(1L).and(cand.employees.contains(e)).and(e.projects.contains(p)));
      query.result(false, e, p);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e, p",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "deptid == 1 && employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3c, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  //@Execution(ExecutionMode.CONCURRENT)
  public void testNavigationWithCompanyConstraint() {
    Object expected =
        Arrays.asList(
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2},
            new Object[] {emp4, proj3},
            new Object[] {emp5, proj3});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      QEmployee e = QEmployee.variable("e");
      QProject p = QProject.variable("p");
      query.filter(
          cand.company
              .name
              .eq("Sun Microsystems, Inc.")
              .and(cand.employees.contains(e))
              .and(e.projects.contains(p)));
      query.result(false, e, p);

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "e, p",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "company.name == \"Sun Microsystems, Inc.\" && employees.contains(e) && e.projects.contains(p)",
              /*VARIABLES*/ "Employee e; Project p",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED_3c, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED_3c, pm, holder, null, true, expected);
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
