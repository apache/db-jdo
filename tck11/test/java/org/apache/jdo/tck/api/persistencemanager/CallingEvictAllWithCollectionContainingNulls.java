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

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Calling EvictAll With Collection Containing Nulls
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion IDs:</B> A12.5-11
 *<BR>
 *<B>Assertion Description: </B>
Passing a non-null Object[] or Collection arguments to evictAll that
contain null elements will have the documented behavior for non-null elements, and the null elements
will be ignored.

 */

public class CallingEvictAllWithCollectionContainingNulls extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5-11 (CallingEvictAllWithCollectionContainingNulls) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CallingEvictAllWithCollectionContainingNulls.class);
    }
    
    private PCPoint p1;
    private PCPoint p2;
    private PCPoint p3;
    private PCPoint p4;
    private PCPoint p5;

    /** */
    public void testCallingEvictAllWithCollectionContainingNulls() {
        pm = getPM();
        createObjects(pm);
        runTestEvictAll1(pm);
        runTestEvictAll2(pm);
        pm.close();
        pm = null;
    }
    
    /** */
    private void createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            p1 = new PCPoint (1,3);
            p2 = new PCPoint (4,3);
            p3 = new PCPoint (4,2);
            p4 = new PCPoint (3,3);
            
            pm.makePersistent(p1);
            pm.makePersistent(p2);
            pm.makePersistent(p3);
            pm.makePersistent(p4);
         
            tx.commit();
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }
    
    /* test evictAll (Collection col1) */
    private void runTestEvictAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            
            PCPoint p5 = null;
            Collection col1 = new java.util.HashSet();
            col1.add (p1);
            col1.add (p5);
            col1.add (p2);
            
            pm.evictAll(col1);
            if (debug) logger.debug(" \nPASSED in testEvictAll1()");
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }
    
    /** test evictAll (Object[] objArray) */
    private void runTestEvictAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            
            PCPoint p5 = null;
            Collection col1 = new java.util.HashSet();
            col1.add (p3);
            col1.add (p4);
            col1.add (p5);
            
            pm.evictAll(col1.toArray());
            if (debug) logger.debug(" \nPASSED in testEvictAll2()");
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }
}
