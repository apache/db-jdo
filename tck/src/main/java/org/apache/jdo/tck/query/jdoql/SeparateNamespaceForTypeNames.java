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
import javax.jdo.query.Expression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * <B>Title:</B> Namespace of Type Names Separate From Fields, Variables, Parameters <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-1. <br>
 * <B>Assertion Description: </B> Type names have their own namespace that is separate from the
 * namespace for fields, variables and parameters.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeparateNamespaceForTypeNames extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-1 (SeparateNamespaceForTypeNames) failed: ";

  /** */
  @Test
  public void testParameterName() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3");

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    Expression<Department> empParam = query.parameter("Department", Department.class);
    query.filter(cand.department.eq(empParam));

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("Department", getPersistentCompanyModelInstance(Department.class, "dept1"));

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "department == Department",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ "Department Department",
            /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Department",
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  public void testVaiableName() {
    List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    QEmployee variable = QEmployee.variable("Employee");
    query.filter(cand.employees.contains(variable).and(variable.firstname.eq("emp1First")));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "employees.contains(Employee) && Employee.firstname == \"emp1First\"",
            /*VARIABLES*/ "Employee Employee",
            /*PARAMETERS*/ null,
            /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Employee",
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  @BeforeAll
  @Override
  public void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  public void tearDown() {
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
