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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Same Classes with Concurrent Persistence Managers <br>
 * <B>Keywords:</B> concurrency multipleJDOimpls <br>
 * <B>Assertion ID:</B> A5.2-2. <br>
 * <B>Assertion Description: </B> The same classes must be supported concurrently by multiple <code>
 * PersistenceManager</code>s from different implementations.
 */
public class ConcurrentPersistenceManagersSameClasses extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A5.2-2 (ConcurrentPersistenceManagersSameClasses) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ConcurrentPersistenceManagersSameClasses.class);
  }

  /** */
  public void test() {
    if (!isBinaryCompatibilitySupported()) {
      printUnsupportedOptionalFeatureNotTested(
          "org.apache.jdo.tck.api.persistencemanager.ConcurrentPersistenceManagersSameClasses",
          "javax.jdo.option.BinaryCompatibility");
      return;
    }
    Properties pmfProperties = loadPMF2Properties();
    PersistenceManagerFactory pmf2 = JDOHelper.getPersistenceManagerFactory(pmfProperties);
    PersistenceManager pm2 = pmf2.getPersistenceManager();
    Transaction tx2 = pm2.currentTransaction();
    PCPoint p21 = null;
    PCPoint p22 = null;
    PCRect rect2 = null;

    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      tx2.begin();

      PCPoint p11 = new PCPoint(110, 120);
      PCPoint p12 = new PCPoint(120, 140);
      PCRect rect1 = new PCRect(0, p11, p12);
      pm.makePersistent(rect1);

      p21 = new PCPoint(210, 220);
      p22 = new PCPoint(220, 240);
      rect2 = new PCRect(0, p21, p22);
      pm2.makePersistent(rect2);

      tx.commit();
      tx2.commit();

      tx.begin();
      tx2.begin();

      PCPoint p11a = findPoint(pm, 110, 120);
      if (p11a != p11) {
        fail(
            ASSERTION_FAILED,
            "unexpected PCPoint instance, expected: 110, 120, found: "
                + p11a.getX()
                + ", "
                + p11a.getY());
      }

      PCPoint p21a = findPoint(pm2, 210, 220);
      if (p21a != p21) {
        fail(
            ASSERTION_FAILED,
            "unexpected PCPoint instance, expected: 210, 220, found: "
                + p21a.getX()
                + ", "
                + p21a.getY());
      }

      tx.commit();
      tx = null;
      tx2.commit();
      tx2 = null;
    } finally {
      cleanupPM(pm);
      pm = null;
      try {
        // delete pm2 instances
        if (pm2.currentTransaction().isActive()) {
          pm2.currentTransaction().rollback();
        }
        pm2.currentTransaction().begin();
        pm2.deletePersistent(rect2);
        pm2.deletePersistent(p21);
        pm2.deletePersistent(p22);
        pm2.currentTransaction().commit();
      } finally {
        cleanupPM(pm2);
        pm2 = null;
        closePMF(pmf2);
      }
    }
  }

  /** */
  private Properties loadPMF2Properties() {
    String PMF2 = System.getProperty("PMF2Properties", "jdori2.properties");
    if (debug) logger.debug("Got PMF2Properties file name:" + PMF2);
    Properties ret = loadProperties(PMF2);
    if (debug) logger.debug("Got PMF2Properties: " + ret);
    return ret;
  }

  /** */
  private PCPoint findPoint(PersistenceManager pm, int x, int y) {
    Query<PCPoint> q = pm.newQuery(PCPoint.class);
    q.declareParameters("int px, int py");
    q.setFilter("x == px & y == py");
    q.setParameters(Integer.valueOf(x), Integer.valueOf(y));
    List<PCPoint> results = q.executeList();
    Iterator<PCPoint> it = results.iterator();
    PCPoint ret = it.next();
    return ret;
  }
}
