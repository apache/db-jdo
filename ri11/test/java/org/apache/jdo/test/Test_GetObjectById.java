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

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* This test is similar to Test_ActivateClass, but it adds extra steps of
* getting OIDs for objects and later retrieving those objects.
*
* @author Dave Bristor
*/
public class Test_GetObjectById extends AbstractTest {
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_GetObjectById.class);
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Object o = new Object();
            Object o2 = pm.getObjectById(o, false);
            throw new RuntimeException("Expected exception not caught from getObjectById()");
        } catch (JDOUserException ex) {
            // this is expected
        }
        finally {
            pm.close();
        }
            
        pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Object inserted[] = new Object[numInsert];
            for (int i = 0; i < numInsert; i++) {
                Object pc = factory.create(i);
                pm.makePersistent(pc);
                inserted[i] = pc;

                Object oid = pm.getObjectId(pc);
                Object obj = pm.getObjectById(oid, false);
                announce("GetObjectId for " + oid + ", obj is " + obj + ", pc is ", pc);
                assertSame(("object mismatch at " + i), pc, obj);
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

    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }
}
