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

package org.apache.jdo.tck.mapping;

import java.util.ArrayList;
import java.util.List;
import org.apache.jdo.tck.AbstractReaderTest;
import org.apache.jdo.tck.pc.order.OrderFactoryRegistry;
import org.apache.jdo.tck.pc.order.OrderModelReader2;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Completeness Test for the Order model <br>
 * <B>Keywords:</B> mapping compound identity <br>
 * <B>Assertion ID:</B> A5.4.1-5 <br>
 * <B>Assertion Description: Compound identity is a special case of application identity. References
 * to other persistence-capable classes can be defined as key fields. In this case, the object id
 * class contains a field that is of the type of the object id of the relationship field.</B>
 */
public class CompletenessTestOrder extends AbstractReaderTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A5.4.1-5[Compound identity is a special case of application identity. References to other persistence-capable classes can be defined as key fields. In this case, the object id class contains a field that is of the type of the object id of the relationship field.] failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    if (runsWithApplicationIdentity()) {
      getPM();
      OrderFactoryRegistry.registerFactory(pm);
      OrderModelReader2 reader = new OrderModelReader2(inputFilename);
      addTearDownClass(reader.getTearDownClassesFromFactory());
      // persist test data
      pm.currentTransaction().begin();
      List<Object> rootList = getRootList(reader);
      pm.makePersistentAll(rootList);
      rootOids = new ArrayList<>();
      for (Object pc : rootList) {
        rootOids.add(pm.getObjectId(pc));
      }
      pm.currentTransaction().commit();
      cleanupPM();
    }
  }

  /** */
  @Test
  public void test() {
    if (runsWithApplicationIdentity()) {
      // register the default factory
      OrderFactoryRegistry.registerFactory();
      // get new obj graph to compare persistent graph with
      OrderModelReader2 reader = new OrderModelReader2(inputFilename);
      List<Object> rootList = getRootList(reader);

      getPM();
      pm.currentTransaction().begin();
      // compare persisted and new
      int size = rootList.size();
      StringBuilder msg = new StringBuilder();
      for (int i = 0; i < size; i++) {
        DeepEquality expected = (DeepEquality) rootList.get(i);
        Object oid = rootOids.get(i);
        Object persisted = pm.getObjectById(oid);
        EqualityHelper equalityHelper = new EqualityHelper();
        if (!expected.deepCompareFields(persisted, equalityHelper)) {
          if (msg.length() > 0) {
            msg.append("\n");
          }
          msg.append(
              "Expected this  instance:\n    "
                  + expected
                  + "\n"
                  + "Got persistent instance:"
                  + "\n    "
                  + persisted
                  + "\n"
                  + "Detailed list of differences follows...\n");
          msg.append(equalityHelper.getUnequalBuffer());
        }
      }
      pm.currentTransaction().commit();
      // fail test if at least one of the instances is not the expected one
      if (msg.length() > 0) {
        fail("CompletenessTestOrder failed; see list of failures below:", msg.toString());
      }
    }
  }
}
