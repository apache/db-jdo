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
 
package org.apache.jdo.tck.transactions;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Commit
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.4-1.
 *<BR>
 *<B>Assertion Description: </B>
 The <code>commit</code> method performs the following operations:
 <UL>
 <LI>calls the <code>beforeCompletion</code> method of the
 <code>Synchronization</code> instance registered with the
 <code>Transaction</code>;</LI>
 <LI>flushes dirty persistent instances;</LI>
 <LI>notifies the underlying data store to commit the transaction
 (this cannot be tested)</LI>
 <LI>transitions persistent instances according to the life cycle specification;</LI>
 <LI>calls the <code>afterCompletion</code> method of the <code>Synchronization</code>
 instance registered with the <code>Transaction</code> with the results of the
 data store commit operation.</LI>
 </UL>
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/15/01     1.0
 */
public class Commit extends JDO_Test implements Synchronization {
    
    private boolean beforeCompletionCalled;
    private boolean afterCompletionCalled;
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.4-1 (Commit) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Commit.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
         
        runTestCommit(pm); 

        pm.close();
        pm = null;
    }

    /** */
    public void beforeCompletion(){
        beforeCompletionCalled = true;
        if (debug) logger.debug("beforeCompletion called ");
    }

    /**
     * This method is called by the transaction manager after the transaction is committed or rolled back.
     * @param status The status of the transaction completion.
     */
    public void afterCompletion(int status) {
        if (status == Status.STATUS_COMMITTED) {
            afterCompletionCalled = true;
            if (debug) logger.debug("afterCompletion called\n ");
        }
        else { 
            fail(ASSERTION_FAILED,
                 "afterCompletion: incorrect status, expected " + 
                 Status.STATUS_COMMITTED + ", got " + status);
        }
    }

    /** test transactions.setSynchronization() */
    void runTestCommit(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = new PCPoint(1,3);
            pm.makePersistent(p1);
            if (!JDOHelper.isDirty(p1)) {
                fail(ASSERTION_FAILED,
                     "P-NEW instance expected to be dirty, JDOHelper.isDirty returns false.");
            }
            
            tx.setSynchronization(this);
            beforeCompletionCalled = false; 
            afterCompletionCalled = false; 
            tx.commit();
            tx = null;
            
            if (JDOHelper.isDirty(p1)) {
                fail(ASSERTION_FAILED,
                     "P-NEW instance should transition to HOLLOW or P-NONTX and then it should not be dirty, JDOHelper.isDirty returns true.");
            }
            if (!beforeCompletionCalled) {
                fail(ASSERTION_FAILED,
                     "commit didn't invoke beforeCompletion method");
            }
            if (!afterCompletionCalled) {
                fail(ASSERTION_FAILED,
                     "commit didn't invoke afterCompletion method");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
