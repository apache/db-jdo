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

package javax.jdo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests class javax.jdo.Enhancer (Enhancer main class).
 *
 * <p>
 */
class EnhancerTest extends AbstractTest {

  /** The path delimiter for constructing classpaths. */
  private static final String PATH_DELIMITER = System.getProperty("path.separator");

  /** The maven basedir identifying the directory of the execution environment. */
  private static final String BASEDIR = System.getProperty("basedir");

  @ParameterizedTest
  @ValueSource(strings = {"?", "-help", "-h"})
  void testUsageOptions(String option) {
    // invoke enhancer with a usage option
    InvocationResult result = invokeEnhancer(option);
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("javax.jdo.Enhancer"),
        "Expected Usage message from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testInvalidOption() {
    // invoke enhancer with an invalid option
    InvocationResult result = invokeEnhancer("-poo");
    assertEquals(Constants.ENHANCER_USAGE_ERROR, result.getExitValue(), "Wrong return value ");
    String errorString = result.getErrorString();
    assertTrue(
        errorString.contains("javax.jdo.Enhancer"),
        "Expected Usage message from err:\n" + errorString);
  }

  @Test
  void testProperties() {
    // invoke enhancer with verbose option
    InvocationResult result = invokeEnhancer("-v");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains(Constants.PROPERTY_ENHANCER_VENDOR_NAME),
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains(Constants.PROPERTY_ENHANCER_VERSION_NUMBER),
        "Expected MockEnhancer version message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("Mock Enhancer"),
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("2.3.0"),
        "Expected MockEnhancer vendor message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("MockKey"),
        "Expected MockEnhancer properties message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
  }

  @ParameterizedTest
  @ValueSource(strings = {"-v", "-verbose"})
  void testVerboseOptions(String option) {
    // invoke enhancer with verbose option
    InvocationResult result = invokeEnhancer(option);
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("javax.jdo.MockEnhancer"),
        "Expected Enhancer class message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
  }

  @Test
  void testVerboseClasses() {
    // invoke enhancer with .class parameter
    InvocationResult result = invokeEnhancer("-v some.class");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some.class"),
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("1"),
        "Expected number of classes from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testVerboseJars() {
    // invoke enhancer with a .jar parameter
    InvocationResult result = invokeEnhancer("-v some.jar");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some.jar"),
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("1"),
        "Expected number of jars from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testVerboseJDOs() {
    // invoke enhancer with a .jdo parameter
    InvocationResult result = invokeEnhancer("-v some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some.jdo"),
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("1"),
        "Expected number of jdos from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testVerboseAll() {
    // invoke enhancer with multiple parameters
    InvocationResult result = invokeEnhancer("-v some.class some.jar some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some.jdo"),
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("some.jar"),
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("some.class"),
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("3"),
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testVerboseCheckonlyAll() {
    // invoke enhancer with a checkonly option
    InvocationResult result = invokeEnhancer("-v -checkonly some.class some.jar some.jdo");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some.jdo"),
        "Expected jdo message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("some.jar"),
        "Expected jar message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("some.class"),
        "Expected class message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("3"),
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testMissingPU() {
    // invoke enhancer with missing parameter
    InvocationResult result = invokeEnhancer("-v -pu");
    assertEquals(3, result.getExitValue(), "Wrong return value ");
  }

  @Test
  void testVerbosePU() {
    // invoke enhancer with a pu parameter
    InvocationResult result = invokeEnhancer("-v -pu myPU -pu yourPU");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("myPU"),
        "Expected pu message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("yourPU"),
        "Expected pu message from out:\n" + outputString + " with err:\n" + errorString);
    assertTrue(
        outputString.contains("2"),
        "Expected number of elements from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testClasspath() {
    // invoke enhancer with a classpath parameter
    // JDOHelper must be loadable from this path
    // the File.toURI should append "/" to the path, so only "target/classes" is needed
    InvocationResult result = invokeEnhancer("-v -cp " + BASEDIR + "/target/classes");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("target/classes"),
        "Expected classpath message from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testBadClasspath() {
    // invoke enhancer with a bad classpath parameter
    // JDOHelper is not loadable from this path
    InvocationResult result = invokeEnhancer("-v -cp target");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        1, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        errorString.contains("JDOHelper"),
        "Expected classpath error message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
  }

  @Test
  void testClasspathJar() throws IOException, InterruptedException {
    // invoke enhancer with a classpath parameter
    // JDOHelper must be loadable from this path
    // create the jar file from the target/classes directory
    String uuid = UUID.randomUUID().toString();
    File uuidDir = new File(BASEDIR + "/target/" + uuid);
    uuidDir.mkdirs();
    String enhancerJar = "target/" + uuid + "/enhancer-test.jar";
    String enhancerJarPathname = BASEDIR + "/" + enhancerJar;
    Process create =
        Runtime.getRuntime()
            .exec("jar -cf " + enhancerJarPathname + " -C " + BASEDIR + "/target/classes .");
    int returnCode = create.waitFor();
    assertEquals(0, returnCode, "jar command returned wrong return code.");
    // find the jdo.jar in target
    InvocationResult result = invokeEnhancer("-v -cp " + enhancerJar);
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains(enhancerJar),
        "Expected classpath message from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testOutputDirectory() {
    // invoke enhancer with an output directory parameter
    InvocationResult result = invokeEnhancer("-v -d some/output/directory");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("some/output/directory"),
        "Expected directory message from out:\n" + outputString + " with err:\n" + errorString);
  }

  @Test
  void testMissingOutputDirectory() {
    // invoke enhancer with missing parameter
    InvocationResult result = invokeEnhancer("-v -d");
    assertEquals(3, result.getExitValue(), "Wrong return value ");
  }

  @Test
  void testDir() {
    // invoke enhancer with directory and not recurse
    InvocationResult result =
        invokeEnhancer("-v " + BASEDIR + "/target/test-classes/enhancer-test-dir");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("enhancer-test-dir"),
        "Expected directory enhancer-test-dir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file1.jdo"),
        "Expected file file1.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file2.class"),
        "Expected file file2.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file3.jar"),
        "Expected file file3.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertFalse(
        outputString.contains("enhancer-test-subdir"),
        "Expected no directory enhancer-test-subdir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("3"),
        "Expected 3 files to be enhanced in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
  }

  @Test
  void testDirRecurse() {
    // invoke enhancer with directory and recurse
    InvocationResult result =
        invokeEnhancer("-v -r " + BASEDIR + "/target/test-classes/enhancer-test-dir");
    String outputString = result.getOutputString();
    String errorString = result.getErrorString();
    assertEquals(
        0, result.getExitValue(), "Wrong exit code from Enhancer with stderr:\n" + errorString);
    assertTrue(
        outputString.contains("enhancer-test-dir"),
        "Expected directory enhancer-test-dir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("enhancer-test-subdir"),
        "Expected directory enhancer-test-subdir in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file1.jdo"),
        "Expected file file1.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file2.class"),
        "Expected file file2.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file3.jar"),
        "Expected file file3.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file4.jdo"),
        "Expected file file4.jdo in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file5.class"),
        "Expected file file5.class in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("file6.jar"),
        "Expected file file6.jar in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
    assertTrue(
        outputString.contains("6"),
        "Expected 6 files to be enhanced in message from out:\n"
            + outputString
            + " with err:\n"
            + errorString);
  }

  private InvocationResult invokeEnhancer(String string) {
    InvocationResult result = new InvocationResult();
    try {
      // create the java command to invoke the Enhancer
      List<String> commands = new ArrayList<>();
      // find the java command in the user's path
      commands.add("java");
      commands.add("-cp");
      commands.add(
          ""
              + BASEDIR
              + "/target/classes"
              + PATH_DELIMITER
              + ""
              + BASEDIR
              + "/target/test-classes");
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
            () -> {
              try {
                while (-1 != reader.read(output))
                  ;
              } catch (IOException e) {
                e.printStackTrace();
              } finally {
                output.flip();
              }
            });
    thread.start();
    return thread;
  }

  static class InvocationResult {
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
