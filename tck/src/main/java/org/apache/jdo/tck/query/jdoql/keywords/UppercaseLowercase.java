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

package org.apache.jdo.tck.query.jdoql.keywords;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Keywords in uppercase and lowercase. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.13-2. <br>
 * <B>Assertion Description: </B> Keywords, identified above in bold, are either all upper-case or
 * all lower-case. Keywords cannot be mixed case.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UppercaseLowercase extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.13-2 (UppercaseLowercase) failed: ";

  /** The array of valid single string queries. */
  private static final String[] VALID_SINGLE_STRING_QUERIES = {
    "SELECT FROM org.apache.jdo.tck.pc.company.Person",
    "select from org.apache.jdo.tck.pc.company.Person",
    "select FROM org.apache.jdo.tck.pc.company.Person",
  };

  /** The array of invalid single string queries. */
  private static final String[] INVALID_SINGLE_STRING_QUERIES = {
    "SeLeCt FrOm org.apache.jdo.tck.pc.company.Person"
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (String validSingleStringQuery : VALID_SINGLE_STRING_QUERIES) {
        compileSingleStringQuery(ASSERTION_FAILED, pm, validSingleStringQuery, true);
      }
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegitve() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (String invalidSingleStringQuery : INVALID_SINGLE_STRING_QUERIES) {
        compileSingleStringQuery(ASSERTION_FAILED, pm, invalidSingleStringQuery, false);
      }
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
}
