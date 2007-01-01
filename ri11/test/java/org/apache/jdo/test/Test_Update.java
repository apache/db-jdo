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

/**
* Tests that we can update objects in a database.  Reads an extent of PCPoint
* objects, and set the coordinate values in them to all be x = 555, y = 1212.
*
* @author Dave Bristor
*/
public class Test_Update extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Update.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        update(false);
        checkExtent(factory.getPCClass(), numInsert);
    }

    /** */
    public void testOptimistic() throws Exception {
        insertObjects();
        update(true);
        checkExtent(factory.getPCClass(), numInsert);
    }

    // Gets an extent of points, and changes them.
    protected void update(boolean optimistic) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("UPDATE in " + (optimistic?"OPTIMISTIC":"DATASTORE") +
                             " Transaction");
            tx.setOptimistic(optimistic);
            tx.setNontransactionalRead(true);
        
            tx.begin();
            Extent e = pm.getExtent(PCPoint.class, false);

            // Increase the existing values by a well known (phone) number
            // Use ArrayList to store objects. This allows to verify fields
            // without going to the database.
            List arr = new ArrayList();
            List oids = new ArrayList();
            int count = 0;
            for (Iterator i = PCPoint.getSortedIterator(e.iterator()); i.hasNext();) {
                PCPoint p = (PCPoint)i.next(); 
                arr.add(p);
                oids.add(pm.getObjectId(p));
                
                // Change every *other* value
                if (count++ % 2 == 1) {
                    continue;
                }
                
                int x = p.getX() + 555;
                Integer yi = p.getY();
                int y = 0;
                if (null != yi) { // should not happen, but ignore it and continue
                    y = yi.intValue();
                }
                y += 1212;
                
                if (debug) logger.debug("updated to x=" + x + ", y=" + y + ": " + p);
                
                p.setX(x);
                p.setY(new Integer(y));
                assertTrue("IsDirty: ", JDOHelper.isDirty(p));
                assertEquals("getX before commit", x, p.getX());
                assertEquals("getY before commit", new Integer(y), p.getY());
            }
            tx.commit(); tx = null;

            // verify updated values in same pm

            count = 0;
            for (Iterator i = arr.iterator(); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                verifyPCPoint(p, count);
                count++;
            }
            pm.close(); pm = null;

            // re-read objects in a new pm and verify updated values

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
        if (debug) logger.debug("verify " + p);
        assertFalse("isDirty after commit", JDOHelper.isDirty(p));
        assertFalse("isDeleted after commit", JDOHelper.isDeleted(p));
        int x = count;
        int y = count;
        if (count % 2 != 1) {
            x += 555;
            y += 1212;
        }
        assertEquals("getX after rollback", x, p.getX());
        assertEquals("getY after rollback", new Integer(y), p.getY());
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
