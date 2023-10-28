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
 * <B>Title:</B>Set IgnoreCache of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-5. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.setIgnoreCache(boolean flag) sets the
 * value of the IgnoreCache property (the query mode that specifies whether cached instances are
 * considered when evaluating the filter expression).
 */
public class SetIgnoreCache extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.1-5 (SetIgnoreCache) failed: ";

  /** */
  @Override
  protected void localSetUp() {
    closePMF();
    pmf = getUnconfiguredPMF();
  }

  /** Set IgnoreCache to true or false and use getIgnoreCache value to verify. */
  @Test
  public void test() {
    try {
      setIgnoreCache(false);
      setIgnoreCache(true);
    } finally {
      closePMF();
    }
  }

  /** */
  private void setIgnoreCache(boolean newValue) {
    pmf.setIgnoreCache(newValue);
    boolean current = pmf.getIgnoreCache();
    if (current != newValue) {
      fail(
          ASSERTION_FAILED,
          "IgnoreCache set to " + newValue + ", value returned by PMF is " + current);
    }
  }
}
