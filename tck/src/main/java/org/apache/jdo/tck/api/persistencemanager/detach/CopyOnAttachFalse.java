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




import javax.jdo.JDOUserException;
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
 * A12.6.8-29 [If the CopyOnAttach property is false, then the instance itself
is transitioned to persistent-clean for datastore transactions or persistent-nontransactional for optimistic
transactions. If there already is an instance in the cache with the same identity, JDOUserException
is thrown.]
 */
public class CopyOnAttachFalse extends DetachTest {
    
    private static final String ASSERTION_FAILED = 
            "Assertion A12.6.8 (CopyOnAttachFalse) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CopyOnAttachFalse.class);
    }

    /** */

    /** 
     */
    public void testAttachClean() {
        Cart detachedCart = createDetachedInstance(ASSERTION_FAILED);
        pm.setCopyOnAttach(false);
        pm.currentTransaction().begin();
        Cart attachedCart = (Cart)pm.makePersistent(detachedCart);
        assertTrue("Expected attached object to == detached object.", 
                attachedCart == detachedCart);
        pm.currentTransaction().commit();
    }

    /** */
    public void testAttachDirty() {
        Cart detachedCart = createDetachedInstance(ASSERTION_FAILED);
        pm.setCopyOnAttach(false);
        pm.currentTransaction().begin();
        cartEntry1.setQuantity(500);
        goldenCartEntry.setQuantity(500);
        Cart attachedCart = (Cart)pm.makePersistent(detachedCart);
        assertTrue("Expected attached object to == detached object.", 
                attachedCart == detachedCart);
        pm.currentTransaction().commit();
    }

    /** 
     */
    public void testNegativeAttachClean() {
        Cart detachedCart = createDetachedInstance(ASSERTION_FAILED);
        pm.setCopyOnAttach(false);
        pm.currentTransaction().begin();
        // Obtain new instance of cart1
        pm.getObjectById(cart1oid, false);
        try {
            Cart attachedCart = (Cart)pm.makePersistent(detachedCart);
            fail("Expected JDOUserException. Object with same identity as "
                    + "attached object is already in the cache.");
        } catch (JDOUserException jdoe) {
            // expected exception
        }
        pm.currentTransaction().commit();
    }

}
