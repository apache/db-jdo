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
 
package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Arrays;
import java.util.Collection;

import java.lang.reflect.Method;

import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>AfterCloseSetMethodsThrowException  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.4-9A.
 *<BR>
 *<B>Assertion Description: </B>
 * If a set method is called after close, then JDOUserException is thrown.
 */

/* 
 * Revision History
 * ================
 * Author :   Craig Russell
 * Date   :  05/16/03
 *
 */

public class AfterCloseSetMethodsThrowException extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.4-9A (AfterCloseSetMethodsThrowException) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AfterCloseSetMethodsThrowException.class);
    }

    /** */
    @SuppressWarnings("unchecked")
    public void test() {
        Class<String>[] stringParameters = new Class[]{String.class};
        Class<Boolean>[] booleanParameters = new Class[]{boolean.class};
        Class<Object>[] objectParameters = new Class[]{Object.class};
        Class<Integer>[] integerParameters = new Class[]{Integer.class};
        Object[] stringParameter = new Object[]{"Nobody knows the trouble"};
        Object[] booleanParameter = new Object[]{Boolean.FALSE};
        Object[] objectParameter = new Object[]{null};
        Object[] integerParameter = new Object[]{Integer.valueOf(0)};
        
        SetProperty[] setMethods = new SetProperty[] {
            new SetProperty("setConnectionDriverName", stringParameters, stringParameter),
            new SetProperty("setConnectionFactory", objectParameters, objectParameter),
            new SetProperty("setConnectionFactory2", objectParameters, objectParameter),
            new SetProperty("setConnectionFactoryName", stringParameters, stringParameter),
            new SetProperty("setConnectionFactory2Name", stringParameters, stringParameter),
            new SetProperty("setConnectionPassword", stringParameters, stringParameter),
            new SetProperty("setConnectionURL", stringParameters, stringParameter),
            new SetProperty("setConnectionUserName", stringParameters, stringParameter),
            new SetProperty("setCopyOnAttach", booleanParameters, booleanParameter),
            new SetProperty("setDetachAllOnCommit", booleanParameters, booleanParameter),
            new SetProperty("setIgnoreCache", booleanParameters, booleanParameter),
            new SetProperty("setMapping", stringParameters, stringParameter),
            new SetProperty("setMultithreaded", booleanParameters, booleanParameter),
            new SetProperty("setName", stringParameters, stringParameter),
            new SetProperty("setNontransactionalRead", booleanParameters, booleanParameter),
            new SetProperty("setNontransactionalWrite", booleanParameters, booleanParameter),
            new SetProperty("setOptimistic", booleanParameters, booleanParameter),
            new SetProperty("setPersistenceUnitName", stringParameters, stringParameter),
            new SetProperty("setReadOnly", booleanParameters, booleanParameter),
            new SetProperty("setRestoreValues", booleanParameters, booleanParameter),
            new SetProperty("setRetainValues", booleanParameters, booleanParameter),
            new SetProperty("setServerTimeZoneID", stringParameters, stringParameter),
            new SetProperty("setTransactionIsolationLevel", stringParameters, stringParameter),
            new SetProperty("setTransactionType", stringParameters, stringParameter)
        };

        GetProperty[] getMethods = new GetProperty[] {
            new GetProperty("getConnectionDriverName"),
            new GetProperty("getConnectionFactory"),
            new GetProperty("getConnectionFactory2"),
            new GetProperty("getConnectionFactoryName"),
            new GetProperty("getConnectionFactory2Name"),
            new GetProperty("getConnectionURL"),
            new GetProperty("getConnectionUserName"),
            new GetProperty("getCopyOnAttach"),
            new GetProperty("getDataStoreCache"),
            new GetProperty("getDetachAllOnCommit"),
            new GetProperty("getFetchGroups"),
            new GetProperty("getIgnoreCache"),
            new GetProperty("getMapping"),
            new GetProperty("getMultithreaded"),
            new GetProperty("getName"),
            new GetProperty("getNontransactionalRead"),
            new GetProperty("getNontransactionalWrite"),
            new GetProperty("getOptimistic"),
            new GetProperty("getPersistenceUnitName"),
            new GetProperty("getProperties"),
            new GetProperty("getReadOnly"),
            new GetProperty("getRestoreValues"),
            new GetProperty("getRetainValues"),
            new GetProperty("getServerTimeZoneID"),
            new GetProperty("getTransactionIsolationLevel"),
            new GetProperty("getTransactionType")
        };

        pmf = getPMF();
        closePMF(pmf); // don't use closePMF() because that sets pmf to null
        // each set method should throw an exception
        Collection<SetProperty> setCollection = Arrays.asList(setMethods);
        for (SetProperty sp : setCollection) {
            String where = sp.getMethodName();
            try {
                sp.execute(pmf);
                fail(ASSERTION_FAILED,
                        "pmf method " + where + " should throw JDOUserException when called for closed pmf");
            } catch (JDOUserException ex) {
                if (debug)
                    logger.debug("Caught expected exception " + ex +
                            " from " + where);
            } catch (Exception ex) {
                fail(ASSERTION_FAILED,
                        "Caught unexpected exception " + ex + " from " + where);
            }
        }
        // each get method should succeed
        Collection<GetProperty> getCollection = Arrays.asList(getMethods);
        for (GetProperty gp : getCollection) {
            String where = gp.getMethodName();
            try {
                gp.execute(pmf);
            } catch (Exception ex) {
                fail(ASSERTION_FAILED,
                        "Caught unexpected exception " + ex + " from " +
                                where);
            }
        }
    }
   
    /** */
    static class SetProperty {
        
        private final Method method;
        private final String methodName;
        private final Class<?>[] parameters;
        private final Object[] parameter;
       
        SetProperty(String methodName, Class<?>[] parameters, Object[] parameter) {
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
                throw (RuntimeException)ex.getTargetException();
            }
        }
       
        String getMethodName() {
            return methodName;
        }
    }
   
    /** */
    static class GetProperty {
       
        private final Method method;
        private final String methodName;
       
        GetProperty(String methodName) {
            this.methodName = methodName;
            try {
                method = PersistenceManagerFactory.class.getMethod(methodName,
                        (Class<?>[])null);
            } catch (NoSuchMethodException ex) {
                throw new JDOFatalInternalException("Method not defined: " + methodName);
            }
        }
        void execute(PersistenceManagerFactory pmf) {
            try {
                method.invoke(pmf, (Object[])null);
            } catch (IllegalAccessException ex) {
                throw new JDOFatalInternalException("IllegalAccessException", ex);
            } catch (java.lang.reflect.InvocationTargetException ex) {
                throw (RuntimeException)ex.getTargetException();
            }
        }
       
        String getMethodName() {
            return methodName;
        }
    }
}
