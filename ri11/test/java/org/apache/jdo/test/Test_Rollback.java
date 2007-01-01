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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.test.util.Util;

/**
* Tests that we can rollback and restore values.  Reads an extent of PCPoint
* objects, and set the coordinate values in them to all be x = 555, y = 1212.
*
* @author Marina Vatkina
*/
public class Test_Rollback extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Rollback.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        updateObject(false, false);
        checkExtent(factory.getPCClass(), numInsert);
    }
    
    /** */
    public void testRestoreValues() throws Exception {
        insertObjects();
        updateObject(false, true);
        checkExtent(factory.getPCClass(), numInsert);
    }

    /** */
    public void testOptimistic() throws Exception {
        insertObjects();
        updateObject(true, false);
        checkExtent(factory.getPCClass(), numInsert);
    }

    /** */
    public void testOptimisticRestoreValues() throws Exception {
        insertObjects();
        updateObject(true, true);
        checkExtent(factory.getPCClass(), numInsert);
    }

    // Gets an extent of points, and changes them.
    protected void updateObject(boolean optimistic, boolean restoreValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("\nROLLBACK with RestoreValues: " + restoreValues + 
                             " in " + (optimistic?"OPTIMISTIC":"DATASTORE") +
                             " Transaction");

            tx.setOptimistic(optimistic);
            tx.setRestoreValues(restoreValues);
            tx.setNontransactionalRead(true);

            tx.begin();
            Extent e = pm.getExtent(PCPoint.class, false);

            // Change every *other* value by increasing the existing values by a
            // well known (phone) number
            int count = 0;
            // Use ArrayList to store objects. This allows to verify fields
            // without going to the database.
            List arr = new ArrayList();
            List oids = new ArrayList();
            for (Iterator i = PCPoint.getSortedIterator(e.iterator()); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                arr.add(p);
                oids.add(pm.getObjectId(p)); 

                if (count++ % 2 == 1) {
                    if (debug)
                        logger.debug("delete : " + Util.getClassName(p) +
                                     ": " + JDOHelper.getObjectId(p) + ", " + p);
                    pm.deletePersistent(p);
                    assertTrue("isDeleted before rollback", JDOHelper.isDeleted(p));
                    continue;
                }
                
                int x = p.getX() + 755;
                Integer yi = p.getY();
                
                int y = 0;
                if (null != yi) { // should not happen, but ignore it and continue
                    y = yi.intValue();
                }
                y += 7212;
                
                if (debug)
                    logger.debug("update to x=" + x + ", y=" + y + ": " + 
                                 Util.getClassName(p) + ": " +
                                 JDOHelper.getObjectId(p) + ", " + p);
                
                p.setX(x);
                p.setY(new Integer(y));
                assertTrue("isDirty before rollback", JDOHelper.isDirty(p));
                assertEquals("getX before rollback", x, p.getX());
                assertEquals("getY before rollback", new Integer(y), p.getY());

                if (debug)
                    logger.debug("updated to x=" + x + ", y=" + y + ": " + 
                                 Util.getClassName(p) + ": " +
                                 JDOHelper.getObjectId(p) + ", " + p);
            }
            tx.rollback(); tx = null;
            
            count = 0;
            for (Iterator i = arr.iterator(); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                if (debug)
                    logger.debug("restored to x=" + p.x + ", y=" + p.y + ": " + 
                                 Util.getClassName(p) + ": " +
                                 JDOHelper.getObjectId(p) + ", " + p);
                verifyPCPoint(p, count);
                count++;
            }
            pm.close(); pm = null;

            // read the instances in a new pm and verify that no updates
            // are stored in the datastore

            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            count = 0;
            for (Iterator i = oids.iterator(); i.hasNext();) {
                PCPoint p = (PCPoint)pm.getObjectById(i.next(), true);
                verifyPCPoint(p, count);
                count++;
            }
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
    private void verifyPCPoint(PCPoint p, int count)
    {
        assertFalse("isDirty after rollback", JDOHelper.isDirty(p));
        assertFalse("isDeleted after rollback", JDOHelper.isDeleted(p));
        assertEquals("getX after rollback", count, p.getX());
        assertEquals("getY after rollback", new Integer(count), p.getY());
    }

    /** */
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }

    /** */
    protected int getDefaultInsert()
    {
        return 5;
    }   
}
