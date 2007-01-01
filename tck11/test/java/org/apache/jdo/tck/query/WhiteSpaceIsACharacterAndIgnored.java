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
 
package org.apache.jdo.tck.query;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> White Space is a Character and is Ignored
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-6.
 *<BR>
 *<B>Assertion Description: </B> White space (non-printing characters space,
 *tab, carriage return, and line feed) is a separator and is otherwise ignored
 *in a <code>Query</code> filter.
 */

public class WhiteSpaceIsACharacterAndIgnored extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-6 (WhiteSpaceIsACharacterAndIgnored) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(WhiteSpaceIsACharacterAndIgnored.class);
    }

    Collection expected = null; 
    
    /** */
    public void test() {
        pm = getPM();

        initDatabase(pm, PCPoint.class);
        initExpectedResult(pm, "x == 0");

        // Escape Sequence
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\t == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\n == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\f == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\r == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\n == \t 0 \r");
        // Unicode
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\u0009 == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\u000c == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\u0020 == 0");
        // Octal Escape
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\11 == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\12 == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\14 == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\15 == 0");
        runTestWhiteSpaceIsACharacterAndIgnored01(pm, "x\40 == 0");
            
        pm.close();
        pm = null;
    }

    /** */
    void initExpectedResult(PersistenceManager pm, String filter) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter(filter);
            expected = (Collection) query.execute();

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
        
    /** */
    void runTestWhiteSpaceIsACharacterAndIgnored01(PersistenceManager pm,
                                                   String filter) {
        Transaction tx = pm.currentTransaction();
        Collection results = null;
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter(filter);
            results = (Collection) query.execute();
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            if (debug)
                logger.debug("Test WhiteSpaceIsACharacterAndIgnored01(\"" +
                             filter + "\"): Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
