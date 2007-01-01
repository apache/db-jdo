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
 * Test_LifeCycle.java
 *
 * Created on July 19, 2001, 3:42 PM
 */

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;

import org.apache.jdo.pc.empdept.PCPerson;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests LifeCycle state transitions for datastore transactions with
 * retainValues flag set to true.
 *
 * @author  clr
 * @version
 */
public class Test_LifeCycle extends AbstractTest {

    final static int BEGIN_TRANSACTION = 0;
    final static int COMMIT_TRANSACTION = 1;
    final static int ROLLBACK_TRANSACTION = 2;
    final static int MAKE_PERSISTENT = 10;
    final static int DELETE_PERSISTENT = 11;
    final static int MAKE_TRANSIENT = 12;
    final static int MAKE_TRANSACTIONAL = 13;
    final static int MAKE_NONTRANSACTIONAL = 14;
    final static int EVICT = 15;
    final static int REFRESH = 16;
    final static int READ_FIELD = 20;
    final static int WRITE_FIELD = 21;
    final static int GET_OBJECT_ID = 22;
    final static int GET_OBJECT_BY_ID_WITH_VALIDATE = 23;
    final static int GET_OBJECT_BY_ID_WOUT_VALIDATE = 24;
    final static int MAKE_DIRTY = 25;
    final static int CATCH_JDO_EXCEPTION = 30;
    final static int CATCH_JDO_USER_EXCEPTION = 31;
    final static int CATCH_JDO_DATASTORE_EXCEPTION = 32;
    final static int CATCH_JDO_FATAL_USER_EXCEPTION = 33;
    final static int CATCH_JDO_FATAL_INTERNAL_EXCEPTION = 34;
    final static int ASSERT_TRANSIENT = 100;
    final static int ASSERT_TRANSIENT_CLEAN = 101;
    final static int ASSERT_TRANSIENT_DIRTY = 102;
    final static int ASSERT_PERSISTENT_NEW = 103;
    final static int ASSERT_PERSISTENT_CLEAN = 104;
    final static int ASSERT_PERSISTENT_NONTRANSACTIONAL = 105;
    final static int ASSERT_PERSISTENT_DIRTY = 106;
    final static int ASSERT_PERSISTENT_NEW_DELETED = 107;
    final static int ASSERT_PERSISTENT_DELETED = 108;
    final static int ASSERT_HOLLOW = 109;
    final static int CREATE_TRANSIENT = 200;

    ArrayList cases = new ArrayList();

    static PCPerson instance = null;
    static Object oid = null;
    static Date date = new Date();

    static String testCaseName;
    static PersistenceManager persistenceManager;
    static Throwable thrown;

    static boolean verbose = Boolean.getBoolean("verbose");

    /** Creates new Test_LifeCycle */
    public Test_LifeCycle() {
        int i = 0;
        cases.add (new Case (i++, "Transient: makeTransient, read, write", new int[] { // 0
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,GET_OBJECT_ID, ASSERT_TRANSIENT //
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-5
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-13
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-16
            ,MAKE_DIRTY, ASSERT_TRANSIENT // A5.9-16
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-2
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-4
            ,EVICT, ASSERT_TRANSIENT // A5.9-12
            ,REFRESH, ASSERT_TRANSIENT // A5.9-10
            }));
        cases.add (new Case (i++, "Transient: read, write, commit, rollback", new int[] { // 1
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-15
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-17
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-6
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, read, commit, rollback", new int[] { // 2
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN // A5.9-88
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-100
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_CLEAN // A5.9-90
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-87
            ,EVICT, ASSERT_TRANSIENT_CLEAN // A5.9-97
            ,REFRESH, ASSERT_TRANSIENT_CLEAN // A5.9-95
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-92
            ,EVICT, ASSERT_TRANSIENT_CLEAN // A5.9-97
            ,REFRESH, ASSERT_TRANSIENT_CLEAN // A5.9-95
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-98
            ,WRITE_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-101
            ,MAKE_DIRTY, ASSERT_TRANSIENT_CLEAN // A5.9-101
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-94
            }));
        cases.add (new Case (i++, "TransientClean: read, write, makeTransactional, makeTransient, commit, rollback", new int[] { // 3
            ASSERT_TRANSIENT_CLEAN
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,READ_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-117
            ,MAKE_DIRTY, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,EVICT, ASSERT_TRANSIENT_DIRTY // A5.9-114
            ,REFRESH, ASSERT_TRANSIENT_DIRTY // A5.9-112
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_DIRTY // A5.9-105
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-106
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_DIRTY // A5.9-107
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-104
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-109
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-119
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-111
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,MAKE_DIRTY, ASSERT_TRANSIENT_DIRTY // A5.9-119
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-111
            }));
        cases.add (new Case (i++, "TransientClean: makeTransactional, makeNonTransactional, commit, rollback", new int[] { // 4
            ASSERT_TRANSIENT_CLEAN
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN // A5.9-88
            ,MAKE_NONTRANSACTIONAL, ASSERT_TRANSIENT // A5.9-89
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-6
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_NONTRANSACTIONAL, ASSERT_TRANSIENT // A5.9-89
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, makePersistent, rollback", new int[] { // 5
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-86
            ,EVICT, ASSERT_PERSISTENT_NEW // A5.9-29
            ,REFRESH, ASSERT_PERSISTENT_NEW // A5.9-27
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, write, makePersistent, rollback", new int[] { // 6
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-103
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));

        cases.add (new Case (i++, "Transient: makePersistent, rollback", new int[] { // 7
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION , ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-1
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-18
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NEW // A5.9-18
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_NEW // A5.9-20
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-21
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-22
            ,READ_FIELD, ASSERT_PERSISTENT_NEW // A5.9-32
            ,WRITE_FIELD, ASSERT_PERSISTENT_NEW // A5.9-34
            ,MAKE_DIRTY, ASSERT_PERSISTENT_NEW // A5.9-34
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT  // A5.9-25
            }));
        cases.add (new Case (i++, "Transient: makePersistent, delete, rollback", new int[] { // 8
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-19
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-120
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-121
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_NEW_DELETED // A5.9-122
            ,EVICT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-131
            ,REFRESH, ASSERT_PERSISTENT_NEW_DELETED // A5.9-129
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-124
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-123
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-134
            ,WRITE_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-136
            ,MAKE_DIRTY, CATCH_JDO_USER_EXCEPTION // A5.9-136
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-127
            }));
        cases.add (new Case (i++, "Transient: makePersistent, delete, commit", new int[] { // 9
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-19
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-125
            }));
        cases.add (new Case (i++, "Transient: makePersistent, commit", new int[] { // 10
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            }));
        cases.add (new Case (i++, "Nontransactional: makeNontransactional, read, write", new int[] { // 11
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_NONTRANSACTIONAL, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-157
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-166
            ,WRITE_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-169
            ,MAKE_DIRTY, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-169
            }));
        cases.add (new Case (i++, "Nontransactional: makePersistent, makeTransactional", new int[] { // 12
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A12.5.7-1
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-154
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-156
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            }));
        cases.add (new Case (i++, "PersistentDirty: makePersistent, makeTransactional, commit", new int[] { // 13
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-170
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-170
            ,EVICT, ASSERT_PERSISTENT_DIRTY // A5.9-63
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,READ_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-66
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_DIRTY // A5.9-54
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-55
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-56
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_DIRTY // A5.9-52
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-57
            }));
        cases.add (new Case (i++, "PersistentDirty: rollback", new int[] { // 14
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,ROLLBACK_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-59
            }));
        cases.add (new Case (i++, "PersistentDirty: commit", new int[] { // 15
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-57
            }));
        cases.add (new Case (i++, "PersistentDirty: refresh", new int[] { // 16
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,REFRESH, ASSERT_PERSISTENT_CLEAN // A5.9-61
            ,ROLLBACK_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-59
            }));
        cases.add (new Case (i++, "PersistentClean: makePersistent, makeTransactional", new int[] { // 17
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN // A5.9-168
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN // A5.9-49
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_CLEAN // A5.9-35
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-37
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-40
            }));
        cases.add (new Case (i++, "PersistentClean: makeNontransactional", new int[] { // 18
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,MAKE_NONTRANSACTIONAL, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-38
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            }));
        cases.add (new Case (i++, "PersistentDeleted: rollback", new int[] { // 19
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-36
            ,EVICT, ASSERT_PERSISTENT_DELETED // A5.9-148
            ,REFRESH, ASSERT_PERSISTENT_DELETED // A5.9-146
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-134
            ,ROLLBACK_TRANSACTION ,ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-145
            }));
        cases.add (new Case (i++, "PersistentDeleted: makePersistent, makeTransactional, commit", new int[] { // 20
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-155
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-137
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-138
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_DELETED // A5.9-139
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-140
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-141
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-151
            ,WRITE_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-153
            ,MAKE_DIRTY, CATCH_JDO_USER_EXCEPTION // A5.9-153
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-142
            }));
        cases.add (new Case (i++, "Transient: getObjectById(false), getObjectById(true), makePersistent, commit", new int[] { // 21
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,GET_OBJECT_BY_ID_WOUT_VALIDATE, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, CATCH_JDO_DATASTORE_EXCEPTION
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, CATCH_JDO_DATASTORE_EXCEPTION
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            }));
        cases.add (new Case (i++, "Nontransactional: getObjectById(false), getObjectById(true)", new int[] { // 22
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,GET_OBJECT_BY_ID_WOUT_VALIDATE, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, ASSERT_PERSISTENT_CLEAN
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            }));
        cases.add (new Case (i++, "Nontransactional: makeTransient, commit, rollback", new int[] { // 23
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            }));
        cases.add (new Case (i++, "Nontransactional: evict", new int[] { // 24
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            ,REFRESH, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-163
            ,EVICT, ASSERT_HOLLOW // A5.9-165
            ,EVICT, ASSERT_HOLLOW // A5.9-80
            ,REFRESH, ASSERT_HOLLOW // A5.9-78
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,EVICT, ASSERT_HOLLOW // A5.9-80
            ,REFRESH, ASSERT_HOLLOW // A5.9-78
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "PersistentClean: evict", new int[] { // 25
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN
            ,REFRESH, ASSERT_PERSISTENT_CLEAN // A5.9-44
            ,EVICT, ASSERT_HOLLOW // A5.9-46
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "Hollow: makeDirty", new int[] { // 26
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-85
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-23
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,EVICT, ASSERT_HOLLOW // A5.9-80
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            ,MAKE_DIRTY, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-84
            }));
    }
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_LifeCycle.class);
    }

    /** */
    public void test() {
        persistenceManager = pmf.getPersistenceManager();
        for (Iterator i = cases.iterator(); i.hasNext();) {
            Case testcase = (Case) i.next();
            testcase.execute();
        }
    }

    /**
     * Initializes JDO startup properties with default values.
     * Sub classes can overwrite this method in order to change those default
     * values.
     */
    protected void initProperties()
    {   super.initProperties();
    //properties.setProperty( "javax.jdo.option.ConnectionUserName", "" );
    //properties.setProperty( "javax.jdo.option.ConnectionPassword", "" );
    //properties.setProperty( "javax.jdo.option.ConnectionURL", "fostore:test" );
        properties.setProperty( "javax.jdo.option.NontransactionalWrite", "true" );
        properties.setProperty( "javax.jdo.option.Optimistic", "false" );
    }

    /** */
    static void resetThrown() {
        thrown = null;
    }

    /** */
    class Case {
        int number;
        String name;
        int[] procedures;
        Case (int number, String name, int[] procedures) {
            this.number = number;
            this.name = name;
            this.procedures = procedures;
        }
        int execute () {
            printFlush ("Test case " + number + ": " + name);
            testCaseName = name;
            for (int i = 0; i < procedures.length; ++i) {
                try {
                    switch (procedures[i]) {
                        case BEGIN_TRANSACTION:
                            resetThrown();
                            Procedure.beginTransaction();
                            break;
                        case COMMIT_TRANSACTION:
                            resetThrown();
                            Procedure.commitTransaction();
                            break;
                        case ROLLBACK_TRANSACTION:
                            resetThrown();
                            Procedure.rollbackTransaction();
                            break;
                       case CREATE_TRANSIENT:
                            resetThrown();
                            Procedure.createTransient();
                            break;
                        case MAKE_PERSISTENT:
                            resetThrown();
                            Procedure.makePersistent();
                            break;
                        case DELETE_PERSISTENT:
                            resetThrown();
                            Procedure.deletePersistent();
                            break;
                        case MAKE_TRANSACTIONAL:
                            resetThrown();
                            Procedure.makeTransactional();
                            break;
                        case MAKE_TRANSIENT:
                            resetThrown();
                            Procedure.makeTransient();
                            break;
                        case MAKE_NONTRANSACTIONAL:
                            resetThrown();
                            Procedure.makeNontransactional();
                            break;
                        case EVICT:
                            resetThrown();
                            Procedure.evict();
                            break;
                        case REFRESH:
                            resetThrown();
                            Procedure.refresh();
                            break;
                        case ASSERT_PERSISTENT_NEW:
                            Procedure.assertPersistentNew();
                            break;
                        case ASSERT_PERSISTENT_NEW_DELETED:
                            Procedure.assertPersistentNewDeleted();
                            break;
                        case ASSERT_TRANSIENT:
                            Procedure.assertTransient();
                            break;
                        case ASSERT_TRANSIENT_CLEAN:
                            Procedure.assertTransientClean();
                            break;
                        case ASSERT_TRANSIENT_DIRTY:
                            Procedure.assertTransientDirty();
                            break;
                        case ASSERT_PERSISTENT_CLEAN:
                            Procedure.assertPersistentClean();
                            break;
                        case ASSERT_PERSISTENT_DIRTY:
                            Procedure.assertPersistentDirty();
                            break;
                        case ASSERT_PERSISTENT_NONTRANSACTIONAL:
                            Procedure.assertPersistentNontransactional();
                            break;
                        case ASSERT_HOLLOW:
                            Procedure.assertHollow();
                            break;
                        case ASSERT_PERSISTENT_DELETED:
                            Procedure.assertPersistentDeleted();
                            break;
                        case READ_FIELD:
                            resetThrown();
                            Procedure.readField();
                            break;
                        case WRITE_FIELD:
                            resetThrown();
                            Procedure.writeField();
                            break;
                        case GET_OBJECT_ID:
                            resetThrown();
                            Procedure.getObjectId();
                            break;
                        case GET_OBJECT_BY_ID_WITH_VALIDATE:
                            resetThrown();
                            Procedure.getObjectById(true);
                            break;
                        case GET_OBJECT_BY_ID_WOUT_VALIDATE:
                            resetThrown();
                            Procedure.getObjectById(false);
                            break;
                        case MAKE_DIRTY:
                            resetThrown();
                            Procedure.makeDirty();
                            break;
                        case CATCH_JDO_EXCEPTION:
                            Procedure.catchJDOException();
                            resetThrown();
                            break;
                        case CATCH_JDO_USER_EXCEPTION:
                            Procedure.catchJDOUserException();
                            resetThrown();
                            break;
                        case CATCH_JDO_FATAL_USER_EXCEPTION:
                            Procedure.catchJDOFatalUserException();
                            resetThrown();
                            break;
                        case CATCH_JDO_DATASTORE_EXCEPTION:
                            Procedure.catchJDODataStoreException();
                            resetThrown();
                            break;
                        default:
                            throw new JDOException ("Unknown Procedure " + procedures[i]);
                    }
                } catch (JDOException t) {
                    thrown = t;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw new JDOException ("Test failed", e);
                }
            }
            return 0;
        }
    }

    /** */
    static class Procedure {
        static void createTransient() {
            printFlush ("createTransient");
            instance = new PCPerson("First", "Last", date);
        }
        static void makePersistent() {
            printFlush ("makePersistent");
            persistenceManager.makePersistent(instance);
        }
        static void deletePersistent() {
            printFlush ("deletePersistent");
            persistenceManager.deletePersistent(instance);
        }
        static void makeTransient() {
            printFlush ("makeTransient");
            persistenceManager.makeTransient(instance);
        }
        static void makeTransactional() {
            printFlush ("makeTransactional");
            persistenceManager.makeTransactional(instance);
        }
        static void makeNontransactional() {
            printFlush ("makeNontransactional");
            persistenceManager.makeNontransactional(instance);
        }
        static void refresh() {
            printFlush ("refresh");
            persistenceManager.refresh(instance);
        }
        static void evict() {
            printFlush ("evict");
            persistenceManager.evict(instance);
        }
        static void getObjectId() {
            printFlush ("getObjectId");
            oid = persistenceManager.getObjectId(instance);
        }
        static void getObjectById(boolean validate) {
            printFlush ("getObjectById_" + validate);
            Object o = persistenceManager.getObjectById(oid, validate);
            instance = (PCPerson)o;
        }
        static void assertPersistentNew() {
            printFlush ("assertPersistentNew");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentNew(instance))
                throw new RuntimeException ("Assertion PersistentNew failed" +
                Assert.printLifeCycleState());
        }
        static void assertPersistentNewDeleted() {
            printFlush ("assertPersistentNewDeleted");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentNewDeleted(instance))
                throw new RuntimeException ("Assertion NewDeleted failed" +
                Assert.printLifeCycleState());
        }
        static void assertTransient() {
            printFlush ("assertTransient");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isTransient(instance))
                throw new RuntimeException ("Assertion Transient failed" +
                Assert.printLifeCycleState());
        }
        static void assertTransientClean() {
            printFlush ("assertTransientClean");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isTransientClean(instance))
                throw new RuntimeException ("Assertion TransientClean failed" +
                Assert.printLifeCycleState());
        }
        static void assertTransientDirty() {
            printFlush ("assertTransientDirty");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isTransientDirty(instance))
                throw new RuntimeException ("Assertion TransientDirty failed" +
                Assert.printLifeCycleState());
        }
        static void assertPersistentClean() {
            printFlush ("assertPersistentClean");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentClean(instance))
                throw new RuntimeException ("Assertion PersistentClean failed" +
                Assert.printLifeCycleState());
        }
        static void assertPersistentDirty() {
            printFlush ("assertPersistentDirty");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentDirty(instance))
                throw new RuntimeException ("Assertion PersistentDirty failed" +
                Assert.printLifeCycleState());
        }
        static void assertPersistentDeleted() {
            printFlush ("assertPersistentDeleted");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentDeleted(instance))
                throw new RuntimeException ("Assertion PersistentDeleted failed" +
                Assert.printLifeCycleState());
        }
        static void assertPersistentNontransactional() {
            printFlush ("assertPersistentNontransactional");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isPersistentNontransactional(instance))
                throw new RuntimeException ("Assertion PersistentNontransactional failed: " +
                Assert.printLifeCycleState());
        }
        static void assertHollow() {
            printFlush ("assertHollow");
            if (thrown != null)
                throw new RuntimeException ("Caught exception: " + thrown);
            if (!Assert.isHollow(instance))
                throw new RuntimeException ("Assertion Hollow failed: " +
                Assert.printLifeCycleState());
        }
        static void beginTransaction() {
            printFlush ("beginTransaction");
            persistenceManager.currentTransaction().begin();
        }
        static void commitTransaction() {
            printFlush ("commitTransaction");
            persistenceManager.currentTransaction().commit();
        }
        static void rollbackTransaction() {
            printFlush ("rollbackTransaction");
            persistenceManager.currentTransaction().rollback();
        }
        static void readField() {
            printFlush ("readField");
            instance.getBirthdate();
        }
        static void writeField() {
            printFlush ("writeField");
            instance.setBirthdate(new Date());
        }
        static void makeDirty() {
            printFlush ("makeDirty");
            JDOHelper.makeDirty(instance, "lastname");
        }
        static void catchJDOException() {
            printFlush ("catchJDOException");
            if (thrown == null || !JDOException.class.isAssignableFrom(thrown.getClass()))
                throw new RuntimeException ("Expected JDOException, got " + thrown);
        }
        static void catchJDOUserException() {
            printFlush ("catchJDOUserException");
            if (thrown == null || !JDOUserException.class.isAssignableFrom(thrown.getClass()))
                throw new RuntimeException ("Expected JDOUserException, got " + thrown);
        }
        static void catchJDOFatalUserException() {
            printFlush ("catchJDOFatalUserException");
            if (thrown == null || !JDOFatalUserException.class.isAssignableFrom(thrown.getClass()))
                throw new RuntimeException ("Expected JDOFatalUserException, got " + thrown);
        }
        static void catchJDODataStoreException() {
            printFlush ("catchJDODataStoreException");
            if (thrown == null || !JDODataStoreException.class.isAssignableFrom(thrown.getClass()))
                throw new RuntimeException ("Expected JDODataStoreException, got " + thrown);
        }
    }

    /** */
    static void flush() {
        try {
            System.out.flush();
        } catch (Exception e) {}
    }

    /** */
    static void printFlush (String s) {
        if (verbose) {
            System.out.println (s);
            flush();
        }
    }

    /** */
    static class Assert {
        static String printLifeCycleState() {
            return
                testCaseName + ": isPersistent: " + JDOHelper.isPersistent(instance) +
                ", isNew: " + JDOHelper.isNew(instance) +
                ", isDirty: " + JDOHelper.isDirty(instance) +
                ", isDeleted: " + JDOHelper.isDeleted(instance) +
                ", isTransactional: " + JDOHelper.isTransactional(instance);
        }
        static boolean isTransient(Object instance) {
            return (
              (!JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (!JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (!JDOHelper.isTransactional(instance))
            );
        }
        static boolean isTransientClean(Object instance) {
            return (
              (!JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (!JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isTransientDirty(Object instance) {
            return (
              (!JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentNew(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (JDOHelper.isNew(instance))
            & (JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentNewDeleted(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (JDOHelper.isNew(instance))
            & (JDOHelper.isDirty(instance))
            & (JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentDeleted(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (JDOHelper.isDirty(instance))
            & (JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentClean(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (!JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentDirty(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (JDOHelper.isTransactional(instance))
            );
        }
        static boolean isPersistentNontransactional(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (!JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (!JDOHelper.isTransactional(instance))
            );
        }
	// The same as PersistentNontransactional from a user's perspective:
        static boolean isHollow(Object instance) {
            return (
              (JDOHelper.isPersistent(instance))
            & (!JDOHelper.isNew(instance))
            & (!JDOHelper.isDirty(instance))
            & (!JDOHelper.isDeleted(instance))
            & (!JDOHelper.isTransactional(instance))
            );
        }
    }

}
