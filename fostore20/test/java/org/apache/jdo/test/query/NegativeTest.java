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
 * NegativeTest.java
 *
 * Created on March 31, 2000
 */

package org.apache.jdo.test.query;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.impl.jdoql.tree.Tree;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.util.I18NHelper;

/** 
 *
 * @author  Michael Bouschen
 */
public abstract class NegativeTest
    implements QueryTest
{
    /** The persistence manager factory. */
    protected PersistenceManagerFactory pmf;

    /** The persistence manager. */
    protected PersistenceManager pm;

    /** The log stream. */
    protected PrintStream log;
    
    /** I18N support */
    protected final static I18NHelper msg = 
        I18NHelper.getInstance("org.apache.jdo.impl.jdoql.Bundle"); //NOI18N

    /**
     *
     */
    public NegativeTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        this.pmf = pmf;
        this.pm = pmf.getPersistenceManager();
        this.log = log;
    }
    
    /**
     * Create a new query tree node
     */
    protected QueryTree newQueryTree()
    {
        if (pmf instanceof FOStorePMF)
            return ((FOStorePMF)pmf).newQueryTree();
        else
            return new Tree();
    }
    
    /**
     * Runs all test method provided by this class.
     */
    public boolean runAll()
    {
        boolean ok = true;
        Method[] methods = this.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method m = methods[i];
            if (isTestMethod(m))
                if (!runTest(m))
                    ok = false;
        }
        return ok;
    }

    /**
     * Runs all test case specified in tests
     */
    public boolean runTests(Vector tests)
    {
        boolean ok = true;
        for (Iterator i = tests.iterator(); i.hasNext();)
        {
            String methodName = (String)i.next();
            try
            {
                if (isTestMethodName(methodName))
                {
                    Method m = this.getClass().getMethod(methodName, new Class[0]);
                    if (!runTest(m))
                        ok = false;
                }
            }
            catch (NoSuchMethodException ex)
            {
                // this test class does not support the test method => skip
            }
        }
        return ok;
    }

    /**
     * Runs test method specified as java.lang.reflect.Method argument.
     * Method must be a method taking no arguments.
     */
    protected boolean runTest(Method method)
    {
        String methodName = method.getName();
        try
        {
            method.invoke(this, new Object[0]);
            log.println(methodName + ": OK ");
        }
        catch (InvocationTargetException ex)
        {
            Throwable target = ex.getTargetException();
            log.println(methodName + ": " + target.getMessage());
            log.println("  stack trace:");
            target.printStackTrace(log);
            return false;
        }
        catch (Exception ex)
        {
            log.println(methodName + ": problems invoking test method " + methodName + " " + ex);
            ex.printStackTrace(log);
            return false;
        }
        finally
        {
            // close transaction, if it is left open
            Transaction tx = pm.currentTransaction();
            if (tx.isActive())
                tx.rollback();
        }
        return true;
    }

    /**
     * Checks the specified method being a test method
     */
    protected boolean isTestMethod(Method method)
    {
       return (isTestMethodName(method.getName()) &&
               method.getReturnType().equals(void.class) && 
               method.getParameterTypes().length == 0);
    }
    
    /**
     * Checks the specified argument being the name of a test method
     */
    protected abstract boolean isTestMethodName(String methodName);

    /**
     * Thows an exception that indicates that the test case expected to catch a JDOQueryException
     * which did not occur.
     */
    protected void checkMissingException(Class expectedException, String expectedMsg)
        throws Exception
    {
        throw new Exception("Missing " + expectedException.getName() + "(" + expectedMsg + ")");
    }
    
    /**
     * Checks whether the JDOExecption has the expected message
     */
    protected void checkException(Exception ex, Class expectedException, String expectedMsg)
        throws Exception
    {
        String lf = System.getProperty("line.separator");
        if (!ex.getClass().equals(expectedException))
        {
            throw new Exception("Wrong exception of " + ex.getClass() + lf +
                                "  expected exception of " + expectedException);
        }
        
        String msg = ex.getMessage();
        if (!msg.startsWith(expectedMsg))
        {
            throw new Exception("Wrong error message: " + msg + lf + "  expected: " + expectedMsg);
        }
    }

    /**
     * Checks whether the JDOExecption has the expected message
     */
    protected void checkJDOException(JDOException ex, Class expectedException, String expectedMsg)
        throws Exception
    {
        String lf = System.getProperty("line.separator");
        if (!ex.getClass().equals(expectedException))
        {
            throw new Exception("Wrong exception of " + ex.getClass() + lf +
                                "  expected exception of " + expectedException);
        }
        
        String msg = ex.getMessage();
        if (!msg.startsWith(expectedMsg))
        {
            throw new Exception("Wrong error message: " + msg + lf + "  expected: " + expectedMsg);
        }
    }

}
