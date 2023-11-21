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
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Declare Variables <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-9. <br>
 * <B>Assertion Description: </B> <code>Query.declareVariables(String variables)</code> binds the
 * unbound variable statements to the query instance. This method defines the types and names of
 * variables that will be used in the filter but not provided as values by the <code>execute</code>
 * method.
 */
public class DeclareVariables extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-9 (DeclareVariables) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTestDeclareVariables01(pm);
    runTestDeclareVariables02(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void runTestDeclareVariables01(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<Department> query = pm.newQuery(Department.class);
      query.setCandidates(pm.getExtent(Department.class, false));
      query.declareVariables("org.apache.jdo.tck.pc.company.Employee e");
      query.setFilter("employees.contains(e) && e.firstname==\"Michael\"");
      Object results = query.execute();

      // Just check whether query compiles

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestDeclareVariables02(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<Department> query = pm.newQuery(Department.class);
      query.setCandidates(pm.getExtent(Department.class, false));
      query.declareVariables(
          "org.apache.jdo.tck.pc.company.Employee e; org.apache.jdo.tck.pc.company.Project p;");
      query.setFilter("employees.contains(e) && (e.projects.contains(p) && p.projid == 1)");
      Object results = query.execute();

      // Just check whether query compiles

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
