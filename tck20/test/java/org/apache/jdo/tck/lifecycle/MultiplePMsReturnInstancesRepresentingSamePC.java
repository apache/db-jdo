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
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test that Multiple PMs return different instances representing same PC object
 *<BR>
 *<B>Keywords:</B> lifecycle Multiple PMs getObjectById equal
 *<BR>
 *<B>Assertion IDs:</B> A5.4-7
 *<BR>
 *<B>Assertion Description: </B>
 The instance returned by a call to getObjectById(Object) of different PersistenceManager 
 instances returned by the same PersistenceManagerFactory represent the same persistent object, 
 but with different Java object identity (specifically, all instances returned by getObjectId from the instances
 must return true to equals comparisons with all others).
 */

public class MultiplePMsReturnInstancesRepresentingSamePC extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A5.4-7 (MultiplePMsReturnInstancesRepresentingSamePC) failed: ";

   	private static final int NUM_OBJECTS = 50;

    /** The persistent instances of this test. */
    private Object[] obj;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
		BatchTestRunner.run(MultiplePMsReturnInstancesRepresentingSamePC.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        pm = getPM();
        addTearDownClass(StateTransitionObj.class);
        obj = generatePersistentInstances();
    }
    
	public void test() {
		PersistenceManager pm2 = null;
		PersistenceManager pm3 = null;
	
	    try {
			// Get two more PMs
			pm2 = pmf.getPersistenceManager();
			pm3 = pmf.getPersistenceManager();

			Extent extent = pm.getExtent(StateTransitionObj.class, false);
			Iterator iter = extent.iterator();
			if( !iter.hasNext() ){
				fail(ASSERTION_FAILED,
					 "Extent for StateTransitionObj should not be empty");
			}
			extent.close(iter);


			for (int i=0; i<NUM_OBJECTS; i++)
			{
				Object obj1 = obj[i];
				Object objId1 = pm.getObjectId(obj1);
				Object obj2 = pm2.getObjectById(objId1,false);
				Object objId2 = pm2.getObjectId(obj2);
				Object obj3 = pm3.getObjectById(objId1,false);
				Object objId3 = pm3.getObjectId(obj3);
				
				//Verify that objects are different instances
				if (!((obj1!=obj2) && (obj1!=obj3)))
				{
					fail(ASSERTION_FAILED,
						 "Objects are not different instances. obj1=" + obj1 + " obj2= " + obj2 + " obj3= " + obj3);
				}
				
				//Verify that object Ids are equal
				if (!(objId1.equals(objId2) && objId1.equals(objId3)))
				{
					fail(ASSERTION_FAILED,
						 "Object Ids are not equal. objId1=" + objId1 + " objId2= " + objId2 + " objId3= " + objId3);
				}
			}
	    }
	    finally {
	    	if (pm2!=null && !pm2.isClosed())
	    	{
	    	    pm2.close();
	    	}
	    	if (pm3!=null && !pm3.isClosed())
	    	{
	    	    pm3.close();
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
