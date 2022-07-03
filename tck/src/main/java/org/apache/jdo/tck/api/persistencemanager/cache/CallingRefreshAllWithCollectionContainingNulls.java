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


package org.apache.jdo.tck.api.persistencemanager.cache;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Calling RefreshAll With Collection Containing Nulls
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion IDs:</B> A12.5-12
 *<BR>
 *<B>Assertion Description: </B>
Passing a non-null Object[] or Collection arguments to refreshAll that
contain null elements will have the documented behavior for non-null elements, and the null
elements will be ignored.

 */

public class CallingRefreshAllWithCollectionContainingNulls extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5-12 (CallingRefreshAllWithCollectionContainingNulls) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CallingRefreshAllWithCollectionContainingNulls.class);
    }
    
    private PCPoint p1;
    private PCPoint p2;
    private PCPoint p3;
    private PCPoint p4;
    private PCPoint p5;

    /** */
    public void testCallingRefreshAllWithCollectionContainingNulls() {
    	pm = getPM();  
    	createObjects(pm);
    	runTestRefreshAll1(pm);
    	runTestRefreshAll2(pm);
        pm.close();
        pm = null;
    }

    /**
     * @param pm the PersistenceManager
     */
    private void createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
        	tx.begin();
        	p1 = new PCPoint (1,3);
        	p2 = new PCPoint (2,3);
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

    /**  test refreshAll (Collection col1) */
    private void runTestRefreshAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
        	tx.begin();

        	PCPoint p5 = null;
        	Collection<PCPoint> col1 = new HashSet<>();
        	col1.add (p1);
        	col1.add (p5);
        	col1.add (p2);

        	pm.refreshAll(col1);
        	if (debug) logger.debug(" \nPASSED in testRefreshAll1()");
        	tx.rollback();
        }
        finally {
        	if (tx.isActive())
        		tx.rollback();
        }
    }


    /* test refreshAll (Object[] objArray) */
    private void runTestRefreshAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {           	
        	tx.begin();

        	PCPoint p5 = null;
        	Collection<PCPoint> col1 = new HashSet<>();
        	col1.add (p3);
        	col1.add (p4);
        	col1.add (p5);
          
        	pm.refreshAll(col1.toArray());
        	if (debug) logger.debug(" \nPASSED in testRefreshAll2()");
        	tx.rollback();
        } 
        finally {
        	if (tx.isActive())
        		tx.rollback();
        }
    }
}
