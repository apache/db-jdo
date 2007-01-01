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

package org.apache.jdo.jdoql;

import java.util.Collection;

import javax.jdo.Extent;
import javax.jdo.Query;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.jdoql.tree.QueryTree;

/**
 * This interface allows for choosing between different
 * JDO query implementations. 
 * Query implementations implementing this interface
 * are capable to run together with other components.
 * The interface declares methods creating JDO query instances
 * and JDO query tree instances.
 * All methods creating query instances take persistence manager
 * instances as parameters.
 * 
 * @author Michael Watzek
 */
public interface JDOQLQueryFactory 
{
    /** 
     * Returns a new QueryTree instance. This instance allows to specify a 
     * query with an API (see {@link org.apache.jdo.jdoql.tree.QueryTree} and 
     * {@link org.apache.jdo.jdoql.tree.ExpressionFactory}) rather than as
     * JDOQL strings. To run you create a query object from the QueryTree (see 
     * {@link javax.jdo.PersistenceManager#newQuery(Object compiled)}) 
     * and call the execute method on the Query object.
     * @return new QueryTree instance.
     */
    QueryTree newTree();
    
    /** 
     * Creates a new <code>Query</code> with no elements.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm);
    
    /** 
     * Creates a new <code>Query</code> using elements from another 
     * <code>Query</code>. The other <code>Query</code> must have been created
     * by the same JDO implementation. It might be active in a different
     * <code>PersistenceManager</code> or might have been serialized and
     * restored. 
     * <P>All of the settings of the other <code>Query</code> are copied to
     * this <code>Query</code>, except for the candidate
     * <code>Collection</code> or <code>Extent</code>. 
     * @return the new <code>Query</code>.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param compiled another <code>Query</code> from the same JDO
     * implementation. 
     */
    Query newQuery(PersistenceManagerInternal pm, Object compiled);
    
    /** 
     * Creates a new <code>Query</code> specifying the <code>Class</code> of
     * the candidate instances. 
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param cls the <code>Class</code> of the candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Class cls);
    
    /** 
     * Creates a new <code>Query</code> with the <code>Class</code> of the
     * candidate instances and candidate <code>Extent</code>.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence 
     * manager is valid.
     * @param cln the <code>Extent</code> of candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Extent cln);
    
    /** 
     * Creates a new <code>Query</code> with the candidate <code>Class</code> 
     * and <code>Collection</code>.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param cls the <code>Class</code> of results.
     * @param cln the <code>Collection</code> of candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Class cls, Collection cln);
    
    /** 
     * Creates a new <code>Query</code> with the <code>Class</code> of the
     * candidate instances and filter.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param cls the <code>Class</code> of results.
     * @param filter the filter for candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Class cls, String filter);
    
    /** 
     * Creates a new <code>Query</code> with the <code>Class</code> of the
     * candidate instances, candidate <code>Collection</code>, and filter.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param cls the <code>Class</code> of candidate instances.
     * @param cln the <code>Collection</code> of candidate instances.
     * @param filter the filter for candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Class cls, Collection cln, 
                   String filter);
    
    /** 
     * Creates a new <code>Query</code> with the
     * candidate <code>Extent</code> and filter; the class
     * is taken from the <code>Extent</code>.
     * @param pm the persistence manager for the new query. 
     * It is the responsibility of the caller to check that the persistence
     * manager is valid. 
     * @param cln the <code>Extent</code> of candidate instances.
     * @param filter the filter for candidate instances.
     * @return the new <code>Query</code>.
     */
    Query newQuery(PersistenceManagerInternal pm, Extent cln, String filter);
}
