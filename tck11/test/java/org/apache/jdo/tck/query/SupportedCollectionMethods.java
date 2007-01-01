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
 *<B>Title:</B>Supported collection methods
 *<BR>
 *<B>Keywords:</B> query collection 
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-44.
 *<BR>
 *<B>Assertion Description: </B>
 * Supported collection methods:
 * <UL>
 * <LI>isEmpty</LI>
 * <LI>contains</LI>
 * </UL>
 */

public class SupportedCollectionMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-36 (SupportedCollectionMethods) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedCollectionMethods.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            // read test data
            CompanyModelReader reader = 
                loadCompanyModel(pm, "org/apache/jdo/tck/query/company.xml");
            runTestIsEmpty(pm, reader);
            runTestContains(pm, reader);
        }
        finally {
            cleanupCompanyModel(pm);
            pm.close();
            pm = null;
        }
    }
    
    /** */
    void runTestIsEmpty(PersistenceManager pm, CompanyModelReader reader) {
        Query q;
        Object result;
        Collection expected;

        Transaction tx = pm.currentTransaction();
        tx.begin();

        q = pm.newQuery(Department.class, "!employees.isEmpty()");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        q = pm.newQuery(Employee.class, "team.isEmpty()");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.commit();
    }

    /** */
    void runTestContains(PersistenceManager pm, CompanyModelReader reader) {
        Query q;
        Object result;
        Collection expected;

        Transaction tx = pm.currentTransaction();
        tx.begin();

        q = pm.newQuery(Department.class);
        q.setFilter("employees.contains(e) && e.personid == 1");
        q.declareVariables("Employee e");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.commit();
    }
}
