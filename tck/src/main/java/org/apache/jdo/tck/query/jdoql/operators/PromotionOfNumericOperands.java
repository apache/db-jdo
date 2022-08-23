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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Promotion of Numeric Operands for Comparisons <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-39. <br>
 * <B>Assertion Description: </B> Numeric operands can be promoted for comparison and arithmetic
 * operations.
 */
public class PromotionOfNumericOperands extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-39 (PromotionOfNumericOperands) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(PromotionOfNumericOperands.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();

    Collection instance9 = (Collection) pm.newQuery(PrimitiveTypes.class, "id == 9").execute();

    // comparison double == int
    runSimplePrimitiveTypesQuery("doubleNotNull == 9", pm, instance9, ASSERTION_FAILED);

    // comparison Double == int
    runSimplePrimitiveTypesQuery("doubleNull == 9", pm, instance9, ASSERTION_FAILED);

    // comparison BigInteger == int
    runSimplePrimitiveTypesQuery("bigInteger == 9", pm, instance9, ASSERTION_FAILED);

    // comparison BigDecimal == int
    runSimplePrimitiveTypesQuery("bigDecimal == 9", pm, instance9, ASSERTION_FAILED);

    // promotion in arithmetic operation: int + byte == int
    runSimplePrimitiveTypesQuery("1 + byteNotNull == 10", pm, instance9, ASSERTION_FAILED);

    // promotion in arithmetic operation: int - short == long
    runSimplePrimitiveTypesQuery("10 - shortNotNull == 1L", pm, instance9, ASSERTION_FAILED);

    // promotion in arithmetic operation: long - Integer == int
    runSimplePrimitiveTypesQuery("10L - intNull == 1", pm, instance9, ASSERTION_FAILED);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PrimitiveTypes.class);
    loadAndPersistPrimitiveTypes(getPM());
  }
}
