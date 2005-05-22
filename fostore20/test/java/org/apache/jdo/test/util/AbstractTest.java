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

package org.apache.jdo.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import java.net.URL;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jdo.JDOHelper;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.jdo.JDOException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.fostore.FOStoreConnectionFactory;
import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.impl.fostore.FOStoreRemoteConnection;

/**
* Tests that we can activate a class.  Actually goes farther than that,
* storing the value of an instance of a class, since that's the only way we can
* cause a class to be activated.
*
* @author Dave Bristor
*/
public abstract class AbstractTest 
    extends TestCase
{
    protected static PrintStream out = System.out;

    protected PersistenceManagerFactory pmf;

    // Default values as given in ant build scripts
    private static final String DEFAULT = "--default--";

    // Put oid's of inserted objects here for later retrieval.
    protected List oids = new ArrayList();

    // Name of host on which server runs (remote case only)
    protected final String dbHost;

    // Port on host on which server runs (remote case only)
    protected final String dbPort;

    // Directory of the database
    protected final String dbDir;

    // Root name of the database
    protected String dbName;

    // If neither null nor empty String, access the PMF via the name given
    // with JNDI.  This takes precedence over dbName.  If dbDir is given, then
    // the JNDI InitialContext is rooted there, otherwise in the current
    // directory.
    protected String pmfLoadName = null;

    // If neither null nor empty String, loads the named file's Properties
    // object and uses it to configure a FOStoreConnectionFactory, which is
    // then set on the PMF.
    protected String cfPropsFile = null;

    // If neither null nor empty String, save the PMF under the given name
    // before exiting.
    protected String pmfSaveName = null;

    // Number of objects to insert.
    // Not final, so that subclasses can override (e.g. Test_ParallelPMs).
    protected int numInsert;

    /** Logger */
    protected Log logger =
        LogFactory.getFactory().getInstance("org.apache.jdo.test");

    /** true if debug logging in enabled. */
    protected boolean debug = logger.isDebugEnabled();

    // Maximum number of messages to print in various phases of processing
    protected final int maxMessages;
    
    // Becomes true once we've announced maxMessages
    protected boolean messagesPrinted = false;

    // If true, class reading instances should invoke verify() on each one.
    // verify() is defined in this file.
    protected int verify;

    // If true, use existing database (and fail if it's not there), otherwise
    // create a new one (which is the default).  Unlike other properties, this
    // one is not final; it can be changed by a subclass to enforce that an
    // existing database is to be used or that a new one is to be created.
    protected boolean existing;

    // Factory object by which PersistenceCapable instances are created.  Not
    // final, so that subclasses can provide their own factory.
    protected Factory factory;

    // Number of objects inserted.  See announce().
    protected int insertedCount = 0;

    protected Properties properties = null;

    // Flag indicating successful test run
    protected boolean testSucceeded;

    /**
    * Construct and initialize from properties.
    */
    protected AbstractTest() {
        super(null);

        //
        // Get property values
        //

        try {
            numInsert = Integer.parseInt(System.getProperty("insert"));
        } catch (Exception ex) {
            numInsert = getDefaultInsert(); // use default
        }
        
        int max = 10;
        try {
            max = Integer.parseInt(System.getProperty("max"));
        } catch (Exception ex) {
            // use default
        }
        maxMessages = max;

        try {
            verify = Integer.parseInt(System.getProperty("verify"));
        } catch (Exception ex) {
            verify = getDefaultVerify(); // use default
        }

        existing = Boolean.getBoolean("existing");

        String host = System.getProperty ("host", "");
        if (host.equals("")) {
            host = null;
        } else {
            host = "//" + host;
        }

        String port = System.getProperty ("port", "");
        if (port.equals("")) {
            port = null;
        } else {
            port = ":" + port;
        }

        if (null == host & null != port) {
            host = "//localhost";
        }

        if (null != host && null == port) {
            port =":" + new Integer(FOStoreRemoteConnection.DEFAULT_PORT);
        }

        dbHost = host;
        dbPort = port;

        String dir = System.getProperty("dir");
        if (null == dir || dir.equals(DEFAULT)) {
            dir = System.getProperty("user.dir");
        }
        dbDir = dir;

        String name = System.getProperty("name");
        if (null == name || name.equals(DEFAULT)) {
            name = "FOStoreTestDB";
        }
        dbName = name;

        pmfLoadName = System.getProperty("pmfLoadName");
        if (null != pmfLoadName && pmfLoadName.length() == 0) {
            pmfLoadName = null;
        }

        cfPropsFile = System.getProperty("cfPropsFile");
        if (null != cfPropsFile && cfPropsFile.length() == 0) {
            cfPropsFile = null;
        }

        pmfSaveName = System.getProperty("pmfSaveName");
        if (null != pmfSaveName && pmfSaveName.length() == 0) {
            pmfSaveName = null;
        }

        factory = getFactory(verify);
    }
    
    /** */
    protected int getDefaultInsert()
    {
        return 1;
    }
 
    /** */
    protected int getDefaultVerify()
    {
        return 0;
    }   

    /**
     * Sets up the PMF for the test.
     */
    protected void setUp() throws Exception {
        setupPMF();
        if (null != pmfSaveName) {
            savePMF();
        }
    }

    /**
     * Runs the bare test sequence.
     * @exception Throwable if any exception is thrown
     */
    public void runBare() throws Throwable {
        setUp();
        try {
            testSucceeded = false;
            runTest();
            testSucceeded = true;
        }
        finally {
            tearDown();
        }
    }

    /**
     */
    protected void tearDown() {
        try {
            insertedCount = 0;
            messagesPrinted = false;
            oids.clear();
            closePMF();
        }
        catch (Throwable ex) {
            if (debug) ex.printStackTrace();
            if (testSucceeded) {
                // runTest succeeded, but closePMF throws exception =>
                // failure
                fail("Exception during tearDown: " + ex);
            }
            else {
                // runTest failed and closePMF throws exception =>
                // just print the closePMF exception, otherwise the
                // closePMF exception would swallow the test case failure
                logger.debug("Exception during tearDown: " + ex);
            }
        }
    }

    /**
     *
     */
    protected void closePMF() {
        JDOException failure = null;
        while (pmf != null) {
            if (debug) logger.debug("closePMF");
            try {
                // Execute pmf.close in a privileged block, otherwise the test
                // class caller needs permission close a PMF.
                AccessController.doPrivileged(new PrivilegedAction () {
                        public Object run () {
                            pmf.close();
                            return null;
                        }});
                pmf = null;
            }
            catch (JDOException ex) {
                // store failure of first call pmf.close
                if (failure == null)
                    failure = ex;
                PersistenceManager[] pms = getFailedPersistenceManagers(ex);
                for (int i = 0; i < pms.length; i++) {
                    closePM(pms[i]);
                }
            }
        }

        // rethrow JDOException thrown by pmf.close
        if (failure != null)
            throw failure;
    }

    /** 
     * This method cleans up the specified 
     * <code>PersistenceManager</code>. If the pm still has an open
     * transaction, it will be rolled back, before closing the pm.
     */
    protected void closePM(PersistenceManager pm) 
    {
        if ((pm != null) && !pm.isClosed()) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            pm.close();
        }
    }

    /** */
    protected PersistenceManager[] getFailedPersistenceManagers(
        JDOException ex) {
        Throwable[] nesteds = ex.getNestedExceptions();
        int numberOfExceptions = nesteds==null ? 0 : nesteds.length;
        PersistenceManager[] result = new PersistenceManager[numberOfExceptions];
        for (int i = 0; i < numberOfExceptions; ++i) {
            JDOException exc = (JDOException)nesteds[i];
            Object failedObject = exc.getFailedObject();
            if (exc.getFailedObject() instanceof PersistenceManager) {
                result[i] = (PersistenceManager)failedObject;
            } else {
                fail("Unexpected failed object of type: " +
                     failedObject.getClass().getName());
            }
        }
        return result;
    }

    /**
     * Sets up the PMF used by this test as per defaults and system properties.
     */
    protected void setupPMF() throws Exception {
        if (null == pmfLoadName) {
            if( null == pmfSaveName &&
                null == cfPropsFile ) {
                pmf = createPMF();
            }
            else {
                pmf = new FOStorePMF();
                if (null != cfPropsFile) {
                    configCF();
                } else {
                    configPMF();
                }
            }
        } else {
            loadPMF();
        }
    }

    /**
     * Initializes JDO startup properties with default values.
     * Sub classes can overwrite this method in order to change those default
     * values.
     */
    protected void initProperties() {
        properties = new Properties();
        properties.setProperty("javax.jdo.option.ConnectionUserName", "fred");
        properties.setProperty("javax.jdo.option.ConnectionPassword", "wombat");
        properties.setProperty("javax.jdo.option.ConnectionURL", createURL());
    }

    /**
     * Returns a PMF instance using a properties file.
     * @return the PMF instance
     */
    protected PersistenceManagerFactory createPMF() throws IOException {
        initProperties();
        // Execute getResourceAsStream in a privileged block, otherwise the
        // test class caller needs permission to read the properties file. 
        InputStream input = (InputStream)AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return AbstractTest.class.getClassLoader().
                        getResourceAsStream("PMF.properties");
                }
            });
        properties.load( input );
        if( !properties.containsKey("javax.jdo.option.ConnectionURL") )
            properties.setProperty("javax.jdo.option.ConnectionURL",
                                   createURL());
        PersistenceManagerFactory pmf = 
            JDOHelper.getPersistenceManagerFactory(properties);
        if( pmf instanceof FOStorePMF )
            ((FOStorePMF)pmf).setConnectionCreate(!existing);
        return pmf;
    }

    /**
     * Configures a PMF with some basic properties, and creates the
     * corresponding database.
     */
    protected void configPMF() throws Exception {
        if( pmf instanceof FOStorePMF )
            ((FOStorePMF)pmf).setConnectionCreate(!existing);

        pmf.setConnectionUserName(System.getProperty("user", "fred"));
        pmf.setConnectionPassword(System.getProperty("password", "wombat"));

        String url = createURL();
        pmf.setConnectionURL(url);
    }

        /** Create url in string form from dbHost, dbPort, dbDir, and path.
         *@return String URL.
         */
    protected String createURL() {
        String path = "";

        if (null != dbHost) {
            path += dbHost;
        }

        if (null != dbPort) {
            path += dbPort + "/";
        }

        if (null == dbHost && null == dbPort) {
            path += dbDir + File.separator;
        }
        path += dbName;

        path = "fostore:" + path;

        if (debug) {
            logger.debug("ConnectionURL is " + path);
        }
        return path;
    }

    /**
     * Loads a previously configured PMF via JNDI.
     */
    protected void loadPMF() throws Exception {
        if (debug) logger.debug("Loading PMF via JNDI from " + pmfLoadName);
        Context c = new InitialContext();
        pmf = (FOStorePMF)c.lookup(pmfLoadName);
        c.close();
    }

    /**
     * Saves the PMF via JNDI.
     */
    protected void savePMF() throws Exception{
        if (debug) logger.debug("Saving PMF via JNDI as " + pmfSaveName);
        Context c = new InitialContext();
        c.rebind(pmfSaveName, pmf);
        c.close();
    }

    /**
     * Configures a ConnectionFactory from properties in a file.
     */
    protected void configCF() throws Exception {
        if (debug)
            logger.debug("Setting PMF's ConnectionFactory from " + cfPropsFile);
        Properties p = new Properties();
        p.load(new FileInputStream(new File(cfPropsFile)));
        FOStoreConnectionFactory cf = new FOStoreConnectionFactory();
        cf.setFromProperties(p);
        // oid file handling need dbName to be initialized => set
        // dbName to the file portion of the connections factory's url
        URL url = new URL(cf.getURL());
        dbName = url.getFile();
        if (debug) {
            logger.debug("CF: " + cf.toString());
            logger.debug("dbName: " + dbName);
        }
        pmf.setConnectionFactory(cf);
        if (debug) {
            logger.debug("PMF: " + pmf.toString());
        }
    }

    /**
     * Inserts some number of objects of some kind in the database.  The
     * number and kind are determined by defaults and system properties.
     */
    protected void insertObjects() throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            Object inserted[] = new Object[numInsert];
            for (int i = 0; i < numInsert; i++) {
                Object pc = factory.create(i);
                pm.makePersistent(pc);
                inserted[i] = pc;
            }
            
            tx.commit();
            
            for (int i = 0; i < numInsert; i++) {
                announce("inserted ", inserted[i]);
            }
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /**
     * Inserts some number of objects of some kind in the database.  The
     * number and kind are determined by defaults and system properties.
     */
    protected void readObjects() throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            int count = 0;
            for (Iterator i = oids.listIterator(); i.hasNext();) {
                Object oid = i.next();
                Object pc = pm.getObjectById(oid, true);
                if (debug)
                    logger.debug("readObjects oid: " + oid + " pc: " + pc);
                verify(count++, pc); 
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /**
     * Helper method checking whether the extent of the specified pc class
     * has the expected number of instances.
     */
    protected void checkExtent(Class pcClass, int expected) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Extent extent = pm.getExtent(pcClass, false);
            int nrOfObjects = 0;
            for (Iterator i = extent.iterator(); i.hasNext();) {
                i.next();
                nrOfObjects++;
            }
            if (debug)
                logger.debug("checkExtent: "+ pcClass.getName() + 
                             " extent has " + nrOfObjects + " instances.");
            assertEquals("Wrong number of instances in " + pcClass.getName() + 
                         " extent", expected, nrOfObjects);
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** 
     * Helper method creating an input stream for reading objects.
     **/
    protected ObjectInputStream getObjectInputStream(final String filename)
        throws IOException
    {
        try {
            return (ObjectInputStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws IOException {
                        return new ObjectInputStream(
                            new FileInputStream(filename));
                    }
                });
        }
        catch (PrivilegedActionException ex) {
            // unwrap IOException
            throw (IOException)ex.getException();
        } 
    }

    /** 
     * Helper method creating an output stream for writing oids into a file.
     **/
    protected ObjectOutputStream getObjectOutputStream(final String filename)
        throws IOException
    {
        try {
            return (ObjectOutputStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws IOException {
                        return new ObjectOutputStream(
                            new FileOutputStream(filename));
                    }
                });
        }
        catch (PrivilegedActionException ex) {
            // unwrap IOException
            throw (IOException)ex.getException();
        } 
    }
    

    /** 
     * Helper method creating an input stream for reading oids from a file.
     **/
    protected ObjectInputStream getOIDFileInputStream() throws IOException
    {
        final String name = dbDir + File.separator + dbName + ".oid";
        final File f = new File(name);
        try {
            return (ObjectInputStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws IOException {
                        assertTrue("OID file " + name + " does not exist",
                                   f.exists());
                        return new ObjectInputStream(new FileInputStream(f));
                    }
                });
        }
        catch (PrivilegedActionException ex) {
            // unwrap IOException
            throw (IOException)ex.getException();
        } 
    }
    
    /** 
     * Helper method creating an output steam for writing oids into a file.
     */
    protected ObjectOutputStream getOIDFileOutputStream() throws IOException
    {
        final File f = new File(dbDir + File.separator + dbName + ".oid");
        try {
            return (ObjectOutputStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws IOException {
                        if (f.exists()) {
                            f.delete();
                        }
                        return new ObjectOutputStream(new FileOutputStream(f));
                    }
                });
        }
        catch (PrivilegedActionException ex) {
            // unwrap IOException
            throw (IOException)ex.getException();
        } 
    }

    /**
     * Writes the OID values to a file, for later reading by tests that want
     * to fetch objects by OID.
     */
    protected void writeOIDs() throws Exception {
        ObjectOutputStream out = getOIDFileOutputStream();
        int count = 0;
        boolean messagePrinted = false;
        TreeSet sorted = new TreeSet(new OIDComparator());
        sorted.addAll(oids);
        for (Iterator i = sorted.iterator(); i.hasNext();) {
            count++;
            Object oid = i.next();
            if (debug && count < maxMessages) {
                logger.debug("writing: " + oid);
            } else {
                if (debug && !messagePrinted) {
                    logger.debug("skip remaining messages");
                    messagePrinted = true;
                }
            }
            out.writeObject(oid);
        }

        if (debug) {
            logger.debug("wrote " + count + " OID's");
        }
        out.close();
    }

    protected synchronized void announce(String msg, Object pc) {
        insertedCount++;
        Object oid = JDOHelper.getObjectId(pc);
        //MBOobjects.put(oid, pc.getClass());
        oids.add(oid);
        if (debug) {
            if (insertedCount < maxMessages) {
                logger.debug(msg + Util.getClassName(pc) +
                             ": " + oid + ", " + pc);
            } else if ( ! messagesPrinted) {
                logger.debug("skip remaining messages");
                messagesPrinted = true;
            }
        }
    }

    /**
     * Determines the kind of objects that are inserted.  
     */
    protected Factory getFactory(int verify)
    {
        return null;
    }

    /**
     *
     */
    protected Factory getFactoryByClassProperty(int verify,
                                                String defaultClassName)
    {
        Factory factory = null;
        String className = System.getProperty("class");
        if (className == null) {
            className = defaultClassName;
        }
        try {
            String name = className;
            int pos = name.indexOf("PC");
            if (pos > 0) {
                name = name.substring(0, pos) + name.substring(pos + 2);
            }
            Class factoryClass = Class.forName(name + "Factory");
            factory = (Factory)factoryClass.newInstance();
            factory.setVerify(verify);
        } catch (ClassNotFoundException ex) {
            fail("Could not load factory for " + className+ ": " + ex);
        } catch (InstantiationException ex) {
            fail("Could not instantiate instance of class " + className + ": " + ex);
        } catch (IllegalAccessException ex) {
            fail("Cannot access class " + className + " or its constructor: " + ex);
        }
        factory.setVerify(verify);
        return factory;
    }

    /**
     * Verifies that an objects is as it is supposed to be.  Leaves the
     * decision as to whether or not the object is OK up to the factory which
     * created it.
     */
    protected void verify(int i, Object pc) {
        if (verify > 0) {
            if (debug) logger.debug("verify (i = " + i + ") " + pc); 
            assertTrue("Verify pc instance", factory.verify(i, pc));
        }
    }
}
