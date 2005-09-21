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

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Query with ignoreCache=false.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-7.
 *<BR>
 *<B>Assertion Description: </B>
 * With ignoreCache set to false:
 * <UL>
 * <LI>if instances were made persistent in the current transaction, 
 * the instances will be considered part of the candidate instances.</LI>
 * <LI>if instances were deleted in the current transaction, the instances 
 * will not be considered part of the candidate instances.</LI>
 * <LI>modified instances will be evaluated using their current transactional 
 * values.</LI>
 * </UL>
 */

public class IgnoreCacheFalse extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-7 (IgnoreCacheFalse) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IgnoreCacheFalse.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        super.localSetUp();
        addTearDownClass(PrimitiveTypes.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        
        try {
            loadPrimitiveTypes(pm);
            // set ignoreCache flag
            pm.setIgnoreCache(false);
            runTestNewInstance(pm);
            runTestDeletedInstance(pm);
            runTestModifiedInstance(pm);
        }
        finally {
            pm.close();
            pm = null;
        }
    }
    
    /** */
    void runTestNewInstance(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create new instance
        PrimitiveTypes newInstance = new PrimitiveTypes();
        newInstance.setId(98L);
        newInstance.setIntNotNull(98);
        pm.makePersistent(newInstance);
        
        Collection result = (Collection)pm.newQuery(
            PrimitiveTypes.class, "intNotNull == 98").execute();
        // check result
        if (result.isEmpty())
            fail(ASSERTION_FAILED, 
                 "Query should find new instance, but query result is empty");
        Iterator i = result.iterator();
        PrimitiveTypes p = (PrimitiveTypes)i.next();
        if (p.getId() != 98L)
            fail(ASSERTION_FAILED, 
                 "Query returned wrong instance with id " + p.getId());
        if (i.hasNext())
            fail(ASSERTION_FAILED, 
                 "Query returned more than one instance");
        
        if (debug)
            logger.debug("New instance is part of query result");
        
        tx.rollback();
    }
    
    /** */
    void runTestDeletedInstance(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Collection tmp = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 3").execute();
        PrimitiveTypes instance3 = (PrimitiveTypes)tmp.iterator().next();
        pm.deletePersistent(instance3);
                
        Collection result = (Collection)pm.newQuery(
            PrimitiveTypes.class, "intNotNull == 3").execute();
        // check result
        if (result.isEmpty()) {
            if (debug)
                logger.debug("deleted instance not part of query result.");
        }
        else {
            // query result not empty => problem
            PrimitiveTypes p = (PrimitiveTypes)result.iterator().next();
            if (JDOHelper.isDeleted(p))
                fail(ASSERTION_FAILED,
                     "query result should not include deleted instance");
            else
                fail(ASSERTION_FAILED,
                     "query returns suspicious isntance " + p);
        }        
        tx.rollback();
    }
    
    /** */
    void runTestModifiedInstance(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Collection tmp = (Collection)pm.newQuery(
            PrimitiveTypes.class, "id == 5").execute();
        PrimitiveTypes instance5 = (PrimitiveTypes)tmp.iterator().next();
        instance5.setIntNotNull(99);
                
        Collection result = (Collection)pm.newQuery(
            PrimitiveTypes.class, "intNotNull == 99").execute();
        // check result
        if (result.isEmpty()) {
            fail(ASSERTION_FAILED, 
                 "Query should find modified instance, but query result is empty");
        }
        Iterator i = result.iterator();
        PrimitiveTypes p = (PrimitiveTypes)i.next();
        if (p.getId() != 5L)
            fail(ASSERTION_FAILED, 
                 "Query returned wrong instance with id " + p.getId());
        if (p.getIntNotNull() != 99)
            fail(ASSERTION_FAILED, 
                 "Query returned instance with wrong intNotNull field value " + 
                 p.getIntNotNull());
        if (i.hasNext())
            fail(ASSERTION_FAILED, 
                 "Query returned more than one instance");
        
        if (debug)
            logger.debug("Modified instance is part of query result");
        
        tx.rollback();
    }
}
