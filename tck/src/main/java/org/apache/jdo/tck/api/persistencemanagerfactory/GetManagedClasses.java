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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Collection;
import javax.jdo.Query;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>GetManagedClasses of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> Test output from PersistenceManagerFactory.getManagedClasses().
 */
public class GetManagedClasses extends JDO_Test {

  /** */
  @SuppressWarnings("rawtypes")
  @Test
  public void test() {
    try {
      // Get PMF and a PM, and do an Extent on some classes
      pmf = getPMF();
      pm = pmf.getPersistenceManager();
      try {
        pm.currentTransaction().begin();

        pm.getExtent(PCPoint.class);

        Query<PCRect> q = pm.newQuery(PCRect.class);
        q.execute();

        pm.currentTransaction().rollback();
      } catch (Exception e) {
        fail("Exception failed accessing Extents");
      } finally {
        if (pm.currentTransaction().isActive()) {
          pm.currentTransaction().rollback();
        }
      }

      // Check that the classes are now managed
      Collection<Class> managedClasses = pmf.getManagedClasses();
      Assertions.assertTrue(
          managedClasses.contains(PCPoint.class), "PCPoint ought to be managed but isnt");
      Assertions.assertTrue(
          managedClasses.contains(PCRect.class), "PCRect ought to be managed but isnt");
    } finally {
      pm.close();
    }
  }
}
