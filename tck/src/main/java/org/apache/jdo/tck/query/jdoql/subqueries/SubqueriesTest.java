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
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryTest;

/** Superclass for all subquery test classes. */
public abstract class SubqueriesTest extends QueryTest {

  /** */
  public static final String SUBQUERIES_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForSubqueriesTests.xml";

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return SUBQUERIES_TEST_COMPANY_TESTDATA;
  }

  /**
   * Helper method retuning all Employee instances.
   *
   * @param pm the PersistenceManager
   * @return a List including all persistent Employee instances
   */
  protected List<Employee> getAllEmployees(PersistenceManager pm) {
    return getAllPersistentInstances(pm, Employee.class);
  }

  /**
   * Helper method retuning all persistent instances of the specified class. Note, this methods
   * executes a JDO query in a new transaction.
   *
   * @param pm the PersistenceManager
   * @param pcClass the persistent capable class
   * @return a List including all persistent instances of the specified class.
   */
  protected <T> List<T> getAllPersistentInstances(PersistenceManager pm, Class<T> pcClass) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<T> all = pm.newQuery(pcClass).executeList();
      tx.commit();
      return all;
    } finally {
      if ((tx != null) && tx.isActive()) {
        tx.rollback();
      }
    }
  }
}
