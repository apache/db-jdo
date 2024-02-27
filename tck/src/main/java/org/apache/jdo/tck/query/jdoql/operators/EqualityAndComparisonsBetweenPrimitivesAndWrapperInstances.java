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
 * <B>Title:</B> Equality and Comparisons Between Primitives and Wrapper Instances <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-3. <br>
 * <B>Assertion Description: </B> Equality and ordering comparisons between primitives and instances
 * of wrapper classes are valid in a <code>Query</code> filter.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EqualityAndComparisonsBetweenPrimitivesAndWrapperInstances extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-3 (EqualityAndComparisonsBetweenPrimitivesAndWrapperInstances) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive1() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> instance9 = pm.newQuery(PrimitiveTypes.class, "id == 9").executeList();

      // case Integer == int
      runSimplePrimitiveTypesQuery("intNull == 9", pm, instance9, ASSERTION_FAILED);
      // case Integer == long
      runSimplePrimitiveTypesQuery("intNull == 9L", pm, instance9, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive2() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> instancesLess3 =
          pm.newQuery(PrimitiveTypes.class, "id < 3").executeList();

      // case int <= Integer
      runParameterPrimitiveTypesQuery(
          "intNotNull <= param",
          "java.lang.Integer param",
          Integer.valueOf(2),
          pm,
          instancesLess3,
          ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive3() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> allOddInstances =
          pm.newQuery(PrimitiveTypes.class, "booleanNull").executeList();

      // case char == Character
      runSimplePrimitiveTypesQuery("'O' == charNull", pm, allOddInstances, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive4() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> allInstances = pm.newQuery(PrimitiveTypes.class, "true").executeList();

      // case Double > double
      runSimplePrimitiveTypesQuery("doubleNull > 0.0", pm, allInstances, ASSERTION_FAILED);

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
