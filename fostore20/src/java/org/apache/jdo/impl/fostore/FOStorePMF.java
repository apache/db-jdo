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

import java.io.Externalizable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;

import javax.jdo.JDOException;
import javax.jdo.JDOFatalException;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl;
import org.apache.jdo.jdoql.JDOQLQueryFactory;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.store.StoreManager;
import org.apache.jdo.store.TranscriberFactory;
import org.apache.jdo.util.I18NHelper;

//
// Note the exception handling herein; it is intentional: if we catch a
// subclass of JDOException, rethrow it as it is "expected" by calling code,
// but if it is not, then create a subclass of JDOException (as are all
// FOStore exceptions) and throw that.  In other words, the intent is that
// only JDOException subclasses be thrown by this class.
//

/**
* The File/Object Store's implementation of a PersistenceManagerFactory.
* <p>
* This class is <code>public</code> so that clients can create instances of it
* with <code>new</code>.
*
* @author Dave Bristor
*/
public class FOStorePMF
    extends PersistenceManagerFactoryImpl
    implements Externalizable, Referenceable
{
    /**
     * StoreManagers provided by this PMF, one per-PM.
     */
    private transient final HashMap storeManagers = new HashMap();

    /**
     * Map provisional id's that were created on behalf of this PMF to the
     * real id's that have been returned by various StoreManager instances
     * that are bound to this PMF.
     */
    private transient final WeakHashMap provisionalOIDs = new WeakHashMap();

    // ConnectionFactory associated with this PMF.
    private transient FOStoreConnectionFactory cf;

    /** Model associated with this PMF. */
    private transient final FOStoreModel model = new FOStoreModel();
    
    /** Flag to tell whether to create.  This is not a JDO property.
     */
    private boolean create;

    /** The Properties instance from which this PersistenceManagerFactory
     * was configured. This is the key into the Properties/PersistenceManagerFactory
     * map.
     */
    private Properties configuredFrom = null;
    
    /** The name of the JDOQLQueryFactory class */
    private String jdoqlQueryFactoryClassName =  
        "org.apache.jdo.impl.jdoql.JDOQLQueryFactoryImpl";

    /** The query factory for JDOQL. */
    private JDOQLQueryFactory jdoqlQueryFactory;

    /** RuntimeJavaModelFactory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(
        I18N.NAME, FOStorePMF.class.getClassLoader());

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /** JNDI property type
     */
    static final String addrType =
        "Serialized-org.apache.jdo.impl.fostore.FOStorePMF"; // NOI18N
    
    /** Supported list of options for this implementation.
     */
    private final String[] optionArray = new String[] {
        "javax.jdo.option.TransientTransactional", // NOI18N
        "javax.jdo.option.NontransactionalRead", // NOI18N
        "javax.jdo.option.NontransactionalWrite", // NOI18N
        "javax.jdo.option.RetainValues", // NOI18N
        "javax.jdo.option.Optimistic", // NOI18N
        "javax.jdo.option.ApplicationIdentity", // NOI18N
        "javax.jdo.option.DatastoreIdentity", // NOI18N
//        "javax.jdo.option.NonDatastoreIdentity", // NOI18N
        "javax.jdo.option.ArrayList", // NOI18N
        "javax.jdo.option.HashMap", // NOI18N
        "javax.jdo.option.Hashtable", // NOI18N
        "javax.jdo.option.LinkedList", // NOI18N
        "javax.jdo.option.TreeMap", // NOI18N
        "javax.jdo.option.TreeSet", // NOI18N
        "javax.jdo.option.Vector", // NOI18N
//            "javax.jdo.option.Map", // NOI18N
//            "javax.jdo.option.List", // NOI18N
        "javax.jdo.option.Array", // NOI18N
        "javax.jdo.option.NullCollection", // NOI18N
        "javax.jdo.query.JDOQL" // NOI18N
    };
    
    /** Properties accessors hash map for fostore-specific properties.
     */
    protected static HashMap fostorePropsAccessors = new HashMap(3);

    /**
     * Mapping of supported java.util classes to the tracked
     * SCO classes in org.apache.jdo.impl.sco package
     */  
    private static HashMap trackedClasses = new HashMap();
    
    /** Initialize fostorePropsAccessors.
     */
    // XXX Jikes bug
    // If this is protected, FOStorePMF.initPropsAccessors cannot invoke it,
    // due to a bug in jikes (http://www-124.ibm.com/developerworks/bugs/?func=detailbug&bug_id=213&group_id=10)
    //protected static void initPropsAccessors() {
    //
    public static void initPropsAccessors() {
        PersistenceManagerFactoryImpl.initPropsAccessors();
          if (fostorePropsAccessors.size() != 0) 
              return;
          synchronized (fostorePropsAccessors) {
              if (fostorePropsAccessors.size() != 0) {
                  return;
              }
              fostorePropsAccessors.put(
              "org.apache.jdo.ConnectionCreate", // NOI18N
              new PMFAccessor() {
              public String get(PersistenceManagerFactoryImpl pmf) { return new Boolean(((FOStorePMF)pmf).getConnectionCreate()).toString(); }
              public String getNonDefault(PersistenceManagerFactoryImpl pmf) { return (!((FOStorePMF)pmf).getConnectionCreate())?null:"true"; } // NOI18N
              public String getDefault() { return "false"; } // NOI18N
              public void set(PersistenceManagerFactoryImpl pmf, String s) { ((FOStorePMF)pmf).setConnectionCreate(Boolean.valueOf(s).booleanValue()); }
              });
            fostorePropsAccessors.put(
                "org.apache.jdo.option.QueryTimeout", // NOI18N
                new PMFAccessor() {
                public String get(PersistenceManagerFactoryImpl pmf) { return Integer.toString(pmf.getQueryTimeout()); }
                public String getNonDefault(PersistenceManagerFactoryImpl pmf) { return (pmf.getQueryTimeout()==0)?null:Integer.toString(pmf.getQueryTimeout()); }
                public String getDefault() { return "0"; } // NOI18N
                public void set(PersistenceManagerFactoryImpl pmf, String s) { pmf.setQueryTimeout(toInt(s)); }
            });
            fostorePropsAccessors.put(
                "org.apache.jdo.option.UpdateTimeout", // NOI18N
                new PMFAccessor() {
                public String get(PersistenceManagerFactoryImpl pmf) { return Integer.toString(pmf.getUpdateTimeout()); }
                public String getNonDefault(PersistenceManagerFactoryImpl pmf) { return (pmf.getUpdateTimeout()==0)?null:Integer.toString(pmf.getUpdateTimeout()); }
                public String getDefault() { return "0"; } // NOI18N
                public void set(PersistenceManagerFactoryImpl pmf, String s) { pmf.setUpdateTimeout(toInt(s)); }
            });
          }
      }

    /** Sets the JDOQLQueryFactory class name used by getJDOQLQueryFactory.
     * @param jdoqlQueryFactoryClassName the name of the JDOQLQueryFactory
     * class.
     */
    public void setJDOQLQueryFactoryClassName(String jdoqlQueryFactoryClassName)
    {
        this.jdoqlQueryFactoryClassName = jdoqlQueryFactoryClassName;
    }

    /**
     * Returns the JDOQLQueryFactory bound to this FOStorePMF.
     * @return JDOQLQueryFactory
     */
    public synchronized JDOQLQueryFactory getJDOQLQueryFactory() {
        if (this.jdoqlQueryFactory == null) {
            try {
                Class clazz = Class.forName(jdoqlQueryFactoryClassName);
                this.jdoqlQueryFactory = (JDOQLQueryFactory) clazz.newInstance();
            } catch (Exception ex) {
                throw new JDOFatalException(
                    msg.msg("EXC_CannotCreateJDOQLQueryFactory",  //NOI18N
                            jdoqlQueryFactoryClassName), ex);
            }
        }
        return this.jdoqlQueryFactory;
    }

    /** Returns a new QueryTree instance. This instance allows to specify a 
     * query with an API (see {@link org.apache.jdo.jdoql.tree.QueryTree} and 
     * {@link org.apache.jdo.jdoql.tree.ExpressionFactory}) rather than as JDOQL 
     * strings. To run you create a query object from the QueryTree (see 
     * {@link javax.jdo.PersistenceManager#newQuery(Object compiled)}) 
     * and call the execute method on the Query object.
     * @return new QueryTree instance.
     */
    public QueryTree newQueryTree() {
        return getJDOQLQueryFactory().newTree();
    }
    
      /** Return the FOStore-specific accessors (the
       * properties that are not in the JDO specification).
       * @return the hash map of FOStore accessors
       */      
    protected HashMap getLocalAccessors() {
        initPropsAccessors();
        return fostorePropsAccessors;
    }

    /** Initialize trackedClasses.
     */  
    private void initTrackedClasses() {
        if (trackedClasses.size() != 0) {
            return;
        }
        synchronized (trackedClasses) {
            if (trackedClasses.size() != 0) {
               return;
            }  

            // We will need to compare equals to ensure that we do not override
            // user's defined classes:

            // java.util.Date and java.sql classes:
            trackedClasses.put(java.util.Date.class,
                org.apache.jdo.impl.sco.Date.class);
            trackedClasses.put(org.apache.jdo.impl.sco.Date.class,
                org.apache.jdo.impl.sco.Date.class);
            trackedClasses.put(java.sql.Date.class,
                org.apache.jdo.impl.sco.SqlDate.class);
            trackedClasses.put(org.apache.jdo.impl.sco.SqlDate.class,
                org.apache.jdo.impl.sco.SqlDate.class);
            trackedClasses.put(java.sql.Time.class,
                org.apache.jdo.impl.sco.SqlTime.class);
            trackedClasses.put(org.apache.jdo.impl.sco.SqlTime.class,
                org.apache.jdo.impl.sco.SqlTime.class);
            trackedClasses.put(java.sql.Timestamp.class,
                org.apache.jdo.impl.sco.SqlTimestamp.class);
            trackedClasses.put(org.apache.jdo.impl.sco.SqlTimestamp.class,
                org.apache.jdo.impl.sco.SqlTimestamp.class);

            // java.util.Set
            trackedClasses.put(java.util.HashSet.class,
                org.apache.jdo.impl.sco.HashSet.class);
            trackedClasses.put(java.util.AbstractSet.class,
                org.apache.jdo.impl.sco.HashSet.class);
            trackedClasses.put(java.util.Set.class,
                org.apache.jdo.impl.sco.HashSet.class);
            trackedClasses.put(org.apache.jdo.impl.sco.HashSet.class,
                org.apache.jdo.impl.sco.HashSet.class);

            // java.util.List
            trackedClasses.put(java.util.ArrayList.class,
                org.apache.jdo.impl.sco.ArrayList.class);
            trackedClasses.put(java.util.AbstractList.class,
                org.apache.jdo.impl.sco.ArrayList.class);
            trackedClasses.put(java.util.List.class,
                org.apache.jdo.impl.sco.ArrayList.class);
            trackedClasses.put(java.util.AbstractCollection.class,
                org.apache.jdo.impl.sco.ArrayList.class);
            trackedClasses.put(java.util.Collection.class,
                org.apache.jdo.impl.sco.ArrayList.class);
            trackedClasses.put(org.apache.jdo.impl.sco.ArrayList.class,
                org.apache.jdo.impl.sco.ArrayList.class);

            // java.util.Vector
            trackedClasses.put(java.util.Vector.class,
                org.apache.jdo.impl.sco.Vector.class);
            trackedClasses.put(org.apache.jdo.impl.sco.Vector.class,
                org.apache.jdo.impl.sco.Vector.class);

            // java.util.SortedSet
            trackedClasses.put(java.util.TreeSet.class,
                org.apache.jdo.impl.sco.TreeSet.class);
            trackedClasses.put(java.util.SortedSet.class,
                org.apache.jdo.impl.sco.TreeSet.class);
            trackedClasses.put(org.apache.jdo.impl.sco.TreeSet.class,
                org.apache.jdo.impl.sco.TreeSet.class);

            // java.util.LinkedList
            trackedClasses.put(java.util.LinkedList.class,
                org.apache.jdo.impl.sco.LinkedList.class);
            trackedClasses.put(java.util.AbstractSequentialList.class,
                org.apache.jdo.impl.sco.LinkedList.class);
            trackedClasses.put(org.apache.jdo.impl.sco.LinkedList.class,
                org.apache.jdo.impl.sco.LinkedList.class);
    
            // java.util.Map
            trackedClasses.put(java.util.Map.class,
                org.apache.jdo.impl.sco.HashMap.class);
            trackedClasses.put(java.util.AbstractMap.class,
                org.apache.jdo.impl.sco.HashMap.class);
            trackedClasses.put(java.util.HashMap.class,
                org.apache.jdo.impl.sco.HashMap.class);
            trackedClasses.put(org.apache.jdo.impl.sco.HashMap.class,
                org.apache.jdo.impl.sco.HashMap.class);

            // java.util.Hashtable
            trackedClasses.put(java.util.Hashtable.class,
                org.apache.jdo.impl.sco.Hashtable.class);
            trackedClasses.put(org.apache.jdo.impl.sco.Hashtable.class,
                org.apache.jdo.impl.sco.Hashtable.class);

            // java.util.SortedMap
            trackedClasses.put(java.util.SortedMap.class,
                org.apache.jdo.impl.sco.TreeMap.class);
            trackedClasses.put(java.util.TreeMap.class,
                org.apache.jdo.impl.sco.TreeMap.class);
            trackedClasses.put(org.apache.jdo.impl.sco.TreeMap.class,
                org.apache.jdo.impl.sco.TreeMap.class);
        }
    }
    
    //
    // Methods from PersistenceManagerFactory that are not already
    // implemented in org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl.
    //
    
    /** Create a new instance of PersistenceManager with
     * the specific user name and password.
     * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#createPersistenceManager(String userid, String password)
     * @param userid the user name
     * @param password the password
     * @return the Persistencemanager
     */
    protected PersistenceManager createPersistenceManager(
        String userid, String password) {

        PersistenceManager rc = null;
        try {
            rc = new FOStorePM(this, userid, password);
            setConfigured();
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "createPersistenceManager(userid, password)", ex); // NOI18N
        }
        return rc;
    }

    /** Close this PersistenceManagerFactory. Check for 
    * JDOPermission("closePersistenceManagerFactory") and if not authorized, 
    * throw SecurityException. 
    * <P>If the authorization check succeeds, check to see that all 
    * PersistenceManager instances obtained from this PersistenceManagerFactory 
    * have no active transactions. If any PersistenceManager instances have 
    * an active transaction, throw a JDOUserException, with one nested 
    * JDOUserException for each PersistenceManager with an active Transaction. 
    * <P>If there are no active transactions, then close all PersistenceManager 
    * instances obtained from this PersistenceManagerFactory, mark this 
    * PersistenceManagerFactory as closed, disallow getPersistenceManager 
    * methods, and allow all other get methods. If a set method or 
    * getPersistenceManager method is called after close, then 
    * JDOUserException is thrown.
    */
    public void close() {
        super.close();
        /* remove this PMF from the map so another PMF with the same 
         * properties can be constructed.
         */
        if (configuredFrom != null) {
            synchronized (hashMapByFilteredProperties) {
                hashMapByFilteredProperties.remove(configuredFrom);
            }
        }
        close(true);
    }

    /**
    * Closes the database unless there are any active store managers.
    * @param force If true, forces the database to close anyway, regardless of
    * whether or not any store managers are still active.
    * @return true if the database was closed, false if not (i.e., force is
    * false and there are active store managers).
    * @see javax.jdo.PersistenceManagerFactory#getPersistenceManager
    */
    public boolean close(boolean force) {
        boolean rc = false;

        try {
            if (storeManagers.isEmpty() || force) {
                if (logger.isDebugEnabled()) {
                    logger.debug("FOPMF closing database"); // NOI18N
                }
                if (null != cf) {
                    cf.closeDatabase();
                    cf = null;
                }
                rc = true;
            }
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "close", ex); // NOI18N
        }
        return rc;
    }

    //
    // Methods from PersistenceManagerFactoryInternal
    //
    
    /** Override PersistenceManagerFactoryImpl's method so we can use our
     * own cf variable.
     * @param cf the connection factory
     */
    public void setConnectionFactory(Object cf) {
        this.cf = (FOStoreConnectionFactory) cf;
    }
    
    /**
     * Override PersistenceManagerFactoryImpl's method so that we can get a
     * handle on the connection factory to close the database at close() time.
     * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#getConnectionFactory
     * @return  the connection factory
     */
    public synchronized Object getConnectionFactory () {
        FOStoreConnectionFactory rc = cf;

        // If we already have a connection factory, use that.  Otherwise, if
        // our superclass has one, use that.  Otherwise, make one, and use
        // that.  In the latter 2 cases, set our connection factory to be the
        // one acquired/made.
        if (null == rc) {
            try {
                cf = (FOStoreConnectionFactory)super.getConnectionFactory();
                if (logger.isDebugEnabled()) {
                    logger.debug("FOPMF.getCF: super.cf = " + cf); // NOI18N
                }
                if (null == cf) {
                    String cfName = getConnectionFactoryName();
                    if (null != cfName) {
                        // cf = JNDI lookup of name
                        // XXX In what context?
                    }
                }
                if (null == cf) {
                    cf = new FOStoreConnectionFactory();
                    cf.setPMF(this);
                    cf.setUserName(getConnectionUserName());
                    cf.setPassword(password);
                    cf.setURL(getConnectionURL());
                    cf.setCreate(create);
                }
                rc = cf;
            } catch (JDOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new FOStoreFatalInternalException(
                    getClass(), "getConnectionFactory", ex); // NOI18N
            }
        }
        return rc;        
    }
    
    /** Verifies that the associated connection factory
     * is configured (at least the URL is specified).
     * @return if the connection factory is properly configured
     */    
    protected boolean isConnectionFactoryConfigured() {
        return (cf==null?false:cf.isConfigured());
    }

    /**
     * @see org.apache.jdo.pm.PersistenceManagerFactoryInternal#getTranscriberFactory()
     * @return The PersistenceManagerFactory's transcriber factory.
     */
    public TranscriberFactory getTranscriberFactory() {
        return FOStoreTranscriberFactory.getInstance();
    }

    /**
    * If parameter is non-null and implements PersistenceCapable, returns
    * OID.class.
    * @see org.apache.jdo.pm.PersistenceManagerFactoryInternal#getObjectIdClass(
    * Class cls)
    */
    public Class getObjectIdClass(Class cls) {
        Class rc = null;
        if (null != cls && PersistenceCapable.class.isAssignableFrom(cls)) {
            JDOClass jdoClass = model.getJDOClass(cls);
            rc = javaModelFactory.getJavaClass(jdoClass.getObjectIdClass());
            if (rc == null)
                rc = OID.class;
        }
        return rc;
    }

    /**
    * @see org.apache.jdo.pm.PersistenceManagerFactoryInternal#getStoreManager(
    * PersistenceManager pm)
    */
    public StoreManager getStoreManager(PersistenceManager pm) {
        FOStoreStoreManager rc = null;
        try {
            rc = (FOStoreStoreManager)storeManagers.get(pm);
            if (null == rc) {
                rc = new FOStoreStoreManager(this);
                storeManagers.put(pm, rc);
            }
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "getStoreManager", ex); // NOI18N
        }
        return rc;
    }

    /**
    * @see org.apache.jdo.pm.PersistenceManagerFactoryInternal#releaseStoreManager(
    * PersistenceManager pm)
    */
    public void releaseStoreManager(PersistenceManager pm) {
        try {
            storeManagers.remove(pm);
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "releaseStoreManager", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.pm.PersistenceManagerFactoryInternal#getTrackedClass(
    * Class type)
    */
    public Class getTrackedClass(Class type) {
        initTrackedClasses();
        return (Class)trackedClasses.get(type);
    }

    /**
    * Returns metadata associated with this PersistenceManagerFactory.
    */
    public FOStoreModel getModel() {
        return model;
    }

    //
    // Package-private methods
    //
    
    /**
    * Provides the request factory.
    * <em>Currently, this is statically bound to return an instance of a
    * {@link BufferedRequestFactory}.  In the future, this could be
    * configurable.</em>
    * @return A RequestFactory.
    */
    RequestFactory getRequestFactory() {
        return BufferedRequestFactory.getInstance();
    }

    /**
    * Sets up a mapping from the given provisional OID to real OID.
    * @exception JDOFatalException Thrown if the given OID is not provisional, or if
    * the given provisional OID is already mapped to a real OID.
    */
    // If you change this code, see method of same name in FOStoreDatabase.
    void mapProvisionalOIDToReal(OID pOID, OID rOID) {
        if (null == pOID || null == rOID || (! pOID.isProvisional())) {
            throw new JDOFatalException(msg.msg("ERR_OIDNotProv", pOID)); // NOI18N
        }
        if (null != provisionalOIDs.get(pOID)) {
            throw new JDOFatalException(msg.msg("ERR_DuplicateProvOID", pOID)); // NOI18N
        }
        provisionalOIDs.put(pOID, rOID);
    }

    /**
    * Provides the real OID for the given provisional OID.  Returns null if
    * there is no mapping.
    * @exception JDOFatalException Thrown if the given OID is not provisional.
    */
    // If you change this code, see method of same name in FOStoreDatabase.
    OID getRealOIDFromProvisional(OID pOID) {
        if (null == pOID || (! pOID.isProvisional())) {
            throw new JDOFatalException(msg.msg("ERR_OIDNotProv", pOID)); // NOI18N
        }
        return (OID)provisionalOIDs.get(pOID);
    }

    //
    // Implement Externalizable
    // Support for serialization.  We don't have any state to save, but want
    // all the superclass's state saved
    //

    /**
    * Writes this PMF's state to the given object output.
    * @param out ObjectOutput to which this PMF's state is written.
    */
    public void writeExternal(java.io.ObjectOutput out)
        throws java.io.IOException {

        java.io.ObjectOutputStream oos = (java.io.ObjectOutputStream)out;
        super.doWriteObject(oos);
        oos.writeBoolean(create);
    }

    /**
    * Reads this PMF's state from the given object input.
    * @param in ObjectInput from which this PMF's state is read.
    */
    public void readExternal(java.io.ObjectInput in)
        throws java.io.IOException, ClassNotFoundException {

        java.io.ObjectInputStream ois = (java.io.ObjectInputStream)in;
        super.doReadObject(ois);
        create = ois.readBoolean();
    }

    /**
      * Uses rot13 algorithm.
      * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#encrypt
      */
    protected String encrypt(String s) {
        return doEncrypt(s);
    }

    /**
      * Uses rot13 algorithm.
      * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#decrypt
      */
    protected  String decrypt(String s) {
        return doEncrypt(s);
    }
    
    /**
     * Use same encryption for others in this package (e.g. FOStoreConnectionFactory).
     */
    static String doEncrypt(String s) {
        return doDecrypt(s);
    }

    /**
     * Use same encryption for others in this package (e.g. FOStoreConnectionFactory).
     */
    static String doDecrypt(String s) {
        String rc = null;
        if (null != s) {
            rc = rot13(s);
        }                
        return rc;
    }

    // Standard Rot13 stuff.  Translated to Java from a C implementation found
    // on the net.
    private static String rot13(String s) {
        String rc = null;
        int length = s.length();
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            int c = s.charAt(i);
            int cap = c & 32;
            c &= ~cap;
            c = ((c >= 'A') && (c <= 'Z') ? ((c - 'A' + 13) % 26 + 'A') : c) | cap;
            sb.append((char)c);
        }
        rc =  sb.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("encrypted " + s + " to be " + rc); // NOI18N
        }
        return rc;
    }
        
    /**
     * @param create specifies whether to create the database */    
    public void setConnectionCreate (boolean create) {
        this.create = create;
    }
    
    /**
     * @param create tells whether to create the database*/    
    public void setConnectionCreate (String create) {
        this.create = Boolean.valueOf(create).booleanValue();
    }
    
    /**
     * @return whether to create the database */    
    public boolean getConnectionCreate() {
        return create;
    }
    
    /**
     * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#setCFProperties
     */
    protected void setCFProperties(Properties p) {
        if (null != cf) {
            cf.setProperties(p);
        }
    }

    /**
     * @see org.apache.jdo.impl.pm.PersistenceManagerFactoryImpl#getCFFromProperties
     */
    protected void getCFFromProperties(Properties p) {
        cf = new FOStoreConnectionFactory();
        cf.setFromProperties(p);
    }

    //
    // Implement Referenceable
    //

    /**
     * Uses StringRefAddr's to store the information
     */
    public Reference getReference() throws NamingException {
        Reference rc = new Reference(
          FOStorePMF.class.getName(),
          FOStorePMFFactory.class.getName(),
          null);

        Properties p = getAsProperties();
        for (Enumeration e = p.propertyNames(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String value = p.getProperty(key);
            rc.add(new StringRefAddr(key, value));
        }                
        return rc;
    }
    
    /**
     * @return configuration information */    
    public String toString() {
        return super.toString() +
            "model: " + model + "\n" + // NOI18N
            "storeManagers: " + storeManagers + "\n" + // NOI18N
            "provisionalOIDs: " + provisionalOIDs + "\n" + // NOI18N
            "cf: " + cf + "\n"; // NOI18N
    }

    /**
    * @see PersistenceManagerFactoryImpl#getOptionArray
    */
    protected String[] getOptionArray() {
        return optionArray;
    }
    
    /** A HashMap that associates PersistenceManagerFactory instances with a
     * Properties instance.
     */
    protected static HashMap hashMapByFilteredProperties = new HashMap();
    
    /** 
     * Construct a Properties instance from the given Properties.  Only
     * those property entries recognized by this implementation will be 
     * stored in the internal Properties instance.
     *
     * <P>This method attempts to find an existing PersistenceManagerFactory 
     * with the properties as specified in the parameter.  Only the non-default
     * properties are considered when trying to find a match.
     *
     * <P>This method cannot be implemented by the superclass because 
     */
    public static PersistenceManagerFactoryImpl getPersistenceManagerFactory (Properties props) {
        initPropsAccessors();
        FOStoreConnectionFactory.initPropsAccessors();
        Properties filtered = new Properties();
        filterProperties (props, filtered, pmfAccessors);
        filterProperties (props, filtered, propsAccessors);
        filterProperties (props, filtered, fostorePropsAccessors);
        filterProperties (props, filtered, FOStoreConnectionFactory.CFpropsAccessors);
        FOStorePMF pmf = null;
        synchronized (hashMapByFilteredProperties) {
            pmf = (FOStorePMF) hashMapByFilteredProperties.get (filtered);
            if (pmf != null) return pmf;
            pmf = new FOStorePMF();
            pmf.setFromProperties (filtered);
            pmf.verifyConfiguration();
            pmf.setConfigured();
            hashMapByFilteredProperties.put (filtered, pmf);
            pmf.configuredFrom = filtered;
        }
        return pmf;
    }
            
    /** Set the PMF class property for this PMF.
     */
    protected void setPMFClassProperty(Properties props) {
        props.setProperty ("javax.jdo.PersistenceManagerFactoryClass", "org.apache.jdo.impl.fostore.FOStorePMF"); // NOI18N
    }    

    /** Method called by the shudown hook to close pmf instances left open 
     * when the JVM exits.
     */
    protected void shutdown() {
        super.shutdown();
        close(true);
    }
}
