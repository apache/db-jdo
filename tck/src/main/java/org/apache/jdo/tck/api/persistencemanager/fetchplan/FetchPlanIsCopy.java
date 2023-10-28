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

package org.apache.jdo.tck.api.persistencemanager.fetchplan;

import java.util.Arrays;
import javax.jdo.Extent;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test TITLE <br>
 * <B>Keywords:</B> fetch plan <br>
 * <B>Assertion IDs:</B> 12.7.5-1 <br>
 * <B>Assertion Description: </B> Subsequent modifications of the Query FetchPlan are not reflected
 * in the FetchPlan of the PersistenceManager.
 */
public class FetchPlanIsCopy extends AbstractFetchPlanTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion 12.7.5-1 (FetchPlanIsCopy) failed: ";

  Query<PCRect> query = null;
  Extent<PCRect> extent = null;

  /** */
  @Test
  public void testRemoveGroup() {
    beginFPTest();
    query.getFetchPlan().removeGroup("PCRect.upperLeft");
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testRemoveGroup()", query.getFetchPlan(), defaultGroup);
    closeFPTest("testRemoveGroup()");
  }

  /** */
  @Test
  public void testAddGroup() {
    beginFPTest();
    query.getFetchPlan().addGroup("PCRect.lowerRight");
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testAddGroup()", query.getFetchPlan(), bothGroup);
    closeFPTest("testAddGroup()");
  }

  /** */
  @Test
  public void testClearGroups() {
    beginFPTest();
    query.getFetchPlan().clearGroups();
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testClearGroups()", query.getFetchPlan(), new String[] {});
    closeFPTest("testClearGroup()");
  }

  /** */
  @Test
  public void testSetGroup() {
    beginFPTest();
    query.getFetchPlan().setGroup("default");
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testSetGroup()", query.getFetchPlan(), defaultGroup);
    closeFPTest("testSetGroup()");
  }

  /** */
  @Test
  public void testSetGroupsCollection() {
    beginFPTest();
    query.getFetchPlan().setGroups(Arrays.asList(bothGroup));
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testSetGroupsCollection()", query.getFetchPlan(), bothGroup);
    closeFPTest("testSetGroupsCollection()");
  }

  /** */
  @Test
  public void testSetGroupsArray() {
    beginFPTest();
    query.getFetchPlan().setGroups(bothGroup);
    // check that query fetch plan is changed
    checkGroups(ASSERTION_FAILED + " testSetGroupsArray()", query.getFetchPlan(), bothGroup);
    closeFPTest("testSetGroupsArray()");
  }

  /** */
  public void beginFPTest() {
    setUpperLeftGroup();
    pm.currentTransaction().begin();
    query = pm.newQuery(PCRect.class);
    extent = pm.getExtent(PCRect.class);
  }

  /**
   * @param location location
   */
  public void closeFPTest(String location) {
    // check that pm fetch plan is unchanged
    checkGroups(ASSERTION_FAILED + " " + location, pm.getFetchPlan(), upperLeftGroup);
    // check that extent fetch plan is unchanged
    checkGroups(ASSERTION_FAILED + " " + location, extent.getFetchPlan(), upperLeftGroup);
    pm.currentTransaction().rollback();
    failOnError();
  }
}
