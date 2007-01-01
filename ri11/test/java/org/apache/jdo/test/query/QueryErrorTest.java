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
 * QueryErrorTest.java
 *
 * Created on April 6, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.*;

import javax.jdo.*;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.pc.xempdept.Employee;

/** 
 *
 * @author  Michael Bouschen
 */
public class QueryErrorTest
  extends NegativeTest
{
    public QueryErrorTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("queryError");
    }
    
    // ==========================================================================
    // Test methods
    // ==========================================================================
    
    /**
     * Testcase: missing candidate class
     */
    public void queryError001()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_MissingCandidateClass"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery();
            query.setFilter("empid == 1");
            Object result = query.execute();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: missing candidate class in query tree
     */
    public void queryError002()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_MissingCandidateClass"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            // create query tree
            QueryTree qt = newQueryTree();
            Expression field = qt.newIdentifier("empid");
            Expression constant = qt.newConstant(new Integer(1));
            Expression equals = qt.newEquals(field, constant);
            qt.setFilter(equals);

            Query query = pm.newQuery(qt);
            Object result = query.execute();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: directly use deserialized query w/o binding to a new PersistenceManager
     */
    public void queryError003()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_UnboundQuery"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3");
        
        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject (query);
        oout.flush ();
        byte[] bytes = bout.toByteArray();
        oout.close ();

        // deserialize query object
        ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
        ObjectInputStream oin = new ObjectInputStream (bin);
        query = (Query)oin.readObject ();
        oin.close ();

        try
        {
            Object result = query.execute();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: compiled query is null
     */
    public void queryError004()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_NullQueryInstance"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query compiled = null;
            Query query = pm.newQuery(compiled);
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid compiled query (compiled query must have the class QueryImpl)
     */
    public void queryError005()
        throws Exception
    {
        String expectedMsg = 
            msg.msg("EXC_InvalidCompiledQuery", "java.lang.Integer"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Object compiled = new Integer(1);
            Query query = pm.newQuery(compiled);
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
}
