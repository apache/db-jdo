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

import java.util.Iterator;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.shoppingcart.CartEntry;
import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.apache.jdo.tck.pc.shoppingcart.Product;
import org.apache.jdo.tck.pc.shoppingcart.Undetachable;

import javax.jdo.PersistenceManager;

/**
 *
 */
public abstract class DetachTest extends JDO_Test {

    protected Cart cart1;
    protected CartEntry cartEntry1;
    protected Product product1;
    
    protected Object cart1oid;
    
    protected Cart goldenCart;
    protected CartEntry goldenCartEntry;
    
    protected Object[] cartClosure;
    
    protected final static String CART_ENTRIES = "Cart.entries";
    protected final static String CARTENTRY_CART = "CartEntry.cart";
    protected final static String CARTENTRY_PRODUCT = "CartEntry.product";
    protected final static String[] CART_FETCH_GROUPS = new String[]
        {FetchPlan.DEFAULT, CART_ENTRIES, CARTENTRY_CART, CARTENTRY_PRODUCT};
    
    /** Creates a new instance of DetachTest */
    public DetachTest() {
    }

    /** */
    protected void localSetUp() {
        addTearDownClass(CartEntry.class);
        addTearDownClass(Cart.class);
        addTearDownClass(Product.class);
        addTearDownClass(Undetachable.class);
        
        Product goldenProduct = new Product("PRODUCT1", "Product 1");
        goldenCart = new Cart("142857");
        goldenCartEntry = 
                goldenCart.newCartEntry(goldenProduct, 100);

        product1 = new Product("PRODUCT1", "Product 1");
        cart1 = new Cart("142857");
        cartEntry1 = new CartEntry(
                cart1, goldenCartEntry.getId(), product1, 100);
        cartClosure = new Object[]
            {cart1, cartEntry1, product1};
        
        getPM().currentTransaction().begin();
        pm.makePersistent(cart1);
        cart1oid = pm.getObjectId(cart1);
        pm.currentTransaction().commit();
    }

    /** */
    protected void setCartFetchGroups() {
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(CART_FETCH_GROUPS);
        fp.setMaxFetchDepth(2);
    }

    /** */
    protected void checkState(String location, Object obj, 
            boolean persistent) {
        if(persistent) {
            if (JDOHelper.isPersistent(obj))
                return;
        } else if (JDOHelper.isDetached(obj))
                return;
        appendMessage(location +
                " should be " + 
                (persistent?"persistent":"detached") + 
                " but is not. The object state is: " +
                states[currentState(obj)] + " " + getStateOfInstance(obj));
    }

    /** */
    protected void reportDifference(String location, 
            long expected, long actual) {
        appendMessage(location + NL +
                "expected: " + expected + NL +
                "actual: " + actual);
    }

    /** */
    protected void reportDifference(String location, 
            Object expected, Object actual) {
        appendMessage(location + NL +
                "expected: " + expected + NL +
                "  actual: " + actual);
    }

    /** */
    protected void checkCartValues(String location, Cart cart) {
        checkCartValues(location, cart, false);
    }

    /** */
    protected void checkCartValues(String location, Cart cart,
            boolean persistent) {
        checkState(location + "Cart instance", cart, persistent);
        if (!goldenCart.getCustomerId().equals(cart.getCustomerId()))
            reportDifference(location + 
                " differences in cart.customerId", 
                goldenCart.getCustomerId(), cart.getCustomerId());
        Iterator goldenCartEntries = goldenCart.getEntries();
        Iterator cartEntries = cart.getEntries();
        while (cartEntries.hasNext()) {
            CartEntry goldenCartEntry = (CartEntry)goldenCartEntries.next();
            CartEntry cartEntry = (CartEntry)cartEntries.next();
            checkState(location + "CartEntry instance", cartEntry,
                    persistent);
            Cart cartEntryCart = cartEntry.getCart();
            if (cartEntryCart != cart) {
                reportDifference(location +
                        " incorrect value for cartEntry.cart",
                        cart, cartEntryCart);
            }
            if (goldenCartEntry.getId() != cartEntry.getId()) 
                reportDifference(location + 
                    " differences in cartEntry.id", 
                    goldenCartEntry.getId(), cartEntry.getId());
            if (goldenCartEntry.getQuantity() != cartEntry.getQuantity()) 
                reportDifference(location + 
                    " differences in cartEntry.quantity", 
                    goldenCartEntry.getQuantity(), cartEntry.getQuantity());
            Product goldenProduct = goldenCartEntry.getProduct();
            Product product = cartEntry.getProduct();
            checkState(location + "Product instance", product,
                    persistent);
            if (!goldenProduct.getDescription()
                    .equals(product.getDescription()))
                reportDifference(location + 
                    " differences in product.description", 
                    goldenProduct.getDescription(), product.getDescription());
        }
    }

    /** */
    protected Cart createDetachedInstance(String ASSERTION_FAILED) {
        getPM().currentTransaction().begin();
        setCartFetchGroups();
        pm.retrieveAll(cartClosure);
        pm.setDetachAllOnCommit(true);
        pm.currentTransaction().commit();
        checkCartValues(ASSERTION_FAILED + 
                "after commit with DetachAllOnCommit,", cart1);
        failOnError();
        pm.setDetachAllOnCommit(false);
        return cart1;
    }
}
