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

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.pc.mylib.QPrimitiveTypes;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Character and String Literals. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-42. <br>
 * <B>Assertion Description: </B> There is no distinction made between character literals and String
 * literals. Single character String literals can be used wherever character literals are permitted.
 * String literals are allowed to be delimited by single quote marks or double quote marks. This
 * allows String literal filters to use single quote marks instead of escaped double quote marks.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CharacterAndStringLiterals extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-42 (CharacterAndStringLiterals) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ PrimitiveTypes.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "charNotNull == 'O.'",
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ PrimitiveTypes.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "charNotNull == \"O.\"",
        /*VARIABLES*/ null,
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
  public void testPositive1() {
    Object expected = getTransientMylibInstancesAsList("primitiveTypesCharacterStringLiterals");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<PrimitiveTypes> query = pm.newJDOQLTypedQuery(PrimitiveTypes.class);
      QPrimitiveTypes cand = QPrimitiveTypes.candidate("this");
      query.filter(cand.stringNull.startsWith("Even").or(cand.charNotNull.eq('0')));

      QueryElementHolder<PrimitiveTypes> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ PrimitiveTypes.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "stringNull.startsWith('Even') || charNotNull == 'O'",
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
    Object expected = getTransientMylibInstancesAsList("primitiveTypesCharacterStringLiterals");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<PrimitiveTypes> query = pm.newJDOQLTypedQuery(PrimitiveTypes.class);
      QPrimitiveTypes cand = QPrimitiveTypes.candidate("this");
      query.filter(cand.stringNull.startsWith("Even").or(cand.charNotNull.eq('0')));

      QueryElementHolder<PrimitiveTypes> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ PrimitiveTypes.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "stringNull.startsWith(\"Even\") || charNotNull == \"O\"",
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
  public void testNegative() {
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

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(MylibReader.getTearDownClasses());
    loadAndPersistMylib(getPM());
  }
}
