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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCRefArrays;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test that we can store and fetch an array of object references.
*
* @author Dave Bristor
*/
public class Test_RefArrays extends Test_Fetch {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_RefArrays.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        readObjects();
        checkExtent(PCPoint.class, 2);
        checkExtent(PCRefArrays.class, 1);
    }

    /** We override this from Test_ActivateClass and insert our own objects */
    protected void insertObjects() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            PCPoint p1 = new PCPoint(1, 3);
            PCPoint p2 = new PCPoint(13, 42);
            PCRefArrays refArrays = new PCRefArrays();
            refArrays.init(p1, p2);
            
            if (debug) logger.debug("Before insert: " + refArrays);

            // Next 2 statements allow this test to work whether or not
            // reachability or navigation work.
            pm.makePersistent(p1);
            pm.makePersistent(p2);
            
            pm.makePersistent(refArrays);
            tx.commit();
            
            // Next 4 statements allow this test to work whether or not
            // reachability or navigation work.
            Object oid_p1 = JDOHelper.getObjectId(p1);
            Object oid_p2 = JDOHelper.getObjectId(p2);
            oids.add(oid_p1);
            oids.add(oid_p2);
            
            Object oid1 = JDOHelper.getObjectId(refArrays);
            if (debug) logger.debug("inserted refArrays: " + oid1);
            oids.add(oid1);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /**
     * redefine verify called by readObjects to check whether the read
     * instance is correct.
     */
    protected void verify(int i, Object pc) {
        if (debug) logger.debug("verify (i = " + i + ") " + pc); 
        PCPoint p1 = null;
        PCPoint p2 = null;
        // PCPoint and PCRefArrays do not redefine equals,
        // so use the string representation.
        switch(i) {
        case 0 : 
            p1 = new PCPoint(1, 3);
            assertEquals("Wrong instance returned from datastore",
                         p1.toString(), pc.toString());
            break;
        case 1:
            p2 = new PCPoint(13, 42);
            assertEquals("Wrong instance returned from datastore",
                         p2.toString(), pc.toString());
            break; 
        case 2:
            p1 = new PCPoint(1, 3);
            p2 = new PCPoint(13, 42);
            PCRefArrays refArrays = new PCRefArrays();
            refArrays.init(p1, p2);
            assertEquals("Wrong instance returned from datastore",
                         refArrays.toString(), pc.toString());
            break;
        default:
            fail("Wrong number of inserted objects, expected three");
            break;
        }
    }
}
