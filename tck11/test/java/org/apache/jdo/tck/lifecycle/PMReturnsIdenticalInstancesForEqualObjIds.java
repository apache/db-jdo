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
 
package org.apache.jdo.tck.lifecycle;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test that a PM returns the same JDO instance if getObjectById(ObjectId) is called multiple times with the same objectId instance
 *<BR>
 *<B>Keywords:</B> lifecycle getObjectById
 *<BR>
 *<B>Assertion IDs:</B> A5.4-2, A5.4-8, A5.4-10
 *<BR>
 *<B>Assertion Description: </B>
 Any instances returned by any calls to getObjectById(Object) with the same object id instance to the same
 PersistenceManager instance must be identical (assuming the instances were not garbage collected between calls).
 */

public class PMReturnsIdenticalInstancesForEqualObjIds extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A5.4-2, A5.4-8, A5.4-10 (PMReturnsIdenticalInstancesForEqualObjIds) failed: ";
    
	private static final int NUM_OBJECTS = 50;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
		BatchTestRunner.run(PMReturnsIdenticalInstancesForEqualObjIds.class);
    }

    public PMReturnsIdenticalInstancesForEqualObjIds()
    {
    }

	public void test() {
	    pm = getPM();

	    Object[] obj = generatePersistentInstances();

    	Extent extent = pm.getExtent(StateTransitionObj.class, false);
    	Iterator iter = extent.iterator();
    	if( !iter.hasNext() ){
    		fail(ASSERTION_FAILED,
				 "Extent for StateTransitionObj should not be empty");
    	}
		extent.close(iter);

		for (int i=0; i<NUM_OBJECTS; i++)
		{
			Object objId=pm.getObjectId(obj[i]);
			// check that getObjectById returns identical instance
			Object obj2 = pm.getObjectById(objId, true);
			if (!(obj2==obj[i]))
			{
				fail(ASSERTION_FAILED,
					 "objects with OId = " + objId + " are not identical");
			}
		}
	}

	private Object[] generatePersistentInstances()
	{
		StateTransitionObj[] objects = new StateTransitionObj[NUM_OBJECTS];
		try {
		    Transaction t = pm.currentTransaction();
		    t.begin();
		    for(int i = 0; i < NUM_OBJECTS; ++i ){
				objects[i]=new StateTransitionObj(i);
		        objects[i].writeField(i);
		        pm.makePersistent(objects[i]);
		    }
		    t.commit();
			return objects;
		}
		finally {
	         Transaction tx = pm.currentTransaction();
	         if (tx.isActive()) {
	             tx.rollback();
	         }
		}
	}
}
