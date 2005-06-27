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
import javax.jdo.Transaction;
import javax.transaction.Synchronization;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Is Active Until After Completion Method Called
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.1-3.
 *<BR>
 *<B>Assertion Description: </B>
 Transaction.isActive returns true after the transaction has been started, until the afterCompletion
 synchronization method is called
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/09/01     1.0
 */
public class IsActiveUntilAfterCompletionMethodCalled 
    extends JDO_Test 
    implements Synchronization {
    
    private Transaction tx;
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.1-3 (IsActiveUntilAfterCompletionMethodCalled) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IsActiveUntilAfterCompletionMethodCalled.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
    }
    
    /** */
    public void beforeCompletion() {
        try {
            if (!tx.isActive()) {
                fail(ASSERTION_FAILED,
                     "tx.isActive returns false in beforeCompletion.");
            }
        }
        catch (Exception ex) {
            fail(ASSERTION_FAILED,
                 "tx.isActive called in beforeCompletion throws unexpected exception: " + ex);
        }
    }

    /** */
    public void afterCompletion(int status) {
        try {
            if (tx.isActive()) {
                fail(ASSERTION_FAILED,
                     "tx.isActive returns true in afterCompletion.");
            }
        }
        catch (Exception ex) {
            fail(ASSERTION_FAILED,
                 "tx.isActive called in afterCompletion throws unexpected exception: " + ex);
        }
    }
    
    /** */
    public void test() {
        pm = getPM();
                    
        runTestIsActiveUntilAfterCompletionMethodCalled(pm);
 
        pm.close(); 
        pm = null;
    }
    
    /** test transactions.isActive() */
    void runTestIsActiveUntilAfterCompletionMethodCalled(PersistenceManager pm) {
        tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = new PCPoint(1,3);
            pm.makePersistent(p1);
            tx.setSynchronization(this);
            if (!tx.isActive()) {
                fail(ASSERTION_FAILED,
                     "tx.isActive returns false after tx.begin before tx.commit is completed.");
            }
            
            tx.commit();
            if (tx.isActive()) {
                fail(ASSERTION_FAILED,
                     "tx.isActive returns true after tx.commit.");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
