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
 * FOStoreRemoteClientImpl.java
 *
 * Created on June 18, 2001, 4:44 PM
 */

package org.apache.jdo.impl.fostore;

import java.net.URL;
import java.net.Socket;

import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Represents a connection to a store that runs in a JVM separate from that of
 * a client.
 * <p>
 * This class is <code>public</code> so that clients can access the value of
 * <code>DEFAULT_PORT</code>.
 *
 * @author  Craig Russell
 * @version 1.0
 */
public class FOStoreRemoteConnection extends FOStoreClientConnectionImpl {

    /** The default port number
     */
    public final static int DEFAULT_PORT = 9919;
    
    /** The socket used to communicate with the remote server.
     */    
    private Socket socket;
    
    /** The DataInputStream from the socket for replies from the server.
     */
    private DataInput serverReplies;
    
    /** The DataOutputStream from the socket for requests for the server.
     */
    private DataOutputStream clientRequests;
    
    /** Creates new FOStoreRemoteConnection
     * @param url the url of the server.
     */
    FOStoreRemoteConnection(URL url) {
        super (url);
    }

    /** Connect to the data store.  The user name and password are stored in
     * the connectionId.
     * @throws IOException if a network error occurs
     */    
    public void connect() throws IOException {
        String host = url.getHost();
        int port = url.getPort();
        if (port == -1)
            port = DEFAULT_PORT;
        if (logger.isDebugEnabled()) logger.debug("FOCCI:connect " +  // NOI18N
            " Host: " + host + // NOI18N
            " Port: " + port); // NOI18N
        socket = new Socket (host, port);
        // we are connected; now send our greeting (login).
        login();
    }
    
    /**
     * Provides DataInput from which the client can read replies from
     * the server.
     * @return DataInput from which the client can read replies.
     * @throws IOException if any problems.
     */
    public DataInput getInputFromServer() throws IOException {
        if (serverReplies == null) {
            serverReplies = new DataInputStream (socket.getInputStream());
        }
            return serverReplies;
    }
    
    /** Write bytes to the store.  This is the only method to actually write
     * data bytes from the Message to the server.  As soon as the remote side
     * has read the bytes, it processes the message and sends the reply.
     * @param buffer the data buffer
     * @param offset the offset within the buffer
     * @param length the number of bytes to write
     * @throws IOException if any problems.
     */
    public void sendToStore (byte[] buffer, int offset, int length) throws IOException {
        if (clientRequests == null) {
            OutputStream os = socket.getOutputStream();
            clientRequests = new DataOutputStream (os);
        }
        clientRequests.writeInt (length - offset); // receiver needs to allocate a buffer this big
        clientRequests.write (buffer, offset, length);
    }
        
    /** Close the database associated with this connection.  This closes the socket
     * associated, which causes the remote database to be closed.
     * @throws IOException if any problems with the socket.
     * @throws FOStoreDatabaseException if any problems with the database (not used here).
     */
    public void closeDatabase() throws IOException, FOStoreDatabaseException {
        socket.close();
    }

    /**
     * @return The path as given, with the leading '/' removed if there is one.
     */
    protected String localizePath(String path) {
        // Use the correct path, not what Java gives you (see RFC1738)
        if (path.startsWith("/")) { // NOI18N
            path = path.substring(1);
        }
        return path;
    }
}
