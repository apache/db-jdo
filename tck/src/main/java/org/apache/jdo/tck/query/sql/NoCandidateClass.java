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

package org.apache.jdo.tck.query.sql;

import java.util.Arrays;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> No Candidate Class. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.7-3. <br>
 * <B>Assertion Description: </B> SQL queries can be defined without a candidate class. These
 * queries can be found by name using the factory method newNamedQuery, specifying the class as
 * null, or can be constructed without a candidate class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoCandidateClass extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.7-3 (CandidateClass) failed: ";

  /** The array of valid SQL queries. */
  private static final String[] VALID_SQL_QUERIES = {"SELECT firstname, lastname FROM {0}.persons"};

  /** The expected results of valid SQL queries. */
  private final Object[] expectedResult = {
    Arrays.asList(
        new Object[] {
          new Object[] {"emp1First", "emp1Last"},
          new Object[] {"emp2First", "emp2Last"},
          new Object[] {"emp3First", "emp3Last"},
          new Object[] {"emp4First", "emp4Last"},
          new Object[] {"emp5First", "emp5Last"}
        })
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNamedQuery() {
    if (isSQLSupported()) {
      int index = 0;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        Query<Employee> query = pm.newNamedQuery(null, "SQLQuery");
        executeJDOQuery(
            ASSERTION_FAILED,
            pm,
            query,
            "Named SQL query",
            false,
            null,
            expectedResult[index],
            true);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNoCandidateClass() {
    if (isSQLSupported()) {
      int index = 0;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            null,
            true,
            null,
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
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
