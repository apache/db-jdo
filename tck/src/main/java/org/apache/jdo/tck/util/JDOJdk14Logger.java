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

package org.apache.jdo.tck.util;

import java.io.InputStream;
import java.io.IOException;

import javax.jdo.JDOFatalInternalException;
import javax.jdo.LegacyJava;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;

import java.util.logging.LogManager;

import javax.jdo.JDOFatalUserException;

import org.apache.commons.logging.impl.Jdk14Logger;

/**
 * JDO-specific subclass of the apache commons logging Log implementation that wraps the standard
 * JDK 1.4 logging. This class configures the JDK LogManager using a properties file called
 * logging.properties found via the CLASSPATH.
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOJdk14Logger extends Jdk14Logger {
  /** Logging properties file name. */
  public static final String PROPERIES_FILE = "logging.properties";

  /** Indicates whether JDK 1.4 logging has been configured by this class. */
  private static boolean configured = false;

  /**
   * Constructor checking whether JDK 1.4 logging should be configuared after calling super
   * constructor.
   *
   * @param name logger name
   */
  public JDOJdk14Logger(String name) {
    super(name);
    if (!configured) {
      configured = true;
      configureJDK14Logger();
    }
  }

  /** Configures JDK 1.4 LogManager. */
  private void configureJDK14Logger() {
    final LogManager logManager = LogManager.getLogManager();
    final ClassLoader cl = getClass().getClassLoader();
    doPrivileged(
        new PrivilegedAction() {
          public Object run() {
            try {
              InputStream config = cl.getResourceAsStream(PROPERIES_FILE);
              logManager.readConfiguration(config);
              return null;
            } catch (IOException ex) {
              throw new JDOFatalUserException(
                  "A IOException was thrown when trying to read the "
                      + "logging configuration file "
                      + PROPERIES_FILE
                      + ".",
                  ex);
            } catch (SecurityException ex) {
              throw new JDOFatalUserException(
                  "A SecurityException was thrown when trying to read "
                      + "the logging configuration file "
                      + PROPERIES_FILE
                      + ". In order to configure JDK 1.4 logging, you must "
                      + "grant java.util.logging.LoggingPermission(control) "
                      + "to the codeBase containing the JDO TCK.",
                  ex);
            }
          }
        });
  }

  @SuppressWarnings("unchecked")
  private static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
    try {
      return (T) LegacyJava.doPrivilegedAction.invoke(null, privilegedAction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new JDOFatalInternalException(e.getMessage());
    }
  }
}
