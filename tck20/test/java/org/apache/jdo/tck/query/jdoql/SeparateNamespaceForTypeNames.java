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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Namespace of Type Names Separate From Fields, Variables, Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-1.
 *<BR>
 *<B>Assertion Description: </B>
Type names have their own namespace that is separate 
from the namespace for fields, variables and parameters.

 */

public class SeparateNamespaceForTypeNames extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-1 (SeparateNamespaceForTypeNames) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SeparateNamespaceForTypeNames.class);
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
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Department dept1 = reader.getDepartment("dept1");


        // query having a parameter with the same name as a type
        Query query = pm.newQuery(Employee.class);
        query.declareImports("import org.apache.jdo.tck.pc.company.Department");
        query.declareParameters("Department Department");
        query.setFilter("department == Department");
        Object results = query.execute(dept1);

        Collection expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);

        // query having a parameter with the same name as a type
        query = pm.newQuery(Department.class);
        query.declareImports("import org.apache.jdo.tck.pc.company.Employee");
        query.declareVariables("Employee Employee");
        query.setFilter("employees.contains(Employee) && Employee.firstname == \"emp1First\"");
        results = query.execute();

        expected = new ArrayList();
        expected.add(dept1);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);

        tx.commit();
        tx = null;
    }
}
