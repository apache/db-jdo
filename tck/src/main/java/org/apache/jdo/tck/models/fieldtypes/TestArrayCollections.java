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

package org.apache.jdo.tck.models.fieldtypes;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Vector;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ArrayCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Support of field type array. <br>
 * <B>Keywords:</B> model <br>
 * <B>Assertion ID:</B> A6.4.3-39. <br>
 * <B>Assertion Description: </B> JDO implementations may optionally support fields of array types.
 */
public class TestArrayCollections extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (TestArrayCollections) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TestArrayCollections.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(ArrayCollections.class);
    addTearDownClass(SimpleClass.class);
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
    if (!isArraySupported()) {
      if (debug) logger.debug("JDO Implementation does not support" + "optional feature Array");
      return;
    }

    Transaction tx = pm.currentTransaction();
    ArrayCollections expectedValue = new ArrayCollections();

    // turn on datastore transactions
    tx.setOptimistic(false);
    tx.begin();
    ArrayCollections pi = new ArrayCollections();
    pi.identifier = 1;
    pm.makePersistent(pi);
    Object oid = pm.getObjectId(pi);
    // Provide initial set of values
    setValues(pi, 1);

    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();
    setValues(expectedValue, 1);

    // check if persistent fields have values set
    checkValues(oid, expectedValue);
    pi = (ArrayCollections) pm.getObjectById(oid, true);

    // Provide new set of values
    setValues(pi, 2);
    tx.commit();
    // cache will be flushed
    pi = null;
    System.gc();

    tx.begin();
    // check new values
    setValues(expectedValue, 2);
    checkValues(oid, expectedValue);
    tx.commit();
  }

  /** */
  private void setValues(ArrayCollections collect, int order) {
    Vector<?> value;
    Class<?> vectorClass = null;
    int n = collect.getLength();
    for (int i = 0; i < n; ++i) {
      String valueType = TestUtil.getFieldSpecs(ArrayCollections.fieldSpecs[i]);
      value = TestUtil.makeNewVectorInstance(valueType, order);
      try {
        // get the right class to instantiate
        vectorClass = value.get(0).getClass();
      } catch (Exception ignored) {
      }

      Object[] valueArray = (Object[]) Array.newInstance(vectorClass, value.size());
      value.toArray(valueArray);

      collect.set(i, valueArray);
      if (debug) logger.debug("Set " + i + "th value to: " + Arrays.toString(valueArray));
    }
  }

  /** */
  private void checkValues(Object oid, ArrayCollections expectedValue) {
    StringBuilder sbuf = new StringBuilder();
    ArrayCollections pi = (ArrayCollections) pm.getObjectById(oid, true);
    int n = pi.getLength();
    for (int i = 0; i < n; ++i) {
      Object obj = new Object();
      Class<?> objClass = obj.getClass();
      Object[] expected = (Object[]) Array.newInstance(objClass, 5);
      Object[] actual = (Object[]) Array.newInstance(objClass, 5);
      expected = expectedValue.get(i);
      actual = pi.get(i);
      if (actual == null) {
        sbuf.append("\nFor " + ArrayCollections.fieldSpecs[i] + ", retrieved field is null.");
        continue;
      }
      if (expected.length != actual.length) {
        sbuf.append(
            "\nFor element "
                + i
                + ", expected size = "
                + expected.length
                + ", actual size = "
                + actual.length
                + " . ");
        continue;
      } else if (!Arrays.equals(expected, actual)) {
        if (TestUtil.getFieldSpecs(ArrayCollections.fieldSpecs[i]).equals("BigDecimal")) {
          for (int j = 0; j < actual.length; ++j) {
            BigDecimal expectedBD = (BigDecimal) expected[j];
            BigDecimal actualBD = (BigDecimal) actual[j];
            if ((expectedBD.compareTo(actualBD) != 0)) {
              sbuf.append(
                  "\nFor element "
                      + i
                      + "["
                      + j
                      + "], expected = "
                      + expectedBD
                      + ", actual = "
                      + actualBD
                      + " . ");
            }
          }
        } else {
          sbuf.append(
              "\nFor element "
                  + i
                  + ", expected = "
                  + Arrays.toString(expected)
                  + ", actual = "
                  + Arrays.toString(actual)
                  + " . ");
        }
      }
    }
    if (sbuf.length() > 0) {
      fail(ASSERTION_FAILED, "Expected and observed do not match!!" + sbuf);
    }
  }
}
