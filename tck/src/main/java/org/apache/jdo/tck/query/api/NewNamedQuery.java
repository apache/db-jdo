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

package org.apache.jdo.tck.query.api;

import java.util.Arrays;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> New Named Query. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-12. <br>
 * <B>Assertion Description: </B> Construct a new query instance with the given candidate class from
 * a named query.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewNamedQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.5-12 (NewNamedQuery) failed: ";

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    Arrays.asList(
        new Object[] {
          new FullName("emp1First", "emp1Last"),
          new FullName("emp2First", "emp2Last"),
          new FullName("emp3First", "emp3Last"),
          new FullName("emp4First", "emp4Last"),
          new FullName("emp5First", "emp5Last")
        }),
    new FullName("emp1First", "emp1Last")
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive0() {
    int index = 0;
    executeNamedQuery(Person.class, "validNotUnique", expectedResult[index], true);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive1() {
    int index = 1;
    executeNamedQuery(Person.class, "validUnique", expectedResult[index], true);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative() {
    executeNamedQuery(Person.class, "invalidUnique", null, false);
  }

  private void executeNamedQuery(
      Class<?> candidateClass, String namedQuery, Object expectedResult, boolean positive) {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<?> query = pm.newNamedQuery(candidateClass, namedQuery);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          query,
          "Named query " + namedQuery,
          false,
          null,
          expectedResult,
          positive);
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
