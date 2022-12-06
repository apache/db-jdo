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

import java.util.Properties;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Concurrent Persistence Managers <br>
 * <B>Keywords:</B> concurrency multipleJDOimpls <br>
 * <B>Assertion ID:</B> A5.2-1. <br>
 * <B>Assertion Description: </B> An implementation should support its own <code>PersistenceManager
 * </code> concurrently with another <code>PersistenceManager</code>. A non-reference implementation
 * will be tested concurrently with the Reference Implementation.
 */
public class ConcurrentPersistenceManagers extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A5.2-1 (ConcurrentPersistenceManagers) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ConcurrentPersistenceManagers.class);
  }

  /** */
  public void test() {
    Properties pmfProperties = loadPMF2Properties();
    PersistenceManagerFactory pmf2 = JDOHelper.getPersistenceManagerFactory(pmfProperties);
    PersistenceManager pm2 = pmf2.getPersistenceManager();
    Transaction tx2 = pm2.currentTransaction();
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      tx2.begin();
      tx.commit();
      tx = null;
      tx2.commit();
      tx2 = null;
    } finally {
      cleanupPM(pm);
      pm = null;
      cleanupPM(pm2);
      pm2 = null;
      closePMF(pmf2);
    }
  }

  private Properties loadPMF2Properties() {
    String PMF2 = System.getProperty("PMF2Properties", "jdori2.properties");
    if (debug) logger.debug("Got PMF2Properties file name:" + PMF2);
    Properties ret = loadProperties(PMF2);
    if (debug) logger.debug("Got PMF2Properties: " + ret);
    return ret;
  }
}
