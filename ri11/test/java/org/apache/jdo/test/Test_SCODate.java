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

import org.apache.jdo.pc.PCSCO;
import org.apache.jdo.sco.SCO;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.test.util.Util;

/**
* Tests that we correctly process updates of SCO Date.
*
* @author Marina Vatkina
*/
public class Test_SCODate extends Test_SCO_Base {

    static GregorianCalendar date;
    static {
        date = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        date.set(1969, 7, 20, 20, 0 , 0);
        date.set(GregorianCalendar.MILLISECOND, 0);
    }

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCODate.class);
    }
    
    /** */
    public void test() {
        insertObjects();
        runDateTest(false, false);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runDateTest(false, true);
    }

    /** */
    public void testOptimistic() {
        insertObjects();
        runDateTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
         insertObjects();
         runDateTest(true, true);
    }

    protected void runDateTest(boolean optimistic, boolean retainValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("Running " + (optimistic?"optimistic":"datastore") + 
                             " transactions with retainValues = " + retainValues);
            tx.setOptimistic(optimistic);
            tx.setRetainValues(retainValues);

            tx.begin();
            PCSCO pcSCO = (PCSCO) pm.getObjectById(oid_date, true);
            Date sco = pcSCO.getSCODate();
            long orig = sco.getTime();
            tx.commit();
            if (debug)
                logger.debug("Date: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));

            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setTime(date.getTime().getTime());
            tx.commit();
            if (debug)
                logger.debug("Date after setTime to 1969/Aug/20: " +
                             Util.longFormatter.format(pcSCO.getSCODate()));
            assertEquals("Date after setTime to 1969/Aug/20: ",
                         date.getTime().getTime(), pcSCO.getSCODate().getTime());
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setYear(99);
            tx.commit();
            if (debug)
                logger.debug("Date after setYear to 1999: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setYear to 1999: ", 99,
                          pcSCO.getSCODate().getYear());
  
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setMonth(10);
            tx.commit();
            if (debug)
                logger.debug("Date after setMonth to Nov: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setMonth to Nov: ", 10,
                         pcSCO.getSCODate().getMonth());
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setDate(15);
            tx.commit();
            if (debug)
                logger.debug("Date after setDate to 15: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setDate to 15: ", 15,
                         pcSCO.getSCODate().getDate());
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setHours(5);
            tx.commit();
            if (debug)
                logger.debug("Date after setHours to 5: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setHours to 5: ", 5,
                         pcSCO.getSCODate().getHours());
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setMinutes(25);
            tx.commit();
            if (debug)
                logger.debug("Date after setMinutes to 25: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setMinutes to 25: ", 25,
                         pcSCO.getSCODate().getMinutes());
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setSeconds(55);
            tx.commit();
            if (debug)
                logger.debug("Date after setSeconds to 55: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after setSeconds to 55: ", 55,
                         pcSCO.getSCODate().getSeconds());
            long time = pcSCO.getSCODate().getTime();

            tx.begin();
            sco = pcSCO.getSCODate();
            Date d = (Date) sco.clone();
            d.setTime(orig);
            assertNotIsDirty("Is dirty: ", pcSCO);
            tx.commit();
            if (debug)
                logger.debug("Date after clone: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after clone: ", time,
                         pcSCO.getSCODate().getTime());
            assertNullOwner("Owner of clone: ", (SCO)d);
            
            tx.begin();
            sco = pcSCO.getSCODate();
            sco.setTime(orig);
            tx.commit();
            if (debug)
                logger.debug("Date after restore: " +
                             Util.longFormatter.format(pcSCO.getSCODate())); 
            assertEquals("Date after restore: ", orig,
                         pcSCO.getSCODate().getTime());        
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    protected void insertObjects() {
        insertDate();
    }

}
