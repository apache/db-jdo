/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * PersistenceManagerFactory.java
 *
 */

package javax.jdo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.metadata.JDOMetadata;
import javax.jdo.metadata.TypeMetadata;
import javax.jdo.spi.JDOPermission;

/**
 * The <code>PersistenceManagerFactory</code> is the interface to use to obtain <code>
 * PersistenceManager</code> instances. All <code>PersistenceManager</code> instances obtained from
 * the same <code>PersistenceManagerFactory</code> will have the same default properties.
 *
 * <p><code>PersistenceManagerFactory</code> instances may be configured and serialized for later
 * use. They may be stored via JNDI and looked up and used later. Any properties configured will be
 * saved and restored.
 *
 * <p>Once the first <code>PersistenceManager</code> is obtained from the <code>
 * PersistenceManagerFactory</code>, the factory can no longer be configured.
 *
 * <p>If the <code>ConnectionFactory</code> property is set (non-<code>null</code>) then all other
 * Connection properties including <code>ConnectionFactoryName</code> are ignored; otherwise, if
 * <code>ConnectionFactoryName</code> is set (non-<code>null</code>) then all other Connection
 * properties are ignored. Similarly, if the <code>ConnectionFactory2</code> property is set (non-
 * <code>null</code>) then <code>ConnectionFactory2Name</code> is ignored.
 *
 * <p>Operational state (<code>PersistenceManager</code> pooling, connection pooling, operational
 * parameters) must not be serialized.
 *
 * @version 2.2
 */
public interface PersistenceManagerFactory extends Serializable {

  /**
   * Close this PersistenceManagerFactory. Check for JDOPermission("closePersistenceManagerFactory")
   * and if not authorized, throw SecurityException.
   *
   * <p>If the authorization check succeeds, check to see that all PersistenceManager instances
   * obtained from this PersistenceManagerFactory have no active transactions. If any
   * PersistenceManager instances have an active transaction, throw a JDOUserException, with one
   * nested JDOUserException for each PersistenceManager with an active Transaction.
   *
   * <p>If there are no active transactions, then close all PersistenceManager instances obtained
   * from this PersistenceManagerFactory, mark this PersistenceManagerFactory as closed, disallow
   * getPersistenceManager methods, and allow all other get methods. If a set method or
   * getPersistenceManager method is called after close, then JDOUserException is thrown.
   *
   * @since 1.0.1
   */
  void close();

  /**
   * A <code>PersistenceManagerFactory</code> instance can be used until it is closed.
   *
   * @return <code>true</code> if this <code>PersistenceManagerFactory</code> has been closed.
   * @see #close()
   * @since 2.0
   */
  boolean isClosed();

  /**
   * Get an instance of <code>PersistenceManager</code> from this factory. The instance has default
   * values for options.
   *
   * <p>After the first use of <code>getPersistenceManager</code>, no "set" methods will succeed.
   *
   * @return a <code>PersistenceManager</code> instance with default options.
   */
  PersistenceManager getPersistenceManager();

  /**
   * Get a thread-safe instance of a proxy that dynamically binds on each method call to an instance
   * of <code>PersistenceManager</code>.
   *
   * <p>When used with a <code>PersistenceManagerFactory</code> that uses TransactionType JTA, the
   * proxy can be used in a server to dynamically bind to an instance from this factory associated
   * with the thread's current transaction. In this case, the close method is ignored, as the <code>
   * PersistenceManager</code> is automatically closed when the transaction completes.
   *
   * <p>When used with a <code>PersistenceManagerFactory</code> that uses TransactionType
   * RESOURCE_LOCAL, the proxy uses an inheritable ThreadLocal to bind to an instance of <code>
   * PersistenceManager</code> associated with the thread. In this case, the close method executed
   * on the proxy closes the <code>PersistenceManager</code> and then clears the ThreadLocal. Use of
   * this method does not affect the configurability of the <code>PersistenceManagerFactory</code>.
   *
   * @since 2.1
   * @return a <code>PersistenceManager</code> proxy.
   */
  PersistenceManager getPersistenceManagerProxy();

  /**
   * Get an instance of <code>PersistenceManager</code> from this factory. The instance has default
   * values for options. The parameters <code>userid</code> and <code>password</code> are used when
   * obtaining datastore connections from the connection pool.
   *
   * <p>After the first use of <code>getPersistenceManager</code>, no "set" methods will succeed.
   *
   * @return a <code>PersistenceManager</code> instance with default options.
   * @param userid the userid for the connection
   * @param password the password for the connection
   */
  PersistenceManager getPersistenceManager(String userid, String password);

  /**
   * Set the user name for the data store connection.
   *
   * @param userName the user name for the data store connection.
   */
  void setConnectionUserName(String userName);

  /**
   * Get the user name for the data store connection.
   *
   * @return the user name for the data store connection.
   */
  String getConnectionUserName();

  /**
   * Set the password for the data store connection.
   *
   * @param password the password for the data store connection.
   */
  void setConnectionPassword(String password);

  /**
   * Set the URL for the data store connection.
   *
   * @param url the URL for the data store connection.
   */
  void setConnectionURL(String url);

  /**
   * Get the URL for the data store connection.
   *
   * @return the URL for the data store connection.
   */
  String getConnectionURL();

  /**
   * Set the driver name for the data store connection. This property might be ignored by the JDO
   * implementation because the JDBC DriverManager handles the driver name.
   *
   * @param driverName the driver name for the data store connection.
   */
  void setConnectionDriverName(String driverName);

  /**
   * Get the driver name for the data store connection. This property might be ignored by the JDO
   * implementation because the JDBC DriverManager handles the driver name.
   *
   * @return the driver name for the data store connection.
   */
  String getConnectionDriverName();

  /**
   * Set the name for the data store connection factory.
   *
   * @param connectionFactoryName the name of the data store connection factory.
   */
  void setConnectionFactoryName(String connectionFactoryName);

  /**
   * Get the name for the data store connection factory.
   *
   * @return the name of the data store connection factory.
   */
  String getConnectionFactoryName();

  /**
   * Set the data store connection factory. JDO implementations will support specific connection
   * factories. The connection factory interfaces are not part of the JDO specification.
   *
   * @param connectionFactory the data store connection factory.
   */
  void setConnectionFactory(Object connectionFactory);

  /**
   * Get the data store connection factory.
   *
   * @return the data store connection factory.
   */
  Object getConnectionFactory();

  /**
   * Set the name for the second data store connection factory. This is needed for managed
   * environments to get nontransactional connections for optimistic transactions.
   *
   * @param connectionFactoryName the name of the data store connection factory.
   */
  void setConnectionFactory2Name(String connectionFactoryName);

  /**
   * Get the name for the second data store connection factory. This is needed for managed
   * environments to get nontransactional connections for optimistic transactions.
   *
   * @return the name of the data store connection factory.
   */
  String getConnectionFactory2Name();

  /**
   * Set the second data store connection factory. This is needed for managed environments to get
   * nontransactional connections for optimistic transactions. JDO implementations will support
   * specific connection factories. The connection factory interfaces are not part of the JDO
   * specification.
   *
   * @param connectionFactory the data store connection factory.
   */
  void setConnectionFactory2(Object connectionFactory);

  /**
   * Get the second data store connection factory. This is needed for managed environments to get
   * nontransactional connections for optimistic transactions.
   *
   * @return the data store connection factory.
   */
  Object getConnectionFactory2();

  /**
   * Set the default Multithreaded setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @param flag the default Multithreaded setting.
   */
  void setMultithreaded(boolean flag);

  /**
   * Get the default Multithreaded setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @return the default Multithreaded setting.
   */
  boolean getMultithreaded();

  /**
   * Set the Mapping setting for this factory. This is used to find the object-datastore mapping
   * file(s).
   *
   * @param mapping the Mapping setting.
   */
  void setMapping(String mapping);

  /**
   * Get the Mapping setting for this factory. This is used to find the object-datastore mapping
   * file(s).
   *
   * @return the Mapping setting.
   */
  String getMapping();

  /**
   * Set the default Optimistic setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @param flag the default Optimistic setting.
   */
  void setOptimistic(boolean flag);

  /**
   * Get the default Optimistic setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @return the default Optimistic setting.
   */
  boolean getOptimistic();

  /**
   * Set the default RetainValues setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @param flag the default RetainValues setting.
   */
  void setRetainValues(boolean flag);

  /**
   * Get the default RetainValues setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @return the default RetainValues setting.
   */
  boolean getRetainValues();

  /**
   * Set the default value for the RestoreValues property. If <code>true</code>, at rollback, fields
   * of newly persistent instances are restored to their values as of the beginning of the
   * transaction, and the instances revert to transient. Additionally, fields of modified instances
   * of primitive types and immutable reference types are restored to their values as of the
   * beginning of the transaction.
   *
   * <p>If <code>false</code>, at rollback, the values of fields of newly persistent instances are
   * unchanged and the instances revert to transient. Additionally, dirty instances transition to
   * hollow. If an implementation does not support this option, a <code>
   * JDOUnsupportedOptionException</code> is thrown.
   *
   * @param restoreValues the value of the restoreValues property
   */
  void setRestoreValues(boolean restoreValues);

  /**
   * Get the default value for the RestoreValues property.
   *
   * @return the value of the restoreValues property
   */
  boolean getRestoreValues();

  /**
   * Set the default NontransactionalRead setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @param flag the default NontransactionalRead setting.
   */
  void setNontransactionalRead(boolean flag);

  /**
   * Get the default NontransactionalRead setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @return the default NontransactionalRead setting.
   */
  boolean getNontransactionalRead();

  /**
   * Set the default NontransactionalWrite setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @param flag the default NontransactionalWrite setting.
   */
  void setNontransactionalWrite(boolean flag);

  /**
   * Get the default NontransactionalWrite setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @return the default NontransactionalWrite setting.
   */
  boolean getNontransactionalWrite();

  /**
   * Set the default IgnoreCache setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @param flag the default IgnoreCache setting.
   */
  void setIgnoreCache(boolean flag);

  /**
   * Get the default IgnoreCache setting for all <code>PersistenceManager</code> instances obtained
   * from this factory.
   *
   * @return the default IngoreCache setting.
   */
  boolean getIgnoreCache();

  /**
   * Gets the detachAllOnCommit setting.
   *
   * @see #setDetachAllOnCommit(boolean)
   * @since 2.0
   * @return the default detachAllOnCommit setting.
   */
  boolean getDetachAllOnCommit();

  /**
   * Sets the default detachAllOnCommit setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @see #getDetachAllOnCommit()
   * @since 2.0
   * @param flag the default DetachAllOnCommit setting
   */
  void setDetachAllOnCommit(boolean flag);

  /**
   * Gets the default copyOnAttach setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * @see #setCopyOnAttach(boolean)
   * @since 2.1
   * @return the copyOnAttach setting.
   */
  boolean getCopyOnAttach();

  /**
   * Sets the default copyOnAttach setting for all <code>PersistenceManager</code> instances
   * obtained from this factory.
   *
   * <p>CopyOnAttach set to <code>true</code> specifies that during makePersistent, copies are made
   * of detached parameter instances. With this flag set to <code>false</code>, detached parameter
   * instances are attached directly and change their state from detached-clean to persistent-clean
   * or from detached-dirty to persistent-dirty.
   *
   * @param flag Whether we should copy on attach
   * @see #getCopyOnAttach()
   * @since 2.1
   */
  void setCopyOnAttach(boolean flag);

  /**
   * Sets the name of this PersistenceManagerFactory.
   *
   * @since 2.1
   * @param name the name of this PMF
   */
  void setName(String name);

  /**
   * Gets the name of this PersistenceManagerFactory.
   *
   * @since 2.1
   * @return the name of this PMF
   */
  String getName();

  /**
   * Sets the PersistenceUnitName for this PersistenceManagerFactory. This has the same semantics as
   * the same-named property in JSR-220 PersistenceUnitInfo.
   *
   * @see #getPersistenceUnitName()
   * @since 2.1
   * @param name the PersistenceUnitName
   */
  void setPersistenceUnitName(String name);

  /**
   * Gets the PersistenceUnitName for this PersistenceManagerFactory.
   *
   * @see #setPersistenceUnitName(String)
   * @since 2.1
   * @return the PersistenceUnitName
   */
  String getPersistenceUnitName();

  /**
   * Sets the TimeZone ID of the server associated with this PersistenceManagerFactory. The
   * parameter is a String suitable for use with TimeZone.getTimeZone(). The String must match an ID
   * returned by TimeZone.getAvailableIDs(). If the ServerTimeZoneID is not set, or set to the null
   * String, assume that the server has the same TimeZone ID as the client. If incorrectly set, the
   * result of PersistenceManager.getServerDate() might be incorrect.
   *
   * @see #getServerTimeZoneID()
   * @see java.util.TimeZone#getTimeZone(String)
   * @see java.util.TimeZone#getAvailableIDs()
   * @see PersistenceManager#getServerDate()
   * @since 2.1
   * @param timezoneid the TimeZone ID of the server
   * @throws JDOUserException if the parameter does not match an ID from TimeZone.getAvailableIDs()
   */
  void setServerTimeZoneID(String timezoneid);

  /**
   * Gets the TimeZone ID of the server associated with this PersistenceManagerFactory. If not set,
   * assume that the server has the same TimeZone ID as the client.
   *
   * @see #setServerTimeZoneID(String)
   * @since 2.1
   * @return the TimeZone of the server
   */
  String getServerTimeZoneID();

  /**
   * Sets the TransactionType for this PersistenceManagerFactory. Permitted values are "JTA" and
   * "RESOURCE_LOCAL". This has the same semantics as the same-named property in JSR-220
   * EntityManagerFactory.
   *
   * @see #getTransactionType()
   * @see Constants#JTA
   * @see Constants#RESOURCE_LOCAL
   * @since 2.1
   * @param name the TransactionType
   * @throws JDOUserException if the parameter is not a permitted value
   */
  void setTransactionType(String name);

  /**
   * Gets the TransactionType for this PersistenceManagerFactory.
   *
   * @see #setTransactionType(String)
   * @since 2.1
   * @return the TransactionType
   */
  String getTransactionType();

  /**
   * Gets the value for read-only for this PMF. Indicates whether the datastore is read-only or
   * writable.
   *
   * @see #setReadOnly(boolean)
   * @since 2.2
   * @return the readOnly setting.
   */
  boolean getReadOnly();

  /**
   * Sets the value for whether the datastore is to be considered read-only.
   *
   * <p>ReadOnly set to <code>false</code> specifies that no updates can be performed to the
   * datastore, and if updates are attempted a JDOReadOnlyException is thrown.
   *
   * @param flag whether we should consider this datastore read-only
   * @see #getReadOnly()
   * @since 2.2
   */
  void setReadOnly(boolean flag);

  /**
   * Get the value for transaction isolation level for this PMF.
   *
   * @return the transaction isolation level
   * @see #setTransactionIsolationLevel(String)
   * @since 2.2
   */
  String getTransactionIsolationLevel();

  /**
   * Set the value for transaction isolation level for this PMF. Transaction isolation levels are
   * defined in javax.jdo.Constants. If the requested level is not available, but a higher level is
   * available, the higher level is silently used. If the requested level is not available, and no
   * higher level is available, then JDOUnsupportedOptionException is thrown. Standard values in
   * order from low to high are:
   *
   * <ul>
   *   <li>read-uncommitted
   *   <li>read-committed
   *   <li>repeatable-read
   *   <li>snapshot
   *   <li>serializable
   * </ul>
   *
   * @param level the transaction isolation level
   * @see #getTransactionIsolationLevel()
   * @see Constants#TX_READ_UNCOMMITTED
   * @see Constants#TX_READ_COMMITTED
   * @see Constants#TX_REPEATABLE_READ
   * @see Constants#TX_SNAPSHOT
   * @see Constants#TX_SERIALIZABLE
   * @since 2.2
   */
  void setTransactionIsolationLevel(String level);

  /**
   * Specify a default timeout interval (milliseconds) for any read operations for persistence
   * managers obtained from this persistence manager factory. To unset the explicit timeout, specify
   * null. For no timeout, specify 0. If the datastore and JDO implementation support timeouts, then
   * javax.jdo.option.DatastoreTimeout is returned by supportedOptions(). If timeouts are not
   * supported,this method will throw JDOUnsupportedOptionException.
   *
   * @since 3.0
   * @param interval the timeout interval (milliseconds)
   */
  void setDatastoreReadTimeoutMillis(Integer interval);

  /**
   * Get the default timeout setting for read operations. If timeouts are not supported,this method
   * will return null.
   *
   * @see #setDatastoreReadTimeoutMillis(Integer)
   * @return the default timeout setting (milliseconds).
   * @since 3.0
   */
  Integer getDatastoreReadTimeoutMillis();

  /**
   * Specify a default timeout interval (milliseconds) for any write operations for persistence
   * managers obtained from this persistence manager factory. To unset the explicit timeout, specify
   * null. For no timeout, specify 0. If the datastore and JDO implementation support timeouts, then
   * javax.jdo.option.DatastoreTimeout is returned by supportedOptions(). If timeouts are not
   * supported,this method will throw JDOUnsupportedOptionException.
   *
   * @since 3.0
   * @param interval the timeout interval (milliseconds)
   */
  void setDatastoreWriteTimeoutMillis(Integer interval);

  /**
   * Get the default timeout setting for write operations. If timeouts are not supported,this method
   * will return null.
   *
   * @see #setDatastoreWriteTimeoutMillis(Integer)
   * @return the default timeout setting (milliseconds).
   * @since 3.0
   */
  Integer getDatastoreWriteTimeoutMillis();

  /**
   * Return non-configurable properties of this <code>PersistenceManagerFactory</code>. Properties
   * with keys <code>VendorName</code> and <code>VersionNumber</code> are required. Other keys are
   * optional.
   *
   * @return the non-configurable properties of this <code>PersistenceManagerFactory</code>.
   */
  Properties getProperties();

  /**
   * The application can determine from the results of this method which optional features, and
   * which query languages are supported by the JDO implementation.
   *
   * <p>Each supported JDO feature is represented by a <code>String</code> with one of the following
   * values:
   *
   * <p><code>javax.jdo.option.TransientTransactional
   * <BR>javax.jdo.option.NontransactionalRead
   * <BR>javax.jdo.option.NontransactionalWrite
   * <BR>javax.jdo.option.RetainValues
   * <BR>javax.jdo.option.Optimistic
   * <BR>javax.jdo.option.ApplicationIdentity
   * <BR>javax.jdo.option.DatastoreIdentity
   * <BR>javax.jdo.option.NonDurableIdentity
   * <BR>javax.jdo.option.ArrayList
   * <BR>javax.jdo.option.HashMap
   * <BR>javax.jdo.option.Hashtable
   * <BR>javax.jdo.option.LinkedList
   * <BR>javax.jdo.option.TreeMap
   * <BR>javax.jdo.option.TreeSet
   * <BR>javax.jdo.option.Vector
   * <BR>javax.jdo.option.Map
   * <BR>javax.jdo.option.List
   * <BR>javax.jdo.option.Array
   * <BR>javax.jdo.option.NullCollection
   * <BR>javax.jdo.option.ChangeApplicationIdentity
   * <BR>javax.jdo.option.BinaryCompatibility
   * <BR>javax.jdo.option.GetDataStoreConnection
   * <BR>javax.jdo.option.UnconstrainedQueryVariables
   * <BR>javax.jdo.option.TransactionIsolationLevel.read-uncommitted
   * <BR>javax.jdo.option.TransactionIsolationLevel.read-committed
   * <BR>javax.jdo.option.TransactionIsolationLevel.repeatable-read
   * <BR>javax.jdo.option.TransactionIsolationLevel.snapshot
   * <BR>javax.jdo.option.TransactionIsolationLevel.serializable
   * <BR>javax.jdo.option.QueryCancel
   * <BR>javax.jdo.option.DatastoreTimeout
   * <BR>javax.jdo.query.SQL
   * <BR>javax.jdo.query.JDOQL
   * </code>
   *
   * <p>The standard JDO query language is represented by a <code>String</code>:
   *
   * <p><code>javax.jdo.query.JDOQL</code>
   *
   * @return the <code>Collection</code> of <code>String</code>s representing the supported options.
   */
  Collection<String> supportedOptions();

  /**
   * Return the {@link DataStoreCache} that this factory uses for controlling a second-level cache.
   * If this factory does not use a second-level cache, the returned instance does nothing. This
   * method never returns <code>null</code>.
   *
   * @since 2.0
   * @return the DataStoreCache
   */
  DataStoreCache getDataStoreCache();

  /**
   * Add the parameter listener to the list of instance lifecycle event listeners set as the initial
   * listeners for each PersistenceManager created by this PersistenceManagerFactory. The <code>
   * addInstanceLifecycleListener</code> and <code>removeInstanceLifecycleListener</code> methods
   * are considered to be configuration methods and can only be called when the
   * PersistenceManagerFactory is configurable (before the first time {@link #getPersistenceManager}
   * is called).
   *
   * <p>The <code>classes</code> parameter identifies all of the classes of interest. If the <code>
   * classes</code> parameter is specified as <code>null</code>, events for all persistent classes
   * and interfaces will be sent to the listener.
   *
   * <p>The listener will be called for each event for which it implements the corresponding {@link
   * InstanceLifecycleListener} interface.
   *
   * @param listener the lifecycle listener
   * @param classes the classes of interest to the listener
   * @since 2.0
   */
  void addInstanceLifecycleListener(InstanceLifecycleListener listener, Class[] classes);

  /**
   * Remove the parameter listener instance from the list of instance lifecycle event listeners set
   * as the initial listeners for each PersistenceManager created by this PersistenceManagerFactory.
   * The <code>addInstanceLifecycleListener</code> and <code>removeInstanceLifecycleListener</code>
   * methods are considered to be configuration methods and can only be called when the
   * PersistenceManagerFactory is configurable (before the first time {@link #getPersistenceManager}
   * is called).
   *
   * @param listener the listener instance to be removed
   * @since 2.0
   */
  void removeInstanceLifecycleListener(InstanceLifecycleListener listener);

  /**
   * Add the <code>FetchGroup</code>s to the set of active fetch groups. <code>FetchGroup</code>s
   * are made unmodifiable before being added. <code>FetchGroup</code>s that match existing <code>
   * FetchGroup</code>s replace the corresponding <code>FetchGroup</code>s. The replaced <code>
   * FetchGroup</code>s become unscoped. Match is based on identical class and equal name. The
   * methods {@link #addFetchGroups}, {@link #removeFetchGroups}, {@link #getFetchGroups}, and
   * {@link #removeAllFetchGroups} are internally serialized.
   *
   * @param groups an array of FetchGroups
   * @throws SecurityException if the caller is not authorized for {@link JDOPermission}
   *     ("manageMetadata")
   * @since 2.2
   */
  void addFetchGroups(FetchGroup... groups);

  /**
   * Remove the <code>FetchGroup</code>s from the set of active <code>FetchGroup</code>s. Existing
   * <code>FetchGroup</code>s that match parameter <code>FetchGroup</code>s are removed. Parameter
   * <code>FetchGroup</code>s that do not match any existing <code>FetchGroup</code> are ignored.
   * Removed <code>FetchGroup</code>s become unscoped. Match is based on identical class and equal
   * name. The methods {@link #addFetchGroups}, {@link #removeFetchGroups}, {@link #getFetchGroups},
   * and {@link #removeAllFetchGroups} are internally serialized.
   *
   * @param groups an array of FetchGroups
   * @throws SecurityException if the caller is not authorized for {@link JDOPermission}
   *     ("manageMetadata")
   * @since 2.2
   */
  void removeFetchGroups(FetchGroup... groups);

  /**
   * Remove all <code>FetchGroup</code>s from the set of active <code>FetchGroup</code>s. All
   * removed <code>FetchGroup</code>s become unscoped. The methods {@link #addFetchGroups}, {@link
   * #removeFetchGroups}, {@link #getFetchGroups}, and {@link #removeAllFetchGroups} are internally
   * serialized.
   *
   * @throws SecurityException if the caller is not authorized for {@link JDOPermission}
   *     ("manageMetadata")
   * @since 2.2
   */
  void removeAllFetchGroups();

  /**
   * Create an unscoped, modifiable <code>FetchGroup</code> for the Class and name. If a
   * corresponding <code>FetchGroup</code> already exists in <code>PersistenceManagerFactory</code>
   * scope, copy its definition to a new <code>FetchGroup</code>. If the <code>FetchGroup</code>
   * does not already exist, create it with no members. The <code>FetchGroup</code> does not become
   * in scope until it is added to the current set via {@link #addFetchGroups}.
   *
   * @param cls the class or interface for the FetchGroup
   * @param name the name of the fetch group
   * @return the FetchGroup
   * @throws JDOUserException if the class is not a persistence-capable class or interface
   * @since 2.2
   */
  FetchGroup getFetchGroup(Class cls, String name);

  /**
   * Get a modifiable Set containing a mutable copy of all currently active (in scope) fetch groups.
   * The methods {@link #addFetchGroups}, {@link #removeFetchGroups}, {@link #getFetchGroups}, and
   * {@link #removeAllFetchGroups} are internally serialized.
   *
   * @return a copy of all currently active fetch groups
   * @throws SecurityException if the caller is not authorized for {@link JDOPermission}
   *     ("getMetadata")
   * @since 2.2
   */
  Set getFetchGroups();

  /**
   * Method to register metadata with the persistence process managed by this <code>
   * PersistenceManagerFactory</code>. Metadata can be created using the method {@link
   * #newMetadata}. If there is already metadata registered for a class contained in this metadata
   * object then a JDOUserException will be thrown.
   *
   * @param metadata The Metadata to register.
   * @since 3.0
   */
  void registerMetadata(JDOMetadata metadata);

  /**
   * Method to return a new metadata object that can be subsequently modified and registered with
   * the persistence process using the method {@link #registerMetadata}.
   *
   * @return The metadata
   * @since 3.0
   */
  JDOMetadata newMetadata();

  /**
   * Method to return the metadata object for the specified class/interface, if there is metadata
   * defined for that class/interface. If there is no metadata for the specified class/interface, or
   * the parameter is null, then null will be returned.
   *
   * @param className Name of the class to get metadata for
   * @return The metadata
   * @since 3.0
   */
  TypeMetadata getMetadata(String className);

  /**
   * Method to return the currently managed classes for this factory.
   *
   * @return Collection of persistable classes that are managed by this factory
   * @since 3.1
   */
  Collection<Class> getManagedClasses();
}
