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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.query.OptionalSample;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Optional Fields.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-9
 *<BR>
 *<B>Assertion Description: </B>
 *Queries on fields of type java.util.Optional .
 */
public class SupportedOptionalMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
            "Assertion A14.6.2-9 (OptionalFields) failed: ";

    private static final int PC_ID = 1;
    private static final int PC_EMPTY_ID = 3;
    private static final int PC_NULL_ID = 4;
    private static final int REFERENCED_PC1_ID = 88;
    private static final int REFERENCED_PC2_ID = 99;
    private static final String STRING = "Hello JDO";
    private static final Integer INTEGER = 2016;
    private static final Date DATE = new Date(1000000000);
    private Object oidReferencedPC1;
    private Object oidReferencedPC2;
    private Object oidPC;
    private Object oidEmpty;
    private Object oidNull;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedOptionalMethods.class);
    }

    /** */
    public void testQueriesWithPresence() {
        //Matches 'optionalPC.isPresent() == true'
        checkQuery("optionalPC != null", oidPC, oidReferencedPC1); 

        //matches !isPresent() but does NOT match Java 'optionalPC==null'
        checkQuery("optionalPC == null", oidEmpty, oidNull, oidReferencedPC2);

        //matches isPresent() 
        checkQuery("!(optionalPC == null)", oidReferencedPC1, oidPC);

        //matches !isPresent() 
        checkQuery("!(optionalPC != null)", oidReferencedPC2, oidEmpty, oidNull);

        checkQuery("optionalPC.get() != null", oidPC, oidReferencedPC1);
        checkQuery("optionalPC.get() == null", oidEmpty, oidNull, oidReferencedPC2);
        checkQuery("optionalPC.isPresent()", oidPC, oidReferencedPC1);
        checkQuery("!optionalPC.isPresent()", oidReferencedPC2, oidEmpty, oidNull);

        //querying non-PC 'Optional' fields
        checkOptionalForPresence("optionalString");
        checkOptionalForPresence("optionalDate");
        checkOptionalForPresence("optionalInteger");
    }

    private void checkOptionalForPresence(String fieldName) {
        checkQuery(fieldName + " != null", 			oidPC);
        checkQuery(fieldName + " == null", 			
                oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);
        checkQuery("!(" + fieldName + " == null)", 	oidPC);
        checkQuery("!("+ fieldName + " != null)", 	
                oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);

        checkQuery(fieldName + ".get() != null", 	oidPC);
        checkQuery(fieldName + ".get() == null", 	
                oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);
        checkQuery(fieldName + ".isPresent()", 		oidPC);
        checkQuery("!"+ fieldName + ".isPresent()", 
                oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2);
    }

    public void testQueriesWithNavigation() {
        checkQuery("optionalPC.id == " + REFERENCED_PC1_ID, oidPC);
        checkQuery("optionalPC.id != " + REFERENCED_PC1_ID, oidReferencedPC1);
        checkQuery("!(optionalPC.id == " + REFERENCED_PC1_ID + ")", oidReferencedPC1);
        checkQuery("optionalPC.get().id == " + REFERENCED_PC1_ID, oidPC);
        checkQuery("optionalPC.get().id != " + REFERENCED_PC1_ID, oidReferencedPC1);
        checkQuery("!(optionalPC.get().id == " + REFERENCED_PC1_ID + ")", oidReferencedPC1);
        checkQuery("optionalPC.optionalPC.isPresent()", oidPC);

        //The following reflects the changed behavior in JDO 3.2 in  the sense that
        //all instances are returned where either 'optionalPC.optionalPC==null' (not present)
        //or 'optionalPC==null' (the 'null' evaluates to 'null', which is followed until it is 
        //evaluated in the 'isPresent()'). In other words, the query also returns all
        //objects that match '!(optionalPC.isPresent())'.
        checkQuery("!(optionalPC.optionalPC.isPresent())", 
                oidReferencedPC1, oidReferencedPC2, oidEmpty, oidNull);

        //A query where 'optionalPC!=null' and 'optionalPC.optionalPC==null; 
        //can be done as follows:
        checkQuery("optionalPC.isPresent() && !(optionalPC.optionalPC.isPresent())", 
                oidReferencedPC1);

        checkQuery("optionalPC.optionalPC.id == " + REFERENCED_PC2_ID, oidPC);
        checkQuery("optionalPC.get().optionalPC.get().id == " + REFERENCED_PC2_ID, oidPC);

        //test with && operator
        checkQuery("!(optionalPC.isPresent() && optionalPC.id == " + REFERENCED_PC1_ID + ")", 
                oidReferencedPC1, oidReferencedPC2, oidEmpty, oidNull);
    }

    private void checkQuery(String filter, Object ... resultOids) {
        QueryElementHolder<OptionalSample> qeh = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        OptionalSample.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       filter,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  null,
                /*paramValues*/ null);

        ArrayList<Object> expectedResults = new ArrayList<>();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < resultOids.length; i++) {
                expectedResults.add(pm.getObjectById(resultOids[i]));
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

        executeAPIQuery(ASSERTION_FAILED, qeh, expectedResults);
        executeSingleStringQuery(ASSERTION_FAILED, qeh, expectedResults);
    }

    /**
     * This methods tests Optional fields and parameters.
     */
    public void testParameterOptional() {
        OptionalSample osReferencedPC1 = getOptionalSampleById(oidReferencedPC1);
        String paramDecl = "java.util.Optional op";
        Map<String, Object> paramValues = new HashMap<>();

        paramValues.put("op", Optional.of(osReferencedPC1));
        checkQuery("this.optionalPC == op", paramDecl, 
                paramValues, new Object[]{oidPC});

        paramValues.put("op", Optional.of(DATE));
        checkQuery("this.optionalDate == op", paramDecl,
                paramValues, new Object[]{oidPC});

        paramValues.put("op", Optional.of(INTEGER));
        checkQuery("this.optionalInteger == op", paramDecl,
                paramValues, new Object[]{oidPC});

        paramValues.put("op", Optional.of(STRING));
        checkQuery("this.optionalString == op", paramDecl,
                paramValues, new Object[]{oidPC});
    }

    /**
     * This methods tests Optional fields and parameters with auto
     * de-referencing.
     */
    public void testParameterOptionalAutoDeref() {
        OptionalSample osReferencedPC1 = getOptionalSampleById(oidReferencedPC1);
        Map<String, Object> paramValues = new HashMap<>();

        paramValues.put("op", osReferencedPC1);
        checkQuery("this.optionalPC == op",
                OptionalSample.class.getName() + " op", 
                paramValues, new Object[]{oidPC});

        paramValues.put("op", DATE);
        checkQuery("this.optionalDate == op",
                "java.util.Date op",  
                paramValues, new Object[]{oidPC});

        paramValues.put("op", INTEGER);
        checkQuery("this.optionalInteger == op",
                "Integer op",
                paramValues, new Object[]{oidPC});

        paramValues.put("op", STRING);
        checkQuery("this.optionalString == op",
                "String op",  
                paramValues, new Object[]{oidPC});
    }

    private OptionalSample getOptionalSampleById(Object id) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            return (OptionalSample) pm.getObjectById(id);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * This methods tests that empty Optional fields and parameters matches with
     * Optional.empty().
     */
    public void testParameterOptionalWithEmptyFields() {
        String paramDecl = "java.util.Optional op";
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("op", Optional.empty());
        checkQuery("this.optionalPC == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC2});
        checkQuery("this.optionalDate == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalInteger == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalString == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
    }

    /**
     * This methods tests that Optional fields and parameters matches with 
     * (Optional)null.
     */
    public void testParameterOptionalWithNull() {
        String paramDecl = "java.util.Optional op";
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("op", null);
        checkQuery("this.optionalPC == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC2});
        checkQuery("this.optionalDate == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalInteger == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalString == op", paramDecl, paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
    }

    /**
     * This methods tests that Optional fields and parameters matches with
     * (Object)null.
     */
    public void testParameterOptionalNull() {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("op", null);
        checkQuery("this.optionalPC == op", OptionalSample.class.getName() + " op", paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC2});
        checkQuery("this.optionalDate == op", "java.util.Date op", paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalInteger == op", "java.lang.Integer op", paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});
        checkQuery("this.optionalString == op", "java.lang.String op", paramValues,
                new Object[]{oidEmpty, oidNull, oidReferencedPC1, oidReferencedPC2});

    }

    /**
     * This methods tests that Optional fields can be accessed in subqueries.
     */
    public void testSubqueries() {
        String queryStr1 = 
                "SELECT FROM " + OptionalSample.class.getName() + " WHERE " + 
                "(select max(a.id) from " + OptionalSample.class.getName() + " a " +
                "where a.optionalPC.isPresent() ) == id";
        Object[] expectedResult1 = new Object[]{oidReferencedPC1};
        checkSingleStringQuery(queryStr1, expectedResult1);
        
        String queryStr2 = 
                "SELECT FROM " + OptionalSample.class.getName() + " WHERE " + 
                "(select max(a.id) from " + OptionalSample.class.getName() + " a " +
                "where a.optionalPC.get() != null) == id";
        Object[] expectedResult2 = new Object[]{oidReferencedPC1};
        checkSingleStringQuery(queryStr2, expectedResult2);
    }

    /**
     * This methods tests that Optional fields can be accessed in subqueries.
     */
    public void testOptionalAggregation() {
        String clsName = OptionalSample.class.getName();
        
        String queryStr1 = "SELECT AVG(optionalInteger) FROM " + clsName;
        Query<?> q1 = pm.newQuery(queryStr1);
        executeJDOQuery(ASSERTION_FAILED, q1, queryStr1, false, null, (double)INTEGER, true);

        String queryStr2 = "SELECT AVG(optionalInteger.get()) FROM " + clsName;
        Query<?> q2 = pm.newQuery(queryStr2);
        executeJDOQuery(ASSERTION_FAILED, q2, queryStr2, false, null, (double)INTEGER, true);

        //return the object whose Integer is the same as the AVG of all
        //objects that have the Integer present.
        String queryStrSub1 = "SELECT FROM " + clsName + " WHERE " + 
                "(select avg(a.optionalInteger) from " + clsName + " a " +
                "where a.optionalInteger.isPresent() ) == optionalInteger";
        Object[] expectedResult1 = new Object[]{oidPC};
        checkSingleStringQuery(queryStrSub1, expectedResult1);

        String queryStrSub2 = "SELECT FROM " + clsName + " WHERE " + 
                "(select avg(a.optionalInteger.get()) from " + clsName + " a " +
                "where a.optionalInteger.isPresent() ) == optionalInteger";
        Object[] expectedResult2 = new Object[]{oidPC};
        checkSingleStringQuery(queryStrSub2, expectedResult2);

        //Find all where the average is the same as the integer value itself.
        //This returns ALL objects!!!
        String queryStrSub3 = "SELECT FROM " + clsName + " WHERE " + 
                "(select avg(a.optionalInteger) from " + clsName + " a " +
                " ) == optionalInteger";
        Object[] expectedResult3 = new Object[]{oidPC};
        checkSingleStringQuery(queryStrSub3, expectedResult3);
    }

    private void checkSingleStringQuery(String singleStringJDOQL, Object ... resultOids) {
        ArrayList<Object> expectedResults = new ArrayList<>();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < resultOids.length; i++) {
                expectedResults.add(pm.getObjectById(resultOids[i]));
            }
        } finally {
            if (tx.isActive())
                tx.rollback();
        }

        Query<?> singleStringQuery = pm.newQuery(singleStringJDOQL);
        executeJDOQuery(ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, 
                            false, null, expectedResults, true);
    }

    private void checkQuery(String filter, String paramDecl, Map<String, Object> paramValues, Object[] result) {
        QueryElementHolder<OptionalSample> qeh = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        OptionalSample.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       filter,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  paramDecl,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   null,
                /*paramValues*/  paramValues);

        ArrayList<Object> expectedResults = new ArrayList<>();
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < result.length; i++) {
                Object o = result[i];
                if (o instanceof String || o instanceof Date || o instanceof Integer) {
                    expectedResults.add(o);
                } else {
                    expectedResults.add(pm.getObjectById(result[i]));
                }
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

        executeAPIQuery(ASSERTION_FAILED, qeh, expectedResults);
        executeSingleStringQuery(ASSERTION_FAILED, qeh, expectedResults);
    }


    /**
     * Result class for queries on OptionalSample. 
     */
    public static class ResultInfo {
        public long id;
        public String optionalString;
        public ResultInfo() {}

        public ResultInfo(long id, String optionalString) {
            this.id = id;
            this.optionalString = optionalString;
        }
    }


    /**
     * Test Optional.orElse() in the SELECT clause of JDOQL queries.
     */
    @SuppressWarnings("unchecked")
    public void testOrElseInSELECT() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query<?> q = pm.newQuery("SELECT id, optionalString.orElse('NotPresent') FROM " + 
                    OptionalSample.class.getName());
            q.setResultClass(ResultInfo.class);
            Collection<ResultInfo> c = (Collection<ResultInfo>) q.execute();
            if (c.size() != 5) {
            	fail(ASSERTION_FAILED, "Wrong result count: " + c.size());
            }
            for (ResultInfo i: c) {
                switch ((int)i.id) {
                case PC_ID:
                	if (!STRING.equals(i.optionalString)) {
                		fail(ASSERTION_FAILED, "Wrong string value: " + i.optionalString);
                	}
                    break;
                case PC_EMPTY_ID:
                case PC_NULL_ID:
                case REFERENCED_PC1_ID:
                case REFERENCED_PC2_ID:
                    assertEquals("NotPresent", i.optionalString);
                    break;
                default: fail(ASSERTION_FAILED, "Wrong object id: " + i.id);
                }
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }


    /**
     * This test assert that null-references are converted to Optional.empty() when
     * loaded from the database.
     */
    public void testPersistenceNotNull() {
        OptionalSample osNotNull = getOptionalSampleById(oidNull);

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            if (osNotNull.getOptionalDate() == null) {
            	fail(ASSERTION_FAILED, "Date field was 'null'");
            }
            if (osNotNull.getOptionalInteger() == null) {
            	fail(ASSERTION_FAILED, "Integer field was 'null'");
            }
            if (osNotNull.getOptionalPC() == null) {
            	fail(ASSERTION_FAILED, "optionalPC field was 'null'");
            }
            if (osNotNull.getOptionalString() == null) {
            	fail(ASSERTION_FAILED, "String field was 'null'");
            }

            if (osNotNull.getOptionalDate().isPresent()) {
            	fail(ASSERTION_FAILED, "Date field was present");
            }
            if (osNotNull.getOptionalInteger().isPresent()) {
            	fail(ASSERTION_FAILED, "Integer field was present");
            }
            if (osNotNull.getOptionalPC().isPresent()) {
            	fail(ASSERTION_FAILED, "optionalPC field was present");
            }
            if (osNotNull.getOptionalString().isPresent()) {
            	fail(ASSERTION_FAILED, "String field was present");
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(OptionalSample.class);
        insertOptionalSample(getPM());
    }


    private void insertOptionalSample(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            //Create two objects that are referenced by other objects, this allows 
            //testing of navigation in queries.
            //The referencedPC1 will be referenced by 'osPC',
            //The referencedPC2 will be referenced by referencedPC1.
            OptionalSample referencedPC1 = new OptionalSample(); 
            referencedPC1.setId(REFERENCED_PC1_ID);
            pm.makePersistent(referencedPC1);
            oidReferencedPC1 = pm.getObjectId(referencedPC1);

            OptionalSample referencedPC2 = new OptionalSample(); 
            referencedPC2.setId(REFERENCED_PC2_ID);
            pm.makePersistent(referencedPC2);
            oidReferencedPC2 = pm.getObjectId(referencedPC2);

            referencedPC1.setOptionalPC(Optional.of(referencedPC2));

            OptionalSample osPC = new OptionalSample();
            osPC.setId(PC_ID);
            osPC.setOptionalPC(Optional.of(referencedPC1));
            osPC.setOptionalDate(Optional.of(DATE));
            osPC.setOptionalInteger(Optional.of(INTEGER));
            osPC.setOptionalString(Optional.of(STRING));
            pm.makePersistent(osPC);
            oidPC = pm.getObjectId(osPC);

            //use empty optionals
            OptionalSample osEmpty = new OptionalSample();
            osEmpty.setId(PC_EMPTY_ID);
            osEmpty.setOptionalPC(Optional.empty());
            osEmpty.setOptionalDate(Optional.empty());
            osEmpty.setOptionalInteger(Optional.empty());
            osEmpty.setOptionalString(Optional.empty());
            pm.makePersistent(osEmpty);
            oidEmpty = pm.getObjectId(osEmpty);

            //use null for optional fields
            OptionalSample osNull = new OptionalSample();
            osNull.setId(PC_NULL_ID);
            osNull.setOptionalPC(null);
            osNull.setOptionalDate(null);
            osNull.setOptionalInteger(null);
            osNull.setOptionalString(null);
            pm.makePersistent(osNull);
            oidNull = pm.getObjectId(osNull);

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    protected void localTearDown() {
        //set all references to null to allow deletion of (otherwise) referenced objects
        Transaction tx = getPM().currentTransaction();
        tx.begin();
        Query<OptionalSample> q = pm.newQuery(OptionalSample.class);
        for (OptionalSample os: q.executeList()) {
            os.setOptionalPC(Optional.empty());
        }
        tx.commit();

        super.localTearDown();
    }

}
