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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Equality and Comparisons Between Date Fields and Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-4.
 *<BR>
 *<B>Assertion Description: </B>
Equality and ordering comparisons of <code>Date</code> fields and
<code>Date</code> parameters are valid in a <code>Query</code> filter.
 */

public class EqualityAndComparisonsBetweenDateFieldsAndParameters 
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-4 (EqualityAndComparisonsBetweenDateFieldsAndParameters) failed: ";

    /** */
    private static final Date FIRST_OF_JAN_1999;

    static {
        // initialize static field FIRST_OF_JAN_1999
        Calendar cal = new GregorianCalendar(
            TimeZone.getTimeZone("America/New_York"));
        cal.set(1999, GregorianCalendar.JANUARY, 1, 0, 0, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        FIRST_OF_JAN_1999 = cal.getTime();
    }
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(EqualityAndComparisonsBetweenDateFieldsAndParameters.class);
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

        // date field == date parameter
        q = pm.newQuery(Employee.class, "hiredate == param");
        q.declareParameters("java.util.Date param");
        result = q.execute(FIRST_OF_JAN_1999);
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
            
        // date field >= date parameter
        q = pm.newQuery(Employee.class, "hiredate >= param");
        q.declareParameters("java.util.Date param");
        result = q.execute(FIRST_OF_JAN_1999);
        expected = new HashSet();
        expected.add(reader.getFullTimeEmployee("emp1"));
        expected.add(reader.getFullTimeEmployee("emp2"));
        expected.add(reader.getPartTimeEmployee("emp3"));
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);
            
        // date parameter < date field
        q = pm.newQuery(Employee.class, "param < birthdate");
        q.declareParameters("java.util.Date param");
        result = q.execute(FIRST_OF_JAN_1999);
        expected = new HashSet();
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expected);

        tx.commit();
    }
        
}
