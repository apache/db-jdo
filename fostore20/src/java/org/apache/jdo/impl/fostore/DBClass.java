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
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.netbeans.mdr.persistence.Streamable;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.StorageIOException;

/**
* Represents a class stored in the database.
* <p>
* This class is <code>public</code> so that it can be used as a
* <code>Streamable</code> and stored in the database.
*
* @author Dave Bristor
*/
public class DBClass implements Streamable {
    // These are not final, because we must implement Streamable to store
    // DBClass instances in the btree, and later read them out.
    
    /** Fully qualified name of the class represented. */
    private String name;

    /** CLID corresponding to the class.  It might be provisional, until the
     * finishing phase of request handling has run. */
    private CLID clid;

    /** FSUID corresponding to the class. */
    private FOStoreSchemaUID fsuid;

    /** Lists the class's PersistenceCapable superclasses, from the class to
     * Object.  Null unless the class has PersistenceCapable superclasses. */
    private ClassDetail supers[] = null;

    /** The fields of the class.  Null unless the class has fields. */
    private ClassDetail fields[] = null;

    /** Indicates whether the CLIDs of any superclass or field were given as
     * provisional. */
    private transient boolean hasProvisionals = false;

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /** A ClassDetail represents a single field or PC superclass of a class
    * that is (or was at one time) stored in the databse.  When created, the
    * given CLID might be provisional.  If so, it will be replaced with a
    * real CLID during ActivateClassHandler's finish phase.
    * @see ActivateClassHandler#handleRequest
    */
    class ClassDetail {
        /** Name of the field or superclass. */
        final String name;

        /** CLID of the field or superclass.  Not final, because it may be
         * provisional when the ClassDetail is created, and updated later
         * during finishing. */
        CLID clid;

        /* FSUID of the field or superclass. */
        final FOStoreSchemaUID fsuid;

        ClassDetail(DataInput in) throws IOException {
            name = in.readUTF();
            clid = CLID.read(in);
            fsuid = FOStoreSchemaUID.read(in);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "DBClass: fieldName=" + name + ", " + clid); // NOI18N
            }
        }

        void write(DataOutputStream dos) throws IOException {
            dos.writeUTF(name);
            clid.write(dos);
            fsuid.write(dos);
        }

        /**
         * If the current clid is provisional, replace it with a real one if
         * possible.
         */
        void remap(FOStoreDatabase db) {
            if (clid.isProvisional()) {
                CLID rCLID = db.getRealCLIDFromProvisional(clid);
                if (null != rCLID) {
                    clid = rCLID;
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "ClassDetail.remap: " + clid + " -> " + // NOI18N
                            rCLID);
                    }
                }
            }
        }        
        
        CLID getCLID() {
            return clid;
        }

        void setCLID(CLID clid) {
            this.clid = clid;
        }

        public String toString() {
            return "'" + name + "' " + clid.toString(); // NOI18N
        }
    }
    // end of class ClassDetail

    /**
     * Constructor
     */
    private DBClass(String name, CLID clid, FOStoreSchemaUID fsuid,
                    DataInput in, FOStoreDatabase db) throws IOException{

        this.name = name;
        this.clid = clid;
        this.fsuid = fsuid;

        if (logger.isDebugEnabled()) {
            logger.debug("DBClass: " + name + ", " + clid // NOI18N
                           + ", fsuid: " + fsuid); // NOI18N
        }

        int numSupers = in.readInt();
        if (numSupers > 0) {
            supers = new ClassDetail[numSupers];
            getClassDetail(supers, in);
        }

        int numFields = in.readInt();
        if (numFields > 0) {
            fields = new ClassDetail[numFields];
            getClassDetail(fields, in);
        }
    }

    /**
     * @see ActivateClassRequest#doRequestBody
     */
    static DBClass create(String name, CLID clid, FOStoreSchemaUID fsuid,
                          DataInput in, FOStoreDatabase db)
        throws IOException{

        return new DBClass(name, clid, fsuid, in, db);
    }

    /**
     * Gets the details for as many slots as are in the given array.
     */
    private void getClassDetail(ClassDetail details[], DataInput in)
        throws IOException {

        int length = details.length;
        for (int i = 0; i < length; i++) {
            ClassDetail detail = new ClassDetail(in);
            details[i] = detail;
            CLID detailCLID = detail.getCLID();
            if (detailCLID.isProvisional()) {
                hasProvisionals = true;
            }
        }
    }


    /**
    * @return True if any of this DBClass's fields or superclasses has a CLID
    * that is provisional.
    */
    // In that case, ActivateClassHandler should cause remapCLIDs to be
    // invoked during finishing.
    boolean hasProvisionals() {
        return hasProvisionals;
    }

    /**
    * Changes the CLID mapping of this DBClass's fields and PC superclasses
    * in the database as from provisional to real as necessary.
    * @param db Database in which to find the provisional-to-real mapping.
    */
    void remapCLIDs(FOStoreDatabase db) {
        remapCLIDs(supers, db);
        remapCLIDs(fields, db);
    }

    /**
     * Changes provisional CLIDs to real CLIDs in the given ClassDetails.
     */
    private void remapCLIDs(ClassDetail details[], FOStoreDatabase db) {
        if (null != details  && details.length > 0) {
            int length = details.length;
            for (int i = 0; i < length; i++) {
                details[i].remap(db);
            }
        }
    }

    /**
    * @return True if this DBClass has superclasses.
    */
    // In that case, ActivateClassHandler should cause setupSubclasses to be
    // invoked during finishing.
    boolean hasSuperclasses() {
        return null != supers;
    }

    /**
    * Sets up subclass relationships between this DBClass and its superclass
    * DBClass instances.
    */
    // Invariants: all superclasses of this DBClass have already been
    // processed, and this class *does* have superclasses.
    void setupSubclasses(FOStoreDatabase db) throws FOStoreDatabaseException {
        int length = supers.length;
        for (int i = 0; i < length; i++) {
            CLID superCLID = supers[i].getCLID();
            if (logger.isDebugEnabled()) {
                logger.debug("DBClass.setupSubclasses for " + clid + //NOI18N
                    " superclass: " + superCLID); // NOI18N
            }
            OID subclassSetOID = DBInfo.getSubclassSetOID(superCLID);
            SubclassSet ss = (SubclassSet)db.getIfExists(subclassSetOID);
            if (null == ss) {
                ss = SubclassSet.create(subclassSetOID, clid);
            } else {
                ss.add(clid);
            }
            db.put(subclassSetOID, ss);
        }        
    }

    OID getOID() {
        return DBInfo.getDBClassOID(clid);
    }

    CLID getCLID() {
        return clid;
    }

    void setCLID(CLID clid) {
        this.clid = clid;
    }

    String getName() {
        return name;
    }

    FOStoreSchemaUID getFSUID() {
        return fsuid;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DBClass '").append(name).append("' "); // NOI18N
        sb.append(clid.toString());

        if (null != supers) {
            for (int i = 0; i < supers.length; i++) {
                sb.append("\n    superclass " + supers[i].toString()); // NOI18N
            }
        }

        if (null != fields) {
            for (int i = 0; i < fields.length; i++) {
                sb.append("\n    field " + fields[i].toString()); // NOI18N
            }
        }

        sb.append("\n"); // NOI18N
        return sb.toString();
    }

    //
    // Implement Streamable
    //

    public DBClass() { }

    /**
    * Write this DBClass to the given stream.
    */
    public void write(OutputStream os) throws StorageException {
        DataOutputStream dos = new DataOutputStream(os);

        try {
            dos.writeUTF(name);
            clid.write(dos);
            fsuid.write(dos);
            
            writeClassDetails(supers, dos);
            writeClassDetails(fields, dos);
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    /**
    * Write the given details of this DBClass to the given stream.
    */
    private void writeClassDetails(ClassDetail details[],
                                   DataOutputStream dos) throws IOException {

        if (null == details) {
            dos.writeInt(0);
        } else {
            int length = details.length;
            dos.writeInt(length);
            for (int i = 0; i < length; i++) {
                details[i].write(dos);
            }
        }
    }

    /**
    * Initialize this DBClass from the given stream.
    */
    public void read(InputStream is) throws StorageException {
        DataInputStream dis = new DataInputStream(is);
        try {
            this.name = dis.readUTF();
            this.clid = CLID.read(dis);
            this.fsuid = FOStoreSchemaUID.read(dis);
            
            int length = dis.readInt();
            if (length > 0) {
                supers = new ClassDetail[length];
                readClassDetails(supers, dis);
            }

            length = dis.readInt();
            if (length > 0) {
                fields = new ClassDetail[length];
                readClassDetails(fields, dis);
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    /**
    * Read details from the given input stream.
    */
    private void readClassDetails(ClassDetail details[],
                                  DataInputStream dis) throws IOException {
        
        int length = details.length;
        for (int i = 0; i < length; i++) {
            details[i] = new ClassDetail(dis);
        }
    }
}
