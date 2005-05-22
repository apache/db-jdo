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

/*
 * BasicQueryResult.java
 *
 * Created on March 18, 2001, 12:48 PM
 */

package org.apache.jdo.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;


import javax.jdo.*;
import javax.jdo.spi.I18NHelper;


/** This class implements the basic query execution
 * strategy.  It first orders the candidate set
 * using the query helper orderCandidates method.
 * It then iterates over the ordered candidate
 * collection, filtering each one, 
 * using the query helper applyFilter method and removing the
 * objects that fail the filter.
 * All of the methods of Collection are then delegated
 * to the result collection.
 *
 * @author Craig Russell
 * @version 1.0
 */
public class BasicQueryResult extends Object implements QueryResult {

    /** The result after filtering the candidates for the
     * proper class, ordering the results, and filtering
     * candidates for the user-defined filter.
     */    
    final List result;
    
    /** The open iterators against the query results.
     */    
    HashSet iterators;
    
    /** The flag indicating whether the query result is
     * closed.  If the query result is closed, no further
     * operations can be done on it.
     */    
    boolean closed;
    
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance(BasicQueryResult.class);

    /** Creates new BasicQueryResult
     * @param qrh the query result helper provided
     * by the query parser.
     */
    public BasicQueryResult(QueryResultHelper qrh) {
        iterators = new HashSet();
        Collection candidates;
        // getCandidates returns either a Collection or an Extent
        Object possibles = qrh.getCandidates();
        if (possibles instanceof Collection) {
            candidates = (Collection) possibles;
        } 
        else {
            // iterate the Extent to create the candidate Collection
            candidates = new ArrayList();
            Iterator it = ((Extent) possibles).iterator();
                while (it.hasNext()) {
                    candidates.add (it.next());
                }
        }
        // order the candidates according to the user's request
        result = qrh.orderCandidates(candidates);
        Iterator it = result.iterator();
        while (it.hasNext()) {
            if (! qrh.applyFilter (it.next())) {
                // remove non-qualifying elements and we are done.
                it.remove();
            }
        }
    }
    
    /** This method closes the result set, closes all open iterators, and releases
     * all resources that might be held by the result.
     */    
    public void close() {
        closed = true;
        for (Iterator qrii = iterators.iterator(); qrii.hasNext(); ) {
           ((QueryResultIterator) qrii.next()).close();
        }
        iterators = null;
        result.clear();
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     * @param collection ignored.
     * @return never.
     */    
    public boolean retainAll(java.util.Collection collection) {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }    

    /** This method delegates to the result collection method.
     * @param obj the object to be tested.
     * @return <CODE>true</CODE> if the result collection contains
     * the parameter object.
     */    
    public boolean contains(java.lang.Object obj) {
        return result.contains (obj);
    }
    
    /** This method delegates to the result collection method.
     * @return an array of all objects in the result
     * collection that are of the runtime
     * type of the parameter array.
     * @param obj an array into which to place the
     * objects from the result collection.
     */    
    public java.lang.Object[] toArray(java.lang.Object[] obj) {
        return result.toArray (obj);
    }

    /** This method delegates to the result collection method.
     * @return an iterator over the result collection.
     */    
    public java.util.Iterator iterator() {
        QueryResultIterator i = new BasicQueryResultIterator (result.iterator());
        if (closed)
            i.close();
        else
            iterators.add(i);
        return i;
    }
    
    /** This method delegates to the result collection method.
     * @return the result collection as an array.
     */    
    public java.lang.Object[] toArray() {
        return result.toArray();
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     * @param collection ignored.
     * @return never.
     */    
    public boolean removeAll(java.util.Collection collection) {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     * @param obj ignored.
     * @return never.
     */    
    public boolean remove(java.lang.Object obj) {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     */    
    public void clear() {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     * @param collection ignored.
     * @return never.
     */    
    public boolean addAll(java.util.Collection collection) {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }
    
    /** This method delegates to the result collection method.
     * @return the size of the results.
     */    
    public int size() {
        return result.size();
    }
    
    /** This method delegates to the result collection method.
     * @return <CODE>true</CODE> if the result collection contains all of the
     * elements in the parameter collection.
     * @param collection a collection of elements to be tested.
     */    
    public boolean containsAll(java.util.Collection collection) {
        return result.containsAll (collection);
    }
    
    /** This method throws UnsupportedOperationException because
     * the collection is read-only.
     * @param obj ignored.
     * @return never.
     */    
    public boolean add(java.lang.Object obj) {
        throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
    }
    
    /** This method delegates to the result collection method.
     * @return <CODE>true</CODE> if the result collection is empty.
     */    
    public boolean isEmpty() {
        return result.isEmpty();
    }
    
    /** This method delegates to the result collection method.
     * @return <CODE>true</CODE> if the result collection is equal to the parameter 
     * collection.
     * @param obj the object to which to compare this object.
     */    
    public boolean equals (Object obj) {
        return result.equals (obj);
    }
    
    /** This method delegates to the result collection method.
     * @return the hashCode of the result collection.
     */    
    public int hashCode() {
        return result.hashCode();
    }
    /** The internal query result iterator supports all
     * iterator methods plus close, allowing early release
     * of resources.
     */    
    public class BasicQueryResultIterator extends Object implements QueryResultIterator {
        
        /** The internal iterator over the query results.
         */        
        Iterator internalIterator;
        
        /** The flag indicating whether the query result is
         * closed.  If the query result is closed, no further
         * operations can be done on it.
         */    
        boolean closed;
        
        /** Construct a new query result iterator given the
         * iterator over the results.
         * @param it The iterator over the results of the query.
         */        
        private BasicQueryResultIterator (Iterator it) {
            internalIterator = it;
            closed = false;
        }
        
        /** Return true if this query result iterator has not been
         * closed and the internal iterator has more elements.
         * @return true if there are more elements.
         */        
        public boolean hasNext() {
            if (isClosed()) return false;
            return internalIterator.hasNext();
        }
        
        /** Advance and return the next element of the iterator.
         * @return the next element of the iterator.
         */        
        public java.lang.Object next() {
            if (isClosed()) throw new NoSuchElementException();
            return internalIterator.next();
        }
        
        /** Close this iterator and release any resources held.  After
         * this method completes, the iterator will return false to
         * hasNext(), and will throw NoSuchElementException to next().
         */
        public void close() {
            // allow iterator to be garbage collected
            internalIterator = null;
            closed = true;
        }
        
        /** Throw an exception.  This iterator is read-only.
         */        
        public void remove() {
            throw new UnsupportedOperationException (msg.msg("EXC_ReadOnly")); //NOI18N
        }
        
        /** Return true if the user has closed this iterator.
         * @return true if the user has closed this iterator.
         */        
        public boolean isClosed() {
            return closed;
        }
        
    }
}
