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

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.pc.PCPoint1;

/**
* Tests that FOStore can correctly determine dups for application identity.
*
* @author Marina Vatkina
*/
public class Test_DupAppId extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_DupAppId.class);
    }

    /** */
    public void test() {
        insertObjects();
        checkExtent(PCPoint1.class, 1);
    }

    /** */
    public void insertObjects() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            PCPoint1 p = new PCPoint1(1,10);
            PCPoint1 p1 = new PCPoint1(1,100);
            if (debug) logger.debug("INSERT");
            tx.begin();
            if (debug) logger.debug("Inserting: " + p);
            pm.makePersistent(p);
            try {
                if (debug) logger.debug("Inserting: " + p1);
                pm.makePersistent(p1);
                fail("Missing JDOException during makePersistent with the same primary key in the PersistenceManager cache.");
            } catch (JDOException ex) {
                // expected exception => OK
                if (debug) logger.debug("caught expected " + ex);
            }
            tx.commit(); tx = null;
            
            if (debug) {
                logger.debug("Inserted: " + p);
                logger.debug("P isPersistent: " + JDOHelper.isPersistent(p));
            }
            assertTrue("instance " + p + " + expected to be persistent", 
                       JDOHelper.isPersistent(p));
            if (debug) 
                logger.debug("P1 isPersistent: " + JDOHelper.isPersistent(p1));
            assertFalse("instance " + p1 + " + expected to be not persistent",
                        JDOHelper.isPersistent(p1));
            
            pm.close(); pm = null;

            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            if (debug) logger.debug("INSERT DUPS");
            tx.begin();
            if (debug) logger.debug("Inserting: " + p1);
            pm.makePersistent(p1);
            try {
                tx.commit(); 
                fail("Missing JDOException during commit when inserting a duplicate in the datastore.");
            } catch (Exception ex) {
                // expected exception => OK
                if (debug) logger.debug("caught expected " + ex);
            }
            if (debug) 
                logger.debug("P1 isPersistent: " + JDOHelper.isPersistent(p1));
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

}
