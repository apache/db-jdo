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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Binary Addition Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-26. <br>
 * <B>Assertion Description: </B> The binary addition operator (<code>+</code>) is supported for all
 * types as they are defined in the Java language. This includes the following types:
 *
 * <UL>
 *   <LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code>
 *   <LI><code>float, double, Float, Double</code>
 *   <LI><code>BigDecimal, BigInteger</code>
 * </UL>
 *
 * The operation on object-valued fields of wrapper types (<code>
 * Boolean, Byte, Short, Integer, Long, Float</code>, and <code>Double</code>), and numeric types (
 * <code>BigDecimal</code> and <code>BigInteger</code>) use the wrapped values as operands.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BinaryAddition extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-26 (BinaryAddition) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive1() {
    if (debug) logger.debug("\nExecuting test BinaryAddition() ...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> instance9 = pm.newQuery(PrimitiveTypes.class, "id == 9").executeList();

      runSimplePrimitiveTypesQuery("id + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("byteNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("shortNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("intNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("longNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("floatNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("doubleNotNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("byteNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("shortNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("intNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("longNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("floatNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("doubleNull + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("bigDecimal + 1 == 10", pm, instance9, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("bigInteger + 1 == 10", pm, instance9, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive2() {
    if (debug) logger.debug("\nExecuting test BinaryAddition() ...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> allOddInstances =
          pm.newQuery(PrimitiveTypes.class, "booleanNull").executeList();

      runSimplePrimitiveTypesQuery("charNull + 1 == 'P'", pm, allOddInstances, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("charNotNull + 1 == 'P'", pm, allOddInstances, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
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
