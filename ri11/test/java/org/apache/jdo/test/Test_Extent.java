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

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.test.util.Factory;

/**
* This test retrieves an extent-full of instances.
*
* @author Dave Bristor
*/
public class Test_Extent extends AbstractTest {
    /** If true, get subclass instances too. */
    private final boolean subclasses;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Extent.class);
    }

    /** */
    public Test_Extent() {
        super();
        this.subclasses = Boolean.getBoolean("subclasses");
    }

    /** */
    public void testOutsideTx() throws Exception {
        insertObjects();
        runOutsideTx();
    }
    
    /** */
    public void testDatastoreTx() throws Exception {
        insertObjects();
        runInsideTx(false);
        }

    /** */
    public void testOptimisticTx() throws Exception {
        insertObjects();
        runInsideTx(true);
        }

    /** */
    public void testNewInstance() throws Exception {
        insertObjects();
        runNewInstance(false);
    }

    /** */
    public void testNewInstanceIgnoreCache() throws Exception {
        insertObjects();
        runNewInstance(true);
    }

    /** */
    public void testDeleteInstance() throws Exception {
        insertObjects();
        runDeleteInstance(false);
    }
    
    /** */
    public void testDeleteInstanceIgnoreCache() throws Exception {
        insertObjects();
        runDeleteInstance(true);
    }

    /** */
    protected void runOutsideTx() {
        PersistenceManager pm = null;
        try {
            if (debug) logger.debug("getExtent outside of a transaction");
            pm = pmf.getPersistenceManager();
            getExtent(pm, factory.getPCClass(), numInsert);
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    protected void runInsideTx(boolean optimistic) {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug)
                logger.debug("getExtent in " + (optimistic?"OPTIMISTIC":"DATASTORE") +
                             " Transaction");
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.setOptimistic(optimistic);
            tx.begin();
            getExtent(pm, factory.getPCClass(), numInsert);
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
    protected void runNewInstance(boolean ignoreCache) {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug)
                logger.debug("getExtent in with persistence-new instance and ignoreCache " + ignoreCache);
            pm = pmf.getPersistenceManager();
            pm.setIgnoreCache(ignoreCache);
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(factory.create(numInsert));
            getExtent(pm, factory.getPCClass(), numInsert + 1);
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
    protected void runDeleteInstance(boolean ignoreCache) {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug)
                logger.debug("getExtent in with persistence-deleted instance and ignoreCache " + ignoreCache);
            pm = pmf.getPersistenceManager();
            pm.setIgnoreCache(ignoreCache);
            tx = pm.currentTransaction();
            tx.begin();
            Object toBeDeleted = pm.getObjectById(oids.get(oids.size()-1), false);
            if (debug) logger.debug("Now delete " + toBeDeleted);
            pm.deletePersistent(toBeDeleted);
            getExtent(pm, factory.getPCClass(), numInsert - 1);
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
    protected void getExtent(PersistenceManager pm,
                             Class clazz, int expectedCount) {
        String className = clazz.getName();
        if (debug) logger.debug("\nEXTENT of " + className);
        Extent e = pm.getExtent(clazz, subclasses);
        
        Map elements = new TreeMap();
        for (Iterator i = e.iterator(); i.hasNext();) {
            Object pc = i.next();
            elements.put(JDOHelper.getObjectId(pc), pc);
        }
        int objCount = 0;
        for (Iterator k = elements.values().iterator(); k.hasNext();) {
            Object pc = k.next();
            verify(objCount, pc);
            if (debug) logger.debug(pc.toString());
            objCount++;
        }
        if (debug)
            logger.debug("extent of " + className + " has " + objCount +
                         " objects\n");
        assertEquals("extent of " + className + " has wrong number of instances", 
                     expectedCount, objCount);
    }

    /** */
    protected Factory getFactory(int verify) {
        return getFactoryByClassProperty(verify, "org.apache.jdo.pc.PCPoint");
    }

    /** */
    protected int getDefaultInsert()
    {
        return 5;
    }

    /** */
    protected int getDefaultVerify()
    {
        return 1;
    }

    /** */
    protected void verify(int i, Object pc) {
        assertEquals("Wrong instance type", factory.getPCClass(), pc.getClass());
        super.verify(i, pc);
    }
    
}
