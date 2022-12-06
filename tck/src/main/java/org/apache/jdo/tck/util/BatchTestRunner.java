/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.jdo.JDOFatalException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;
import org.apache.jdo.tck.JDO_Test;

/**
 * TestRunner class for running a single test or a test suite in batch mode. The format of the test
 * output is specified by the result printer class. The main method sets an exit code according to
 * the test result:
 *
 * <ul>
 *   <li><code>0</code>: success
 *   <li><code>1</code>: failure, the test shows an unexpected behavior
 *   <li><code>2</code>: exception, the test throws an unhandled exception
 * </ul>
 *
 * @author Michael Bouschen
 */
public class BatchTestRunner extends TestRunner {
  /** Name of the system property to specify the result printer class. */
  public static final String RESULTPRINTER_PROPERTY = "ResultPrinterClass";

  /** Default of the system property ResultPrinterClass. */
  public static final String RESULTPRINTER_DEFAULT = BatchResultPrinter.class.getName();

  /** */
  public static final String LOG_DIRECTORY;

  static {
    String directory = System.getProperty("jdo.tck.log.directory");
    if (directory != null && !directory.endsWith(File.separator)) {
      directory += File.separator;
    }
    LOG_DIRECTORY = directory;
  }

  /**
   * Constructor. It creates a result printer instance based on the system property and delegates to
   * the constructor taking a result printer argument.
   */
  public BatchTestRunner() {
    super();
    setPrinter(getResultPrinter());
  }

  /**
   * Constructor. Uses the specified resultPrinter to format the test result.
   *
   * @param resultPrinter the result printer
   */
  public BatchTestRunner(ResultPrinter resultPrinter) {
    super(resultPrinter);
  }

  /**
   * Runs all test methods from the specified class.
   *
   * @param clazz class object
   */
  public static void run(Class<? extends TestCase> clazz) {
    run(new TestSuite(clazz));
  }

  /**
   * Runs the specified test or test suite
   *
   * @param test test or test suite
   * @return the test result
   */
  public static TestResult run(Test test) {
    return new BatchTestRunner().doRun(test);
  }

  /**
   * Runs the specified test and waits until the user types RETURN.
   *
   * @param suite suite
   */
  public static void runAndWait(Test suite) {
    new BatchTestRunner().doRun(suite, true);
  }

  /**
   * Runs the specified test or test suite and closes the pmf.
   *
   * @param test test or test suite
   * @return the test result
   */
  @Override
  public TestResult doRun(Test test) {
    TestResult result = null;
    try {
      result = doRun(test, false);
      JDO_Test.dumpSupportedOptions(LOG_DIRECTORY + "configuration");
    } finally {
      JDO_Test.closePMF();
    }
    return result;
  }

  /**
   * Runs in batch mode and sets an exit code. If the specified String array includes a single fully
   * qualified class name, this test class is executed.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      TestResult r = new BatchTestRunner().start(args);
      if (!r.wasSuccessful()) System.exit(FAILURE_EXIT);
      System.exit(SUCCESS_EXIT);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.exit(EXCEPTION_EXIT);
    }
  }

  /**
   * Starts the test
   *
   * @param args arguments
   * @return the test result
   */
  @Override
  public TestResult start(String[] args) {
    Test suite = null;
    if ((args == null) || args.length == 0) {
      String conf = System.getProperty("jdo.tck.cfg");
      throw new JDOFatalException(
          "Missing JDO TCK test classes for configuration '"
              + conf
              + "'. Please check the property 'jdo.tck.classes'.");
    } else if (args.length == 1) {
      suite = getTest(args[0]);
    } else {
      suite = getTestSuite(args);
    }
    return doRun(suite);
  }

  /**
   * Returns a JUnit TestSuite instance for the classes of the specified list of class names.
   *
   * @param classNames class names
   * @return the test suite
   */
  @SuppressWarnings("unchecked")
  protected TestSuite getTestSuite(String[] classNames) {
    TestSuite testSuite = new TestSuite();
    for (String className : classNames) {
      try {
        Class<? extends TestCase> clazz = (Class<? extends TestCase>) Class.forName(className);
        testSuite.addTestSuite(clazz);
      } catch (ClassNotFoundException ex) {
        System.out.println("Cannot find test class '" + className + "'.");
      }
    }
    return testSuite;
  }

  /**
   * Returns a result printer instance. The system property ResultPrinterClass specifies the class
   * of the returned instance. The class must extend junit.textui.ResultPrinter.
   *
   * @return the result printer
   */
  protected ResultPrinter getResultPrinter() {
    String className = System.getProperty(RESULTPRINTER_PROPERTY);
    if (className != null) {
      className = className.trim();
      if (className.length() != 0) {
        String msg = null;
        try {
          // get class instance
          Class<?> clazz = Class.forName(className);

          Constructor<?> ctor = null;
          OutputStream stream = null;

          // choose constructor taking ConsoleFileOutput arg
          if (!Boolean.getBoolean("no.log.file")) {
            try {
              ctor = clazz.getConstructor(ConsoleFileOutput.class);
              stream = new ConsoleFileOutput();
            } catch (NoSuchMethodException ex) {
              ctor = null;
            }
          }

          // choose constructor taking PrintStream arg
          if (ctor == null) {
            ctor = clazz.getConstructor(PrintStream.class);
            stream = System.out;
          }

          return (ResultPrinter) ctor.newInstance(new Object[] {stream});
        } catch (ClassNotFoundException ex) {
          // specified ResultPrinter class not
          msg = "Cannot find specified result printer class " + className + ".";
        } catch (NoSuchMethodException ex) {
          msg = "Class " + className + " does not provide constructor taking a PrintStream.";
        } catch (InstantiationException ex) {
          msg = "Class " + className + " is abstract.";
        } catch (IllegalAccessException ex) {
          msg = "Constructor taking a PrintStream of class " + className + " is not accessible.";
        } catch (InvocationTargetException ex) {
          msg = "Constructor call results in exception " + ex + ".";
        }

        // ResultPrinter class specified, but not available
        System.out.println(msg);
        ResultPrinter printer = getDefaultResultPrinter();
        System.out.println("Using default result printer of class " + printer.getClass().getName());
      }
    }

    // ResultPrinter class not specified => use default
    return getDefaultResultPrinter();
  }

  /**
   * Returns an instance of the default result printer class BatchResultPrinter.
   *
   * @return the default result printer
   */
  protected ResultPrinter getDefaultResultPrinter() {
    return new BatchResultPrinter(System.out);
  }

  /**
   * Returns a file name which is determined by method {@link
   * BatchTestRunner#changeFileName(String)}. The file name has suffix <code>.txt</code>.
   *
   * @return the file name
   */
  public static String getFileName() {
    return changeFileName("junit.txt");
  }

  /**
   * Returns a file name which is constructed by values of some system properties appended by the
   * given file name. The system properties evaluated are:
   *
   * <ul>
   *   <li>jdo.tck.log.directory: Specifies the directory for the file.
   *   <li>jdo.tck.database, jdo.tck.cfg: The values of these properties prepend the given file
   *       name.
   *   <li>jdo.tck.identitytype: The value of this property is replaced by <code>"app"</code> if it
   *       equals <code>"applicationidentity"</code>, else it is replaced by <code>"dsid"</code>.
   * </ul>
   *
   * The returned file name is constructed as follows:<br>
   * ${jdo.tck.log.directory}/${jdo.tck.database}-${jdo.tck.identitytype}-${jdo.tck.cfg}${given file
   * name} Values of properties which do not exist default to <code>""</code>.
   *
   * @param fileName the file name
   * @return the changed file name
   */
  public static String changeFileName(String fileName) {
    String directory = LOG_DIRECTORY;
    String db = System.getProperty("jdo.tck.database");

    String identityType = System.getProperty("jdo.tck.identitytype");
    if (identityType != null) {
      if (identityType.equals("applicationidentity")) {
        identityType = "app";
      } else {
        identityType = "dsid";
      }
    }

    String configuration = System.getProperty("jdo.tck.cfg");
    if (configuration != null) {
      int index = configuration.indexOf('.');
      if (index != -1) {
        configuration = configuration.substring(0, index);
      }
    }

    directory = fixPartialFileName(directory);
    db = fixPartialFileName(db, '-', new String[] {identityType, configuration, fileName});
    identityType = fixPartialFileName(identityType, '-', new String[] {configuration, fileName});
    configuration = fixPartialFileName(configuration, '-', new String[] {fileName});

    return directory + db + identityType + configuration + fileName;
  }

  private static String fixPartialFileName(String str) {
    if (str == null) {
      str = "";
    }
    return str;
  }

  private static String fixPartialFileName(String str, char c, String[] values) {
    str = fixPartialFileName(str);
    if (!str.equals("")) {
      for (String value : values) {
        if (value != null && !value.equals("") && !value.startsWith(".")) {
          str += c;
          break;
        }
      }
    }
    return str;
  }
}
