/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.util.ArrayList;
import java.util.Collection;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can do correct processing of collections and arrays
* in PersistenceManager.xxxAll() calls.
*
* @author Marina Vatkina
*/
public class Test_NegAll extends AbstractTest {

    Collection c = new ArrayList();
    PersistenceManager pm = null;
    Transaction tx = null;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_NegAll.class);
    }
    
    /** */
    public void test() {
        try {
            // Add PC object that will fail makeTr() and makeNonTx()
            // It will pass makeP() and deleteP()
            c.add( new PCPoint(11, 111) );
            
            // Add NON-PC object that will fail all calls
            c.add( new Point(12, 122) );
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            
            insertAll();
            
            // Add Transient PC object that will fail makeNonTx() and deleteP().
            // It will pass makeTx() and MakeTr()
            c.add( new PCPoint(13, 133) );
            
            makeTrAll();
            makeNonTxAll();
            deleteAll();
            makeTxAll();
            evictAll();
            
            tx.rollback(); 

        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // Tries to insert object with dups Ids
    private void insertAll() {
        if (debug) logger.debug("\nINSERT ALL");
        try {
            pm.makePersistentAll(c);
            fail("ERROR ON INSERT ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("INSERT ALL COLLECTION Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.makePersistentAll(c.toArray());
            fail("ERROR ON INSERT ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("INSERT ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
    }
    
    // Tries to make Transient P_NEW Objects
    private void makeTrAll() {
        if (debug) logger.debug("\nMAKE TR ALL");

        try {
            pm.makeTransientAll(c);
            fail("ERROR ON MAKE TR ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE TR ALL Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.makeTransientAll(c.toArray());
            fail("ERROR ON MAKE TR ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE TR ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
    }
    
    // Tries to make NonTransactional P_NEW Objects
    private void makeNonTxAll() {
        if (debug) logger.debug("\nMAKE NON-TX ALL");
        try {
            pm.makeNontransactionalAll(c);
            fail("ERROR ON MAKE NON-TX ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE NON-TX ALL Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.makeNontransactionalAll(c.toArray());
            fail("ERROR ON MAKE NON-TX ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE NON-TX ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
    }
    
    // Tries to delete Transient Objects
    private void deleteAll() {
        if (debug) logger.debug("\nDELETE ALL");
        try {
            pm.deletePersistentAll(c);
            fail("ERROR ON DELETE ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("DELETE ALL COLLECTION Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.deletePersistentAll(c.toArray());
            fail("ERROR ON DELETE ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("DELETE ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
    }
    
    // Tries to make Transactional nonExistent objects
    private void makeTxAll() {
        if (debug) logger.debug("\nMAKE TX ALL");
        try {
            pm.makeTransactionalAll(c);
            fail("ERROR ON MAKE TX ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE TX ALL COLLECTION Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.makeTransactionalAll(c.toArray());
            fail("ERROR ON MAKE TX ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("MAKE TX ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
    }
    
    // Tries to evict all instances.
    private void evictAll() {
        if (debug) logger.debug("\nEVICT ALL");
        try {
            pm.evictAll(c);
            fail("ERROR ON EVICT ALL COLLECTION");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("EVICT ALL COLLECTION Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        try {
            pm.evictAll(c.toArray());
            fail("ERROR ON EVICT ALL ARRAY");
        } catch (JDOUserException e) {
            if (debug)
                logger.debug("EVICT ALL ARRAY Caught expected: " +
                             e.getNestedExceptions().length + " exceptions");
        }
        pm.evictAll();
        if (debug) logger.debug("EVICT ALL.");
    }
}
