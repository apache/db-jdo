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
package org.apache.jdo.tck.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.runner.TestCollector;

/**
 * This class implements a test suite including all test cases as specified
 * in a testlist file. The user can specify the name of the testlist by
 * setting the system property testlist. The default is
 * JDOTCKTestCases.list. The swing GUI uses this class when browsing all
 * test classes.
 * 
 * @author Michael Bouschen
 */
public class TestListSuite
    extends TestSuite
    implements TestCollector
{
    /** Name of the system property to specify the list of test class names. */
    public static final String TESTLIST_PROPERTY = "testlist";

    /** Default of the system property testlist. */
    public static final String TESTLIST_DEFAULT = "JDOTCKTestCases.list";

    /** 
     * No arg constructor used by the swing GUI when browsing the test
     * classes via the TestCollector interface.
     */
    public TestListSuite() { }
    
    /** 
     * Creates a test suite with the specified name and reads the test
     * class names fom a file specified by a system property.
     */
    public TestListSuite(String name) {
        setName(name);
        addTestClasses(getTestClassNames());
    }
    
    /** 
     * Creates a test suite with the specified name including the test
     * classes from the specified list.
     */
    public TestListSuite(String name, List classNames) {
    	setName(name);
        addTestClasses(classNames);
    }
    
    /** Runs this test suite in batch mode. */
    public static void main(String args[]) {
        BatchTestRunner.run(suite());
    }

    /** */
	public static Test suite() {
        return new TestListSuite("JDOTCK tests");
    }

    /** 
     * Adds all test classes from the specified list to this test
     * suite. 
     */ 
    private void addTestClasses(List classNames) {
    	for (Iterator i = classNames.iterator(); i.hasNext();) {
    		String className = (String)i.next();
    		try {
    			addTestSuite(Class.forName(className));
    		}
    		catch (ClassNotFoundException ex) {
    			System.out.println("Cannot find test class " + className);
    		}
        }
    }
    
	/** 
     * Returns an enumeration of Strings with qualified class names. 
     * Method defined in the JUnit interface TestCollector.
     */
	public Enumeration collectTests() {
        return Collections.enumeration(getTestClassNames());
    }

    /** 
     * Returns a list of fully qualified test class names. The method
     * checks the system property testlist for the name of the test list
     * (default is JDOTCKTestCases.list). Each line of the file is expected
     * to be the fully qualified class name of a test class. Line starting
     * with a # are skipped.
     */
    protected List getTestClassNames() {
        // get the name of the testlist file as system property 
        String testlist = System.getProperty(TESTLIST_PROPERTY, TESTLIST_DEFAULT);
        List testClassNames = new ArrayList();
        try {
            BufferedReader reader = getTestListReader(testlist);
            for (String line = reader.readLine(); 
                 line != null; 
                 line = reader.readLine()) {
                line = line.trim();
                if (isTestClassName(line)) {
                    testClassNames.add(line);
                }
            }
            reader.close();
        }
        catch (IOException ex) {
            System.out.println("Problems reading testlist " + testlist + ": " + ex);
        }
        return testClassNames;
    }
    
    /** Returns a BufferedReader for the specified testlist filename. */
    protected BufferedReader getTestListReader(final String testlist) 
        throws FileNotFoundException {
        try {
            return (BufferedReader)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws IOException {
                        return new BufferedReader(new FileReader(testlist));
                    }
                });
        }
        catch (PrivilegedActionException ex) {
            // unwrap IOException
            throw (FileNotFoundException)ex.getException();
        } 
    }

    /** Returns true if the specified String defines a test class name. */
    protected boolean isTestClassName(String line) {
        return (line != null) && 
               (line.length() > 0) &&
               !line.trim().startsWith("#");
    }
}

