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


import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Methods and Object Construction not Supported
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-8.
 *<BR>
 *<B>Assertion Description: </B> Methods, including object construction, are not
 *supported in a <code>Query</code> filter.
 */

public class MethodsAndObjectConstructionNotSupported extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-8 (MethodsAndObjectConstructionNotSupported) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MethodsAndObjectConstructionNotSupported.class);
    }

    /** */
    public void test() {
        pm = getPM();

        initDatabase(pm, PCPoint.class);
        runTestUnsupportedOperators01(pm, "this.getX() == 1");
        runTestUnsupportedOperators01(pm, "y.intValue() == 1");
        runTestUnsupportedOperators01(pm, "y == new Integer(1)");
        
        pm.close();
        pm = null;
    }

    /** */
    void runTestUnsupportedOperators01(PersistenceManager pm, String filter) {
        String expectedMsg = "setFilter: Invalid method call ....";
        Query query = pm.newQuery();
        query.setClass(PCPoint.class);
        query.setCandidates(pm.getExtent(PCPoint.class, false));
        
        try {
            query.setFilter(filter);                
            query.compile();
            
            fail(ASSERTION_FAILED, "Missing JDOUserException(" + expectedMsg +
                                   ") for filter " + filter);
        } 
        catch (JDOUserException ex) {
            if (debug) logger.debug("expected exception " + ex);
        }
    }
}

