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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOUserException;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>AfterCloseGetPMThrowsException of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.4-9B. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.getPersistenceManager() throws
 * JDOUserException after the PersistenceManagerFactory is closed.
 */

/*
 * Revision History
 * ================
 * Author :  Craig Russell
 * Date   :  05/16/03
 *
 */

public class AfterCloseGetPMThrowsException extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.4-9B (AfterCloseGetPMThrowsException) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AfterCloseGetPMThrowsException.class);
  }

  /** */
  public void test() {
    try {
      pmf = getPMF();
      closePMF(pmf); // don't use closePMF() because that sets pmf to null
      pm = pmf.getPersistenceManager();
      fail(
          ASSERTION_FAILED,
          "pmf.getPersistenceManager should throw JDOUserException if pmf is closed.");
    } catch (JDOUserException ex) {
      // expected exception
      if (debug) logger.debug("caught expected exception " + ex.toString());
    } catch (JDOFatalUserException ex) {
      // unexpected exception
      fail(
          ASSERTION_FAILED,
          "Wrong exception thrown from getPersistenceManager after close.\n"
              + "Expected JDOUserException, got JDOFatalUserException.");
    } finally {
      if (pm != null) pm.close();
    }
  }
}
