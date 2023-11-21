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

import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOUserException;
import javax.jdo.LegacyJava;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Close of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> A11.4-2, A11.4-10 <br>
 * <B>Assertion Description: </B> PersistenceManagerFactory.close() closes this
 * PersistenceManagerFactory. <B>Assertion Description: </B> PersistenceManagerFactory.isClosed();
 * Return true if this PersistenceManagerFactory is closed; and false otherwise.
 */
public class Close extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertions A11.4-2 (Close), A11.4-10 (isClosed) failed: ";

  @SuppressWarnings("unchecked")
  private static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
    try {
      return (T) LegacyJava.doPrivilegedAction.invoke(null, privilegedAction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new JDOFatalInternalException(e.getMessage());
    }
  }

  /** */
  @Test
  public void test() {
    pmf = getPMF();

    // check pmf.isClosed() before and after pmf.close()
    try {
      if (pmf.isClosed()) {
        fail(ASSERTION_FAILED, "PMF.isClosed() returned true on an open pmf");
      }

      closePMF(pmf); // don't use closePMF() because that sets pmf to null

      if (!pmf.isClosed()) {
        fail(ASSERTION_FAILED, "PMF.isClosed() returned false on a closed pmf");
      }
    } catch (JDOUserException | JDOFatalUserException ex) {
      // unexpected exception
      fail(ASSERTION_FAILED, "Unexpected exception at pmf.close()/isClosed(): " + ex);
    }

    // pmf.close() on already-closed pmf should not throw an exception
    try {
      // don't use closePMF methods because they check isClosed before calling
      doPrivileged(
          () -> {
            pmf.close();
            return null;
          });
    } catch (JDOUserException | JDOFatalUserException ex) {
      // unexpected exception
      fail(ASSERTION_FAILED, "Unexpected exception at repeated pmf.close(): " + ex);
    }

    // trying to get a getPersistenceManager should result in a exception
    try {
      pm = pmf.getPersistenceManager();
      fail(
          ASSERTION_FAILED,
          "JDOUserException was not thrown when calling pmf.getPersistenceManager() after pmf was closed");
    } catch (JDOUserException ex) {
      // expected exception
      if (debug) {
        logger.debug("caught expected exception " + ex);
      }
    } catch (JDOFatalUserException ex) {
      // unexpected exception
      fail(
          ASSERTION_FAILED,
          "Wrong exception thrown from getPersistenceManager after close.\n"
              + "Expected JDOUserException, got JDOFatalUserException.");
    }

    // have next invocation of getPMF() get a new pmf
    pmf = null;
  }
}
