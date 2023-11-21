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

package org.apache.jdo.tck.api.persistencemanager.close;

import javax.jdo.JDOFatalUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> AfterCloseAllMethodsThrowException <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5-6 <br>
 * <B>Assertion Description: </B> After the PersistenceManager.close method completes, all methods
 * on PersistenceManager except isClosed throw a JDOFatalUserException.
 */
public class AfterCloseAllMethodsThrowException extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5-6 (AfterCloseAllMethodsThrowException) failed: ";

  private PersistenceManagerFactory pmf;
  private PersistenceManager pm;
  private Transaction tx;

  /** */
  @Test
  public void testAfterCloseAllMethodsThrowException() {
    pm = getPM();
    pm.close();
    runTestEvict();
    runTestRefresh();
    runTestIsClosed();
    pm = null;
  }

  /** */
  private void runTestEvict() {
    try {
      pm.evictAll();
      fail(ASSERTION_FAILED, "pm.evictAll does not throw exception if pm is closed.");
    } catch (JDOFatalUserException ex) {
      // caught expected exception
    }
  }

  /** */
  private void runTestRefresh() {
    try {
      pm.refreshAll();
      fail(ASSERTION_FAILED, "pm.refreshAll does not throw exception if pm is closed.");
    } catch (JDOFatalUserException ex) {
      // caught expected exception
    }
  }

  /** */
  private void runTestIsClosed() {
    if (!pm.isClosed()) fail(ASSERTION_FAILED, "pm.isClosed returns false for closed pm.");
  }
}
