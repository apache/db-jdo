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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Set optimistic of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-1, A11.1-2. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.getOptimistic() returns Value of the
 * Optimistic property,persistenceManagerFactory.setOptimistic(boolean flag) sets the value of the
 * Optimistic property (the transaction mode that specifies concurrency control.
 */
public class SetOptimistic extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.1-1, A11.1-2 (SetOptimistic) failed: ";

  /** */
  @Override
  protected void localSetUp() {
    closePMF();
    pmf = getUnconfiguredPMF();
  }

  /** Set Optimistic to true or false and use getOptimistic value to verify. */
  @Test
  public void test() {
    if (!isOptimisticSupported()) {
      printUnsupportedOptionalFeatureNotTested(
          "org.apache.jdo.tck.api.persistencemanagerfactory.SetOptimistic",
          "javax.jdo.option.Optimistic");
      return;
    }

    try {
      setOptimistic(false);
      setOptimistic(true);
    } finally {
      closePMF();
    }
  }

  /** */
  private void setOptimistic(boolean newValue) {
    pmf.setOptimistic(newValue);
    boolean optimistic = pmf.getOptimistic();
    if (optimistic != newValue) {
      fail(
          ASSERTION_FAILED,
          "Optimistic set to " + newValue + ", value returned by PMF is " + optimistic);
    }
  }
}
