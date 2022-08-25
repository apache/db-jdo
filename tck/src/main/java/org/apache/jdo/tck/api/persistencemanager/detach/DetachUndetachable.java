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

import javax.jdo.JDOHelper;

import org.apache.jdo.tck.pc.shoppingcart.Undetachable;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test DetachUndetachable
 * <BR>
 * <B>Keywords:</B> detachCopy detach detachable
 * <BR>
 * <B>Assertion IDs:</B> A12.6.8-32
 * <BR>
 * <B>Assertion Description: </B>
Instances to be detached that are not of a Detachable class are detached as transient instances.
 */
public class DetachUndetachable extends DetachTest {
    
    private static final String ASSERTION_FAILED = 
            "Assertion A12.6.8-32 (DetachUndetachable) failed: ";

    protected Undetachable un1;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DetachUndetachable.class);
    }

    /** */
    public void testDetachUndetachable() {
        setupUndetachable();
        getPM().currentTransaction().begin();
        Undetachable instance = pm.detachCopy(un1);
        if (JDOHelper.isPersistent(instance)) {
            appendMessage("testDetachUndetachable: should be transient" + 
                " but is not. The object state is: " +
                states[currentState(instance)]
                + " " + getStateOfInstance(instance));
        }
        pm.currentTransaction().commit();
        failOnError();
    }

    protected void setupUndetachable() {
        un1 = new Undetachable();
        
        getPM().currentTransaction().begin();
        pm.makePersistent(un1);
        pm.currentTransaction().commit();
    }
}
