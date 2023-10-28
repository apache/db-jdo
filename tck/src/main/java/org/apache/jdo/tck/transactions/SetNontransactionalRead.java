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

package org.apache.jdo.tck.transactions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Set Nontransactional Read <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-9. <br>
 * <B>Assertion Description: </B> If an implementation supports nontransactional read, then a call
 * to <code>Transaction.setNontransactionalRead</code> with a parameter value of <code>true</code>
 * will set the flag to <code>true</code> and allows persistent instances to be read outside of a
 * transaction. Queries and navigation will be allowed without an active transaction.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/18/01     1.0
 */
public class SetNontransactionalRead extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.2-9 (SetNontransactionalRead) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(Department.class);
    addTearDownClass(Company.class);
  }

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTestSetNontransactionalRead(pm);

    pm.close();
    pm = null;
  }

  /**
   * test transactions.setNonteansactionalRead()
   *
   * @param pm the PersistenceManager
   */
  public void runTestSetNontransactionalRead(PersistenceManager pm) {
    if (!isNontransactionalReadSupported()) {
      if (debug) logger.debug("Implementation does not support non transactional read");
      return;
    }

    Transaction tx = pm.currentTransaction();
    try {
      tx.setNontransactionalRead(true);
      tx.begin();
      Company c = new Company(1L, "MyCompany", new Date(), null);
      Department d = new Department(999, "MyDepartment", c);
      pm.makePersistent(c);
      pm.makePersistent(d);
      Object oid = pm.getObjectId(d);
      if (!tx.getNontransactionalRead()) {
        fail(
            ASSERTION_FAILED,
            "tx.getNontransactionalRead returns false after setting the flag to true.");
      }
      tx.commit();
      if (!tx.getNontransactionalRead()) {
        fail(
            ASSERTION_FAILED,
            "tx.getNontransactionalRead returns false after setting the flag to true.");
      }

      // make sure transaction is not active
      if (tx.isActive()) {
        fail(ASSERTION_FAILED, "transaction still active after tx.commit.");
      }
      tx = null;

      // read department
      d = (Department) pm.getObjectById(oid, true);
      long deptid = d.getDeptid();
      if (deptid != 999) {
        fail(
            "Reading department outside of a transaction returns unexpected value of d.deptid, expected 999, got "
                + deptid);
      }

      // navigate from department to company
      c = (Company) d.getCompany();
      if (c == null) {
        fail("Navigating from department to company outside of a transaction returns null.");
      }
      String companyName = c.getName();
      if (!"MyCompany".equals(companyName)) {
        fail(
            "Navigated company returns unexpected value of c.name, expected MyCompany, got "
                + companyName);
      }

      // run query
      Query<Department> q = pm.newQuery(Department.class);
      q.setFilter("name == \"MyDepartment\"");
      List<Department> result = q.executeList();
      Iterator<Department> i = result.iterator();
      if (!i.hasNext()) {
        fail(ASSERTION_FAILED, "Query outside of a transaction returned empty collection.");
      }
      d = i.next();
      String deptName = d.getName();
      if (!"MyDepartment".equals(deptName)) {
        fail(
            "Department in query result returns unexpected value of d.name, expected MyDepartment, got "
                + deptName);
      }
      if (i.hasNext()) {
        fail(ASSERTION_FAILED, "Query outside of a transaction returns more than one instance.");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
