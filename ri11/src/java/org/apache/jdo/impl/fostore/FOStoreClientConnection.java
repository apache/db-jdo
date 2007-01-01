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

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents the connection as seen by the client.  This contains two types
 * of interface: that used by the ConnectionFactory, and that used by the
 * Message.  It provides a means for managing the data sent and received
 * from the store, as well as for being managed in a pool.
 *
 * @author Dave Bristor
 */
interface FOStoreClientConnection extends FOStoreConnection {

    /** Connect to the data source.
     */
    public void connect() throws IOException;
    
    /**
    * Provides DataInput from which the client can read replies from
    * the server.
    * @return DataInput from which the client can read replies.
    */
    public DataInput getInputFromServer() throws IOException;

    /**
     * Indicates that the client has finished writing.  The data is sent to the
     * server and processed there.
     */
    public void sendToStore(byte[] buffer, int start, int length) throws IOException;

    /**
     * Put this connection back into the connection pool managed by the 
     * ConnectionFactory.
     */
    public void close() throws IOException;
    
    /** Set the connection id used to create this connection.  This is used
     * to determine which connection pool is used.
     */
    public void setConnectionId (FOStoreConnectionId connectionId);
    
    /** Get the connection id used to create this connection.  Connections
     * are pooled based on this connection id.
     */
    public FOStoreConnectionId getConnectionId();
    
    /** Set the connection id used to create this connection.  This is used
     * to determine which connection pool is used.
     */
    public void setConnectionFactory (FOStoreConnectionFactory connectionFactory);
    
    /** Get the connection Factory used to create this connection.  Connections
     * are pooled based on this connection Factory.
     */
    public FOStoreConnectionFactory getConnectionFactory();
    
    /** Close the database associated with this connection.  In the remote case,
     * close the socket.
     */
    public void closeDatabase() throws IOException, FOStoreDatabaseException ;
    
}
