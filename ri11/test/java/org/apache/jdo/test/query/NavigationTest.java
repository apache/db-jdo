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
 * NavigationTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Insurance;

/** 
 *
 * @author  Michael Bouschen
 */
public class NavigationTest
    extends PositiveTest
{
    public NavigationTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("navigation");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================
   
    /**
     * Testcase: simple navigation
     */
    public void navigation001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.name == \"Engineering\"");
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
     * Testcase: simple navigation of self referencing relationship
     */
    public void navigation002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("manager.lastname == \"lastSalesFour\"");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: path expression including multiple navigation
     */
    public void navigation003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("employee.department.name == \"Engineering\"");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 11;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: path expression including multiple navigation including self referencing relationship
     */
    public void navigation004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("employee.manager.empid == 1");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple navigation + &-clause
     */
    public void navigation005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 1 & department.name == \"Engineering\"");
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
     * Testcase: simple navigation + &-clause (changed order of & operands)
     */
    public void navigation006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.name == \"Engineering\" & empid == 1");
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
     * Testcase: simple navigation of self referencing relationship + &-clause
     */
    public void navigation007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 2 & manager.empid == 1");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple navigation + |-clause
     */
    public void navigation008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3 | department.name == \"Engineering\"");
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
     * Testcase: simple navigation + |-clause (changed order of | operands)
     */
    public void navigation009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.name == \"Engineering\" | empid == 3");
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
     * Testcase: simple navigation of self referencing relationship + |-clause
     */
    public void navigation010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3 | manager.empid == 4");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }


    /**
     * Testcase: navigate different relationships in single ==-clause
     */
    public void navigation011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.deptid == insurance.insid");
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
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate different relationships (including self referencing relationship) in single ==-clause
     */
    public void navigation012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.deptid == manager.empid");
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
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same relationship multiple times, &&-clause
     */
    public void navigation013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.deptid == 1 && department.name == \"Engineering\"");
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
     * Testcase: navigate the same self referencing relationship multiple times, &&-clause
     */
    public void navigation014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("manager.empid == 1 && manager.lastname == \"lastEngOne\"");
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
     * Testcase: navigate the same relationship multiple times, &&-clause, empty result set
     */
    public void navigation015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.deptid == 1 && department.name == \"Sales\"");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same relationship multiple times, |-clause
     */
    public void navigation016()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.deptid == 2 | department.deptid == 3");
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same self referencing relationship multiple times, |-clause
     */
    public void navigation017()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("manager.empid == 4 | manager.empid == 1");
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
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same relationship multiple times in different AND/OR clauses
     * checks bug report 4342223
     */
    public void navigation018()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(firstname == \"firstEngOne\" & department.name == \"Engineering\") | department.deptid == 1");
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same self referencing relationship multiple times in different AND/OR clauses
     * checks bug report 4342223
     */
    public void navigation019()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(firstname == \"firstEngTwo\" & manager.empid == 1) | manager.empid == 4");
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
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same relationship multiple times in different AND/OR clauses
     * checks bug report 4342223
     */
    public void navigation020()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(firstname == \"firstEngOne\" & department.deptid == 1) | (firstname == \"firstSalesFour\" & department.deptid == 2)");
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
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigate the same relationship multiple times in different AND/OR clauses
     * checks bug report 4342223
     */
    public void navigation021()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(firstname == \"firstEngTwo\" | department.deptid == 2) & (firstname == \"firstSalesFour\" | department.deptid == 1)");
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
     * Testcase: path expression including multiple navigation, &&-clause
     * checks bug report 4380404 "query: fatal exception in multiple navigation filter"
     */
    public void navigation022()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("employee.department.name == \"Engineering\" & (insid < 3)");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   relship == null
     */
    public void navigation023()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department == null");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   null == relship
     */
    public void navigation024()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("null == department");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   relship != null
     */
    public void navigation025()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department != null");
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   null != relship
     */
    public void navigation026()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department != null");
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigation of reference side of a 1:n relationship mapped to compound FK
     */
    public void navigation027()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("company.name == \"xyz\"");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigation self-ref 1:1 relationship
     */
    public void navigation028()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("mentor.empid == 2");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: multiple navigation level 3
     */
    public void navigation029()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("manager.department.company.name == \"xyz\"");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigation of different relationships between the same classes (1:n, 1:n)
     */
    public void navigation030()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("manager.empid == 1 & hradvisor.empid == 8");
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
     * Testcase: navigation of different relationships between the same classes (1:1, 1:n)
     */
    public void navigation031()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("mentor.empid == 2 & manager.empid == 1");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

}
