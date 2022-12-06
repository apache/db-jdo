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

import java.util.Enumeration;
import java.util.Properties;
import javax.jdo.PersistenceManagerFactory;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Get properties of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.4-1. <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.getProperties() returns a Properties
 * instance containing two standard JDO implementation properties VendorName: The name of the JDO
 * vendor. VersionNumber: The version number string.
 */
public class GetProperties extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.4-1 (GetProperties) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetProperties.class);
  }

  /** */
  public void test() {
    PersistenceManagerFactory pmf = getPMF();
    int foundStandardProperties = 0;

    Properties p = pmf.getProperties();
    for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements(); ) {
      String s = (String) e.nextElement();
      if (debug) logger.debug("\t" + s + ": " + p.getProperty(s));

      if (s.equals("VendorName") || s.equals("VersionNumber")) {
        foundStandardProperties++;
      }
    }

    if (foundStandardProperties != 2) {
      fail(ASSERTION_FAILED, "Standard properties not found.");
    }
  }
}
