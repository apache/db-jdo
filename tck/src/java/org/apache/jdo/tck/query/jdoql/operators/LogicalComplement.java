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
 *<B>Title:</B> LogicalComplement Query Operator
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-32.
 *<BR>
 *<B>Assertion Description: </B>
The logical complement operator (<code>!</code>) is supported for all types as
they are defined in the Java language. This includes the following types:
<UL>
<LI><code>Boolean, boolean</code></LI>
</UL>
 */

public class LogicalComplement extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-32 (LogicalComplement) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(LogicalComplement.class);
    }
    
    /**
     * Tests logical complement operator ! used with constants or simple boolean fields
     */
    public void testPositiveSimpleComplement() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection allEvenInstances = (Collection)pm.newQuery(
                PrimitiveTypes.class, "booleanNull == false").execute();
            Collection allInstances = (Collection)pm.newQuery(
                PrimitiveTypes.class, "true").execute();
            Collection empty = new HashSet();

            // case !false
            runSimplePrimitiveTypesQuery(
                    "! false", pm, allInstances, ASSERTION_FAILED);

            // case !true
            runSimplePrimitiveTypesQuery(
                    "! true", pm, empty, ASSERTION_FAILED);

            // case !boolean
            runSimplePrimitiveTypesQuery(
                    "! booleanNotNull", pm, allEvenInstances, ASSERTION_FAILED);

            // case ! Boolean
            runSimplePrimitiveTypesQuery(
                    "! booleanNull", pm, allEvenInstances, ASSERTION_FAILED);

            tx.commit();
            tx = null;
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * Tests logical complement operator ! negating the result of a relational.
     */
    public void testPositiveComplementOfRelationalOp() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection instancesLess3 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id < 3").execute();
            Collection instancesNot3 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id != 3").execute();
            Collection instances3 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id == 3").execute();

            // case !(field >= value)
            runSimplePrimitiveTypesQuery("! (id >= 3)",
                    pm, instancesLess3, ASSERTION_FAILED);

            // case !(field == value)
            runSimplePrimitiveTypesQuery(
                    "! (id == 3)", pm, instancesNot3, ASSERTION_FAILED);

            // case !(field != value)
            runSimplePrimitiveTypesQuery(
                    "! (id != 3)", pm, instances3, ASSERTION_FAILED);

            // case !!(field == value)
            runSimplePrimitiveTypesQuery(
                    "!! (id == 3)", pm, instances3, ASSERTION_FAILED);

            // case !!(field != value)
            runSimplePrimitiveTypesQuery(
                    "!! (id != 3)", pm, instancesNot3, ASSERTION_FAILED);

            tx.commit();
            tx = null;
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * Tests logical complement operator ! negating field comparison with a non null value.
     */
    public void testPositiveNullFieldComparison() {
        PersistenceManager pm = getPM();
        createAndStoreNullInstance(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection instances3 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id == 3").execute();
            Collection instancesNot0Not3 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id != 3 && id != 0").execute();


            // case (nullableField == value)
            runSimplePrimitiveTypesQuery(
                    "intNull == 3", pm, instances3, ASSERTION_FAILED);

            // case (nullableField != value)
            runSimplePrimitiveTypesQuery(
                    "intNull != 3", pm, instancesNot0Not3, ASSERTION_FAILED);

            // case ! (nullableField == value)
            runSimplePrimitiveTypesQuery(
                    "!(intNull == 3)", pm, instancesNot0Not3, ASSERTION_FAILED);

            // case ! (nullableField != value)
            runSimplePrimitiveTypesQuery(
                    "!(intNull != 3)", pm, instances3, ASSERTION_FAILED);

            tx.commit();
            tx = null;
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * Tests logical complement operator ! negating a null check.
     */
    public void testPositiveNullCheck() {
        PersistenceManager pm = getPM();
        createAndStoreNullInstance(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection instancesGreater0 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id > 0").execute();
            Collection instances0 = (Collection)pm.newQuery(
                    PrimitiveTypes.class, "id == 0").execute();

            // case !(field == null)
            runSimplePrimitiveTypesQuery(
                    "! (intNull == null)", pm, instancesGreater0, ASSERTION_FAILED);

            // case !(field != null)
            runSimplePrimitiveTypesQuery(
                    "! (intNull != null)", pm, instances0, ASSERTION_FAILED);

            // case !!(field == null)
            runSimplePrimitiveTypesQuery(
                    "!! (intNull == null)", pm, instances0, ASSERTION_FAILED);

            // case !!(field != null)
            runSimplePrimitiveTypesQuery(
                    "!! (intNull != null)", pm, instancesGreater0, ASSERTION_FAILED);

            tx.commit();
            tx = null;
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PrimitiveTypes.class);
        loadAndPersistPrimitiveTypes(getPM());
    }

    private void createAndStoreNullInstance(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PrimitiveTypes primitiveObject = new PrimitiveTypes(
                    (long)0, false, null, (byte)0, null,
                    (short)0, null, (int) 0, null,
                    (long)0, null, (float)0, null,
                    (double)0, null, '0', null,
                    null, null,
                    null,
                    null,
                    null);
            pm.makePersistent(primitiveObject);
            tx.commit();
            tx = null;
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
