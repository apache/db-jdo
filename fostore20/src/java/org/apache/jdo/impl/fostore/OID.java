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

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.StringTokenizer;

import javax.jdo.JDOFatalException;
import javax.jdo.JDOUserException;
import javax.jdo.spi.PersistenceCapable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;
import org.netbeans.mdr.persistence.btreeimpl.btreestorage.BtreeFactory;
import org.netbeans.mdr.persistence.Storage;
import org.netbeans.mdr.persistence.MOFID;

/**
 * Represents the identity of a JDO object in the File/Object store.  This
 * implementation uses datastore identity.  The identity is based on the class
 * of the object and a unique identifier within that class.  These OID's are
 * unique only within a single datastore.
 * <p>
 * This class is <code>public</code> as required by the JDO specification.
 *
 * @author Dave Bristor
 * @version 1.0.1
 */
// XXX TBD Remote: Allocate provisional OID's per-PMF w/ remote store.
public class OID implements Serializable, Comparable {
    //
    // Make sure that the number of bits uses here add up to the right number
    // of bits as per the size of the OID.
    //
    // In the initial implementation, we are using a long as the size of the
    // OID.  The layout is:
    // [reserved: 2] [provisional-CLID: 1] [provisional-UID: 1] [class id: 12] [unique id: 48]
    //
    static final long RESERVED_MASK =    0xc000000000000000L;
    static final long PROV_UID_MASK =    0x1000000000000000L;
    static final long PROV_CLID_MASK =   0x2000000000000000L;
    static final long CLID_MASK =        0x0fff000000000000L;
    static final long UID_MASK =         0x0000ffffffffffffL;

    // Shift a clid's id by this much to "or" it into an oid.
    static final int CLID_SHIFT = 48;

    // Maximum value for a CLID and UID.
    static final int MAX_CLID = (int)(CLID_MASK >> CLID_SHIFT);
    static final long MAX_UID = UID_MASK;

    // Shift the reserved bits over by this much to "or" them into an oid.
    static final int RESERVED_SHIFT = 61;

    /**
    * The 'value' of this OID.
    */
    // JDO spec - required.
    public long oid;

    // Hashcode uniquely identifying this CLID within this JVM.
    private int hashCode;

    // The value for oid that will be used by the next-created provisional OID.
    private static long nextProvisional = 0;

    // The Class that defined this OID
    private Class pcClass = null;

    // For synchronizing access to nextProvisional;
    private static final Integer lock = new Integer(1);

    /** I18N support. */
    static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    //
    // OID's are created 3 ways:
    // * Creating an "empty" Oid and then (presumably) setting
    //   the long oid value.
    // * Creating a filled-in OID from a long value.
    // * Creting an OID from a CLID.
    //

    /**
    * Creates an OID with the no value.
    */
    // JDO spec - required.
    public OID() { }

    /**
     * Constructor that takes the result of toString() and creates a new
     * OID.  Currently only the CLID and UID are used.  The provisional
     * bits are ignored.
     * @see org.apache.jdo.impl.fostore.OID#toString
     */
    // JDO spec - required.
    public OID (String str) {
        StringTokenizer st = new StringTokenizer (str, "OID: -()"); // NOI18N
        String clid = st.nextToken();
        String uid = st.nextToken();
        long clidBits = Long.parseLong (clid);
        long uidBits = Long.parseLong (uid);
        oid = (clidBits << CLID_SHIFT) | uidBits;
    }

    /**
    * Creates an OID with the given value.
    */
    // public for user convenience
    public OID(long oid) {
        this.oid = oid;
    }

    // Returns a new OID for the given CLID.  The OID is provisional if the
    // CLID is provisional.
    private OID(CLID clid) {
        synchronized(lock) {
            long clidBits = clid.getId();
            clidBits <<= CLID_SHIFT;
            oid = clidBits | ++nextProvisional;

            if (clid.isProvisional()) {
                oid |= (PROV_UID_MASK | PROV_CLID_MASK);
            }
        }
    }

    //
    // I prefer to avoid using constructors outside of a given class, and to
    // instead use factory methods.  The above constructors which are public
    // are that way for the sake of the JDO spec.  Code within fostore uses
    // these factory methods instead.
    //

    /**
    * Create and return a provisional OID
    * @return A Provisional OID.
    */
    static OID create(CLID clid) {
        OID rc = new OID(clid);
        if (clid.isProvisional()) {
            rc.oid |= PROV_CLID_MASK;
        }
        rc.oid |= PROV_UID_MASK;
        return rc;
    }

    /**
    * Create and return a based on a given representation.
    * @return A real, non-provisional OID.
    * @exception JDOFatalException Thrown if given oid has its provisional bit set.
    */
    OID create(long oid) {
        OID rc = new OID(oid);
        if (rc.isProvisional()) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "create(oid)", // NOI18N
                msg.msg("ERR_InvalidAttemptToCreateOID", new Long(oid))); // NOI18N
        }
        return rc;
    }

    /**
    * Provides an OID for the given CLID and UID.  The given CLID must not be
    * provisional, or a JDOFatalException will result.
    * @param clid CLID for the OID.
    * @param uid UID part of the OID.
    * @return A new OID based on the given clid and uid.
    * @exception JDOFatalException Thrown if given CLID is provisional.
    */
    static OID create(CLID clid, long uid) {
        if (clid.isProvisional()) {
            throw new FOStoreFatalInternalException(
                OID.class, "create(clid, oid)", // NOI18N
                msg.msg("ERR_InvalidAttemptToCreateOID", clid, new Long(uid))); // NOI18N
        }
        long clidBits = clid.getId();
        clidBits = clidBits << CLID_SHIFT;
        return new OID(clidBits | uid);
    }

    //
    // Provide access to information about an OID.
    //

    /**
    * Indicates whether this OID is provisional.
    * @return true if this OID is provisional, false otherwise.
    */
    public boolean isProvisional() {
        boolean rc = false;
        // If CLID is provisional, it *must* be provisional.  If not, it can
        // still be a real CLID with a provisional UID part.
        if ((oid & PROV_CLID_MASK) != 0) {
            rc = true;
        } else if ((oid & PROV_UID_MASK) != 0) {
            rc = true;
        }
        return rc;
    }

    /**
    * Provides the CLID part of this OID.  The resulting CLID is
    * provisional if it is provisional within this OID.  I.e., this might be a
    * provisional OID, but the CLID part could still be datastore-assigned.
    * @return The CLID part of this OID.
    */
    public CLID getCLID() {
        long clidBits = oid & CLID_MASK;
        clidBits >>= CLID_SHIFT;
        return CLID.create((int)clidBits, (oid & PROV_CLID_MASK) != 0);
    }

    /**
    * Provides the unique id part of this OID.
    * @return The unique id part of this OID.
    */
    public long getUID() {
        return oid & UID_MASK;
    }

    /**
    * Provides a JVM-unique hashCode for this OID.
    */
    public int hashCode() {
        if (0 == hashCode) {
            hashCode = new Long(oid).hashCode();
        }
        return hashCode;
    }

    /**
    * Determines if this OID is equal to another.
    * @param other The other OID in the equality comparison.
    * @return True if they are equal, false otherwise.
    */
    public boolean equals(Object other) {
        boolean rc = false;
        if (other instanceof OID) {
            rc = oid == ((OID)other).oid;
        }
        return rc;
    }

    /**
    * Returns a String representation of this OID.  Includes whether or not
    * the OID is provisional, and its reserved bits, if they are set.
    */
    public String toString() {
        StringBuffer rc =
            new StringBuffer(
                "OID: " + // NOI18N
                ((oid & CLID_MASK) >> CLID_SHIFT) +
                "-" + (oid & UID_MASK)); // NOI18N
        if (isProvisional()) {
            rc.append(" (provisional)"); // NOI18N
        }
        long res = oid & RESERVED_MASK;
        if (res > 0) {
            res = res >> RESERVED_SHIFT;
            rc.append(" (reserved=" + res + ")"); // NOI18N
        }
        return rc.toString();
    }

    /**
    * Returns the id itself in String form, for debugging.
    */
    public String oidString() {
        return "" + oid; // NOI18N
    }

    //
    // Serialization
    // We provide the {write,read}Object methods for java.io.Serialization, so
    // that we know exactly what's being written and read.  We also have
    // methods used elsewhere in the fostore package that don't rely
    // ObjectOutput stuff.
    //

    /**
    * Writes this OID to the output stream.
    */
    private void writeObject(ObjectOutputStream out) throws IOException {
        write(out);
    }

    /**
    * Reads this OID's value from the input stream.
    */
    private void readObject(ObjectInputStream in) throws IOException {
        boolean appIdType = in.readBoolean();
        oid = in.readLong();
    }

    void write(DataOutput out) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("OID.write: " + oid);  // NOI18N
        }
        out.writeBoolean(false);
        out.writeLong(oid);
    }

    static OID read(DataInput in) throws IOException {
        boolean appIdType = in.readBoolean();
        long oid = in.readLong();
        if (logger.isDebugEnabled()) {
            logger.debug("OID.read: " + oid + " appIdType: " + appIdType);  // NOI18N
        }
        OID rc = null;
        if (appIdType) {
            rc = new AID(oid);
            ((AID)rc).readBuffer(in);
        } else {
            rc = new OID(oid);
        }
        return rc;
    }

    /**
     * Skip OID bytes from the input.
     * @param in DataInput.
     * @throws IOException.
     */
    static void skip(DataInput in) throws IOException {
        boolean appIdType = in.readBoolean();
        long oid = in.readLong();
        if (logger.isDebugEnabled()) {
            logger.debug("OID.skip: " + oid + " appIdType: " + appIdType);  // NOI18N
        }
        if (appIdType) {
            int length = in.readInt();
            in.skipBytes(length);
        }
    }

    //
    // Implementation methods.
    //

    /**
     * Returns copy of the requested oid to be accessed by the user.
     */
    Object getExternalObjectId(PersistenceCapable pc) {
        return new OID(oid);
    }

    /**
    * Provides the OID in a form that can be used by the database as a key.
    */
    MOFID keyValue(FOStoreBtreeStorage storage) {
        return storage.createMOFID(getCLID().getId(), getUID());
    }

    /** Replaces provisional oid with real oid (datastore identity only)
     * @param realOID as OID instance
     * @param pmf as FOStorePMF
     * @param sm as StateManagerInternal
     */
    void replaceProvisionalOIDWithReal(OID realOID, FOStorePMF pmf,
        StateManagerInternal sm) {

        pmf.mapProvisionalOIDToReal(this, realOID);
        sm.setObjectId(realOID);
    }

    /**
     * Returns copy of the requested oid.
     */
    OID copy() {
        return new OID(oid);
    }

    /**
     * Copy key fields from OID into PC instance. No-op for the
     * datastore identity type for this OID.
     * @param sm as StateManagerInternal
     * @param pmf as FOStorePMF
     * @param pcClass Class of the PC instance.
     * @param pkfields array of PK field numbers.
     */
    void copyKeyFieldsToPC(StateManagerInternal sm, FOStorePMF pmf,
                                    Class pcClass, int[] pkfields) {}

    /**
     * Returns Class that defined OID.
     * @param pmf as FOStorePMF
     */
    Class getPCClass(FOStorePMF pmf) {
        if (pcClass == null) {
            FOStoreModel model = pmf.getModel();
            pcClass = model.getClass(getCLID());
            if (logger.isDebugEnabled()) {
                logger.debug("OID.getPCClass: " + getCLID() + " " + pcClass);  // NOI18N
            }
        }
        return pcClass;
    }

    /**
     * Returns false for application identity type for this OID.
     */
    boolean isApplicationIdentity() {
        return false;
    }

    /**
     * Returns true for datastore identity type for this OID.
     */
    boolean isDataStoreIdentity() {
        return true;
    }
    
    /** Compare this OID to another OID. This is needed to implement an
     * absolute ordering of OIDs. The ordering must provide for comparing
     * provisional OIDs to permanent OIDs, with all provisional OIDs 
     * comparing greater than all permanent OIDs.
     * @since 1.0.1
     */
    public int compareTo(Object obj) {
        if (!(obj instanceof OID)) {
            throw new JDOUserException(msg.msg("EXC_CannotCompareNonOID")); // NOI18N
        }
        OID other = (OID)obj;
        // if other is provisional and we're not, other is bigger
        if (other.isProvisional() & !this.isProvisional()) {
            return -1;
        } else if (this.isProvisional() & !other.isProvisional()) {
            return 1;
        } else // compare UIDs
            // both are provisional or both not; which is bigger UID?
            return (this.getUID() < other.getUID())?-1:1;
    }
    
}
