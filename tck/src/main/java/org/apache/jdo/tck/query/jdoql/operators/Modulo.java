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

import java.util.List;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Modulo operator.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-40.
 *<BR>
 *<B>Assertion Description: </B>
 * modulo operator
 */
public class Modulo extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-40 (Modulo) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Modulo.class);
    }

    public void testPositive() {
        List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp2", "emp4");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.personid.mod(2).eq(0L));

        // Import Department twice
        QueryElementHolder<Person> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid % 2 == 0",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        // DataNucleus: UnsupportedOperationException: Dont currently support operator  %  in JDOQL conversion
        //executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testPostiveUsingPrimitiveTypes() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        List<PrimitiveTypes> instance4 = pm.newQuery(
            PrimitiveTypes.class, "id == 10").executeList();
                
        runSimplePrimitiveTypesQuery("id % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("byteNotNull % 10 == 0",
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("shortNotNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);            
        runSimplePrimitiveTypesQuery("intNotNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);            
        runSimplePrimitiveTypesQuery("longNotNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("byteNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("shortNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("intNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("longNull % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);
        runSimplePrimitiveTypesQuery("bigInteger % 10 == 0", 
                                     pm, instance4, ASSERTION_FAILED);

        tx.commit();
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        addTearDownClass(PrimitiveTypes.class);
        PersistenceManager pm = getPM();
        loadAndPersistCompanyModel(pm);
        loadAndPersistPrimitiveTypes(pm);
    }
}
