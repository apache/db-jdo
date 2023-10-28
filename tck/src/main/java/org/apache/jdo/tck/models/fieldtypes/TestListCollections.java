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

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ListCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Support of field type List <br>
 * <B>Keywords:</B> model <br>
 * <B>Assertion ID:</B> A6.4.3-36. <br>
 * <B>Assertion Description: </B> If the List optional feature is supported, then JDO
 * implementations must support fields of the interface type <code>List</code>, supporting them as
 * Second Class Objects or First Class Objects.
 */
public class TestListCollections extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A6.4.3-36 (Commit) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(ListCollections.class);
    addTearDownClass(SimpleClass.class);
  }

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTest(pm);

    pm.close();
    pm = null;
  }

  /** */
  void runTest(PersistenceManager pm) {
    if (!isListSupported()) {
      if (debug) logger.debug("JDO Implementation does not support " + "the optional feature List");
      return;
    }

    if (!isLinkedListSupported() && !isArrayListSupported()) {
      fail(
          ASSERTION_FAILED,
          "JDO Implementation supports List, but neither " + "ArrayList nor LinkedList.");
    }

    Transaction tx = pm.currentTransaction();
    ListCollections expectedValue = new ListCollections();

    // turn on datastore transactions
    tx.setOptimistic(false);
    tx.begin();
    ListCollections pi = new ListCollections();
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
    pi = (ListCollections) pm.getObjectById(oid, true);
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
  private void setValues(ListCollections collect, int order) {
    @SuppressWarnings("rawtypes")
    Vector value;
    int n = collect.getLength();
    for (int i = 0; i < n; ++i) {
      String valueType = TestUtil.getFieldSpecs(ListCollections.fieldSpecs[i]);
      value = TestUtil.makeNewVectorInstance(valueType, order);
      collect.set(i, value);
      if (debug) logger.debug("Set " + i + "th value to: " + value.toString());
    }
  }

  /** */
  private void checkValues(Object oid, ListCollections expectedValue) {
    int i;
    StringBuilder sbuf = new StringBuilder();
    ListCollections pi = (ListCollections) pm.getObjectById(oid, true);
    int n = pi.getLength();
    for (i = 0; i < n; ++i) {
      List<?> expected = expectedValue.get(i);
      List<?> actual = pi.get(i);
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
        if (TestUtil.getFieldSpecs(ListCollections.fieldSpecs[i]).equals("BigDecimal")) {
          ListIterator<?> expectedIt = expected.listIterator();
          ListIterator<?> actualIt = actual.listIterator();
          int index = 0;
          while (expectedIt.hasNext()) {
            BigDecimal bigDecExpected = (BigDecimal) (expectedIt.next());
            BigDecimal bigDecActual = (BigDecimal) (actualIt.next());
            if ((bigDecExpected.compareTo(bigDecActual)) != 0) {
              sbuf.append(
                  "\nFor element "
                      + i
                      + "("
                      + index
                      + "), expected = "
                      + bigDecExpected
                      + ", actual = "
                      + bigDecActual);
            }
            index++;
          }
        } else {
          sbuf.append(
              "\nFor element " + i + ", expected = " + expected + ", actual = " + actual + " . ");
        }
      }
    }
    if (sbuf.length() > 0) {
      fail(ASSERTION_FAILED, "Expected and observed do not match!!" + sbuf);
    }
  }
}
