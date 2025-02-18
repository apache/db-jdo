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

import java.util.List;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> SampleModifyQueries <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> This test class runs the example modify queries from the JDO
 * specification.
 */
public class SampleModifyQueries extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (SampleModifyQueries) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";

  /**
   * Deleting Multiple Instances.
   *
   * <p>This query deletes all Employees who make more than the parameter salary.
   */
  @Test
  public void testQuery21() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
      q.declareParameters("Double sal");
      q.deletePersistentAll(30000.);
      tx.commit();

      tx.begin();
      Query<FullTimeEmployee> allQuery = pm.newQuery(FullTimeEmployee.class);
      List<FullTimeEmployee> allFTE = allQuery.executeList();
      if (!allFTE.isEmpty()) {
        fail(
            ASSERTION_FAILED,
            "All FullTimeEmployee instances should have been deleted,"
                + " there are still "
                + allFTE.size()
                + " instances left.");
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
  }
}
