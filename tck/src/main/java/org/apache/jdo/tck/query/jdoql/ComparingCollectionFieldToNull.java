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

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Comparing a Collection Field to Null <br>
 * <B>Keywords:</B> query nullcollection <br>
 * <B>Assertion ID:</B> A14.6.2-36. <br>
 * <B>Assertion Description: </B> For datastores that support <code>null</code> values for <code>
 * Collection</code> types, it is valid to compare the field to <code>null</code>. Datastores that
 * do not support <code>null</code> values for <code>Collection</code> types will return <code>false
 * </code> if the query compares the field to <code>null</code>.
 */
public class ComparingCollectionFieldToNull extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-36 (ComparingCollectionFieldToNull) failed: ";

  /** The expected results of valid queries. */
  private List<Employee> expectedResult;

  /** */
  @Test
  public void testPositive() {
    JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.filter(cand.personid.eq(1L).and(cand.projects.eq(null)));

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1 && projects == null",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, pm, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expectedResult);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
    Employee employee = getPersistentCompanyModelInstance(Employee.class, "emp1");
    expectedResult =
        // emp1 should be in the query result set,
        // if the JDO Implentation supports null values for Collections
        getTransientCompanyModelInstancesAsList(
            Employee.class, isNullCollectionSupported() ? new String[] {"emp1"} : new String[] {});
    if (isNullCollectionSupported()) {
      getPM().currentTransaction().begin();
      employee.setProjects(null);
      getPM().currentTransaction().commit();
    }
  }
}
