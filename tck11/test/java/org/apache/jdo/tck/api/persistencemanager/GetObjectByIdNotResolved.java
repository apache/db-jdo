/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Get ObjectId For Null Or Not Persistent
 *<BR>
 *<B>Keywords:</B> identity
 *<BR>
 *<B>Assertion IDs:</B> A12.5.6-1
 *<BR>
 *<B>Assertion Description: </B>
In a call to PersistenceManager.getObjectById, if the PersistenceManager is
unable to resolve the oid parameter to an ObjectId instance, then it throws a JDOUserException.
 */

public class GetObjectByIdNotResolved extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.6-1 (GetObjectByIdNotResolved) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectByIdNotResolved.class);
    }

    /* passing null paramameter to getObjectId */
    public void testGetObjectByIdNotResolved() {
        pm = getPM();
        if (debug) logger.debug(" *** testObjectByIdNotResolved *** ");
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCPoint p1 = new PCPoint (1,3);
            pm.makePersistent(p1);
            Object oid = pm.getObjectId(p1);
            pm.deletePersistent(p1);
            tx.commit();

            tx.begin();
            try {
                PCPoint p2 = (PCPoint) pm.getObjectById(oid, true);
                fail(ASSERTION_FAILED,
                     "pm.getObjectById(oid, true) should throw JDOObjectNotFoundException, if oid refers to an non existing object");
            } 
            catch (JDOObjectNotFoundException e) {
                // expected exception
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
        pm.close();
        pm = null;
    }
}

