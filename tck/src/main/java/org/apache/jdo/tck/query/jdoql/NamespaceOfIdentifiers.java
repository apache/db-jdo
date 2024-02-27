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

package org.apache.jdo.tck.query.jdoql;

import java.util.Collections;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Namespace of Identifiers <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-11. <br>
 * <B>Assertion Description: </B> Identifiers in the expression are considered to be in the name
 * space of the specified class, with the addition of declared imports, parameters and variables.
 */
public class NamespaceOfIdentifiers extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-11 (NamespaceOfIdentifiers) failed: ";

  /** */
  @Test
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();

    List<PrimitiveTypes> instance9 = pm.newQuery(PrimitiveTypes.class, "id == 9").executeList();
    List<PrimitiveTypes> allInstances = pm.newQuery(PrimitiveTypes.class, "true").executeList();
    List<PrimitiveTypes> empty = Collections.emptyList();

    // use of field names
    runSimplePrimitiveTypesQuery("intNotNull == intNotNull", pm, allInstances, ASSERTION_FAILED);

    // use of parameter names
    runParameterPrimitiveTypesQuery(
        "intNotNull == intNotNull",
        "String intNotNull",
        "Michael",
        pm,
        allInstances,
        ASSERTION_FAILED);

    // use of field/parameter names
    runParameterPrimitiveTypesQuery(
        "this.intNotNull == intNotNull",
        "int intNotNull",
        Integer.valueOf(9),
        pm,
        instance9,
        ASSERTION_FAILED);

    tx.commit();
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
