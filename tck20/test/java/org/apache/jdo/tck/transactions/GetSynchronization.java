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
 *<B>Title:</B> Get Synchronization
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.3-8.
 *<BR>
 *<B>Assertion Description: </B>
 A call to Transaction.getSynchronization
 retrieves the Synchronization instance that has been registered via
 setSynchronization. 
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/11/01     1.0
 */
public class GetSynchronization 
    extends JDO_Test 
    implements Synchronization {
    
    private Transaction tx;

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.3-8 (GetSynchronization) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetSynchronization.class);
    }

    /** */
    public void beforeCompletion(){
        try {
            if (debug) 
                logger.debug("before Complition isActive returns :" +
                             tx.isActive());
        }
        catch (Exception ex) {
            fail(ASSERTION_FAILED,
                 "tx.isActive called in beforeCompletion throws unexpected exception: " + ex);
        }
    }

    /** */
    public void afterCompletion(int status) {
        try {
            if (debug) 
                logger.debug("after Complition isActive returns :" +
                             tx.isActive()); 
        }
        catch (Exception ex) {
            fail(ASSERTION_FAILED,
                 "tx.isActive called in afterCompletion throws unexpected exception: " + ex);
        }
    }
      
    /** */
    public void test() {
        pm = getPM();
         
        runTestGetSynchronization(pm); 

        pm.close(); 
        pm = null;
    }

    /** test transactions.getSynchronization() */
    void runTestGetSynchronization(PersistenceManager pm) {
        tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = new PCPoint(1,3);
            addTearDownInstance((Object)p1);
            pm.makePersistent(p1);
            
            tx.setSynchronization(this);
            Synchronization s = tx.getSynchronization();
            if (s != this) {
                fail(ASSERTION_FAILED,
                     "wrong synchronization instance, expected " + this + ", got " + s);
            }
            
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
