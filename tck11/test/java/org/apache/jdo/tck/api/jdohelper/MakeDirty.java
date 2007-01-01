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
 *<B>Title:</B> Make Dirty 
 *<BR>
 *<B>Keywords:</B> jdohelper 
 *<BR>
 *<B>Assertion ID:</B> A8.2-1
 *<BR>
 *<B>Assertion Description: </B>
  The jdohelper.makeDirty method delegates to the parameter instance
   and marks the specified field  dirty if parameter is instance of Persistence
   capable .  evaluating to true when == is used. 
 */
 /*
 * Revision History
 * ================
 * Author         :     Date   :    Version  
 * Azita Kamangar   10/2/01      1.0
 */
public class MakeDirty extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A8.2-1 (MakeDirty) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeDirty.class);
    }

    /* test JDOHelper.MakeDirty(Object pc)
     *  
     */
    public void testMakeDirty() {
        pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        PCPoint p1 = new PCPoint(1,3);
        pm.makePersistent(p1);
        if (debug) logger.debug(" isDirty1:"+ JDOHelper.isDirty(p1));  
        tx.commit();
        if (debug) logger.debug(" isDirty2:" + JDOHelper.isDirty(p1));
        
        tx.begin();
        JDOHelper.makeDirty(p1,"x");
        boolean dirty = JDOHelper.isDirty(p1);
        tx.commit();
        if (debug) logger.debug(" isDirty3:" + dirty);
        if (!dirty)
            fail(ASSERTION_FAILED,
                 "Instance is not dirty after calling JDOHelper.makeDirty");
        pm.close();
        pm = null;
    }
}
