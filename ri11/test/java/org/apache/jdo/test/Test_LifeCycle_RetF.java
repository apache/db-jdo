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
 * Test_LifeCycle_RetF.java
 *
 * Created on August 17, 2001, 3:42 PM
 */

/*
 * Test_LifeCycle_RetF.java
 *
 * Created on Aug 17, 2001
 */

package org.apache.jdo.test;

import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests LifeCycle state transitions for datastore transactions with
 * retainValues flag set to false.
 *
 * @author  Marina Vatkina
 * @version
 */
public class Test_LifeCycle_RetF extends Test_LifeCycle{

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_LifeCycle_RetF.class);
    }

    /** Creates new Test_LifeCycle_RetF */
    public Test_LifeCycle_RetF() {
        int i = 0;
	cases.clear();
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
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-99
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT_CLEAN // A5.9-90
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-91
            ,READ_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-98
            ,WRITE_FIELD, ASSERT_TRANSIENT_CLEAN // A5.9-101
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-93
            }));
        cases.add (new Case (i++, "TransientClean: read, write, commit, rollback", new int[] {
            ASSERT_TRANSIENT_CLEAN
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-102
            ,READ_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-116
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-108
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT_CLEAN
            ,WRITE_FIELD, ASSERT_TRANSIENT_DIRTY // A5.9-119
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT_CLEAN // A5.9-110
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
            ,READ_FIELD, ASSERT_PERSISTENT_NEW // A5.9-31
            ,WRITE_FIELD, ASSERT_PERSISTENT_NEW // A5.9-34
            ,ROLLBACK_TRANSACTION, ASSERT_TRANSIENT  // A5.9-25
            }));
        cases.add (new Case (i++, "Transient: makePersistent, delete, rollback", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_NEW_DELETED // A5.9-19
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
        cases.add (new Case (i++, "Hollow: makePersistent, makeTransactional, commit", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,MAKE_PERSISTENT, ASSERT_HOLLOW // A5.9-69
            ,MAKE_TRANSACTIONAL, ASSERT_PERSISTENT_CLEAN // A5.9-71
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-57
            }));
        cases.add (new Case (i++, "Hollow: makeNontransactional, read", new int[] {
            ASSERT_HOLLOW
            ,MAKE_NONTRANSACTIONAL, ASSERT_HOLLOW // A5.9-72
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-81
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-57
            }));
        cases.add (new Case (i++, "Nontransactional: commit, rollback", new int[] {
            ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-81
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,COMMIT_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-159
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,ROLLBACK_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-161
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-57
            }));
        cases.add (new Case (i++, "Hollow: commit, rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-74
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,ROLLBACK_TRANSACTION, ASSERT_HOLLOW // A5.9-76
            }));
        cases.add (new Case (i++, "Hollow: write, commit", new int[] {
            ASSERT_HOLLOW
            ,WRITE_FIELD, ASSERT_PERSISTENT_NONTRANSACTIONAL // A5.9-84
            ,BEGIN_TRANSACTION, ASSERT_PERSISTENT_NONTRANSACTIONAL
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-68
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-57
            }));
        cases.add (new Case (i++, "Hollow: write, rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,READ_FIELD, ASSERT_PERSISTENT_CLEAN // A5.9-83
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-51
            ,ROLLBACK_TRANSACTION, ASSERT_HOLLOW // A5.9-59
            }));
        cases.add (new Case (i++, "Hollow: deletePersistent, rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-70
            ,ROLLBACK_TRANSACTION, ASSERT_HOLLOW // A5.9-145
            }));
        cases.add (new Case (i++, "Hollow: deletePersistent, commit", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,WRITE_FIELD, ASSERT_PERSISTENT_DIRTY // A5.9-85
            ,DELETE_PERSISTENT, ASSERT_PERSISTENT_DELETED // A5.9-70
            ,COMMIT_TRANSACTION, ASSERT_TRANSIENT // A5.9-142
            }));
        cases.add (new Case (i++, "Transient: getObjectById(false), getObjectById(true), makePersistent, commit", new int[] {
            CREATE_TRANSIENT, ASSERT_TRANSIENT
            ,BEGIN_TRANSACTION, ASSERT_TRANSIENT
            ,MAKE_PERSISTENT, ASSERT_PERSISTENT_NEW
            ,GET_OBJECT_ID, ASSERT_PERSISTENT_NEW
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "Hollow: getObjectById(false), getObjectById(true)", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,GET_OBJECT_BY_ID_WOUT_VALIDATE, ASSERT_HOLLOW
            ,GET_OBJECT_BY_ID_WITH_VALIDATE, ASSERT_PERSISTENT_CLEAN
            ,COMMIT_TRANSACTION, ASSERT_HOLLOW // A5.9-23
            }));
        cases.add (new Case (i++, "Hollow: makeTransient, commit, rollback", new int[] {
            ASSERT_HOLLOW
            ,BEGIN_TRANSACTION, ASSERT_HOLLOW
            ,MAKE_TRANSIENT, ASSERT_TRANSIENT // A5.9-73
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
        properties.setProperty( "javax.jdo.option.Optimistic", "false" );
    }
}
