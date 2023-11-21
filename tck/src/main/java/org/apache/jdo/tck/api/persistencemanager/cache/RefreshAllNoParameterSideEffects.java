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

package org.apache.jdo.tck.api.persistencemanager.cache;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Refresh All No Parameters Side Effects <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion ID:</B> A12.5.1-5B <br>
 * <B>Assertion Description: </B> The refreshAll() updates the values in the parameter instance[s]
 * from the data in the data store. ((The intended use is for optimistic transactions where the
 * state of the JDO Instance is not guaranteed to reflect the state in the data store. This method
 * can be used to minimize the occurrence of commit failures due to mismatch between the state of
 * cached instances and the state of data in the data store.)) This can be tested by using 2
 * PersistenceManagers, independently change an object, then refresh.
 */
public class RefreshAllNoParameterSideEffects extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.1-5B (RefreshAllNoParameterSideEffects) failed: ";

  Object oid = null;

  PersistenceManager pm1;
  PersistenceManager pm2;
  PersistenceManager pmVerify;

  /** */
  @Override
  public void localSetUp() {
    addTearDownClass(VersionedPCPoint.class);
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    VersionedPCPoint pnt = new VersionedPCPoint(0, 0);
    pm.makePersistent(pnt);
    oid = pm.getObjectId(pnt);
    tx.commit();
  }

  /** */
  @Test
  public void test() {
    if (!isOptimisticSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          "RefreshAllNoParameterSideEffect", "javax.jdo.option.Optimistic");
      return;
    }

    pm1 = pmf.getPersistenceManager();
    pm2 = pmf.getPersistenceManager();
    pmVerify = pmf.getPersistenceManager();

    try {
      // don't refresh cache
      runTestRefreshAllNoParameterSideEffects(false);
      // refresh cache
      runTestRefreshAllNoParameterSideEffects(true);
    } finally {
      cleanupPM(pmVerify);
      pmVerify = null;
      cleanupPM(pm1);
      pm1 = null;
      cleanupPM(pm1);
      pm2 = null;
    }
    failOnError();
  }

  /**
   * @param doRefresh doRefresh flag
   */
  private void runTestRefreshAllNoParameterSideEffects(boolean doRefresh) {

    if (debug) logger.debug("\nSTART RefreshAllNoParameterSideEffects");

    Transaction tx1 = pm1.currentTransaction();
    tx1.setOptimistic(true);
    tx1.begin();
    VersionedPCPoint pnt1 = (VersionedPCPoint) pm1.getObjectById(oid, true);
    pnt1.setX(11); // make transactional

    Transaction tx2 = pm2.currentTransaction();
    tx2.begin();
    VersionedPCPoint pnt2 = (VersionedPCPoint) pm2.getObjectById(oid);
    pnt2.setX(22);
    pnt2.setY(Integer.valueOf("22"));
    tx2.commit();

    if (doRefresh) pm1.refreshAll();
    pnt1.setX(33);
    pnt1.setY(Integer.valueOf("33"));
    try {
      tx1.commit();
    } catch (javax.jdo.JDOOptimisticVerificationException ove) {
      if (doRefresh) {
        appendMessage("Expected no exception on commit with doRefresh " + "true, but got " + ove);
      }
      // else expect exception
    } catch (Exception e) {
      appendMessage(
          "Unexpected exception on commit. doRefresh is " + doRefresh + ".  Exception is: " + e);
    }

    // verify that correct value was committed
    VersionedPCPoint pntExpected = new VersionedPCPoint(33, 33);
    if (!doRefresh) {
      pntExpected.setX(22);
      pntExpected.setY(Integer.valueOf("22"));
    }

    Transaction txVerify = pmVerify.currentTransaction();
    txVerify.begin();
    VersionedPCPoint pntVerify = (VersionedPCPoint) pmVerify.getObjectById(oid, true);
    if (pntVerify.getX() != pntExpected.getX()
        || pntVerify.getY().intValue() != pntExpected.getY().intValue()) {
      appendMessage(
          "After commit with doRefresh "
              + doRefresh
              + " expected ("
              + pntExpected.getX()
              + ", "
              + pntExpected.getY()
              + ") but actual is ("
              + pntVerify.getX()
              + ", "
              + pntVerify.getY()
              + ").");
    }
    txVerify.commit();

    if (debug) logger.debug("END RefreshAllNoParameterSideEffects" + "doRefresh is " + doRefresh);
  }
}
