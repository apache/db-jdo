/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager;

import java.util.HashSet;
import java.util.Set;
import javax.jdo.JDOException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> OptimisticFailure <br>
 * <B>Keywords:</B> optimistic <br>
 * <B>Assertion IDs:</B> A13.5-1 <br>
 * <B>Assertion Description: </B> If any instance fails the verification, a
 * JDOOptimisticVerificationException is thrown which contains an array of
 * JDOOptimisticVerificationException, one for each instance that failed the verification. The
 * optimistic transaction is failed, and the transaction is rolled back. The definition of "changed
 * instance" is a JDO implementation choice, but it is required that a field that has been changed
 * to different values in different transactions results in one of the transactions failing.
 */
public class OptimisticFailure extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A13.5-1 (OptimisticFailure) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(OptimisticFailure.class);
  }

  private VersionedPCPoint p1 =
      new VersionedPCPoint(1, 1); // this will be updated in tx1, updated in tx2, verified in tx3
  private VersionedPCPoint p2 =
      new VersionedPCPoint(2, 2); // this will be updated in tx1, deleted in tx2, verified in tx3
  private VersionedPCPoint p3 =
      new VersionedPCPoint(3, 3); // this will be deleted in tx1, updated in tx2
  private VersionedPCPoint p4 =
      new VersionedPCPoint(4, 4); // this will be deleted in tx1, deleted in tx2
  private VersionedPCPoint p5 =
      new VersionedPCPoint(5, 5); // this will be unchanged in tx1, updated in tx2, verified in tx3
  private Object p1oid = null;
  private Object p2oid = null;
  private Object p3oid = null;
  private Object p4oid = null;
  private Object p5oid = null;

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(VersionedPCPoint.class);
  }

  /** */
  public void test() {
    pm = getPM();
    PersistenceManager pm2 = pmf.getPersistenceManager();
    PersistenceManager pm3 = pmf.getPersistenceManager();

    try {
      runTestOptimistic(pm, pm2, pm3);
    } finally {
      cleanupPM(pm3);
      pm3 = null;
      cleanupPM(pm2);
      pm2 = null;
      cleanupPM(pm);
      pm = null;
    }
  }

  /** */
  private void runTestOptimistic(
      PersistenceManager pm1, PersistenceManager pm2, PersistenceManager pm3) {
    if (!isOptimisticSupported()) {
      if (debug) logger.debug("OptimisticFailure tests not run; Optimistic not supported");
      return;
    }

    Transaction tx1 = pm1.currentTransaction();
    Transaction tx2 = pm2.currentTransaction();
    Transaction tx3 = pm3.currentTransaction();
    try {
      tx1.setOptimistic(true);
      tx2.setOptimistic(true);

      // create five instances to test
      tx1.begin();
      pm1.makePersistent(p1);
      pm1.makePersistent(p2);
      pm1.makePersistent(p3);
      pm1.makePersistent(p4);
      pm1.makePersistent(p5);
      p1oid = pm1.getObjectId(p1);
      p2oid = pm1.getObjectId(p2);
      p3oid = pm1.getObjectId(p3);
      p4oid = pm1.getObjectId(p4);
      p5oid = pm1.getObjectId(p5);
      tx1.commit();

      // update/delete the instances in tx1
      tx1.begin();
      VersionedPCPoint p1tx1 = (VersionedPCPoint) pm1.getObjectById(p1oid, true);
      VersionedPCPoint p2tx1 = (VersionedPCPoint) pm1.getObjectById(p2oid, true);
      VersionedPCPoint p3tx1 = (VersionedPCPoint) pm1.getObjectById(p3oid, true);
      VersionedPCPoint p4tx1 = (VersionedPCPoint) pm1.getObjectById(p4oid, true);
      p1tx1.setX(101);
      p2tx1.setX(201);
      pm1.deletePersistent(p3tx1);
      pm1.deletePersistent(p4tx1);

      // update/delete the instances in tx2
      tx2.begin();
      VersionedPCPoint p1tx2 = (VersionedPCPoint) pm2.getObjectById(p1oid, true);
      VersionedPCPoint p2tx2 = (VersionedPCPoint) pm2.getObjectById(p2oid, true);
      VersionedPCPoint p3tx2 = (VersionedPCPoint) pm2.getObjectById(p3oid, true);
      VersionedPCPoint p4tx2 = (VersionedPCPoint) pm2.getObjectById(p4oid, true);
      VersionedPCPoint p5tx2 = (VersionedPCPoint) pm2.getObjectById(p5oid, true);
      p1tx2.setX(102);
      pm2.deletePersistent(p2tx2);
      p3tx2.setX(202);
      pm2.deletePersistent(p4tx2);
      p5tx2.setX(502); // this change must not be committed
      Set expectedFailedObjects = new HashSet();
      expectedFailedObjects.add(p1tx2);
      expectedFailedObjects.add(p2tx2);
      expectedFailedObjects.add(p3tx2);
      expectedFailedObjects.add(p4tx2);

      // commit tx1 (should succeed)
      tx1.commit();
      tx1 = null;

      // commit tx2 (should fail)
      try {
        tx2.commit();
        fail(ASSERTION_FAILED, "concurrent commit not detected");
      } catch (JDOOptimisticVerificationException ex) {
        // verify the correct information in the exception
        Throwable[] ts = ex.getNestedExceptions();
        int length = ts == null ? 0 : ts.length;
        int expectedFailures = expectedFailedObjects.size();
        if (length != expectedFailures) {
          fail(
              ASSERTION_FAILED,
              "Nested exceptions[] wrong size: expected " + expectedFailures + ", got " + length);
        }
        for (int i = 0; i < length; ++i) {
          Throwable t = ts[i];
          if (t instanceof JDOOptimisticVerificationException) {
            if (debug) logger.debug("Expected exception caught " + t.toString());
            JDOException jex = (JDOException) t;
            Object failed = jex.getFailedObject();
            if (failed == null) {
              fail(ASSERTION_FAILED, "Found unexpected null in failed object");
            } else {
              if (expectedFailedObjects.remove(failed)) {
                if (debug)
                  logger.debug(
                      "Found expected failed instance, oid: " + JDOHelper.getObjectId(failed));
              } else {
                fail(ASSERTION_FAILED, "Unexpected failed instance: " + failed.toString());
              }
            }
          } else {
            fail(ASSERTION_FAILED, "Unexpected nested exception: " + t.toString());
          }
        }
      }
      tx2 = null;

      tx3.begin();
      VersionedPCPoint p1tx3 = (VersionedPCPoint) pm3.getObjectById(p1oid, true);
      VersionedPCPoint p2tx3 = (VersionedPCPoint) pm3.getObjectById(p2oid, true);
      VersionedPCPoint p5tx3 = (VersionedPCPoint) pm3.getObjectById(p5oid, true);
      verify(p1tx3, 101);
      verify(p2tx3, 201);
      verify(p5tx3, 5);
      tx3.commit();
      tx3 = null;
    } finally {
      if ((tx3 != null) && tx3.isActive()) tx3.rollback();
      if ((tx2 != null) && tx2.isActive()) tx2.rollback();
      if ((tx1 != null) && tx1.isActive()) tx1.rollback();
    }
  }

  /**
   * @param p PCPoint instance
   * @param value value
   */
  protected void verify(VersionedPCPoint p, int value) {
    if (p.getX() != value) {
      fail(
          ASSERTION_FAILED,
          "VersionedPCPoint has wrong value: expected " + value + ", got " + p.getX());
    }
  }
}
