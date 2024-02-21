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

import java.util.Collections;
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
 * <B>Title:</B> LogicalComplement Query Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-32. <br>
 * <B>Assertion Description: </B> The logical complement operator (<code>!</code>) is supported for
 * all types as they are defined in the Java language. This includes the following types:
 *
 * <UL>
 *   <LI><code>Boolean, boolean</code>
 * </UL>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogicalComplement extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-32 (LogicalComplement) failed: ";

  /** Tests logical complement operator ! used with constants or simple boolean fields */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveSimpleComplement1() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> allEvenInstances =
          pm.newQuery(PrimitiveTypes.class, "booleanNull == false").executeList();

      // case !boolean
      runSimplePrimitiveTypesQuery("! booleanNotNull", pm, allEvenInstances, ASSERTION_FAILED);

      // case ! Boolean
      runSimplePrimitiveTypesQuery("! booleanNull", pm, allEvenInstances, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** Tests logical complement operator ! used with constants or simple boolean fields */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveSimpleComplement2() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> allInstances = pm.newQuery(PrimitiveTypes.class, "true").executeList();

      // case !false
      runSimplePrimitiveTypesQuery("! false", pm, allInstances, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** Tests logical complement operator ! used with constants or simple boolean fields */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveSimpleComplement3() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<PrimitiveTypes> empty = Collections.emptyList();

      // case !true
      runSimplePrimitiveTypesQuery("! true", pm, empty, ASSERTION_FAILED);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** Tests logical complement operator ! negating the result of a relational. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveComplementOfRelationalOp() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      List<PrimitiveTypes> instancesLess3 =
          pm.newQuery(PrimitiveTypes.class, "id < 3").executeList();
      List<PrimitiveTypes> instancesNot3 =
          pm.newQuery(PrimitiveTypes.class, "id != 3").executeList();
      List<PrimitiveTypes> instances3 = pm.newQuery(PrimitiveTypes.class, "id == 3").executeList();

      // case !(field >= value)
      runSimplePrimitiveTypesQuery("! (id >= 3)", pm, instancesLess3, ASSERTION_FAILED);

      // case !(field == value)
      runSimplePrimitiveTypesQuery("! (id == 3)", pm, instancesNot3, ASSERTION_FAILED);

      // case !(field != value)
      runSimplePrimitiveTypesQuery("! (id != 3)", pm, instances3, ASSERTION_FAILED);

      // case !!(field == value)
      runSimplePrimitiveTypesQuery("!! (id == 3)", pm, instances3, ASSERTION_FAILED);

      // case !!(field != value)
      runSimplePrimitiveTypesQuery("!! (id != 3)", pm, instancesNot3, ASSERTION_FAILED);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
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
