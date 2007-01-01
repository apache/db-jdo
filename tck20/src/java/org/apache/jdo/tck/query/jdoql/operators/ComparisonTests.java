/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package org.apache.jdo.tck.query.jdoql.operators;

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.AllTypes;

public abstract class ComparisonTests extends JDO_Test {    
    protected   Query               query;
    protected   Transaction         tx;
    protected   Collection          query_result;
        
    protected static    String      BooleanParameter = "Boolean value";
    protected static    String      booleanParameter = "boolean value";
    protected static    String      ByteParameter = "Byte value";
    protected static    String      byteParameter = "byte value";
    protected static    String      CharacterParameter = "Character value";
    protected static    String      charParameter = "char value";
    protected static    String      DoubleParameter = "Double value";
    protected static    String      doubleParameter = "double value";
    protected static    String      FloatParameter = "Float value";
    protected static    String      floatParameter = "float value";
    protected static    String      IntegerParameter = "Integer value";
    protected static    String      intParameter = "int value";
    protected static    String      LongParameter = "Long value";
    protected static    String      longParameter = "long value";
    protected static    String      ShortParameter = "Short value";
    protected static    String      shortParameter = "short value";
    protected static    String      StringParameter = "String value";
    protected static    String      LocaleParameter = "java.util.Locale value";
    protected static    String      BigDecimalParameter = "java.math.BigDecimal value";
    protected static    String      BigIntegerParameter = "java.math.BigInteger value";
    protected static    String      DateParameter = "java.util.Date value";
    protected static    String      AllTypesParameter = "org.apache.jdo.tck.pc.fieldtypes.AllTypes value";
    
    /** */
    protected void fail(String assertion, String message, String filter, String parameter)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(message);
        buf.append("(");
        buf.append(" filter \"").append(filter).append("\"");
        if (parameter != null) {
            buf.append(" , parameter \"").append(parameter).append("\"");
        }
        buf.append(")");
        
        fail(assertion, buf.toString());
    }
    
    /** */
    protected void runQuery(PersistenceManager pm, 
                            String filter, String parameter, Object parameterValue, 
                            String assertion)
    {
        Extent e = pm.getExtent(AllTypes.class, false);
        query = pm.newQuery(e, filter);
        query_result = null;
        try {
            if (parameter != null) {
                query.declareParameters(parameter);
                query_result = (Collection) query.execute(parameterValue);
            } 
            else {
                query_result = (Collection) query.execute();
            }
        } 
        catch (Throwable throwable) {
            if (debug)
                throwable.printStackTrace();
            fail(assertion, "Exception on Query.execute " + throwable, filter, parameter);
            query_result = null;
            if (tx.isActive()) 
                tx.rollback();
            return;
        }
        if (query_result == null) {
            fail(assertion, "Query.execute returned a null", filter, parameter);
            if (tx.isActive()) 
                tx.rollback();
        }
    }    
}

