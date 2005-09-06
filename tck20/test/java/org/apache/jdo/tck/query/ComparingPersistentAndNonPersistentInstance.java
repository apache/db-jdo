/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.tck.query;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Comparing persistent and non-persistent instance
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-44.
 *<BR>
 *<B>Assertion Description: </B>
 * Comparisons between persistent and non-persistent instances return
 * not equal. 
 */

public class ComparingPersistentAndNonPersistentInstance
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-36 (ComparingPersistentAndNonPersistentInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ComparingPersistentAndNonPersistentInstance.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            // read test data
            CompanyModelReader reader = loadCompanyModel(pm, COMPANY_TESTDATA);
            runTest(pm, reader);
        }
        finally {
            cleanupCompanyModel(pm);
            pm.close();
            pm = null;
        }
    }
    
    /** */
    void runTest(PersistenceManager pm, CompanyModelReader reader) {
        Query q;
        Object result;
        Collection expected;

        Transaction tx = pm.currentTransaction();
        tx.begin();
        Employee transientEmp1 = reader.getFullTimeEmployee("emp1");
        // access the Employee values to make sure they are loaded
        transientEmp1.toString();
        pm.makeTransient(transientEmp1);
        tx.commit();
        
        tx.begin();

        // query comparing persistent instance (this) with
        // non-persistent instance (param) => 
        // result should be empty
        q = pm.newQuery(Employee.class, "this == param");
        q.declareParameters("Employee param");
        result = q.execute(transientEmp1);
        expected = new HashSet();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        // Get the persistent employee with personid 1 
        Collection tmp = 
            (Collection)pm.newQuery(Employee.class, "personid == 1").execute();
        Employee persistentEmp1 = (Employee)tmp.iterator().next();

        // query comparing the personid field of a persistent instance
        // (this) with the ipersonid of the non-persistent instance (param) => 
        // result should include persistent employee with personid 1
        q = pm.newQuery(Employee.class, "this.personid == param.personid");
        q.declareParameters("Employee param");
        result = q.execute(transientEmp1);
        expected = new HashSet();
        expected.add(persistentEmp1);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.commit();
    }
}
