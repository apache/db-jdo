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


package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.JDOObjectNotFoundException;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 *<B>Title:</B> Get Object By Id No Validation Instance Not In Datastore
 *<BR>
 *<B>Keywords:</B> identity exception
 *<BR>
 *<B>Assertion ID:</B> A12.5.6-6.
 *<BR>
 *<B>Assertion Description: </B>
If <code>PersistenceManager.getObjectById</code> is called with a value of
<code>false</code> for the second parameter named <code>validate</code>,
and the instance does not exist in the data store, a subsequent access
of the fields of the instance will throw a <code>JDODatastoreException</code>
if the instance does not exist at that time.

 */

public class GetObjectByIdNoValidationInstanceNotInDatastore extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.6-6 (GetIgnoreCGetObjectByIdNoValidationInstanceNotInDatastore) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectByIdNoValidationInstanceNotInDatastore.class);
    }

    /** */
    public void testGetObjectByIdNoValidationInstanceNotInDatastore() {
        if (debug)
            logger.debug ("\nSTART GetObjectByIdNoValidationInstanceNotInDatastore");
        Object oid = null;
        pm = getPM();
        oid = createPCPointInstance(pm);
        deletePCPointInstance(pm, oid);
    
        try {
            pm.currentTransaction().begin();
            PCPoint p1 = (PCPoint)pm.getObjectById(oid, false); // might throw exception here
            if (debug)
                logger.debug ("Got object in cache, even though not in datastore.");
            p1.getX(); // if not thrown above, throws exception here
            pm.currentTransaction().commit();
            fail(ASSERTION_FAILED,
                 "accessing unknown instance should throw JDOObjectNotFoundException");
        } 
        catch (JDOObjectNotFoundException ex) {
            // expected exception
        }
        if (debug)
            logger.debug ("END GetObjectByIdNoValidationInstanceNotInDatastore");
   }
}
