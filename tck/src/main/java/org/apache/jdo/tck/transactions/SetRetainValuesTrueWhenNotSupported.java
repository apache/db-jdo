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

package org.apache.jdo.tck.transactions;

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Retain Values True When Not Supported <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-8. <br>
 * <B>Assertion Description: </B> If the optional feature RetainValues is not supported, then a call
 * to Transaction.setRetainValues with a value of true will cause a JDOUnsupportedOptionException to
 * be thrown.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/22/01     1.0
 */
public class SetRetainValuesTrueWhenNotSupported extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.2-8 (SetRetainValuesTrueWhenNotSupported) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetRetainValuesTrueWhenNotSupported.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetRetainValuesTrueWhenNotSupported(pm);

    pm.close();
    pm = null;
  }

  /**
   * test transactions.setRetainValues()
   *
   * @param pm the PersistenceManager
   */
  void runTestSetRetainValuesTrueWhenNotSupported(PersistenceManager pm) {
    if (isRetainValuesSupported()) {
      if (debug) logger.debug("RetainValues supported.");
      return;
    }

    Transaction tx = pm.currentTransaction();
    try {
      tx.setRetainValues(true);
      fail(
          ASSERTION_FAILED,
          "tx.setRetainValues(true) should throw JDOUnsupportedOptionException, if the implementation does not support retainValues.");
    } catch (JDOUnsupportedOptionException ex) {
      // expected excepted
      if (debug) logger.debug("caught expected exception " + ex);
    }
  }
}
