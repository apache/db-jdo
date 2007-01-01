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

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can rollback an insert and commit the same insert in the
* next transaction.
*
* @author Marina Vatkina
*/
public class Test_Insert2 extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Insert2.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        checkExtent(factory.getPCClass(), 1);
        readObjects();
    }

    /** Tries to insert an object rolled back in a previous tx. */
    protected void insertObjects() throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug) logger.debug("\nINSERT-ROLLBACK-INSERT-COMMIT");
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            PCPoint p = new PCPoint(11, 111);
            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            pm.makePersistent(p);
            if (debug) logger.debug("Make Persistent OK");
            tx.rollback();
            if (debug) logger.debug("Transaction rollback OK");

            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            pm.makePersistent(p);
            if (debug) logger.debug("Make Persistent OK");

            // This try-catch block is usefull for internal testing only.
            // It should not affect this test in a regular test runs.
            try {
                tx.commit();
                if (debug) logger.debug("Transaction commit OK");
            } catch (JDOException e) {
                if (debug) logger.debug("Transaction active: " + tx.isActive());
                throw e;
            }
            announce("inserted ", p);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }

    /** */
    protected void verify(int i, Object pc) {
        assertEquals("wrong PCPoint instance", new PCPoint(11, 111), pc);
    }
    
}

