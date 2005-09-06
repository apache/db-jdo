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

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Only one instance of persistent object in cache per PersistenceManager
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion ID:</B> A5.4-2.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations must manage the cache of JDO Instances such that there is
only one JDO Instance, associated with each JDO <code>PersistenceManager</code>,
representing the persistent state of each corresponding data store object.
(One needs a hashtable that uses <code>==</code> in order to test this).

 */

public class OneInstanceOfObjectPerPersistenceManager extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A5.4-2 (OneInstanceOfObjectPerPersistenceManager) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(OneInstanceOfObjectPerPersistenceManager.class);
    }

    /** */
    public void test() {
        pm = getPM();
        Transaction tx = pm .currentTransaction();
        try {
            tx.setRetainValues(false);
            tx.setRestoreValues(false);
    
            tx.begin();
            PCPoint p1 = new PCPoint(10, 20);
            PCPoint p2 = new PCPoint(20, 40);
            PCRect rect = new PCRect(0, p1, p2);
            pm.makePersistent(rect);
            tx.commit();
    
            tx.begin();
            Object p1Id = pm.getObjectId(p1);
            PCPoint p1a = (PCPoint)pm.getObjectById(p1Id, true);
            PCPoint p1b = rect.getUpperLeft();
            PCPoint p1c = findPoint(10, 20);
            tx.commit();
            tx = null;
    
            if (p1 != p1a) {
                fail(ASSERTION_FAILED, "getObjectById results differ");
            }
            if (p1 != p1b) {
                fail(ASSERTION_FAILED, "navigation results differ");
            }
            if (p1 != p1c) {
                fail(ASSERTION_FAILED, "query results differ");
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
        pm.close();
        pm = null;
    }

    /** */
    private PCPoint findPoint (int x, int y) {
        Query q = getPM().newQuery (PCPoint.class);
        q.declareParameters ("int px, int py");
        q.setFilter ("x == px & y == py");
        Collection results = (Collection)q.execute (new Integer(x), new Integer(y));
        Iterator it = results.iterator();
        PCPoint ret = (PCPoint)it.next();
        return ret;
    }
    
}
