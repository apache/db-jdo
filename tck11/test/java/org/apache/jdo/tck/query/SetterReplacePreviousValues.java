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
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Setter replace previous values.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.15.
 *<BR>
 *<B>Assertion Description: </B>
 * All of these methods replace the previously set query element, by the 
 * parameter. [The methods are not additive].
 */

public class SetterReplacePreviousValues extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.15 (SetterReplacePreviousValues) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetterReplacePreviousValues.class);
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
        Query q;
        Collection result;
        Collection expected;
        
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // replace parameter declaration
        q = pm.newQuery(Department.class, "deptid == param");
        q.declareParameters("String x");
        q.declareParameters("long param");
        result = (Collection)q.execute(new Long(1));
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
       
        // replace filter setting
        q = pm.newQuery(Employee.class, "personid == 1L");
        q.setFilter("personid == 2L");
        result = (Collection)q.execute();
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp2"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        // repalce variable declaration
        q = pm.newQuery(Department.class);
        q.declareVariables("Employee e1; Employee e2");
        q.declareVariables("Employee e");
        q.setFilter("employees.contains(e) && e.personid == 1");
        result = (Collection)q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.rollback();
    }
}
