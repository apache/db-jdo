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
 * QueryImpl.java
 *
 * Created on August 31, 2001
 */

package org.apache.jdo.impl.jdoql;

import java.io.IOException;
import java.util.*;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Extent;
import javax.jdo.Transaction;
import javax.jdo.FetchPlan;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLAST;
import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLC;
import org.apache.jdo.impl.jdoql.scope.ParameterTable;
import org.apache.jdo.impl.jdoql.scope.VariableTable;
import org.apache.jdo.impl.jdoql.tree.Tree;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.query.QueryResult;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.store.StoreManager;
import org.apache.jdo.util.I18NHelper;


/** 
 * This class implements the JDO query interface 
 * (see {@link javax.jdo.Query}).
 *
 * @author  Michael Bouschen
 */
public class QueryImpl
    implements Query
{
    /** The pm for this query instance. */
    private transient PersistenceManagerInternal pm;

    /** The candidate class as specified by setClass. */
    private transient Class candidateClass;

    /** The name of the candidate class (Needed for serialization support). */
    private String candidateClassName;

    /** The candidates as specified by setCandidates. */
    private transient Object candidates;
    
    /** The import declaration string as specified by declareImports. */
    private String importDeclarations;
    
    /** The parameter declaration string as specified by declareParameters. */
    private String parameterDeclarations;
    
    /** The variable declaration string as specified by declareVariables. */
    private String variableDeclarations;
    
    /** The ordering specification string as specified by setOrdering. */
    private String orderingSpecification;

    /** The filter expression string as specified by setFilter. */
    private String filterExpression;
    
    /** 
     * The ignoreCache flag as specified by setIgnoreCache. The constructor
     * defaults it to the PM setting.
     */
    private boolean ignoreCache;

    /** The internal query representation as compilation result. */
    private transient Tree compiledTree = null;
    
    /** 
     * The internal query representation set by the constructor taking a 
     * compiled query.
     */
    private Tree queryTree;

    /** Represents parameter values. */
    private ParameterTable paramtab;

    /** Represents variable values. */
    private VariableTable vartab;

    /** Set of open query result instances. */
    private transient Set openQueryResults = new HashSet();
    
    /** I18N support */
    private final static I18NHelper msg =  
        I18NHelper.getInstance(QueryImpl.class);

    /**
     * Create an empty query instance with no elements.
     */
    public QueryImpl(PersistenceManagerInternal pm)
    {
        // check valid PersistenceManager
        if (pm == null)
            throw new JDOQueryException(msg.msg("EXC_UnboundQuery")); //NOI18N

        this.pm = pm;
        this.ignoreCache = pm.getIgnoreCache();
        this.paramtab = new ParameterTable();
        this.vartab = new VariableTable();
    }
    
    /** 
     * Create a new Query using elements from another Query.  The other Query
     * must have been created by the same JDO implementation.  It might be active
     * in a different PersistenceManager or might have been serialized and
     * restored.
     * <P>All of the settings of the other Query are copied to this Query,
     * except for the candidate Collection or Extent.
     * @param compiled another Query from the same JDO implementation
     */
    public QueryImpl(PersistenceManagerInternal pm, Object compiled)
    {
        this(pm);
        if (compiled == null) {
            throw new JDOQueryException(
                msg.msg("EXC_NullQueryInstance")); //NOI18N
        }
        
        if (compiled instanceof QueryImpl) {
            QueryImpl other = (QueryImpl)compiled;
            if (other.candidateClass != null) {
                this.candidateClass = other.candidateClass;
            }
            else if (other.candidateClassName != null) {
                // Support for deserialized query instances: 
                // after deserialization other.candidateClass is not set,
                // but other.candidateClassName is set => 
                // use PM algorithm to calculate the candidateClass instance
                try {
                    this.candidateClass = 
                        pm.loadClass(other.candidateClassName, null);
                }
                catch (ClassNotFoundException ex) {
                    throw new JDOQueryException(
                        msg.msg("EXC_UnknownCandidateClass", //NOI18N
                                other.candidateClassName), ex);
                }
            }
            this.importDeclarations = other.importDeclarations;
            this.parameterDeclarations = other.parameterDeclarations;
            this.variableDeclarations = other.variableDeclarations;
            this.orderingSpecification = other.orderingSpecification;
            this.filterExpression = other.filterExpression;
            this.ignoreCache = other.ignoreCache;
            // Set compiled tree to null in order to ensure that this Query 
            // instance is compiled prior to execution.
            // A possible optimization might want to reuse compilation results 
            // from the other query, but this requires serializing the typeInfo
            // from the JDOQLAST.
            this.compiledTree = null;
            this.queryTree = other.queryTree;
            this.paramtab = other.paramtab;
            this.vartab = other.vartab;
            checkQueryTreeCandidateClass();
        }
        else if (compiled instanceof Tree) {
            this.queryTree = (Tree)compiled;
            // TBD: check compiledTree?
            this.compiledTree = null;
            checkQueryTreeCandidateClass();
        }
        else {
            throw new JDOQueryException(
                msg.msg("EXC_InvalidCompiledQuery", //NOI18N
                        compiled.getClass().getName()));
        }
    }

    /** 
     * Create a new Query specifying the Class of the candidate instances.
     * @param cls the Class of the candidate instances
     */
    public QueryImpl(PersistenceManagerInternal pm, Class cls)
    {
        this(pm);
        setClass(cls);
    }
    
    /** 
     * Create a new Query with the candidate Extent; the class 
     * is taken from the Extent.
     * @param cln the Extent of candidate instances
     */
    public QueryImpl(PersistenceManagerInternal pm, Extent cln)
    {
        this(pm);
        setClass(cln.getCandidateClass());
        setCandidates(cln);
    }
    
    /**
     * Create a query instance with the candidate class and 
     * candidate collection specified.
     * @param cls the Class of the candidate instances.
     * @param cln the Collection of candidate instances.
     */
    public QueryImpl(PersistenceManagerInternal pm, Class cls, Collection cln)
    {
        this(pm);
        setClass(cls);
        setCandidates(cln);
    }
    
    /** 
     * Create a new Query with the Class of the candidate instances and Filter.
     * @param cls the Class of results
     * @param filter the filter for candidate instances
     */
    public QueryImpl(PersistenceManagerInternal pm, Class cls, String filter)
    {
        this(pm);
        setClass(cls);
        setFilter(filter);
    }
        
    /** 
     * Create a new Query with the Class of the candidate instances, 
     * candidate Collection, and filter.
     * @param cls the Class of candidate instances
     * @param cln the Collection of candidate instances
     * @param filter the filter for candidate instances
     */
    public QueryImpl(PersistenceManagerInternal pm, Class cls, Collection cln, 
                     String filter)
    {
        this(pm);
        setClass(cls);
        setCandidates(cln);
        setFilter(filter);
    }
    
    /** 
     * Create a new Query with the
     * candidate Extent and filter; the class
     * is taken from the Extent.
     * @param cln the Extent of candidate instances
     * @param filter the filter for candidate instances
     */
    public QueryImpl(PersistenceManagerInternal pm, Extent cln, String filter)
    {
        this(pm);
        setClass(cln.getCandidateClass());
        setCandidates(cln);
        setFilter(filter);
    }

    /** Set the class of the candidate instances of the query.
     * <P>The class specifies the class
     * of the candidates of the query.  Elements of the candidate collection
     * that are of the specified class are filtered before being
     * put into the result Collection.
     *
     * @param cls the Class of the candidate instances.
     */
    public void setClass(Class cls)
    {
        synchronized (this.paramtab) {
            this.candidateClass = cls;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Set the candidate Extent to query.
     * @param pcs the Candidate Extent.
     */
    public void setCandidates(Extent pcs)
    {
        synchronized (this.paramtab) {
            this.candidates = pcs;
        }
    }
    
    /** 
     * Set the candidate Collection to query.
     * @param pcs the Candidate collection.
     */
    public void setCandidates(Collection pcs)
    {
        synchronized (this.paramtab) {
            this.candidates = pcs;
        }
    }
    
    /** 
     * Set the filter for the query.
     * @param filter the query filter.
     */
    public void setFilter(String filter)
    {
        synchronized (this.paramtab) {
            this.filterExpression = filter;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Set the import statements to be used to identify the fully qualified name 
     * of variables or parameters.  Parameters and unbound variables might 
     * come from a different class from the candidate class, and the names 
     * need to be declared in an import statement to eliminate ambiguity. 
     * Import statements are specified as a String with semicolon-separated 
     * statements. 
     * <P>The String parameter to this method follows the syntax of the  
     * import statement of the Java language.
     * @param imports import statements separated by semicolons.
     */
    public void declareImports(String imports)
    {
        synchronized (this.paramtab) {
            this.importDeclarations = imports;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Declare the list of parameters query execution.
     *
     * The parameter declaration is a String containing one or more query 
     * parameter declarations separated with commas. Each parameter named 
     * in the parameter declaration must be bound to a value when 
     * the query is executed.
     * <P>The String parameter to this method follows the syntax for formal 
     * parameters in the Java language. 
     * @param parameters the list of parameters separated by commas.
     */
    public void declareParameters(String parameters)
    {
        synchronized (this.paramtab) {
            this.parameterDeclarations = parameters;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Declare the unbound variables to be used in the query. Variables 
     * might be used in the filter, and these variables must be declared 
     * with their type. The unbound variable declaration is a String 
     * containing one or more unbound variable declarations separated 
     * with semicolons. It follows the syntax for local variables in 
     * the Java language.
     * @param variables the variables separated by semicolons.
     */
    public void declareVariables(String variables)
    {
        synchronized (this.paramtab) {
            this.variableDeclarations = variables;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Set the ordering specification for the result Collection.  The
     * ordering specification is a String containing one or more ordering
     * declarations separated by commas.
     *
     * <P>Each ordering declaration is the name of the field on which
     * to order the results followed by one of the following words:
     * "ascending" or "descending".
     *
     *<P>The field must be declared in the candidate class or must be
     * a navigation expression starting with a field in the candidate class.
     *
     *<P>Valid field types are primitive types except boolean; wrapper types 
     * except Boolean; BigDecimal; BigInteger; String; and Date.
     * @param ordering the ordering specification.
     */
    public void setOrdering(String ordering)
    {
        synchronized (this.paramtab) {
            this.orderingSpecification = ordering;
            this.compiledTree = null;
        }
    }
    
    /** 
     * Set the ignoreCache option.  The default value for this option was
     * set by the PersistenceManagerFactory or the PersistenceManager used 
     * to create this Query.
     *
     * The ignoreCache option setting specifies whether the query should execute
     * entirely in the back end, instead of in the cache.  If this flag is set
     * to true, an implementation might be able to optimize the query
     * execution by ignoring changed values in the cache.  For optimistic
     * transactions, this can dramatically improve query response times.
     * @param ignoreCache the setting of the ignoreCache option.
     */
    public void setIgnoreCache(boolean ignoreCache)
    {
        synchronized (this.paramtab) {
            this.ignoreCache = ignoreCache;
        }
    }
    
    /** 
     * Get the ignoreCache option setting.
     * @return the ignoreCache option setting.
     * @see #setIgnoreCache
     */
    public boolean getIgnoreCache()
    {
        return ignoreCache;
    }
    
    /** 
     * Verify the elements of the query and provide a hint to the query to
     * prepare and optimize an execution plan.
     */
    public void compile()
    {
        // check valid PersistenceManager
        if (pm == null)
            throw new JDOQueryException(msg.msg("EXC_UnboundQuery")); //NOI18N
        
        synchronized (this.paramtab) {
            if (this.compiledTree == null) {
                JDOQLC jdoqlc = new JDOQLC(pm);
                if (queryTree != null) {
                    jdoqlc.setQueryTree(queryTree);
                }
                else {
                    // define the query parts including syntax checks
                    jdoqlc.setClass(candidateClass);
                    jdoqlc.declareImports(importDeclarations);
                    jdoqlc.declareParameters(parameterDeclarations);
                    jdoqlc.declareVariables(variableDeclarations);
                    jdoqlc.setFilter(filterExpression);
                    jdoqlc.setOrdering(orderingSpecification);
                }

                // call semantic analysis
                JDOQLAST ast = jdoqlc.semanticCheck(paramtab, vartab);
                // call optimizer
                compiledTree = (Tree)jdoqlc.optimize(ast);
            }
        }
    }
    
    /** 
     * Execute the query and return the filtered Collection.
     * @return the filtered Collection.
     * @see #executeWithArray(Object[] parameters)
     */
    public Object execute()
    {
        synchronized (this.paramtab) {
            compile();
            pm.assertIsOpen();
            checkTransaction();
            checkCandidates();
            // Each execute call gets its own copy of paramtab and vartab.
            // This allows multiple execution of the same query in parallel.
            ParameterTable params = paramtab.getCopy();
            params.initValueHandling();
            params.checkUnboundParams();
            VariableTable vars = vartab.getCopy();
            vars.initValueHandling();
            StoreManager srm = pm.getStoreManager();
            QueryResult queryResult = 
                srm.newQueryResult(new QueryResultHelperImpl(
                    pm, compiledTree, candidates, params, vars));
            openQueryResults.add(queryResult);
            return queryResult;
        }
    }
    
    /** 
     *  Execute the query and return the filtered Collection.
     * @return the filtered Collection.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     */
    public Object execute(Object p1)
    {
        Object[] params = new Object[1];
        params[0] = p1;
        return executeWithArray(params);
    }
    
    /** 
     * Execute the query and return the filtered Collection.
     * @return the filtered Collection.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     */
    public Object execute(Object p1, Object p2)
    {
        Object[] params = new Object[2];
        params[0] = p1;
        params[1] = p2;
        return executeWithArray(params);
    }
    
    /** 
     * Execute the query and return the filtered Collection.
     * @return the filtered Collection.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     * @param p3 the value of the third parameter declared.
     */
    public Object execute(Object p1, Object p2, Object p3)
    {
        Object[] params = new Object[3];
        params[0] = p1;
        params[1] = p2;
        params[2] = p3;
        return executeWithArray(params);
    }
    
    /** 
     * Execute the query and return the filtered Collection.
     * @return the filtered Collection.
     * @see #executeWithArray(Object[] parameters)
     * @param parameters the Map containing all of the parameters.
     */
    public Object executeWithMap(Map parameters)
    {
        synchronized (this.paramtab)
        {
            compile();
            pm.assertIsOpen();
            checkTransaction();
            checkCandidates();
            // Each execute call gets its own copy of paramtab and vartab.
            // This allows multiple execution of the same query in parallel.
            ParameterTable params = paramtab.getCopy();
            params.initValueHandling();
            params.setValues(pm, parameters);
            params.checkUnboundParams();
            VariableTable vars = vartab.getCopy();
            vars.initValueHandling();
            StoreManager srm = pm.getStoreManager();
            QueryResult queryResult = 
                srm.newQueryResult(new QueryResultHelperImpl(
                    pm, compiledTree, candidates, params, vars));
            openQueryResults.add(queryResult);
            return queryResult;
        }
    }

    /** Execute the query and return the filtered Collection.
     *
     * <P>The execution of the query obtains the values of the parameters and
     * matches them against the declared parameters in order.  The names
     * of the declared parameters are ignored.  The type of
     * the declared parameters must match the type of the passed parameters,
     * except that the passed parameters might need to be unwrapped to get
     * their primitive values.
     *
     * <P>The filter, import, declared parameters, declared variables, and
     * ordering statements are verified for consisten1cy.
     *
     * <P>Each element in the candidate Collection is examined to see that it
     * is assignment compatible to the Class of the query.  It is then evaluated
     * by the boolean expression of the filter.  The element passes the filter
     * if there exist unique values for all variables for which the filter
     * expression evaluates to true.
     * @return the filtered Collection.
     * @param parameters the Object array with all of the parameters.
     */
    public Object executeWithArray(Object[] parameters)
    {
        synchronized (this.paramtab)
        {
            compile();
            pm.assertIsOpen();
            checkTransaction();
            checkCandidates();
            // Each execute call gets its own copy of paramtab and vartab.
            // This allows multiple execution of the same query in parallel.
            ParameterTable params = paramtab.getCopy();
            params.initValueHandling();
            params.setValues(pm, parameters);
            params.checkUnboundParams();
            VariableTable vars = vartab.getCopy();
            vars.initValueHandling();
            StoreManager srm = pm.getStoreManager();
            QueryResult queryResult = 
                srm.newQueryResult(new QueryResultHelperImpl(
                    pm, compiledTree, candidates, params, vars));
            openQueryResults.add(queryResult);
            return queryResult;
        }
    }
    
    /** 
     * Get the PersistenceManager associated with this Query.
     *
     * <P>If this Query was restored from a serialized form, it has no 
     * PersistenceManager, and this method returns null.
     * @return the PersistenceManager associated with this Query.
     */
    public PersistenceManager getPersistenceManager()
    {
        return (pm == null) ? null : pm.getCurrentWrapper();
    }
  
    /** 
     * Close a query result and release any resources associated with it.  The
     * parameter is the return from execute(...) and might have iterators open 
     * on it. Iterators associated with the query result are invalidated: they 
     * return false to hasNext() and throw NoSuchElementException to next().
     * @param queryResult the result of execute(...) on this Query instance.
     */    
    public void close (Object queryResult)
    {
        if (queryResult instanceof QueryResult) {
            openQueryResults.remove(queryResult);
            ((QueryResult)queryResult).close();
        }
    }
    
    /** 
     * Close all query results associated with this Query instance, and release
     * all resources associated with them.  The query results might have 
     * iterators open on them.  Iterators associated with the query results are 
     * invalidated: they return false to hasNext() and 
     * throw NoSuchElementException to next().
     */    
    public void closeAll ()
    {
        for(Iterator i = openQueryResults.iterator(); i.hasNext();) {
            QueryResult queryResult = (QueryResult)i.next();
            queryResult.close();
        }
        openQueryResults.clear();
    }

    /**
     * Set the grouping expressions, optionally including a "having"
     * clause. When grouping is specified, each result expression
     * must either be an expression contained in the grouping, or an
     * aggregate evaluated once per group.
     * 
     * @param	group	a comma-delimited list of expressions, optionally
     * followed by the "having" keyword and a boolean expression
     * @since	2.0
     */
    public void setGrouping (String group) {
        throw new UnsupportedOperationException(
            "method setGrouping(String) is not yet implemented");
    }

    /**
     * Specify that only the first result of the query should be
     * returned, rather than a collection. The execute method will
     * return null if the query result size is 0.
     * @since 2.0
     * @param unique if true, only one element is returned
     */
    public void setUnique (boolean unique) {
        throw new UnsupportedOperationException(
            "method setUnique(boolean) is not yet implemented");
    }

    /**
     * Specifies what type of data this query should return. If this
     * is unset or set to <code>null</code>, this query returns
     * instances of the query's candidate class. If set, this query
     * will return expressions, including field values (projections) and 
     * aggregate function results.
     * @param data a comma-delimited list of expressions 
     * (fields, functions on fields, or aggregate functions) 
     * to return from this query
     * @since 2.0
     */
    public void setResult (String data) {
        throw new UnsupportedOperationException(
            "method setResult(String) is not yet implemented");
    }

    /**
     * Specify the type of object in which to return each element of
     * the result of invoking {@link #execute} or one of its siblings. 
     * If the result is not set or set to null, the result class defaults
     * to the candidate class of the query. If the result consists of one
     * expression, the result class defaults to the type of that expression.
     * If the result consists of more than one expression, the result class
     * defaults to Object[].
     * The result class may be specified to be one of the java.lang classes 
     * Character, Boolean, Byte, Short, Integer, Long, Float, Double, String, 
     * or Object[]; or one of the java.math classes BigInteger or BigDecimal; 
     * or the java.util class Date; or one of the java.sql classes Date, 
     * Time, or Timestamp; or a user-defined class.
     * <P>If there are multiple result expressions, the result class 
     * must be able to hold all elements of the result specification 
     * or a JDOUserException is thrown. 
     *<P>If there is only one result expression, the result class must be 
     * assignable from the type of the result expression or must be able 
     * to hold all elements of the result specification. A single value 
     * must be able to be coerced into the specified result class 
     * (treating wrapper classes as equivalent to their unwrapped 
     * primitive types) or by matching. If the result class does not satisfy 
     * these conditions, a JDOUserException is thrown.
     *<P>A constructor of a result class specified in the setResult method 
     * will be used if the results specification matches the parameters 
     * of the constructor by position and type. If more than one constructor 
     * satisfies the requirements, the JDO implementation chooses one of them. 
     * If no constructor satisfies the results requirements, or if the result 
     * class is specified via the setResultClass method, the following 
     * requirements apply:
     * <ul>
     * <li>A user-defined result class must have a no-args constructor and 
     * one or more public <code>set</code> or <code>put</code> methods or fields. 
     * <li>Each result expression must match one of:
     * <ul>
     * <li>a public field that matches the name of the result expression 
     * and is of the type (treating wrapper types equivalent to primitive 
     * types) of the result expression; 
     * <li>or if no public field matches the name and type, a public 
     * <code>set</code method that returns void and matches the name of the 
     * result expression and takes a single parameter which is the 
     * exact type of the result expression;
     * <li>or if neither of the above applies,a public method must be found 
     * with the signature <code>void put(Object, Object)</code>.
     * During processing of the results,  
     * the first argument is the name of the result expression and 
     * the second argument is the value from the query result.
     * </ul>
     * </ul>
     * Portable result classes do not invoke any persistence behavior 
     * during their no-args constructor or <code>set</code methods.
     * @param cls the result class
     * @since 2.0
     */
    public void setResultClass (Class cls) {
        throw new UnsupportedOperationException(
            "method setResultClass(Class) is not yet implemented");
    }

    /**
     * Set the range of results to return. The execution of the query is
     * modified to return only a subset of results. If the filter would
     * normally return 100 instances, and fromIncl is set to 50, and
     * toExcl is set to 70, then the first 50 results that would have 
     * been returned are skipped, the next 20 results are returned and the
     * remaining 30 results are ignored. An implementation should execute
     * the query such that the range algorithm is done at the data store.
     * @param fromIncl 0-based inclusive start index
     * @param toExcl 0-based exclusive end index, or 
     *     {@link Long#MAX_VALUE} for no limit.
     * @since 2.0
     */
    public void setRange (long fromIncl, long toExcl) {
        throw new UnsupportedOperationException(
            "method setRange(long. long) is not yet implemented");
    }

    /**
     * Set the range of results to return. The parameter is a String
     * containing a comma-separated fromIncl and toExcl. The fromIncl and
     * toExcl can be either String representations of long values, or can
     * be parameters identified with a leading ":". For example, 
     * <code>setRange("50, 70");</code> or 
     * <code>setRange(":from, :to");</code> or 
     * <code>setRange("50, :to");</code>.
     * The execution of the query is
     * modified to return only a subset of results. If the filter would
     * normally return 100 instances, and fromIncl is set to 50, and
     * toExcl is set to 70, then the first 50 results that would have 
     * been returned are skipped, the next 20 results are returned and the
     * remaining 30 results are ignored. An implementation should execute
     * the query such that the range algorithm is done at the data store.
     * @param fromInclToExcl comma-separated fromIncl and toExcl values
     * @see #setRange(long, long)
     * @since 2.0
     */
    public void setRange (String fromInclToExcl) {
        throw new UnsupportedOperationException(
            "method setRange(String) is not yet implemented");
    }

    /**
     * Add a vendor-specific extension to this query. The key and value
     * are not standard.
     * An implementation must ignore keys that are not recognized.
     * @param key the key of the extension
     * @param value the value of the extension
     * @since 2.0
     */
    public void addExtension (String key, Object value) {
        throw new UnsupportedOperationException(
            "method addExtension(String, Object) is not yet implemented");
    }

    /**
     * Set multiple extensions, or use null to clear all extensions.
     * Map keys and values are not standard.
     * An implementation must ignore entries that are not recognized.
     * @param extensions the map of extensions
     * @see #addExtension
     * @since 2.0
     */
    public void setExtensions (Map extensions) {
        throw new UnsupportedOperationException(
            "method setExtensions(Map) is not yet implemented");
    }

    /**
     * Returns the <code>FetchPlan</code> used by this
     * <code>Query</code>. Modifications of the returned fetch plan will not
     * cause this query's owning <code>PersistenceManager</code>'s
     * <code>FetchPlan</code> to be modified.
     * @since 2.0
     * @return the fetch plan used by this query
     */
    public FetchPlan getFetchPlan () {
        throw new UnsupportedOperationException(
            "method getFetchPlan() is not yet implemented");
    }

    /**
     * Deletes all the instances of the candidate class that pass the
     * filter.
     * @see #deletePersistentAll()
     * @param parameters for the query
     * @return the number of instances of the candidate class that were deleted
     * @since 2.0
     */
    public long deletePersistentAll (Object[] parameters) {
        throw new UnsupportedOperationException(
            "method deletePersistentAll(Object[]) is not yet implemented");
    }

    /**
     * Deletes all the instances of the candidate class that pass the
     * filter.
     * @see #deletePersistentAll()
     * @param parameters for the query
     * @return the number of instances of the candidate class that were deleted
     * @since 2.0
     */
    public long deletePersistentAll (Map parameters) {
        throw new UnsupportedOperationException(
            "method deletePersistentAll(Map) is not yet implemented");
    }

    /**
     * Deletes all the instances of the candidate class that pass the
     * filter. Returns the number of instances of the candidate
     * class that were deleted, specifically not including the number
     * of dependent and embedded instances.
     * <P>Dirty instances of affected classes in the cache are first
     * flushed to the datastore. Instances in the cache or brought into
     * the cache as a result of executing one of the 
     * <code>deletePersistentAll</code>
     * methods undergo life cycle changes as if <code>deletePersistent</code>
     * were called on them.
     * <P>Specifically, if the class of deleted instances implements the
     * delete callback interface, the corresponding callback methods
     * are called on the deleted instances. Similarly, if there are
     * lifecycle listeners registered for delete events on affected
     * classes, the listener is called for each appropriate deleted instance.
     * <P>Before returning control to the application, instances of affected
     * classes in the cache are refreshed to reflect whether they were
     * deleted from the datastore.
     * 
     * @return the number of instances of the candidate class that were deleted
     * @since 2.0
     */
    public long deletePersistentAll () {
        throw new UnsupportedOperationException(
            "method deletePersistentAll() is not yet implemented");
    }
    
    /**
     * The unmodifiable flag, when set, disallows further 
     * modification of the query, except for specifying the range, 
     * result class, and ignoreCache option.
     * The unmodifiable flag can also be set in metadata.
     * @since 2.0
      */
    public void setUnmodifiable() {
        throw new UnsupportedOperationException(
            "method setUnmodifiable() is not yet implemented");
    }

    /**
     * The unmodifiable flag, when set, disallows further 
     * modification of the query, except for specifying the range, 
     * result class, and ignoreCache option.
     * @return the current setting of the flag
     * @since 2.0
      */
    public boolean isUnmodifiable() {
        throw new UnsupportedOperationException(
            "method isUnmodifiable() is not yet implemented");
    }

    //========= Internal helper methods ==========
    
    /** 
     * This method verifies that if there is no transaction in progress then 
     * the NontransactionalRead flag must be set to true.
     */
    private void checkTransaction() 
    {
        Transaction tx = pm.currentTransaction();
        if ((!tx.isActive()) & !tx.getNontransactionalRead() )
                throw new JDOQueryException(msg.msg("EXC_NoTransaction")); //NOI18N
    }
    
    /**
     * This method checks a valid candidates setting for this query. 
     */
    private void checkCandidates()
    {
        if (candidates == null) {
            if (queryTree != null) {
                candidates = pm.getExtent(queryTree.getCandidateClass(), true);
            }
            else if (candidateClass != null) {
                candidates = pm.getExtent(candidateClass, true);
            }
            else {
                throw new JDOQueryException(msg.msg("EXC_MissingCandidateClass")); //NOI18N
            }
        }
    }

    /**
     * This method checks the candidate class of the underlying query
     * tree. If this query instance was created from a query tree, and 
     * subsequently serialized and deserialized then The class instance of
     * the candidate class is not set. The class instance of the candidate
     * class is not part of the serialized state of a querty tree; only the
     * name gets stored. In this case the method uses the PM algorithm to
     * calculate the class instance of the candidate class and stores it in
     * the query tree. 
     */
    private void checkQueryTreeCandidateClass()
    {
        if (queryTree == null) 
            return;

        if (queryTree.getCandidateClass() == null) {
            String candidateClassName = 
                queryTree.getSerializedCandidateClassName();
            if (candidateClassName != null) {
                try {
                    queryTree.setCandidateClass(
                        pm.loadClass(candidateClassName, null));
                }
                catch (ClassNotFoundException ex) {
                    throw new JDOQueryException(
                        msg.msg("EXC_UnknownCandidateClass", //NOI18N
                                candidateClassName), ex);
                }
            }
        }
    }

    /** Serialization support. */
    private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.openQueryResults = new HashSet();
    }

    /** Deserialization support. */
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        if (candidateClass != null)
            this.candidateClassName = candidateClass.getName();
        out.defaultWriteObject();
    }
}
