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
 
package org.apache.jdo.tck.query.jdoql;


import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Assignment, Pre- and Post-Increment and Decrement Operators 
 *are not supported
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion IDs:</B> A14.6.2-7.
 *<BR>
 *<B>Assertion Description: </B>
The assignment operators =, +=, etc. and pre- and post-increment and -decrement
are not supported in a <code>Query</code> filter.

 */

public class AssignmentPrePostIncrementDecrementNotSupported extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-7 (AssignmentPrePostIncrementDecrementNotSupported) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AssignmentPrePostIncrementDecrementNotSupported.class);
    }

    /** */
    public void testNegative() {
        PersistenceManager pm = getPM();
        
        runTestUnsupportedOperators01(pm, "x = 1");
        runTestUnsupportedOperators01(pm, "x += 1");
        runTestUnsupportedOperators01(pm, "x -= 1");
        runTestUnsupportedOperators01(pm, "x *= 1");
        runTestUnsupportedOperators01(pm, "x /= 1");
        runTestUnsupportedOperators01(pm, "x &= 1");
        runTestUnsupportedOperators01(pm, "x |= 1");
        runTestUnsupportedOperators01(pm, "x ^= 1");
        runTestUnsupportedOperators01(pm, "x %= 1");
        runTestUnsupportedOperators01(pm, "x++ == 1");
        runTestUnsupportedOperators01(pm, "x-- == 1");
        runTestUnsupportedOperators01(pm, "++x == 1");
        runTestUnsupportedOperators01(pm, "--x == 1");
        runTestUnsupportedOperators02(pm, "x == ++param", new Integer(1));
        runTestUnsupportedOperators02(pm, "x == --param", new Integer(1));
        runTestUnsupportedOperators02(pm, "x == param++", new Integer(1));
        runTestUnsupportedOperators02(pm, "x == param--", new Integer(1));
        
        pm.close();
        pm = null;
    }

    /** */
    void runTestUnsupportedOperators01(PersistenceManager pm, String filter) {
        
        Query query = pm.newQuery();
        String expectedMsg = "setFilter: Syntax error unexpected token ...";
        
        query.setClass(PCPoint.class);
        query.setCandidates(pm.getExtent(PCPoint.class, false));
        
        try {
            query.setFilter(filter);                
            query.compile();
            fail(ASSERTION_FAILED,
                 "Test UnsupportedOperators01(" + filter + ") - " + 
                 "Missing JDOUserException(" +  expectedMsg + ")");
        } 
        catch (JDOUserException ex) {
            // expected exception
            if (debug) {
                logger.debug("expected exception: " + ex);
                logger.debug("Test UnsupportedOperators01(\"" + filter + 
                             "\") - Passed\n");
            }
        }
    }
    
    /** */
    void runTestUnsupportedOperators02(PersistenceManager pm, 
                                       String filter,
                                       Integer param) {

        Query query = pm.newQuery();
        String params = "Integer param";
        String expectedMsg = "setFilter: Syntax error unexpected token ...";
        
        query.setClass(PCPoint.class);
        query.setCandidates(pm.getExtent(PCPoint.class, false));
        query.declareParameters(params);

        try {
            query.setFilter(filter);
            query.compile();

            fail(ASSERTION_FAILED,
                 "Test UnsupportedOperators02(" + filter + ") - " + 
                 "Missing JDOUserException(" +  expectedMsg + ")");
        } 
        catch (JDOUserException ex) {
            if (debug) {
                logger.debug("expected exception: " + ex);
                logger.debug("Test UnsupportedOperators02(\"" + filter + 
                             "\") - Passed\n");
            }
        }
    }
}

