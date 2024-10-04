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
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Test using methods in the result expression <br>
 * <B>Keywords:</B> query result <br>
 * <B>Assertion ID:</B> A14.6.9-5. <br>
 * <B>Assertion Description: </B> The result expressions include: method expression: the value of an
 * expression calling any of the methods allowed in queries applied to fields is returned.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MethodsInResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.9-5 (MethodsInResult) failed: ";

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCollectionSizeInResult() {
    // collection.size()
    Object expected = Arrays.asList(Integer.valueOf(3), Integer.valueOf(2));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      query.result(false, cand.employees.size());
      query.orderBy(cand.name.asc());

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this.employees.size()",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.name ascending",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMapSizeInResult() {
    // map.size()
    Object expected =
        Arrays.asList(
            Integer.valueOf(2),
            Integer.valueOf(2),
            Integer.valueOf(2),
            Integer.valueOf(2),
            Integer.valueOf(2));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      query.result(false, cand.phoneNumbers.size());
      query.orderBy(cand.lastname.asc(), cand.firstname.asc());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this.phoneNumbers.size()",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lastname ascending, this.firstname ascending",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMaxAndSizeInResult() {
    // MAX(collection.size())
    Object expected = Integer.valueOf(3);
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Department> query = pm.newJDOQLTypedQuery(Department.class);
      QDepartment cand = QDepartment.candidate("this");
      query.result(false, cand.employees.size().max());

      QueryElementHolder<Department> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ "MAX(this.employees.size())",
              /*INTO*/ null,
              /*FROM*/ Department.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
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
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMapGetInResult() {
    // map.get()
    Object expected = Arrays.asList("1111", "2222", "3333", "3343", "3363");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      query.result(false, cand.phoneNumbers.get("home"));
      query.orderBy(cand.lastname.asc(), cand.firstname.asc());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this.phoneNumbers.get('home')",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lastname ascending, this.firstname ascending",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSubstringInResult() {
    // String.substring()
    Object expected = Arrays.asList("mp1", "mp2", "mp3", "mp4", "mp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      query.result(false, cand.firstname.substring(1, 4));
      query.orderBy(cand.lastname.asc(), cand.firstname.asc());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this.firstname.substring(1,4)",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lastname ascending, this.firstname ascending",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testIndexOfInResult() {
    // String.indexOf()
    Object expected =
        Arrays.asList(
            Integer.valueOf(4),
            Integer.valueOf(4),
            Integer.valueOf(4),
            Integer.valueOf(4),
            Integer.valueOf(4));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      query.result(false, cand.firstname.indexOf("First"));
      query.orderBy(cand.lastname.asc(), cand.firstname.asc());

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this.firstname.indexOf('First')",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lastname ascending, this.firstname ascending",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
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
