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
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Calendar;
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
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.jdo.JDOHelper;
import javax.jdo.JDOUserException;
import javax.jdo.spi.PersistenceCapable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.sco.Freezer;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.sco.SCOMap;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.Transcriber;
import org.apache.jdo.util.I18NHelper;

//
// Transcribers for non-primitives.  ObjectTranscriber is, like the primitive
// transcribers, a singleton.  Lexically within it is an AbstractTranscriber,
// which is the base of other transcribers for e.g. Bytes and arrays.
//

/**
* Transcribes all non-primitive, non-String values.  This includes immutable
* types (e.g. Boolean).  Values which are object references are represented
* by their OID's, except for values which are Collections.  These are
* represented out 'inline', that is, information about the kind of collection
* is first transcribed (e.g., hashtable vs. vector, etc.) and then the values
* in the collection themselves are transcribed.
*
* @author Dave Bristor
* @version 1.0.1
*/
class ObjectTranscriber extends FOStoreTranscriber {
    // PM on whose behalf we are storing an object, and potentially an object
    // graph.  Used to get an OID for a PersistenceCapable.  Set by
    // storeObject.
    private PersistenceManagerInternal pm;

    // Offsets of provisonal OID's stored.  The ArrayList is created and
    // destroyed by one of the storeObject methods.
    protected ArrayList offsets;
    
    // A different transcriber for each immutable object type
    private static HashMap transcribers = new HashMap();

    // Support for transcribing arrays of PersistenceCapables.
    private static /*final*/ ObjArrayTranscriber objArrayTranscriber;

    // Support for transcribing SCO's and their non-SCO equivalents
    private static /*final*/ DateTranscriber dateTranscriber;
    private static /*final*/ ArrayListTranscriber arrayListTranscriber;
    private static /*final*/ VectorTranscriber vectorTranscriber;
    private static /*final*/ HashSetTranscriber hashSetTranscriber;
    private static /*final*/ TreeSetTranscriber treeSetTranscriber;
    private static /*final*/ LinkedListTranscriber linkedListTranscriber;
    private static /*final*/ HashMapTranscriber hashMapTranscriber;
    private static /*final*/ HashtableTranscriber hashtableTranscriber;
    private static /*final*/ TreeMapTranscriber treeMapTranscriber;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /** Default value for allow nulls in SCO collections and maps. */
    private static final boolean DEFAULT_ALLOW_NULLS = true;

    /** Default value for element, key, and value types in SCO collections
    * and maps. */
    private static final String DEFAULT_TYPE = "java.lang.Object"; // NOI18N

    // We would like to do this static-ally, but member classes cannot be
    // created in a static block in the class of which they are members.
    ObjectTranscriber() {
        objArrayTranscriber = new ObjArrayTranscriber();
        dateTranscriber = new DateTranscriber();
        arrayListTranscriber = new ArrayListTranscriber();
        vectorTranscriber = new VectorTranscriber();
        hashSetTranscriber = new HashSetTranscriber();
        treeSetTranscriber = new TreeSetTranscriber();
        linkedListTranscriber = new LinkedListTranscriber();
        hashMapTranscriber = new HashMapTranscriber();
        hashtableTranscriber = new HashtableTranscriber();
        treeMapTranscriber = new TreeMapTranscriber();

        // Keep this list in sync with the list in in CLID.java.  (No code
        // should assume that, but it makes keeping both lists up-to-date just
        // that much easier.)

        // Immutables
        transcribers.put(Boolean.class,    new ImmutableBooleanTranscriber());
        transcribers.put(Character.class,  new ImmutableCharacterTranscriber());
        transcribers.put(Byte.class,       new ImmutableByteTranscriber());
        transcribers.put(Short.class,      new ImmutableShortTranscriber());
        transcribers.put(Integer.class,    new ImmutableIntegerTranscriber());
        transcribers.put(Long.class,       new ImmutableLongTranscriber());
        transcribers.put(Float.class,      new ImmutableFloatTranscriber());
        transcribers.put(Double.class,     new ImmutableDoubleTranscriber());

        // String
        transcribers.put(String.class,     new ImmutableStringTranscriber());

        // Collections for which we don't have an SCO equivalent

        // Maps for which we don't have an SCO equivalent
        //transcribers.put(TreeMap.class,    new TreeMapTranscriber());

        // Numbers
        transcribers.put(BigDecimal.class, new BigDecimalTranscriber());
        transcribers.put(BigInteger.class, new BigIntegerTranscriber());

        // BitSet
        transcribers.put(BitSet.class,     new BitSetTranscriber());

        // Locale
        transcribers.put(Locale.class,     new LocaleTranscriber());

        // Arrays of primitives & String
        transcribers.put(CLID.booleanArray.getClass(),
                         new BooleanArrayTranscriber());
        transcribers.put(CLID.charArray.getClass(),
                         new CharArrayTranscriber());
        transcribers.put(CLID.byteArray.getClass(),
                         new ByteArrayTranscriber());
        transcribers.put(CLID.shortArray.getClass(),
                         new ShortArrayTranscriber());
        transcribers.put(CLID.intArray.getClass(),
                         new IntArrayTranscriber());
        transcribers.put(CLID.longArray.getClass(),
                         new LongArrayTranscriber());
        transcribers.put(CLID.floatArray.getClass(),
                         new FloatArrayTranscriber());
        transcribers.put(CLID.doubleArray.getClass(),
                         new DoubleArrayTranscriber());
        transcribers.put(CLID.StringArray.getClass(),
                         new StringArrayTranscriber());

        // Simple SCO's and their non-SCO equivalents
        transcribers.put(org.apache.jdo.impl.sco.Date.class, dateTranscriber);
        transcribers.put(java.util.Date.class, dateTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.SqlDate.class, dateTranscriber);
        transcribers.put(java.sql.Date.class, dateTranscriber);
        // Calendars use date as their representation,
        transcribers.put(java.util.Calendar.class, dateTranscriber);

        // Collection SCO's and their non-SCO equivalents
        transcribers.put(java.util.ArrayList.class, arrayListTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.ArrayList.class,
                         arrayListTranscriber);
        transcribers.put(java.util.Vector.class, vectorTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.Vector.class,
                         vectorTranscriber);
        transcribers.put(java.util.HashSet.class, hashSetTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.HashSet.class,
                         hashSetTranscriber);
        transcribers.put(java.util.TreeSet.class, treeSetTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.TreeSet.class,
                         treeSetTranscriber);
        transcribers.put(java.util.LinkedList.class, linkedListTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.LinkedList.class,
                         linkedListTranscriber);
        transcribers.put(java.util.HashMap.class, hashMapTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.HashMap.class,
                         hashMapTranscriber);
        transcribers.put(java.util.Hashtable.class, hashtableTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.Hashtable.class,
                         hashtableTranscriber);
        transcribers.put(java.util.TreeMap.class, treeMapTranscriber);
        transcribers.put(org.apache.jdo.impl.sco.TreeMap.class,
                         treeMapTranscriber);

        initSupported();
    }
    

    // We can't treat this in the "normal" fashion; when we try we get a
    // NullPointerException in creating the Immutable* transcribers.
    static ObjectTranscriber getInstance() {
        return FOStoreTranscriber.objectTranscriber;
    }
    
    /**
     * Stores an object on the given FOStoreOutput by delegating to the other
     * storeObject method after stashing away pm.
     */
    // XXX PERF syncronized to protect from MT access to the "offsets" field,
    // but we do *not* want synchronization here.
    synchronized int[] storeObject(Object value, FOStoreOutput out,
                      PersistenceManagerInternal pm) throws IOException {
        // pm could be null if, for example, value is-a String.
        this.pm = pm;

        offsets = new ArrayList();

        int rc[] = storeObject(value, out);

        offsets = null; // Allow GC to reclaim.

        return rc;
    }

    /**
    * Stores an object on the given FOStoreOutput.  An object is always
    * stored by first storing it's CLID.  If the value is null, instead of
    * the OID, we write CLID.nullOID, followed by an integer 0...this is OK,
    * because metadata will tell us type of thing that is null.  If it's not
    * null...well, it writes the object's CLID and value.
    * @return null if there were no provisional OID's stored while storing
    * this object, otherwise an array of offsets into the given output
    * stream.  The array will have at least one element.
    */
    // This MUST be kept in sync with fetchObject!!
    synchronized protected int[] storeObject(Object value, FOStoreOutput out)
        throws IOException {

        if (null == value) {
            // Object's value is null
            CLID.nullCLID.write(out);
            out.writeInt(0);
        } else {
            // Value is not null, class could be 'known', as per above table
            Class cls = value.getClass();
            AbstractTranscriber t = (AbstractTranscriber)transcribers.get(cls);
            if (null != t) {
                // If we've got an AbstractTranscriber it *must* be a known
                // type.
                CLID.writeForKnown(cls, out);
                t.store(value, out);
                
            } else if (value instanceof PersistenceCapable) {
                // The value is-a PersistenceCapable.  Write it's OID.
                CLID.forOID.write(out);

                OID oid = (OID)pm.getInternalObjectId(value);

                if (oid.isProvisional()) {
                    offsets.add(new Integer(out.getPos()));
                    if (logger.isDebugEnabled()) {
                        logger.debug("FOT.sO: " + oid + " pos=" + // NOI18N
                                       out.getPos());
                    }
                }
                oid.write(out);
                
            } else if (cls.isArray()) {
                // It could be an "unknown" array, that is, one not directly
                // supported by the above table of transcribers.
                //
                // For now, at least, we only support arrays of the above, and
                // of PersistenceCapable objects, by storing OIDs.
                // XXX TBD Model: Add ability to embed PC objects.
                Class componentType = cls.getComponentType();
                if (isSupportedArrayType(componentType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("FOT.sO: objArray " + cls.getName()); // NOI18N
                    }
                    CLID.forOIDArray.write(out);
                    out.writeUTF(componentType.getName());
                    objArrayTranscriber.store(value, out);
                } else {
                    throw new FOStoreUnsupportedException(
                        msg.msg("EXC_UnsupportedArrayType", // NOI18N
                             componentType.getName()));
                }
                
            } else {
                // What IS this thing???
                throw new FOStoreFatalInternalException(
                    this.getClass(), "storeObject", // NOI18N
                    msg.msg("ERR_NoTranscriberForClass", cls)); // NOI18N
            }
        }
        int size = 0;
        if (null != offsets) {
            size = offsets.size();
        }
        int rc[] = null;
        if (size > 0) {
            rc = new int[size];
            for (int i = 0; i < size; i++) {
                rc[i] = ((Integer)offsets.get(i)).intValue();
            }
        }
        return rc;
    }            

    //
    // Putting this declaration and static block here is probably heresy by
    // someone's Java coding standards.  But!  This is *only* used by
    // isSupportedArrayType, and Java does not support function-local statics
    // (which good ol' C does).  If (when?) Java does support that capability,
    // move these into that function.
    //
    private static final HashSet supported = new HashSet();
    private static boolean initSupported = false;

    // This would be a static block, except that Java does not support static
    // blocks inside inner classes.  It is called from ObjectTranscriber().
    private void initSupported() {
        if (! initSupported) {
            initSupported = true;
            
            supported.add(Boolean.class);
            supported.add(Character.class);
            supported.add(Byte.class);
            supported.add(Short.class);
            supported.add(Integer.class);
            supported.add(Long.class);
            supported.add(Float.class);
            supported.add(Double.class);

            // String
            supported.add(String.class);

            supported.add(java.math.BigDecimal.class);
            supported.add(java.math.BigInteger.class);
            supported.add(java.util.BitSet.class);
            supported.add(java.util.Date.class);
            supported.add(java.util.Locale.class);
            supported.add(org.apache.jdo.impl.sco.Date.class);

            supported.add(ArrayList.class);
            supported.add(HashMap.class);
            supported.add(Hashtable.class);
            supported.add(HashSet.class);
            supported.add(LinkedList.class);
            supported.add(TreeMap.class);
            supported.add(TreeSet.class);
            supported.add(Vector.class);
        }
    }
    
    // Returns true if cls is one we can support as an element of an array
    //
    private boolean isSupportedArrayType(Class cls) {
        boolean rc =
            PersistenceCapable.class.isAssignableFrom(cls);
        if ( !rc) {
            rc = supported.contains(cls);
        }
        return rc;
    }
    
    /**
    * Retrieves an object from the given DataInput by delegating to the other
    * fetchObject method after stashing away pm
    */
    Object fetchObject(DataInput in, Object owner, int fieldNum,
                       PersistenceManagerInternal pm)
        throws IOException, Exception {

        this.pm = pm;
        
        return fetchObject(in, owner, fieldNum);
    }

    /**
    * Retrieves an object from the given DataInput.  If it is an SCO, then the
    * SCO's owner is set to the given owner.
    */
    // This MUST be kept in sync with storeObject!
    protected Object fetchObject(DataInput in, Object owner, int fieldNum)
        throws IOException, Exception {

        Object rc = null;
        
        CLID clid = CLID.read(in);
        if (logger.isDebugEnabled()) {
            logger.debug("OT.fetchObject: " + clid); // NOI18N
        }
        
        if (CLID.nullCLID.equals(clid)) {
            // Object's value is null
            in.readInt();

        } else if (CLID.forOID.equals(clid)) {
            // Object is-a PersistenceCapable.  Return a hollow PC.
            OID oid = OID.read(in);
            FOStorePMF pmf = (FOStorePMF)pm.getPersistenceManagerFactory();
            rc = pm.getStateManager(oid, oid.getPCClass(pmf)).getObject();
            if (logger.isDebugEnabled()) {
                logger.debug("OT.fetchObject: " + oid); // NOI18N
            }

        } else if (CLID.forOIDArray.equals(clid)) {
            String clsName = in.readUTF();
            Object obj = ((StateManagerInternal)owner).getObject();
            objArrayTranscriber.setComponentType(
                loadClass(clsName, obj));
            rc = objArrayTranscriber.fetch(in, owner, fieldNum);

        } else {
            // Object could be of a 'known' type.  Note that this doesn't use
            // the above table!
            Class cls = CLID.getKnownType(clid);
            if (logger.isDebugEnabled()) {
                logger.debug("OT.fetchObject known: " + cls.getName()); // NOI18N
            }

            if (null != cls) {
                AbstractTranscriber t =
                    (AbstractTranscriber)transcribers.get(cls);
                
                if (null != t) {
                    rc = t.fetch(in, owner, fieldNum);
                } else {
                    throw new FOStoreUnsupportedException(
                        msg.msg("EXC_ClassNotTranscribable", cls)); // NOI18N
                }
            }
        }
        return rc;
    }

    /**
    * Skips an object's bytes from the given DataInput.  
    */
    protected void skip(DataInput in)
        throws IOException {

        skipObject(in);
    }

    /**
    * Skips an object's bytes from the given DataInput.  
    */
    // This MUST be kept in sync with storeObject!
    protected void skipObject(DataInput in)
        throws IOException {

        CLID clid = CLID.read(in);
        if (logger.isDebugEnabled()) {
            logger.debug("OT.skip: " + clid); // NOI18N
        }
        
        if (CLID.nullCLID.equals(clid)) {
            // Object's value is null
            in.readInt();

        } else if (CLID.forOID.equals(clid)) {
            // Object is-a PersistenceCapable.  Read OID bytes only.
            OID.skip(in);

        } else if (CLID.forOIDArray.equals(clid)) {
            in.readUTF(); // clsName
            objArrayTranscriber.skip(in);

        } else {
            // Object could be of a 'known' type.  Note that this doesn't use
            // the above table!
            Class cls = CLID.getKnownType(clid);
            if (logger.isDebugEnabled()) {
                logger.debug("OT.skipObject known: " + cls.getName()); // NOI18N
            }

            if (null != cls) {
                AbstractTranscriber t =
                    (AbstractTranscriber)transcribers.get(cls);
                
                if (null != t) {
                    t.skip(in);
                } else {
                    throw new FOStoreUnsupportedException(
                        msg.msg("EXC_ClassNotTranscribable", cls)); // NOI18N
                }
            }
        }
    }
    /**
    * An AbstractTranscriber knows how to transcribe one (and only one) kind
    * of object.  This is abstract class instead of interface to avoid
    * 'publicity' of methods.
    */
    abstract class AbstractTranscriber implements Transcriber {
        abstract void store(Object value, FOStoreOutput out)
            throws IOException;

        abstract Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception;

        abstract void skip(DataInput in)
            throws IOException;
    }

    //
    // Following are Transcribers for immutables.
    //
    
    abstract class ImmutableTranscriber extends AbstractTranscriber {
        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception{

                return fetch(in);
        }

        // Immutables *never* need owner, nor fieldNum
        abstract Object fetch(DataInput in) throws Exception;

        abstract void skip(DataInput in) throws IOException;
    }

    class ImmutableBooleanTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeBoolean(((Boolean)value).booleanValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Boolean(in.readBoolean());
        }

        void skip(DataInput in) throws IOException {
            in.readBoolean();
        }
    }

    class ImmutableCharacterTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeChar(((Character)value).charValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Character(in.readChar());
        }

        void skip(DataInput in) throws IOException {
            in.readChar();
        }
    }

    class ImmutableByteTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeByte(((Byte)value).byteValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Byte(in.readByte());
        }

        void skip(DataInput in) throws IOException {
            in.readByte();
        }
    }

    class ImmutableShortTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeShort(((Short)value).shortValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Short(in.readShort());
        }

        void skip(DataInput in) throws IOException {
            in.readShort();
        }
    }

    class ImmutableIntegerTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeInt(((Integer)value).intValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Integer(in.readInt());
        }

        void skip(DataInput in) throws IOException {
            in.readInt();
        }
    }

    class ImmutableLongTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeLong(((Long)value).longValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Long(in.readLong());
        }

        void skip(DataInput in) throws IOException {
            in.readLong();
        }
    }

    class ImmutableFloatTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeFloat(((Float)value).floatValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Float(in.readFloat());
        }

        void skip(DataInput in) throws IOException {
            in.readFloat();
        }
    }

    class ImmutableDoubleTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeDouble(((Double)value).doubleValue());
        }

        Object fetch(DataInput in) throws IOException {
            return new Double(in.readDouble());
        }

        void skip(DataInput in) throws IOException {
            in.readDouble();
        }
    }

    class ImmutableStringTranscriber extends ImmutableTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            out.writeUTF((String)value);
        }

        public Object fetch(DataInput in) throws IOException {
            return in.readUTF();
        }

        void skip(DataInput in) throws IOException {
            in.readUTF();
        }
    }

    //
    // Following are Transcribers for arrays
    //
    // Each is similar: write/read the size of the array, followed by the
    // elements.
    //

    /**
     * Abstract class which provides support for transcribing arrays of any
     * type. For each type of array, there should be a subclass.  
     * @see ObjectTranscriber.IntArrayTranscriber
     */
    // Note that lack of symmetry between store/storeElement and
    // fetch/fetchElements.  This is intentional.
    //
    // We can have the array indexing loop in store, and have storeElement
    // just write out the data value immediately, whereby subclasses don't
    // have to each implement the loop.  We cannot, however, do the same with
    // the fetch methods, because the subclass itself has to create the array
    // of the appropriate type, and then read the values using type-specific
    // methods.
    //
    // So what you can say about the abstract methods, is that they do the
    // type-specific things.
    //
    // Note too that this uses java.lang.reflect.Array.  It may be that we
    // have a big loss of performance here, and that we'd be better off by
    // having each array transcriber type do all the work itself, and pay the
    // costs of code size and maintenance.
    //
    abstract class ArrayTranscriber extends AbstractTranscriber {
        private Object owner;
        private int fieldNum;

        protected Object getOwner() {
            return owner;
        }

        protected int getFieldNum() {
            return fieldNum;
        }
        
        /**
         * Stores the entire array by delegating the storing of each element
         * to the storeElement method, which is implemented by a subclass.
         */
        final void store(Object value, FOStoreOutput out) throws IOException {
            try {
                int length = Array.getLength(value);
                if (logger.isDebugEnabled()) {
                    logger.debug(this.getClass().getName()
                                   + ".store: length=" + length // NOI18N
                                   + " starting at=" + out.getPos()); // NOI18N
                }
                out.writeInt(length);
                for (int i = 0; i < length; i++) {
                    storeElement(value, out, i);
                }
            } catch (IllegalArgumentException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "store", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "store", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            }
        }

        /**
         * Stores a single element of an array.
         * @param value An array of some type.  The type of the array is
         * conditional on the class which is implementing this method.
         * @param out Where the value should be stored
         * @param index Index into the value which should be stored.
         */
        abstract void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException;

        /**
         * Fetches the entire array by delegating to fetchElements.
         */
        final Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            this.owner = owner;
            this.fieldNum = fieldNum;
            
            Object rc = null;
            try {
                int length = in.readInt();
                if (logger.isDebugEnabled()) {
                    logger.debug(this.getClass().getName()
                                   + ".fetch: length=" + length); // NOI18N
                }
                rc = fetchElements(in, length);
            } catch (IllegalArgumentException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "fetch", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "fetch", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            }
            return rc;
        }

        /**
         * Skips the entire array by delegating to skipElements.
         */
        final void skip(DataInput in)
            throws IOException {

            try {
                int length = in.readInt();
                if (logger.isDebugEnabled()) {
                    logger.debug(this.getClass().getName()
                                   + ".skip: length=" + length); // NOI18N
                }
                skipElements(in, length);
            } catch (IllegalArgumentException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "skip", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "skip", // NOI18N
                    msg.msg("ERR_ShouldNotHappen", ex)); // NOI18N
            }
        }

        /**
         * Creates an array of some type and returns it, after reading in all
         * the array's element's values.  The type of the array that should be
         * created is conditional on the class which is implementing this
         * method. 
         * @param in Place from where the array's values should be read
         * @param length Length of the array to create.
         */
        abstract Object fetchElements(DataInput in, int length)
            throws Exception, IllegalArgumentException,
            ArrayIndexOutOfBoundsException;

        /**
         * Skips an array of some type after skipping all the array's element's values.
         * @param in Place from where the array's values should be read
         * @param length Length of the array.
         */
        abstract void skipElements(DataInput in, int length)
            throws IOException;
    }


    //
    // Following are Transcribers for arrays of individual types.
    //
    
    class BooleanArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeBoolean(Array.getBoolean(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws Exception, IllegalArgumentException, 
            ArrayIndexOutOfBoundsException {

            boolean rc[] = new boolean[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readBoolean();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readBoolean();
            }
        }
    }

    class CharArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeChar(Array.getChar(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            char rc[] = new char[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readChar();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readChar();
            }
        }
    }

    class ByteArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeByte(Array.getByte(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            byte rc[] = new byte[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readByte();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readByte();
            }
        }
    }

    class ShortArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeShort(Array.getShort(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            short rc[] = new short[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readShort();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readShort();
            }
        }
    }

    class IntArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeInt(Array.getInt(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            int rc[] = new int[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readInt();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readInt();
            }
        }
    }

    class LongArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeLong(Array.getLong(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            long rc[] = new long[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readLong();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readLong();
            }
        }
    }

    class FloatArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeFloat(Array.getFloat(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            float rc[] = new float[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readFloat();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readFloat();
            }
        }
    }

    class DoubleArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeDouble(Array.getDouble(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            double rc[] = new double[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readDouble();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readDouble();
            }
        }
    }

    class StringArrayTranscriber extends ArrayTranscriber {
        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            out.writeUTF((String)Array.get(value, index));
        }

        Object fetchElements(DataInput in, int length)
            throws IOException {

            String rc[] = new String[length];
            for (int i = 0; i < length; i++) {
                rc[i] = in.readUTF();
            }
            return rc;
        }

        void skipElements(DataInput in, int length) throws IOException {

            for (int i = 0; i < length; i++) {
                in.readUTF();
            }
        }
    }

    class ObjArrayTranscriber extends ArrayTranscriber {
        private Class componentType;

        void setComponentType(Class componentType) {
            this.componentType = componentType;
        }

        void storeElement(Object value, FOStoreOutput out, int index)
            throws IOException, IllegalArgumentException,
            ArrayIndexOutOfBoundsException {

            Object o = Array.get(value, index);
            storeObject(o, out);
        }

        Object fetchElements(DataInput in, int length)
            throws Exception {

            if (null == componentType) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "fetchElements", // NOI18N
                    msg.msg("ERR_NullComponentType"));  // NOI18N
            }
            if (logger.isDebugEnabled()) {
                logger.debug("PCAT.fetchElements: componentType=" // NOI18N
                               + componentType.getName());
            }
            
            Object rc = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                Array.set(rc, i, fetchObject(in, getOwner(), getFieldNum()));
            }
            return rc;
        }

        void skipElements(DataInput in, int length)
            throws IOException {

            if (null == componentType) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "skipElements", // NOI18N
                    msg.msg("ERR_NullComponentType"));  // NOI18N
            }
            if (logger.isDebugEnabled()) {
                logger.debug("PCAT.skipElements: componentType=" // NOI18N
                               + componentType.getName());
            }
            
            for (int i = 0; i < length; i++) {
                skipObject(in);
            }
        }
    }

    //
    // Following are Transcribers for Collections.
    //
    // Each is similar: write/read out the size of the collection, then its
    // elements.
    //


    /**
    * Transcribe Collections.
    */
    abstract class CollectionTranscriber extends AbstractTranscriber {
        /**
        * Writes information for Collections, particularly for SCO
        * Collections.
        * @param obj Collection (possibly SCO) for which information in
        * written. 
        * @param out Output to which information is written.
        * @throws IOException if there are problems writing information.
        */
        protected void writeInfo(Collection obj, FOStoreOutput out)
            throws IOException {
            
            if ((obj instanceof SCOCollection)) {
                SCOCollection sco = (SCOCollection)obj;
                out.writeUTF(sco.getElementType().getName());
                out.writeBoolean(sco.allowNulls());
            } else {
                out.writeUTF(DEFAULT_TYPE);
                out.writeBoolean(DEFAULT_ALLOW_NULLS);
            }
        }
        
        /** Store the elements of the collection. Freeze the elements,
         * then iterate over them.
         */
        protected void storeCollection(Collection obj, FOStoreOutput out)
                throws IOException {
            FOStoreTranscriberFactory factory =
                FOStoreTranscriberFactory.getInstance();
            FOStoreTranscriber t;
            Iterator iterator = null;
            if (obj instanceof SCOCollection) {
                iterator = ((SCOCollection)obj).frozenIterator();
            } else {
                iterator = obj.iterator();
            }
            while (iterator.hasNext()) {
                Object elem = iterator.next();
                t = (FOStoreTranscriber)factory.getTranscriber(elem.getClass())[0];
                t.storeObject(elem, out);
            }
        }

       /**
        * Skips elements of a Collection.
        * @param in Place from where the array's values should be read
        * @throws IOException if there are problems writing information.
        */
        void skip(DataInput in)
            throws IOException {

            int size = in.readInt();
            in.readUTF(); // elementType
            in.readBoolean(); // allowNulls

            for (int i = 0; i < size; i++) {
                skip(in);
            }
        }
    }
    
    /**
    * Transcribe ArrayLists.
    */
    class ArrayListTranscriber extends CollectionTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            ArrayList obj = (ArrayList)value;
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            storeCollection(obj, out);
        }
        
        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class elementType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            org.apache.jdo.impl.sco.ArrayList rc = 
                new org.apache.jdo.impl.sco.ArrayList(
                    elementType, allowNulls, size);
            for (int i = 0; i < size; i++) {
                rc.addInternal(fetchObject(in, owner, fieldNum));
            }
            rc.setOwner(owner, fieldNum);
            return rc;
        }
    }

    // Sigh: ArrayList and Vector are sooooo close, yet different, and these
    // methods are small, so that it's hardly worth it to abstract out the
    // differences.  You wind up with more lines of code than by keeping them
    // completely separate!

    /**
    * Transcribe Vectors
    */
    class VectorTranscriber extends CollectionTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            Vector obj = (Vector)value;
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            FOStoreTranscriberFactory factory =
                FOStoreTranscriberFactory.getInstance();
            for (int i = 0; i < size; i++) {
                Object o = obj.elementAt(i);
                FOStoreTranscriber t =
                    (FOStoreTranscriber)factory.getTranscriber(o.getClass())[0];
                t.storeObject(o, out);
            }
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class elementType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            org.apache.jdo.impl.sco.Vector rc = 
                new org.apache.jdo.impl.sco.Vector(
                    elementType, allowNulls);
            for (int i = 0; i < size; i++) {
                rc.addInternal(fetchObject(in, owner, fieldNum));
            }
            rc.setOwner(owner, fieldNum);
            return rc;
        }
    }

    /**
    * Transcribe LinkedList.
    */
    class LinkedListTranscriber extends CollectionTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            LinkedList obj = (LinkedList)value;
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            storeCollection(obj, out);
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class elementType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            org.apache.jdo.impl.sco.LinkedList rc =
                new org.apache.jdo.impl.sco.LinkedList(
                    elementType, allowNulls);

            for (int i = 0; i < size; i++) {
                rc.addInternal(fetchObject(in, owner, fieldNum));
            }
            rc.setOwner(owner, fieldNum);
            return rc;
        }
    }

    /**
    * Transcribe TreeSets.
    */
    class TreeSetTranscriber extends CollectionTranscriber {
       void store(Object value, FOStoreOutput out) throws IOException {

            TreeSet obj = (TreeSet)value;
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            Comparator c = obj.comparator();
            if (c != null) {
                out.writeBoolean(true);
                out.writeUTF(c.getClass().getName());
            } else {
                out.writeBoolean(false);
            }

            storeCollection(obj, out);
        }
 
        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class elementType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            boolean nonNullComparator = in.readBoolean();
            Comparator c = null;

            if (nonNullComparator) {
                String clsName = in.readUTF();
                Class cls = loadClass(clsName, obj);
                c = (Comparator) cls.newInstance();
            }

            org.apache.jdo.impl.sco.TreeSet rc = new 
                org.apache.jdo.impl.sco.TreeSet(
                    elementType, allowNulls, c);

            Object[] frozen = new Object[size];
            for (int i = 0; i < size; i++) {
                Object elem = fetchObject(in, owner, fieldNum);
                frozen[i] = elem;
            }
            rc.setFrozen(frozen);
            rc.setOwner(owner, fieldNum);
            return rc;
        }

        void skip(DataInput in)
            throws IOException {

            int size = in.readInt();
            in.readUTF(); // elementType
            in.readBoolean(); // allowNulls

            boolean nonNullComparator = in.readBoolean();
            if (nonNullComparator) {
                in.readUTF(); // clsName
            }

            for (int i = 0; i < size; i++) {
                skipObject(in);
            }
        }
    }


    /**
    * Transcribe HashSets.
    */
    // XXX TBD Model: Use elementType, allowNulls when available.
    class HashSetTranscriber extends CollectionTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            HashSet obj = (HashSet)value;
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            storeCollection(obj, out);
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class elementType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            org.apache.jdo.impl.sco.HashSet rc = 
                new org.apache.jdo.impl.sco.HashSet(
                    elementType, allowNulls);
            Object[] frozen = new Object[size];
            for (int i = 0; i < size; i++) {
                Object elem = fetchObject(in, owner, fieldNum);
                frozen[i] = elem;
            }
            rc.setFrozen(frozen);
            rc.setOwner(owner, fieldNum);
            return rc;
        }
    }

    /**
    * Transcribe Maps.
    */
    abstract class MapTranscriber extends AbstractTranscriber {
        void storeMap(Map obj, FOStoreOutput out) throws IOException {
            int size = obj.size();
            out.writeInt(size);
            writeInfo(obj, out);

            writeExtras(obj, out);
            
            FOStoreTranscriberFactory factory =
                FOStoreTranscriberFactory.getInstance();
            FOStoreTranscriber tk, tv;
            Iterator iterator;
            if (obj instanceof SCOMap) {
                iterator = ((SCOMap)obj).frozenIterator();
            } else {
                iterator = Freezer.frozenIterator(obj, obj.size());
            }
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry)iterator.next();
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();
                tk = (FOStoreTranscriber)factory.getTranscriber(key.getClass())[0];
                tv = (FOStoreTranscriber)factory.getTranscriber(value.getClass())[0];
                tk.storeObject(key, out);
                tv.storeObject(value, out);
            }                
        }

        void writeExtras(Map obj, FOStoreOutput out) 
            throws IOException {
                // the basic Map doesn't have any extras; see TreeMap
        }
        
        Object fetchMap(SCOMap rc, int size, DataInput in, Object owner,
                        int fieldNum) throws Exception { 

            Map.Entry[] frozen = new Map.Entry[size];
            Map absoluteMap = Freezer.createAbsoluteOrderMap();
            for (int i = 0; i < size; i++) {
                Object key = fetchObject(in, owner, fieldNum);
                Object val = fetchObject(in, owner, fieldNum);
                absoluteMap.put(key, val);
            }
            rc.setFrozen((Map.Entry[])absoluteMap.entrySet().toArray(frozen));
            rc.setOwner(owner, fieldNum);
            return rc;
        }

        protected void writeInfo(Map obj, FOStoreOutput out)
            throws IOException {

            if (obj instanceof SCOMap) {
                SCOMap map = (SCOMap) obj;
                out.writeUTF(map.getKeyType().getName());
                out.writeUTF(map.getValueType().getName());
                out.writeBoolean(map.allowNulls());
            } else {
                out.writeUTF(DEFAULT_TYPE);
                out.writeUTF(DEFAULT_TYPE);
                out.writeBoolean(DEFAULT_ALLOW_NULLS);
            }
        }

        void skip(DataInput in)
            throws IOException {

            int size = in.readInt();
            in.readUTF(); // keyType
            in.readUTF(); // valueType
            in.readBoolean(); // allowNulls

            for (int i = 0; i < size; i++) {
                skipObject(in); // key
                skipObject(in); // value
            }
        }
    }

    /**
    * Transcribe HashMaps.
    */
    class HashMapTranscriber extends MapTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {

            storeMap((Map)value, out);
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class keyType = loadClass(in.readUTF(), obj);
            Class valueType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            org.apache.jdo.impl.sco.HashMap rc = new 
                org.apache.jdo.impl.sco.HashMap(
                    keyType, valueType, allowNulls, size);
            return fetchMap(rc, size, in, owner, fieldNum);
        }
    }


    /**
    * Transcribe Hashtable, including sco.Hashtable
    */
    class HashtableTranscriber extends MapTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            storeMap((Map)value, out);
        }                                                 
 
        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {
 
            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class keyType = loadClass(in.readUTF(), obj);
            Class valueType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();
            org.apache.jdo.impl.sco.Hashtable rc = new
                org.apache.jdo.impl.sco.Hashtable(
                    keyType, valueType, allowNulls);
            return fetchMap(rc, size, in, owner, fieldNum);
        }
    }   

    /**
    * Transcribe TreeMap, including sco.TreeMap.
    */
    class TreeMapTranscriber extends MapTranscriber {
       void store(Object value, FOStoreOutput out) throws IOException {
            storeMap((Map)value, out);
        }
 
       void writeExtras(Map obj, FOStoreOutput out) 
            throws IOException {
            Comparator c = ((SortedMap)obj).comparator();
            if (c != null) {
                out.writeBoolean(true);
                out.writeUTF(c.getClass().getName());
            } else {
                out.writeBoolean(false);
            }
       }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Object obj = ((StateManagerInternal)owner).getObject();

            int size = in.readInt();
            Class keyType = loadClass(in.readUTF(), obj);
            Class valueType = loadClass(in.readUTF(), obj);
            boolean allowNulls = in.readBoolean();

            boolean nonNullComparator = in.readBoolean();
            Comparator c = null;

            if (nonNullComparator) {
                String clsName = in.readUTF();
                Class cls = loadClass(clsName, obj);
                c = (Comparator) cls.newInstance();
            }

            org.apache.jdo.impl.sco.TreeMap rc = new 
                org.apache.jdo.impl.sco.TreeMap(
                    keyType, valueType, allowNulls, c);

            return fetchMap (rc, size, in, owner, fieldNum);
        }

        void skip(DataInput in)
            throws IOException {

            int size = in.readInt();
            in.readUTF(); // keyType
            in.readUTF(); // valueType
            in.readBoolean(); // allowNulls

            boolean nonNullComparator = in.readBoolean();
            if (nonNullComparator) {
                in.readUTF(); // clsName
            }

            for (int i = 0; i < size; i++) {
                skipObject(in); // key
                skipObject(in); // value
            }
        }
    }

    /**
    * Transcribe Date.
    */
    class DateTranscriber extends AbstractTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            boolean sqlDate = false;
            if (value instanceof java.sql.Date ||
                value instanceof org.apache.jdo.impl.sco.SqlDate) {
                sqlDate = true;
            }
            out.writeBoolean(sqlDate);
                
            Date date = (Date)value;
            out.writeLong(date.getTime());
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            org.apache.jdo.sco.SCO rc = null;

            boolean sqlDate = in.readBoolean();

            if (sqlDate) {
                rc = new org.apache.jdo.impl.sco.SqlDate(in.readLong());
            } else {
                rc = new org.apache.jdo.impl.sco.Date(in.readLong());
            }
            rc.setOwner(owner, fieldNum);
            return rc;
        }

        void skip(DataInput in)
            throws IOException {

            in.readBoolean(); // sqlDate
            in.readLong(); // time
        }
    }
    /**
    * Transcribe Calendar.
    */
    class CalendarTranscriber extends AbstractTranscriber {

        void store(Object value, FOStoreOutput out) throws IOException {
            Calendar calendar = (Calendar)value;
            TimeZone tz = calendar.getTimeZone();
            String tzid = tz.getID();
            Date date = ((Calendar)value).getTime();

            out.writeUTF(calendar.getClass().getName());
            out.writeUTF(tzid);
            out.writeLong(date.getTime());
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            Calendar rc = null;

            String className = in.readUTF();
            Object obj = ((StateManagerInternal)owner).getObject();
            Class c = loadClass(className, obj);
            rc = (Calendar)c.newInstance();

            String tzid = in.readUTF();
            rc.setTimeZone(TimeZone.getTimeZone(tzid));

            org.apache.jdo.impl.sco.Date date =
                new org.apache.jdo.impl.sco.Date(in.readLong());
            date.setOwner(owner, fieldNum);
            rc.setTime(date);

            return rc;
        }

        void skip(DataInput in)
            throws IOException {

            in.readInt(); // type
            in.readLong(); // time
        }
    }
    
    /**
    * Transcribe BigDecimal.
    */
    // We use the string representation instead of some other alternatives
    // (e.g. serialization) because BigDecimal's javadoc says toString is
    // compatible with the String constructor, which I take to mean that no
    // information is lost.
    class BigDecimalTranscriber extends AbstractTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            BigDecimal val = (BigDecimal)value;
            out.writeUTF(val.toString());
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            return new BigDecimal(in.readUTF());
        }

        void skip(DataInput in)
            throws IOException {

            in.readUTF(); 
        }
    }
    
    /**
    * Transcribe BigInteger.
    */
    // Ditto the comments above for BigDecimal.
    class BigIntegerTranscriber extends AbstractTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            BigInteger val = (BigInteger)value;
            out.writeUTF(val.toString());
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            return new BigInteger(in.readUTF());
        }

        void skip(DataInput in)
            throws IOException {

            in.readUTF(); 
        }
    }

    /**
     * Transcribe BitSet.
     */
    class BitSetTranscriber extends AbstractTranscriber {
        final static int BYTE = 8;
        final static byte LEAST_SIGNIFICANT = 1;

        void store(Object value, FOStoreOutput out) throws IOException {
            BitSet bitSet = (BitSet)value;
            int length = bitSet.length() / BYTE;

            // if necessary, enlarge buffer
            if (bitSet.length() % BYTE  > 0) {
                length++;
            }
            byte[] buffer = new byte[length];

            // set bits in byte array according to BitSet object
            for (int i = 0; i < length; i++) {
                byte b = 0;

                for (int j = 0; j < BYTE; j++) {
                    int index = i * BYTE + j;
                    if (index < bitSet.length() && bitSet.get(index)) {
                        b |= LEAST_SIGNIFICANT << j;
                    }
                }
                buffer[i] = b;

            }

            out.writeInt(length);
            out.write(buffer);
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            int length = in.readInt();
            byte[] buffer = new byte[length];
            BitSet rc = new BitSet(length * BYTE);
            
            in.readFully(buffer);
            for (int i = 0; i < length; i++) {
                byte b = buffer[i];

                // set bits in BitSet according to byte array values
                for (int j = 0; j < BYTE; j++) {
                    int index = i * BYTE + j;
                    byte pos = (byte)(LEAST_SIGNIFICANT << j);

                    if ((b & pos) != 0) {
                        rc.set(index);
                    }
                }
            }

            return rc;
        }

        void skip(DataInput in)
            throws IOException {

            int length = in.readInt();
            in.skipBytes(length);
        }
    }

    /**
    * Transcribe Locale.
    */
    class LocaleTranscriber extends AbstractTranscriber {
        void store(Object value, FOStoreOutput out) throws IOException {
            Locale val = (Locale)value;
            out.writeUTF(val.getLanguage());
            out.writeUTF(val.getCountry());
            out.writeUTF(val.getVariant());
        }

        Object fetch(DataInput in, Object owner, int fieldNum)
            throws Exception {

            String language = in.readUTF();
            String country = in.readUTF();
            String variant = in.readUTF();
            return new Locale(language, country, variant);
        }

        void skip(DataInput in)
            throws IOException {

            in.readUTF(); // language
            in.readUTF(); // country
            in.readUTF(); // variant
        }
    }

    /**
    * Returns a class for the given name.
    * @param name Name of the Class to return.
    * @throws JDOFatalUserException if the named Class cannot be loaded.
    */
    private Class loadClass(String name, Object obj) {
        Class rc = null;
        try {
            rc = pm.loadClass(name, obj.getClass().getClassLoader());
        } catch (ClassNotFoundException ex) {
            throw new JDOUserException(
                msg.msg("EXC_CannotLoadClass", name)); // NOI18N
        }
        return rc;
    }
}
