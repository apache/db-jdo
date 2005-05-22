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
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

import javax.jdo.JDOFatalInternalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;
import org.netbeans.mdr.persistence.Streamable;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.StorageIOException;

/**
* Represents a set of instances of a given class stored in the database.
* <p>
* This class is <code>public</code> so that it can be used as a
* <code>Streamable</code> and stored in the database.
*
* @author Dave Bristor
*/
public class DBExtent implements Streamable {
    
    /** This DBExtent lives in a database.
     */
    private FOStoreDatabase fodb;
    
    /** Name of the class of instances in this extent.
    */
    private String name;

    /* Along with name, determines equality of extent instances.
    */
    private FOStoreSchemaUID fsuid;

    /** CLID of the class of the instances of this extent.
    */
    private CLID classCLID;

    /** OID of this extent.  Cached so that we don't have to calculate each
    * time we store the extent
    */
    private OID extentOID;

    /** OIDs of instances of this extent.
    */
    private HashSet instances = new HashSet();

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /**
    * Create a new DBExtent
    */
    private DBExtent(FOStoreDatabase fodb, String name,
                     FOStoreSchemaUID fsuid, CLID classCLID) {

        this.fodb = fodb;
        this.name = name;
        this.fsuid = fsuid;
        this.classCLID = classCLID;
        this.extentOID = DBInfo.getExtentOID(classCLID);

        if (logger.isDebugEnabled()) { dump("()"); } // NOI18N
    }

    /**
    * Return a new DBExtent to the caller.
    */
    static DBExtent create(FOStoreDatabase fodb, String name,
                           FOStoreSchemaUID fsuid, CLID classCLID) {
        return new DBExtent(fodb, name, fsuid, classCLID);
    }

    /** @return true if the given name and FOStoreSchemaUID are equal to those
     * in this DBExtent.
     */
    boolean isExtentFor(String n, FOStoreSchemaUID f) {
        boolean rc = false;
        if (null != n && null != f) {
            rc = name.equals(n) && fsuid.equals(f);
        }
        return rc;
    }        

    String getName() {
        return name;
    }

    OID getExtentOID() {
        return extentOID;
    }

    CLID getExtentCLID() {
        return extentOID.getCLID();
    }

    OID getDBClassOID() {
        return DBInfo.getDBClassOID(classCLID);
    }
    
    CLID getClassCLID() {
        return classCLID;
    }

    synchronized void add(OID oid) {
        if (instances.contains(oid)) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_SecondInstance", oid, name)); // NOI18N
        }
        instances.add(oid);
        if (logger.isDebugEnabled()) {
            logger.debug(
                "DBExtent: added " + oid + ", size=" + size()); // NOI18N
        }
    }

    synchronized void remove(OID oid) {
        instances.remove(oid);
    }

    synchronized int size() {
        return instances.size();
    }
    
    Iterator iterator() {
        return instances.iterator();
    }

    synchronized void store(FOStoreDatabase db)
        throws FOStoreDatabaseException {

        db.put(extentOID, this);

        if (logger.isDebugEnabled()) { dump(" store"); } // NOI18N
    }

    //
    // Implement Streamable
    //

    public DBExtent() { }

    public void write(OutputStream os) throws StorageException {
        DataOutputStream dos = new DataOutputStream(os);

        try {
            if (logger.isDebugEnabled()) { dump(" write"); } // NOI18N
                
            dos.writeUTF(name);
            classCLID.write(dos);
            fsuid.write(dos);

            int numInstances = instances.size();
            dos.writeInt(numInstances);
            for (Iterator i = instances.iterator(); i.hasNext();) {
                OID oid = (OID)i.next();
                oid.write(dos);
                if (logger.isDebugEnabled()) {
                    logger.debug("DBExtent.write: wrote " + oid); // NOI18N
                }
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    public void read(InputStream is) throws StorageException {
        DataInputStream dis = new DataInputStream(is);
        try {
            name = dis.readUTF();
            classCLID = CLID.read(dis);
            fsuid = FOStoreSchemaUID.read(dis);

            extentOID = DBInfo.getExtentOID(classCLID);

            int numInstances = dis.readInt();
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "DBExtent.read: numInstances=" + numInstances); // NOI18N
            }
            instances = new HashSet(numInstances);
            for (int i = 0; i < numInstances; i++) {
                instances.add(OID.read(dis));
            }
            if (logger.isDebugEnabled()) { dump(".read"); } // NOI18N
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    private void dump(String where) {
        logger.debug("DBExtent" + where + ": " + toString()); // NOI18N
    }

    public String toString() {
        return name + " " + extentOID + " " + // NOI18N
            Tester.toHex(extentOID.oid, 16) + 
            ", numInstances=" + instances.size(); // NOI18N
    }
}
