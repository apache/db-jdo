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
import javax.jdo.JDOUserException;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Test fix for bug 6214617: the pm is still open after pm.close fails
 * because of an active transaction. 
 */
public class Test_6214617 extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_6214617.class);
    }

    /** */
    public void test() {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            pm.currentTransaction().begin();
            pm.close(); // fails because tx is still active
        }
        catch (JDOUserException ex) {
            // expected exception: cannot close pm while tx is active
            if (debug) 
                logger.debug("Caught expected JDOUserException cannot close pm while tx is active");
        }
        finally {
            // should succeed
            pm.currentTransaction().rollback();
            pm.close();
            if (debug) 
                logger.debug("Could rollback tx and close pm");
        }
    }
}
