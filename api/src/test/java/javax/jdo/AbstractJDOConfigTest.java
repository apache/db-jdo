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

import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.jdo.util.AbstractTest;

public abstract class AbstractJDOConfigTest extends AbstractTest {

  /** A class path prefix used in the various tests where the class path needs to be set. */
  protected static String JDOCONFIG_CLASSPATH_PREFIX = initJDOConfigClasspathPrefix();

  /**
   * Returns the JDO configuration class path prefix's default value, which is the project base
   * directory suffixed by the path to the configuration directory
   * (<tt>src/test/resources/jdoconfig</tt>).
   *
   * @return the default class path prefix used by this test suite.
   */
  protected static String initJDOConfigClasspathPrefix() {
    return initBasedir() + "src/test/resources/jdoconfig";
  }

  /**
   * The class path used to specify the location of test class files.
   *
   * @return the class path where test class files can be found.
   */
  protected static String TEST_CLASSPATH = initTestClasspath();

  /**
   * Returns the default class path for JDO test class files (<tt>target/test-classes/</tt>).
   *
   * @return the default class path for JDO test class files.
   */
  protected static String initTestClasspath() {
    return initBasedir() + "target/test-classes/";
  }

  /** The class path used to locate the JDO API class files. */
  protected static String API_CLASSPATH = initAPIClasspath();

  /**
   * Returns the default class path for JDO API class files (<tt>target/classes/</tt>).
   *
   * @return the default class path for JDO API class files.
   */
  protected static String initAPIClasspath() {
    return initBasedir() + "target/classes/";
  }

  /**
   * Returns the base directory for this project. This base directory is used to build up the other
   * class paths defined in this test suite. The value returned is the value returned by <code>
   * System.getProperty("basedir")</code>. A trailing slash is appended to the path if it doesn't
   * exist.
   *
   * @return the default base directory of the project.
   */
  protected static String initBasedir() {
    String basedir = System.getProperty("basedir");
    if (basedir != null) {
      if (!basedir.endsWith("/")) {
        basedir += "/";
      }
    } else {
      basedir = "";
    }
    return basedir;
  }

  /** A randomizer seeded with the system clock's current time. */
  protected static Random RANDOM = new Random(System.currentTimeMillis());

  /**
   * Fails the test if the number of properties in the two specified {@link java.util.Map Map}
   * objects are not identical or their values do not match.
   *
   * @param expected the first {@link java.util.Map Map} object to test.
   * @param actual the second {@link java.util.Map Map} object to test.
   */
  static void assertEqualProperties(Map expected, Map actual) {
    Iterator i = expected.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry entry = (Map.Entry) i.next();
      String key = (String) entry.getKey();
      String expectedValue = (String) entry.getValue();
      String actualValue = (String) actual.get(key);

      assertEquals(
          "Actual property at key ["
              + key
              + "] with value ["
              + actualValue
              + "] not equal to expected value ["
              + expectedValue
              + "]",
          expectedValue,
          actualValue);
    }
  }

  protected String getPMFClassNameViaServiceLookup(ClassLoader loader) {
    try {
      Enumeration urls = JDOHelper.getResources(loader, SERVICE_LOOKUP_PMF_RESOURCE_NAME);
      while (urls.hasMoreElements()) {
        // return the first one found
        return JDOHelper.getClassNameFromURL((URL) urls.nextElement());
      }
    } catch (Exception ex) {
      // ignore exceptions from i/o errors
    }
    return null;
  }
}
