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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Optional;
import javax.jdo.JDOFatalException;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

/** A serializable class used to store test results in a file. */
public class ResultSummary implements Serializable {
  private static final long serialVersionUID = 1L;

  /** The name of the file to store a serialized instance of this class. */
  private static final String FILE_NAME_OF_RESULT_SUMMARY = "ResultSummary.ser";

  private static final DecimalFormat THREE_DIGITS_FORMATTER = new DecimalFormat("000");
  private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("000.0");

  /** The name of the TCK result file. */
  private static final String RESULT_FILE_NAME = "TCK-results.txt";

  private static final String NEWLINE = System.getProperty("line.separator");

  /* The number of all configurations. */
  private int nrOfTotalConfigurations = 0;

  /* The number of failed configurations. */
  private int nrOfFailedConfigurations = 0;

  /* The total number of tests. */
  private int totalTestCount = 0;

  /* The total number of failures. */
  private int totalFailureCount = 0;

  /**
   * Deserializes an instance and prints that instance to {@link System#out} and the TCK result
   * file. Finally deletes the file of the serialized instance.
   *
   * @param args the first element contains the directory where the test result is stored.
   */
  public static void main(String[] args) {
    // print result summary

    String directory = args[0];
    ResultSummary resultSummary = ResultSummary.load(directory);
    String resultMessage = resultSummary != null ? resultSummary.toString() : "No tests were run.";
    String message = "-------" + NEWLINE + resultMessage;
    String resultFileName = directory + File.separator + RESULT_FILE_NAME;
    appendTCKResultMessage(resultFileName, message);
    System.out.println(resultMessage);
    System.out.println("See file '" + resultFileName + "' for details.");

    // delete file
    String resultSummaryFileName = directory + File.separator + FILE_NAME_OF_RESULT_SUMMARY;
    File file = new File(resultSummaryFileName);
    file.delete();
  }

  /**
   * Appends the given message to the TCK result file in the given directory.
   *
   * @param fileName the name of the result file
   * @param message the message
   */
  private static void appendTCKResultMessage(String fileName, String message) {
    try (PrintStream resultStream = new PrintStream(new FileOutputStream(fileName, true))) {
      resultStream.println(message);
    } catch (FileNotFoundException e) {
      throw new JDOFatalException("Cannot create file " + fileName, e);
    }
  }

  /**
   * Creates an instance for the given result object and serializes that instance to a file in the
   * geven directory.
   *
   * @param directory the directory
   * @param result the result object
   */
  public static void save(
      String directory, String idtype, String config, Optional<TestExecutionSummary> result) {
    ResultSummary resultSummary = load(directory);
    if (resultSummary == null) {
      resultSummary = new ResultSummary();
    }
    resultSummary.appendTestResult(directory, idtype, config, result);
    resultSummary.increment(result);
    resultSummary.save(directory);
  }

  /**
   * Returns a deserialized instance stored in the given direcotry.
   *
   * @param directory the directory
   * @return the deserialized instance
   */
  private static ResultSummary load(String directory) {
    ResultSummary result;
    String fileName = directory + File.separator + FILE_NAME_OF_RESULT_SUMMARY;
    try {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
        result = (ResultSummary) ois.readObject();
      }
    } catch (FileNotFoundException e) {
      result = null;
    } catch (IOException | ClassNotFoundException e) {
      throw new JDOFatalException("Cannot deserialize result summary in file " + fileName, e);
    }
    return result;
  }

  /**
   * Increments fields of this instance based on the given result object.
   *
   * @param result the result object
   */
  private void increment(Optional<TestExecutionSummary> result) {
    // total numbers
    this.nrOfTotalConfigurations++;
    result.ifPresent(
        r -> {
          this.totalTestCount += r.getTestsFoundCount();
          if (r.getTestsFailedCount() > 0) {
            this.nrOfFailedConfigurations++;
            this.totalFailureCount += r.getTestsFailedCount();
          }
        });
  }

  private void appendTestResult(
      String directory, String idtype, String config, Optional<TestExecutionSummary> result) {
    String resultFileName = directory + File.separator + RESULT_FILE_NAME;
    String header = "Running tests for " + config + " with " + idtype + ":" + NEWLINE + "  ";
    StringBuilder builder = new StringBuilder(header);
    if (result.isPresent()) {
      builder.append(getTestResult(result.get()));
    } else {
      builder.append("No tests were run.");
    }
    appendTCKResultMessage(resultFileName, builder.toString());
  }

  private String getTestResult(TestExecutionSummary summary) {
    StringBuilder builder = new StringBuilder();
    builder.append(summary.getTestsFailedCount() == 0 ? "OK " : "** ");
    builder.append("Tests found: ").append(summary.getTestsFoundCount());
    builder.append(", started: ").append(summary.getTestsStartedCount());
    builder.append(", succeeded: ").append(summary.getTestsSucceededCount());
    builder.append(", failed: ").append(summary.getTestsFailedCount());
    builder.append(", skipped: ").append(summary.getTestsSkippedCount());
    builder.append(", aborted: ").append(summary.getTestsAbortedCount());
    builder
        .append(", time: ")
        .append(((double) (summary.getTimeFinished() - summary.getTimeStarted()) / 1000.0))
        .append(" seconds");
    return builder.toString();
  }

  /**
   * Serializes this instance to a file in the given directory.
   *
   * @param directory the directory
   */
  private void save(String directory) {
    String fileName = directory + File.separator + FILE_NAME_OF_RESULT_SUMMARY;
    try {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
        oos.writeObject(this);
      }
    } catch (FileNotFoundException e) {
      throw new JDOFatalException("Cannot create file " + fileName, e);
    } catch (IOException e) {
      throw new JDOFatalException("Cannot serialize result summary to file " + fileName, e);
    }
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("Total tests run: ").append(totalTestCount).append(".");
    if (this.nrOfFailedConfigurations == 0) {
      result.append(NEWLINE);
      result.append("All (").append(this.nrOfTotalConfigurations);
      result.append(") configurations passed.");
    } else {
      result.append(" Failures: ").append(totalFailureCount).append(".");
      result.append(NEWLINE);
      result.append(this.nrOfFailedConfigurations).append(" of ");
      result.append(this.nrOfTotalConfigurations);
      result.append(" configurations failed.");
    }
    return result.toString();
  }
}
