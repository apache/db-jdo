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
import java.util.HashSet;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Boolean Logical OR Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-22.
 *<BR>
 *<B>Assertion Description: </B>
The Boolean logical OR (not bitwise) operator (<code>|</code>) is supported for
all types as they are defined in the Java language.
This includes the following types:
<UL>
<LI><code>Boolean, boolean</code></LI>
</UL>
 */

public class BooleanLogicalOR extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-22 (BooleanLogicalOR) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(BooleanLogicalOR.class);
    }
    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        if (debug) logger.debug("\nExecuting positive test BooleanLogicalOR() ...");

        Transaction tx = pm.currentTransaction();
        tx.begin();

        Collection instance9 = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 9").execute();
        Collection instancesLess3 = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id < 3").execute();
        Collection allOddInstances = (Collection)pm.newQuery(
            PrimitiveTypes.class, "booleanNull").execute();
        Collection allInstances = (Collection)pm.newQuery(
            PrimitiveTypes.class, "true").execute();
        Collection empty = new HashSet();
        
        // case true | true
        runSimplePrimitiveTypesQuery("true | true", 
                                     pm, allInstances, ASSERTION_FAILED);

        // case true | false
        runSimplePrimitiveTypesQuery("true | false", 
                                     pm, allInstances, ASSERTION_FAILED);

        // case false | true
        runSimplePrimitiveTypesQuery("false | true", 
                                     pm, allInstances, ASSERTION_FAILED);

        // case false | false
        runSimplePrimitiveTypesQuery("false | false", 
                                     pm, empty, ASSERTION_FAILED);

        // case boolean | boolean
        runSimplePrimitiveTypesQuery("intNotNull == 9 | booleanNotNull", 
                                     pm, allOddInstances, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("id == 1 | id == 2", 
                                     pm, instancesLess3, ASSERTION_FAILED);

        // case boolean | Boolean
        runSimplePrimitiveTypesQuery("intNotNull == 9 | booleanNull", 
                                     pm, allOddInstances, ASSERTION_FAILED);
        // case Boolean | boolean
        runSimplePrimitiveTypesQuery("booleanNull | intNotNull == 9", 
                                     pm, allOddInstances, ASSERTION_FAILED);
        // case Boolean | Boolean
        runSimplePrimitiveTypesQuery("booleanNull | booleanNull", 
                                     pm, allOddInstances, ASSERTION_FAILED);

        // case Boolean parameter
        runParameterPrimitiveTypesQuery("param | id == 9", 
                                        "Boolean param", Boolean.TRUE,
                                        pm, allInstances, ASSERTION_FAILED);
        runParameterPrimitiveTypesQuery("param | id == 9", 
                                        "Boolean param", Boolean.FALSE,
                                        pm, instance9, ASSERTION_FAILED);

        // case boolean parameter
        runParameterPrimitiveTypesQuery("param | id == 9", 
                                        "boolean param", Boolean.TRUE,
                                        pm, allInstances, ASSERTION_FAILED);
        runParameterPrimitiveTypesQuery("param | id == 9", 
                                        "boolean param", Boolean.FALSE,
                                        pm, instance9, ASSERTION_FAILED);
        tx.commit();
    }

    /** */
    public void testNegative() {
        PersistenceManager pm = getPM();
        if (debug) logger.debug("\nExecuting positive test BooleanLogicalAND() ...");

        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try {
            Query q = pm.newQuery(PrimitiveTypes.class, 
                                  "stringNull | stringNull");
            Object result = q.execute();
            fail(ASSERTION_FAILED,
                 "Query using | operator for non-supported types should throw JDOUserException.");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) {
                logger.debug("expected exception: " + ex);
            }
        }
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

