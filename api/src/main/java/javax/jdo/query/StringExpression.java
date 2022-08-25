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

/** Representation of a string in a query. */
public interface StringExpression extends ComparableExpression<String> {
  /**
   * Method to return an expression for this expression added to the passed expression (String
   * concatenation).
   *
   * @param expr The other expression
   * @return The summation
   */
  StringExpression add(Expression expr);

  /**
   * Method to return an expression for this expression added to the passed string (String
   * concatenation).
   *
   * @param str The other string
   * @return The summation
   */
  StringExpression add(String str);

  /**
   * Method to return an expression for the character at a position of this string expression.
   *
   * @param pos The position
   * @return Expression for the character
   */
  CharacterExpression charAt(int pos);

  /**
   * Method to return an expression for the character at a position of this string expression.
   *
   * @param pos The position
   * @return Expression for the character
   */
  CharacterExpression charAt(NumericExpression<Integer> pos);

  /**
   * Method returning an expression for whether this string expression ends with the passed string
   * expression.
   *
   * @param expr The expression that it ends with.
   * @return Whether it ends with the other string
   */
  BooleanExpression endsWith(StringExpression expr);

  /**
   * Method returning an expression for whether this string expression ends with the passed string
   * expression.
   *
   * @param str The string that it ends with.
   * @return Whether it ends with the other string
   */
  BooleanExpression endsWith(String str);

  /**
   * Method returning an expression for whether this string expression is equal to (ignoring case)
   * the passed string expression.
   *
   * @param expr The expression
   * @return Whether they are equal
   */
  BooleanExpression equalsIgnoreCase(StringExpression expr);

  /**
   * Method returning an expression for whether this string expression is equal to (ignoring case)
   * the passed string.
   *
   * @param str The string
   * @return Whether they are equal
   */
  BooleanExpression equalsIgnoreCase(String str);

  /**
   * Method to return an expression for the position of the passed string in this string.
   *
   * @param expr The other string
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(StringExpression expr);

  /**
   * Method to return an expression for the position of the passed string in this string.
   *
   * @param str The other string
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(String str);

  /**
   * Method to return an expression for the position of the passed string in this string after a
   * position.
   *
   * @param expr The other string
   * @param pos Start point of the search
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(StringExpression expr, NumericExpression<Integer> pos);

  /**
   * Method to return an expression for the position of the passed string in this string after a
   * position.
   *
   * @param str The other string
   * @param pos Start point of the search
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(String str, NumericExpression<Integer> pos);

  /**
   * Method to return an expression for the position of the passed string in this string after a
   * position.
   *
   * @param str The other string
   * @param pos Start point of the search
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(String str, int pos);

  /**
   * Method to return an expression for the position of the passed string in this string after a
   * position.
   *
   * @param expr The other string
   * @param pos Start point of the search
   * @return Expression for the position of the passed string
   */
  NumericExpression<Integer> indexOf(StringExpression expr, int pos);

  /**
   * Method returning a expression for the length of this string.
   *
   * @return Expression for the length
   */
  NumericExpression<Integer> length();

  /**
   * Method to return an expression for whether this string expression matches the provided
   * expression.
   *
   * @param expr The expression to match against
   * @return Whether this expression matches the provided expression
   */
  BooleanExpression matches(StringExpression expr);

  /**
   * Method to return an expression for whether this string expression matches the provided string.
   *
   * @param str String literal to match against
   * @return Whether this expression matches the provided string
   */
  BooleanExpression matches(String str);

  /**
   * Method returning an expression for whether this string expression starts with the passed string
   * expression.
   *
   * @param expr The expression that it starts with.
   * @return Whether it starts with the other string
   */
  BooleanExpression startsWith(StringExpression expr);

  /**
   * Method returning an expression for whether this string expression starts with the passed
   * string.
   *
   * @param str The string that it starts with.
   * @return Whether it starts with the other string
   */
  BooleanExpression startsWith(String str);

  /**
   * Method returning an expression for whether the substring of this string beginning at the
   * specified index starts with the passed string expression.
   *
   * @param expr The expression that it starts with.
   * @param index where to begin looking in this string
   * @return Whether it starts with the other string
   */
  BooleanExpression startsWith(StringExpression expr, int index);

  /**
   * Method returning an expression for whether the substring of this string beginning at the
   * specified index starts with the passed string.
   *
   * @param str The string that it starts with.
   * @param index where to begin looking in this string
   * @return Whether it starts with the other string
   */
  BooleanExpression startsWith(String str, int index);

  /**
   * Method to return an expression for the substring of this string expression.
   *
   * @param pos The position of the start point of the substring
   * @return Expression for the substring
   */
  StringExpression substring(NumericExpression<Integer> pos);

  /**
   * Method to return an expression for the substring of this string expression.
   *
   * @param pos The position of the start point of the substring
   * @return Expression for the substring
   */
  StringExpression substring(int pos);

  /**
   * Method to return an expression for the substring of this string expression.
   *
   * @param startPos The position of the start point of the substring (inclusive, origin 0)
   * @param endPos The position of the end point of the substring (exclusive, origin 0)
   * @return Expression for the substring
   */
  StringExpression substring(
      NumericExpression<Integer> startPos, NumericExpression<Integer> endPos);

  /**
   * Method to return an expression for the substring of this string expression.
   *
   * @param startPos The position of the start point of the substring (inclusive, origin 0)
   * @param endPos The position of the end point of the substring (exclusive, origin 0)
   * @return Expression for the substring
   */
  StringExpression substring(int startPos, int endPos);

  /**
   * Method to return a StringExpression representing this string expression in lower case.
   *
   * @return The lower case expression
   */
  StringExpression toLowerCase();

  /**
   * Method to return a StringExpression representing this string expression in upper case.
   *
   * @return The upper case expression
   */
  StringExpression toUpperCase();

  /**
   * Method returning a string expression with whitespace trimmed from start and end.
   *
   * @return String expression with whitespace trimmed
   */
  StringExpression trim();
}
