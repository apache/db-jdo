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
 * OrderingTest.java
 *
 * Created on March 23, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Insurance;

/** 
 *
 * @author  Michael Bouschen
 */
public class OrderingTest
    extends PositiveTest
{
    public OrderingTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("ordering");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================
   
    /**
     * Testcase: simple order by query ascending
     */
    public void ordering001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setOrdering("firstname ascending, empid ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
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
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple order by query descending
     */
    public void ordering002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setOrdering("carrier descending");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 12;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 13;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 11;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 8;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple order by query ascending date field
     */
    public void ordering003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("hiredate descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple order by query ascending using primary key.
     */
    public void ordering004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("empid ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
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
     * Testcase: simple order by query descending using primary key.
     */
    public void ordering005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("empid descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query ascending using simple navigation
     */
    public void ordering006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 10");
        query.setOrdering("insurance.carrier ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query descending using simple navigation 
     */
    public void ordering007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 10");
        query.setOrdering("insurance.insid descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query descending using simple navigation plus 
     * navigating the same realtionship in the filter
     */
    public void ordering008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("2 < insurance.insid & insurance.insid < 7 ");
        query.setOrdering("insurance.insid descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query ascending using multiple navigation in one ordering definition
     */
    public void ordering009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("insid < 10");
        query.setOrdering("employee.department.name ascending, insid ascending");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
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
        key.insid = 8;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query descending using multiple navigation in one ordering definition
     */
    public void ordering010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("insid < 10");
        query.setOrdering("employee.department.name descending, insid descending");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 8;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query including multiple ordering definitions, one using navigation
     */
    public void ordering011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("department.deptid ascending, empid ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
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
     * Testcase: order by query including multiple ordering definitions having different directions, 
     * one using navigation
     */
    public void ordering012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("department.deptid ascending, empid descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query including multiple ordering definitions having different directions, 
     * one using navigation, changed order of ordering definitions.
     */
    public void ordering013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid < 100");
        query.setOrdering("empid descending, department.deptid ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query including multiple ordering definitions, both using navigation
     */
    public void ordering014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.setFilter("insid >= 1");
        query.setOrdering("employee.department.name ascending, employee.empid descending");
        setInsuranceCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 12;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 11;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 13;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 8;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: order by query ascending using simple navigation w/o filter
     * checks bug report 4384774
     */
    public void ordering015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setOrdering("insurance.carrier ascending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }

}


