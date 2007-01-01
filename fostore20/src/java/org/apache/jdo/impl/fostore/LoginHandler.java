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
 * Handler for LoginRequests.
 *
 * LoginHandler.java
 *
 * Created on June 7, 2001, 4:16 PM
 */

package org.apache.jdo.impl.fostore;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOFatalDataStoreException;

import org.apache.jdo.util.I18NHelper;


/**
 * Handler for LoginRequests.
 * @author  Craig Russell
 * @version 1.0
 */
class LoginHandler extends RequestHandler {
    /** the database name
     */    
    String dbname;
    
    /** the user of the database
     */    
    String user;
    
    /** a pseudo random number
     */    
    long timestamp; 
    
    /** a message digest which is a shared secret
     */    
    byte[] secret;
    
    /** a flag to tell whether to create the database
     */
    boolean create;

    /** Construct an instance of the LoginHandler to service this request.
     * @param reply the reply for the request
     * @param length the length of the request
     * @param con the FOStoreServerConnection with the request
     */    
    private LoginHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    /** the factory used to create the handler for this request
     */    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new LoginHandler(reply, length, con);
            }};

    /** Process the request by analyzing the database and user login information
     * from the request buffer.  This will create the database if needed, open
     * it, and verify the user and password.
     *
     * @throws IOException if any problems
     * @return null; there is no need for a finisher.
     */            
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {
        DataInput in = con.getInputFromClient();
        // read the utf encoded user and password for verification.
        dbname = in.readUTF();
        user = in.readUTF();
        timestamp = in.readLong();
        int secretSize = in.readInt();
        secret = new byte[secretSize];
        in.readFully(secret);
        create = in.readBoolean();
        try {
            con.openDatabase(dbname, user, timestamp, secret, create);
        } catch (Exception e) {
            throw new FOStoreLoginException(dbname, user, e);
        }
        reply.setStatus(Status.OK);
        return null;
    }
}
