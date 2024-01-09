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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.query.NamedQueriesSample;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_11 =
      "Assertion A14.5-11 (NewQuerySingleString) failed: ";

  /** */
  private static final String ASSERTION_FAILED_3 =
      "Assertion A14.5-3 (NewQueryFromExistingQueryBoundToPMFromSameVendor) failed: ";

  /** */
  private static final String ASSERTION_FAILED_2 =
      "Assertion A14.5-2 (NewQueryFromRestoredSerializedQuery) failed: ";

  /** */
  private static final String ASSERTION_FAILED_5 =
      "Assertion A14.5-5 (NewQueryWithCandidateClass) failed: ";

  /** */
  private static final String ASSERTION_FAILED_7 =
      "Assertion A14.5-7 (NewQueryWithCandidateClassAndCollection) failed: ";

  /** */
  private static final String ASSERTION_FAILED_6 =
      "Assertion A14.5-6 (NewQueryWithCandidateClassAndExtent) failed: ";

  /** */
  private static final String ASSERTION_FAILED_8 =
      "Assertion A14.5-8 (NewQueryWithCandidateClassAndFilter) failed: ";

  /** */
  private static final String ASSERTION_FAILED_9 =
      "Assertion A14.5-9 (NewQueryWithCandidateClassCollectionFilter) failed: ";

  /** */
  private static final String ASSERTION_FAILED_1a =
      "Assertion A14.5-1 (NewQueryWithExtent) failed: ";

  /** */
  private static final String ASSERTION_FAILED_1b =
      "Assertion A14.5-1 (NewQueryWithExtentAndFilter) failed: ";

  /** */
  private static final String ASSERTION_FAILED_4 =
      "Assertion A14.5-4 (NewQueryWithSpecifiedLanguageAndQuery) failed: ";

  /** */
  private static final String ASSERTION_FAILED_14 =
      "Assertion A14.5-14 (NamedQueryNotFound) failed: ";

  private static final String ASSERTION_FAILED =
      "Assertion A19.?? (NamedQueryRepeatableAnnotations) failed: ";

  /** */
  private static final String ASSERTION_FAILED_16 = "Assertion A14.5-16 (InvalidNamedQuery) failed: ";

  /**
   * The array of valid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] VALID_QUERIES = {
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
        /*TO*/ null)
  };

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5"),
    Arrays.asList(
        new Object[] {
          new FullName("emp1First", "emp1Last"),
          new FullName("emp2First", "emp2Last"),
          new FullName("emp3First", "emp3Last"),
          new FullName("emp4First", "emp4Last"),
          new FullName("emp5First", "emp5Last")
        }),
    new FullName("emp1First", "emp1Last")
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryFromExistingQueryBoundToPMFromSameVendor01() {
    if (debug)
      logger.debug("\nExecuting test NewQueryFromExistingQueryBoundToPMFromSameVendor01()...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);

      Query<PCPoint> query1 = pm.newQuery(query);
      query1.compile();

      Object results = query1.execute();
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_3, results, expected);
      if (debug)
        logger.debug("Test NewQueryFromExistingQueryBoundToPMFromSameVendor01() - Passed\n");
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryFromExistingQueryBoundToPMFromSameVendor02() {
    if (debug)
      logger.debug("\nExecuting test NewQueryFromExistingQueryBoundToPMFromSameVendor02()...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      Query<PCPoint> query1 = pm.newQuery(query);
      query1.setCandidates(pm.getExtent(PCPoint.class, true));
      query1.compile();

      Object results = query1.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_3, results, expected);
      if (debug)
        logger.debug("Test NewQueryFromExistingQueryBoundToPMFromSameVendor02() - Passed\n");
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryFromRestoredSerializedQuery() throws IOException, ClassNotFoundException {
    PersistenceManager pm = getPMF().getPersistenceManager();
    if (debug) logger.debug("\nExecuting test NewQueryFromRestoredSerializedQuery01() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setFilter("x == 3");
      query.compile();

      ObjectOutputStream oos = null;
      try {
        if (debug) logger.debug("Attempting to serialize Query object.");
        oos = new ObjectOutputStream(new FileOutputStream(SERIALZED_QUERY));
        oos.writeObject(query);
        if (debug) logger.debug("Query object serialized.");
      } finally {
        if (oos != null) {
          try {
            oos.flush();
          } catch (Exception ignored) {
          }
          try {
            oos.close();
          } catch (Exception ignored) {
          }
        }
      }

      ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
      Query<PCPoint> query1 = (Query<PCPoint>) in.readObject();

      // init and execute query
      query = pm.newQuery(query1);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(3));
      PCPoint p4 = new PCPoint(3, 3);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_2, "x == 3", results, expected);
      if (debug) logger.debug("Test NewQueryFromRestoredSerializedQuery01(): Passed");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithCandidateClass() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      Object results = query.execute();

      // check query result
      printOutput(results, transientPCPoints);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_5, results, transientPCPoints);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithCandidateClassAndCollection() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PCPoint> candidateCollection = pm.newQuery(PCPoint.class).executeList();

      Query<PCPoint> query = pm.newQuery(PCPoint.class, candidateCollection);
      Object results = query.execute();

      // check query result
      printOutput(results, transientPCPoints);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_7, results, transientPCPoints);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithCandidateClassAndExtent() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      Extent<PCPoint> extent = pm.getExtent(PCPoint.class, true);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(extent);
      Object results = query.execute();

      // check query result
      printOutput(results, transientPCPoints);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_6, results, transientPCPoints);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithCandidateClassAndFilter() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class, "x == 1");
      query.setClass(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(1));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_8, "x == 1", results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithCandidateClassCollectionFilter() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PCPoint> candidateCollection = pm.newQuery(PCPoint.class).executeList();
      Query<PCPoint> query = pm.newQuery(PCPoint.class, candidateCollection, "x ==2");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_9, "x ==2", results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithExtent() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(pm.getExtent(PCPoint.class, false));
      query.setClass(PCPoint.class);
      Object results = query.execute();

      // check query result
      printOutput(results, transientPCPoints);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_1a, results, transientPCPoints);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithExtentAndFilter() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      Query<PCPoint> query = pm.newQuery(pm.getExtent(PCPoint.class, false), "x == 1");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(1));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_1b, "x == 1", results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQueryWithSpecifiedLanguageAndQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    if (debug) logger.debug("\nExecuting test NewQueryWithSpecifiedLanguageAndQuery()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      Query<PCPoint> query1 = pm.newQuery("javax.jdo.query.JDOQL", query);
      query1.setCandidates(pm.getExtent(PCPoint.class, false));
      query1.compile();
      Object results = query1.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_4, results, expected);
      if (debug) logger.debug("Test NewQueryWithSpecifiedLanguageAndQuery() - Passed\n");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewQuerySingleString() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (int i = 0; i < VALID_QUERIES.length; i++) {
        executeSingleStringQuery(ASSERTION_FAILED_11, pm, VALID_QUERIES[i], expectedResult[i]);
      }
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNamedQueryNotFound() {
    try {
      getPM().newNamedQuery(Person.class, "nonExistingNamedQuery");
      fail(
          ASSERTION_FAILED_14
              + "The lookup of named query 'nonExistingNamedQuery' "
              + "is successful, though that named query is undefined.");
    } catch (JDOUserException ignored) {
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNamedQueryRepeatableAnnotations() {
    try {
      getPM().newNamedQuery(NamedQueriesSample.class, "NameIsJohn");
    } catch (JDOUserException e) {
      fail(ASSERTION_FAILED + "Lookup of query failed but should have been found");
    }
    try {
      getPM().newNamedQuery(NamedQueriesSample.class, "NameIsFred");
    } catch (JDOUserException e) {
      fail(ASSERTION_FAILED + "Lookup of query failed but should have been found");
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewNamedQuery0() {
    int index = 1;
    executeNamedQuery(Person.class, "validNotUnique", expectedResult[index], true);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewNamedQuery1() {
    int index = 2;
    executeNamedQuery(Person.class, "validUnique", expectedResult[index], true);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNewNamedQueryNegative() {
    executeNamedQuery(Person.class, "invalidUnique", null, false);
  }

  private void executeNamedQuery(
      Class<?> candidateClass, String namedQuery, Object expectedResult, boolean positive) {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<?> query = pm.newNamedQuery(candidateClass, namedQuery);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          query,
          "Named query " + namedQuery,
          false,
          null,
          expectedResult,
          positive);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testInvalidNamedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      pm.newNamedQuery(Person.class, "invalidQuery");
      fail(
              ASSERTION_FAILED
                      + "Lookup of named query 'invalidQuery' "
                      + " succeeded, though the query is not compilable.");
    } catch (JDOUserException ignored) {
      // ignored
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
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
  }
}
