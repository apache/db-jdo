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

package org.apache.jdo.test;

import javax.jdo.PersistenceManager;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Test fix for bug 4510817: Ensure that PersistenceManager.getObjectIdClass
 * returns null when given class does not implement PersistenceCapable or is
 * null.
 */
public class Test_4510817 extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_4510817.class);
    }

    /** */
    public void test() {
        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            // First case: Given class does not implement PersistenceCapable.
            Class c1 = pm.getObjectIdClass(Thread.class);
            assertNull("ObjectIdClass for Thread.class is not null: " + c1, c1);

            // Second case: Given parameter is null.
            Class c2 = pm.getObjectIdClass(null);
            assertNull("ObjectIdClass for null is not null: " + c2, c2);

            if (debug)
                logger.debug("Success: ObjectIdClass for Thread.class and null returns null");
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
