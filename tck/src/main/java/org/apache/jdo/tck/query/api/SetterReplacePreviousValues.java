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

import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Setter replace previous values. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.15. <br>
 * <B>Assertion Description: </B> All of these methods replace the previously set query element, by
 * the parameter. [The methods are not additive].
 */
public class SetterReplacePreviousValues extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.15 (SetterReplacePreviousValues) failed: ";

  /**
   * The array of valid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] VALID_QUERIES = {
    // replace parameter declaration
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Department.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "deptid == param",
        /*VARIABLES*/ null,
        /*PARAMETERS*/ "String x",
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // replace filter setting
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Employee.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "personid == 1L",
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // replace variable declaration
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Department.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "employees.contains(e) && e.personid == 1",
        /*VARIABLES*/ "Employee e1; Employee e2",
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    // replace parameter declaration
    getTransientCompanyModelInstancesAsList(Department.class, "dept1"),
    // replace filter setting
    getTransientCompanyModelInstancesAsList(Employee.class, "emp2"),
    // replace variable declaration
    getTransientCompanyModelInstancesAsList(Department.class, "dept1")
  };

  /** */
  @Test
  public void testPositive() {
    // replace parameter declaration
    int index = 0;
    Query<?> query = VALID_QUERIES[index].getAPIQuery(getPM());
    query.declareParameters("long param");
    Object[] parameters = new Object[] {Long.valueOf(1)};
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        parameters,
        expectedResult[index],
        true);
    query = VALID_QUERIES[index].getSingleStringQuery(getPM());
    query.declareParameters("long param");
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        parameters,
        expectedResult[index],
        true);

    // replace filter setting
    index++;
    query = VALID_QUERIES[index].getAPIQuery(getPM());
    query.setFilter("personid == 2L");
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        null,
        expectedResult[index],
        true);
    query = VALID_QUERIES[index].getSingleStringQuery(getPM());
    query.setFilter("personid == 2L");
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        null,
        expectedResult[index],
        true);

    // replace variable declaration
    index++;
    query = VALID_QUERIES[index].getAPIQuery(getPM());
    query.declareVariables("Employee e");
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        null,
        expectedResult[index],
        true);
    query = VALID_QUERIES[index].getSingleStringQuery(getPM());
    query.declareVariables("Employee e");
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        query,
        VALID_QUERIES[index].toString(),
        false,
        null,
        expectedResult[index],
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
