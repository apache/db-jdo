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
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Get Object Id Not Persistence Capable
 *<BR>
 *<B>Keywords:</B> jdohelper
 *<BR>
 *<B>Assertion ID:</B> A8.3-3.
 *<BR>
 *<B>Assertion Description: </B>
The jdohelper.getObjectId method returns null if Object is not persistence capable

 */

 /*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version  
 * Azita Kamangar  	9/26/01		 1.0
 */

public class GetObjectIdNotPersistenceCapable extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.3-3 (GetObjectIdNotPersistenceCapable) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetObjectIdNotPersistenceCapable.class);
    }

    /* test JDOHelper.getObjectId(Object pc)
     *  
     */
    public void testGetObjectIdNotPersistenceCapable() {
        pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Point p1 = new Point(1,3);
        Object oid = JDOHelper.getObjectId(p1);
        tx.commit();
        if (oid != null) 
            fail(ASSERTION_FAILED,
                 "JDOHelper.getObjectId called for instance of non-pc class returned non-null ObjectId " + oid);
        pm.close();
        pm = null;
    }
}
