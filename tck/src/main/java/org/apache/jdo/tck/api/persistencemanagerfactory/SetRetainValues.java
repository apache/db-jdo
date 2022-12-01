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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Set RetainValues of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-3,A11.1-4. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.setRetainValues(boolean flag) sets the
 * value of the RetainValues property (the transaction mode that specifies the treatment of
 * persistent instances after commit), PersistenceManagerFactory.getRetainValues() returns the value
 * of the RetainValues property.
 */
public class SetRetainValues extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.1-3,A11.1-4 (SetRetainValues) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetRetainValues.class);
  }

  /** */
  @Override
  protected void localSetUp() {
    closePMF();
    pmf = getUnconfiguredPMF();
  }

  /** Set RetainValues to true or false and use getRetainValues value to verify. */
  public void test() {
    try {
      setRetainValues(false);
      setRetainValues(true);
    } finally {
      closePMF();
    }
  }

  /** */
  private void setRetainValues(boolean newValue) {
    pmf.setRetainValues(newValue);
    boolean current = pmf.getRetainValues();
    if (current != newValue) {
      fail(
          ASSERTION_FAILED,
          "RetainValues set to " + newValue + ", value returned by PMF is " + current);
    }
  }
}
