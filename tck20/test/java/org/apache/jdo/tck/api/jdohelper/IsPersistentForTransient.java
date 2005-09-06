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
 
package org.apache.jdo.tck.api.jdohelper;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Is Persistent For Transient
 *<BR>
 *<B>Keywords:</B> jdohelper 
 *<BR>
 *<B>Assertion ID:</B> A8.4.3-3
 *<BR>
 *<B>Assertion Description: </B>
  The jdohelper.isPersistent  method returns false If the instance is transient,  
  evaluating to true when == is used. 
 */
  
  /*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version  
 * Azita Kamangar  	10/2/01		 1.0
 */
public class IsPersistentForTransient extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.4.3-3 (IsPersistentForTransient) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IsPersistentForTransient.class);
    }
 
    /* test JDOHelper.isPersistent(Object pc)
     *  
     */
    public void testIsPersistentForTransient() {
    	pm = getPM();
    	Transaction tx = pm.currentTransaction();
    	tx.begin();
    	PCPoint p1 = new PCPoint(1,3);
    	boolean persistent = JDOHelper.isPersistent(p1);
    	tx.commit();
    	if (persistent)
    		fail(ASSERTION_FAILED,
                 "JDOHelper.isPersistent returns true for transient instance.");
        pm.close();
        pm = null;
    }
}
 
