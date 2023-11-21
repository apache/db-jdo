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

package org.apache.jdo.tck.transactions;

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Set Nontransactional Read True When Not Supported <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-5. <br>
 * <B>Assertion Description: </B> If an implementation does not support nontransactional read, then
 * a call to Transaction.setNontransactionalRead with a parameter value of true will cause a
 * JDOUnsupportedOptionException to be thrown.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/18/01     1.0
 */
public class SetNontransactionalReadTrueWhenNotSupported extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.2-5 (SetNontransactionalReadTrueWhenNotSupported) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTestSetNontransactionalReadTrueWhenNotSupported(pm);

    pm.close();
    pm = null;
  }

  /**
   * test transactions.setNonteansactionalRead()
   *
   * @param pm the PersistenceManager
   */
  public void runTestSetNontransactionalReadTrueWhenNotSupported(PersistenceManager pm) {
    if (isNontransactionalReadSupported()) {
      if (debug) logger.debug("Implementation does support non transactional read.");
      return;
    }

    Transaction tx = pm.currentTransaction();
    try {
      tx.setNontransactionalRead(true);
      fail(
          ASSERTION_FAILED,
          "tx.setNontransactionalRead(true) should throw JDOUnsupportedOptionException, if the implementation does not support non transactional read.");
    } catch (JDOUnsupportedOptionException ex) {
      // expected excepted
      if (debug) logger.debug("caught expected exception " + ex);
    }
  }
}
