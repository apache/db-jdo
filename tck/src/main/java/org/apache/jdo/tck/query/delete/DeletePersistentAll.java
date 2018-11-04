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

package org.apache.jdo.tck.query.delete;

import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Insurance;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 *<B>Title:</B> Delete Persistent All.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.8-1, A14.8-2
 *<BR>
 *<B>Assertion Description: </B>
 * These methods delete the instances of affected classes 
 * that pass the filter, and all dependent instances. 
 * Affected classes are the candidate class and 
 * its persistence-capable subclasses.
 * 
 * The number of instances of affected classes that were deleted is returned. 
 * Embedded instances and dependent instances are not counted 
 * in the return value.
 */
public class DeletePersistentAll extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.8-1 (DeletePersistentAll) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DeletePersistentAll.class);
    }

    /** */
    public void testNoParametersAPI() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance";
            query = pm.newQuery(Insurance.class);
            long expectedNrOfDeletedObjects = 11;
            long nr = query.deletePersistentAll();
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /** */
    public void testNoParametersSingleString() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance";
            query = pm.newQuery(singleStringQuery);
            long expectedNrOfDeletedObjects = 11;
            long nr = query.deletePersistentAll();
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /** */
    public void testObjectArrayParametersAPI() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance " +
                                       "WHERE carrier == param PARAMETERS String param";
            query = pm.newQuery(Insurance.class, "carrier == param");
            query.declareParameters("String param");
            Object[] parameters = new Object[] {"Carrier1"};
            long expectedNrOfDeletedObjects = 2;
            long nr = query.deletePersistentAll(parameters);
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /** */
    public void testObjectArrayParametersSingleString() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance " +
                                       "WHERE carrier == param PARAMETERS String param";
            query = pm.newQuery(singleStringQuery);
            Object[] parameters = new Object[] {"Carrier1"};
            long expectedNrOfDeletedObjects = 2;
            long nr = query.deletePersistentAll(parameters);
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /** */
    public void testMapParametersAPI() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance " +
                                       "WHERE carrier == param PARAMETERS String param";
            query = pm.newQuery(Insurance.class, "carrier == param");
            query.declareParameters("String param");
            Map parameters = new HashMap();
            parameters.put("param", "Carrier1");
            long expectedNrOfDeletedObjects = 2;
            long nr = query.deletePersistentAll(parameters);
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /** */
    public void testMapParametersSingleString() {
        Transaction tx = pm.currentTransaction();
        Query<Insurance> query = null;
        Object result = null;
        try {
            tx.begin();
            String singleStringQuery = "SELECT FROM org.apache.jdo.tck.pc.company.Insurance " +
                                       "WHERE carrier == param PARAMETERS String param";
            query = pm.newQuery(singleStringQuery);
            Map parameters = new HashMap();
            parameters.put("param", "Carrier1");
            long expectedNrOfDeletedObjects = 2;
            long nr = query.deletePersistentAll(parameters);
            if (nr != expectedNrOfDeletedObjects) {
                fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                        ", expected is " + expectedNrOfDeletedObjects +
                        ". Query: " + singleStringQuery);
            }
            tx.commit();
        } finally {
            if (query != null) {
                query.close(result);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
