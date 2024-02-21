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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> AutoCloseable <br>
 * <B>Keywords:</B> query close <br>
 * <B>Assertion IDs:</B> A14.6.7-3. <br>
 * <B>Assertion Description: </B> In a non-managed environment, if the query is created with
 * try-with-resources all results of execute(...) methods on this query instance are automatically
 * closed at the end of that block and all resources associated with it are released.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CloseQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_3 = "Assertion A14.6.7-3 (AutoCloseable) failed: ";

  /** */
  private static final String ASSERTION_FAILED_1 = "Assertion A14.6.7-1 (Close) failed: ";

  /** */
  private static final String ASSERTION_FAILED_2 = "Assertion A14.6.7-2 (CloseAll) failed: ";

  /**
   * This methods creates a query instance with try-with-resources and checks that an iterator for
   * the query result is not accessible after the block.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testTryWithResource() {

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();

      Query<PCPoint> query = null;
      List<PCPoint> queryResult = null;
      Iterator<PCPoint> iterator = null;
      try (Query<PCPoint> query1 = pm.newQuery(PCPoint.class)) {
        query = query1;
        queryResult = query1.executeList();
        iterator = queryResult.iterator();
        if (!iterator.hasNext()) {
          fail(ASSERTION_FAILED_3, "(1) Iterator of open query result should have elements.");
        }
      } catch (Exception ex) {
        fail(ASSERTION_FAILED_3, "(2) Unexpected exception " + ex);
      }

      // check iterator retrieved in try-with-resource block
      if (iterator.hasNext()) {
        fail(
            ASSERTION_FAILED_3,
            "(3) Iterator of closed query result should return false on hasNext().");
      }
      try {
        PCPoint next = iterator.next();
        fail(
            ASSERTION_FAILED_3,
            "(4) Iterator of closed query result should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      // create new Iterator and check its behaviour
      Iterator<PCPoint> iterator2 = queryResult.iterator();
      if (iterator2.hasNext()) {
        fail(
            ASSERTION_FAILED_3,
            "(5) Iterator of closed query result should return false on hasNext().");
      }
      try {
        PCPoint next = iterator2.next();
        fail(
            ASSERTION_FAILED_3,
            "(6) Iterator of closed query result should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      // check query result itself
      try {
        int size = queryResult.size();
        fail(ASSERTION_FAILED_3, "(7) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }
      try {
        PCPoint elem = queryResult.get(0);
        fail(ASSERTION_FAILED_3, "(8) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }
      try {
        boolean empty = queryResult.isEmpty();
        fail(ASSERTION_FAILED_3, "(9) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }
      // Check query instance is still usable
      queryResult = query.executeList();
      if (queryResult.isEmpty()) {
        fail(
            ASSERTION_FAILED_3,
            "(10) query instance should be usable and execution should return a non empty result.");
      }

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * This methods creates a query instance with try-with-resources and checks that an iterator for
   * the query result is not accessible after the block, if the block is ended with an exception.
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testTryWithResourceThrowingException() {

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = null;
      List<PCPoint> queryResult = null;
      Iterator<PCPoint> iterator = null;
      try (Query<PCPoint> query1 = pm.newQuery(PCPoint.class)) {
        query = query1;
        queryResult = query1.executeList();
        iterator = queryResult.iterator();
        if (!iterator.hasNext()) {
          fail(ASSERTION_FAILED_3, "(1) Iterator of open query result should have elements.");
        }
        throw new DummyException();
      } catch (DummyException ex) {
        // expected exception
      } catch (Exception ex) {
        fail(ASSERTION_FAILED_3, "(2) Unexpected exception " + ex);
      }

      // check iterator retrieved in try-with-resource block
      if (iterator.hasNext()) {
        fail(
            ASSERTION_FAILED_3,
            "(3) Iterator of closed query result should return false on hasNext().");
      }
      try {
        PCPoint next = iterator.next();
        fail(
            ASSERTION_FAILED_3,
            "(4) Iterator of closed query result should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      // create new Iterator and check its behaviour
      Iterator<PCPoint> iterator2 = queryResult.iterator();
      if (iterator2.hasNext()) {
        fail(
            ASSERTION_FAILED_3,
            "(5) Iterator of closed query result should return false on hasNext().");
      }
      try {
        PCPoint next = iterator2.next();
        fail(
            ASSERTION_FAILED_3,
            "(6) Iterator of closed query result should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      // check query result itself
      try {
        int size = queryResult.size();
        fail(ASSERTION_FAILED_3, "(7) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }
      try {
        PCPoint elem = queryResult.get(0);
        fail(ASSERTION_FAILED_3, "(8) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }
      try {
        boolean empty = queryResult.isEmpty();
        fail(ASSERTION_FAILED_3, "(9) closed query result should not be accessible.");
      } catch (JDOUserException ex) {
        // expected exception when accessing closed query result
      }

      // Check query instance is still usable
      queryResult = query.executeList();
      if (queryResult.isEmpty()) {
        fail(
            ASSERTION_FAILED_3,
            "(10) query instance should be usable and execution should return a non empty result.");
      }

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testClose() {
    PersistenceManager pm = getPMF().getPersistenceManager();

    if (debug) logger.debug("\nExecuting test Close()...");
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));

      // printOutput(results);

      checkQueryResultWithoutOrder(ASSERTION_FAILED_1, results, expected);
      if (debug)
        logger.debug("Test Close: Results are as expected and accessible before query is closed");

      Iterator<PCPoint> resIterator = results.iterator();
      query.close(results);

      if (resIterator.hasNext()) {
        fail(
            ASSERTION_FAILED_1,
            "Iterator.hasNext() should return false after closing the query result.");
      }
      try {
        resIterator.next();
        fail(
            ASSERTION_FAILED_1,
            "Iterator.hasNext() should throw NoSuchElementException after closing the query result.");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCloseAll() {
    PersistenceManager pm = getPMF().getPersistenceManager();

    if (debug) logger.debug("\nExecuting test CloseAll()...");
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      List<PCPoint> results = query.executeList();
      Iterator<PCPoint> resIterator = results.iterator();

      query.closeAll();

      if (resIterator.hasNext()) {
        fail(
            ASSERTION_FAILED_2,
            "Iterator.hasNext() should return false after closing all query results.");
      }

      try {
        resIterator.next();
        fail(
            ASSERTION_FAILED_2,
            "Iterator.hasNext() should throw NoSuchElementException after closing all query results.");
      } catch (NoSuchElementException ex) {
        // expected exception
      }
      tx.commit();
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
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
  }

  /** DummyException used in method testTryWithResourceThrowingException. */
  private static final class DummyException extends Exception {
    private static final long serialVersionUID = 1L;
  }
}
