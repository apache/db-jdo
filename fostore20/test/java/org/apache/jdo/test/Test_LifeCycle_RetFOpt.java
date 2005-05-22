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

/*
 * Test_LifeCycle_RetFOpt.java
 *
 * Created on August 17, 2001, 3:42 PM
 */

package org.apache.jdo.test;

import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests LifeCycle state transitions for optimistic transactions with
 * retainValues flag set to false.
 *
 * @author  Marina Vatkina
 * @version
 */
public class Test_LifeCycle_RetFOpt extends Test_LifeCycle{

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_LifeCycle_RetFOpt.class);
    }

    /** Creates new Test_LifeCycle_RetFOpt */
    public Test_LifeCycle_RetFOpt() {
        int i = 0;
	cases.clear();
        cases.add (new Case (i++, "Transient: makeTransient, read, write", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,GET_OBJECT_ID, ASSERT_TRANSIENT //
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-5
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-13
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-16
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-2
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-4
            }));
        cases.add (new Case (i++, "Transient: read, write, commit, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-15
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-17
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-6
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, read, commit, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN // A5.9-88
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-99
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_CLEAN // A5.9-90
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-87
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-92
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-98
            ,WRITE_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-101
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-94
            }));
        cases.add (new Case (i++, "TransientClean: read, write, makeTransactional, makeTransient, commit, rollback", new int[] {
            ASSERT_TRANSIENT_CLEAN
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,READ_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-116
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_DIRTY // A5.9-105
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-106
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_DIRTY // A5.9-107
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-104
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-109
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-119
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-111
            }));
        cases.add (new Case (i++, "TransientClean: makeTransactional, makeNonTransactional, commit, rollback", new int[] {
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
        cases.add (new Case (i++, "Transient: makeTransactional, makePersistent, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-86
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-26
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, write, makePersistent, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-103
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-26
            }));
        cases.add (new Case (i++, "Transient: makePersistent, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION , ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-1
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-18
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NEW // A5.9-18
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_NEW // A5.9-20
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-21
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-22
            ,READ_FIELD, ASSERT_PERSISTENT_NEW // A5.9-31
            ,WRITE_FIELD, ASSERT_PERSISTENT_NEW // A5.9-34
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT  // A5.9-25
            }));
        cases.add (new Case (i++, "Transient: makePersistent, delete, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-19
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-120
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-121
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_NEW_DELETED // A5.9-122
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-124
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-123
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-133
            ,WRITE_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-136
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-127
            }));
        cases.add (new Case (i++, "Transient: makePersistent, delete, commit", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-19
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-125
            }));
        cases.add (new Case (i++, "Transient: makePersistent, commit", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "PersistentDirty: makePersistent, makeTransactional, commit", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-170
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,READ_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-65
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_DIRTY // A5.9-54
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-55
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-56
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_DIRTY // A5.9-52
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-57
            }));
        cases.add (new Case (i++, "Hollow: makeNontransactional, read", new int[] {
            ASSERT_HOLLOW
            ,MAKE_NONTRANSACTIONAL, ASSERT_HOLLOW // A5.9-157
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-166
            }));
        cases.add (new Case (i++, "Nontransactional: makePersistent, makeTransactional", new int[] {
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A12.5.7-1
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-154
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-156
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN // A5.9-48
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-41
            }));
        cases.add (new Case (i++, "PersistentDirty: rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,ROLLBACK_TRANSACTION, ASSERT_HOLLOW // A5.9-59
            }));
        cases.add (new Case (i++, "PersistentNonTransactional: makePersistent, makeTransactional", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-154
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-37
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-40
            }));
        cases.add (new Case (i++, "PersistentNonTransactional: makeNontransactional", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_NONTRANSACTIONAL, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-38
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-160
            }));
        cases.add (new Case (i++, "PersistentDeleted: rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-36
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-134
            ,ROLLBACK_TRANSACTION ,ASSERT_HOLLOW // A5.9-145
            }));
        cases.add (new Case (i++, "PersistentDeleted: makePersistent, makeTransactional, commit", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,GET_OBJECT_ID, ASSERT_HOLLOW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-155
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-137
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-138
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_DELETED // A5.9-139
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-140
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-141
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-151
            ,WRITE_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-153
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-142
            }));
        cases.add (new Case (i++, "Transient: getObjectById(false), getObjectById(true), makePersistent, commit", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,GET_OBJECT_BY_ID_WOUT_VALIDATE, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, CATCH_JDO_DATASTORE_EXCEPTION
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, CATCH_JDO_DATASTORE_EXCEPTION
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "Nontransactional: getObjectById(false), getObjectById(true)", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,GET_OBJECT_BY_ID_WOUT_VALIDATE, ASSERT_HOLLOW
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "Nontransactional: makeTransient, commit, rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-158
            }));
    }

    /**
     * Initializes JDO startup properties with default values.
     * Sub classes can overwrite this method in order to change those default
     * values.
     */
    protected void initProperties()
    {   super.initProperties();
        properties.setProperty( "javax.jdo.option.RetainValues", "false" );
        properties.setProperty( "javax.jdo.option.RestoreValues", "false" );
        properties.setProperty( "javax.jdo.option.Optimistic", "true" );
    }
}
