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

package org.apache.jdo.tck.api.persistencemanager.fetchplan;

import java.util.Collection;
import java.util.HashSet;
import org.apache.jdo.tck.api.persistencemanager.fetchplan.AbstractFetchPlanTest;

import org.apache.jdo.tck.pc.mylib.PCRect;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test FetchPlanRefresh <br>
 * <B>Keywords:</B> FetchPlan getObjectById <br>
 * <B>Assertion IDs:</B> Assertion 12.7.1 <br>
 * <B>Assertion Description: </B> 12.7.1 For refresh, after clearing fields in the instances, the
 * JDO implementation uses the fetch plan to determine which fields to load from the datastore.
 */
public class FetchPlanRefresh extends AbstractFetchPlanTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion 12.7.1 (FetchPlanRefresh) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(FetchPlanRefresh.class);
  }

  /** */
  public void testRefresh() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    pm.refresh(instance);
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  public void testRefreshAllCollection() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    Collection instances = new HashSet();
    instances.add(instance);
    pm.refreshAll(instances);
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  public void testRefreshAllArray() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    Object[] instances = new Object[] {instance};
    pm.refreshAll(instances);
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }
}
