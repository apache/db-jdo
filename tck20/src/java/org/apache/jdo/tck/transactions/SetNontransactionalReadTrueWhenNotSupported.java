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

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Set Nontransactional Read True When Not Supported
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.2-5.
 *<BR>
 *<B>Assertion Description: </B>
 If an implementation does not support nontransactional read, then a
 call to Transaction.setNontransactionalRead with a parameter value of true will
 * cause a JDOUnsupportedOptionException to be thrown.
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/18/01     1.0
 */
public class SetNontransactionalReadTrueWhenNotSupported extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.2-5 (SetNontransactionalReadTrueWhenNotSupported) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetNontransactionalReadTrueWhenNotSupported.class);
    }

    /** */
    public void test() {
        pm = getPM();
          
        runTestSetNontransactionalReadTrueWhenNotSupported(pm);   

        pm.close(); 
        pm = null;
    }
    
    /* test transactions.setNonteansactionalRead()
     *  
     */
    public void runTestSetNontransactionalReadTrueWhenNotSupported(
        PersistenceManager pm) {
        if (isNontransactionalReadSupported()) {
            if (debug)
                logger.debug("Implementation does support non transactional read.");
            return;
        }

        Transaction tx = pm.currentTransaction();
        try {
            tx.setNontransactionalRead(true);
            fail(ASSERTION_FAILED,
                 "tx.setNontransactionalRead(true) should throw JDOUnsupportedOptionException, if the implementation does not support non transactional read.");
        }
        catch (JDOUnsupportedOptionException ex) {
            // expected excepted
            if (debug) logger.debug("caught expected exception " + ex);
        }
    }
}
