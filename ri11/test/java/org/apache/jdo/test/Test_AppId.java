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

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.pc.PCPoint1;
import org.apache.jdo.pc.PCPoint1Key;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that FOStore can perform insert/update/fetch/delete for 
* an instance with application identity.
*
* @author Marina Vatkina
*/
public class Test_AppId extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_AppId.class);
    }

    /** */
    public void test() {

        int start = -numInsert;
        int end = numInsert + 1; // exclusive!
        int nrOfObjects = end - start;
        PCPoint1Key pk  = new PCPoint1Key();
        
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        
        try {
            if (debug){
                logger.debug("====================");
                logger.debug("INSERT");
            }
        
            tx.begin();
            for (int i = start; i < end; i++) {
                PCPoint1 p = new PCPoint1(i,10*i);
                pm.makePersistent(p);
                if (debug) logger.debug("Inserting: " + p);
            }
            tx.commit(); tx = null;
            pm.close(); pm = null;

            if (debug){
                logger.debug("====================");
                logger.debug("UPDATE");
            }

            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            for (int i = start; i < end; i++) {
                pk.x = i;
                PCPoint1 p = (PCPoint1)pm.getObjectById(pk, true);
                p.setY(new Integer(i+100));
                if (debug) logger.debug("Updating: " + p);
                if (debug) printObject(p, pm);
            }
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            if (debug) {
                logger.debug("====================");
                logger.debug("EXTENT");
            }
            
            pm = pmf.getPersistenceManager();
            List list = new ArrayList();
            Extent ext = pm.getExtent(org.apache.jdo.pc.PCPoint1.class, false);
            for (java.util.Iterator i = ext.iterator(); i.hasNext();) {
                PCPoint1 p = (PCPoint1)i.next();
                if (debug) logger.debug("Fetched: " + p);
                if (debug) printObject(p, pm);
                list.add(p);
            }
            Collections.sort(list, new PCPoint1Comparator());
            assertEquals("Extent has wrong size", nrOfObjects, list.size());
            for (int i = start; i < end; i++) {
                PCPoint1 p = (PCPoint1)list.get(i - start);
                assertEquals("Wrong value of PCPoint1.x", i, p.getX());
                assertEquals("Wrong value of PCPoint1.y", (i+100), p.getY().intValue());
            }
            pm.close(); pm = null;
            
            if (debug) {
                logger.debug("====================");
                logger.debug("DELETE");
            }
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            for (int i = start; i < end; i++) {
                pk.x = i;
                PCPoint1 p = (PCPoint1)pm.getObjectById(pk, true);
                if (debug) logger.debug("Deleting: " + p);
                if (debug) printObject(p, pm);
                pm.deletePersistent(p);
            }
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            if (debug) {
                logger.debug("====================");
                logger.debug("CHECK DELETED");
            }

            pm = pmf.getPersistenceManager();
            for (int i = start; i < end; i++) {
                pk.x = i;
                try {
                    PCPoint1 p = (PCPoint1)pm.getObjectById(pk, true);
                    fail("Object with primary key " + pk + " was found: " + p);
                } catch (JDODataStoreException ex) {
                    // expected exception => OK;
                    if (debug) logger.debug("Deleted " + pk);
                }
            }
            pm.close(); pm = null;
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    void printObject(PCPoint1 p, PersistenceManager pm) {
        Object oid = pm.getObjectId(p);
        logger.debug("Instance OID: " + oid);

        PersistenceManager pm1 = pmf.getPersistenceManager();
        Object o =  pm1.getObjectById(oid, true);
        logger.debug("Instance from OID: " + o);
        logger.debug("OID from instance: " + pm1.getObjectId(o));
        pm1.close();
    }

    static class PCPoint1Comparator implements Comparator
    {
        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof PCPoint1))
                throw new ClassCastException("instance " + o1 +
                        " + is not a PCPoint1 instance");
            if (!(o2 instanceof PCPoint1))
                throw new ClassCastException("instance " + o2 +
                        " + is not a PCPoint1 instance");
            return new Integer(((PCPoint1)o1).getX()).compareTo(
                new Integer(((PCPoint1)o2).getX()));
        }
    }
}
