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
package org.apache.jdo.tck.pc.shoppingcart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/** This class represents an online shopping cart.  It has a list of entries of
 * type CartEntry.
 */
public class Cart implements Serializable {

    protected static long nextId = System.currentTimeMillis();

    public synchronized static long nextId() {
        return nextId++;
    }

    /** Identity field for use with application identity */
    protected long id;

    /** The identity of the customer whose shopping cart this is. */
    protected String customerId;

    /** The list of entries in this cart */
    protected List entries = new ArrayList(); // element-type CartEntry

    public Cart() {
        this(nextId());
    }

    protected Cart(long id) {
        setId(id);
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public CartEntry newCartEntry(Product product, int quantity) {
        CartEntry ce = new CartEntry(this, CartEntry.nextId(), product, quantity);
        entries.add(ce);
        return ce;
    }

    public void addCartEntry(CartEntry ce) {
        if (ce == null) {
            throw new IllegalArgumentException("no CartEntry given");
        }
        ce.setCart(this);
        entries.add(ce);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void add(int arg0, CartEntry arg1) {
        entries.add(arg0, arg1);
    }

    public boolean add(CartEntry arg0) {
        return entries.add(arg0);
    }

    public boolean addAll(int arg0, Collection arg1) {
        Iterator i = arg1.iterator();
        while (i.hasNext()) {
            CartEntry entry = (CartEntry) i.next();
        }
        return entries.addAll(arg0, arg1);
    }

    public boolean addAll(Collection arg0) {
        Iterator i = arg0.iterator();
        while (i.hasNext()) {
            CartEntry entry = (CartEntry) i.next();
        }
        return entries.addAll(arg0);
    }

    public void clear() {
        entries.clear();
    }

    public boolean contains(CartEntry o) {
        return entries.contains(o);
    }

    public boolean containsAll(Collection arg0) {
        return entries.containsAll(arg0);
    }

    public CartEntry get(int index) {
        return (CartEntry) entries.get(index);
    }

    public int indexOf(CartEntry o) {
        return entries.indexOf(o);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public Iterator iterator() {
        return entries.iterator();
    }

    public int lastIndexOf(CartEntry o) {
        return entries.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return entries.listIterator();
    }

    public ListIterator listIterator(int index) {
        return entries.listIterator(index);
    }

    public Object remove(int index) {
        return entries.remove(index);
    }

    public boolean remove(CartEntry o) {
        return entries.remove(o);
    }

    public boolean removeAll(Collection arg0) {
        Iterator i = arg0.iterator();
        while (i.hasNext()) {
            CartEntry entry = (CartEntry) i.next();
        }
        return entries.removeAll(arg0);
    }

    public boolean retainAll(Collection arg0) {
        return entries.retainAll(arg0);
    }

    public Object set(int arg0, CartEntry arg1) {
        return entries.set(arg0, arg1);
    }

    public int entrySize() {
        return entries.size();
    }

    public List subList(int fromIndex, int toIndex) {
        return entries.subList(fromIndex, toIndex);
    }
    
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof Cart)) {
            return false;
        }
        return equals((Cart) that);
    }
    
    public boolean equals(Cart that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        return this.id == that.id;
    }
    
    public int hashCode() {
        return (int) id;
    }

    public static class Oid implements Serializable {
        public long id;

        public Oid() {
        }

        public Oid(String s) {
            id = Long.parseLong(justTheId(s));
        }

        public String toString() {
            return this.getClass().getName() + ":" + id;
        }

        public int hashCode() {
            return (int) id;
        }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid that = (Oid) other;
                return that.id == this.id;
            }
            return false;
        }

        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }
    }
}
