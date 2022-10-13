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

package org.apache.jdo.tck.api.persistencemanager.flags;

import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get IgnoreCache Value from PersistenceManager <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.5.3-1. <br>
 * <B>Assertion Description: </B> The PersistenceManager.getIgnoreCache method returns the current
 * value of the IgnoreCache option.
 */
public class GetIgnoreCache extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.3-1 (GetIgnoreCache) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetIgnoreCache.class);
  }

  /** */
  public void testGetIgnoreCache() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      PCPoint p1 = new PCPoint();
      tx.begin();
      pm.setIgnoreCache(true);
      boolean returnValue = pm.getIgnoreCache();

      if (!returnValue)
        fail(ASSERTION_FAILED, "pm.getIgnoreCache() returns false after setting the falg to true");
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
