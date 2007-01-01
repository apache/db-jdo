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

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;

import org.apache.jdo.pc.empdept.PCDepartment;
import org.apache.jdo.pc.empdept.PCEmployee;
import org.apache.jdo.pc.empdept.PCFullTimeEmployee;
import org.apache.jdo.pc.empdept.PCInsurance;
import org.apache.jdo.pc.empdept.PCPartTimeEmployee;
import org.apache.jdo.pc.empdept.PCProject;

/**
* This class is the superclass for test classes Test_Inheritance and
* Test_StringOID and provides common code.
*
* @author Michael Bouschen
*/
class EmpDeptSupport extends AbstractTest {
    
   protected HashSet persistentInstances = new HashSet (9);

    // We override this from AbstractTest and insert our own objects.
    // Use Test_Extent to read them back.
    protected void insertObjects() {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            createObjects(pm);
            checkObjects(pm);
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    protected void createObjects(PersistenceManager pm) {
        // Create and set up employees.  Scott is Ed's manager.  Ed is
        // Scott's sole employee.
        //
        HashSet h;
        
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        PCEmployee scott =
            new PCFullTimeEmployee(
                "Scott", "McNealy", born.getTime(), 
                1L, hired.getTime(),
                200000.0);
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        PCEmployee ed = 
            new PCPartTimeEmployee(
                "Ed", "Zander", born.getTime(), 
                100L, hired.getTime(),
                400000.0); 
                           
        ed.setManager(scott);

        h = new HashSet();
        h.add(ed);
        scott.setEmployees(h);

        // Set up their departments.
        PCDepartment board =
            new PCDepartment(100L, "board");
        h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);

        PCDepartment emg =
            new PCDepartment(200L, "emg");
        h = new HashSet();
        h.add(ed);
        emg.setEmployees(h);
        ed.setDepartment(emg);

        // Insure these guys
        PCInsurance scottIns = new PCInsurance(1000, "Aetna", scott);
        PCInsurance edIns = new PCInsurance(1001, "BlueCross", ed);
        scott.setInsurance(scottIns);
        ed.setInsurance(edIns);

        // Give them some projects to work on.  Scott works on both; Ed only
        // on one.
        PCProject solaris = new PCProject(1L, "Solaris");
        PCProject sparc = new PCProject(2L, "Sparc");
        h = new HashSet();
        h.add(scott);
        h.add(ed);
        solaris.setEmployees(h); // Solaris is worked on by Scott and Ed

        h = new HashSet();
        h.add(scott);
        sparc.setEmployees(h); // Sparc is worked on by Scott
        
        h = new HashSet();
        h.add(solaris);
        h.add(sparc);
        scott.setProjects(h); // Scott works on Solaris and Sparc

        h = new HashSet();
        h.add(solaris);
        ed.setProjects(h); // Ed works on Solaris

        // Show what we've got
        if (debug) {
            logger.debug("Before insert: ");
            logger.debug(scott.toString());
            logger.debug(ed.toString());
            logger.debug(board.toString());
            logger.debug(emg.toString());
            logger.debug(scottIns.toString());
            logger.debug(edIns.toString());
            logger.debug(solaris.toString());
            logger.debug(sparc.toString());
        }

        // Make it all persistent.  If reachability were implemented, we'd
        // only have to make scott and ed persistent, as everything else is
        // reachable from them.
        pm.makePersistent(scott);
        persistentInstances.add(scott);
        pm.makePersistent(ed);
        persistentInstances.add(ed);
        pm.makePersistent(board);
        persistentInstances.add(board);
        pm.makePersistent(emg);
        persistentInstances.add(emg);
        pm.makePersistent(scottIns);
        persistentInstances.add(scottIns);
        pm.makePersistent(edIns);
        persistentInstances.add(edIns);
        pm.makePersistent(solaris);
        persistentInstances.add(solaris);
        pm.makePersistent(sparc);
        persistentInstances.add(sparc);
    }
        
    /** This method can be overridden by a subclass to perform special checks.
     */
    protected void checkObjects(PersistenceManager pm) {
    }

}
