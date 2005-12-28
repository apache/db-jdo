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

import java.math.BigDecimal;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Single String Query.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-23.
 *<BR>
 *<B>Assertion Description: </B>
 * The single string query is first parsed to yield the result, 
 * result class, filter, variable list, parameter list, import list, 
 * grouping, ordering, and range. 
 * Then, the values specified in APIs setResult, setResultClass, 
 * setFilter, declareVariables, declareParamters, declareImports, 
 * setGrouping, setOrdering, and setRange 
 * override the corresponding settings from the single string query.
 */
public class SingleStringQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-23 (SingleStringQuery) failed: ";
    
    private static String singleStringQuery = 
        "SELECT UNIQUE firstname, lastname " +
        "INTO FullName " +
        "FROM Person " +
        "WHERE salary > 1000 & " +
        "      projects.contains(p) & p.budget > limit & " +
        "      firstname == 'emp1First' " +
        "VARIABLES Project p " +
        "PARAMETERS BigDecimal limit " +
        "IMPORTS import org.apache.jdo.tck.query.result.classes.FullName; " +
        "        import org.apache.jdo.tck.pc.company.Person; " +
        "        import org.apache.jdo.tck.pc.company.Project; " +
        "        import java.math.BigDecimal; " +
        "GROUP BY firstname, lastname " +
        "ORDER BY lastname ASCENDING " +
        "RANGE 0,5";

    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        new FullName("emp1First", "emp1Last"),
        getTransientCompanyModelInstancesAsList(new String[]{"emp1", "emp2", "emp5"}) 
    };
            
    /** Parameters of valid queries. */
    private Object[][] parameters = {
        {new BigDecimal("2000")}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SingleStringQuery.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        Query query = getPM().newQuery(singleStringQuery);
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, 
                true, parameters[index], expectedResult[index], true);
        
        index = 1;
        String singleStringQuery = "SELECT FROM FullTimeEmployee";
        query.setUnique(false);
        query.setResult(null);
        query.setResultClass(null);
        query.setClass(FullTimeEmployee.class);
        query.setFilter(null);
        query.declareVariables(null);
        query.declareParameters(null);
        query.declareImports(null);
        query.setGrouping(null);
        query.setOrdering(null);
        query.setRange(null);
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, false, null, 
                expectedResult[index], true);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
