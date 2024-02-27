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

package org.apache.jdo.tck.query.jdoql.subqueries;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Multiple Calls of addSubquery Replaces Previous Instance <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-51. <br>
 * <B>Assertion Description: </B> If the same value of variableDeclaration is used to add multiple
 * subqueries, the subquery replaces the previous subquery for the same named variable.
 */
public class MultipleCallsReplaceSubquery extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-51 (MultipleCallsReplaceSubquery) failed: ";

  /** */
  @Test
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
    // Query returning the weeklyhours of employee with id 1
    Query<Employee> tmp = pm.newQuery(Employee.class);
    tmp.setResult("this.weeklyhours");
    tmp.setFilter("this.id == 1");
    // Query returning the avg of weeklyhours of all employees
    Query<Employee> sub = pm.newQuery(Employee.class);
    sub.setResult("avg(this.weeklyhours)");
    Query<Employee> apiQuery = pm.newQuery(Employee.class);
    apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
    apiQuery.addSubquery(tmp, "double averageWeeklyhours", null);
    // second call of addSubquery using the same variable
    // should replace the previous setting, so apiQuery should
    // represent the query of singleStringJDOQL
    apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
    executeJDOQuery(
        ASSERTION_FAILED, pm, apiQuery, singleStringJDOQL, false, null, expectedResult, true);
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
