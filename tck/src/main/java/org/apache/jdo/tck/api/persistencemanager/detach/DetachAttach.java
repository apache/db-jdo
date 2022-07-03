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

import org.apache.jdo.tck.pc.shoppingcart.Cart;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test DetachAttach
 * <BR>
 * <B>Keywords:</B> makePersistent detach
 * <BR>
 * <B>Assertion IDs:</B> 
 * <BR>
 * <B>Assertion Description: </B>
For a detached instance, they locate or create a persistent 
instance with the same JDO identity as the detached instance
 */
public class DetachAttach extends DetachTest {
    
    private static final String ASSERTION_FAILED = 
            "Assertion A12.6.8 (DetachAttach) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DetachAttach.class);
    }

    /** */
    public void testAttachClean() {
        Cart detachedCart = createDetachedInstance(ASSERTION_FAILED);
        pm.currentTransaction().begin();
        Cart attachedCart = (Cart)pm.makePersistent(detachedCart);
        checkCartValues(ASSERTION_FAILED +
                "after attach," + NL, attachedCart, true);
        pm.currentTransaction().commit();
        failOnError();
    }

}
