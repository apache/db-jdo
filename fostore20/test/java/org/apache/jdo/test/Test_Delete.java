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

import java.io.EOFException;
import java.io.ObjectInputStream;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.test.util.Factory;

/**
* This test is similar to Test_ActivateClass, but it adds extra steps of
* getting OIDs for objects and deleting those objects.
*
* @author Dave Bristor
*/
public class Test_Delete extends AbstractTest {

    /** */
    private static boolean silent = false;
    
    /** */
    public static void main(String args[]) {
        handleArgs(args);
        if (silent)
            runSilentDelete();
        else
            JDORITestRunner.run(Test_Delete.class);
    }

    /** */
    public void test() throws Exception {
        if (!existing) {
            insertObjects();
            writeOIDs();
        }
        deleteObjects(false, false);
        checkExtent(factory.getPCClass(), 0);
    }

    /** */
    public void testOptimistic() throws Exception {
        if (!existing) {
            insertObjects();
            writeOIDs();
        }
        deleteObjects(true, false);
        checkExtent(factory.getPCClass(), 0);
    }

    /** */
    public void testValidate() throws Exception {
        if (!existing) {
            insertObjects();
            writeOIDs();
        }
        deleteObjects(false, true);
        checkExtent(factory.getPCClass(), 0);
    }

    /** */
    public void testOptimisticValidate() throws Exception {
        if (!existing) {
            insertObjects();
            writeOIDs();
        }
        deleteObjects(true, true);
        checkExtent(factory.getPCClass(), 0);
    }

    /** */
    void deleteObjects(boolean optimistic, boolean validate) throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            ObjectInputStream in = getOIDFileInputStream();
            tx.setOptimistic(optimistic);
            tx.begin();
            
            try {
                while (true) {
                    Object pc = null;
                    Object oid = in.readObject();
                    if (debug) logger.debug("fetching: " + oid);
                    pc = pm.getObjectById(oid, validate);
                    if (debug) logger.debug("Before delete: " + pc);
                    pm.deletePersistent(pc);
                    assertTrue("IsDeleted", JDOHelper.isDeleted(pc));
                }
            } catch (EOFException ex) {
                // OK
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
    protected Factory getFactory(int verify) {
        return getFactoryByClassProperty(verify, "org.apache.jdo.pc.PCPoint");
    }

    /** */
    protected int getDefaultInsert()
    {
        return 5;
    }

    /** */
    private static void runSilentDelete() {
        try {
            Test_Delete delete = new Test_Delete();
            delete.setupPMF();
            delete.test();
            delete.closePMF();
        }
        catch (Exception ex) {
            System.out.println("Excetion during delete");
            ex.printStackTrace();
        }
    }

    /** */
    private static void handleArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-silent".equals(args[i]))
                silent = true;
            else
                System.err.println("Test_Delete: ignoring unknon option" + args[i]);
        }
    }
}
