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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Arrays;

import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.security.NoSuchAlgorithmException;

import javax.jdo.JDOFatalException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;


/**
 * Represents the identity of a JDO object in the File/Object store.  This
 * implementation uses application identity.  The identity is based on the class
 * of the object and a byte[] buffer that represents values in the user
 * object Id instance which creates a unique identifier within that class.  
 * These ID's are unique only within a single datastore.
 * <p>
 *
 * @author Marina Vatkina
 */
public class AID extends OID {

    // These bytes represent user ID:
    private byte[] buffer;

    // Hashcode uniquely identifying this AID via UID.
    private int hashCode;

    // CLID for the superclass that corresponds to the user defined
    // key class.
    private long sCLIDBits;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** JDOImplHelper instance */
    private final static JDOImplHelper jdoImplHelper = 
        (JDOImplHelper) AccessController.doPrivileged (
            new PrivilegedAction () {
                public Object run () {
                    try {
                        return JDOImplHelper.getInstance();
                    }
                    catch (SecurityException e) {
                        throw new JDOFatalUserException (msg.msg(
                            e.getMessage()), e); // NOI18N
                    }
                }
            }    
        );

    /** 
     * Default constructor.
     */
    public AID() {
        super();
        sCLIDBits = getCLID().getId();
    }

    /**
    * Creates an AID with the given value.
    */
    public AID(long oid) {
        super(oid);
        sCLIDBits = getCLID().getId();
    }

    /**
    * Create and return a new instance of AID. 
    * @return a new instance of AID.
    */
    static AID create(Class pcClass, PersistenceCapable pc, 
                                      Object userOid, 
                                      PersistenceManagerInternal pm,
                                      FOStorePMF pmf) {
        boolean fromPC = false;
        if (userOid == null) { 
            fromPC = true;
            userOid = pc.jdoNewObjectIdInstance();
            pc.jdoCopyKeyFieldsToObjectId(userOid);
        }
        AID rc = new AID();
        rc.createBuffer(userOid, pcClass, pmf, fromPC, pc);
        rc.setSuperCLIDBits(pcClass, userOid.getClass(), pm, pmf);

        return rc;
    }


    /**
    * Determines if this AID is equal to another.
    * @param other The other AID in the equality comparison.
    * @return True if they are equal, false otherwise.
    */
    public boolean equals(Object other) {
        boolean rc = false;
        if (other instanceof AID) {
            AID o = (AID)other;

            long uidBits = this.oid & UID_MASK;
            long o_uidBits = o.oid & UID_MASK;

            // It is enough to compare super class CLID bits,
            // as there are no 2 subclasses with the same uid.
            rc = Arrays.equals(this.buffer, o.buffer) && 
                (uidBits == o_uidBits) && (sCLIDBits == o.sCLIDBits);
        }
        return rc;
    }

    /**
    * Returns a String representation of this AID.  Includes whether or not
    * the instance is provisional, and its reserved bits, if they are set.
    */
    public String toString() {
        StringBuffer rc =
            new StringBuffer(
                "OID: " + // NOI18N
                ((oid & CLID_MASK) >> CLID_SHIFT) +
                "-0x") ; // NOI18N
        for (int i = 0; i < buffer.length; i++) {
            rc.append(toHexString(buffer[i]));
        }       

        if (isProvisional()) {
            rc.append(" (provisional)"); // NOI18N
        }
        long res = oid & RESERVED_MASK;
        if (res > 0) {
            res = res >> RESERVED_SHIFT;
            rc.append(" (reserved=" + res + ")"); // NOI18N
        }
        rc.append(" (super CLID=" + sCLIDBits + ")"); // NOI18N
        return rc.toString();      
    }

    /**
    * Returns the id itself in String form, for debugging.
    */
    public String oidString() {
        return "" + oid; // NOI18N
    }

    /**
    * Provides a unique hashCode for this AID.
    */
    public int hashCode() {
        if (0 == hashCode) {
            hashCode = new Long(oid & UID_MASK).hashCode();
        }
        return hashCode;
    }

    
    /**
     * Returns true for application identity type for this OID.
     */
    boolean isApplicationIdentity() {
        return true;
    }

    /**
     * Returns false for datastore identity type for this OID.
     */
    boolean isDataStoreIdentity() {
        return false;
    }

    /**
     * Returns copy of the requested oid to be accessed by the user.
     */
    Object getExternalObjectId(PersistenceCapable pc) {

        // Create and return user Oid value from persistence-capable
        // instance.
        Object rc = pc.jdoNewObjectIdInstance();
        pc.jdoCopyKeyFieldsToObjectId(rc);

        return rc;
    }

    //
    // Serialization
    // We provide the {write,read}Object methods for java.io.Serialization, so
    // that we know exactly what's being written and read.  We also have
    // methods used elsewhere in the fostore package that don't rely
    // ObjectOutput stuff.
    //

    /**
    * Writes this AID to the output stream.
    */
    public void writeObject(ObjectOutputStream out) throws IOException {
        write(out);
    }

    /**
    * Reads this AID's value from the input stream. 
    */
    public void readObject(ObjectInputStream in) throws IOException {
        boolean applicationIdentity = in.readBoolean();
        oid = in.readLong();
        this.readBuffer(in);
    }

    /**
    * Writes this AID to the output stream.
    */
    void write(DataOutput out) throws IOException {
        if (logger.isDebugEnabled()) {
                logger.debug("AID.write: " + this + " oid: " + oid);  // NOI18N
        }
        out.writeBoolean(true);
        out.writeLong(oid);
        out.writeLong(sCLIDBits);
        out.writeInt(buffer.length);
        out.write(buffer);
    }

    /**
    * Reads AID buffer's value from the input stream. 
    */
    void readBuffer(DataInput in) throws IOException {
        try {
            sCLIDBits = in.readLong();
            int length = in.readInt();
            buffer = new byte[length];
            in.readFully(buffer);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                AID.class, "read", ex); // NOI18N
        }
        if (logger.isDebugEnabled()) {
                logger.debug("AID.read: " + this);  // NOI18N
        }
    }

    /** Replaces provisional oid with real oid (datastore identity only)
     * @param realOID as OID instance
     * @param pmf as FOStorePMF
     * @param sm as StateManagerInternal
     */
    void replaceProvisionalOIDWithReal(OID realOID, FOStorePMF pmf, 
                                           StateManagerInternal sm) {
        // Construct oid from new CLID and existing UID.
        if (logger.isDebugEnabled()) {
            logger.debug("AID.replaceProvisionalOIDWithReal: " +   // NOI18N
                this + " with: " + realOID); // NOI18N
        }
        OID oldOid = this.copy();

        long clidBits = realOID.oid & CLID_MASK;
        long uidBits = this.oid & UID_MASK;
        this.oid = clidBits | uidBits; 

        if (sm != null && sm.getPCClass() != null) { 
            // Calculate superclass version of this OID for future lookup.
            setSuperCLIDBits(sm.getPCClass(), null, sm.getPersistenceManager(), pmf);

        } else if (oldOid.isProvisional()) {
            // else just sync the values.
            sCLIDBits = getCLID().getId();
        }
    }

    /**
     * Returns copy of the requested oid.
     */  
    OID copy() {
        AID rc = new AID();
        rc.oid = oid;
        rc.sCLIDBits = sCLIDBits;
        rc.buffer = new byte[buffer.length];
        System.arraycopy(buffer, 0, rc.buffer, 0, buffer.length);

        return rc;
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
                                    Class pcClass, int[] pkfields) {
        FieldFetcher ff =
            new FieldFetcher(new FOStoreInput(buffer, 0, buffer.length),
                 pmf.getModel(),
                 sm.getPersistenceManager(),
                 pcClass.getClassLoader(),
                 false); // do not skip other fields
        ff.setPCClass(pcClass);

        sm.replaceFields(pkfields, ff);
    }

    //
    // Internal methods.
    //
    
    private void createBuffer (Object userOid, Class pcClass, FOStorePMF pmf, 
            boolean fromPC, PersistenceCapable pc) {
        if (logger.isDebugEnabled()) {
            logger.debug("AID.createBuffer: " +   // NOI18N
                " for userOid: " + userOid + ", pcClass: " + pcClass.getName()); // NOI18N
        }
        FOStoreModel model = pmf.getModel();
        CLID clid = model.getCLID(pcClass);
        long clidBits = clid.getId();
        clidBits <<= CLID_SHIFT;
        if (clid.isProvisional()) {
            clidBits |= PROV_CLID_MASK;
        }

        FOStoreOutput out = new FOStoreOutput();
        AIDTranscriber aidTranscriber = new AIDTranscriber(out, pcClass, pmf);
        try {
            jdoImplHelper.copyKeyFieldsFromObjectId(
                    pcClass, aidTranscriber, userOid);
        } catch (NullPointerException npe) {
            if (fromPC) {
                throw new JDOUserException(msg.msg("EXC_MakePersistentKeyFieldNull", 
                    npe.getMessage()), pc);
            } else {
                throw new JDOUserException(msg.msg("EXC_GetObjectByIdKeyFieldNull", 
                    npe.getMessage()), userOid);
            }
        }

        // Read the field values part of the block.
        int length = out.getPos();
        byte[] temp = out.getBuf();
        buffer = new byte[length];
        System.arraycopy(temp, 0, buffer, 0, length);

        long uidBits = computeUID();
        oid = clidBits | (uidBits & UID_MASK); 
        if (logger.isDebugEnabled()) {
            logger.debug("AID.createBuffer: " +   // NOI18N
                this + " oid: " + Long.toHexString(oid)); // NOI18N
        }

    }

    /** 
     * Sets CLID bits for the superclass that corresponds to the user
     * defined key class.
     * @param pcClass the class of the persisntence-capable instance or
     * null if not known.
     * @param keyClass the class of the user defined key or null if not
     * known.
     * @param pm the PersistenceManagerInternal that requested the operation.
     * @param pmf the FOStorePMF that requested the operation.
     */
    private void setSuperCLIDBits(Class pcClass, Class keyClass, 
                                  PersistenceManagerInternal pm,
                                  FOStorePMF pmf) {
        long rc = getCLID().getId();

        if (pcClass != null) {
            if (keyClass == null) {
                keyClass = jdoImplHelper.newObjectIdInstance(pcClass).getClass();
            } 
            try {

                Class cls = pm.loadPCClassForObjectIdClass(keyClass);
                if (cls != null) {
                    rc = pmf.getModel().getCLID(cls).getId();

                    if (logger.isDebugEnabled()) {
                        logger.debug("AID.getSuperCLID: " + rc);  // NOI18N
                    }
                }
            } catch (Exception e) {
                // ignore - will set superclass to the pcClass.
            }

        }
        sCLIDBits = rc;
    }

    /**
     * Compute unique user Id from the buffer.
     */
    private long computeUID() {
        long rc = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA"); // NOI18N
            byte hasharray[] = md.digest(buffer);
            for (int i = 0; i < Math.min(8, hasharray.length); i++) {
                rc += (long)(hasharray[i] & 255) << (i * 8);
            }
         
        } catch (NoSuchAlgorithmException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_Algorithm"), ex); // NOI18N
        }
        return rc;
    }

    /**
     * Covert byte into 2-digit hexadecimal String.
     */
    private static String toHexString(int b) {
        char a = (b < 0)? 'F' : '0';
        return "" + a + hexDigit[(b & 0xF)]; //NOI18N
    }

    /** A table of hex digits */
    private static final char[] hexDigit = {
        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };

}
