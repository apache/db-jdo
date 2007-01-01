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

package org.apache.jdo.test;

import java.util.Iterator;
import java.util.TreeMap;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.appid.PCPoint;
import org.apache.jdo.pc.appid.PCRect;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test a PC with application identity that has some fields which are PC's.
*
* @author Marina Vatkina
*/
public class Test_RectAppId extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_RectAppId.class);
    }

    /** The idea is that we're going to write a bunch of stuff to a data
     * store, then read it back in; we should get the same data back.
     */
    public void test() throws Exception {
        insertObjects();
    }

    /** We override this to insert our own objects */
    protected void insertObjects() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug("INSERT");
            tx.begin();

            PCPoint p1 = new PCPoint(1, 3);
            PCPoint p2 = new PCPoint(13, 42);
            PCRect pcRect = new PCRect(1, p1, p2);
    
            if (debug) logger.debug("Before insert: " + pcRect);
            
            pm.makePersistent(pcRect);
            if (debug) {
                logger.debug("After makePersistent pcRect OID: " +
                             pm.getObjectId(pcRect));
                logger.debug("After makePersistent p1 OID: " +
                             pm.getObjectId(p1));
                logger.debug("After makePersistent p2 OID: " +
                             pm.getObjectId(p2));
            }
    
            tx.commit(); tx = null;
            
            loadAndVerifyObject(p1, pm);
            loadAndVerifyObject(p2, pm);
            loadAndVerifyObject(pcRect, pm);
    
            pm.close(); pm = null;
    
            if (debug) logger.debug("EXTENT PCRect");
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            Extent ext =
                pm.getExtent(org.apache.jdo.pc.appid.PCRect.class, false);
            for (Iterator i = ext.iterator(); i.hasNext();) {
                Object p = i.next();
                if (debug) logger.debug("Fetched: " + p);
                loadAndVerifyObject(p, pm);
            }
    
            if (debug) logger.debug("EXTENT PCPoint");
            ext = pm.getExtent(org.apache.jdo.pc.appid.PCPoint.class, false);
            TreeMap elements = new TreeMap(); // Sort the results.
            for (Iterator i = ext.iterator(); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                elements.put(p.toString(), p);
            }    

            for (Iterator i = elements.values().iterator(); i.hasNext();) {
                Object p = i.next();
                if (debug) logger.debug("Fetched: " + p);
                loadAndVerifyObject(p, pm);
            }
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
     }

    /** */
    void loadAndVerifyObject(Object obj, PersistenceManager pm) {
        Object oid = pm.getObjectId(obj);
        if (debug) logger.debug("Instance OID: " + oid);

        PersistenceManager pm1 = null;
        try {
            pm1 = pmf.getPersistenceManager();
            Object obj1 = pm1.getObjectById(oid, true);
            if (debug) logger.debug("Instance from OID: " + obj1);
            assertEquals("Wrong string represenatation of loaded instance",
                         obj.toString(), obj1.toString());
            Object oid1 = pm1.getObjectId(obj1);
            if (debug) logger.debug("OID from instance: " + oid1);
            assertEquals("OID from instance not equal", oid, oid1);
        }
        finally {
            if (pm1 != null && !pm1.isClosed())
                pm1.close();
        }
    }

}
