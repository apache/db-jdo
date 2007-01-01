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

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests nontransactional read/write access
*
* @author Marina Vatkina
*/
public class Test_NonTxAccess extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_NonTxAccess.class);
    }

    /** */
    public void testNonTxReadTrue() throws Exception {
        insertObjects();
        nonTxRead(false);
        checkExtent(factory.getPCClass(), numInsert);
        readObjects();
    }

    /** */
    public void testNonTxReadFalse() throws Exception {
        insertObjects();
        nonTxRead(true);
        checkExtent(factory.getPCClass(), numInsert);
        readObjects();
    }

    /** */
    public void testNonTxWriteTrue() throws Exception {
        insertObjects();
        nonTxWrite(false);
        checkExtent(factory.getPCClass(), numInsert);
        readObjects();
    }

    /** */
    public void testNonTxWriteFalse() throws Exception {
        insertObjects();
        nonTxWrite(true);
        checkExtent(factory.getPCClass(), numInsert);
        readObjects();
    }
    
    // Gets an extent of points, and accesses them.
    protected void nonTxRead(boolean nontxRead) {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug) logger.debug("\nREAD: " + nontxRead);

            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.setNontransactionalRead(nontxRead);

            // Print the object by accessing its fields
            Extent ext = pm.getExtent(PCPoint.class, false);
            int count = 0;
            for (Iterator i = PCPoint.getSortedIterator(ext.iterator()); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                p.getX(); // access the object
                if (debug) logger.debug("Read: " + p);
                if (!nontxRead)
                    fail("Reading a pc instance should fail if non-tx read is false");
            }
        } 
        catch (JDOUserException e) {
            if (!nontxRead) {
                if (debug) logger.debug("Caught expected exception on read");
            } else {
                throw e;
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // Gets an extent of points, and update them.
    protected void nonTxWrite(boolean nontxWrite) {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug) logger.debug("\nWRITE: " + nontxWrite);
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.setNontransactionalRead(true);
            tx.setNontransactionalWrite(nontxWrite);
            
            Extent ext = pm.getExtent(PCPoint.class, false);
            
            // Print the object by accessing its fields
            int count = 0;
            for (Iterator i = PCPoint.getSortedIterator(ext.iterator()); i.hasNext();) {
                PCPoint p = (PCPoint)i.next();
                try {
                    p.setY(new Integer(1234));
                    if (debug) logger.debug("Updated: " + p);
                    assertEquals("Wrong value of p.y", new Integer(1234), p.getY());
                    if (!nontxWrite)
                        fail("Modifying a pc instance should fail if non-tx write is false");
                } catch (JDOUserException e) {
                    if (!nontxWrite) {
                        if (debug)
                            logger.debug("Caught expected exception on write");
                    } else {
                        throw e;
                    } 
                }
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    protected Factory getFactory(int ignore) {
        PointFactory rc = new PointFactory();
        // do verify in any case
        rc.setVerify(verify);
        return rc;
    }

    /** */
    protected int getDefaultInsert()
    {
        return 4;
    }   

    /** */
    protected int getDefaultVerify() {
        return 1;
    }

}
