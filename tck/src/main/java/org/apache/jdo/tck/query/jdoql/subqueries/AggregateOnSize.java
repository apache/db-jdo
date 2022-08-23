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

package org.apache.jdo.tck.query.jdoql.subqueries;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test using an aggregate on a size expression <br>
 * <B>Keywords:</B> query aggregate size <br>
 * <B>Assertion ID:</B> <br>
 * <B>Assertion Description: </B>
 */
public class AggregateOnSize extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (AggregateOnSize) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AggregateOnSize.class);
  }

  /** */
  public void testMaxAndSizeInSubquery() {
    PersistenceManager pm = getPM();

    List expectedResult = getTransientCompanyModelInstancesAsList(new String[] {"dept1"});

    // Select departments with the maximum number of employees
    String singleStringJDOQL =
        "SELECT FROM "
            + Department.class.getName()
            + " WHERE this.employees.size() == "
            + "(SELECT MAX(d.employees.size()) FROM "
            + Department.class.getName()
            + " d)";

    // API query
    Query sub = pm.newQuery(Department.class);
    sub.setResult("MAX(this.employees.size())");
    Query apiQuery = pm.newQuery(Department.class);
    apiQuery.setFilter("this.employees.size() == number");
    apiQuery.addSubquery(sub, "long number", null);
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

    // API query against memory model
    List allDepartments = getAllPersistentInstances(pm, Department.class);
    apiQuery.setCandidates(allDepartments);
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

    // single String JDOQL
    Query singleStringQuery = pm.newQuery(singleStringJDOQL);
    executeJDOQuery(
        ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, false, null, expectedResult, true);
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
