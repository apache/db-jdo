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
 * <B>Title:</B>Set ConnectionPassword of persistencemanagerfactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.1-15. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.setConnectionPassword(String password)
 * sets the value of the ConnectionPassword property (the password for the user)
 */
public class SetConnectionPassword extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.1-15 (SetConnectionPassword) failed: ";

  /** The value of the ConnectionPassword property. */
  private String password;

  /** */
  @Override
  protected void localSetUp() {
    closePMF();
    pmf = getUnconfiguredPMF();
    password = getPMFProperty(CONNECTION_PASSWORD_PROP);
  }

  /** Set ConnectionPassword. */
  @Test
  public void test() {
    try {
      pmf.setConnectionPassword(password);
    } finally {
      closePMF();
    }
  }
}
