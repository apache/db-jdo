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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollection;
import org.apache.jdo.pc.PCId;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* 
* See bugid 4833532.  Tests that a Set/Map containing PC instances which have
* a user-supplied hashCode() referencing persistent fields works.
*
* @author Dave Bristor
* @version 1.0.1
*/
public class Test_UserHashCode extends AbstractTest {
    
    private static final int NUMBER_TO_INITIALIZE = 2;
    
    private static final int NUMBER_TO_ADD = 2;
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_UserHashCode.class);
    }

    /** */
    public void test() {
        populate();
        update(false);
        update(true);
        traverse();
    }

    /** Traverse an extent of objects, printing each one along the way. */
    protected void traverse() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.setOptimistic(false);
            
            if (debug) logger.debug("Begin traverse");
            
            tx.begin();
            
            if (debug) logger.debug("Traverse: getExtent");
            Extent e = pm.getExtent(PCCollection.class, true);
            
            if (debug)
                logger.debug("Traverse: begin iteration of PCCollection Extent");
            int nrOfPCs = 0;
            int nrOfSetElements = 0;
            int nrOfMapEntries = 0;
            for (Iterator i = e.iterator(); i.hasNext();) {
                PCCollection c = (PCCollection)i.next();
                if (debug) logger.debug(c.toString());
                nrOfPCs++;
                Iterator j = c.getSet().iterator();
                while (j.hasNext()) {
                    Object o = j.next();
                    if (debug) logger.debug("Set element: " + o.toString());
                    nrOfSetElements++;
                }
                assertEquals("Wrong number of set elements", 
                             (NUMBER_TO_INITIALIZE + NUMBER_TO_ADD * 2),
                             nrOfSetElements);
                
                Iterator k = c.getMap().entrySet().iterator();
                while (k.hasNext()) {
                    Map.Entry p = (Map.Entry)k.next();
                    if (debug)
                        logger.debug("Map entry: " + p.getKey()+ ";" +
                                     p.getValue());
                    nrOfMapEntries++;
                }
                assertEquals("Wrong number of set elements", 
                             (NUMBER_TO_INITIALIZE + NUMBER_TO_ADD * 2),
                             nrOfSetElements);
            }
            assertEquals("Wrong number of PCCollection instances", 1, nrOfPCs);
            
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** Traverse an extent of objects, updating each one along the way. */
    protected void update(boolean optimistic) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("Running " + (optimistic?"optimistic":"datastore") +
                             " transactions.");
            tx.setOptimistic(optimistic);
            
            if (debug) logger.debug("Begin update");
            
            tx.begin();
            
            if (debug) logger.debug("Update: getExtent");
            Extent e = pm.getExtent(PCCollection.class, true);
            
            if (debug) logger.debug("Update: begin iteration for update");
            for (Iterator i = e.iterator(); i.hasNext();) {
                if (debug) logger.debug("Update: before PCCollection.next()");
                PCCollection c = (PCCollection)i.next();
                if (debug) logger.debug(c.toString());
                for (int a = 0; a < NUMBER_TO_ADD; ++a) {
                    if (debug) logger.debug("Update: before Set.add()");
                    c.getSet().add(new PCId());
                    if (debug) logger.debug("Update: before Map.put()");
                    c.getMap().put(new PCId(), new PCId());
                }
            }
            if (debug) logger.debug("Update: before commit()");
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** Populate an extent of objects. */
    protected void populate() {
        PersistenceManager pm = pmf.getPersistenceManager(); 
        Transaction tx = pm.currentTransaction();
        try {
        if (debug) logger.debug("Begin populate");
            PCCollection pcCollection = new PCCollection();
            pcCollection.setSet(new HashSet());
            pcCollection.setMap(new HashMap());
            pcCollection.setId(0);
            for (int i = 0; i < NUMBER_TO_INITIALIZE; i++) {
                pcCollection.getSet().add(new PCId());
                pcCollection.getMap().put(new PCId(), new PCId());
            }
            tx.setOptimistic(false);
            tx.begin();
            pm.makePersistent(pcCollection);
            tx.commit();
            if (debug) logger.debug("End populate");
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
     }
}
