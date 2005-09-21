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

import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Null Collections and Contains Method
 *<BR>
 *<B>Keywords:</B> query nullcollection
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-35.
 *<BR>
 *<B>Assertion Description: </B>
 * <code>null</code>-valued fields of <code>Collection</code> types are treated
 * as if they were empty and all <code>contains</code> methods return
 * <code>false</code>.
 */

public class NullCollectionsAndContainsMethod extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-35 (NullCollectionsAndContainsMethod) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullCollectionsAndContainsMethod.class);
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
        Employee emp1 = reader.getFullTimeEmployee("emp1");
        emp1.setProjects(null);
        tx.commit();
        
        tx = pm.currentTransaction();
        tx.begin();
        
        // contains 
        q = pm.newQuery(Employee.class, "personid == 1 && projects.contains(p)");
        q.declareParameters("org.apache.jdo.tck.pc.company.Project p");
        result = q.execute(new Project(999l, "TestProject", null));
        expected = new HashSet();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
                
        // contains 
        q = pm.newQuery(Employee.class, "!team.contains(null)");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
            
        tx.commit();
    }
}
