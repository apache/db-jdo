/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql.keywords;

import org.apache.jdo.tck.pc.query.JDOQLKeywordsAsFieldNames;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Keywords as field names. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-7. <br>
 * <B>Assertion Description: </B> Keywords are permitted as field names only if they are on the
 * right side of the "." in field access expressions.
 */
public class KeywordsAsFieldNames extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-7 (KeywordsAsFieldNames) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "select",
        /*INTO*/ null,
        /*FROM*/ JDOQLKeywordsAsFieldNames.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /**
   * The array of valid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] VALID_QUERIES = {
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "this.select",
        /*INTO*/ null,
        /*FROM*/ JDOQLKeywordsAsFieldNames.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(KeywordsAsFieldNames.class);
  }

  /** */
  public void testPositive() {
    for (QueryElementHolder<?> validQuery : VALID_QUERIES) {
      compileAPIQuery(ASSERTION_FAILED, validQuery, true);
      compileSingleStringQuery(ASSERTION_FAILED, validQuery, true);
    }
  }

  public void testNegative() {
    for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
      compileAPIQuery(ASSERTION_FAILED, invalidQuery, false);
      compileSingleStringQuery(ASSERTION_FAILED, invalidQuery, false);
    }
  }
}
