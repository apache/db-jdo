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

package org.apache.jdo.tck.query.jdoql;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Comparing a Collection Field to Null
 *<BR>
 *<B>Keywords:</B> query nullcollection
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-36.
 *<BR>
 *<B>Assertion Description: </B>
 * For datastores that support <code>null</code> values for 
 * <code>Collection</code> types, it is valid to compare the field to 
 * <code>null</code>. Datastores that do not support <code>null</code> values 
 * for <code>Collection</code> types will return <code>false</code> if the query
 * compares the field to <code>null</code>.
 */

public class ComparingCollectionFieldToNull extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-36 (ComparingCollectionFieldToNull) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1 && projects == null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ComparingCollectionFieldToNull.class);
    }
    
    /** */
    public void testPositive() {
        for (int i = 0; i < VALID_QUERIES.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    expectedResult[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    expectedResult[i]);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        Employee employee = (Employee) getPersistentCompanyModelInstance("emp1");
        expectedResult = new Object[] {
            // emp1 should be in the query result set,
            // if the JDO Implentation supports null values for Collections
            getTransientCompanyModelInstancesAsList(
                isNullCollectionSupported() ? 
                    new String[]{"emp1"} : new String[]{})
        };
        if (isNullCollectionSupported()) {
            getPM().currentTransaction().begin();
            employee.setProjects(null);
            getPM().currentTransaction().commit();
        }
    }
}
