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
        "      projects.contains(project) & project.budget > limit & " +
        "      firstname == 'emp1First' " +
        "VARIABLES Project project " +
        "PARAMETERS BigDecimal limit " +
        "IMPORTS IMPORT org.apache.jdo.tck.query.result.classes.FullName; " +
        "        IMPORT org.apache.jdo.tck.pc.company.Person; " +
        "        IMPORT org.apache.jdo.tck.pc.company.Project; " +
        "        IMPORT java.math.BigDecimal; " +
        "ORDER BY personid ASCENDING" +
        "GROUP BY firstname, lastname " +
        "RANGE 0 TO 5";

    /** The expected results of valid queries. */
    private static Object[][] expectedResult = {
        {new FullName("emp1First", "emp1Last")},
        //Note: These are no bean names!
        {"emp1First", "emp2First", "emp5First"} 
    };
            
    /** Parameters of valid queries. */
    private static Object[][] parameters = {
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
        execute(ASSERTION_FAILED, query, singleStringQuery, 
                true, true, parameters[index], expectedResult[index]);
        
        index = 1;
        String singleStringQuery = 
            "SELECT firstName FROM org.apache.jdo.tck.pc.company.FullTimeEmployee";
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
        query.setRange(0, 0);
        execute(ASSERTION_FAILED, query, singleStringQuery, 
                false, false, parameters[index], expectedResult[index]);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
