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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;
/**
 *<B>Title:</B> Bitwise Complement Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-24.
 *<BR>
 *<B>Assertion Description: </B>
The integral unary bitwise complement operator (<code>~</code>) is supported
for all types as they are defined in the Java language.
This includes the following types:
<UL>
<LI><code>byte, short, int, long, char, Byte, Short Integer, Long, Character</code></LI>
</UL>

 */

public class BitwiseComplement extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-24 (BitwiseComplement) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(BitwiseComplement.class);
    }
    
    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Collection instance9 = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 9").execute();
        Collection allOddInstances = (Collection)pm.newQuery(
            PrimitiveTypes.class, "booleanNull").execute();
        
        runSimplePrimitiveTypesQuery("~id == -10", 
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~byteNotNull == -10",
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~shortNotNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);            
        runSimplePrimitiveTypesQuery("~intNotNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);            
        runSimplePrimitiveTypesQuery("~longNotNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~byteNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~shortNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~intNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~longNull == -10", 
                                     pm, instance9, ASSERTION_FAILED);
  

        runSimplePrimitiveTypesQuery("~charNull == -80", 
                                     pm, allOddInstances, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("~charNotNull == -80", 
                                     pm, allOddInstances, ASSERTION_FAILED);
        
        tx.commit();
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PrimitiveTypes.class);
        loadAndPersistPrimitiveTypes(getPM());
    }
}
