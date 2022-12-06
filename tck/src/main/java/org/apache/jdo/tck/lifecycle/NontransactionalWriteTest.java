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

package org.apache.jdo.tck.lifecycle;

import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;

/**
 * <B>Title:</B> Test NontransactionalWrite abstract base class <br>
 * <B>Keywords:</B> NontransactionalWrite <br>
 * <B>Assertion ID:</B> <br>
 * <B>Assertion Description: </B>
 */
public abstract class NontransactionalWriteTest extends JDO_Test {

  /** The ObjectId of the pc instance, set by method createAndModifyInstance. */
  protected Object oid;

  /**
   * The original value of the X field of the pc instance, set by method createAndModifyInstance.
   */
  protected static final int ORIGINAL_XVALUE = 100;

  protected static final int NEW_XVALUE = 999;
  protected static final int CONFLICT_XVALUE = 555;

  /**
   * Create a new VersionedPCPoint instance,modify its X value, and store its oid in the test class
   * oid. The persistence manager referenced by the pm field (in the superclass) manages the
   * nontransactional dirty instance. This method is only executed if the JDO implementation
   * supports the required features, otherwise this method would be localSetUp.
   *
   * @return PCPoint instance
   */
  protected VersionedPCPoint createAndModifyVersionedPCPoint() {
    addTearDownClass(VersionedPCPoint.class);
    getPM().currentTransaction().begin();
    pm.currentTransaction().setNontransactionalWrite(true);
    pm.currentTransaction().setNontransactionalRead(true);
    pm.currentTransaction().setRetainValues(true);
    VersionedPCPoint instance = new VersionedPCPoint(ORIGINAL_XVALUE, 200);
    pm.makePersistent(instance);
    oid = pm.getObjectId(instance);
    pm.currentTransaction().commit();
    instance.setX(NEW_XVALUE);
    return instance;
  }

  /**
   * Begin and rollback a transaction using the persistence manager that manages the
   * nontransactional dirty instance.
   *
   * @param optimistic use optimistic transaction
   */
  protected void beginAndRollbackTransaction(boolean optimistic) {
    getPM().currentTransaction().setOptimistic(optimistic);
    pm.currentTransaction().begin();
    pm.currentTransaction().rollback();
  }

  /**
   * Begin and commit a transaction using the persistence manager that manages the nontransactional
   * dirty instance.
   *
   * @param optimistic use optimistic transaction
   */
  protected void beginAndCommitTransaction(boolean optimistic) {
    getPM().currentTransaction().setOptimistic(optimistic);
    pm.currentTransaction().begin();
    pm.currentTransaction().commit();
  }

  /**
   * Begin and commit a transaction using the persistence manager that manages the nontransactional
   * dirty instance. This transaction must fail due to a conflicting update.
   *
   * @param location location for error message
   * @param optimistic use optimistic transaction
   */
  protected void beginAndCommitTransactionFails(String location, boolean optimistic) {
    getPM().currentTransaction().setOptimistic(optimistic);
    pm.currentTransaction().begin();
    try {
      pm.currentTransaction().commit();
    } catch (JDOOptimisticVerificationException ex) {
      // good catch; return
      return;
    }
    appendMessage(location + "transaction succeeded but" + " should not succeed.");
  }

  /**
   * Check the x value of the persistent instance referenced by the oid. Use a new persistence
   * manager and check the value in a new transaction so there is no interference with the
   * persistence manager that managed the nontransactional dirty instance.
   *
   * @param location location for error message
   * @param expectedXValue expected value
   */
  protected void checkXValue(String location, int expectedXValue) {
    PersistenceManager pmCheck = pmf.getPersistenceManager();
    try {
      pmCheck.currentTransaction().begin();
      VersionedPCPoint instance = (VersionedPCPoint) pmCheck.getObjectById(oid, true);
      int actualXValue = instance.getX();
      pmCheck.currentTransaction().commit();
      if (expectedXValue != actualXValue) {
        appendMessage(
            location + NL + "expected: " + expectedXValue + NL + "  actual: " + actualXValue);
      }
    } finally {
      cleanupPM(pmCheck);
    }
  }

  /**
   * Perform a conflicting transaction that updates the same field as the nontransactional dirty
   * instance. The field value is conflictXValue. This conflicting transaction uses a new
   * persistence manager.
   */
  protected void conflictingUpdate() {
    PersistenceManager pmConflict = pmf.getPersistenceManager();
    try {
      pmConflict.currentTransaction().setOptimistic(false);
      pmConflict.currentTransaction().begin();
      VersionedPCPoint instance = (VersionedPCPoint) pmConflict.getObjectById(oid);
      instance.setX(CONFLICT_XVALUE);
      pmConflict.currentTransaction().commit();
    } finally {
      cleanupPM(pmConflict);
    }
  }

  /**
   * Check that all Nontransactional features are supported.
   *
   * @param optimistic check for whether Optimistic is supported as well.
   * @return true if nontransactional supported
   */
  protected boolean checkNontransactionalFeaturesSupported(boolean optimistic) {
    if (!checkNontransactionalWriteSupported()
        || !checkNontransactionalReadSupported()
        || !checkRetainValuesSupported()) return false;
    if (optimistic) {
      if (!checkOptimisticSupported()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if NontransactionalWrite is supported, and log a debug message if it is not.
   *
   * @return true if NontransactionalWriteSupported
   */
  protected boolean checkNontransactionalWriteSupported() {
    if (!isNontransactionalWriteSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          getClass().getName(), "javax.jdo.option.NontransactionalWrite");
      return false;
    }
    return true;
  }

  /**
   * Check if NontransactionalRead is supported, and log a debug message if it is not.
   *
   * @return true NontransactionalReadSupported
   */
  protected boolean checkNontransactionalReadSupported() {
    if (!isNontransactionalReadSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          getClass().getName(), "javax.jdo.option.NontransactionalRead");
      return false;
    }
    return true;
  }

  /**
   * Check if Optimistic is supported, and log a debug message if it is not.
   *
   * @return true if optimistic is supported
   */
  protected boolean checkOptimisticSupported() {
    if (!isOptimisticSupported()) {
      printUnsupportedOptionalFeatureNotTested(getClass().getName(), "javax.jdo.option.Optimistic");
      return false;
    }
    return true;
  }

  /**
   * Check if RetainValues is supported, and log a debug message if it is not.
   *
   * @return true if retain values is supported
   */
  protected boolean checkRetainValuesSupported() {
    if (!isRetainValuesSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          getClass().getName(), "javax.jdo.option.RetainValues");
      return false;
    }
    return true;
  }
}
