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

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * This tests exists to demonstrate that a flushed persistent new instance is
 * correctly rolled back.  See 4522830.
 */
public class Test_RollbackFlushedNew extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_RollbackFlushedNew.class);
    }

    /**/
    public void test() throws Exception
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.setOptimistic(false);
            tx.begin();
        
            // create new PCPoint
            PCPoint newPoint = new PCPoint(5, 10);
            pm.makePersistent(newPoint);
            
            // Create query
            Query query = pm.newQuery(PCPoint.class);
            query.setFilter("true");
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            // Query.execute flushes the new PCPoint instance
            Object result = query.execute();
            
            // iterate result set
            int nrOfObjects = 0;
            for (Iterator i = ((Collection)result).iterator(); i.hasNext();) {
                i.next();
                nrOfObjects++;
            }
            if (nrOfObjects != 1)
                throw new Exception("Query result has wrong number of objects; expected 1, actual " + nrOfObjects);
            // Rollback the transaction
            tx.rollback();
            
            checkExtent(PCPoint.class, 0);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
