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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.Transaction;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.StateManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.query.BasicQueryResult;
import org.apache.jdo.query.QueryResult;
import org.apache.jdo.query.QueryResultHelper;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOIdentityType;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.Connector;
import org.apache.jdo.store.StoreManagerImpl;
import org.apache.jdo.util.I18NHelper;

//
// Note the exception handling herein; it is intentional: if we catch a
// subclass of JDOException, rethrow it as it is "expected" by calling code,
// but if it is not, then create a subclass of JDOException (as are all
// FOStore exceptions) and throw that.  In other words, the intent is that
// only JDOException subclasses be thrown by this class.
//

/**
* StoreManager represents the datastore to the rest of the JDO components.
* It provides the means to write and read instances, to get the extent of
* classes, and to get the object id for a persistence capable object.
*
* @author Dave Bristor
*/
class FOStoreStoreManager extends StoreManagerImpl {
    private final FOStorePMF pmf;
    private final FOStoreConnector connector;
    private final RequestFactory rf;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(
        I18N.NAME, FOStoreStoreManager.class.getClassLoader());

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /** JDOImplHelper instance. */
    private static final JDOImplHelper jdoImplHelper = 
        (JDOImplHelper) AccessController.doPrivileged (
            new PrivilegedAction () {
                public Object run () {
                    try {
                        return JDOImplHelper.getInstance();
                    }
                    catch (SecurityException e) {
                        throw new JDOFatalUserException (msg.msg(
                            "ERR_Security", e.getMessage()), e); // NOI18N
                    }
                }
            }    
        );

    /** RuntimeJavaModelFactory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    FOStoreStoreManager(FOStorePMF pmf) {
        this.pmf = pmf;
        this.connector = new FOStoreConnector(pmf);
        this.rf = pmf.getRequestFactory();
    }
    
    //
    // Implement org.apache.jdo.store.StoreManager
    //
    
    /**
    * @see org.apache.jdo.store.StoreManager#getConnector
    */
    public Connector getConnector() {
        return connector;
    }

    /**
    * @see org.apache.jdo.store.StoreManager#getConnector(String userid,
    * String password)
    */
    public Connector getConnector(String userid, String password) {
        throw new JDOUserException("Not yet implemented"); // NOI18N
    }

    
    /**
    * @see org.apache.jdo.store.StoreManager#insert(BitSet, BitSet,
    * StateManagerInternal)
    */
    public synchronized int insert(
        BitSet loadedFields, BitSet dirtyFields, StateManagerInternal sm) {

        Message message = connector.getMessage();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("FOSRM.insert"); // NOI18N
            }

            InsertRequest request = rf.getInsertRequest(sm, message, pmf);
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "insert", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "insert", ex); // NOI18N
        }
        dirtyFields.xor(dirtyFields); // Clear all bits.
        return StateManagerInternal.FLUSHED_COMPLETE;
    }


    /**
    * @see org.apache.jdo.store.StoreManager#update(BitSet, BitSet, 
    * StateManagerInternal)
    */
    public synchronized int update(
        BitSet loadedFields, BitSet dirtyFields, StateManagerInternal sm) {

        Message message = connector.getMessage();
        try {
            UpdateRequest request =
                rf.getUpdateRequest(sm, message, pmf,
                                    loadedFields, dirtyFields, optimistic);
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "update", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "update", ex); // NOI18N
        }
        dirtyFields.xor(dirtyFields); // Clear all bits.
        return StateManagerInternal.FLUSHED_COMPLETE;
    }

    
    /**
     * @see org.apache.jdo.store.StoreManager#verifyFields(BitSet,
     * BitSet, StateManagerInternal)
     */
    public synchronized int verifyFields(
        BitSet ignoredFields, BitSet fieldsToVerify, StateManagerInternal sm) {

        if ( ! verify(sm, true, fieldsToVerify)) {
            throw new JDODataStoreException(
                msg.msg("EXC_VerifyFields")); // NOI18N
        }
        fieldsToVerify.xor(fieldsToVerify); // Clear all bits.
        return StateManagerInternal.FLUSHED_COMPLETE;
    }

    // RESOLVE: Marina, do we need this?  @see org.apache.jdo.store.StoreManager#verifyExistence.
    /**
     * @see org.apache.jdo.store.StoreManager#verifyExistence
     */
//    public boolean verifyExistence(StateManagerInternal sm) {
//        return verify(sm, false, null, null);
//    }

    /**
     * @see org.apache.jdo.store.StoreManager#delete(BitSet, BitSet,
     * StateManagerInternal)
     */
    public synchronized int delete(
        BitSet loadedFields, BitSet dirtyFields, StateManagerInternal sm) {

        Message message = connector.getMessage();
        try {
            DeleteRequest request = rf.getDeleteRequest(sm, message, pmf);
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "delete", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "delete", ex); // NOI18N
        }
        dirtyFields.xor(dirtyFields);
        return StateManagerInternal.FLUSHED_COMPLETE;
    }

    /**
    * @see org.apache.jdo.store.StoreManager#fetch
    */
    public synchronized void fetch(StateManagerInternal sm, int fieldNums[]) {

        Message message = connector.getMessage();
        try {
            FetchRequest request = rf.getFetchRequest(sm, message, pmf);
            request.doRequest();
            connector.flush();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "fetch", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "fetch", ex); // NOI18N
        }
    }

    /**
    * @see org.apache.jdo.store.StoreManager#getExtent
    */
    public synchronized Extent getExtent(Class pcClass, boolean subclasses,
                            PersistenceManagerInternal pm) {

        FOStoreModel model = pmf.getModel();
        Message message = connector.getMessage();

        // If the instance's class has PC superclasses, we must make sure
        // they are activated too.
        activateClasses(pcClass, message);

        return new FOStoreExtent(pcClass, subclasses, pm, rf, connector);
    }

    /**
     * Creates a new object id for the given sm.  Delegates implementation to
     * #createInternalObjectId(StateManagerInternal sm,Class cls, 
     * PersistenceManagerInternal pm)
    * @see org.apache.jdo.store.StoreManager#createObjectId
    */
    public synchronized Object createObjectId(StateManagerInternal sm,
                                              PersistenceManagerInternal pm) {
        PersistenceCapable obj = sm.getObject();
        Class cls = obj.getClass();
        OID rc = createInternalObjectId(sm, obj, null, cls, pm);
        return rc;
    }

    /**
     * Creates a new object id for the given class. Binds metadata for the sm
     * (i.e., ensures that there is a CLID for instances of the sm's object's
     * class).  Creates a request in the message that will provide a datastore
     * oid for the provisional oid which is returned, but does not interact
     * with the store.
    * @see org.apache.jdo.store.StoreManager#createObjectId
    */
    public synchronized OID createInternalObjectId(StateManagerInternal sm,
                                   PersistenceCapable pc, Object oid,
                                   Class cls, PersistenceManagerInternal pm) {
        FOStoreModel model = pmf.getModel();
        Message message = connector.getMessage();

        // If the instance's class has PC superclasses, we must make sure
        // they are activated too.
        JDOClass jdoClass = model.getJDOClass(cls);

        // Now we can bind class type to the correct OID instance.
        OID rc = model.bind(cls, jdoClass.getIdentityType(), pc, oid, pm, pmf); 

        activateClasses(cls, message);

        // If the instance's CLID is provisional, we must activate its class.
        CLID clid = rc.getCLID();
        if (clid.isProvisional() && ! message.containsCLID(clid)) {
            activateClass(cls, message);
            message.addCLID(clid);
        }

        try {
            CreateOIDRequest request =
                rf.getCreateOIDRequest(sm, message, pmf, rc, pm);
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "createObjectid", ex); // NOI18N
        }
        return rc;
    }

    /**
     * Provides a datastore object id.
     * If objectId is not provisional, return it.  Otherwise, see if we have a
     * corresponding datastore objectId and return that if so.  If neither of
     * those works out, flush the current message which (we hope) will have a
     * corresponding CreateObjectIdRequest in it.  Then look as before for a
     * corresponding datastore objectId in our mapping and return that.  If we
     * still don't have a correspondance, throw an exception.
    * @see org.apache.jdo.store.StoreManager#getExternalObjectId(Object oid,
    * PersistenceCapable pc)
    */
    public synchronized Object getExternalObjectId(Object objectId,
                                                   PersistenceCapable pc) {
        Object rc = null;

        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.getEOID given " + objectId); // NOI18N
        }
        OID oid = (OID)objectId;
        if (oid.isProvisional()) {
            if (oid.isDataStoreIdentity()) {
                oid = pmf.getRealOIDFromProvisional(oid);

                if (null == oid) {
                
                    // Given objectId is provisional, and we have no mapping.
                    // Flush so that the CreateObjectId requests that are in
                    // the current message get written to the store, which
                    // should cause us to get a datastore oid for the given
                    // provisional oid.
                    preFlush();
                    oid = pmf.getRealOIDFromProvisional((OID)objectId);
                    if (null == oid) {
                        throw new JDOUserException(
                            msg.msg("EXC_UnboundOID"), objectId); // NOI18N
                    }
                }
            } else {
                // Do flush only:
                preFlush();
            }
        }
        // Always return a copy
        rc =  oid.getExternalObjectId(pc);

        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.getEOID returning " + rc); // NOI18N
        }
        
        return rc;
    }

    /**
    * @see org.apache.jdo.store.StoreManager#copyKeyFieldsFromObjectId
    */
    public void copyKeyFieldsFromObjectId(StateManagerInternal sm, 
                                          Class pcClass) {
        OID oid = (OID)sm.getInternalObjectId();
        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.copyKeyFieldsFromObjectId: " + oid); // NOI18N
        }
        PersistenceManagerInternal pm = sm.getPersistenceManager();
        FOStoreModel model = pmf.getModel();
        JDOClass jdoClass = model.getJDOClass(pcClass);
        oid.copyKeyFieldsToPC(sm, pmf, pcClass, 
                              jdoClass.getPrimaryKeyFieldNumbers());
    }

    /**
    * @see org.apache.jdo.store.StoreManager#hasActualPCClass
    */
    public boolean hasActualPCClass(Object objectId) {
        boolean rc = false;
        if (objectId instanceof OID) {
            rc = ((OID)objectId).isDataStoreIdentity();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.hasActualPCClass: " + rc); // NOI18N
        }
        return rc;
    }

    /**
    * @see org.apache.jdo.store.StoreManager#getInternalObjectId
    */
    public synchronized Object getInternalObjectId(Object objectId,
                                                   PersistenceManagerInternal pm) {
        OID rc = null;
        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.getInternalObjectId: " + objectId); // NOI18N
        }
        if (objectId instanceof OID) {
            rc = ((OID)objectId).copy();
        } else {
            Class cls = getPCClassForOid(objectId, pm);
            rc = createInternalObjectId(null, null, objectId, cls, pm);
            if (logger.isDebugEnabled()) {
                logger.debug("FOSRM.getInternalObjectId: got=" + rc); // NOI18N
            }
        }
        return rc;
    }

    /**
     * @see org.apache.jdo.store.StoreManager#isMediationRequiredToCopyOid
     */
    public boolean isMediationRequiredToCopyOid() {
        return true;
    }

    /**
    * @see org.apache.jdo.store.StoreManager#getPCClassForOid
    */
    public synchronized Class getPCClassForOid(Object objectId,
                                             PersistenceManagerInternal pm) {
        Class rc = null;
        Message message = connector.getMessage();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("FOSRM.getPCClassForOid: " + 
                               objectId.getClass().getName() + objectId); // NOI18N
            }
            if (!(objectId instanceof OID)) {
                Class cls = objectId.getClass();
                rc = pm.loadPCClassForObjectIdClass(cls);
            } else {
                OID oid = (OID)objectId;
                CLID clid = oid.getCLID();

                FOStoreModel model = pmf.getModel();
                rc = model.getClass(clid);

                if (null == rc) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "FOSRM.getPCClassForOid: asking store for CLID: " +
                            clid); // NOI18N
                    }
                    // The clid is not known in this JVM, but it might be known
                    // in the store.  Try to get it.
                    GetClassRequest request =
                        rf.getGetClassRequest(clid, message, pmf, pm);
                    try {
                        request.doRequest();
                        connector.flush();
                        rc = request.getClassForCLID();
                    } catch (IOException ex) {
                        throw new FOStoreFatalIOException(
                            getClass(), "getPCClassForOid", ex); // NOI18N
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("FOSRM.getPCClassForOid: got " + rc); // NOI18N
            }
        } catch (ClassCastException ex) {
            throw new JDOUserException(
                msg.msg("EXC_NotOID"), ex, objectId); // NOI18N
        } catch (ClassNotFoundException ex) {
            throw new JDOUserException(
                msg.msg("EXC_NotOID"), ex, objectId); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "getPCClassForOid", ex); // NOI18N
        }
        return rc;
    }

    /** 
     * This method returns an object id instance corresponding to the Class 
     * and String arguments. The String argument might have been the 
     * result of executing toString on an object id instance. 
     * @param pcClass the Class of the persistence-capable instance
     * @param str the String form of the object id
     * @return an instance of the object identity class
     */
    public Object newObjectIdInstance (Class pcClass, String str) {
        Object rc = null;
        FOStoreModel model = pmf.getModel();
        JDOClass jdoClass = model.getJDOClass(pcClass);
        switch (jdoClass.getIdentityType()) {
          case JDOIdentityType.APPLICATION:
            //No need to create an AID here - it will not be used.
            rc = jdoImplHelper.newObjectIdInstance(pcClass, str);
            break;
          case JDOIdentityType.DATASTORE:
            rc = new OID(str);
            break;
          default:
            break;
        }
        return rc;
    }

    /**
     * Returns a QueryResult instance which is then returned as the result of 
     * Query.execute(...). This method allows support for datastore specific 
     * query execution strategies, since each StoreManager can have its own
     * implementation of the QueryResult interface. 
     * For now fostore uses the non optimized BasicQueryResult as QueryResult
     * implemenatation.
     * @param qrh the helper providing the query tree, the candidates 
     * and the actual parameters.
     * @return a datastore specific query result instance
     */
    public QueryResult newQueryResult(QueryResultHelper qrh) {
        return new BasicQueryResult(qrh);
    }

    //
    // Used within FOStore
    //

    /**
     * Activates this class and all supeclasses.
     */  
    private void activateClasses(Class cls, Message message) {
        FOStoreModel model = pmf.getModel();
        // If the instance's class has PC superclasses, we must make sure
        // they are activated too.
        JDOClass jdoClass = model.getJDOClass(cls);
        JDOClass jdoSuper = jdoClass.getPersistenceCapableSuperclass();
        if (null != jdoSuper) {
            ArrayList javaSupers = new ArrayList();
            while (null != jdoSuper) {
                Class javaSuper = javaModelFactory.
                    getJavaClass(jdoSuper.getJavaType());
                javaSupers.add(javaSuper);
                jdoSuper = jdoSuper.getPersistenceCapableSuperclass();
            }

            // Activate the superclasses in order from Object on down, so
            // that the store can recognize subclasses.
            int size = javaSupers.size();
            for (int i = size - 1; i >= 0; i--) {
                Class javaSuper = (Class)javaSupers.get(i);                
                CLID clidSuper = model.getCLID(javaSuper);
                if (clidSuper.isProvisional() && 
                    ! message.containsCLID(clidSuper)) {
                    activateClass(javaSuper, message);
                    message.addCLID(clidSuper);
                }
            }
        }

    }
    /**
     * Writes a request to activate the given state manager's class
     */
    private void activateClass(Class cls, Message message) {
        ActivateClassRequest request =
            rf.getActivateClassRequest(cls, message, pmf);
        try {
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "activateClass", ex); // NOI18N
        }
    }

    /**
    * Dumps information about the store. The provided information
    * depends on the <code>option</code> parameter.  Currently,
    * there are the following options supported: 
    * <ul>
    * <li>DBInfo: Provide information about all classes stored in the
    * database.</li>
    * <li>MetaData: Provide metadata information about the class
    * <code>name</code>.</li>
    * </ul>
    * @param option Dump option, specifies the kind of information.
    * @param name Optional fully qualified classname.
    * @see org.apache.jdo.impl.fostore.DumpOption
    */
    public String dump(DumpOption option, String name) {
        Message message = connector.getMessage();
        try {
            DumpRequest request =
                rf.getDumpRequest(option, name, message, pmf);
            request.doRequest();
            connector.flush();
            return request.getDump();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "dump", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FOStoreFatalInternalException(
                getClass(), "dump", ex); // NOI18N
        }
    }

    /**
     * Get instances for oids
     * @param oids List of oids
     * @param start Starting index within <code>oids</code> of oids whose
     * instances are to be returned.
     * @param numInstances Number of instances to return.
     * @param pm PersistenceManagerInternal on whose behalf the instances are
     * being obtained.
     * @param cls Candidate Class for which instances are being obtained.
     * @return ArrayList of instances corresponding to
     * <code>numInstances</code> of oids in the <code>oids</code> parameter,
     * starting at <code>start</code>.
     */
    synchronized ArrayList getInstances(
        ArrayList oids, int start, int numInstances,
        PersistenceManagerInternal pm, Class cls) {

        ArrayList rc = null;
        Message message = connector.getMessage();
        try {
            GetInstancesRequest request =
                rf.getGetInstancesRequest(oids, start, numInstances,
                                          message, pm, cls);
            request.doRequest();
            connector.flush();
            rc = request.getInstances();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "getInstances", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "getInstances", ex); // NOI18N
        }
        return rc;
    }

    //
    // Implementation detail
    //
    
    /**
     * Verifies existence or values of a state manager's object in the
     * database.
     * @param sm The state manager whose object is to be verified.
     * @param verifyFields If true, verify values of object, otherwise verify
     * only existence (and ignore remaining parameters).
     * @param fieldsToVerify Set of fields to be verified against those in the
     * database.
     * @return true if verify was successful (either by existence of value
     * matching as per verifyFields).
     */
    private boolean verify(StateManagerInternal sm,
                           boolean verifyFields, BitSet fieldsToVerify) {

        boolean rc = false;
        Message message = connector.getMessage();
        try {
            VerifyRequest request =
                rf.getVerifyRequest(sm, message, pmf,
                                    verifyFields, fieldsToVerify);
            request.doRequest();
            connector.flush();
            rc = request.getVerified();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "verify", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "verify", ex); // NOI18N
        }
        return rc; //success
    }

    /**
     * Write a CommitRequest and flush the connector, to cause all
     * CreateOid and ActivateClass requests to be committed in the
     * database *before* any inserts, updates, or deletes.
     */
    protected void preFlush() {
        Message message = connector.getMessage();
        CommitRequest request = rf.getCommitRequest(message, pmf);
        if (logger.isDebugEnabled()) {
            logger.debug("FOSRM.preFlush"); // NOI18N
        }
        try {
            request.setOkToReleaseDatabase(false);
            request.doRequest();
            connector.flush();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "flush", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "flush", ex); // NOI18N
        }
    }
}
