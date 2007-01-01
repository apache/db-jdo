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
 * ArithmeticTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.PrimitiveTypes;

/** 
 *
 * @author  Michael Bouschen
 */
public class ArithmeticTest
    extends PositiveTest
{
    public ArithmeticTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
        return methodName.startsWith("arithmetic");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================
    
    /**
     * Testcase: arithmetic operation 
     * field == literal + literal
     */
    public void arithmetic001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("intNotNull == ( 1 + 2 )");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 3;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: arithmetic operation 
     * field - literal > literal
     */
    public void arithmetic002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("floatNotNull - 10000.0 > 10000.0");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 14;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: arithmetic operation 
     * field * literal > literal
     */
    public void arithmetic003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("doubleNotNull * 10 > 45.0");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 5;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 14;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: arithmetic operation 
     * field / literal < literal
     */
    public void arithmetic004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("doubleNull > 0.0 && (doubleNull / 10.0 < 0.5)");
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
        key.id = 10;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: unary operation +
     */
    public void arithmetic005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("+byteNotNull == 1");
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
     * Testcase: unary operation -
     * check bug report 4437010
     */
    public void arithmetic006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("-byteNull == -1");
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
     * Testcase: complement operation ~
     */
    public void arithmetic007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("~shortNotNull == -2");
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
     * Testcase: string concatenation operator +
     */
    public void arithmetic008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("stringNull == \"yet \" + \"another string\"");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
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
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
}

