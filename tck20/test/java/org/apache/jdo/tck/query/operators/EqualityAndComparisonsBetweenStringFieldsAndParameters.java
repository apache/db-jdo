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

package org.apache.jdo.tck.query.operators;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Equality and Comparisons Between String Fields and Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-5.
 *<BR>
 *<B>Assertion Description: </B>
Equality and ordering comparisons of <code>String</code> fields and
<code>String</code> parameters are valid.
The comparison is done lexicographically.
 */

public class EqualityAndComparisonsBetweenStringFieldsAndParameters 
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-5 (EqualityAndComparisonsBetweenStringFieldsAndParameters) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(EqualityAndComparisonsBetweenStringFieldsAndParameters.class);
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

        // string field == string parameter
        q = pm.newQuery(Employee.class, "firstname == param");
        q.declareParameters("java.lang.String param");
        result = q.execute("emp1First");
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
            
        // string field >= string parameter
        q = pm.newQuery(Employee.class, "firstname >= param");
        q.declareParameters("java.lang.String param");
        result = q.execute("emp1First");
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
            
        // string parameter < stringe field
        q = pm.newQuery(Employee.class, "param < firstname");
        q.declareParameters("java.lang.String param");
        result = q.execute("emp2First");
        expected = new HashSet();
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        tx.commit();
    }
}