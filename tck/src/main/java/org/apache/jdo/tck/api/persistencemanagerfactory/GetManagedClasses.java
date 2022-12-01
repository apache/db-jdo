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

package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Collection;
import javax.jdo.Query;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>GetManagedClasses of PersistenceManagerFactory <br>
 * <B>Keywords:</B> persistencemanagerfactory <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> Test output from PersistenceManagerFactory.getManagedClasses().
 */
public class GetManagedClasses extends JDO_Test {

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetManagedClasses.class);
  }

  /** */
  @SuppressWarnings("rawtypes")
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
      assertTrue("PCPoint ought to be managed but isnt", managedClasses.contains(PCPoint.class));
      assertTrue("PCRect ought to be managed but isnt", managedClasses.contains(PCRect.class));
    } finally {
      pm.close();
    }
  }
}
