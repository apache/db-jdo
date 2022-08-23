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

import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Set ReadOnly property of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs: A11.1-29, A11.1-30</B> <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.setReadOnly(boolean) sets the value of
 * the ReadOnly property PersistenceManagerFactory.getReadOnly() returns the value of the ReadOnly
 * property
 */
public class SetReadOnly extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.1-29, A11.1-30 (SetRetainValues) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetReadOnly.class);
  }

  /** */
  protected void localSetUp() {
    closePMF();
    pmf = getUnconfiguredPMF();
  }

  /** Set ReadOnly to true or false and use getReadOnly to verify. */
  public void test() {
    try {
      setReadOnly(false);
      setReadOnly(true);
    } finally {
      closePMF();
    }
  }

  /** */
  private void setReadOnly(boolean newValue) {
    pmf.setReadOnly(newValue);
    boolean current = pmf.getReadOnly();
    if (current != newValue) {
      fail(
          ASSERTION_FAILED,
          "ReadOnly set to " + newValue + ", value returned by PMF is " + current);
    }
  }
}
