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
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfPrimitivebyte;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Support of field type byte. <br>
 * <B>Keywords:</B> model <br>
 * <B>Assertion ID:</B> A6.4.3-2. <br>
 * <B>Assertion Description: </B> JDO implementations must support fields of the primitive type
 * <code>int</code>.
 */
public class TestFieldsOfPrimitivebyte extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.4.3-2 (TestFieldsOfPrimitivebyte) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TestFieldsOfPrimitivebyte.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(FieldsOfPrimitivebyte.class);
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
    byte value;
    tx.begin();
    FieldsOfPrimitivebyte pi = new FieldsOfPrimitivebyte();
    pi.identifier = 1;
    pm.makePersistent(pi);
    Object oid = pm.getObjectId(pi);
    n = pi.getLength();
    // Provide initial set of values
    for (i = 0, value = 10; i < n; ++i) {
      pi.set(i, value);
    }
    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();

    pi = (FieldsOfPrimitivebyte) pm.getObjectById(oid, true);
    checkValues(oid, (byte) 10);

    // Provide new set of values
    for (i = 0, value = 127; i < n; ++i) {
      pi.set(i, value);
    }
    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();
    // check new values
    checkValues(oid, (byte) 127);
    tx.commit();
  }

  /** */
  private void checkValues(Object oid, byte startValue) {
    int i;
    FieldsOfPrimitivebyte pi = (FieldsOfPrimitivebyte) pm.getObjectById(oid, true);
    int n = pi.getLength();
    for (i = 0; i < n; ++i) {
      if (!FieldsOfPrimitivebyte.isPersistent[i]) continue;
      byte val = pi.get(i);
      if (val != startValue) {
        fail(
            ASSERTION_FAILED,
            "Incorrect value for "
                + FieldsOfPrimitivebyte.fieldSpecs[i]
                + ", expected value "
                + startValue
                + ", value is "
                + val);
      }
    }
  }
}
