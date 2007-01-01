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
 * BasicTest.java
 *
 * Created on March 31, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.math.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;

/** 
 *
 * @author  Michael Bouschen
 */
public class BasicTest
  extends PositiveTest
{
    public BasicTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("basic");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================
    
    /**
     * Testcase: relational operation 
     * field == literal
     */
    public void basic001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 1");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: relational operation 
     * field == literal (String)
     */
    public void basic002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname == \"lastEngTwo\"");
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
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    /**
     * Testcase: relational operation 
     * literal == field
     */
    public void basic003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("1 == empid");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: relational operation 
     * this.field == literal
     */
    public void basic004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("this.empid == 1");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: relational operation 
     * field != literal
     */
    public void basic005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create and execute query
       
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid != 2");
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
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: relational operation 
     * field != literal (Strings)
     */
    public void basic006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create and execute query
       
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname != \"lastEngThree\"");
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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: relational operation 
     * field < literal
     */
    public void basic007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid < 6");
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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: relational operation 
     * field <= literal
     */
    public void basic008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid <= 3");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: relational operation 
     * field > literal
     */
    public void basic009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid > 4");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

         // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: relational operation 
     * field >= literal
     */
    public void basic010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid >= 4");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

         // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: relational operation 
     * literal < field
     */
    public void basic011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery(Employee.class);
        query.setFilter("4 < empid");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

         // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: logical operation
     * cond & cond
     */
    public void basic012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 1) & (lastname == \"lastEngOne\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: logical operation
     * cond | cond
     */
    public void basic013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 2) | (lastname == \"lastEngThree\")");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: conditional operation
     * cond && cond
     */
    public void basic014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 1) && (lastname == \"lastEngOne\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: conditional operation
     * cond || cond
     */
    public void basic015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 2) || (lastname == \"lastEngThree\")");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: ! operation
     * checks bug report 4343836
     */
    public void basic016()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("!(empid > 2)");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: access of static field
     */
    public void basic017()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import java.lang.Byte");
        query.setFilter("empid < Byte.MAX_VALUE");
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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: true filter
     */
    public void basic018()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("true");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: false filter
     */
    public void basic019()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("false");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: iterate extent collection
     * checks bug report 4371445
     */
    public void basic020()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Collection result = new ArrayList();
        Iterator i = (departmentCandidates == null) ? 
            pm.getExtent(Department.class, false).iterator() :
            departmentCandidates.iterator();
        while (i.hasNext())
            result.add(i.next());
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: query result takes updated and new instances into accout
     * datastore transaction
     */
    public void basic021()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        boolean originalOptimisticFlag = tx.getOptimistic();
        tx.setOptimistic(false);
        tx.begin();
        
        // create new Employee having weeklyhours = 40.0
        Employee e = new Employee();
        e.setEmpid(1000);
        e.setLastname("Bouschen");
        e.setFirstname("Michael");
        e.setWeeklyhours(40.0);
        e.setMentor(e);
        pm.makePersistent(e);
        
        // update weeklyhours of emplyoee 5 to be 40.0
        Employee emp05 = getEmployeeById(5L);
        emp05.setWeeklyhours(40.0);

        // update salary of emplyoee 8 to be less than 40.0
        Employee emp08 = getEmployeeById(8L);
        emp08.setWeeklyhours(30.0);
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("weeklyhours > 39.9");
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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        // In the case of an extent query the new employee with the empid 1000
        // should be part of the result set.
        // In the case of a memory query it should not.
        if (employeeCandidates == null) { // means extent query
            key = new Employee.Oid();
            key.empid = 1000;
            expected.add(key);
        }
        checkQueryResult(oids, expected);
        
        tx.rollback();
        tx.setOptimistic(originalOptimisticFlag);
    }

    /**
     * Testcase: query result takes updated and new instances into accout
     * optimistic transaction
     */
    public void basic022()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        boolean originalOptimisticFlag = tx.getOptimistic();
        tx.setOptimistic(true);
        tx.begin();
        
        // create new Employee having weeklyhours = 40.0
        Employee e = new Employee();
        e.setEmpid(1000);
        e.setLastname("Bouschen");
        e.setFirstname("Michael");
        e.setWeeklyhours(40.0);
        e.setMentor(e);
        pm.makePersistent(e);
        
        // update weeklyhours of emplyoee 5 to be 40.0
        Employee emp05 = getEmployeeById(5L);
        emp05.setWeeklyhours(40.0);

        // update salary of emplyoee 8 to be less than 40.0
        Employee emp08 = getEmployeeById(8L);
        emp08.setWeeklyhours(30.0);
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("weeklyhours > 39.9");
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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        // In the case of an extent query the new employee with the empid 1000
        // should be part of the result set.
        // In the case of a memory query it should not.
        if (employeeCandidates == null) { // means extent query
            key = new Employee.Oid();
            key.empid = 1000;
            expected.add(key);
        }
        checkQueryResult(oids, expected);

        tx.rollback();
        tx.setOptimistic(originalOptimisticFlag);
    }

    /**
     * Testcase: char field compared with int literal
     * checks bug report 4530265
     */
    public void basic023()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("charNotNull == 48");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result

        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 0;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 12;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 13;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 14;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 15;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: Character field compared with double literal
     * checks bug report 4530265
     */
    public void basic024()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("charNull == 49.0");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: char field compared with Double parameter
     * checks bug report 4530265
     */
    public void basic025()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("Double value");
        query.setFilter("charNotNull == value");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(new Double(50.0));
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: Character field compared with BigInteger parameter
     * checks bug report 4530265
     */
    public void basic026()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("java.math.BigInteger value");
        query.setFilter("charNull == value");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(new BigInteger("50"));
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: using != on BigInteger operand(s)
     * checks bug report 4530670
     */
    public void basic027()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("bigInteger != 0");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 2;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 3;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 4;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 5;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 10;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 11;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: using != on BigDecimal operand(s)
     * checks bug report 4530670
     */
    public void basic028()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("bigDecimal != doubleNotNull");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 10;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 11;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 12;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 13;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 14;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 15;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: using > on strings
     * checks bug report 4530899
     */
    public void basic029()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname > \"lastHREight\"");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: using >= on strings
     * checks bug report 4530899
     */
    public void basic030()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname >= \"lastHREight\"");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: using < on strings
     * checks bug report 4530899
     */
    public void basic031()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname < \"lastHREight\"");
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
     * Testcase: using <= on strings
     * checks bug report 4530899
     */
    public void basic032()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname <= \"lastHREight\"");
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
        key.empid = 8;
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
}
