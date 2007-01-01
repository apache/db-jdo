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

package org.apache.jdo.tck.query;

import java.util.Collection;
import javax.jdo.JDOUserException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Element Returned in Query Result
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-8.
 *<BR>
 *<B>Assertion Description: </B>
 * If a persistent instance associated with another PersistenceManager is 
 * passed as a parameter, JDOUserException is thrown during execute().
 */

public class ParameterBoundToDifferentPM extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-8 (ParameterBoundToDifferentPM) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ParameterBoundToDifferentPM.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            // read test data
            CompanyModelReader reader = 
                loadCompanyModel(pm, "org/apache/jdo/tck/query/company.xml");
            runTest(pm, reader);
        }
        finally {
            cleanupCompanyModel(pm);
            pm.close();
            pm = null;
        }
    }
    
    /** */
    void runTest(PersistenceManager pm, CompanyModelReader reader) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Department dept1 = reader.getDepartment("dept1");
        tx.commit();
        
        PersistenceManager pm2 = pmf.getPersistenceManager();
        try {
            pm2.currentTransaction().begin();
            Query q = pm2.newQuery(Employee.class, "department == d");
            q.declareParameters("Department d");  
            Collection result = (Collection)q.execute(dept1);
            fail(ASSERTION_FAILED,
                 "Query.execute should throw a JDOUserException if a query " + 
                 "parameter is bound to a different PersistenceManager");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) logger.debug("caught expected exception " + ex);
        }
        finally {
            if ((pm2 != null) && !pm2.isClosed()) {
                if (pm2.currentTransaction().isActive()) {
                    pm2.currentTransaction().rollback();
                }
                pm2.close();
            }
        }
    }
}
