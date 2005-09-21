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

package org.apache.jdo.tck.query.result;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Changing query result fails.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-7.
 *<BR>
 *<B>Assertion Description: </B>
 * Executing any operation on the result that might change it throws 
 * UnsupportedOperationException.
 */

public class ImmutableQueryResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-7 (ImmutableQueryResult) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ImmutableQueryResult.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            loadPrimitiveTypes(pm);
            runTest(pm);
        }
        finally {
            cleanupDatabase(pm, PrimitiveTypes.class);
            pm.close();
            pm = null;
        }
    }
    
    /** */
    void runTest(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // get PrimitiveTypes instance with id 5
        Collection instance5Collection = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 5").execute();
        PrimitiveTypes instance5 = 
            (PrimitiveTypes)instance5Collection.iterator().next();
        
        // get query result collection
        Collection result = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id > 3").execute();
        
        // method add
        try {
            result.add(new Integer(1));
            fail(ASSERTION_FAILED,
                 "Method add called on a query result show throw " + 
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method add called on a query result throws " +
                             "expected exception " + ex);
        }
        
        // method addAll
        try {
            result.addAll(instance5Collection);
            fail(ASSERTION_FAILED,
                 "Method addAll called on a query result show throw " +
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method addAll called on a query result throws " +
                             "expected exception " + ex);
        }
     
        // method clear
        try {
            result.clear();
            fail(ASSERTION_FAILED,
                 "Method clear called on a query result show throw " + 
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method clear called on a query result throws " +
                             "expected exception " + ex);
        }
        
        // method remove
        try {
            result.remove(instance5);
            fail(ASSERTION_FAILED,
                 "Method remove called on a query result show throw " + 
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method remove called on a query result throws " +
                             "expected exception " + ex);
        }
        
        // method removeAll
        try {
            result.removeAll(instance5Collection);
            fail(ASSERTION_FAILED,
                 "Method removeAll called on a query result show throw " + 
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method removeAll called on a query result " + 
                             "throws expected exception " + ex);
        }
        
        // method retainAll
        try {
            result.retainAll(instance5Collection);
            fail(ASSERTION_FAILED,
                 "Method retainAll called on a query result show throw " + 
                 "UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug("Method retainAll called on a query result "  + 
                             "throws expected exception " + ex);
        }
        
        // method iterator.remove
        try {
            result.iterator().remove();
            fail(ASSERTION_FAILED,
                 "Method remove called on an iterator obtained from a query " + 
                 "result show throw UnsupportedOperationException"); 
        }
        catch (UnsupportedOperationException ex) {
            // expected exception
            if (debug) 
                logger.debug(
                    "Method remove called on an iterator obtained " + 
                    "from a query result throws expected exception " + ex);
        }
    }
}
