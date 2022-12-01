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

package org.apache.jdo.tck.lifecycle;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test Transient Transactional Commit <br>
 * <B>Keywords:</B> lifecycle transienttransactional commit <br>
 * <B>Assertion IDs:</B> A5.1-3 <br>
 * <B>Assertion Description: </B> If <code>TransientTransactional</code> is supported, a transient
 * transactional instance will have its state preserved when its associated transaction commits.
 */
public class TransientTransactionalStateCommit extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A5.1-3 (TransientTransactionalStateCommit) failed: ";

  private static final int CLEAN_VALUE = 12;
  private static final int DIRTY_VALUE = 123;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TransientTransactionalStateCommit.class);
  }

  /** */
  public void test() {
    if (!isTransientTransactionalSupported()) {
      logger.debug("Transient transactional instances are not supported");
    } else {
      pm = getPM();

      StateTransitionObj obj = getTransientInstance();

      pm.currentTransaction().begin();

      makeTransientDirty(obj);

      int beforeValue = obj.readField();
      pm.currentTransaction().commit();
      int afterValue = obj.readField();
      if (beforeValue != afterValue) {
        fail(
            ASSERTION_FAILED,
            "Field value incorrect after commit. Expected: "
                + beforeValue
                + " Found: "
                + afterValue);
      }
    }
  }

  protected StateTransitionObj getTransientInstance() {
    StateTransitionObj obj = new StateTransitionObj(CLEAN_VALUE);
    int curr = currentState(obj);
    if (curr != TRANSIENT) {
      fail(ASSERTION_FAILED, "Unable to create transient instance, state is " + states[curr]);
    }
    return obj;
  }

  protected void makeTransientClean(StateTransitionObj obj) {
    if (obj == null) return;
    pm.makeTransactional(obj);
    int curr = currentState(obj);
    if (curr != TRANSIENT_CLEAN) {
      fail(
          ASSERTION_FAILED,
          "Unable to create transient-clean instance "
              + "from a transient instance via makeTransactional(), state is "
              + states[curr]);
    }
  }

  protected void makeTransientDirty(StateTransitionObj obj) {
    if (obj == null) return;
    makeTransientClean(obj);
    obj.writeField(DIRTY_VALUE);
    int curr = currentState(obj);
    if (curr != TRANSIENT_DIRTY) {
      fail(
          ASSERTION_FAILED,
          "Unable to create transient-dirty instance "
              + "from a transient-clean instance via modifying a field, state is "
              + states[curr]);
    }
  }
}
