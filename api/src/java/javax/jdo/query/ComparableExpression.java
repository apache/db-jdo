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
package javax.jdo.query;

/**
 * Representation of an expression for a Java type that implements java.lang.Comparable.
 *
 * @param <T> Java type being represented here
 */
public interface ComparableExpression<T> extends Expression<T>
{
    /**
     * Method returning whether this expression is less than the other expression.
     * @param expr Other expression
     * @return Whether this is less than the other
     */
    BooleanExpression lt(ComparableExpression expr);

    /**
     * Method returning whether this expression is less than the literal.
     * @param t literal
     * @return Whether this is less than the other
     */
    BooleanExpression lt(T t);

    /**
     * Method returning whether this expression is less than or equal the other expression.
     * @param expr Other expression
     * @return Whether this is less than or equal the other
     */
    BooleanExpression lteq(ComparableExpression expr);

    /**
     * Method returning whether this expression is less than or equal the literal.
     * @param t literal
     * @return Whether this is less than or equal the other
     */
    BooleanExpression lteq(T t);

    /**
     * Method returning whether this expression is greater than the other expression.
     * @param expr Other expression
     * @return Whether this is greater than the other
     */
    BooleanExpression gt(ComparableExpression expr);

    /**
     * Method returning whether this expression is greater than the literal.
     * @param t literal
     * @return Whether this is greater than the other
     */
    BooleanExpression gt(T t);

    /**
     * Method returning whether this expression is greater than or equal the other expression.
     * @param expr Other expression
     * @return Whether this is greater than or equal to the other
     */
    BooleanExpression gteq(ComparableExpression expr);

    /**
     * Method returning whether this expression is greater than or equal the literal.
     * @param t literal
     * @return Whether this is greater than or equal to the other
     */
    BooleanExpression gteq(T t);

    /**
     * Method to return a numeric expression representing the aggregated minimum of this expression.
     * @return Numeric expression for the minimum
     */
    NumericExpression min();

    /**
     * Method to return a numeric expression representing the aggregated maximum of this expression.
     * @return Numeric expression for the maximum
     */
    NumericExpression max();

    /**
     * Method to return an order expression for this expression in ascending order.
     * @return The order expression
     */
    OrderExpression asc();

    /**
     * Method to return an order expression for this expression in descending order.
     * @return The order expression
     */
    OrderExpression desc();
}