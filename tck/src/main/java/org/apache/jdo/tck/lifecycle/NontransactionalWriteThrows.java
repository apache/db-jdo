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

package org.apache.jdo.tck.lifecycle;

import javax.jdo.JDOUnsupportedOptionException;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test NontransactionalWriteThrows <br>
 * <B>Keywords:</B> NontransactionalWrite <br>
 * <B>Assertion ID:</B> A13.4.2-6 <br>
 * <B>Assertion Description: </B> If an implementation does not support the [NontranasctionalWrite]
 * option, then an attempt to set the flag to an unsupported value will throw
 * JDOUnsupportedOptionException.
 */
public class NontransactionalWriteThrows extends NontransactionalWriteTest {

  /** */
  protected static final String ASSERTION_FAILED =
      "Assertion A13.4.2-6 (NontransactionalWriteThrows) failed: ";

  /**
   * Make sure that if NontransactionalWrite is not supported, an exception is thrown when setting
   * NontransactionalWrite.
   */
  @Test
  public void testNontransactionalWriteThrows() {
    if (!checkNontransactionalWriteSupported()) {
      try {
        getPM().currentTransaction().setNontransactionalWrite(true);
      } catch (JDOUnsupportedOptionException ex) {
        return; // good catch
      } catch (Exception ex) {
        // threw wrong exception
        appendMessage(ASSERTION_FAILED + "threw wrong exception " + ex);
      }
      appendMessage(ASSERTION_FAILED + "failed to throw " + "JDOUnsupportedOptionException");
    }
    failOnError();
  }
}
