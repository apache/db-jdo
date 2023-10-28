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

package org.apache.jdo.tck.api.persistencemanager.extent;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> GetExtentWithNoSubclasses <br>
 * <B>Keywords:</B> inheritance extent <br>
 * <B>Assertion ID:</B> A12.5.4-2. <br>
 * <B>Assertion Description: </B> The getExtent method returns an Extent that contains all of the
 * instances in the parameter class. With the subclasses parameter false, only instances of the
 * specified class are returned.
 */
public class GetExtentWithNoSubclasses extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.4-2 (GetExtentWithNoSubclasses) failed: ";

  private final PCPoint p1 = null;
  private final PCPoint p2 = null;

  /** */
  @Test
  public void testGetExtentWithNoSubclasses() {
    pm = getPM();
    createObjects(pm);
    runTestGetExtent(pm);
    pm.close();
    pm = null;
  }

  /** */
  private void createObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint p1 = new PCPoint(1, 3);
      PCPoint p2 = new PCPoint(3, 5);
      pm.makePersistent(p1);
      pm.makePersistent(p2);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestGetExtent(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Extent<PCPoint> e = pm.getExtent(PCPoint.class, false);

      int c = 0;
      for (PCPoint p : e) {
        if (debug) logger.debug("p.getX() = " + p.getX());
        if ((p.getX() == 1) || (p.getX() == 3)) {
          // OK
        } else {
          fail(
              ASSERTION_FAILED,
              "Extent of class "
                  + PCPoint.class.getName()
                  + " includes unexpected instance, p.getX():"
                  + p.getX());
        }
      }
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
