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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.List;

import junit.framework.AssertionFailedError;

import javax.jdo.JDOFatalException;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> DatastoreTimeout
 *<BR>
 *<B>Keywords:</B> datastore timeout 
 *<BR>
 *<B>Assertion ID:</B> A12.6.9-1, A14.6.1-7
 *<BR>
 *<B>Assertion Description: </B>
 */

public class DatastoreTimeout extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.6.9-1, A14.6.1-7 (DatastoreTimeout) failed: ";

    /** Timeout value for datastore read */
    private static Integer READ_TIMEOUT = new Integer(100);

    /** Timeout value for datastore write */
    private static Integer WRITE_TIMEOUT = new Integer(100);

    /** Zero Timeout value */
    private static Integer ZERO_TIMEOUT = new Integer(0);

    /** Time for the main thread to sleep after starting a parallel thread. */
    private static int MAIN_SLEEP_MILLIS = 1000;

    /** Time for the parallel threads to sleep before commit. */
    private static int THREAD_SLEEP_MILLIS_SHORT = 2500;

    /** Time for the parallel threads to sleep before commit. */
    private static int THREAD_SLEEP_MILLIS_LONG = 7500;

    /** Oid of the PCRect instance created by localSetUp */
    private Object rectOid;

    /** Oid of the first PCPOint instance created by localSetUp */
    private Object point1Oid;

    /** Oid of the second PCPoint instance created by localSetUp */
    private Object point2Oid;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DatastoreTimeout.class);
    }

    /**
     * Method testing DatastoreReadTimeout.
     * @throws Exception exception
     */
    public void testDatastoreReadTimeout() throws Exception {

        if (debug) logger.debug("isDatastoreTimeoutSupported:" + isDatastoreTimeoutSupported());

        // Parallel thread writing the instances and causing them to be locked
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        ParallelWriter runnable = new ParallelWriter(THREAD_SLEEP_MILLIS_LONG);
        Thread t = new Thread(group, runnable, "Parallel Writer");
        t.start();

        // Wait for a second such that the other thread can lock the instances
        Thread.sleep(MAIN_SLEEP_MILLIS);
        
        try {
            runQueryReadingPCPointInstances(READ_TIMEOUT);
            runGetObjectByIdReadingPCPointInstance(READ_TIMEOUT);
            runNavigationalReadPCPointInstance(READ_TIMEOUT);
        }
        finally {
            t.join();
            Throwable problem = group.getUncaughtException(t);
            if (problem != null) {
                if (problem instanceof AssertionFailedError)
                    throw (AssertionFailedError)problem;
                else
                    throw new JDOFatalException( "Thread " + t.getName()+ 
                                                 " results in exception ", problem);
            }
        }
    }

    /**
     * Method testing DatastoreWriteTimeout.
     * @throws Exception exception
     */
    public void testDatastoreWriteTimeout() throws Exception {

        if (debug) logger.debug("isDatastoreTimeoutSupported:" + isDatastoreTimeoutSupported());

        // Parallel thread reading the instances and causing them to be locked
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        ParallelReader runnable = new ParallelReader(THREAD_SLEEP_MILLIS_LONG);
        Thread t = new Thread(group, runnable, "Parallel Reader");
        t.start();

        // Wait for a second such that the other thread can lock the instances
        Thread.sleep(MAIN_SLEEP_MILLIS);

        try {
            runUpdatePCointInstance(WRITE_TIMEOUT);
            runDeletePCPointInstance(WRITE_TIMEOUT);
            runDeletePCPointInstancesByQuery(WRITE_TIMEOUT);
        } 
        finally {
            t.join();
            Throwable problem = group.getUncaughtException(t);
            if (problem != null) {
                if (problem instanceof AssertionFailedError)
                    throw (AssertionFailedError)problem;
                else
                    throw new JDOFatalException( "Thread " + t.getName()+ 
                                                 " results in exception ", problem);
            }
        }
    }

    /**
     * Method testing zero DatastoreReadTimeout.
     * @throws Exception exception
     */
    public void testZeroDatastoreReadTimeout() throws Exception {

        // Parallel thread writing the instances and causing them to be locked
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        ParallelWriter runnable = new ParallelWriter(THREAD_SLEEP_MILLIS_SHORT);
        Thread t = new Thread(group, runnable, "Parallel Writer");
        t.start();

        // Wait for a second such that the other thread can lock the instances
        Thread.sleep(MAIN_SLEEP_MILLIS);

        try {
            runQueryReadingPCPointInstances(ZERO_TIMEOUT);
        }
        finally {
            t.join();
            Throwable problem = group.getUncaughtException(t);
            if (problem != null) {
                if (problem instanceof AssertionFailedError)
                    throw (AssertionFailedError)problem;
                else
                    throw new JDOFatalException( "Thread " + t.getName()+ 
                                                 " results in exception ", problem);
            }
        }
    }

    /**
     * Method testing zero DatastoreWriteTimeout.
     * @throws Exception exception
     */
    public void testZeroDatastoreWriteTimeout() throws Exception {

        // Parallel thread reading the instances and causing them to be locked
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        ParallelReader runnable = new ParallelReader(THREAD_SLEEP_MILLIS_SHORT);
        Thread t = new Thread(group, runnable, "Parallel Reader");
        t.start();

        // Wait for a second such that the other thread can lock the instances
        Thread.sleep(MAIN_SLEEP_MILLIS);

        try {
            runUpdatePCointInstance(ZERO_TIMEOUT);
        }
        finally {
            t.join();
            Throwable problem = group.getUncaughtException(t);
            if (problem != null) {
                if (problem instanceof AssertionFailedError)
                    throw (AssertionFailedError)problem;
                else
                    throw new JDOFatalException( "Thread " + t.getName()+ 
                                                 " results in exception ", problem);
            }
        }
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCRect.class);
        addTearDownClass(PCPoint.class);

        getPM();
        try {
            Transaction tx = pm.currentTransaction();
            tx.begin();
            PCPoint p1 = new PCPoint(110, 120);
            PCPoint p2 = new PCPoint(130, 140);
            PCRect rect = new PCRect (0, p1, p2);
            pm.makePersistent (rect);
            tx.commit();
            rectOid = pm.getObjectId(rect);
            point1Oid = pm.getObjectId(p1);
            point2Oid = pm.getObjectId(p2);
        } 
        finally {
            cleanupPM();
        }
    }

    // ----------------- Helper methods ---------------------------

    /** DatastoreReadTimeout helper method: query reading PCPoint instance. */
    private void runQueryReadingPCPointInstances(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            Query q = pm.newQuery(PCPoint.class);
            q.setDatastoreReadTimeoutMillis(timeout);
            @SuppressWarnings("unused")
            List<?> result = (List<?>)q.execute();
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.setDatastoreReadTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.setDatastoreReadTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.execute should not result in a " +
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "Query.execute should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }

    /** DatastoreReadTimeout helper method: getObjectById reading PCPoint instance. */
    private void runGetObjectByIdReadingPCPointInstance(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            pm.setDatastoreReadTimeoutMillis(timeout);
            @SuppressWarnings("unused")
            PCPoint point1 = (PCPoint)pm.getObjectById(point1Oid, true);
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreReadTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreReadTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " + 
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }

    /** DatastoreReadTimeout helper method: navigation reading PCPoint instance. */
    private void runNavigationalReadPCPointInstance(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            pm.setDatastoreReadTimeoutMillis(timeout);
            PCRect rect = (PCRect)pm.getObjectById(rectOid, true);
            PCPoint p1 = rect.getUpperLeft();
            p1.name();
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreReadTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreReadTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Navigational access should not result in a " + 
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "Navigational access should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }

    /** DatastoreWriteTimeout helper method: update PCPoint instance. */
    private void runUpdatePCointInstance(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            pm.setDatastoreWriteTimeoutMillis(timeout);
            PCPoint point1 = (PCPoint)pm.getObjectById(point1Oid, true);
            point1.setX(500);
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreWriteTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreWriteTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " + 
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }

    /** DatastoreWriteTimeout helper method: delete PCPoint instance. */
    private void runDeletePCPointInstance(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            pm.setDatastoreWriteTimeoutMillis(timeout);
            PCPoint point1 = (PCPoint)pm.getObjectById(point1Oid, true);
            pm.deletePersistent(point1);
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreWriteTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.setDatastoreWriteTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " + 
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "PM.getObjectById should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }    

    /** DatastoreWriteTimeout helper method: delete by query. */
    private void runDeletePCPointInstancesByQuery(Integer timeout) {
        getPM();
        try {
            pm.currentTransaction().begin();
            Query q = pm.newQuery(PCPoint.class);
            q.setDatastoreWriteTimeoutMillis(timeout);
            q.deletePersistentAll();
            pm.currentTransaction().commit();
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.setDatastoreWriteTimeoutMillis should throw a " +
                     "JDOUnsupportedOptionException, if datastore timeout is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.setDatastoreWriteTimeoutMillis should not result " + 
                     "in a JDOUnsupportedOptionException, if datastore timeout is supported ");
            }
        }
        catch (JDODataStoreException ex) {
            if (!isDatastoreTimeoutSupported()) {
                fail(ASSERTION_FAILED, "Query.deletePersistentAll should not result in a " +
                     "JDODataStoreException, if datastore timeout is not supported.");
            } else if (timeout == ZERO_TIMEOUT) {
                fail(ASSERTION_FAILED, "Query.deletePersistentAll should not result in a " +
                     "JDODataStoreException, if zero timeout is specified (meaning no timeout).");
            }
        }
        finally {
            cleanupPM();
        }
    }

    // ------------- Helper classes -------------------------------

    /** Runnable class updating instances and causing them to be read locked. */
    class ParallelReader implements Runnable {

        private final int sleepMillis; 

        ParallelReader(int sleepMillis) { 
            this.sleepMillis = sleepMillis;
        }

        public void run() {
            PersistenceManager parallelPM = getPMF().getPersistenceManager();
            Transaction tx = parallelPM.currentTransaction();
            // Run datastore transaction
            tx.setOptimistic(false);
            try {
                tx.begin();
                // read PCPoint instances
                PCPoint p1 = (PCPoint)parallelPM.getObjectById(point1Oid, true);
                p1.name();
                PCPoint p2 = (PCPoint)parallelPM.getObjectById(point2Oid, true);
                p2.name();
                // Give the main thread a chance to try to write the instances
                Thread.sleep(sleepMillis);
                tx.commit();
            }
            catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            finally {
                cleanupPM(parallelPM);
            }
        }
    }

    /** Runnable class updating instances and causing them to be write locked. */
    class ParallelWriter implements Runnable {

        private final int sleepMillis; 

        ParallelWriter(int sleepMillis) { 
            this.sleepMillis = sleepMillis;
        }

        public void run() {
            PersistenceManager parallelPM = getPMF().getPersistenceManager();
            Transaction tx = parallelPM.currentTransaction();
            // Run datastore transaction
            tx.setOptimistic(false);
            try {
                tx.begin();
                PCPoint p1 = (PCPoint)parallelPM.getObjectById(point1Oid);
                p1.setX(210);
                p1.setY(220);
                PCPoint p2 = (PCPoint)parallelPM.getObjectById(point2Oid);
                p2.setX(230);
                p2.setY(240);
                // Flush will set a write lock on the instances
                parallelPM.flush();
                // Give the main thread a chance to try to read the instances
                Thread.sleep(sleepMillis);
                tx.commit();
            } 
            catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } 
            finally {
                cleanupPM(parallelPM);
            }
        }
    }

}

