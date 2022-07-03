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


package org.apache.jdo.tck.query.jdoql;

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Executing Query when no Transaction, no NontransactionalRead
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-2.
 *<BR>
 *<B>Assertion Description: </B>
If the <code>NontransactionalRead</code> property is <code>false</code>,
and a transaction is not active, then the <code>execute</code> methods throw
<code>JDOUserException</code>.

 */

public class ExecutingQueryWhenNoTransactionNoNontransactionalRead
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-2 (ExecutingQueryWhenNoTransactionNoNontransactionalRead) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecutingQueryWhenNoTransactionNoNontransactionalRead.class);
    }

    /** */
    public void testNegative() {
        PersistenceManager pm = getPM();
        
        pm.currentTransaction().setNontransactionalRead(false);
        runTestNewQuery(pm);
    }

    /** */
    void runTestNewQuery(PersistenceManager pm) {
        Extent<PCPoint> extent = pm.getExtent(PCPoint.class, true);
        Query<PCPoint> query = pm.newQuery();
        query.setClass(PCPoint.class);
        query.setCandidates(extent);
        try {
            query.execute();
            fail(ASSERTION_FAILED,
                 "Querying outside transactions should throw exception with non transactional read set to false");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) logger.debug("expected exception " + ex);
        }
    }
}
