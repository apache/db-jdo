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

package org.apache.jdo.tck.transactions;

import javax.jdo.Constants;
import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Set isolation level of transaction <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-xxxx, A11.1-xxxx. <br>
 * <B>Assertion Description: </B> Transaction.getIsolationLevel() returns the value of the isolation
 * level. Transaction.setIsolationLevel(String) sets the value of the isolation level.
 */
public class SetIsolationLevel extends JDO_Test implements Constants {

  /** */
  private static final String ASSERTION_29_FAILED =
      "Assertion A13.4.2-29 (setIsolationLevel) failed: ";
  /** */
  private static final String ASSERTION_25_FAILED =
      "Assertion A13.4.2-25 (setIsolationLevel) failed: ";

  /** All specified isolation levels */
  private static final String[] isolationLevels =
      new String[] {
        Constants.TX_READ_UNCOMMITTED,
        Constants.TX_READ_COMMITTED,
        Constants.TX_REPEATABLE_READ,
        Constants.TX_SNAPSHOT,
        Constants.TX_SERIALIZABLE
      };

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetIsolationLevel.class);
  }

  /** */
  @Override
  protected void localSetUp() {
    pm = getPM();
  }

  /** Set IsolationLevel to all values. */
  public void testSetIsolationLevelOutsideTransaction() {
    // iterate through all possible IsolationLevels
    for (String isolationLevel : isolationLevels) {
      setIsolationLevel(isolationLevel);
    }
    closePMF(pmf);
    failOnError();
  }

  /** Set IsolationLevel to all values within datastore transaction. */
  public void testSetIsolationLevelWithActiveDataStoreTransaction() {
    pm.currentTransaction().setOptimistic(false);
    pm.currentTransaction().begin();
    // iterate through all possible IsolationLevels
    for (String isolationLevel : isolationLevels) {
      try {
        pm.currentTransaction().setIsolationLevel(isolationLevel);
        appendMessage(
            ASSERTION_25_FAILED
                + "active datastore transaction; no exception thrown for "
                + "setIsolationLevel("
                + isolationLevel
                + ").");
      } catch (JDOUnsupportedOptionException ex) {
        // good catch
      } catch (Throwable t) {
        appendMessage(
            ASSERTION_25_FAILED
                + "active datastore transaction; "
                + "JDOUnsupportedOptionException not thrown for "
                + "setIsolationLevel("
                + isolationLevel
                + "). Unexpected exception: \n"
                + t);
      }
    }
    pm.currentTransaction().commit();
    closePMF(pmf);
    failOnError();
  }

  /** Set IsolationLevel to all values within optimistic transaction. */
  public void testSetIsolationLevelWithActiveOptimisticTransaction() {
    if (!isOptimisticSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          "testSetIsolationLevelWithActiveOptimisticTransaction", "Optimistic");
      return;
    }
    pm.currentTransaction().setOptimistic(true);
    pm.currentTransaction().begin();
    // iterate through all possible IsolationLevels
    for (String isolationLevel : isolationLevels) {
      setIsolationLevel(isolationLevel);
    }
    pm.currentTransaction().commit();
    closePMF(pmf);
    failOnError();
  }

  /** */
  private void setIsolationLevel(String level) {
    Transaction tx = pm.currentTransaction();
    String property = Constants.PROPERTY_TRANSACTION_ISOLATION_LEVEL + "." + level;
    if (isSupported(property)) {
      tx.setIsolationLevel(level);
      String actual = tx.getIsolationLevel();
      if (!validLevelSubstitution(level, actual)) {
        appendMessage(
            ASSERTION_29_FAILED
                + "\nIsolationLevel set to "
                + level
                + "; value returned by Transaction is "
                + actual);
      }
    } else {
      try {
        tx.setIsolationLevel(level);
        // no exception thrown; bad
        appendMessage(ASSERTION_29_FAILED + "\nThe expected JDOUserException was not thrown.");
      } catch (JDOUnsupportedOptionException ex) {
        // good catch
        return;
      } catch (Throwable t) {
        // wrong exception thrown; bad
        appendMessage(
            ASSERTION_29_FAILED
                + "active datastore transaction; "
                + "JDOUnsupportedOptionException not thrown for "
                + "setIsolationLevel("
                + level
                + "). Unexpected exception: \n"
                + t);
        return;
      }
    }
  }
}
