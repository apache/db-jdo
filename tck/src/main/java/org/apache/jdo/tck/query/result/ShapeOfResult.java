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
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Shape of Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.12-2. <br>
 * <B>Assertion Description: </B> Table 6: Shape of Result (C is the candidate class)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShapeOfResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.12-2 (ShapeOfResult) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNoResult() {
    // result: null
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
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
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testThisAsC() {
    // result: this AS C
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.result(false, cand);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "this AS Person",
              /*INTO*/ null,
              /*FROM*/ Person.class,
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
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNoResultUnique() {
    // result: null, unique: true
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testThisAsCUnique() {
    // result: this AS C, unique: true
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));
      query.result(false, cand);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ "this AS Person",
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSingleExpression() {
    // result: expression of type T
    Object expected =
        Arrays.asList("emp1First", "emp2First", "emp3First", "emp4First", "emp5First");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.result(false, cand.firstname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "firstname",
              /*INTO*/ null,
              /*FROM*/ Person.class,
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSingleExpressionUnique() {
    // result: expression of type T, unique: true
    Object expected = "emp1First";
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));
      query.result(false, cand.firstname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ "firstname",
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleExpressions() {
    // result: multiple expressions of type T
    Object expected =
        Arrays.asList(
            new Object[] {"emp1First", "emp1Last"},
            new Object[] {"emp2First", "emp2Last"},
            new Object[] {"emp3First", "emp3Last"},
            new Object[] {"emp4First", "emp4Last"},
            new Object[] {"emp5First", "emp5Last"});
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.result(false, cand.firstname, cand.lastname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "firstname, lastname",
              /*INTO*/ null,
              /*FROM*/ Person.class,
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleExpressionsUnique() {
    // result: multiple expressions of type T, unique: true
    Object expected = new Object[] {"emp1First", "emp1Last"};
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));
      query.result(false, cand.firstname, cand.lastname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ "firstname, lastname",
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
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

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleExpressionsResultClass() {
    // result: multiple expressions of type T, result class
    Object expected =
        Arrays.asList(
            new FullName("emp1First", "emp1Last"),
            new FullName("emp2First", "emp2Last"),
            new FullName("emp3First", "emp3Last"),
            new FullName("emp4First", "emp4Last"),
            new FullName("emp5First", "emp5Last"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      // JDOQLTypedQuery API
      query.result(false, cand.firstname, cand.lastname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "firstname, lastname",
              /*INTO*/ FullName.class,
              /*FROM*/ Person.class,
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
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, FullName.class, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleExpressionResultClassUnique() {
    // result: multiple expressions of type T, result class, unique: true
    Object expected = new FullName("emp1First", "emp1Last");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));
      // JDOQLTypedQuery API
      query.result(false, cand.firstname, cand.lastname);

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ "firstname, lastname",
              /*INTO*/ FullName.class,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
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
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, FullName.class, true, expected);
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
