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

package org.apache.jdo.tck.query.jdoql.methods;

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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Supported collection methods <br>
 * <B>Keywords:</B> query collection <br>
 * <B>Assertion ID:</B> A14.6.2-45. <br>
 * <B>Assertion Description: </B> Supported collection methods:
 *
 * <UL>
 *   <LI>isEmpty
 *   <LI>contains
 *   <LI>size
 * </UL>
 */
public class SupportedCollectionMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-45 (SupportedCollectionMethods) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SupportedCollectionMethods.class);
  }

  /** */
  @SuppressWarnings("unchecked")
  public void testContains() {
    // contains(VARIABLE)
    List<Department> expectedResult =
        getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    QEmployee eVariable = QEmployee.variable("e");
    query.filter(cand.employees.contains(eVariable).and(eVariable.personid.eq(1L)));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "employees.contains(e) && e.personid == 1",
            /*VARIABLES*/ "Employee e",
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);

    // contains(PARAMETER)

    expectedResult = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    getPM().currentTransaction().begin();
    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("e", getPersistentCompanyModelInstance(Employee.class, "emp1"));
    getPM().currentTransaction().commit();

    query = getPM().newJDOQLTypedQuery(Department.class);
    cand = QDepartment.candidate();
    Expression<Employee> paramExpression = query.parameter("e", Employee.class);
    query.filter(cand.employees.contains(paramExpression));

    holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "employees.contains(e)",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ "Employee e",
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);
  }

  /** */
  public void testIsEmpty() {

    // !isEmpty
    List<Department> expectedResult =
        getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    query.filter(cand.employees.isEmpty().not());

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "!employees.isEmpty()",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);

    // isEmpty
    List<Employee> expectedResult2 =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Employee> query2 = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee empCand = QEmployee.candidate();
    query2.filter(empCand.team.isEmpty());

    QueryElementHolder<Employee> holder2 =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "team.isEmpty()",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query2,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder2, expectedResult2);
    executeSingleStringQuery(ASSERTION_FAILED, holder2, expectedResult2);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder2, expectedResult2);
  }

  /** */
  @SuppressWarnings("unchecked")
  public void testSize() {
    // size
    List<Department> expectedResult =
        getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    query.filter(cand.employees.size().eq(3));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "employees.size() == 3",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);
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
