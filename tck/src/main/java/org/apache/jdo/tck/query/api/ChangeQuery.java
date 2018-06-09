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

package org.apache.jdo.tck.query.api;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Change Query.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.5-15.
 *<BR>
 *<B>Assertion Description: </B>
 * The Query instance returned from this method can be modified 
 * by the application, just like any other Query instance.
 */
public class ChangeQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.5-15 (ChangeQuery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ChangeQuery.class);
    }
    
    /** */
    public void testPositive() {
        Query query = getPM().newNamedQuery(Person.class, "changeQuery");
        
        // change query
        query.setResult("DISTINCT firstname, lastname");
        query.setResultClass(FullName.class);
        query.setClass(FullTimeEmployee.class);
        String filter = "salary > 1000 & projects.contains(p) & " +
                        "p.budget > limit";
        query.setFilter(filter);
        String imports = "import org.apache.jdo.tck.pc.company.Project; " +
                         "import java.math.BigDecimal;";
        query.declareImports(imports);
        query.declareVariables("Project p");
        query.declareParameters("BigDecimal limit");
        query.setOrdering("firstname ASCENDING, lastname ASCENDING");
        query.setRange(0, 5);
        String singleStringQuery = 
            "SELECT DISTINCT firstname, lastname " +
            "INTO FullName FROM FullTimeEmployee " +
            "WHERE salary > 1000 & projects.contains(p) & " +
            "p.budget > limit " +
            "VARIABLES Project p PARAMETERS BigDecimal limit " +
            "ORDER BY firstname ASCENDING, lastname ASCENDING RANGE 0, 5";

        // query parameters
        Object[] parameters = {new BigDecimal("2000")};        
        // expected result
        List expectedResult = Arrays.asList(new Object[] {
            new FullName("emp1First", "emp1Last"), 
            new FullName("emp2First", "emp2Last"),
            new FullName("emp5First", "emp5Last")});

        // execute query
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, true, 
                parameters, expectedResult, true);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
