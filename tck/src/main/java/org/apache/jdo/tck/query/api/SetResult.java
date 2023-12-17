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
import javax.jdo.JDOUserException;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * <B>Title:</B> Set Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-16. <br>
 * <B>Assertion Description: </B> void setResult (String result); Specify the results of the query
 * if not instances of the candidate class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-16 (SetResult) failed: ";

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    Arrays.asList("emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last")
  };

  /** */
  @Test
  public void testPositive() {
    int index = 0;
    Query<Person> query = getPM().newQuery(Person.class);
    query.setResult("lastname");
    String singleStringQuery = "SELECT lastname FROM Person";
    executeJDOQuery(
        ASSERTION_FAILED, query, singleStringQuery, false, null, expectedResult[index], true);
  }

  /** */
  @Test
  public void testNegative() {
    Query<Person> query = getPM().newQuery(Person.class);
    try {
      query.setResult("noname");
      query.compile();
      fail(
          ASSERTION_FAILED
              + "Compilation for query "
              + "'SELECT noname FROM Person' "
              + "succeeded, though the result clause is invalid.");
    } catch (JDOUserException ignored) {
    }
  }

  @BeforeAll
  @Override
  public void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  public void tearDown() {
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
