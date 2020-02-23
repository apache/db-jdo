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

package org.apache.jdo.tck.api.persistencemanager.nullargs;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;

/**
  * The superclass for the tests of null arguments to pm methods.
  *
  * Null arguments to APIs that take an Object parameter cause the API 
  * to have no effect. Null arguments to APIs that take Object[] or Collection
  * will cause the API to throw NullPointerException. Non-null Object[] or 
  * Collection arguments that contain null elements will have the documented 
  * behavior for non-null elements, and the null elements will be ignored.
  *
  */

public class PersistenceManagerNullsTest extends JDO_Test {

    /**
     */
    public static class MethodUnderTest {
        public void pmApi(PersistenceManager pm, Object obj) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
        public void pmApi(PersistenceManager pm, Collection coll) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
        public void pmApi(PersistenceManager pm, Object[] objs) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
        public Object pmApiReturn(PersistenceManager pm, Object obj) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
        public Collection pmApiReturn(PersistenceManager pm, Collection coll) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
        public Object[] pmApiReturn(PersistenceManager pm, Object[] objs) {
            throw new UnsupportedOperationException("Test must implement this method");
        }
    } 


    private static final String ASSERTION3_FAILED = 
        "Assertion A12.6-3 failed: ";
    
    private static final String ASSERTION4_FAILED = 
        "Assertion A12.6-4 failed: ";
    
    private static final String ASSERTION5_FAILED = 
        "Assertion A12.6-5 failed: ";

    protected PCPoint pNotNull = null;
    protected Collection collNullElem = null;
    protected Collection expectedCollection = null;
    protected Object[] arrayNullElem = new Object[] {null, null};
    protected Object[] expectedArray = new Object[] {null, null};
    protected Collection testInstances = null;
    
    /** */
    protected PersistenceManagerNullsTest() { }

    /** 
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        // The order of addTearDownClass calls is significant
        // as it takes into account database FKs.
        addTearDownClass(PCPoint.class);

        // Initialize test objects and expected values
        pNotNull = new PCPoint(3, 5);
        arrayNullElem[1] = pNotNull;
        expectedArray[1] = pNotNull;
        collNullElem = Arrays.asList(arrayNullElem);

        pm = getPM();   
        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                pm.makePersistent(pNotNull);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tx.commit();

            logger.debug(" \nSetup committed in DeletePersistentNullArgs()");
        }
        finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }       expectedCollection = Arrays.asList(arrayNullElem);

    }
    
    protected static String toString(Object[] objs) {
        StringBuffer out = new StringBuffer();
        for (int i=0; i < objs.length; i++) {
            out.append("[" + i + "]: ");
            if (objs[i] == null)
                out.append("null");
            else out.append(objs[i].toString());
            out.append(",  ");
        }
        return out.toString();
    }

    protected static String toString(Collection objs) {
        return toString(objs.toArray());
    }

    /**
      * Checks if expected and actual arguments match for null/non-null value
      * @return true if arguments match
      * @param expected Collection
      * @param actual Collection
      */
    protected boolean checkReturn(Collection expected, Collection actual) {
        Object eElem = null;
        Object aElem = null;
        if (expected.size() != actual.size())
            return false;
        Iterator eIt = expected.iterator();
        Iterator aIt = actual.iterator();
        while (eIt.hasNext()) {
            eElem = eIt.next();
            aElem = aIt.next();
            if ( (eElem == null && aElem != null)
               || (aElem == null && eElem != null) )
                return false;
        }
        return true;
    }

    /**
      * Checks if expected and actual arguments match for null/non-null value
      * @return true if arguments match
      * @param expected Object[]
      * @param actual Object[]
      */
    protected boolean checkReturn(Object[] expected, Object[] actual) {
        Object eElem = null;
        Object aElem = null;
        if (expected.length != actual.length)
            return false;
        for (int i=0; i < expected.length; i++) {
            eElem = expected[i];
            aElem = actual[i];
            if ( (eElem == null && aElem != null)
               || (aElem == null && eElem != null) )
                return false;
        }
        return true;
    }

    /**
     * Test that method under test with null valued argument does nothing.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullObjectParameter(MethodUnderTest mut, String method) {

        Transaction tx = pm.currentTransaction();
        Object obj = null;
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                mut.pmApi(pm, obj);
            } catch (Exception e) {
                fail(ASSERTION3_FAILED,
                        method + " on a null object should do nothing."
                        + " Instead we get: " + e.toString());
            }
            tx.commit();

            logger.debug(" \nPASSED in executeNullObjectParameter() on " 
                    + method);
        } finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with null valued Collection argument
     * throws NullPointerException.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullCollectionParameter(MethodUnderTest mut,
            String method) {

        Collection coll = null;
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            try {
                mut.pmApi(pm, coll);
                fail(ASSERTION4_FAILED,
                        method
                        + " with null Collection argument should throw NPE.");
            } catch (NullPointerException npe) {
                // this is what we want
            } catch (Exception e) {
                fail(ASSERTION4_FAILED,
                        method 
                        + " with null Collection argument should throw NPE."
                        + " Instead we get: " + e.toString());
                e.printStackTrace();
            }
            tx.commit();

            logger.debug(" \nPASSED in executeNullCollectionParameter()");
        } finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with null valued array argument
     * throws NullPointerException.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullArrayParameter(MethodUnderTest mut,
                String method) {

        Object[] array = null; 
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            try {
                mut.pmApi(pm, array);
                fail(ASSERTION4_FAILED, method +
                        " with null array argument should throw NPE.");
            } catch (NullPointerException npe) {
                // this is what we want
            } catch (Exception e) {
                fail(ASSERTION4_FAILED,
                        method + " with null array argument should throw NPE."
                        + " Instead we get: " + e.toString());
                e.printStackTrace();
            }
            tx.commit();

            logger.debug(" \nPASSED in executeNullArrayParameter()");
        } finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with a null element of a 
     * Collection argument ignores the null element.
     * @param coll collection argument
     * @param mut method under test
     * @param method method name
     */
    public void executeCollectionNullElement(Collection coll,
            MethodUnderTest mut, String method) {

        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                mut.pmApi(pm, coll);
            } catch (Exception e) {
                fail(ASSERTION5_FAILED,
                        method + " on a null Collection element should"
                        + " do nothing. Instead we get: " + e.toString());
                e.printStackTrace();
            }

            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with a null element of a 
     * array argument ignores the null element.
     * @param array argument
     * @param mut method under test
     * @param method method name
     */
    public void executeArrayNullElement(Object[] array, MethodUnderTest mut,
                String method) {

        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                mut.pmApi(pm, array);
            } catch (Exception e) {
                fail(ASSERTION5_FAILED,
                        method + " on a null array element should " 
                        + "do nothing. Instead we get: " + e.toString());
                e.printStackTrace();
            }

            tx.commit();

        } finally {
            if (tx.isActive())
                tx.rollback();
        }

    }

    /** 
     * Test that method under test with null valued argument does nothing.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullObjectParameterReturn(MethodUnderTest mut, 
            String method) {

        Object returnVal = null;
        Object obj = null;
        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                returnVal = mut.pmApiReturn(pm, obj);
            } catch (Exception e) {
                fail(ASSERTION3_FAILED,
                        method + " on a null object should do nothing."
                        + " Instead we get: " + e.toString());
                e.printStackTrace();
            }
            if (returnVal != null)
                fail(ASSERTION3_FAILED,
                        method + " returns non-null Object; expected null.");

            tx.commit();

            logger.debug(" \nPASSED in executeNullObjectParameter() on " 
                    + method);
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with null valued Collection argument
     * throws NullPointerException.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullCollectionParameterReturn( MethodUnderTest mut, 
            String method) {

        Collection returnVal = null;
        Collection coll = null;
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            try {
                returnVal = mut.pmApiReturn(pm, coll);
                fail(ASSERTION4_FAILED,
                        method
                        + " with null Collection argument should throw NPE.");
            } catch (NullPointerException npe) {
                // this is what we want
            } catch (Exception e) {
                fail(ASSERTION4_FAILED,
                        method 
                        + " with null Collection argument should throw NPE."
                        + " Instead we get: " + e.toString());
                e.printStackTrace();
            }
            if (returnVal != null)
                fail(ASSERTION4_FAILED,
                        method + " returns non-null Object. ");
            tx.commit();

            logger.debug(" \nPASSED in executeNullCollectionParameter()");
            }
        finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with null valued array argument
     * throws NullPointerException.
     * @param mut method under test
     * @param method method name
     */
    public void executeNullArrayParameterReturn(MethodUnderTest mut, 
            String method) {

        Object[] returnVal = null;
        Object[] array = null;
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            try {
                returnVal = mut.pmApiReturn(pm, array);
                fail(ASSERTION4_FAILED, method 
                        + " with null array argument should throw NPE.");
            } catch (NullPointerException npe) {
                // this is what we want
            } catch (Exception e) {
                fail(ASSERTION4_FAILED,
                        method + " with null array argument should throw NPE."
                        + " Instead we get: " + e.toString());
                e.printStackTrace();
            }
            if (returnVal != null)
                fail(ASSERTION4_FAILED,
                        method + " returns non-null Object.");
            tx.commit();

            logger.debug(" \nPASSED in executeNullArrayParameter()");
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with a null element of a 
     * Collection argument ignores the null element.
     * @param coll argument
     * @param mut method under test
     * @param method method name
     */
    public void executeCollectionNullElementReturn(Collection coll,
            MethodUnderTest mut, String method) {

        Collection returnVal = null;
        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                returnVal = mut.pmApiReturn(pm, coll);
            } catch (Exception e) {
                fail(ASSERTION5_FAILED,
                        method + " on a null Collection element should"
                        + " do nothing. Instead we get: " + e.toString());
                e.printStackTrace();
            }

            if (!checkReturn(expectedCollection, returnVal))
                fail(ASSERTION5_FAILED,
                        method + " returns incorrect Object. Expected "
                        + expectedCollection.toString() + " actual was " 
                        + returnVal.toString());
            tx.commit();
        } finally {
            if (tx.isActive())
                tx.rollback();
        }        

    }

    /** 
     * Test that the method under test with a null element of a 
     * array argument ignores the null element.
     * @param obj argument
     * @param mut method under test
     * @param method method name
     */
    public void executeArrayNullElementReturn(Object[] obj,
            MethodUnderTest mut, String method) {

        Object[] returnVal = null;
        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();

            tx.begin();
            try {
                returnVal = mut.pmApiReturn(pm, obj);
            } catch (Exception e) {
                fail(ASSERTION5_FAILED,
                        method + " on a null array element should " 
                        + "do nothing. Instead we get: " + e.toString());
                e.printStackTrace();
            }

            if (!checkReturn(expectedArray, returnVal))
                fail(ASSERTION5_FAILED,
                        method + " returns incorrect Object. Expected "
                        + Arrays.asList(expectedArray).toString() 
                        + " actual was " 
                        + Arrays.asList(returnVal).toString());
            tx.commit();

        } finally {
            if (tx.isActive())
                tx.rollback();
        }

    }
}
