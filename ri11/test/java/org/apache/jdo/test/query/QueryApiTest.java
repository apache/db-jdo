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

/* QueryApiTest.java
 * 
 * These are positive tests written to test the basic functionalities of 
 * the following API of Query module
 *
 * Testing the following Query Factory methods of PersistenceManager Interface
 * Query newQuery() 
 * Query newQuery(Object query) 
 * Query newQuery (Class cls) 
 * Query newQuery (Class cls, Collection cln) 
 * Query newQuery (Class cls, String filter) 
 * Query newQuery (Class cls, Collection cln, String filter)  
 *
 * Collection getExtent (Class PersistenceCapableClass, boolean subclasses) 
 *
 * Testing the following methods of Query Interface 
 *
 * PersistenceManaget getPersistenceManager()
 * void setClass (Class cls)
 * void setCollection (Collection cln) OR SetCandidates(Collection cln) 
 * void setFilter (String filter) 
 * void declareImports (String imports) 
 * void declareVariables (String variables) 
 * void declareParameters (String parameters) 
 * void setOrdering (String ordering) 
 * void setIgnoreCache() 
 * boolean getIgnoreCache()
 * void compile()
 * void execute()
 * void execute(Object p1) 
 * void execute(Object p1, Object p2) 
 * void execute(Object p1, Object p2, Object p3)
 * Object executeWithArray(Object[] a)
 * Object executeWithMap( Map param)
 *
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Employee;

/**
 *
 * @author Shrikant Wagh
 * @author Michael Bouschen
 */

public class QueryApiTest extends PositiveTest
{
    public QueryApiTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }
    
    protected boolean isTestMethodName(String methodName)
    {
        return methodName.startsWith("queryApi");
    }

    // Tests the following methods :
    // PersistenceManager::newQuery()
    // Query::setClass(Class class)
    // Query::setCandidate(Collection cln);
    // Query::setFilter(String filter);
    // Query::execute()
    // Query::getExtent(Class PersistenceCapableClass, boolean subclass)


    public void queryApi001() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 1");
        setEmployeeCandidates(query);

        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);

        tx.commit();
    }

    // Tests PersistenceManager::newQuery(Class cls) method

    public void queryApi002() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid == 1");
        setEmployeeCandidates(query);
     
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    // Tests PersistenceManager::Query(Class cls, Collection cln) method

    public void queryApi003() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = (employeeCandidates == null) ?
            pm.newQuery(pm.getExtent(Employee.class, true)) :
            pm.newQuery(Employee.class, employeeCandidates);
        query.setFilter("empid == 1");

        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);

        tx.commit();
    }

    // Tests PersistenceManager::newQuery(Class cls, String filter) method

    public void queryApi004() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery(Employee.class, "empid == 1");
        setEmployeeCandidates(query);

        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);

        tx.commit();
    }



    // Tests PersistenceManager::newQuery(Extent cln, String filter) method

    public void queryApi005() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = (employeeCandidates == null) ?
            pm.newQuery(pm.getExtent(Employee.class, true), "empid == 1") :
            pm.newQuery(Employee.class, employeeCandidates, "empid == 1");

        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);

        tx.commit();
    }

    // Tests PersistenceManager::newQuery(Object query) method

    public void queryApi006() throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery(Employee.class);
        query.setFilter("empid == 1");
        setEmployeeCandidates(query);

        Query nwQuery = pm.newQuery(query);
        setEmployeeCandidates(nwQuery);

        Object result = nwQuery.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);

        tx.commit();
    }

    // Tests void Query::declareParameters(String param)
    // void Query::execute(Object obj)

    public void queryApi007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
       
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long id");
        query.setFilter("empid == id");
        setEmployeeCandidates(query);

        Object result = query.execute(new Long(1));
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }
  
    // Tests  void Query::execute(Object obj1, Object obj2)

    public void queryApi008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
       
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long id, String lname");
        query.setFilter("(empid == id) && (this.lastname == lname)");
        setEmployeeCandidates(query);

        Object result = query.execute(new Long(1), "lastEngOne");
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    // Tests void Query::execute(Object obj1, Object obj2, Object obj3)

    public void queryApi009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
       
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long id, String lname, String fname");
        query.setFilter("(empid == id) && (this.lastname == lname) && (this.firstname == fname)");
        setEmployeeCandidates(query);

        Object result = query.execute(new Long(1), "lastEngOne", "firstEngOne");
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    // Tests void Query::executeWithArray(Object[] obj) method

    public void queryApi010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l");
        query.setFilter("empid == l");
        setEmployeeCandidates(query);
        Object[] actualParams = {new Long(1)};

        Object result = query.executeWithArray(actualParams);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }    

    // Tests void Query::executeWithArray(Object[] obj) method with multiple parameters

    public void queryApi011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l, String lname");
        query.setFilter("(empid == l) && (this.lastname == lname)");
        setEmployeeCandidates(query);
        Object[] actualParams = {new Long(1), "lastEngOne"};

        Object result = query.executeWithArray(actualParams);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }    

    // Tests void Query::executeWithMap(Map param) method

    public void queryApi012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l");
        query.setFilter("empid == l");
        setEmployeeCandidates(query);

        Map actualParams = new java.util.HashMap();
        actualParams.put("l", new Long(1));

        Object result = query.executeWithMap(actualParams);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }    

    // Tests void Query::executeWithMap(Map param) method with multiple params

    public void queryApi013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l, String lname");
        query.setFilter("(empid == l) && (this.lastname == lname)");
        setEmployeeCandidates(query);

        Map actualParams = new java.util.HashMap();
        actualParams.put("l", new Long(1));
        actualParams.put("lname", "lastEngOne");

        Object result = query.executeWithMap(actualParams);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);

        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    // Tests Query::setOrdering(String ordering) method with order by ascending

    public void queryApi014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid > 2 & empid < 9");
        query.setOrdering("lastname ascending");
        setEmployeeCandidates(query);

        Object result = query.execute();
        List oids = getResultOids(result);
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    // Tests Query::setOrdering(String ordering) method with order by descending

    public void queryApi015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid > 2 & empid < 9");
        query.setOrdering("lastname descending");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);

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
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    

}
