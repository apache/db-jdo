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

package org.apache.jdo.tck.query.operators;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Unary Addition Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-25.
 *<BR>
 *<B>Assertion Description: </B>
The unary addition operator (<code>+</code>) is supported for all types as they
are defined in the Java language. This includes the following types:
<UL>
<LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code></LI>
<LI><code>float, double, Float, Double</code></LI>
<LI><code>BigDecimal, BigInteger</code></LI>
</UL>
The operation on object-valued fields of wrapper types <code>(Boolean, Byte,
Short, Integer, Long, Float</code>, and <code>Double</code>), and numeric types
(<code>BigDecimal and BigInteger</code>) use the wrapped values as operands.

 */

public class UnaryPlus extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-25 (UnaryPlus) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(UnaryPlus.class);
    }
    
    /** */
    public void test() {
        pm = getPM();

        try {
            loadPrimitiveTypes(pm);
            runTestBinaryAddition(pm);
        }
        finally {
            cleanupDatabase(pm, PrimitiveTypes.class);
            pm.close();
            pm = null;
        }
    }

    /** */
    void runTestBinaryAddition(PersistenceManager pm) {
        if (debug) logger.debug("\nExecuting test BinaryAddition() ...");

        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Collection instance9 = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 9").execute();
                
        runSimplePrimitiveTypesQuery("+id == 9", 
                                     pm, instance9, ASSERTION_FAILED);
        tx.commit();
    }
}
