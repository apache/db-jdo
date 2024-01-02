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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.query.OptionalSample;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Optional Fields. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-9 <br>
 * <B>Assertion Description: </B> Queries on fields of type java.util.Optional .
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupportedOptionalMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-9 (OptionalFields) failed: ";

  private static final int PC_ID = 1;
  private static final int PC_EMPTY_ID = 3;
  private static final int PC_NULL_ID = 4;
  private static final int REFERENCED_PC1_ID = 88;
  private static final int REFERENCED_PC2_ID = 99;
  private static final String STRING = "Hello JDO";
  private static final Integer INTEGER = 2016;
  private static final Date DATE = new Date(1000000000);
  private Object oidReferencedPC1;
  private Object oidReferencedPC2;
  private Object oidPC;
  private Object oidEmpty;
  private Object oidNull;

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQueriesWithPresence() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      // Matches 'optionalPC.isPresent() == true'
      checkQuery(pm, "optionalPC != null", oidPC, oidReferencedPC1);

      // matches !isPresent() but does NOT match Java 'optionalPC==null'
      checkQuery(pm, "optionalPC == null", oidEmpty, oidNull, oidReferencedPC2);

      // matches isPresent()
      checkQuery(pm, "!(optionalPC == null)", oidReferencedPC1, oidPC);

      // matches !isPresent()
      checkQuery(pm, "!(optionalPC != null)", oidReferencedPC2, oidEmpty, oidNull);

      checkQuery(pm, "optionalPC.get() != null", oidPC, oidReferencedPC1);
      checkQuery(pm, "optionalPC.get() == null", oidEmpty, oidNull, oidReferencedPC2);
      checkQuery(pm, "optionalPC.isPresent()", oidPC, oidReferencedPC1);
      checkQuery(pm, "!optionalPC.isPresent()", oidReferencedPC2, oidEmpty, oidNull);

      // querying non-PC 'Optional' fields
      checkOptionalForPresence(pm, "optionalString");
      checkOptionalForPresence(pm, "optionalDate");
      checkOptionalForPresence(pm, "optionalInteger");
    } finally {
      cleanupPM(pm);
    }
  }

  private void checkOptionalForPresence(PersistenceManager pm, String fieldName) {
    checkQuery(pm, fieldName + " != null", oidPC);
    checkQuery(pm, fieldName + " == null", oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);
    checkQuery(pm, "!(" + fieldName + " == null)", oidPC);
    checkQuery(
        pm, "!(" + fieldName + " != null)", oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);

    checkQuery(pm, fieldName + ".get() != null", oidPC);
    checkQuery(
        pm, fieldName + ".get() == null", oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);
    checkQuery(pm, fieldName + ".isPresent()", oidPC);
    checkQuery(
        pm,
        "!" + fieldName + ".isPresent()",
        oidEmpty,
        oidNull,
        oidReferencedPC1,
        oidReferencedPC2);
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testQueriesWithNavigation() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      checkQuery(pm, "optionalPC.id == " + REFERENCED_PC1_ID, oidPC);
      checkQuery(pm, "optionalPC.id != " + REFERENCED_PC1_ID, oidReferencedPC1);
      checkQuery(pm, "!(optionalPC.id == " + REFERENCED_PC1_ID + ")", oidReferencedPC1);
      checkQuery(pm, "optionalPC.get().id == " + REFERENCED_PC1_ID, oidPC);
      checkQuery(pm, "optionalPC.get().id != " + REFERENCED_PC1_ID, oidReferencedPC1);
      checkQuery(pm, "!(optionalPC.get().id == " + REFERENCED_PC1_ID + ")", oidReferencedPC1);
      checkQuery(pm, "optionalPC.optionalPC.isPresent()", oidPC);

      // The following reflects the changed behavior in JDO 3.2 in  the sense that
      // all instances are returned where either 'optionalPC.optionalPC==null' (not present)
      // or 'optionalPC==null' (the 'null' evaluates to 'null', which is followed until it is
      // evaluated in the 'isPresent()'). In other words, the query also returns all
      // objects that match '!(optionalPC.isPresent())'.
      checkQuery(
          pm,
          "!(optionalPC.optionalPC.isPresent())",
          oidReferencedPC1,
          oidReferencedPC2,
          oidEmpty,
          oidNull);

      // A query where 'optionalPC!=null' and 'optionalPC.optionalPC==null;
      // can be done as follows:
      checkQuery(
          pm, "optionalPC.isPresent() && !(optionalPC.optionalPC.isPresent())", oidReferencedPC1);

      checkQuery(pm, "optionalPC.optionalPC.id == " + REFERENCED_PC2_ID, oidPC);
      checkQuery(pm, "optionalPC.get().optionalPC.get().id == " + REFERENCED_PC2_ID, oidPC);

      // test with && operator
      checkQuery(
          pm,
          "!(optionalPC.isPresent() && optionalPC.id == " + REFERENCED_PC1_ID + ")",
          oidReferencedPC1,
          oidReferencedPC2,
          oidEmpty,
          oidNull);
    } finally {
      cleanupPM(pm);
    }
  }

  private void checkQuery(PersistenceManager pm, String filter, Object... resultOids) {
    QueryElementHolder<OptionalSample> qeh =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ OptionalSample.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ filter,
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ null);

    ArrayList<Object> expectedResults = new ArrayList<>();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (Object resultOid : resultOids) {
        expectedResults.add(pm.getObjectById(resultOid));
      }
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }

    executeAPIQuery(ASSERTION_FAILED, pm, qeh, expectedResults);
    executeSingleStringQuery(ASSERTION_FAILED, pm, qeh, expectedResults);
  }

  /** This methods tests Optional fields and parameters. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterOptional() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      OptionalSample osReferencedPC1 = getOptionalSampleById(pm, oidReferencedPC1);
      String paramDecl = "java.util.Optional op";
      Map<String, Object> paramValues = new HashMap<>();

      paramValues.put("op", Optional.of(osReferencedPC1));
      checkQuery(pm, "this.optionalPC == op", paramDecl, paramValues, new Object[] {oidPC});

      paramValues.put("op", Optional.of(DATE));
      checkQuery(pm, "this.optionalDate == op", paramDecl, paramValues, new Object[] {oidPC});

      paramValues.put("op", Optional.of(INTEGER));
      checkQuery(pm, "this.optionalInteger == op", paramDecl, paramValues, new Object[] {oidPC});

      paramValues.put("op", Optional.of(STRING));
      checkQuery(pm, "this.optionalString == op", paramDecl, paramValues, new Object[] {oidPC});
    } finally {
      cleanupPM(pm);
    }
  }

  /** This methods tests Optional fields and parameters with auto de-referencing. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterOptionalAutoDeref() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      OptionalSample osReferencedPC1 = getOptionalSampleById(pm, oidReferencedPC1);
      Map<String, Object> paramValues = new HashMap<>();

      paramValues.put("op", osReferencedPC1);
      checkQuery(
          pm,
          "this.optionalPC == op",
          OptionalSample.class.getName() + " op",
          paramValues,
          new Object[] {oidPC});

      paramValues.put("op", DATE);
      checkQuery(
          pm, "this.optionalDate == op", "java.util.Date op", paramValues, new Object[] {oidPC});

      paramValues.put("op", INTEGER);
      checkQuery(pm, "this.optionalInteger == op", "Integer op", paramValues, new Object[] {oidPC});

      paramValues.put("op", STRING);
      checkQuery(pm, "this.optionalString == op", "String op", paramValues, new Object[] {oidPC});
    } finally {
      cleanupPM(pm);
    }
  }

  private OptionalSample getOptionalSampleById(PersistenceManager pm, Object id) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      return (OptionalSample) pm.getObjectById(id);
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /** This methods tests that empty Optional fields and parameters matches with Optional.empty(). */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterOptionalWithEmptyFields() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      String paramDecl = "java.util.Optional op";
      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("op", Optional.empty());
      checkQuery(
          pm,
          "this.optionalPC == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalDate == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalInteger == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalString == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
    } finally {
      cleanupPM(pm);
    }
  }

  /** This methods tests that Optional fields and parameters matches with (Optional)null. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterOptionalWithNull() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      String paramDecl = "java.util.Optional op";
      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("op", null);
      checkQuery(
          pm,
          "this.optionalPC == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalDate == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalInteger == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalString == op",
          paramDecl,
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
    } finally {
      cleanupPM(pm);
    }
  }

  /** This methods tests that Optional fields and parameters matches with (Object)null. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterOptionalNull() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("op", null);
      checkQuery(
          pm,
          "this.optionalPC == op",
          OptionalSample.class.getName() + " op",
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalDate == op",
          "java.util.Date op",
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalInteger == op",
          "java.lang.Integer op",
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
      checkQuery(
          pm,
          "this.optionalString == op",
          "java.lang.String op",
          paramValues,
          new Object[] {oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
    } finally {
      cleanupPM(pm);
    }
  }

  /** This methods tests that Optional fields can be accessed in subqueries. */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSubqueries() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      String queryStr1 =
          "SELECT FROM "
              + OptionalSample.class.getName()
              + " WHERE "
              + "(select max(a.id) from "
              + OptionalSample.class.getName()
              + " a "
              + "where a.optionalPC.isPresent() ) == id";
      Object[] expectedResult1 = new Object[] {oidReferencedPC1};
      checkSingleStringQuery(pm, queryStr1, expectedResult1);

      String queryStr2 =
          "SELECT FROM "
              + OptionalSample.class.getName()
              + " WHERE "
              + "(select max(a.id) from "
              + OptionalSample.class.getName()
              + " a "
              + "where a.optionalPC.get() != null) == id";
      Object[] expectedResult2 = new Object[] {oidReferencedPC1};
      checkSingleStringQuery(pm, queryStr2, expectedResult2);
    } finally {
      cleanupPM(pm);
    }
  }

  /** This methods tests that Optional fields can be accessed in subqueries. */
  @Test
  public void testOptionalAggregation() {
    String clsName = OptionalSample.class.getName();

    String queryStr1 = "SELECT AVG(optionalInteger) FROM " + clsName;
    Query<?> q1 = pm.newQuery(queryStr1);
    executeJDOQuery(ASSERTION_FAILED, pm, q1, queryStr1, false, null, (double) INTEGER, true);

    String queryStr2 = "SELECT AVG(optionalInteger.get()) FROM " + clsName;
    Query<?> q2 = pm.newQuery(queryStr2);
    executeJDOQuery(ASSERTION_FAILED, pm, q2, queryStr2, false, null, (double) INTEGER, true);

    // return the object whose Integer is the same as the AVG of all
    // objects that have the Integer present.
    String queryStrSub1 =
        "SELECT FROM "
            + clsName
            + " WHERE "
            + "(select avg(a.optionalInteger) from "
            + clsName
            + " a "
            + "where a.optionalInteger.isPresent() ) == optionalInteger";
    Object[] expectedResult1 = new Object[] {oidPC};
    checkSingleStringQuery(pm, queryStrSub1, expectedResult1);

    String queryStrSub2 =
        "SELECT FROM "
            + clsName
            + " WHERE "
            + "(select avg(a.optionalInteger.get()) from "
            + clsName
            + " a "
            + "where a.optionalInteger.isPresent() ) == optionalInteger";
    Object[] expectedResult2 = new Object[] {oidPC};
    checkSingleStringQuery(pm, queryStrSub2, expectedResult2);

    // Find all where the average is the same as the integer value itself.
    // This returns ALL objects!!!
    String queryStrSub3 =
        "SELECT FROM "
            + clsName
            + " WHERE "
            + "(select avg(a.optionalInteger) from "
            + clsName
            + " a "
            + " ) == optionalInteger";
    Object[] expectedResult3 = new Object[] {oidPC};
    checkSingleStringQuery(pm, queryStrSub3, expectedResult3);
  }

  private void checkSingleStringQuery(
      PersistenceManager pm, String singleStringJDOQL, Object... resultOids) {
    ArrayList<Object> expectedResults = new ArrayList<>();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (Object resultOid : resultOids) {
        expectedResults.add(pm.getObjectById(resultOid));
      }
    } finally {
      if (tx.isActive()) tx.rollback();
    }

    Query<?> singleStringQuery = pm.newQuery(singleStringJDOQL);
    executeJDOQuery(
        ASSERTION_FAILED,
        pm,
        singleStringQuery,
        singleStringJDOQL,
        false,
        null,
        expectedResults,
        true);
  }

  private void checkQuery(
      PersistenceManager pm,
      String filter,
      String paramDecl,
      Map<String, Object> paramValues,
      Object[] result) {
    QueryElementHolder<OptionalSample> qeh =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ OptionalSample.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ filter,
            /*VARIABLES*/ null,
            /*PARAMETERS*/ paramDecl,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    ArrayList<Object> expectedResults = new ArrayList<>();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (Object o : result) {
        if (o instanceof String || o instanceof Date || o instanceof Integer) {
          expectedResults.add(o);
        } else {
          expectedResults.add(pm.getObjectById(o));
        }
      }
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }

    executeAPIQuery(ASSERTION_FAILED, pm, qeh, expectedResults);
    executeSingleStringQuery(ASSERTION_FAILED, pm, qeh, expectedResults);
  }

  /** Result class for queries on OptionalSample. */
  public static class ResultInfo {
    public long id;
    public String optionalString;

    public ResultInfo() {}

    public ResultInfo(long id, String optionalString) {
      this.id = id;
      this.optionalString = optionalString;
    }
  }

  /** Test Optional.orElse() in the SELECT clause of JDOQL queries. */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testOrElseInSELECT() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<?> q =
          pm.newQuery(
              "SELECT id, optionalString.orElse('NotPresent') FROM "
                  + OptionalSample.class.getName());
      q.setResultClass(ResultInfo.class);
      Collection<ResultInfo> c = (Collection<ResultInfo>) q.execute();
      if (c.size() != 5) {
        fail(ASSERTION_FAILED, "Wrong result count: " + c.size());
      }
      for (ResultInfo i : c) {
        switch ((int) i.id) {
          case PC_ID:
            if (!STRING.equals(i.optionalString)) {
              fail(ASSERTION_FAILED, "Wrong string value: " + i.optionalString);
            }
            break;
          case PC_EMPTY_ID:
          case PC_NULL_ID:
          case REFERENCED_PC1_ID:
          case REFERENCED_PC2_ID:
            Assertions.assertEquals("NotPresent", i.optionalString);
            break;
          default:
            fail(ASSERTION_FAILED, "Wrong object id: " + i.id);
        }
      }
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * This test assert that null-references are converted to Optional.empty() when loaded from the
   * database.
   */
  @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // Wrong result when executed in parallel
  // Date field was 'null'
  public void testPersistenceNotNull() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      OptionalSample osNotNull = (OptionalSample) pm.getObjectById(oidNull);

      if (osNotNull.getOptionalDate() == null) {
        fail(ASSERTION_FAILED, "Date field was 'null'");
      }
      if (osNotNull.getOptionalInteger() == null) {
        fail(ASSERTION_FAILED, "Integer field was 'null'");
      }
      if (osNotNull.getOptionalPC() == null) {
        fail(ASSERTION_FAILED, "optionalPC field was 'null'");
      }
      if (osNotNull.getOptionalString() == null) {
        fail(ASSERTION_FAILED, "String field was 'null'");
      }

      if (osNotNull.getOptionalDate().isPresent()) {
        fail(ASSERTION_FAILED, "Date field was present");
      }
      if (osNotNull.getOptionalInteger().isPresent()) {
        fail(ASSERTION_FAILED, "Integer field was present");
      }
      if (osNotNull.getOptionalPC().isPresent()) {
        fail(ASSERTION_FAILED, "optionalPC field was present");
      }
      if (osNotNull.getOptionalString().isPresent()) {
        fail(ASSERTION_FAILED, "String field was present");
      }
      tx.rollback();
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
    addTearDownClass(OptionalSample.class);
    insertOptionalSample(getPM());
  }

  private void insertOptionalSample(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      // Create two objects that are referenced by other objects, this allows
      // testing of navigation in queries.
      // The referencedPC1 will be referenced by 'osPC',
      // The referencedPC2 will be referenced by referencedPC1.
      OptionalSample referencedPC1 = new OptionalSample();
      referencedPC1.setId(REFERENCED_PC1_ID);
      pm.makePersistent(referencedPC1);
      oidReferencedPC1 = pm.getObjectId(referencedPC1);

      OptionalSample referencedPC2 = new OptionalSample();
      referencedPC2.setId(REFERENCED_PC2_ID);
      pm.makePersistent(referencedPC2);
      oidReferencedPC2 = pm.getObjectId(referencedPC2);

      referencedPC1.setOptionalPC(Optional.of(referencedPC2));

      OptionalSample osPC = new OptionalSample();
      osPC.setId(PC_ID);
      osPC.setOptionalPC(Optional.of(referencedPC1));
      osPC.setOptionalDate(Optional.of(DATE));
      osPC.setOptionalInteger(Optional.of(INTEGER));
      osPC.setOptionalString(Optional.of(STRING));
      pm.makePersistent(osPC);
      oidPC = pm.getObjectId(osPC);

      // use empty optionals
      OptionalSample osEmpty = new OptionalSample();
      osEmpty.setId(PC_EMPTY_ID);
      osEmpty.setOptionalPC(Optional.empty());
      osEmpty.setOptionalDate(Optional.empty());
      osEmpty.setOptionalInteger(Optional.empty());
      osEmpty.setOptionalString(Optional.empty());
      pm.makePersistent(osEmpty);
      oidEmpty = pm.getObjectId(osEmpty);

      // use null for optional fields
      OptionalSample osNull = new OptionalSample();
      osNull.setId(PC_NULL_ID);
      osNull.setOptionalPC(null);
      osNull.setOptionalDate(null);
      osNull.setOptionalInteger(null);
      osNull.setOptionalString(null);
      pm.makePersistent(osNull);
      oidNull = pm.getObjectId(osNull);

      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  @Override
  protected void localTearDown() {
    // set all references to null to allow deletion of (otherwise) referenced objects
    Transaction tx = getPM().currentTransaction();
    tx.begin();
    Query<OptionalSample> q = pm.newQuery(OptionalSample.class);
    for (OptionalSample os : q.executeList()) {
      os.setOptionalPC(Optional.empty());
    }
    tx.commit();

    super.localTearDown();
  }
}
