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


package org.apache.jdo.tck.api.persistencemanager.cache;

import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Evicting With RetainValues False
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion ID:</B> A12.5.1-3.
 *<BR>
 *<B>Assertion Description: </B>
If users wish to automatically <code>evict</code> transactional instances at transaction
<code>commit</code> time, then they should set <code>RetainValues</code> to <code>false</code>.
For each persistent-clean instance that the
JDO <code>PersistenceManager</code> evicts, it:
<UL>
<LI>calls the <code>jdoPreClear</code> method on each instance,
if the class of the instance implements <code>InstanceCallbacks</code></LI>
<LI>clears persistent fields on each instance (sets the value of the field to its Java default value)</LI>
<LI>changes the state of instances to hollow</LI>
</UL>

 */

public class EvictingWithRetainValuesFalse extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.1-3 (EvictingWithRetainValuesFalse) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(EvictingWithRetainValuesFalse.class);
    }	

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        super.localSetUp();
        addTearDownClass(PCPoint2.class);
    }

    /** */
    public void test() {

	    if (!isRetainValuesSupported()) {
    		printUnsupportedOptionalFeatureNotTested("EvictingWithRetainValuesFalse", "javax.jdo.option.RetainValues");
	    }
		else
		{
	        pm = getPM();
			PCPoint2 pt = getHollowInstance();

			Transaction tx = pm.currentTransaction();
			tx.setRetainValues(false); //This should cause eviction of transactional instances when transaction is later commited.
						
			//Test
			tx.begin();
			makePersistentClean(pt);
			tx.commit(); // This should evict pt
			verify(pt);
		}
    }

	/** */
    private PCPoint2 getHollowInstance() {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        PCPoint2 pt = new PCPoint2(1,2);
        pm.makePersistent(pt);
        tx.commit();

		int curr = currentState(pt);
	    if (curr != HOLLOW){
			fail(ASSERTION_FAILED,
			     "Unable to create HOLLOW instance, state is " + states[curr]);
	    }
		return pt;
    }

    /** */
    private void makePersistentClean(PCPoint2 pt) {
		pm.makeTransactional(pt);
        int curr = currentState(pt);
        if (curr != PERSISTENT_CLEAN){
        	fail(ASSERTION_FAILED,
        	     "Unable to create PERSISTENT_CLEAN instance, state is " + states[curr]);
        }
    }

	/** */
    private void verify(PCPoint2 pt) {
		// When PersistenceManager evicts instances, it: calls the jdoPreClear method on each instance
        if ( !pt.wasClearCalled()) {
            fail(ASSERTION_FAILED,
                 "missing call of jdoPreClear");
        }

		// When PersistenceManager evicts instances, it: changes the state of the instances to hollow
		int curr = currentState(pt);
        if ( (curr != HOLLOW) && (curr != PERSISTENT_NONTRANSACTIONAL) ) {
            fail(ASSERTION_FAILED,
                 "Instance should be HOLLOW after tx.commit(), state is " + states[curr]);
        }
		
        pm.makeTransient(pt);
        // When PersistenceManager evicts instances, it: clears persistent fields on each instance (sets the value of the field to its Java default value)
        int x=pt.getX();
        Integer y=pt.getY();
        if ((x != 0) || (y != null)) {
        	fail(ASSERTION_FAILED,
        	     "persistent fields not cleared, pt.getX()=="+x+" and pt.getY()=="+y);
        }
    }	
}
