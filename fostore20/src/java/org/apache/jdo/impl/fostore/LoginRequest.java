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

/*
 * Request to login to a database.
 *
 * LoginRequest.java
 *
 * Created on June 7, 2001, 3:28 PM
 */

package org.apache.jdo.impl.fostore;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.util.I18NHelper;


/**
 * Request to login to a database.
 * @author  Craig Russell
 * @version 1.0
 */
class LoginRequest extends AbstractRequest {
    /** The database name from the PMF URL property.
     */    
    String dbname;
    
    /** The user from the PMF user property.
     */    
    String user;
    
    /** The password from the PMF password property.
     */    
    String password;
    
    /** This is the Date.getTime() of the time the request
     * was created.  It is used to construct the shared secret
     * to verify the password at the server side, without
     * transmitting the password in clear.
     */    
    long timestamp;
    
    /** The secret constructed from the user, timestamp, and password.
     */    
    byte[] secret;
    
    /** A flag telling whether to create the database
     */
    boolean create;
    
    /** Creates new LoginRequest
     * @param m the Message
     * @param pmf the PersistenceManagerFactory
     * @param user the user
     * @param password the password
     */
    public LoginRequest(Message m, FOStorePMF pmf, String dbname, String user, String password, boolean create) {
        super (m, pmf);
        this.dbname = dbname;
        this.user = user;
        this.password = password;
        this.create = create;
        timestamp = new Date().getTime();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5"); // NOI18N
            md.update (dbname.getBytes("UTF-8")); // NOI18N
            md.update (user.getBytes("UTF-8")); // NOI18N
            md.update ((Long.toString(timestamp)).getBytes("UTF-8")); // NOI18N
//            md.update (password.getBytes("UTF-8")); // XXX add back when database can look up password
            this.secret = md.digest(); 
        } catch (NoSuchAlgorithmException nsae) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_NoSuchAlgorithmException", dbname, user), // NOI18N
                new Exception[]{nsae});
        } catch (UnsupportedEncodingException uee) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_UnsupportedEncodingException", dbname), // NOI18N
                new Exception[]{uee});
        }
    }

    /**
     * Subclasses must implement in this method the actual writing of their
     * Request type-specific data.
     * @throws IOException if any errors constructing the stream
     */
    protected void doRequestBody() throws IOException {
        //
        // The format of this request is (aside from the request header):
        //
        // dbname: the database name property (part of the URL) of the PMF
        // user: the user property of the PMF
        // timestamp: the current Date in milliseconds
        // XXX change the following to be a shared secret based on the password
        // password: the password property of the PMF 
        // secret.length: the length of the secret
        // secret: the secret as a byte array

        if (logger.isDebugEnabled()) logger.debug("LoginRequest.dRB"); // NOI18N
        out.writeUTF (dbname);
        out.writeUTF (user);
        out.writeLong (timestamp);
        out.writeInt (secret.length);
        out.write (secret);
        out.writeBoolean (create);
    }
    
    /**
     * Processes the results of the effect of the request in the store.  To be
     * invoked after the store has processed the request, and has returned
     * information about that request, such as its status and any accompanying
     * data.
     * @param in
     * @param length
     * @param status Indication as to the success, failure, etc. of the
     * request as handled by the store.
     * @throws IOException
     */
    public void handleReply(Status status, DataInput in, int length) throws IOException {
        if (logger.isDebugEnabled()) logger.debug("LoginRequest.hR"); // NOI18N

        if (!status.equals (Status.OK)) {
            throw new JDODataStoreException (msg.msg("EXC_CannotLogin")); // NOI18N
        }
    }
}
