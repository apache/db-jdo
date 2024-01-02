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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.query.TimeSample;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Supported Time methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-47. <br>
 * <B>Assertion Description: </B> New supported Time methods:
 *
 * <ul>
 *   <li>getHour()
 *   <li>getMinute()
 *   <li>getSecond()
 * </ul>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupportedTimeMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-47 (SupportedTimeMethods) failed: ";

  /** */
  private Object oidOfTime1;

  /** */
  private Object oidOfTime2;

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testHour() {
    final String filter = "time.getHour() == 10";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Collection<TimeSample> expectedResult = new ArrayList<>();
      expectedResult.add(pm.getObjectById(TimeSample.class, oidOfTime1));

      Query<TimeSample> q = pm.newQuery(TimeSample.class);
      q.setFilter(filter);
      List<TimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMinute() {
    final String filter = "time.getMinute() == 15";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Collection<TimeSample> expectedResult = new ArrayList<>();
      expectedResult.add((TimeSample) pm.getObjectById(oidOfTime2));

      Query<TimeSample> q = pm.newQuery(TimeSample.class);
      q.setFilter(filter);
      List<TimeSample> results = q.executeList();
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSecond() {
    final String filter = "time.getSecond() == 45";
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Collection<TimeSample> expectedResult = new ArrayList<>();
      expectedResult.add(pm.getObjectById(TimeSample.class, oidOfTime2));

      Query<TimeSample> q = pm.newQuery(TimeSample.class);
      q.setFilter(filter);
      List<TimeSample> results = q.executeList();
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
    addTearDownClass(TimeSample.class);
    insertTimeSampleData(getPM());
  }

  /** */
  @SuppressWarnings("deprecation")
  private void insertTimeSampleData(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      TimeSample ts1 = new TimeSample();
      ts1.setId(1);
      Time time = new Time(0);
      time.setHours(10);
      time.setMinutes(0);
      time.setSeconds(0);
      ts1.setTime(time);
      pm.makePersistent(ts1);

      TimeSample ts2 = new TimeSample();
      ts2.setId(2);
      Time time2 = new Time(0);
      time2.setHours(16);
      time2.setMinutes(15);
      time2.setSeconds(45);
      ts2.setTime(time2);
      pm.makePersistent(ts2);
      tx.commit();
      oidOfTime1 = pm.getObjectId(ts1);
      oidOfTime2 = pm.getObjectId(ts2);
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
