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

package org.apache.jdo.tck.query.api;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PCClass;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Metadata Search Order.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.5-13.
 *<BR>
 *<B>Assertion Description: </B>
 * If the named query is not found in already-loaded metadata, 
 * the query is searched for using an algorithm. 
 * Files containing metadata are examined in turn until the query is found. 
 * The order is based on the metadata search order for class metadata, 
 * but includes files named based on the query name.
 */
public class MetadataSearchOrder extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.5-13 (MetadataSearchOrder) failed: ";
    
    /** The expected results of valid queries. */
    private static String[][] expectedResult = {
        {"emp1", "emp2", "emp3", "emp4", "emp5"},
        {"emp2", "emp3", "emp4", "emp5"},
        {"pcClass1", "pcClass2"},
        {"emp3", "emp4", "emp5"},
        {"emp4", "emp5"}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MetadataSearchOrder.class);
    }
    
    /** */
    public void testPackageJDOInDefaultPackage() {
        int index = 0;
        Object[] expectedResultValues = 
            getCompanyModelInstances(expectedResult[index]);
        executeNamedQuery(null, "packageJDOInDefaultPackage", 
                false, expectedResultValues);
    }
    
    /** */
    public void testPackageJDO() {
        int index = 1;
        Object[] expectedResultValues = 
            getCompanyModelInstances(expectedResult[index]);
        executeNamedQuery(Person.class, "packageJDO", 
                false, expectedResultValues);
    }
    
    /** */
    public void testClassJDO() {
        int index = 2;
        Object[] expectedResultValues = 
            getMylibInstances(expectedResult[index]);
        executeNamedQuery(PCClass.class, "classJDO", 
                false, expectedResultValues);
    }
    
    /** */
    public void testPackageORM() {
        int index = 3;
        Object[] expectedResultValues = 
            getCompanyModelInstances(expectedResult[index]);
        executeNamedQuery(Person.class, "packageORM", 
                false, expectedResultValues);
    }
    
    /** */
    public void testPackageJDOQuery() {
        int index = 4;
        Object[] expectedResultValues = 
            getCompanyModelInstances(expectedResult[index]);
        executeNamedQuery(Person.class, "packageJDOQuery", 
                false, expectedResultValues);
    }

    private void executeNamedQuery(Class candidateClass, String namedQuery,
            boolean isUnique, Object[] expectedResultValues) {
        Query query = getPM().newNamedQuery(candidateClass, namedQuery); 
        execute(ASSERTION_FAILED, query, "Named query " + namedQuery,
                isUnique, false, null, expectedResultValues);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadMylib(getPM(), MYLIB_TESTDATA);
        addTearDownClass(MylibReader.getTearDownClasses());
    }
}
