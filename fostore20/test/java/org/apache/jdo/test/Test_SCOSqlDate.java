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

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCSCO;
import org.apache.jdo.sco.SCO;
import org.apache.jdo.test.util.JDORITestRunner;
import org.apache.jdo.test.util.Util;

/**
* Tests that we correctly process updates of SCO java.sql.Date.
*
* @author Marina Vatkina
*/
public class Test_SCOSqlDate extends Test_SCO_Base {

    static GregorianCalendar date;
    static {
        date = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        date.set(1969, 7, 20, 20, 0 , 0);
        date.set(GregorianCalendar.MILLISECOND, 0);
    }

    Object oid = null;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOSqlDate.class);
    }

    /** */
    public void test() {
        insertObjects();
        runSqlDateTest(false, false);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runSqlDateTest(false, true);
    }

    /** */
    public void testOptimistic() {
        insertObjects();
        runSqlDateTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
         insertObjects();
         runSqlDateTest(true, true);
    }

    /** */
    protected void runSqlDateTest(boolean optimistic, boolean retainValues) {
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
            Date sco = pcSCO.getSCOSqlDate();
            long orig = sco.getTime();
            tx.commit();
            if (debug)
                logger.debug("SqlDate: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate()));
            
            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            sco.setTime(date.getTime().getTime());
            tx.commit();
            if (debug)
                logger.debug("SqlDate after setTime to 1969/Aug/20: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate()));
            assertEquals("SqlDate after setTime to 1969/Aug/20: ",
                         date.getTime().getTime(),
                         pcSCO.getSCOSqlDate().getTime());
 
            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            sco.setYear(99);
            tx.commit();
            if (debug)
                logger.debug("SqlDate after setYear to 1999: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate())); 
            assertEquals("SqlDate after setYear to 1999: ", 99,
                         pcSCO.getSCOSqlDate().getYear());
  
            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            sco.setMonth(10);
            tx.commit();
            if (debug)
                logger.debug("SqlDate after setMonth to Nov: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate())); 
            assertEquals("SqlDate after setMonth to Nov: ", 10,
                         pcSCO.getSCOSqlDate().getMonth());

            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            sco.setDate(15);
            tx.commit();
            if (debug)
                logger.debug("SqlDate after setDate to 15: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate())); 
            assertEquals("SqlDate after setDate to 15: ", 15,
                         pcSCO.getSCOSqlDate().getDate());
            long time = pcSCO.getSCOSqlDate().getTime();
            
            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            Date d = (Date) sco.clone();
            d.setTime(orig);
            assertNotIsDirty("Is dirty: ", pcSCO);
            tx.commit();
            assertEquals("SqlDate after clone: ", time,
                         pcSCO.getSCOSqlDate().getTime());
            assertNullOwner("Owner of clone: ", (SCO)d);
  
            tx.begin();
            sco = pcSCO.getSCOSqlDate();
            sco.setTime(orig);
            tx.commit();
            if (debug)
                logger.debug("SqlDate after restore: " +
                             Util.longFormatter.format(pcSCO.getSCOSqlDate())); 
            assertEquals("Date after restore: ", orig,
                         pcSCO.getSCOSqlDate().getTime());
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    protected void insertObjects() {
        insertDate();
    }
}
