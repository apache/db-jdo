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

package org.apache.jdo.tck.api.persistencemanager.flags;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 *<B>Title:</B> Set IgnoreCache To False
 *<BR>
 *<B>Keywords:cache
 *<BR>
 *<B>Assertion ID:</B> A12.5.3-3.
 *<BR>
 *<B>Assertion Description: </B>
The PersistenceManager.setIgnoreCache method called with a value of false instructs the
query engine that the user expects queries to return results that reflect changed values in the cache.


 */

public class SetIgnoreCacheToFalse extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.3-3 (SetIgnoreCacheToFalse) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetIgnoreCacheToFalse.class);
    }

    /** */
    public void test() {
        pm = getPM();
    
        runTestSetIgnoreCacheToFalse(pm);
        
        pm.close();
        pm = null;
    }

    /** */
    private void runTestSetIgnoreCacheToFalse(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            PCPoint p1 = new PCPoint();
            tx.begin();
            pm.setIgnoreCache(false);
            if (pm.getIgnoreCache()) {
                fail(ASSERTION_FAILED,
                     "pm.getIgnoreCache() should return false after setting the flag to false.");
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
