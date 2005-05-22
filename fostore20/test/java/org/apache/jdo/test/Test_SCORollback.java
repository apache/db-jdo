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
import java.util.HashSet;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCSCO;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.test.util.Util;

/**
* This test verifies the expected behavior of rollback operation
* on an instance with references to SCO objects.
*
* @author Marina Vatkina
*/
public class Test_SCORollback extends Test_SCO_Base {

    static GregorianCalendar date;
    static {
        date = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        date.set(1979, 7, 21, 20, 0 , 0);
        date.set(GregorianCalendar.MILLISECOND, 0);
    }

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCORollback.class);
    }

    /** */
    public void test()
    {
        insertDate();
        insertAllTypes();
        updateObjects(false, false);
    }
    
    /** */
    public void testRestoreValues()
    {
        insertDate();
        insertAllTypes();
        updateObjects(false, true);
    }
    
    /** */
    public void testOptimistic()
    {
        insertDate();
        insertAllTypes();
        updateObjects(true, false);
    }

    /** */
    public void testOptimisticRestoreValues()
    {
        insertDate();
        insertAllTypes();
        updateObjects(true, true);
    }
    
   /** */
    public void testInsertAllRollback()
    {
        insertAllRollback(false, false);
    }
    
    /** */
    public void testInsertAllRollbackRestoreValues()
    {
        insertAllRollback(false, true);
    }
    
   /** */
    public void testInsertAllRollbackOptimistic()
    {
        insertAllRollback(true, false);
    }
    
    /** */
    public void testInsertAllRollbackOptimisticRestoreValues()
    {
        insertAllRollback(true, true);
    }
    
    protected void updateObjects(boolean optimistic, boolean restoreValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("\nUPDATE-ROLLBACK " + (optimistic?"optimistic":"datastore") + 
                             " transactions with RestoreValues is set to " + 
                             (restoreValues ? "TRUE" : "FALSE"));
            tx.setOptimistic(optimistic);
            tx.setRestoreValues(restoreValues);

            PCPoint p = new PCPoint(2, 2);
            
            tx.begin();
            pcSCO = (PCSCO) pm.getObjectById(oid_date, true);
            Date sco = pcSCO.getSCODate();
            pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            HashSet hs = pcCollections.getSCOHashSet();
            
            if (debug)
                logger.debug("Date before update: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date before update: ", 
                         Util.moonWalkDate.getTime().getTime(),
                         pcSCO.getSCODate().getTime());
            assertCollectionSize("HashSet before update: ",
                                 pcCollections.getSCOHashSet(), 8);
            
            sco.setTime(date.getTime().getTime());
            hs.add(p);
            if (debug)
                logger.debug("Date after setTime to 1979/Aug/20: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date after setTime to 1979/Aug/20: ", 
                         date.getTime().getTime(), pcSCO.getSCODate().getTime());
            assertCollectionSize("HashSet after add 1 element: ",
                                 pcCollections.getSCOHashSet(), 9);
            
            tx.rollback(); 
            if (debug)
                logger.debug("Date after rollback: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date after rollback: ", 
                         Util.moonWalkDate.getTime().getTime(),
                         pcSCO.getSCODate().getTime());
            assertCollectionSize("HashSet after rollback: ",
                                 pcCollections.getSCOHashSet(), 8);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    protected void insertAllRollback(boolean optimistic, boolean restoreValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("\nINSERT-ROLLBACK " + (optimistic?"optimistic":"datastore") + 
                             " transactions with RestoreValues is set to " + 
                             ((restoreValues)? "TRUE" : "FALSE"));
            tx.setOptimistic(optimistic);
            tx.setRestoreValues(restoreValues);
            tx.begin();

            insertDateWithoutCommit(pm);
            insertAllTypesWithoutCommit(pm);

            if (debug)
                logger.debug("Date after makePersistent: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date after makePersistent: ",
                         Util.moonWalkDate.getTime().getTime(),
                         pcSCO.getSCODate().getTime());
            assertCollectionSize("HashSet after makePersistent: ",
                                 pcCollections.getSCOHashSet(), 8);

            PCPoint p = new PCPoint(2, 2);
            
            Date sco = pcSCO.getSCODate();
            HashSet hs = pcCollections.getSCOHashSet();
            sco.setTime(date.getTime().getTime());
            hs.add(p);
            
            if (debug)
                logger.debug("Date after setTime to 1979/Aug/21: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date after setTime to 1979/Aug/21: ",
                         date.getTime().getTime(), pcSCO.getSCODate().getTime());
            assertCollectionSize("HashSet after replace: ",
                                 pcCollections.getSCOHashSet(), 9);

            tx.rollback();
            sco = pcSCO.getSCODate();
            hs = pcCollections.getSCOHashSet();
            if (debug)
                logger.debug("Date after rollback: " +
                             ((sco == null)? "NULL" : Util.longFormatter.format(sco)));
            assertEquals("Date after rollback: ", 
                         (restoreValues ? Util.moonWalkDate.getTime().getTime() :
                          date.getTime().getTime()), 
                         sco.getTime());
            assertCollectionSize("HashSet after rollback: ", hs,
                                 (restoreValues ? 8 : 9));
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

}
