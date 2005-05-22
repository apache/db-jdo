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

import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.pc.PCCycle;
import org.apache.jdo.pc.PCCycle2;

//
// This _test_ does not yet work.
//
// When printing the objects after reading them from the database, NPE's are
// generated because we don't yet have an enhancer...so the field references
// to the other class, made in each class's toString() method, fail after
// reading.  When we have an enhancer, these will be enhanced to be function
// calls to get the field of the other class, and toString() will be invoked
// in the result of that.
//

/**
* Test 2 PC's that refer to each other in a cycle/loop.
*
* @author Dave Bristor
*/
public class Test_Cycle extends Test_Fetch {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Cycle.class);
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception {
        insertObjects();
        initExpected();
        readObjects();
        checkExtent(PCCycle.class, 1);
        checkExtent(PCCycle2.class, 1);
    }

    // We override this from AbstractTest and insert our own objects 
    protected void insertObjects() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            PCCycle c = new PCCycle();
            PCCycle2 c2 = new PCCycle2();

            c.init("cyrcular", c2);
            c2.init(12358, c);
            
            if (debug) {
                logger.debug("Before insert: " + c);
                logger.debug("Before insert: " + c2);
            }
            
            pm.makePersistent(c);
            pm.makePersistent(c2);
            
            tx.commit();
        
            Object oid_c = JDOHelper.getObjectId(c);
            Object oid_c2 = JDOHelper.getObjectId(c2);
            
            if (debug) {
                logger.debug("inserted c: " + oid_c);
                logger.debug("inserted c2: " + oid_c2);
            }
            
            oids.add(oid_c);
            oids.add(oid_c2);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    private PCCycle expectedC = null;
    private PCCycle2 expectedC2 =null;

    private void initExpected() {
        expectedC = new PCCycle();
        expectedC2 = new PCCycle2();
        expectedC.init("cyrcular", expectedC2);
        expectedC2.init(12358,  expectedC);
    }

    /**
     * redefine verify called by readObjects to check whether the read
     * instance is correct.
     */
    protected void verify(int i, Object pc) {
        // PCCycle and PCCycle2 do not redefine equals, so use the string representation.
        switch(i) {
        case 0 : 
            assertEquals("Wrong instance returned from datastore", expectedC.toString(), pc.toString());
            break; 
        case 1:
            assertEquals("Wrong instance returned from datastore", expectedC2.toString(), pc.toString());
            break;
        default:
            fail("Wrong number of inserted objects, expected two");
            break;
        }
    }
}
