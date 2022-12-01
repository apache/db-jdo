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

import javax.jdo.Query;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test FetchPlanQuery <br>
 * <B>Keywords:</B> FetchPlan Query <br>
 * <B>Assertion IDs:</B> Assertion 12.7.1 <br>
 * <B>Assertion Description: </B> 12.7.1 When executing a query the JDO implementation loads the
 * fields as specified in the fetch plan associated with the Query instance.
 */
public class FetchPlanQuery extends AbstractFetchPlanTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion 12.7.1 (FetchPlanQuery) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(FetchPlanQuery.class);
  }

  /** */
  public void testQuery() {
    setBothGroup();
    pm.currentTransaction().begin();
    Query<PCRect> query = pm.newQuery(PCRect.class);
    checkGroups(
        ASSERTION_FAILED + " after newQuery().getFetchPlan()", query.getFetchPlan(), bothGroup);
    query.setUnique(true);
    PCRect instance = (PCRect) query.execute();
    checkBothLoaded(ASSERTION_FAILED + " after query.execute()", instance);
    pm.currentTransaction().commit();
    failOnError();
  }
}
