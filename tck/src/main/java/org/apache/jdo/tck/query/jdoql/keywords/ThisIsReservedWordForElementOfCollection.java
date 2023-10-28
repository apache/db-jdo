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

package org.apache.jdo.tck.query.jdoql.keywords;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> This is Reserved Word for Element of Collection <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-12. <br>
 * <B>Assertion Description: </B> <code>this</code> is a reserved word which means the element of
 * the candidate collection being evaluated.
 */
public class ThisIsReservedWordForElementOfCollection extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-12 (ThisIsReservedWordForElementOfCollection) failed: ";

  /** */
  @Test
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();

    List<PrimitiveTypes> instance9 = pm.newQuery(PrimitiveTypes.class, "id == 9").executeList();

    // compare this with a parameter
    runParameterPrimitiveTypesQuery(
        "this == param",
        "PrimitiveTypes param",
        instance9.iterator().next(),
        pm,
        instance9,
        ASSERTION_FAILED);

    // use this to access a field
    runParameterPrimitiveTypesQuery(
        "this.intNotNull == intNotNull",
        "int intNotNull",
        Integer.valueOf(9),
        pm,
        instance9,
        ASSERTION_FAILED);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PrimitiveTypes.class);
    loadAndPersistPrimitiveTypes(getPM());
  }
}
