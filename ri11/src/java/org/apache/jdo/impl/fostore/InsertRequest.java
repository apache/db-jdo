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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.jdo.model.jdo.PersistenceModifier;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.sco.SCOMap;
import org.apache.jdo.state.FieldManager;
import org.apache.jdo.state.StateManagerInternal;

/**
 * Represents a request to write a new object to the datastore.
 *
 * @author Dave Bristor
 * @version 1.0.1
 */
//
// This is client-side code.  It does not need to live in the server.
//
class InsertRequest extends AbstractFieldRequest {
    // Provisional object id of object being inserted by this request.
    private final OID oid;

    // Metadata for this object.
    private final FOStoreModel model;

    private final Class cls;

    // Set of CLID's of the object instances referenced by this object's
    // fields (note that Strings are omitted).  This is sent to the store
    // along with the object values, as an appendix, so that when
    // reconstituting the object, information about the referenced classes is
    // available.  Of course, metadata for the object is available (it was or
    // will be sent to the store in an ActivateClassRequest), but that field
    // information is about the type of referenced objects, not about their
    // class.  See storeObjectField().
    private Set clids = null;

    // Set of int arrays of object references in this object which are
    // provisional, as offsets of OID's in the request buffer.  See the end
    // of doRequestBody, and storeObjectField().  Each field may add one
    // entry to the ArrayList; each field may have any number of offsets
    // within it.  totNumOffsets is the total number of offsets, across all
    // arrays in the ArrayList.
    private ArrayList oidOffsets = new ArrayList();
    private int numOidOffsets = 0;

    private int clidOffsets[];
    private int numClidOffsets;

    InsertRequest(StateManagerInternal sm, Message m, FOStorePMF pmf) {
        super(sm, m, pmf);
        this.oid = (OID)sm.getInternalObjectId();
        this.model = pmf.getModel();
        this.cls = sm.getPCClass();
    }

    //
    // Methods from AbstractRequest
    //

    /**
     * Provides detail about data being inserted in an InsertRequest.
     * The format of this request is (aside from the request header):
     * <pre>
     * oid: OID
     * length of data block containing classname, field values, CLID info: int
     * className: String (fully qualified name of class of object)
     * fostoreSchemaUID for class
     * length of the field values part of this block: int
     * fieldValue: Object... (repeats numFields times)
     * number of [CLID, classname] pairs for Object fields of this object
     * [CLID, classname]...
     * (the following are not in the above noted length bytes of this block)
     * number of provisional OID's written in this request
     * OID offset...
     * number of the above written CLIDs that are provisional
     * CLID offset...
     * </pre>
     *
     * In the case of both the OID and CLID offsets, the offset is from
     * the beginning of the block.  In the case of the CLIDs and the OID
     * and CLID offsets, the size of each set written may be zero but
     * that size is always written.
     * <p>
     * Note that the number of CLID/classname pairs and the CLID/classname
     * pairs themselves are part of the data block that is intended to be
     * stored by the database server (and hence later returned upon
     * fetch/getExtent).  That is, the size written includes those pairs.
     * The offsets, however, are not part of the datablock; they are used
     * by the database server and discarded.
     * @see AbstractRequest#doRequestBody
     */
    protected void doRequestBody() throws IOException {
        oid.write(out);

        // Write out the values of all the fields.
        int startPos = writeBlock(jdoClass.getPersistentFieldNumbers(), false);

        // Now write the oidOffsets, so that the store can turn provisional
        // OID's into datastore OID's.
        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.dRB: numoidOffsets=" + // NOI18N
                           numOidOffsets);
        }
        out.writeInt(numOidOffsets);
        int size = oidOffsets.size();
        for (int i = 0; i < size; i++) {
            int fieldOffsets[] = (int[])(oidOffsets.get(i));
            int numOffsets = fieldOffsets.length;
            for (int j = 0; j < numOffsets; j++) {
                // Write out offset relative to start of the data block.
                out.writeInt(fieldOffsets[j] - startPos);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "InsertRequest.dRB: " + oid // NOI18N
                        + " rawOffset=" + fieldOffsets[j] // NOI18N
                        + " offset=" + // NOI18N
                        (fieldOffsets[j] - startPos));
                }
            }
        }

        // Now write the offsets of the provisional CLID's
        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.dRB: numClidOffsets=" + // NOI18N
                           numClidOffsets);
        }
        out.writeInt(numClidOffsets);
        for (int i = 0; i < numClidOffsets; i++) {
            out.writeInt(clidOffsets[i]);
        }
    }

    //
    // Methods from Request
    //

    /**
     * Reads the oid, and notifies the persistence manager and state manager
     * of the new oid.
     * @see Request#handleReply
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        OID replyOid = OID.read(in);

        // We don't have to update the cache, because we will when the
        // instance was made persistent, we did a CreateOIDRequest, and it's
        // reply updated the cache.

        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.hR: " + status + ", " + // NOI18N
                           oid + " " + // NOI18N
                           Tester.toHex(oid.oid, 16) +
                           " -> " + replyOid + " " + // NOI18N
                           Tester.toHex(replyOid.oid, 16));
        }
    }

    //
    // Methods from FieldManager
    //

    /**
    * @see org.apache.jdo.state.FieldManager#storeBooleanField(int fieldNum,
    * boolean value)
    */
    public void storeBooleanField(int fieldNum, boolean value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeBoolean(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeBooleanField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeCharField(int fieldNum,
    * char value)
    */
    public void storeCharField(int fieldNum, char value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeChar(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeCharField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeByteField(int fieldNum,
    * byte value)
    */
    public void storeByteField(int fieldNum, byte value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeByte(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeByteField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeShortField(int fieldNum,
    * short value)
    */
    public void storeShortField(int fieldNum, short value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeShort(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeShortField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeIntField(int fieldNum, int value)
    */
    public void storeIntField(int fieldNum, int value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeInt(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeIntField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeLongField(int fieldNum,
    * long value)
    */
    public void storeLongField(int fieldNum, long value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeLong(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeLongField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeFloatField(int fieldNum,
    * float value)
    */
    public void storeFloatField(int fieldNum, float value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeFloat(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeFloatField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeDoubleField(int fieldNum,
    * double value)
    */
    public void storeDoubleField(int fieldNum, double value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            t.storeDouble(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeDoubleField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeStringField(int fieldNum,
    * String value)
    */
    public void storeStringField(int fieldNum, String value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            // Though it is a String, we need to preserve pm value
            // in the ObjectTranscriber in case this is request
            // to fetch a PC (embedded or because its field is accessed.
            PersistenceManagerInternal pm =
                (PersistenceManagerInternal)sm.getPersistenceManager();
            t.storeObject(value, out, pm); 
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeStringField", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.state.FieldManager#storeObjectField(int fieldNum,
    * Object value)
    */
    public void storeObjectField(int fieldNum, Object value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            PersistenceManagerInternal pm =
                (PersistenceManagerInternal)sm.getPersistenceManager();
            int fieldOffsets[] = t.storeObject(value, out, pm);

            // Add the field's object's CLID.
            addCLID(value);

            // If value is Collection, Map, or array then need to add the
            // Class's of its elements.
            if (null != value) {
                if (value instanceof Collection) {
                    addCollectionCLIDs((Collection)value);
                } else if (value instanceof Map) {
                    addMapCLIDs((Map)value);
                } else {
                    Class cls = value.getClass().getComponentType();
                    if (null != cls) {
                        // We have an array.  We have to add the CLIDs for all
                        // its elements, not just for the component type,
                        // because the class of the elements could be a
                        // subclass or implementation of the component type.
                        addArrayCLIDs(value);
                    }
                }
            }

            // Check for and store offsets.
            if (null != fieldOffsets) {                
                oidOffsets.add(fieldOffsets);
                numOidOffsets += fieldOffsets.length;
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "InsertRequest.sOF: fieldNum=" + fieldNum // NOI18N
                        + " numOffsets=" + fieldOffsets.length); // NOI18N
                }
            }
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeObjectField", ex); // NOI18N
        }
    }

    //
    // Implementation detail
    //

    protected OID getOID() {
        return oid;
    }

    /**
     * Writes a data block, which consists of the values of the specified
     * fields, plus the CLID's and corresponding class names of referenced
     * objects.
     * @param fields Field numbers of the fields to be written
     * @param identifying If true, write before/flushed image fields,
     * otherwise write current fields.
     * @return Position in output stream at which data block starts
     */
    protected int writeBlock(int fields[], boolean identifying) throws IOException {    
        // Save and overwrite the position where we later write the length of
        // the data.
        int dataLengthPos = out.getPos();
        out.writeInt(LENGTH_COOKIE);

        // Save start of data block
        int startPos = out.getPos();

        // Initialize list of CLIDs used by this instance
        clids = new HashSet();

        // Fully qualified class name and FOStoreSchemaUID
        if (logger.isDebugEnabled()) {
            logger.debug("IR: className=" + jdoClass.getName() + // NOI18N
                           ", fsuid=" + fsuid); // NOI18N
        }
        out.writeUTF(jdoClass.getName());
        fsuid.write(out);

        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.writeBlock: classname=" + // NOI18N
                           jdoClass.getName());
        }

        insertFields(fields, identifying);

        // Write the CLID's and classnames.  Some might be provisional, so as
        // with the OID's we must write their offsets so they can be turned
        // into real CLID's in the store.  Here we save the offsets, to write
        // them later, after the data block.
        int size = clids.size();
        clidOffsets = new int[size]; // Max num of possible provisionals
        numClidOffsets = 0;
        if (logger.isDebugEnabled()) {
            logger.debug(
                "InsertRequest.writeBlock: numCLID's=" +  // NOI18N
                size);
        }
        out.writeInt(size);
        for (Iterator i = clids.iterator(); i.hasNext();) {
            CLID c = (CLID)i.next();
            if (c.isProvisional()) {
                clidOffsets[numClidOffsets++] = out.getPos() - startPos;
            }
            c.write(out);
            Class clz = CLID.getKnownType(c);
            if (null == clz) {
                clz = model.getClass(c);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("InsertRequest.writeBlock: " + c); // NOI18N
            }
            String className = clz.getName();
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "InsertRequest.writeBlock: field type " + // NOI18N
                    className);
            }
            out.writeUTF(className);
            FOStoreSchemaUID fldUID = FOStoreSchemaUID.lookup(clz, model);
            fldUID.write(out);
        }

        // Write the length of the block (data + CLID's)
        int currentPos = out.getPos();
        int length = currentPos - startPos;
        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.dRB: length=" + length); // NOI18N
        }
        out.setPos(dataLengthPos);
        out.writeInt(length);
        out.setPos(currentPos);

        return startPos;
    }

    /**
     * Writes values of the specified fields.  If identifying is true, writes
     * identifying fields, otherwise writes current fields.
     */
    private void insertFields(int[] fields, boolean identifying)
        throws IOException {

        int fieldLengthPos = out.getPos();
        out.writeInt(LENGTH_COOKIE);
        int fieldPos = out.getPos();
        
        sm.provideFields(fields, this, identifying);
        
        // Write the length of the field values part of the block.
        int currentPos = out.getPos();
        int length = currentPos - fieldPos;
        out.setPos(fieldLengthPos);
        out.writeInt(length);
        out.setPos(currentPos);

        if (logger.isDebugEnabled()) {
            logger.debug("InsertRequest.dRB: field values length=" + length); // NOI18N
        }
    }

    private void addCollectionCLIDs(Collection c) {
        Iterator i;
        if (c instanceof SCOCollection) {
            SCOCollection sco = (SCOCollection)c;
            i = sco.eitherIterator();
        } else {
            i = c.iterator();
        }
        while(i.hasNext()) {
            addCLID(i.next());
        }
    }

    private void addMapCLIDs(Map m) {
        Iterator i = null;
        if (m instanceof SCOMap) {
            i = ((SCOMap)m).eitherIterator();
        } else {
            i = m.entrySet().iterator();
        }
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry)i.next();
            addCLID(entry.getKey());
            addCLID(entry.getValue());
        }
    }

    private void addArrayCLIDs(Object arr) {
        try {
            int length = Array.getLength(arr);
            for (int i = 0; i < length; i++) {
                Object o = Array.get(arr, i);
                addCLID(o);
            }
        } catch (RuntimeException ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "addArrayCLIDs", ex); // NOI18N
        }
    }

    private void addCLID(Object o) {
        if (null != o) {
            Class cls = o.getClass();
            if (! CLID.isKnown(cls)) {
                clids.add(model.getCLID(cls));
            }
        }
    }
}
