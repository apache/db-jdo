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

/*
 * Test_StringOID.java
 *
 * Created on August 22, 2001, 10:49 AM
 */

package org.apache.jdo.test;

/**
 *
 * @author  Craig Russell
 */
import java.util.Iterator;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test getting OIDs and creating new OIDs from them.
*/
public class Test_StringOID extends EmpDeptSupport {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_StringOID.class);
    }

    /** The idea is that we're going to write a bunch of stuff to a data
     * output stream, then read it back in; we should get the same data
     * back. 
     */
    public void test() {
        insertObjects();
    }

    /** This method can be overridden by a subclass to perform special checks.
     */
    protected void checkObjects(PersistenceManager pm) {
        if (debug) logger.debug ("Verifying OIDs of all instances");
        for (Iterator it = persistentInstances.iterator(); it.hasNext();) {
            Object oldObject = it.next();
            Object oldOid = JDOHelper.getObjectId(oldObject);
            Object newOid = pm.newObjectIdInstance(oldObject.getClass(), oldOid.toString());
            Object newObject = pm.getObjectById(newOid, false);
            if (debug) {
                logger.debug ("  ---------------");
                logger.debug ("  old object id: " + oldOid.toString());
                logger.debug ("  new object id: " + newOid.toString());
                logger.debug ("  old object: " + oldObject.toString());
                logger.debug ("  new object: "  + newObject.toString());
            }
            assertNotNull("Null returned by getObjectById", newObject);
            assertSame("Mismatch between old and new objects", oldObject, newObject);
        }
    }
}
