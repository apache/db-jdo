/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.impl.sco.Freezer;
import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCSCO;
import org.apache.jdo.sco.SCO;

/**
* This test is a base class for all SCO tests.  It cannot be run itself.
*
* @author Craig Russell
*/
public class Test_SCO_Base extends Test_Fetch {

    protected Object oid_point = null;
    protected Object oid_date = null;
    protected Object oid_collections = null;

    protected PCPoint pcPoint = null;
    protected PCCollections pcCollections = null;
    protected PCSCO pcSCO = null;

    /** this method can be called from insertObjects in subclasses */
    protected void insertDate() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            insertDateWithoutCommit(pm);
            tx.commit();
        
            oid_date = pm.getObjectId(pcSCO);
            if (debug) logger.debug("inserted pcSCO: " + oid_date);
            oids.add(oid_date);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** Allows to commit or rollback the transaction.
     * This method can be called from insertObjects in subclasses
     */
    protected void insertDateWithoutCommit(PersistenceManager pm) {
        pcSCO = createDate();
        if (debug) logger.debug("Before makePersistent: " + pcSCO);
        pm.makePersistent(pcSCO);
        if (debug) logger.debug("After makePersistent: " + pcSCO);
    }

    /** this method can be called from insertObjects in subclasses */
    protected void insertAllTypes() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            insertAllTypesWithoutCommit(pm);
            tx.commit();
            
            // Next 2 statements allow this to work whether or not 
            // reachability or navigation work.
            oid_point = pm.getObjectId(pcPoint);
            oids.add(oid_point);
            
            oid_collections = pm.getObjectId(pcCollections);
            if (debug) 
                logger.debug("inserted pcCollections: " + oid_collections);
            oids.add(oid_collections);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** Allows to commit or rollback the transaction.
     * this method can be called from insertObjects in subclasses
     */
    protected void insertAllTypesWithoutCommit(PersistenceManager pm) {

        pcPoint = createPoint();
        pcCollections = createAllTypes(pcPoint);
        if (debug) logger.debug("Before makePersistent: " + pcCollections);

        // Next statement allows this to work whether or not reachability or
        // navigation work.
        pm.makePersistent(pcPoint);

        // The order of this and the previous statements is important!
        pm.makePersistent(pcCollections);

        if (debug) logger.debug("After makePersistent: " + pcCollections);

    }

    /** */
    protected PCSCO createDate() {
        PCSCO pcSCO = new PCSCO();
        pcSCO.init();
        // Note, the value of the specified Date is changed by setSCODate
        pcSCO.setSCODate(new Date());
        return pcSCO;
    }
    
    /** */
    protected PCPoint createPoint() {
        return new PCPoint(42, 99);
    }

    /** */
    protected PCCollections createAllTypes(PCPoint pcPoint) {
        PCCollections pcCollections = new PCCollections();
        pcCollections.init();
        
        ArrayList al = new ArrayList(3);
        pcCollections.setSCOArrayList(al);

        Vector v = new Vector(3);
        pcCollections.setSCOVector(v);
                                                
        HashSet hs = new HashSet(3);
        pcCollections.setSCOHashSet(hs);

        LinkedList ll = new LinkedList();
        pcCollections.setSCOLinkedList(ll);
                                                
        HashMap hm = new HashMap(3);
        pcCollections.setSCOHashMap(hm);
                                                
        Hashtable ht = new Hashtable(3);
        pcCollections.setSCOHashtable(ht);
                                                
        TreeMap tm = new TreeMap();
        pcCollections.setSCOTreeMap(tm);
                                                
        TreeSet ts = new TreeSet();
        pcCollections.setSCOTreeSet(ts);

        pcCollections.addPoint(pcPoint);
        
        return pcCollections;
    }

    /** */
    protected void assertEqualsPCSCO(PCSCO expected, Object actual)
    {
        assertNotNull("Actual instance is null", actual);
        assertTrue("Expected PCSCO instance; instance has type " + 
                   actual.getClass().getName(), (actual instanceof PCSCO));

        // Note, this implementation 
        // - assumes all _nullXXX fields are null
        // - assumes all _XXX fields are not null in this instance
        // - compares the time for all date fields
        
        PCSCO other = (PCSCO)actual;
        assertEquals("Wrong _date value", expected._date, other._date);
        assertEquals("Wrong _nullDate value", 
                     expected._nullDate, other._nullDate);
        assertEquals("Wrong _scoDate value", expected._scoDate, other._scoDate);
        assertEquals("Wrong _nullSCODate value",
                     expected._nullSCODate, other._nullSCODate);
        assertEquals("Wrong _sqlDate value", expected._sqlDate, other._sqlDate);
        assertEquals("Wrong _nullSqlDate value",
                     expected._nullSqlDate, other._nullSqlDate);
        assertEquals("Wrong _bigDecimal value", 
                     expected._bigDecimal, other._bigDecimal);
        assertEquals("Wrong _nullBigInteger value", 
                     expected._nullBigInteger, other._nullBigInteger);
        assertEquals("Wrong _bigInteger value", 
                     expected._bigInteger, other._bigInteger);
        assertEquals("Wrong _nullBigInteger value",
                     expected._nullBigInteger, other._nullBigInteger);
        assertEquals("Wrong _bitSet value", expected._bitSet, other._bitSet);
        assertEquals("Wrong _nullBitSet value",
                     expected._nullBitSet, other._nullBitSet);
        assertEquals("Wrong _locale value", expected._locale, other._locale);
        assertEquals("Wrong _nullLocale value",
                     expected._nullLocale, other._nullLocale);
    }
    
    /** */
    protected void assertEqualsPCCollections(PCCollections expected,
                                             Object actual)
    {
        assertNotNull("Actual instance is null", actual);
        assertTrue("Expected PCCollections instance; instance has type " +
                   actual.getClass().getName(), 
                   (actual instanceof PCCollections));
        
        PCCollections other = (PCCollections)actual;

        assertEqualsCollection("Wrong _arrayList value",
                               expected._arrayList, other._arrayList);
        assertEquals("Wrong _emptyArrayList value",
                     expected._emptyArrayList, other._emptyArrayList);
        assertEquals("Wrong _nullArrayList value",
                     expected._nullArrayList, other._nullArrayList);
        assertEqualsCollection("Wrong _sco_arrayList value",
                               expected._sco_arrayList, other._sco_arrayList);
        assertEquals("Wrong _sco_emptyArrayList value",
                     expected._sco_emptyArrayList, other._sco_emptyArrayList);
        assertEquals("Wrong _sco_nullArrayList value",
                     expected._sco_nullArrayList, other._sco_nullArrayList);
        assertEqualsCollection("Wrong _vector value",
                               expected._vector, other._vector);
        assertEquals("Wrong _emptyVector value",
                     expected._emptyVector, other._emptyVector);
        assertEquals("Wrong _nullVector value",
                     expected._nullVector, other._nullVector);
        assertEqualsCollection("Wrong _sco_vector value",
                               expected._sco_vector, other._sco_vector);
        assertEquals("Wrong _sco_emptyVector value",
                     expected._sco_emptyVector, other._sco_emptyVector);
        assertEquals("Wrong _sco_nullVector value",
                     expected._sco_nullVector, other._sco_nullVector);
        assertEqualsMap("Wrong _hashMap value",
                        expected._hashMap, other._hashMap);
        assertEquals("Wrong _emptyHashMap value",
                     expected._emptyHashMap, other._emptyHashMap);
        assertEquals("Wrong _nullHashMap value",
                     expected._nullHashMap, other._nullHashMap);
        assertEqualsMap("Wrong _sco_hashMap value",
                        expected._sco_hashMap, other._sco_hashMap);
        assertEquals("Wrong _sco_emptyHashMap value",
                     expected._sco_emptyHashMap, other._sco_emptyHashMap);
        assertEquals("Wrong _sco_nullHashMap value",
                     expected._sco_nullHashMap, other._sco_nullHashMap);
        assertEqualsMap("Wrong _hashtable value",
                        expected._hashtable, other._hashtable);
        assertEquals("Wrong _sco_emptyHashtable value",
                     expected._sco_emptyHashtable, other._sco_emptyHashtable);
        assertEquals("Wrong _sco_nullHashtable value",
                     expected._sco_nullHashtable, other._sco_nullHashtable);
        assertEqualsCollection("Wrong _hashSet value",
                               expected._hashSet, other._hashSet);
        assertEquals("Wrong _emptyHashSet value",
                     expected._emptyHashSet, other._emptyHashSet);
        assertEquals("Wrong _nullHashSet value",
                     expected._nullHashSet, other._nullHashSet);
        assertEqualsCollection("Wrong _sco_hashSet value",
                               expected._sco_hashSet, other._sco_hashSet);
        assertEquals("Wrong _sco_emptyHashSet value",
                     expected._sco_emptyHashSet, other._sco_emptyHashSet);
        assertEquals("Wrong _sco_nullHashSet value",
                     expected._sco_nullHashSet, other._sco_nullHashSet);
        assertEqualsCollection("Wrong _linkedList value",
                               expected._linkedList, other._linkedList);
        assertEquals("Wrong _emptyLinkedList value",
                     expected._emptyLinkedList, other._emptyLinkedList);
        assertEquals("Wrong _nullLinkedList value",
                     expected._nullLinkedList, other._nullLinkedList);
        assertEqualsCollection("Wrong _sco_linkedList value",
                               expected._sco_linkedList, other._sco_linkedList);
        assertEquals("Wrong _sco_emptyLinkedList value",
                     expected._sco_emptyLinkedList, other._sco_emptyLinkedList);
        assertEquals("Wrong _sco_nullLinkedList value",
                     expected._sco_nullLinkedList, other._sco_nullLinkedList);
        assertEqualsMap("Wrong _treeMap value",
                        expected._treeMap, other._treeMap);
        assertEquals("Wrong _emptyTreeMap value",
                     expected._emptyTreeMap, other._emptyTreeMap);
        assertEquals("Wrong _nullTreeMap value",
                     expected._nullTreeMap, other._nullTreeMap);
        assertEqualsMap("Wrong _sco_treeMap value",
                        expected._sco_treeMap, other._sco_treeMap);
        assertEquals("Wrong _sco_emptyTreeMap value",
                     expected._sco_emptyTreeMap, other._sco_emptyTreeMap);
        assertEquals("Wrong _sco_nullTreeMap value",
                     expected._sco_nullTreeMap, other._sco_nullTreeMap);
        assertEqualsCollection("Wrong _treeSet value",
                               expected._treeSet, other._treeSet);
        assertEquals("Wrong _emptyTreeSet value",
                     expected._emptyTreeSet, other._emptyTreeSet);
        assertEquals("Wrong _nullTreeSet value",
                     expected._nullTreeSet, other._nullTreeSet);
        assertEqualsCollection("Wrong _sco_treeSet value",
                               expected._sco_treeSet, other._sco_treeSet);
        assertEquals("Wrong _sco_emptyTreeSet value",
                     expected._sco_emptyTreeSet, other._sco_emptyTreeSet);
        assertEquals("Wrong _sco_nullTreeSet value",
                     expected._sco_nullTreeSet, other._sco_nullTreeSet);
    }
    
    /** */
    protected void assertEqualsCollection(String msg, Collection expected, 
                                          Collection actual)
    {
        if (expected == null && actual == null)
            return;
        int expectedSize = expected.size();
        int actualSize = actual.size();
        assertEquals(msg + " collection size differs ", expectedSize, actualSize);
        Object[] expectedFrozen = Freezer.freeze(expected, expectedSize);
        Object[] actualFrozen = Freezer.freeze(actual, actualSize);
        for (int i = 0; i < expectedSize; i++) {
            Object expectedElement = expectedFrozen[i];
            Object actualElement = actualFrozen[i];
            assertEqualsExtended(msg + " element", expectedFrozen[i], 
                                 actualFrozen[i]);
        }
    }

    /** */
    protected void assertEqualsMap(String msg, Map expected, Map actual)
    {
        if (expected == null && actual == null)
            return;
        int expectedSize = expected.size();
        int actualSize = actual.size();
        assertEquals(msg + " map size differs ", expectedSize, actualSize);
        Map.Entry[] expectedFrozen = Freezer.freeze(expected, expectedSize);
        Map.Entry[] actualFrozen = Freezer.freeze(actual, actualSize);
        for (int i = 0; i < expectedSize; i++) {
            assertEqualsExtended(msg + " key", expectedFrozen[i].getKey(), actualFrozen[i].getKey());
            assertEqualsExtended(msg + " value", expectedFrozen[i].getValue(), actualFrozen[i].getValue());
        }
    }

    /** */
    protected void assertEqualsExtended(String msg, Object expected, Object actual) {
        if (expected == null && actual == null)
            return;
        if ((expected instanceof Collection) && (actual instanceof Collection))
            assertEqualsCollection(msg, (Collection)expected, (Collection)actual);
        else if ((expected instanceof Map) && (actual instanceof Map))
            assertEqualsMap(msg, (Map)expected, (Map)actual);
        else if ((expected instanceof Date) && (actual instanceof Date))
            assertEquals(msg, ((Date)expected).getTime(), ((Date)actual).getTime());
        else if ((expected instanceof Object[]) && (actual instanceof Object[]))
            assertTrue(msg, Arrays.equals((Object[])expected, (Object[])actual));
        else if ((expected instanceof boolean[]) && (actual instanceof boolean[]))
            assertTrue(msg, Arrays.equals((boolean[])expected, (boolean[])actual));
        else if ((expected instanceof byte[]) && (actual instanceof byte[]))
            assertTrue(msg, Arrays.equals((byte[])expected, (byte[])actual));
        else if ((expected instanceof char[]) && (actual instanceof char[]))
            assertTrue(msg, Arrays.equals((char[])expected, (char[])actual));
        else if ((expected instanceof int[]) && (actual instanceof int[]))
            assertTrue(msg, Arrays.equals((int[])expected, (int[])actual));
        else if ((expected instanceof long[]) && (actual instanceof long[]))
            assertTrue(msg, Arrays.equals((long[])expected, (long[])actual));
        else if ((expected instanceof float[]) && (actual instanceof float[]))
            assertTrue(msg, Arrays.equals((float[])expected, (float[])actual));
        else if ((expected instanceof double[]) && (actual instanceof double[]))
            assertTrue(msg, Arrays.equals((double[])expected, (double[])actual));
        else if ((expected instanceof Object[]) && (actual instanceof Object[]))
            assertTrue(msg, Arrays.equals((Object[])expected, (Object[])actual));
        else
            assertEquals(msg, expected, actual);
    }

    /** */
    protected void assertCollectionSize(String msg, 
                                        Collection collection, int expected) {
        int size = collection == null ? 0 : collection.size();
        if (debug) logger.debug(msg + size);
        assertEquals("Wrong size of " + msg, expected, size);
    }

    /** */
    protected void assertMapSize(String msg, Map map, int expected) {
        int size = map == null ? 0 : map.size();
        if (debug) logger.debug(msg + size);
        assertEquals("Wrong size of " + msg, expected, size);
    }

    /** */
    protected void assertNotIsDirty(String msg, Object pc) {
        boolean isDirty = JDOHelper.isDirty(pc);
        if (debug) logger.debug(msg + isDirty);
        assertFalse("Unexpected dirty instance " + pc, isDirty);
    }

    /** */
    protected void assertNullOwner(String msg, SCO sco) {
        Object owner = sco.getOwner();
        if (debug) logger.debug(msg + owner);
        assertNull(msg, owner);
    }
}
