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

package org.apache.jdo.exectck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.jdo.JDOException;

public class Utilities {

  static final String DELIMITER_REGEX = "[ \t\n,;]+";

  private static final String DATE_FORMAT_NOW = "yyyyMMdd-HHmmss";

  private Utilities() {
    // This method is deliberately left empty.
  }

  /*
   * Return the current date/time as a String.
   */
  public static String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());
  }

  /*
   * From an array of URLs, create a classpath String suitable for use
   * as a command line argument
   */
  public static String urls2ClasspathString(List<URL> urls) {
    StringBuilder cp = new StringBuilder();

    for (URL url : urls) {
      cp.append(url.getPath());
      cp.append(File.pathSeparator);
    }
    return cp.toString();
  }

  public static String removeSubstrs(String original, String exclude) {
    String[] deleteThese = exclude.split(DELIMITER_REGEX);
    String filtered = original;
    for (String sub : deleteThese) {
      filtered = filtered.replaceAll(sub.trim(), "");
    }
    return filtered.trim();
  }

  public static void printClasspath(ClassLoader loader) {
    if (loader instanceof URLClassLoader) {
      // Get the URLs
      URL[] urls = ((URLClassLoader) loader).getURLs();
      System.out.println(urls.length + " URL(s) for loader: ");
      for (URL url : urls) {
        if (url != null) {
          System.out.println("    " + url.getFile());
        }
      }
    }
  }

  public static void printClasspath() {

    // Get the System Classloader
    printClasspath(ClassLoader.getSystemClassLoader());

    // Get the System Classloader
    printClasspath(Thread.currentThread().getContextClassLoader());
  }

  static String readFile(String fileName) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = null;
      StringBuilder stringBuf = new StringBuilder();
      String ls = System.getProperty("line.separator");
      while ((line = reader.readLine()) != null) {
        stringBuf.append(line);
        stringBuf.append(ls);
      }
      return stringBuf.toString();
    }
  }

  public static int invokeCommand(List<String> command, File directory, String logFile) {
    ProcessBuilder pb = new ProcessBuilder(command);
    pb.directory(directory);
    if (logFile != null) {
      File log = new File(logFile);
      pb.redirectErrorStream(true);
      pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
    }
    try {
      Process proc = pb.start();
      return proc.waitFor();
    } catch (InterruptedException ex) {
      // Restore interrupted state...
      Thread.currentThread().interrupt();
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
      return 1;
    }
  }
}
