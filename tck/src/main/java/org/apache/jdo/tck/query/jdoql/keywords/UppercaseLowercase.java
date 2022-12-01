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

import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Keywords in uppercase and lowercase. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.13-2. <br>
 * <B>Assertion Description: </B> Keywords, identified above in bold, are either all upper-case or
 * all lower-case. Keywords cannot be mixed case.
 */
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

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(UppercaseLowercase.class);
  }

  /** */
  public void testPositive() {
    for (String validSingleStringQuery : VALID_SINGLE_STRING_QUERIES) {
      compileSingleStringQuery(ASSERTION_FAILED, validSingleStringQuery, true);
    }
  }

  public void testNegitve() {
    for (String invalidSingleStringQuery : INVALID_SINGLE_STRING_QUERIES) {
      compileSingleStringQuery(ASSERTION_FAILED, invalidSingleStringQuery, false);
    }
  }
}
