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
 
package org.apache.jdo.tck.api.jdohelper;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Is Dirty False
 *<BR>
 *<B>Keywords:</B> jdohelper 
 *<BR>
 *<B>Assertion ID:</B> A8.4.1-2
 *<BR>
 *<B>Assertion Description: </B>
  The jdohelper.isDirty method returns false if the instance of persistence capable
  have not  changed the state in the current transaction 
  evaluating to true when == is used. 
 */
 /*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version  
 * Azita Kamangar  	9/25/01		 1.0
 */
public class IsDirtyFalse extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.4.1-2 (IsDirtyFalse) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IsDirtyFalse.class);
    }
 
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
    }
    
    /* test JDOHelper.isDirty(Object pc)
     *  
     */
    public void testIsDirtyFalse() {
    	pm = getPM();
    	Transaction tx = pm.currentTransaction();
    	tx.begin();
    	PCPoint p1 = new PCPoint(1,3);
    	pm.makePersistent(p1);
        tx.commit();
        
        tx.begin();
        // access p1 field to make sure the instance is loaded in new tx
        p1.getX();
    	boolean dirty = JDOHelper.isDirty(p1);
    	tx.commit();
    	if (dirty)
    		fail(ASSERTION_FAILED,
                 "JDOHelper.isDirty returns true for P-CLEAN instance");
        pm.close();
        pm = null;
    }
}
