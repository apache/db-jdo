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

package org.apache.jdo.tck.query.jdoql.variables;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.query.Expression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Variables of type String. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-2<br>
 * <B>Assertion Description: An element of the candidate collection is returned in the result if:
 * ... for all variables there exists a value for which the filter expression evaluates to true.</B>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringVariables extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-2 (StringVariables) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org.apache.jdo.tck.pc.company.data.SampleQueryTestData";

  private static final String COLLECTION_STRING_VARIABLE_SSQ =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where languages.contains(lang) && lang == 'German' variables String lang";

  private static final String MAP_KEY_STRING_VARIABLE_SSQ =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where phoneNumbers.containsKey(key) && key == 'home' variables String key";

  private static final String MAP_VALUE_STRING_VARIABLE_SSQ =
      "select from org.apache.jdo.tck.pc.company.FullTimeEmployee "
          + "where phoneNumbers.containsValue(value) && value == '1111' variables String value";

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void collectionStringVariable() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "languages.contains(lang) && lang == 'German'")) {
        q.declareVariables("String lang");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, COLLECTION_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void collectionStringVariableSSQ() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(COLLECTION_STRING_VARIABLE_SSQ)) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, COLLECTION_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection speaking
   * German (i.e. the language set includes the string "German").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void collectionStringVariableTypedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp5");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        Expression<String> lang = q.variable("lang", String.class);
        q.filter(cand.languages.contains(lang).and(lang.eq("German")));
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, COLLECTION_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * home phone number (i.e. the phoneNumbers map includes an entry with key "home").
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapKeyStringVariable() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(FullTimeEmployee.class, "phoneNumbers.containsKey(key) && key == 'home'")) {
        q.declareVariables("String key");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, MAP_KEY_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * home phone number (i.e. the phoneNumbers map includes an entry with key "home").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapKeyStringVariableSSQ() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (Query<FullTimeEmployee> q = pm.newQuery(MAP_KEY_STRING_VARIABLE_SSQ)) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, MAP_KEY_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * home phone number (i.e. the phoneNumbers map includes an entry with key "home").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapKeyStringVariableTypedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1", "emp2", "emp5");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        Expression<String> key = q.variable("key", String.class);
        q.filter(cand.phoneNumbers.containsKey(key).and(key.eq("home")));
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, MAP_KEY_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * phone number "1111" (i.e. the phoneNumbers map includes an entry with value "1111").
   */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapValueStringVariable() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q =
          pm.newQuery(
              FullTimeEmployee.class, "phoneNumbers.containsValue(value) && value == '1111'")) {
        q.declareVariables("String value");
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, MAP_VALUE_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * phone number "1111" (i.e. the phoneNumbers map includes an entry with value "1111").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapValueStringVariableSSQ() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (Query<FullTimeEmployee> q = pm.newQuery(MAP_VALUE_STRING_VARIABLE_SSQ)) {
        List<FullTimeEmployee> emps = (List<FullTimeEmployee>) q.execute();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, MAP_VALUE_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Navigation through multi-valued field.
   *
   * <p>This query selects all FullTimeEmployee instances from the candidate collection having a
   * phone number "1111" (i.e. the phoneNumbers map includes an entry with value "1111").
   */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void mapValueStringVariableTypedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<FullTimeEmployee> expected =
          getTransientCompanyModelInstancesAsList(FullTimeEmployee.class, "emp1");
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        Expression<String> value = q.variable("value", String.class);
        q.filter(cand.phoneNumbers.containsValue(value).and(value.eq("1111")));
        List<FullTimeEmployee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, MAP_VALUE_STRING_VARIABLE_SSQ, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
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
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
  }
}
