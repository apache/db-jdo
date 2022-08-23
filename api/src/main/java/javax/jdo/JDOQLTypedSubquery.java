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
package javax.jdo;

import java.io.Serializable;

import javax.jdo.query.BooleanExpression;
import javax.jdo.query.CharacterExpression;
import javax.jdo.query.CollectionExpression;
import javax.jdo.query.DateExpression;
import javax.jdo.query.DateTimeExpression;
import javax.jdo.query.Expression;
import javax.jdo.query.NumericExpression;
import javax.jdo.query.PersistableExpression;
import javax.jdo.query.StringExpression;
import javax.jdo.query.TimeExpression;

/**
 * Interface for a type-safe subquery, using a fluent API. Users should call methods on the subquery
 * instance and end with a select of what the subquery returns; this returns the expression that
 * they use to link it with the owning query.
 *
 * @param <T> (Candidate) type being represented
 */
public interface JDOQLTypedSubquery<T> extends Serializable {
  /**
   * Method to return an expression for the candidate of the subquery. Cast the returned expression
   * to the candidate "Q" type to be able to call methods on it.
   *
   * @return Expression for the candidate
   */
  PersistableExpression<T> candidate();

  /**
   * Method to set the filter of the query.
   *
   * @param expr Filter expression
   * @return The query
   */
  JDOQLTypedSubquery<T> filter(BooleanExpression expr);

  /**
   * Method to set the grouping(s) for the query.
   *
   * @param exprs Grouping expression(s)
   * @return The query
   */
  JDOQLTypedSubquery<T> groupBy(Expression<?>... exprs);

  /**
   * Method to set the having clause of the query.
   *
   * @param expr Having expression
   * @return The query
   */
  JDOQLTypedSubquery<T> having(Expression<?> expr);

  /**
   * Accessor for the subquery (numeric) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   * @param <S> numeric type
   */
  <S> NumericExpression<S> selectUnique(NumericExpression<S> expr);

  /**
   * Accessor for the subquery (string) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  StringExpression selectUnique(StringExpression expr);

  /**
   * Accessor for the subquery (date) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  DateExpression selectUnique(DateExpression expr);

  /**
   * Accessor for the subquery (datetime) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  DateTimeExpression selectUnique(DateTimeExpression expr);

  /**
   * Accessor for the subquery (time) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  TimeExpression selectUnique(TimeExpression expr);

  /**
   * Accessor for the subquery (character) expression from the subquery when the subquery returns a
   * single value.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  CharacterExpression selectUnique(CharacterExpression expr);

  /**
   * Accessor for the subquery (collection) expression from the subquery.
   *
   * @param expr The expression
   * @return Expression for the typesafe query
   */
  CollectionExpression<?, ?> select(CollectionExpression<?, ?> expr);
}
