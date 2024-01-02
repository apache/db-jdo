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

import java.util.Arrays;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Unmodifiable Query. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-22. <br>
 * <B>Assertion Description: </B> The Unmodifiable option, when set to true, disallows further
 * modification of the query, except for specifying the range and result class and ignoreCache
 * option.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnmodifiableQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-22 (UnmodifiableQuery) failed: ";

  private static final String SINGLE_STRING_QUERY =
      "SELECT firstname, lastname FROM org.apache.jdo.tck.pc.company.Person";

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    Arrays.asList(
        new FullName("emp1First", "emp1Last"),
        new FullName("emp2First", "emp2Last"),
        new FullName("emp3First", "emp3Last"),
        new FullName("emp4First", "emp4Last"),
        new FullName("emp5First", "emp5Last"))
  };

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive() {
    int index = 0;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(SINGLE_STRING_QUERY);
      query.setUnmodifiable();
      query.setResultClass(FullName.class);
      query.setRange(0, 5);
      query.setIgnoreCache(true);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          query,
          SINGLE_STRING_QUERY,
          false,
          null,
          expectedResult[index],
          true);

      query = pm.newNamedQuery(Person.class, "unmodifiable");
      query.setResultClass(FullName.class);
      query.setRange(0, 5);
      query.setIgnoreCache(true);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          query,
          SINGLE_STRING_QUERY,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(SINGLE_STRING_QUERY);
      query.setUnmodifiable();
      checkSetters(query);

      query = pm.newNamedQuery(Person.class, "unmodifiable");
      checkSetters(query);
    } finally {
      cleanupPM(pm);
    }
  }

  private void checkSetters(Query<?> query) {
    checkSetResult(query);
    checkSetClass(query);
    checkSetFilter(query);
    checkDeclareVariables(query);
    checkDeclareParameters(query);
    checkDeclareImports(query);
    checkSetOrdering(query);
    checkSetGrouping(query);
    checkSetOrdering(query);
  }

  private void checkSetResult(Query<?> query) {
    try {
      query.setResult("firstname, lastname");
      methodFailed("setResult()");
    } catch (JDOUserException ignored) {
    }
  }

  @SuppressWarnings("unchecked")
  private void checkSetClass(@SuppressWarnings("rawtypes") Query query) {
    try {
      query.setClass(Employee.class);
      methodFailed("setClass()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetFilter(Query<?> query) {
    try {
      query.setFilter("firstname == 'emp1First'");
      methodFailed("setFilter()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareVariables(Query<?> query) {
    try {
      query.declareVariables("Employee emp");
      methodFailed("declareVariables()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareParameters(Query<?> query) {
    try {
      query.declareParameters("Employee emp");
      methodFailed("declareParameters()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareImports(Query<?> query) {
    try {
      query.declareImports("import org.apache.jdo.tck.pc.company.Employee");
      methodFailed("declareImports()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetGrouping(Query<?> query) {
    try {
      query.setGrouping("firstname");
      methodFailed("setGrouping()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetOrdering(Query<?> query) {
    try {
      query.setOrdering("firstname ASCENDING");
      methodFailed("setOrdering()");
    } catch (JDOUserException ignored) {
    }
  }

  private void methodFailed(String method) {
    fail(ASSERTION_FAILED + method + " on an unmodifiable query must throw JDOUserException.");
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
