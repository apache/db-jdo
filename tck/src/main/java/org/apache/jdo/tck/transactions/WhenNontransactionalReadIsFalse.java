/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package org.apache.jdo.tck.transactions;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.JDOUserException;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> When Nontransactional Read Is False
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.2-10.
 *<BR>
 *<B>Assertion Description: </B>
    If this flag is set to false, then queries and field read access
    (including navigation) outside an active transaction
    throw a JDOUserException.
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Michelle Caisse   11/11/04     1.0
 */
public class WhenNontransactionalReadIsFalse extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.2-10 (WhenNontransactionalReadIsFalse) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(WhenNontransactionalReadIsFalse.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(Department.class);
        addTearDownClass(Company.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
          
        runTestWhenNontransactionalReadIsFalse(pm);

        pm.close(); 
        pm = null;
    }

    /**
     * @param pm the PersistenceManager
     */
    public void runTestWhenNontransactionalReadIsFalse(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.setNontransactionalRead(false);
            tx.begin();
            Company c = new Company(1L, "MyCompany", new Date(), null);
            Department d = new Department(999, "MyDepartment", c);
            
            pm.makePersistent(c);
            pm.makePersistent(d);
            if (tx.getNontransactionalRead()) {
                fail(ASSERTION_FAILED,
                     "tx.getNontransactionalRead before commit returns true after setting the flag to false.");
            }
            tx.commit();
            if (tx.getNontransactionalRead()) {
                fail(ASSERTION_FAILED,
                     "tx.getNontransactionalRead after commit returns true after setting the flag to false.");
            }

            // make sure transaction is not active
            if (tx.isActive()) {
                fail(ASSERTION_FAILED,
                     "transaction still active after tx.commit.");
            }
            tx = null;

            try {
                // read department name
                String name = d.getName();
                fail(ASSERTION_FAILED,
                     "Field read permitted outside an active transaction when NontransactionalRead is false.");
            } catch (JDOUserException juex) {
                 if (debug)
                     logger.debug(" Caught expected JDOUserException " + juex);
            }
            try {
                // navigate from department to company
                c = (Company)d.getCompany();
                fail(ASSERTION_FAILED,
                     "Navigation permitted outside an active transaction when NontransactionalRead is false.");
            } catch (JDOUserException juex) {
                 if (debug)
                     logger.debug(" Caught expected JDOUserException " + juex);
            }
            try {
                // run query
                Query<Department> q = pm.newQuery(Department.class);
                q.setFilter("name == \"MyDepartment\"");
                List<Department> result = q.executeList();
                fail(ASSERTION_FAILED,
                     "Query permitted outside an active transaction when NontransactionalRead is false.");
            } catch (JDOUserException juex) {
                 if (debug)
                     logger.debug(" Caught expected JDOUserException " + juex);
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
