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
 * <B>Title:</B> Handling of Null Collections and isEmpty in Queries <br>
 * <B>Keywords:</B> query nullcollection <br>
 * <B>Assertion ID:</B> A14.6.2-34. <br>
 * <B>Assertion Description: </B> <code>null</code>-valued fields of <code>Collection</code> types
 * are treated as if they were empty and <code>isEmpty</code> returns <code>true</code>.
 */
public class NullCollectionsAndIsEmpty extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-34 (NullCollectionsAndIsEmpty) failed: ";

  /** */
  @Test
  public void testPositive() {
    // isEmpty
    List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp1");

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.filter(cand.personid.eq(1L).and(cand.projects.isEmpty()));

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1 && projects.isEmpty()",
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
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
    getPM().currentTransaction().begin();
    Employee emp1 = getPersistentCompanyModelInstance(pm, Employee.class, "emp1");
    emp1.setProjects(null);
    getPM().currentTransaction().commit();
  }
}
