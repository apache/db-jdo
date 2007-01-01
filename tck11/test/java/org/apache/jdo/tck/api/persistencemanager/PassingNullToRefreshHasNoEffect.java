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


package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Passing Null to Refresh has no Effect
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion IDs:</B> A12.5-8
 *<BR>
 *<B>Assertion Description: </B>
Passing a null value to PersistenceManager.refresh will have no effect. A NullPointerException should NOT be thrown.

 */

public class PassingNullToRefreshHasNoEffect extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5-8 (PassingNullToRefreshHasNoEffect) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(PassingNullToRefreshHasNoEffect.class);
    }

    /** */
    public void test() {
        pm = getPM();

        runTestPassingNullToRefreshHasNoEffect(pm);
        
        pm.close();
        pm = null;
    }

    /* test evict (object pc) */
    private void runTestPassingNullToRefreshHasNoEffect(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = null;
            pm.refresh(p1);
            if (debug)
                logger.debug (" \nPASSED in testPassingNullToRefreshHasNoEffect()");
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
