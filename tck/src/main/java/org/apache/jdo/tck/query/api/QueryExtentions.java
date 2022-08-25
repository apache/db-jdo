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

import java.util.HashMap;
import java.util.Map;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Query Extentions.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.9-1.
 *<BR>
 *<B>Assertion Description: </B>
 * Some JDO vendors provide extensions to the query, 
 * and these extensions must be set in the query instance prior to execution.
 */
public class QueryExtentions extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.9-1 (QueryExtentions) failed: ";
    
    private static String singleStringQuery = 
        "SELECT FROM " + Person.class.getName();

    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        getTransientCompanyModelInstancesAsList(
                new String[]{"emp1", "emp2", "emp3", "emp4", "emp5"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(QueryExtentions.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        Query query = getPM().newQuery(singleStringQuery);
        Map extentions = new HashMap();
        extentions.put("unknown key 1", "unknown value 1");
        query.setExtensions(extentions);
        query.addExtension("unknown key 2", "unknown value 2");
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
