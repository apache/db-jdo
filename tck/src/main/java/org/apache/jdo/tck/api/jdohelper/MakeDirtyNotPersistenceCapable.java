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
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Make Dirty Not Persistence Capable
 *<BR>
 *<B>Keywords:</B> jdohelper 
 *<BR>
 *<B>Assertion ID:</B> A8.2-4
 *<BR>
 *<B>Assertion Description: </B>
  The JDOHelper.makeDirty method has no affect if the instance is not persistence capable
  evaluating to true when == is used. 
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/3/01      1.0
 */
public class MakeDirtyNotPersistenceCapable extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.2-4 (MakeDirtyNotPersistenceCapable) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeDirtyNotPersistenceCapable.class);
    }

    /* test JDOHelper.makeDirthy(Object pc)
     *  
     */
    public void testMakeDirtyNotPersistenceCapable() {
        pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Point p1 = new Point(1,2);
        JDOHelper.makeDirty(p1,"x");
        boolean dirty = JDOHelper.isDirty(p1);
        tx.commit();
        if (dirty)
            fail(ASSERTION_FAILED,
                 "instance of non-pc class is dirty after calling JDOHelper.makeDirty.");
        pm.close();
        pm = null;
    }
}
