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

/*
 * FOStoreServerConnectionImpl.java
 *
 * Created on June 7, 2001, 3:16 PM
 */

package org.apache.jdo.impl.fostore;

import java.io.File;
import java.io.IOException;
import java.io.DataInput;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;

import java.util.HashSet;
import java.util.Iterator;

import javax.jdo.JDOFatalInternalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;

/**
 *
 * @author  Craig Russell
 * @version 1.0
 */

 /** This class implements the FOStoreServerConnection contract.  It is used
  * in two modes: local and remote.  In local mode, the default constructor
  * is used.  In remote mode, the constructor that takes a Socket is used.
  * The connection after construction does not contain any information about
  * the data store.  After the login request is successfully executed, the
  * Database information is known.  Subsequent requests know the Database.
  */
// XXX This class needs to be split into 2: one which handles the
// socket-connected case, the other which handles the local case.
class FOStoreServerConnectionImpl implements FOStoreServerConnection {

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /** Used by server to write its replies.
     */
    private FOStoreOutput serverData = new FOStoreOutput();

    /** Used by server to read client's requests.
     */
    private FOStoreInput clientDataInput;

    /** The socket over which we communicate to the client.
     */
    private final Socket socket;

    /** Root in filesystem at which database will be created.
     */
    private final String root;
    
    /** The input stream from the socket.  Set after connect; reset at
     * socket.close().
     */
    private DataInputStream socketDataInputStream = null;
    
    /** The output stream from the socket.  Set after connect; reset at
     * socket.close().
     */
    private OutputStream socketOutputStream = null;

    /** The local database name.
     */
    private String dbname;
    
    /** The local database
     */    
    private FOStoreDatabase fodb;

    /** Indicates if releaseDatabase can really do so.
     */
    private boolean okToReleaseDatabase = true;
    
    /** Creates new FOStoreServerConnectionImpl for the local case.
     */
    public FOStoreServerConnectionImpl() {
        this.socket = null;
        this.root = null;
    }

    /** Creates new FOStoreServerConnectionImpl for the remote case.
     */
    public FOStoreServerConnectionImpl(Socket socket, String root) {
        this.socket = socket;
        this.root = root;
    }

    /**
     * Commits the work done in this connection.  The database must already
     * be connected.  Stores in the database (a) the extents that have been
     * changed and (b) the database's DBInfo. 
     */
    public void commit() throws FOStoreDatabaseException {
        if (logger.isDebugEnabled()) logger.debug("FOSCI.commit"); // NOI18N
        FOStoreDatabase db = getDatabase();

        DBInfo dbInfo = db.getDBInfo();
        dbInfo.storeDirtyExtents();
        dbInfo.store();
        db.commitChanges();
    }
    
    /**
     * Rolls back the work done in this connection.
     */
    public void rollback() throws FOStoreDatabaseException {
        FOStoreDatabase db = getDatabase();
        db.getDBInfo().clearDirtyExtents();
        db.rollbackChanges();
    }
    
    /**
     * Provides a Reply to which the server can write replies to the
     * client's requests.
     * @return Reply object for writing information about one reply.
     */
    public Reply createReply(RequestId requestId) throws IOException {
        return new Reply(requestId, serverData);
    }
    
    /**
     * Provides a DataInput from which the server can read the client's
     * requests.
     * @return DataInput for reading requests.
     */
    public FOStoreInput getInputFromClient() {
        return clientDataInput;
    }
    
    public void readInputFromClient() throws IOException {
        if (socketDataInputStream == null) {
            InputStream is = socket.getInputStream();
            socketDataInputStream = new DataInputStream (is);
        }
        if (logger.isDebugEnabled()) {
            logger.debug (
                "FOSCI.readInputFromClient available: " + // NOI18N
                socketDataInputStream.available());
        }

        int length = socketDataInputStream.readInt();
        byte[] buffer = new byte[length];
        socketDataInputStream.readFully(buffer);
        setClientInput( new FOStoreInput (buffer, 0, length));
    }
    
    /**
     * @see FOStoreServerConnection#sendToClient
     */
    public void sendToClient() throws IOException, FOStoreDatabaseException {
    }
    
    /**
     * Adds the extent to this connections set of extents that have been
     * changed during a transaction.  They will be put to the database when
     * sendToClient is invoked.
     */
    public boolean addExtent(DBExtent dbExtent) {
        boolean rc = getDatabase().getDBInfo().makeExtentDirty(dbExtent);
        if (logger.isDebugEnabled()) {
            logger.debug("FOSCI.addExtent to: " + this + // NOI18N
                           " dbExtent: " + dbExtent + // NOI18N
                           "add rc: "  + rc); // NOI18N
        }
        return rc;
    }
    
    /** Open the database if it exists, and verify the user authentication.
     * If the database does not exist, create it.
     */
    public void openDatabase (String dbname, String user, long timestamp,
                              byte[] secret, boolean create) 
        throws FOStoreDatabaseException, InterruptedException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("FOSCI.openDatabase " + dbname +  // NOI18N
            " user: " + user); // NOI18N
        }

        fodb = FOStoreDatabase.getDatabase(databasePath(dbname), create);
        this.dbname = dbname;
        fodb.verifyUserPassword(user, timestamp, secret);
    }

    /**
     * Provides the database to be used by the server using this connection.
     * @return A database.
     */
    public FOStoreDatabase getDatabase() {
        try {
            if (fodb == null) {
                 fodb = FOStoreDatabase.getDatabase(
                     databasePath(dbname), false);
            }
            return fodb;
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_GetDatabaseInterrupted"), ex); // NOI18N
        } catch (FOStoreDatabaseException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_GetDatabaseException"), ex); // NOI18N
        }
    }
        
    /**
     * Provides a DataOutput to which the server can write data for the
     * client.  This is not normally used for writing data associated with a
     * single reply (use createReply for that purpose).
     * @return FOStoreOutput for writing data to client.
     */
    public FOStoreOutput getOutputForClient() {
        return serverData;
    }
    
    /** Provides a DataInput from which the local client can read the server's
     * replies.  Reset for the next output request.
     */
    public DataInput getOutputFromServer() {
        DataInput di = new DataInputStream(
            new ByteArrayInputStream(
                serverData.getBuf(), 0, serverData.getPos()));
        
        serverData.reset(); // reset the output stream
        return di;
    }
    
    /** Writes the output to the client socket.  Reset for the next output
     * request. 
     */
    public void writeOutputToClient() throws IOException {
        if (socketOutputStream == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "FOSCI.writeOutputToClient getting socketOutputStream"); // NOI18N
            }

            socketOutputStream = socket.getOutputStream();
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "FOSCI.writeOutputToClient got socketOutputStream"); // NOI18N
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(
                "FOSCI.writeOutputToClient writing socketOutputStream: " + // NOI18N
                serverData.getPos() + " bytes"); // NOI18N
        }

        socketOutputStream.write(serverData.getBuf(), 0, serverData.getPos());
        serverData.reset(); // reset the output stream
    }
    
    public void setClientInput (FOStoreInput fi) {
        clientDataInput = fi;
    }
    
    public void processRequests() {
        RequestHandler.handleRequests(this);
    }
    
    /** Indicates whether or not releaseDatabase can actually release the
     * database.
     */
    public void setOkToReleaseDatabase(boolean ok) {
        if (logger.isDebugEnabled()) {
            logger.debug("FOSCI.setOk: " + ok); // NOI18N
        }
        this.okToReleaseDatabase = ok;
    }
    
    /** Release the database associated with this connection.
     */
    void releaseDatabase() throws InterruptedException {
        if (okToReleaseDatabase && fodb != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("FOSCI.releaseDatabase: releasing"); // NOI18N
            }
            FOStoreDatabase.releaseDatabase(fodb);
            fodb = null;
        }
    }

    /** Close the database associated with this connection.
     */
    public void closeDatabase() throws FOStoreDatabaseException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("FOSCI.closeDatabase " + dbname); // NOI18N
            }
            releaseDatabase();
            FOStoreDatabase.closeDatabase(databasePath(dbname));
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
            msg.msg("ERR_CloseDatabaseInterrupted"), ex); // NOI18N
        }
    }
    
    /** Close this connection; release all resources.
     */
    public void close() throws FOStoreDatabaseException {
        if (logger.isDebugEnabled()) {
            logger.debug("FOSCI.close " + dbname); // NOI18N
        }
        closeDatabase();
    }

    /** @return A String for the databse's pathnamne.  It is based on the
    * given dbname, plus the root, if one was given when this connection was
    * created.
    */
    private final String databasePath(String dbname) {
        String rc;
        if (null == root) {
            rc = dbname;
        } else {
            rc = root + File.separator + dbname;
        }
        return rc;
    }
}
