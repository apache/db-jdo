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
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test that ObjectId is not modified even if ObjectId returned by GetObjectId() is modified
 *<BR>
 *<B>Keywords:</B> lifecycle GetObjectId
 *<BR>
 *<B>Assertion IDs:</B> A5.4-6
 *<BR>
 *<B>Assertion Description: </B>
 The object returned by any call to getObjectId might be modified by the user, 
 but that modification does not affect the identity of the object that was originally referred.
 */

public class ObjectIdNotModifiedWhenObjectIdInstanceModified extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A5.4-6 (ObjectIdNotModifiedWhenObjectIdInstanceModified) failed: ";
		
	private static final int NUM_OBJECTS = 50;

    /** The persistent instances of this test. */
    private Object[] obj;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
		BatchTestRunner.run(ObjectIdNotModifiedWhenObjectIdInstanceModified.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        pm = getPM();
        addTearDownClass(StateTransitionObj.class);
        obj = generatePersistentInstances();
    }
    
	public void test() throws Exception {
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
			mangleObject(objId);
			Object objId2 = pm.getObjectId(obj[i]); // get another ObjectId copy
			if (objId.equals(objId2))
				fail(ASSERTION_FAILED,
					 "object Id has been changed");
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
