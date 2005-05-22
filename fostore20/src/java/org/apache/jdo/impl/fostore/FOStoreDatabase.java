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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.util.Set;
import java.util.HashMap;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOFatalDataStoreException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;
import org.apache.jdo.util.Pool;
import org.netbeans.mdr.persistence.Storage;
import org.netbeans.mdr.persistence.SinglevaluedIndex;
import org.netbeans.mdr.persistence.StorageBadRequestException;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.Streamable;

/**
* File/Object Store Database, using an underlying Btree implementation.
*
* @author Dave Bristor
*/
class FOStoreDatabase {
    
    /** We manage a map of pools of databases by database name.
     */
    private static final HashMap databaseMap = new HashMap();
    
    private static final int poolSize = 1;
    
    // We delegate all calls to this storage.  Not final, because for
    // rollback's sake we have to close and then make a new storage.
    private FOStoreBtreeStorage storage;

    // Map from provisional IDs to real, datastore-created, durable IDs.
    //
    // XXX TBD Remote: Provisional CLID's are per client (PMF).
    // That is because each client will independently generate its
    // own set of provisional ID's.  One way to manage this might be for
    // each client to send a "hello" message when it first connects, with
    // something like it's IP address and process id.  Future communications
    // from that same client will contain that same info.  A "goodbye" message
    // would be nice, too, so that the store can remove any tables that will
    // not ever be used again.
    private final HashMap provisionalCLIDs = new HashMap();

    // Same as above, but for OIDs.
    private final HashMap provisionalOIDs = new HashMap();
    
    /** Count the number of connections open on this database.  When the last 
     * connection closes, then really close the database.
     */
    private int openConnections = 0;
    
    private DBInfo dbInfo = null;
    /** Pool for this database.
     */
    private Pool pool = null;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /** Find an open database of the given name.  If it is not already open,
     * open it and put it into the pool.
     */
    public synchronized static FOStoreDatabase getDatabase (String dbname, 
                                                            boolean create) 
            throws InterruptedException, FOStoreDatabaseException {
        Pool pool = (Pool) databaseMap.get(dbname);
        FOStoreDatabase db = null;
        if (pool == null) {
            try {
                db = new FOStoreDatabase (dbname, create);
            } catch (FOStoreDatabaseException fode) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "FODB:getDatabase exception for new database " + // NOI18N
                        dbname + " create: " + create); // NOI18N
                }
                throw (fode);
            }
            pool = new Pool (poolSize);
            databaseMap.put (dbname, pool);
            pool.put (db);
            db.setPool(pool);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("FODB:getDatabase found pool for database " + dbname); // NOI18N
        }
        db = (FOStoreDatabase) pool.get();
        return db;
    }
    
    /** Release the database for other connections to use.
     */
    public synchronized static void releaseDatabase (FOStoreDatabase db)
        throws InterruptedException {

        db.pool.put (db);
    }
        
    /**
    * Create an FODatabase and create/open the files.
    * @param name name of database, which is used to name the files
    * @param isNew true if the database is being created
    */
     FOStoreDatabase(String name, boolean isNew)
        throws FOStoreDatabaseException {
            // first verify that the directory exists
            // if not, there is no way to continue
            // Note, the filename might include a mixture of \ and / as
            // path separator, make them all / for the lookup
            int slashIndex = name.replace('\\', '/').lastIndexOf('/');
            if (slashIndex != -1) { // at least one / in pathname
                String dirName = name.substring(0, slashIndex);
                File dir = new File(dirName); // NOI18N
                if (!existsFile(dir)) {
                    throw new JDOFatalDataStoreException (
                        msg.msg("ERR_DirectoryDoesNotExist", name)); // NOI18N
                }
            }
            // XXX the DBInfo is not a singleton; change this code
            // and DBInfo to support multiple Databases in one VM.
        if (isNew) {
            // We must remove a database if one does exist.  This means delete
            // both the .btd and .btx files.
            String extensions[] = {"btd", "btx"}; // NOI18N
            boolean found = false;
            for (int i = 0; i < extensions.length; i++) {
                File f = new File(name + "." + extensions[i]); // NOI18N
                if (existsFile(f)) {
                    if (! found) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("FODB: Deleting existing database " +
                                           name); // NOI18N
                        }
                        found = true;
                    }
                    deleteFile(f);
                }
            }
            storage = createBtreeStorage(name, true);
            dbInfo = new DBInfo(this);
            dbInfo.store();
            commitChanges();
            if (logger.isDebugEnabled()) {
                logger.debug("FODB: Created database " + name); // NOI18N
            }
        } else {
            storage = createBtreeStorage(name, false);
            dbInfo = DBInfo.get(this);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "FODB: Opened existing database " + name); // NOI18N
            }
        }
    }

    /** Verify that this user is authorized to use this database.
     */
    public void verifyUserPassword (String user, long timestamp, byte[] secret) {
        // XXX look up Authentication objects and verify this user is authorized.
        addConnection();
    }
    
    /** Get the DBInfo for this database.
     */
    public DBInfo getDBInfo() {
        return dbInfo;
    }
    
    /** Associates the specified value with the specified key in this index.
    * @return true if there was an item in this index that was associated
    * with the key prior to this call
    * @param key
    * @param value
    * @throws FOStoreDatabaseException
    */
    public boolean put(OID key, Object value) throws FOStoreDatabaseException {
        try {
            Object keyValue = key.keyValue(storage);
// Uncomment the next lines to simulate a fatal error during login.
//            if (null != keyValue)
//                throw new NullPointerException("fodb: NPE on purpose"); // YYY
            return storage.getPrimaryIndex().put(keyValue, value);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }

    /** Replaces the original value associated with the specified key in this
    * index with new value. If no value was associated with this key prior
    * to this call StorageBadRequestException is thrown.
    * @param key
    * @param value
    * @throws FOStoreDatabaseException
    */
    public void replace(OID key, Object value)
        throws FOStoreDatabaseException {

        try {
            Object keyValue = key.keyValue(storage);
            storage.getPrimaryIndex().replace(keyValue, value);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }
        
    /** Returns the value to which this index maps the specified key.
    * StorageBadRequestException is thrown if there is no value for the key.
    * @return value associated with specified key
    * @param key
    * @throws FOStoreDatabaseException
    */
    public Object get(OID key)
        throws FOStoreDatabaseException {

        try {
            Object keyValue = key.keyValue(storage);
            return storage.getPrimaryIndex().get(keyValue);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }

    /** Returns the value to which this index maps the specified key
    * or null if there is no value for this key.
    * @return value associated with specified key or null
    * @param key
    * @throws FOStoreDatabaseException
    */
    public Object getIfExists (OID key) throws FOStoreDatabaseException {
        try {
            Object keyValue = key.keyValue(storage);
            return storage.getPrimaryIndex().getIfExists(keyValue);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }
    
    /** Returns the unique name of the index in the Storage.
    * @return The name of this index.
    */
    public String getName()  {
        return storage.getName();
    }

    /** Returns a set view of the keys contained in this index.
    * Returned set is read only and may not be modified.
    * @return keys contained in this index
    * @throws FOStoreDatabaseException
    */
    public Set keySet() throws FOStoreDatabaseException {
        try {
            return storage.getPrimaryIndex().keySet();
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }

    /** Adds the specified value to values associated in this index with the
    * specified key. If the index puts limit on number of values associated
    * with one key and adding value would break this limit, it thorows
    * StorageBadRequestException.
    * @param key
    * @param value
    * @throws FOStoreDatabaseException
    */
    public void add(OID key, Object value) throws FOStoreDatabaseException {
        try {
            Object keyValue = key.keyValue(storage);
            storage.getPrimaryIndex().add(keyValue, value);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }
    
    /** Removes all values assosiated in the index with specified key.
    * @return true if this index changed as a result of this call
    * @param key
    * @throws FOStoreDatabaseException
    */
    public boolean remove (OID key) throws FOStoreDatabaseException {
        try {
            Object keyValue = key.keyValue(storage);
            return storage.getPrimaryIndex().remove(keyValue);
        } catch (StorageException ex) {
            throw new FOStoreDatabaseException(ex);
        }
    }

    public void commitChanges() throws FOStoreDatabaseException {
        if (logger.isDebugEnabled()) logger.debug("FODB.commitChanges"); // NOI18N

        // Need to have privileges to perform this operation.
        final FOStoreBtreeStorage storage1 = storage;
        try {
            AccessController.doPrivileged (
                new PrivilegedExceptionAction () {
                    public Object run () throws StorageException {
                        storage1.commitChanges();
                        return null;
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            throw new FOStoreDatabaseException(
                (StorageException)ex.getException());
        }

    }

    /** Increment the openConnections to allow proper close when the last
     * connection closes the database.
     */
    public synchronized void addConnection() {
        openConnections++;
    }
    
    /** Decrement the open connections counter and close the database when
     * it reaches zero.
     */
    public synchronized void close() throws FOStoreDatabaseException {
        if (--openConnections > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("FODB.close: " + getName() +  // NOI18N
                               " count still " + openConnections); // NOI18N
            }
            try {
                releaseDatabase(this);
            } catch (InterruptedException ex) {
                // ignore
            }
            return;
        }
        // count has reached zero.  Now really close the database.
        if (logger.isDebugEnabled()) {
            logger.debug(
                "FODB.close: count reached zero." + getName()); // NOI18N
        }

        closeBtreeStorage(storage);
        databaseMap.remove(getName());
        pool = null;
    }
    
    public static synchronized void closeDatabase (String dbname)
        throws FOStoreDatabaseException, InterruptedException {

        Pool pool = (Pool) databaseMap.get(dbname);
        if (pool == null) {
            throw new JDOFatalDataStoreException (
                msg.msg("ERR_CloseDatabase", dbname)); // NOI18N
        }
        FOStoreDatabase fodb = (FOStoreDatabase) pool.get();
        fodb.close();
    }

    public void rollbackChanges() throws FOStoreDatabaseException {
        if (logger.isDebugEnabled()) logger.debug("FODB.rollbackChanges"); // NOI18N

        // The btree doesn't *really* rollback.  But closing it and
        // getting a new one works fine.
        final String name = storage.getName();
        closeBtreeStorage(storage);
        
        storage = createBtreeStorage(name, false);
        dbInfo = DBInfo.get(this);
    }

    static Block createBlock(FOStoreInput in, int length) {
        return new Block(in, length);
    }
    
    /**
    * Establishes a mapping in this database from provisional to real
    * CLIDs.  The mapping lasts as long as the server is running, that is, it
    * is not durable.
    * @param pCLID A provisional CLID.
    * @param rCLID The non-provisional, datastore CLID corresponding to
    * provCLID.
    * @exception JDOFatalException Thrown if the given CLID is not provisional.
    */
    // If you change this code, please see also method of same name in FOStorePMF.
    public synchronized void mapProvisionalCLIDToReal(
        CLID pCLID, CLID rCLID) {

        if (! pCLID.isProvisional()) {
            throw new JDOFatalException(msg.msg("ERR_CLIDNotProv", pCLID)); // NOI18N
        }
        if (null == provisionalCLIDs.get(pCLID)) {
            provisionalCLIDs.put(pCLID, rCLID);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(
                "FODB.mapProvCLIDToReal: " + pCLID + " -> " + rCLID); // NOI18N
        }
    }

    /**
    * Provides a datastore CLID corresponding to the given provisional CLID.
    * @param provCLID A provisional CLID for which there 
    * @return A non-provisional, datastore CLID corresponding to provCLID, or
    * null if the given provCLID is not mapped to a datastore CLID.
    * @exception JDOFatalException Thrown if the given CLID is not provisional.
    */
    // If you change this code, see method of same name in FOStorePMF.
    public CLID getRealCLIDFromProvisional(CLID provCLID) {
        if (! provCLID.isProvisional()) {
            throw new JDOFatalException(msg.msg("ERR_CLIDNotProv", provCLID)); // NOI18N
        }
        return (CLID)provisionalCLIDs.get(provCLID);
    }

    /**
    * Establishes a mapping in this database from provisional to real
    * OIDs.  The mapping lasts as long as the server is running, that is, it
    * is not durable.
    * @param pOID A provisional OID.
    * @param rOID The non-provisional, datastore OID corresponding to
    * pOID.
    * @exception JDOFatalException Thrown if the given OID is not provisional.
    */
    // If you change this code, please see also method of same name in FOStorePMF.
    synchronized void mapProvisionalOIDToReal(
        OID pOID, OID rOID) {

        if (! pOID.isProvisional()) {
            throw new JDOFatalException(msg.msg("ERR_OIDNotProv", pOID)); // NOI18N
        }
        if (null == provisionalOIDs.get(pOID)) {
            provisionalOIDs.put(pOID, rOID);
        }
    }

    /**
    * Provides a datastore OID corresponding to the given provisional OID.
    * @param pOID A provisional OID for which there 
    * @return A non-provisional, datastore OID corresponding to pOID or null
    * if the given pOID is not mapped to a datastore OID.
    * @exception JDOFatalException Thrown if the given OID is not provisional.
    */
    // If you change this code, see method of same name in FOStorePMF.
    OID getRealOIDFromProvisional(OID pOID) {
        if (! pOID.isProvisional()) {
            throw new JDOFatalException(msg.msg("ERR_OIDNotProv", pOID)); // NOI18N
        }
        return (OID)provisionalOIDs.get(pOID);
    }
    
    private void setPool (Pool pool) {
        this.pool = pool;
    }

    /**
     * Helper method to create a BtreeStorage. This method excutes the
     * storage operation in a doPrivileged block and handles possible
     * exceptions.  
     * @param name name of database, which is used to name the files
     * @param isNew true if the database is being created
     * @return new database
     * @exception FOStoreDatabaseException wraps a StorageException thrown
     * by the create call.
     * @exception JDOFatalUserException wraps a possible SecurityException.
     */
    private FOStoreBtreeStorage createBtreeStorage(final String name, 
                                                   final boolean isNew)
        throws FOStoreDatabaseException, JDOFatalUserException
    {
        try {
            return (FOStoreBtreeStorage) AccessController.doPrivileged (
                new PrivilegedExceptionAction () {
                    public Object run () throws StorageException {
                        return new FOStoreBtreeStorage(name, isNew);
                    }
                });
        } catch (SecurityException ex) {
            throw new JDOFatalUserException(msg.msg( 
                "EXC_SecurityExceptionOnCreateBtreeStorage"), ex); //NOI18N
        } catch (PrivilegedActionException ex) {
            throw new FOStoreDatabaseException(
                (StorageException)ex.getException());
        } 
    }

    /** 
     * Helper method to close a BtreeStorage. 
     * @param storage the database to be closed
     * @exception FOStoreDatabaseException wraps a StorageException thrown
     * by the close call.
     */
    private void closeBtreeStorage(final FOStoreBtreeStorage storage)
        throws FOStoreDatabaseException
    {
        try {
            AccessController.doPrivileged (
                new PrivilegedExceptionAction () {
                    public Object run () throws StorageException {
                        storage.shutDown();
                        storage.close();
                        return null;
                    }
                });
        } catch (SecurityException ex) {
            throw new JDOFatalUserException(msg.msg( 
                "EXC_SecurityExceptionOnCloseBtreeStorage"), ex); //NOI18N
        } catch (PrivilegedActionException ex) {
            throw new FOStoreDatabaseException(
                (StorageException)ex.getException());
        } 
    }

    /** 
     * Helper method to check whether a file exists. This method delegates
     * to File.exists and handles possible SecurityExceptions.
     * @param file the file to be checked
     * @return <code>true</code> if the specified file exists.
     */ 
    private boolean existsFile(File file) {
        try {
            return file.exists();
        } catch (SecurityException ex) {
            throw new JDOFatalUserException(msg.msg(
                "EXC_CannotReadFile", file.getName()), ex); //NOI18N
        }
    }

    /** 
     * Helper method to delete a file. This method delegates to File.delete
     * and handles possible SecurityExceptions.
     * @param file the file to be deleted
     */
    private void deleteFile(File file) {
        try {
            file.delete();
        } catch (SecurityException ex) {
            throw new JDOFatalUserException(msg.msg(
                "EXC_CannotDeleteFile", file.getName()), ex); //NOI18N
        }
    }
}
