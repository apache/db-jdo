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
 * FOStoreClientConnectionImpl.java
 *
 * Created on June 7, 2001, 3:17 PM
 */

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

import java.net.URLConnection;
import java.net.URL;
import java.net.Socket;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the client side of a FOStore connection.
 *
 * @author  Craig Russell
 * @version 1.0
 */
abstract class FOStoreClientConnectionImpl extends URLConnection implements FOStoreClientConnection {

    /** Message to handle connection-to-connection messages (login).
     */
    Message message = new Message();
    
    FOStoreConnectionFactory connectionFactory;
    
    FOStoreConnectionId connectionId;
    
    /** Flag set while logging in.  This flag, when set, causes close() to
     * retain the connection instead of returning the connection to the pool.
     */
    private boolean loggingIn = true;

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /** Creates new FOStoreClientConnectionImpl */
    public FOStoreClientConnectionImpl(URL url) {
        super (url);
        if (logger.isDebugEnabled()) logger.debug("FOCCI<init>" +  // NOI18N
            " Host: " + url.getHost() + // NOI18N
            " Port: " + url.getPort() + // NOI18N
            " Path: " + url.getPath()); // NOI18N
    }

    /**
     * Return this connection to the connection pool.
     */
    public void close() throws IOException {
        if (!loggingIn) {
            if (logger.isDebugEnabled()) {
                logger.debug ("FOCCI.close: closing"); // NOI18N
            }
            connectionFactory.closeConnection(this);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug ("FOCCI.close: loggingIn; not closing"); // NOI18N
            }
        }
    }
        
    /** Log in to the datastore.  This will be done for both local and
     * remote connections.
     */
    protected void login() throws IOException {
        String path = localizePath(url.getPath());

        if (logger.isDebugEnabled()) {
            logger.debug("FOCCI:login " +  // NOI18N
                           " Database: " + path + // NOI18N
                           " User: " + connectionId.getUser() + // NOI18N
                           " Password: " + connectionId.getPassword() + // NOI18N
                           " Create: " + connectionId.getCreate()); // NOI18N
        }
        LoginRequest logreq =
            new LoginRequest(
                message, connectionFactory.getPMF(),
                path, connectionId.getUser(), connectionId.getPassword(),
                connectionId.getCreate());
        logreq.doRequest();
        // false => NOT ok to close (this) connection
        message.processInStore(this, false);
        loggingIn = false;
    }
        
    public void setConnectionFactory(FOStoreConnectionFactory cf) {
        this.connectionFactory = cf;
    }
    
    /** Get the connection Factory used to create this connection.  Connections
     * are pooled based on this connection Factory.
     */
    public FOStoreConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
    
    /** Set the connection id used to create this connection.  This is used
     * to determine which connection pool is used.
     */
    public void setConnectionId (FOStoreConnectionId id) {
        if (logger.isDebugEnabled()) {
            logger.debug("FOCCI.setConnectionId(): " + // NOI18N
            " URL: " + id.getUrl() + // NOI18N
            " User: " + id.getUser() + // NOI18N
            " Password: " + id.getPassword() + // NOI18N 
            " Create: " + id.getCreate()); // NOI18N
        }
        this.connectionId = id;
    }
    
    /** Get the connection id used to create this connection.  The id
     * encapsulates URL, user, and password.  Connections
     * are pooled based on this connection id.
     */
    public FOStoreConnectionId getConnectionId() {
        return connectionId;
    }
    
    /** 
     * @return The path, modified as required for the kind of connection.
     */
    abstract protected String localizePath(String path);
}
