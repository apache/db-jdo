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

package org.apache.jdo.tck.extents;


import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TimeZone;

import javax.jdo.Extent;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.pc.company.PartTimeEmployee;
import org.apache.jdo.tck.pc.company.Project;

/**
 *
 * @author  Craig Russell
 * @version 1.0
 */
abstract class ExtentTest extends JDO_Test {

    protected Class extentClass = Employee.class;
    protected Company company;
    protected Object companyOID;
    
    /** Creates new ExtentTest */
    public ExtentTest() {
        if (PMFProperties == null)
        {
            PMFProperties = System.getProperty("user.home") + "/.jdo/PMFProperties.properties";
            if (PMFProperties == null)
                System.out.println ("Please specify PMF properties in {user.home}/.jdo/PMFProperties.properties");
        }
    }
     
    /** */
    protected void checkPM() {
        try {
            Extent ex = getPM().getExtent(Company.class, false);
            int count = countIterator(ex.iterator());
            if (count == 1) {
                if (debug) logger.debug ("Found company");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.err.println ("Failed to find Company; initializing DB");
        }
        initDB();
    }
    
    /** */
    protected void initDB() {
        HashSet h;
        Address addr1 = new Address (7001L, "18 Network Circle",
                                     "Santa Clara", "CA", "94455", "USA");
        company = new Company (1L, "Sun Microsystems", new Date(), addr1);
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"));

        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        Address addr2 = new Address (7002L, "43 Sematery Drive",
                                     "Woodside", "CA", "94320", "USA");
        Employee scott = new FullTimeEmployee(3001L, "Scott", "McNealy", "G", 
                                              born.getTime(), addr2, 
                                              hired.getTime(), 200000.0);
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        Address addr3 = new Address (7003L, "1298 Wanderlust Road",
                                     "Pleasanton", "CA", "95560", "USA");
        Employee ed =  new PartTimeEmployee(3002L, "Ed", "Zander", null, 
                                            born.getTime(), addr3, 
                                            hired.getTime(), 400000.0); 
        scott.addToTeam(ed);

        // Set up their departments.
        Department board =
            new Department(2001L, "board", company);
        h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);
        
        company.addDepartment(board);

        Department emg = new Department(2002L, "emg", company);
        h = new HashSet();
        h.add(ed);
        emg.setEmployees(h);
        ed.setDepartment(emg);
        
        company.addDepartment(emg);

        // Insure these guys
        DentalInsurance scottDentalIns = new DentalInsurance(
            5001L, "Aetna", scott, new BigDecimal("12000"));
        MedicalInsurance scottMedicalIns = new MedicalInsurance(
            5002L, "Aetna", scott, "PPO");
        DentalInsurance edDentalIns = new DentalInsurance(
            5003L, "BlueCross", ed, new BigDecimal("10000"));
        MedicalInsurance edMedicalIns = new MedicalInsurance(
            5004L, "BlueCross", ed, "EPO");
        scott.setDentalInsurance(scottDentalIns);
        scott.setMedicalInsurance(scottMedicalIns);
        ed.setDentalInsurance(edDentalIns);
        ed.setMedicalInsurance(edMedicalIns);

        // Give them some projects to work on.  Scott works on both; Ed only
        // on one.
        Project solaris = new Project(4001L, "Solaris", new BigDecimal(100.375));
        Project sparc = new Project(4002L, "Sparc", new BigDecimal(200.500));
        h = new HashSet();
        h.add(scott);
        h.add(ed);
        solaris.setMembers(h); // Solaris is worked on by Scott and Ed

        h = new HashSet();
        h.add(scott);
        sparc.setMembers(h); // Sparc is worked on by Scott
        
        h = new HashSet();
        h.add(solaris);
        h.add(sparc);
        scott.setProjects(h); // Scott works on Solaris and Sparc

        h = new HashSet();
        h.add(solaris);
        ed.setProjects(h); // Ed works on Solaris
        
        /* Now put all of these into the database
         */
        pm.currentTransaction().begin();
        pm.makePersistent (company);
        pm.currentTransaction().commit();
        // System.out.println ("Company OID: " + pm.getObjectId(company));
    }
    
    /** */
    protected Employee addEmployee() {
        Address addr1 = new Address (7004L, "456 Chelsey Lane", 
                                     "Mountain View", "CA", "94040", "USA");
        Employee emp1 = new FullTimeEmployee (3003L, "First3003", "Last3003", "Middle3003", 
                                              new Date(), addr1, new Date(), 10000.0);
        getPM().makePersistent (emp1);
        return emp1;
    }
    
    /** */
    protected void deleteEmployee(Employee e) {
        getPM().deletePersistent (e);
    }

    /** */
    protected int countIterator(Iterator it) {
        int count = 0;
        for (;it.hasNext();count++, it.next());
        return count;
    }
    
    /** */
    protected int printIterator(Iterator it) {
        int count = 0;
        for (;it.hasNext();count++) {
            System.out.println (it.next().toString());
        }
        return count;
    }
    
    /** */
    protected Extent getExtent() {
        return getPM().getExtent(extentClass, true);
    }
    
    /** */
    protected Object getCompanyOID () {
        String companyOIDString = (String)
            PMFPropertiesObject.get("org.apache.jdo.tck.extents.CompanyOID");
        String companyOIDClassName = (String)
            PMFPropertiesObject.get("org.apache.jdo.tck.extents.CompanyOIDClass");
        if (companyOIDClassName == null) {
            companyOIDClassName = "org.apache.jdo.impl.fostore.OID";
        }
        try {
            Class companyOIDClass = Class.forName(companyOIDClassName);
            Constructor companyOIDConstructor = companyOIDClass.getConstructor(new Class[] {String.class});
            Object companyOID = companyOIDConstructor.newInstance (new Object[] {companyOIDString});
            return companyOID;
        } 
        catch (Exception ex) {
            throw new JDOFatalInternalException (
                "PMFProperties must be configured with the following properties\n" +
                "\torg.apache.jdo.tck.extents.CompanyOID = <string result of oid.toString()>\n" +
                "\torg.apache.jdo.tck.extents.CompanyOIDClass = <name of companyOID class>\n", ex);
        }
    }
    
    /** */
    protected PersistenceManager getPM() {
        if (pm == null) {
            pm = getPMF().getPersistenceManager();
            checkPM();
        }
        return pm;
    }
    
    /** */
    public PersistenceManagerFactory getPMF() {
        if (pmf == null) {
            pmf = super.getPMF();
        }
        return pmf;
    }
    
    /** */
    protected void beginTransaction() {
        getPM().currentTransaction().begin();
    }
    
    /** */
    protected void commitTransaction() {
        getPM().currentTransaction().commit();
    }
    
    /** */
    protected void rollbackTransaction() {
        getPM().currentTransaction().rollback();
    }
}
