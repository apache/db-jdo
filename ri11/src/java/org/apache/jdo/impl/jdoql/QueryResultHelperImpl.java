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
 * QueryResultHelperImpl.java
 *
 * Created on August 31, 2001
 */

package org.apache.jdo.impl.jdoql;

import java.util.*;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOHelper;
import javax.jdo.JDOUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLAST;
import org.apache.jdo.impl.jdoql.scope.ParameterTable;
import org.apache.jdo.impl.jdoql.scope.VariableTable;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.jdoql.tree.TreeWalker;
import org.apache.jdo.jdoql.tree.ValueTable;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.query.QueryResultHelper;
import org.apache.jdo.util.I18NHelper;

/** 
 * This class implements the helper interface to support the query 
 * execution strategy of the StoreManager.   When a query is executed, 
 * the filter is parsed.  The parsed query, candidate collection or extent,
 * and actual parameters of the execute are stored in the
 * QueryResultHelper.
 *
 * @author Michael Bouschen
 */
public class QueryResultHelperImpl
    implements QueryResultHelper 
{
    /** The query PM */
    private PersistenceManagerInternal pm;

    /** The query tree. */
    private QueryTree queryTree;

    /** The candidates for this query evalutaion. */
    private Object candidates;

    /** The table of parameter values. */
    private ParameterTable parameters;

    /** The table of variable values. */
    private VariableTable variables;

    /** The memory query evaluator. */
    private MemoryQuery eval;

    /** The tree walker used when applying the filter and do the ordering. */
    private TreeWalker treeWalker;

    /** The candidate class taken from the queryTree. */
    private Class candidateClass;

    /** The ordering comparator. */
    private OrderingComparator ordering;
    
    /** Logger */
    private static Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.jdoql"); //NOI18N

    /** I18N support */
    protected final static I18NHelper msg =  
        I18NHelper.getInstance(QueryResultHelperImpl.class);
 
    /**
     *
     */
    public QueryResultHelperImpl(PersistenceManagerInternal pm,
                                 QueryTree queryTree, Object candidates, 
                                 ParameterTable parameters, VariableTable variables)
    {
        this.pm = pm;
        this.queryTree = queryTree;
        this.candidates = candidates;
        this.parameters = parameters;
        this.variables = variables;
        this.candidateClass = queryTree.getCandidateClass();
        this.eval = new MemoryQuery(pm, parameters, variables);
        this.treeWalker = new TreeWalker();
        this.ordering = new OrderingComparator(treeWalker, eval);
        if (logger.isTraceEnabled()) {
            String repr = null;
            if (queryTree == null)
                repr = "null"; //NOI18N
            else if (queryTree instanceof JDOQLAST)
                repr = ((JDOQLAST)queryTree).treeToString();
            else
                repr = queryTree.toString();
            logger.trace("QueryTree\n" + repr); //NOI18N
        }
    }

    /** Return the candidate Collection or Extent specified by
     * the user.
     * @return the candidate Collection or Extent.
     */
    public Object getCandidates()
    {
        return candidates;
    }
        
    /** This method filters the specified collection, removing all elements that
     * are not assignment compatible to the candidate Class specified by the 
     * user, and then orders the results according to the ordering expression 
     * specified by the user.  A new List is returned.
     * @param candidates the collection of instances to be filtered and ordered
     * @return the filtered parameter collection ordered by the ordering 
     * expression.
     */    
    public List orderCandidates(Collection candidates)
    {
        List list = new ArrayList();
        for (Iterator i = candidates.iterator(); i.hasNext();) {
            Object next = i.next();
            if (candidateClass.isInstance(next))
                list.add(next);
        }
        ordering.setQueryTree(queryTree);
        Collections.sort(list, ordering);
        if (logger.isTraceEnabled())
            logger.trace("orderCandidates -> " + list); //NOI18N
        return list;
    }
    
    /** This method determines whether the specified object is assignment 
     * compatible to the candidate Class specified by the user and satisfies 
     * the query filter.
     * @return <CODE>true</CODE> if the specified object is of the candidate 
     * class and satisfies the query filter; <CODE>false otherwise</CODE>
     * @param obj the candidate object.
     */    
    public boolean applyFilter (Object obj)
    {
        boolean satisfies = false;
        if (candidateClass.isInstance(obj)) {
            // check whether obj is bound to the same pm as the one from the query
            checkPM(pm, obj);
            eval.setCurrent(obj);
            Object result = treeWalker.walk(queryTree.getFilter(), eval);
            satisfies = ((result instanceof Boolean) && ((Boolean)result).booleanValue());
        }
        if (logger.isTraceEnabled())
            logger.trace("applyFilter -> " + satisfies + "\t" + obj); //NOI18N
        return satisfies;
    }
    
    /** Return the query tree which is either specified by the user or compiled 
     * from a JDOQL query.
     * @return the query tree
     */
    public QueryTree getQueryTree()
    {
        return queryTree;
    }
    
    /** This method returns the parameter values passed by the user
     * in the execute(...) method.
     * @return a ValueTable representing the parameter values
     */
    public ValueTable getParameterValues()
    {
        return parameters;
    }

    //========= Helper method ==========

    /**
     * Checks the PersistenceManager of the specified value to be 
     * identical to the one from the query instance.
     */
    public static void checkPM(PersistenceManagerInternal queryPM, Object value)
    {
        if (value instanceof Collection) {
            // iterate collection and call checkPM for all elements
            for (Iterator i = ((Collection)value).iterator(); i.hasNext();)
                checkPM(queryPM, i.next());
        }
        else if (value instanceof Object[]) {
            // iterate array and call checkPM for all elements
            // No need to check array of primitive types
            Object[] array = (Object[])value;
            for (int i = 0; i < array.length; i++)
                checkPM(queryPM, array[i]);
        }
        else
        {
            PersistenceManager instancePM = 
                JDOHelper.getPersistenceManager(value);
            if ((instancePM != null) && !instancePM.equals(queryPM)) {
                // instancePM is NOT the one from the query => exception
                throw new JDOUserException(
                    msg.msg("EXC_InstanceBoundToDifferentPM", value)); //NOI18N
            }
        }
    }
        
}

