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
 * <B>Title:</B> No PersistenceManager If Transient <br>
 * <B>Keywords:</B> transient <br>
 * <B>Assertion ID:</B> A12.5-1. <br>
 * <B>Assertion Description: </B> A JDO Instance is associated with no <code>PersistenceManager
 * </code> if and only if the instance is in the transient state. This is a duplicate of A8.1-2.
 */
public class NoPersistenceManagerIfTransient extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5-1 (NoPersistenceManagerIfTransient) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NoPersistenceManagerIfTransient.class);
  }

  /** */
  public void test() {
    if (debug)
      logger.debug(
          "org.apache.jdo.tck.api.persistencemanager.NoPersistenceManagerIfTransient duplicate of A8.1-2.");
  }
}
