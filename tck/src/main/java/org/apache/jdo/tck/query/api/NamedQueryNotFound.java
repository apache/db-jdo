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

package org.apache.jdo.tck.query.api;

import javax.jdo.JDOUserException;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Named Query not Found. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-14. <br>
 * <B>Assertion Description: </B> If the metadata is not found in the above, a JDOUserException is
 * thrown.
 */
public class NamedQueryNotFound extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.5-14 (NamedQueryNotFound) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NamedQueryNotFound.class);
  }

  /** */
  public void testNegative() {
    try {
      getPM().newNamedQuery(Person.class, "nonExistingNamedQuery");
      fail(
          ASSERTION_FAILED
              + "The lookup of named query 'nonExistingNamedQuery' "
              + "is successful, though that named query is undefined.");
    } catch (JDOUserException e) {
    }
  }
}
