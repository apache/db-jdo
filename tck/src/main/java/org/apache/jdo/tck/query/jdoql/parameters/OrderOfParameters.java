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

package org.apache.jdo.tck.query.jdoql.parameters;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * <B>Title:</B> Order of Parameters. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.13-3. <br>
 * <B>Assertion Description: </B> If implicit parameters are used, their order of appearance in the
 * query determines their order for binding to positional parameters for execution.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderOfParameters extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.13-3 (OrderOfParameters) failed: ";

  /** */
  @Test
  public void testAPIQuery() {
    // Do not use QueryElementHolder, because QueryElementHolder always uses a Map for parameter
    // values
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query<Person> query = null;
    Object result = null;
    try {
      tx.begin();
      String singleStringQuery =
          "select from org.apache.jdo.tck.pc.company.Person where firstname == :param1 & lastname == :param2";
      query = pm.newQuery(Person.class, "firstname == :param1 & lastname == :param2");
      result = query.execute("emp1First", "emp1Last");
      List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");
      checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
      tx.rollback();
    } finally {
      if (query != null) {
        query.close(result);
      }
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  public void testSingleStringAPIQuery() {
    // Do not use QueryElementHolder, because QueryElementHolder always uses a Map for parameter
    // values
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query<Person> query = null;
    Object result = null;
    try {
      tx.begin();
      String singleStringQuery =
          "select from org.apache.jdo.tck.pc.company.Person where firstname == :param1 & lastname == :param2";
      query = pm.newQuery(singleStringQuery);
      result = query.execute("emp1First", "emp1Last");
      List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");
      checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
      tx.commit();
    } finally {
      if (query != null) {
        query.close(result);
      }
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
