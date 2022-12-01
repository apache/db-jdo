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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;
import java.util.HashSet;
import javax.jdo.datastore.DataStoreCache;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> DataStoreCacheTest <br>
 * <B>Keywords:</B> DataStoreCache cache pin unpin evict <br>
 * <B>Assertion ID:</B> A11.8 <br>
 * <B>Assertion Description: </B> Most JDO implementations allow instances to be cached in a
 * second-level cache, and allow direct management of the cache by knowledgeable applications. The
 * second-level cache is typically a single VM cache and is used for persistent instances associated
 * with a single PersistenceManagerFactory. For the purpose of standardizing this behavior, the
 * DataStoreCache interface is used.
 */
public class DataStoreCacheTest extends PersistenceManagerTest {

  Object pointoid;
  Collection<Object> pointoidCollection;
  Object[] pointoidArray;

  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.8 (DataStoreCacheTest) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DataStoreCacheTest.class);
  }

  /**
   * In setup, create a persistent instance and get its oid. The oid is a valid parameter to the
   * cache APIs.
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
    PCPoint point = new PCPoint(50, 100);
    getPM().currentTransaction().begin();
    pm.makePersistent(point);
    pointoid = pm.getObjectId(point);
    pointoidCollection = new HashSet<>();
    pointoidCollection.add(pointoid);
    pointoidArray = new Object[] {pointoid};
    pm.currentTransaction().commit();
  }

  /**
   * There is no mandated behavior of the DataStoreCache methods. This test makes sure that the
   * instance returned does not throw exceptions on any method.
   */
  public void testDataStoreCache() {
    DataStoreCache ds = getPMF().getDataStoreCache();
    ds.evict(pointoid);
    ds.evictAll();
    ds.evictAll(pointoidCollection);
    ds.evictAll(pointoidArray);
    ds.evictAll(PCPoint.class, true);
    ds.pin(pointoid);
    ds.unpin(pointoid);
    ds.pinAll(pointoidCollection);
    ds.unpinAll(pointoidCollection);
    ds.pinAll(pointoidArray);
    ds.unpinAll(pointoidArray);
    ds.pinAll(PCPoint.class, true);
    ds.unpinAll(PCPoint.class, true);
  }
}
