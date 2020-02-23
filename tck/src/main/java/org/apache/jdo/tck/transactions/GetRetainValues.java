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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Get Retain Values
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.2-17.
 *<BR>
 *<B>Assertion Description: </B>
 Transaction.getRetainValues returns the currently active setting for the 
 RetainValues flag.
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/09/01     1.0
 */
public class GetRetainValues extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
    "Assertion A13.4.2-17 (GetRetainValues) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetRetainValues.class);
    }

    /** */
    public void test() {
        pm = getPM();
        
        runTestGetRetainValues(pm);   

        pm.close(); 
        pm = null;
    }

    /** test transaction.getRetainValues()
     * @param pm the PersistenceManager
     */
    public void runTestGetRetainValues(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.setRetainValues(true);
            tx.begin();
            if (!tx.getRetainValues()) {
                fail(ASSERTION_FAILED,
                     "tx.getRetainValues returns false after setting the flag to true.");
            }
            tx.commit();
            if (!tx.getRetainValues()) {
                fail(ASSERTION_FAILED,
                     "tx.getRetainValues returns false after setting the flag to true.");
            }
            
            tx.setRetainValues(false);
            tx.begin();
            if (tx.getRetainValues()) {
                fail(ASSERTION_FAILED,
                     "tx.getRetainValues returns true after setting the flag to false.");
            }
            tx.commit();
            if (tx.getRetainValues()) {
                fail(ASSERTION_FAILED,
                     "tx.getRetainValues returns true after setting the flag to false.");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
