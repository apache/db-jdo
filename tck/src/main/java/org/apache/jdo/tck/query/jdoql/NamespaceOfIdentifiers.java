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

package org.apache.jdo.tck.query.jdoql;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Namespace of Identifiers
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-11.
 *<BR>
 *<B>Assertion Description: </B>
 * Identifiers in the expression are considered to be in the name
 * space of the specified class, with the addition of declared
 * imports, parameters and variables. 
 */

public class NamespaceOfIdentifiers extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-11 (NamespaceOfIdentifiers) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NamespaceOfIdentifiers.class);
    }
    
    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Collection instance9 = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 9").execute();
        Collection allInstances = (Collection)pm.newQuery(
            PrimitiveTypes.class, "true").execute();
        Collection empty = new HashSet();
        
        // use of field names
        runSimplePrimitiveTypesQuery("intNotNull == intNotNull", 
                                     pm, allInstances, ASSERTION_FAILED);
        
        // use of parameter names
        runParameterPrimitiveTypesQuery(
            "intNotNull == intNotNull", "String intNotNull", "Michael",
             pm, allInstances, ASSERTION_FAILED);
        
        // use of field/parameter names
        runParameterPrimitiveTypesQuery(
            "this.intNotNull == intNotNull", "int intNotNull", Integer.valueOf(9),
             pm, instance9, ASSERTION_FAILED);

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
