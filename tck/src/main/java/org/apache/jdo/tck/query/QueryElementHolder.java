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
 
package org.apache.jdo.tck.query;

import javax.jdo.Extent;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Map;

/**
 * This class is an abstraction of a JDOQL query,
 * which may be represented as a single string 
 * (e.g. <code>SELECT FROM Person WHERE personid == 1</code>) or
 * as an API query using methods on class {@link Query}.<p>
 * 
 * The class may be used as the factory of a JDO {@link Query} instance
 * using method {@link QueryElementHolder#getAPIQuery(PersistenceManager)} or
 * method {@link QueryElementHolder#getSingleStringQuery(PersistenceManager)}.<p>
 * 
 * Instances of this class are capable to hold all elements of a JDOQL query,
 * e.g. the candidate class, the filter, etc. These elements are passed
 * calling the constructor. It is valid to pass <code>null</code> as a value
 * for JDOQL querys elements. Such elements are not transfered into a 
 * JDO {@link javax.jdo.Query} instance. 
 * Instead, the default of JDO {@link javax.jdo.Query} instance is taken.
 */
public class QueryElementHolder {

    // fields holding JDOQL query elements
    private Boolean unique;
    private String result;
    private Class resultClass;
    private Class candidateClass;
    private Boolean excludeSubClasses;
    private String filter;
    private String variables;
    private String parameters;
    private String imports;
    private String grouping;
    private String ordering;
    private String fromString;
    private String toString;
    private Long   fromLong;
    private Long   toLong;
    private JDOQLTypedQuery<?> jdoqlTypedQuery;
    private Map<String, ?> paramValues;
    
    /**
     * Returns an instance of this class holding the given arguments
     * such as the candidate class, the filter, etc. 
     * The given arguments represent JDOQL query elements. 
     * It is valid to pass <code>null</code> as a value
     * for JDOQL querys elements. Such elements are not transfered into a 
     * JDO {@link javax.jdo.Query} instance.
     * Instead, the default of JDO {@link javax.jdo.Query} instance is taken.
     * @param unique the JDOQL unique query element
     * @param result the JDOQL result query element
     * @param resultClass the JDOQL result class query element
     * @param candidateClass the JDOQL candidate class query element
     * @param excludeSubClasses the JDOQL exclude subclasses query element
     * @param filter the JDOQL filter query element
     * @param variables the JDOQL variables query element
     * @param parameters the JDOQL parameters query element
     * @param imports the JDOQL imports query element
     * @param grouping the JDOQL grouping query element
     * @param ordering the JDOQL ordering query element
     * @param from the JDOQL range from query element
     * @param to the JDOQL range to query element
     */
    public QueryElementHolder(Boolean unique, String result, 
            Class resultClass, Class candidateClass, 
            Boolean excludeSubClasses, String filter,
            String variables, String parameters, String imports, 
            String grouping, String ordering, String from, String to) {
        if (from == null ^ to == null) {
            throw new IllegalArgumentException(
                    "Arguments from and to must both be null, " +
                    "or must not be null both.");
        }
        this.unique = unique;
        this.result = result;
        this.resultClass = resultClass;
        this.candidateClass = candidateClass;
        this.excludeSubClasses = excludeSubClasses;
        this.filter = filter;
        this.variables = variables;
        this.parameters = parameters;
        this.imports = imports;
        this.grouping = grouping;
        this.ordering = ordering;
        this.fromString = from;
        this.toString = to;
    }

    /**
     * Returns an instance of this class holding the given arguments
     * such as the candidate class, the filter, etc.
     * The given arguments represent JDOQL query elements.
     * It is valid to pass <code>null</code> as a value
     * for JDOQL querys elements. Such elements are not transfered into a
     * JDO {@link javax.jdo.Query} instance.
     * Instead, the default of JDO {@link javax.jdo.Query} instance is taken.
     * @param unique the JDOQL unique query element
     * @param result the JDOQL result query element
     * @param resultClass the JDOQL result class query element
     * @param candidateClass the JDOQL candidate class query element
     * @param excludeSubClasses the JDOQL exclude subclasses query element
     * @param filter the JDOQL filter query element
     * @param variables the JDOQL variables query element
     * @param parameters the JDOQL parameters query element
     * @param imports the JDOQL imports query element
     * @param grouping the JDOQL grouping query element
     * @param ordering the JDOQL ordering query element
     * @param from the JDOQL range from query element
     * @param to the JDOQL range to query element
     */
    public QueryElementHolder(Boolean unique, String result,
                              Class resultClass, Class candidateClass,
                              Boolean excludeSubClasses, String filter,
                              String variables, String parameters, String imports,
                              String grouping, String ordering, String from, String to,
                              JDOQLTypedQuery<?> jdoqlTypedQuery,
                              Map<String, ?> paramValues) {
        if (from == null ^ to == null) {
            throw new IllegalArgumentException(
                    "Arguments from and to must both be null, " +
                            "or must not be null both.");
        }
        this.unique = unique;
        this.result = result;
        this.resultClass = resultClass;
        this.candidateClass = candidateClass;
        this.excludeSubClasses = excludeSubClasses;
        this.filter = filter;
        this.variables = variables;
        this.parameters = parameters;
        this.imports = imports;
        this.grouping = grouping;
        this.ordering = ordering;
        this.fromString = from;
        this.toString = to;
        this.jdoqlTypedQuery = jdoqlTypedQuery;
        this.paramValues = paramValues;
    }

    /**
     * Returns an instance of this class holding the given arguments
     * such as the candidate class, the filter, etc.
     * The given arguments represent JDOQL query elements.
     * It is valid to pass <code>null</code> as a value
     * for JDOQL querys elements. Such elements are not transfered into a
     * JDO {@link javax.jdo.Query} instance.
     * Instead, the default of JDO {@link javax.jdo.Query} instance is taken.
     * @param unique the JDOQL unique query element
     * @param result the JDOQL result query element
     * @param resultClass the JDOQL result class query element
     * @param candidateClass the JDOQL candidate class query element
     * @param excludeSubClasses the JDOQL exclude subclasses query element
     * @param filter the JDOQL filter query element
     * @param variables the JDOQL variables query element
     * @param parameters the JDOQL parameters query element
     * @param imports the JDOQL imports query element
     * @param grouping the JDOQL grouping query element
     * @param ordering the JDOQL ordering query element
     * @param from the JDOQL from query element
     * @param to the JDOQL to query element
     */
    public QueryElementHolder(Boolean unique, String result,
                              Class resultClass, Class candidateClass,
                              Boolean excludeSubClasses, String filter,
                              String variables, String parameters, String imports,
                              String grouping, String ordering, long from, long to,
                              JDOQLTypedQuery<?> jdoqlTypedQuery,
                              Map<String, ?> paramValues) {
        this.unique = unique;
        this.result = result;
        this.resultClass = resultClass;
        this.candidateClass = candidateClass;
        this.excludeSubClasses = excludeSubClasses;
        this.filter = filter;
        this.variables = variables;
        this.parameters = parameters;
        this.imports = imports;
        this.grouping = grouping;
        this.ordering = ordering;
        this.fromLong = new Long(from);
        this.toLong = new Long(to);
        this.jdoqlTypedQuery = jdoqlTypedQuery;
        this.paramValues = paramValues;
    }
    
    /**
     * Returns the single string JDOQL representation.
     * @see Object#toString()
     */
    public String toString() {
        return "SELECT " +
        toString("UNIQUE", this.unique) +
        toString(this.result) +
        toString("INTO", this.resultClass) +
        toString("FROM", this.candidateClass) +
        toString("EXCLUDE SUBCLASSES", this.excludeSubClasses) +
        toString("WHERE", this.filter) +
        toString("VARIABLES", this.variables) +
        toString("PARAMETERS", this.parameters) +
        toString(this.imports) +
        toString("GROUP BY", this.grouping) +
        toString("ORDER BY", this.ordering) +
        rangeToString();
    }
    
    /**
     * Creates a JDO {@link javax.jdo.Query} instance using the JDOQL query elements 
     * of this instance. The returned instance is created calling
     * {@link PersistenceManager#newQuery(String)}.
     * The passed {@link String} instance is the 
     * single string representation of this,
     * e.g. <code>SELECT FROM Person WHERE personid == 1</code>.
     * @param pm the persistence manager
     * @return the JDO query instance
     */
    public Query getSingleStringQuery(PersistenceManager pm) {
        return pm.newQuery(toString());
    }

    /**
     * Creates a JDO {@link javax.jdo.Query} instance using the JDOQL query elements 
     * of this instance. The returned instance is created calling
     * {@link PersistenceManager#newQuery(Extent)}.
     * Afterwards, all query elements of this are transfered 
     * into that instance using API methods like 
     * {@link javax.jdo.Query#setFilter(java.lang.String)} etc.
     * @param pm the persistence manager
     * @return the JDO query instance
     */
    public Query getAPIQuery(PersistenceManager pm) {
        Extent extent = this.excludeSubClasses != null ? 
                pm.getExtent(this.candidateClass, 
                        !this.excludeSubClasses.booleanValue()) :
                pm.getExtent(this.candidateClass);
        Query query = pm.newQuery(extent);
        if (this.unique != null) {
            query.setUnique(this.unique.booleanValue());
        }
        if (this.result != null ) {
            query.setResult(this.result);
        }
        if (this.resultClass != null) {
            query.setResultClass(this.resultClass);
        }
        if (this.filter != null) {
            query.setFilter(this.filter);
        }
        if (this.variables != null) {
            query.declareVariables(this.variables);
        }
        if (this.parameters != null) {
            query.declareParameters(this.parameters);
        }
        if (this.imports != null ) {
            query.declareImports(this.imports);
        }
        if (this.grouping != null) {
            query.setGrouping(this.grouping);
        }
        if (this.ordering != null) {
            query.setOrdering(this.ordering);
        }
        rangeToAPI(query);
        return query;
    }

    /**
     * Returns the JDOQLTypedQuery instance.
     * @return the JDOQLTypedQuery instance
     */
    public JDOQLTypedQuery<?> getJDOQLTypedQuery() {
        if (this.jdoqlTypedQuery != null) {
            this.rangeToJDOQLTypedQuery(this.jdoqlTypedQuery);
        }
        return this.jdoqlTypedQuery;
    }

    /**
     * Returns the unique JDOQL query element.
     * @return the unique JDOQL query element.
     */
    public boolean isUnique() {
        return this.unique != null && this.unique.booleanValue();
    }

    /**
     * Returns the unique JDOQL query element.
     * @return the unique JDOQL query element.
     */
    public boolean hasOrdering() {
        return this.ordering != null;
    }
    
    /**
     * Returns the candtidate class JDOQL query element.
     * @return the candtidate class JDOQL query element.
     */
    public Class getCandidateClass() {
        return this.candidateClass;
    }

    /**
     * Returns the map of parameter values.
     * @return the map of parameter values
     */
    public Map<String, ?> getParamValues() {
        return this.paramValues;
    }

    /**
     * Delegates to {@link QueryElementHolder#toString(String, String)
     * if argument <code>clazz</code> does not equal <code>null</code>,
     * otherwise returns an empty string.
     * @param prefix the prefix of the returned string.
     * @param clazz the returned string has the class name as a suffix.
     * @return the string.
     */
    private String toString(String prefix, Class clazz) {
        return (clazz != null? toString(prefix, clazz.getName()) : "");
    }

    /**
     * Returns a string prefixed by argument <code>prefix</code> and 
     * suffixed by the string representation of argument <code>bool</code>,
     * if argument <code>bool</code> does not equal <code>null</code>.
     * Otherwise, an empty string is returned.
     * @param prefix the prefix of the returned string.
     * @param bool the returned string has the string representation 
     * of the value as a suffix.
     * @return the string.
     */
    private String toString(String prefix, Boolean bool) {
        return bool!=null && bool.booleanValue() ? prefix + ' ' : "";
    }

    /**
     * Returns a string prefixed by argument <code>prefix</code> and 
     * suffixed by argument <code>suffix</code>,
     * if argument <code>suffix</code> does not equal <code>null</code>.
     * Otherwise, an empty string is returned.
     * @param prefix the prefix of the returned string.
     * @param suffix the suffix of the returned string.
     * @return the string.
     */
    private String toString(String prefix, String suffix) {
        return (suffix != null ? prefix + ' ' + suffix + ' ' : "");
    }

    /**
     * Returns a string prefixed by argument <code>prefix</code>,
     * if argument <code>prefix</code> does not equal <code>null</code>.
     * Otherwise, an empty string is returned.
     * @param prefix the prefix of the returned string.
     * @return the string.
     */
    private String toString(String prefix) {
        return (prefix != null ? prefix + ' ' : "");
    }

    /**
     * Returns the single string representation 
     * of the JDOQL query element <i>range</i>.
     * If that element is <code>null</code>, 
     * then an empty string is returned.
     * @return the single string representation 
     * of the JDOQL query element <i>range</i> or <code>null</code>, 
     */
    private String rangeToString() {
        String result = "";
        if (this.fromString != null && this.toString != null) { 
            result = "RANGE " + this.fromString + ',' + this.toString;
        }
        else if (this.fromLong != null && this.toLong != null) {
            result = "RANGE " + this.fromLong + ',' + this.toLong;
        }
        return result;
    }
    
    /**
     * Calls API method {@link javax.jdo.Query#setRange(String)} or
     * {@link javax.jdo.Query#setRange(long, long)} depending on
     * which of the from/to fields are set.
     * @param query the query instance
     */
    private void rangeToAPI(Query query) {
        if (this.fromString != null && this.toString != null) {
            query.setRange(this.fromString + ',' + this.toString);
        } 
        else if (this.fromLong != null && this.toLong != null) {
            query.setRange(this.fromLong.longValue(), this.toLong.longValue());
        }
    }

    /**
     * Call JDOQLTypedQuery API method {@link javax.jdo.JDOQLTypedQuery#range(long, long)}.
     * @param query the JDOQLTypedQuery instance
     */
    private void rangeToJDOQLTypedQuery(JDOQLTypedQuery query) {
        if (this.fromString != null && this.toString != null) {
            query.range(Long.parseLong(this.fromString),  Long.parseLong(this.toString));
        }
        else if (this.fromLong != null && this.toLong != null) {
            query.range(this.fromLong.longValue(), this.toLong.longValue());
        }
    }
}
