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

package org.apache.jdo.tck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.jdo.JDOConstants;
import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.LegacyJava;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class JDO_Test extends TestCase {
  public static final int TRANSIENT = 0;
  public static final int PERSISTENT_NEW = 1;
  public static final int PERSISTENT_CLEAN = 2;
  public static final int PERSISTENT_DIRTY = 3;
  public static final int HOLLOW = 4;
  public static final int TRANSIENT_CLEAN = 5;
  public static final int TRANSIENT_DIRTY = 6;
  public static final int PERSISTENT_NEW_DELETED = 7;
  public static final int PERSISTENT_DELETED = 8;
  public static final int PERSISTENT_NONTRANSACTIONAL = 9;
  public static final int PERSISTENT_NONTRANSACTIONAL_DIRTY = 10;
  public static final int DETACHED_CLEAN = 11;
  public static final int DETACHED_DIRTY = 12;
  public static final int NUM_STATES = 13;
  public static final int ILLEGAL_STATE = 13;

  protected static final String[] states = {
    "transient",
    "persistent-new",
    "persistent-clean",
    "persistent-dirty",
    "hollow",
    "transient-clean",
    "transient-dirty",
    "persistent-new-deleted",
    "persistent-deleted",
    "persistent-nontransactional",
    "persistent-nontransactional-dirty",
    "detached-clean",
    "detached-dirty",
    "illegal"
  };
  private static final int IS_PERSISTENT = 0;
  private static final int IS_TRANSACTIONAL = 1;
  private static final int IS_DIRTY = 2;
  private static final int IS_NEW = 3;
  private static final int IS_DELETED = 4;
  private static final int IS_DETACHED = 5;
  private static final int NUM_STATUSES = 6;

  /*
   * This table indicates the values returned by the status interrogation
   * methods for each state. This is used to determine the current lifecycle
   * state of an object.
   */
  private static final boolean[][] state_statuses = {
    // IS_PERSISTENT IS_TRANSACTIONAL    IS_DIRTY      IS_NEW      IS_DELETED  IS_DETACHED
    // transient
    {false, false, false, false, false, false},

    // persistent-new
    {true, true, true, true, false, false},

    // persistent-clean
    {true, true, false, false, false, false},

    // persistent-dirty
    {true, true, true, false, false, false},

    // hollow
    {true, false, false, false, false, false},

    // transient-clean
    {false, true, false, false, false, false},

    // transient-dirty
    {false, true, true, false, false, false},

    // persistent-new-deleted
    {true, true, true, true, true, false},

    // persistent-deleted
    {true, true, true, false, true, false},

    // persistent-nontransactional
    {true, false, false, false, false, false},

    // persistent-nontransactional-dirty
    {true, false, true, false, false, false},

    // detached_clean
    {false, false, false, false, false, true},

    // detached_dirty
    {false, false, true, false, false, true}
  };

  /** Name of the PersistenceManagerFactoryClass PMF property. */
  public static final String PMF_CLASS_PROP = "javax.jdo.PersistenceManagerFactoryClass";

  /** Name of the ConnectionURL PMF property. */
  public static final String CONNECTION_URL_PROP = "javax.jdo.option.ConnectionURL";

  /** Name of the ConnectionUserName PMF property. */
  public static final String CONNECTION_USERNAME_PROP = "javax.jdo.option.ConnectionUserName";

  /** Name of the ConnectionPassword PMF property. */
  public static final String CONNECTION_PASSWORD_PROP = "javax.jdo.option.ConnectionPassword";

  /** identitytype value for applicationidentity. */
  public static final String APPLICATION_IDENTITY = "applicationidentity";

  /** identitytype value for datastoreidentity. */
  public static final String DATASTORE_IDENTITY = "datastoreidentity";

  /** Map of transaction isolation String values to Integer */
  protected static final Map<String, Integer> levelValues = new HashMap<>();

  static {
    levelValues.put(JDOConstants.TX_READ_UNCOMMITTED, 0);
    levelValues.put(JDOConstants.TX_READ_COMMITTED, 1);
    levelValues.put(JDOConstants.TX_REPEATABLE_READ, 2);
    levelValues.put(JDOConstants.TX_SNAPSHOT, 3);
    levelValues.put(JDOConstants.TX_SERIALIZABLE, 4);
  }

  /**
   * String indicating the type of identity used for the current test case. The value is either
   * "applicationidentity" or "datastoreidentity".
   */
  protected static final String IDENTITYTYPE = System.getProperty("jdo.tck.identitytype");

  /** String indicating the name of the schema for the current test. */
  protected static final String SCHEMANAME = System.getProperty("jdo.tck.schemaname");

  /** Name of the file containing the properties for the PMF. */
  protected static String PMFProperties = System.getProperty("PMFProperties");

  /**
   * Flag indicating whether to clean up data after tests or not. If false then test will not clean
   * up data from database. The default value is true.
   */
  protected static final boolean CLEANUP_DATA =
      System.getProperty("jdo.tck.cleanupaftertest", "true").equalsIgnoreCase("true");

  /** Flag indicating whether to close the PMF after each test or not. It defaults to false. */
  protected static final boolean CLOSE_PMF_AFTER_EACH_TEST =
      System.getProperty("jdo.tck.closePMFAfterEachTest", "false").equalsIgnoreCase("true");

  /** Flag indicating whether to skip JNDI related tests. */
  protected static final boolean SKIP_JNDI =
      System.getProperty("jdo.tck.skipJndi", "false").equalsIgnoreCase("true");

  /** The Properties object for the PersistenceManagerFactory. */
  protected static Properties PMFPropertiesObject;

  /** The PersistenceManagerFactory. */
  protected static PersistenceManagerFactory pmf;

  /** The collection of supported options of the pmf. */
  protected static Collection<String> supportedOptions;

  /** The name of the pmf supported options summary file. */
  private static final String PMF_SUPPORTED_OPTIONS_FILE_NAME = "pmf_supported_options.txt";

  /** The PersistenceManager. */
  protected PersistenceManager pm;

  // Flag indicating successful test run
  protected boolean testSucceeded;

  /** Logger */
  protected final Log logger = LogFactory.getFactory().getInstance("org.apache.jdo.tck");

  /** true if debug logging in enabled. */
  protected final boolean debug = logger.isDebugEnabled();

  /**
   * Indicates an exception thrown in method <code>tearDown</code>. At the end of method <code>
   * tearDown</code> this field is nullified.
   */
  private Throwable tearDownThrowable;

  /**
   * A list of registered oid instances. Corresponding pc instances are deleted in <code>
   * localTearDown</code>.
   */
  private final Collection<Object> tearDownInstances = new LinkedList<>();

  /**
   * A list of registered pc classes. The extents of these classes are deleted in <code>
   * localTearDown</code>.
   */
  private final Collection<Class<?>> tearDownClasses = new LinkedList<>();

  /**
   * Intended for subclasses so that they may skip this class's normal set up procedure.
   *
   * @return true to run normal set up, false to skip normal set up
   */
  protected boolean preSetUp() {
    return true;
  }

  @Override
  protected final void setUp() {
    if (!preSetUp()) {
      return;
    }

    pmf = getPMF();
    localSetUp();
  }

  /**
   * Subclasses may override this method to allocate any data and resources that they need in order
   * to successfully execute this testcase.
   */
  protected void localSetUp() {}

  /**
   * Runs the bare test sequence.
   *
   * @exception Throwable if any exception is thrown
   */
  @Override
  public final void runBare() throws Throwable {
    try {
      testSucceeded = false;
      setUp();
      runTest();
      testSucceeded = true;
    } catch (Throwable e) {
      if (logger.isInfoEnabled()) logger.info("Exception during setUp or runtest: ", e);
      throw e;
    } finally {
      tearDown();
      if (debug) {
        logger.debug("Free memory: " + Runtime.getRuntime().freeMemory());
      }
    }
  }

  /**
   * Sets field <code>tearDownThrowable</code> if it is <code>null</code>. Else, the given throwable
   * is logged using fatal log level.
   *
   * @param throwable the throwable
   */
  private void setTearDownThrowable(String context, Throwable throwable) {
    if (logger.isInfoEnabled()) logger.info("Exception during " + context + ": ", throwable);
    if (this.tearDownThrowable == null) {
      this.tearDownThrowable = throwable;
    }
  }

  /**
   * Intended for subclasses so that they may skip this class's normal tear down procedure.
   *
   * @return true to run normal tear down, false to skip normal tear down
   */
  protected boolean preTearDown() {
    return true;
  }

  /**
   * This method clears data and resources allocated by testcases. It first closes the persistence
   * manager of this testcase. Then it calls method <code>localTearDown</code>. Subclasses may
   * override that method to clear any data and resources that they have allocated in method <code>
   * localSetUp</code>. Finally, this method closes the persistence manager factory.
   *
   * <p><b>Note:</b>These methods are called always, regardless of any exceptions. The first caught
   * exception is kept in field <code>tearDownThrowable</code>. That exception is thrown as a nested
   * exception of <code>JDOFatalException</code> if and only if the testcase executed successful.
   * Otherwise that exception is logged using fatal log level. All other exceptions are logged using
   * fatal log level, always.
   *
   * <p><b>Note:</b>By default, the method tearDown does not close the pmf. This is done at the end
   * of each configuration, unless the property jdo.tck.closePMFAfterEachTest is set to true.
   */
  @Override
  protected final void tearDown() {
    if (!preTearDown()) {
      return;
    }

    try {
      cleanupPM();
    } catch (Throwable t) {
      setTearDownThrowable("cleanupPM", t);
    }

    if ((pmf == null || pmf.isClosed())
        && (this.tearDownInstances.size() > 0 || this.tearDownClasses.size() > 0))
      throw new JDOFatalException(
          "PMF must not be nullified or closed when tear down instances and /or classes have been added.");

    if (pmf != null && pmf.isClosed()) pmf = null;

    try {
      if (CLEANUP_DATA) {
        localTearDown();
      }
    } catch (Throwable t) {
      setTearDownThrowable("localTearDown", t);
    }

    if (CLOSE_PMF_AFTER_EACH_TEST) {
      try {
        closePMF();
      } catch (Throwable t) {
        setTearDownThrowable("closePMF", t);
      }
    }

    if (this.tearDownThrowable != null) {
      Throwable t = this.tearDownThrowable;
      this.tearDownThrowable = null;
      if (testSucceeded) {
        // runTest succeeded, but this method threw exception => error
        throw new JDOFatalException("Exception during tearDown", t);
      }
    }
  }

  /**
   * Deletes all registered pc instances and extents of all registered pc classes. Subclasses may
   * override this method to clear any data and resources that they have allocated in method <code>
   * localSetUp</code>.
   */
  protected void localTearDown() {
    deleteTearDownInstances();
    deleteTearDownClasses();
  }

  protected void addTearDownObjectId(Object oid) {
    // ensure that oid is not a PC instance
    if (JDOHelper.getObjectId(oid) != null || JDOHelper.isTransactional(oid))
      throw new IllegalArgumentException("oid");
    this.tearDownInstances.add(oid);
  }

  protected void addTearDownInstance(Object pc) {
    Object oid = JDOHelper.getObjectId(pc);
    addTearDownObjectId(oid);
  }

  protected void addTearDownClass(Class<?> pcClass) {
    this.tearDownClasses.add(pcClass);
  }

  protected void addTearDownClass(Class<?>[] pcClasses) {
    if (pcClasses == null) return;
    for (Class<?> pcClass : pcClasses) {
      addTearDownClass(pcClass);
    }
  }

  /**
   * Deletes and removes tear down instances. If there are no tear down instances, the this method
   * is a noop. Otherwise, tear down instances are deleted exactly in the order they have been
   * added. Tear down instances are deleted in a separate transaction.
   */
  protected void deleteTearDownInstances() {
    if (this.tearDownInstances.size() > 0) {
      getPM();
      try {
        this.pm.currentTransaction().begin();
        for (Iterator<Object> i = this.tearDownInstances.iterator(); i.hasNext(); ) {
          Object pc;
          try {
            pc = this.pm.getObjectById(i.next(), true);
          } catch (JDOObjectNotFoundException e) {
            pc = null;
          }
          // we only delete those persistent instances
          // which have not been deleted by tests already.
          if (pc != null) {
            this.pm.deletePersistent(pc);
          }
        }
        this.pm.currentTransaction().commit();
      } finally {
        this.tearDownInstances.clear();
        cleanupPM();
      }
    }
  }

  /**
   * Deletes and removes tear down classes. If there are no tear down classes, the this method is a
   * noop. Otherwise, tear down classes are deleted exactly in the order they have been added. Tear
   * down classes are deleted in a separate transaction. Deleting a tear down class means to delete
   * the extent.
   */
  protected void deleteTearDownClasses() {
    if (this.tearDownClasses.size() > 0) {
      getPM();
      try {
        this.pm.currentTransaction().begin();
        for (Class<?> tearDownClass : this.tearDownClasses) {
          this.pm.deletePersistentAll(getAllObjects(this.pm, tearDownClass));
        }
        this.pm.currentTransaction().commit();
      } finally {
        this.tearDownClasses.clear();
        cleanupPM();
      }
    }
  }

  /**
   * Returns a collection of persistence instances of the specified class.
   *
   * @param pm the PersistenceManager
   * @param pcClass the class object of the PersistenceCapabale class
   * @return a Collection of persistence objects
   */
  protected <T> Collection<T> getAllObjects(PersistenceManager pm, Class<T> pcClass) {
    Query<T> query = pm.newQuery(pcClass);
    Extent<T> candidates = null;
    try {
      candidates = pm.getExtent(pcClass, false);
    } catch (JDOException ex) {
      if (debug) logger.debug("Exception thrown for getExtent of class " + pcClass.getName());
      return Collections.emptyList();
    }
    query.setCandidates(candidates);
    return query.executeList();
  }

  /**
   * Get the <code>PersistenceManagerFactory</code> instance for the implementation under test.
   *
   * @return field <code>pmf</code> if it is not <code>null</code>, else sets field <code>pmf</code>
   *     to a new instance and returns that instance.
   */
  protected PersistenceManagerFactory getPMF() {
    if (pmf == null) {
      PMFPropertiesObject = loadProperties(PMFProperties); // will exit here if no properties
      pmf = JDOHelper.getPersistenceManagerFactory(PMFPropertiesObject);
      if (supportedOptions == null) {
        supportedOptions = pmf.supportedOptions();
      }
    }
    return pmf;
  }

  protected Class<?> getPMFClass() {
    if (pmf != null) {
      return pmf.getClass();
    }

    PMFPropertiesObject = loadProperties(PMFProperties);
    String name = PMFPropertiesObject.getProperty(PMF_CLASS_PROP);
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException ex) {
      throw new JDOException("Cannot find PMF class '" + name + "'.", ex);
    }
  }

  /**
   * Get the <code>PersistenceManagerFactory</code> instance for the implementation under test. This
   * method does NOT use the JDOHelper method to retrieve the PMF, instead it creates an instance of
   * the class specified as javax.jdo.PersistenceManagerFactoryClass property. The returned PMF is
   * not configured.
   *
   * @return field <code>pmf</code> if it is not <code>null</code>, else sets field <code>pmf</code>
   *     to a new instance and returns that instance.
   */
  protected PersistenceManagerFactory getUnconfiguredPMF() {
    if (pmf == null) {
      String name = null;
      try {
        Class<?> pmfClass = getPMFClass();
        name = pmfClass.getName();
        pmf = (PersistenceManagerFactory) pmfClass.getDeclaredConstructor().newInstance();
        if (supportedOptions == null) {
          supportedOptions = pmf.supportedOptions();
        }
      } catch (NoSuchMethodException ex) {
        throw new JDOException("No no-args constructor of PMF class '" + name + "'.", ex);
      } catch (InvocationTargetException ex) {
        throw new JDOException("Exception thrown by constructor of PMF class '" + name + "'.", ex);
      } catch (InstantiationException ex) {
        throw new JDOException("Cannot instantiate PMF class '" + name + "'.", ex);
      } catch (IllegalAccessException ex) {
        throw new JDOException(
            "Cannot access PMF class '" + name + "' or its no-arg constructor.", ex);
      }
    }
    return pmf;
  }

  /**
   * Get the <code>PersistenceManager</code> instance for the implementation under test.
   *
   * @return the PersistenceManager
   */
  protected PersistenceManager getPM() {
    if (pm == null) {
      pm = getPMF().getPersistenceManager();
    }
    return pm;
  }

  /**
   * This method cleans up the environment: closes the <code>PersistenceManager</code>. This should
   * avoid leaving multiple PersistenceManager instances around, in case the
   * PersistenceManagerFactory performs PersistenceManager pooling.
   */
  protected void cleanupPM() {
    cleanupPM(pm);
    pm = null;
  }

  /**
   * This method cleans up the specified <code>PersistenceManager</code>. If the pm still has an
   * open transaction, it will be rolled back, before closing the pm.
   *
   * @param pm the PersistenceManager
   */
  protected static void cleanupPM(PersistenceManager pm) {
    if ((pm != null) && !pm.isClosed()) {
      if (pm.currentTransaction().isActive()) {
        pm.currentTransaction().rollback();
      }
      pm.close();
    }
  }

  /** Closes the pmf stored in this instance. */
  public static void closePMF() {
    JDOException failure = null;
    while (pmf != null) {
      try {
        if (!pmf.isClosed()) {
          closePMF(pmf);
        }
        pmf = null;
      } catch (JDOException ex) {
        // store failure of first call pmf.close
        if (failure == null) failure = ex;
        PersistenceManager[] pms = getFailedPersistenceManagers("closePMF", ex);
        for (PersistenceManager persistenceManager : pms) {
          cleanupPM(persistenceManager);
        }
      } catch (RuntimeException ex) {
        pmf = null;
        ex.printStackTrace(System.out);
        throw ex;
      }
    }

    // rethrow JDOException thrown by pmf.close
    if (failure != null) throw failure;
  }

  /**
   * Closes the pmf passed as a parameter. This must be done in a doPrivileged block.
   *
   * @param PMF the PersistenceManagerFactory
   */
  public static void closePMF(final PersistenceManagerFactory PMF) {
    if (PMF != null) {
      if (!PMF.isClosed()) {
        doPrivileged(
            () -> {
              PMF.close();
              return null;
            });
      }
    }
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
   * Returns failed PersistenceManagers
   *
   * @param assertionFailure failure
   * @param ex exception
   * @return failed PersistenceManagers
   */
  protected static PersistenceManager[] getFailedPersistenceManagers(
      String assertionFailure, JDOException ex) {
    Throwable[] nesteds = ex.getNestedExceptions();
    int numberOfExceptions = nesteds == null ? 0 : nesteds.length;
    PersistenceManager[] result = new PersistenceManager[numberOfExceptions];
    for (int i = 0; i < numberOfExceptions; ++i) {
      JDOException exc = (JDOException) nesteds[i];
      Object failedObject = exc.getFailedObject();
      if (exc.getFailedObject() instanceof PersistenceManager) {
        result[i] = (PersistenceManager) failedObject;
      } else {
        throw new JDOFatalException(
            assertionFailure,
            "Unexpected failed object of type: " + failedObject.getClass().getName());
      }
    }
    return result;
  }

  /**
   * This method load Properties from a given file.
   *
   * @param fileName the name of the properties file
   * @return a Properties instance with the loaded properties
   */
  protected Properties loadProperties(String fileName) {
    if (fileName == null) {
      fileName = System.getProperty("user.home") + "/.jdo/PMFProperties.properties";
    }
    Properties props = new Properties();
    InputStream propStream = null;
    try {
      propStream = new FileInputStream(fileName);
    } catch (IOException ex) {
      System.out.println("Could not open properties file \"" + fileName + "\"");
      System.out.println(
          "Please specify a system property PMFProperties "
              + "with the PMF properties file name as value "
              + "(defaults to {user.home}/.jdo/PMFProperties.properties)");
      System.exit(1);
    }
    try {
      props.load(propStream);
    } catch (IOException ex) {
      System.out.println("Error loading properties file \"" + fileName + "\"");
      ex.printStackTrace();
      System.exit(1);
    }
    return props;
  }

  /**
   * Prints the specified msg (if debug is true), before it aborts the test case.
   *
   * @param assertionFailure the assertion failure
   * @param msg the message text
   */
  public void fail(String assertionFailure, String msg) {
    if (debug) logger.debug(msg);
    fail(assertionFailure + NL + msg);
  }

  // Helper methods to check for supported options

  /**
   * Dump the supportedOptions to the a file in the specified directory.
   *
   * @param directory the directory the options are dumped to
   */
  public static void dumpSupportedOptions(String directory) {
    if (supportedOptions == null) return;
    File file = new File(directory, PMF_SUPPORTED_OPTIONS_FILE_NAME);
    if (file.exists())
      // PMF supported options have been dumped before => return
      return;
    try (PrintStream resultStream = new PrintStream(new FileOutputStream(file))) {
      for (String supportedOption : supportedOptions) {
        resultStream.println(supportedOption);
      }
    } catch (FileNotFoundException e) {
      throw new JDOFatalException("dumpSupportedOptions: cannot create file " + file.getName(), e);
    }
  }

  /**
   * Prints a message (if debug is true) saying the test with the specified name is not executed,
   * because the JDO implementation under test does not support the specified optional feature.
   *
   * @param testName the name of the test method that is skipped.
   * @param optionalFeature the name of the option not supported by the JDO implementation under
   *     tets.
   */
  protected void printUnsupportedOptionalFeatureNotTested(String testName, String optionalFeature) {
    if (debug) {
      logger.debug(
          "Test "
              + testName
              + " was not run, because optional feature "
              + optionalFeature
              + " is not supported by the JDO implementation under test");
    }
  }

  /**
   * Reports whether TransientTransactional is supported.
   *
   * @return true if TransientTransactional is supported.
   */
  public boolean isTransientTransactionalSupported() {
    return supportedOptions.contains("javax.jdo.option.TransientTransactional");
  }

  /**
   * Reports whether NontransactionalRead is supported.
   *
   * @return true if NontransactionalRead is supported.
   */
  public boolean isNontransactionalReadSupported() {
    return supportedOptions.contains("javax.jdo.option.NontransactionalRead");
  }

  /**
   * Reports whether NontransactionalWrite is supported.
   *
   * @return true if NontransactionalWrite is supported.
   */
  public boolean isNontransactionalWriteSupported() {
    return supportedOptions.contains("javax.jdo.option.NontransactionalWrite");
  }

  /**
   * Reports whether RetainValues is supported.
   *
   * @return true if RetainValues is supported.
   */
  public boolean isRetainValuesSupported() {
    return supportedOptions.contains("javax.jdo.option.RetainValues");
  }

  /**
   * Reports whether Optimistic is supported.
   *
   * @return true if Optimistic is supported.
   */
  public boolean isOptimisticSupported() {
    return supportedOptions.contains("javax.jdo.option.Optimistic");
  }

  /**
   * Reports whether Application Identity is supported.
   *
   * @return true if Application Identity is supported.
   */
  public boolean isApplicationIdentitySupported() {
    return supportedOptions.contains("javax.jdo.option.ApplicationIdentity");
  }

  /**
   * Reports whether Datastore Identity is supported.
   *
   * @return true if Datastore Identity is supported.
   */
  public boolean isDatastoreIdentitySupported() {
    return supportedOptions.contains("javax.jdo.option.DatastoreIdentity");
  }

  /**
   * Reports whether Non-Durable Identity is supported.
   *
   * @return true if Non-Durable Identity is supported.
   */
  public boolean isNonDurableIdentitySupported() {
    return supportedOptions.contains("javax.jdo.option.NonDurableIdentity");
  }

  /**
   * Reports whether an <code>ArrayList</code> collection is supported.
   *
   * @return true if an <code>ArrayList</code> collection is supported.
   */
  public boolean isArrayListSupported() {
    return supportedOptions.contains("javax.jdo.option.ArrayList");
  }

  /**
   * Reports whether a <code>HashMap</code> collection is supported.
   *
   * @return true if a <code>HashMap</code> collection is supported.
   */
  public boolean isHashMapSupported() {
    return supportedOptions.contains("javax.jdo.option.HashMap");
  }

  /**
   * Reports whether a <code>Hashtable</code> collection is supported.
   *
   * @return true if a <code>Hashtable</code> collection is supported.
   */
  public boolean isHashtableSupported() {
    return supportedOptions.contains("javax.jdo.option.Hashtable");
  }

  /**
   * Reports whether a <code>LinkedList</code> collection is supported.
   *
   * @return true if a <code>LinkedList</code> collection is supported.
   */
  public boolean isLinkedListSupported() {
    return supportedOptions.contains("javax.jdo.option.LinkedList");
  }

  /**
   * Reports whether a <code>TreeMap</code> collection is supported.
   *
   * @return true if a <code>TreeMap</code> collection is supported.
   */
  public boolean isTreeMapSupported() {
    return supportedOptions.contains("javax.jdo.option.TreeMap");
  }

  /**
   * Reports whether a <code>TreeSet</code> collection is supported.
   *
   * @return true if a <code>TreeSet</code> collection is supported.
   */
  public boolean isTreeSetSupported() {
    return supportedOptions.contains("javax.jdo.option.TreeSet");
  }

  /**
   * Reports whether a <code>Vector</code> collection is supported.
   *
   * @return true if a <code>Vector</code> collection is supported.
   */
  public boolean isVectorSupported() {
    return supportedOptions.contains("javax.jdo.option.Vector");
  }

  /**
   * Reports whether a <code>Map</code> collection is supported.
   *
   * @return true if a <code>Map</code> collection is supported.
   */
  public boolean isMapSupported() {
    return supportedOptions.contains("javax.jdo.option.Map");
  }

  /**
   * Reports whether a <code>List</code> collection is supported.
   *
   * @return true if a <code>List</code> collection is supported.
   */
  public boolean isListSupported() {
    return supportedOptions.contains("javax.jdo.option.List");
  }

  /**
   * Reports whether arrays are supported.
   *
   * @return true if arrays are supported.
   */
  public boolean isArraySupported() {
    return supportedOptions.contains("javax.jdo.option.Array");
  }

  /**
   * Reports whether a null collection is supported.
   *
   * @return true if a null collection is supported.
   */
  public boolean isNullCollectionSupported() {
    return supportedOptions.contains("javax.jdo.option.NullCollection");
  }

  /**
   * Reports whether Changing Application Identity is supported.
   *
   * @return true if Changing Application Identity is supported.
   */
  public boolean isChangeApplicationIdentitySupported() {
    return supportedOptions.contains("javax.jdo.option.ChangeApplicationIdentity");
  }

  /**
   * Reports whether Binary Compatibility is supported.
   *
   * @return true if Binary Compatibility is supported.
   */
  public boolean isBinaryCompatibilitySupported() {
    return supportedOptions.contains("javax.jdo.option.BinaryCompatibility");
  }

  /**
   * Reports whether UnconstrainedVariables is supported.
   *
   * @return true if UnconstrainedVariables is supported.
   */
  public boolean isUnconstrainedVariablesSupported() {
    return supportedOptions.contains("javax.jdo.query.JDOQL.UnconstraintedQueryVariables");
  }

  /**
   * Reports whether BitwiseOperations is supported.
   *
   * @return true if BitwiseOperations is supported.
   */
  public boolean isBitwiseOperationsSupported() {
    return supportedOptions.contains("javax.jdo.query.JDOQL.bitwiseOperations");
  }

  /**
   * Reports whether SQL queries are supported.
   *
   * @return true if SQL queries are supported.
   */
  public boolean isSQLSupported() {
    return supportedOptions.contains("javax.jdo.query.SQL");
  }

  /**
   * Reports whether getting the DataStoreConnection is supported.
   *
   * @return true if getting the DataStoreConnection is supported.
   */
  public boolean isDataStoreConnectionSupported() {
    return supportedOptions.contains("javax.jdo.option.GetDataStoreConnection");
  }

  /**
   * Reports whether canceling a running query is supported.
   *
   * @return true if canceling a running query is supported.
   */
  public boolean isQueryCancelSupported() {
    return supportedOptions.contains("javax.jdo.option.QueryCancel");
  }

  /**
   * Reports whether setting a Datastore timout is supported.
   *
   * @return true if setting a Datastore timout is supported.
   */
  public boolean isDatastoreTimeoutSupported() {
    return supportedOptions.contains(JDOConstants.OPTION_DATASTORE_TIMEOUT);
  }

  /**
   * Reports whether a feature is supported
   *
   * @param option the option
   * @return true if the specified option is supported
   */
  public boolean isSupported(String option) {
    return supportedOptions.contains(option);
  }

  /**
   * Determine if a class is loadable in the current environment.
   *
   * @param className the name of the class
   * @return true if the class is loadable in the current environment.
   */
  public static boolean isClassLoadable(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * Determine if the environment is 1.4 version of JRE or better.
   *
   * @return true if 1.4 version of JRE or better.
   */
  public static boolean isJRE14orBetter() {
    return isClassLoadable("java.util.Currency");
  }

  /**
   * This utility method returns a <code>String</code> that indicates the current state of an
   * instance.
   *
   * @param o The object.
   * @return The current state of the instance, by using the <code>JDOHelper</code> state
   *     interrogation methods.
   */
  public static String getStateOfInstance(Object o) {
    boolean existingEntries = false;
    StringBuilder buff = new StringBuilder("{");
    if (JDOHelper.isPersistent(o)) {
      buff.append("persistent");
      existingEntries = true;
    }
    if (JDOHelper.isTransactional(o)) {
      if (existingEntries) buff.append(", ");
      buff.append("transactional");
      existingEntries = true;
    }
    if (JDOHelper.isDirty(o)) {
      if (existingEntries) buff.append(", ");
      buff.append("dirty");
      existingEntries = true;
    }
    if (JDOHelper.isNew(o)) {
      if (existingEntries) buff.append(", ");
      buff.append("new");
      existingEntries = true;
    }
    if (JDOHelper.isDeleted(o)) {
      if (existingEntries) buff.append(", ");
      buff.append("deleted");
    }
    if (JDOHelper.isDetached(o)) {
      if (existingEntries) buff.append(", ");
      buff.append("detached");
    }
    buff.append("}");
    return buff.toString();
  }

  /**
   * This method will return the current lifecycle state of an instance.
   *
   * @param o the object
   * @return the current lifecycle state
   */
  public static int currentState(Object o) {
    boolean[] status = new boolean[NUM_STATUSES];
    status[IS_PERSISTENT] = JDOHelper.isPersistent(o);
    status[IS_TRANSACTIONAL] = JDOHelper.isTransactional(o);
    status[IS_DIRTY] = JDOHelper.isDirty(o);
    status[IS_NEW] = JDOHelper.isNew(o);
    status[IS_DELETED] = JDOHelper.isDeleted(o);
    status[IS_DETACHED] = JDOHelper.isDetached(o);
    int i;
    int j;
    outerloop:
    for (i = 0; i < NUM_STATES; ++i) {
      for (j = 0; j < NUM_STATUSES; ++j) {
        if (status[j] != state_statuses[i][j]) continue outerloop;
      }
      return i;
    }
    return NUM_STATES;
  }

  /**
   * Tests if a found state matches an expected state.
   *
   * @param foundState the found state
   * @param expectedState the expected state
   * @return true if the found state matches the expected state
   */
  public static boolean compareStates(int foundState, int expectedState) {
    // status interrogation gives same values for PERSISTENT_NONTRANSACTIONAL and HOLLOW
    return (expectedState < 0
        || foundState == expectedState
        || (foundState == HOLLOW && expectedState == PERSISTENT_NONTRANSACTIONAL)
        || (foundState == PERSISTENT_NONTRANSACTIONAL && expectedState == HOLLOW));
  }

  /**
   * This method mangles an object by changing all its non-static, non-final fields. It returns true
   * if the object was mangled, and false if there are no fields to mangle.
   *
   * @param oid the oid of the object
   * @return a mangled object
   * @throws Exception exception
   */
  protected boolean mangleObject(Object oid) throws IllegalAccessException {
    Field[] fields = getModifiableFields(oid);
    if (fields.length == 0) return false;
    for (Field field : fields) {
      Class<?> fieldType = field.getType();
      if (fieldType == long.class) {
        field.setLong(oid, 10000L + field.getLong(oid));
      } else if (fieldType == int.class) {
        field.setInt(oid, 10000 + field.getInt(oid));
      } else if (fieldType == short.class) {
        field.setShort(oid, (short) (10000 + field.getShort(oid)));
      } else if (fieldType == byte.class) {
        field.setByte(oid, (byte) (100 + field.getByte(oid)));
      } else if (fieldType == char.class) {
        field.setChar(oid, (char) (10 + field.getChar(oid)));
      } else if (fieldType == String.class) {
        field.set(oid, "This is certainly a challenge" + field.get(oid));
      } else if (fieldType == Integer.class) {
        field.set(oid, Integer.valueOf(10000 + ((Integer) field.get(oid)).intValue()));
      } else if (fieldType == Long.class) {
        field.set(oid, Long.valueOf(10000L + ((Long) field.get(oid)).longValue()));
      } else if (fieldType == Short.class) {
        field.set(oid, Short.valueOf((short) (10000 + ((Short) field.get(oid)).shortValue())));
      } else if (fieldType == Byte.class) {
        field.set(oid, Byte.valueOf((byte) (100 + ((Byte) field.get(oid)).byteValue())));
      } else if (fieldType == Character.class) {
        field.set(oid, Character.valueOf((char) (10 + ((Character) (field.get(oid))).charValue())));
      }
    }
    return true;
  }

  /**
   * Returns modifiable Fields of the class of the parameter. Fields are considered modifiable if
   * they are not static or final. This method requires several permissions in order to run with a
   * SecurityManager, hence the doPrivileged block:
   *
   * <ul>
   *   <li>ReflectPermission("suppressAccessChecks")
   *   <li>RuntimePermission("accessDeclaredMembers")
   * </ul>
   *
   * @param obj the object
   * @return an array of fields
   */
  protected Field[] getModifiableFields(final Object obj) {
    return doPrivileged(
        () -> {
          Class<?> cls = obj.getClass();
          List<Field> result = new ArrayList<>();
          Field[] fields = cls.getFields();
          for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) continue;
            field.setAccessible(true);
            result.add(field);
          }
          return result.toArray(new Field[result.size()]);
        });
  }

  /**
   * Returns <code>true</code> if the current test runs with application identity. This means the
   * system property jdo.tck.identitytype has the value applicationidentity.
   *
   * @return <code>true</code> if current test runs with application identity; <code>false</code>
   *     otherwise:
   */
  public boolean runsWithApplicationIdentity() {
    return APPLICATION_IDENTITY.equals(IDENTITYTYPE);
  }

  /**
   * Prints a message (if debug is true) saying the test with the specified name is not executed,
   * because the JDO implementation under test is run for an inapplicable identity type.
   *
   * @param testName the name of the test method that is skipped.
   * @param requiredIdentityType the name of the required identity type.
   */
  protected void printNonApplicableIdentityType(String testName, String requiredIdentityType) {
    if (debug) {
      logger.debug(
          "Test "
              + testName
              + " was not run, because it is only applicable for identity type "
              + requiredIdentityType
              + ". The identity type of the current configuration is "
              + IDENTITYTYPE);
    }
  }

  /**
   * Returns the value of the PMF property given by argument <code>key</code>.
   *
   * @param key the key
   * @return the value
   */
  protected String getPMFProperty(String key) {
    return PMFPropertiesObject.getProperty(key);
  }

  /**
   * Returns <code>true</code> if the implementation under test supports all JDO options contained
   * in system property <code>jdo.tck.requiredOptions</code>.
   *
   * @return <code>true</code> if the implementation under test supports all JDO options contained
   *     in system property <code>jdo.tck.requiredOptions</code>
   */
  protected boolean isTestToBePerformed() {
    boolean isTestToBePerformed = true;
    String requiredOptions = System.getProperty("jdo.tck.requiredOptions");
    //        Collection supportedOptions = supportedOptions;
    StringTokenizer tokenizer = new StringTokenizer(requiredOptions, " ,;\n\r\t");
    while (tokenizer.hasMoreTokens()) {
      String requiredOption = tokenizer.nextToken();
      logger.debug("Required option: " + requiredOption);
      if (!requiredOption.equals("") && !supportedOptions.contains(requiredOption)) {
        isTestToBePerformed = false;
        printUnsupportedOptionalFeatureNotTested(getClass().getName(), requiredOption);
      }
    }
    return isTestToBePerformed;
  }

  /** New line. */
  public static final String NL = System.getProperty("line.separator");

  /** A buffer of of error messages. */
  protected static StringBuffer messages;

  /**
   * Appends to error messages.
   *
   * @param message the message
   */
  protected static synchronized void appendMessage(String message) {
    if (messages == null) {
      messages = new StringBuffer(NL);
    }
    messages.append(message);
    messages.append(NL);
  }

  /**
   * Appends to error messages.
   *
   * @param test test option
   * @param context context
   * @param message message
   */
  protected static synchronized void deferredAssertTrue(
      boolean test, String context, String message) {
    if (!test) {
      appendMessage(context + ": " + message);
    }
  }

  /**
   * Appends an error if the actual value does not equal the expected value. Primitive values are
   * autoboxed. Null values are ok for both expected and actual.
   *
   * @param message the message
   * @param expected the expected value
   * @param actual the actual value
   */
  protected void errorIfNotEqual(String message, Object expected, Object actual) {
    if (expected == null) {
      if (actual != null) {
        appendMessage(message + " failed. expected: null; actual: " + actual);
      }
    } else {
      if (!expected.equals(actual)) {
        appendMessage(message + " failed. expected: " + expected + "; actual: " + actual);
      }
    }
  }

  /**
   * Appends an error if the actual value equals the unexpected value. Primitive values are
   * autoboxed. Null values are ok for both unexpected and actual.
   *
   * @param message the message
   * @param unexpected the unexpected value
   * @param actual the actual value
   */
  protected void errorIfEqual(String message, Object unexpected, Object actual) {
    if (unexpected == null) {
      if (actual == null) {
        appendMessage(message + " failed. unexpected: null");
      }
    } else {
      if (unexpected.equals(actual)) {
        appendMessage(message + " failed. unexpected: " + unexpected);
      }
    }
  }

  /**
   * Returns collected error messages, or <code>null</code> if there are none, and clears the
   * buffer.
   *
   * @return collected error messages
   */
  protected static synchronized String retrieveMessages() {
    if (messages == null) {
      return null;
    }
    final String msg = messages.toString();
    messages = null;
    return msg;
  }

  /** Fail the test if there are any error messages. */
  protected void failOnError() {
    String errors = retrieveMessages();
    if (errors != null) {
      fail(errors);
    }
  }

  /**
   * Validate an actual isolation level against the requested level.
   *
   * @param requested requested level
   * @param actual actual level
   * @return true if the actual level is greater or equal the requsted level
   */
  protected boolean validLevelSubstitution(String requested, String actual) {
    int requestedLevel = (levelValues.get(requested)).intValue();
    int actualLevel = (levelValues.get(actual)).intValue();
    return actualLevel >= requestedLevel;
  }
}
