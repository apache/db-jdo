/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.jdo.query;

/**
 * Representation of a numeric expression.
 *
 * @param <T> Number type
 */
public interface NumericExpression<T> extends ComparableExpression<T> {
  /**
   * Method to return an expression for this expression added to the passed expression.
   *
   * @param expr The other expression
   * @return The summation
   */
  NumericExpression<T> add(Expression<T> expr);

  /**
   * Method to return an expression for this expression added to the passed number.
   *
   * @param num Number to add
   * @return The summation
   */
  NumericExpression<T> add(Number num);

  /**
   * Method to return an expression for this expression subtracting the passed expression.
   *
   * @param expr The other expression
   * @return The difference
   */
  NumericExpression<T> sub(Expression<T> expr);

  /**
   * Method to return an expression for this expression subtracting the passed number.
   *
   * @param num Number to subtract
   * @return The difference
   */
  NumericExpression<T> sub(Number num);

  /**
   * Method to return an expression for this expression multiplied by the passed expression.
   *
   * @param expr The other expression
   * @return The multiplication
   */
  NumericExpression<T> mul(Expression<T> expr);

  /**
   * Method to return an expression for this expression multiplied by the passed number.
   *
   * @param num Number
   * @return The multiplication
   */
  NumericExpression<T> mul(Number num);

  /**
   * Method to return an expression for this expression divided by the passed expression.
   *
   * @param expr The other expression
   * @return The division
   */
  NumericExpression<T> div(Expression<T> expr);

  /**
   * Method to return an expression for this expression divided by the passed number.
   *
   * @param num Number to divide by
   * @return The division
   */
  NumericExpression<T> div(Number num);

  /**
   * Method to return an expression for this expression modulus the passed expression (
   *
   * <pre>a % b</pre>
   *
   * ).
   *
   * @param expr The other expression
   * @return The modulus
   */
  NumericExpression<T> mod(Expression<T> expr);

  /**
   * Method to return an expression for this expression modulus the passed number.
   *
   * @param num Number
   * @return The modulus
   */
  NumericExpression<T> mod(Number num);

  /**
   * Method to return an expression that is the current expression negated.
   *
   * @return The negated expression
   */
  NumericExpression<T> neg();

  /**
   * Method to return an expression that is the complement of the current expression.
   *
   * @return The complement expression
   */
  NumericExpression<T> com();

  /**
   * Method to return a numeric expression representing the aggregated average of this expression.
   *
   * @return Numeric expression for the average
   */
  NumericExpression<Double> avg();

  /**
   * Method to return a numeric expression representing the aggregated (distinct) average of this
   * expression.
   *
   * @return Numeric expression for the average
   */
  NumericExpression<Double> avgDistinct();

  /**
   * Method to return a numeric expression representing the aggregated sum of this expression.
   *
   * @return Numeric expression for the sum
   */
  NumericExpression<T> sum();

  /**
   * Method to return a numeric expression representing the aggregated (distinct) sum of this
   * expression.
   *
   * @return Numeric expression for the sum
   */
  NumericExpression<T> sumDistinct();

  /**
   * Method to return the absolute value expression of this expression.
   *
   * @return The absolute value expression
   */
  NumericExpression<T> abs();

  /**
   * Method to return the square-root value expression of this expression.
   *
   * @return The square-root value expression
   */
  NumericExpression<Double> sqrt();

  /**
   * Method to return the arc cosine value expression of this expression.
   *
   * @return The arc cosine value expression
   */
  NumericExpression<Double> acos();

  /**
   * Method to return the arc sine value expression of this expression.
   *
   * @return The arc sine value expression
   */
  NumericExpression<Double> asin();

  /**
   * Method to return the arc tangent value expression of this expression.
   *
   * @return The arc tangent value expression
   */
  NumericExpression<Double> atan();

  /**
   * Method to return the sine value expression of this expression.
   *
   * @return The sine value expression
   */
  NumericExpression<Double> sin();

  /**
   * Method to return the cosine value expression of this expression.
   *
   * @return The cosine value expression
   */
  NumericExpression<Double> cos();

  /**
   * Method to return the tangent value expression of this expression.
   *
   * @return The tangent value expression
   */
  NumericExpression<Double> tan();

  /**
   * Method to return the exponential value expression of this expression.
   *
   * @return The exponential value expression
   */
  NumericExpression<Double> exp();

  /**
   * Method to return the logarithm value expression of this expression.
   *
   * @return The logarithm value expression
   */
  NumericExpression<Double> log();

  /**
   * Method to return the ceiling value expression of this expression.
   *
   * @return The ceiling value expression
   */
  NumericExpression<T> ceil();

  /**
   * Method to return the floor value expression of this expression.
   *
   * @return The floor value expression
   */
  NumericExpression<T> floor();

  /**
   * Method to return a bitwise AND expression for this expression with the supplied bit path.
   *
   * @param bitExpr Bit expression
   * @return Bitwise AND expression
   */
  NumericExpression<T> bAnd(NumericExpression<T> bitExpr);

  /**
   * Method to return a bitwise OR expression for this expression with the supplied bit path.
   *
   * @param bitExpr Bit expression
   * @return Bitwise OR expression
   */
  NumericExpression<T> bOr(NumericExpression<T> bitExpr);

  /**
   * Method to return a bitwise XOR expression for this expression with the supplied bit path.
   *
   * @param bitExpr Bit expression
   * @return Bitwise XOR expression
   */
  NumericExpression<T> bXor(NumericExpression<T> bitExpr);
}
