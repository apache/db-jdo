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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get Object By Id <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion ID:</B> A12.5.6-9. <br>
 * <B>Assertion Description: </B> If <code>PersistenceManager.getObjectById</code> is called with a
 * value of <code>true</code> for the second parameter named <code>validate</code>, and there is not
 * an instance already in the cache with the same JDO identity as the oid parameter, then this
 * method creates an instance with the specified JDO identity, verifies that it exists in the data
 * store, and returns it.
 *
 * <OL TYPE=A>
 *   <LI>If there is no transaction in progress, the returned instance will be hollow or
 *       persistent-nontransactional, at the choice of the implementation.
 *   <LI>If there is a data store transaction in progress, the returned instance will be
 *       persistent-clean.
 *   <LI>If there is an optimistic transaction in progress, the returned instance will be
 *       persistent-nontransactional.
 * </OL>
 */
public class GetObjectById extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.6-9 (GetObjectById) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectById.class);
  }

  /** */
  public void testGetObjectById() {
    if (debug) logger.debug("\nSTART GetObjectById");

    Transaction tx = null;
    try {
      pm = getPM();
      Object oid = createPCPointInstance(pm);
      if (pm != null) {
        pm.close();
        pm = null; // make sure to get a different pm so obj is not in cache
      }

      Object obj = null;
      int state = 0;

      // nontransactional access
      if (isNontransactionalReadSupported()) {
        if (debug) logger.debug("nontransactional");
        pm = getPM();
        tx = pm.currentTransaction();
        tx.setNontransactionalRead(true);
        obj = pm.getObjectById(oid, true);
        state = currentState(obj);
        if (state != PERSISTENT_NONTRANSACTIONAL && state != HOLLOW) {
          fail(
              ASSERTION_FAILED,
              "Expected persistent-nontransactional or hollow; got " + getStateOfInstance(obj));
        }
        tx = null;
        pm.close();
        pm = null; // make sure to get a different pm so obj is not in cache
      }

      // pessimistic transactional access
      if (debug) logger.debug("pessimistic");
      pm = getPM();
      tx = pm.currentTransaction();
      tx.setOptimistic(false);
      tx.begin();
      obj = pm.getObjectById(oid, true);
      state = currentState(obj);
      tx.commit();
      tx = null;
      if (state != PERSISTENT_CLEAN) {
        fail(ASSERTION_FAILED, "Expected persistent-clean; got " + getStateOfInstance(obj));
      }
      pm.close();
      pm = null; // make sure to get a different pm so obj is not in cache

      // optimistic transactional access
      if (isOptimisticSupported()) {
        if (debug) logger.debug("optimistic");
        pm = getPM();
        tx = pm.currentTransaction();
        tx.setOptimistic(true);
        tx.begin();
        obj = pm.getObjectById(oid, true);
        state = currentState(obj);
        tx.commit();
        if (state != PERSISTENT_NONTRANSACTIONAL & state != HOLLOW) {
          fail(
              ASSERTION_FAILED,
              "Expected persistent-nontransactional; got " + getStateOfInstance(obj));
        }
        tx = null;
        pm.close();
        pm = null; // make sure to get a different pm so obj is not in cache
      }
    } finally {
      if (debug) logger.debug("END GetObjectById");

      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
