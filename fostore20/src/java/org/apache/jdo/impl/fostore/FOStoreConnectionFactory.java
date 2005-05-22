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

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jdo.JDOFatalException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOFatalDataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.JDOFatalUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.pm.Accessor;
import org.apache.jdo.util.I18NHelper;
import org.apache.jdo.util.Pool;

/**
 * A connection factory for FOStore.  Allows both same- and remote-address
 * space connections.  For the same address space-connections, the URL must
 * not include the Host (Server) parameter.  For remote address space
 * connections, the URL's protocol is ignored.
 * <p>
 * This class is <code>public</code> so that clients can create instances of it
 * with <code>new</code>.
 *
 * @author Dave Bristor
 */
public class FOStoreConnectionFactory implements Serializable {
    private String url;
    private String userName;
    private String password;
    private String driverName;
    private boolean create;
    
    private FOStorePMF pmf;

    private int loginTimeout;

    private transient PrintWriter logWriter;
    
    /** Connections are created by the FOStoreURLStreamHandler.
     */
    private final FOStoreURLStreamHandler streamHandler = 
        FOStoreURLStreamHandler.getInstance();

    /** Connections are pooled.  Each unique combination of url, 
     * user, password has its own pool.  The hashmap associates 
     * a FOStoreConnectionId with its pool of connections.
     */
    private final HashMap connectionMap = new HashMap();
    
    /** For now, set the pool size to 1.
     */
    // XXX this needs to be configurable...
    private static final int poolSize = 1;

    private FOStoreConnectionId defaultConnectionId;
    
    private FOStoreConnectionId userConnectionId;

    /** True until setConfigured has been invoked.  Allows properties to be
     * set if true.
     */
    private boolean configurable = true;

    /** This table maps from names to CFAccessors.  The names are the same as the
     * persistence manager factory's property names, but with 
     * org.apache.jdo.FOStoreConnectionFactory.option prepended.
     */
    protected static HashMap CFpropsAccessors = new HashMap(9);

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /**
     * First time a FOStoreConnectionFactory is created, initialize accessors
     * which are used to store/save instances via JNDI.
     */
    public FOStoreConnectionFactory() {
        if (logger.isDebugEnabled()) logger.debug("FOCF()"); // NOI18N
            initPropsAccessors();
    }
    
    /** Set the url, user, and password into the ConnectionIds for this
     * connection factory.
     */
    private void setConfigured() {
        if (logger.isDebugEnabled()) {
            logger.debug("FOCF.setConfigured: URL: " + url); // NOI18N
        }
        configurable = false;
        defaultConnectionId =
            new FOStoreConnectionId(url, userName, password, create);
        userConnectionId = new FOStoreConnectionId(url, null, null, false);
    }
    
    private void assertConfigurable() {
        if (configurable) return;
        throw new JDOUserException (
            msg.msg("EXC_AssertConfigurableFailure")); // NOI18N
    }

    /**
     * Provides a connection to the database using the given userName and
     * password.  The first time a connection is made, the
     * factory can no longer be configured.
     * @return A FOStoreClientConnection
     */
    public synchronized FOStoreClientConnection getConnection(
            String user, String password) {
        setConfigured(); // initializes userConnectionId with url.
        FOStoreConnection rc = null;

        if (logger.isDebugEnabled()) {
            logger.debug("FOCF.getConnection(" + user + ", " + 
                           password + "): " + hashCode()); // NOI18N
        }
        // We reuse the same userConnectionId until we need to create a new
        // Pool for it.  The we create a new one for next time.
        FOStoreConnectionId connectionId = userConnectionId;
        connectionId.setUser (user);
        connectionId.setPassword (password);
        
        // Try to find the existing connection in the pool.
        // First time we get here, database is not yet open and connections
        // are not yet created/in-pool.
        Pool pool = (Pool) connectionMap.get (connectionId);
        if (null == pool) {
            pool = createPool (connectionId);
            userConnectionId = new FOStoreConnectionId (url, null, null);
        }
        try {
            // blocks until a connection is available.
            return (FOStoreClientConnection) pool.get(); 
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_PoolGet"), ex); // NOI18N
        }
    }

    /** Create a new pool of connections for this combination of url, user,
     * and password.  This might be either the default or for a specific user.
     */
     Pool createPool (FOStoreConnectionId id) {
        Pool pool = new Pool(poolSize);
        if (connectionMap.put (id, pool) != null) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_DuplicatePool")); // NOI18N
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("FOCF: first time; filling pool"); // NOI18N
            }
            for (int i = 0; i < poolSize; i++) {
                FOStoreClientConnection connection =
                    createConnection(id);
                pool.put(connection);
            }
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_PoolPutFill"), ex); // NOI18N
        }
        return pool;
        }

    /**
     * Provides a connection to the database using the configured userName,
     * password, and url.  The first time a connection is made, the
     * factory can no longer be configured.
     * @return A FOStoreClientConnection
     */
    public synchronized FOStoreClientConnection getConnection() {
        setConfigured();
        FOStoreConnection rc = null;

        if (logger.isDebugEnabled()) {
            logger.debug("FOCF.getConnection(): " + hashCode()); // NOI18N
        }

        // First time we get here, database is not yet open and connections
        // are not yet created/in-pool.
        
        Pool pool = (Pool) connectionMap.get (defaultConnectionId);
        if (null == pool) {
            pool = createPool (defaultConnectionId);
        }
        try {
            // blocks until a connection is available.
            return (FOStoreClientConnection) pool.get(); 
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_PoolGet"), ex); // NOI18N
        }
    }
            
    /** 
     * This method requires permission to perform the following requests:
     * Create new URL with the specified StreamHandler.
     * Delete old database id create flag is set to true.
     */
    private FOStoreClientConnection createConnection(
        final FOStoreConnectionId id) {
        final FOStoreConnectionFactory cf = this;
        return (FOStoreClientConnection) AccessController.doPrivileged (
            new PrivilegedAction () {
                public Object run () {
                    URL uRL = null;
                    try {
                        uRL = new URL (null, url, streamHandler);
                        FOStoreClientConnection connection =
                            (FOStoreClientConnection) streamHandler.openConnection(uRL);
                        connection.setConnectionFactory(cf);
                        connection.setConnectionId(id);
                        connection.connect();
                        return connection;
                    } catch (SecurityException se) {
                        throw new JDOFatalUserException(
                            msg.msg("EXC_CannotSpecifyStreamHandler"), se); //NOI18N
                    } catch (UnknownHostException ioe) {
                        throw new JDOFatalUserException (
                        msg.msg("EXC_UnknownHostException", uRL.getHost()), ioe); // NOI18N
                    } catch (ConnectException ioe) {
                        int port = uRL.getPort();
                        if (port == -1) 
                            port = FOStoreRemoteConnection.DEFAULT_PORT;
                        throw new JDOFatalUserException(
                            msg.msg("EXC_ConnectException", // NOI18N
                                    uRL.getHost(), new Integer(port)),
                            ioe); 
                    } catch (IOException ioe) {
                        throw new JDOFatalUserException (
                        msg.msg("EXC_CannotCreateConnection", url), ioe); // NOI18N
                    }
                }
            }
        );
    }
         

    /**
     * Returns a connection to the pool
     * @param connection Connection to be returned to the pool.
     */
    void closeConnection(FOStoreClientConnection connection) {
        FOStoreConnectionId id = connection.getConnectionId();
        Pool pool = (Pool) connectionMap.get(id);
        try {
            pool.put(connection);
        } catch (InterruptedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_CloseConnectionpoolPut"), ex); // NOI18N
        }
    }

    /**
     * Close the database.  This really means close all connections that
     * have been opened.  Closing the last connection on a database actually 
     * closes the database, whether local or remote.
     */
    public synchronized void closeDatabase() {
        Object timer = Tester.startTime();
        try {
            for (Iterator hmi = connectionMap.values().iterator();
                 hmi.hasNext();) {
                
                Pool dbPool = (Pool) hmi.next();
                // we know that there are poolSize entries in each pool
                for (int i = 0; i < poolSize; ++i) {
                    FOStoreClientConnection focci =
                        (FOStoreClientConnection) dbPool.get();
                    focci.closeDatabase();
                }
            }
            if (logger.isTraceEnabled()) {
                Tester.printTime(timer, "Time to close database"); // NOI18N
            }
        } catch (FOStoreDatabaseException ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(),
                "close", msg.msg("ERR_CannotClose", url), ex); // NOI18N
        } catch (IOException ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(),
                "close", msg.msg("ERR_CannotClose", url), ex); // NOI18N
        } catch (InterruptedException ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(),
                "close", msg.msg("ERR_CannotClose", url), ex); // NOI18N
        }
        if (logger.isDebugEnabled()) {
            logger.debug("FOCF: closed " + url); // NOI18N
        }
    }

    /**
    * Sets name of the driver for connections
    * @param driverName driver name
    */
    public void setDriverName(String driverName){
        assertConfigurable();
        this.driverName = driverName;
    }

    /**
    * Provides PersistenceManagerFactory for connections
    * @return PMF
    */
    public FOStorePMF getPMF() {
        return pmf;
    }
  
    /**
    * Sets PersistenceManagerFactory for connections
    * @param pmf PersistenceManagerFactory
    */
    public void setPMF(FOStorePMF pmf){
        assertConfigurable();
        this.pmf = pmf;
    }

    /**
    * Provides name of driver used for connections
    * @return driver name
    */
    public String getDriverName() {
        return driverName;
    }
  
    /**
    * Sets connection URL
    * @param url connection URL
    */
    public void setURL(String url) {
        assertConfigurable();
        this.url = url;
    }

    /**
    * Returns connection URL
    * @return      connection URL
    */
    public String getURL() {
        return url;
    }
  
    /**
    * Sets database user
    * @param userName      database user
    */
    public void setUserName(String userName) {
        assertConfigurable();
        this.userName = userName;
    }

    /**
    * Returns database user name
    * @return      current database user name
    */
    public String getUserName() {
        return userName;
    }
  
    /**
    * Sets database user password
    * @param password      database user password
    */
    public void setPassword(String password) {
        assertConfigurable();
        this.password = password;
    }
  
    /**
    * Sets minimum number of connections in the connection pool
    * @param minPool       minimum number of connections
    */
    public void setMinPool(int minPool) { 
        assertConfigurable();
    }

    /**
    * Returns minimum number of connections in the connection pool
    * @return      connection minPool
    */
    public int getMinPool() {
        return 1;
    }
  
    /**
    * Sets maximum number of connections in the connection pool
    * @param maxPool       maximum number of connections
    */
    public void setMaxPool(int maxPool) {
        assertConfigurable();
    }

    /**
    * Returns maximum number of connections in the connection pool
    * @return      connection maxPool
    */
    public int getMaxPool() {
        return 1;
    }
  
    /**
    * Sets the amount of time, in milliseconds, between the connection
    * manager's attempts to get a pooled connection.
    * @param msInterval    the interval between attempts to get a database
    *                      connection, in milliseconds.
    *
    */
    public void setMsInterval(int msInterval) { }

    /**
    * Returns the amount of time, in milliseconds, between the connection
    * manager's attempts to get a pooled connection.
    * @return      the length of the interval between tries in milliseconds
    */
    public int getMsInterval() {
        return 0;
    }
  
    /**
    * Sets the number of milliseconds to wait for an available connection
    * from the connection pool before throwing an exception
    * @param msWait        number in milliseconds
    */
    public void setMsWait(int msWait) { 
        assertConfigurable();
    }

    /**
    * Returns the number of milliseconds to wait for an available connection
    * from the connection pool before throwing an exception
    * @return      number in milliseconds
    */
    public int getMsWait() {
        return 0;
    }
  
    /**
    * Sets the LogWriter to which messages should be sent
    * @param logWriter            logWriter
    */
    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
    * Returns the LogWriter to which messages should be sent
    * @return      logWriter
    */
    public PrintWriter getLogWriter() {
        return logWriter;
    }
 
    /**
    * Sets the number of seconds to wait for a new connection to be
    * established to the data source
    * @param loginTimeout           wait time in seconds
    */
    public void setLoginTimeout(int loginTimeout) {
        assertConfigurable();
        this.loginTimeout = loginTimeout;
    }

    /**
    * Returns the number of seconds to wait for a new connection to be
    * established to the data source
    * @return      wait time in seconds
    */
    public int getLoginTimeout() {
        return loginTimeout;
    }

    /**
    * Sets whether to create the database.
    * @param create whether to create the database.
    */
    public void setCreate(boolean create) {
        assertConfigurable();
        this.create = create;
    }

    /**
    * Sets whether to create the database.
    * @param create whether to create the database.
    */
    public void setCreate(String create) {
        assertConfigurable();
        this.create = Boolean.valueOf (create).booleanValue();
    }

    /**
    * Returns whether to create the database.
    * @return whether to create the database
    */
    public boolean getCreate() {
        return create;
    }

    //
    // Support for JNDI: we want to save/restore a FOStoreConnectionFactory
    // via JNDI, and have the stored representation be via properties.
    //

    /** CFAccessor implementation instances allow copying values to/from a
     * FOStoreConnectionFactory
     * and a Properties.  They do the proper type translation too.
     */
    interface CFAccessor extends Accessor {
        /** @return String form of a value in a FOStoreConnectionFactory.
         */
        public String get(FOStoreConnectionFactory focf);

        /** @return String form of a value in a FOStoreConnectionFactory if
         * is not a default value.
         */
        public String getNonDefault(FOStoreConnectionFactory focf);

        /** @param s String form of a value in a FOStoreConnectionFactory.
         */
        public void set(FOStoreConnectionFactory focf, String s);
    }

    // Initialize the property accessors map.
    protected static void initPropsAccessors() {
        synchronized (CFpropsAccessors) {
            if (CFpropsAccessors.size() == 0) {

                CFpropsAccessors.put(
                    "org.apache.jdo.FOStoreConnectionFactory.option.URL", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return focf.getURL(); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return focf.getURL(); }
                    public String getDefault() {return null;}
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setURL(s); }
                });
                CFpropsAccessors.put(
                    "org.apache.jdo.FOStoreConnectionFactory.option.UserName", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return focf.getUserName(); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return focf.getUserName(); }
                    public String getDefault() {return null;}
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setUserName(s); }
                });
                CFpropsAccessors.put(
                    "org.apache.jdo.FOStoreConnectionFactory.option.Password", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return FOStorePMF.doEncrypt(focf.password); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return FOStorePMF.doEncrypt(focf.password); }
                    public String getDefault() {return null;}
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setPassword(FOStorePMF.doDecrypt(s)); }
                });
                CFpropsAccessors.put(
                    "org.apache.jdo.FOStoreConnectionFactory.option.DriverName", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return focf.getDriverName(); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return focf.getDriverName(); }
                    public String getDefault() {return null;}
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setDriverName(s); }
                });
                CFpropsAccessors.put(
                    "org.apache.jdo.FOStoreConnectionFactory.option.LoginTimeout", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return focf.getDriverName(); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return focf.getDriverName(); }
                    public String getDefault() {return null;}
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setDriverName(s); }
                });
                CFpropsAccessors.put(
                    "javax.jdo.FOStoreConnectionFactory.option.MinPool", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return Integer.toString(focf.getMinPool()); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return (focf.getMinPool()==1)?null:Integer.toString(focf.getMinPool()); }
                    public String getDefault() { return "1"; } // NOI18N
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setMinPool(toInt(s)); }
                });
                CFpropsAccessors.put(
                    "javax.jdo.FOStoreConnectionFactory.option.MaxPool", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return Integer.toString(focf.getMaxPool()); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return (focf.getMaxPool()==1)?null:Integer.toString(focf.getMaxPool()); }
                    public String getDefault() { return "1"; } // NOI18N
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setMaxPool(toInt(s)); }
                });
                CFpropsAccessors.put(
                    "javax.jdo.FOStoreConnectionFactory.option.MsWait", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return Integer.toString(focf.getMsWait()); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return (focf.getMsWait()==0)?null:Integer.toString(focf.getMsWait()); }
                    public String getDefault() { return "0"; } // NOI18N
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setMsWait(toInt(s)); }
                });
                CFpropsAccessors.put(
                    "javax.jdo.FOStoreConnectionFactory.option.Create", // NOI18N
                    new CFAccessor() {
                    public String get(FOStoreConnectionFactory focf) { return new Boolean(focf.getCreate()).toString(); }
                    public String getNonDefault(FOStoreConnectionFactory focf) { return (!focf.getCreate())?null:"true"; } // NOI18N
                    public String getDefault() { return "false"; } // NOI18N
                    public void set(FOStoreConnectionFactory focf, String s) { focf.setCreate(Boolean.valueOf(s).booleanValue()); }
                });
            }
        }
    }

    /**
     * It should *never* be the case that our translation process encounters
     * a NumberFormatException.  If so, tell the user in the JDO-approved
     * manner.
     */ 
    private static int toInt(String s) {
        int rc = 0;
        try {
            rc = new Integer(s).intValue();
        } catch (NumberFormatException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_Badformat")); // NOI18N
        }
        return rc;
    }

    /**
     * Sets properties as per the property values in the connection factory.
     * For each CFAccessor in the given HashMap, gets the corresponding value
     * from the FOStoreConnectionFactory and puts it in the given 
     * Properties object.
     */
    void setProperties(Properties p) {
        Set s = CFpropsAccessors.entrySet();
        for (Iterator i = s.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            String key = (String)e.getKey();
            CFAccessor a = (CFAccessor)e.getValue();
            String value = (String)a.getNonDefault(this);
            if (null != value) {
                p.put(key, value);
            }
        }
    }

    /**
     * Configures a FOStoreConnectionFactory from the given Properties.
     * For each Accessor in the given HashMap, gets the corresponding value
     * from the Properties and sets that value in the PMF.
     * This is public so that a test program can create a
     * FOSToreConnectionFactory, and configure it from a Properties object.
     */
    public void setFromProperties(Properties p) {
        Set s = CFpropsAccessors.entrySet();
        for (Iterator i = s.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            String key = (String)e.getKey();
            String value = p.getProperty(key);
            if (null != value) {
                CFAccessor a = (CFAccessor)e.getValue();
                if (logger.isDebugEnabled()) {
                    logger.debug("FOStoreConnectionFactory setting property: " + key + " to: " + value); // NOI18N
                }
                a.set(this, value);
            }
        }
    }
    
    /**
     * Returns true if this connection factory has been configured with a URL.
     */
    public boolean isConfigured() {
        if (logger.isDebugEnabled()) 
            logger.debug("FOStoreConnectionFactory url is: " + url); // NOI18N
        return (url != null);
    }

    public String toString() {
        return "" + // NOI18N
            "FOCF.url: " + url + "\n" + // NOI18N
            "FOCF.userName: " + userName + "\n" + // NOI18N
            "FOCF.password: " + password + "\n" + // NOI18N
            "FOCF.driverName: " + driverName + "\n" + // NOI18N
            "FOCF.loginTimeout: " + loginTimeout; // NOI18N
   }        
}
