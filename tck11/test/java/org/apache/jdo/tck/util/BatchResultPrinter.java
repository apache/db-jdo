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

import java.io.PrintStream;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.ResultPrinter;

/**
 * Default result printer implementation for running tests in batch mode.
 * 
 * @author Michael Bouschen
 */
public class BatchResultPrinter
    extends ResultPrinter
{
    /** */
    public BatchResultPrinter(PrintStream writer) {
        super(writer);
    }
        
    /** Called in case of a test error. */
    public void addError(Test test, Throwable t) {
        getWriter().print("   ERROR");
    }
        
    /** Called in case of a test failure. */ 
    public void addFailure(Test test, AssertionFailedError t) {
        getWriter().print("   FAILURE");
    }
        
    /** Called when a test case is finished. */
    public void endTest(Test test) {
        getWriter().println();
    }
        
    /** Called when a test case is started. */
    public void startTest(Test test) {
        String testName;
        if (test instanceof TestCase) {
            testName = getClassBaseName(test) + "." + ((TestCase)test).getName();
        }
        else {
            testName = test.toString();
        }
        getWriter().print("RUN " + testName);
    }
        
    /** */
    protected void printHeader(long runTime) {
        getWriter().println("Time: "+elapsedTimeAsString(runTime));
    }
        
    /** */
    protected void printFooter(TestResult result) {
        if (result.wasSuccessful()) {
            getWriter().print("OK");
            getWriter().println (" (" + result.runCount() + " test" + (result.runCount() == 1 ? "": "s") + ")");
                
        } else {
            getWriter().println("FAILURES!!!");
            getWriter().println("Tests run: "+result.runCount()+ 
                                ",  Failures: "+result.failureCount()+
                                ",  Errors: "+result.errorCount());
        }
    }
        
    // helper method
        
    /** 
     * @return Name of the class of the given object without package prefix
     */
    private String getClassBaseName(Object obj) {
        if (obj == null) return null;
        String className = obj.getClass().getName();
        int index = className.lastIndexOf('.');
        if (index != -1) {
            className = className.substring(index + 1);
        }
        return className;
    }
        
}


