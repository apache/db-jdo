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

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Declare Imports <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-8. <br>
 * <B>Assertion Description: </B> <code>Query.declareImports(String imports)</code> binds the import
 * statements to the query instance. All imports must be declared in the same method call, and the
 * imports must be separated by semicolons.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeclareImports extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-8 (DeclareImports) failed: ";

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareImports01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareImports("import java.lang.Integer");
      query.declareParameters("Integer param");
      query.setFilter("y == param");
      Object results = query.execute(Integer.valueOf(2));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "y == param", results, expected);

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareImports02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<Department> query = pm.newQuery(Department.class);
      query.setCandidates(pm.getExtent(Department.class, false));
      query.declareImports("import org.apache.jdo.tck.pc.company.Employee");
      query.declareVariables("Employee e");
      query.setFilter("employees.contains(e) && e.firstname==\"Michael\"");
      Object results = query.execute();

      // Just check whether query with import declaration compiles

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareImports03() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<Company> query = pm.newQuery(Company.class);
      query.setCandidates(pm.getExtent(Company.class, false));
      query.declareImports(
          "import org.apache.jdo.tck.pc.company.Employee; import org.apache.jdo.tck.pc.company.Department");
      query.declareVariables("Department d; Employee e");
      query.setFilter(
          "departments.contains(d) && (d.employees.contains(e) && e.firstname==\"Michael\")");
      Object results = query.execute();

      // Just check whether query with import declarations compiles

      tx.commit();
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
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
  }
}
