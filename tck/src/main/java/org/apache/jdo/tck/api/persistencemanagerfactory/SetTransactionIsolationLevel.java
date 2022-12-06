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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.HashMap;
import java.util.Map;
import javax.jdo.Constants;
import javax.jdo.JDOHelper;
import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Set transaction isolation level of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-xxxx, A11.1-xxxx. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.getTransactionIsolationLevel() returns
 * the value of the transaction isolation level.
 * PersistenceManagerFactory.setTransactionIsolationLevel(String) sets the value of the transaction
 * isolation level.
 */
public class SetTransactionIsolationLevel extends JDO_Test implements Constants {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.1-1, A11.1-2 (setTransactionIsolationLevel) failed: ";

  /** All specified transaction isolation levels */
  private static final String[] transactionIsolationLevels =
      new String[] {
        TX_READ_UNCOMMITTED, TX_READ_COMMITTED, TX_REPEATABLE_READ, TX_SNAPSHOT, TX_SERIALIZABLE
      };

  private PersistenceManagerFactory pmf2;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetTransactionIsolationLevel.class);
  }

  /** */
  @Override
  protected void localSetUp() {
    // setUp gets a PMF that needs to be closed
    closePMF();
  }

  /** Set TransactionIsolationLevel to all values via API. */
  public void testSetTransactionIsolationLevelByAPI() {
    // iterate through all possible TransactionIsolationLevels
    for (String transactionIsolationLevel : transactionIsolationLevels) {
      setTransactionIsolationLevelByAPI(transactionIsolationLevel);
    }
    failOnError();
  }

  /** Set TransactionIsolationLevel to all values from properties. */
  public void testSetTransactionIsolationLevelFromProperties() {
    // iterate through all possible TransactionIsolationLevels
    for (String transactionIsolationLevel : transactionIsolationLevels) {
      getPMFsetTransactionIsolationLevelFromProperties(transactionIsolationLevel);
    }
    failOnError();
  }

  /** Set TransactionIsolationLevel to all values. */
  public void testTransactionIsolationLevelReadCommittedSupported() {
    // this test depends on setUp initializing supportedOptions
    String readCommitted = PROPERTY_TRANSACTION_ISOLATION_LEVEL_READ_COMMITTED;
    // make sure read committed is supported
    if (!isSupported(readCommitted)) {
      appendMessage(
          ASSERTION_FAILED + "\nSupportedOptions does not include " + readCommitted + ".");
    }
    failOnError();
  }

  /** */
  private void setTransactionIsolationLevelByAPI(String level) {
    pmf = getUnconfiguredPMF();
    // Set datastore details so the implementation knows what is supported
    pmf.setConnectionURL(getPMFProperty(CONNECTION_URL_PROP));
    pmf.setConnectionUserName(getPMFProperty(CONNECTION_USERNAME_PROP));
    pmf.setConnectionPassword(getPMFProperty(CONNECTION_PASSWORD_PROP));
    pmf.setConnectionDriverName(getPMFProperty(PROPERTY_CONNECTION_DRIVER_NAME));

    String property = PROPERTY_TRANSACTION_ISOLATION_LEVEL + "." + level;
    try {
      pmf.setTransactionIsolationLevel(level);

      // Get first PM so the PMF is frozen
      pmf.getPersistenceManager();
      if (!isSupported(property)) {
        appendMessage(
            ASSERTION_FAILED
                + "\nCreated PersistenceManager for isolation level "
                + level
                + " yet the PMF says that this level is not supported!\n");
      }
    } catch (JDOUnsupportedOptionException ex) {
      // not supported, so check with the PMF if this should be supported
      if (isSupported(property)) {
        appendMessage(
            ASSERTION_FAILED
                + "\nReceived JDOUnsupportedOptionException on creating the "
                + "first PersistenceManager yet the PMF says that isolation level "
                + "is supported!\n");
      }
    } catch (Throwable t) {
      appendMessage(
          ASSERTION_FAILED
              + "\nThe expected JDOUnsupportedOptionException was not "
              + "thrown for unsupported isolation level "
              + level
              + " but unexpected exception:\n"
              + t);
    }

    closePMF();
    return;
  }
  /** */
  private void getPMFsetTransactionIsolationLevelFromProperties(String level) {
    String property = PROPERTY_TRANSACTION_ISOLATION_LEVEL + "." + level;
    Map<Object, Object> modifiedProps = new HashMap<>(PMFPropertiesObject);
    modifiedProps.put(PROPERTY_TRANSACTION_ISOLATION_LEVEL, level);
    if (isSupported(property)) {
      pmf2 = JDOHelper.getPersistenceManagerFactory(modifiedProps);
      String actual = pmf2.getTransactionIsolationLevel();
      if (!validLevelSubstitution(level, actual)) {
        appendMessage(
            ASSERTION_FAILED
                + "\nTransactionIsolationLevel set to "
                + level
                + "; value returned by PMF is "
                + actual);
      }
      PersistenceManager pm2 = pmf2.getPersistenceManager();
      actual = pm2.currentTransaction().getIsolationLevel();
      if (!validLevelSubstitution(level, actual)) {
        appendMessage(
            ASSERTION_FAILED
                + "\nTransactionIsolationLevel set to "
                + level
                + "; value returned by Transaction is "
                + actual);
      }
    } else {
      try {
        pmf2 = JDOHelper.getPersistenceManagerFactory(modifiedProps);
        // no exception thrown; bad
        appendMessage(
            ASSERTION_FAILED
                + "\nThe expected JDOUserException was not thrown for "
                + "JDOHelper.getPersistenceManagerFactory "
                + "for unsupported isolation level "
                + level
                + ".");
      } catch (JDOUnsupportedOptionException ex) {
        // good catch
      } catch (Throwable t) {
        appendMessage(
            ASSERTION_FAILED
                + "\nThe expected JDOUserException was not thrown for "
                + "JDOHelper.getPersistenceManagerFactory "
                + "for unsupported isolation level "
                + level
                + "but unexpected exception:\n"
                + t);
      }
    }
    closePMF(pmf2);
    return;
  }
}
