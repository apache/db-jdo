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

package org.apache.jdo.tck.api.persistencemanager.lifecycle;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 * <B>Title:</B> make instance persistent <br>
 * <B>Keywords:</B> persistencemanager <br>
 * <B>Assertion IDs:</B> A12.5.7-6A <br>
 * <B>Assertion Description: </B> The method <code>PersistenceManager.makePersistent</code> and
 * <code>makePersistentAll</code> makes a transient instance(s) persistent directly. It must be
 * called in the context of an active transaction, or a <code>JDOUserException</code> is thrown.
 */
public class MakePersistent extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.7-6A (MakePersistent) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakePersistent.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** Overrides parent method to avoid querying for classes that may not exist in the schema */
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
  }

  /** */
  public void testMakePersistent() {
    pm = getPM();

    createObjects();

    /* positive tests */
    runTestMakePersistent1(pm);
    runTestMakePersistent2(pm);
    runTestMakePersistent3(pm);

    /* negative tests */
    runTestMakePersistent4(pm);
    runTestMakePersistent5(pm);
    runTestMakePersistent6(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createObjects() {
    p1 = new PCPoint(1, 3);
    p2 = new PCPoint(2, 4);
    p3 = new PCPoint(3, 5);
    p4 = new PCPoint(4, 6);
    p5 = new PCPoint(5, 7);
  }

  /* test makePersistent (Object pc) */
  private void runTestMakePersistent1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.makePersistent(p1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistent1()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistent2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new HashSet();
      col1.add(p2);
      col1.add(p3);

      pm.makePersistentAll(col1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistent2()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestMakePersistent3(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();

      Collection col1 = new HashSet();
      col1.add(p1);
      col1.add(p2);

      Object[] obj1 = col1.toArray();

      pm.makePersistentAll(obj1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistent3()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistent (Object pc) */
  private void runTestMakePersistent4(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    if (tx.isActive()) tx.rollback();

    PCPoint np1 = new PCPoint(1, 3);
    try {
      pm.makePersistent(np1);
      fail(ASSERTION_FAILED, "pm.makePersistent outside of a tx should throw a JDOUserException");
    } catch (JDOUserException ex) {
      // expected expected
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistent5(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    if (tx.isActive()) tx.rollback();

    PCPoint np1 = new PCPoint(3, 3);
    PCPoint np2 = new PCPoint(4, 4);

    Collection col1 = new HashSet();
    col1.add(np1);
    col1.add(np2);

    try {
      pm.makePersistentAll(col1);
      fail(
          ASSERTION_FAILED,
          "pm.makePersistentAll(Collection) outside of a tx should throw a JDOUserException");
    } catch (JDOUserException ex) {
      // expected expected
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestMakePersistent6(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    if (tx.isActive()) tx.rollback();

    PCPoint np1 = new PCPoint(3, 3);
    PCPoint np2 = new PCPoint(4, 4);

    Collection col1 = new HashSet();
    col1.add(np1);
    col1.add(np2);

    Object[] obj1 = col1.toArray();

    try {
      pm.makePersistentAll(obj1);
      fail(
          ASSERTION_FAILED,
          "pm.makePersistentAll(Object[]) outside of a tx should throw a JDOUserException");
    } catch (JDOUserException ex) {
      // expected expected
    }
  }
}
