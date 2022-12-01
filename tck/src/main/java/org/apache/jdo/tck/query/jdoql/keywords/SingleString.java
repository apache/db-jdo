/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql.keywords;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Single string query. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.13-1. <br>
 * <B>Assertion Description: </B> The String version of Query represents all query elements using a
 * single string. The string contains the following structure:
 */
public class SingleString extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.13-1 (SingleString) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SingleString.class);
  }

  /** */
  public void testPositive() {
    Object expected =
        Arrays.asList(
            new FullName("emp1First", "emp1Last"),
            new FullName("emp2First", "emp2Last"),
            new FullName("emp5First", "emp5Last"));

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("limit", new BigDecimal("2000"));

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.FALSE,
            /*RESULT*/ "firstname AS firstName, lastname AS lastName",
            /*INTO*/ FullName.class,
            /*FROM*/ FullTimeEmployee.class,
            /*EXCLUDE*/ Boolean.TRUE,
            /*WHERE*/ "salary > 1000 & projects.contains(p) & p.budget > limit",
            /*VARIABLES*/ "Project p",
            /*PARAMETERS*/ "BigDecimal limit",
            /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Project; "
                + "import java.math.BigDecimal",
            /*GROUP BY*/ "firstname, lastname HAVING lastname.startsWith('emp')",
            /*ORDER BY*/ "lastname ASCENDING",
            /*FROM*/ 0,
            /*TO*/ 3,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
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
