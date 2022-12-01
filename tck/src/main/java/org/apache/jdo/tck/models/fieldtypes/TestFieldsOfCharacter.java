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

package org.apache.jdo.tck.models.fieldtypes;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfCharacter;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Support of field type Character. <br>
 * <B>Keywords:</B> model <br>
 * <B>Assertion ID:</B> A6.4.3-10. <br>
 * <B>Assertion Description: </B> JDO implementations must support fields of the immutable object
 * class <code>java.lang.Character</code>, and may choose to support them as Second Class Objects or
 * First Class Objects.
 */
public class TestFieldsOfCharacter extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.4.3-10 (TestFieldsOfCharacter) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TestFieldsOfCharacter.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(FieldsOfCharacter.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTest(pm);

    pm.close();
    pm = null;
  }

  /** */
  void runTest(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    int i, n;
    Character firstValue = Character.valueOf(Character.MIN_VALUE);
    Character secondValue = Character.valueOf(Character.MAX_VALUE);
    tx.begin();
    FieldsOfCharacter pi = new FieldsOfCharacter();
    pi.identifier = 1;
    pm.makePersistent(pi);
    Object oid = pm.getObjectId(pi);
    n = pi.getLength();
    // Provide initial set of values
    for (i = 0; i < n; ++i) {
      pi.set(i, firstValue);
    }
    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();

    pi = (FieldsOfCharacter) pm.getObjectById(oid, true);
    checkValues(oid, firstValue);

    // Provide new set of values
    for (i = 0; i < n; ++i) {
      pi.set(i, secondValue);
    }
    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();
    // check new values
    checkValues(oid, secondValue);
    tx.commit();
  }

  /** */
  private void checkValues(Object oid, Character startValue) {
    int i;
    FieldsOfCharacter pi = (FieldsOfCharacter) pm.getObjectById(oid, true);
    int n = pi.getLength();
    for (i = 0; i < n; ++i) {
      if (!FieldsOfCharacter.isPersistent[i]) continue;
      Character val = pi.get(i);
      if (!val.equals(startValue)) {
        fail(
            ASSERTION_FAILED,
            "Incorrect value for "
                + FieldsOfCharacter.fieldSpecs[i]
                + ", expected value "
                + startValue.toString()
                + ", value is "
                + val);
      }
    }
  }
}
