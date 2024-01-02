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

package org.apache.jdo.tck.query.result;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Unique. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.11-1. <br>
 * <B>Assertion Description: </B> When the value of the Unique flag is true, then the result of a
 * query is a single value, with null used to indicate that none of the instances in the candidates
 * satisfied the filter. If more than one instance satisfies the filter, and the range is not
 * limited to one result, then execute throws a JDOUserException.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Unique extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.11-1 (Unique) failed: ";
  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive0() {
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(1l));

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 1",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
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
  public void testPositive1() {
    Object expected = null;
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.filter(cand.personid.eq(0l));

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid == 0",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
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
  public void testPositive2() {
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate();
      query.orderBy(cand.personid.asc());

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ Boolean.TRUE,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "personid ASCENDING",
              /*FROM*/ "0",
              /*TO*/ "1",
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative() {
    String singleString = "SELECT UNIQUE FROM " + Person.class.getName();
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<Person> query = pm.newQuery(Person.class);
      query.setUnique(true);
      executeJDOQuery(ASSERTION_FAILED, pm, query, singleString, false, null, null, false);

      query = pm.newQuery(singleString);
      executeJDOQuery(ASSERTION_FAILED, pm, query, singleString, false, null, null, false);
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
}
