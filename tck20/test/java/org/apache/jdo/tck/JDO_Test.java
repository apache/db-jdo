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


package org.apache.jdo.tck;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;

import javax.jdo.JDOException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class JDO_Test extends TestCase {
    public static final int TRANSIENT                   = 0;
    public static final int PERSISTENT_NEW              = 1;
    public static final int PERSISTENT_CLEAN            = 2;
    public static final int PERSISTENT_DIRTY            = 3;
    public static final int HOLLOW                      = 4;
    public static final int TRANSIENT_CLEAN             = 5;
    public static final int TRANSIENT_DIRTY             = 6;
    public static final int PERSISTENT_NEW_DELETED      = 7;
    public static final int PERSISTENT_DELETED          = 8;
    public static final int PERSISTENT_NONTRANSACTIONAL = 9;
    public static final int NUM_STATES = 10;
    public static final int ILLEGAL_STATE = 10;

    public static final String[] states = {
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
        "illegal"
    };
    private static final int IS_PERSISTENT       = 0;
    private static final int IS_TRANSACTIONAL    = 1;
    private static final int IS_DIRTY            = 2;
    private static final int IS_NEW              = 3;
    private static final int IS_DELETED          = 4;
    private static final int NUM_STATUSES        = 5;

    /*
     * This table indicates the values returned by the status interrogation
     * methods for each state. This is used to determine the current lifecycle
     * state of an object.
     */
    private static final boolean state_statuses[][] = {
        // IS_PERSISTENT IS_TRANSACTIONAL    IS_DIRTY      IS_NEW      IS_DELETED
        // transient
        {   false,          false,              false,      false,      false},

        // persistent-new
        {   true,           true,               true,       true,       false},

        // persistent-clean
        {   true,           true,               false,      false,      false},

        // persistent-dirty
        {   true,           true,               true,       false,      false},

        // hollow
        {   true,           false,              false,      false,      false},

        // transient-clean
        {   false,          true,               false,      false,      false},

        // transient-dirty
        {   false,          true,               true,       false,      false},

        // persistent-new-deleted
        {   true,           true,               true,       true,       true},

        // persistent-deleted
        {   true,           true,               true,       false,      true},

        // persistent-nontransactional
        {   true,           false,              false,      false,      false}
    };
  
    /** Name of the file contaninig the properties for the PMF. */
    protected static String PMFProperties;

    /** The Properties object for the PersistenceManagerFactory. */
    protected static Properties PMFPropertiesObject;

    /** The PersistenceManagerFactory. */
    protected PersistenceManagerFactory pmf;
    
    /** The PersistenceManager. */
    protected PersistenceManager pm;
    
    // Flag indicating successful test run
    protected boolean testSucceeded;

    /** Logger */
    protected Log logger = 
        LogFactory.getFactory().getInstance("org.apache.jdo.tck");

    /** true if debug logging in enabled. */
    protected boolean debug = logger.isDebugEnabled();
    
    /** 
     * Indicates an exception thrown in method <code>tearDown</code>.
     * At the end of method <code>tearDown</code> this field is nullified. 
     */
    private Throwable tearDownThrowable;
    
    /** 
     * A list of registered oid instances. 
     * Corresponding pc instances are deleted in <code>localTearDown</code>.
     */
    private Collection oids = new LinkedList();
    
    /** 
     * A list of registered pc classes. 
     * Th extents of these classes are deleted in <code>localTearDown</code>.
     */
    private Collection pcClasses = new LinkedList();
    
    /** */
    protected JDO_Test() {
        PMFProperties = System.getProperty("PMFProperties");
    }

    /** */
    protected void setUp() throws Exception {
        pmf = getPMF();
        localSetUp();
    }
    
    /**
     * Subclasses may override this method to allocate any data and resources
     * that they need in order to successfully execute this testcase.
     */
    protected void localSetUp() {}
    
    /**
     * Runs the bare test sequence.
     * @exception Throwable if any exception is thrown
     */
    public void runBare() throws Throwable {
        try {
            testSucceeded = false;
            setUp();
            runTest();
            testSucceeded = true;
        }
        catch (AssertionFailedError e) {
            logger.error("Exception during setUp or runtest: ", e);
            throw e;
        }
        catch (Throwable t) {
            logger.fatal("Exception during setUp or runtest: ", t);
            throw t;
        }
        finally {
            tearDown();
            if (debug) {
                logger.debug("Free memory: " + Runtime.getRuntime().freeMemory());
            }
        }
    }

    /**
     * Sets field <code>tearDownThrowable</code> if it is <code>null</code>.
     * Else, the given throwable is logged using fatal log level. 
     * @param throwable the throwable
     */
    private void setTearDownThrowable(String context, Throwable throwable)
    {
        logger.fatal("Exception during "+context+": ", throwable);
        if (this.tearDownThrowable == null) {
            this.tearDownThrowable = throwable;
        }
    }
    
    /**
     * This method clears data and resources allocated by testcases.
     * It first closes the persistence manager of this testcase.
     * Then it calls method <code>localTearDown</code>. 
     * Subclasses may override that method to clear any data and resources
     * that they have allocated in method <code>localSetUp</code>.
     * Finally, this method closes the persistence manager factory.<p>
     * 
     * <b>Note:</b>These methods are called always, regardless of any exceptions.
     * The first caught exception is kept in field <code>tearDownThrowable</code>. 
     * That exception is thrown as a nested exception of <code>JDOFatalException</code>
     * if and only if the testcase executed successful.
     * Otherwise that exception is logged using fatal log level.
     * All other exceptions are logged using fatal log level, always.
     */
    protected void tearDown() {
        try {
            cleanupPM();
        } 
        catch (Throwable t) {
            setTearDownThrowable("cleanupPM", t);
        }
        
        try {
            localTearDown();
        } 
        catch (Throwable t) {
            setTearDownThrowable("localTearDown", t);
        }
        
        try {
            closePMF();
        }
        catch (Throwable t) {
            setTearDownThrowable("closePMF", t);
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
     * Deletes all registered pc instances and extents of all registered pc classes. 
     * Subclasses may override this method to clear any data and resources
     * that they have allocated in method <code>localSetUp</code>.
     */
    protected void localTearDown() {
        deleteAndUnregisterPCInstances();
        deleteAndUnregisterPCClasses();
    }

    protected void addTearDownObjectId(Object oid) {
        // ensure that oid is not a PC instance
        if (JDOHelper.getObjectId(oid) != null ||
            JDOHelper.isTransactional(oid))
            throw new IllegalArgumentException("oid");
        this.oids.add(oid);
    }
    
    protected void addTearDownInstance(Object pc) {
        Object oid = JDOHelper.getObjectId(pc);
        addTearDownObjectId(oid);
    }
    
    protected void addTearDownClass(Class pcClass) {
        this.pcClasses.add(pcClass);
    }
    
    /**
     * Deletes and unregistres all registered pc instances. 
     */
    protected void deleteAndUnregisterPCInstances() {
        getPM();
        try {
            this.pm.currentTransaction().begin();
            for (Iterator i = this.oids.iterator(); i.hasNext(); ) {
                Object pc;
                try {
                    pc = this.pm.getObjectById(i.next(), true);
                }
                catch (JDOObjectNotFoundException e) {
                    pc = null;
                }
                // we only delete those persistent instances
                // which have not been deleted by tests already.
                if (pc != null) {
                    this.pm.deletePersistent(pc);
                }
            }
            this.pm.currentTransaction().commit();
        }
        finally {
            this.oids.clear();
            cleanupPM();
        }
    }

    /**
     * Deletes extents of all registered pc instances and unregisters all pc classes. 
     */
    protected void deleteAndUnregisterPCClasses() {
        getPM();
        try {
            this.pm.currentTransaction().begin();
            for (Iterator i = this.pcClasses.iterator(); i.hasNext(); ) {
                this.pm.deletePersistentAll(getAllObjects(this.pm, (Class)i.next()));
            }
            this.pm.currentTransaction().commit();
        }
        finally {
            this.pcClasses.clear();
            cleanupPM();
        }
    }

    /** */
    protected Collection getAllObjects(PersistenceManager pm, Class pcClass) {
        Collection col = new Vector() ;
        Query query = pm.newQuery();
        query.setClass(pcClass);
        query.setCandidates(pm.getExtent(pcClass, false));
        Object result = query.execute();
        return (Collection)result;
    }

    /**
     * Get the <code>PersistenceManagerFactory</code> instance 
     * for the implementation under test.
     */
    protected PersistenceManagerFactory getPMF()
    {
        if (pmf == null) {
            PMFPropertiesObject = loadProperties(PMFProperties); // will exit here if no properties
            pmf = JDOHelper.getPersistenceManagerFactory(PMFPropertiesObject);
        }
        return pmf;
    }

    /**
     * Get the <code>PersistenceManager</code> instance 
     * for the implementation under test.
     */
    protected PersistenceManager getPM() {
        if (pm == null) {
            pm = getPMF().getPersistenceManager();
        }
        return pm;
    }

    
    /** 
     * This method cleans up the environment: closes the
     * <code>PersistenceManager</code>.  This  should avoid leaving
     * multiple PersistenceManager instances around, in case the
     * PersistenceManagerFactory performs PersistenceManager pooling.  
     */
    protected void cleanupPM() 
    {
        cleanupPM(pm);
        pm = null;
    }

    /** 
     * This method cleans up the specified 
     * <code>PersistenceManager</code>. If the pm still has an open
     * transaction, it will be rolled back, before closing the pm.
     */
    protected static void cleanupPM(PersistenceManager pm) 
    {
        if ((pm != null) && !pm.isClosed()) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            pm.close();
        }
    }

    /** Closes the pmf stored in this instance. */
    protected void closePMF()
    {
        JDOException failure = null;
        while (pmf != null) {
            try {
                pmf.close();
                pmf = null;
            }
            catch (JDOException ex) {
                // store failure of first call pmf.close
                if (failure == null)
                    failure = ex;
                PersistenceManager[] pms = getFailedPersistenceManagers(
                    "closePMF", ex);
                for (int i = 0; i < pms.length; i++) {
                    cleanupPM(pms[i]);
                }
            }
        }

        // rethrow JDOException thrown by pmf.close
        if (failure != null)
            throw failure;
    }

    /** */
    protected PersistenceManager[] getFailedPersistenceManagers(
        String assertionFailure, JDOException ex) {
        Throwable[] nesteds = ex.getNestedExceptions();
        int numberOfExceptions = nesteds==null ? 0 : nesteds.length;
        PersistenceManager[] result = new PersistenceManager[numberOfExceptions];
        for (int i = 0; i < numberOfExceptions; ++i) {
            JDOException exc = (JDOException)nesteds[i];
            Object failedObject = exc.getFailedObject();
            if (exc.getFailedObject() instanceof PersistenceManager) {
                result[i] = (PersistenceManager)failedObject;
            } else {
                fail(assertionFailure,
                     "Unexpected failed object of type: " +
                     failedObject.getClass().getName());
            }
        }
        return result;
    }

    /**
     * This method load Properties from a given file.
     */
    protected Properties loadProperties(String fileName)
    {
        if (fileName == null) {
            fileName = System.getProperty("user.home") + "/.jdo/PMFProperties.properties";
        }
        Properties props = new Properties();
        InputStream propStream = null;
        try {
            propStream = new FileInputStream(fileName);
        }
        catch (IOException ex) {
            System.out.println("Could not open properties file \"" + fileName + "\"");
            System.out.println("Please specify a system property PMFProperties " + 
                               "with the PMF properties file name as value " + 
                               "(defaults to {user.home}/.jdo/PMFProperties.properties)");
            System.exit(1);
        }
        try {
            props.load(propStream);
        } 
        catch (IOException ex) {
            System.out.println("Error loading properties file \"" + fileName + "\"");
            ex.printStackTrace();
            System.exit(1);
        }
        return props;
    }

    /** 
     * Prints the specified msg (if debug is true), before it aborts the
     * test case.
     */
    public void fail(String assertionFailure, String msg) {
        if (debug) logger.debug(msg);
        fail(assertionFailure + "\n" + msg);
    }

    // Helper methods to check for supported options
    
    /** 
     * Prints a message (if debug is true) saying the test with the
     * specified name is not executed, because the JDO implementation under
     * test does not support the specified optional feature. 
     * @param testName the name of the test method that is skipped.
     * @param optionalFeature the name of the option not supported by the
     * JDO implementation under tets.
     */
    protected void printUnsupportedOptionalFeatureNotTested(
        String testName, String optionalFeature) {
        if (debug) {
            logger.debug(
                "Test " + testName + 
                " was not run, because optional feature " + optionalFeature + 
                " is not supported by the JDO implementation under test");
        }
    }

    /** Reports whether TransientTransactional is supported. */
    public boolean isTransientTransactionalSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.TransientTransactional");
    }
    
    /** Reports whether NontransactionalRead is supported. */
    public boolean isNontransactionalReadSupported(){
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.NontransactionalRead");
    }
    
    /** Reports whether NontransactionalWrite is supported. */
    public boolean isNontransactionalWriteSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.NontransactionalWrite");
    }

    /** Reports whether RetainValues is supported. */
    public boolean isRetainValuesSupported()
    {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.RetainValues");
    }

    /** Reports whether Optimistic is supported. */
    public boolean isOptimisticSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.Optimistic");
    }

    /** Reports whether Application Identity is supported. */
    public boolean isApplicationIdentitySupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.ApplicationIdentity");
    }

    /** Reports whether Datastore Identity is supported. */
    public boolean isDatastoreIdentitySupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.DatastoreIdentity");
    }

    /** Reports whether Non-Durable Identity is supported. */
    public boolean isNonDurableIdentitySupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.NonDatastoreIdentity");
    }

    /** Reports whether an <code>ArrayList</code> collection is supported. */
    public boolean isArrayListSupported() {        
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.ArrayList");
    }

    /** Reports whether a <code>HashMap</code> collection is supported. */
    public boolean isHashMapSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.HashMap");
    }

    /** Reports whether a <code>Hashtable</code> collection is supported. */
    public boolean isHashtableSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.Hashtable");
    }

    /** Reports whether a <code>LinkedList</code> collection is supported. */
    public boolean isLinkedListSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.LinkedList");
    }

    /** Reports whether a <code>TreeMap</code> collection is supported. */
    public boolean isTreeMapSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.TreeMap");
    }

    /** Reports whether a <code>TreeSet</code> collection is supported. */
    public boolean isTreeSetSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.TreeSet");
    }

    /** Reports whether a <code>Vector</code> collection is supported. */
    public boolean isVectorSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.Vector");
    }

    /** Reports whether a <code>Map</code> collection is supported. */
    public boolean isMapSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.Map");
    }

    /** Reports whether a <code>List</code> collection is supported. */
    public boolean isListSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.List");
    }

    /** Reports whether arrays are supported. */
    public boolean isArraySupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.Array");
    }

    /** Reports whether a null collection is supported. */
    public boolean isNullCollectionSupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.NullCollection");
    }

    /** Reports whether Changing Application Identity is supported. */
    public boolean isChangeApplicationIdentitySupported() {
        return getPMF().supportedOptions().contains(
            "javax.jdo.option.ChangeApplicationIdentity");
    }

    /**
     * This utility method returns a <code>String</code> that indicates the
     * current state of an instance. 
     * @param o The object.
     * @return The current state of the instance, by using the
     * <code>JDOHelper</code> state interrogation methods.
     */
    public static String getStateOfInstance(Object o)
    {
        boolean existingEntries = false;
        StringBuffer buff = new StringBuffer("{");
        if( JDOHelper.isPersistent(o) ){
            buff.append("persistent");
            existingEntries = true;
        }
        if( JDOHelper.isTransactional(o) ){
            if( existingEntries ) buff.append(", ");
            buff.append("transactional");
            existingEntries = true;
        }
        if( JDOHelper.isDirty(o) ){
            if( existingEntries ) buff.append(", ");
            buff.append("dirty");
            existingEntries = true;
        }
        if( JDOHelper.isNew(o) ){
            if( existingEntries ) buff.append(", ");
            buff.append("new");
            existingEntries = true;
        }
        if( JDOHelper.isDeleted(o) ){
            if( existingEntries ) buff.append(", ");
            buff.append("deleted");
        }
        buff.append("}");
        return buff.toString();
    }
    
    /**
     * This method will return the current lifecycle state of an instance.
     */
    public static int currentState(Object o)
    {
        boolean[] status = new boolean[5];
        status[IS_PERSISTENT]       = JDOHelper.isPersistent(o);
        status[IS_TRANSACTIONAL]    = JDOHelper.isTransactional(o);
        status[IS_DIRTY]            = JDOHelper.isDirty(o);
        status[IS_NEW]              = JDOHelper.isNew(o);
        status[IS_DELETED]          = JDOHelper.isDeleted(o);
        int i, j;
    outerloop:
        for( i = 0; i < NUM_STATES; ++i ){
            for( j = 0; j < NUM_STATUSES; ++j ){
                if( status[j] != state_statuses[i][j] )
                    continue outerloop;
            }
            return i;
        }
        return NUM_STATES;
    }

    /** This method mangles an object by changing all its public fields
     */
    protected void mangleObject (Object oid) 
        throws Exception {
        Class oidClass = oid.getClass();
        Field[] fields = oidClass.getFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            if (debug) 
                logger.debug("field" + i + " has name: " + field.getName() +
                             " type: " + field.getType());
            Class fieldType = field.getType();
            if (fieldType == long.class) {
                field.setLong(oid, 10000L);
            }
            if (fieldType == int.class) {
                field.setInt(oid, 10000);
            }
            if (fieldType == short.class) {
                field.setShort(oid, (short)10000);
            }
            if (fieldType == byte.class) {
                field.setByte(oid, (byte)100);
            }
            if (fieldType == char.class) {
                field.setChar(oid, '0');
            }
            if (fieldType == String.class) {
                field.set(oid, "This is certainly a challenge");
            }
            if (fieldType == Integer.class) {
                field.set(oid, new Integer(10000));
            }
            if (fieldType == Long.class) {
                field.set(oid, new Long(10000L));
            }
            if (fieldType == Short.class) {
                field.set(oid, new Short((short)10000));
            }
            if (fieldType == Byte.class) {
                field.set(oid, new Byte((byte)100));
            }
            if (fieldType == Character.class) {
                field.set(oid, new Character('0'));
            }
        }
    }

}
