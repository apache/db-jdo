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

package org.apache.jdo.tck.models.inheritance;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.inheritance.AllPersist4;
import org.apache.jdo.tck.pc.inheritance.Constants;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Transactional Fields are Transactional in Subclasses <br>
 * <B>Keywords:</B> inheritance <br>
 * <B>Assertion ID:</B> A6.5-5. <br>
 * <B>Assertion Description: </B> Fields marked as transactional in persistence-capable classes will
 * be transactional in subclasses.
 */
public class TransactionalFieldsAreTransactionalInSubclasses extends TestParts {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.5-5 (TransactionalFieldsAreTransactionalInSubclasses) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(TransactionalFieldsAreTransactionalInSubclasses.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(AllPersist4.class);
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
    Transaction t = pm.currentTransaction();
    try {
      t.setRestoreValues(true);

      t.begin();
      // create new objects and make persistent
      AllPersist4 c =
          new AllPersist4(
              Constants.intA_V[8],
              Constants.doubleB_V[8],
              Constants.intB_V[8],
              Constants.charC_V[8],
              Constants.booleanD_V[8],
              Constants.floatE_V[8],
              Constants.shortF_V[8],
              Constants.shortG_V[8],
              Constants.intH_V[8]);
      AllPersist4 d =
          new AllPersist4(
              Constants.intA_V[9],
              Constants.doubleB_V[9],
              Constants.intB_V[9],
              Constants.charC_V[9],
              Constants.booleanD_V[9],
              Constants.floatE_V[9],
              Constants.shortF_V[9],
              Constants.shortG_V[9],
              Constants.intH_V[9]);
      c.thirdObj = d;
      c.fourthObj = d;
      TestParts.thirdObj_V[8] = d;
      TestParts.fourthObj_V[8] = d;
      pm.makePersistent(c);

      // change values of newly persistent object
      c.intA = Constants.intA_V[10];
      c.charC = Constants.charC_V[10];
      c.booleanD = Constants.booleanD_V[10];
      c.shortG = Constants.shortG_V[10];
      c.fourthObj = null;
      c.floatE = Constants.floatE_V[10];
      c.secondObj = d;
      c.doubleB = Constants.doubleB_V[10];
      c.intB = Constants.intB_V[10];
      c.shortF = Constants.shortF_V[10];
      c.thirdObj = null;
      c.intH = Constants.intH_V[10];

      t.rollback();
      t = null;

      // verify objects revert back to transient after rollback
      checkTransactionalAreCorrect(
          ASSERTION_FAILED, transientAfterRollback, 8, c.floatE, c.secondObj);
    } finally {
      if ((t != null) && t.isActive()) t.rollback();
    }
  }

  void removeAllInstances(PersistenceManager pm) {
    AllPersist4 a = new AllPersist4(0, 0.0, 0, '0', false, 0.0f, (short) 0, (short) 0, 0);
    pm.makePersistent(a); // guarantee the class is registered; this will be removed
    Extent e = pm.getExtent(AllPersist4.class, true);
    Iterator i = e.iterator();
    while (i.hasNext()) {
      pm.deletePersistent(i.next());
    }
  }
}
