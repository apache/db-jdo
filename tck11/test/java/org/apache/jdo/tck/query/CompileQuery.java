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
 
package org.apache.jdo.tck.query;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Compile Query
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-14.
 *<BR>
 *<B>Assertion Description: </B> <code>Query.compile()</code> requires the <code>Query</code> instance to validate any elements bound to the query instance and report any inconsistencies by throwing a <code>JDOUserException</code>. It is only a hint to the <code>Query</code> instance to prepare and optimize an execution plan for the query, implementations are not required to support query compilation.
 */

public class CompileQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-14 (CompileQuery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CompileQuery.class);
    }

    /** */
    public void test() {
        pm = getPM();

        initDatabase(pm, PCPoint.class);
        runTestCompileQuery(pm);

        pm.close();
        pm = null;
    }

    /** */
    private void runTestCompileQuery(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String params = "Integer param";
            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters(params);
            query.setFilter("x == param");
            query.compile();
            
            Object results = query.execute(new Integer(4));

            // check query result
            List expected = new ArrayList();
            Object p5 = new PCPoint(4, 4);
            expected.add(p5);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
