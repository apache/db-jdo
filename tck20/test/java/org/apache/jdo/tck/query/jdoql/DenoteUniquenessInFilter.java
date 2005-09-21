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

package org.apache.jdo.tck.query.jdoql;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Element Returned in Query Result
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-2.
 *<BR>
 *<B>Assertion Description: </B>
An element of the candidate collection is returned in the result if:
<UL>
<LI>it is assignment compatible to the candidate <code>Class</code> of the
<code>Query</code>; and</LI>
<LI>for all variables there exists a value for which the filter expression
evaluates to <code>true</code>.
The user may denote uniqueness in the filter expression
by explicitly declaring an expression
(for example, <code>e1 != e2</code>).</LI>
</UL>
 */

public class DenoteUniquenessInFilter extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-2 (DenoteUniquenessInFilter) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DenoteUniquenessInFilter.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            // read test data
            CompanyModelReader reader = 
                loadCompanyModel(pm, COMPANY_TESTDATA);
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
        String filter;
        
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // Scenario: only employee with personid 1 has a medicalInsurance
        
        // Uniqueness not specified => employee 1 qualifies => return dept1
        filter = 
            "employees.contains(e1) && (e1.personid == 1 && " +
            "(employees.contains(e2) && (e2.medicalInsurance != null)))";
        q = pm.newQuery(Department.class, filter);
        q.declareVariables("Employee e1; Employee e2");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        // Uniqueness specified => there is only a single employee qualifying =>
        // e1 != e2 does not apply => result is empty
        filter = 
            "employees.contains(e1) && (e1.personid == 1 && " +
            "(employees.contains(e2) && (e2.medicalInsurance != null &&" + 
            "e1 != e2)))";
        q = pm.newQuery(Department.class, filter);
        q.declareVariables("Employee e1; Employee e2");
        result = q.execute();
        expected = new HashSet();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        // Changed second contains to look for null medicalInsurance => 
        // employee 1 and 2 match the two conditions => return dept1
        filter = 
            "employees.contains(e1) && (e1.personid == 1 && " +
            "(employees.contains(e2) && (e2.medicalInsurance == null &&" + 
            "e1 != e2)))";
        q = pm.newQuery(Department.class, filter);
        q.declareVariables("Employee e1; Employee e2");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.commit();
    }
}
