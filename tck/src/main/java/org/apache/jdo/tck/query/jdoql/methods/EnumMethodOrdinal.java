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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfSimpleEnum;
import org.apache.jdo.tck.pc.fieldtypes.SimpleEnum;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Supported Enum methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-59. <br>
 * <B>Assertion Description: </B> New supported Enum methods:
 *
 * <ul>
 *   <li>ordinal()
 *   <li>toString()
 * </ul>
 */
public class EnumMethodOrdinal extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-59 (EnumMethodOrdinal) failed: ";

  /** */
  private Object oidOfExpectedResult;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(EnumMethodOrdinal.class);
  }

  /** */
  public void testOrdinal() {
    final String filter = "SimpleEnum0.ordinal() == 5";
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FieldsOfSimpleEnum> expectedResult = new ArrayList<>();
      expectedResult.add((FieldsOfSimpleEnum) pm.getObjectById(oidOfExpectedResult));

      Query<FieldsOfSimpleEnum> q = pm.newQuery(FieldsOfSimpleEnum.class);
      q.setFilter(filter);
      List<FieldsOfSimpleEnum> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(FieldsOfSimpleEnum.class);
    insertFieldsOfSimpleEnums(getPM());
  }

  /** */
  private void insertFieldsOfSimpleEnums(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      FieldsOfSimpleEnum f1 = new FieldsOfSimpleEnum();
      f1.set(0, SimpleEnum.CA);
      f1.identifier = 1;
      pm.makePersistent(f1);
      FieldsOfSimpleEnum f2 = new FieldsOfSimpleEnum();
      f2.set(0, SimpleEnum.HI);
      f2.identifier = 2;
      pm.makePersistent(f2);
      FieldsOfSimpleEnum f3 = new FieldsOfSimpleEnum();
      f3.set(0, SimpleEnum.DC);
      f3.identifier = 3;
      pm.makePersistent(f3);
      oidOfExpectedResult = pm.getObjectId(f1);
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
