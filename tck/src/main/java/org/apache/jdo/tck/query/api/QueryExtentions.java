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

import java.util.HashMap;
import java.util.Map;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Query Extentions. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.9-1. <br>
 * <B>Assertion Description: </B> Some JDO vendors provide extensions to the query, and these
 * extensions must be set in the query instance prior to execution.
 */
public class QueryExtentions extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.9-1 (QueryExtentions) failed: ";

  private static final String SINGLE_STRING_QUERY = "SELECT FROM " + Person.class.getName();

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5")
  };

  /** */
  @SuppressWarnings("unchecked")
  @Test
  public void testPositive() {
    int index = 0;
    Query<Person> query = getPM().newQuery(SINGLE_STRING_QUERY);
    Map<String, String> extentions = new HashMap<>();
    extentions.put("unknown key 1", "unknown value 1");
    query.setExtensions(extentions);
    query.addExtension("unknown key 2", "unknown value 2");
    executeJDOQuery(
        ASSERTION_FAILED, pm, query, SINGLE_STRING_QUERY, false, null, expectedResult[index], true);
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
