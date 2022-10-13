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

import java.util.Properties;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>GetPersistenceManagerForUser of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.3-2. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.getPersistenceManager(String userid,
 * String password) returns a PersistenceManager instance with the configured properties and the
 * default values for option settings.
 */
public class GetPersistenceManagerForUser extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A11.3-2 (GetPersistenceManagerForUser) failed: ";

  /** The value of the ConnectionUserName property. */
  private String username;

  /** The value of the ConnectionPassword property. */
  private String password;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPersistenceManagerForUser.class);
  }

  /** */
  @Override
  protected void localSetUp() {
    Properties props = loadProperties(PMFProperties);
    username = (String) props.remove(CONNECTION_USERNAME_PROP);
    password = (String) props.remove(CONNECTION_PASSWORD_PROP);
    pmf = JDOHelper.getPersistenceManagerFactory(props);
  }

  /** */
  public void test() {
    PersistenceManager pm = null;
    try {
      pm = pmf.getPersistenceManager(username, password);
      if (pm == null) {
        fail(
            ASSERTION_FAILED,
            "pmf.getPersistenceManager(user, password) should " + "return a non-null value.");
      }
    } finally {
      if ((pm != null) && !pm.isClosed()) {
        pm.close();
      }
      closePMF();
    }
  }
}
