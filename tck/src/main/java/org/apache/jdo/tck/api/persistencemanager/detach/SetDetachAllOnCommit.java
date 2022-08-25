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
package org.apache.jdo.tck.api.persistencemanager.detach;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test SetDetachAllOnCommit
 * <BR>
 * <B>Keywords:</B> DetachAllOnCommit commit detach
 * <BR>
 * <B>Assertion IDs:</B> A12.6.8-1, A12.6.8-2
 * <BR>
 * <B>Assertion Description: </B>
 * We define a new property called DetachAllOnCommit
 * PersistenceManager.setDetachAllOnCommit(boolean detachAllOnCommit)
 * sets the DetachAllOnCommit property
 * PersistenceManager.getDetachAllOnCommit()
 * The value of the DetachAllOnCommit flag is returned. 
 */
public class SetDetachAllOnCommit extends DetachTest {
    
    private static final String ASSERTION_FAILED = "Assertion A12.6.8-1 (pm.setDetachAllOnCommit) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetDetachAllOnCommit.class);
    }

    /** */
    public void test() {
        pm = getPM();
    
        runTestSetDetachAllOnCommit(pm);
        
        pm.close();
        pm = null;
    }

    /**
     * @param pm the PersistenceManager
     */
    protected void runTestSetDetachAllOnCommit(PersistenceManager pm) {
        pm.setDetachAllOnCommit(false);
        if (pm.getDetachAllOnCommit()) {
            fail(ASSERTION_FAILED,
                 "pm.getDetachAllOnCommit() should return false after setting the flag to false.");
        }

        pm.setDetachAllOnCommit(true);
        if (!pm.getDetachAllOnCommit()) {
            fail(ASSERTION_FAILED,
                 "pm.getDetachAllOnCommit() should return true after setting the flag to true.");
        }
    }
}
