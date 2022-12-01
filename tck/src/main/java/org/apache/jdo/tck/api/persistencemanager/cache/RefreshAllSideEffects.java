/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Refresh All Side Effects <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion ID:</B> A12.5.1-5D <br>
 * <B>Assertion Description: </B> The refreshAll(Object[] pcs) updates the values in the parameter
 * instance[s] from the data in the data store. ((The intended use is for optimistic transactions
 * where the state of the JDO Instance is not guaranteed to reflect the state in the data store.
 * This method can be used to minimize the occurrence of commit failures due to mismatch between the
 * state of cached instances and the state of data in the data store.)) This can be tested by using
 * 2 PersistenceManagers, independently change an object, then refresh.
 */
public class RefreshAllSideEffects extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.1-5D (RefreshAllSideEffects) failed: ";

  private final List<Object> oids = new ArrayList<>();

  private PersistenceManager pm1;
  private PersistenceManager pm2;
  private PersistenceManager pmVerify;
  private final List<VersionedPCPoint> coll =
      new ArrayList<>(); // Collection of persistent instances
  private boolean useCollection = true;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(RefreshAllSideEffects.class);
  }

  /** */
  @Override
  public void localSetUp() {
    addTearDownClass(VersionedPCPoint.class);
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    VersionedPCPoint pnt1 = new VersionedPCPoint(0, 0);
    VersionedPCPoint pnt2 = new VersionedPCPoint(-1, -1);
    coll.add(pnt1);
    coll.add(pnt2);
    pm.makePersistentAll(coll);

    oids.add(0, pm.getObjectId(pnt1));
    oids.add(1, pm.getObjectId(pnt2));
    tx.commit();
  }

  /** */
  public void testRefreshAllWithCollection() {
    useCollection = true;
    runtest();
  }

  /** */
  public void testRefreshAllWithArray() {
    useCollection = false;
    runtest();
  }

  /** */
  private void runtest() {
    if (!isOptimisticSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          "RefreshAllSideEffects", "javax.jdo.option.Optimistic");
      return;
    }

    pm1 = pmf.getPersistenceManager();
    pm2 = pmf.getPersistenceManager();
    pmVerify = pmf.getPersistenceManager();

    try {
      // don't refresh cache
      runTestRefreshAllSideEffects(false);
      // refresh cache
      runTestRefreshAllSideEffects(true);
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

  /** */
  @SuppressWarnings("unchecked")
  private void runTestRefreshAllSideEffects(boolean doRefresh) {

    if (debug) logger.debug("\nSTART RefreshAllSideEffects");

    Transaction tx1 = pm1.currentTransaction();
    tx1.setOptimistic(true);
    tx1.begin();
    Collection<VersionedPCPoint> points1 = pm1.getObjectsById(oids, true);
    ((VersionedPCPoint) points1.toArray()[0]).setX(11); // make transactional

    Transaction tx2 = pm2.currentTransaction();
    tx2.begin();
    Collection<VersionedPCPoint> points2 = pm2.getObjectsById(oids);
    ((VersionedPCPoint) points2.toArray()[0]).setX(22);
    ((VersionedPCPoint) points2.toArray()[1]).setX(22);
    ((VersionedPCPoint) points2.toArray()[0]).setY(Integer.valueOf("22"));
    ((VersionedPCPoint) points2.toArray()[1]).setY(Integer.valueOf("22"));
    tx2.commit();

    if (doRefresh) {
      if (useCollection) pm1.refreshAll(points1);
      else pm1.refreshAll(points1.toArray());
    }
    ((VersionedPCPoint) points1.toArray()[0]).setX(33);
    ((VersionedPCPoint) points1.toArray()[1]).setX(33);
    ((VersionedPCPoint) points1.toArray()[0]).setY(Integer.valueOf("33"));
    ((VersionedPCPoint) points1.toArray()[1]).setY(Integer.valueOf("33"));
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
    Collection<VersionedPCPoint> pointsVerify = pmVerify.getObjectsById(oids, true);
    if (((VersionedPCPoint) pointsVerify.toArray()[0]).getX() != pntExpected.getX()
        || ((VersionedPCPoint) pointsVerify.toArray()[0]).getY().intValue()
            != pntExpected.getY().intValue()) {
      appendMessage(
          "After commit with doRefresh "
              + doRefresh
              + " expected ("
              + pntExpected.getX()
              + ", "
              + pntExpected.getY()
              + ") but actual is ("
              + ((VersionedPCPoint) pointsVerify.toArray()[0]).getX()
              + ", "
              + ((VersionedPCPoint) pointsVerify.toArray()[0]).getY().intValue()
              + ").");
    }
    txVerify.commit();

    if (debug) logger.debug("END RefreshAllSideEffects" + "doRefresh is " + doRefresh);
  }
}
