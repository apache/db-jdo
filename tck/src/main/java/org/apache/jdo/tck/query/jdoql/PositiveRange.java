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

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Positive Range. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.8-1. <br>
 * <B>Assertion Description: </B> setRange(long fromIncl, long toExcl)
 */
public class PositiveRange extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.8-1 (PositiveRange) failed: ";

  @Test
  public void testPositive0() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.orderBy(cand.personid.asc());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
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
            /*FROM*/ 0,
            /*TO*/ 5,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  @Test
  public void testPositive1() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(Person.class, "emp1", "emp2", "emp3", "emp4");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.orderBy(cand.personid.asc());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
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
            /*FROM*/ 0,
            /*TO*/ 4,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  @Test
  public void testPositive2() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(Person.class, "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.orderBy(cand.personid.asc());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
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
            /*FROM*/ 1,
            /*TO*/ 5,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  @Test
  public void testPositive3() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(Person.class, "emp2", "emp3", "emp4");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.orderBy(cand.personid.asc());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
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
            /*FROM*/ 1,
            /*TO*/ 4,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
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
