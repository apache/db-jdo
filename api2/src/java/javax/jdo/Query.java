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
 * Query.java
 *
 */

package javax.jdo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/** The <code>Query</code> interface allows applications to obtain persistent
 * instances, values, and aggregate data
 * from the data store.
 *
 * The {@link PersistenceManager} is the factory for <code>Query</code> 
 * instances.  There may be many <code>Query</code> instances associated with a 
 * <code>PersistenceManager</code>.  Multiple queries might be executed 
 * simultaneously by different threads, but the implementation might choose to 
 * execute them serially.  In either case, the implementation must be thread 
 * safe.
 *
 * <P>There are three required elements in a <code>Query</code>: the class of 
 * the results, the candidate collection of instances, and the filter.
 *
 * <P>There are optional elements: parameter declarations, variable
 * declarations, import statements, ordering and grouping specifications,
 * result and result class, the range of results,
 * and flags indicating whether the query result
 * is unique and whether the query can be modified.
 * <P>The query namespace is modeled after methods in Java:
 * <ul>
 * <li><code>setClass</code> corresponds to the class definition
 * <li><code>declareParameters</code> corresponds to formal parameters of a 
 * method
 * <li><code>declareVariables</code> corresponds to local variables of a method
 * <li><code>setFilter</code> and <code>setOrdering</code> correspond to the 
 * method body
 * </ul>
 * <P>There are two namespaces in queries. Type names have their own
 * namespace that is separate from the namespace for fields, variables
 * and parameters.
 * <P>The method <code>setClass</code> introduces the name of the candidate 
 * class in the type namespace. The method <code>declareImports</code> 
 * introduces the names of the imported class or interface types in the type 
 * namespace. Imported type names must be unique. When used (e.g. in a parameter
 * declaration, cast expression, etc.) a type name must be the name of the 
 * candidate class, the name of a class or interface imported by method
 * <code>declareImports</code>, or denote a class or interface from the same
 * package as the candidate class.
 * <P>The method <code>setClass</code> introduces the names of the candidate 
 * class fields.
 * <P>The method <code>declareParameters</code> introduces the names of the
 * parameters. A name introduced by <code>declareParameters</code> hides the 
 * name of a candidate class field of the same name. Parameter names must be 
 * unique.
 * <P>The method <code>declareVariables</code> introduces the names of the 
 * variables.  A name introduced by <code>declareVariables</code> hides the name
 * of a candidate class field if equal. Variable names must be unique and must 
 * not conflict with parameter names.
 * <P>The result of the query by default is a list of result class instances,
 * but might be specified via <code>setResult</code>. The class of the result
 * by default is the candidate class, but might be specified via 
 * <code>setResultClass</code>.
 * <P>A hidden field may be accessed using the 'this' qualifier:
 * <code>this.fieldName</code>.
 * <P>The <code>Query</code> interface provides methods which execute the query
 * based on the parameters given. They return a single instance or a 
 * <code>List</code> of result class instances which the
 * user can iterate to get results. The signature
 * of the <code>execute</code> methods specifies that they return an 
 * <code>Object</code> which must be cast to the appropriate result by the user.
 * <P>Any parameters passed to the <code>execute</code> methods are used only 
 * for this execution, and are not remembered for future execution.
 * @version 2.1
 */

public interface Query extends Serializable {
    
    /**
     * The string constant used as the first argument to 
     * {@link PersistenceManager#newQuery(String,Object)} to identify that the 
     * created query should obey the JDOQL syntax and semantic rules.
     * <p>This is the default query language used when creating a query with any
     * of the other {@link PersistenceManager#newQuery} methods, except 
     * {@link PersistenceManager#newQuery(Object)}, which uses the query 
     * language of the compiled query template object passed to that method.</p>
     * @since 2.0
     */
    String JDOQL = "javax.jdo.query.JDOQL";

    /**
     * The string constant used as the first argument to {@link
     * PersistenceManager#newQuery(String,Object)} to identify that
     * the created query should use SQL semantics. This is only
     * meaningful for relational JDO implementations.
     * <p>If this is used, the <code>Object</code> argument to the
     * {@link PersistenceManager#newQuery(String,Object)} method
     * should be a <code>String</code> containing a SQL
     * <code>SELECT</code> statement.</p>
     * @since 2.0
     */
    String SQL = "javax.jdo.query.SQL";

    /** Set the class of the candidate instances of the query.
     * <P>The class specifies the class
     * of the candidates of the query.  Elements of the candidate collection
     * that are of the specified class are filtered before being
     * put into the result <code>Collection</code>.
     *
     * @param cls the <code>Class</code> of the candidate instances.
     */
    void setClass(Class cls);
    
    /** Set the candidate <code>Extent</code> to query.
     * @param pcs the candidate <code>Extent</code>.
     */
    void setCandidates(Extent pcs);
    
    /** Set the candidate <code>Collection</code> to query.
     * @param pcs the candidate <code>Collection</code>.
     */
    void setCandidates(Collection pcs);
    
    /** Set the filter for the query.
     *
     * <P>The filter specification is a <code>String</code> containing a Boolean
     * expression that is to be evaluated for each of the instances
     * in the candidate collection. If the filter is not specified,
     * then it defaults to "true", which has the effect of filtering
     * the input <code>Collection</code> only for class type.
     * <P>An element of the candidate collection is returned in the result if:
     * <ul><li>it is assignment compatible to the candidate <code>Class</code> 
     * of the <code>Query</code>; and
     * <li>for all variables there exists a value for which the filter
     * expression evaluates to <code>true</code>.
     * </ul>
     * <P>The user may denote uniqueness in the filter expression by
     * explicitly declaring an expression (for example, <code>e1 != e2</code>).
     * <P>Rules for constructing valid expressions follow the Java
     * language, except for these differences:
     * <ul>
     * <li>Equality and ordering comparisons between primitives and instances
     * of wrapper classes are valid.
     * <li>Equality and ordering comparisons of <code>Date</code> fields and 
     * <code>Date</code> parameters are valid.
     * <li>White space (non-printing characters space, tab, carriage
     * return, and line feed) is a separator and is otherwise ignored.
     * <li>The assignment operators <code>=</code>, <code>+=</code>, etc. and 
     * pre- and post-increment and -decrement are not supported. Therefore, 
     * there are no side effects from evaluation of any expressions.
     * <li>Methods, including object construction, are not supported, except
     * for <code>Collection.contains(Object o)</code>, 
     * <code>Collection.isEmpty()</code>, 
     * <code>String.startsWith(String s)</code>, and 
     * <code>String.endsWith(String e)</code>.  Implementations might choose to
     * support non-mutating method calls as non-standard extensions.
     * <li>Navigation through a <code>null</code>-valued field, which would 
     * throw <code>NullPointerException</code>, is treated as if the filter 
     * expression returned <code>false</code> for the evaluation of the current
     * set of variable values. Other values for variables might still qualify 
     * the candidate instance for inclusion in the result set.
     * <li>Navigation through multi-valued fields (<code>Collection</code> 
     * types) is specified using a variable declaration and the
     * <code>Collection.contains(Object o)</code> method.
     * </ul>
     * <P>Identifiers in the expression are considered to be in the name
     * space of the specified class, with the addition of declared imports,
     * parameters and variables. As in the Java language, <code>this</code> is a 
     * reserved word which means the element of the collection being evaluated.
     * <P>Navigation through single-valued fields is specified by the Java
     * language syntax of <code>field_name.field_name....field_name</code>.
     * <P>A JDO implementation is allowed to reorder the filter expression
     * for optimization purposes.
     * @param filter the query filter.
     */
    void setFilter(String filter);
    
    /** Set the import statements to be used to identify the fully qualified 
     * name of variables or parameters.  Parameters and unbound variables might 
     * come from a different class from the candidate class, and the names 
     * need to be declared in an import statement to eliminate ambiguity. 
     * Import statements are specified as a <code>String</code> with 
     * semicolon-separated statements. 
     * <P>The <code>String</code> parameter to this method follows the syntax of 
     * the import statement of the Java language.
     * @param imports import statements separated by semicolons.
     */
    void declareImports(String imports);
    
    /** Declare the list of parameters query execution.
     *
     * The parameter declaration is a <code>String</code> containing one or more 
     * query parameter declarations separated with commas. Each parameter named 
     * in the parameter declaration must be bound to a value when 
     * the query is executed.
     * <P>The <code>String</code> parameter to this method follows the syntax 
     * for formal parameters in the Java language. 
     * @param parameters the list of parameters separated by commas.
     */
    void declareParameters(String parameters);
    
    /** Declare the unbound variables to be used in the query. Variables 
     * might be used in the filter, and these variables must be declared 
     * with their type. The unbound variable declaration is a 
     * <code>String</code> containing one or more unbound variable declarations
     * separated with semicolons. It follows the syntax for local variables in 
     * the Java language.
     * @param variables the variables separated by semicolons.
     */
    void declareVariables(String variables);
    
    /** Set the ordering specification for the result <code>Collection</code>.  
     * The ordering specification is a <code>String</code> containing one or
     * more ordering declarations separated by commas.
     *
     * <P>Each ordering declaration is the name of the field on which
     * to order the results followed by one of the following words:
     * "<code>ascending</code>" or "<code>descending</code>".
     *
     *<P>The field must be declared in the candidate class or must be
     * a navigation expression starting with a field in the candidate class.
     *
     *<P>Valid field types are primitive types except <code>boolean</code>; 
     * wrapper types except <code>Boolean</code>; <code>BigDecimal</code>; 
     * <code>BigInteger</code>; <code>String</code>; and <code>Date</code>.
     * @param ordering the ordering specification.
     */
    void setOrdering(String ordering);
    
    /** Set the ignoreCache option.  The default value for this option was
     * set by the <code>PersistenceManagerFactory</code> or the
     * <code>PersistenceManager</code> used to create this <code>Query</code>.
     *
     * The ignoreCache option setting specifies whether the query should execute
     * entirely in the back end, instead of in the cache.  If this flag is set
     * to <code>true</code>, an implementation might be able to optimize the 
     * query execution by ignoring changed values in the cache.  For optimistic
     * transactions, this can dramatically improve query response times.
     * @param ignoreCache the setting of the ignoreCache option.
     */
    void setIgnoreCache(boolean ignoreCache);   
    
    /** Get the ignoreCache option setting.
     * @return the ignoreCache option setting.
     * @see #setIgnoreCache
     */
    boolean getIgnoreCache();
    
    /** Verify the elements of the query and provide a hint to the query to
     * prepare and optimize an execution plan.
     */
    void compile();
    
    /** Execute the query and return the filtered Collection.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     */
    Object execute();
    
    /** Execute the query and return the filtered <code>Collection</code>.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     */
    Object execute(Object p1);
    
    /** Execute the query and return the filtered <code>Collection</code>.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     */
    Object execute(Object p1, Object p2);
    
    /** Execute the query and return the filtered <code>Collection</code>.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     * @param p3 the value of the third parameter declared.
     */
    Object execute(Object p1, Object p2, Object p3);
    
    /** Execute the query and return the filtered <code>Collection</code>.  The 
     * query is executed with the parameters set by the <code>Map</code> values.  
     * Each <code>Map</code> entry consists of a key which is the name of the 
     * parameter in the <code>declareParameters</code> method, and a value which 
     * is the value used in the <code>execute</code> method.  The keys in the 
     * <code>Map</code> and the declared parameters must exactly match or a 
     * <code>JDOUserException</code> is thrown.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @param parameters the <code>Map</code> containing all of the parameters.
     */
    Object executeWithMap (Map parameters);
    
    /** Execute the query and return the filtered <code>Collection</code>.
     *
     * <P>The execution of the query obtains the values of the parameters and
     * matches them against the declared parameters in order.  The names
     * of the declared parameters are ignored.  The type of
     * the declared parameters must match the type of the passed parameters,
     * except that the passed parameters might need to be unwrapped to get
     * their primitive values.
     *
     * <P>The filter, import, declared parameters, declared variables, and
     * ordering statements are verified for consistency.
     *
     * <P>Each element in the candidate <code>Collection</code> is examined to 
     * see that it is assignment compatible to the <code>Class</code> of the 
     * query.  It is then evaluated by the Boolean expression of the filter.  
     * The element passes the filter if there exist unique values for all 
     * variables for which the filter expression evaluates to <code>true</code>.
     * <P>Cancellation of the query using cancel() will result in JDOQueryInterruptedException
     * being thrown here
     * @return the filtered <code>Collection</code>.
     * @param parameters the <code>Object</code> array with all of the 
     * parameters.
     */
    Object executeWithArray (Object... parameters);
    
    /** Get the <code>PersistenceManager</code> associated with this 
     * <code>Query</code>.
     *
     * <P>If this <code>Query</code> was restored from a serialized form, it has
     * no <code>PersistenceManager</code>, and this method returns 
     * <code>null</code>.
     * @return the <code>PersistenceManager</code> associated with this 
     * <code>Query</code>.
     */
    PersistenceManager getPersistenceManager();
  
    /** Close a query result and release any resources associated with it.  The
     * parameter is the return from <code>execute(...)</code> and might have 
     * iterators open on it.  Iterators associated with the query result are 
     * invalidated: they return <code>false</code>  to <code>hasNext()</code> 
     * and throw <code>NoSuchElementException</code> to <code>next()</code>.
     * @param queryResult the result of <code>execute(...)</code> on this 
     * <code>Query</code> instance.
     */    
    void close (Object queryResult);
    
    /** Close all query results associated with this <code>Query</code> 
     * instance, and release all resources associated with them.  The query 
     * results might have iterators open on them.  Iterators associated with the
     * query results are invalidated: 
     * they return <code>false</code> to <code>hasNext()</code> and throw 
     * <code>NoSuchElementException</code> to <code>next()</code>.
     */    
    void closeAll ();

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
    void setGrouping (String group);

    /**
     * Specify that only the first result of the query should be
     * returned, rather than a collection. The execute method will
     * return null if the query result size is 0.
     * @since 2.0
     * @param unique if true, only one element is returned
     */
    void setUnique (boolean unique);

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
    void setResult (String data);

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
     * one or more public <code>set</code> or <code>put</code> methods or 
     * fields. 
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
    void setResultClass (Class cls);

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
    void setRange (long fromIncl, long toExcl);

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
    void setRange (String fromInclToExcl);

    /**
     * Add a vendor-specific extension to this query. The key and value
     * are not standard.
     * An implementation must ignore keys that are not recognized.
     * @param key the key of the extension
     * @param value the value of the extension
     * @since 2.0
     */
    void addExtension (String key, Object value);

    /**
     * Set multiple extensions, or use null to clear all extensions.
     * Map keys and values are not standard.
     * An implementation must ignore entries that are not recognized.
     * @param extensions the map of extensions
     * @see #addExtension
     * @since 2.0
     */
    void setExtensions (Map extensions);

    /**
     * Returns the <code>FetchPlan</code> used by this
     * <code>Query</code>. Modifications of the returned fetch plan will not
     * cause this query's owning <code>PersistenceManager</code>'s
     * <code>FetchPlan</code> to be modified.
     * @since 2.0
     * @return the fetch plan used by this query
     */
    FetchPlan getFetchPlan ();

    /**
     * Deletes all the instances of the candidate class that pass the
     * filter.
     * @see #deletePersistentAll()
     * @param parameters for the query
     * @return the number of instances of the candidate class that were deleted
     * @since 2.0
     */
    long deletePersistentAll (Object... parameters);

    /**
     * Deletes all the instances of the candidate class that pass the
     * filter.
     * @see #deletePersistentAll()
     * @param parameters for the query
     * @return the number of instances of the candidate class that were deleted
     * @since 2.0
     */
    long deletePersistentAll (Map parameters);

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
    long deletePersistentAll ();
    
    /**
     * The unmodifiable flag, when set, disallows further 
     * modification of the query, except for specifying the range, 
     * result class, and ignoreCache option.
     * The unmodifiable flag can also be set in metadata.
     * @since 2.0
      */
    void setUnmodifiable();

    /**
     * The unmodifiable flag, when set, disallows further 
     * modification of the query, except for specifying the range, 
     * result class, and ignoreCache option.
     * @return the current setting of the flag
     * @since 2.0
      */
    boolean isUnmodifiable();

    /**
     * Add a subquery to this query.
     * @param sub the subquery to add to this Query
     * @param variableDeclaration the name of the variable in the outer query
     * to bind the results of the subquery
     * @param candidateCollectionExpression the candidate collection 
     * of the subquery as an expression using terms of the outer query
     * @see #addSubquery(Query sub, String variableDeclaration, 
     *    String candidateCollectionExpression, String... parameters)
     * @since 2.1
     */
    void addSubquery
        (Query sub, String variableDeclaration, 
            String candidateCollectionExpression);

    /**
     * Add a subquery to this query.
     * The String version of the method binds the named expression 
     * to the parameter implictly or explicitly declared in the 
     * subquery.
     * @param sub the subquery to add to this Query
     * @param variableDeclaration the name of the variable 
     * to be used in this Query
     * @param candidateCollectionExpression the candidate collection 
     * to apply to the subquery
     * @param parameter the expression from the outer query to bind 
     * the parameter in the subquery
     * @see #addSubquery(Query sub, String variableDeclaration, 
     *    String candidateCollectionExpression, String... parameters)
     * @since 2.1
     */
    void addSubquery
        (Query sub, String variableDeclaration, 
         String candidateCollectionExpression, String parameter);

    /**
     * Add a subquery to this query.
     * A subquery is composed as a Query and subsequently attached
     * to a different query (the outer query) by calling this method.
     * The query parameter instance is unmodified as a result of the
     * addSubquery or subsequent execution of the outer query.
     * Only some of the query parts are copied for use as the subquery.
     * The parts copied include the candidate class, filter, parameter
     * declarations, variable declarations, imports, ordering specification,
     * uniqueness, result specification, and grouping specification.
     * The association with a PersistenceManager, the candidate collection
     * or extent, result class, and range limits are not used.
     * The String parameters are trimmed of white space.
     * The variableDeclaration parameter is the name of the variable
     * containing the results of the subquery execution. If the same value
     * of variableDeclaration is used to add multiple subqueries, the
     * subquery replaces the previous subquery for the same named variable.
     * If the subquery parameter is null, the variable is unset,
     * effectively making the variable named in the variableDeclaration
     * unbound. If the trimmed value is the empty String, or the parameter
     * is null, then JDOUserException is thrown.
     * The candidateCollectionExpression is the expression from the
     * outer query that represents the candidates over which the subquery
     * is evaluated. If the trimmed value is the empty String, or the
     * parameter is null, then the candidate collection is the extent
     * of the candidate class.
     * The String... version of the method binds the named expressions in 
     * turn to parameters in the order in which they are declared in the 
     * subquery, or in the order they are found in the filter if not 
     * explicitly declared in the subquery.
     * @param sub the subquery to add to this Query
     * @param variableDeclaration the name of the variable in the outer query
     * to bind the results of the subquery
     * @param candidateCollectionExpression the candidate collection 
     * of the subquery as an expression using terms of the outer query
     * @param parameters the expressions from the outer query to bind 
     * the parameters in the subquery
     * @since 2.1
     */
    void addSubquery
        (Query sub, String variableDeclaration, 
         String candidateCollectionExpression, String... parameters);

    /**
     * Add a subquery to this query.
     * The Map version of the method treats the key of each map entry as 
     * the name of  the parameter in the subquery, with or without the 
     * leading ":", and the value as the name of the expression in the 
     * outer query. If the trimmed expression is the empty String for 
     * either the parameter or the value of the String[], or for any 
     * map key or value, that expression is ignored.
     * @param sub the subquery to add to this Query
     * @param variableDeclaration the name of the variable 
     * to be used in this Query
     * @param candidateCollectionExpression the candidate collection 
     * to apply to the subquery
     * @param parameters the expressions from the outer query to bind 
     * the parameter in the subquery
     * @see #addSubquery(Query sub, String variableDeclaration, 
     *    String candidateCollectionExpression, String... parameters)
     * @since 2.1
     */
    void addSubquery
        (Query sub, String variableDeclaration, 
         String candidateCollectionExpression, Map parameters);

    /**
     * Specify a timeout interval (milliseconds) for any query executions.
     * If a query hasn't completed within this interval execute() will throw a
     * JDOQueryTimeoutException.
     * @since 2.3
     * @param interval The timeout interval (millisecs)
     */
    void setTimeoutMillis(Integer interval);

    /** Get the timeout setting for query executions. 
     *
     * @return the query timeout setting.
     * @since 2.3
     */
    Integer getQueryTimeoutMillis();

    /**
     * Method to cancel any executing queries.
     * If the underlying datastore doesn't support cancellation of queries this will
     * throw JDOUnsupportedOptionException.
     */
    void cancel();
}
