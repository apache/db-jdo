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
import org.apache.jdo.tck.pc.companyMapWithoutJoin.CompanyFactoryRegistry;
import org.apache.jdo.tck.pc.companyMapWithoutJoin.CompanyModelReader;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Completeness Test Map <br>
 * <B>Keywords:</B> mapping <br>
 * <B>Assertion ID:</B> A18.[not identified] <br>
 * <B>Assertion Description: </B>
 */
public class CompletenessTestMap extends AbstractReaderTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A18-[not identified] failed: ";

  /** */
  private final boolean isTestToBePerformed = isTestToBePerformed();

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    if (isTestToBePerformed) {
      getPM();
      CompanyFactoryRegistry.registerFactory(pm);
      CompanyModelReader reader = new CompanyModelReader(inputFilename);
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
    if (isTestToBePerformed) {
      // register the default factory
      CompanyFactoryRegistry.registerFactory();
      // get new obj graph to compare persistent graph with
      CompanyModelReader reader = new CompanyModelReader(inputFilename);
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
        fail("CompletenessTestMap failed; see list of failures below:", msg.toString());
      }
    }
  }
}
