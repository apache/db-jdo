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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestFailure;
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
    private static final DecimalFormat THREE_DIGITS_FORMATTER = new DecimalFormat("000");

    /** The stream to delegate the output. */
    private ConsoleFileOutput consoleFileOutput;
    
    /** The time elapsed to run a test suite. */
    private long runtime;
    
    /** */
    public BatchResultPrinter(ConsoleFileOutput consoleFileOutput) {
        this(new PrintStream(consoleFileOutput));
        this.consoleFileOutput = consoleFileOutput;
    }
        
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
        getWriter().print("RUN " + testName + '\t');
        getWriter().flush();
    }
        
    /**
     * @see ResultPrinter#elapsedTimeAsString(long)
     */
    protected String elapsedTimeAsString(long runTime) {
        return THREE_DIGITS_FORMATTER.format((double)runTime/1000);
    }

    /** */
    protected void printHeader(long runTime) {
        this.runtime = runTime;
        getWriter().println("Description: " + System.getProperty("jdo.tck.description"));    
        getWriter().println("Time: "+elapsedTimeAsString(runTime));
    }
        
    /** */
    protected void printFooter(TestResult result) {
        String message = null;
        if (this.consoleFileOutput != null) { 
            message = getResultMessage(result, this.consoleFileOutput);
            String directory = this.consoleFileOutput.getDirectory();
            ResultSummary.appendTCKResultMessage(directory, message);
            ResultSummary.save(directory, result);
        } else {
            message = getResultMessage(result);
        }
        
        if (!result.wasSuccessful()) {
            getWriter().println("FAILURES!!!");
            printErrorSummary(result);
        }
        
        getWriter().println(message);
        getWriter().println("Excluded tests: " + System.getProperty("jdo.tck.exclude"));
    }
        
    // helper method
    
    /**
     * Returns the result message for the given result instance and
     * the given console file output instance.
     * @param result the result instance
     * @param consoleFileOutput the console file output instance
     * @return the result message
     */
    private String getResultMessage(TestResult result, ConsoleFileOutput consoleFileOutput) {
        String message = this.consoleFileOutput.getFileName() + ':';
        message += System.getProperty("line.separator") + "    ";
        message += getResultMessage(result);
        return message;
    }
        
    /**
     * Returns the result message for the given result instance.
     * @param result the result instance
     * @return the result message
     */
    private String getResultMessage(TestResult result) {
        boolean success = result.wasSuccessful();
        String message = success ? "OK " : "** ";
        message += "Tests run: " + THREE_DIGITS_FORMATTER.format(result.runCount()) + 
                   ", Time: "+elapsedTimeAsString(this.runtime)+" seconds.";
        if (!success) {
            message += " Failures: "+result.failureCount() + 
                       ", Errors: "+result.errorCount();
        }
        return message;
    }
        
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
        
    private void printErrorSummary(TestResult result) {
        Object[] array = getSortedArrayOfErrorSummaryEntries(result);
        if (array.length>0) {
            getWriter().println("Error summary:");
            for (int i=0; i<array.length; i++) {
                getWriter().println(array[i]);
            }
        }
    }
    
    private static Object[] getSortedArrayOfErrorSummaryEntries(TestResult result) {
        Map map = new HashMap();
        for (Enumeration e=result.errors(); e.hasMoreElements(); ) {
            TestFailure testFailure = (TestFailure) e.nextElement();
            Throwable t = testFailure.thrownException();
            String message = getRootCause(t).toString();
            ErrorSummaryEntry errorSummaryEntry = (ErrorSummaryEntry) map.get(message);
            if (errorSummaryEntry==null ) {
                errorSummaryEntry = new ErrorSummaryEntry(t);
                map.put(message, errorSummaryEntry);
            }
            errorSummaryEntry.count++;   
        }
        
        Object[] array = map.values().toArray();
        Arrays.sort(array);
        return array;
    }
    
    private static Throwable getRootCause(Throwable t) {
        while (t.getCause()!=null) {
            t = t.getCause();
        }
        return t;
    }
    
    private static class ErrorSummaryEntry implements Comparable {
        private int count = 0;
        private Throwable t;
        
        private ErrorSummaryEntry(Throwable t) {
            this.t = t;
        }
        
        public boolean equals(Object o) {
            return compareTo(o)==0;
        }
        
        public int hashCode() {
            return this.count;
        }
        
        public int compareTo(Object o) {
            int result = this.count - ((ErrorSummaryEntry)o).count;
            if (result==0) {
                String message1 = getRootCause().toString();
                String message2 = 
                    ((ErrorSummaryEntry)o).getRootCause().toString();
                result = message1.compareTo(message2);
            }
            return result;
        }
        
        public String toString() {
            StringBuffer buffer = 
                new StringBuffer(THREE_DIGITS_FORMATTER.format(count));
            buffer.append(" error" );
            if (this.count!=1) {
                buffer.append("s: ");
            } else {
                buffer.append(":  ");
            }
            buffer.append(getRootCause());
            return buffer.toString();
        }
        
        private Throwable getRootCause() {
            return BatchResultPrinter.getRootCause(this.t);
        }
    }
}
