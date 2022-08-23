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

package org.apache.jdo.tck.query.jdoql;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.fieldtypes.AllTypes;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

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
public class OrderingSpecification extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.6-1 (OrderingSpecification) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(OrderingSpecification.class);
  }

  /** */
  public void testPositiveCompanyQueries0() {
    // nulls first
    Object expected =
        getTransientCompanyModelInstancesAsList(
            new String[] {
              "dentalIns99", "dentalIns1", "dentalIns2", "dentalIns3", "dentalIns4", "dentalIns5"
            });

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
    query.orderBy(cand.lifetimeOrthoBenefit.asc().nullsFirst());

    QueryElementHolder holder =
        new QueryElementHolder(
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testPositiveCompanyQueries1() {
    // nulls last
    Object expected =
        getTransientCompanyModelInstancesAsList(
            new String[] {
              "dentalIns1", "dentalIns2", "dentalIns3", "dentalIns4", "dentalIns5", "dentalIns99"
            });

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
    query.orderBy(cand.lifetimeOrthoBenefit.asc().nullsLast());

    QueryElementHolder holder =
        new QueryElementHolder(
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();

    runTestOrderingSpecification01(pm);
    runTestOrderingSpecification02(pm);
    checkOrderingTypes(pm);
  }

  /** */
  void runTestOrderingSpecification01(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test OrderingSpecification01() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery();
      query.setClass(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x ascending");
      Object results = query.execute();

      // check query result
      printOutput(results, inserted);
      checkQueryResultWithOrder(ASSERTION_FAILED, "null", results, inserted);
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

      Query query = pm.newQuery();
      query.setClass(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setOrdering("x descending");
      Object results = query.execute();

      // check query result
      List expected = new ArrayList();
      ListIterator li = inserted.listIterator(inserted.size());
      // construct expected results by iterating inserted objects backwards
      while (li.hasPrevious()) {
        Object obj = li.previous();
        expected.add(obj);
      }
      expected = getFromInserted(expected);
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
    Class clazz = PCPoint.class;
    try {
      tx.begin();

      Query query = pm.newQuery();
      query.setClass(AllTypes.class);
      query.setCandidates(pm.getExtent(AllTypes.class, false));

      StringBuffer buffer = new StringBuffer();
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
