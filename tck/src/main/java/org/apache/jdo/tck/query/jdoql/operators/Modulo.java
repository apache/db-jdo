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

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Modulo operator. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-40. <br>
 * <B>Assertion Description: </B> modulo operator
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Modulo extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.2-40 (Modulo) failed: ";

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp2", "emp4");
    try {
      JDOQLTypedQuery<Person> query = pm.newJDOQLTypedQuery(Person.class);
      QPerson cand = QPerson.candidate("this");
      query.filter(cand.personid.mod(2).eq(0L));

      // Import Department twice
      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "personid % 2 == 0",
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
      // DataNucleus: UnsupportedOperationException: Dont currently support operator  %  in JDOQL
      // conversion
      // executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPostiveUsingPrimitiveTypes() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      List<PrimitiveTypes> instance4 = pm.newQuery(PrimitiveTypes.class, "id == 10").executeList();

      runSimplePrimitiveTypesQuery("id % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("byteNotNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("shortNotNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("intNotNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("longNotNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("byteNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("shortNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("intNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("longNull % 10 == 0", pm, instance4, ASSERTION_FAILED);
      runSimplePrimitiveTypesQuery("bigInteger % 10 == 0", pm, instance4, ASSERTION_FAILED);

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
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    addTearDownClass(PrimitiveTypes.class);
    PersistenceManager pm = getPM();
    loadAndPersistCompanyModel(pm);
    loadAndPersistPrimitiveTypes(pm);
  }
}
