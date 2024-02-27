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
 * <B>Assertion IDs:</B> A11.1.2-1 <br>
 * <B>Assertion Description: </B> Users can access a PersistenceManagerFactory by creating a
 * jdoconfig.xml file and making it available on the class path as META-INF/jdoconfig.xml.
 */
public class Jdoconfig extends JDO_Test {

  /** Creates a new instance of Jdoconfig */
  public Jdoconfig() {}
  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.1.2-1 failed: ";

  private static final String ANONYMOUS_PMF_NAME = "";
  // Do not use superclass pmf, pm
  private PersistenceManagerFactory privatePmf = null;
  private PersistenceManager privatePm = null;
  private final Map<String, String> overrides = new HashMap<>();

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    /* Instance can be persisted ONLY if javax.jdo.mapping.Schema
     *   is overriden with
     *   JDOHelper.getPersistenceManagerFactory(Map overrides, ...).
     *   The value depends on identity type so can only be known at runtime.
     */
    if (isTestToBePerformed()) {
      overrides.put("javax.jdo.mapping.Schema", SCHEMANAME);
    }
  }

  /** */
  @Test
  public void testGetPMFNoArgs() {
    privatePmf = JDOHelper.getPersistenceManagerFactory();
    checkIsOpen(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFEmptyString() {
    String name = "";
    privatePmf = JDOHelper.getPersistenceManagerFactory(name);
    checkIsOpen(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFNull() {
    String name = null;
    privatePmf = JDOHelper.getPersistenceManagerFactory(name);
    checkIsOpen(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFStringSpace() {
    String name = " ";
    privatePmf = JDOHelper.getPersistenceManagerFactory(name);
    checkIsOpen(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFNamed() {
    String name = "namedPMF0";
    privatePmf = JDOHelper.getPersistenceManagerFactory(name);
    Assertions.assertEquals(
        privatePmf.getRestoreValues(), false, "Incorrect value for RestoreValues");
    checkIsOpen(name);
  }

  /** */
  @Test
  public void testGetPMFEmptyStringOverrides() {
    String name = "";
    privatePmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    checkPersistent(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFNullOverrides() {
    String name = null;
    privatePmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    checkPersistent(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFStringSpaceOverrides() {
    String name = " ";
    privatePmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    checkPersistent(ANONYMOUS_PMF_NAME);
  }

  /** */
  @Test
  public void testGetPMFNamedOverrides() {
    String name = "namedPMF0";
    privatePmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
    Assertions.assertEquals(
        privatePmf.getRestoreValues(), false, "Incorrect value for RestoreValues");
    checkPersistent(name);
  }

  /** */
  @Test
  public void testGetPMFNamedSpacesOverrides() {
    String name = "namedPMF1";
    privatePmf = JDOHelper.getPersistenceManagerFactory(overrides, " \t" + name + " \n");
    Assertions.assertEquals(
        privatePmf.getRestoreValues(), true, "Incorrect value for RestoreValues");
    checkPersistent(name);
  }

  /**
   * @param name the name
   */
  public void checkIsOpen(String name) {
    Assertions.assertEquals(name, privatePmf.getName(), "Incorrect PMF name");
    if (privatePmf.isClosed()) {
      fail(ASSERTION_FAILED, "PMF.isClosed() returned true on an open pmf");
    }
    closePMF(privatePmf);
    // have next invocation of getPMF() get a new pmf
    privatePmf = null;
  }

  /**
   * Checks if instance can be persisted. Can be used if javax.jdo.mapping.Schema is overriden with
   * the correct value with JDOHelper.getPersistenceManagerFactory(Map overrides, ...).
   *
   * @param name the name
   */
  public void checkPersistent(String name) {
    Assertions.assertEquals(name, privatePmf.getName(), "Incorrect PMF name");

    makePersistent();

    closePMF(privatePmf);
    if (!privatePmf.isClosed()) {
      fail(ASSERTION_FAILED, "PMF.isClosed() returned false on a closed pmf");
    }
    // have next invocation of getPMF() get a new pmf
    privatePmf = null;
  }

  protected void makePersistent() {
    addTearDownClass(PCPoint.class);
    privatePm = privatePmf.getPersistenceManager();
    Transaction tx = privatePm.currentTransaction();
    tx.begin();
    PCPoint comp = new PCPoint(1, 2);
    privatePm.makePersistent(comp);
    tx.commit();
  }
}
