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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

/**
 * TestRunner class for running a single test or a test suite in batch
 * mode. The format of the test output is specified by the result printer
 * class. The main method sets an exit code according to the test result:
 * <ul>
 * <li><code>0</code>: success
 * <li><code>1</code>: failure, the test shows an unexpected behavior
 * <li><code>2</code>: exception, the test throws an unhandled excption 
 * </ul>
 * 
 * @author Michael Bouschen
 */
public class BatchTestRunner
    extends TestRunner
{
    /** Name of the system property to specify the result printer class. */
    public static final String RESULTPRINTER_PROPERTY = "ResultPrinterClass"; 
    
    /** Default of the system property ResultPrinterClass. */
    public static final String RESULTPRINTER_DEFAULT = BatchResultPrinter.class.getName();

    /** Redirect System.out and System.err to an ConsoleFileOutput instance. */
    static {
        if (!Boolean.getBoolean("noLogFile")) {
            PrintStream printStream = new PrintStream(new ConsoleFileOutput());
            System.setErr(printStream);
            System.setOut(printStream);
        }
    }

    /** 
     * Constructor. 
     * It creates a result printer instance based on the system property
     * and delegates to the constructor taking a result printer argument. 
     */
    public BatchTestRunner() {
        super();
        setPrinter(getResultPrinter());
    }
    
    /**  
     * Constructor. Uses the specified resultPrinter to format the test result.
     */
    public BatchTestRunner(ResultPrinter resultPrinter) {
        super(resultPrinter);
    }

    /** Runs all test methods from the specified class. */
    public static void run(Class clazz) {
        run(new TestSuite(clazz));
    }
    
    /** Runs the specified test. */
    public static TestResult run(Test test) {
        return new BatchTestRunner().doRun(test);
    }

    /** Runs the specified test and waits until the user types RETURN. */
    public static void runAndWait(Test suite) {
        new BatchTestRunner().doRun(suite, true);
    }

    /** 
     * Runs in batch mode and sets an exit code. If the specified String
     * array includes a single fully qualified class name, this test class
     * is executed. If it is empty it runs the TestListSuite.
     */
    public static void main(String args[]) {
        try {
            TestResult r = new BatchTestRunner().start(args);
            if (!r.wasSuccessful()) 
                System.exit(FAILURE_EXIT);
            System.exit(SUCCESS_EXIT);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(EXCEPTION_EXIT);
        }
    }

    /** */
    public TestResult start(String[] args) {
        Test suite = null;
        if ((args == null) || args.length == 0) {
            suite = getTest(TestListSuite.class.getName());
        }
        else if (args.length == 1) {
            suite = getTest(args[0]);
        }
        else {
            suite = new TestListSuite("JDO TCK", Arrays.asList(args));
        }
        return doRun(suite);
    }

    /** 
     * Returns a result printer instance. The system property
     * ResultPrinterClass specifies the class of the returned instanec. The
     * class must extend junit.textui.ResultPrinter.
     */
    protected ResultPrinter getResultPrinter() {
        String className =  System.getProperty(RESULTPRINTER_PROPERTY);
        if (className != null) {
            className = className.trim();
            if (className.length() != 0) {
                String msg = null;
                try {
                    // get class instance
                    Class clazz = Class.forName(className);
                    // constructor taking PrintStream arg
                    Constructor ctor = clazz.getConstructor(
                        new Class[] { PrintStream.class } );
                    // create instance
                    return (ResultPrinter)ctor.newInstance(
                        new Object[] { System.out });
                }
                catch (ClassNotFoundException ex) {
                    // specified ResultPrinter class not 
                    msg = "Cannot find specified result printer class " + 
                        className + ".";
                }
                catch (NoSuchMethodException ex) {
                    msg = "Class " + className + 
                        " does not provide constructor taking a PrintStream.";
                }
                catch (InstantiationException ex) {
                    msg = "Class " + className + " is abstract.";
                }
                catch (IllegalAccessException ex) {
                    msg = "Constructor taking a PrintStream of class " + 
                        className + " is not accessible.";
                }
                catch (InvocationTargetException ex) {
                    msg = "Constructor call results in exception " + ex + ".";
                }

                // ResultPrinter class specified, but not avaiable
                System.out.println(msg);
                ResultPrinter printer = getDefaultResultPrinter();
                System.out.println("Using default result printer of class " + 
                                   printer.getClass().getName());
            }
        }
        
        // ResultPrinter class not specified => use default
        return getDefaultResultPrinter();
    }

    /** 
     * Returns an instance of the default result printer class
     * BatchResultPrinter.
     */
    protected ResultPrinter getDefaultResultPrinter() {
        return new BatchResultPrinter(System.out);
    }
    
    private static class ConsoleFileOutput extends OutputStream {

        private static String outDir = "logs";
        private static String fileNameSuffix = ".txt";
        private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        private PrintStream systemOut = System.out;
        private FileOutputStream fileOut;
        
        private ConsoleFileOutput() {
            String identityType =  System.getProperty("jdo.tck.identitytype");
            String db =  System.getProperty("jdo.tck.database");
            String testConfig =  System.getProperty("jdo.tck.cfg");
            if (identityType.equals("applicationidentity"))
                identityType = "app";
            else identityType = "dsid";
            testConfig = testConfig.substring(0, testConfig.indexOf('.'));
            String fileName = db + "-"
                              + identityType + "-"
                              + testConfig + "-"
                              + simpleDateFormat.format(new Date())
                              + fileNameSuffix;
            File dir = new File(outDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            
            try {
                fileOut = new FileOutputStream(new File(dir, fileName));
            } catch (FileNotFoundException e) {
                System.err.println("Cannot create log file "+fileName+". "+e);
            }
        }
        
        /* 
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            this.systemOut.write(b);
            this.fileOut.write(b);
        }
        
        /**
         * @see java.io.OutputStream#close()
         */
        public void close()  throws IOException {
            this.fileOut.close();
            this.systemOut.close();
        }
    
        /**
         * @see java.io.OutputStream#flush()
         */
        public void flush()  throws IOException {
            this.systemOut.flush();
            this.fileOut.flush();
        }        
    }
}
