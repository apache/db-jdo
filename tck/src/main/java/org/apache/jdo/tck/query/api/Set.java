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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.PCPoint;
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
 * <B>Title:</B> Set Candidate Collection <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-5. <br>
 * <B>Assertion Description: </B> <code>Query.setCandidates(Collection
 * candidateCollection)</code> binds the candidate <code>Collection</code> to the query instance.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Set extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_5 =
      "Assertion A14.6-5 (SetCandidateCollection) failed: ";

  /** */
  private static final String ASSERTION_FAILED_6 =
      "Assertion A14.6-6 (SetCandidateExtent) failed: ";

  /** */
  private static final String ASSERTION_FAILED =
          "Assertion A14.6.15 (SetterReplacePreviousValues) failed: ";

  /** */
  private static final String ASSERTION_FAILED_7 = "Assertion A14.6-7 (SetFilter) failed: ";

  /** */
  private static final String ASSERTION_FAILED_17 = "Assertion A14.6-17 (SetGrouping) failed: ";

  /** */
  private static final String ASSERTION_FAILED_12 = "Assertion A14.6-12 (SetIgnoreCache) failed: ";

  /** */
  private static final String ASSERTION_FAILED_11 = "Assertion A14.6-11 (SetOrdering) failed: ";

  /** */
  private static final String ASSERTION_FAILED_20 = "Assertion A14.6-20 (SetRange) failed: ";

  /** */
  private static final String ASSERTION_FAILED_16 = "Assertion A14.6-16 (SetResult) failed: ";

  /** */
  private static final String ASSERTION_FAILED_19 = "Assertion A14.6-19 (SetResultClass) failed: ";

  /** */
  private static final String ASSERTION_FAILED_18 = "Assertion A14.6-18 (SetUnique) failed: ";

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
  private final Object[] expectedResultValidQueries = {
          // replace parameter declaration
          getTransientCompanyModelInstancesAsList(Department.class, "dept1"),
          // replace filter setting
          getTransientCompanyModelInstancesAsList(Employee.class, "emp2"),
          // replace variable declaration
          getTransientCompanyModelInstancesAsList(Department.class, "dept1")
  };


  private final Object[] expectedResult = {
    Arrays.asList("emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5"),
    Arrays.asList("emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last"),
    Arrays.asList(
        new Object[] {
          new FullName("emp1First", "emp1Last"),
          new FullName("emp2First", "emp2Last"),
          new FullName("emp3First", "emp3Last"),
          new FullName("emp4First", "emp4Last"),
          new FullName("emp5First", "emp5Last")
        }),
    getTransientCompanyModelInstance(Employee.class, "emp1"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5")
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetCandidateCollection() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    if (debug) logger.debug("\nExecuting test SetCandidateCollection()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PCPoint> candidateCollection = pm.newQuery(PCPoint.class).executeList();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(candidateCollection);
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_5, results, expected);
      if (debug) logger.debug("Test SetCandidateCollection: Passed");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetCandidateExtent() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    if (debug) logger.debug("\nExecuting test SetCandidateExtent()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_6, results, expected);
      if (debug) logger.debug("Test SetCandidateExtent: Passed");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetFilter() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Class<PCPoint> clazz = PCPoint.class;
    try {
      Extent<PCPoint> extent = pm.getExtent(clazz, true);
      tx.begin();
      Query<PCPoint> query = pm.newQuery(clazz);
      query.setClass(clazz);
      query.setCandidates(extent);
      query.setFilter("x == 2");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_7, "x == 2", results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetGrouping() {
    int index = 0;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setResult("lastname");
      query.setGrouping("lastname");
      String singleStringQuery = "SELECT lastname FROM Person GROUP BY lastname";
      executeJDOQuery(
          ASSERTION_FAILED_17,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestSetIgnoreCache01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    tx.setOptimistic(false);
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      if (debug) logger.debug("Pessimistic: IgnoreCache - Setting value = true");
      query.setIgnoreCache(true);
      if (query.getIgnoreCache()) {
        if (debug) logger.debug("Pessimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED_12,
            "query.getIgnoreCache() returns false after setting the flag to true");
      }

      if (debug) logger.debug("Pessimistic: IgnoreCache - Setting value = false");
      query.setIgnoreCache(false);
      if (!query.getIgnoreCache()) {
        if (debug) logger.debug("Pessimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED_12,
            "query.getIgnoreCache() returns true after setting the flag to false");
      }
      query.compile();

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestSetIgnoreCache02() {
    if (!isOptimisticSupported()) {
      if (debug) logger.debug("Optimistic tx not supported");
      return;
    }

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.setOptimistic(true);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      if (debug) logger.debug("Optimistic: IgnoreCache - Setting value = true");
      query.setIgnoreCache(true);
      if (query.getIgnoreCache()) {
        if (debug) logger.debug("Optimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED_12,
            "query.getIgnoreCache() returns false after setting the flag to true");
      }

      if (debug) logger.debug("Optimistic: IgnoreCache - Setting value = false");
      query.setIgnoreCache(false);
      if (!query.getIgnoreCache()) {
        if (debug) logger.debug("Optimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED_12,
            "query.getIgnoreCache() returns true after setting the flag to false");
      }

      query.compile();

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetOrderingAscending() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      // ascending
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x ascending");
      Object results = query.execute();

      // check result
      printOutput(results, transientPCPoints);
      checkQueryResultWithOrder(ASSERTION_FAILED_11, results, transientPCPoints);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetOrderingDescending() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      // descending
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x descending");
      Object results = query.execute();

      // check result
      List<PCPoint> expected = new ArrayList<>(transientPCPoints);
      Collections.reverse(expected);
      printOutput(results, expected);
      checkQueryResultWithOrder(ASSERTION_FAILED_11, results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetRange() {
    int index = 1;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setRange(0, 5);
      String singleStringQuery = "SELECT FROM Person RANGE 0, 5";
      executeJDOQuery(
          ASSERTION_FAILED_20,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetResultPositive() {
    int index = 2;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setResult("lastname");
      String singleStringQuery = "SELECT lastname FROM Person";
      executeJDOQuery(
          ASSERTION_FAILED_16,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetResultNegative() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setResult("noname");
      query.compile();
      fail(
          ASSERTION_FAILED_16
              + "Compilation for query "
              + "'SELECT noname FROM Person' "
              + "succeeded, though the result clause is invalid.");
    } catch (JDOUserException ignored) {

    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetResultClass() {
    int index = 3;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setResultClass(FullName.class);
      query.setResult("firstname, lastname");
      String singleStringQuery = "SELECT firstname, lastname INTO FullName FROM Person";
      executeJDOQuery(
          ASSERTION_FAILED_19,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetUnique01() {
    int index = 4;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setUnique(true);
      query.setFilter("lastname == 'emp1Last'");
      String singleStringQuery = "SELECT FROM Person WHERE lastname == 'emp1Last'";
      executeJDOQuery(
          ASSERTION_FAILED_18,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetUnique02() {
    int index = 5;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setUnique(false);
      String singleStringQuery = "SELECT FROM Person";
      executeJDOQuery(
          ASSERTION_FAILED_18,
          pm,
          query,
          singleStringQuery,
          false,
          null,
          expectedResult[index],
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetterReplacePreviousValues() {
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
            expectedResultValidQueries[index],
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
            expectedResultValidQueries[index],
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
            expectedResultValidQueries[index],
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
            expectedResultValidQueries[index],
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
            expectedResultValidQueries[index],
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
            expectedResultValidQueries[index],
            true);
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
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }
}
