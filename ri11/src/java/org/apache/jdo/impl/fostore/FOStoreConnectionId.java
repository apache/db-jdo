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
 * FOStoreConnectionId.java
 *
 * Created on June 15, 2001, 1:30 PM
 */

package org.apache.jdo.impl.fostore;

/**
 * Represents the information required to connect to a database.
 * @author  Craig Russell
 * @version 1.0
 */
class FOStoreConnectionId {
    
    /** The URL of this connection.  Must not be null.
     */    
    private String url;
    
    /** The user id of this connection.  May be null.
     */    
    private String user;
    
    /** The password of this connection.  May be null.
     */    
    private String password;
    
    /** The flag telling whether to create.
     */
    private boolean create;

    /** Creates new FOStoreConnectionId.
     * @param url the URL of the connection.
     * @param user the user id of the connection.
     * @param password the password of the connection.
     * @param create the flag whether to create the database.
     */
    public FOStoreConnectionId(String url, String user, String password, boolean create) {
        if (url == null) {
            throw new NullPointerException();
        }
        this.url = url;
        this.user = user;
        this.password = password;
        this.create = create;
    }
    

    /** Creates new FOStoreConnectionId.
     * @param url the URL of the connection.
     * @param user the user id of the connection.
     * @param password the password of the connection.
     */
    public FOStoreConnectionId(String url, String user, String password) {
        if (url == null) {
            throw new NullPointerException();
        }
        this.url = url;
        this.user = user;
        this.password = password;
        this.create = false;
    }
    
    public void setUrl (String url) {
        if (url == null) {
            throw new NullPointerException();
        }
        this.url = url;
    }
    
    public String getUrl () {
        return url;
    }
    
    public void setUser (String user) {
        this.user = user;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public String getPassword () {
        return password;
    }
    
    public void setCreate (boolean create) {
        this.create = create;
    }
    
    public boolean getCreate() {
        return create;
    }

    /** Combine the hashCodes of URL, user, and password.
     * @return the combined hashCode.
     * Note that the create flag is not part of the hashCode.
     */    
    public int hashCode () {
        int urlHashCode = url.hashCode();
        int userHashCode = user==null?0:user.hashCode();
        int passwordHashCode = password==null?0:password.hashCode();
        return urlHashCode + userHashCode + passwordHashCode;
    }
    
    /** Returns true if this represents the same URL,
     * user, and password as the other.
     * @param other another connection id.
     * @return true if this represents the same URL,
     * user, and password as the other.
     * Note that the create flag is not part of the identity.
     */    
    public boolean equals (Object other) {
        if (this.getClass() != other.getClass()) return false;
        FOStoreConnectionId foci = (FOStoreConnectionId) other;
        return this.url.equals(foci.url) 
            & ((this.user!=null&foci.user!=null)?this.user.equals(foci.user):this.user==foci.user)
            & ((this.password!=null&foci.password!=null)?this.password.equals(foci.password):this.password==foci.password);
    }
    
    // XXX this 'main' really belongs in a test class...
    public static void main (String argv[]) {
        FOStoreConnectionId id0 = new FOStoreConnectionId("url", null, null); // NOI18N
        FOStoreConnectionId id1 = new FOStoreConnectionId("url", "1", null); // NOI18N
        FOStoreConnectionId id2 = new FOStoreConnectionId("url", null, "2"); // NOI18N
        FOStoreConnectionId id3 = new FOStoreConnectionId("url", "1", "2"); // NOI18N
        FOStoreConnectionId id4 = new FOStoreConnectionId("url2", "1", "2"); // NOI18N
        FOStoreConnectionId id5 = new FOStoreConnectionId("url2", "1", "2", true); // NOI18N
        try {
            FOStoreConnectionId id = new FOStoreConnectionId (null, null, null);
            System.out.println ("Failure."); // NOI18N
            return;
        } catch (NullPointerException npe) {
            // good catch
        } catch (Throwable t) {
            System.out.println ("Failure."); // NOI18N
            return;
        }
        if    (!id0.equals(id1)
            & (!id0.equals(id2))
            & (!id0.equals(id3))
            & (!id0.equals(id4))
            & (!id1.equals(id0))
            & (!id2.equals(id0))
            & (!id3.equals(id0))
            & (!id4.equals(id0))
             & (id0.equals(id0))
             & (id1.equals(id1))
             & (id2.equals(id2))
             & (id3.equals(id3))
             & (id4.equals(id4))
             & (id4.equals(id5))
                                ) {
            System.out.println ("Success."); // NOI18N
        } else {
            System.out.println ("Failure."); // NOI18N
        }
    }

}
