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
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Get Object By Id With Validation, Instance Not in Cache, Not in Datastore
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion IDs:</B> A12.6.5-2.
 *<BR>
 *<B>Assertion Description: </B>
Object getObjectById (Object oid, boolean validate) with validate==true. If the instance does not exist in the datastore, then a JDOObjectNotFoundException is thrown.
 */

public class GetObjectByIdWithValidationInstanceNotInCacheNotInDatastore extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.6.5-2 (GetObjectByIdWithValidationInstanceNotInCacheNotInDatastore) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectByIdWithValidationInstanceNotInCacheNotInDatastore.class);
    }

    /** */
    public void test() {
        pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.setRestoreValues(false); //This should cause eviction of transactional instances when transaction is later rolled back.
        tx.begin();		
        PCPoint p1 = new PCPoint (1,3);
        pm.makePersistent(p1);
        Object oid = pm.getObjectId(p1);
        tx.rollback();

        tx.begin();
        try {
            PCPoint p2 = (PCPoint) pm.getObjectById(oid, true);
            fail(ASSERTION_FAILED, "pm.getObjectById(oid, true) should throw JDOObjectNotFoundException, if oid refers to an non existing object");
        }
        catch (JDOObjectNotFoundException ex) {
            // expected exception
        }
        finally {
            tx.rollback();
        }
    }
}
