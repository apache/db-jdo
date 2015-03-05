package javax.jdo.stub;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jdo.Constants;
import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.metadata.JDOMetadata;
import javax.jdo.metadata.TypeMetadata;

public class StubPMF implements PersistenceManagerFactory, Constants {

    public static PersistenceManagerFactory getPersistenceManagerFactory(
        Map props) {

        StubPMF pmf = new StubPMF();

        pmf.setProperties(props);

        for (Object key : props.keySet()) {
            if (!(key instanceof String)) {
            continue;
            }
            String prop = (String) key;
            if (!prop.startsWith("javax.jdo.")) {
            continue;
            }
            pmf.setProperty(prop, props.get(prop));
        }

        return pmf;
    }

    public static PersistenceManagerFactory getPersistenceManagerFactory(
        Map overrides, Map props) {

        StubPMF pmf = new StubPMF();

        pmf.setProperties(props);
        pmf.setProperties(overrides);

        return pmf;
    }

    String name;
    String persistenceManagerFactoryClassName;
    String spiResourceName;
    String optionConnectionUserName;
    String optionConnectionPassword;
    String optionConnectionURL;
    String optionConnectionDriverName;
    String optionConnectionFactoryName;
    Object optionConnectionFactory;
    String optionConnectionFactory2Name;
    Object optionConnectionFactory2;
    boolean optionMultithreaded;
    String optionMapping;
    boolean optionOptimistic;
    boolean optionRetainValues;
    boolean optionRestoreValues;
    boolean optionNontransactionalRead;
    boolean optionNontransactionalWrite;
    boolean optionIgnoreCache;
    boolean optionDetachAllOnCommit;
    boolean optionCopyOnAttach;
    String optionName;
    String optionPersistenceUnitName;
    String optionServerTimeZoneID;
    String optionTransactionType;
    boolean optionReadOnly;
    String optionTransactionIsolationLevel;
    Integer optionDatastoreReadTimeoutMillis;
    Integer optionDatastoreWriteTimeoutMillis;

    Properties properties = new Properties();

    public String getConnectionUserName() {
        return optionConnectionUserName;
    }

    public void setConnectionUserName(String connectionUserName) {
        this.optionConnectionUserName = connectionUserName;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.optionConnectionPassword = connectionPassword;
    }

    public String getConnectionURL() {
        return optionConnectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.optionConnectionURL = connectionURL;
    }

    public String getConnectionDriverName() {
        return optionConnectionDriverName;
    }

    public void setConnectionDriverName(String connectionDriverName) {
        this.optionConnectionDriverName = connectionDriverName;
    }

    public String getConnectionFactoryName() {
        return optionConnectionFactoryName;
    }

    public void setConnectionFactoryName(String connectionFactoryName) {
        this.optionConnectionFactoryName = connectionFactoryName;
    }

    public Object getConnectionFactory() {
        return optionConnectionFactory;
    }

    public void setConnectionFactory(Object connectionFactory) {
        this.optionConnectionFactory = connectionFactory;
    }

    public String getConnectionFactory2Name() {
        return optionConnectionFactory2Name;
    }

    public void setConnectionFactory2Name(String connectionFactory2Name) {
        this.optionConnectionFactory2Name = connectionFactory2Name;
    }

    public Object getConnectionFactory2() {
        return optionConnectionFactory2;
    }

    public void setConnectionFactory2(Object connectionFactory2) {
        this.optionConnectionFactory2 = connectionFactory2;
    }

    public boolean getMultithreaded() {
        return optionMultithreaded;
    }

    public void setMultithreaded(boolean multithreaded) {
        this.optionMultithreaded = multithreaded;
    }

    public String getMapping() {
        return optionMapping;
    }

    public void setMapping(String mapping) {
        this.optionMapping = mapping;
    }

    public boolean getOptimistic() {
        return optionOptimistic;
    }

    public void setOptimistic(boolean optimistic) {
        this.optionOptimistic = optimistic;
    }

    public boolean getRetainValues() {
        return optionRetainValues;
    }

    public void setRetainValues(boolean retainValues) {
        this.optionRetainValues = retainValues;
    }

    public boolean getRestoreValues() {
        return optionRestoreValues;
    }

    public void setRestoreValues(boolean restoreValues) {
        this.optionRestoreValues = restoreValues;
    }

    public boolean getNontransactionalRead() {
        return optionNontransactionalRead;
    }

    public void setNontransactionalRead(boolean nontransactionalRead) {
        this.optionNontransactionalRead = nontransactionalRead;
    }

    public boolean getNontransactionalWrite() {
        return optionNontransactionalWrite;
    }

    public void setNontransactionalWrite(boolean nontransactionalWrite) {
        this.optionNontransactionalWrite = nontransactionalWrite;
    }

    public boolean getIgnoreCache() {
        return optionIgnoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.optionIgnoreCache = ignoreCache;
    }

    public boolean getDetachAllOnCommit() {
        return optionDetachAllOnCommit;
    }

    public void setDetachAllOnCommit(boolean detachAllOnCommit) {
        this.optionDetachAllOnCommit = detachAllOnCommit;
    }

    public boolean getCopyOnAttach() {
        return optionCopyOnAttach;
    }

    public void setCopyOnAttach(boolean copyOnAttach) {
        this.optionCopyOnAttach = copyOnAttach;
    }

    public String getName() {
        return optionName;
    }

    public void setName(String name) {
        this.optionName = name;
    }

    public String getPersistenceUnitName() {
        return optionPersistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.optionPersistenceUnitName = persistenceUnitName;
    }

    public String getServerTimeZoneID() {
        return optionServerTimeZoneID;
    }

    public void setServerTimeZoneID(String serverTimeZoneID) {
        this.optionServerTimeZoneID = serverTimeZoneID;
    }

    public String getTransactionType() {
        return optionTransactionType;
    }

    public void setTransactionType(String transactionType) {
        this.optionTransactionType = transactionType;
    }

    public boolean getReadOnly() {
        return optionReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.optionReadOnly = readOnly;
    }

    public String getTransactionIsolationLevel() {
        return optionTransactionIsolationLevel;
    }

    public void setTransactionIsolationLevel(String transactionIsolationLevel) {
        this.optionTransactionIsolationLevel = transactionIsolationLevel;
    }

    public Integer getDatastoreReadTimeoutMillis() {
        return optionDatastoreReadTimeoutMillis;
    }

    public void setDatastoreReadTimeoutMillis(Integer datastoreReadTimeoutMillis) {
        this.optionDatastoreReadTimeoutMillis = datastoreReadTimeoutMillis;
    }

    public Integer getDatastoreWriteTimeoutMillis() {
        return optionDatastoreWriteTimeoutMillis;
    }

    public void setDatastoreWriteTimeoutMillis(
        Integer datastoreWriteTimeoutMillis) {
        this.optionDatastoreWriteTimeoutMillis = datastoreWriteTimeoutMillis;
    }

    public void close() {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean isClosed() {
        return true;
    }

    public PersistenceManager getPersistenceManager() {
        throw new UnsupportedOperationException("not implemented");
    }

    public PersistenceManager getPersistenceManagerProxy() {
        throw new UnsupportedOperationException("not implemented");
    }

    public PersistenceManager getPersistenceManager(String userid,
        String password) {
    throw new UnsupportedOperationException("not implemented");
    }

    public Properties getProperties() {
        return properties;
    }

    public Collection<String> supportedOptions() {
        throw new UnsupportedOperationException("not implemented");
    }

    public DataStoreCache getDataStoreCache() {
    return new DataStoreCache() {

        public void evict(Object oid) {
        }

        public void evictAll() {
        }

        public void evictAll(Object... oids) {
        }

        public void evictAll(Collection oids) {
        }

        public void evictAll(Class pcClass, boolean subclasses) {
        }

        public void evictAll(boolean subclasses, Class pcClass) {
        }

        public void pin(Object oid) {
        }

        public void pinAll(Collection oids) {
        }

        public void pinAll(Object... oids) {
        }

        public void pinAll(Class pcClass, boolean subclasses) {
        }

        public void pinAll(boolean subclasses, Class pcClass) {
        }

        public void unpin(Object oid) {
        }

        public void unpinAll(Collection oids) {
        }

        public void unpinAll(Object... oids) {
        }

        public void unpinAll(Class pcClass, boolean subclasses) {
        }

        public void unpinAll(boolean subclasses, Class pcClass) {
        }
    };
    }

    public void addInstanceLifecycleListener(
        InstanceLifecycleListener listener, Class[] classes) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void removeInstanceLifecycleListener(
        InstanceLifecycleListener listener) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFetchGroups(FetchGroup... groups) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void removeFetchGroups(FetchGroup... groups) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void removeAllFetchGroups() {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("rawtypes")
    public FetchGroup getFetchGroup(Class cls, String name) {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("rawtypes")
    public Set getFetchGroups() {
        throw new UnsupportedOperationException("not implemented");
    }

    public void registerMetadata(JDOMetadata metadata) {
        throw new UnsupportedOperationException("not implemented");
    }

    public JDOMetadata newMetadata() {
        throw new UnsupportedOperationException("not implemented");
    }

    public TypeMetadata getMetadata(String className) {
        throw new UnsupportedOperationException("not implemented");
    }

    void setSpiResourceName(String spiPropertiesResourceName) {
        this.spiResourceName = spiPropertiesResourceName;
    }

    public String getSpiPropertiesResourceName() {
        return spiResourceName;
    }

    void setPersistenceManagerFactoryClass(String className) {
        this.persistenceManagerFactoryClassName = className;
    }

    public String getPersistenceManagerFactoryClass() {
        return this.persistenceManagerFactoryClassName;
    }

    void setProperty(String name, Object value) {
        String val = value.toString();

        if (name.equals(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS)) {
            setPersistenceManagerFactoryClass(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_DRIVER_NAME)) {
            setConnectionDriverName(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_FACTORY_NAME)) {
            setConnectionFactoryName(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_FACTORY2_NAME)) {
            setConnectionFactory2Name(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_PASSWORD)) {
            setConnectionPassword(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_URL)) {
            setConnectionURL(val);
            return;
        }
        if (name.equals(PROPERTY_CONNECTION_USER_NAME)) {
            setConnectionUserName(val);
            return;
        }
        if (name.equals(PROPERTY_IGNORE_CACHE)) {
            setCopyOnAttach(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_DATASTORE_READ_TIMEOUT_MILLIS)) {
            setDatastoreReadTimeoutMillis(Integer.parseInt(val));
            return;
        }
        if (name.equals(PROPERTY_DATASTORE_WRITE_TIMEOUT_MILLIS)) {
            setDatastoreWriteTimeoutMillis(Integer.parseInt(val));
            return;
        }
        if (name.equals(PROPERTY_DETACH_ALL_ON_COMMIT)) {
            setDetachAllOnCommit(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_IGNORE_CACHE)) {
            setIgnoreCache(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_MAPPING)) {
            setMapping(val);
            return;
        }
        if (name.equals(PROPERTY_MULTITHREADED)) {
            setMultithreaded(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_NAME)) {
            setName(val);
            return;
        }
        if (name.equals(PROPERTY_NONTRANSACTIONAL_READ)) {
            setNontransactionalRead(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_NONTRANSACTIONAL_WRITE)) {
            setNontransactionalWrite(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_OPTIMISTIC)) {
            setOptimistic(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_PERSISTENCE_UNIT_NAME)) {
            setPersistenceUnitName(val);
            return;
        }
        if (name.equals(PROPERTY_READONLY)) {
            setReadOnly(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_RESTORE_VALUES)) {
            setRestoreValues(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_RETAIN_VALUES)) {
            setRetainValues(Boolean.parseBoolean(val));
            return;
        }
        if (name.equals(PROPERTY_SERVER_TIME_ZONE_ID)) {
            setServerTimeZoneID(val);
            return;
        }
        if (name.equals(PROPERTY_TRANSACTION_ISOLATION_LEVEL)) {
            setTransactionIsolationLevel(val);
            return;
        }
        if (name.equals(PROPERTY_TRANSACTION_TYPE)) {
            setTransactionType(val);
            return;
        }
        if (name.equals(PROPERTY_SPI_RESOURCE_NAME)) {
            setSpiResourceName(val);
            return;
        }

        throw new IllegalArgumentException("unhandled stub PMF property "
            + name);
    }

    void setProperties(Map properties) {
        for (Object key : properties.keySet()) {
            String k = key.toString();
            Object v = properties.get(key);

            this.properties.put(k, v);
            setProperty(k, v);
        }
    }

    public Collection<Class> getManagedClasses() {
        throw new UnsupportedOperationException("not implemented");
    }
}
