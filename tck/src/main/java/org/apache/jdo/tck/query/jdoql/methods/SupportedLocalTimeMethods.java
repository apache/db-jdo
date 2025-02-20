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
package org.apache.jdo.tck.query.jdoql.methods;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.query.LocalTimeSample;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Supported LocalTime methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-60. <br>
 * <B>Assertion Description: </B> New supported LocalTime methods:
 *
 * <ul>
 *   <li>getHour()
 *   <li>getMinute()
 *   <li>getSecond()
 * </ul>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupportedLocalTimeMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-60 (SupportedLocalTimeMethods) failed: ";

  /** */
  private Object oidOfLocalTime1;

  /** */
  private Object oidOfLocalTime2;

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testHour() {
    final String filter = "localTime.getHour() == 14";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalTimeSample) pm.getObjectById(oidOfLocalTime1));

      Query<LocalTimeSample> q = pm.newQuery(LocalTimeSample.class, filter);
      List<LocalTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMinute() {
    final String filter = "localTime.getMinute() == 22";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalTimeSample) pm.getObjectById(oidOfLocalTime2));

      Query<LocalTimeSample> q = pm.newQuery(LocalTimeSample.class, filter);
      List<LocalTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSecond() {
    final String filter = "localTime.getSecond() == 25";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalTimeSample) pm.getObjectById(oidOfLocalTime1));

      Query<LocalTimeSample> q = pm.newQuery(LocalTimeSample.class, filter);
      List<LocalTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
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
    addTearDownClass(LocalTimeSample.class);
    insertLocalTimeSampleData(getPM());
  }

  /** */
  private void insertLocalTimeSampleData(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      LocalTimeSample lds1 = new LocalTimeSample();
      lds1.setId(1);
      LocalTime localTime1 = LocalTime.of(14, 10, 25);
      lds1.setLocalTime(localTime1);
      pm.makePersistent(lds1);

      LocalTimeSample lds2 = new LocalTimeSample();
      lds2.setId(2);
      LocalTime localTime2 = LocalTime.of(9, 22, 12);
      lds2.setLocalTime(localTime2);
      pm.makePersistent(lds2);

      tx.commit();
      oidOfLocalTime1 = pm.getObjectId(lds1);
      oidOfLocalTime2 = pm.getObjectId(lds2);
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
