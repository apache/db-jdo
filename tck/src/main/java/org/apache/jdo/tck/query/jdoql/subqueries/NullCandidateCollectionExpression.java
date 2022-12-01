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

package org.apache.jdo.tck.query.jdoql.subqueries;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Null Candidate Collection Expression in addSubquery <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-53. <br>
 * <B>Assertion Description: </B> The candidateCollectionExpression is the expression from the outer
 * query that represents the candidates over which the subquery is evaluated. If the trimmed value
 * is the empty String, or the parameter is null, then the candidate collection is the extent of the
 * candidate class.
 */
public class NullCandidateCollectionExpression extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-53 (NullCandidateCollectionExpression) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NullCandidateCollectionExpression.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();

    List<IEmployee> expectedResult =
        getTransientCompanyModelInstancesAsList(
            IEmployee.class, "emp1", "emp2", "emp4", "emp5", "emp6", "emp7", "emp10");

    // select employees who work more than the average of all employees
    String singleStringJDOQL =
        "SELECT FROM "
            + Employee.class.getName()
            + " WHERE this.weeklyhours > "
            + "(SELECT AVG(e.weeklyhours) FROM "
            + Employee.class.getName()
            + " e)";

    // API query
    Query<Employee> sub = pm.newQuery(Employee.class);
    sub.setResult("avg(this.weeklyhours)");
    Query<Employee> apiQuery = pm.newQuery(Employee.class);
    apiQuery.setFilter("this.weeklyhours > averageWeeklyhours");
    // null candidate collection
    apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);

    apiQuery = pm.newQuery(Employee.class);
    apiQuery.setFilter("this.weeklyhours > averageWeeklyhours");
    // empty candidate collection
    apiQuery.addSubquery(sub, "double averageWeeklyhours", " ");
    executeJDOQuery(
        ASSERTION_FAILED, apiQuery, singleStringJDOQL, false, null, expectedResult, true);
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
