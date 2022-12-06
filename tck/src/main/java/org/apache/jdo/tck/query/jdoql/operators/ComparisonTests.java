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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.List;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.AllTypes;

public abstract class ComparisonTests extends JDO_Test {
  protected Query<AllTypes> query;
  protected Transaction tx;
  protected List<AllTypes> queryResult;

  protected static final String BooleanParameter = "Boolean value";
  protected static final String booleanParameter = "boolean value";
  protected static final String ByteParameter = "Byte value";
  protected static final String byteParameter = "byte value";
  protected static final String CharacterParameter = "Character value";
  protected static final String charParameter = "char value";
  protected static final String DoubleParameter = "Double value";
  protected static final String doubleParameter = "double value";
  protected static final String FloatParameter = "Float value";
  protected static final String floatParameter = "float value";
  protected static final String IntegerParameter = "Integer value";
  protected static final String intParameter = "int value";
  protected static final String LongParameter = "Long value";
  protected static final String longParameter = "long value";
  protected static final String ShortParameter = "Short value";
  protected static final String shortParameter = "short value";
  protected static final String StringParameter = "String value";
  protected static final String LocaleParameter = "java.util.Locale value";
  protected static final String BigDecimalParameter = "java.math.BigDecimal value";
  protected static final String BigIntegerParameter = "java.math.BigInteger value";
  protected static final String DateParameter = "java.util.Date value";
  protected static final String AllTypesParameter =
      "org.apache.jdo.tck.pc.fieldtypes.AllTypes value";

  /**
   * @param assertion assertion
   * @param message the message
   * @param filter the filter
   * @param parameter the parameter
   */
  protected void fail(String assertion, String message, String filter, String parameter) {
    StringBuilder buf = new StringBuilder();
    buf.append(message);
    buf.append("(");
    buf.append(" filter \"").append(filter).append("\"");
    if (parameter != null) {
      buf.append(" , parameter \"").append(parameter).append("\"");
    }
    buf.append(")");

    fail(assertion, buf.toString());
  }

  /**
   * @param pm the PersistenceManager
   * @param filter the filter
   * @param parameter the parameter declaration
   * @param parameterValue the parameter value
   * @param assertion assertion
   */
  protected void runQuery(
      PersistenceManager pm,
      String filter,
      String parameter,
      Object parameterValue,
      String assertion) {
    Extent<AllTypes> e = pm.getExtent(AllTypes.class, false);
    query = pm.newQuery(e, filter);
    queryResult = null;
    try {
      if (parameter != null) {
        query.declareParameters(parameter);
        query.setParameters(parameterValue);
      }
      queryResult = query.executeList();
    } catch (Throwable throwable) {
      if (debug) throwable.printStackTrace();
      fail(assertion, "Exception on Query.execute " + throwable, filter, parameter);
      queryResult = null;
      if (tx.isActive()) tx.rollback();
      return;
    }
    if (queryResult == null) {
      fail(assertion, "Query.execute returned a null", filter, parameter);
      if (tx.isActive()) tx.rollback();
    }
  }
}
