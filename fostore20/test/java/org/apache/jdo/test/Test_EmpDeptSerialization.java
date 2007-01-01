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

/*
 * Test_EmpDeptSerialization.java
 *
 * Created on June 29, 2001, 3:37 PM
 */

package org.apache.jdo.test;

import java.util.*;
import java.io.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.pc.empdept.PCDepartment;
import org.apache.jdo.pc.empdept.PCEmployee;
import org.apache.jdo.pc.empdept.PCFullTimeEmployee;
import org.apache.jdo.pc.empdept.PCInsurance;
import org.apache.jdo.pc.empdept.PCPartTimeEmployee;
import org.apache.jdo.pc.empdept.PCProject;

/** This class measures the difference between Serialization and JDO as a
 * persistence strategy.  
 * Some number of instances are created, and then the test starts timing.
 * First the objects are serialized; then a transaction is started, the 
 * objects are made persistent in JDO, and the transaction is committed.
 *
 * @author  Craig Russell
 * @version 1.0
 */
public class Test_EmpDeptSerialization extends AbstractTest {
    
    ArrayList allObjects = new ArrayList();
    PCEmployee scott;
    PCEmployee ed;
    PCInsurance scottIns;
    PCInsurance edIns;
    PCDepartment board;
    PCDepartment emg;
    PCProject solaris;
    PCProject sparc;
    
    /**
     * @param args the command line arguments
     */
    public static void main (String args[]) {
        switch (args.length) {
        case 0:
            JDORITestRunner.run(Test_EmpDeptSerialization.class);
            break;
        case 2:
            runLocal(args[0], args[1]);
            break;
        default: 
            System.err.println ("usage: \nTest_EmpDeptSerialization <url> <file>\n\t<url>: the url for fostore\n\t<file>: the file name for serialization");
            break;
        }
    }
    
    /** */
    public void test() throws Exception {
        doTest("serialize.tmp");
    }

    /** Run the test.  */
    void doTest(String fileName) throws Exception {
        createObjects();
        long serializeTime = serializeObjects(fileName);
        long fostoreTime = persistObjects();
    }
    
    /** */
    void createObjects() {
        HashSet h;
        
        // Create and set up employees.  Scott is Ed's manager.  Ed is
        // Scott's sole employee.
        //
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        scott =
            new PCFullTimeEmployee(
                "Scott", "McNealy", born.getTime(), 
                1L, hired.getTime(), 
                //null, null, null, null, null,
                200000.0);
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        ed = 
            new PCPartTimeEmployee(
                "Ed", "Zander", born.getTime(), 
                100L, hired.getTime(), 
                //null, null, null, null, null,
                400.0); 
        allObjects.add(ed);
        allObjects.add(scott);
        ed.setManager(scott);

        h = new HashSet();
        h.add(ed);
        scott.setEmployees(h);

        // Set up their departments.
        board =
            new PCDepartment(100L, "board");
        h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);

        emg =
            new PCDepartment(200L, "emg");
        h = new HashSet();
        h.add(ed);
        emg.setEmployees(h);
        ed.setDepartment(emg);

        // Insure these guys
        scottIns = new PCInsurance(1000, "Aetna", scott);
        edIns = new PCInsurance(1001, "BlueCross", ed);
        scott.setInsurance(scottIns);
        ed.setInsurance(edIns);

        // Give them some projects to work on.  Scott works on both; Ed only
        // on one.
        solaris = new PCProject(1L, "Solaris");
        sparc = new PCProject(2L, "Sparc");
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
    }

    /** Insert objects into the database.  If reachability were implemented, we'd
     * only have to make scott and ed persistent, as everything else is
     * reachable from them.
     */
    long persistObjects() {
        Timer timer = new Timer();
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        pm.makePersistentAll(allObjects);
        tx.commit();
        pm.close();
        long elapsed = timer.stop();
        if (debug) logger.debug ("FOStore results:        \t" + elapsed);
        return elapsed;
    }
    
    /** Write the objects to a file using serialization.
     */
    long serializeObjects(String fileName) throws IOException {
        Timer timer = new Timer();
        ObjectOutputStream oos = getObjectOutputStream(fileName);
        oos.writeObject(allObjects);
        oos.flush();
        oos.close();
        long elapsed = timer.stop();
        if (debug) logger.debug ("Serialization results:\t" + elapsed);
        return elapsed;
    }

    /** Timer utility class.  Measures wall clock time.
     */
    static class Timer {
        long startTime;
        long stopTime;
        Timer() {
            startTime = new Date().getTime();
        }
        
        long stop() {
            stopTime = new Date().getTime();
            return (stopTime - startTime);
        }
        
        void start() {
            startTime = new Date().getTime();
        }
    }
            
    /** */
    static void runLocal(String url, String file) {
        try {
            // create PMF
            FOStorePMF pmf = new FOStorePMF();
            pmf.setConnectionURL(url);
            pmf.setConnectionUserName("craig");
            pmf.setConnectionPassword("secret");
            pmf.setConnectionCreate(true);

            // create and setup test
            Test_EmpDeptSerialization sm = new Test_EmpDeptSerialization();
            sm.logger.debug("Test_EmpDeptSerialization using URL: " + url +
                            " FileName: " + file);
            sm.pmf = pmf;
            sm.debug = true;
            sm.doTest(file);
            sm.closePMF();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
