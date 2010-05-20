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
 * JDOHelper.java
 *
 */
 
package javax.jdo;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import javax.jdo.spi.I18NHelper;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.JDOImplHelper.StateInterrogationBooleanReturn;
import javax.jdo.spi.JDOImplHelper.StateInterrogationObjectReturn;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.StateInterrogation;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Enumeration;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * This class can be used by a JDO-aware application to call the JDO behavior
 * of <code>PersistenceCapable</code> instances without declaring them to be
 * <code>PersistenceCapable</code>.
 * <P>It is also used to acquire a <code>PersistenceManagerFactory</code> via 
 * various methods.
 * <P>This helper class defines static methods that allow a JDO-aware
 * application to examine the runtime state of instances.  For example,
 * an application can discover whether the instance is persistent, 
 * transactional, dirty, new, deleted, or detached; and to get its associated
 * <code>PersistenceManager</code> if it has one.
 * 
 * @version 2.1
 */
public class JDOHelper implements Constants {

    /**
     * A mapping from jdoconfig.xsd element attributes to PMF properties.
     */
    static final Map<String, String> ATTRIBUTE_PROPERTY_XREF
        = createAttributePropertyXref();

    /** The Internationalization message helper.
     */
    private final static I18NHelper msg = 
        I18NHelper.getInstance ("javax.jdo.Bundle"); //NOI18N

    /**
     * Creates a map from jdoconfig.xsd element attributes to PMF properties.
     * @return An unmodifiable Map of jdoconfig.xsd element attributes to PMF
     * properties.
     */
    static Map<String, String> createAttributePropertyXref() {
        Map<String, String> xref = new HashMap<String,String>();

        xref.put(
            PMF_ATTRIBUTE_CLASS,
            PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_DRIVER_NAME,
            PROPERTY_CONNECTION_DRIVER_NAME);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_FACTORY_NAME,
            PROPERTY_CONNECTION_FACTORY_NAME);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_FACTORY2_NAME,
            PROPERTY_CONNECTION_FACTORY2_NAME);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_PASSWORD,
            PROPERTY_CONNECTION_PASSWORD);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_URL,
            PROPERTY_CONNECTION_URL);
        xref.put(
            PMF_ATTRIBUTE_CONNECTION_USER_NAME,
            PROPERTY_CONNECTION_USER_NAME);
        xref.put(
            PMF_ATTRIBUTE_IGNORE_CACHE,
            PROPERTY_IGNORE_CACHE);
        xref.put(
            PMF_ATTRIBUTE_MAPPING,
            PROPERTY_MAPPING);
        xref.put(
            PMF_ATTRIBUTE_MULTITHREADED,
            PROPERTY_MULTITHREADED);
        xref.put(
            PMF_ATTRIBUTE_NONTRANSACTIONAL_READ,
            PROPERTY_NONTRANSACTIONAL_READ);
        xref.put(
            PMF_ATTRIBUTE_NONTRANSACTIONAL_WRITE,
            PROPERTY_NONTRANSACTIONAL_WRITE);
        xref.put(
            PMF_ATTRIBUTE_OPTIMISTIC,
            PROPERTY_OPTIMISTIC);
        xref.put(
            PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME,
            PROPERTY_PERSISTENCE_UNIT_NAME);
        xref.put(
            PMF_ATTRIBUTE_NAME,
            PROPERTY_NAME);
        xref.put(
            PMF_ATTRIBUTE_RESTORE_VALUES,
            PROPERTY_RESTORE_VALUES);
        xref.put(
            PMF_ATTRIBUTE_RETAIN_VALUES,
            PROPERTY_RETAIN_VALUES);
        xref.put(
            PMF_ATTRIBUTE_DETACH_ALL_ON_COMMIT,
            PROPERTY_DETACH_ALL_ON_COMMIT);
        xref.put(
            PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID,
            PROPERTY_SERVER_TIME_ZONE_ID);
        xref.put(
            PMF_ATTRIBUTE_DATASTORE_READ_TIMEOUT_MILLIS,
            PROPERTY_DATASTORE_READ_TIMEOUT_MILLIS);
        xref.put(
            PMF_ATTRIBUTE_DATASTORE_WRITE_TIMEOUT_MILLIS,
            PROPERTY_DATASTORE_WRITE_TIMEOUT_MILLIS);

        return Collections.unmodifiableMap(xref);
    }

    /** The JDOImplHelper instance used for handling non-binary-compatible
     *  implementations.
     */
    private static JDOImplHelper implHelper = (JDOImplHelper)
        AccessController.doPrivileged(
            new PrivilegedAction<JDOImplHelper> () {
                public JDOImplHelper run () {
                    return JDOImplHelper.getInstance();
                }
            }
        );

    /** The singleton instance of JDOHelper.
     * @since 2.1
     */
    private static JDOHelper instance = new JDOHelper();

    /**
     * Return the singleton instance of JDOHelper. This instance is 
     * thread-safe.
     * @since 2.1
     * @return the thread-safe singleton JDOHelper
     */
    public static JDOHelper getInstance() {
        return instance;
    }

    /** Some applications might prefer to use instance
     * methods instead of static methods.
     * @since 2.1
     */
    public JDOHelper() {}

    /** The stateless instance used for handling non-binary-compatible
    *  implementations of getPersistenceManager.
    */
    static StateInterrogationObjectReturn getPersistenceManager =
        new StateInterrogationObjectReturn() {
            public Object get(Object pc, StateInterrogation si) {
                return si.getPersistenceManager(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of getObjectId.
    */
    static StateInterrogationObjectReturn getObjectId =
        new StateInterrogationObjectReturn() {
            public Object get(Object pc, StateInterrogation si) {
                return si.getObjectId(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of getTransactionalObjectId.
    */
    static StateInterrogationObjectReturn getTransactionalObjectId =
        new StateInterrogationObjectReturn() {
            public Object get(Object pc, StateInterrogation si) {
                return si.getTransactionalObjectId(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of getVersion.
    */
    static StateInterrogationObjectReturn getVersion =
        new StateInterrogationObjectReturn() {
            public Object get(Object pc, StateInterrogation si) {
                return si.getVersion(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isPersistent.
    */
    static StateInterrogationBooleanReturn isPersistent =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isPersistent(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isTransactional.
    */
    static StateInterrogationBooleanReturn isTransactional =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isTransactional(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isDirty.
    */
    static StateInterrogationBooleanReturn isDirty =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isDirty(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isNew.
    */
    static StateInterrogationBooleanReturn isNew =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isNew(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isDeleted.
    */
    static StateInterrogationBooleanReturn isDeleted =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isDeleted(pc);
            }
        };

   /** The stateless instance used for handling non-binary-compatible
    *  implementations of isDetached.
    */
    static StateInterrogationBooleanReturn isDetached =
        new StateInterrogationBooleanReturn() {
            public Boolean is(Object pc, StateInterrogation si) {
                return si.isDetached(pc);
            }
        };

    /** Return the associated <code>PersistenceManager</code> if there is one.
     * Transactional and persistent instances return the associated
     * <code>PersistenceManager</code>.  
     *
     * <P>Transient non-transactional instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return 
     * <code>null</code>.
     * @see PersistenceCapable#jdoGetPersistenceManager()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return the <code>PersistenceManager</code> associated with the parameter
     * instance.
     */
     public static PersistenceManager getPersistenceManager(Object pc) {
        if (pc instanceof PersistenceCapable) {
            return ((PersistenceCapable)pc).jdoGetPersistenceManager();
        } else {
            return (PersistenceManager)
                implHelper.nonBinaryCompatibleGet(pc, getPersistenceManager);
        }
      }
    
    /** Explicitly mark the parameter instance and field dirty.
     * Normally, <code>PersistenceCapable</code> classes are able to detect 
     * changes made to their fields.  However, if a reference to an array is 
     * given to a method outside the class, and the array is modified, then the
     * persistent instance is not aware of the change.  This API allows the
     * application to notify the instance that a change was made to a field.
     *
     * <P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> ignore this method.
     * @see PersistenceCapable#jdoMakeDirty(String fieldName)
     * @param pc the <code>PersistenceCapable</code> instance.
     * @param fieldName the name of the field to be marked dirty.
     */
    public static void makeDirty(Object pc, String fieldName) {
        if (pc instanceof PersistenceCapable) {
            ((PersistenceCapable)pc).jdoMakeDirty(fieldName);
        } else {
             implHelper.nonBinaryCompatibleMakeDirty(pc, fieldName);
        }
    }
    
    /** Return a copy of the JDO identity associated with the parameter 
     * instance.
     *
     * <P>Persistent instances of <code>PersistenceCapable</code> classes have a
     * JDO identity managed by the <code>PersistenceManager</code>.  This method
     * returns a copy of the ObjectId that represents the JDO identity.  
     * 
     * <P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>null</code>.
     *
     * <P>The ObjectId may be serialized
     * and later restored, and used with a <code>PersistenceManager</code> from 
     * the same JDO implementation to locate a persistent instance with the same
     * data store identity.
     *
     * <P>If the JDO identity is managed by the application, then the ObjectId 
     * may be used with a <code>PersistenceManager</code> from any JDO 
     * implementation that supports the <code>PersistenceCapable</code> class.
     *
     * <P>If the JDO identity is not managed by the application or the data 
     * store, then the ObjectId returned is only valid within the current 
     * transaction.
     *<P>
     * @see PersistenceManager#getObjectId(Object pc)
     * @see PersistenceCapable#jdoGetObjectId()
     * @see PersistenceManager#getObjectById(Object oid, boolean validate)
     * @param pc the PersistenceCapable instance.
     * @return a copy of the ObjectId of the parameter instance as of the 
     * beginning of the transaction.
     */
    public static Object getObjectId(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoGetObjectId();
        } else {
            return implHelper.nonBinaryCompatibleGet(pc, getObjectId);
        }
    }

    /** Get object ids for a collection of instances. For each instance
     * in the parameter, the getObjectId method is called. This method
     * returns one identity instance for each element 
     * in the parameter. The order of iteration of the returned
     * Collection exactly matches the order of iteration of the
     * parameter Collection.
     * @param pcs the persistence-capable instances
     * @return the object ids of the parameters
     * @see #getObjectId(Object pc)
     * @see #getObjectIds(Object[] pcs)
     * @since 2.0
     */
    public static Collection<Object> getObjectIds(Collection<?> pcs) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (Iterator<?> it = pcs.iterator(); it.hasNext();) {
            result.add(getObjectId(it.next()));
        }
        return result;
    }

    /** Get object ids for an array of instances. For each instance
     * in the parameter, the getObjectId method is called. This method
     * returns one identity instance for each element 
     * in the parameter. The order of instances of the returned
     * array exactly matches the order of instances of the
     * parameter array.
     * @param pcs the persistence-capable instances
     * @return the object ids of the parameters
     * @see #getObjectId(Object pc)
     * @see #getObjectIds(Collection pcs)
     * @since 2.0
     */
    public static Object[] getObjectIds(Object[] pcs) {
        Object[] result = new Object[pcs.length];
        for (int i = 0; i < pcs.length; ++i) {
            result[i] = getObjectId(pcs[i]);
        }
        return result;
    }

    /** Return a copy of the JDO identity associated with the parameter 
     * instance.
     *
     * @see PersistenceCapable#jdoGetTransactionalObjectId()
     * @see PersistenceManager#getObjectById(Object oid, boolean validate)
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return a copy of the ObjectId of the parameter instance as modified in 
     * this transaction.
     */
    public static Object getTransactionalObjectId(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoGetTransactionalObjectId();
        } else {
            return implHelper.nonBinaryCompatibleGet(
                pc, getTransactionalObjectId);
        }
    }
    
    /**
     * Return the version of the instance.
     * @since 2.0
     * @param pc the instance
     * @return the version of the instance
     */
    public static Object getVersion (Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoGetVersion();
        } else {
            return implHelper.nonBinaryCompatibleGet(pc, getVersion);
        }
    }
    /** Tests whether the parameter instance is dirty.
     *
     * Instances that have been modified, deleted, or newly 
     * made persistent in the current transaction return <code>true</code>.
     *
     *<P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see javax.jdo.spi.StateManager#makeDirty(PersistenceCapable pc, 
     * String fieldName)
     * @see PersistenceCapable#jdoIsDirty()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance has been modified in 
     * the current transaction.
     */
    public static boolean isDirty(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsDirty();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isDirty);
        }
    }

    /** Tests whether the parameter instance is transactional.
     *
     * Instances whose state is associated with the current transaction 
     * return true. 
     *
     *<P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>false</code>.
     * @see PersistenceCapable#jdoIsTransactional()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance is transactional.
     */
    public static boolean isTransactional(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsTransactional();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isTransactional);
        }
    }

    /** Tests whether the parameter instance is persistent.
     *
     * Instances that represent persistent objects in the data store 
     * return <code>true</code>. 
     *
     *<P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @see PersistenceCapable#jdoIsPersistent()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance is persistent.
     */
    public static boolean isPersistent(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsPersistent();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isPersistent);
        }
    }

    /** Tests whether the parameter instance has been newly made persistent.
     *
     * Instances that have been made persistent in the current transaction 
     * return <code>true</code>.
     *
     *<P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @see PersistenceCapable#jdoIsNew()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance was made persistent
     * in the current transaction.
     */
    public static boolean isNew(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsNew();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isNew);
        }
    }

    /** Tests whether the parameter instance has been deleted.
     *
     * Instances that have been deleted in the current transaction return 
     * <code>true</code>.
     *
     *<P>Transient instances and instances of classes that do not implement 
     * <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#deletePersistent(Object pc)
     * @see PersistenceCapable#jdoIsDeleted()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance was deleted
     * in the current transaction.
     */
    public static boolean isDeleted(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsDeleted();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isDeleted);
        }
    }
    
    /**
     * Tests whether the parameter instance has been detached.
     * 
     * Instances that have been detached return true.
     * 
     * <P>Transient instances return false.
     * <P>
     * @see PersistenceCapable#jdoIsDetached()
     * @return <code>true</code> if this instance is detached.
     * @since 2.0
     * @param pc the instance
     */
    public static boolean isDetached(Object pc) {
      if (pc instanceof PersistenceCapable) {
          return ((PersistenceCapable)pc).jdoIsDetached();
        } else {
            return implHelper.nonBinaryCompatibleIs(pc, isDetached);
        }
    }

    /** Accessor for the state of the passed object.
     * @param pc The object
     * @return The object state
     * @since 2.1
     */
    public static ObjectState getObjectState(Object pc) {
        if (pc == null) {
            return null;
        }

        if (isDetached(pc)) {
            if (isDirty(pc)) {
                // Detached Dirty
                return ObjectState.DETACHED_DIRTY;
            }
            else {
                // Detached Not Dirty
                return ObjectState.DETACHED_CLEAN;
            }
        }
        else {
            if (isPersistent(pc)) {
                if (isTransactional(pc)) {
                    if (isDirty(pc)) {
                        if (isNew(pc)) {
                            if (isDeleted(pc)) {
                                // Persistent Transactional Dirty New Deleted
                                return ObjectState.PERSISTENT_NEW_DELETED;
                            } else {
                                // Persistent Transactional Dirty New Not Deleted
                                return ObjectState.PERSISTENT_NEW;
                            }
                        } else {
                            if (isDeleted(pc)) {
                                // Persistent Transactional Dirty Not New Deleted
                                return ObjectState.PERSISTENT_DELETED;
                            } else {
                                // Persistent Transactional Dirty Not New Not Deleted
                                return ObjectState.PERSISTENT_DIRTY;
                            }
                        }
                    } else {
                        // Persistent Transactional Not Dirty
                        return ObjectState.PERSISTENT_CLEAN;
                    }
                }
                else {
                    if (isDirty(pc)) {
                    // Persistent Nontransactional Dirty
                        return ObjectState.PERSISTENT_NONTRANSACTIONAL_DIRTY;
                    }
                    else {
                    // Persistent Nontransactional Not Dirty
                        return ObjectState.HOLLOW_PERSISTENT_NONTRANSACTIONAL;
                    }
                }
            }
            else {
                if (isTransactional(pc)) {
                    if (isDirty(pc)) {
                        // Not Persistent Transactional Dirty
                        return ObjectState.TRANSIENT_DIRTY;                        
                    } else {
                        // Not Persistent Transactional Not Dirty
                        return ObjectState.TRANSIENT_CLEAN;
                    }
                }
                else {
                    // Not Persistent Not Transactional
                    return ObjectState.TRANSIENT;
                }
            }
        }
    }

    /** Get the anonymous <code>PersistenceManagerFactory</code> configured via
     * the standard configuration file resource "META-INF/jdoconfig.xml", using
     * the current thread's context class loader
     * to locate the configuration file resource(s).
     * @return the anonymous <code>PersistenceManagerFactory</code>.
     * @since 2.1
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        ClassLoader cl = getContextClassLoader();
        return getPersistenceManagerFactory(
                null, ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, cl, cl);
    }

    /** Get the anonymous <code>PersistenceManagerFactory</code> configured via
     * the standard configuration file resource "META-INF/jdoconfig.xml", using
     * the given class loader.
     * @return the anonymous <code>PersistenceManagerFactory</code>.
     * @param pmfClassLoader the ClassLoader used to load resources and classes
     * @since 2.1
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory(
            ClassLoader pmfClassLoader) {
        return getPersistenceManagerFactory(
                null,
                ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME,
                pmfClassLoader, pmfClassLoader);
    }

    /** Get a <code>PersistenceManagerFactory</code> based on a <code>Properties</code>
     * instance, using the current thread's context class loader to locate the
     * <code>PersistenceManagerFactory</code> class.
     * @return the <code>PersistenceManagerFactory</code>.
     * @param props a <code>Properties</code> instance with properties of the
     * <code>PersistenceManagerFactory</code>.
     * @see #getPersistenceManagerFactory(java.util.Map,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Map<?, ?> props) {
        return getPersistenceManagerFactory(
                null, props, getContextClassLoader());
    }


    /** Get a <code>PersistenceManagerFactory</code> based on a 
     * <code>Map</code> and a class loader.
     * This method delegates to the getPersistenceManagerFactory
     * method that takes a Map of overrides and a Map of properties,
     * passing null as the overrides parameter.
     * @see #getPersistenceManagerFactory(java.util.Map, java.util.Map, ClassLoader)
     * @return the <code>PersistenceManagerFactory</code>.
     * @param props a <code>Map</code> with properties of the 
     * <code>PersistenceManagerFactory</code>.
     * @param pmfClassLoader the class loader used to load the
     * <code>PersistenceManagerFactory</code> class
     * @since 1.0
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Map<?, ?> props, ClassLoader pmfClassLoader) {
        return getPersistenceManagerFactory(
                null, props, pmfClassLoader);
    }

    /**
     * Get a <code>PersistenceManagerFactory</code> based on a 
     * <code>Map</code> of overrides, a <code>Map</code> of 
     * properties, and a class loader.
     * The following are standard key names:
     * <BR><code>"javax.jdo.PersistenceManagerFactoryClass"
     * <BR>"javax.jdo.option.Optimistic",
     * <BR>"javax.jdo.option.RetainValues",
     * <BR>"javax.jdo.option.RestoreValues",
     * <BR>"javax.jdo.option.IgnoreCache",
     * <BR>"javax.jdo.option.NontransactionalRead",
     * <BR>"javax.jdo.option.NontransactionalWrite",
     * <BR>"javax.jdo.option.Multithreaded",
     * <BR>"javax.jdo.option.ConnectionUserName",
     * <BR>"javax.jdo.option.ConnectionPassword",
     * <BR>"javax.jdo.option.ConnectionURL",
     * <BR>"javax.jdo.option.ConnectionFactoryName",
     * <BR>"javax.jdo.option.ConnectionFactory2Name",
     * <BR>"javax.jdo.option.Mapping",
     * <BR>"javax.jdo.mapping.Catalog",
     * <BR>"javax.jdo.mapping.Schema",
     * <BR>"javax.jdo.option.PersistenceUnitName",
     * <BR>"javax.jdo.option.DetachAllOnCommit",
     * <BR>"javax.jdo.option.CopyOnAttach",
     * <BR>"javax.jdo.option.ReadOnly",
     * <BR>"javax.jdo.option.TransactionIsolationLevel",
     * <BR>"javax.jdo.option.TransactionType",
     * <BR>"javax.jdo.option.ServerTimeZoneID",
     * <BR>"javax.jdo.option.DatastoreReadTimeoutMillis",
     * <BR>"javax.jdo.option.DatastoreWriteTimeoutMillis",
     * <BR>"javax.jdo.option.Name".
     * </code>
     * and properties of the form
     * <BR><code>javax.jdo.option.InstanceLifecycleListener.{listenerClass}[=[{pcClasses}]]</code>
     * where <code>{listenerClass}</code> is the fully qualified name of a
     * class that implements
     * {@link javax.jdo.listener.InstanceLifecycleListener}, and
     * <code>{pcClasses}</code> is an optional comma- or whitespace-delimited
     * list of persistence-capable classes to be observed; the absence of a
     * value for a property of this form means that instances of all
     * persistence-capable classes will be observed by an instance of the given
     * listener class.
     * <P>JDO implementations
     * are permitted to define key values of their own.  Any key values not
     * recognized by the implementation must be ignored.  Key values that are
     * recognized but not supported by an implementation must result in a
     * <code>JDOFatalUserException</code> thrown by the method.
     * <P>The returned <code>PersistenceManagerFactory</code> is not 
     * configurable (the <code>set<I>XXX</I></code> methods will throw an 
     * exception).
     * <P>JDO implementations might manage a map of instantiated
     * <code>PersistenceManagerFactory</code> instances based on specified 
     * property key values, and return a previously instantiated 
     * <code>PersistenceManagerFactory</code> instance.  In this case, the 
     * properties of the returned instance must exactly match the requested 
     * properties.
     * @return the <code>PersistenceManagerFactory</code>.
     * @param props a <code>Properties</code> instance with properties of the 
     * <code>PersistenceManagerFactory</code>.
     * @param pmfClassLoader the class loader to use to load the
     * <code>PersistenceManagerFactory</code> class
     * @throws JDOFatalUserException if
     * <ul><li>the pmfClassLoader passed is invalid; or 
     * </li><li>a valid class name cannot be obtained from
     * either <code>props</code> or system resources 
     * (an entry in META-INF/services/javax.jdo.PersistenceManagerFactory); or
     * </li><li>all implementations throw an exception.
     * </li></ul>
     * @since 2.1
     */
    protected static PersistenceManagerFactory getPersistenceManagerFactory
            (Map<?, ?> overrides, Map<?, ?> props, ClassLoader pmfClassLoader) {
        List<Throwable> exceptions = new ArrayList<Throwable>();
        if (pmfClassLoader == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullLoader")); //NOI18N

        // first try to get the class name from the properties object.
        String pmfClassName = (String) props.get (
                PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS);

        if (!isNullOrBlank(pmfClassName)) {
            // a valid name was returned from the properties.
            return invokeGetPersistenceManagerFactoryOnImplementation(
                    pmfClassName, overrides, props, pmfClassLoader);

        } else {
            /*
             * If you have a jar file that provides the jdo implementation,
             * a file naming the implementation goes into the file 
             * packaged into the jar file, called
             * META-INF/services/javax.jdo.PersistenceManagerFactory.
             * The contents of the file is a string that is the PMF class name, 
             * null or blank. 
             * For each file in pmfClassLoader named
             * META-INF/services/javax.jdo.PersistenceManagerFactory,
             * this method will try to invoke the getPersistenceManagerFactory
             * method of the implementation class. 
             * Return the factory if a valid class name is extracted from 
             * resources and the invocation returns an instance.  
             * Otherwise add the exception thrown to 
             * an exception list.
             */
            Enumeration<URL> urls = null;
            try {
                urls = getResources(pmfClassLoader,
                        SERVICE_LOOKUP_PMF_RESOURCE_NAME);
            } catch (Throwable ex) {
                exceptions.add(ex);
            }

            if (urls != null){
                while (urls.hasMoreElements()) {

                    try {
                        pmfClassName = getClassNameFromURL(
                                (URL) urls.nextElement());

                        // return the implementation that is valid.
                        PersistenceManagerFactory pmf = 
                            invokeGetPersistenceManagerFactoryOnImplementation(
                                pmfClassName, overrides, props, pmfClassLoader);
                        return pmf;

                    } catch (Throwable ex) {

                        // remember exceptions from failed pmf invocations
                        exceptions.add(ex);

                    }
                }
            }
        }

        // no PMF class name in props and no services.  

        throw new JDOFatalUserException(msg.msg(
                "EXC_GetPMFNoPMFClassNamePropertyOrPUNameProperty"),
                (Throwable[])
                    exceptions.toArray(new Throwable[exceptions.size()]));
    }

    /** Get a class name from a URL. The URL is from getResources with 
     * e.g. META-INF/services/javax.jdo.PersistenceManagerFactory as the
     * parameter. Parse the file, removing blank lines, comment lines,
     * and comments.
     * @param url the URL of the services file
     * @return the name of the class contained in the file
     * @throws java.io.IOException
     * @since 2.1
     */

    protected static String getClassNameFromURL (URL url) 
            throws IOException {
        InputStream is = openStream(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                // else assume first line of text is the PMF class name
                String[] tokens = line.split("\\s");
                String pmfClassName = tokens[0];
                int indexOfComment = pmfClassName.indexOf("#");
                if (indexOfComment == -1) {
                    return pmfClassName;
                }
                // else pmfClassName has a comment at the end of it -- remove
                return pmfClassName.substring(0, indexOfComment);
            }
            return null;
        } finally {
            try {
                reader.close();
            }
            catch (IOException x) {
                // gulp
            }
        }
    }

    /**
     * Returns a named {@link PersistenceManagerFactory} or persistence
     * unit.
     *
     * @since 2.1
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String name) {
        ClassLoader cl = getContextClassLoader();
        return getPersistenceManagerFactory(null, name, cl, cl);
    }

    /**
     * Returns a named {@link PersistenceManagerFactory} or persistence
     * unit.
     *
     * @since 1.0
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String name, ClassLoader loader) {
        
        return getPersistenceManagerFactory(null, name, loader, loader);
    }

    /**
     * Returns a named {@link PersistenceManagerFactory} or persistence
     * unit.
     *
     * @since 2.0
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String name, ClassLoader resourceLoader, ClassLoader pmfLoader) {

        return getPersistenceManagerFactory(
                null, name, resourceLoader, pmfLoader);
    }

    /**
     * Returns a named {@link PersistenceManagerFactory} or persistence
     * unit.
     *
     * @since 2.1
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Map<?, ?> overrides, String name) {

        ClassLoader cl = getContextClassLoader();
        return getPersistenceManagerFactory(overrides, name, cl, cl);
    }

    /**
     * Returns a named {@link PersistenceManagerFactory} or persistence
     * unit.
     *
     * @since 2.1
     * @see #getPersistenceManagerFactory(Map,String,ClassLoader,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Map<?, ?> overrides, String name, ClassLoader resourceLoader) {

        return getPersistenceManagerFactory(
                overrides, name, resourceLoader, resourceLoader);
    }
    

    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the properties stored in the resource at
     * <code>name</code>, or, if not found, returns a
     * {@link PersistenceManagerFactory} with the given
     * name or, if not found, returns a
     * <code>javax.persistence.EntityManagerFactory</code> cast to a
     * {@link PersistenceManagerFactory}.  If the name given is null or consists
     * only of whitespace, it is interpreted as
     * {@link Constants#ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME}.
     * The following are standard key names:
     * <BR><code>"javax.jdo.PersistenceManagerFactoryClass"
     * <BR>"javax.jdo.option.Optimistic",
     * <BR>"javax.jdo.option.RetainValues",
     * <BR>"javax.jdo.option.RestoreValues",
     * <BR>"javax.jdo.option.IgnoreCache",
     * <BR>"javax.jdo.option.NontransactionalRead",
     * <BR>"javax.jdo.option.NontransactionalWrite",
     * <BR>"javax.jdo.option.Multithreaded",
     * <BR>"javax.jdo.option.ConnectionUserName",
     * <BR>"javax.jdo.option.ConnectionPassword",
     * <BR>"javax.jdo.option.ConnectionURL",
     * <BR>"javax.jdo.option.ConnectionFactoryName",
     * <BR>"javax.jdo.option.ConnectionFactory2Name",
     * <BR>"javax.jdo.option.Mapping",
     * <BR>"javax.jdo.mapping.Catalog",
     * <BR>"javax.jdo.mapping.Schema",
     * <BR>"javax.jdo.option.PersistenceUnitName".
     * <BR>"javax.jdo.option.DetachAllOnCommit".
     * <BR>"javax.jdo.option.CopyOnAttach".
     * <BR>"javax.jdo.option.TransactionType".
     * <BR>"javax.jdo.option.ServerTimeZoneID".
     * <BR>"javax.jdo.option.DatastoreReadTimeoutMillis",
     * <BR>"javax.jdo.option.DatastoreWriteTimeoutMillis",
     * <BR>"javax.jdo.option.Name".
     * </code>
     * and properties of the form
     * <BR><code>javax.jdo.option.InstanceLifecycleListener.{listenerClass}[=[{pcClasses}]]</code>
     * where <code>{listenerClass}</code> is the fully qualified name of a
     * class that implements
     * {@link javax.jdo.listener.InstanceLifecycleListener}, and
     * <code>{pcClasses}</code> is an optional comma- or whitespace-delimited
     * list of persistence-capable classes to be observed; the absence of a
     * value for a property of this form means that instances of all
     * persistence-capable classes will be observed by an instance of the given
     * listener class.
     * <P>JDO implementations
     * are permitted to define key values of their own.  Any key values not
     * recognized by the implementation must be ignored.  Key values that are
     * recognized but not supported by an implementation must result in a
     * <code>JDOFatalUserException</code> thrown by the method.
     * <P>The returned <code>PersistenceManagerFactory</code> is not 
     * configurable (the <code>set<I>XXX</I></code> methods will throw an 
     * exception).
     * 
     * This method loads the properties found at <code>name</code>, if any, via
     * <code>resourceLoader</code>, and creates a {@link
     * PersistenceManagerFactory} with <code>pmfLoader</code>. Any
     * exceptions thrown during resource loading will
     * be wrapped in a {@link JDOFatalUserException}.
     * If multiple PMFs with the requested name are found, a
     * {@link JDOFatalUserException} is thrown.
     * @since 2.1
     * @param overrides a Map containing properties that override properties
     * defined in any resources loaded according to the "name" parameter
     * @param name interpreted as the name of the resource containing the PMF
     * properties, the name of the PMF, or the persistence unit name, in that
     * order; if name is null, blank or whitespace, it is interpreted as
     * indicating the anonymous {@link PersistenceManagerFactory}.
     * @param resourceLoader the class loader to use to load properties file
     * resources; must be non-null if <code>name</code> is non-null or blank
     * @param pmfLoader the class loader to use to load the 
     * {@link PersistenceManagerFactory} or
     * <code>javax.persistence.EntityManagerFactory</code> classes
     * @return the {@link PersistenceManagerFactory} with properties in the
     * given resource, with the given name, or with the given persitence unit
     * name
     * @see Constants#ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory(
            Map<?, ?> overrides,
            String name,
            ClassLoader resourceLoader,
            ClassLoader pmfLoader) {
        if (pmfLoader == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullPMFLoader")); //NOI18N
        if (resourceLoader == null) {
            throw new JDOFatalUserException(msg.msg(
                "EXC_GetPMFNullPropsLoader")); //NOI18N
        }

        Map<Object,Object> props = null;
        // trim spaces from name and ensure non-null
        name = (name == null?ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME:name.trim());
        if (!ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME.equals(name)) {
            props = loadPropertiesFromResource(resourceLoader, name);
        }

        if (props != null) {
            // add the SPI property to inform the implementation that
            // the PMF was configured by the given resource name
            // and not via named PMF for proper deserialization
            props.put(PROPERTY_SPI_RESOURCE_NAME, name);
            props.remove(PROPERTY_NAME);
            return getPersistenceManagerFactory(overrides, props, pmfLoader);
        }
        // props were null; try getting from jdoconfig.xml
        props = getPropertiesFromJdoconfig(name, pmfLoader);
        if (props != null) {
            // inform the impl that the config came from a jdoconfig.xml
            // element with the given name
            props.put(PROPERTY_NAME, name);
            props.remove(PROPERTY_SPI_RESOURCE_NAME);
            // we have loaded a Properties, delegate to implementation
            return getPersistenceManagerFactory(overrides, props, pmfLoader);
        }
        // no properties found; last try to see if name is a JPA PU name
        if (!ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME.equals(name)) {
            props = new Properties();
            props.put(PROPERTY_PERSISTENCE_UNIT_NAME, name);
            return getPersistenceManagerFactory(overrides, props, pmfLoader);
        }
        
        // no PMF found; give up
        throw new JDOFatalUserException (msg.msg (
            "EXC_NoPMFConfigurableViaPropertiesOrXML", name)); //NOI18N
    }

    /** Invoke the getPersistenceManagerFactory method on the implementation.
     * If the overrides parameter to this method is not null, the static method 
     * with Map overrides, Map properties parameters will be invoked.
     * If the overrides parameter to this method is null,  the static method 
     * with Map properties parameter will be invoked.
     * @param pmfClassName the name of the implementation factory class
     * @param overrides a Map of overrides
     * @param properties a Map of properties
     * @param cl the class loader to use to load the implementation class
     * @return the PersistenceManagerFactory
     */
    protected static PersistenceManagerFactory
        invokeGetPersistenceManagerFactoryOnImplementation(
            String pmfClassName, Map<?, ?> overrides, Map<?, ?> properties, ClassLoader cl) {
        if (overrides != null) {
            // overrides is not null; use getPersistenceManagerFactory(Map overrides, Map props)
            try {
                Class<?> implClass = forName(pmfClassName, true, cl);
                Method m = getMethod(implClass,
                        "getPersistenceManagerFactory", //NOI18N
                        new Class[]{Map.class, Map.class});
                PersistenceManagerFactory pmf = 
                    (PersistenceManagerFactory) invoke(m,
                        null, new Object[]{overrides, properties});
                if (pmf == null) {
                        throw new JDOFatalInternalException(msg.msg (
                            "EXC_GetPMFNullPMF", pmfClassName)); //NOI18N
                    }
                return pmf;

            } catch (ClassNotFoundException e) {
                throw new JDOFatalUserException(msg.msg(
                        "EXC_GetPMFClassNotFound", pmfClassName), e); //NOI18N
            } catch (NoSuchMethodException e) {
                throw new JDOFatalInternalException(msg.msg(
                        "EXC_GetPMFNoSuchMethod2", pmfClassName), e); //NOI18N
            } catch (NullPointerException e) {
                throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFNullPointerException", pmfClassName), e); //NOI18N
            } catch (IllegalAccessException e) {
                throw new JDOFatalUserException(msg.msg(
                        "EXC_GetPMFIllegalAccess", pmfClassName), e); //NOI18N
            } catch (ClassCastException e) {
                throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFClassCastException", pmfClassName), e); //NOI18N
            } catch (InvocationTargetException ite) {
                Throwable nested = ite.getTargetException();
                if (nested instanceof JDOException) {
                    throw (JDOException)nested;
                } else throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFUnexpectedException"), ite); //NOI18N
            }
        } else {
            // overrides is null; use getPersistenceManagerFactory(Map props)
            try {
                Class<?> implClass = forName(pmfClassName, true, cl);
                Method m = getMethod(implClass,
                        "getPersistenceManagerFactory", //NOI18N
                        new Class[]{Map.class});
                PersistenceManagerFactory pmf = 
                    (PersistenceManagerFactory) invoke(m,
                        null, new Object[]{properties});
                if (pmf == null) {
                        throw new JDOFatalInternalException(msg.msg (
                            "EXC_GetPMFNullPMF", pmfClassName)); //NOI18N
                    }
                return pmf;
            } catch (ClassNotFoundException e) {
                throw new JDOFatalUserException(msg.msg(
                        "EXC_GetPMFClassNotFound", pmfClassName), e); //NOI18N
            } catch (NoSuchMethodException e) {
                throw new JDOFatalInternalException(msg.msg(
                        "EXC_GetPMFNoSuchMethod", pmfClassName), e); //NOI18N
            } catch (NullPointerException e) {
                throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFNullPointerException", pmfClassName), e); //NOI18N
            } catch (IllegalAccessException e) {
                throw new JDOFatalUserException(msg.msg(
                        "EXC_GetPMFIllegalAccess", pmfClassName), e); //NOI18N
            } catch (ClassCastException e) {
                throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFClassCastException", pmfClassName), e); //NOI18N
            } catch (InvocationTargetException ite) {
                Throwable nested = ite.getTargetException();
                if (nested instanceof JDOException) {
                    throw (JDOException)nested;
                } else throw new JDOFatalInternalException (msg.msg(
                    "EXC_GetPMFUnexpectedException"), ite); //NOI18N
            }
        }
    }

    /** Load a Properties instance by name from the class loader.
     * 
     * @param resourceLoader the class loader from which to load the properties
     * @param name the name of the resource
     * @return a Properties instance or null if no resource is found
     */
    protected static Map<Object,Object> loadPropertiesFromResource(
            ClassLoader resourceLoader, String name) {
        InputStream in = null;
        Properties props = null;
        // try to load resources from properties file
        try {
            in = getResourceAsStream(resourceLoader, name);
            if (in != null) {
                // then some kind of resource was found by the given name;
                // assume that it's a properties file
                props = new Properties();
                ((Properties) props).load(in);
            }
        } catch (IOException ioe) {
            throw new JDOFatalUserException(msg.msg(
                "EXC_GetPMFIOExceptionRsrc", name), ioe); //NOI18N
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
        }
        return props;
    }

    /**
     * @see #getNamedPMFProperties(String,ClassLoader,String)
     * @since 2.1
     */
    protected static Map<Object,Object> getPropertiesFromJdoconfig(
            String name,
            ClassLoader resourceLoader) {
        return getNamedPMFProperties(
            name, resourceLoader, JDOCONFIG_RESOURCE_NAME);
    }

    /**
     * Find and return the named {@link PersistenceManagerFactory}'s properties,
     * or null if not found.
     * If multiple named PMF property sets with
     * the given name are found (including anonymous ones), throw
     * {@link JDOFatalUserException}.
     * This method is here only to facilitate testing; the parameter
     * "jdoconfigResourceName" in public usage should always have the value
     * given in the constant {@link Constants#JDOCONFIG_RESOURCE_NAME}.
     *
     * @param name The persistence unit name; null is disallowed.
     * @param resourceLoader The ClassLoader used to load the standard JDO
     * configuration file.
     * @param jdoconfigResourceName The name of the configuration file to read.
     * In public usage, this should always be the value of
     * {@link Constants#JDOCONFIG_RESOURCE_NAME}.
     * @return The named <code>PersistenceManagerFactory</code> properties if
     * found, null if not.
     * @since 2.1
     * @throws JDOFatalUserException if multiple named PMF property sets are
     * found with the given name, or any other exception is encountered.
     */
    protected static Map<Object,Object> getNamedPMFProperties(
            String name,
            ClassLoader resourceLoader,
            String jdoconfigResourceName) {
        // key is PU name, value is Map of PU properties
        Map<String,Map<Object,Object>> propertiesByNameInAllConfigs
                = new HashMap<String,Map<Object,Object>>();
        try {
            URL firstFoundConfigURL = null;

            // get all JDO configurations
            Enumeration<URL> resources =
                getResources(resourceLoader, jdoconfigResourceName);

            if (resources.hasMoreElements()) {
                ArrayList<URL> processedResources = new ArrayList<URL>();

                // get ready to parse XML
                DocumentBuilderFactory factory = getDocumentBuilderFactory();
                do {
                    URL currentConfigURL = resources.nextElement();
                    if (processedResources.contains(currentConfigURL)) {
                        continue;
                    }
                    else {
                        processedResources.add(currentConfigURL);
                    }
                    
                    Map<String,Map<Object,Object>> propertiesByNameInCurrentConfig =
                        readNamedPMFProperties(
                            currentConfigURL,
                            name,
                            factory);

                    // try to detect duplicate requested PU
                    if (propertiesByNameInCurrentConfig.containsKey(name)) {
                        // possible dup -- check for it
                        if (firstFoundConfigURL == null) {
                            firstFoundConfigURL = currentConfigURL;
                        }
                        
                        if (propertiesByNameInAllConfigs.containsKey(name))
                            throw new JDOFatalUserException (msg.msg(
                                "EXC_DuplicateRequestedNamedPMFFoundInDifferentConfigs",
                                "".equals(name)
                                        ? "(anonymous)"
                                        : name,
                                firstFoundConfigURL.toExternalForm(),
                                currentConfigURL.toExternalForm())); //NOI18N
                    }
                    // no dups -- add found PUs to all PUs and keep going
                    propertiesByNameInAllConfigs
                        .putAll(propertiesByNameInCurrentConfig);
                } while (resources.hasMoreElements());
            }
        }
        catch (FactoryConfigurationError e) {
            throw new JDOFatalUserException(
                msg.msg("ERR_NoDocumentBuilderFactory"), e);
        }
        catch (IOException ioe) {
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFIOExceptionRsrc", name), ioe); //NOI18N
        }

        // done with reading all config resources;
        // return what we found, which may very well be null
        return (Map<Object,Object>) propertiesByNameInAllConfigs.get(name);
    }


    protected static DocumentBuilderFactory getDocumentBuilderFactory() {
        @SuppressWarnings("static-access")
        DocumentBuilderFactory factory =
                implHelper.getRegisteredDocumentBuilderFactory();
        if (factory == null) {
            factory = getDefaultDocumentBuilderFactory();
        }
        return factory;
    }
    
    protected static DocumentBuilderFactory getDefaultDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setExpandEntityReferences(true);

        return factory;
    }

    protected static ErrorHandler getErrorHandler() {
        @SuppressWarnings("static-access")
        ErrorHandler handler = implHelper.getRegisteredErrorHandler();
        if (handler == null) {
            handler = getDefaultErrorHandler();
        }
        return handler;
    }
    
    protected static ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
                public void error(SAXParseException exception)
                        throws SAXException {
                    throw exception;
                }

                public void fatalError(SAXParseException exception)
                        throws SAXException {
                    throw exception;
                }

                public void warning(SAXParseException exception)
                        throws SAXException {
                    // gulp:  ignore warnings
                }
            };
    }


    /** Reads JDO configuration file, creates a Map for each
     * persistence-manager-factory, then returns the map.
     * @param url URL of a JDO configuration file compliant with
     * javax/jdo/jdoconfig.xsd.
     * @param requestedPMFName The name of the requested
     * persistence unit (allows for fail-fast).
     * @param factory The <code>DocumentBuilderFactory</code> to use for XML
     * parsing.
     * @return a Map<String,Map> holding persistence unit configurations; for
     * the anonymous persistence unit, the
     * value of the String key is the empty string, "".
     */
    protected static Map<String,Map<Object,Object>> readNamedPMFProperties(
            URL url,
            String requestedPMFName,
            DocumentBuilderFactory factory) {
        requestedPMFName = requestedPMFName == null
            ? ""
            : requestedPMFName.trim();

        Map<String,Map<Object,Object>>
                propertiesByName = new HashMap<String,Map<Object,Object>>();
        InputStream in = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(getErrorHandler());

            in = openStream(url);
            Document doc = builder.parse(in);

            Element root = doc.getDocumentElement();
            if (root == null) {
                throw new JDOFatalUserException(
                    msg.msg("EXC_InvalidJDOConfigNoRoot", url.toExternalForm())
                );
            }

            NodeList pmfs = root.getElementsByTagName(
                ELEMENT_PERSISTENCE_MANAGER_FACTORY);

            for(int i = 0; i < pmfs.getLength(); i++) {
                Node pmfElement = pmfs.item(i);

                Properties pmfPropertiesFromAttributes
                    = readPropertiesFromPMFElementAttributes(pmfElement);

                Properties pmfPropertiesFromElements
                    = readPropertiesFromPMFSubelements(pmfElement, url);

                // for informative error handling, get name (or names) now
                String pmfNameFromAtts =
                    pmfPropertiesFromAttributes.getProperty(PROPERTY_NAME);
                String pmfNameFromElem =
                    pmfPropertiesFromElements.getProperty(PROPERTY_NAME);

                String pmfName = null;
                if (isNullOrBlank(pmfNameFromAtts)) {
                    // no PMF name attribute given
                    if (!isNullOrBlank(pmfNameFromElem)) {
                        // PMF name element was given
                        pmfName = pmfNameFromElem;
                    }
                    else  {
                        // PMF name not given at all, means the "anonymous" PMF
                        pmfName = ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME;
                    }
                }
                else {
                    // PMF name given in an attribute
                    if (!isNullOrBlank(pmfNameFromElem)) {
                        // exception -- PMF name given as both att & elem
                        throw new JDOFatalUserException(
                            msg.msg(
                                "EXC_DuplicatePMFNamePropertyFoundWithinConfig",
                                pmfNameFromAtts,
                                pmfNameFromElem,
                                url.toExternalForm()));
                    }
                    pmfName = pmfNameFromAtts;
                }
                pmfName = pmfName == null ? "" : pmfName.trim();

                // check for duplicate properties among atts & elems
                if (requestedPMFName.equals(pmfName)) {
                    Iterator<?> it =
                        pmfPropertiesFromAttributes.keySet().iterator();
                    while (it.hasNext()) {
                        String property = (String) it.next();
                        if (pmfPropertiesFromElements.contains(property)) {
                            throw new JDOFatalUserException(
                                msg.msg(
                                    "EXC_DuplicatePropertyFound",
                                    property,
                                    pmfName,
                                    url.toExternalForm()));
                        }
                    }
                }
                
                // at this point, we're guaranteed not to have duplicate
                // properties -- merge them
                Properties pmfProps = new Properties();
                pmfProps.putAll(pmfPropertiesFromAttributes);
                pmfProps.putAll(pmfPropertiesFromElements);

                // check for duplicate requested PMF name
                if (pmfName.equals(requestedPMFName)
                    && propertiesByName.containsKey(pmfName)) {

                    throw new JDOFatalUserException(msg.msg(
                            "EXC_DuplicateRequestedNamedPMFFoundInSameConfig",
                        pmfName,
                        url.toExternalForm()));
                }
                propertiesByName.put(pmfName, pmfProps);
            }
            return propertiesByName;
        }
        catch (IOException ioe) {
            throw new JDOFatalUserException(
                msg.msg("EXC_GetPMFIOExceptionRsrc", url.toString()),
                ioe); //NOI18N
        }
        catch (ParserConfigurationException e) {
            throw new JDOFatalInternalException(
                msg.msg("EXC_ParserConfigException"),
                e);
        }
        catch (SAXParseException e) {
            throw new JDOFatalUserException(
                msg.msg(
                    "EXC_SAXParseException",
                    url.toExternalForm(),
                    new Integer(e.getLineNumber()),
                    new Integer(e.getColumnNumber())),
                e);
        }
        catch (SAXException e) {
            throw new JDOFatalUserException(
                msg.msg("EXC_SAXException", url.toExternalForm()),
                e);
        }
        catch (JDOException e) {
            throw e;
        }
        catch (RuntimeException e) {
            throw new JDOFatalUserException(
                msg.msg("EXC_SAXException", url.toExternalForm()),
                e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ioe) { /* gulp */ }
            }
        }
    }

    protected static Properties readPropertiesFromPMFElementAttributes(
        Node pmfElement) {
        Properties p = new Properties();
        NamedNodeMap attributes = pmfElement.getAttributes();
        if (attributes == null) {
            return p;
        }

        for(int i = 0; i < attributes.getLength(); i++) {
            Node att = attributes.item(i);
            String attName = att.getNodeName();
            String attValue = att.getNodeValue().trim();

            String jdoPropertyName =
                (String) ATTRIBUTE_PROPERTY_XREF.get(attName);

            p.put(
                jdoPropertyName != null
                    ? jdoPropertyName
                    : attName,
                attValue);
        }

        return p;
    }

    protected static Properties readPropertiesFromPMFSubelements(
        Node pmfElement, URL url) {
        Properties p = new Properties();
        NodeList elements = pmfElement.getChildNodes();
        if (elements == null) {
            return p;
        }
        for(int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            if (element.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            
            String elementName = element.getNodeName();
            NamedNodeMap attributes = element.getAttributes();
            if (ELEMENT_PROPERTY.equalsIgnoreCase(elementName)) {
                // <property name="..." value="..."/>

                // get the "name" attribute's value (required)
                Node nameAtt = attributes.getNamedItem(PROPERTY_ATTRIBUTE_NAME);
                if (nameAtt == null) {
                    throw new JDOFatalUserException(
                        msg.msg("EXC_PropertyElementHasNoNameAttribute", url));
                }
                String name = nameAtt.getNodeValue().trim();
                if ("".equals(name)) {
                    throw new JDOFatalUserException(
                        msg.msg(
                            "EXC_PropertyElementNameAttributeHasNoValue",
                            name,
                            url));
                }
                // The next call allows users to use either the
                // <persistence-manager-factory> attribute names or the
                // "javax.jdo" property names in <property> element "name"
                // attributes.  Handy-dandy.
                String jdoPropertyName =
                    (String) ATTRIBUTE_PROPERTY_XREF.get(name);
                
                String propertyName = jdoPropertyName != null
                        ? jdoPropertyName
                        : name;

                if (p.containsKey(propertyName)) {
                    throw new JDOFatalUserException(
                        msg.msg(
                            "EXC_DuplicatePropertyNameGivenInPropertyElement",
                            propertyName,
                            url));
                }

                // get the "value" attribute's value (optional)
                Node valueAtt = attributes.getNamedItem(
                    PROPERTY_ATTRIBUTE_VALUE);
                String value = valueAtt == null
                    ? null
                    : valueAtt.getNodeValue().trim();

                p.put(propertyName, value);
            }
            else if (ELEMENT_INSTANCE_LIFECYCLE_LISTENER.equals(elementName)) {
                // <instance-lifecycle-listener listener="..." classes="..."/>

                // get the "listener" attribute's value
                Node listenerAtt = attributes.getNamedItem(
                    INSTANCE_LIFECYCLE_LISTENER_ATTRIBUTE_LISTENER);
                if (listenerAtt == null) {
                    throw new JDOFatalUserException(
                        msg.msg(
                            "EXC_MissingListenerAttribute",
                            url));
                }
                String listener = listenerAtt.getNodeValue().trim();
                if ("".equals(listener)) {
                    throw new JDOFatalUserException(
                        msg.msg(
                            "EXC_MissingListenerAttributeValue",
                            url));
                }

                // listener properties are of the form
                // "javax.jdo.option.InstanceLifecycleListener." + listener
                listener =
                    PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER + listener;

                // get the "classes" attribute's value (optional)
                Node classesAtt = attributes.getNamedItem(
                    INSTANCE_LIFECYCLE_LISTENER_ATTRIBUTE_CLASSES);
                String value = classesAtt == null
                    ? ""
                    : classesAtt.getNodeValue().trim();

                p.put(listener,  value);
            }
        }
        return p;
    }

    protected static boolean isNullOrBlank(String s) {
        return s == null || "".equals(s.trim());
    }
    
    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the properties stored in the file at
     * <code>propsFile</code>. This method is equivalent to
     * invoking {@link
     * #getPersistenceManagerFactory(File,ClassLoader)} with
     * <code>Thread.currentThread().getContextClassLoader()</code> as
     * the <code>loader</code> argument.
     * @since 2.0
     * @param propsFile the file containing the Properties
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (File propsFile) {
        return getPersistenceManagerFactory(
                propsFile, getContextClassLoader());
    }

    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the properties stored in the file at
     * <code>propsFile</code>. Creates a {@link
     * PersistenceManagerFactory} with <code>loader</code>. Any
     * <code>IOException</code>s or
     * <code>FileNotFoundException</code>s thrown during resource
     * loading will be wrapped in a {@link JDOFatalUserException}.
     * @since 2.0
     * @param propsFile the file containing the Properties
     * @param loader the class loader to use to load the 
     * <code>PersistenceManagerFactory</code> class
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (File propsFile, ClassLoader loader) {
        if (propsFile == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullFile")); //NOI18N

        InputStream in = null;
        try {
            in = new FileInputStream(propsFile);
            return getPersistenceManagerFactory(in, loader);
        } catch (FileNotFoundException fnfe) {
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNoFile", propsFile), fnfe); //NOI18N
        } finally {
            if (in != null)
                try { 
                    in.close (); 
                } catch (IOException ioe) { }
        }
    }

    /**
     * Returns a {@link PersistenceManagerFactory} at the JNDI
     * location specified by <code>jndiLocation</code> in the context
     * <code>context</code>. If <code>context</code> is
     * <code>null</code>, <code>new InitialContext()</code> will be
     * used. This method is equivalent to invoking {@link
     * #getPersistenceManagerFactory(String,Context,ClassLoader)}
     * with <code>Thread.currentThread().getContextClassLoader()</code> as
     * the <code>loader</code> argument.
     * @since 2.0
     * @param jndiLocation the JNDI location containing the 
     * PersistenceManagerFactory
     * @param context the context in which to find the named
     * PersistenceManagerFactory
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (String jndiLocation, Context context) {
        return getPersistenceManagerFactory (jndiLocation, context,
            getContextClassLoader());
    }


    /**
     * Returns a {@link PersistenceManagerFactory} at the JNDI
     * location specified by <code>jndiLocation</code> in the context
     * <code>context</code>. If <code>context</code> is
     * <code>null</code>, <code>new InitialContext()</code> will be
     * used. Creates a {@link PersistenceManagerFactory} with
     * <code>loader</code>. Any <code>NamingException</code>s thrown
     * will be wrapped in a {@link JDOFatalUserException}.
     * @since 2.0
     * @param jndiLocation the JNDI location containing the 
     * PersistenceManagerFactory
     * @param context the context in which to find the named 
     * PersistenceManagerFactory
     * @param loader the class loader to use to load the 
     * <code>PersistenceManagerFactory</code> class
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (String jndiLocation, Context context, ClassLoader loader) {
        if (jndiLocation == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullJndiLoc")); //NOI18N
        if (loader == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullLoader")); //NOI18N
        try {
            if (context == null)
                context = new InitialContext ();

            Object o = context.lookup (jndiLocation);
            return (PersistenceManagerFactory) PortableRemoteObject.narrow
                (o, PersistenceManagerFactory.class);
        } catch (NamingException ne) {
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNamingException", jndiLocation, loader), ne); //NOI18N
        }
    }
    
    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the Properties stored in the input stream at
     * <code>stream</code>. This method is equivalent to
     * invoking {@link
     * #getPersistenceManagerFactory(InputStream,ClassLoader)} with
     * <code>Thread.currentThread().getContextClassLoader()</code> as
     * the <code>loader</code> argument.
     * @since 2.0
     * @param stream the stream containing the Properties
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (InputStream stream) {
        return getPersistenceManagerFactory(
                stream, getContextClassLoader());
    }

    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the Properties stored in the input stream at
     * <code>stream</code>. Creates a {@link
     * PersistenceManagerFactory} with <code>loader</code>. Any
     * <code>IOException</code>s thrown during resource
     * loading will be wrapped in a {@link JDOFatalUserException}.
     * @since 2.0
     * @param stream the stream containing the Properties
     * @param loader the class loader to use to load the 
     * <code>PersistenceManagerFactory</code> class
     * @return the PersistenceManagerFactory
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (InputStream stream, ClassLoader loader) {
        if (stream == null)
            throw new JDOFatalUserException (msg.msg (
                "EXC_GetPMFNullStream")); //NOI18N

        Properties props = new Properties ();
        try {
            props.load (stream);
        } catch (IOException ioe) {
            throw new JDOFatalUserException
                (msg.msg ("EXC_GetPMFIOExceptionStream"), ioe); //NOI18N
        }
        return getPersistenceManagerFactory (props, loader);
    }

    /**
     * Get a <code>JDOEnhancer</code> using the available enhancer(s) specified in
     * "META-INF/services/JDOEnhancer" using the context class loader.
     * @return the <code>JDOEnhancer</code>.
     * @throws JDOFatalUserException if no available enhancer
     * @since 3.0
     */
    public static JDOEnhancer getEnhancer() {
        return getEnhancer(getContextClassLoader());
    }

    /**
     * Get a <code>JDOEnhancer</code> using the available enhancer(s) specified in
     * "META-INF/services/JDOEnhancer"
     * @param loader the loader to use for loading the JDOEnhancer class (if any)
     * @return the <code>JDOEnhancer</code>.
     * @throws JDOFatalUserException if no available enhancer
     * @since 3.0
     */
    public static JDOEnhancer getEnhancer(ClassLoader loader) {
            ClassLoader ctrLoader = loader;
        if (ctrLoader == null) {
            ctrLoader = Thread.currentThread().getContextClassLoader();
        }

    /*
     * If you have a jar file that provides the jdo enhancer implementation,
     * a file naming the implementation goes into the file 
     * packaged into the jar file, called "META-INF/services/javax.jdo.JDOEnhancer".
     * The contents of the file is a string that is the enhancer class name.
     * For each file in the class loader named "META-INF/services/javax.jdo.JDOEnhancer",
     * this method will invoke the default constructor of the implementation class.
     * Return the enhancer if a valid class name is extracted from resources and
     * the invocation returns an instance.
     * Otherwise add the exception thrown to an exception list.
     */
        ArrayList<Throwable> exceptions = new ArrayList<Throwable>();
        int numberOfJDOEnhancers = 0;
        try {
            Enumeration<URL> urls = getResources(loader, SERVICE_LOOKUP_ENHANCER_RESOURCE_NAME);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    numberOfJDOEnhancers++;
                    try {
                        String enhancerClassName = getClassNameFromURL((URL)urls.nextElement());
                        Class<?> enhancerClass = forName(enhancerClassName, true, ctrLoader);
                        JDOEnhancer enhancer = (JDOEnhancer)enhancerClass.newInstance();
                        return enhancer;
                    } catch (Throwable ex) {
                        // remember exceptions from failed enhancer invocations
                        exceptions.add(ex);
                    }
                }
            }
        } catch (Throwable ex) {
            exceptions.add(ex);
        }

        throw new JDOFatalUserException(msg.msg("EXC_GetEnhancerNoValidEnhancerAvailable", numberOfJDOEnhancers),
                (Throwable[])exceptions.toArray(new Throwable[exceptions.size()]));
    }

    /** Get the context class loader associated with the current thread. 
     * This is done in a doPrivileged block because it is a secure method.
     * @return the current thread's context class loader.
     * @since 2.0
     */
    private static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(
            new PrivilegedAction<ClassLoader> () {
                public ClassLoader run () {
                    return Thread.currentThread().getContextClassLoader();
                }
            }
        );
    }

    /** Get the named resource as a stream from the resource loader.
     * Perform this operation in a doPrivileged block.
     */
    private static InputStream getResourceAsStream(
            final ClassLoader resourceLoader, final String name) {
        return AccessController.doPrivileged(
            new PrivilegedAction<InputStream>() {
                public InputStream run() {
                    return resourceLoader.getResourceAsStream(name);
                }
            }
        );
    }


    /** Get the named Method from the named class. 
     * Perform this operation in a doPrivileged block.
     * 
     * @param implClass the class
     * @param methodName the name of the method
     * @param parameterTypes the parameter types of the method
     * @return the Method instance
     */
    private static Method getMethod(
            final Class<?> implClass, 
            final String methodName, 
            final Class<?>[] parameterTypes) 
                throws NoSuchMethodException {
        try {
            return AccessController.doPrivileged(
                new PrivilegedExceptionAction<Method>() {
                    public Method run() throws NoSuchMethodException {
                        return implClass.getMethod(methodName, parameterTypes);
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            throw (NoSuchMethodException)ex.getException();
        }
    }

    /** Invoke the method.
     * Perform this operation in a doPrivileged block.
     */
    private static Object invoke(final Method method,
            final Object instance, final Object[] parameters) 
                throws IllegalAccessException, InvocationTargetException {
        try {
            return (Object) AccessController.doPrivileged(
                new PrivilegedExceptionAction<Object>() {
                    public Object run() 
                        throws IllegalAccessException, 
                            InvocationTargetException {
                        return method.invoke (instance, parameters);
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            Exception cause = (Exception)ex.getException();
            if (cause instanceof IllegalAccessException)
                throw (IllegalAccessException)cause;
            else //if (cause instanceof InvocationTargetException)
                throw (InvocationTargetException)cause;
        }
    }

    /** Get resources of the resource loader. 
     * Perform this operation in a doPrivileged block.
     * @param resourceLoader
     * @param resourceName
     * @return the resources
     */
    protected static Enumeration<URL> getResources(
            final ClassLoader resourceLoader, 
            final String resourceName) 
                throws IOException {
        try {
            return AccessController.doPrivileged(
                new PrivilegedExceptionAction<Enumeration<URL>>() {
                    public Enumeration<URL> run() throws IOException {
                        return resourceLoader.getResources(resourceName);
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            throw (IOException)ex.getException();
        }
    }

    /** Get the named class.
     * Perform this operation in a doPrivileged block.
     * 
     * @param name the name of the class
     * @param init whether to initialize the class
     * @param loader which class loader to use
     * @return the class
     */
    private static Class<?> forName(
            final String name, 
            final boolean init, 
            final ClassLoader loader) 
                throws ClassNotFoundException {
        try {
            return AccessController.doPrivileged(
                new PrivilegedExceptionAction<Class<?>>() {
                    public Class<?> run() throws ClassNotFoundException {
                        return Class.forName(name, init, loader);
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            throw (ClassNotFoundException)ex.getException();
        }
    }

    /** Open an input stream on the url.
     * Perform this operation in a doPrivileged block.
     * 
     * @param url
     * @return the input stream
     */
    private static InputStream openStream(final URL url) 
            throws IOException {
        try {
            return AccessController.doPrivileged(
                new PrivilegedExceptionAction<InputStream>() {
                    public InputStream run() throws IOException {
                        return url.openStream();
                    }
                }
            );
        } catch (PrivilegedActionException ex) {
            throw (IOException)ex.getException();
        }
    }

}
