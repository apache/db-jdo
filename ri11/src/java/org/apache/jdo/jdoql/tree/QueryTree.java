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

package org.apache.jdo.jdoql.tree;

import java.util.List;
import java.util.Map;

/**
 * Instances of classes implementing this interface
 * represent the root of a query tree.
 * You can use these intances to factorize this node's children, as there are
 * candidate class, declarations, filter expression and
 * ordering expressions as children.
 *
 * @author Michael Watzek
 */
public interface QueryTree extends ExpressionFactory, Node
{
    /**
     * Sets the candidate class for this query tree.
     * @param clazz the candidate class
     */
    public void setCandidateClass(Class clazz);

    /**
     * Declares a parameter for this query tree.
     * Once you have declared a parameter, you can access it using method
     * <code>newIdentifier</code>.
     * @param type the instance of a Java class which is the type of the declared parameter
     * @param parameter the name of the declared parameter
     */
    public void declareParameter(Class type, String parameter);

    /**
     * Declares a variable for this query tree.
     * Once you have declared a variable, you can access it using method
     * <code>newIdentifier</code>.
     * @param type the instance of a Java class which is the type of the declared variable
     * @param variable the name of the declared variable
     */
    public void declareVariable(Class type, String variable);

    /**
     * Sets the filter expression for this query tree.
     * @param filter the filter expression
     */
    public void setFilter(Expression filter);

    /**
     * Adds an ascending ordering expression to this query tree.
     * The order of adding ascending and descending ordering expressions defines
     * the order of instances in the result collection of an executed query
     * instance corresponding with this query tree.
     * @param expression the order expression
     */
    public void addAscendingOrdering(Expression expression);

    /**
     * Adds an descending ordering expression to this query tree.
     * The order of adding ascending and descending ordering expressions defines
     * the order of instances in the result collection of an executed query
     * instance corresponding with this query tree.
     * @param expression the order expression
     */
    public void addDescendingOrdering(Expression expression);

    /**
     * Returns the candidate class.
     * @return the candidate class
     */
    public Class getCandidateClass();


    /**
     * Returns a map containing all declared parameters.
     * This map contains parameter names as keys and instances of
     * <code>ParameterDeclaration</code> as values.
     * @return the map of declared parameters
     */
    public Map getDeclaredParameters();
    /**
     * Returns a map containing all declared variables.
     * This map contains variable names as keys and instances of
     * <code>VariableDeclaration</code> as values.
     * @return the map of declared variables
     */
    public Map getDeclaredVariables();

    /**
     * Returns a list of all declared parameters. The order of entries is
     * defined by the order of calls <code>declareParameter</code>.
     * This list contains instances of
     * <code>ParametersDeclaration</code> as entries.
     * @return the list of declared parameters
     */
    public List getDeclaredParametersAsList();

    /**
     * Returns the filter expression of this query tree.
     * @return the filter or null.
     */
    public Expression getFilter();

    /**
     * Returns a list of all added ordering expressions.
     * The order of entries is defined by the order of calls
     * <code>addAscendingOrdering</code> and <code>addDescendingOrdering</code>.
     * This list contains instances of
     * <code>OrderingExpression</code> as entries.
     * @return the list of declared parameters
     */
    public List getOrderingExpressions();
}
