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

package org.apache.jdo.tck.api.persistencemanager.close;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> AutoCloseable <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.6 ?. <br>
 * <B>Assertion Description: </B> In a non-managed environment, if the PM is created with
 * try-with-resources then it is automatically closed at the end of that block.
 */
public class AutoCloseable extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.6-? (AutoCloseable) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AutoCloseable.class);
  }

  /**
   * The method creates a pm with try-with-resources and checks that it is closed after the block.
   */
  public void testTryWithResource() {

    try (PersistenceManager pm1 = getPM()) {
      pm = pm1;
      if (pm.isClosed()) {
        fail(
            ASSERTION_FAILED,
            "PersistenceManager is expected to be open inside try-with-resource block.");
      }
    }

    if (!pm.isClosed()) {
      fail(ASSERTION_FAILED, "PersistenceManager should be closed after try-with-resource block.");
    }
  }

  /**
   * The method creates a pm with try-with-resources and checks that it is closed after the block,
   * if the block is ended with an exception.
   */
  public void testTryWithResourceThrowingException() {

    try (PersistenceManager pm1 = getPM()) {
      pm = pm1;
      if (pm.isClosed()) {
        fail(
            ASSERTION_FAILED,
            "PersistenceManager is expected to be open inside try-with-resource block.");
      }
      throw new DummyException();
    } catch (DummyException ex) {
      // exception is expected
    }

    if (!pm.isClosed()) {
      fail(ASSERTION_FAILED, "PersistenceManager should be closed after try-with-resource block.");
    }
  }

  /** DummyException used in method testTryWithResourceThrowingException. */
  private static final class DummyException extends Exception {
    private static final long serialVersionUID = 1L;
  }
}
