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
 * PersistenceManager.java
 *
 */
 
package javax.jdo;

import java.util.Collection;

import javax.jdo.datastore.JDOConnection;
import javax.jdo.datastore.Sequence;

import javax.jdo.listener.InstanceLifecycleListener;

/** <code>PersistenceManager</code> is the primary interface for JDO-aware application
 * components.  It is the factory for <code>Query</code> and <code>Transaction</code> instances,
 * and contains methods to manage the life cycle of <code>PersistenceCapable</code>
 * instances.
 *
 * <P>A <code>PersistenceManager</code> is obtained from the
 * {@link PersistenceManagerFactory}
 * (recommended) or by construction.
 * @version 2.0
 */

public interface PersistenceManager {
    /** 
     * A <code>PersistenceManager</code> instance can be used until it is closed.
     * @return <code>true</code> if this <code>PersistenceManager</code> has been closed.
     * @see #close()
     */
    boolean isClosed ();
    
    /** Close this <code>PersistenceManager</code> so that no further requests may be 
     * made on it.  A <code>PersistenceManager</code> instance can be used 
     * only until it is closed.
     *
     * <P>Closing a <code>PersistenceManager</code> might release it to the pool of available
     * <code>PersistenceManager</code>s, or might be garbage collected, at the option of
     * the JDO implementation.  Before being used again to satisfy a
     * <code>getPersistenceManager()</code> request, the default values for options will
     * be restored to their values as specified in the <code>PersistenceManagerFactory</code>.
     *
     * <P>This method closes the <code>PersistenceManager</code>.
     */
    void close ();

    /** Return the <code>Transaction</code> instance associated with a <code>PersistenceManager</code>.
     * There is one <code>Transaction</code> instance associated with each <code>PersistenceManager</code>
     * instance.  The <code>Transaction</code> instance supports options as well as
     * transaction completion requests.
     * @return the <code>Transaction</code> associated with this
     * <code>PersistenceManager</code>.
     */
    Transaction currentTransaction();

    /** Mark an instance as no longer needed in the cache.
     * Eviction is normally done automatically by the <code>PersistenceManager</code>
     * at transaction completion.  This method allows the application to
     * explicitly provide a hint to the <code>PersistenceManager</code> that the instance
     * is no longer needed in the cache.
     * @param pc the instance to evict from the cache.
     */
    void evict (Object pc);
    
    /** Mark an array of instances as no longer needed in the cache.
     * @see #evict(Object pc)
     * @param pcs the array of instances to evict from the cache.
     */
    void evictAll (Object[] pcs);
    
    /** Mark a <code>Collection</code> of instances as no longer needed in the cache.
     * @see #evict(Object pc)
     * @param pcs the <code>Collection</code> of instances to evict from the cache.
     */
    void evictAll (Collection pcs);
    
    /** Mark all persistent-nontransactional instances as no longer needed 
     * in the cache.  It transitions
     * all persistent-nontransactional instances to hollow.  Transactional
     * instances are subject to eviction based on the RetainValues setting.
     * @see #evict(Object pc)
     */
    void evictAll ();
    
    /** Refresh the state of the instance from the data store.
     *
     * <P>In an optimistic transaction, the state of instances in the cache
     * might not match the state in the data store.  This method is used to
     * reload the state of the instance from the data store so that a subsequent
     * commit is more likely to succeed.
     * <P>Outside a transaction, this method will refresh nontransactional state.
     * @param pc the instance to refresh.
     */
    void refresh (Object pc);
    
    /** Refresh the state of an array of instances from the data store.
     *
     * @see #refresh(Object pc)
     * @param pcs the array of instances to refresh.
     */
    void refreshAll (Object[] pcs);
    
    /** Refresh the state of a <code>Collection</code> of instances from the data store.
     *
     * @see #refresh(Object pc)
     * @param pcs the <code>Collection</code> of instances to refresh.
     */
    void refreshAll (Collection pcs);
    
    /** Refresh the state of all applicable instances from the data store.
     * <P>If called with an active transaction, all transactional instances
     * will be refreshed.  If called outside an active transaction, all
     * nontransactional instances will be refreshed.
     * @see #refresh(Object pc)
     */
    void refreshAll ();

    /**
     * Refreshes all instances in the exception that failed verification.
     *
     * @since 2.0
     */
    void refreshAll (JDOException jdoe);
    
    /** Create a new <code>Query</code> with no elements.
     * @return the new <code>Query</code>.
     */
    Query newQuery ();
    
    /** Create a new <code>Query</code> using elements from another <code>Query</code>.
     * The other <code>Query</code> must have been created by the same JDO implementation.
     * It might be active
     * in a different <code>PersistenceManager</code> or might have been serialized and restored.
     * <P>All of the settings of the other <code>Query</code> are copied to this <code>Query</code>,
     * except for the candidate <code>Collection</code> or <code>Extent</code>.
     * @return the new <code>Query</code>
     * @param compiled another <code>Query</code> from the same JDO implementation
     */
    Query newQuery (Object compiled);
    
    /** Create a Construct a new query instance using the specified String 
     * as the single-string representation of the query.
     * @param query the single-string query
     * @return the new <code>Query</code>
     * @since 2.0
     */
    Query newQuery (String query);
    
    /** Create a new <code>Query</code> using the specified language.
     * @param language the language of the query parameter
     * @param query the query, which is of a form determined by the language
     * @return the new <code>Query</code>
     */    
    Query newQuery (String language, Object query);
    
    /** Create a new <code>Query</code> specifying the <code>Class</code> of the candidate instances.
     * @param cls the <code>Class</code> of the candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Class cls);
    
    /** Create a new <code>Query</code> with the <code>Class</code> of the
     * candidate instances and candidate <code>Extent</code>.
     * @param cln the <code>Extent</code> of candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Extent cln);
    
    /** Create a new <code>Query</code> with the candidate <code>Class</code> 
     * and <code>Collection</code>.
     * @param cls the <code>Class</code> of results
     * @param cln the <code>Collection</code> of candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Class cls, Collection cln);
    
    /** Create a new <code>Query</code> with the <code>Class</code> of the
     * candidate instances and filter.
     * @param cls the <code>Class</code> of results
     * @param filter the filter for candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Class cls, String filter);
    
    /** Create a new <code>Query</code> with the <code>Class</code> of the candidate instances, 
     * candidate <code>Collection</code>, and filter.
     * @param cls the <code>Class</code> of candidate instances
     * @param cln the <code>Collection</code> of candidate instances
     * @param filter the filter for candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Class cls, Collection cln, String filter);
    
    /** Create a new <code>Query</code> with the
     * candidate <code>Extent</code> and filter; the class
     * is taken from the <code>Extent</code>.
     * @param cln the <code>Extent</code> of candidate instances
     * @param filter the filter for candidate instances
     * @return the new <code>Query</code>
     */
    Query newQuery (Extent cln, String filter);

    /**
     * Create a new <code>Query</code> with the given candidate class
     * from a named query. The query name given must be the name of a
     * query defined in metadata.
     * @param cls the <code>Class</code> of candidate instances
     * @param queryName the name of the query to look up in metadata
     * @return the new <code>Query</code>
     */
    Query newNamedQuery (Class cls, String queryName);

    /** The <code>PersistenceManager</code> manages a collection of instances in the data
     * store based on the class of the instances.  This method returns an
     * <code>Extent</code> of instances in the data store that might be iterated or
     * given to a <code>Query</code>.  The <code>Extent</code> itself might not reference any 
     * instances, but only hold the class name and an
     * indicator as to whether subclasses are included in the <code>Extent</code>.
     * <P>Note that the <code>Extent</code> might be very large.
     * @param persistenceCapableClass <code>Class</code> of instances
     * @param subclasses whether to include instances of subclasses
     * @return an <code>Extent</code> of the specified <code>Class</code>
     * @see Query
     */
    Extent getExtent (Class persistenceCapableClass, boolean subclasses);

    /**
     * Equivalent to <code>getExtent (persistenceCapableClass,
     * true)</code>.
     * @see #getExtent(Class,boolean)
     * @since 2.0
     */
    Extent getExtent (Class persistenceCapableClass);

    /** This method locates a persistent instance in the cache of instances
     * managed by this <code>PersistenceManager</code>.
     * The <code>getObjectById</code> method attempts 
     * to find an instance in the cache with the specified JDO identity. 
     * The <code>oid</code> parameter object might have been returned by an earlier call 
     * to <code>getObjectId</code> or <code>getTransactionalObjectId</code>,
     * or might have been constructed by the application. 
     * <P>If the <code>PersistenceManager</code> is unable to resolve the <code>oid</code> parameter 
     * to an ObjectId instance, then it throws a <code>JDOUserException</code>.
     * <P>If the <code>validate</code> flag is <code>false</code>, and there is already an instance in the
     * cache with the same JDO identity as the <code>oid</code> parameter, then this method
     * returns it. There is no change made to the state of the returned
     * instance.
     * <P>If there is not an instance already in the cache with the same JDO
     * identity as the <code>oid</code> parameter, then this method creates an instance
     * with the specified JDO identity and returns it. If there is no
     * transaction in progress, the returned instance will be hollow or
     * persistent-nontransactional, at the choice of the implementation.
     * <P>If there is a transaction in progress, the returned instance will
     * be hollow, persistent-nontransactional, or persistent-clean, at the
     * choice of the implementation.
     * <P>It is an implementation decision whether to access the data store,
     * if required to determine the exact class. This will be the case of
     * inheritance, where multiple <code>PersistenceCapable</code> classes share the
     * same ObjectId class.
     * <P>If the validate flag is <code>false</code>, and the instance does not exist in
     * the data store, then this method might not fail. It is an
     * implementation choice whether to fail immediately with a
     * <code>JDODataStoreException</code>. But a subsequent access of the fields of the
     * instance will throw a <code>JDODataStoreException</code> if the instance does not
     * exist at that time. Further, if a relationship is established to this
     * instance, then the transaction in which the association was made will
     * fail.
     * <P>If the <code>validate</code> flag is <code>true</code>, and there is already a transactional
     * instance in the cache with the same JDO identity as the <code>oid</code> parameter,
     * then this method returns it. There is no change made to the state of
     * the returned instance.
     * <P>If there is an instance already in the cache with the same JDO
     * identity as the <code>oid</code> parameter, but the instance is not transactional,
     * then it must be verified in the data store. If the instance does not
     * exist in the datastore, then a <code>JDODataStoreException</code> is thrown.
     * <P>If there is not an instance already in the cache with the same JDO
     * identity as the <code>oid</code> parameter, then this method creates an instance
     * with the specified JDO identity, verifies that it exists in the data
     * store, and returns it. If there is no transaction in progress, the
     * returned instance will be hollow or persistent-nontransactional,
     * at the choice of the implementation.
     * <P>If there is a data store transaction in progress, the returned
     * instance will be persistent-clean.
     * If there is an optimistic transaction in progress, the returned
     * instance will be persistent-nontransactional.
     * @see #getObjectId(Object pc)
     * @see #getTransactionalObjectId(Object pc)
     * @return the <code>PersistenceCapable</code> instance with the specified ObjectId
     * @param oid an ObjectId
     * @param validate if the existence of the instance is to be validated
     */
    Object getObjectById (Object oid, boolean validate);

    /**
     * Looks up the instance of the given type with the given key.
     * @param cls The type of object to load
     * @param key either the string representation of the object id, or
     * an object representation of a single field identity key
     * @return the corresponding persistent instance
     * @since 2.0
     */
    Object getObjectById (Class cls, Object key);

    /**
     * Looks up the instance corresponding to the specified oid. This is
     * equivalent to <code>getObjectById(oid, true);
     * @param oid The object id of the object to load
     * @return the corresponding persistent instance
     */
    Object getObjectById (Object oid);

    /** The ObjectId returned by this method represents the JDO identity of
     * the instance.  The ObjectId is a copy (clone) of the internal state
     * of the instance, and changing it does not affect the JDO identity of
     * the instance.  
     * <P>The <code>getObjectId</code> method returns an ObjectId instance that represents
     * the object identity of the specified JDO instance. The identity is
     * guaranteed to be unique only in the context of the JDO
     * <code>PersistenceManager</code> that created the identity, and only for two types
     * of JDO Identity: those that are managed by the application, and
     * those that are managed by the data store.
     * <P>If the object identity is being changed in the transaction, by the
     * application modifying one or more of the application key fields,
     * then this method returns the identity as of the beginning of the
     * transaction. The value returned by <code>getObjectId</code> will be different
     * following <code>afterCompletion</code> processing for successful transactions.
     * <P>Within a transaction, the ObjectId returned will compare equal to
     * the ObjectId returned by only one among all JDO instances associated
     * with the <code>PersistenceManager</code> regardless of the type of ObjectId.
     * <P>The ObjectId does not necessarily contain any internal state of the
     * instance, nor is it necessarily an instance of the class used to
     * manage identity internally. Therefore, if the application makes a
     * change to the ObjectId instance returned by this method, there is
     * no effect on the instance from which the ObjectId was obtained.
     * <P>The <code>getObjectById</code> method can be used between instances of
     * <code>PersistenceManager</code> of different JDO vendors only for instances of
     * persistence capable classes using application-managed (primary key)
     * JDO identity. If it is used for instances of classes using datastore
     * identity, the method might succeed, but there are no guarantees that
     * the parameter and return instances are related in any way.
     * @see #getTransactionalObjectId(Object pc)
     * @see #getObjectById(Object oid, boolean validate)
     * @param pc the <code>PersistenceCapable</code> instance
     * @return the ObjectId of the instance
     */
    Object getObjectId (Object pc);
    
    /** The ObjectId returned by this method represents the JDO identity of
     * the instance.  The ObjectId is a copy (clone) of the internal state
     * of the instance, and changing it does not affect the JDO identity of
     * the instance.
     * <P>If the object identity is being changed in the transaction, by the
     * application modifying one or more of the application key fields,
     * then this method returns the current identity in the transaction.
     * <P>If there is no transaction in progress, or if none of the key fields
     * is being modified, then this method will return the same value as
     * <code>getObjectId</code>.
     * @see #getObjectId(Object pc)
     * @see #getObjectById(Object oid, boolean validate)
     * @param pc a <code>PersistenceCapable</code> instance
     * @return the ObjectId of the instance
     */
    Object getTransactionalObjectId (Object pc);

    /** 
     * This method returns an object id instance corresponding to the pcClass
     * and key arguments.
     * @param pcClass the <code>Class</code> of the persistence-capable instance
     * @param key the value of the key field for single-field identity.
     * @return an instance of the object identity class
     */
    Object newObjectIdInstance (Class pcClass, Object key);
    
    /**
     * Return the objects with the given oids.
     * @param oids the oids of the objects to return
     * @param validate if true, the existance of the objects in
     *     the datastore will be validated.
     * @return the objects that were looked up, in the
     *     same order as the oids parameter.
     * @see #getObjectById(Object,boolean)
     * @since 2.0
     */
    Collection getObjectsById (Collection oids, boolean validate);

    /**
     * Return the objects with the given oids. This method is equivalent 
     * to calling {@link #getObjectsById(Collection, boolean)}
     * with the validate flag true.
     * @param oids the oids of the objects to return
     * @return the objects that were looked up, in the
     *     same order as the oids parameter.
     * @see #getObjectsById(Collection,boolean)
     * @since 2.0
     */
    Collection getObjectsById (Collection oids);

    /**
     * Return the objects with the given oids.
     * @param oids the oids of the objects to return
     * @param validate if true, the existance of the objects in
     *     the datastore will be validated.
     * @return the objects that were looked up, in the
     *     same order as the oids parameter.
     * @see #getObjectById(Object,boolean)
     * @since 2.0
     */
    Object[] getObjectsById (Object[] oids, boolean validate);

    /**
     * Return the objects with the given oids. This method is equivalent
     * to calling {@link #getObjectsById(Object[],boolean)} 
     * with the validate flag true.
     * @param oids the oids of the objects to return
     * @return the objects that were looked up, in the
     *     same order as the oids parameter.
     * @see #getObjectsById(Object[],boolean)
     * @since 2.0
     */
    Object[] getObjectsById (Object[] oids);

    /** Make the transient instance persistent in this <code>PersistenceManager</code>.
     * This method must be called in an active transaction.
     * The <code>PersistenceManager</code> assigns an ObjectId to the instance and
     * transitions it to persistent-new.
     * The instance will be managed in the <code>Extent</code> associated with its <code>Class</code>.
     * The instance will be put into the data store at commit.
     * The closure of instances of <code>PersistenceCapable</code> classes
     * reachable from persistent
     * fields will be made persistent at commit.  [This is known as 
     * persistence by reachability.]
     * @param pc a transient instance of a <code>Class</code> that implements
     * <code>PersistenceCapable</code>
     */
    void makePersistent (Object pc);
    
    /** Make an array of instances persistent.
     * @param pcs an array of transient instances
     * @see #makePersistent(Object pc)
     */
    void makePersistentAll (Object[] pcs);
    
    /** Make a <code>Collection</code> of instances persistent.
     * @param pcs a <code>Collection</code> of transient instances
     * @see #makePersistent(Object pc)
     */
    void makePersistentAll (Collection pcs);
    
    /** Delete the persistent instance from the data store.
     * This method must be called in an active transaction.
     * The data store object will be removed at commit.
     * Unlike <code>makePersistent</code>, which makes the closure of the instance persistent,
     * the closure of the instance is not deleted from the data store.
     * This method has no effect if the instance is already deleted in the
     * current transaction.
     * This method throws <code>JDOUserException</code> if the instance is transient or 
     * is managed by another <code>PersistenceManager</code>.
     *
     * @param pc a persistent instance
     */
    void deletePersistent (Object pc);
    
    /** Delete an array of instances from the data store.
     * @param pcs a <code>Collection</code> of persistent instances
     * @see #deletePersistent(Object pc)
     */
    void deletePersistentAll (Object[] pcs);
    
    /** Delete a <code>Collection</code> of instances from the data store.
     * @param pcs a <code>Collection</code> of persistent instances
     * @see #deletePersistent(Object pc)
     */
    void deletePersistentAll (Collection pcs);
    
    /** Make an instance transient, removing it from management by this
     * <code>PersistenceManager</code>.
     *
     * <P>The instance loses its JDO identity and it is no longer associated
     * with any <code>PersistenceManager</code>.  The state of fields is preserved unchanged.
     * @param pc the instance to make transient.
     */
    void makeTransient (Object pc);
    
    /** Make an array of instances transient, removing them from management by this
     * <code>PersistenceManager</code>.
     *
     * <P>The instances lose their JDO identity and they are no longer associated
     * with any <code>PersistenceManager</code>.  The state of fields is preserved unchanged.
     * @param pcs the instances to make transient.
     */
    void makeTransientAll (Object[] pcs);
    
    /** Make a <code>Collection</code> of instances transient, removing them from
     * management by this <code>PersistenceManager</code>.
     *
     * <P>The instances lose their JDO identity and they are no longer associated
     * with any <code>PersistenceManager</code>.  The state of fields is preserved unchanged.
     * @param pcs the instances to make transient.
     */ 
    void makeTransientAll (Collection pcs);
    
    /** Make an instance subject to transactional boundaries.
     *
     * <P>Transient instances normally do not observe transaction boundaries.
     * This method makes transient instances sensitive to transaction completion.
     * If an instance is modified in a transaction, and the transaction rolls back,
     * the state of the instance is restored to the state before the first change
     * in the transaction.
     *
     * <P>For persistent instances read in optimistic transactions, this method
     * allows the application to make the state of the instance part of the
     * transactional state.  At transaction commit, the state of the instance in
     * the cache is compared to the state of the instance in the data store.  If they
     * are not the same, then an exception is thrown.
     * @param pc the instance to make transactional.
     */
    void makeTransactional (Object pc);

    /** Make an array of instances subject to transactional boundaries.
     * @param pcs the array of instances to make transactional.
     * @see #makeTransactional(Object pc)
     */
    void makeTransactionalAll (Object[] pcs);

    /** Make a <code>Collection</code> of instances subject to transactional boundaries.
     * @param pcs the <code>Collection</code> of instances to make transactional.
     * @see #makeTransactional(Object pc)
     */
    void makeTransactionalAll (Collection pcs);
    
    /** Make an instance non-transactional after commit.
     *
     * <P>Normally, at transaction completion, instances are evicted from the
     * cache.  This method allows an application to identify an instance as
     * not being evicted from the cache at transaction completion.  Instead,
     * the instance remains in the cache with nontransactional state.
     *
     * @param pc the instance to make nontransactional.
     */
    void makeNontransactional (Object pc);
    
    /** Make an array of instances non-transactional after commit.
     *
     * @param pcs the array of instances to make nontransactional.
     * @see #makeNontransactional(Object pc)
     */
    void makeNontransactionalAll (Object[] pcs);
    
    /** Make a <code>Collection</code> of instances non-transactional after commit.
     *
     * @param pcs the <code>Collection</code> of instances to make nontransactional.
     * @see #makeNontransactional(Object pc)
     */
    void makeNontransactionalAll (Collection pcs);
    
    /** Retrieve field values of an instance from the store.  This tells
     * the <code>PersistenceManager</code> that the application intends to use the
     * instance, and its field values must be retrieved.
     * <P>The <code>PersistenceManager</code> might use policy information about the
     * class to retrieve associated instances.
     * @param pc the instance
     */
    void retrieve (Object pc);
    
    /** Retrieve field values of instances from the store.  This tells
     * the <code>PersistenceManager</code> that the application intends to use the
     * instances, and all field values must be retrieved.
     * <P>The <code>PersistenceManager</code> might use policy information about the
     * class to retrieve associated instances.
     * @param pcs the instances
     */
    void retrieveAll (Collection pcs);
    
    /** Retrieve field values of instances from the store.  This tells
     * the <code>PersistenceManager</code> that the application intends to use the
     * instances, and their field values should be retrieved.  The fields
     * in the default fetch group must be retrieved, and the implementation
     * might retrieve more fields than the default fetch group.
     * <P>The <code>PersistenceManager</code> might use policy information about the
     * class to retrieve associated instances.
     * @param pcs the instances
     * @param DFGOnly whether to retrieve only the default fetch group fields
     * @since 1.0.1
     */
    void retrieveAll (Collection pcs, boolean DFGOnly);
    
    /** Retrieve field values of instances from the store.  This tells
     * the <code>PersistenceManager</code> that the application intends to use the
     * instances, and all field values must be retrieved.
     * <P>The <code>PersistenceManager</code> might use policy information about the
     * class to retrieve associated instances.
     * @param pcs the instances
     */
    void retrieveAll (Object[] pcs);
           
    /** Retrieve field values of instances from the store.  This tells
     * the <code>PersistenceManager</code> that the application intends to use the
     * instances, and their field values should be retrieved.  The fields
     * in the default fetch group must be retrieved, and the implementation
     * might retrieve more fields than the default fetch group.
     * <P>The <code>PersistenceManager</code> might use policy information about the
     * class to retrieve associated instances.
     * @param pcs the instances
     * @param DFGOnly whether to retrieve only the default fetch group fields
     * @since 1.0.1
     */
    void retrieveAll (Object[] pcs, boolean DFGOnly);
           
    /** The application can manage the <code>PersistenceManager</code> instances
     * more easily by having an application object associated with each
     * <code>PersistenceManager</code> instance.
     * @param o the user instance to be remembered by the <code>PersistenceManager</code>
     * @see #getUserObject
     */
    void setUserObject (Object o);
    
    /** The application can manage the <code>PersistenceManager</code> instances
     * more easily by having an application object associated with each
     * <code>PersistenceManager</code> instance.
     * @return the user object associated with this <code>PersistenceManager</code>
     * @see #setUserObject
     */
    Object getUserObject ();
     
    /** This method returns the <code>PersistenceManagerFactory</code> used to create
     * this <code>PersistenceManager</code>.  
     * @return the <code>PersistenceManagerFactory</code> that created
     * this <code>PersistenceManager</code>
     */
    PersistenceManagerFactory getPersistenceManagerFactory();

    /** Return the <code>Class</code> that implements the JDO Identity for the
     * specified <code>PersistenceCapable</code> class.  The application can use the
     * returned <code>Class</code> to construct a JDO Identity instance for
     * application identity <code>PersistenceCapable</code> classes.  This JDO Identity
     * instance can then be used to get an instance of the
     * <code>PersistenceCapable</code> class for use in the application.
     *
     * <P>In order for the application to construct an instance of the ObjectId class
     * it needs to know the class being used by the JDO implementation.
     * @param cls the <code>PersistenceCapable Class</code>
     * @return the <code>Class</code> of the ObjectId of the parameter
     * @see #getObjectById
     */
    Class getObjectIdClass(Class cls);
  
    /** Set the Multithreaded flag for this <code>PersistenceManager</code>.  Applications
     * that use multiple threads to invoke methods or access fields from 
     * instances managed by this <code>PersistenceManager</code> must set this flag to <code>true</code>.
     * Instances managed by this <code>PersistenceManager</code> include persistent or
     * transactional instances of <code>PersistenceCapable</code> classes, as well as 
     * helper instances such as <code>Query</code>, <code>Transaction</code>, or <code>Extent</code>.
     * @param flag the Multithreaded setting.
     */
    void setMultithreaded (boolean flag);
  
    /** Get the current Multithreaded flag for this <code>PersistenceManager</code>.  
     * @see #setMultithreaded
     * @return the Multithreaded setting.
     */
    boolean getMultithreaded();
    
    /** Set the ignoreCache parameter for queries.
     *
     * <P>IgnoreCache set to <code>true</code> specifies that for all <code>Query</code> instances created by this
     * <code>PersistenceManager</code>, the default is the cache should be ignored for queries.
     * @param flag the ignoreCache setting.
     */
    void setIgnoreCache(boolean flag);
  
    /** Get the ignoreCache setting for queries.
     *
     * <P>IgnoreCache set to <code>true</code> specifies that for all <code>Query</code> instances created by this
     * <code>PersistenceManager</code>, the default is the cache should be ignored for queries.
     * @return the ignoreCache setting.
     */
   boolean getIgnoreCache();
    /**
     * Detach the specified object from the <code>PersistenceManager</code>.
     * @param pc the instance to detach
     * @return the detached instance
     * @see #detachCopyAll(Object[])
     * @since 2.0
     */
    Object detachCopy (Object pc);

    /**
     * Detach the specified objects from the <code>PersistenceManager</code>.
     * @param pcs the instances to detach
     * @return the detached instances
     * @see #detachCopyAll(Object[])
     * @since 2.0
     */
    Collection detachCopyAll (Collection pcs);

    /**
     * Detach the specified objects from the
     * <code>PersistenceManager</code>. The objects returned can be
     * manipulated and re-attached with 
     * {@link #attachCopyAll(Object[], boolean)}. 
     * The detached instances will be
     * unmanaged copies of the specified parameters, and are suitable
     * for serialization and manipulation outside of a JDO
     * environment. When detaching instances, only fields in the
     * current {@link FetchPlan} will be traversed. Thus, to detach a
     * graph of objects, relations to other persistent instances must
     * either be in the <code>default-fetch-group</code>, or in the
     * current custom {@link FetchPlan}.
     * @param pcs the instances to detach
     * @return the detached instances
     * @throws JDOUserException if any of the instances do not
     * @see #attachCopyAll(Object[], boolean)
     * @see #getFetchPlan
     * @since 2.0
     */
    Object[] detachCopyAll (Object [] pcs);

    /**
     * Import the specified object into the
     * <code>PersistenceManager</code>.
     * @param pc instance to import
     * @param makeTransactional if <code>true</code>, this method will
     *     mark transactional the persistent instances corresponding
     *     to all instances in the closure of the detached graph.
     * @return the re-attached instance
     * @see #attachCopyAll(Object[],boolean)
     * @since		2.0
     */
    Object attachCopy (Object pc, boolean makeTransactional);

    /**
     * Import the specified objects into the
     * <code>PersistenceManager</code>.
     * @param pcs Collection of instances to import
     * @param makeTransactional if <code>true</code>, this method will
     *     mark transactional the persistent instances corresponding
     *     to all instances in the closure of the detached graph.
     * @return the re-attached instances
     * @see #attachCopyAll(Object[],boolean)
     * @since 2.0
     */
    Collection attachCopyAll (Collection pcs, boolean makeTransactional);

    /**
     * Import the specified objects into the
     * <code>PersistenceManager</code>. Instances that were
     * previously detached from this or another
     * <code>PersistenceManager</code> will have their changed merged
     * into the persistent instances. Instances that are new will be
     * persisted as new instances.
     * @param pcs array of instances to import
     * @param makeTransactional	if <code>true</code>, this method will
     *     mark transactional the persistent instances corresponding
     *     to all instances in the closure of the detached graph.
     * @return the re-attached instances
     * @see #detachCopyAll(Object[])
     * @since 2.0
     */
    Object[] attachCopyAll (Object[] pcs, boolean makeTransactional);

    /**
     * Put the specified key-value pair into the map of user objects.
     * @since 2.0
     */
    Object putUserObject (Object key, Object val);

    /**
     * Get the value for the specified key from the map of user objects.
     * @param key the key of the object to be returned
     * @return the object 
     * @since 2.0
     */
    Object getUserObject (Object key);

    /**
     * Remove the specified key and its value from the map of user objects.
     * @param key the key of the object to be removed
     * @since 2.0
     */
    Object removeUserObject (Object key);

    /**
     * Flushes all dirty, new, and deleted instances to the data
     * store. It has no effect if a transaction is not active.
     * <p>If a datastore transaction is active, this method
     * synchronizes the cache with the datastore and reports any
     * exceptions.</p>
     * <p>If an optimistic transaction is active, this method obtains
     * a datastore connection, synchronizes the cache with the
     * datastore using this connection and reports any
     * exceptions. The connection obtained by this method is held
     * until the end of the transaction.</p>
     * <p>If exceptions occur during flush, the implementation will
     * set the current transaction's <code>RollbackOnly</code> flag
     * (see {@link Transaction#setRollbackOnly}).</p>
     * @since	2.0
     */
    void flush ();

    /**
     * Validates the <code>PersistenceManager</code> cache with the
     * datastore. This method has no effect if a transaction is not
     * active.
     * <p>If a datastore transaction is active, this method verifies
     * the consistency of instances in the cache against the
     * datastore. An implementation might flush instances as if
     * {@link #flush} were called, but it is not required to do
     * so.</p>
     * <p>If an optimistic transaction is active, this method obtains
     * a datastore connection and verifies the consistency of the
     * instances in the cache against the datastore. If any
     * inconsistencies are detected, a {@link
     * JDOOptimisticVerificationException} is thrown. This exception
     * contains a nested {@link JDOOptimisticVerificationException}
     * for each object that failed the consistency check. No
     * datastore resources acquired during the execution of this
     * method are held beyond the scope of this method.</p>
     * @since 2.0
     */
    void checkConsistency ();

    /**
     * Returns the <code>FetchPlan</code> used by this
     * <code>PersistenceManager</code>.
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan getFetchPlan ();

    /**
     * Creates an instance of a persistence-capable interface or
     * abstract class. The returned instance is transient.
     * @param pcClass Must be an abstract class or interface 
     *     that is declared in the metadata.
     * @return the created instance
     * @since 2.0
     */
    Object newInstance (Class pcClass);

    /**
     * Returns the sequence identified by <code>name</code>.
     * @param name the name of the Sequence
     * @return the Sequence
     * @since 2.0
     */
    Sequence getSequence (String name);

    /**
     * If this method is called while a datastore transaction is
     * active, the object returned will be enlisted in the current
     * transaction. If called in an optimistic transaction or outside
     * an active transaction, the object returned will not be
     * enlisted in any transaction.
     * @return the JDOConnection instance
     * @since 2.0
     */
    JDOConnection getDataStoreConnection ();

    /**
     * Adds the listener instance to the list of lifecycle event
     * listeners. The <code>classes</code> parameter identifies all
     * of the classes of interest. If the <code>classes</code>
     * parameter is specified as <code>null</code>, events for all
     * persistent classes and interfaces will be sent to
     * <code>listenerInstance</code>.
     * <p>The listenerInstance will be called for each event for which it
     * implements the corresponding listenerInstance interface.</p>
     * @param listener the lifecycle listener
     * @param classes the classes of interest to the listener
     * @since 2.0
     */
    void addInstanceLifecycleListener (InstanceLifecycleListener listener,
        Class[] classes);

    /**
     * Removes the listener instance from the list of lifecycle event listeners.
     * @param listener the listener instance to be removed
     * @since 2.0
     */
    void removeInstanceLifecycleListener (InstanceLifecycleListener listener);
}
