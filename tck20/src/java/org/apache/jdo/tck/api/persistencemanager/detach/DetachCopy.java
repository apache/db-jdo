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
package org.apache.jdo.tck.api.persistencemanager.detach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.shoppingcart.Cart;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test DetachCopy
 * <BR>
 * <B>Keywords:</B> detachCopy detach
 * <BR>
 * <B>Assertion IDs:</B> 
 * <BR>
 * <B>Assertion Description: </B>
For each instance in the parameter Collection, a 
corresponding detached copy is returned. Each field in the 
persistent instance is handled based on its type and whether the 
field is contained in the fetch group for the persistence-capable 
class. If there are duplicates in the parameter Collection, 
the corresponding detached copy is used for each such duplicate.  
 */
public class DetachCopy extends DetachTest {
    
    private static final String ASSERTION_FAILED = 
            "Assertion A12.6.8 (DetachCopy) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DetachCopy.class);
    }

    /** */
    public void testDetachCopy() {
        getPM().currentTransaction().begin();
        setCartFetchGroups();
        //pm.retrieveAll(cartClosure);
        Cart cartDetached = (Cart)pm.detachCopy(cart1);
        checkCartValues(ASSERTION_FAILED + 
                "after detachCopy," + NL, cartDetached);
        pm.currentTransaction().commit();
        failOnError();
    }

    /** */
    public void testDetachCopyAllCollection() {
        getPM().currentTransaction().begin();
        setCartFetchGroups();
        //pm.retrieveAll(cartClosure);
        Collection instances = new HashSet();
        instances.add(cart1);
        Collection detached = pm.detachCopyAll(instances);
        Cart cartDetached = (Cart)detached.iterator().next();
        checkCartValues(ASSERTION_FAILED + 
                "after detachCopyAll(Collection)," + NL, cartDetached);
        pm.currentTransaction().commit();
        failOnError();
    }

    /** */
    public void testDetachCopyAllArray() {
        getPM().currentTransaction().begin();
        setCartFetchGroups();
        //pm.retrieveAll(cartClosure);
        Object[] instances = new Object[]{cart1};
        Object[] detached = pm.detachCopyAll(instances);
        Cart cartDetached = (Cart)detached[0];
        checkCartValues(ASSERTION_FAILED + 
                "after detachCopyAll(Object[])," + NL, cartDetached);
        pm.currentTransaction().commit();
        failOnError();
    }

}
