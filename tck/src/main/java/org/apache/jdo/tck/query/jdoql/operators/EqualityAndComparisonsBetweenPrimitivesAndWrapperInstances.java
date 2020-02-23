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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Equality and Comparisons Between Primitives and Wrapper
 Instances
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-3.
 *<BR>
 *<B>Assertion Description: </B> Equality and ordering comparisons between 
 * primitives and instances of wrapper classes are valid in a <code>Query</code>
 * filter.
 */

public class EqualityAndComparisonsBetweenPrimitivesAndWrapperInstances 
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-3 (EqualityAndComparisonsBetweenPrimitivesAndWrapperInstances) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(EqualityAndComparisonsBetweenPrimitivesAndWrapperInstances.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
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
        
        // case Integer == int
        runSimplePrimitiveTypesQuery("intNull == 9", 
                                     pm, instance9, ASSERTION_FAILED); 
        
        // case Integer == long
        runSimplePrimitiveTypesQuery("intNull == 9L", 
                                     pm, instance9, ASSERTION_FAILED); 
        
        // case char == Character
        runSimplePrimitiveTypesQuery("'O' == charNull", 
                                     pm, allOddInstances, ASSERTION_FAILED); 
        
        // case int <= Integer
        runParameterPrimitiveTypesQuery(
            "intNotNull <= param", "java.lang.Integer param", new Integer(2),
            pm, instancesLess3, ASSERTION_FAILED); 
        
        // case Double > double
        runSimplePrimitiveTypesQuery("doubleNull > 0.0", 
                                     pm, allInstances, ASSERTION_FAILED); 
        
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
