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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jdo.Constants;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Close of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory, jdoconfig.xml <br>
 * <B>Assertion IDs:</B> A8.7-1 <br>
 * <B>Assertion Description: </B> The application provides a resource named META-INF/persistence.xml
 * and optionally META-INF/services/javax.persistence.EntityManagerFactory which contain
 * configuration information
 */
public class Persistence extends JDO_Test {

  /** Creates a new instance of Jdoconfig */
  public Persistence() {}
  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.1.2-1 failed: ";
  // Do not use superclass pmf, pm
  private PersistenceManagerFactory pmf = null;
  private PersistenceManager pm = null;

  /** */
  @Test
  public void testGetPMFfromNamedPU() {
    String name = "PUNamed_" + IDENTITYTYPE + "0";
    pmf = JDOHelper.getPersistenceManagerFactory(name);
    Assertions.assertEquals(false, pmf.getRestoreValues(), "Incorrect value for RestoreValues");
    runTest(name);
  }

  /** */
  @Test
  public void testGetPMFfromNamedPUWithNullOverrides() {
    String name = "PUNamed_" + IDENTITYTYPE + "0";
    pmf = JDOHelper.getPersistenceManagerFactory(null, name);
    Assertions.assertEquals(false, pmf.getRestoreValues(), "Incorrect value for RestoreValues");
    runTest(name);
  }

  /** */
  @Test
  public void testGetPMFfromNamedPUWithEmptyOverrides() {
    String name = "PUNamed_" + IDENTITYTYPE + "0";
    Map<Object, Object> overrides = new HashMap<>();
    pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    Assertions.assertEquals(false, pmf.getRestoreValues(), "Incorrect value for RestoreValues");
    runTest(name);
  }

  /** */
  @Test
  public void testGetPMFfromNamedPUWithOverrides() {
    String name = "PUNamed_" + IDENTITYTYPE + "0";
    Properties overrides = new Properties();
    overrides.setProperty(Constants.PROPERTY_RESTORE_VALUES, "true");
    pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    Assertions.assertEquals(true, pmf.getRestoreValues(), "Incorrect value for RestoreValues");
    runTest(name);
  }

  /** */
  @Test
  public void testGetPMFfromNamedPUWithWhiteSpace() {
    String name = "PUNamed_" + IDENTITYTYPE + "0";
    pmf = JDOHelper.getPersistenceManagerFactory(" \t" + name + " \n");
    Assertions.assertEquals(false, pmf.getRestoreValues(), "Incorrect value for RestoreValues");
    runTest(name);
  }

  /**
   * @param name the name
   */
  public void runTest(String name) {
    String actualPUName = pmf.getPersistenceUnitName();
    Assertions.assertEquals(name, actualPUName, "Incorrect PersistenceUnitName");

    // check pmf.isClosed() before and after pmf.close()
    if (pmf.isClosed()) {
      fail(ASSERTION_FAILED, "PMF.isClosed() returned true on an open pmf");
    }
    makePersistent();

    closePMF(pmf); // don't use closePMF() because that sets pmf to null

    if (!pmf.isClosed()) {
      fail(ASSERTION_FAILED, "PMF.isClosed() returned false on a closed pmf");
    }
    // have next invocation of getPMF() get a new pmf
    pmf = null;
  }

  protected void makePersistent() {
    addTearDownClass(PCPoint.class);
    pm = pmf.getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    PCPoint comp = new PCPoint(1, 2);
    pm.makePersistent(comp);
    tx.commit();
  }
}
