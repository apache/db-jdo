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

package org.apache.jdo.tck.query.jdoql.variables;

import java.util.LinkedList;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.query.NoExtent;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Variables without Extent. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.5-2. <br>
 * <B>Assertion Description: </B> If the class does not manage an Extent, then no results will
 * satisfy the query.
 */
public class VariablesWithoutExtent extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.5-2 (VariablesWithoutExtent) failed: ";

  /** */
  @Test
  public void testPositive() {
    if (isUnconstrainedVariablesSupported()) {

      Object expected = new LinkedList<>();

      QueryElementHolder<Person> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Person.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "this.personid = noExtent.id",
              /*VARIABLES*/ "NoExtent noExtent",
              /*PARAMETERS*/ null,
              /*IMPORTS*/ "import org.apache.jdo.tck.pc.query.NoExtent;",
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    }
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
    NoExtent noExtent = new NoExtent(1);
    makePersistent(noExtent);
    addTearDownInstance(noExtent);
    logger.info("Michael " + noExtent);
    logger.info("ID " + JDOHelper.getObjectId(noExtent));
  }

  /**
   * Makes the given instance persistent.
   *
   * @param o the instance to be made persistent.
   */
  private void makePersistent(Object o) {
    PersistenceManager pm = getPM();
    Transaction transaction = pm.currentTransaction();
    transaction.begin();
    try {
      pm.makePersistent(o);
      transaction.commit();
    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }
}
