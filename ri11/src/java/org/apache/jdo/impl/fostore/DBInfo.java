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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;
import org.netbeans.mdr.persistence.Streamable;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.StorageIOException;

/**
* This class represents the information about the contents of the store
* itself which must be durable across JVMs.
* <p>
* This class is <code>public</code> so that it can be used as a
* <code>Streamable</code> and stored in the database.
*
* @author Dave Bristor
*/
// Streamable requires that the class be public.  I think!.
// This class is server-side only, not needed in client.
public class DBInfo implements Streamable {
    // There is only ever one instance of DBInfo in a store.
    //
    // When a database is first created, we use the initial values here
    // specified.  If the database is being brought up from an existing
    // backing store, then the read method below will provide the correct
    // values from the store.
    //
    private FOStoreDatabase fodb = null;

    /**
    * This is the OID of the DBInfo.
    */
    // See CLID.java: This relies on the facts that (a) CLID's less than 100
    // are reserved, and (b) the primitive types are final and cannot be
    // subclassed.
    private static final OID dbInfoOID = new OID(0);
    
    // Next available CLID.
    private CLID nextCLID = CLID.firstCLID;

    // For the OID of the DBClass corresponding to a given CLID.
    private static final int DBCLASS_UID = 0;

    // For the OID of the OID's which are of DBClass instances that are
    // subclasses of an OID's CLID's class.  Got that?  No?  Then see
    // GetExtentHandler.java.
    private static final int SUBCLASS_UID = 1;

    // For the OID of the extent of instances of the class indicated by a
    // CLID.
    private static final int EXTENT_UID = 2;

    // Reserve the first N UID's in each OID for future use.
    //
    // Note to maintainers: Make sure that this is greater than
    // LAST_RESERVED_UID.
    private static final int FIRST_CLASS_UID = 10;

    // Indexed by CLID: the value in a given position is the value of the
    // next available UID for that CLID.  That is, when we need a new
    // datastore OID for a particular CLID, we use that CLID's id value as an
    // index into this list, and extract the value there.  We use that value
    // as the UID part of the OID being created.  And we update the list
    // element by incrementing the value in the list by 1.
    private ArrayList nextUIDs = new ArrayList(CLID.firstCLID.getId());

    /** Set of extents that are currently modified and need to be stored when
     * a transaction commits.
     */
    // XXX This relies on the fact that transactions are serialized.  Once
    // that goes away, index a set of dirtyExtents HashSets by some kind of
    // transaction id.
    private HashSet dirtyExtents = new HashSet();

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /**
    * Create a new instance. 
    * Use this constructor when creating an instance for a brand new
    * database (as opposed to an existing database, in which we use the
    * Streamable-required default constructor, below).
    * @param fodb The FOStoreDatabase instance.
    * @exception FOStoreFatalInternalException Thrown when there is an attempt
    * to create a second DBInfo within a store.
    */
    DBInfo(FOStoreDatabase fodb) {
        if (logger.isDebugEnabled()) {
            logger.debug("DBInfo: new for " + fodb); // NOI18N
        }
        this.fodb = fodb;
        // Initialize nextUIDs for the reserved CLIDs.
        int size = nextCLID.getId();
        for (int i = 0; i < size; i++) {
            // FIRST_CLASS_UID number of uid's are reserved per-CLID.
            nextUIDs.add(i, new Long(FIRST_CLASS_UID));
        }
    }

    void store() throws FOStoreDatabaseException {
        fodb.put(dbInfoOID, this);
    }

    static DBInfo get(FOStoreDatabase db) throws FOStoreDatabaseException {
        DBInfo rc = (DBInfo)db.getIfExists(dbInfoOID);
        if (null == rc) {
            rc = new DBInfo(db);
            rc.fodb = db;
            rc.store();
        }
        rc.fodb = db;
        if (logger.isDebugEnabled()) {
            logger.debug("DBInfo.get: " + rc); // NOI18N
        }
        return rc;
    }

    //
    // The intent of the next 2 methods is to provide an OID which can be
    // used to store information about the class represented by the CLID.
    // ActivateClassHandler uses newClassOID to create an OID for storing
    // class metadata, GetClassHandler uses getDBClassOID to get that OID.
    //
    
    /**
    * Provide a new OID to represent a class.  Use this when activating a
    * class.
    * @return An OID for a class just now known to this store.
    */
    OID newClassOID() {
        // FIRST_CLASS_UID number of uid's are reserved per-CLID.
        nextUIDs.add(nextCLID.getId(), new Long(FIRST_CLASS_UID));

        OID rc = OID.create(nextCLID, DBCLASS_UID);

        nextCLID = nextCLID.next();

        if (logger.isDebugEnabled()) {
            logger.debug(
                "newClassOID: returning; nextCLID now=" + nextCLID); // NOI18N
        }

        return rc;
    }

    /**
    * Provides the OID which represents the given CLID's class.
    * @param clid The CLID for which the corresponding OID is needed.
    * @return The OID of the CLID.
    */
    static OID getDBClassOID(CLID clid) {
        return OID.create(clid, DBCLASS_UID);
    }

    /**
    * Provides the OID which at which is stored the ArrayList of the CLIDs of
    * of subclasses of the class corresponding to the CLID.
    * @param clid The CLID for which the corresponding OID is needed.
    * @return The OID of the ArrayList of CLIDs of the given CLID's
    * subclasses.
    */
    static OID getSubclassSetOID(CLID clid) {
        return OID.create(clid, SUBCLASS_UID);
    }

    /**
    * Provides the OID which represents extent of instances of objects all of
    * which have the given CLID.
    * @param clid CLID of extent to return.
    * @return OID of the extent of instances of CLID's class.
    */
    static OID getExtentOID(CLID clid) {
        return OID.create(clid, EXTENT_UID);
    }

    /**
    * @return Iterator of DBClass objects
    */
    Iterator getDBClasses() {
        if (logger.isDebugEnabled()) {
            logger.debug(
                "DBInfo.getDBClasses: first=" + CLID.firstCLID + // NOI18N
                ", next=" + nextCLID); // NOI18N
        }
        return new DBClassIterator(CLID.firstCLID, nextCLID);
    }

    class DBClassIterator implements Iterator {
        private int current;
        private final int finish;
        
        DBClassIterator(CLID start, CLID finish) {
            this.current = start.getId();
            this.finish = finish.getId();
        }
        
        public boolean hasNext() {
            return current < finish;
        }
        
        public Object next() {
            Object rc = null;
            try {
                rc = fodb.get(getDBClassOID(CLID.create(current++, false)));
            } catch (FOStoreDatabaseException ex) {
            }
            return rc;
        }

        public void remove() { }
    }

    //
    // Dirty extent handling
    //

    /**
     * Marks the given extent as dirty, so that it can later be stored.
     */
    boolean makeExtentDirty(DBExtent e) {
        return dirtyExtents.add(e);
    }

    /**
     * Stores all extents that have been marked dirty since the last time this
     * method was invoked.
     */
    void storeDirtyExtents() throws FOStoreDatabaseException {
        for (Iterator i = dirtyExtents.iterator(); i.hasNext();) {
            DBExtent dbExtent = (DBExtent)i.next();
            if (logger.isDebugEnabled()) {
                logger.debug("FOSCI.commit:" + dbExtent); // NOI18N
            }
            dbExtent.store(fodb);
        }
        clearDirtyExtents();
    }

    /**
     * Causes this DBInfo to forget about the dirty state of all extents
     * marked as dirty since the last time storeDirtyExtents was invoked.
     */    
    void clearDirtyExtents() {
        dirtyExtents.clear();
    }

    /**
    * @return Iterator of DBExtent objects
    */
    Iterator getExtents() {
        if (logger.isDebugEnabled()) {
            logger.debug(
                "DBInfo.getExtents: first=" + CLID.firstCLID + // NOI18N
                ", next=" + nextCLID); // NOI18N
        }
        return new ExtentIterator(CLID.firstCLID, nextCLID);
    }

    class ExtentIterator implements Iterator {
        private int current;
        private final int finish;
        
        ExtentIterator(CLID start, CLID finish) {
            this.current = start.getId();
            this.finish = finish.getId();
        }
        
        public boolean hasNext() {
            return current < finish;
        }
        
        public Object next() {
            Object rc = null;
            try {
                rc = fodb.get(getExtentOID(CLID.create(current++, false)));
            } catch (FOStoreDatabaseException ex) {
            }
            return rc;
        }

        public void remove() { }
    }
        
    /**
    * Provide a new OID for the given CLID.  Use this when creating an
    * instance in the store.  The OID will have its CLID part as per the
    * given CLID, and its UID part one greater than the last-created
    * newInstanceOID.
    * @param clid CLID for which a new OID is needed.
    * @return An OID for the CLID.
    * @exception FOStoreFatalInternalException thrown if the CLID is invalid.
    */
    OID newInstanceOID(CLID clid) {
        OID rc = null;
        int clidIndex = clid.getId();

        synchronized(nextUIDs) {
            if (nextUIDs.size() > clidIndex) {

                // Get the next-uid value, increment by one, store the new
                // next-uid.  Make an OID to return.
                Long UID = (Long)nextUIDs.get(clidIndex);
                long uid = UID.longValue();
                if (uid == OID.MAX_UID) {
                    throw new FOStoreFatalInternalException(
                        this.getClass(), "newInstance", // NOI18N
                        msg.msg("ERR_OutOfUIDs", // NOI18N
                                new Long(OID.MAX_UID)));
                }
                uid++;
                UID = new Long(uid);
                nextUIDs.set(clidIndex, UID);
                
                rc = OID.create(clid, uid);
            } else {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "newInstance", // NOI18N
                    msg.msg("ERR_NoUIDsForCLID", clid)); // NOI18N
            }
        }
        return rc;
    }

    /** Returns a human-readable description of this DBInfo.  If system
     * property "dbinfo.shortname" is true, then the description includes the
     * name of the database (e.g. 'foo'), otherwise it includes the complete
     * pathname of the database (e.g. '/bleem/foo').
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DBInfo '"); // NOI18N
        String name = fodb.getName();
        // Default is to provide full pathname of database; for testing the
        // short name is more appropriate.
        if (Boolean.getBoolean("dbinfo.shortname")) { // NOI18N
            int pos = name.lastIndexOf(File.separatorChar);
            if (pos > 0) {
                name = name.substring(pos + 1);
            }
        }
        sb.append(name).append("'\n");  // NOI18N
            
        sb.append("Next ").append(nextCLID.toString()).append("\n"); // NOI18N

        for (Iterator i = getExtents(); i.hasNext();) {
            DBExtent e = (DBExtent)i.next();
            CLID clid = e.getClassCLID();
            Long uid = (Long)nextUIDs.get(clid.getId());

            sb.append(
                "    class ").append(e.toString()).append(", "); // NOI18N
            sb.append(
                "next UID: ").append(uid.toString()).append(".\n"); // NOI18N
        }

        return sb.toString();
    }

    //
    // Implement Streamable
    //
    
    public DBInfo() {
        this(null);
    }
    
    public void write(OutputStream os) throws StorageException {
        DataOutputStream dos = new DataOutputStream(os);

        try {
            nextCLID.write(dos);

            int size = nextUIDs.size();
            dos.writeInt(size);
            for (int i = 0; i < size; i++) {
                Long clid = (Long)nextUIDs.get(i);
                dos.writeLong(clid.longValue());
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    public void read(InputStream is) throws StorageException {
        DataInputStream dis = new DataInputStream(is);
        try {
            nextCLID = CLID.read(dis);
            
            int size = dis.readInt();
            nextUIDs = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                nextUIDs.add(i, new Long(dis.readLong()));
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }
}
