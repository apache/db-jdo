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

package org.apache.jdo.impl.fostore;

import java.util.Collection;

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.Query;

import org.apache.jdo.jdoql.JDOQLQueryFactory;

import org.apache.jdo.impl.pm.PersistenceManagerImpl;

/**
 * Subclass of {@link #PersistenceManagerImpl} implementing
 * abstract methods <code>newQuery</code>.
 *
 * @author Michael Watzek
 */
public class FOStorePM extends PersistenceManagerImpl 
{
    /** The JDOQLQueryFactory. */
    private JDOQLQueryFactory jdoqlQueryFactory;

    /**
     * Constructs new instance of PersistenceManagerImpl for this
     * PersistenceManagerFactoryInternal and particular combination of 
     * username and password.
     * @param pmf calling PersistenceManagerFactory as
     * PersistenceManagerFactoryInternal 
     * @param username user name used for accessing Connector or null if none
     * is provided.
     * @param password user password used for accessing Connector or null if 
     * none is provided.
     */
    FOStorePM(FOStorePMF pmf, String username, String password) 
    {
        super(pmf, username, password);
        this.jdoqlQueryFactory = pmf.getJDOQLQueryFactory();
    }
    
    /** Create a new Query with no elements.
     * @return a new Query instance with no elements.
     */  
     public Query newQuery() 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this);
     }

     /** Create a new Query using elements from another Query.  The other Query
     * must have been created by the same JDO implementation.  It might be active
     * in a different PersistenceManager or might have been serialized and
     * restored.
     * @return the new Query
     * @param compiled another Query from the same JDO implementation
     */  
     public Query newQuery(Object compiled) 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this, compiled);
     }

    /** Create a Construct a new query instance using the specified String 
     * as the single-string representation of the query.
     * @param query the single-string query
     * @return the new <code>Query</code>
     * @since 2.0
     */
    public Query newQuery (String query) {
        throw new UnsupportedOperationException(
            "Method newQuery(String) not yet implemented");
    }
    
     /** Create a new Query using the specified language.
      * @param language the language of the query parameter
      * @param query the query, which is of a form determined by the language
      * @return the new Query
      */    
     public Query newQuery(String language, Object query) 
     {
         assertIsOpen();
         if ("javax.jdo.query.JDOQL".equals(language)) //NOI18N
             return this.jdoqlQueryFactory.newQuery(this, query);
         throw new JDOUserException(msg.msg(
                 "EXC_UnsupportedQueryLanguage", language)); // NOI18N
     }
     
     /** Create a new Query specifying the Class of the results.
     * @param cls the Class of the results
     * @return the new Query
     */
     public Query newQuery(Class cls) 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this, cls);
     }

     /** Create a new Query with the candidate Extent; the class is taken
      * from the Extent.
      * @return the new Query
      * @param cln the Extent of candidate instances */  
     public Query newQuery(Extent cln) 
     {
           assertIsOpen();
           return this.jdoqlQueryFactory.newQuery(this, cln);
     }

     /** Create a new Query with the Class of the results and candidate 
      * Collection.
     * @param cls the Class of results
     * @param cln the Collection of candidate instances
     * @return the new Query
     */
     public Query newQuery(Class cls, Collection cln) 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this, cls, cln);
     }

     /** Create a new Query with the Class of the results and Filter.
     * @param cls the Class of results
     * @param filter the Filter for candidate instances
     * @return the new Query
     */
     public Query newQuery(Class cls, String filter) 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this, cls, filter);
     }

     /** Create a new Query with the Class of the results, candidate Collection,
     * and Filter.
     * @param cls the Class of results
     * @param cln the Collection of candidate instances
     * @param filter the Filter for candidate instances
     * @return the new Query
     */
     public Query newQuery(Class cls, Collection cln, String filter) 
     {
         assertIsOpen();
         return this.jdoqlQueryFactory.newQuery(this, cls, cln, filter);
     }

     /** Create a new Query with the candidate Extent and Filter.
      * The class is taken from the Extent.
      * @return the new Query
      * @param cln the Extent of candidate instances
      * @param filter the Filter for candidate instances */  
     public Query newQuery(Extent cln, String filter) 
     {
           assertIsOpen();
           return this.jdoqlQueryFactory.newQuery(this, cln, filter);
     }

    /**
     * Create a new <code>Query</code> with the given candidate class
     * from a named query. The query name given must be the name of a
     * query defined in metadata.
     * @param cls the <code>Class</code> of candidate instances
     * @param queryName the name of the query to look up in metadata
     * @return the new <code>Query</code>
     */
    public Query newNamedQuery (Class cls, String queryName) {
        throw new UnsupportedOperationException(
            "Method newNamedQuery(Class, String) not yet implemented");
    }
}
