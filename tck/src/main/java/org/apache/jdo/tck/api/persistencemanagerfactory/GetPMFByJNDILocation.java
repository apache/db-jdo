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

import java.io.File;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>GetPMFByJNDILocation of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A8.6-19. <br>
 * <B>Assertion Description: </B> Uses the parameter(s) passed as arguments to construct a
 * Properties instance, and then delegates to the static method getPersistenceManagerFactory in the
 * class named in the property javax.jdo.PersistenceManagerFactoryClass. If there are any exceptions
 * while trying to construct the Properties instance or to call the static method, then either
 * A8.6-4 [JDOFatalUserException] or A8.6-5 [JDOFatalInternalException is thrown], depending on
 * whether the exception is due to the user or the implementation. The nested exception indicates
 * the cause of the exception.
 *
 * @author Michael Watzek
 */
public class GetPMFByJNDILocation extends AbstractGetPMF {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A8.6-19 (GetPMFByJNDILocation) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPMFByJNDILocation.class);
  }

  /** */
  public void testInvalidGetPMF() {
    if (SKIP_JNDI) {
      logger.debug(
          "Skipped test GetPMFByJNDILocation.testInvalidGetPMF because of jdo.tck.skipJndi.");
    } else {
      checkGetPMFWithInvalidProperties(ASSERTION_FAILED);
    }
  }

  /** */
  public void testValidGetPMF() {
    if (SKIP_JNDI) {
      logger.debug(
          "Skipped test GetPMFByJNDILocation.testValidGetPMF because of jdo.tck.skipJndi.");
    } else {
      checkGetPMFWithValidProperties();
    }
  }

  /** */
  protected PersistenceManagerFactory getPMF(String name) {
    Context context = null;
    try {
      // We need a JNDI context which contains a PMF instance.
      // For this reason, we create a JNDI context,
      // create a PMF instance and bind that to the context.
      context = new InitialContext();
      if (name.equals(validPropertiesFile)) {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(new File(name));
        verifyProperties(pmf, loadProperties(validPropertiesFile));
        context.bind(jndiName, pmf);
      }
      return JDOHelper.getPersistenceManagerFactory(jndiName, context);
    } catch (NamingException e) {
      throw new JDOFatalException("Caught NamingException trying to bind " + e.getMessage(), e);
    } finally {
      if (context != null) {
        try {
          context.unbind(jndiName);
          context.close();
        } catch (NamingException e) {
          fail("Caught NamingException trying to unbind or close." + e.getMessage());
          // stay quiet
        }
      }
    }
  }
}
