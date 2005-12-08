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

package org.apache.jdo.tck.query.delete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.listener.DeleteLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.StoreLifecycleListener;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ConversionHelper;

/**
 *<B>Title:</B> Delete Persistent All.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.8-4
 *<BR>
 *<B>Assertion Description: </B>
 * Dirty instances of affected classes are first flushed to the datastore. 
 * Instances already in the cache when deleted via these methods 
 * or brought into the cache as a result of these methods 
 * undergo the life cycle transitions as if deletePersistent 
 * had been called on them. 
 * That is, if an affected class implements the DeleteCallback interface, 
 * the instances to be deleted are instantiated in memory and 
 * the jdoPreDelete method is called prior 
 * to deleting the instance in the datastore. 
 * If any LifecycleListener instances are registered with affected classes, 
 * these listeners are called for each deleted instance. 
 * Before returning control to the application, 
 * instances of affected classes in the cache are refreshed 
 * by the implementation so their status in the cache reflects 
 * whether they were deleted from the datastore.
 */
public class DeleteCallback extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.8-4 (DeleteCallback) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
        /*INTO*/        null, 
        /*FROM*/        PrimitiveTypes.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private List[] expectedResult = {
            getTransientCompanyModelInstancesAsList(new String[]{
                    "emp1", "emp2", "emp3", "emp4", "emp5"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", 
                    "primitiveTypesNegative",
                    "primitiveTypesCharacterStringLiterals"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DeleteCallback.class);
    }
    
    /** */
    public void testRelationshipsAPI() {
        queryUpdateDeleteVerify(0, false, "middlename");
    }
    
    /** */
    public void testRelationshipsSingleString() {
        queryUpdateDeleteVerify(0, true, "middlename");
    }
    
    /** */
    public void testNoRelationshipsAPI() {
        queryUpdateDeleteVerify(1, false, "stringNull");
    }
    
    /** */
    public void testNoRelationshipsSingleString() {
        queryUpdateDeleteVerify(1, true, "stringNull");
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        addTearDownClass(MylibReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        loadAndPersistMylib(getPM());
    }
    
    /**
     * Adds a lifecycle listener to the persistence manager.
     * Converts the query element holder instance refered to by argument
     * <code>index</code> to a JDO query instance based on argument
     * <code>asSingleString</code>.
     * Executes the query instance and marks all queried pc instances as dirty 
     * by calling {@link JDOHelper#makeDirty(java.lang.Object, java.lang.String)}.
     * Passes argument <code>fieldName</code> to that call. 
     * Afterwards, calls {@link Query#deletePersistentAll()}, and
     * verifies the lifecycle callbacks and the lifecycle states.
     * @param index the index of the query element holder instance
     * @param fieldName the field name passed as argument to
     * {@link JDOHelper#makeDirty(java.lang.Object, java.lang.String)
     * @param asSingleString determines if the query is executed as
     * single string query or as API query.
     */
    private void queryUpdateDeleteVerify(int index, 
            boolean asSingleString, String fieldName) {
        PersistenceManager pm = getPM();
        Transaction transaction = pm.currentTransaction();
        transaction.begin();
        try
        {
            LifecycleVerifier lifecycleVerifier;
            Query query = asSingleString ? 
                    VALID_QUERIES[index].getSingleStringQuery(pm) :
                        VALID_QUERIES[index].getAPIQuery(pm);
                    
            Collection result = executeQuery(query, index, asSingleString);
            try {
                lifecycleVerifier = new LifecycleVerifier(result);
                pm.addInstanceLifecycleListener(lifecycleVerifier, 
                        new Class[]{VALID_QUERIES[index].getCandidateClass()});
                updateInstances(result, fieldName);
                deleteInstances(query, index, asSingleString, result.size());
            } finally
            {
                query.close(result);
            }
            
            lifecycleVerifier.verifyCallbacksAndStates();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
    
    /**
     * Executes the given query, checks and returns the query result.
     * Note: This mthod does not close the query result.
     * @param query the query.
     * @param index the index of the query element holder instance
     * which was used to created the given query.
     * @param asSingleString indicates if the given query was created 
     * using API methods or if it was created by a single string.
     * @return the query result.
     */
    private Collection executeQuery(Query query, 
            int index, boolean asSingleString) {
        if (logger.isDebugEnabled()) {
            if (asSingleString) {
                logger.debug("Executing single string query: " + 
                        VALID_QUERIES[index]);
            } else {
                logger.debug("Executing API query: " + 
                        VALID_QUERIES[index]);
            }
        }
        
        Collection result = (Collection) query.execute();
        
        if (logger.isDebugEnabled()) {
            logger.debug("Query result: " + ConversionHelper.
                convertObjectArrayElements(result));
        }
        
        checkQueryResultWithoutOrder(ASSERTION_FAILED, result, expectedResult[index]);
        return result;
    }
    
    /**
     * Makes all instances in the given collection dirty.
     * If instances are employees, then all relationships are cleared. 
     * @param instances the instances
     * @param fieldName the field name passed as argument to
     * {@link JDOHelper#makeDirty(java.lang.Object, java.lang.String)
     */
    private void updateInstances(Collection instances, String fieldName) {
        for (Iterator i = instances.iterator(); i.hasNext(); ) {
            Object pc = i.next();
            
            // clear employee relationships
            if (pc instanceof Employee) {
                Employee employee = (Employee) pc;
                if (employee.getDentalInsurance() != null) {
                    employee.getDentalInsurance().setEmployee(null);
                }
                if (employee.getMedicalInsurance() != null) {
                    employee.getMedicalInsurance().setEmployee(null);
                }
                if (employee.getDepartment() != null) {
                    ((Department)employee.getDepartment()).removeEmployee(employee);
                }
                if (employee.getFundingDept() != null) {
                    ((Department)employee.getFundingDept()).removeEmployee(employee);
                }
                if (employee.getManager() != null) {
                    ((Employee)employee.getManager()).removeFromTeam(employee);
                }
                if (employee.getMentor() != null) {
                    employee.getMentor().setProtege(null);
                }
                if (employee.getProtege() != null) {
                    employee.getProtege().setMentor(null);
                }
                if (employee.getHradvisor() != null) {
                    ((Employee)employee.getHradvisor()).removeAdvisee(employee);
                }
                if (employee.getReviewedProjects() != null) {
                    for (Iterator it=employee.getReviewedProjects().iterator(); 
                            it.hasNext(); ) {
                        Project other = (Project) it.next();
                        other.removeReviewer(employee);
                    }
                }
                if (employee.getProjects() != null) {
                    for (Iterator it=employee.getProjects().iterator(); 
                            it.hasNext(); ) {
                        Project other = (Project) it.next();
                        other.removeMember(employee);
                    }
                }
                if (employee.getTeam() != null) {
                    for (Iterator it=employee.getTeam().iterator(); it.hasNext(); ) {
                        Employee other = (Employee) it.next();
                        other.setManager(null);
                    }
                }
                if (employee.getHradvisees() != null) {
                    for (Iterator it=employee.getHradvisees().iterator(); it.hasNext(); ) {
                        Employee other = (Employee) it.next();
                        other.setHradvisor(employee);
                    }
                }
            }
            
            // make the instance dirty.
            if (logger.isDebugEnabled()) {
                logger.debug("Calling JDOHelper.makeDirty(" + 
                        pc + ", \"" + fieldName + "\")");
            }
            JDOHelper.makeDirty(pc, fieldName);
        }
    }

    /**
     * Calls {@link Query#deletePersistentAll()} on the given query.
     * @param query the query.
     * @param index the index of the query element holder instance
     * which was used to created the given query.
     * @param asSingleString indicates if the given query was created 
     * using API methods or if it was created by a single string.
     * @param expectedNumberOfDeletedInstances the expected number 
     * of deleted instances.
     */
    private void deleteInstances(Query query, int index, 
            boolean asSingleString, int expectedNumberOfDeletedInstances) {
        if (logger.isDebugEnabled()) {
            if (asSingleString) {
                logger.debug("Deleting persistent by single string query: " + 
                        VALID_QUERIES[index]);
            } else {
                logger.debug("Deleting persistent by API query: " + 
                        VALID_QUERIES[index]);
            }
        }

        long nr = query.deletePersistentAll();
        
        if (logger.isDebugEnabled()) {
            logger.debug(nr + " objects deleted.");
        }
        
        if (nr != expectedNumberOfDeletedInstances) {
            fail(ASSERTION_FAILED, "deletePersistentAll returned " + nr +
                    ", expected is " + expectedNumberOfDeletedInstances + 
                    ". Query: " + VALID_QUERIES[index]);
        }
    }
    
    /**
     * A lifecycle listener which may be added to persistence managers.
     * Gathers delete events and store events and keeps those
     * in a list. 
     * Method {@link LifecycleVerifier#verifyCallbacksAndStates()}
     * may be called to check if the right events have been called
     * on all expected instances.
     * The expected instances are passed through
     * {@link LifecycleVerifier#LifecycleVerifier(Collection).
     */
    private class LifecycleVerifier
        implements DeleteLifecycleListener, StoreLifecycleListener {
        
        /** The oids of expected pc instances. */
        private Collection expectedOids = new HashSet();
        
        /** The list of events. */
        private List events = new ArrayList();
        
        /**
         * Argument <code>expectedPCInstances</code> holds pc instances
         * which are expected to be sources of events.
         * @param expectedPCInstances the pc instances
         * which are expected to be sources of events.
         */
        public LifecycleVerifier(Collection expectedPCInstances) {
            for (Iterator i = expectedPCInstances.iterator(); i.hasNext(); ) {
                this.expectedOids.add(JDOHelper.getObjectId(i.next()));
            }
        }
        
        /**
         * Verifies if the right events have been called for all
         * expected pc instances.
         * All store events must have been fired before the
         * first delete event has been fired.
         * Furthermore, checks if pc instances kept in 
         * delete events have state persistent-deleted.
         * The test case fails if one of these conditions
         * is violated. 
         */
        public void verifyCallbacksAndStates() {
            if (logger.isDebugEnabled()) {
                logger.debug("Verifying callbacks and states.");
            }
            // The two collections are filled iterating through the list of 
            // events. Finally, they are compared against field expectedOids.
            // Note: Set implementations are used instead of list 
            // implementations to eliminate duplicates. Duplicates may occur
            // if multiple updates or deletions are executed for the same
            // pc instances.
            Collection oidsOfDeletedInstances = new HashSet();
            Collection oidsOfUpdateInstances = new HashSet();
            
            boolean hasDeleteEventBeenPassed = false;
            int size = events.size();
            for (int i = 0; i < size; i++) {
                InstanceLifecycleEvent event = 
                    (InstanceLifecycleEvent) this.events.get(i);
                Object source = event.getSource();
                int eventType = event.getEventType();
                if (eventType == InstanceLifecycleEvent.DELETE) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Verifying delete event on " + 
                                JDOHelper.getObjectId(source));
                    }
                    hasDeleteEventBeenPassed = true;
                    if (!JDOHelper.isDeleted(source)) {
                        fail(ASSERTION_FAILED, 
                                "PC instance must have persistent deleted " +
                                "state: " + source);
                    }
                    oidsOfDeletedInstances.add(JDOHelper.getObjectId(source));
                } else if (eventType == InstanceLifecycleEvent.STORE) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Verifying store event on " + 
                                JDOHelper.getObjectId(source));
                    }
                    
                    if (hasDeleteEventBeenPassed) {
                        fail(ASSERTION_FAILED, 
                                "PC instances must not be flushed " +
                                "after delete has been executed.");
                    }
                    oidsOfUpdateInstances.add(JDOHelper.getObjectId(source));
                }
            }
            
            if (!equalsCollection(oidsOfDeletedInstances, 
                    this.expectedOids)) {
                String lf = System.getProperty("line.separator");
                fail(ASSERTION_FAILED, "Got delete events for oids " + 
                        oidsOfDeletedInstances + '.' + lf +
                        "Expected deleted events for oids " + 
                        this.expectedOids + '.');
            } else if (!oidsOfUpdateInstances.containsAll(this.expectedOids)) {
                String lf = System.getProperty("line.separator");
                fail(ASSERTION_FAILED, "Got store events for oids " +  
                        oidsOfUpdateInstances + '.' + lf +
                        "Expected store events for oids " + 
                        this.expectedOids + '.');
            }
        }
        
        /**
         * @see DeleteLifecycleListener#preDelete(javax.jdo.listener.InstanceLifecycleEvent)
         */
        public void preDelete(InstanceLifecycleEvent event) {
            if (logger.isDebugEnabled()) {
                logger.debug("preDelete event: " + 
                        JDOHelper.getObjectId(event.getSource()));
            }
        }

        /**
         * @see DeleteLifecycleListener#postDelete(javax.jdo.listener.InstanceLifecycleEvent)
         */
        public void postDelete(InstanceLifecycleEvent event) {
            this.events.add(event);
            if (logger.isDebugEnabled()) {
                logger.debug("postDelete event: " + 
                        JDOHelper.getObjectId(event.getSource()));
            }
        }

        /**
         * @see StoreLifecycleListener#preStore(javax.jdo.listener.InstanceLifecycleEvent)
         */
        public void preStore(InstanceLifecycleEvent event) {
            if (logger.isDebugEnabled()) {
                logger.debug("preStore event: " + 
                        JDOHelper.getObjectId(event.getSource()));
            }
        }
        
        /**
         * @see StoreLifecycleListener#postStore(javax.jdo.listener.InstanceLifecycleEvent)
         */
        public void postStore(InstanceLifecycleEvent event) {
            this.events.add(event);
            if (logger.isDebugEnabled()) {
                logger.debug("postStore event: " + 
                        JDOHelper.getObjectId(event.getSource()));
            }
        }
    }
}
