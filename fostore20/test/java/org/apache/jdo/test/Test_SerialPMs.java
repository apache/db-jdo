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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can insert objects into a database.  Creates objects as per
* Test_ActivateClass, and writes their OIDs into a file.
* Test_FetchInserted reads that file, and fetches the objects by OID.
*
* Both this and Test_Insert can be instructed (via the "insert" JVM property)
* to insert some number of objects.  If that is greater than one, Test_Insert
* inserts all its instances using one PersistenceManager.  Test_SerialPMs, on
* the other hand, uses a different PM for each insert.
*
* @author Dave Bristor
*/
public class Test_SerialPMs extends AbstractTest {
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SerialPMs.class);
    }

    /** */
    protected void insertObjects() {
        PersistenceManager[] pms = new PersistenceManager[numInsert];
        Transaction[] txs = new Transaction[numInsert];

        try {
            if (debug) logger.debug("INSERT");
            for (int i = 0; i < numInsert; i++) {
                pms[i] = pmf.getPersistenceManager();
                txs[i] = pms[i].currentTransaction();
                txs[i].begin();
            }
            
            for (int i = 0; i < numInsert; i++) {
                Object pc = factory.create(i);
                pms[i].makePersistent(pc);
            }
            for (int i = 0; i < numInsert; i++) {
                txs[i].commit();
            }
        }
        finally {
            for (int i = 0; i < numInsert; i++) {
                Transaction tx = txs[i];
                if (tx != null && tx.isActive())
                    tx.rollback();
                PersistenceManager pm = pms[i];
                if (pm != null && !pm.isClosed())
                    pm.close();
            }
        }
        if (debug) logger.debug("inserted " + numInsert + " objects");
    }
    
    protected int getDefaultInsert() {
        return 50;
    }
    
    /**
     * Inserts some number of objects in the database
     */
    public void test() {
        insertObjects();
        checkExtent(factory.getPCClass(), numInsert);
    }
    
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }
}
