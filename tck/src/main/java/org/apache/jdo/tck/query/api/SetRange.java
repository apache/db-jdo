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

import javax.jdo.Query;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set Range.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-20.
 *<BR>
 *<B>Assertion Description: </B>
 * setRange(int fromIncl, int toExcl); 
 * Specify the number of instances to skip over 
 * and the maximum number of result instances to return.
 */
public class SetRange extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-20 (SetRange) failed: ";
    
    /** 
     * The expected results of valid queries.
     */
    private final Object[] expectedResult = {
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5")
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetRange.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        Query<Person> query = getPM().newQuery(Person.class);
        query.setRange(0, 5);
        String singleStringQuery = 
            "SELECT FROM Person RANGE 0, 5";
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, 
                false, null, expectedResult[index], true);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
