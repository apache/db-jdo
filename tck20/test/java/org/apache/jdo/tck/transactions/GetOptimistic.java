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
 
package org.apache.jdo.tck.transactions;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.query.BoundParameterCheck;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Get Optimistic
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.1-2.
 *<BR>
 *<B>Assertion Description: </B>
 The transactions.getOptimistic method 
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/09/01     1.0
 */
public class GetOptimistic extends JDO_Test {
    private PersistenceManagerFactory   pmf;
    private PersistenceManager          pm;
    private PersistenceManager          pm1;
    private Transaction                 transaction;
    private boolean         flag;
    private boolean         flag2;

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.1-2 (BoundParameterCheck) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(BoundParameterCheck.class);
    }

    /** */
    public void test() {
        pm = getPM();
          
        runTestGetOptimistic(pm);

        pm.close(); 
        pm = null;
    }

    /** test transactions.getOptimistic() */
    public void runTestGetOptimistic(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            if (isOptimisticSupported()) {
                tx.setOptimistic(true);
                tx.begin();
                if (!tx.getOptimistic()) {
                    fail(ASSERTION_FAILED,
                         "tx.getOptimistic() returns false after setting the flag to true.");
                }
                tx.commit();
                if (!tx.getOptimistic()) {
                    fail(ASSERTION_FAILED,
                         "tx.getOptimistic() returns false after setting the flag to true.");
                }
            }
            
            tx.setOptimistic(false);
            tx.begin();
            if (tx.getOptimistic()) {
                fail(ASSERTION_FAILED,
                     "tx.getOptimistic() returns true after setting the flag to false.");
            }
            tx.commit();
            if (tx.getOptimistic()) {
                fail(ASSERTION_FAILED,
                     "tx.getOptimistic() returns true after setting the flag to false.");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
