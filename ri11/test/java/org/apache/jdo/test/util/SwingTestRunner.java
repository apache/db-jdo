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

package org.apache.jdo.test.util;

import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

/**
 * TestRunner class for running a single test or a test suite in GUI mode
 * using swing.
 *
 * @author Michael Bouschen
 */
public class SwingTestRunner
    extends TestRunner
{
    /** */
    private static final String TESTCOLLECTOR_KEY= "TestCollectorClass";
    
    /** */
    private static final String USER_DEFINED = "User defined test list";
    
    /** */
    private Test suite = null;

    /** */
    public SwingTestRunner() {
        super();
        setPreference(TESTCOLLECTOR_KEY, TestListSuite.class.getName());
        // Disable feature: reloading of test classes every run.
        setPreference("loading", "false");
    }

    /** */
    public static void main(String[] args) {
        new SwingTestRunner().start(args);
    }
    
    /** */
    public static void run(Class test) {
        main(new String[] { test.getName() });
    }

    /** */
    public void start(String[] args) {
        String suiteName = "JDO TCK";
        fFrame = createUI(suiteName);
        fFrame.pack();
        fFrame.setVisible(true);
        if ((args == null) || args.length == 0) {
            suite = getTest(TestListSuite.class.getName());
        }
        else if (args.length == 1) {
            suiteName = args[0];
            suite = getTest(args[0]);
        }
        else {
            suite = new TestListSuite(suiteName, Arrays.asList(args));
        }
        setSuite(suiteName);
        runTest(suite);
    }

    /** Disable feature: reloading of test classes every run. */
    protected JCheckBox createUseLoaderCheckBox() {
        JCheckBox box = super.createUseLoaderCheckBox();
        box.setVisible(false);
        return box;
    }
    
    /** */
    public void browseTestClasses() {
        TestSelector selector= new TestSelector(fFrame, new TestListSuite("JDO TCK test selection"));
        if (selector.isEmpty()) {
            JOptionPane.showMessageDialog(fFrame, "No Test Cases found.\nCheck that the configured \'TestCollector\' is supported on this platform.");
            return;
        }
        selector.show();
        List classNames = selector.getSelectedItems();
        if ((classNames != null) && (!classNames.isEmpty())) {
            if (classNames.size() == 1) {
                setSuite((String)classNames.get(0));
            }
            else {
                setSuite(USER_DEFINED);
                suite = new TestListSuite("Selected JDORI tests", classNames);
            } 
        }
    }
    
    /** */
    public Test getTest(String suiteClassName) {
        if ((suiteClassName != null ) && suiteClassName.equals(USER_DEFINED)) {
            if (suite == null) {
                // user selected 'User defines test list' from history, 
                // but there is no user selection => use all tests   
                suite = new TestListSuite("JDORI tests");
            }
            return suite;
        }
        return super.getTest(suiteClassName);
    }
        
    /**
     * Terminates the TestRunner
     */
    public void terminate() {
        fFrame.dispose();
        System.exit(0);
    }
}
