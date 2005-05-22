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

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO HashSet correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOHashSet extends Test_SCO_Base {


    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOHashSet.class);
    }

 
    /** */
    public void test() {
        insertObjects();
        runHashSetTest(false, true);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runHashSetTest(false, true);
    }
    
    /** */
    public void testOptimistic() {
        insertObjects();
        runHashSetTest(true, false);
    }
    
    /** */
    public void testOptimisticRetainValues() {
        insertObjects();
        runHashSetTest(true, true);
    }
    
    /** */
    protected void runHashSetTest(boolean optimistic, boolean retainValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("Running " + (optimistic?"optimistic":"datastore") + 
                             " transactions with retainValues = " + retainValues);
            tx.setOptimistic(optimistic);
            tx.setRetainValues(retainValues);
            tx.setNontransactionalRead(false);
            
            ArrayList c = new ArrayList();
            PCPoint p = new PCPoint(1, 1);
            c.add(p);
            p = new PCPoint(2, 2);
            c.add(p);
            
            tx.begin();
            PCCollections pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();

            tx.begin();
            HashSet set = null;
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet: ", set, 8);
            set.add(p);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after add: ", set, 9);
            set.remove(p);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after remove: ", set, 8);
            set.addAll(c);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after addAll 2: ", set, 10);
            set.retainAll(c);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after retainAll 2: ", set, 2);
            HashSet h = (HashSet) set.clone();
            h.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after clone: ", set, 2);
            assertNullOwner("Owner of clone: ", (SCOCollection)h);
            set.removeAll(c);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after removeAll 2: ", set, 0);
            set.addAll(c);
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after addAll 2: ",set, 2);
            set.clear();
            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet after clear: ", set, 0);
            pcCollections.setSCOHashSet(new HashSet());
            PCPoint pcPoint = new PCPoint(42, 99);
            pcCollections.getSCOHashSet().add(pcPoint);

            tx.commit();

            tx.begin();
            set = pcCollections.getSCOHashSet();
            assertCollectionSize("HashSet restored: ", set, 8);
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** */
    protected void insertObjects() {
        insertAllTypes();
    }
}
