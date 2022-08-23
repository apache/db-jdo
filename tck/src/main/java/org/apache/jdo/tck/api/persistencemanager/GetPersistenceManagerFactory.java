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

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get PersistenceManagerFactory <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.9-1. <br>
 * <B>Assertion Description: </B> The PersistenceManagerFactory that created a PersistenceManager is
 * returned by the method getPersistenceManagerFactory.
 */
public class GetPersistenceManagerFactory extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.9-1 (GetPersistenceManagerFactory) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPersistenceManagerFactory.class);
  }

  /** */
  public void testGetPersistenceManagerFactory() {
    PersistenceManagerFactory pmf = getPMF();
    // Note, getPM uses the pmf returned by getPMF
    PersistenceManager pm = getPM();
    PersistenceManagerFactory pmf2 = pm.getPersistenceManagerFactory();
    pm.close();
    if (pmf2 != pmf) {
      fail(
          ASSERTION_FAILED,
          "pm.getPMF() returned different pmf, expected " + pmf + ", got " + pmf2);
    }
  }
}
