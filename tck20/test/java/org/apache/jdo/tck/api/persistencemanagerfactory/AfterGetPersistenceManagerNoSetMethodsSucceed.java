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


package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> After GetPersistenceManager No Set Methods Succeed
 *<BR>
 *<B>Keywords:</B> 
 *<BR>
 *<B>Assertion ID:</B> A11.3-3.
 *<BR>
 *<B>Assertion Description: </B>
After the first use of <code>PersistenceManagerFactory.getPersistenceManager()</code>,
none of the <code>set</code> methods will succeed.

 */

public class AfterGetPersistenceManagerNoSetMethodsSucceed extends JDO_Test {

    private String username;
    private String password;
    private static final String USERNAME_PROPERTY = "javax.jdo.option.ConnectionUserName";
    private static final String PASSWORD_PROPERTY = "javax.jdo.option.ConnectionPassword";

    private Class[] stringParameters = null;
    private Class[] booleanParameters = null;
    private Object[] stringParameter = null;
    private Object[] booleanParameter = null;
    private SetProperty[] setMethods = null;
    private GetProperty[] getMethods = null;

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.3-3 (AfterGetPersistenceManagerNoSetMethodsSucceed) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AfterGetPersistenceManagerNoSetMethodsSucceed.class);
    }

    /** */
    public AfterGetPersistenceManagerNoSetMethodsSucceed() {
		super();
        initVariables();
    }

    /** */
    public void initVariables() {
        stringParameters = new Class[]{String.class};
        booleanParameters = new Class[]{boolean.class};
        stringParameter = new Object[]{"Nobody knows the trouble"};
        booleanParameter = new Object[]{Boolean.FALSE};

        setMethods = new SetProperty[] {
            new SetProperty("setConnectionDriverName", stringParameters, stringParameter),
            new SetProperty("setConnectionFactoryName", stringParameters, stringParameter),
            new SetProperty("setConnectionFactory2Name", stringParameters, stringParameter),
            new SetProperty("setConnectionURL", stringParameters, stringParameter),
            new SetProperty("setConnectionUserName", stringParameters, stringParameter),
            new SetProperty("setConnectionPassword", stringParameters, stringParameter),
            new SetProperty("setIgnoreCache", booleanParameters, booleanParameter),
            new SetProperty("setMultithreaded", booleanParameters, booleanParameter),
            new SetProperty("setNontransactionalRead", booleanParameters, booleanParameter),
            new SetProperty("setNontransactionalWrite", booleanParameters, booleanParameter),
            new SetProperty("setOptimistic", booleanParameters, booleanParameter),
            new SetProperty("setRestoreValues", booleanParameters, booleanParameter),
            new SetProperty("setRetainValues", booleanParameters, booleanParameter)
        };

        getMethods = new GetProperty[] {
            new GetProperty("getConnectionDriverName"),
            new GetProperty("getConnectionFactoryName"),
            new GetProperty("getConnectionFactory2Name"),
            new GetProperty("getConnectionURL"),
            new GetProperty("getConnectionUserName"),
            new GetProperty("getIgnoreCache"),
            new GetProperty("getMultithreaded"),
            new GetProperty("getNontransactionalRead"),
            new GetProperty("getNontransactionalWrite"),
            new GetProperty("getOptimistic"),
            new GetProperty("getRestoreValues"),
            new GetProperty("getRetainValues")
        };
    }

    /** */
    public void testGetPersistenceManagerWithNoParametes() {
        runTest(false);
    }

    /** */
    public void testGetPersistenceManagerWithParameters() {
        Properties props = loadProperties(PMFProperties);
        username = props.getProperty(USERNAME_PROPERTY);  
        password = props.getProperty(PASSWORD_PROPERTY);  
        runTest(true);
    }

	/** */
    public void runTest(boolean bUserAndPasswd) {       
        pmf = getPMF();
        if (!bUserAndPasswd)
            pm = getPM();
        else
            pm = getPMF().getPersistenceManager(username,password);	
	
        // each set method should throw an exception
        Collection setCollection = Arrays.asList(setMethods);
        for (Iterator it = setCollection.iterator(); it.hasNext();) {
            SetProperty sp = (SetProperty)it.next();
            String where = sp.getMethodName();
            try {
                sp.execute(pmf);
                fail(ASSERTION_FAILED,
                     "pmf method " + where + 
                     " should throw JDOUserException when called after getPersistenceManager");
            } catch (JDOUserException ex) {
                if (debug)
                    logger.debug("Caught expected exception " + ex.toString() + " from " + where);
            }
        }
        // each get method should succeed
        Collection getCollection = Arrays.asList(getMethods);
        for (Iterator it = getCollection.iterator(); it.hasNext();) {
            GetProperty gp = (GetProperty)it.next();
            String where = gp.getMethodName();
            try {
                gp.execute(pmf);
            } catch (JDOUserException ex) {
                fail(ASSERTION_FAILED,
                     "Caught unexpected exception " + ex.toString() + " from " + where);
            }
        }
    }
   
    /** */
    static class SetProperty {
        
        java.lang.reflect.Method method;
        String methodName;
        Class[] parameters;
        Object[] parameter;
       
        SetProperty(String methodName, Class[] parameters, Object[] parameter) {
            this.methodName = methodName;
            this.parameters = parameters;
            this.parameter = parameter;
            try {
                method = PersistenceManagerFactory.class.getMethod(methodName, parameters);
            } catch (NoSuchMethodException ex) {
                throw new JDOFatalInternalException("Method not defined: " + methodName);
            }
        }
        void execute(PersistenceManagerFactory pmf) {
            try {
                method.invoke(pmf, parameter);
            } catch (IllegalAccessException ex) {
                throw new JDOFatalInternalException("IllegalAccessException", ex);
            } catch (java.lang.reflect.InvocationTargetException ex) {
                throw (JDOException)ex.getTargetException();
            }
        }
       
        String getMethodName() {
            return methodName;
        }
    }
   
    /** */
    static class GetProperty {
       
        java.lang.reflect.Method method;
        String methodName;
       
        GetProperty(String methodName) {
            this.methodName = methodName;
            try {
                method = PersistenceManagerFactory.class.getMethod(methodName, null);
            } catch (NoSuchMethodException ex) {
                throw new JDOFatalInternalException(
                    "Method not defined: " + methodName);
            }
        }
        void execute(PersistenceManagerFactory pmf) {
            try {
                method.invoke(pmf, null);
            } catch (IllegalAccessException ex) {
                throw new JDOFatalInternalException("IllegalAccessException", ex);
            } catch (java.lang.reflect.InvocationTargetException ex) {
                throw (JDOException)ex.getTargetException();
            }
        }
       
        String getMethodName() {
            return methodName;
        }
    }
}
