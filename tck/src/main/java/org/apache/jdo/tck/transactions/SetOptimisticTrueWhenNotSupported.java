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

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Set Optimistic True When Not Supported
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.2-7.
 *<BR>
 *<B>Assertion Description: </B>
 If the optional feature Optimistic is not supported, then a call to Transaction.setOptimistic with a value of true will
 throw a JDOUnsupportedOptionException. 

 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/18/01     1.0
 */
public class SetOptimisticTrueWhenNotSupported extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.2-7 (SetOptimisticTrueWhenNotSupported) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetOptimisticTrueWhenNotSupported.class);
    }

    /** */
    public void test() {
        pm = getPM();
          
        runTestSetOptimisticTrueWhenNotSupported(pm);   

        pm.close(); 
        pm = null;
    }

    /**
     *
     * @param pm the PersistenceManager
     */
    void runTestSetOptimisticTrueWhenNotSupported(PersistenceManager pm) {
        if (isOptimisticSupported()) {
            if (debug) logger.debug("Optimistic supported.");
            return;
        }
        
        Transaction tx = pm.currentTransaction();
        try {
            tx.setOptimistic(true);
            fail(ASSERTION_FAILED,
                 "tx.setOptimistic(true) should throw JDOUnsupportedOptionException, if the implementation does not support optimistic transactions.");
        }
        catch (JDOUnsupportedOptionException ex) {
            // expected excepted
            if (debug) logger.debug("caught expected exception " + ex);
        }
    }
}
