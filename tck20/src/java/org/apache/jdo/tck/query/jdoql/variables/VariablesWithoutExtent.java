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

package org.apache.jdo.tck.query.jdoql.variables;

import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.query.NoExtent;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Variables without Extent.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.5-2.
 *<BR>
 *<B>Assertion Description: </B>
 * If the class does not manage an Extent, 
 * then no results will satisfy the query.
 */
public class VariablesWithoutExtent extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.5-2 (VariablesWithoutExtent) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "this.personid = noExtent.id",
        /*VARIABLES*/   "NoExtent noExtent",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     "import org.apache.jdo.tck.pc.query.NoExtent;",
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        new LinkedList()
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(VariablesWithoutExtent.class);
    }
    
    /** */
    public void testPositive() {
        if (isUnconstrainedVariablesSupported()) {
            for (int i = 0; i < VALID_QUERIES.length; i++) {
                executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                        expectedResult[i]);
                executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                        expectedResult[i]);
            }
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        NoExtent noExtent = new NoExtent(1);
        makePersistent(noExtent);
        addTearDownInstance(noExtent);
    }
    
    /**
     * Makes the given instance persistent.
     * @param o the instance to be made persistent.
     */
    private void makePersistent(Object o) {
        PersistenceManager pm = getPM();
        Transaction transaction = pm.currentTransaction();
        transaction.begin();
        try {
            pm.makePersistent(o);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

}
