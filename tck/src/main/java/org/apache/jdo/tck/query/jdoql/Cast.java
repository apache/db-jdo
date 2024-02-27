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
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * <B>Title:</B> Cast Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-38. <br>
 * <B>Assertion Description: </B> The cast operator can be used for type conversions on classes.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Cast extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-38 (Cast) failed: ";

  /** */
  @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // ToDo: the test methods hangs when executed in parallel
  public void testPositive0() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      // DataNucleus: java.lang.ClassCastException:
      // org.datanucleus.api.jdo.query.PersistableExpressionImpl
      // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
      QFullTimeEmployee cast = (QFullTimeEmployee) cand.cast(FullTimeEmployee.class);
      query.filter(cast.salary.gt(15000.0));

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "((FullTimeEmployee)this).salary > 15000.0",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.FullTimeEmployee",
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

  /** */
  @SuppressWarnings("unchecked")
  @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // ToDo: the test methods hangs when executed in parallel
  public void testPositive1() {
    List<Department> expected =
        getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate();
      QEmployee e = QEmployee.variable("e");
      // DataNucleus: java.lang.ClassCastException:
      // org.datanucleus.api.jdo.query.PersistableExpressionImpl
      // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
      QFullTimeEmployee cast = (QFullTimeEmployee) e.cast(FullTimeEmployee.class);
      query.filter(cand.employees.contains(e).and(cast.salary.gt(15000.0)));

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employees.contains(e) && ((FullTimeEmployee)e).salary > 15000.0",
              /*VARIABLES*/ "Employee e",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.FullTimeEmployee",
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
