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
 * <B>Title:</B> Multiplication Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-30. <br>
 * <B>Assertion Description: </B> The multiplication operator (<code>*</code>) is supported for all
 * types as they are defined in the Java language. This includes the following types:
 *
 * <UL>
 *   <LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code>
 *   <LI><code>float, double, Float, Double</code>
 *   <LI><code>BigDecimal, BigInteger</code>
 * </UL>
 *
 * The operation on object-valued fields of wrapper types (<code>Boolean, Byte,
 * Short, Integer, Long, Float</code>, and <code>Double</code>), and numeric types (<code>BigDecimal
 * </code> and <code>BigInteger</code>) use the wrapped values as operands.
 */
public class Multiplication extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-30 (Multiplication) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(Multiplication.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    if (debug) logger.debug("\nExecuting test Multiplication() ...");

    Transaction tx = pm.currentTransaction();
    tx.begin();

    Collection instance4 = (Collection) pm.newQuery(PrimitiveTypes.class, "id == 4").execute();

    runSimplePrimitiveTypesQuery("id * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("byteNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("shortNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("intNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("longNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("floatNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("doubleNotNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("byteNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("shortNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("intNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("longNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("floatNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("doubleNull * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("bigDecimal * 2 == 8", pm, instance4, ASSERTION_FAILED);
    runSimplePrimitiveTypesQuery("bigInteger * 2 == 8", pm, instance4, ASSERTION_FAILED);

    tx.commit();
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
