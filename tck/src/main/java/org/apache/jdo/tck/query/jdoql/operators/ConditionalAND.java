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

import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Conditional AND Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-21.
 *<BR>
 *<B>Assertion Description: </B>
The conditional AND operator (<code>&amp;&amp;</code>) is supported for all
types as they are defined in the Java language.
This includes the following types:
<UL>
<LI><code>Boolean, boolean</code></LI>
</UL>
*/

public class ConditionalAND extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-21 (ConditionalAND) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ConditionalAND.class);
    }
    
    
   /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        if (debug) logger.debug("\nExecuting test ConditionalAND() ...");

        Transaction tx = pm.currentTransaction();
        tx.begin();

        List<PrimitiveTypes> instance9 = pm.newQuery(
            PrimitiveTypes.class, "id == 9").executeList();
        List<PrimitiveTypes> allOddInstances = pm.newQuery(
            PrimitiveTypes.class, "booleanNull").executeList();
        List<PrimitiveTypes> allInstances = pm.newQuery(
            PrimitiveTypes.class, "true").executeList();
        List<PrimitiveTypes> empty = Collections.emptyList();
        
        // case true && true
        runSimplePrimitiveTypesQuery("true && true", 
                                     pm, allInstances, ASSERTION_FAILED);

        // case true && false
        runSimplePrimitiveTypesQuery("true && false", 
                                     pm, empty, ASSERTION_FAILED);

        // case false && true
        runSimplePrimitiveTypesQuery("false && true", 
                                     pm, empty, ASSERTION_FAILED);

        // case false && false
        runSimplePrimitiveTypesQuery("false && false", 
                                     pm, empty, ASSERTION_FAILED);

        // case boolean && boolean
        runSimplePrimitiveTypesQuery("intNotNull == 9 && booleanNotNull", 
                                     pm, instance9, ASSERTION_FAILED);

        // case boolean && Boolean
        runSimplePrimitiveTypesQuery("intNotNull == 9 && booleanNull", 
                                     pm, instance9, ASSERTION_FAILED);
        // case Boolean && boolean
        runSimplePrimitiveTypesQuery("booleanNull && intNotNull == 9", 
                                     pm, instance9, ASSERTION_FAILED);
        // case Boolean && Boolean
        runSimplePrimitiveTypesQuery("booleanNull && booleanNull", 
                                     pm, allOddInstances, ASSERTION_FAILED);

        // case Boolean parameter
        runParameterPrimitiveTypesQuery("param && id == 9", 
                                        "Boolean param", Boolean.TRUE,
                                        pm, instance9, ASSERTION_FAILED);
        runParameterPrimitiveTypesQuery("param && id == 9", 
                                        "Boolean param", Boolean.FALSE,
                                        pm, empty, ASSERTION_FAILED);

        // case boolean parameter
        runParameterPrimitiveTypesQuery("param && id == 9", 
                                        "boolean param", Boolean.TRUE,
                                        pm, instance9, ASSERTION_FAILED);
        runParameterPrimitiveTypesQuery("param && id == 9", 
                                        "boolean param", Boolean.FALSE,
                                        pm, empty, ASSERTION_FAILED);

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
