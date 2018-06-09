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
package org.apache.jdo.tck.query.jdoql.methods;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.query.LocalTimeSample;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *<B>Title:</B> Supported LocalTime methods.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-60.
 *<BR>
 *<B>Assertion Description: </B>
 * New supported LocalTime methods:
 * <ul>
 * <li>getHour()</li>
 * <li>getMinute()</li>
 * <li>getSecond()</li>
 * </ul>
 */
public class SupportedLocalTimeMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED =
            "Assertion A14.6.2-60 (SupportedLocalTimeMethods) failed: ";

    /** */
    private Object oidOfLocalTime1;

    /** */
    private Object oidOfLocalTime2;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedLocalTimeMethods.class);
    }

    /** */
    public void testHour() {
        final String filter = "localTime.getHour() == 14";
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            List<LocalTimeSample> expectedResult = new ArrayList();
            expectedResult.add((LocalTimeSample)pm.getObjectById(oidOfLocalTime1));

            Query q =  pm.newQuery(LocalTimeSample.class, filter);
            List<LocalTimeSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void testMinute() {
        final String filter = "localTime.getMinute() == 22";
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            List<LocalTimeSample> expectedResult = new ArrayList();
            expectedResult.add((LocalTimeSample)pm.getObjectById(oidOfLocalTime2));

            Query q =  pm.newQuery(LocalTimeSample.class, filter);
            List<LocalTimeSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void testSecond() {
        final String filter = "localTime.getSecond() == 25";
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            List<LocalTimeSample> expectedResult = new ArrayList();
            expectedResult.add((LocalTimeSample)pm.getObjectById(oidOfLocalTime1));

            Query q =  pm.newQuery(LocalTimeSample.class, filter);
            List<LocalTimeSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(LocalTimeSample.class);
        insertLocalTimeSampleData(getPM());
    }

    /** */
    private void insertLocalTimeSampleData(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            LocalTimeSample lds1 = new LocalTimeSample();
            lds1.setId(1);
            LocalTime localTime1 = LocalTime.of(14, 10, 25);
            lds1.setLocalTime(localTime1);
            pm.makePersistent(lds1);

            LocalTimeSample lds2 = new LocalTimeSample();
            lds2.setId(2);
            LocalTime localTime2 = LocalTime.of(9, 22, 12);
            lds2.setLocalTime(localTime2);
            pm.makePersistent(lds2);

            tx.commit();
            oidOfLocalTime1 = pm.getObjectId(lds1);
            oidOfLocalTime2 = pm.getObjectId(lds2);
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
