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

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Changing ObjectId Has No Effect On Instance
 *<BR>
 *<B>Keywords:</B> identity
 *<BR>
 *<B>Assertion ID:</B> A12.5.6-13.
 *<BR>
 *<B>Assertion Description: </B>
If the application makes a change to the <code>ObjectId</code> instance
returned by <code>PersistenceManager.getObjectId</code>, there is no effect
on the instance from which the <code>ObjectId</code> was obtained.

 */

public class ChangingObjectIdHasNoEffectOnInstance extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.6-13 (ChangingObjectIdHasNoEffectOnInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ChangingObjectIdHasNoEffectOnInstance.class);
    }

    /** */
    public void testChangingObjectIdHasNoEffectOnInstance() throws Exception {
        pm = getPM();
        Object oid = createPCPointInstance(pm);
        Object p1 = pm.getObjectById(oid, false);
        Object oid2 = pm.getObjectId(p1);
        
        mangleObject(oid);
        // now oid3 should equal oid2 even though we mangled oid
        Object oid3 = getPM().getObjectId(p1);
        if (!oid2.equals(oid3))
        	fail(ASSERTION_FAILED,
        	     "Changing the ObjectId returned by getObjectId has an effect on ObjectId's returned by subsequent calls of getObjectId");
        pm.close();
        pm = null;
    }          
}
