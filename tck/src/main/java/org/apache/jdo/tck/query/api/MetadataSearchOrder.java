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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PCClass;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Metadata Search Order. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-13. <br>
 * <B>Assertion Description: </B> If the named query is not found in already-loaded metadata, the
 * query is searched for using an algorithm. Files containing metadata are examined in turn until
 * the query is found. The order is based on the metadata search order for class metadata, but
 * includes files named based on the query name.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataSearchOrder extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-13 (MetadataSearchOrder) failed: ";

  /** The expected results of valid queries. */
  private final Object[] expectedResult = {
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp2", "emp3", "emp4", "emp5"),
    getTransientMylibInstancesAsList("pcClass1", "pcClass2"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp3", "emp4", "emp5"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp4", "emp5")
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPackageJDOInDefaultPackage() {
    int index = 0;
    executeNamedQuery(null, "packageJDOInDefaultPackage", expectedResult[index]);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPackageJDO() {
    int index = 1;
    executeNamedQuery(Person.class, "packageJDO", expectedResult[index]);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testClassJDO() {
    int index = 2;
    executeNamedQuery(PCClass.class, "classJDO", expectedResult[index]);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPackageORM() {
    int index = 3;
    executeNamedQuery(Person.class, "packageORM", expectedResult[index]);
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testClassJDOQuery() {
    int index = 4;
    executeNamedQuery(Person.class, "classJDOQuery", expectedResult[index]);
  }

  private void executeNamedQuery(
      Class<?> candidateClass, String namedQuery, Object expectedResult) {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<?> query = pm.newNamedQuery(candidateClass, namedQuery);
      executeJDOQuery(
          ASSERTION_FAILED,
          pm,
          query,
          "Named query " + namedQuery,
          false,
          null,
          expectedResult,
          true);
    } finally {
      cleanupPM(pm);
    }
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    addTearDownClass(MylibReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
    loadAndPersistMylib(getPM());
  }
}
