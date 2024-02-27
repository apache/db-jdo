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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.query.LocalDateTimeSample;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Supported LocalDateTime methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-60. <br>
 * <B>Assertion Description: </B> New supported LocalDateTime methods:
 *
 * <ul>
 *   <li>getDayOfMonth()
 *   <li>getMonthValue()
 *   <li>getYear()
 *   <li>getHour()
 *   <li>getMinute()
 *   <li>getSecond()
 * </ul>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupportedLocalDateTimeMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-60 (SupportedLocalDateTimeMethods) failed: ";

  /** */
  private Object oidOfLocalDateTime1;

  /** */
  private Object oidOfLocalDateTime2;

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testDayOfMonth() {
    final String filter = "localDateTime.getDayOfMonth() == 12";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime1));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMonthValue() {
    final String filter = "localDateTime.getMonthValue() == 8";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime2));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testYear() {
    final String filter = "localDateTime.getYear() == 2017";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime1));
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime2));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testHour() {
    final String filter = "localDateTime.getHour() == 14";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime1));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
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
    final String filter = "localDateTime.getMinute() == 22";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime2));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
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
    final String filter = "localDateTime.getSecond() == 25";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<LocalDateTimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((LocalDateTimeSample) pm.getObjectById(oidOfLocalDateTime1));

      Query<LocalDateTimeSample> q = pm.newQuery(LocalDateTimeSample.class, filter);
      List<LocalDateTimeSample> results = q.executeList();
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
    addTearDownClass(LocalDateTimeSample.class);
    insertLocalDateTimeSampleData(getPM());
  }

  /** */
  private void insertLocalDateTimeSampleData(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      LocalDateTimeSample lds1 = new LocalDateTimeSample();
      lds1.setId(1);
      LocalDateTime localDateTime1 = LocalDateTime.of(2017, Month.SEPTEMBER, 12, 14, 10, 25);
      lds1.setLocalDateTime(localDateTime1);
      pm.makePersistent(lds1);

      LocalDateTimeSample lds2 = new LocalDateTimeSample();
      lds2.setId(2);
      LocalDateTime localDateTime2 = LocalDateTime.of(2017, Month.AUGUST, 20, 9, 22, 12);
      lds2.setLocalDateTime(localDateTime2);
      pm.makePersistent(lds2);

      tx.commit();
      oidOfLocalDateTime1 = pm.getObjectId(lds1);
      oidOfLocalDateTime2 = pm.getObjectId(lds2);
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
