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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> One PersistenceManager If Persistent or Transactional <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.5-2. <br>
 * <B>Assertion Description: </B> A JDO Instance is associated with exactly one <code>
 * PersistenceManager</code> if the instance is persistent or transactional. This is a duplicate
 * test.
 */
public class OnePersistenceManagerIfPersistentOrTransactional extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5-2 (OnePersistenceManagerIfPersistentOrTransactional) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(OnePersistenceManagerIfPersistentOrTransactional.class);
  }

  /** */
  public void test() {
    if (debug)
      logger.debug(
          "org.apache.jdo.tck.api.persistencemanager.OnePersistenceManagerIfPersistentOrTransactional.run duplicate");
  }
}
