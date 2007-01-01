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

package org.apache.jdo.impl.jdoql;

import java.util.Collection;

import javax.jdo.Extent;
import javax.jdo.Query;

import org.apache.jdo.impl.jdoql.tree.Tree;
import org.apache.jdo.jdoql.JDOQLQueryFactory;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.pm.PersistenceManagerInternal;

/**
 * Implements the @link{QueryFactory} interface 
 * in order to implement a component which is capable 
 * to run together with JDO runtime.
 * 
 * @author Michael Watzek
 */
public class JDOQLQueryFactoryImpl implements JDOQLQueryFactory 
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
    public QueryTree newTree()
    {
        return new Tree();
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal)
     */
    public Query newQuery(PersistenceManagerInternal pm) 
    {
        return new QueryImpl(pm);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, java.lang.Object)
     */
    public Query newQuery(PersistenceManagerInternal pm, Object compiled) 
    {
        return new QueryImpl(pm, compiled);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, java.lang.Class)
     */
    public Query newQuery(PersistenceManagerInternal pm, Class cls) 
    {
        return new QueryImpl(pm, cls);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, javax.jdo.Extent)
     */
    public Query newQuery(PersistenceManagerInternal pm, Extent cln) 
    {
        return new QueryImpl(pm, cln);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, java.lang.Class, java.util.Collection)
     */
    public Query newQuery(PersistenceManagerInternal pm, Class cls, Collection cln) 
    {
        return new QueryImpl(pm, cls, cln);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, java.lang.Class, java.lang.String)
     */
    public Query newQuery(PersistenceManagerInternal pm, Class cls, String filter) 
    {
        return new QueryImpl(pm, cls, filter);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, java.lang.Class, java.util.Collection, java.lang.String)
     */
    public Query newQuery(PersistenceManagerInternal pm, Class cls, Collection cln, String filter) 
    {
        return new QueryImpl(pm, cls, cln, filter);
    }

    /*
     * @see org.apache.jdo.jdoql.QueryFactory#newQuery(org.apache.jdo.pm.PersistenceManagerInternal, javax.jdo.Extent, java.lang.String)
     */
    public Query newQuery(PersistenceManagerInternal pm, Extent cln, String filter) 
    {
        return new QueryImpl(pm, cln, filter);
    }
}
