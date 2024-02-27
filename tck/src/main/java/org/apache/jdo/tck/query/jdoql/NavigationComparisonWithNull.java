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

package org.apache.jdo.tck.query.jdoql;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QMedicalInsurance;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Navigation Through a Reference and comparing a Relationship with null <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-13. <br>
 * <B>Assertion Description: </B> Navigation through single-valued fields is specified by the Java
 * language syntax of <code>field_name.field_name....field_name</code>.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NavigationComparisonWithNull extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-13 (NavigationComparisonWithNull) failed: ";

  /** */
  public static final String NAVIGATION_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForNavigationTests.xml";

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return NAVIGATION_TEST_COMPANY_TESTDATA;
  }

  /*
   * Manager relationship:
   * emp0
   *   emp1
   *     emp2
   *     emp3
   *     emp10
   * emp4
   *   emp5
   *   emp6
   * emp7
   *   emp8
   *   emp9
   */

  /** this.manager == null */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive0() {
    // 0: simple manager check being null
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp0", "emp4", "emp7");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.eq((Employee) null));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.manager == null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.manager != null */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive1() {
    // 1: simple manager check being not null
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.ne((Employee) null));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.manager != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** !(this.manager == null) */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive2() {
    // 2: simple manager check being not null using not operator
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.eq((Employee) null).not());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "!(this.manager == null)",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.manager.manager == null Disabled, because it currently fails on the RI. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive3() {
    // 3: manager's manager check
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp0", "emp1", "emp4", "emp5", "emp6", "emp7", "emp8", "emp9");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.manager.eq((Employee) null));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.manager.manager == null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.manager != null AND this.manager.manager == null */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive4() {
    // 4: manager's manager check with extra check on first level manager
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp5", "emp6", "emp8", "emp9");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.ne((Employee) null).and(cand.manager.manager.eq((Employee) null)));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.manager != null && this.manager.manager == null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.manager.manager != null */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive5() {
    // 5 : manager's manager check not being null
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp2", "emp3", "emp10");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.filter(cand.manager.manager.ne((Employee) null));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.manager.manager != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** !(this.manager.manager == null) */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive6() {
    // 6 : manager's manager check not being null using not operator
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp2", "emp3", "emp10");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      QEmployee e = QEmployee.variable("e");
      query.filter(cand.manager.manager.eq((Employee) null).not());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "!(this.manager.manager == null)",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.employee.manager.manager == null Disabled, because it currently fails on the RI. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive7() {
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns1", "medicalIns4", "medicalIns5", "medicalIns98");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<MedicalInsurance> query = pm.newJDOQLTypedQuery(MedicalInsurance.class);
      QMedicalInsurance cand = QMedicalInsurance.candidate();
      query.filter(cand.employee.manager.manager.eq((Employee) null));

      QueryElementHolder<MedicalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ MedicalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.employee.manager.manager == null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * this.employee != null AND this.employee.manager != null AND this.employee.manager.manager ==
   * null
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive8() {
    // 8 : multiple relationships
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns1", "medicalIns5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<MedicalInsurance> query = pm.newJDOQLTypedQuery(MedicalInsurance.class);
      QMedicalInsurance cand = QMedicalInsurance.candidate();
      query.filter(
          cand.employee
              .ne((Employee) null)
              .and(cand.employee.manager.ne((Employee) null))
              .and(cand.employee.manager.manager.eq((Employee) null)));

      QueryElementHolder<MedicalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ MedicalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.employee != null && this.employee.manager != null && "
                  + "this.employee.manager.manager == null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** this.employee.manager.manager != null */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive9() {
    // 9 : multiple relationships
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns2", "medicalIns3");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<MedicalInsurance> query = pm.newJDOQLTypedQuery(MedicalInsurance.class);
      QMedicalInsurance cand = QMedicalInsurance.candidate();
      query.filter(cand.employee.manager.manager.ne((Employee) null));

      QueryElementHolder<MedicalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ MedicalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.employee.manager.manager != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** !(this.employee.manager.manager == null) */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive10() {
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns2", "medicalIns3");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<MedicalInsurance> query = pm.newJDOQLTypedQuery(MedicalInsurance.class);
      QMedicalInsurance cand = QMedicalInsurance.candidate();
      query.filter(cand.employee.manager.manager.eq((Employee) null).not());

      QueryElementHolder<MedicalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ MedicalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "!(this.employee.manager.manager == null)",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
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
