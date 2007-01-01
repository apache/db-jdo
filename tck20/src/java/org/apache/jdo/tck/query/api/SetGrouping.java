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

import java.util.Arrays;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set Grouping.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-17.
 *<BR>
 *<B>Assertion Description: </B>
 * void setGrouping (String grouping); 
 * Specify the grouping of results for aggregates.
 */
public class SetGrouping extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-17 (SetGrouping) failed: ";
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[] {
                "emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetGrouping.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        Query query = getPM().newQuery(Person.class);
        query.setResult("lastname");
        query.setGrouping("lastname");
        String singleStringQuery = "SELECT lastname FROM Person GROUP BY lastname";
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, 
                false, null, expectedResult[index], true);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
