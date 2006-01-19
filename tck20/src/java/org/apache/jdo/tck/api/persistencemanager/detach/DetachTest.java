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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.shoppingcart.CartEntry;
import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.apache.jdo.tck.pc.shoppingcart.Product;
import org.apache.jdo.tck.pc.shoppingcart.Undetachable;

import javax.jdo.PersistenceManager;

/**
 *
 */
public class DetachTest extends JDO_Test {

    /** Creates a new instance of DetachTest */
    public DetachTest() {
    }

    protected void localSetUp() {
        addTearDownClass(CartEntry.class);
        addTearDownClass(Cart.class);
        addTearDownClass(Product.class);
        addTearDownClass(Undetachable.class);
    }
}
