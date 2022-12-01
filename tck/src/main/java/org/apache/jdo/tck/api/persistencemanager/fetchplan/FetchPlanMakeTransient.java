/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager.fetchplan;

import java.util.Collection;
import java.util.HashSet;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test FetchPlanMakeTransient <br>
 * <B>Keywords:</B> FetchPlan makeTransient <br>
 * <B>Assertion IDs:</B> Assertion 12.7.1 <br>
 * <B>Assertion Description: </B> If the useFetchPlan parameter is true, the current FetchPlan,
 * including MaxFetchDepth, DETACH_LOAD_FIELDS, and DETACH_UNLOAD_FIELDS, is applied to the pc or
 * pcs parameter instance(s) to load fields and instances from the datastore. The DetachmentRoots is
 * not affected. After the fetch plan is used to load instances, the entire graph of instances
 * reachable via loaded fields of the parameter instances is made transient. Transient fields are
 * not modified by the method.
 */
public class FetchPlanMakeTransient extends AbstractFetchPlanTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion 12.7.1 (FetchPlanMakeTransient) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(FetchPlanMakeTransient.class);
  }

  /** */
  public void testMakeTransientAllCollection() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    Collection<PCRect> instances = new HashSet<>();
    instances.add(instance);
    pm.makeTransientAll(instances, true);
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  public void testMakeTransientAllArray() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    pm.makeTransientAll(true, new Object[] {instance});
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  public void testMakeTransient() {
    setBothGroup();
    pm.currentTransaction().begin();
    PCRect instance = (PCRect) pm.getObjectById(pcrectoid, false);
    pm.makeTransient(instance, true);
    checkBothLoaded(ASSERTION_FAILED, instance);
    pm.currentTransaction().commit();
    failOnError();
  }
}
