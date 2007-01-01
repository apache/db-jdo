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
 *<B>Title:</B> Set Retain Values
 *<BR>
 *<B>Keywords:</B> transactions
 *<BR>
 *<B>Assertion ID:</B> A13.4.2-18.
 *<BR>
 *<B>Assertion Description: </B>
 The retainValues setting passed to setRetainValues replaces 
 the retainValues setting currently active. 
 */


/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/09/01     1.0
 */
public class SetRetainValues extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A13.4.2-18 (SetRetainValues) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetRetainValues.class);
    }

    /** */
    public void test() {
        pm = getPM();
          
        runTestSetRetainValues(pm);

        pm.close(); 
        pm = null;
    }

    /** */
    void runTestSetRetainValues(PersistenceManager pm) {
        if (!isRetainValuesSupported()) {
            if (debug) logger.debug("RetainValues not supported.");
            return;
        }
        
        Transaction tx = pm.currentTransaction();
        boolean orig = tx.getRetainValues();
        tx.setRetainValues(!orig);
        if (tx.getRetainValues() == orig) {
            fail(ASSERTION_FAILED,
                 "changing the retainValues flag by calling tx.setRetainValues does not have a effect.");
        }
        if ((tx != null) && tx.isActive()) {
        	tx.rollback();
        }
    }
}

