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


package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> PassingNullToEvictAllThrowsException
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion IDs:</B> A12.5-9
 *<BR>
 *<B>Assertion Description: </B>
Passing a null valued argument to evictAll will throw a NullPointerException.

 */

public class PassingNullToEvictAllThrowsException extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5-9 (PassingNullToEvictAllThrowsException) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(PassingNullToEvictAllThrowsException.class);
    }

    /** */
    public void testPassingNullToEvictAllThrowsException() {
        pm = getPM();

        runTestEvictAll1(pm);
        runTestEvictAll2(pm);
       
        pm.close();
        pm = null;
    }

    /* test evictAll (Collection pcs) */
    private void runTestEvictAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug ("**  in runTestEvictAll1() ");
        try {
            tx.begin();
            Collection col1 = null;
            try {
                pm.evictAll(col1);
                fail(ASSERTION_FAILED,
                     "pm.evictAll should throw NullPointerException when called with argument null");
            }
            catch (NullPointerException ex) {
                // expected exception
            }
            tx .rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test evictAll (Object[] objArray) */
    private void runTestEvictAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug ("**  in runTestEvictAll1() ");
        try {
            tx.begin();
            Object[] arr = null;
            try {
                pm.evictAll(arr); 
                fail(ASSERTION_FAILED,
                     "pm.evictAll should throw NullPointerException when called with argument null");
            }
            catch (NullPointerException ex) {
                // expected exception
            }
            tx .rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
