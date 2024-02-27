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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.query.Expression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Multiple identical imports <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-5. <br>
 * <B>Assertion Description: </B> It is valid to specify the same import multiple times.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultipleIdenticalImports extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-5 (MultipleIdenticalImports) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testImportDepartmentTwice() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      Expression<Department> empParam = query.parameter("d", Department.class);
      query.filter(cand.department.eq(empParam));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("d", getPersistentCompanyModelInstance(pm, Department.class, "dept1"));

      // Import Department twice
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "department == d",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "Department d",
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Department; "
                  + "import org.apache.jdo.tck.pc.company.Department;",
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testImportDepartmentExplicitlyAndPerTypeImportOnDemand() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      Expression<Department> empParam = query.parameter("d", Department.class);
      query.filter(cand.department.eq(empParam));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("d", getPersistentCompanyModelInstance(pm, Department.class, "dept1"));

      // Import Department explictly and per type-import-on-demand
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "department == d",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "Department d",
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Department; "
                  + "import org.apache.jdo.tck.pc.company.*",
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testTypeImportOnDemandTwice() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      Expression<Department> empParam = query.parameter("d", Department.class);
      query.filter(cand.department.eq(empParam));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("d", getPersistentCompanyModelInstance(pm, Department.class, "dept1"));

      // type-import-on-demand twice
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "department == d",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "Department d",
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.*; "
                  + "import org.apache.jdo.tck.pc.company.*",
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

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
