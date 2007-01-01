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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.net.URL;
import java.net.URLConnection;

import javax.jdo.JDOUserException;

/**
* Implements the client and server/store connections for the case in which
* the client and store are running in the same address space.
*
* @author Dave Bristor
*/
class FOStoreLocalConnection extends FOStoreClientConnectionImpl {

    /** The server that implements the requests
     */
    private FOStoreServerConnection server = new FOStoreServerConnectionImpl();

    /**
    * Provides a connection to the database, using the URL support in 
    * the superclass.
    */
    FOStoreLocalConnection(URL url) {
        super(url);
    }

    /**
     * @see FOStoreClientConnection#getInputFromServer
     */
    public DataInput getInputFromServer() throws IOException {
        return server.getOutputFromServer();
    }
    
    /** Write bytes to the store.  The data is used to construct a FOStoreInput
     * for the server to use; then the server is called to process the requests.
     */
    public void sendToStore(byte[] buffer, int offset, int length) throws IOException {
        if (logger.isDebugEnabled()) logger.debug("FOLC.wTS: " +  // NOI18N
                                             (length - offset));
        server.setClientInput (new FOStoreInput (buffer, offset, length));
        server.processRequests();
    }
 
    /** This connects to the data store, and verifies the user name and password..
     */
    public void connect() throws IOException {
        login();
    }
    
    /** Close the database associated with this connection.  This closes the local
     * server, which causes the remote database to be closed.
     */
    public void closeDatabase() throws FOStoreDatabaseException {
        server.closeDatabase();
    }
    
    /**
     * @return The <code>path</code> as given.
     */
    protected String localizePath(String path) {
        return path;
    }
}
