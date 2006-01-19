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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 *<B>Title:</B> Get ObjectId Class For Abstract or Non-PersistenceCapable Class
 *<BR>
 *<B>Keywords:</B> identity
 *<BR>
 *<B>Assertion IDs:</B> A12.10-2
 *<BR>
 *<B>Assertion Description: </B>
A call to PersistenceManager.getObjectIdClass returns null if the class is abstract or not persistence-capable, or the parameter is null.
 */

public class GetObjectIdClassForAbstractOrNonPersistenceCapableClass extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.10-2 (GetObjectIdClassForAbstractOrNonPersistenceCapableClass) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectIdClassForAbstractOrNonPersistenceCapableClass.class);
    }

    /** */
    public void testGetObjectIdClassForAbstractOrNonPersistenceCapableClass() {
        pm = getPM();
        runTestNonPcClass(pm);
        runTestNullParam(pm);
        pm.close();
        pm = null;
    }
    
    /** */
    private void runTestNonPcClass(PersistenceManager pm) {
        Class c1 = pm.getObjectIdClass(Point.class);
        if ( c1 != null) {
            fail(ASSERTION_FAILED,
                 "pm.getObjectIdClass returns non-null ObjectId class for non pc class");
        }
    }

    /** */
    private void runTestNullParam(PersistenceManager pm) {
        Class c1 = pm.getObjectIdClass(null);
        if (c1 != null) {
            fail(ASSERTION_FAILED,
                 "pm.getObjectIdClass for null returned non-null class: " +
                 c1.getName());    
        }
    }
}
