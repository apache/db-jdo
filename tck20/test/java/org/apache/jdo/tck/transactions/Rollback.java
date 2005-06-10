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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.BoundParameterCheck;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Rollback
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.4-2.
 *<BR>
 *<B>Assertion Description: </B>
 The <code>commit</code> method performs the following operations:
 <UL>
 <LI>transitions persistent instances
 according to the life cycle specification;</LI>
 <LI>rolls back changes made in this transaction from the data store;</LI>
 <LI>calls the <code>afterCompletion</code> method of the
 <code>Synchronization</code> instance registered with the
 <code>Transaction</code>.</LI>
 </UL>

 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/17/01     1.0
 */
public class Rollback 
    extends JDO_Test 
    implements Synchronization {
    
    private boolean beforeCompletionCalled;
    private boolean afterCompletionCalled;
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.4-2 (BoundParameterCheck) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(BoundParameterCheck.class);
    }
   
    /** */
    public void beforeCompletion(){
        beforeCompletionCalled = true;
        if (debug) logger.debug ("beforeCompletion called ");
    }
     
    /** */
    public void afterCompletion(int status) {
        if (status == javax.transaction.Status.STATUS_ROLLEDBACK) {
            afterCompletionCalled = true;
            if (debug) logger.debug("afterCompletion called\n ");
        }
        else { 
            fail(ASSERTION_FAILED,
                 "afterCompletion: incorrect status, expected " + 
                 Status.STATUS_ROLLEDBACK + ", got " + status);
        }
    }
      
    /** */
    public void test() {
        pm = getPM();
                    
        runTestRollback(pm);

        pm.close(); 
        pm = null;
    }
    
    /** test transactions.setSynchronization() */
    void runTestRollback(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = new PCPoint(1,3);
            addTearDownInstance((Object)p1);
            pm.makePersistent(p1);
            tx.commit();
          
            tx.begin();
            p1.setX(55);
                    
            if (!JDOHelper.isDirty(p1)) {
                fail(ASSERTION_FAILED,
                     "JDOHelper.isDirty returns false when called for dirty instance.");
            }
          
            tx.setSynchronization(this);
            beforeCompletionCalled = false; 
            afterCompletionCalled = false ; 
            tx.rollback(); 
         
            if (JDOHelper.isDirty(p1)) {
                fail(ASSERTION_FAILED,
                     "P-NEW instance should transition to HOLLOW or P-NONTX and then it should not be dirty, JDOHelper.isDirty returns true.");
            }
            tx.setSynchronization(null);
            tx.begin();
            int x = p1.getX();
            tx.commit();
            if (x != 1) {
                fail(ASSERTION_FAILED,
                     "tx.rollback should rollback change of ip1.x, expected 1, got " + x);
            }
            
            if (beforeCompletionCalled) {
                fail(ASSERTION_FAILED,
                     "rollback did invoke beforeCompletion method.");
            }
            if (!afterCompletionCalled) {
                fail(ASSERTION_FAILED,
                     "commit didn't invoke afterCompletion method.");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
