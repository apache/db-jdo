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

package org.apache.jdo.tck.query;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Multiple identical imports
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-5.
 *<BR>
 *<B>Assertion Description: </B>
 * It is valid to specify the same import multiple times.
 */

public class MultipleIdenticalImports extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-5 (MultipleIdenticalImports) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MultipleIdenticalImports.class);
    }

    /** */
    public void test() {
        pm = getPM();
        
        try {
            // read test data
            CompanyModelReader reader = 
                loadCompanyModel(pm, "org/apache/jdo/tck/query/company.xml");
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
        Query query;
        Collection expected;
        Object result;
            
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Department dept1 = reader.getDepartment("dept1");
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
            
        // Import Department twice
        query = pm.newQuery(Employee.class);
        query.declareImports("import org.apache.jdo.tck.pc.company.Department; import org.apache.jdo.tck.pc.company.Department;");
        query.declareParameters("Department d");
        query.setFilter("department == d");
        result = query.execute(dept1);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        // Import Department explictly and per type-import-on-demand
        query = pm.newQuery(Employee.class);
        query.declareImports("import org.apache.jdo.tck.pc.company.Department; import org.apache.jdo.tck.pc.company.*");
        query.declareParameters("Department d");
        query.setFilter("department == d");
        result = query.execute(dept1);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        // type-import-on-demand twice
        query = pm.newQuery(Employee.class);
        query.declareImports("import org.apache.jdo.tck.pc.company.*; import org.apache.jdo.tck.pc.company.*");
        query.declareParameters("Department d");
        query.setFilter("department == d");
        result = query.execute(dept1);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        tx.commit();
        tx = null;
    }
}
