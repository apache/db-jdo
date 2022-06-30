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

package javax.jdo;

/**
 * Constant values used in JDO.
 *
 * @since 2.1
 * @version 3.0
 */
public interface Constants {

    /**
     * The JDO standard package name.
     * @since 3.1
     */
    static String JAVAX_JDO = "javax.jdo";

    /**
     * The JDO standard property string and option string prefix.
     */
    static String JAVAX_JDO_PREFIX = JAVAX_JDO + ".";

    /**
     * The name of the standard service configuration resource text file containing
     * the name of an implementation of {@link PersistenceManagerFactory}.
     * Constant value is <code>META-INF/services/javax.jdo.PersistenceManagerFactory</code>.
     *
     * @since 2.1
     */
    static String SERVICE_LOOKUP_PMF_RESOURCE_NAME
        = "META-INF/services/javax.jdo.PersistenceManagerFactory";

    /**
     * The name of the standard service configuration resource text file containing
     * the name of an enhancer of {@link JDOEnhancer}.
     * Constant value is <code>META-INF/services/javax.jdo.JDOEnhancer</code>.
     * @since 3.0
     */
    static String SERVICE_LOOKUP_ENHANCER_RESOURCE_NAME
        = "META-INF/services/javax.jdo.JDOEnhancer";

    /**
     * The name of the standard JDO configuration resource file(s).
     * Constant value is <code>META-INF/jdoconfig.xml</code>.
     *
     * @since 2.1
     */
    static String JDOCONFIG_RESOURCE_NAME
        = "META-INF/jdoconfig.xml";

    /**
     * The standard JDO configuration schema namespace.
     * Constant value is <code>https://db.apache.org/jdo/xmlns/jdoconfig</code>.
     *
     * @since 2.1
     */
    static String JDOCONFIG_XSD_NS
        = "https://db.apache.org/jdo/xmlns/jdoconfig";

    /**
     * The standard JDO metadata schema namespace.
     * Constant value is <code>https://db.apache.org/jdo/xmlns/jdo</code>.
     *
     * @since 2.1
     */
    static String JDO_XSD_NS
        = "https://db.apache.org/jdo/xmlns/jdo";

    /**
     * The standard JDO object-repository mapping schema namespace.
     * Constant value is <code>https://db.apache.org/jdo/xmlns/orm</code>.
     *
     * @since 2.1
     */
    static String ORM_XSD_NS
        = "https://db.apache.org/jdo/xmlns/orm";

    /**
     * The standard JDO query schema namespace.
     * Constant value is <code>https://db.apache.org/jdo/xmlns/jdoquery</code>.
     *
     * @since 2.1
     */
    static String JDOQUERY_XSD_NS
        = "https://db.apache.org/jdo/xmlns/jdoquery";

    /**
     * The name of the persistence manager factory element in the JDO
     * configuration file.
     * Constant value is <code>persistence-manager-factory</code>.
     *
     * @since 2.1
     */
    static String ELEMENT_PERSISTENCE_MANAGER_FACTORY
        = "persistence-manager-factory";

    /**
     * The name of the persistence manager factory element's "class" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CLASS
        = "class";
    /**
     * The name of the persistence manager factory element's
     * "name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_NAME
        = "name";
    /**
     * The name of the persistence manager factory element's
     * "persistence-unit-name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME
        = "persistence-unit-name";
    /**
     * The name of the persistence manager factory element's "optimistic"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_OPTIMISTIC
        = "optimistic";
    /**
     * The name of the persistence manager factory element's "readonly"
     * attribute.
     *
     * @since 2.2
     */
    static String PMF_ATTRIBUTE_READONLY
        = "readonly";
    /**
     * The name of the persistence manager factory element's "retain-values"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_RETAIN_VALUES
        = "retain-values";
    /**
     * The name of the persistence manager factory element's "restore-values"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_RESTORE_VALUES
        = "restore-values";
    /**
     * The name of the persistence manager factory element's "ignore-cache"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_IGNORE_CACHE
        = "ignore-cache";
    /**
     * The name of the persistence manager factory element's
     * "nontransactional-read" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_NONTRANSACTIONAL_READ
        = "nontransactional-read";
    /**
     * The name of the persistence manager factory element's
     * "nontransactional-write" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_NONTRANSACTIONAL_WRITE
        = "nontransactional-write";
    /**
     * The name of the persistence manager factory element's "multithreaded"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_MULTITHREADED
        = "multithreaded";
    /**
     * The name of the persistence manager factory element's
     * "connection-driver-name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_DRIVER_NAME
        = "connection-driver-name";
    /**
     * The name of the persistence manager factory element's
     * "connection-user-name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_USER_NAME
        = "connection-user-name";
    /**
     * The name of the persistence manager factory element's
     * "connection-password" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_PASSWORD
        = "connection-password";
    /**
     * The name of the persistence manager factory element's "connection-url"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_URL
        = "connection-url";
    /**
     * The name of the persistence manager factory element's
     * "connection-factory-name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_FACTORY_NAME
        = "connection-factory-name";
    /**
     * The name of the persistence manager factory element's
     * "connection-factory2-name" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_CONNECTION_FACTORY2_NAME
        = "connection-factory2-name";
    /**
     * The name of the persistence manager factory element's
     * "detach-all-on-commit" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_DETACH_ALL_ON_COMMIT
        = "detach-all-on-commit";
    /**
     * The name of the persistence manager factory element's
     * "copy-on-attach" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_COPY_ON_ATTACH
        = "copy-on-attach";
    /**
     * The name of the persistence manager factory element's "mapping"
     * attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_MAPPING
        = "mapping";
    /**
     * The name of the persistence manager factory element's
     * "server-time-zone-id" attribute.
     *
     * @since 2.1
     */
    static String PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID
        = "server-time-zone-id";
    /**
     * The name of the persistence manager factory element's
     * "datastore-read-timeout-millis" attribute.
     *
     * @since 3.0
     */
    static String PMF_ATTRIBUTE_DATASTORE_READ_TIMEOUT_MILLIS
        = "datastore-read-timeout-millis";
    /**
     * The name of the persistence manager factory element's
     * "datastore-write-timeout-millis" attribute.
     *
     * @since 3.0
     */
    static String PMF_ATTRIBUTE_DATASTORE_WRITE_TIMEOUT_MILLIS
        = "datastore-write-timeout-millis";
    /**
     * The name of the persistence manager factory property elements in the JDO
     * configuration file.
     */
    static String ELEMENT_PROPERTY
        = "property";
    /**
     * The name of the persistence manager factory property element's "name"
     * attribute.
     */
    static String PROPERTY_ATTRIBUTE_NAME
        = "name";
    /**
     * The name of the persistence manager factory property element's "value"
     * attribute.
     */
    static String PROPERTY_ATTRIBUTE_VALUE
        = "value";

    /**
     * The name of the instance lifecycle listener element in the JDO
     * configuration file.
     */
    static String ELEMENT_INSTANCE_LIFECYCLE_LISTENER
        = "instance-lifecycle-listener";

    /**
     * The name of the instance lifecycle listener element's "listener"
     * attribute.
     */
    static String INSTANCE_LIFECYCLE_LISTENER_ATTRIBUTE_LISTENER
        = "listener";
    /**
     * The name of the instance lifecycle listener element's "classes"
     * attribute.
     */
    static String INSTANCE_LIFECYCLE_LISTENER_ATTRIBUTE_CLASSES
        = "classes";

    /**
     * "javax.jdo.option.TransientTransactional"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_TRANSACTIONAL_TRANSIENT
        = "javax.jdo.option.TransientTransactional";
    /**
     * "javax.jdo.option.NontransactionalRead"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_NONTRANSACTIONAL_READ
        = "javax.jdo.option.NontransactionalRead";
    /**
     * "javax.jdo.option.NontransactionalWrite"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_NONTRANSACTIONAL_WRITE
        = "javax.jdo.option.NontransactionalWrite";
    /**
     * "javax.jdo.option.RetainValues"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_RETAIN_VALUES
        = "javax.jdo.option.RetainValues";
    /**
     * "javax.jdo.option.Optimistic"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_OPTIMISTIC
        = "javax.jdo.option.Optimistic";
    /**
     * "javax.jdo.option.ApplicationIdentity"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_APPLICATION_IDENTITY
        = "javax.jdo.option.ApplicationIdentity";
    /**
     * "javax.jdo.option.DatastoreIdentity"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_DATASTORE_IDENTITY
        = "javax.jdo.option.DatastoreIdentity";
    /**
     * "javax.jdo.option.NonDurableIdentity"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_NONDURABLE_IDENTITY
        = "javax.jdo.option.NonDurableIdentity";
    /**
     * "javax.jdo.option.ArrayList"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_ARRAYLIST
        = "javax.jdo.option.ArrayList";
    /**
     * "javax.jdo.option.LinkedList"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_LINKEDLIST
        = "javax.jdo.option.LinkedList";
    /**
     * "javax.jdo.option.TreeMap"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_TREEMAP
        = "javax.jdo.option.TreeMap";
    /**
     * "javax.jdo.option.TreeSet"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_TREESET
        = "javax.jdo.option.TreeSet";
    /**
     * "javax.jdo.option.Vector"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_VECTOR
        = "javax.jdo.option.Vector";
    /**
     * "javax.jdo.option.Array"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_ARRAY
        = "javax.jdo.option.Array";
    /**
     * "javax.jdo.option.NullCollection"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_NULL_COLLECTION
        = "javax.jdo.option.NullCollection";
    /**
     * "javax.jdo.option.ChangeApplicationIdentity"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_CHANGE_APPLICATION_IDENTITY
        = "javax.jdo.option.ChangeApplicationIdentity";
    /**
     * "javax.jdo.option.BinaryCompatibility"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_BINARY_COMPATIBILITY
        = "javax.jdo.option.BinaryCompatibility";
    /**
     * "javax.jdo.option.GetDataStoreConnection"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_GET_DATASTORE_CONNECTION
        = "javax.jdo.option.GetDataStoreConnection";
    /**
     * "javax.jdo.option.GetJDBCConnection"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_GET_JDBC_CONNECTION
        = "javax.jdo.option.GetJDBCConnection";
    /**
     * "javax.jdo.query.SQL"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_QUERY_SQL = "javax.jdo.query.SQL";

    /**
     * "javax.jdo.option.QueryCancel"
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 3.0
     */
    static String OPTION_QUERY_CANCEL = "javax.jdo.option.QueryCancel";

    /**
     * "javax.jdo.query.JDOQL.BitwiseOperations"
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 3.2
     */
    static String OPTION_QUERY_JDOQL_BITWISE_OPERATIONS = "javax.jdo.query.JDOQL.BitwiseOperations";

    /**
     * "javax.jdo.query.JDOQL.UnconstrainedQueryVariables"
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_UNCONSTRAINED_QUERY_VARIABLES = "javax.jdo.query.JDOQL.UnconstrainedQueryVariables";

    /**
     * "javax.jdo.option.DatastoreTimeout"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 3.0
     */
    static String OPTION_DATASTORE_TIMEOUT
        = "javax.jdo.option.DatastoreTimeout";
    /**
     * "javax.jdo.option.version.DateTime"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_VERSION_DATETIME
        = "javax.jdo.option.version.DateTime";
    /**
     * "javax.jdo.option.version.StateImage"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_VERSION_STATE_IMAGE
        = "javax.jdo.option.version.StateImage";
    /**
     * "javax.jdo.option.PreDirtyEvent"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_PREDIRTY_EVENT
        = "javax.jdo.option.PreDirtyEvent";
    /**
     * "javax.jdo.option.mapping.HeterogeneousObjectType"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_HETEROGENEOUS_OBJECT_TYPE
        = "javax.jdo.option.mapping.HeterogeneousObjectType";
    /**
     * "javax.jdo.option.mapping.HeterogeneousInterfaceType"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_HETEROGENEOUS_INTERFACE_TYPE
        = "javax.jdo.option.mapping.HeterogeneousInterfaceType";
    /**
     * "javax.jdo.option.mapping.JoinedTablePerClass"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_JOINED_TABLE_PER_CLASS
        = "javax.jdo.option.mapping.JoinedTablePerClass";
    /**
     * "javax.jdo.option.mapping.JoinedTablePerConcreteClass"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_JOINED_TABLE_PER_CONCRETE_CLASS
        = "javax.jdo.option.mapping.JoinedTablePerConcreteClass";
    /**
     * "javax.jdo.option.mapping.NonJoinedTablePerConcreteClass"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_NON_JOINED_TABLE_PER_CONCRETE_CLASS
        = "javax.jdo.option.mapping.NonJoinedTablePerConcreteClass";
    /**
     * "javax.jdo.option.mapping.RelationSubclassTable"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.1
     */
    static String OPTION_MAPPING_RELATION_SUBCLASS_TABLE
        = "javax.jdo.option.mapping.RelationSubclassTable";

    /**
     * "javax.jdo.PersistenceManagerFactoryClass"
     *
     * @see JDOHelper#getPersistenceManagerFactory(java.util.Map)
     * @since 2.1
     */
    static String PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS
        = "javax.jdo.PersistenceManagerFactoryClass";

    /**
     * "VendorName"
     *
     * @see JDOEnhancer#getProperties()
     * @since 3.0
     */
    public final static String PROPERTY_ENHANCER_VENDOR_NAME
        = "VendorName";

    /**
     * "VersionNumber"
     *
     * @see JDOEnhancer#getProperties()
     * @since 3.0
     */
    public final static String PROPERTY_ENHANCER_VERSION_NUMBER
        = "VersionNumber";

    /** Exit value for no enhancer found
     * @since 3.0
     * */
    public final static int ENHANCER_NO_JDO_ENHANCER_FOUND = 2;

    /** Exit value for usage error
     * @since 3.0
     * */
    public final static int ENHANCER_USAGE_ERROR = 3;

    /** Exit value for an exception from the JDOEnhancer
     * @since 3.0
     * */
    public final static int ENHANCER_EXCEPTION = 1;

    /**
     * "javax.jdo.option.Optimistic"
     *
     * @see PersistenceManagerFactory#getOptimistic()
     * @since 2.1
     */
    static String PROPERTY_OPTIMISTIC
        = "javax.jdo.option.Optimistic";
    /**
     * "javax.jdo.option.ReadOnly"
     *
     * @see PersistenceManagerFactory#getReadOnly()
     * @since 2.2
     */
    static String PROPERTY_READONLY
        = "javax.jdo.option.ReadOnly";
    /**
     * "javax.jdo.option.RetainValues"
     *
     * @see PersistenceManagerFactory#getRetainValues()
     * @since 2.1
     */
    static String PROPERTY_RETAIN_VALUES
        = "javax.jdo.option.RetainValues";
    /**
     * "javax.jdo.option.RestoreValues"
     *
     * @see PersistenceManagerFactory#getRestoreValues()
     * @since 2.1
     */
    static String PROPERTY_RESTORE_VALUES
        = "javax.jdo.option.RestoreValues";
    /**
     * "javax.jdo.option.IgnoreCache"
     *
     * @see PersistenceManagerFactory#getIgnoreCache()
     * @since 2.1
     */
    static String PROPERTY_IGNORE_CACHE
        = "javax.jdo.option.IgnoreCache";
    /**
     * "javax.jdo.option.NontransactionalRead"
     *
     * @see PersistenceManagerFactory#getNontransactionalRead()
     * @since 2.1
     */
    static String PROPERTY_NONTRANSACTIONAL_READ
        = "javax.jdo.option.NontransactionalRead";
    /**
     * "javax.jdo.option.NontransactionalWrite"
     *
     * @see PersistenceManagerFactory#getNontransactionalWrite()
     * @since 2.1
     */
    static String PROPERTY_NONTRANSACTIONAL_WRITE
        = "javax.jdo.option.NontransactionalWrite";
    /**
     * "javax.jdo.option.Multithreaded"
     *
     * @see PersistenceManagerFactory#getMultithreaded()
     * @since 2.1
     */
    static String PROPERTY_MULTITHREADED
        = "javax.jdo.option.Multithreaded";
    /**
     * "javax.jdo.option.TransactionIsolationLevel"
     *
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL
        = "javax.jdo.option.TransactionIsolationLevel";
    /**
     * "javax.jdo.option.TransactionIsolationLevel.read-uncommitted"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL_READ_UNCOMMITTED
        = "javax.jdo.option.TransactionIsolationLevel.read-uncommitted";
    /**
     * "javax.jdo.option.TransactionIsolationLevel.read-committed"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL_READ_COMMITTED
        = "javax.jdo.option.TransactionIsolationLevel.read-committed";
    /**
     * "javax.jdo.option.TransactionIsolationLevel.repeatable-read"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL_REPEATABLE_READ
        = "javax.jdo.option.TransactionIsolationLevel.repeatable-read";
    /**
     * "javax.jdo.option.TransactionIsolationLevel.snapshot"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL_SNAPSHOT
        = "javax.jdo.option.TransactionIsolationLevel.snapshot";
    /**
     * "javax.jdo.option.TransactionIsolationLevel.serializable"
     *
     * @see PersistenceManagerFactory#supportedOptions()
     * @since 2.2
     */
    static String PROPERTY_TRANSACTION_ISOLATION_LEVEL_SERIALIZABLE
        = "javax.jdo.option.TransactionIsolationLevel.serializable";
    /**
     * "javax.jdo.option.DetachAllOnCommit"
     *
     * @see PersistenceManagerFactory#getDetachAllOnCommit()
     * @since 2.1
     */
    static String PROPERTY_DETACH_ALL_ON_COMMIT
        = "javax.jdo.option.DetachAllOnCommit";
    /**
     * "javax.jdo.option.CopyOnAttach"
     *
     * @see PersistenceManagerFactory#getCopyOnAttach()
     * @since 2.1
     */
    static String PROPERTY_COPY_ON_ATTACH
        = "javax.jdo.option.CopyOnAttach";
    /**
     * "javax.jdo.option.ConnectionDriverName"
     * This property might be ignored by the JDO implementation
     * because the JDBC DriverManager handles the driver name.
     * @see PersistenceManagerFactory#getConnectionDriverName()
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_DRIVER_NAME
        = "javax.jdo.option.ConnectionDriverName";
    /**
     * "javax.jdo.option.ConnectionUserName"
     *
     * @see PersistenceManagerFactory#getConnectionUserName()
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_USER_NAME
        = "javax.jdo.option.ConnectionUserName";
    /**
     * "javax.jdo.option.Password"
     *
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_PASSWORD
        = "javax.jdo.option.ConnectionPassword";
    /**
     * "javax.jdo.option.ConnectionURL"
     *
     * @see PersistenceManagerFactory#getConnectionURL()
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_URL
        = "javax.jdo.option.ConnectionURL";
    /**
     * "javax.jdo.option.ConnectionFactoryName"
     *
     * @see PersistenceManagerFactory#getConnectionFactoryName()
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_FACTORY_NAME
        = "javax.jdo.option.ConnectionFactoryName";
    /**
     * "javax.jdo.option.ConnectionFactory2Name"
     *
     * @see PersistenceManagerFactory#getConnectionFactory2Name()
     * @since 2.1
     */
    static String PROPERTY_CONNECTION_FACTORY2_NAME
        = "javax.jdo.option.ConnectionFactory2Name";
    /**
     * "javax.jdo.option.Mapping"
     *
     * @see PersistenceManagerFactory#getMapping()
     * @since 2.1
     */
    static String PROPERTY_MAPPING
        = "javax.jdo.option.Mapping";
    /**
     * "javax.jdo.option.PersistenceUnitName"
     *
     * @see PersistenceManagerFactory#getPersistenceUnitName()
     * @since 2.1
     */
    static String PROPERTY_PERSISTENCE_UNIT_NAME
        = "javax.jdo.option.PersistenceUnitName";
    /**
     * "javax.jdo.option.Name"
     *
     * @see PersistenceManagerFactory#getName()
     * @since 2.1
     */
    static String PROPERTY_NAME
        = "javax.jdo.option.Name";

    /**
     * "javax.jdo.spi.PropertiesFileName"
     *
     * @since 3.2
     */
    static String PROPERTY_SPI_PROPERTIES_FILE_NAME
            = "javax.jdo.spi.PropertiesFileName";

    /**
     * "javax.jdo.option.spi.ResourceName"
     * @since 2.1
     */
    static String PROPERTY_SPI_RESOURCE_NAME
        = "javax.jdo.option.spi.ResourceName";

    /**
     * "javax.jdo.listener.InstanceLifecycleListener"
     *
     * @see PersistenceManagerFactory#addInstanceLifecycleListener(javax.jdo.listener.InstanceLifecycleListener,Class[])
     * @see PersistenceManagerFactory#removeInstanceLifecycleListener(javax.jdo.listener.InstanceLifecycleListener)
     */
    static String PROPERTY_INSTANCE_LIFECYCLE_LISTENER
        = "javax.jdo.listener.InstanceLifecycleListener";

    /**
     * Prefix used to configure
     * {@link javax.jdo.listener.InstanceLifecycleListener} instances
     * externally.
     * To configure an <code>InstanceLifecycleListener</code> via properties,
     * create a property name with the prefix of
     * this constant and append the fully qualified listener class name, then
     * set its value to the comma- or whitespace-delimited list
     * of persistence-capable classes whose instances are to be observed.
     * Use no value to indicate that instances of
     * all persistence-capable classes are to be observed.<br>
     * For example,<br>
     * <code>javax.jdo.option.InstanceLifecycleListener.com.example.MyListener=com.example.Foo,com.example.Bar</code><br>
     * is equivalent to calling<br>
     * <code>pmf.addInstanceLifecycleListener(new com.example.MyListener(), new Class[] {com.example.Foo.class, com.example.Bar.class});</code><br>
     * where <code>pmf</code> is an instance of type
     * <code>PersistenceManagerFactory</code>.
     *
     * @see javax.jdo.PersistenceManagerFactory#addInstanceLifecycleListener(javax.jdo.listener.InstanceLifecycleListener,Class[])
     * @since 2.1
     */
    static String PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER
        = PROPERTY_INSTANCE_LIFECYCLE_LISTENER + ".";

    /**
     * Mapping "javax.jdo.mapping.Catalog"
     *
     * @since 2.1
     */
    static String PROPERTY_MAPPING_CATALOG
        = "javax.jdo.mapping.Catalog";
    /**
     * Mapping "javax.jdo.mapping.Schema"
     *
     * @since 2.1
     */
    static String PROPERTY_MAPPING_SCHEMA
        = "javax.jdo.mapping.Schema";

    /**
     * Specified value "javax.jdo.option.ServerTimeZoneID"
     *
     * @since 2.1
     */
    static String PROPERTY_SERVER_TIME_ZONE_ID
        = "javax.jdo.option.ServerTimeZoneID";

    /**
     * Specified value "javax.jdo.option.DatastoreReadTimeoutMillis"
     *
     * @since 3.0
     */
    static String PROPERTY_DATASTORE_READ_TIMEOUT_MILLIS
        = "javax.jdo.option.DatastoreReadTimeoutMillis";

    /**
     * Specified value "javax.jdo.option.DatastoreWriteTimeoutMillis"
     *
     * @since 3.0
     */
    static String PROPERTY_DATASTORE_WRITE_TIMEOUT_MILLIS
        = "javax.jdo.option.DatastoreWriteTimeoutMillis";

    /**
     * Nonconfigurable property constant "VendorName"
     *
     * @see PersistenceManagerFactory#getProperties()
     * @since 2.1
     */
    static String NONCONFIGURABLE_PROPERTY_VENDOR_NAME
        = "VendorName";

    /**
     * Nonconfigurable property constant "VersionNumber"
     *
     * @see PersistenceManagerFactory#getProperties()
     * @since 2.1
     */
    static String NONCONFIGURABLE_PROPERTY_VERSION_NUMBER
        = "VersionNumber";

    /**
     * Specified value "javax.jdo.option.TransactionType"
     *
     * @since 2.1
     */
    static String PROPERTY_TRANSACTION_TYPE
        = "javax.jdo.option.TransactionType";

    /**
     * The value for TransactionType to specify that transactions
     * are managed by the Java Transactions API, as documented in
     * JSR-220.
     *
     * @since 2.1
     */
    static String JTA
        = "JTA";

    /**
     * The value for TransactionType to specify that transactions
     * are managed by the javax.jdo.Transaction instance, similar
     * to the usage as documented in JSR-220.
     *
     * @since 2.1
     */
    static String RESOURCE_LOCAL
        = "RESOURCE_LOCAL";

    /**
     * The name of the resource for the DTD of the standard JDO configuration
     * file.
     *
     * @since 2.1
     */
    static String JDOCONFIG_DTD_RESOURCE
        = "javax/jdo/jdoconfig_3_0.dtd";

    /**
     * The name of the resource for the XML schema of the standard JDO
     * configuration file.
     *
     * @since 2.1
     */
    static String JDOCONFIG_XSD_RESOURCE
        = "javax/jdo/jdoconfig_3_0.xsd";

    /**
     * The name of the resource for the DTD of the standard JDO metadata file.
     *
     * @since 2.1
     */
    static String JDO_DTD_RESOURCE
        = "javax/jdo/jdo_3_0.dtd";

    /**
     * The name of the resource for the XML schema of the standard JDO
     * metadata file.
     *
     * @since 2.1
     */
    static String JDO_XSD_RESOURCE
        = "javax/jdo/jdo_3_0.xsd";

    /**
     * The name of the resource for the DTD of the standard JDO
     * object-relational mapping metadata file.
     *
     * @since 2.1
     */
    static String ORM_DTD_RESOURCE
        = "javax/jdo/orm_3_0.dtd";

    /**
     * The name of the resource for the XML schema of the standard JDO
     * object-relational mapping metadata file.
     *
     * @since 2.1
     */
    static String ORM_XSD_RESOURCE
        = "javax/jdo/orm_3_0.xsd";

    /**
     * The name of the resource for the DTD of the standard JDO query
     * metadata file.
     *
     * @since 2.1
     */
    static String JDOQUERY_DTD_RESOURCE
        = "javax/jdo/jdoquery_3_0.dtd";

    /**
     * The name of the resource for the XML schema of the standard JDO query
     * metadata file.
     *
     * @since 2.1
     */
    static String JDOQUERY_XSD_RESOURCE
        = "javax/jdo/jdoquery_3_0.xsd";

    /**
     * The name of the anonymous PersistenceManagerFactory, which
     * is the empty string.
     *
     * @since 2.1
     */
    static String ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME
        = "";

    /**
     * Transaction isolation level representing the ability to read
     * uncommitted data.
     * @see PersistenceManagerFactory#getTransactionIsolationLevel()
     * @see Transaction#getIsolationLevel()
     * @since 2.2
     */
    public static final String TX_READ_UNCOMMITTED = "read-uncommitted";

    /**
     * Transaction isolation level representing the requirement to read
     * committed data only.
     * @see PersistenceManagerFactory#getTransactionIsolationLevel()
     * @see Transaction#getIsolationLevel()
     * @since 2.2
     */
    public static final String TX_READ_COMMITTED = "read-committed";

    /**
     * Transaction isolation level representing the requirement to read
     * the same data in the same transaction.
     * @see PersistenceManagerFactory#getTransactionIsolationLevel()
     * @see Transaction#getIsolationLevel()
     * @since 2.2
     */
    public static final String TX_REPEATABLE_READ = "repeatable-read";

    /**
     * Transaction isolation level representing the requirement to keep
     * a snapshot for reading data.
     * @see PersistenceManagerFactory#getTransactionIsolationLevel()
     * @see Transaction#getIsolationLevel()
     * @since 2.2
     */
    public static final String TX_SNAPSHOT = "snapshot";

    /**
     * Transaction isolation level representing the requirement to serialize
     * transactions.
     * @see PersistenceManagerFactory#getTransactionIsolationLevel()
     * @see Transaction#getIsolationLevel()
     * @since 2.2
     */
    public static final String TX_SERIALIZABLE = "serializable";
}
