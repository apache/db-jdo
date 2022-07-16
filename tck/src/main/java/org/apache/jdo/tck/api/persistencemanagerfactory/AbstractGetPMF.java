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

import java.lang.reflect.Method;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jdo.JDOFatalUserException;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;

/**
 * The abstract super class for all GetPMF test cases. 
 * @author Michael Watzek
 */

abstract class AbstractGetPMF extends JDO_Test {
    
    /** 
     * Used as the PMF name in positive tests.
     * The variable is set to test/conf/jdori.properties.
     * This file contains valid JDO properties.
     */
    protected static final String validPropertiesFile = PMFProperties;
    
    /** 
     * Used as the PMF name in negative tests.
     * The variable is set to test/conf/logging.properties.
     * This file does not contain valid JDO properties.
     */
/*    protected static final String invalidPropertiesFile =
        validPropertiesFile.substring(0, validPropertiesFile.lastIndexOf(File.separatorChar)+1) +
        "logging.properties";
*/
    protected static final String invalidPropertiesFile;
    static
    {
        if (validPropertiesFile==null)
            System.out.println ("******************************");
        invalidPropertiesFile = validPropertiesFile.substring(0, validPropertiesFile.lastIndexOf(File.separatorChar)+1) +
        "logging.properties";
    }

    /**
     * Used as the PMF name in positive JNDI tests.
     * The variable is set to jdori.properties.
     * This resource contains valid JDO properties.
     */
    protected static final String jndiName =
        validPropertiesFile.substring(0, validPropertiesFile.lastIndexOf(File.separatorChar)+1) +
        "pmf.ser";
    
    /**
     * Removing the path prefix from argument <code>name</code>.
     * @param name the name
     * @return argument <code>name</code> removed by the path prefix.
     */
    protected String removePathPrefix(String name) {
        int index = name.lastIndexOf(File.separatorChar);
        if (index!=-1) {
            name = name.substring(index+1);
        }
        return name;
    }

    protected void makePersistent() {
        addTearDownClass(PCPoint.class);
        pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        PCPoint comp = new PCPoint(1, 2);
        pm.makePersistent(comp);
        tx.commit();
    }

    /**
     * Returns pmf instance for the given name.
     * Subclasses may use argument <code>name</code> as file name,
     * resource name etc.
     * @param name the name
     * @return the pmf instance
     */
    protected abstract PersistenceManagerFactory getPMF(String name);

    /**
     * This method creates a PMF calling 
     * method AbstractGetPMF#getPMF(name).
     * It passes an name of an invalid properties file as parameter.
     * The method expects the PMF creation to fail.
     * @param assertionMessage the assertion message if the test fails. 
     */
    protected void checkGetPMFWithInvalidProperties(String assertionMessage) {
        try {
            pmf = getPMF(invalidPropertiesFile);
            fail(assertionMessage);
        } catch (JDOFatalUserException e) {
            // expected exception
            if (debug)
                logger.debug("caught expected exception " + e);
        }
    }
    
    /**
     * This method creates a PMF calling 
     * method AbstractGetPMF#getPMF(name).
     * It passes an name of a valid properties file as parameter.
     * The method expects the PMF creation to succeed.
     */
    protected void checkGetPMFWithValidProperties() {
        pmf = getPMF(validPropertiesFile);
        verifyProperties(pmf, loadProperties(validPropertiesFile));
        makePersistent();
    }

    /**
     * Verify that the Properties are correctly set in the PMF.
     * @param pmf the PersistenceManagerFactory
     * @param props the Properties
     */
    protected void verifyProperties(PersistenceManagerFactory pmf,
            Properties props) {
        Object[] noArgs = new Object[]{};
        String javaxjdooption = "javax.jdo.option.";
        Class<?> pmfclass = pmf.getClass();
        Set<Map.Entry<Object, Object>> entries = props.entrySet();
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<Object, Object> entry : entries) {
            String key = (String) entry.getKey();
            if (key.equals("javax.jdo.option.ConnectionPassword")) {
                continue;
            }
            if (key.equals("javax.jdo.option.ConnectionUserName")) {
                continue;
            }
            String expected = (String) entry.getValue();
            if (key.startsWith(javaxjdooption)) {
                String optionName = key.substring(javaxjdooption.length());
                Method getMethod = getGetMethod(pmfclass, optionName);
                Object actual = getValue(getMethod, pmf, noArgs);
                if (actual == null) {
                    buffer.append("\n");
                    buffer.append("Key ");
                    buffer.append(key);
                    buffer.append(" was null.");
                    continue;
                }
                String actualString = actual.toString();
                if (!expected.equals(actualString)) {
                    buffer.append("\n");
                    buffer.append("Key ");
                    buffer.append(key);
                    buffer.append(" expected: \"");
                    buffer.append(expected);
                    buffer.append("\" actual: \"");
                    buffer.append(actual);
                    buffer.append("\".");
                }
            }
        }
        if (buffer.length() != 0) {
            fail(buffer.toString());
        }
    }
    
    /** Get the "get" method corresponding to the option name.
     */
    Method getGetMethod(Class<?> cls, String optionName) {
        try {
            return cls.getMethod("get" + optionName);
        } catch (Exception ex) {
            fail("Unexpected exception thrown from getMethod on PMF class with option name" + 
                    optionName);
            return null;
        }
    }
    
    /** 
     * Get the result of executing the Method.
     */
    Object getValue(Method method, Object obj, Object[] args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception ex) {
            fail("Unexpected exception executing method " + 
                    method.getName() + ".");
            return null;
        }
    }
}
