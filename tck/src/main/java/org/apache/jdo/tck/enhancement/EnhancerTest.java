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

/*
 * EnhancerTest.java
 *
 * Created on February 17, 2002, 1:59 PM
 */

package org.apache.jdo.tck.enhancement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.jdo.tck.JDO_Test;

/**
 * @author Craig Russell
 * @version 1.0
 */
public abstract class EnhancerTest extends JDO_Test {

  /** Creates new EnhancerTest */
  public EnhancerTest() {}

  /**
   * @param resourceName name of teh resource
   * @return Properties object
   */
  protected Properties getProperties(String resourceName) {
    Properties props = null;
    try {
      InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourceName);
      props = new Properties();
      props.load(in);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("EnhancerTest:", "Error loading properties " + resourceName + " exception " + ex);
    }
    return props;
  }

  /**
   * @param packageName package name
   * @param fullyQualifiedClassNameList list of class names
   */
  protected abstract void runTestOnePackage(String packageName, List fullyQualifiedClassNameList);

  /**
   * @param packageName package name
   * @param className class name
   * @return converted class name
   */
  protected String convertClassName(String packageName, String className) {
    return packageName + "." + className;
  }

  /** */
  void runTestAllPackages() {
    // First, get classes to test from properties file.
    Properties classesToTest = getProperties("enhancement-test.properties"); // NOI18N

    Enumeration enumeration = classesToTest.propertyNames();
    int numberOfPackages = 0;

    // Each key is a package name; the value is a list of class names to test.
    while (enumeration.hasMoreElements()) {
      ++numberOfPackages;
      String packageName = (String) enumeration.nextElement();
      if (debug) logger.debug("EnhancerTest Package: " + packageName);
      String classNames = (String) classesToTest.get(packageName);
      if (debug) logger.debug("EnhancerTest Classes: " + classNames);
      StringTokenizer st = new StringTokenizer(classNames, " ,");
      ArrayList classNameList = new ArrayList();
      // Each entry is a list of class names separated by comma or space
      while (st.hasMoreTokens()) {
        String className = st.nextToken();
        String listEntry = convertClassName(packageName, className);
        classNameList.add(listEntry);
        if (debug) logger.debug("EnhancerTest Class: " + className);
      }
      runTestOnePackage(packageName, classNameList);
    }
    if (debug) logger.debug("EnhancerTest numberOfPackages: " + numberOfPackages);
    return;
  }
}
