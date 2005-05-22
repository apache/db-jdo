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

package org.apache.jdo.pc;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.jdo.test.util.Util;


/**
* Test ability to store & fetch instances of various collections.  This class
* provides three fields for each type of collection we can store: one which
* has values, one which is empty, and one which represents the field being
* null.
*
* @author Dave Bristor
*/
public class PCCollections {
    public java.util.ArrayList _arrayList;
    public java.util.ArrayList _emptyArrayList;
    public java.util.ArrayList _nullArrayList;

    public java.util.ArrayList _sco_arrayList;
    public java.util.ArrayList _sco_emptyArrayList;
    public java.util.ArrayList _sco_nullArrayList;

    public java.util.Vector _vector;
    public java.util.Vector _emptyVector;
    public java.util.Vector _nullVector;

    public java.util.Vector _sco_vector;
    public java.util.Vector _sco_emptyVector;
    public java.util.Vector _sco_nullVector;

    public java.util.HashMap _hashMap;
    public java.util.HashMap _emptyHashMap;
    public java.util.HashMap _nullHashMap;
    
    public java.util.HashMap _sco_hashMap;
    public java.util.HashMap _sco_emptyHashMap;
    public java.util.HashMap _sco_nullHashMap;
    
    public java.util.Hashtable _hashtable;
    public java.util.Hashtable _emptyHashtable;
    public java.util.Hashtable _nullHashtable;
    
    public java.util.Hashtable _sco_hashtable;
    public java.util.Hashtable _sco_emptyHashtable;
    public java.util.Hashtable _sco_nullHashtable;
    
    public java.util.HashSet _hashSet;
    public java.util.HashSet _emptyHashSet;
    public java.util.HashSet _nullHashSet;

    public java.util.HashSet _sco_hashSet;
    public java.util.HashSet _sco_emptyHashSet;
    public java.util.HashSet _sco_nullHashSet;

    public java.util.LinkedList _linkedList;
    public java.util.LinkedList _emptyLinkedList;
    public java.util.LinkedList _nullLinkedList;

    public java.util.LinkedList _sco_linkedList;
    public java.util.LinkedList _sco_emptyLinkedList;
    public java.util.LinkedList _sco_nullLinkedList;

    public java.util.TreeMap _treeMap;
    public java.util.TreeMap _emptyTreeMap;
    public java.util.TreeMap _nullTreeMap;
    
    public java.util.TreeMap _sco_treeMap;
    public java.util.TreeMap _sco_emptyTreeMap;
    public java.util.TreeMap _sco_nullTreeMap;
    
    public java.util.TreeSet _treeSet;
    public java.util.TreeSet _emptyTreeSet;
    public java.util.TreeSet _nullTreeSet;
    
    public java.util.TreeSet _sco_treeSet;
    public java.util.TreeSet _sco_emptyTreeSet;
    public java.util.TreeSet _sco_nullTreeSet;
    
    private final Date date;

    private static final BigDecimal bigDecimal = new BigDecimal(3.14159);
    private static final BigInteger bigInteger = new
        BigInteger("12345678998765432112345");
    private static final Locale locale = new Locale("fr", "CA", "MAC");

    public PCCollections() {
        date = Util.moonWalkDate.getTime();
    }

    // get methods for SCO types.

    public java.util.ArrayList getSCOArrayList() {
        return _sco_arrayList;
    }

    public java.util.Vector getSCOVector() {
        return _sco_vector;
    }

    public java.util.TreeSet getSCOTreeSet() {
        return _sco_treeSet;
    }

    public java.util.HashSet getSCOHashSet() {
        return _sco_hashSet;
    }

    public java.util.LinkedList getSCOLinkedList() {
        return _sco_linkedList;
    }

    public java.util.HashMap getSCOHashMap() {
        return _sco_hashMap;
    }

    public java.util.Hashtable getSCOHashtable() {
        return _sco_hashtable;
    }

    public java.util.TreeMap getSCOTreeMap() {
        return _sco_treeMap;
    }

    //
    // Notice the method pairs below, and that the first invokes the second.
    // In fact, once enhanced, the first will invoke the *enhanced* version of
    // the second, as defined in PCCollections, because that class is that of
    // which Test_Collections makes an instance, and so of course the derived
    // method is invoked.
    //
    
    public void setSCOArrayList(java.util.ArrayList scoArrayList) {
        _sco_arrayList = scoArrayList;
        _sco_arrayList.add(new Short((short)18));
        _sco_arrayList.add("home, home on the range...");
        _sco_arrayList.add(new Float(9876.5432f));
        _sco_arrayList.add(date);
        _sco_arrayList.add(bigDecimal);
        _sco_arrayList.add(bigInteger);
        _sco_arrayList.add(locale);
    }

    public void setSCOVector(java.util.Vector scoVector) {
        _sco_vector = scoVector;
        _sco_vector.addElement(new Byte((byte)13));
        _sco_vector.addElement("Call me Ishmael.");
        _sco_vector.addElement(new Boolean(true));
        _sco_vector.addElement(new Boolean(false));
        _sco_vector.addElement(date);
        _sco_vector.addElement(bigDecimal);
        _sco_vector.addElement(bigInteger);
        _sco_vector.addElement(locale);
    }

    public void setSCOHashSet(java.util.HashSet scoHashSet) {
        _sco_hashSet = scoHashSet;
        _sco_hashSet.add("home, home on the range...");
        _sco_hashSet.add(new Character('J'));
        _sco_hashSet.add(new Double(3.0e+6));
        _sco_hashSet.add(date);
        _sco_hashSet.add(bigDecimal);
        _sco_hashSet.add(bigInteger);
        _sco_hashSet.add(locale);
    }

    public void setSCOLinkedList(java.util.LinkedList scoLinkedList) {
        _sco_linkedList = scoLinkedList;
        _sco_linkedList.add(new Byte((byte)13));
        _sco_linkedList.add("once upon a time ...");
        _sco_linkedList.add(new Boolean(true));
        _sco_linkedList.add(new Boolean(false));
        _sco_linkedList.add(date);
        _sco_linkedList.add(bigDecimal);
        _sco_linkedList.add(bigInteger);
        _sco_linkedList.add(locale);
    }

    public void setSCOHashMap(java.util.HashMap scoHashMap) {
        _sco_hashMap = scoHashMap;
        _sco_hashMap.put("veni, vidi, vici", "in vino veritas ...");
        _sco_hashMap.put(new Integer(1), new Integer(42));
        _sco_hashMap.put(new Double(3.14159), new Float(6.02e+23));
        _sco_hashMap.put(new Character('d'), "d is 13");
        _sco_hashMap.put(new Byte((byte)0xf), date);
        _sco_hashMap.put("wombat", bigDecimal);
        _sco_hashMap.put(new Float(0.1f), bigInteger);
        _sco_hashMap.put(new Long(123456789), locale);
    }

    public void setSCOHashtable(java.util.Hashtable scoHashtable) {
        _sco_hashtable = scoHashtable;
        _sco_hashtable.put("in the beginning", "here I am ...");
        _sco_hashtable.put(new Integer(1), new Integer(42));
        _sco_hashtable.put(new Double(3.14159), new Float(6.02e+23));
        _sco_hashtable.put(new Character('d'), "d is 13");
        _sco_hashtable.put(new Byte((byte)0xf), date);
        _sco_hashtable.put("wombat", bigDecimal);
        _sco_hashtable.put(new Float(0.1f), bigInteger);
        _sco_hashtable.put(new Long(123456789), locale);
    }

    public void setSCOTreeMap(java.util.TreeMap scoTreeMap) {
        _sco_treeMap = scoTreeMap;
        _sco_treeMap.put(new Double(100), "he who knows not ...");
        _sco_treeMap.put(new Double(3), new Float(6.02e+23));
        _sco_treeMap.put(new Double(1), new Integer(42));
        _sco_treeMap.put(new Double(-123), "d is 13");
        _sco_treeMap.put(new Double(111), bigDecimal);
        _sco_treeMap.put(new Double(10), date);
        _sco_treeMap.put(new Double(0), bigInteger);
        _sco_treeMap.put(new Double(789), locale);
    }

    public void setSCOTreeSet(java.util.TreeSet scoTreeSet) {
        _sco_treeSet = scoTreeSet;
        _sco_treeSet.add(new Double(100));
        _sco_treeSet.add(new Double(3));
        _sco_treeSet.add(new Double(3.0e+6));
        _sco_treeSet.add(new Double(-123));
        _sco_treeSet.add(new Double(111));
        _sco_treeSet.add(new Double(890));
        _sco_treeSet.add(new Double(411));
    }

    // Add the given PC to each collection type.  The caller in fact *should*
    // create a PCPoint, so that we can test that collections can contain PC's
    // as well.
    public void addPoint(PCPoint p) {
        _arrayList.add(p);
        _sco_arrayList.add(p);
        _vector.addElement(p);
        _sco_vector.addElement(p);
        _hashMap.put("hello", p);
        _sco_hashMap.put("hello", p);
        _hashtable.put("hello", p);
        _sco_hashtable.put("hello", p);
        _hashSet.add(p);
        _sco_hashSet.add(p);
        _treeMap.put(new Double(300000), p);
        _sco_treeMap.put(new Double(300000), p);
        _linkedList.add(p);
        _sco_linkedList.add(p);
    }
        

    // Create a PCCollections with "interesting" values.
    public void init() {

        // ArrayList
        _arrayList = new java.util.ArrayList();
        _arrayList.add(new Integer(42));
        _arrayList.add(new Double(3.14159));
        _arrayList.add("hello-world");
        _arrayList.add(date);
        _arrayList.add(bigDecimal);
        _arrayList.add(bigInteger);
        _arrayList.add(locale);
        
        _emptyArrayList = new java.util.ArrayList();

        _nullArrayList = null;

        // SCO ArrayList
        _sco_arrayList = null; // No PM => Test_Collections uses setSCOArrayList
        _sco_emptyArrayList = new java.util.ArrayList();
        _sco_nullArrayList = null;

        // Vector
        _vector = new java.util.Vector();
        _vector.add("four score and twenty beers ago");
        _vector.add(new Double(0.0102030508));
        _vector.add(new Integer(13));
        _vector.add(new Character('z'));
        _vector.add(date);
        _vector.add(bigDecimal);
        _vector.add(bigInteger);
        _vector.add(locale);

        _emptyVector = new java.util.Vector();

        _nullVector = null;

        // SCO Vector
        _sco_vector = null; // No PM => Test_Collections uses setSCOVector
        _sco_emptyVector = new java.util.Vector();
        _sco_nullVector = null;

        // HashMap
        _hashMap = new java.util.HashMap();
        _hashMap.put(new Integer(1), new Integer(42));
        _hashMap.put(new Double(3.14159), new Float(6.02e+23));
        _hashMap.put(new Character('d'), "d is 13");
        _hashMap.put(new Byte((byte)0xf), date);
        _hashMap.put("wombat", bigDecimal);
        _hashMap.put(new Float(0.1f), bigInteger);
        _hashMap.put(new Long(123456789), locale);

        _emptyHashMap = new HashMap();

        _nullHashMap = null;

        // SCO HashMap
        _sco_hashMap = null; // No PM => Test_Collections uses setSCOHashMap
        _sco_emptyHashMap = new java.util.HashMap();
        _sco_nullHashMap = null;

        // Hashtable
        _hashtable = new java.util.Hashtable();
        _hashtable.put(new Integer(1), new Integer(42));
        _hashtable.put(new Double(3.14159), new Float(6.02e+23));
        _hashtable.put(new Character('d'), "d is 13");
        _hashtable.put(new Byte((byte)0xf), date);
        _hashtable.put("wombat", bigDecimal);
        _hashtable.put(new Float(0.1f), bigInteger);
        _hashtable.put(new Long(123456789), locale);

        _emptyHashtable = new Hashtable();

        _nullHashtable = null;

        // SCO Hashtable
        _sco_hashtable = null; // No PM => Test_Collections uses setSCOHashtable
        _sco_emptyHashtable = new java.util.Hashtable();
        _sco_nullHashtable = null;

        // HashSet
        _hashSet = new java.util.HashSet();
        _hashSet.add(new Integer(13));
        _hashSet.add(new Float(6.02e+23));
        _hashSet.add("when I was back there in seminary school, ...");
        _hashSet.add(date);
        _hashSet.add(bigDecimal);
        _hashSet.add(bigInteger);
        _hashSet.add(locale);

        _emptyHashSet = new java.util.HashSet();

        _nullHashSet = null;

        // SCO HashSet
        _sco_hashSet = null; // No PM => Test_Collections uses setSCOHashSet
        _sco_emptyHashSet = new java.util.HashSet();
        _sco_nullHashSet = null;

        // LinkedList
        _linkedList = new java.util.LinkedList();
        _linkedList.add(new Double(.00001));
        _linkedList.add(new Integer(3800));
        _linkedList.add("four score and twenty years ago");
        _linkedList.add(date);
        _linkedList.add(bigDecimal);
        _linkedList.add(bigInteger);
        _linkedList.add(locale);

        _emptyLinkedList = new LinkedList();

        _nullLinkedList = null;        

        // SCO LinkedList
        _sco_linkedList = null; // No PM => Test_Collections uses setSCOLinkedList
        _sco_emptyLinkedList = new java.util.LinkedList();
        _sco_nullLinkedList = null;

        // TreeMap
        // Keys must all be of same type for TreeMap.
        _treeMap = new java.util.TreeMap();
        _treeMap.put(new Double(6.02e+23), new Integer(42));
        _treeMap.put(new Double(3.14159), new Float(6.02e+23));
        _treeMap.put(new Double(186000), "d is 13");
        _treeMap.put(new Double(0.1), date);
        _treeMap.put(new Double(0.12), bigDecimal);
        _treeMap.put(new Double(0.123), bigInteger);
        _treeMap.put(new Double(0.1234), locale);

        _emptyTreeMap = new TreeMap();

        _nullTreeMap = null;

        // SCO TreeMap
        _sco_treeSet = null; // No PM => Test_Collections uses setSCOTreeMap
        _sco_emptyTreeMap = new java.util.TreeMap();
        _sco_nullTreeMap = null;

        // TreeSet
        _treeSet = new java.util.TreeSet();
        _treeSet.add(new Double(13));
        _treeSet.add(new Double(6.02e+23));
        _treeSet.add(new Double(186000));
        _treeSet.add(new Double(0.1234));
        _treeSet.add(new Double(0.123));
        _treeSet.add(new Double(0.12));
        _treeSet.add(new Double(0.1));

        _emptyTreeSet = new java.util.TreeSet();

        _nullTreeSet = null;

        // SCO TreeSet
        _sco_treeSet = null; // No PM => Test_Collections uses setSCOTreeSet
        _sco_emptyTreeSet = new java.util.TreeSet();
        _sco_nullTreeSet = null;

        //
        // Extra Collection and Map tests, putting maps and arrays into
        // collections and maps.
        //

        // Don't reuse the above _arrayList and _hashMap because they contain
        // Date objects, which I've determined empirically to have different
        // toString() representations in different JDK's (e.g. on Solaris and
        // WinNT).
        java.util.ArrayList al = new java.util.ArrayList();
        al.add(new Integer(42));
        al.add("hello-world");
        al.add(bigDecimal);
        al.add(locale);

        java.util.HashMap hm = new java.util.HashMap();
        hm = new java.util.HashMap();
        hm.put(new Integer(1), new Integer(42));
        hm.put(new Character('d'), "d is 13");
        hm.put("wombat", bigDecimal);
        hm.put(new Float(0.1f), bigInteger);

        int xyz[] = new int[] { 1, 2, 3, 4, 5 }; // Array of primitive
        Date dates[] = new Date[] { date, date }; // Array of Object

        // Extra Collection tests: Collection of Collection, Collection of
        // Map, Collection of array of primitive, Collection of array of Object.
        _vector.add(al); // Collection of Collection
        _vector.add(hm); // Collection of Map
        _vector.add(xyz);
        _vector.add(dates);

        // Extra tests: Map of Map, Map of Collection, Map of array of
        // primitive, Map of array of Object.
        _treeMap.put(new Double(1234.5), al); // Map of collection
        _treeMap.put(new Double(1.0), hm); // Map of Map
        _treeMap.put(new Double(2.345), xyz); // Map of array of primitive
        _treeMap.put(new Double(0.0000000000000009), dates); // Map of array of SCO
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(Util.getClassName(this));
        StringBuffer info;
        try {
            // ArrayList
            rc.append(Util.stringifyList(_arrayList,
                                         Util.getInfo("_arrayList", _arrayList)));
            rc.append(Util.stringifyList(_emptyArrayList, "_emptyArrayList"));
            rc.append(Util.stringifyList(_nullArrayList, "_nullArrayList"));
            rc.append("\n");

            // SCO ArrayList
            rc.append(Util.stringifyList(_sco_arrayList,
                                         Util.getInfo("_sco_arrayList", _sco_arrayList)));
            rc.append(Util.stringifyList(_sco_emptyArrayList, "_sco_emptyArrayList"));
            rc.append(Util.stringifyList(_sco_nullArrayList, "_sco_nullArrayList"));
            rc.append("\n");

            // Vector
            rc.append(Util.stringifyList(_vector, Util.getInfo("_vector", _vector)));
            rc.append(Util.stringifyList(_emptyVector, "_emptyVector"));
            rc.append(Util.stringifyList(_nullVector, "_nullVector"));
            rc.append("\n");

            // SCO Vector
            rc.append(Util.stringifyList(_sco_vector,
                                         Util.getInfo("_sco_vector", _sco_vector)));
            rc.append(Util.stringifyList(_sco_emptyVector, "_sco_emptyVector"));
            rc.append(Util.stringifyList(_sco_nullVector, "_sco_nullVector"));
            rc.append("\n");

            // HashMap
            rc.append(Util.stringifyMap(_hashMap, Util.getInfo("_hashMap", _hashMap)));
            rc.append(Util.stringifyMap(_emptyHashMap, "_emptyHashMap"));
            rc.append(Util.stringifyMap(_nullHashMap, "_nullHashMap"));
            rc.append("\n");

            // SCO HashMap
            rc.append(Util.stringifyMap(_sco_hashMap,
                                        Util.getInfo("_sco_hashMap", _sco_hashMap)));
            rc.append(Util.stringifyMap(_sco_emptyHashMap, "_sco_emptyHashMap"));
            rc.append(Util.stringifyMap(_sco_nullHashMap, "_sco_nullHashMap"));
            rc.append("\n");

            // Hashtable
            rc.append(Util.stringifyMap(_hashtable, Util.getInfo("_hashtable", _hashtable)));
            rc.append(Util.stringifyMap(_emptyHashtable, "_emptyHashtable"));
            rc.append(Util.stringifyMap(_nullHashtable, "_nullHashtable"));
            rc.append("\n");

            // SCO Hashtable
            rc.append(Util.stringifyMap(_sco_hashtable,
                                        Util.getInfo("_sco_hashtable", _sco_hashtable)));
            rc.append(Util.stringifyMap(_sco_emptyHashtable, "_sco_emptyHashtable"));
            rc.append(Util.stringifyMap(_sco_nullHashtable, "_sco_nullHashtable"));
            rc.append("\n");

            // HashSet
            rc.append(Util.stringifySet(_hashSet, Util.getInfo("_hashSet", _hashSet)));
            rc.append(Util.stringifySet(_emptyHashSet, "_emptyHashSet"));
            rc.append(Util.stringifySet(_nullHashSet, "_nullHashSet"));
            rc.append("\n");

            // SCO HashSet
            rc.append(Util.stringifySet(_sco_hashSet,
                                        Util.getInfo("_sco_hashSet", _sco_hashSet)));
            rc.append(Util.stringifySet(_sco_emptyHashSet, "_sco_emptyHashSet"));
            rc.append(Util.stringifySet(_sco_nullHashSet, "_sco_nullHashSet"));
            rc.append("\n");

            // LinkedList
            rc.append(Util.stringifyList(_linkedList, Util.getInfo("_linkedList", _linkedList)));
            rc.append(Util.stringifyList(_emptyLinkedList, "_emptyLinkedList"));
            rc.append(Util.stringifyList(_nullLinkedList, "_nullLinkedList"));
            rc.append("\n");

            // SCO LinkedList
            rc.append(Util.stringifyList(_sco_linkedList,
                                         Util.getInfo("_sco_linkedList", _sco_linkedList)));
            rc.append(Util.stringifyList(_sco_emptyLinkedList, "_sco_emptyLinkedList"));
            rc.append(Util.stringifyList(_sco_nullLinkedList, "_sco_nullLinkedList"));
            rc.append("\n");

            // TreeMap
            rc.append(Util.stringifyMap(_treeMap, Util.getInfo("_treeMap", _treeMap)));
            rc.append(Util.stringifyMap(_emptyTreeMap, "_emptyTreeMap"));
            rc.append(Util.stringifyMap(_nullTreeMap, "_nullTreeMap"));
            rc.append("\n");

            // SCO TreeMap
            rc.append(Util.stringifyMap(_sco_treeMap, Util.getInfo("_sco_treeMap", _sco_treeMap)));
            rc.append(Util.stringifyMap(_sco_emptyTreeMap, "_sco_emptyTreeMap"));
            rc.append(Util.stringifyMap(_sco_nullTreeMap, "_sco_nullTreeMap"));
            rc.append("\n");

            // TreeSet
            rc.append(Util.stringifySet(_treeSet, Util.getInfo("_treeSet", _treeSet)));
            rc.append(Util.stringifySet(_emptyTreeSet, "_emptyTreeSet"));
            rc.append(Util.stringifySet(_nullTreeSet, "_nullTreeSet"));
            rc.append("\n");

            // SCO TreeSet
            rc.append(Util.stringifySet(_sco_treeSet,
                                        Util.getInfo("_sco_treeSet", _sco_treeSet)));
            rc.append(Util.stringifySet(_sco_emptyTreeSet, "_sco_emptyTreeSet"));
            rc.append(Util.stringifySet(_sco_nullTreeSet, "_sco_nullTreeSet"));
            rc.append("\n");

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return (rc.toString() + "\nPCCollections has null values");
        }
        return rc.toString();
    }
}
