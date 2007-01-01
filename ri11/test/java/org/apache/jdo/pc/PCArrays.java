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

package org.apache.jdo.pc;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.jdo.test.util.Util;

/**
* Contains 3 arrays of each supported type: one with elements, one without,
* and one whose reference is null
*
* @author Dave Bristor
*/
public class PCArrays {
    // boolean
    public boolean _booleanArray[];
    public boolean _emptyBooleanArray[];
    public boolean _nullBooleanArray[];

    public Boolean _lang_BooleanArray[];
    public Boolean _empty_lang_BooleanArray[];
    public Boolean _null_lang_BooleanArray[];

    // char
    public char _charArray[];
    public char _emptyCharArray[];
    public char _nullCharArray[];

    public Character _lang_CharacterArray[];
    public Character _empty_lang_CharacterArray[];
    public Character _null_lang_CharacterArray[];

    // byte
    public byte _byteArray[];
    public byte _emptyByteArray[];
    public byte _nullByteArray[];

    public Byte _lang_ByteArray[];
    public Byte _empty_lang_ByteArray[];
    public Byte _null_lang_ByteArray[];

    // short
    public short _shortArray[];
    public short _emptyShortArray[];
    public short _nullShortArray[];
    
    public Short _lang_ShortArray[];
    public Short _empty_lang_ShortArray[];
    public Short _null_lang_ShortArray[];

    // int
    public int _intArray[];
    public int _emptyIntArray[];
    public int _nullIntArray[];

    public Integer _lang_IntegerArray[];
    public Integer _empty_lang_IntegerArray[];
    public Integer _null_lang_IntegerArray[];

    // long
    public long _longArray[];
    public long _emptyLongArray[];
    public long _nullLongArray[];

    public Long _lang_LongArray[];
    public Long _empty_lang_LongArray[];
    public Long _null_lang_LongArray[];

    // float
    public float _floatArray[];
    public float _emptyFloatArray[];
    public float _nullFloatArray[];

    public Float _lang_FloatArray[];
    public Float _empty_lang_FloatArray[];
    public Float _null_lang_FloatArray[];

    // double
    public double _doubleArray[];
    public double _emptyDoubleArray[];
    public double _nullDoubleArray[];

    public Double _lang_DoubleArray[];
    public Double _empty_lang_DoubleArray[];
    public Double _null_lang_DoubleArray[];

    // String
    public String _stringArray[];
    public String _emptyStringArray[];
    public String _nullStringArray[];

    private final Date date;

    private static final BigDecimal bigDecimal = new BigDecimal(3.14159);
    private static final BigInteger bigInteger = new
        BigInteger("12345678998765432112345");
    private static final Locale locale = new Locale("fr", "CA", "MAC");

    // Date
    public Date _dateArray[];
    public Date _emptyDateArray[];
    public Date _nullDateArray[];

    // BigDecimal
    public BigDecimal _bigDecimalArray[];
    public BigDecimal _emptyBigDecimalArray[];
    public BigDecimal _nullBigDecimalArray[];
    
    // BigInteger
    public BigInteger _bigIntegerArray[];
    public BigInteger _emptyBigIntegerArray[];
    public BigInteger _nullBigIntegerArray[];

    // Locale
    public Locale _localeArray[];
    public Locale _emptyLocaleArray[];
    public Locale _nullLocaleArray[];

    // ArrayList
    public ArrayList _arrayListArray[];
    public ArrayList _emptyArrayListArray[];
    public ArrayList _nullArrayListArray[];

    // HashMap
    public HashMap _hashMapArray[];
    public HashMap _emptyHashMapArray[];
    public HashMap _nullHashMapArray[];

    // HashSet
    public HashSet _hashSetArray[];
    public HashSet _emptyHashSetArray[];
    public HashSet _nullHashSetArray[];

    // Hashtable
    public Hashtable _hashtableArray[];
    public Hashtable _emptyHashtableArray[];
    public Hashtable _nullHashtableArray[];

    // LinkedList 
    public LinkedList _linkedListArray[];
    public LinkedList _emptyLinkedListArray[];
    public LinkedList _nullLinkedListArray[];

    // TreeMap
    public TreeMap _treeMapArray[];
    public TreeMap _emptyTreeMapArray[];
    public TreeMap _nullTreeMapArray[];

    // TreeSet
    public TreeSet _treeSetArray[];
    public TreeSet _emptyTreeSetArray[];
    public TreeSet _nullTreeSetArray[];

    // Vector
    public Vector _vectorArray[];
    public Vector _emptyVectorArray[];
    public Vector _nullVectorArray[];

    public PCArrays() { 
        date = Util.moonWalkDate.getTime();
    }
    
    // Create some arrays with "interesting" values.
    //
    public void init() {
        // boolean
        _booleanArray = new boolean[] { true, false, true, false};
        _emptyBooleanArray = new boolean[0];
        _nullBooleanArray = null;
        
        _lang_BooleanArray = new Boolean[] {
            new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false)};
        _empty_lang_BooleanArray = new Boolean[0];
        _null_lang_BooleanArray = null;

        // char
        _charArray = new char[] { 'h', 'e', 'l', 'l', 'o' };
        _emptyCharArray = new char[0];
        _nullCharArray = null;
        
        _lang_CharacterArray = new Character[] {
            new Character('w'), new Character('o'), new Character('r'),
                new Character('l'), new Character('d') };
        _empty_lang_CharacterArray = new Character[0];
        _null_lang_CharacterArray = null;

        // byte
        _byteArray = new byte[] { 0xf, 0xe, 0xd, 0xc, 0xb, 0xa };
        _emptyByteArray = new byte[0];
        _nullByteArray = null;
        
        _lang_ByteArray = new Byte[] {
            new Byte((byte)0xa), new Byte((byte)0xb), new Byte((byte)0xc), new
                Byte((byte)0xd), new Byte((byte)0xe), new Byte((byte)0xf) };
        _empty_lang_ByteArray = new Byte[0];
        _null_lang_ByteArray = null;

        // short
        _shortArray = new short[] { 10, 20, 30, 40 };
        _emptyShortArray = new short[0];
        _nullShortArray = null;
        
        _lang_ShortArray = new Short[] {
            new Short((short)10), new Short((short)9), new Short((short)8),
                new Short((short)7), new Short((short)6) };
        _empty_lang_ShortArray = new Short[0];
        _null_lang_ShortArray = null;

        // int
        _intArray = new int[] { 1, 3, 5 };
        _emptyIntArray = new int[0];
        _nullIntArray = null;

        _lang_IntegerArray = new Integer[] {
            new Integer(2), new Integer(4), new Integer(6) };
        _empty_lang_IntegerArray = new Integer[0];
        _null_lang_IntegerArray = null;

        // long
        _longArray = new long[] { Long.MIN_VALUE, 0L, Long.MAX_VALUE };
        _emptyLongArray = new long[0];
        _nullLongArray = null;
        
        _lang_LongArray = new Long[] {
            new Long(-2), new Long(-1), new Long(0), new Long(1), new Long(2) };
        _empty_lang_LongArray = new Long[0];
        _null_lang_LongArray = null;
        
        // float
        _floatArray = new float[] { 6.02e+23f, 3.14159f, 186000.0f };
        _emptyFloatArray = new float[0];
        _nullFloatArray = null;

        _lang_FloatArray = new Float[] {
            new Float(3.14159f), new Float(6.02e+23f), new Float(186000.0f) };
        _empty_lang_FloatArray = new Float[0];
        _null_lang_FloatArray = null;
        
        // double
        _doubleArray = new double[] { 0.0, 1.0, 2.0 };
        _emptyDoubleArray = new double[0];
        _nullDoubleArray = null;

        _lang_DoubleArray = new Double[] {
            new Double(3.333), new Double(5.55555), new Double(7.7777777) };
        _empty_lang_DoubleArray = new Double[0];
        _null_lang_DoubleArray = null;

        // String
        _stringArray = new String[] { "hello", "world", "hi", "mom" };
        _emptyStringArray = new String[0];
        _nullStringArray = null;

        // Date
        _dateArray = new Date[] { date, date };
        _emptyDateArray = new Date[0];
        _nullDateArray = null;

        // BigDecimal
        _bigDecimalArray = new BigDecimal[] {
            new BigDecimal(3.1415), new BigDecimal(6.02e+23) };
        _emptyBigDecimalArray = new BigDecimal[0];
        _nullBigDecimalArray = null;

        // BigInteger
        _bigIntegerArray = new BigInteger[] {
            new BigInteger("123456789"), new BigInteger("987654321") };
        _emptyBigIntegerArray = new BigInteger[0];
        _nullBigIntegerArray = null;

        // Locale
        _localeArray = new Locale[] { locale, locale, locale };
        _emptyLocaleArray = new Locale[0];
        _nullLocaleArray = null;

        // ArrayList
        _arrayListArray = new ArrayList[2];
        ArrayList al = new ArrayList();
        al.add("hello");
        al.add(new Double(3.14159));
        _arrayListArray[0] = al;
        
        al = new ArrayList();
        al.add(new Integer(42));
        al.add(new Boolean(true));
        al.add(new Byte((byte)0xd));
        _arrayListArray[1] = al;
        
        _emptyArrayListArray = new ArrayList[0];
        _nullArrayListArray = null;

        // HashMap
        _hashMapArray = new HashMap[2];
        HashMap hm = new HashMap();
        hm.put(new Character('d'), "d is 13");
        hm.put("wombat", bigDecimal);
        _hashMapArray[0] = hm;
        
        hm = new HashMap();
        hm.put(new Integer(1), new Integer(42));
        hm.put(new Double(3.14159), new Float(6.02e+23));
        _hashMapArray[1] = hm;
        
        _emptyHashMapArray = new HashMap[0];
        _nullHashMapArray = null;

        // HashSet
        _hashSetArray = new HashSet[3];
        HashSet hs = new HashSet();
        hs.add("home, home on the range...");
        hs.add(new Character('J'));
        _hashSetArray[0] = hs;
        
        hs = new HashSet();
        hs.add(new Double(3.0e+6));
        hs.add(new Integer(13));
        _hashSetArray[1] = hs;
        
        hs = new HashSet();
        hs.add(new Float(6.02e+23));
        hs.add("when I was back there in seminary school, ...");
        _hashSetArray[2] = hs;
        
        _emptyHashSetArray = new HashSet[0];
        _nullHashSetArray = null;

        // Hashtable
        _hashtableArray = new Hashtable[2];
        Hashtable ht = new Hashtable();
        ht.put(new Character('d'), "d is 13");
        ht.put("wombat", bigDecimal);
        _hashtableArray[0] = ht;
        
        ht = new Hashtable();
        ht.put(new Integer(1), new Integer(42));
        ht.put(new Double(3.14159), new Float(6.02e+23));
        _hashtableArray[1] = ht;
        
        _emptyHashtableArray = new Hashtable[0];
        _nullHashtableArray = null;

        // LinkedList
        _linkedListArray = new LinkedList[2];
        LinkedList ll = new LinkedList();
        ll.add("'hello, world'");
        ll.add(new Double(1234.56789));
        _linkedListArray[0] = ll;

        ll = new LinkedList();
        ll.add(new Integer(9876));
        _linkedListArray[1] = ll;

        _emptyLinkedListArray = new LinkedList[0];
        _nullLinkedListArray = null;
        

        // TreeMap
        _treeMapArray = new TreeMap[2];
        TreeMap tm = new TreeMap();
        tm.put(new Double(6.02e+23), new Integer(42));
        tm.put(new Double(3.14159), new Float(6.02e+23));
        _treeMapArray[0] = tm;
        
        tm = new TreeMap();
        tm.put(new Double(186000), "d is 13");
        _treeMapArray[1] = tm;
        
        _emptyTreeMapArray = new TreeMap[0];
        _nullTreeMapArray = null;

        // TreeSet
        _treeSetArray = new TreeSet[2];
        TreeSet ts = new TreeSet();
        ts.add(new Double(6.02e+23));
        ts.add(new Double(3.14159));
        _treeSetArray[0] = ts;
        
        ts = new TreeSet();
        ts.add(new Double(186000));
        _treeSetArray[1] = ts;
        
        _emptyTreeSetArray = new TreeSet[0];
        _nullTreeSetArray = null;

        // Vector
        _vectorArray = new Vector[3];
        Vector v = new Vector();
        v.addElement(new Byte((byte)13));
        v.addElement(new Boolean(true));
        _vectorArray[0] = v;
        
        v = new Vector();
        v.addElement("Call me Ishmael.");
        v.addElement(new Boolean(false));
        _vectorArray[1] = v;
        
        v = new Vector();
        v.add("four score and twenty beers ago");
        v.add(new Double(0.0102030508));
        _vectorArray[2] = v;
        
        _emptyVectorArray = new Vector[0];
        _nullVectorArray = null;
    }

    String stringify(Object arr, String name) {
        StringBuffer rc =new StringBuffer("\n").append(name).append(": ");

        if (null == arr) {
            rc.append(" __null__");
        } else try {
            int length = Array.getLength(arr);
            if (0 == length) {
                rc.append(" __empty__");
            } else {
                for (int i = 0; i < length; i++) {
                    if (i > 0) {
                        rc.append(",");
                    }
                    rc.append(" ");
                    Object obj = Array.get(arr, i);
                    if (obj instanceof Date) {
                        rc.append(Util.longFormatter.format((Date)obj));
                    } else if (obj instanceof Map) {
                        rc.append(Util.sortMap((Map)obj));
                    } else {
                        rc.append(obj);
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("illegal argument: " + arr.getClass().getName());
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("out of bounds: " + arr.getClass().getName());
        }

        return rc.toString();
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(Util.getClassName(this));

        // boolean
        rc.append(stringify(_booleanArray, "_booleanArray"));
        rc.append(stringify(_emptyBooleanArray, "_emptyBooleanArray"));
        rc.append(stringify(_nullBooleanArray, "_nullBooleanArray"));
        rc.append("\n");

        rc.append(stringify(_lang_BooleanArray, "_lang_BooleanArray"));
        rc.append(stringify(_empty_lang_BooleanArray, "_empty_lang_BooleanArray"));
        rc.append(stringify(_null_lang_BooleanArray, "_null_lang_BooleanArray"));
        rc.append("\n");
        
        // char
        rc.append(stringify(_charArray, "_charArray"));
        rc.append(stringify(_emptyCharArray, "_emptyCharArray"));
        rc.append(stringify(_nullCharArray, "_nullCharArray"));
        rc.append("\n");
            
        rc.append(stringify(_lang_CharacterArray, "_lang_CharacterArray"));
        rc.append(stringify(_empty_lang_CharacterArray, "_empty_lang_CharacterArray"));
        rc.append(stringify(_null_lang_CharacterArray, "_null_lang_CharacterArray"));
        rc.append("\n");
            
        // byte
        rc.append(stringify(_byteArray, "_byteArray"));
        rc.append(stringify(_emptyByteArray, "_emptyByteArray"));
        rc.append(stringify(_nullByteArray, "_nullByteArray"));
        rc.append("\n");
            
        rc.append(stringify(_lang_ByteArray, "_lang_ByteArray"));
        rc.append(stringify(_empty_lang_ByteArray, "_empty_lang_ByteArray"));
        rc.append(stringify(_null_lang_ByteArray, "_null_lang_ByteArray"));
        rc.append("\n");
            
        // short
        rc.append(stringify(_shortArray, "_shortArray"));
        rc.append(stringify(_emptyShortArray, "_emptyShortArray"));
        rc.append(stringify(_nullShortArray, "_nullShortArray"));
        rc.append("\n");

        rc.append(stringify(_lang_ShortArray, "_lang_ShortArray"));
        rc.append(stringify(_empty_lang_ShortArray, "_empty_lang_ShortArray"));
        rc.append(stringify(_null_lang_ShortArray, "_null_lang_ShortArray"));
        rc.append("\n");

        // int
        rc.append(stringify(_intArray, "_intArray"));
        rc.append(stringify(_emptyIntArray, "_emptyIntArray"));
        rc.append(stringify(_nullIntArray, "_nullIntArray"));
        rc.append("\n");

        rc.append(stringify(_lang_IntegerArray, "_lang_IntegerArray"));
        rc.append(stringify(_empty_lang_IntegerArray, "_empty_lang_IntegerArray"));
        rc.append(stringify(_null_lang_IntegerArray, "_null_lang_IntegerArray"));
        rc.append("\n");

        // long
        rc.append(stringify(_longArray, "_longArray"));
        rc.append(stringify(_emptyLongArray, "_emptyLongArray"));
        rc.append(stringify(_nullLongArray, "_nullLongArray"));
        rc.append("\n");
        
        rc.append(stringify(_lang_LongArray, "_lang_LongArray"));
        rc.append(stringify(_empty_lang_LongArray, "_empty_lang_LongArray"));
        rc.append(stringify(_null_lang_LongArray, "_null_lang_LongArray"));
        rc.append("\n");
        
        // float
        rc.append(stringify(_floatArray, "_floatArray"));
        rc.append(stringify(_emptyFloatArray, "_emptyFloatArray"));
        rc.append(stringify(_nullFloatArray, "_nullFloatArray"));
        rc.append("\n");
            
        rc.append(stringify(_lang_FloatArray, "_lang_FloatArray"));
        rc.append(stringify(_empty_lang_FloatArray, "_empty_lang_FloatArray"));
        rc.append(stringify(_null_lang_FloatArray, "_null_lang_FloatArray"));
        rc.append("\n");
            
        // double
        rc.append(stringify(_doubleArray, "_doubleArray"));
        rc.append(stringify(_emptyDoubleArray, "_emptyDoubleArray"));
        rc.append(stringify(_nullDoubleArray, "_nullDoubleArray"));
        rc.append("\n");
            
        rc.append(stringify(_lang_DoubleArray, "_lang_DoubleArray"));
        rc.append(stringify(_empty_lang_DoubleArray, "_empty_lang_DoubleArray"));
        rc.append(stringify(_null_lang_DoubleArray, "_null_lang_DoubleArray"));
        rc.append("\n");
            
        // String
        rc.append(stringify(_stringArray, "_stringArray"));
        rc.append(stringify(_emptyStringArray, "_emptyStringArray"));
        rc.append(stringify(_nullStringArray, "_nullStringArray"));
        rc.append("\n");

        // Date
        rc.append(stringify(_dateArray, "_dateArray"));
        rc.append(stringify(_emptyDateArray, "_emptyDateArray"));
        rc.append(stringify(_nullDateArray, "_nullDateArray"));
        rc.append("\n");

        // BigDecimal
        rc.append(stringify(_bigDecimalArray, "_bigDecimalArray"));
        rc.append(stringify(_emptyBigDecimalArray, "_emptyBigDecimalArray"));
        rc.append(stringify(_nullBigDecimalArray, "_nullBigDecimalArray"));
        rc.append("\n");

        // BigInteger
        rc.append(stringify(_bigIntegerArray, "_bigIntegerArray"));
        rc.append(stringify(_emptyBigIntegerArray, "_emptyBigIntegerArray"));
        rc.append(stringify(_nullBigIntegerArray, "_nullBigIntegerArray"));
        rc.append("\n");

        // Locale
        rc.append(stringify(_localeArray, "_localeArray"));
        rc.append(stringify(_emptyLocaleArray, "_emptyLocaleArray"));
        rc.append(stringify(_nullLocaleArray, "_nullLocaleArray"));
        rc.append("\n");

        // ArrayList
        rc.append(stringify(_arrayListArray, "_arrayListArray"));
        rc.append(stringify(_emptyArrayListArray, "_emptyArrayListArray"));
        rc.append(stringify(_nullArrayListArray, "_nullArrayListArray"));
        rc.append("\n");

        // HashMap
        rc.append(stringify(_hashMapArray, "_hashMapArray"));
        rc.append(stringify(_emptyHashMapArray, "_emptyHashMapArray"));
        rc.append(stringify(_nullHashMapArray, "_nullHashMapArray"));
        rc.append("\n");

        // HashSet
        rc.append(stringify(_hashSetArray, "_hashSetArray"));
        rc.append(stringify(_emptyHashSetArray, "_emptyHashSetArray"));
        rc.append(stringify(_nullHashSetArray, "_nullHashSetArray"));
        rc.append("\n");

        // Hashtable
        rc.append(stringify(_hashtableArray, "_hashtableArray"));
        rc.append(stringify(_emptyHashtableArray, "_emptyHashtableArray"));
        rc.append(stringify(_nullHashtableArray, "_nullHashtableArray"));
        rc.append("\n");

        // LinkedList
        rc.append(stringify(_linkedListArray, "_linkedListArray"));
        rc.append(stringify(_emptyLinkedListArray, "_emptyLinkedListArray"));
        rc.append(stringify(_nullLinkedListArray, "_nullLinkedListArray"));
        rc.append("\n");

        // TreeMap
        rc.append(stringify(_treeMapArray, "_treeMapArray"));
        rc.append(stringify(_emptyTreeMapArray, "_emptyTreeMapArray"));
        rc.append(stringify(_nullTreeMapArray, "_nullTreeMapArray"));
        rc.append("\n");

        // TreeSet
        rc.append(stringify(_treeSetArray, "_treeSetArray"));
        rc.append(stringify(_emptyTreeSetArray, "_emptyTreeSetArray"));
        rc.append(stringify(_nullTreeSetArray, "_nullTreeSetArray"));
        rc.append("\n");

        // Vector
        rc.append(stringify(_vectorArray, "_vectorArray"));
        rc.append(stringify(_emptyVectorArray, "_emptyVectorArray"));
        rc.append(stringify(_nullVectorArray, "_nullVectorArray"));
        rc.append("\n");

        return rc.toString();
    }
}
