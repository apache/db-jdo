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

package org.apache.jdo.tck.query.jdoql.parameters;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;

/**
 *<B>Title:</B> Order of Parameters.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.13-3.
 *<BR>
 *<B>Assertion Description: </B>
 * If implicit parameters are used, their order of appearance in the query 
 * determines their order for binding to positional parameters for execution.
 */
public class OrderOfParameters extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.13-3 (OrderOfParameters) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(OrderOfParameters.class);
    }
    
    /** */
    public void testAPIQuery() {
        // Do not use QueryElementHolder, because QueryElementHolder always uses a Map for parameter values
        Transaction tx = pm.currentTransaction();
        Query<Person> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery =
                    "select from org.apache.jdo.tck.pc.company.Person where firstname == :param1 & lastname == :param2";
            query = pm.newQuery(Person.class, "firstname == :param1 & lastname == :param2");
            result = query.execute("emp1First", "emp1Last");
            List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /** */
    @SuppressWarnings("unchecked")
    public void testSingleStringAPIQuery() {
        // Do not use QueryElementHolder, because QueryElementHolder always uses a Map for parameter values
        Transaction tx = pm.currentTransaction();
        Query<Person> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery =
                    "select from org.apache.jdo.tck.pc.company.Person where firstname == :param1 & lastname == :param2";
            query = pm.newQuery(singleStringQuery);
            result = query.execute("emp1First", "emp1Last");
            List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
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
