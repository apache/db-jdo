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

package org.apache.jdo.tck.api.persistencemanager.flags;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set IgnoreCache To True <br>
 * <B>Keywords:cache</B> <br>
 * <B>Assertion ID:</B> A12.5.3-2. <br>
 * <B>Assertion Description: </B> The PersistenceManager.setIgnoreCache method called with a value
 * of true is a hint to the query engine that the user expects queries to be optimized to return
 * approximate results by ignoring changed values in the cache. This is not testable, except to see
 * whether the get/set works.
 */
public class SetIgnoreCacheToTrue extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.3-2 (SetIgnoreCacheToTrue) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetIgnoreCacheToTrue.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetIgnoreCacheToTrue(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  public void runTestSetIgnoreCacheToTrue(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      PCPoint p1 = new PCPoint();
      tx.begin();
      pm.setIgnoreCache(true);
      if (!pm.getIgnoreCache()) {
        fail(
            ASSERTION_FAILED,
            "pm.getIgnoreCache() should return true after setting the flag to true.");
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
