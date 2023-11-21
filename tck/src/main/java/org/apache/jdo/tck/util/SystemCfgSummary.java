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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.jdo.JDOFatalException;

/** A class to produce a text summary of system configuration information. */
public class SystemCfgSummary {

  /** The name of the system configuration summary file. */
  private static final String SYSCFG_FILE_NAME = "system_config.txt";

  private static final String NL = System.getProperty("line.separator");

  private final String path;

  /**
   * Creates a new file containing system configuration information.
   *
   * @param args the first element contains the output directory; the second element contains the
   *     file name
   */
  public static void main(String[] args) {
    String directory = args[0];
    String fileName = null;
    if (args[1] != null) {
      fileName = args[1];
    } else {
      fileName = SYSCFG_FILE_NAME;
    }

    SystemCfgSummary summary = new SystemCfgSummary(directory, fileName);
    summary.save();
  }

  public SystemCfgSummary(String directory, String fileName) {
    this.path = directory + File.separator + fileName;
  }

  public void save() {
    String message = getSystemInfo();
    if (message == null) {
      message = "No system information found.";
    }
    saveSystemInfo(this.path, message);
  }

  /**
   * Gets system information from System properties
   *
   * @return System property keys and values as a String, one per line
   */
  private String getSystemInfo() {
    Properties props = System.getProperties();
    Enumeration<?> propEnum = props.propertyNames();
    StringBuilder sysinfo = new StringBuilder();
    while (propEnum.hasMoreElements()) {
      String key = (String) propEnum.nextElement();
      sysinfo.append(key + ":  " + props.getProperty(key) + NL);
    }
    return sysinfo.toString();
  }

  /**
   * Saves the given message to the system configuration summary file in the given directory.
   *
   * @param path the path
   * @param message the message
   */
  private void saveSystemInfo(String path, String message) {
    try (PrintStream resultStream = new PrintStream(new FileOutputStream(path, true))) {
      resultStream.println(message);
    } catch (FileNotFoundException e) {
      throw new JDOFatalException("Cannot create file " + path, e);
    }
  }
}
