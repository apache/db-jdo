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

import javax.jdo.JDOUserException;
import org.apache.jdo.tck.pc.query.NamedQueriesSample;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> NamedQueryRepeatableAnnotations <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> Unknown <br>
 * <B>Assertion Description: </B> We test use of @Query where multiple have been defined in a class,
 * so reliant on Java 8 @Repeatable.
 */
public class NamedQueryRepeatableAnnotations extends QueryTest {

  private static final String ASSERTION_FAILED =
      "Assertion A19.?? (NamedQueryRepeatableAnnotations) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NamedQueryRepeatableAnnotations.class);
  }

  /** */
  public void testPositive() {
    try {
      getPM().newNamedQuery(NamedQueriesSample.class, "NameIsJohn");
    } catch (JDOUserException e) {
      fail(ASSERTION_FAILED + "Lookup of query failed but should have been found");
    }
    try {
      getPM().newNamedQuery(NamedQueriesSample.class, "NameIsFred");
    } catch (JDOUserException e) {
      fail(ASSERTION_FAILED + "Lookup of query failed but should have been found");
    }
  }
}
