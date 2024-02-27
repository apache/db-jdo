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

package org.apache.jdo.tck.query.jdoql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Ordering Specification <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.6-1. <br>
 * <B>Assertion Description: </B> The <code>Query</code> ordering specification includes a list of
 * one or more ordering declarations separated by commas. Each ordering declaration includes an
 * expression whose type is one of:
 *
 * <UL>
 *   <LI>primitive types except <code>boolean</code>
 *   <LI>wrapper types except <code>Boolean</code>
 *   <LI><code>BigDecimal</code>
 *   <LI><code>BigInteger</code>
 *   <LI><code>String</code>
 *   <LI><code>Date</code>
 * </UL>
 *
 * followed by one of the following words: <code>&quot;ascending</code>&quot; or <code>
 * &quot;descending</code>&quot; which indicates the ordering of the values for that expression.
 * Ordering might be specified including navigation. The name of the field to be used in ordering
 * via navigation through single-valued fields is specified by the Java language syntax of <code>
 * field_name.field_name....field_name</code>.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderingSpecification extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.6-1 (OrderingSpecification) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveCompanyQueries0() {
    // nulls first
    List<DentalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            DentalInsurance.class,
            "dentalIns99",
            "dentalIns1",
            "dentalIns2",
            "dentalIns3",
            "dentalIns4",
            "dentalIns5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate();
      query.orderBy(cand.lifetimeOrthoBenefit.asc().nullsFirst());

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lifetimeOrthoBenefit ascending nulls first",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositiveCompanyQueries1() {
    // nulls last
    List<DentalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            DentalInsurance.class,
            "dentalIns1",
            "dentalIns2",
            "dentalIns3",
            "dentalIns4",
            "dentalIns5",
            "dentalIns99");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate();
      query.orderBy(cand.lifetimeOrthoBenefit.asc().nullsLast());

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.lifetimeOrthoBenefit ascending nulls last",
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      runTestOrderingSpecification01(pm);
      runTestOrderingSpecification02(pm);
      checkOrderingTypes(pm);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  void runTestOrderingSpecification01(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test OrderingSpecification01() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x ascending");
      Object results = query.execute();

      // check query result
      printOutput(results, transientPCPoints);
      checkQueryResultWithOrder(ASSERTION_FAILED, "null", results, transientPCPoints);
      if (debug) logger.debug("Test OrderingSpecification01(): Passed");

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestOrderingSpecification02(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test OrderingSpecification02() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x descending");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>(transientPCPoints);
      Collections.reverse(expected);
      printOutput(results, expected);
      checkQueryResultWithOrder(ASSERTION_FAILED, "null", results, expected);
      if (debug) logger.debug("Test OrderingSpecification02(): Passed");

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void checkOrderingTypes(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<AllTypes> query = pm.newQuery(AllTypes.class);
      query.setCandidates(pm.getExtent(AllTypes.class, false));

      StringBuilder buffer = new StringBuilder();
      buffer.append("  fld_byte ascending");
      buffer.append(", fld_char ascending");
      buffer.append(", fld_double ascending");
      buffer.append(", fld_float ascending");
      buffer.append(", fld_int ascending");
      buffer.append(", fld_long ascending");
      buffer.append(", fld_short ascending");
      buffer.append(", fld_Byte descending");
      buffer.append(", fld_Character descending");
      buffer.append(", fld_Double descending");
      buffer.append(", fld_Float ascending");
      buffer.append(", fld_Integer descending");
      buffer.append(", fld_Long descending");
      buffer.append(", fld_Short descending");
      buffer.append(", fld_String descending");
      buffer.append(", fld_Date descending");
      buffer.append(", fld_BigDecimal descending");
      buffer.append(", fld_BigInteger ascending");
      query.setOrdering(buffer.toString());
      Object results = query.execute();

      // Just check whether query compiles

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
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }
}
