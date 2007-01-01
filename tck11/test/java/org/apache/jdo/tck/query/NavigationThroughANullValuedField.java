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
 *<B>Title:</B> Navigation Through a Null-Valued Field
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-9.
 *<BR>
 *<B>Assertion Description: </B>
 * Navigation through a null-valued field, which would throw
 * <code>NullPointerException</code>, is treated as if the filter expression 
 * returned <code>false</code> for the evaluation of the current set of variable
 * values. Other values for variables might still qualify the candidate instance
 * for inclusion in the result set.
 */

public class NavigationThroughANullValuedField extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-9 (NavigationThroughANullValuedField) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NavigationThroughANullValuedField.class);
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
        Object result;
        Collection expected;
        
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // navigation through reference relationship field
        // the relationship medicalInsurence is not set for emp2 and emp3 =>
        // they should not be part of the result
        q = pm.newQuery(Employee.class);
        q.setFilter("medicalInsurance.carrier == \"Carrier1\"");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        // navigation through collection relationship field
        // employees emp2 and emp3 do not have a medicalInsurence, but emp1 
        // matches the filter such that dept1 qualifies for inclusion in the 
        // result set.
        q = pm.newQuery(Department.class);
        q.declareVariables("Employee e");
        q.setFilter("employees.contains(e) && e.medicalInsurance.carrier == \"Carrier1\"");
        result = q.execute();
        expected = new HashSet();
        expected.add(reader.getDepartment("dept1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
        
        tx.commit();
    }
}
