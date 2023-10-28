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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;
import java.util.HashSet;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Delete Persistent <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5.7-9 <br>
 * <B>Assertion Description: </B> PersistenceManager.deletePersistent and deletePersistentAll delete
 * a persistent instance(s) from the data store. It must be called in the context of an active
 * transaction, or a JDOUserExceptionis thrown. The representation in the data store will be deleted
 * when this instance is flushed to the data store (via commit, or evict).
 */
public class DeletePersistent extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.7-9 (DeletePersistent) failed: ";

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  @Test
  public void testDeletePersistent() {
    pm = getPM();
    createObjects(pm);

    /* positive tests */
    /* method called in the context of an active transaction */
    runTestDeletePersistent1(pm);
    runTestDeletePersistent2(pm);
    runTestDeletePersistent3(pm);

    /* negative tests */
    /* method called in the context of an non-active transaction */
    runTestDeletePersistent4(pm);
    runTestDeletePersistent5(pm);
    runTestDeletePersistent6(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      p1 = new PCPoint(1, 3);
      p2 = new PCPoint(2, 4);
      p3 = new PCPoint(3, 5);
      p4 = new PCPoint(4, 6);
      p5 = new PCPoint(5, 7);

      pm.makePersistent(p1);
      pm.makePersistent(p2);
      pm.makePersistent(p3);
      pm.makePersistent(p4);
      pm.makePersistent(p5);

      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /** test deletePersistent (Object pc) */
  private void runTestDeletePersistent1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();
      pm.deletePersistent(p1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistent1()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Collection pcs) */
  private void runTestDeletePersistent2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p2);
      col1.add(p3);

      pm.deletePersistentAll(col1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistent2()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Object[] o) */
  private void runTestDeletePersistent3(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p4);
      col1.add(p5);

      Object[] obj1 = col1.toArray();

      pm.deletePersistentAll(obj1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistent3()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistent (Object pc) */
  public void runTestDeletePersistent4(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint np1 = new PCPoint(1, 3);
      pm.makePersistent(np1);
      tx.commit();

      try {
        pm.deletePersistent(np1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistent(Object) outside of tx should throw an exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Collection pcs) */
  public void runTestDeletePersistent5(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);
      pm.makePersistent(np1);
      pm.makePersistent(np2);
      tx.commit();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(np1);
      col1.add(np2);

      try {
        pm.deletePersistentAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistentAll(Collection) outside of tx should throw an exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Object[] o) */
  public void runTestDeletePersistent6(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);

      pm.makePersistent(np1);
      pm.makePersistent(np2);
      tx.commit();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(np1);
      col1.add(np2);
      Object[] obj1 = col1.toArray();

      try {
        pm.deletePersistentAll(obj1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistentAll(Object[]) outside of tx should throw an exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
