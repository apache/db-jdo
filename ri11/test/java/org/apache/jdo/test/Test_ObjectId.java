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
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that getObjectId and getTransactionalObjectId return oids that
* compares equal if no PK change happen and getObjectById returns the same
* instance for all oids.
*
* @author Marina Vatkina
*/
public class Test_ObjectId extends AbstractTest {

    PCPoint p = null;
    PersistenceManager pm = null;
    Transaction tx = null;
    
    Object oid1 = null;
    Object toid1 = null;
    Class cls1 = null;
    
    /** TBD */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_ObjectId.class);
    }
    
    /** */  
    public void test() throws Exception {
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            
            insertObject();
            checkObjectId();
            checkNegObjectId(); // bug fix for 4518855.
            
            pm.close();
        } catch (Exception ex) {
            logger.debug("caught " + ex);
            ex.printStackTrace(System.err);
        }
    }

    // Insert object to test
    protected void insertObject() {
        if (debug) logger.debug("\nINSERT");
        p = new PCPoint(11, 111);
        tx.begin();
        pm.makePersistent(p);
        tx.commit();
        if (debug) logger.debug("Inserted: " + p);
    }

    // Fetches objectIds and corresponding PC instances, and compares them.
    protected void checkObjectId() {
        if (debug) logger.debug("\nGETOBJECTID");
        oid1 = pm.getObjectId(p);
        toid1 = pm.getTransactionalObjectId(p);
        cls1 = pm.getObjectIdClass(p.getClass());
        String ret = testOids();
        if (debug) logger.debug("Outside a transaction: " + ret);
        tx.begin();

        ret = testOids();
        if (debug) logger.debug("Inside a transaction: " + ret);

        p.setY(new Integer(9876));

        ret = testOids();
        if (debug) logger.debug("After non-PK update: " + ret);

        tx.commit();
        
        ret = testOids();
        if (debug) logger.debug("After commit: " + ret);
    }

    // Test non-PC Oids are null
    protected void checkNegObjectId() {
        if (debug) logger.debug("Test null Object: " + pm.getObjectId(null));
        if (debug) logger.debug("Test non-PC Object: " + pm.getObjectId(this));
        p = new PCPoint(11, 111);
        if (debug)
            logger.debug("Test non-persistent Object: " + pm.getObjectId(p));
        pm.makeTransactional(p);
        if (debug)
            logger.debug("Test non-persistent Object: " + pm.getObjectId(p));
    }

    // Test oids and instances. 
    private String testOids()
    {
        Object oid2 = pm.getObjectId(p);
        Object toid2 = pm.getTransactionalObjectId(p);
        Class cls2 = pm.getObjectIdClass(p.getClass());
        
        assertTrue("New getObjectIdClass not equals getObjectIdClass!", cls1.equals(cls2));
        assertTrue("getObjectId not equals getTransactionalObjectId!", oid2.equals(toid2));
        assertTrue("New getObjectId not equals original getObjectId!", oid1.equals(oid2));
        Object o = pm.getObjectById(oid1, false);
        assertSame("Object got from original oid is not correct!", p, o);
        o = pm.getObjectById(oid2, false);
        assertSame("Object got from new oid is not correct!", p, o);
        o = pm.getObjectById(toid1, false);
        assertSame("Object got from original tx-oid is not correct!", p, o);
        o = pm.getObjectById(toid2, false);
        assertSame("Object got from new tx-oid is not correct!", p, o);
        return "OK";
    }
}
