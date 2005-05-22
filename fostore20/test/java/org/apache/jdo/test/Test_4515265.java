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

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Test fix for bug 4515265: StoreManager.getClassForOid must throw
 * JDOUserException if given parameter is not an OID (spec section 12.5.6).
 */
public class Test_4515265 extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_4515265.class);
    }

    // Test different wrong cases of application identity:
    class PCPointKey {
        int x;
        Integer y;

        PCPointKey(int _x, int _y) {
            x = _x;
            y = new Integer(_y);
        }
    }
    
    class Oid {
        int x;
        Integer y;

        Oid(int _x, int _y) {
            x = _x;
            y = new Integer(_y);
        }
    }
        
    public void test() {
        Oid oid = new Oid(1, 1);
        PCPointKey key = new PCPointKey(1, 1);

        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            try {
                pm.getObjectById(key, true);
                fail("getObjectById should throw an exception or non-existing PC Class.");
            } catch (JDOUserException ex) {
                // expected exception => OK
                if (debug)
                    logger.debug("Caught expected JDOUserException for non-existing PC Class.");
            }

            try {
                pm.getObjectById(oid, true); 
                fail("getObjectById should throw an exception for non-PC Class."); 
            } catch (JDOUserException ex) {
                // expected exception => OK
                if (debug)
                    logger.debug("Caught expected JDOUserException for non-PC Class.");
            }

            try {
                pm.getObjectById(this, true);
                fail("getObjectById should throw an exception for wrong OID Class name.");
            } catch (JDOUserException ex) {
                // expected exception => OK
                if (debug)
                    logger.debug("Caught expected JDOUserException for wrong OID Class name.");
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }

    }
}
