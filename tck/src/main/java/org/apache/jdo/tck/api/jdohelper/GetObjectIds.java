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
 
package org.apache.jdo.tck.api.jdohelper;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.identity.SingleFieldIdentity;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldInteger;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test GetObjectIds
 *<BR>
 *<B>Keywords:</B> getObjectIds getObjectId
 *<BR>
 *<B>Assertion IDs:</B> A8.3-11
 *<BR>
 *<B>Assertion Description: </B>
 * These methods return the JDO identities of the parameter instances. 
 * For each instance in the parameter, the getObjectId method is called. 
 * They return one identity instance for each persistence-capable instance 
 * in the parameter. The order of iteration of the returned Collection 
 * exactly matches the order of iteration of the parameter Collection.]
 */

public class GetObjectIds extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.3-11 (GetObjectIds) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectIds.class);
    }

    /** The parameter objects
     */
    Object obj0;
    Object obj1;
    Object obj2;
    Object obj3;
    Object obj4;

    Object[] objs;

    /** The object ids
     */
    Object oid0;
    Object oid1;
    Object oid2;
    Object oid3;
    Object oid4;

    Object[] oids;  

    /** Typed objects
     */
    PCPointSingleFieldInteger singleFieldIdentityObj0;
    PCPointSingleFieldInteger singleFieldIdentityObj1;

    PCPointSingleFieldInteger[] singleFieldIdentityObjs;

    /** Typed object ids
     */
    SingleFieldIdentity singleFieldIdentityOid0;
    SingleFieldIdentity singleFieldIdentityOid1;

    SingleFieldIdentity[] singleFieldIdentityOids;  

    /** The second persistence manager.
     */
    PersistenceManager pm2;
    
    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
        objs = new Object[] {
            obj0 = null,
            obj1 = new Date(),
            obj2 = new PCPoint(23,45), // persistent in pm1
            obj3 = new PCPoint(26,47), // persistent in pm2
            obj4 = new PCPoint(23,45) // not persistent
        };

        singleFieldIdentityObjs = new PCPointSingleFieldInteger[] {
            singleFieldIdentityObj0 = new PCPointSingleFieldInteger(0, 1), // persistent in pm1
            singleFieldIdentityObj1 = new PCPointSingleFieldInteger(1, 1) // persistent in pm1
        };

        getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(obj2);
        if (runsWithApplicationIdentity()) {
            pm.makePersistent(singleFieldIdentityObj0);
            pm.makePersistent(singleFieldIdentityObj1);
        }
        pm.currentTransaction().commit();
        
        pm2 = getPMF().getPersistenceManager();
        pm2.currentTransaction().begin();
        pm2.makePersistent(obj3);
        pm2.currentTransaction().commit();
        
        oids = new Object[] {
                oid0 = JDOHelper.getObjectId(obj0),
                oid1 = JDOHelper.getObjectId(obj1),
                oid2 = JDOHelper.getObjectId(obj2),
                oid3 = JDOHelper.getObjectId(obj3),
                oid4 = JDOHelper.getObjectId(obj4),
        };

        singleFieldIdentityOids = new SingleFieldIdentity[] {
                singleFieldIdentityOid0 = (SingleFieldIdentity)JDOHelper.getObjectId(singleFieldIdentityObj0),
                singleFieldIdentityOid1 = (SingleFieldIdentity)JDOHelper.getObjectId(singleFieldIdentityObj1)
        };

    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localTearDown()
     */
    @Override
    protected void localTearDown() {
        pm2.close();
    }

    /** */
    public void testGetObjectIdsArray() {
        StringBuilder messages = new StringBuilder();
        Object[] expectedArray = oids;
        Object[] actualArray = JDOHelper.getObjectIds(objs);
        for (int i = 0; i < objs.length; ++i) {
            Object expected = expectedArray[i];
            Object actual = actualArray[i];
            if (expected == null? 
                    actual != null:
                    !expected.equals(actual)) {
                messages.append(
                        "\nComparison failed for object ids at position " + i +
                        "\nexpected: " + expected +
                        "\nactual: " + actual);
            }
        }
        if (messages.length() != 0) {
            fail(ASSERTION_FAILED + "getObjectIds(Object[] pcs) " + messages);
        }
    }

    /** */
    public void testGetObjectIdsCollection() {
        StringBuilder messages = new StringBuilder();
        List<Object> paramList = Arrays.asList(objs);
        List<Object> expectedList = Arrays.asList(oids);
        Collection<Object> actualCollection = JDOHelper.getObjectIds(paramList);
        Iterator<Object> expectedIterator = expectedList.iterator();
        Iterator<Object> actualIterator = actualCollection.iterator();
        for (int i = 0; i < objs.length; ++i) {
            Object expected = expectedIterator.next();
            Object actual = actualIterator.next();
            if (expected == null? 
                    actual != null:
                    !expected.equals(actual)) {
                messages.append(
                        "\nComparison failed for object ids at position " + i +
                        "\nexpected: " + expected +
                        "\nactual: " + actual);
            }
        }
        if (messages.length() != 0) {
            fail(ASSERTION_FAILED + "getObjectIds(Collection pcs) " + messages);
        }
    }

    /** */
    public void testTypedGetObjectIdsArray() {
        if (!runsWithApplicationIdentity()) return;
        StringBuilder messages = new StringBuilder();
        Object[] expectedArray = singleFieldIdentityOids;
        Object[] actualArray = JDOHelper.getObjectIds(singleFieldIdentityObjs);
        for (int i = 0; i < singleFieldIdentityObjs.length; ++i) {
            Object expected = expectedArray[i];
            Object actual = actualArray[i];
            if (expected == null? 
                    actual != null:
                    !expected.equals(actual)) {
                messages.append(
                        "\nComparison failed for object ids at position " + i +
                        "\nexpected: " + expected +
                        "\nactual: " + actual);
            }
        }
        if (messages.length() != 0) {
            fail(ASSERTION_FAILED + "getObjectIds(Object[] pcs) " + messages);
        }
    }

    /** */
    public void testTypedGetObjectIdsCollection() {
        if (!runsWithApplicationIdentity()) return;
        StringBuilder messages = new StringBuilder();
        List<PCPointSingleFieldInteger> paramList = Arrays.asList(singleFieldIdentityObjs);
        List<?> expectedList = Arrays.asList(singleFieldIdentityOids);
        Collection<?> actualCollection = JDOHelper.getObjectIds(paramList);
        Iterator<?> expectedIterator = expectedList.iterator();
        Iterator<?> actualIterator = actualCollection.iterator();
        for (int i = 0; i < singleFieldIdentityObjs.length; ++i) {
            Object expected = expectedIterator.next();
            Object actual = actualIterator.next();
            if (expected == null? 
                    actual != null:
                    !expected.equals(actual)) {
                messages.append(
                        "\nComparison failed for object ids at position " + i +
                        "\nexpected: " + expected +
                        "\nactual: " + actual);
            }
        }
        if (messages.length() != 0) {
            fail(ASSERTION_FAILED + "getObjectIds(Collection<SingleFieldIdentyty> pcs) " + messages);
        }
    }

}
