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

/**
* A connection as used by a server/store.  It provides a means of getting the
* client's input, and of writing reply data back to the client.
*
* @author Dave Bristor
*/
interface FOStoreServerConnection extends FOStoreConnection {
    /**
    * Provides a DataInput from which the server can read the client's
    * requests.
    * @return DataInput for reading requests.
    */
    public FOStoreInput getInputFromClient();

    /**
    * Provides a DataOutput to which the server can write data for the
    * client.  This is not normally used for writing data associated with a
    * single reply (use createReply for that purpose).
    * @return FOStoreOutput for writing data to client.
    */
    public FOStoreOutput getOutputForClient();

    /**
    * Provides a Reply to which the server can write replies to the
    * client's requests.
    * @return Reply object for writing information about one reply.
    */
    public Reply createReply(RequestId requestId) throws IOException;

    /**
    * Adds the extent to this connections set of extents that have been
    * changed during a transaction.
    */
    public boolean addExtent(DBExtent dbExtent);

    /**
    * Commits the work done in this connection.
    */
    public void commit() throws FOStoreDatabaseException;

    /**
    * Rolls back the work done in this connection.
    */
    public void rollback() throws FOStoreDatabaseException;

    /**
    * Sends all the reply data to the client.
    */
    public void sendToClient() throws IOException, FOStoreDatabaseException;

    /**
    * Provides the database to be used by the server using this connection.
    * @return A database.
    */
    public FOStoreDatabase getDatabase();
    
    /** Set client data stream.  Only used in local case.
     */
    public void setClientInput (FOStoreInput in) throws IOException;
    
    /** Process requests from client.
     */
    public void processRequests ();
    
    /** Log in to the database.
     */
    public void openDatabase(String dbname, String user, long timestamp, byte[] secret, boolean create)
        throws java.lang.InterruptedException, FOStoreDatabaseException;
    
    /** Provide the output from the server to the client reply handler.
     */
    public DataInput getOutputFromServer();
    
    /** Write the output from the server to the socket
     */
    public void writeOutputToClient() throws IOException;
    
    /** Read the input stream from the socket.
     */
    public void readInputFromClient() throws IOException;

    /** Close the database.  Each open connection will perform this operation;
     * only the last open connection to be closed will actually close the 
     * database.
     */
    public void closeDatabase() throws FOStoreDatabaseException;

    /** Indicates whether or not it is OK to release a database after a
     * message's contents are processed.
     */
    // XXX TBD The server connection and implementation  needs refactoring
    public void setOkToReleaseDatabase(boolean ok);
}
