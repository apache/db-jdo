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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.empdept.PCPerson;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can do update of a hollow instance in Optimistic transaction.
*
* @author Marina Vatkina
*/
public class Test_HollowUpdate extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_HollowUpdate.class);
    }

    /** */
    private static Date date;

    /** */
    static {
        GregorianCalendar calendarDate =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendarDate.set(1969, 7, 20);
        date = calendarDate.getTime();
    }

    /** */
    public void testPCPoint() {
        insertPCPoint();
        checkExtent(PCPoint.class, 1);
    }
    
    /** */
    public void testPCPerson() {
        insertPCPerson();
        checkExtent(PCPerson.class, 1);
    }
    
    /** */
    private void insertPCPoint() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug("\nUPDATE HOLLOW");
            
            tx.setRetainValues(false);
            tx.setOptimistic(true);
            PCPoint p = new PCPoint(0xaa, 0xcc);
    
            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            pm.makePersistent(p);
            if (debug) logger.debug("Make Persistent OK, p is " + p);
            tx.commit();
            if (debug) logger.debug("Transaction commit OK");
            
            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            p.setY(new Integer(0xee));
            Object oid = pm.getObjectId(p);
            if (debug) logger.debug("Updated to " + p);
            tx.commit(); tx = null;
            if (debug) logger.debug("Transaction commit OK, p is " + p);
            pm.close(); pm =null;

            // check state of object in with pm
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            PCPoint p2 = (PCPoint)pm.getObjectById(oid, true);
            assertEquals("Field y has wrong value", new Integer(0xee), p2.getY());
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    public void insertPCPerson() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug("\nUPDATE HOLLOW");

            tx.setRetainValues(false);
            tx.setOptimistic(true);
            PCPerson p = new PCPerson("FFFFFF", "LLLL", date);
    
            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            pm.makePersistent(p);
            if (debug) logger.debug("Make Persistent OK, p is " + p);
            tx.commit();
            if (debug) logger.debug("Transaction commit OK");
            
            tx.begin();
            if (debug) logger.debug("Transaction begin OK");
            p.setLastname("xxxxxx");
            Object oid = pm.getObjectId(p);
            if (debug) logger.debug("Updated to " + p);
            tx.commit(); tx = null;
            if (debug) logger.debug("Transaction commit OK, p is " + p);
            pm.close(); pm = null;

            // check state of object in with pm
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            PCPerson p2 = (PCPerson)pm.getObjectById(oid, true);
            assertEquals("Field lastname has wrong value", "xxxxxx", p2.getLastname());
            tx.commit();
         }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
