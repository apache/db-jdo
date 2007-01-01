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

/*
 * QueryResultHelper.java
 *
 * Created on March 18, 2001, 12:35 PM
 */

package org.apache.jdo.query;

import java.util.Collection;
import java.util.List;
import javax.jdo.Extent;

//brazil: import org.apache.jdo.jdoql.tree.QueryTree;
//brazil: import org.apache.jdo.jdoql.tree.ValueTable;


/** This interface is a helper for the query execution strategy
 * of the StoreManager.   When a query is executed, the filter
 * is parsed.  The parsed query, candidate collection or extent,
 * and actual parameters of the execute are stored in the
 * QueryResultHelper.
 * This interface also provides methods useful for ordering
 * the candidate objects and for filtering objects.
 * @author Craig Russell
 * @version 1.0
 */
public interface QueryResultHelper {

    /** Return the candidate Collection or Extent specified by
     * the user.
     * @return the candidate Collection or Extent.
     */
    Object getCandidates();
        
    /** This method filters the specified collection, removing all elements that
     * are not assignment compatible to the candidate Class specified by the 
     * user, and then orders the results according to the ordering expression 
     * specified by the user.  A new List is returned.
     * @param candidates the collection of instances to be filtered and ordered
     * @return the filtered parameter collection ordered by the ordering 
     * expression.
     */    
    List orderCandidates(Collection candidates);
    
    /** This method determines whether the specified object is assignment 
     * compatible to the candidate Class specified by the user and satisfies 
     * the query filter.
     * @return <CODE>true</CODE> if the specified object is of the candidate 
     * class and satisfies the query filter; <CODE>false otherwise</CODE>
     * @param obj the candidate object.
     */    
    boolean applyFilter(Object obj);
    
    /** Return the query tree which is either specified by the user or compiled 
     * from a JDOQL query.
     * @return the query tree
     *
     */
    //brazil: QueryTree getQueryTree();
    
    /** This method returns the parameter values passed by the user
     * in the execute(...) method.
     * @return a ValueTable representing the parameter values
     */
    //brazil: ValueTable getParameterValues();
}

