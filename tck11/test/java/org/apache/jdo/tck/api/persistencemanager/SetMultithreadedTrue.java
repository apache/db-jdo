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

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set Multithreaded True
 *<BR>
 *<B>Keywords:</B> multithreaded
 *<BR>
 *<B>Assertion ID:</B> A12.7-1.
 *<BR>
 *<B>Assertion Description: </B>
If PersistenceManager.setMultithreaded is called with a value of true, then the JDO
implementation must perform synchronizations to support multiple application threads. A value of true will be returned
when getMultithreaded is called. In testing, multi-threading should be turned on and then multi-threading tests should
be run.
 */

public class SetMultithreadedTrue extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.7-1 (SetMultithreadedTrue) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetMultithreadedTrue.class);
    }

    /** */
    public void test() {
        pm = getPM();
        
        runTestSetMultithreadedTrue(pm);
        
        pm.close();
        pm = null;
    }
    
    /** */
    public void runTestSetMultithreadedTrue(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.setMultithreaded(true);
            if (!pm.getMultithreaded()) {
                fail(ASSERTION_FAILED,
                     "pm.getMultithreaded() should true true after setting the flag to true.");
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
