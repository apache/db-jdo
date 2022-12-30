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
 * JDOImplHelper.java
 *
 */

package javax.jdo.spi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.jdo.JDOConstants;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOUserException;
import javax.jdo.LegacyJava;
import javax.jdo.LegacyJava.SecurityManager;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;

/**
 * This class is a helper class for JDO implementations. It contains methods to register metadata
 * for persistence-capable classes and to perform common operations needed by implementations, not
 * by end users.
 *
 * <p><code>JDOImplHelper</code> allows construction of instances of persistence-capable classes
 * without using reflection.
 *
 * <p>Persistence-capable classes register themselves via a static method at class load time. There
 * is no security restriction on this access. JDO implementations get access to the functions
 * provided by this class only if they are authorized by the security manager. To avoid having every
 * call go through the security manager, only the call to get an instance is checked. Once an
 * implementation has an instance, any of the methods can be invoked without security checks.
 *
 * @version 2.1
 */
public class JDOImplHelper extends java.lang.Object {

  /**
   * This synchronized <code>HashMap</code> contains a static mapping of <code>PersistenceCapable
   * </code> class to metadata for the class used for constructing new instances. New entries are
   * added by the static method in each <code>PersistenceCapable</code> class. Entries are never
   * removed.
   */
  private static final Map<Class<?>, Meta> registeredClasses =
      Collections.synchronizedMap(new HashMap<>());

  /**
   * This Set contains all classes that have registered for setStateManager permissions via
   * authorizeStateManagerClass. Only the key is used in order to maintain a weak set of classes.
   */
  private static final Map<Class<?>, Class<?>> authorizedStateManagerClasses = new WeakHashMap<>();

  /** This list contains the registered listeners for <code>RegisterClassEvent</code>s. */
  private static final List<RegisterClassListener> listeners = new ArrayList<>();

  /** The list of registered StateInterrogation instances */
  private static List<StateInterrogation> stateInterrogations = new ArrayList<>();

  /** The singleton <code>JDOImplHelper</code> instance. */
  private static final JDOImplHelper jdoImplHelper = new JDOImplHelper();

  /** The Internationalization message helper. */
  private static final I18NHelper msg = I18NHelper.getInstance("javax.jdo.Bundle"); // NOI18N

  /** The DateFormat pattern. */
  private static String dateFormatPattern;

  /** The default DateFormat instance. */
  private static DateFormat dateFormat;

  /** The DocumentBuilderFactory used during jdoconfig.xml parsing. */
  private static DocumentBuilderFactory documentBuilderFactory;

  /** The ErrorHandler used during jdoconfig.xml parsing. */
  private static ErrorHandler errorHandler;

  /** JDO standard properties that the user can configure. */
  public static final Set<String> USER_CONFIGURABLE_STANDARD_PROPERTIES =
      createUserConfigurableStandardProperties();

  private static Set<String> createUserConfigurableStandardProperties() {
    Set<String> props = new HashSet<>();

    props.add(JDOConstants.PROPERTY_CONNECTION_DRIVER_NAME);
    props.add(JDOConstants.PROPERTY_CONNECTION_FACTORY2_NAME);
    props.add(JDOConstants.PROPERTY_CONNECTION_FACTORY_NAME);
    props.add(JDOConstants.PROPERTY_CONNECTION_PASSWORD);
    props.add(JDOConstants.PROPERTY_CONNECTION_URL);
    props.add(JDOConstants.PROPERTY_CONNECTION_USER_NAME);
    props.add(JDOConstants.PROPERTY_COPY_ON_ATTACH);
    props.add(JDOConstants.PROPERTY_DATASTORE_READ_TIMEOUT_MILLIS);
    props.add(JDOConstants.PROPERTY_DATASTORE_WRITE_TIMEOUT_MILLIS);
    props.add(JDOConstants.PROPERTY_DETACH_ALL_ON_COMMIT);
    props.add(JDOConstants.PROPERTY_IGNORE_CACHE);
    props.add(JDOConstants.PROPERTY_INSTANCE_LIFECYCLE_LISTENER);
    props.add(JDOConstants.PROPERTY_MAPPING);
    props.add(JDOConstants.PROPERTY_MAPPING_CATALOG);
    props.add(JDOConstants.PROPERTY_MAPPING_SCHEMA);
    props.add(JDOConstants.PROPERTY_MULTITHREADED);
    props.add(JDOConstants.PROPERTY_NAME);
    props.add(JDOConstants.PROPERTY_NONTRANSACTIONAL_READ);
    props.add(JDOConstants.PROPERTY_NONTRANSACTIONAL_WRITE);
    props.add(JDOConstants.PROPERTY_OPTIMISTIC);
    props.add(JDOConstants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS);
    props.add(JDOConstants.PROPERTY_PERSISTENCE_UNIT_NAME);
    props.add(JDOConstants.PROPERTY_READONLY);
    props.add(JDOConstants.PROPERTY_RESTORE_VALUES);
    props.add(JDOConstants.PROPERTY_RETAIN_VALUES);
    props.add(JDOConstants.PROPERTY_SERVER_TIME_ZONE_ID);
    props.add(JDOConstants.PROPERTY_SPI_RESOURCE_NAME);
    props.add(JDOConstants.PROPERTY_TRANSACTION_ISOLATION_LEVEL);
    props.add(JDOConstants.PROPERTY_TRANSACTION_TYPE);

    return Collections.unmodifiableSet(props);
  }

  static final String JAVAX_JDO_PREFIX_LOWER_CASED = JDOConstants.JAVAX_JDO_PREFIX.toLowerCase();

  static final String PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER_LOWER_CASED =
      JDOConstants.PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER.toLowerCase();

  static final Set<String> USER_CONFIGURABLE_STANDARD_PROPERTIES_LOWER_CASED =
      createUserConfigurableStandardPropertiesLowerCased();

  static Set<String> createUserConfigurableStandardPropertiesLowerCased() {
    Set<String> mixedCased = createUserConfigurableStandardProperties();
    Set<String> lowerCased = new HashSet<>(mixedCased.size());

    for (String propertyName : mixedCased) {
      lowerCased.add(propertyName.toLowerCase());
    }
    return Collections.unmodifiableSet(lowerCased);
  }

  /** Register the default DateFormat instance. */
  static {
    jdoImplHelper.registerDateFormat(getDateTimeInstance());
  }

  /** Creates new JDOImplHelper */
  private JDOImplHelper() {}

  /**
   * Get an instance of <code>JDOImplHelper</code>. This method checks that the caller is authorized
   * for <code>JDOPermission("getMetadata")</code>, and if not, throws <code>SecurityException
   * </code>.
   *
   * @return an instance of <code>JDOImplHelper</code>.
   * @throws SecurityException if the caller is not authorized for JDOPermission("getMetadata").
   */
  public static JDOImplHelper getInstance() throws SecurityException {
    SecurityManager sec = LegacyJava.getSecurityManager();
    if (sec != null) {
      // throws exception if caller is not authorized
      sec.checkPermission(JDOPermission.GET_METADATA);
    }
    return jdoImplHelper;
  }

  /**
   * Get the field names for a <code>PersistenceCapable</code> class. The order of fields is the
   * natural ordering of the <code>String</code> class (without considering localization).
   *
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @return the field names for the class.
   */
  public String[] getFieldNames(Class<?> pcClass) {
    Meta meta = getMeta(pcClass);
    return meta.getFieldNames();
  }

  /**
   * Get the field types for a <code>PersistenceCapable</code> class. The order of fields is the
   * same as for field names.
   *
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @return the field types for the class.
   */
  public Class<?>[] getFieldTypes(Class<?> pcClass) {
    Meta meta = getMeta(pcClass);
    return meta.getFieldTypes();
  }

  /**
   * Get the field flags for a <code>PersistenceCapable</code> class. The order of fields is the
   * same as for field names.
   *
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @return the field types for the class.
   */
  public byte[] getFieldFlags(Class<?> pcClass) {
    Meta meta = getMeta(pcClass);
    return meta.getFieldFlags();
  }

  /**
   * Get the persistence-capable superclass for a <code>PersistenceCapable</code> class.
   *
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @return The <code>PersistenceCapable</code> superclass for this class, or <code>null</code> if
   *     there isn't one.
   */
  public Class<?> getPersistenceCapableSuperclass(Class<?> pcClass) {
    Meta meta = getMeta(pcClass);
    return meta.getPersistenceCapableSuperclass();
  }

  /**
   * Create a new instance of the class and assign its <code>jdoStateManager</code>. The new
   * instance has its <code>jdoFlags</code> set to <code>LOAD_REQUIRED</code>.
   *
   * @see PersistenceCapable#jdoNewInstance(StateManager sm)
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @param sm the <code>StateManager</code> which will own the new instance.
   * @return the new instance, or <code>null</code> if the class is not registered.
   */
  public PersistenceCapable newInstance(Class<?> pcClass, StateManager sm) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    return pcInstance == null ? null : pcInstance.jdoNewInstance(sm);
  }

  /**
   * Create a new instance of the class and assign its <code>jdoStateManager</code> and key values
   * from the ObjectId. If the oid parameter is <code>null</code>, no key values are copied. The new
   * instance has its <code>jdoFlags</code> set to <code>LOAD_REQUIRED</code>.
   *
   * @see PersistenceCapable#jdoNewInstance(StateManager sm, Object oid)
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @param sm the <code>StateManager</code> which will own the new instance.
   * @return the new instance, or <code>null</code> if the class is not registered.
   * @param oid the ObjectId instance from which to copy key field values.
   */
  public PersistenceCapable newInstance(Class<?> pcClass, StateManager sm, Object oid) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    return pcInstance == null ? null : pcInstance.jdoNewInstance(sm, oid);
  }

  /**
   * Create a new instance of the ObjectId class of this <code>PersistenceCapable</code> class. It
   * is intended only for application identity. This method should not be called for classes that
   * use single field identity; newObjectIdInstance(Class, Object) should be used instead. If the
   * class has been enhanced for datastore identity, or if the class is abstract, null is returned.
   *
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @return the new ObjectId instance, or <code>null</code> if the class is not registered.
   */
  public Object newObjectIdInstance(Class<?> pcClass) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    return pcInstance == null ? null : pcInstance.jdoNewObjectIdInstance();
  }

  /**
   * Create a new instance of the class used by the parameter Class for JDO identity, using the key
   * constructor of the object id class. It is intended for single field identity. The identity
   * instance returned has no relationship with the values of the primary key fields of the
   * persistence-capable instance on which the method is called. If the key is the wrong class for
   * the object id class, null is returned.
   *
   * <p>For classes that use single field identity, if the parameter is of one of the following
   * types, the behavior must be as specified:
   *
   * <ul>
   *   <li><code>Number</code> or <code>Character</code>: the parameter must be the single field
   *       type or the wrapper class of the primitive field type; the parameter is passed to the
   *       single field identity constructor
   *   <li><code>ObjectIdFieldSupplier</code>: the field value is fetched from the <code>
   *       ObjectIdFieldSupplier</code> and passed to the single field identity constructor
   *   <li><code>String</code>: the String is passed to the single field identity constructor
   * </ul>
   *
   * @return the new ObjectId instance, or <code>null</code> if the class is not registered.
   * @param obj the <code>Object</code> form of the object id
   * @param pcClass the <code>PersistenceCapable</code> class.
   * @since 2.0
   */
  public Object newObjectIdInstance(Class<?> pcClass, Object obj) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    return (pcInstance == null) ? null : pcInstance.jdoNewObjectIdInstance(obj);
  }

  /**
   * Copy fields from an outside source to the key fields in the ObjectId. This method is generated
   * in the <code>PersistenceCapable</code> class to generate a call to the field manager for each
   * key field in the ObjectId.
   *
   * <p>For example, an ObjectId class that has three key fields (<code>int id</code>, <code>
   * String name</code>, and <code>Float salary</code>) would have the method generated:
   *
   * <p><code>
   * void jdoCopyKeyFieldsToObjectId (Object oid, ObjectIdFieldSupplier fm) {
   * <BR>    oid.id = fm.fetchIntField (0);
   * <BR>    oid.name = fm.fetchStringField (1);
   * <BR>    oid.salary = fm.fetchObjectField (2);
   * <BR>}</code>
   *
   * <p>The implementation is responsible for implementing the <code>ObjectIdFieldSupplier</code> to
   * provide the values for the key fields.
   *
   * @param pcClass the <code>PersistenceCapable Class</code>.
   * @param oid the ObjectId target of the copy.
   * @param fm the field manager that supplies the field values.
   */
  public void copyKeyFieldsToObjectId(
      Class<?> pcClass, PersistenceCapable.ObjectIdFieldSupplier fm, Object oid) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    if (pcInstance == null) {
      throw new JDOFatalInternalException(
          msg.msg("ERR_AbstractClassNoIdentity", pcClass.getName())); // NOI18N
    }
    pcInstance.jdoCopyKeyFieldsToObjectId(fm, oid);
  }

  /**
   * Copy fields to an outside source from the key fields in the ObjectId. This method is generated
   * in the <code>PersistenceCapable</code> class to generate a call to the field manager for each
   * key field in the ObjectId. For example, an ObjectId class that has three key fields (<code>
   * int id</code>, <code>String name</code>, and <code>Float salary</code>) would have the method
   * generated:
   *
   * <p><code>void jdoCopyKeyFieldsFromObjectId
   * <BR>        (PersistenceCapable oid, ObjectIdFieldConsumer fm) {
   * <BR>     fm.storeIntField (0, oid.id);
   * <BR>     fm.storeStringField (1, oid.name);
   * <BR>     fm.storeObjectField (2, oid.salary);
   * <BR>}</code>
   *
   * <p>The implementation is responsible for implementing the <code>ObjectIdFieldConsumer</code> to
   * store the values for the key fields.
   *
   * @param pcClass the <code>PersistenceCapable</code> class
   * @param oid the ObjectId source of the copy.
   * @param fm the field manager that receives the field values.
   */
  public void copyKeyFieldsFromObjectId(
      Class<?> pcClass, PersistenceCapable.ObjectIdFieldConsumer fm, Object oid) {
    Meta meta = getMeta(pcClass);
    PersistenceCapable pcInstance = meta.getPC();
    if (pcInstance == null) {
      throw new JDOFatalInternalException(
          msg.msg("ERR_AbstractClassNoIdentity", pcClass.getName())); // NOI18N
    }
    pcInstance.jdoCopyKeyFieldsFromObjectId(fm, oid);
  }

  /**
   * Register metadata by class. The registration will be done in the class named <code>
   * JDOImplHelper</code> loaded by the same or an ancestor class loader as the <code>
   * PersistenceCapable</code> class performing the registration.
   *
   * @param pcClass the <code>PersistenceCapable</code> class used as the key for lookup.
   * @param fieldNames an array of <code>String</code> field names for persistent and transactional
   *     fields
   * @param fieldTypes an array of <code>Class</code> field types
   * @param fieldFlags the Field Flags for persistent and transactional fields
   * @param pc an instance of the <code>PersistenceCapable</code> class
   * @param persistenceCapableSuperclass the most immediate superclass that is <code>
   *     PersistenceCapable</code>
   */
  public static void registerClass(
      Class<?> pcClass,
      String[] fieldNames,
      Class<?>[] fieldTypes,
      byte[] fieldFlags,
      Class<?> persistenceCapableSuperclass,
      PersistenceCapable pc) {
    if (pcClass == null) throw new NullPointerException(msg.msg("ERR_NullClass")); // NOI18N
    Meta meta = new Meta(fieldNames, fieldTypes, fieldFlags, persistenceCapableSuperclass, pc);
    registeredClasses.put(pcClass, meta);

    // handle class registration listeners
    synchronized (listeners) {
      if (!listeners.isEmpty()) {
        RegisterClassEvent event =
            new RegisterClassEvent(
                jdoImplHelper,
                pcClass,
                fieldNames,
                fieldTypes,
                fieldFlags,
                persistenceCapableSuperclass);
        for (Iterator<RegisterClassListener> i = listeners.iterator(); i.hasNext(); ) {
          RegisterClassListener crl = i.next();
          if (crl != null) {
            crl.registerClass(event);
          }
        }
      }
    }
  }

  /**
   * Unregister metadata by class loader. This method unregisters all registered <code>
   * PersistenceCapable</code> classes loaded by the specified class loader. Any attempt to get
   * metadata for unregistered classes will result in a <code>JDOFatalUserException</code>.
   *
   * @param cl the class loader.
   * @since 1.0.2
   */
  public void unregisterClasses(ClassLoader cl) {
    SecurityManager sec = LegacyJava.getSecurityManager();
    if (sec != null) {
      // throws exception if caller is not authorized
      sec.checkPermission(JDOPermission.MANAGE_METADATA);
    }
    synchronized (registeredClasses) {
      for (Iterator<Class<?>> i = registeredClasses.keySet().iterator(); i.hasNext(); ) {
        Class<?> pcClass = i.next();
        // Note, the pc class was registered by calling the static
        // method JDOImplHelper.registerClass. This means the
        // JDOImplHelper class loader is the same as or an ancestor
        // of the class loader of the pc class. In this case method
        // getClassLoader does not perform a security check for
        // RuntimePermission("getClassLoader") and thus we do not
        // need a privileged block for the getClassLoader call.
        if ((pcClass != null) && (pcClass.getClassLoader() == cl)) {
          // unregister pc class, if its class loader is the
          // specified one.
          i.remove();
        }
      }
    }
  }

  /**
   * Unregister metadata by class. This method unregisters the specified class. Any further attempt
   * to get metadata for the specified class will result in a <code>JDOFatalUserException</code>.
   *
   * @param pcClass the <code>PersistenceCapable</code> class to be unregistered.
   * @since 1.0.2
   */
  public void unregisterClass(Class<?> pcClass) {
    if (pcClass == null) throw new NullPointerException(msg.msg("ERR_NullClass")); // NOI18N
    SecurityManager sec = LegacyJava.getSecurityManager();
    if (sec != null) {
      // throws exception if caller is not authorized
      sec.checkPermission(JDOPermission.MANAGE_METADATA);
    }
    registeredClasses.remove(pcClass);
  }

  /**
   * Add the specified <code>RegisterClassListener</code> to the listener list.
   *
   * @param crl the listener to be added
   */
  public void addRegisterClassListener(RegisterClassListener crl) {
    HashSet<Class<?>> alreadyRegisteredClasses = null;
    synchronized (listeners) {
      listeners.add(crl);
      // Make a copy of the existing set of registered classes.
      // Between these two lines of code, any number of new class
      // registrations might occur, and will then all wait until this
      // synchronized block completes. Some of the class registrations
      // might be delivered twice to the newly registered listener.
      alreadyRegisteredClasses = new HashSet<>(registeredClasses.keySet());
    }
    // new registrations will call the new listener while the following
    // occurs notify the new listener about already-registered classes
    for (Iterator<Class<?>> it = alreadyRegisteredClasses.iterator(); it.hasNext(); ) {
      Class<?> pcClass = it.next();
      Meta meta = getMeta(pcClass);
      RegisterClassEvent event =
          new RegisterClassEvent(
              this,
              pcClass,
              meta.getFieldNames(),
              meta.getFieldTypes(),
              meta.getFieldFlags(),
              meta.getPersistenceCapableSuperclass());
      crl.registerClass(event);
    }
  }

  /**
   * Remove the specified <code>RegisterClassListener</code> from the listener list.
   *
   * @param crl the listener to be removed
   */
  public void removeRegisterClassListener(RegisterClassListener crl) {
    synchronized (listeners) {
      listeners.remove(crl);
    }
  }

  /**
   * Returns a collection of class objects of the registered persistence-capable classes.
   *
   * @return registered persistence-capable classes
   */
  public Collection<Class<?>> getRegisteredClasses() {
    return Collections.unmodifiableCollection(registeredClasses.keySet());
  }

  /**
   * Look up the metadata for a <code>PersistenceCapable</code> class.
   *
   * @param pcClass the <code>Class</code>.
   * @return the <code>Meta</code> for the <code>Class</code>.
   */
  private static Meta getMeta(Class<?> pcClass) {
    Meta ret = registeredClasses.get(pcClass);
    if (ret == null) {
      throw new JDOFatalUserException(msg.msg("ERR_NoMetadata", pcClass.getName())); // NOI18N
    }
    return ret;
  }

  /**
   * Register a class authorized to replaceStateManager. The caller of this method must be
   * authorized for JDOPermission("setStateManager"). During replaceStateManager, a
   * persistence-capable class will call the corresponding checkAuthorizedStateManager and the class
   * of the instance of the parameter must have been registered.
   *
   * @param smClass a Class that is authorized for JDOPermission("setStateManager").
   * @throws SecurityException if the caller is not authorized for JDOPermission("setStateManager").
   * @since 1.0.1
   */
  public static void registerAuthorizedStateManagerClass(Class<?> smClass)
      throws SecurityException {
    if (smClass == null) throw new NullPointerException(msg.msg("ERR_NullClass")); // NOI18N
    SecurityManager sm = LegacyJava.getSecurityManager();
    if (sm != null) {
      sm.checkPermission(JDOPermission.SET_STATE_MANAGER);
    }
    synchronized (authorizedStateManagerClasses) {
      authorizedStateManagerClasses.put(smClass, null);
    }
  }

  /**
   * Register classes authorized to replaceStateManager. The caller of this method must be
   * authorized for JDOPermission("setStateManager"). During replaceStateManager, a
   * persistence-capable class will call the corresponding checkAuthorizedStateManager and the class
   * of the instance of the parameter must have been registered.
   *
   * @param smClasses a Collection of Classes that are authorized for
   *     JDOPermission("setStateManager").
   * @throws SecurityException if the caller is not authorized for JDOPermission("setStateManager").
   * @since 1.0.1
   */
  public static void registerAuthorizedStateManagerClasses(Collection<?> smClasses)
      throws SecurityException {
    SecurityManager sm = LegacyJava.getSecurityManager();
    if (sm != null) {
      sm.checkPermission(JDOPermission.SET_STATE_MANAGER);
      synchronized (authorizedStateManagerClasses) {
        for (Iterator<?> it = smClasses.iterator(); it.hasNext(); ) {
          Object smClass = it.next();
          if (!(smClass instanceof Class)) {
            throw new ClassCastException(
                msg.msg(
                    "ERR_StateManagerClassCast", // NOI18N
                    smClass.getClass().getName()));
          }
          registerAuthorizedStateManagerClass((Class<?>) it.next());
        }
      }
    }
  }

  /**
   * Register a DocumentBuilderFactory instance for use in parsing the resource(s)
   * META-INF/jdoconfig.xml. The default is governed by the semantics of
   * DocumentBuilderFactory.newInstance().
   *
   * @param factory the DocumentBuilderFactory instance to use
   * @since 2.1
   */
  public synchronized void registerDocumentBuilderFactory(DocumentBuilderFactory factory) {
    documentBuilderFactory = factory;
  }

  /**
   * Return the registered instance of DocumentBuilderFactory.
   *
   * @return the DocumentBuilderFactory if registered; null otherwise
   * @since 2.1
   */
  public static DocumentBuilderFactory getRegisteredDocumentBuilderFactory() {
    return documentBuilderFactory;
  }

  /**
   * Register an ErrorHandler instance for use in parsing the resource(s) META-INF/jdoconfig.xml.
   * The default is an ErrorHandler that throws on error or fatalError and ignores warnings.
   *
   * @param handler the ErrorHandler instance to use
   * @since 2.1
   */
  public synchronized void registerErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }

  /**
   * Return the registered instance of ErrorHandler.
   *
   * @return the registered ErrorHandler if registered; null otherwise
   * @since 2.1
   */
  public static ErrorHandler getRegisteredErrorHandler() {
    return errorHandler;
  }

  /**
   * Check that the parameter instance is of a class that is authorized for
   * JDOPermission("setStateManager"). This method is called by the replaceStateManager method in
   * persistence-capable classes. A class that is passed as the parameter to replaceStateManager
   * must be authorized for JDOPermission("setStateManager"). To improve performance, first the set
   * of authorized classes is checked, and if not present, a regular permission check is made. The
   * regular permission check requires that all callers on the stack, including the
   * persistence-capable class itself, must be authorized for JDOPermission("setStateManager").
   *
   * @param sm an instance of StateManager whose class is to be checked.
   * @since 1.0.1
   */
  public static void checkAuthorizedStateManager(StateManager sm) {
    checkAuthorizedStateManagerClass(sm.getClass());
  }

  /**
   * Check that the parameter instance is a class that is authorized for
   * JDOPermission("setStateManager"). This method is called by the constructors of JDO Reference
   * Implementation classes.
   *
   * @param smClass a Class to be checked for JDOPermission("setStateManager")
   * @since 1.0.1
   */
  public static void checkAuthorizedStateManagerClass(Class<?> smClass) {
    final SecurityManager scm = LegacyJava.getSecurityManager();
    if (scm == null) {
      // if no security manager, no checking.
      return;
    }
    synchronized (authorizedStateManagerClasses) {
      if (authorizedStateManagerClasses.containsKey(smClass)) {
        return;
      }
    }
    // if not already authorized, perform "long" security checking.
    scm.checkPermission(JDOPermission.SET_STATE_MANAGER);
  }

  /**
   * Construct an instance of a key class using a String as input. This is a helper interface for
   * use with ObjectIdentity. Classes without a String constructor (such as those in java.lang and
   * java.util) will use this interface for constructing new instances. The result might be a
   * singleton or use some other strategy.
   */
  public interface StringConstructor {
    /**
     * Construct an instance of the class for which this instance is registered.
     *
     * @param s the parameter for construction
     * @return the constructed object
     */
    public Object construct(String s);
  }

  /**
   * Special StringConstructor instances for use with specific classes that have no public String
   * constructor. The Map is keyed on class instance and the value is an instance of
   * StringConstructor.
   */
  static final Map<Class<?>, StringConstructor> stringConstructorMap = new HashMap<>();

  /**
   * Register special StringConstructor instances. These instances are for constructing instances
   * from String parameters where there is no String constructor for them.
   *
   * @param cls the class to register a StringConstructor for
   * @param sc the StringConstructor instance
   * @return the previous StringConstructor registered for this class
   */
  public Object registerStringConstructor(Class<?> cls, StringConstructor sc) {
    synchronized (stringConstructorMap) {
      return stringConstructorMap.put(cls, sc);
    }
  }

  /** Register the default special StringConstructor instances. */
  static {
    if (isClassLoadable("java.util.Currency")) {
      jdoImplHelper.registerStringConstructor(
          Currency.class,
          s -> {
            try {
              return Currency.getInstance(s);
            } catch (IllegalArgumentException ex) {
              throw new JDOUserException(
                  msg.msg(
                      "EXC_CurrencyStringConstructorIllegalArgument", // NOI18N
                      s),
                  ex);
            } catch (Exception ex) {
              throw new JDOUserException(
                  msg.msg("EXC_CurrencyStringConstructorException"), // NOI18N
                  ex);
            }
          });
    }
    jdoImplHelper.registerStringConstructor(
        Locale.class,
        s -> {
          try {
            return getLocale(s);
          } catch (Exception ex) {
            throw new JDOUserException(
                msg.msg("EXC_LocaleStringConstructorException"), ex); // NOI18N
          }
        });
    jdoImplHelper.registerStringConstructor(
        Date.class,
        new StringConstructor() {
          public synchronized Object construct(String s) {
            try {
              // first, try the String as a Long
              return new Date(Long.parseLong(s));
            } catch (NumberFormatException ex) {
              // not a Long; try the formatted date
              ParsePosition pp = new ParsePosition(0);
              Date result = dateFormat.parse(s, pp);
              if (result == null) {
                throw new JDOUserException(
                    msg.msg(
                        "EXC_DateStringConstructor",
                        new Object[] // NOI18N
                        {s, pp.getErrorIndex(), dateFormatPattern}));
              }
              return result;
            }
          }
        });
  }

  /**
   * Parse the String to a Locale.
   *
   * @param s the name of the Locale
   * @return the Locale corresponding to the name
   */
  private static Locale getLocale(String s) {
    String lang = s;
    int firstUnderbar = s.indexOf('_');
    if (firstUnderbar == -1) {
      // nothing but language
      return new Locale(lang);
    }
    lang = s.substring(0, firstUnderbar);
    String country;
    int secondUnderbar = s.indexOf('_', firstUnderbar + 1);
    if (secondUnderbar == -1) {
      // nothing but language, country
      country = s.substring(firstUnderbar + 1);
      return new Locale(lang, country);
    }
    country = s.substring(firstUnderbar + 1, secondUnderbar);
    String variant = s.substring(secondUnderbar + 1);
    return new Locale(lang, country, variant);
  }
  /**
   * Determine if a class is loadable in the current environment.
   *
   * @param className the fully-qualified name of the class
   * @return true if the class can be loaded; false otherwise
   */
  private static boolean isClassLoadable(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * Construct an instance of the parameter class, using the keyString as an argument to the
   * constructor. If the class has a StringConstructor instance registered, use it. If not, try to
   * find a constructor for the class with a single String argument. Otherwise, throw a
   * JDOUserException.
   *
   * @param className the name of the class
   * @param keyString the String parameter for the constructor
   * @return the result of construction
   */
  public static Object construct(String className, String keyString) {
    StringConstructor stringConstructor;
    try {
      Class<?> keyClass = Class.forName(className);
      synchronized (stringConstructorMap) {
        stringConstructor = stringConstructorMap.get(keyClass);
      }
      if (stringConstructor != null) {
        return stringConstructor.construct(keyString);
      } else {
        Constructor<?> keyConstructor = keyClass.getConstructor(new Class[] {String.class});
        return keyConstructor.newInstance(new Object[] {keyString});
      }
    } catch (JDOException ex) {
      throw ex;
    } catch (Exception ex) {
      /* ClassNotFoundException,
      NoSuchMethodException,
      InstantiationException,
      IllegalAccessException,
      InvocationTargetException */
      throw new JDOUserException(
          msg.msg(
              "EXC_ObjectIdentityStringConstruction", // NOI18N
              new Object[] {ex.toString(), className, keyString}),
          ex);
    }
  }

  /**
   * Get the DateFormat instance for the default locale from the VM. This requires the following
   * privileges for JDOImplHelper in the security permissions file: permission
   * java.util.PropertyPermission "user.country", "read"; permission java.util.PropertyPermission
   * "user.timezone", "read,write"; permission java.util.PropertyPermission "java.home", "read"; If
   * these permissions are not present, or there is some other problem getting the default date
   * format, a simple formatter is returned.
   *
   * @since 2.1
   * @return the default date-time formatter
   */
  static DateFormat getDateTimeInstance() {
    DateFormat result = null;
    try {
      result = doPrivileged(() -> DateFormat.getDateTimeInstance());
    } catch (Exception ex) {
      result = DateFormat.getInstance();
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
    try {
      return (T) LegacyJava.doPrivilegedAction.invoke(null, privilegedAction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new JDOFatalInternalException(e.getMessage());
    }
  }

  /**
   * Register a DateFormat instance for use with constructing Date instances. The default is the
   * default DateFormat instance. If the new instance implements SimpleDateFormat, get its pattern
   * for error messages.
   *
   * @since 2.0
   * @param df the DateFormat instance to use
   */
  public synchronized void registerDateFormat(DateFormat df) {
    dateFormat = df;
    if (df instanceof SimpleDateFormat) {
      dateFormatPattern = ((SimpleDateFormat) df).toPattern();
    } else {
      dateFormatPattern = msg.msg("MSG_unknown"); // NOI18N
    }
  }

  /**
   * This is a helper class to manage metadata per persistence-capable class. The information is
   * used at runtime to provide field names and field types to the JDO Model.
   *
   * <p>This is the value of the <code>HashMap</code> which relates the <code>
   * PersistenceCapable Class</code> as a key to the metadata.
   */
  static class Meta {

    /**
     * Construct an instance of <code>Meta</code>.
     *
     * @param fieldNames An array of <code>String</code>
     * @param fieldTypes An array of <code>Class</code>
     * @param fieldFlags an array of <code>int</code>
     * @param persistenceCapableSuperclass the most immediate <code>PersistenceCapable</code>
     *     superclass
     * @param pc An instance of the <code>PersistenceCapable</code> class
     */
    Meta(
        String[] fieldNames,
        Class<?>[] fieldTypes,
        byte[] fieldFlags,
        Class<?> persistenceCapableSuperclass,
        PersistenceCapable pc) {
      this.fieldNames = fieldNames;
      this.fieldTypes = fieldTypes;
      this.fieldFlags = fieldFlags;
      this.persistenceCapableSuperclass = persistenceCapableSuperclass;
      this.pc = pc;
    }

    /**
     * This is an array of field names used for the Model at runtime. The field is passed by the
     * static class initialization.
     */
    final String[] fieldNames;

    /**
     * Get the field names from the metadata.
     *
     * @return the array of field names.
     */
    String[] getFieldNames() {
      return fieldNames;
    }

    /**
     * This is an array of field types used for the Model at runtime. The field is passed by the
     * static class initialization.
     */
    final Class<?>[] fieldTypes;

    /**
     * Get the field types from the metadata.
     *
     * @return the array of field types.
     */
    Class<?>[] getFieldTypes() {
      return fieldTypes;
    }

    /**
     * This is an array of field flags used for the Model at runtime. The field is passed by the
     * static class initialization.
     */
    final byte[] fieldFlags;

    /**
     * Get the field types from the metadata.
     *
     * @return the array of field types.
     */
    byte[] getFieldFlags() {
      return fieldFlags;
    }

    /**
     * This is the <code>Class</code> instance of the <code>PersistenceCapable</code> superclass.
     */
    final Class<?> persistenceCapableSuperclass;

    /**
     * Return the <code>PersistenceCapable</code> superclass.
     *
     * @return the <code>PersistenceCapable</code> superclass
     */
    Class<?> getPersistenceCapableSuperclass() {
      return persistenceCapableSuperclass;
    }
    /**
     * This is an instance of <code>PersistenceCapable</code>, used at runtime to create new
     * instances.
     */
    final PersistenceCapable pc;

    /**
     * Get an instance of the <code>PersistenceCapable</code> class.
     *
     * @return an instance of the <code>PersistenceCapable Class</code>.
     */
    PersistenceCapable getPC() {
      return pc;
    }

    /**
     * Return the string form of the metadata.
     *
     * @return the string form
     */
    public String toString() {
      return "Meta-" + pc.getClass().getName(); // NOI18N
    }
  }

  /**
   * Add a StateInterrogation to the list. Create a new list in case there is an iterator open on
   * the original list.
   *
   * @param si the StateInterrogation to add
   */
  public synchronized void addStateInterrogation(StateInterrogation si) {
    List<StateInterrogation> newList = new ArrayList<>(stateInterrogations);
    newList.add(si);
    stateInterrogations = newList;
  }

  /**
   * Remove a StateInterrogation from the list. Create a new list in case there is an iterator open
   * on the original list.
   *
   * @param si the StateInterrogation to remove
   */
  public synchronized void removeStateInterrogation(StateInterrogation si) {
    List<StateInterrogation> newList = new ArrayList<>(stateInterrogations);
    newList.remove(si);
    stateInterrogations = newList;
  }

  /**
   * Return an Iterator over all StateInterrogation instances. Synchronize to avoid
   * add/remove/iterate conflicts.
   *
   * @return an Iterator over all StateInterrogation instances.
   */
  private synchronized Iterator<StateInterrogation> getStateInterrogationIterator() {
    return stateInterrogations.iterator();
  }

  /**
   * Mark a non-binary-compatible instance dirty. Delegate to all registered StateInterrogation
   * instances until one of them handles the call.
   *
   * @param pc the instance to mark dirty
   * @param fieldName the field to mark dirty
   */
  public void nonBinaryCompatibleMakeDirty(Object pc, String fieldName) {
    Iterator<StateInterrogation> sit = getStateInterrogationIterator();
    while (sit.hasNext()) {
      StateInterrogation si = sit.next();
      try {
        if (si.makeDirty(pc, fieldName)) return;
      } catch (Throwable t) {
        continue; // ignore exceptions from errant StateInterrogations
      }
    }
  }

  /**
   * Determine the state of a non-binary-compatible instance. Delegate to all registered
   * StateInterrogation instances until one of them handles the call (returns a non-null Boolean
   * with the answer). The caller provides the stateless "method object" that does the actual call
   * to the StateInterrogation instance.
   *
   * @param pc the instance to be checked
   * @param sibr the method object that delegates to the non-binary-compatible implementation
   * @return Boolean.TRUE if the instance satisfies the state interrogation; Boolean.FALSE if the
   *     instance does not satisfy the interrogation; or null if the implementation does not manage
   *     the class of the instance
   */
  public boolean nonBinaryCompatibleIs(Object pc, StateInterrogationBooleanReturn sibr) {
    Iterator<StateInterrogation> sit = getStateInterrogationIterator();
    while (sit.hasNext()) {
      StateInterrogation si = sit.next();
      Boolean result;
      try {
        result = sibr.is(pc, si);
      } catch (Throwable t) {
        continue; // ignore exceptions from errant StateInterrogations
      }
      if (result != null) return result.booleanValue();
    }
    return false;
  }

  /**
   * Return an object associated with a non-binary-compatible instance. Delegate to all registered
   * StateInterrogation instances until one of them handles the call (returns a non-null answer).
   * The caller provides the stateless "method object" that does the actual call to the
   * StateInterrogation instance.
   *
   * @param pc the instance whose associated object is needed
   * @param sibr the method object that delegates to the non-binary-compatible implementation
   * @return the associated object or null if the implementation does not manage the class of the
   *     instance
   */
  public Object nonBinaryCompatibleGet(Object pc, StateInterrogationObjectReturn sibr) {
    Iterator<StateInterrogation> sit = getStateInterrogationIterator();
    while (sit.hasNext()) {
      StateInterrogation si = sit.next();
      Object result;
      try {
        result = sibr.get(pc, si);
      } catch (Throwable t) {
        continue; // ignore exceptions from errant StateInterrogations
      }
      if (result != null) return result;
    }
    return null;
  }

  /**
   * This is an interface used to interrogate the state of an instance that does not implement
   * PersistenceCapable. It is used for the methods that return a boolean value.
   */
  public static interface StateInterrogationBooleanReturn {
    /**
     * Interrogate the state of the instance
     *
     * @param pc the instance
     * @param si the method object
     * @return the state of the instance or null
     */
    public Boolean is(Object pc, StateInterrogation si);
  }

  /**
   * This is an interface used to interrogate the state of an instance that does not implement
   * PersistenceCapable. It is used for the methods that return an Object value.
   */
  public static interface StateInterrogationObjectReturn {
    /**
     * Return the associated instance.
     *
     * @param pc the instance
     * @param si the method object
     * @return the associated object or null
     */
    public Object get(Object pc, StateInterrogation si);
  }

  /**
   * Examines the given map for keys beginning with the JDO standard prefix, {@link
   * JDOConstants#JAVAX_JDO_PREFIX}. If any property keys are found with that prefix but are unknown
   * to this version of the JDO standard, a JDOUserException is thrown with a message indicating the
   * unknown property. Keys that are not strings are ignored, as are string keys beginning with
   * {@link JDOConstants#PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER} or not beginning with {@link
   * JDOConstants#JAVAX_JDO_PREFIX}.
   *
   * @param properties The properties to examine.
   * @see JDOConstants#JAVAX_JDO_PREFIX
   * @since 3.1
   */
  public static void assertOnlyKnownStandardProperties(Map<?, ?> properties) {
    if (properties == null || properties.isEmpty()) return;

    List<JDOUserException> exceptions = new ArrayList<>();
    StringBuilder unknowns = new StringBuilder();

    for (Object key : properties.keySet()) {
      if (!(key instanceof String)) {
        continue; // ignore keys not of type string
      }
      String s = ((String) key).toLowerCase(); // compare case-insensitively
      if (!s.startsWith(JAVAX_JDO_PREFIX_LOWER_CASED)) {
        continue; // ignore vendor-specific keys
      }
      if (s.startsWith(PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER_LOWER_CASED)) {
        continue; // ignore listener class names
      }
      if (!USER_CONFIGURABLE_STANDARD_PROPERTIES_LOWER_CASED.contains(s)) {
        exceptions.add(new JDOUserException(msg.msg("EXC_UnknownStandardProperty", s)));

        if (exceptions.size() > 1) {
          unknowns.append(",");
        }
        unknowns.append(s);
      }
    }

    if (exceptions.size() == 1) {
      throw exceptions.get(0);
    } else if (exceptions.size() > 1) {
      throw new JDOUserException(
          msg.msg("EXC_UnknownStandardProperties", unknowns.toString()),
          exceptions.toArray(new JDOUserException[] {}));
    }
  }
}
