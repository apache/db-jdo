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

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;

/**
 * Represents a request to get the extent of a class and possibly its
 * subclasses.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
/**
 * This is an in-memory extent.
 */
class FOStoreExtent implements Extent {
private final FOStoreConnector connector;
private final RequestFactory rf;

    /** Iterators requested by user. */
    private final HashSet iterators = new HashSet();

    /** Class specified by user. */
    private final Class cls;

    /** If true, extent includes subclasses of user's class. */
    private final boolean subclasses;

    /** Persistence manager on which getExtent was invoked by user. */
    private final PersistenceManagerInternal pm;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    FOStoreExtent(Class cls, boolean subclasses,
                  PersistenceManagerInternal pm,
                  RequestFactory rf,
                  FOStoreConnector connector) {

         this.cls = cls;
         this.subclasses = subclasses;
         this.pm = pm;
         this.rf = rf;
         this.connector = connector;
    }

    //
    // Implement Extent
    //

    /**
     * @see javax.jdo.Extent#iterator
     */
    public Iterator iterator() {
        pm.assertReadAllowed();
        Iterator rc = new FOStoreExtentIterator(this);
        iterators.add(rc);
        return rc;
    }

    /**
     * @see javax.jdo.Extent#hasSubclasses
     */
    public boolean hasSubclasses() {
        return subclasses;
    }

    /**
     * @see javax.jdo.Extent#getCandidateClass
     */
    public Class getCandidateClass() {
        return cls;
    }

    /**
     * @see javax.jdo.Extent#getPersistenceManager
     */
    public PersistenceManager getPersistenceManager() {
        return pm.getCurrentWrapper();
    }

    /**
     * @see javax.jdo.Extent#closeAll
     */
    public void closeAll() {
        for (Iterator i = iterators.iterator(); i.hasNext();) {
            Iterator extentIterator = (Iterator)i.next();
            if (logger.isDebugEnabled()) {
                logger.debug("Extent.closeAll: removing " + extentIterator); // NOI18N
            }
            i.remove(); // remove first because close also removes it
            close(extentIterator);
        }
    }

    /**
     * @see javax.jdo.Extent#close
     */
    public void close(Iterator it) {
        if (it instanceof FOStoreExtentIterator) {
            FOStoreExtentIterator fit = (FOStoreExtentIterator)it;
            fit.close();
            if (iterators.contains(fit)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Extent.close: removing " + fit); // NOI18N
                }
                // this will be true only if not executing closeAll.
                iterators.remove(fit);
            }
        }
    }

    /**
     * Iterates over the elements in a FOStoreExtent.
     */
    class FOStoreExtentIterator implements Iterator {
        /** Instances that have been retrieved from store. */
        // We have our own copy and do not use those from the enclosing
        // FOStoreExtent, because we use a GetInstancesRequest to re-fill
        // the ArrayList if needed.
        private ArrayList instances;
        private ArrayList oids;
        private int maxInstances;

        /** Index into instances. */
        private int instanceIndex = 0;

        /** Index into oids. */
        private int oidsIndex = 0;

        /** Index into extent as a whole.*/
        private int index = 0;
    
        /** Size of extent (number of instances + number of oids). */
        private final int size;

        /** If false, then can get next object, otherwise 
         * next() always throws NoSuchElementException and 
         * hasNext() always returns false. */
        private boolean closed = false;

        private Object nextObject = null;

        FOStoreExtentIterator(FOStoreExtent extent) {
            try {
                Message message = connector.getMessage();
                GetExtentRequest request =
                    rf.getGetExtentRequest(extent, cls, subclasses, message, pm);
                request.doRequest();
                connector.flush();
                instances = request.getInstances();
                oids = request.getOIDs();
                maxInstances = request.getMaxInstances();
            } catch (IOException ex) {
                throw new FOStoreFatalIOException(
                    getClass(), "init", ex); // NOI18N
            } catch (JDOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new FOStoreFatalInternalException(
                    getClass(), "init", ex); // NOI18N
            }
    
            Collection insertedInstances = pm.getInsertedInstances();
            for (Iterator it = insertedInstances.iterator(); it.hasNext();) {
                Object o = it.next();
                Class clz = o.getClass();
                if (cls.equals(clz) || (subclasses && cls.isAssignableFrom(clz))) {
                    instances.add(o);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug(
                    "FOStoreExtentIterator.<init>: cls=" + cls.getName() + // NOI18N
                    ", numInstances=" + instances.size()); // NOI18N
            }

            this.size = instances.size() + oids.size();
            if (size == 0) {
                close();
            }
        }

        /**
         * @see java.util.Iterator#hasNext
         */
        public boolean hasNext() {
            // This code advances to the next not deleted
            // instance, or until the iterator is closed.
            // Iterator is closed in getNext() if there
            // are no more instances available. nextObject
            // is null at the beginning and after close().
            while (! closed && (nextObject == null || 
                                JDOHelper.isDeleted(nextObject))) {
                getNext();
            }
            return (nextObject != null);
        }

        /**
         * @see java.util.Iterator#next
         */
        public Object next() {
            if (nextObject == null && !hasNext()) {
                throw new NoSuchElementException();
            } 

            Object rc = nextObject;
            nextObject = null;
            return rc;
       }

       /** Get the next instance. Close the iterator if there are 
        * no more instances available.
        */
       private void getNext() {
            if (index < size) {
                if (instanceIndex >= instances.size()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "FOStoreExtentIterator.next get new instances"); // NOI18N
                    }
                    FOStoreStoreManager srm =
                        (FOStoreStoreManager)pm.getStoreManager();
                    instances = srm.getInstances(
                        oids, oidsIndex, maxInstances, pm, cls);
                    oidsIndex += instances.size();
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "FOStoreExtent.next got " + // NOI18N
                            instances.size() + " instances"); // NOI18N
                    }
                    instanceIndex = 0;
                }
                nextObject = instances.get(instanceIndex);
                instanceIndex++;
                index++;

            } else {
                close();
            }
        }

        /**
         * Always throws UnsupportedOperationException.
         */
        public void remove() {
            throw new UnsupportedOperationException(
                msg.msg("EXC_RemoveNotSupported")); // NOI18N
        }

        /**
         * Disallow getting further objects from this iterator.
         */
        void close() {
            closed = true;
            instances = null;
            index = size;
            nextObject = null;
        }
    }
}
