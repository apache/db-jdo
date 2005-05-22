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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.xempdept.Company;
import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.FullTimeEmployee;
import org.apache.jdo.pc.xempdept.Insurance;
import org.apache.jdo.pc.xempdept.PartTimeEmployee;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;
import org.apache.jdo.pc.xempdept.Project;
import org.apache.jdo.test.query.AdvancedTest;
import org.apache.jdo.test.query.ArithmeticTest;
import org.apache.jdo.test.query.BasicTest;
import org.apache.jdo.test.query.CollectionTest;
import org.apache.jdo.test.query.InheritanceTest;
import org.apache.jdo.test.query.NavigationTest;
import org.apache.jdo.test.query.OrderingTest;
import org.apache.jdo.test.query.ParameterTest;
import org.apache.jdo.test.query.QueryApiTest;
import org.apache.jdo.test.query.QueryErrorTest;
import org.apache.jdo.test.query.QueryTest;
import org.apache.jdo.test.query.ScopingTest;
import org.apache.jdo.test.query.SemanticErrorTest;
import org.apache.jdo.test.query.SyntaxErrorTest;
import org.apache.jdo.test.query.TreeTest;
import org.apache.jdo.test.query.UnsupportedTest;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests JDOQL queries. The following test scenarios are supported:
 * <ul>
 * <li> Extent queries with new fostore. The test first initializes a new 
 * fostore with the expected test instances from the xempdept schema. The 
 * test queries use the extent of a xempdept pc class as the query candidates.
 * <li> Extent queries with existing fostore. The test ommits creating a new 
 * fostore, if the property <code>existing</code> is set to <code>true</code>.
 * The test queries use the extent of a xempdept pc class as the query 
 * candidates.
 * <li> Memory queries using transient instances of the xempdept classes. 
 * The test uses a memory collection of transient xempdept instances, 
 * if the property <code>memory</code> is set to <code>true</code>.
 * <li> Memory query using persistent instances (not yet implemented).
 * </ul> 
 * 
 * @author Michael Bouschen
 */
public class Test_Query extends AbstractTest
{
    PrintStream log;

    Collection companies;
    Collection departments;
    Collection employees;
    Collection fullTimeEmployees;
    Collection partTimeEmployees;
    Collection insurances;
    Collection projects;
    Collection primitiveTypes; 
    String logFileSuffix = "";

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Query.class);
    }

    /** */
    public Test_Query()
    {
        super();
        String suffix = System.getProperty("logFileSuffix");
        if ((suffix != null) && (suffix.length() > 0))
            logFileSuffix = suffix;
    }

    /** 
     * TestCase: run negative query tests.
     */
    public void testNegativeTests()
    {
        String logFileName = "NegativeQueryTests" + logFileSuffix + ".log";
        initLogFile(logFileName);
        boolean ok = runQueryTestSuite(createNegativeTestClasses());
        finitLogFile(ok);
        assertTrue("see " + logFileName + " for details", ok);
    }

    /** 
     * TestCase: run positive query tests as memory queries using transient
     * instances. 
     */
    public void testMemoryQueries()
    {
        String logFileName = "MemoryQueryTests" + logFileSuffix + ".log";
        createObjects();
        initLogFile(logFileName);
        boolean ok = runQueryTestSuite(createPositiveTestClasses());
        finitLogFile(ok);
        assertTrue("see " + logFileName + " for details", ok);
    }

    /** 
     * TestCase: run positive query tests as extent queries using
     * persistent instances. 
     */
    public void testExtentQueries()
    {
        String logFileName = "ExtentQueryTests" + logFileSuffix + ".log";
        if (!existing) {
            createFOStore();
        }
        initLogFile(logFileName);
        boolean ok = runQueryTestSuite(createPositiveTestClasses());
        finitLogFile(ok);
        assertTrue("see " + logFileName + " for details", ok);
    }
    
    // ------------------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------------------

    /**
     * Create persistent instances and store them in FOStore.
     */
    private void createFOStore()
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        createObjects();
        pm.makePersistentAll(companies);
        pm.makePersistentAll(departments);
        pm.makePersistentAll(employees);
        // Note, there is no need to call makePersistentAll on
        // fullTimeEmployees and partTimeEmployees, because they are part
        // of employees
        //pm.makePersistentAll(fullTimeEmployees);
        //pm.makePersistentAll(partTimeEmployees);
        pm.makePersistentAll(insurances);
        pm.makePersistentAll(projects);
        pm.makePersistentAll(primitiveTypes);
        tx.commit();
        pm.close();

        // cleanup candidates
        companies = null;
        departments = null;
        employees = null;
        fullTimeEmployees = null;
        partTimeEmployees = null;
        insurances = null;
        projects = null;
        primitiveTypes = null;
    }
    
    /** 
     * Iterate query test classes and run the tests.
     */
    private boolean runQueryTestSuite(List testClasses)
    {
        boolean ok = true;
        // run all tests, if no specific test cases specified
        for (Iterator i = testClasses.iterator(); i.hasNext();)
            if (!((QueryTest)i.next()).runAll())
                ok = false;
        return ok;
    }
    
    /**
     * Create test classes representing negative query tests.
     */
    private List createNegativeTestClasses()
    {
        List testClasses = new ArrayList(4);
        // negative tests
        testClasses.add(new QueryErrorTest (pmf, log));
        testClasses.add(new SyntaxErrorTest (pmf, log));
        testClasses.add(new SemanticErrorTest (pmf, log));
        testClasses.add(new UnsupportedTest (pmf, log));
        return testClasses;
    }
    
    /**
     * Create test classes representing positive query tests.
     */
    private List createPositiveTestClasses()
    {
        List testClasses = new ArrayList(11);

        // positive tests
        QueryApiTest queryApiTest = new QueryApiTest(pmf, log);
        queryApiTest.initCandidates(companies, departments, employees, 
                                    fullTimeEmployees, partTimeEmployees,
                                    insurances, projects, primitiveTypes);
        testClasses.add(queryApiTest);
        BasicTest basicTest = new BasicTest (pmf, log);
        basicTest.initCandidates(companies, departments, employees, 
                                 fullTimeEmployees, partTimeEmployees,
                                 insurances, projects, primitiveTypes);
        testClasses.add(basicTest);
        ArithmeticTest arithmeticTest = new ArithmeticTest (pmf, log);
        arithmeticTest.initCandidates(companies, departments, employees, 
                                      fullTimeEmployees, partTimeEmployees,
                                      insurances, projects, primitiveTypes);
        testClasses.add(arithmeticTest);
        ParameterTest parameterTest = new ParameterTest (pmf, log);
        parameterTest.initCandidates(companies, departments, employees, 
                                     fullTimeEmployees, partTimeEmployees,
                                     insurances, projects, primitiveTypes);
        testClasses.add(parameterTest);
        AdvancedTest advancedTest = new AdvancedTest (pmf, log);
        advancedTest.initCandidates(companies, departments, employees, 
                                    fullTimeEmployees, partTimeEmployees,
                                    insurances, projects, primitiveTypes);
        testClasses.add(advancedTest);
        NavigationTest navigationTest = new NavigationTest (pmf, log);
        navigationTest.initCandidates(companies, departments, employees, 
                                      fullTimeEmployees, partTimeEmployees,
                                      insurances, projects, primitiveTypes);
        testClasses.add(navigationTest);
        CollectionTest collectionTest = new CollectionTest (pmf, log);
        collectionTest.initCandidates(companies, departments, employees, 
                                      fullTimeEmployees, partTimeEmployees,
                                      insurances, projects, primitiveTypes);
        testClasses.add(collectionTest);
        OrderingTest orderingTest = new OrderingTest (pmf, log);
        orderingTest.initCandidates(companies, departments, employees, 
                                    fullTimeEmployees, partTimeEmployees,
                                    insurances, projects, primitiveTypes);
        testClasses.add(orderingTest);
        ScopingTest scopingTest = new ScopingTest (pmf, log);
        scopingTest.initCandidates(companies, departments, employees, 
                                   fullTimeEmployees, partTimeEmployees,
                                   insurances, projects, primitiveTypes);
        testClasses.add(scopingTest);
        TreeTest treeTest = new TreeTest (pmf, log);
        treeTest.initCandidates(companies, departments, employees, 
                                fullTimeEmployees, partTimeEmployees,
                                insurances, projects, primitiveTypes);
        testClasses.add(treeTest);
        InheritanceTest inheritanceTest = new InheritanceTest (pmf, log);
        inheritanceTest.initCandidates(companies, departments, employees,
                                       fullTimeEmployees, partTimeEmployees,
                                       insurances, projects, primitiveTypes);
        testClasses.add(inheritanceTest);
	
        return testClasses;
    }

    /**
     *
     */
    private void initLogFile(final String logFileName)
    {
        try {
            log = (PrintStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws FileNotFoundException {
                        return new PrintStream(new FileOutputStream(logFileName)); 
                        }
                });
        } 
        catch (PrivilegedActionException ex) {
            System.err.println("Cannot create log file " + logFileName + " reason " + ex.getException());
            log = System.out;
        }
        log.println("START " + new Date());
    }
    
    /**
     *
     */
    private void finitLogFile(boolean ok)
    {
        log.println("END " + new Date() + (ok ? " TEST OK" : " TEST NOT OK"));
        if ((log != System.out) && (log != System.err))
            log.close();
    }


    /**
     * Creates test instances for positive query tests.
     */
    void createObjects()
    {
        companies = new HashSet();
        departments = new HashSet();
        employees = new HashSet();
        fullTimeEmployees = new HashSet();
        partTimeEmployees = new HashSet();
        insurances = new HashSet();
        projects = new HashSet();
        primitiveTypes = new HashSet();

        HashSet tmp; // temporay variable to update collection relatinships

        // create Company instances

        Company abcCompany = new Company(
            "abc", new GregorianCalendar(1995, 0, 1).getTime(), 
            "Lombard Street 2", null);
        companies.add(abcCompany);
        Company xyzCompany = new Company(
            "xyz", new GregorianCalendar(1998, 11, 1).getTime(), 
            "Kurfuerstendamm 5", null);
        companies.add(xyzCompany);

        // create Department instances

        Department dept01 = new Department(
            1L, "Engineering", abcCompany, null);
        departments.add(dept01);
        Department dept02 = new Department(
            2L, "Sales", abcCompany, null);
        departments.add(dept02);
        Department dept03 = new Department(
            3L, "Marketing", abcCompany, null);
        departments.add(dept03);
        Department dept04 = new Department(
            4L, "Human Resource", abcCompany, null);
        departments.add(dept04);

        Department dept11 = new Department(
            11L, "Engineering", xyzCompany, null);
        departments.add(dept11);
        Department dept12 = new Department(
            12L, "Human Resource", xyzCompany, null);
        departments.add(dept12);

        Department dept100 = new Department(
            100L, "Unrelated", null, null);
        departments.add(dept100);

        // set relatoinship Company.departments<->Department.company
        // This code assumes the field Department.company is already 
        // initialzed on Department creation
        tmp = new HashSet();
        tmp.add(dept01);
        tmp.add(dept02);
        tmp.add(dept03);
        tmp.add(dept04);
        abcCompany.setDepartments(tmp);
        tmp = new HashSet();
        tmp.add(dept11);
        tmp.add(dept12);
        xyzCompany.setDepartments(tmp);

        // create Employee instances
        
        Employee emp01 = new FullTimeEmployee(
            1L, "lastEngOne", "firstEngOne", 
            new GregorianCalendar(2000, 0, 1).getTime(), 
            new GregorianCalendar(1911, 0, 1).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept01, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            50000.0D);
        employees.add(emp01);
        fullTimeEmployees.add(emp01);
        Employee emp02 = new FullTimeEmployee(
            2L, "lastEngTwo", "firstEngTwo", 
            new GregorianCalendar(2000, 1, 2).getTime(), 
            new GregorianCalendar(1922, 1, 2).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept01, // Insurance, Department
            emp01, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            45000.0D);
        employees.add(emp02);
        fullTimeEmployees.add(emp02);
        Employee emp03 = new PartTimeEmployee(
            3L, "lastEngThree", "firstEngThree", 
            new GregorianCalendar(2000, 2, 3).getTime(), 
            new GregorianCalendar(1933, 2, 3).getTime(), 
            30.0D, 'P', null, null, // Projects
            null, dept01, // Insurance, Department
            emp01, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            25000.0D);
        employees.add(emp03);
        partTimeEmployees.add(emp03);
        Employee emp04 = new FullTimeEmployee(
            4L, "lastSalesFour", "firstSalesFour", 
            new GregorianCalendar(2000, 3, 4).getTime(), 
            new GregorianCalendar(1944, 3, 4).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept02, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            45000.0D);
        employees.add(emp04);
        fullTimeEmployees.add(emp04);
        Employee emp05 = new PartTimeEmployee(
            5L, "lastSalesFive", "firstSalesFive", 
            new GregorianCalendar(2000, 4, 5).getTime(), 
            new GregorianCalendar(1955, 4, 5).getTime(), 
            20.0D, 'P', null, null, // Projects
            null, dept02, // Insurance, Department
            emp04, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            22000.0D);
        employees.add(emp05);
        partTimeEmployees.add(emp05);
        Employee emp06 = new FullTimeEmployee(
            6L, "lastMarketingSix", "firstMarketingSix", 
            new GregorianCalendar(2000, 5, 6).getTime(), 
            new GregorianCalendar(1966, 5, 6).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept03, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            50000.0D);
        employees.add(emp06);
        fullTimeEmployees.add(emp06);
        Employee emp07 = new PartTimeEmployee(
            7L, "lastMarketingSeven", "firstMarketingSeven", 
            new GregorianCalendar(2000, 6, 7).getTime(), 
            new GregorianCalendar(1977, 6, 7).getTime(), 
            30.0D, 'P', null, null, // Projects
            null, dept03, // Insurance, Department
            emp06, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            30000.0D);
        employees.add(emp07);
        partTimeEmployees.add(emp07);
        Employee emp08 = new FullTimeEmployee(
            8L, "lastHREight", "firstHREight", 
            new GregorianCalendar(2000, 7, 8).getTime(), 
            new GregorianCalendar(1988, 7, 8).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept04, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            55000.0D);
        employees.add(emp08);
        fullTimeEmployees.add(emp08);
        Employee emp09 = new PartTimeEmployee(
            9L, "lastHRNine", "firstHRNine", 
            new GregorianCalendar(2000, 8, 9).getTime(), 
            new GregorianCalendar(1999, 8, 9).getTime(), 
            30.0D, 'P', null, null, // Projects
            null, dept04, // Insurance, Department
            emp08, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            30000.0D);
        employees.add(emp09);
        partTimeEmployees.add(emp09);

        Employee emp11 = new FullTimeEmployee(
            11L, "lastEngOne", "firstEngOne", 
            new GregorianCalendar(1999, 0, 1).getTime(), 
            new GregorianCalendar(1911, 0, 1).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept11, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            55000.0D);
        employees.add(emp11);
        fullTimeEmployees.add(emp11);
        Employee emp12 = new PartTimeEmployee(
            12L, "lastEngTwo", "firstEngTwo", 
            new GregorianCalendar(1999, 1, 2).getTime(), 
            new GregorianCalendar(1922, 1, 2).getTime(), 
            30.0D, 'P', null, null, // Projects
            null, dept11, // Insurance, Department
            emp11, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            20000.0D);
        employees.add(emp12);
        partTimeEmployees.add(emp12);
        Employee emp13 = new FullTimeEmployee(
            13L, "lastHRThree", "firstHRThree", 
            new GregorianCalendar(1999, 2, 3).getTime(), 
            new GregorianCalendar(1933, 1, 2).getTime(), 
            40.0D, 'F', null, null, // Projects
            null, dept12, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null, // HRAdvisor, HRAdvisees
            50000.0D);
        employees.add(emp13);
        fullTimeEmployees.add(emp13);

        Employee emp100 = new Employee(
            100L, "unrelated", null, 
            null, 
            null,
            0.0D, ' ', null, null, // Projects
            null, null, // Insurance, Department
            null, null, // Manager, Team 
            null, null, // Mentor, Protege
            null, null); // HRAdvisor, HRAdvisees
        employees.add(emp100);

        // set relationship Department.employees<->Emplyoee.department
        // This code assumes the field Emplyoee.department is already 
        // initialzed on Emplyoee creation
        tmp = new HashSet();
        tmp.add(emp01);
        tmp.add(emp02);
        tmp.add(emp03);
        dept01.setEmployees(tmp);
        tmp = new HashSet();
        tmp.add(emp04);
        tmp.add(emp05);
        dept02.setEmployees(tmp);
        tmp = new HashSet();
        tmp.add(emp06);
        tmp.add(emp07);
        dept03.setEmployees(tmp);
        tmp = new HashSet();
        tmp.add(emp08);
        tmp.add(emp09);
        dept04.setEmployees(tmp);
        tmp = new HashSet();
        tmp.add(emp11);
        tmp.add(emp12);
        dept11.setEmployees(tmp);
        tmp = new HashSet();
        tmp.add(emp13);
        dept12.setEmployees(tmp);

        // set relationship Employee.manager<->Employee.team
        // This code assumes the field Emplyoee.manager is already 
        // initialzed on Emplyoee creation.
        // NOTE: there is a difference in the team relationship: 
        // emp02, emp03, emp05, emp07, emp09 have an empty set
        // emp12, emp13 have no relationship, meaning the field is null
        tmp = new HashSet();
        tmp.add(emp02);
        tmp.add(emp03);
        emp01.setTeam(tmp);
        emp02.setTeam(new HashSet());
        emp03.setTeam(new HashSet());
        tmp = new HashSet();
        tmp.add(emp05);
        emp04.setTeam(tmp);
        emp05.setTeam(new HashSet());
        tmp = new HashSet();
        tmp.add(emp07);
        emp06.setTeam(tmp);
        emp07.setTeam(new HashSet());
        tmp = new HashSet();
        tmp.add(emp09);
        emp08.setTeam(tmp);
        emp09.setTeam(new HashSet());
        tmp = new HashSet();
        tmp.add(emp12);
        emp11.setTeam(tmp);
        emp12.setTeam(null);
        emp13.setTeam(null);

        // set relationship Employee.manager<->Employee.team
        emp01.setMentor(emp09);
        emp09.setProtege(emp01);
        emp02.setMentor(emp01);
        emp01.setProtege(emp02);
        emp03.setMentor(emp02);
        emp02.setProtege(emp03);
        emp04.setMentor(emp03);
        emp03.setProtege(emp04);
        emp05.setMentor(emp04);
        emp04.setProtege(emp05);
        emp06.setMentor(emp05);
        emp05.setProtege(emp06);
        emp07.setMentor(emp06);
        emp06.setProtege(emp07);
        emp08.setMentor(emp07);
        emp07.setProtege(emp08);
        emp09.setMentor(emp08);
        emp08.setProtege(emp09);
        emp11.setMentor(emp13);
        emp13.setProtege(emp11);
        emp12.setMentor(emp11);
        emp11.setProtege(emp12);
        emp13.setMentor(emp12);
        emp12.setProtege(emp13);

        // set relationship Employee.hradvisor <-> Employee.hradvisees relationship
        tmp = new HashSet();
        emp01.setHradvisor(emp08);
        tmp.add(emp01);
        emp02.setHradvisor(emp08);
        tmp.add(emp02);
        emp03.setHradvisor(emp08);
        tmp.add(emp03);
        emp04.setHradvisor(emp08);
        tmp.add(emp04);
        emp05.setHradvisor(emp08);
        tmp.add(emp05);
        emp06.setHradvisor(emp08);
        tmp.add(emp06);
        emp07.setHradvisor(emp08);
        tmp.add(emp07);
        emp09.setHradvisor(emp08);
        tmp.add(emp09);
        emp08.setHradvisees(tmp);
        tmp = new HashSet();
        emp11.setHradvisor(emp13);
        tmp.add(emp11);
        emp12.setHradvisor(emp13);
        tmp.add(emp12);
        emp13.setHradvisees(tmp);

        // create Insurance instances 

        Insurance ins01 = new Insurance(1L, "Carrier One", emp01);
        insurances.add(ins01);
        Insurance ins02 = new Insurance(2L, "Carrier Two", emp02);
        insurances.add(ins02);
        Insurance ins03 = new Insurance(3L, "Carrier Three", emp03);
        insurances.add(ins03);
        Insurance ins04 = new Insurance(4L, "Carrier Four", emp04);
        insurances.add(ins04);
        Insurance ins05 = new Insurance(5L, "Carrier Five", emp05);
        insurances.add(ins05);
        Insurance ins06 = new Insurance(6L, "Carrier Six", emp06);
        insurances.add(ins06);
        Insurance ins07 = new Insurance(7L, "Carrier Seven", emp07);
        insurances.add(ins07);
        Insurance ins08 = new Insurance(8L, "Carrier Eight", emp08);
        insurances.add(ins08);
        Insurance ins09 = new Insurance(9L, "Carrier Nine", emp09);
        insurances.add(ins09);

        Insurance ins11 = new Insurance(11L, "Carrier Eleven", emp11);
        insurances.add(ins11);
        Insurance ins12 = new Insurance(12L, "Carrier Twelve", emp12);
        insurances.add(ins12);
        Insurance ins13 = new Insurance(13L, "Carrier Thirteen", emp13);
        insurances.add(ins13);

        // set relationship Insurance.employee<->Emplyoee.insurance
        // This code assumes the field Insurance.employee is already 
        // initialzed on Insurance creation
        emp01.setInsurance(ins01);
        emp02.setInsurance(ins02);
        emp03.setInsurance(ins03);
        emp04.setInsurance(ins04);
        emp05.setInsurance(ins05);
        emp06.setInsurance(ins06);
        emp07.setInsurance(ins07);
        emp08.setInsurance(ins08);
        emp09.setInsurance(ins09);
        emp11.setInsurance(ins11);
        emp12.setInsurance(ins12);
        emp13.setInsurance(ins13);

        // create Project instances

        Project proj01 = new Project(
            1L, "Engineering Project", new BigDecimal("10000000.0"), null, null);
        projects.add(proj01);
        Project proj02 = new Project(
            2L, "Sales Project", new BigDecimal("5000000.0"), null, null);
        projects.add(proj02);
        Project proj03 = new Project(
            3L, "Marketing Project", new BigDecimal("8000000.0"), null, null);
        projects.add(proj03);
        Project proj04 = new Project(
            4L, "HR Project", new BigDecimal("10000000.0"), null, null);
        projects.add(proj04);
        Project proj11 = new Project(
            11L, "Engineering Project", new BigDecimal("2000000.0"), null, null);
        projects.add(proj11);
        Project proj12 = new Project(
            12L, "HR Project",  new BigDecimal("1500000.0"), null, null);
        projects.add(proj12);

        // set relationship Employee.projects<->Project.employees
        HashSet proj01Emps = new HashSet();
        HashSet proj02Emps = new HashSet();
        HashSet proj03Emps = new HashSet();
        HashSet proj11Emps = new HashSet();
        HashSet proj12Emps = new HashSet();
        HashSet emp01Projs = new HashSet();
        HashSet emp02Projs = new HashSet();
        HashSet emp03Projs = new HashSet();
        HashSet emp04Projs = new HashSet();
        HashSet emp05Projs = new HashSet();
        HashSet emp06Projs = new HashSet();
        HashSet emp07Projs = new HashSet();
        HashSet emp11Projs = new HashSet();
        HashSet emp12Projs = new HashSet();
        HashSet emp13Projs = new HashSet();

        proj01Emps.add(emp01);
        emp01Projs.add(proj01);
        proj01Emps.add(emp02);
        emp02Projs.add(proj01);
        proj01Emps.add(emp03);
        emp03Projs.add(proj01);
        proj01Emps.add(emp04);
        emp04Projs.add(proj01);
        proj01Emps.add(emp06);
        emp06Projs.add(proj01);
        proj02Emps.add(emp01);
        emp01Projs.add(proj02);
        proj02Emps.add(emp06);
        emp06Projs.add(proj02);
        proj02Emps.add(emp04);
        emp04Projs.add(proj02);
        proj02Emps.add(emp05);
        emp05Projs.add(proj02);
        proj03Emps.add(emp01);
        emp01Projs.add(proj03);
        proj03Emps.add(emp04);
        emp04Projs.add(proj03);
        proj03Emps.add(emp06);
        emp06Projs.add(proj03);
        proj03Emps.add(emp07);
        emp07Projs.add(proj03);
        proj11Emps.add(emp11);
        emp11Projs.add(proj11);
        proj11Emps.add(emp12);
        emp12Projs.add(proj11);
        proj12Emps.add(emp13);
        emp13Projs.add(proj12);

        proj01.setEmployees(proj01Emps);
        proj02.setEmployees(proj02Emps);
        proj03.setEmployees(proj03Emps);
        proj11.setEmployees(proj11Emps);
        proj12.setEmployees(proj12Emps);
        emp01.setProjects(emp01Projs);
        emp02.setProjects(emp02Projs);
        emp03.setProjects(emp03Projs);
        emp04.setProjects(emp04Projs);
        emp05.setProjects(emp05Projs);
        emp06.setProjects(emp06Projs);
        emp07.setProjects(emp07Projs);
        emp11.setProjects(emp11Projs);
        emp12.setProjects(emp12Projs);
        emp13.setProjects(emp13Projs);

        // set relationship Employee.reviewedProjects<->Projects.reviewers
        HashSet proj01Reviewers = new HashSet();
        HashSet proj02Reviewers = new HashSet();
        HashSet proj03Reviewers = new HashSet();
        HashSet proj04Reviewers = new HashSet();
        HashSet proj11Reviewers = new HashSet();
        HashSet proj12Reviewers = new HashSet();
        HashSet emp01RevProjs = new HashSet();
        HashSet emp04RevProjs = new HashSet();
        HashSet emp06RevProjs = new HashSet();
        HashSet emp08RevProjs = new HashSet();
        HashSet emp11RevProjs = new HashSet();
        HashSet emp12RevProjs = new HashSet();

        proj01Reviewers.add(emp01);
        emp01RevProjs.add(proj01);
        proj01Reviewers.add(emp04);
        emp04RevProjs.add(proj01);
        proj01Reviewers.add(emp06);
        emp06RevProjs.add(proj01);
        proj02Reviewers.add(emp04);
        emp04RevProjs.add(proj03);
        proj02Reviewers.add(emp06);
        emp06RevProjs.add(proj02);
        proj03Reviewers.add(emp04);
        emp04RevProjs.add(proj03);
        proj03Reviewers.add(emp06);
        emp06RevProjs.add(proj03);
        proj03Reviewers.add(emp08);
        emp08RevProjs.add(proj03);
        proj04Reviewers.add(emp08);
        emp08RevProjs.add(proj04);
        proj11Reviewers.add(emp12);
        emp12RevProjs.add(proj11);
        proj12Reviewers.add(emp11);
        emp11RevProjs.add(proj12);

        proj01.setReviewers(proj01Reviewers);
        proj02.setReviewers(proj02Reviewers);
        proj03.setReviewers(proj03Reviewers);
        proj04.setReviewers(proj04Reviewers);
        proj11.setReviewers(proj11Reviewers);
        proj12.setReviewers(proj12Reviewers);
        emp01.setReviewedProjects(emp01RevProjs);
        emp04.setReviewedProjects(emp04RevProjs);
        emp06.setReviewedProjects(emp06RevProjs);
        emp08.setReviewedProjects(emp08RevProjs);
        emp11.setReviewedProjects(emp11RevProjs);
        emp12.setReviewedProjects(emp12RevProjs);

        // create Primitive_Types instances
        PrimitiveTypes prim100 = new PrimitiveTypes(
            100L, false, null, (byte)0, null, (short)0, null, 0, null,
            0L, null, 0.0F, null, 0.0, null, '0', null, 
            null, null, null, null, null);
        primitiveTypes.add(prim100);
        PrimitiveTypes prim00 = new PrimitiveTypes(
            0L, false, Boolean.FALSE, 
            (byte)0, new Byte((byte)0), (short)0, new Short((short)0), 
            0, new Integer(0), 0L, new Long(0L), 
            0.0F, new Float(0.0F), 0.0, new Double(0.0), 
            '0', new Character('0'), 
            new GregorianCalendar(2001, 0, 1).getTime(), " ", 
            new BigDecimal("0.0"), new BigInteger("0"), new Long(0L));
        primitiveTypes.add(prim00);
        PrimitiveTypes prim01 = new PrimitiveTypes(
            1L, true, Boolean.TRUE, 
            (byte)1, new Byte((byte)1), (short)1, new Short((short)1), 
            1, new Integer(1), 1L, new Long(1L), 
            1.0F, new Float(1.0F), 1.0, new Double(1.0), 
            '1', new Character('1'), 
            new GregorianCalendar(2001, 0, 1).getTime(), "text", 
            new BigDecimal("1.0"), new BigInteger("1"), new Long(1L));
        primitiveTypes.add(prim01);
        PrimitiveTypes prim02 = new PrimitiveTypes(
            2L, false, Boolean.FALSE, 
            (byte)2, new Byte((byte)2), (short)2, new Short((short)2), 
            2, new Integer(2), 2L, new Long(2L), 
            2.0F, new Float(2.0F), 2.0, new Double(2.0), 
            '2', new Character('2'), 
            new GregorianCalendar(2001, 1, 2).getTime(), "text", 
            new BigDecimal("2.0"), new BigInteger("2"), new Long(2L));
        primitiveTypes.add(prim02);
        PrimitiveTypes prim03 = new PrimitiveTypes(
            3L, true, Boolean.TRUE, 
            (byte)3, new Byte((byte)3), (short)3, new Short((short)3), 
            3, new Integer(3), 3L, new Long(3L), 
            3.0F, new Float(3.0F), 3.0, new Double(3.0), 
            '3', new Character('3'), 
            new GregorianCalendar(2001, 2, 3).getTime(), "text", 
            new BigDecimal("3.0"), new BigInteger("3"), new Long(3L));
        primitiveTypes.add(prim03);
        PrimitiveTypes prim04 = new PrimitiveTypes(
            4L, false, Boolean.FALSE, 
            (byte)4, new Byte((byte)4), (short)4, new Short((short)4), 
            4, new Integer(4), 4L, new Long(4L), 
            4.0F, new Float(4.0F), 4.0, new Double(4.0), 
            '4', new Character('4'), 
            new GregorianCalendar(2001, 3, 4).getTime(), "text", 
            new BigDecimal("4.0"), new BigInteger("4"), new Long(4L));
        primitiveTypes.add(prim04);
        PrimitiveTypes prim05 = new PrimitiveTypes(
            5L, true, Boolean.TRUE, 
            (byte)5, new Byte((byte)5), (short)5, new Short((short)5), 
            5, new Integer(5), 5L, new Long(5L), 
            5.0F, new Float(5.0F), 5.0, new Double(5.0), 
            '5', new Character('5'), 
            new GregorianCalendar(2001, 4, 5).getTime(), "text", 
            new BigDecimal("5.0"), new BigInteger("5"), new Long(5L));
        primitiveTypes.add(prim05);
        PrimitiveTypes prim10 = new PrimitiveTypes(
            10L, false, Boolean.FALSE, 
            (byte)127, new Byte((byte)127), (short)32767, new Short((short)32767), 
            2147483647, new Integer(2147483647), 9223372036854775807L, new Long(9223372036854775807L), 
            1.2345F, new Float(1.2345F), 1.2345, new Double(1.2345), 
            'a', new Character('a'), 
            new GregorianCalendar(2001, 5, 6).getTime(), "this is a string", 
            new BigDecimal("10000.123"), new BigInteger("10000"), new Long(10L));
        primitiveTypes.add(prim10);
        PrimitiveTypes prim11 = new PrimitiveTypes(
            11L, true, Boolean.TRUE, 
            (byte)-128, new Byte((byte)-128), (short)-32767, new Short((short)-32767), 
            -2147483648, new Integer(-2147483648), -9223372036854775808L, new Long(-9223372036854775808L), 
            -1.2345F, new Float(-1.2345F), -1.2345, new Double(-1.2345), 
            'A', new Character('A'), 
            new GregorianCalendar(2001, 6, 7).getTime(), "this is a string", 
            new BigDecimal("-10000.123"), new BigInteger("-10000"), new Long(11L));
        primitiveTypes.add(prim11);
        PrimitiveTypes prim12 = new PrimitiveTypes(
            12L, false, Boolean.FALSE, 
            (byte)0, new Byte((byte)0), (short)0, new Short((short)0), 
            0, new Integer(0), 0L, new Long(0L), 
            0.00005F, new Float(0.00005F), 0.000000005, new Double(0.000000005), 
            '0', new Character('0'), 
            new GregorianCalendar(2001, 7, 8).getTime(), "yet another string", 
            new BigDecimal("0.0"), new BigInteger("0"), new Long(0L));
        primitiveTypes.add(prim12);
        PrimitiveTypes prim13 = new PrimitiveTypes(
            13L, false, Boolean.FALSE, 
            (byte)0, new Byte((byte)0), (short)0, new Short((short)0), 
            0, new Integer(0), 0L, new Long(0L), 
            -0.00005F, new Float(-0.00005F), -0.000000005, new Double(-0.000000005), 
            '0', new Character('0'), 
            new GregorianCalendar(2001, 8, 9).getTime(), "yet another string", 
            new BigDecimal("0.0"), new BigInteger("0"), new Long(0L));
        primitiveTypes.add(prim13);
        PrimitiveTypes prim14 = new PrimitiveTypes(
            14L, false, Boolean.FALSE, 
            (byte)0, new Byte((byte)0), (short)0, new Short((short)0), 
            0, new Integer(0), 0L, new Long(0L), 
            1234567.8F, new Float(1234567.8F), 1234567890000.5, new Double(1234567890000.5), 
            '0', new Character('0'), 
            new GregorianCalendar(2001, 9, 10).getTime(), "yet another string", 
            new BigDecimal("0.0"), new BigInteger("0"), new Long(0L));
        primitiveTypes.add(prim14);
        PrimitiveTypes prim15 = new PrimitiveTypes(
            15L, false, Boolean.FALSE, 
            (byte)0, new Byte((byte)0), (short)0, new Short((short)0), 
            0, new Integer(0), 0L, new Long(0L), 
            -1234567.8F, new Float(-1234567.8F), -1234567890000.5, new Double(-1234567890000.5), 
            '0', new Character('0'), 
            new GregorianCalendar(2001, 11, 12).getTime(), "yet another string", 
            new BigDecimal("0.0"), new BigInteger("0"), new Long(0L));
        primitiveTypes.add(prim15);
    }

}
