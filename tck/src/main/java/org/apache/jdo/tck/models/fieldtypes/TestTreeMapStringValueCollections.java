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

package org.apache.jdo.tck.models.fieldtypes;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.pc.fieldtypes.TreeMapStringValueCollections;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Support of field type TreeMap, varying key type <br>
 * <B>Keywords:</B> model <br>
 * <B>Assertion ID:</B> A6.4.3-27. <br>
 * <B>Assertion Description: </B> If the TreeMap optional feature is supported, then JDO
 * implementation must support fields of the mutable object class <code>TreeMap</code>, supporting
 * them as Second Class Objects or First Class Objects.
 */
public class TestTreeMapStringValueCollections extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.4.3-27 (TestTreeMapStringValueCollections) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TestTreeMapStringValueCollections.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(TreeMapStringValueCollections.class);
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
    if (!isTreeMapSupported()) {
      if (debug)
        logger.debug("JDO Implementation does not support " + "the optional feature TreeMap");
      return;
    }

    Transaction tx = pm.currentTransaction();
    TreeMapStringValueCollections expectedValue = new TreeMapStringValueCollections();

    // turn on datastore transactions
    tx.setOptimistic(false);
    tx.begin();
    TreeMapStringValueCollections pi = new TreeMapStringValueCollections();
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
    checkValues(oid, expectedValue);
    pi = (TreeMapStringValueCollections) pm.getObjectById(oid, true);
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
  private void setValues(TreeMapStringValueCollections collect, int order) {
    int keyOrder = order;
    int valueOrder = (order == 1) ? 2 : 1; // why??
    int n = collect.getLength();
    for (int i = 0; i < n; ++i) {
      Vector fieldSpecs = TestUtil.getFieldSpecsForMap(TreeMapStringValueCollections.fieldSpecs[i]);
      Vector key = TestUtil.makeNewVectorInstance((String) fieldSpecs.get(0), keyOrder);
      Vector value = TestUtil.makeNewVectorInstance((String) fieldSpecs.get(1), valueOrder);

      TreeMap map = new TreeMap();
      for (int j = 0; j < key.size(); j++) {
        map.put(key.get(j), value.get(j));
      }
      collect.set(i, map);
      if (debug) logger.debug("Set " + i + "th value to: " + map.toString());
    }
  }

  /** */
  private void checkValues(Object oid, TreeMapStringValueCollections expectedValue) {
    StringBuffer sbuf = new StringBuffer();
    TreeMapStringValueCollections pi = (TreeMapStringValueCollections) pm.getObjectById(oid, true);
    int n = pi.getLength();
    for (int i = 0; i < n; ++i) {
      TreeMap expected = expectedValue.get(i);
      TreeMap actual = pi.get(i);
      if (actual.size() != expected.size()) {
        sbuf.append(
            "\nFor element "
                + i
                + ", expected size = "
                + expected.size()
                + ", actual size = "
                + actual.size()
                + " . ");
      } else if (!expected.equals(actual)) {
        if (TestUtil.getFieldSpecsForMap(TreeMapStringValueCollections.fieldSpecs[i])
            .get(0)
            .equals("BigDecimal")) {
          Set expectedKeySet = expected.keySet();
          Set actualKeySet = actual.keySet();
          Iterator expectedIter = expectedKeySet.iterator();
          while (expectedIter.hasNext()) {
            BigDecimal expectedKey = (BigDecimal) expectedIter.next();
            // compare keys
            if (!TestUtil.containsBigDecimalKey(expectedKey, actualKeySet)) {
              sbuf.append(
                  "\nFor element "
                      + i
                      + " expected key = "
                      + expectedKey
                      + " not found in actual Map.  Actual keyset is "
                      + actualKeySet.toString());
              // compare values
            } else {
              String expectedVal = (String) expected.get(expectedKey);
              String actualValue =
                  (String) actual.get(TestUtil.getBigDecimalKey(expectedKey, actualKeySet));
              if (!expectedVal.equals(actualValue)) {
                sbuf.append(
                    "\nFor element "
                        + i
                        + " expected value = "
                        + expectedVal
                        + " actual Value = "
                        + actualValue);
              }
            }
          }
        } else {
          sbuf.append(
              "\nFor element " + i + ", expected = " + expected + ", actual = " + actual + " . ");
        }
      }
    }
    if (sbuf.length() > 0) {
      fail(ASSERTION_FAILED, "Expected and observed do not match!!" + sbuf.toString());
    }
  }
}
