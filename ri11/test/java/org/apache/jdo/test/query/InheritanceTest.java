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

/*
 * InheritanceTest.java
 *
 * Created on March 11, 2003
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.FullTimeEmployee;
import org.apache.jdo.pc.xempdept.Person;

/** 
 *
 * @author  Michael Bouschen
 */
public class InheritanceTest
    extends PositiveTest
{
    public InheritanceTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("inheritance");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: candidate class = Person, candidates = employees
     */
    public void inheritance001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Person.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: candidate class = Employee, candidates = employees
     */
    public void inheritance002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: candidate class = FullTimeEmployee, candidates = employees
     */
    public void inheritance003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(FullTimeEmployee.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        // Do not include employees with empid 3 and 12, because they are
        // PartTimeEmployees 
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: candidate class = Employee, candidates = partTimeEmployees
     */
    public void inheritance004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
        setPartTimeEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: candidate class = Employee, candidates = department
     */
    public void inheritance005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        // expected is empty, because of a mismatch of candidate class
        // (Employee) and candidates (Department)
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: cast expression 
     * checks bug report 4711477
     */
    public void inheritance006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.FullTimeEmployee");
        query.setFilter("((FullTimeEmployee)this).salary < 50000.0D");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: cast expression inside or expression 
     * checks bug report 4833898
     */
    public void inheritance007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.FullTimeEmployee");
        query.setFilter("((FullTimeEmployee)this).salary < 50000.0D | this.empid == 3");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: cast expression inside or expression 
     * checks bug report 4833898
     */
    public void inheritance008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.FullTimeEmployee");
        query.setFilter("this.empid == 3 || ((FullTimeEmployee)this).salary < 50000.0D");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

}

