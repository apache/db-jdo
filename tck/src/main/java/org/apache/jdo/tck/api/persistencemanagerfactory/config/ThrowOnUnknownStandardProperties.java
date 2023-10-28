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
package org.apache.jdo.tck.api.persistencemanagerfactory.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Properties;
import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThrowOnUnknownStandardProperties extends JDO_Test {

  @Override
  protected boolean preSetUp() {
    return false;
  }

  @Override
  protected boolean preTearDown() {
    return false;
  }

  protected Method getStaticGetPMFMethod() {
    Class<?> pmfClass = getPMFClass();
    try {
      Method m = pmfClass.getDeclaredMethod("getPersistenceManagerFactory", Map.class);

      if ((m.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
        throw new JDOException(
            "PMF class "
                + pmfClass.getName()
                + " method 'getPersistenceManagerFactory' is not static");
      }

      return m;
    } catch (SecurityException e) {
      throw new JDOException(
          "Cannot get method 'getPersistenceManagerFactory' from PMF class " + pmfClass.getName(),
          e);
    } catch (NoSuchMethodException e) {
      throw new JDOException(
          "No method 'getPersistenceManagerFactory' from PMF class " + pmfClass.getName(), e);
    }
  }

  protected void invokeGetPMF(Object... args) throws Exception {
    Method getPMF = getStaticGetPMFMethod();
    getPMF.invoke(null, args);
  }

  @Test
  public void testUnknownStandardProperty() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property", "value");
    try {
      invokeGetPMF(p);
      fail(
          "testUnknownStandardProperty should result in JDOUserException. "
              + "No exception was thrown.");
    } catch (InvocationTargetException ite) {
      Throwable cause = ite.getCause();
      if (cause != null && cause instanceof JDOUserException) {
        // :)
      } else {
        throw new JDOException("PMF threw " + cause + " instead of JDOException");
      }
    } catch (Exception e) {
      throw new JDOException(
          "failed to invoke static getPersistenceManagerFactory(Map) method on PMF class", e);
    }
  }

  @Test
  public void testUnknownStandardProperties() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property.1", "value");
    p.setProperty("javax.jdo.unknown.standard.property.2", "value");

    JDOUserException x = null;

    try {
      invokeGetPMF(p);
      fail(
          "testUnknownStandardProperties should result in JDOUserException. "
              + "No exception was thrown.");
    } catch (InvocationTargetException ite) {
      Throwable cause = ite.getCause();
      if (cause != null && cause instanceof JDOUserException) {
        x = (JDOUserException) cause;
      } else {
        throw new JDOException("PMF threw " + cause + " instead of JDOException");
      }
    } catch (Exception e) {
      throw new JDOException(
          "failed to invoke static getPersistenceManagerFactory(Map) method on PMF class", e);
    }

    Throwable[] nesteds = x.getNestedExceptions();

    Assertions.assertNotNull(nesteds);
    Assertions.assertEquals(2, nesteds.length, "should have been 2 nested exceptions");
    for (int i = 0; i < nesteds.length; i++) {
      Throwable t = nesteds[i];
      Assertions.assertTrue(
          t instanceof JDOUserException,
          "nested exception " + i + " should have been JDOUserException");
    }
  }
}
