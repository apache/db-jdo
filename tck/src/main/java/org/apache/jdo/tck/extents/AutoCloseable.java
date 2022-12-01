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

package org.apache.jdo.tck.extents;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> AutoCloseable <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.6 ?. <br>
 * <B>Assertion Description: </B> In a non-managed environment, if the extent is created with
 * try-with-resources all results of execute(...) methods on this query instance are automatically
 * closed at the end of that block and all resources associated with it are released.
 */
public class AutoCloseable extends ExtentTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.6-? (AutoCloseable) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AutoCloseable.class);
  }

  /**
   * This methods creates an extent instance with try-with-resources and checks that an iterator for
   * the query result is not accessible after the block.
   */
  public void testTryWithResource() {

    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();

      Extent<Department> extent = null;
      Iterator<Department> iterator = null;
      try (Extent<Department> extent1 = pm.getExtent(Department.class)) {
        extent = extent1;
        iterator = extent1.iterator();
        if (!iterator.hasNext()) {
          fail(ASSERTION_FAILED, "(1) Open extent iterator should have elements.");
        }
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, "(2) Unexpected exception " + ex);
      }

      // check iterator retrieved in try-with-resource block
      if (iterator.hasNext()) {
        fail(ASSERTION_FAILED, "(3) Closed extent iterator should return false on hasNext().");
      }
      try {
        iterator.next();
        fail(
            ASSERTION_FAILED,
            "(4) Closed extent iterator should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      Iterator<Department> iterator2 = extent.iterator();
      if (!iterator2.hasNext()) {
        fail(ASSERTION_FAILED, "(5) extent should be usable and should have elements.");
      }

      tx.commit();
    } finally {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * This methods creates an extent instance with try-with-resources and checks that an iterator for
   * the query result is not accessible after the block, if the block is ended with an exception.
   */
  public void testTryWithResourceThrowingException() {

    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();

      Extent<Department> extent = null;
      Iterator<Department> iterator = null;
      try (Extent<Department> extent1 = pm.getExtent(Department.class)) {
        extent = extent1;
        iterator = extent1.iterator();
        if (!iterator.hasNext()) {
          fail(ASSERTION_FAILED, "(1) Open extent iterator should have elements.");
        }
        throw new DummyException();
      } catch (DummyException ex) {
        // expected exception
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, "(2) Unexpected exception " + ex);
      }

      // check iterator retrieved in try-with-resource block
      if (iterator.hasNext()) {
        fail(ASSERTION_FAILED, "(3) Closed extent iterator should return false on hasNext().");
      }
      try {
        iterator.next();
        fail(
            ASSERTION_FAILED,
            "(4) Closed extent iterator should throw NoSuchElementException on next().");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

      Iterator<Department> iterator2 = extent.iterator();
      if (!iterator2.hasNext()) {
        fail(ASSERTION_FAILED, "(5) extent should be usable and should have elements.");
      }

      tx.commit();
    } finally {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /** DummyException used in method testTryWithResourceThrowingException. */
  private static final class DummyException extends Exception {
    private static final long serialVersionUID = 1L;
  }
}
