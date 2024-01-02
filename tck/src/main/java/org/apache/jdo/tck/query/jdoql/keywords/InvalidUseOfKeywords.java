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
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Invalid uses of keywords. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-6. <br>
 * <B>Assertion Description: </B> Keywords must not be used as package names, class names, parameter
 * names, or variable names in queries.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvalidUseOfKeywords extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-6 (InvalidUseOfKeywords) failed: ";

  /** The array of invalid single string queries. */
  private static final String[] INVALID_SINGLE_STRING_QUERIES = {
    "SELECT INTO range.PersonResult FROM org.apache.jdo.tck.pc.company.Person",
    "SELECT INTO range FROM org.apache.jdo.tck.pc.company.Person",
    "SELECT FROM select.Person",
    "SELECT FROM select",
    "SELECT FROM org.apache.jdo.tck.pc.company.Person PARAMETERS int this",
    "SELECT FROM org.apache.jdo.tck.pc.company.Person VARIABLES long this"
  };

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Person.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ "int this",
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Person.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ "long this",
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative1() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (String invalidSingleStringQuery : INVALID_SINGLE_STRING_QUERIES) {
        compileSingleStringQuery(ASSERTION_FAILED, pm, invalidSingleStringQuery, false);
      }
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative2() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
        compileAPIQuery(ASSERTION_FAILED, pm, invalidQuery, false);
        compileSingleStringQuery(ASSERTION_FAILED, pm, invalidQuery, false);
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
