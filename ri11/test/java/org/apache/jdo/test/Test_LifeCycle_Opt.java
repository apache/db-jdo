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
 * Test_LifeCycle_Opt.java
 *
 * Created on August 14, 2001, 3:42 PM
 */

package org.apache.jdo.test;

import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests LifeCycle state transitions for optimistic transactions with
 * retainValues flag set to true.
 *
 * @author  Marina Vatkina
 * @version
 */
public class Test_LifeCycle_Opt extends Test_LifeCycle{

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_LifeCycle_Opt.class);
    }

    /** Creates new Test_LifeCycle_Opt */
    public Test_LifeCycle_Opt() {
        int i = 0;
	cases.clear();
        cases.add (new Case (i++, "Transient: makeTransient, read, write", new int[] { // 0
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,GET_OBJECT_ID, ASSERT_TRANSIENT //
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-5
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-13
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-16
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-2
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-4
            ,EVICT, ASSERT_TRANSIENT // A5.9-12
            ,REFRESH, ASSERT_TRANSIENT // A5.9-11
            }));
        cases.add (new Case (i++, "Transient: read, write, commit, rollback", new int[] { // 1
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,READ_FIELD, ASSERT_TRANSIENT // A5.9-15
            ,WRITE_FIELD, ASSERT_TRANSIENT // A5.9-17
            ,MAKE_DIRTY, ASSERT_TRANSIENT // A5.9-17
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-6
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-8
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, read, commit, rollback", new int[] { // 2
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN // A5.9-88
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-99
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_CLEAN // A5.9-93
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-87
            ,EVICT, ASSERT_TRANSIENT_CLEAN // A5.9-97
            ,REFRESH, ASSERT_TRANSIENT_CLEAN // A5.9-96
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-92
            ,EVICT, ASSERT_TRANSIENT_CLEAN // A5.9-97
            ,REFRESH, ASSERT_TRANSIENT_CLEAN // A5.9-96
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
            ,MAKE_DIRTY, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,READ_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-116
            ,EVICT, ASSERT_TRANSIENT_DIRTY // A5.9-114
            ,REFRESH, ASSERT_TRANSIENT_DIRTY // A5.9-113
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_DIRTY // A5.9-105
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-106
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_DIRTY // A5.9-107
            ,DELETE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A5.9-104
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-109
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
            ,REFRESH, ASSERT_PERSISTENT_NEW // A5.9-28
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-26
            }));
        cases.add (new Case (i++, "Transient: makeTransactional, write, makePersistent, rollback", new int[] { // 6
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_TRANSACTIONAL, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW // A5.9-103
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT // A5.9-26
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
            ,READ_FIELD, ASSERT_PERSISTENT_NEW // A5.9-31
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
            ,REFRESH, ASSERT_PERSISTENT_NEW_DELETED // A5.9-130
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-124
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-123
            ,READ_FIELD, CATCH_JDO_USER_EXCEPTION // A5.9-133
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
        cases.add (new Case (i++, "PersistentDirty: makePersistent, makeTransactional, commit", new int[] { // 11
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-170
            ,EVICT, ASSERT_PERSISTENT_DIRTY // A5.9-63
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,READ_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-65
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_DIRTY // A5.9-54
            ,MAKE_NONTRANSACTIONAL, CATCH_JDO_USER_EXCEPTION // A5.9-55
            ,MAKE_TRANSIENT, CATCH_JDO_USER_EXCEPTION // A5.9-56
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_DIRTY // A5.9-52
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-57
            }));
        cases.add (new Case (i++, "Nontransactional: makeNontransactional, read, write", new int[] { // 12
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_NONTRANSACTIONAL, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-157
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-166
            ,WRITE_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-169
            ,MAKE_DIRTY, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-169
            }));
        cases.add (new Case (i++, "Nontransactional: makePersistent, makeTransactional", new int[] { // 13
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_PERSISTENT, CATCH_JDO_USER_EXCEPTION // A12.5.7-1
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,REFRESH, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-164
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-154
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-156
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN // A5.9-48
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-41
            }));
        cases.add (new Case (i++, "PersistentDirty: rollback", new int[] { // 14
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,ROLLBACK_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-59
            }));
        cases.add (new Case (i++, "PersistentDirty: commit", new int[] { // 15
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_DIRTY, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-41
            }));
        cases.add (new Case (i++, "PersistentDirty: refresh", new int[] { // 16
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,REFRESH, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-62
            ,ROLLBACK_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-59
            }));
        cases.add (new Case (i++, "PersistentNonTransactional: makePersistent, makeTransactional", new int[] { // 17
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-154
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-37
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-40
            }));
        cases.add (new Case (i++, "PersistentNonTransactional: makeNontransactional", new int[] { // 18
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,MAKE_NONTRANSACTIONAL, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-38
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-160
            }));
        cases.add (new Case (i++, "PersistentDeleted: rollback", new int[] { // 19
            ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-167
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-36
            ,EVICT, ASSERT_PERSISTENT_DELETED // A5.9-148
            ,REFRESH, ASSERT_PERSISTENT_DELETED // A5.9-147
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
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, ASSERT_PERSISTENT_NONTRANSACTIONAL
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
            ,REFRESH, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-164
            ,EVICT, ASSERT_HOLLOW // A5.9-165
            ,EVICT, ASSERT_HOLLOW // A5.9-80
            ,REFRESH, ASSERT_HOLLOW // A5.9-79
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,EVICT, ASSERT_HOLLOW // A5.9-80
            ,REFRESH, ASSERT_HOLLOW // A5.9-79
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "PersistentClean: evict", new int[] { // 25
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-37
            ,REFRESH, ASSERT_PERSISTENT_CLEAN // A5.9-45
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

    /**
     * Initializes JDO startup properties with default values.
     * Sub classes can overwrite this method in order to change those default
     * values.
     */
    protected void initProperties()
    {   super.initProperties();
        properties.setProperty( "javax.jdo.option.Optimistic", "true" );
    }
}
