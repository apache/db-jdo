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

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.util.I18NHelper;


/**
* Represents a class id.  These are in no way visible to clients, but are an
* integral part of an OID.  Each distinct CLID represents a different class.
* In this sense, a 'different class' is one which is structurally distinct
* from all others.  Two classes with (e.g.) different fields with the same
* name will therefore be considered different and hence have different
* CLID's (methods, however, are not considered by structural equivalence).
* @see OID
*
* @author Dave Bristor
*/
// XXX TBD Remote: Allocate provisional CLID's per-PMF w/ remote store.
class CLID {
    private static final int PROVISIONAL_MASK = 0x01000000;

    // Indicates if this CLID is provisional.
    private final boolean provisional;

    // The "value" of this CLID.
    private final int id;

    // Value used to create the next provisional CLID
    private static int nextProvisional = 0;

    // For synchronizing access to nextProvisional;
    private static final Integer lock = new Integer(1);

    // Hashcode uniquely identifying this CLID within this JVM.
    private final int hashCode;

    // Minimum value for a CLID that is created by code that is a client of
    // this class.  See the knownCLID table below, and make sure that it has
    // less than this many elements.
    private static final int MIN_USER_CLID = 100;

    /**
    * We reserve the first 100 CLID's for internal use.  We have no purpose
    * for them (yet) but this allows for future expansion.
    */
    public static final CLID firstCLID = new CLID(MIN_USER_CLID, false);

    private static I18NHelper msg = I18NHelper.getInstance(I18N.NAME);
    
    private CLID(int id, boolean provisional) {
        this.id = id;
        this.provisional = provisional;
        this.hashCode = new Integer(id).hashCode();
    }

    static CLID createProvisional() {
        CLID rc = null;
        synchronized(lock) {
            nextProvisional++;
            if (nextProvisional <= OID.MAX_CLID) {
                rc = new CLID(nextProvisional, true);
            }
        }
        if (null == rc) {
            throw new FOStoreFatalInternalException(
                CLID.class, "createProvisional", // NOI18N
                msg.msg("ERR_OutOfProvisionalCLIDs", // NOI18N
                        new Integer(OID.MAX_CLID)));
        }
        return rc;
    }

    static CLID create(int id, boolean provisional) {
        if (id > OID.MAX_CLID) {
            throw new FOStoreFatalInternalException(
                CLID.class, "create", // NOI18N
                msg.msg("ERR_InvalidRange", new Integer(id), // NOI18N
                        new Integer(MIN_USER_CLID), new Integer(OID.MAX_CLID)));
        }
        return new CLID(id, provisional);
    }

    // These are to be able to get the class of an array of a type.  They're
    // not private, in order to allow FOStoreTranscriber.ObjectTranscriber to
    // share them.
    static final OID oidArray[]         = new OID[0];
    static final boolean booleanArray[] = new boolean[0];
    static final char charArray[]       = new char[0];
    static final byte byteArray[]       = new byte[0];
    static final short shortArray[]     = new short[0];
    static final int intArray[]         = new int[0];
    static final long longArray[]       = new long[0];
    static final float floatArray[]     = new float[0];
    static final double doubleArray[]   = new double[0];
    
    static final Boolean BooleanArray[] = new Boolean[0];
    static final Character CharacterArray[]       = new Character[0];
    static final Byte ByteArray[]       = new Byte[0];
    static final Short ShortArray[]     = new Short[0];
    static final Integer IntegerArray[]         = new Integer[0];
    static final Long LongArray[]       = new Long[0];
    static final Float FloatArray[]     = new Float[0];
    static final Double DoubleArray[]   = new Double[0];
    
    static final String StringArray[]   = new String[0];

    //static final TreeMap TreeMapArray[] = new TreeMap[0];
    static final BigDecimal BigDecimalArray[] = new BigDecimal[0];
    static final BigInteger BigIntegerArray[] = new BigInteger[0];
    static final BitSet BitSetArray[] = new BitSet[0];
    static final Locale LocaleArray[] = new Locale[0];

    static final java.util.Date utilDateArray[] = new java.util.Date[0];
    static final org.apache.jdo.impl.sco.Date scoDateArray[] = new org.apache.jdo.impl.sco.Date[0];
    static final java.sql.Date sqlDateArray[] = new java.sql.Date[0];
    static final org.apache.jdo.impl.sco.SqlDate scoSqlDateArray[] = new org.apache.jdo.impl.sco.SqlDate[0];

    static final java.util.ArrayList utilArrayListArray[] = new java.util.ArrayList[0];
    static final org.apache.jdo.impl.sco.ArrayList scoArrayListArray[] = new org.apache.jdo.impl.sco.ArrayList[0];
    static final java.util.Vector utilVectorArray[] = new java.util.Vector[0];
    static final org.apache.jdo.impl.sco.Vector scoVectorArray[] = new org.apache.jdo.impl.sco.Vector[0];
    static final java.util.HashSet utilHashSetArray[] = new java.util.HashSet[0];
    static final org.apache.jdo.impl.sco.HashSet scoHashSetArray[] = new org.apache.jdo.impl.sco.HashSet[0];
    static final java.util.TreeSet utilTreeSetArray[] = new java.util.TreeSet[0];
    static final org.apache.jdo.impl.sco.TreeSet scoTreeSetArray[] = new org.apache.jdo.impl.sco.TreeSet[0];
    static final java.util.LinkedList utilLinkedListArray[] = new java.util.LinkedList[0];
    static final org.apache.jdo.impl.sco.LinkedList scoLinkedListArray[] = new org.apache.jdo.impl.sco.LinkedList[0];
    static final java.util.HashMap utilHashMapArray[] = new java.util.HashMap[0];
    static final org.apache.jdo.impl.sco.HashMap scoHashMapArray[] = new org.apache.jdo.impl.sco.HashMap[0];
    static final java.util.Hashtable utilHashtableArray[] = new java.util.Hashtable[0];
    static final org.apache.jdo.impl.sco.Hashtable scoHashtableArray[] = new org.apache.jdo.impl.sco.Hashtable[0];
    static final java.util.TreeMap utilTreeMapArray[] = new java.util.TreeMap[0];
    static final org.apache.jdo.impl.sco.TreeMap scoTreeMapArray[] = new org.apache.jdo.impl.sco.TreeMap[0];

    // Table mapping instances of java.lang.Class to CLID.  Represents CLIDs
    // that are compile-time known.
    private static HashMap knownCLIDs = new HashMap();

    // Table mapping CLIDs to instancs of java.lang.Class for those CLIDs that
    // represent classes for which we have both a java.* class and an SCO
    // class (such as Date).
    private static HashMap scoCLIDs = new HashMap();

    // For numbering knownCLID's
    static int nextCLID = 1;

    // The CLID's for some classes are considered to be well-known, and
    // are given below.  We provide a special method for writing out the CLID
    // of a known class.  The "null" CLID gets special treatment.
    //
    static final CLID nullCLID = new CLID(0, false);

    // CLIDs for special, well-known kinds of objects.
    static final CLID forOID = new CLID(nextCLID++,  false);
    static final CLID forOIDArray = new CLID(nextCLID++,  false);

    static {
        // Used only for inserting into knownCLIDs, scoCLIDs tables.
        CLID tmpCLID;

        knownCLIDs.put(OID.class,       forOID);

        // Primitives
        knownCLIDs.put(boolean.class,   new CLID(nextCLID++,  false));
        knownCLIDs.put(char.class,      new CLID(nextCLID++,  false));
        knownCLIDs.put(byte.class,      new CLID(nextCLID++,  false));
        knownCLIDs.put(short.class,     new CLID(nextCLID++,  false));
        knownCLIDs.put(int.class,       new CLID(nextCLID++,  false));
        knownCLIDs.put(long.class,      new CLID(nextCLID++,  false));
        knownCLIDs.put(float.class,     new CLID(nextCLID++,  false));
        knownCLIDs.put(double.class,    new CLID(nextCLID++,  false));

        // Immutables
        knownCLIDs.put(Boolean.class,   new CLID(nextCLID++, false));
        knownCLIDs.put(Character.class, new CLID(nextCLID++, false));
        knownCLIDs.put(Byte.class,      new CLID(nextCLID++, false));
        knownCLIDs.put(Short.class,     new CLID(nextCLID++, false));
        knownCLIDs.put(Integer.class,   new CLID(nextCLID++, false));
        knownCLIDs.put(Long.class,      new CLID(nextCLID++, false));
        knownCLIDs.put(Float.class,     new CLID(nextCLID++, false));
        knownCLIDs.put(Double.class,    new CLID(nextCLID++, false));

        // String
        knownCLIDs.put(String.class,    new CLID(nextCLID++, false));

        // Collections for which we don't have an SCO equivalent

        // Maps for which we don't have an SCO equivalent

        // Numbers
        knownCLIDs.put(BigDecimal.class, new CLID(nextCLID++, false));
        knownCLIDs.put(BigInteger.class, new CLID(nextCLID++, false));

        // BitSet
        knownCLIDs.put(BitSet.class,     new CLID(nextCLID++, false));

        // Locale
        knownCLIDs.put(Locale.class,     new CLID(nextCLID++, false));

        // Note: util and SCO CLID's
        // We use the same CLID for the util and SCO classes.  Consider the
        // case in which application code creates a field of a util class for
        // which JDORI has a corresponding SCO class (e.g. Date).  When the
        // field value is fetched, FOStore instantiates an instance of the
        // SCO class regardless.
        //
        // So, we use the same CLID for each util/SCO pair.  And we make an
        // entry in scoCLIDs from the CLID to the SCO class.

        // Simple SCO's and their non-SCO equivalents.
        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.Date.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.Date.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.Date.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.sql.Date.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.SqlDate.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.SqlDate.class);

        // Collection SCO's and their non-SCO equivalents.
        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.ArrayList.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.ArrayList.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.ArrayList.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.Vector.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.Vector.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.Vector.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.HashSet.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.HashSet.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.HashSet.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.TreeSet.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.TreeSet.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.TreeSet.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.LinkedList.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.LinkedList.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.LinkedList.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.HashMap.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.HashMap.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.HashMap.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.Hashtable.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.Hashtable.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.Hashtable.class);

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(java.util.TreeMap.class, tmpCLID);
        knownCLIDs.put(org.apache.jdo.impl.sco.TreeMap.class, tmpCLID);
        scoCLIDs.put(tmpCLID, org.apache.jdo.impl.sco.TreeMap.class);

        // Arrays for all of the above
        knownCLIDs.put(oidArray.getClass(),     forOIDArray);
        knownCLIDs.put(booleanArray.getClass(), new CLID(nextCLID++, false));
        knownCLIDs.put(charArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(byteArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(shortArray.getClass(),   new CLID(nextCLID++, false));
        knownCLIDs.put(intArray.getClass(),     new CLID(nextCLID++, false));
        knownCLIDs.put(longArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(floatArray.getClass(),   new CLID(nextCLID++, false));
        knownCLIDs.put(doubleArray.getClass(),  new CLID(nextCLID++, false));
        
        knownCLIDs.put(BooleanArray.getClass(), new CLID(nextCLID++, false));
        knownCLIDs.put(CharacterArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(ByteArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(ShortArray.getClass(),   new CLID(nextCLID++, false));
        knownCLIDs.put(IntegerArray.getClass(),     new CLID(nextCLID++, false));
        knownCLIDs.put(LongArray.getClass(),    new CLID(nextCLID++, false));
        knownCLIDs.put(FloatArray.getClass(),   new CLID(nextCLID++, false));
        knownCLIDs.put(DoubleArray.getClass(),  new CLID(nextCLID++, false));
        
        knownCLIDs.put(StringArray.getClass(),  new CLID(nextCLID++, false));

        // Numbers
        knownCLIDs.put(BigDecimalArray.getClass(), new CLID(nextCLID++, false));
        knownCLIDs.put(BigIntegerArray.getClass(), new CLID(nextCLID++, false));

        // BitSet
        knownCLIDs.put(BitSetArray.getClass(),     new CLID(nextCLID++, false));

        // Locale
        knownCLIDs.put(LocaleArray.getClass(),     new CLID(nextCLID++, false));

        // Arrays of simple SCO's and their non-SCO equivalents.
        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilDateArray.getClass(), tmpCLID);
        knownCLIDs.put(scoDateArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoDateArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(sqlDateArray.getClass(), tmpCLID);
        knownCLIDs.put(scoSqlDateArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoSqlDateArray.getClass());

        // Arrays of collection SCO's and their non-SCO equivalents.
        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilArrayListArray.getClass(), tmpCLID);
        knownCLIDs.put(scoArrayListArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoArrayListArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilVectorArray.getClass(), tmpCLID);
        knownCLIDs.put(scoVectorArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoVectorArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilHashSetArray.getClass(), tmpCLID);
        knownCLIDs.put(scoHashSetArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoHashSetArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilTreeSetArray.getClass(), tmpCLID);
        knownCLIDs.put(scoTreeSetArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoTreeSetArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilLinkedListArray.getClass(), tmpCLID);
        knownCLIDs.put(scoLinkedListArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoLinkedListArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilHashMapArray.getClass(), tmpCLID);
        knownCLIDs.put(scoHashMapArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoHashMapArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilHashtableArray.getClass(), tmpCLID);
        knownCLIDs.put(scoHashtableArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoHashtableArray.getClass());

        tmpCLID = new CLID(nextCLID++, false);
        knownCLIDs.put(utilTreeMapArray.getClass(), tmpCLID);
        knownCLIDs.put(scoTreeMapArray.getClass(), tmpCLID);
        scoCLIDs.put(tmpCLID, scoTreeMapArray.getClass());
    }


    /**
     * Indicate whether a Class is known, that is, has built-in support
     * @return true if known, false otherwise
     */
    static boolean isKnown(Class cls) {
        return getKnownCLID(cls) != null;
    }

    /**
    * Get the CLID that corresponds to the given Class.  Return null if there
    * is no such CLID.
    */
    static CLID getKnownCLID(Class cls) {
        CLID rc = (CLID)knownCLIDs.get(cls);
        if (null == rc) {
            if (cls.isArray()) {
                Class componentType = cls.getComponentType();
                if (PersistenceCapable.class.isAssignableFrom(componentType)) {
                    rc = CLID.forOIDArray;
                }
            }
        }
        return rc;
    }

    /**
    * Writes the CLID for the given class, which *must* be a known class
    */ 
    static void writeForKnown(Class cls, DataOutput out)
        throws IOException {

        CLID clid = (CLID)knownCLIDs.get(cls);
        if (null != clid) {
            clid.write(out);
        } else {
            throw new FOStoreFatalInternalException(
                CLID.class, "writeForKnown", // NOI18N
                msg.msg("ERR_Unknown", cls.getName())); // NOI18N
        }
    }

    /**
    * Get the java.lang.Class that corresponds to the given CLID.
    */
    static Class getKnownType(CLID clid) {
        Class rc = null;
        
        // If the CLID corresponds to a java.* class for which we have an SCO
        // equivalent, return the SCO Class.  This test is separate because
        // the knownCLIDs HashMap contains, for those cases, both the java.*
        // class and the SCO class mapped to the *same* CLID.
        rc = (Class)scoCLIDs.get(clid);
        
        if (null == rc) {
            Set entries = knownCLIDs.entrySet();
            for (Iterator i = entries.iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry)i.next();
                CLID ci = (CLID)entry.getValue();
                if (clid.equals(ci)) {
                    rc = (Class)entry.getKey();
                    break;
                }
            }
        }
        return rc;
    }

    /**
     * Indicates whether the given CLID is provisional or not.  A 'provisonal'
     * CLID is one that has been allocated within a JVM, but for which the
     * corresponding class is not yet known to have a correspondence in the
     * store itself.
     * @return True if this CLID is provisonal, false otherwise.
     */
    boolean isProvisional() {
        return provisional;
    }

    // hashCode and the public equals are required for HashMaps, such as the
    // provisional CLID map.
    public int hashCode() {
        return hashCode;
    }
    
    public boolean equals(Object other) {
        boolean rc = false;
        if (other instanceof CLID) {
            CLID clid = (CLID)other;
            rc = id == clid.id && provisional == clid.provisional;
        }
        return rc;
    }

    //
    // Serialization.  Not java.io.Serialization serialization, just what we
    // need for the store.
    //
    
    void write(DataOutput out) throws IOException {
        int writtenId = id;
        if (provisional) {
            writtenId |= PROVISIONAL_MASK;
        }
        out.writeInt(writtenId);
    }

    static CLID read(DataInput in) throws IOException {
        int id = in.readInt();
        boolean provisional = (id & PROVISIONAL_MASK) > 0;
        if (provisional) {
            id &= ~PROVISIONAL_MASK;
        }

        return new CLID(id, provisional);
    }

    /**
    * Provides a new CLID whose id is one greater than this one's.
    * @return A new CLID.  */
    CLID next() {
        return new CLID(id + 1, false);
    }

    /**
    * Provides the id part of a CLID.
    * @return The CLID's representation.
    */
    int getId() {
        return id;
    }

    // For debugging
    public String toString() {
        StringBuffer rc = new StringBuffer("CLID: " + id); // NOI18N
        if (provisional) {
            rc.append(" (provisional)"); // NOI18N
        }
        return rc.toString();
    }
}
