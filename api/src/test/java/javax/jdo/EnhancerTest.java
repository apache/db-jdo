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

package javax.jdo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/**
 * Tests class javax.jdo.Enhancer (Enhancer main class).
 *
 * <p>
 */
public class EnhancerTest extends AbstractTest {

  /** The path delimiter for constructing classpaths. */
  private static String pathDelimiter = System.getProperty("path.separator");

  /** The maven basedir identifying the directory of the execution environment. */
  private static String basedir = System.getProperty("basedir");

  /**
   * Main
   *
   * @param args command line arguments
   */
  public static void main(String args[]) {
    BatchTestRunner.run(EnhancerTest.class);
  }

  public void testUsageOption() {
    // invoke enhancer with a usage option
    InvocationResult result = invokeEnhancer("?");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected Usage message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("javax.jdo.Enhancer"));
  }

  public void testHelpOption() {
    // invoke enhancer with a usage option
    InvocationResult result = invokeEnhancer("-help");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected Usage message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("javax.jdo.Enhancer"));
  }

  public void testHOption() {
    // invoke enhancer with a usage option
    InvocationResult result = invokeEnhancer("-h");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected Usage message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("javax.jdo.Enhancer"));
  }

  public void testInvalidOption() {
    // invoke enhancer with an invalid option
    InvocationResult result = invokeEnhancer("-poo");
    assertEquals("Wrong return value ", ENHANCER_USAGE_ERROR, result.getExitValue());
    String errorString = result.getErrorString();
    assertTrue(
        "Expected Usage message from err:\n" + errorString,
        errorString.contains("javax.jdo.Enhancer"));
  }

  public void testProperties() {
    // invoke enhancer with verbose option
    InvocationResult result = invokeEnhancer("-v");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains(PROPERTY_ENHANCER_VENDOR_NAME));
    assertTrue(
        "Expected MockEnhancer version message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains(PROPERTY_ENHANCER_VERSION_NUMBER));
    assertTrue(
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("Mock Enhancer"));
    assertTrue(
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("2.3.0"));
    assertTrue(
        "Expected MockEnhancer properties message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("MockKey"));
  }

  public void testVOption() {
    // invoke enhancer with verbose option
    InvocationResult result = invokeEnhancer("-v");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected Enhancer class message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("javax.jdo.MockEnhancer"));
  }

  public void testVerboseOption() {
    // invoke enhancer with verbose option
    InvocationResult result = invokeEnhancer("-verbose");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected Enhancer class message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("javax.jdo.MockEnhancer"));
  }

  public void testVerboseClasses() {
    // invoke enhancer with .class parameter
    InvocationResult result = invokeEnhancer("-v some.class");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.class"));
    assertTrue(
        "Expected number of classes from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("1"));
  }

  public void testVerboseJars() {
    // invoke enhancer with a .jar parameter
    InvocationResult result = invokeEnhancer("-v some.jar");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jar"));
    assertTrue(
        "Expected number of jars from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("1"));
  }

  public void testVerboseJDOs() {
    // invoke enhancer with a .jdo parameter
    InvocationResult result = invokeEnhancer("-v some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jdo"));
    assertTrue(
        "Expected number of jdos from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("1"));
  }

  public void testVerboseAll() {
    // invoke enhancer with multiple parameters
    InvocationResult result = invokeEnhancer("-v some.class some.jar some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jdo"));
    assertTrue(
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jar"));
    assertTrue(
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.class"));
    assertTrue(
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("3"));
  }

  public void testVerboseCheckonlyAll() {
    // invoke enhancer with a checkonly option
    InvocationResult result = invokeEnhancer("-v -checkonly some.class some.jar some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jdo"));
    assertTrue(
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.jar"));
    assertTrue(
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some.class"));
    assertTrue(
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("3"));
  }

  public void testMissingPU() {
    // invoke enhancer with missing parameter
    InvocationResult result = invokeEnhancer("-v -pu");
    assertEquals("Wrong return value ", 3, result.getExitValue());
  }

  public void testVerbosePU() {
    // invoke enhancer with a pu parameter
    InvocationResult result = invokeEnhancer("-v -pu myPU -pu yourPU");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected pu message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("myPU"));
    assertTrue(
        "Expected pu message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("yourPU"));
    assertTrue(
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("2"));
  }

  public void testClasspath() {
    // invoke enhancer with a classpath parameter
    // JDOHelper must be loadable from this path
    // the File.toURI should append "/" to the path, so only "target/classes" is needed
    InvocationResult result = invokeEnhancer("-v -cp " + basedir + "/target/classes");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected classpath message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("target/classes"));
  }

  public void testBadClasspath() {
    // invoke enhancer with a bad classpath parameter
    // JDOHelper is not loadable from this path
    InvocationResult result = invokeEnhancer("-v -cp target");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 1, result.getExitValue());
    assertTrue(
        "Expected classpath error message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        errorString.contains("JDOHelper"));
  }

  public void testClasspathJar() throws IOException, InterruptedException {
    // invoke enhancer with a classpath parameter
    // JDOHelper must be loadable from this path
    // create the jar file from the target/classes directory
    String uuid = UUID.randomUUID().toString();
    File uuidDir = new File(basedir + "/target/" + uuid);
    uuidDir.mkdirs();
    String enhancerJar = "target/" + uuid + "/enhancer-test.jar";
    String enhancerJarPathname = basedir + "/" + enhancerJar;
    Process create =
        Runtime.getRuntime()
            .exec("jar -cf " + enhancerJarPathname + " -C " + basedir + "/target/classes .");
    int returnCode = create.waitFor();
    assertEquals("jar command returned wrong return code.", 0, returnCode);
    // find the jdo.jar in target
    InvocationResult result = invokeEnhancer("-v -cp " + enhancerJar);
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected classpath message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains(enhancerJar));
  }

  public void testOutputDirectory() {
    // invoke enhancer with an output directory parameter
    InvocationResult result = invokeEnhancer("-v -d some/output/directory");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected directory message from out:\n" + outputString + " with err:\n" + errorString,
        outputString.contains("some/output/directory"));
  }

  public void testMissingOutputDirectory() {
    // invoke enhancer with missing parameter
    InvocationResult result = invokeEnhancer("-v -d");
    assertEquals("Wrong return value ", 3, result.getExitValue());
  }

  public void testDir() {
    // invoke enhancer with directory and not recurse
    InvocationResult result =
        invokeEnhancer("-v " + basedir + "/target/test-classes/enhancer-test-dir");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected directory enhancer-test-dir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("enhancer-test-dir"));
    assertTrue(
        "Expected file file1.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file1.jdo"));
    assertTrue(
        "Expected file file2.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file2.class"));
    assertTrue(
        "Expected file file3.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file3.jar"));
    assertFalse(
        "Expected no directory enhancer-test-subdir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("enhancer-test-subdir"));
    assertTrue(
        "Expected 3 files to be enhanced in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("3"));
  }

  public void testDirRecurse() {
    // invoke enhancer with directory and recurse
    InvocationResult result =
        invokeEnhancer("-v -r " + basedir + "/target/test-classes/enhancer-test-dir");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        "Wrong exit code from Enhancer with stderr:\n" + errorString, 0, result.getExitValue());
    assertTrue(
        "Expected directory enhancer-test-dir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("enhancer-test-dir"));
    assertTrue(
        "Expected directory enhancer-test-subdir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("enhancer-test-subdir"));
    assertTrue(
        "Expected file file1.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file1.jdo"));
    assertTrue(
        "Expected file file2.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file2.class"));
    assertTrue(
        "Expected file file3.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file3.jar"));
    assertTrue(
        "Expected file file4.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file4.jdo"));
    assertTrue(
        "Expected file file5.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file5.class"));
    assertTrue(
        "Expected file file6.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("file6.jar"));
    assertTrue(
        "Expected 6 files to be enhanced in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString,
        outputString.contains("6"));
  }

  private InvocationResult invokeEnhancer(String string) {
    InvocationResult result = new InvocationResult();
    try {
      // create the java command to invoke the Enhancer
      List<String> commands = new ArrayList<String>();
      // find the java command in the user's path
      commands.add("java");
      commands.add("-cp");
      commands.add(
          "" + basedir + "/target/classes" + pathDelimiter + "" + basedir + "/target/test-classes");
      commands.add("javax.jdo.Enhancer");
      // add the test options (from the method parameter) to the java command
      String[] optionArray = string.split(" ");
      for (String option : optionArray) {
        commands.add(option);
      }
      String[] cmdarray = commands.toArray(new String[commands.size()]);
      ProcessBuilder builder = new ProcessBuilder(cmdarray);
      Process proc = builder.start();
      InputStream stdout = proc.getInputStream();
      InputStream stderr = proc.getErrorStream();
      CharBuffer outBuffer = CharBuffer.allocate(1000000);
      CharBuffer errBuffer = CharBuffer.allocate(1000000);
      Thread outputThread = createReaderThread(stdout, outBuffer);
      Thread errorThread = createReaderThread(stderr, errBuffer);
      int exitValue = proc.waitFor();
      result.setExitValue(exitValue);
      errorThread.join(10000); // wait ten seconds to get stderr after process terminates
      outputThread.join(10000); // wait ten seconds to get stdout after process terminates
      result.setErrorString(errBuffer.toString());
      result.setOutputString(outBuffer.toString());
      // wait until the Enhancer command finishes
    } catch (InterruptedException ex) {
      throw new RuntimeException("InterruptedException", ex);
    } catch (IOException ex) {
      throw new RuntimeException("IOException", ex);
    } catch (JDOException jdoex) {
      jdoex.printStackTrace();
      Throwable[] throwables = jdoex.getNestedExceptions();
      System.out.println("Exception throwables of size: " + throwables.length);
      for (Throwable throwable : throwables) {
        throwable.printStackTrace();
      }
    }
    return result;
  }

  private Thread createReaderThread(final InputStream input, final CharBuffer output) {
    final Reader reader = new InputStreamReader(input);
    Thread thread =
        new Thread(
            new Runnable() {
              public void run() {
                int count = 0;
                int outputBytesRead = 0;
                try {
                  while (-1 != (outputBytesRead = reader.read(output))) {
                    count += outputBytesRead;
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                } finally {
                  output.flip();
                }
              }
            });
    thread.start();
    return thread;
  }

  class InvocationResult {
    private int exitValue;
    private String errorString;
    private String outputString;

    int getExitValue() {
      return exitValue;
    }

    private void setExitValue(int exitValue) {
      this.exitValue = exitValue;
    }

    private void setErrorString(String errorString) {
      this.errorString = errorString;
    }

    String getErrorString() {
      return errorString;
    }

    private void setOutputString(String outputString) {
      this.outputString = outputString;
    }

    String getOutputString() {
      return outputString;
    }
  }
}
