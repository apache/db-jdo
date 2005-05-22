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


import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.appid.PCObjectKey;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test makePersistent with an instance with null key fields throws an exception.
*
* @author Craig Russell
*/
public class Test_KeyFieldNull extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_KeyFieldNull.class);
    }

    /** Create an instance with a null key field and verify that the proper
     * exception is thrown.
     */
    public void test1() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCObjectKey obj = new PCObjectKey();
            try {
                pm.makePersistent(obj);
                fail("Test failed. Expected JDOUserException on makePersistent with key field null");
            } catch (JDOUserException ex) {
                if (debug) logger.debug("caught expected " + ex);
            }
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** Create an instance with a null key field and verify that the proper
     * exception is thrown for makePersistentAll.
     */
    public void test2() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCObjectKey obj = new PCObjectKey();
            try {
                pm.makePersistentAll(new Object[] {obj});
                fail("Test failed. Expected JDOUserException on makePersistent with key field null");
            } catch (JDOUserException ex) {
                if (debug) logger.debug("caught expected " + ex);
            }
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** GetObjectById with an object id with a key field null and verify that the
     * proper exception is thrown.
     */
    public void test3() {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            PCObjectKey pcobj = new PCObjectKey();
            PCObjectKey.Oid key = new PCObjectKey.Oid();
            try {
                Object obj = pm.getObjectById(key, false);
                fail("Test failed. Expected JDOUserException on getObjectById with key field null");
            } catch (JDOUserException ex) {
                if (debug) logger.debug("caught expected " + ex);
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }    

}
