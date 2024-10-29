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

import java.io.Closeable;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.jdo.query.BooleanExpression;
import javax.jdo.query.CharacterExpression;
import javax.jdo.query.CollectionExpression;
import javax.jdo.query.DateExpression;
import javax.jdo.query.DateTimeExpression;
import javax.jdo.query.Expression;
import javax.jdo.query.IfThenElseExpression;
import javax.jdo.query.ListExpression;
import javax.jdo.query.MapExpression;
import javax.jdo.query.NumericExpression;
import javax.jdo.query.OrderExpression;
import javax.jdo.query.PersistableExpression;
import javax.jdo.query.StringExpression;
import javax.jdo.query.TimeExpression;

/**
 * Interface for a type-safe refactorable JDOQL query, using a fluent API, based around expressions.
 * Note that a JDOQLTypedQuery only supports named parameters, and the values of these parameters
 * should be set using the relevant setter methods prior to execution, and that the values for the
 * parameters will only be retained until the subsequent query execution.
 *
 * @param <T> candidate type for this query
 */
public interface JDOQLTypedQuery<T> extends Serializable, Closeable {
  public static final String QUERY_CLASS_PREFIX = "Q";

  /**
   * Method to return an expression for the candidate of the query. Cast the returned expression to
   * the candidate "Q" type to be able to call methods on it. This calls the method
   * "Q{type}.candidate(null)" The preference is to use the "Q{type}.candidate()" method for real
   * type-safe handling.
   *
   * @return Expression for the candidate
   * @deprecated Users should use the static public method Q{type}.candidate("this")
   */
  PersistableExpression<T> candidate();

  /**
   * Method to return a parameter for the query. Cast the returned parameter to the right type to be
   * able to call methods on it. The preference is to use the "xxxParameter(String)" methods for
   * real type-safe handling.
   *
   * @param name Name of the parameter
   * @param type Java type of the parameter
   * @return Expression for the parameter
   * @param <P> type for the parameter
   */
  <P> Expression<P> parameter(String name, Class<P> type);

  /**
   * Method to return a string parameter for the query.
   *
   * @param name Name of the parameter
   * @return StringExpression for the parameter
   */
  StringExpression stringParameter(String name);

  /**
   * Method to return a character parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  CharacterExpression characterParameter(String name);

  /**
   * Method to return a numeric parameter for the query.
   *
   * @param name Name of the parameter
   * @return NumericExpression for the parameter
   */
  NumericExpression<?> numericParameter(String name);

  /**
   * Method to return a numeric parameter for the query.
   *
   * @param name Name of the parameter
   * @param type Type of the numeric parameter
   * @param <N> Type for the numeric parameter
   * @return NumericExpression for the parameter
   */
  @SuppressWarnings("unchecked")
  default <N> NumericExpression<N> numericParameter(String name, Class<N> type) {
    return (NumericExpression<N>) numericParameter(name);
  }

  /**
   * Method to return a date parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  DateExpression dateParameter(String name);

  /**
   * Method to return a time parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  TimeExpression timeParameter(String name);

  /**
   * Method to return a datetime parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  DateTimeExpression datetimeParameter(String name);

  /**
   * Method to return a collection parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  CollectionExpression<?, ?> collectionParameter(String name);

  /**
   * Method to return a collection parameter for the query.
   *
   * @param name Name of the parameter
   * @param elementType Element type of the collection parameter
   * @param <E> Element type for the collection parameter
   * @return Expression for the parameter
   */
  @SuppressWarnings("unchecked")
  default <E> CollectionExpression<Collection<E>, E> collectionParameter(
      String name, Class<E> elementType) {
    return (CollectionExpression<Collection<E>, E>) collectionParameter(name);
  }

  /**
   * Method to return a map parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  MapExpression<?, ?, ?> mapParameter(String name);

  /**
   * Method to return a map parameter for the query.
   *
   * @param name Name of the parameter
   * @param keyType Key type of the map parameter
   * @param valueType Value type of the map parameter
   * @param <K> Key type for the map parameter
   * @param <V> Value type for the map parameter
   * @return Expression for the parameter
   */
  @SuppressWarnings("unchecked")
  default <K, V> MapExpression<Map<K, V>, K, V> mapParameter(
      String name, Class<K> keyType, Class<V> valueType) {
    return (MapExpression<Map<K, V>, K, V>) mapParameter(name);
  }

  /**
   * Method to return a list parameter for the query.
   *
   * @param name Name of the parameter
   * @return Expression for the parameter
   */
  ListExpression<?, ?> listParameter(String name);

  /**
   * Method to return a list parameter for the query.
   *
   * @param name Name of the parameter
   * @param elementType Element type of the list parameter
   * @param <E> Element type for the list parameter
   * @return Expression for the parameter
   */
  @SuppressWarnings("unchecked")
  default <E> ListExpression<List<E>, E> listParameter(String name, Class<E> elementType) {
    return (ListExpression<List<E>, E>) listParameter(name);
  }

  /**
   * Method to return a variable for this query. Cast the returned variable to the right type to be
   * able to call methods on it.
   *
   * @param name Name of the variable
   * @param type Type of the variable
   * @return Expression for the variable
   * @param <V> type for the variable
   * @deprecated Users should use the static public method Q{type}.variable(String name, Expression exp)
   */
  <V> Expression<V> variable(String name, Class<V> type);

  /**
   * Method to set the candidates to use over which we are querying. If no candidates are set then
   * the query is performed on the datastore.
   *
   * @param candidates The candidates
   * @return The query
   */
  JDOQLTypedQuery<T> setCandidates(Collection<T> candidates);

  /**
   * Method to remove subclasses (of the candidate) from the query
   *
   * @return The query
   */
  JDOQLTypedQuery<T> excludeSubclasses();

  /**
   * Method to include subclasses (of the candidate) to the query
   *
   * @return The query
   */
  JDOQLTypedQuery<T> includeSubclasses();

  /**
   * Method to set the filter of the query.
   *
   * @param expr Filter expression
   * @return The query
   */
  JDOQLTypedQuery<T> filter(BooleanExpression expr);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param type The type returned by the IfElse.
   * @param cond The if condition
   * @param thenValueExpr Expression for value to return when the if expression is met
   * @param elseValueExpr Expression for value to return when the if expression is not met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThenElse(
      Class<V> type,
      BooleanExpression cond,
      Expression<V> thenValueExpr,
      Expression<V> elseValueExpr);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param cond The if condition
   * @param thenValue Value to return when the if expression is met
   * @param elseValueExpr Expression to return when the if expression is not met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThenElse(
      BooleanExpression cond, V thenValue, Expression<V> elseValueExpr);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param cond The if condition
   * @param thenValueExpr Expression to return when the if expression is met
   * @param elseValue Value to return when the if expression is not met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThenElse(
      BooleanExpression cond, Expression<V> thenValueExpr, V elseValue);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param cond The if condition
   * @param thenValue Value to return when the if expression is met
   * @param elseValue Value to return when the if expression is not met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThenElse(BooleanExpression cond, V thenValue, V elseValue);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param type The type returned by the IfElse.
   * @param cond The if condition
   * @param thenValueExpr Expression for value to return when the if expression is met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThen(
      Class<V> type, BooleanExpression cond, Expression<V> thenValueExpr);

  /**
   * Method to return an "IF (...) ... ELSE ..." expression for use in this query.
   *
   * @param cond The if condition
   * @param thenValue Value to return when the if expression is met
   * @param <V> type for the IfThenElseExpression
   * @return The IfThenElse expression
   */
  <V> IfThenElseExpression<V> ifThen(BooleanExpression cond, V thenValue);

  /**
   * Method to set the grouping(s) for the query.
   *
   * @param exprs Grouping expression(s)
   * @return The query
   */
  JDOQLTypedQuery<T> groupBy(Expression<?>... exprs);

  /**
   * Method to set the having clause of the query.
   *
   * @param expr Having expression
   * @return The query
   */
  JDOQLTypedQuery<T> having(Expression<?> expr);

  /**
   * Method to set the ordering of the query.
   *
   * @param orderExprs Ordering expression(s)
   * @return The query
   */
  JDOQLTypedQuery<T> orderBy(OrderExpression<?>... orderExprs);

  /**
   * Method to set the result of the query.
   *
   * @param distinct Whether results are distinct
   * @param exprs The result expressions
   * @return The query
   */
  JDOQLTypedQuery<T> result(boolean distinct, Expression<?>... exprs);

  /**
   * Method to set the range of any required results, using expressions.
   *
   * @param lowerInclExpr The position of the first result (inclusive)
   * @param upperExclExpr The position of the last result (exclusive)
   * @return The query
   */
  JDOQLTypedQuery<T> range(NumericExpression<?> lowerInclExpr, NumericExpression<?> upperExclExpr);

  /**
   * Method to set the range of any required results, using long values.
   *
   * @param lowerIncl The position of the first result (inclusive)
   * @param upperExcl The position of the last result (exclusive)
   * @return The query
   */
  JDOQLTypedQuery<T> range(long lowerIncl, long upperExcl);

  /**
   * Method to set the range of any required results, using parameters (expressions).
   *
   * @param paramLowerInclExpr Expression for a parameter defining the position of the first result
   *     (inclusive)
   * @param paramUpperExclExpr Expression for a parameter defining the position of the last result
   *     (exclusive)
   * @return The query
   */
  JDOQLTypedQuery<T> range(Expression<?> paramLowerInclExpr, Expression<?> paramUpperExclExpr);

  /**
   * Method to return a subquery for use in this query using the same candidate class as this query.
   * To obtain the expression for the subquery to link it back to this query, call "result(...)" on
   * the subquery.
   *
   * @param candidateAlias Alias for the candidate
   * @return The subquery
   */
  JDOQLTypedSubquery<T> subquery(String candidateAlias);

  /**
   * Method to return a subquery for use in this query. To obtain the expression for the subquery to
   * link it back to this query, call "result(...)" on the subquery.
   *
   * @param candidate Candidate for the subquery
   * @param candidateAlias Alias for the candidate
   * @return The subquery
   * @param <S> Candidate type for subquery
   */
  <S> JDOQLTypedSubquery<S> subquery(Class<S> candidate, String candidateAlias);

  /**
   * Method to return a correlated subquery for use in this query. The candidate collection of the
   * subquery is defined using a collection relationship of the outer query. To obtain the
   * expression for the subquery to link it back to this query, call "result(...)" on the subquery.
   *
   * @param candidateCollection Expression defining the candidate collection for the subquery
   * @param candidate Candidate for the subquery
   * @param candidateAlias Alias for the candidate
   * @return The subquery
   * @param <E> Candidate type for subquery
   */
  <E> JDOQLTypedSubquery<E> subquery(
      CollectionExpression<Collection<E>, E> candidateCollection,
      Class<E> candidate,
      String candidateAlias);

  /**
   * Method to set the named parameters on this query prior to execution. All parameter values
   * specified in this method will only be retained until the subsequent query execution.
   *
   * @param namedParamMap The map of parameter values keyed by their names.
   * @return This query
   */
  JDOQLTypedQuery<T> setParameters(Map<String, ?> namedParamMap);

  /**
   * Method to set a parameter value for the specified (parameter) expression when executing the
   * query. All parameter values specified in this method will only be retained until the subsequent
   * query execution.
   *
   * @param paramExpr Expression defining the parameter
   * @param value The value
   * @return The query
   */
  JDOQLTypedQuery<T> setParameter(Expression<?> paramExpr, Object value);

  /**
   * Method to set the value for a named parameter for use when executing the query. All parameter
   * values specified in this method will only be retained until the subsequent query execution.
   *
   * @param paramName Parameter name
   * @param value The value
   * @return The query
   */
  JDOQLTypedQuery<T> setParameter(String paramName, Object value);

  /**
   * Method to execute the query where there are (potentially) multiple rows and we are returning
   * the candidate type. Any parameters required should be set prior to calling this method, using
   * one of the <cite>setParameter</cite> methods.
   *
   * <p>Calling this method with a result being specified will result in JDOUserException being
   * thrown.
   *
   * @return The results
   */
  List<T> executeList();

  /**
   * Method to execute the query where there is a single row and we are returning the candidate
   * type. Any parameters required should be set prior to calling this method, using one of the
   * <cite>setParameter</cite> methods.
   *
   * <p>Calling this method with a result being specified will result in JDOUserException being
   * thrown.
   *
   * @return The result
   */
  T executeUnique();

  /**
   * Method to execute the query where there are (potentially) multiple rows and we are returning
   * either the result type. Any parameters required should be set prior to calling this method,
   * using one of the <cite>setParameters</cite> methods.
   *
   * <p>Calling this method with no result being specified will result in JDOUserException being
   * thrown.
   *
   * @param resultCls Result class
   * @return The results
   * @param <R> result type
   */
  <R> List<R> executeResultList(Class<R> resultCls);

  /**
   * Method to execute the query where there is a single row and we are returning either the result
   * type. Any parameters required should be set prior to calling this method, using one of the
   * <cite>setParameter</cite> methods.
   *
   * <p>Calling this method with no result being specified will result in JDOUserException being
   * thrown.
   *
   * @param resultCls Result class
   * @return The result
   * @param <R> result type
   */
  <R> R executeResultUnique(Class<R> resultCls);

  /**
   * Method to execute the query where there are (potentially) multiple rows and we have a result
   * defined but no result class. Any parameters required should be set prior to calling this
   * method, using one of the <cite>setParameter</cite> methods.
   *
   * <p>Calling this method with no result being specified will result in JDOUserException being
   * thrown.
   *
   * @return The results
   */
  List<Object> executeResultList();

  /**
   * Method to execute the query where there is a single row and we have a result defined but no
   * result class. Any parameters required should be set prior to calling this method, using one of
   * the <cite>setParameters</cite> methods.
   *
   * <p>Calling this method with no result being specified will result in JDOUserException being
   * thrown.
   *
   * @return The results
   */
  Object executeResultUnique();

  /**
   * Method to execute the query deleting the affected instances. Any parameters required should be
   * set prior to calling this method, using one of the <cite>setParameter</cite> methods.
   *
   * @return The number of objects deleted
   */
  long deletePersistentAll();

  /**
   * Get the effective timeout setting for read operations. If the timeout has not been set on this
   * query explicitly, the effective datastore read timeout value from the persistence manager is
   * returned.
   *
   * @see PersistenceManager#setDatastoreReadTimeoutMillis(Integer)
   * @return the effective timeout setting (milliseconds).
   */
  Integer getDatastoreReadTimeoutMillis();

  /**
   * Set the datastore read timeout (millis).
   *
   * @param interval The interval
   * @return This query
   */
  JDOQLTypedQuery<T> datastoreReadTimeoutMillis(Integer interval);

  /**
   * Get the effective timeout setting for write operations. If the timeout has not been set on this
   * query explicitly, the effective datastore write timeout value from the persistence manager is
   * returned.
   *
   * @see PersistenceManager#setDatastoreWriteTimeoutMillis(Integer)
   * @return the effective timeout setting (milliseconds).
   */
  Integer getDatastoreWriteTimeoutMillis();

  /**
   * Set the datastore write timeout (millis).
   *
   * @param interval The interval
   * @return This query
   */
  JDOQLTypedQuery<T> datastoreWriteTimeoutMillis(Integer interval);

  /**
   * Return the current value of the serializeRead property.
   *
   * @return the value of the serializeRead property
   */
  Boolean getSerializeRead();

  /**
   * Set whether we to lock all objects read by this query.
   *
   * @param serialize Whether to lock
   * @return This query
   */
  JDOQLTypedQuery<T> serializeRead(Boolean serialize);

  /**
   * The unmodifiable flag, when set, disallows further modification of the query, except for
   * specifying the range, result class, and ignoreCache option.
   *
   * @return the current setting of the flag
   */
  boolean isUnmodifiable();

  /**
   * Set to make this query unmodifiable hereafter.
   *
   * @return This query
   */
  JDOQLTypedQuery<T> unmodifiable();

  /**
   * Get the ignoreCache option setting.
   *
   * @return the ignoreCache option setting.
   */
  boolean getIgnoreCache();

  /**
   * Set whether we to ignore the cache with this query.
   *
   * @param flag Whether to ignore the cache
   * @return This query
   */
  JDOQLTypedQuery<T> ignoreCache(boolean flag);

  /**
   * Specify an extension for this query.
   *
   * @param key The extension key
   * @param value The extension value
   * @return This query
   */
  JDOQLTypedQuery<T> extension(String key, Object value);

  /**
   * Specify a map of extensions for this query.
   *
   * @param values The extension map of keys and values
   * @return This query
   */
  JDOQLTypedQuery<T> extensions(Map<String, Object> values);

  /**
   * Save the query, as it is currently defined, as a named query under the specified name. If a
   * named query already exists under this name (either defined in metadata, or previously saved)
   * then it will be overwritten.
   *
   * @param name Name to save it under.
   * @return This query
   */
  JDOQLTypedQuery<T> saveAsNamedQuery(String name);

  /**
   * Accessor for the PersistenceManager for this query
   *
   * @return The PersistenceManager
   */
  PersistenceManager getPersistenceManager();

  /**
   * Accessor for the FetchPlan for this query
   *
   * @return The FetchPlan
   */
  FetchPlan getFetchPlan();

  /**
   * Method to cancel any executing queries. If the underlying datastore doesn't support
   * cancellation of queries this will throw JDOUnsupportedOptionException. If the cancellation
   * fails (e.g in the underlying datastore) then this will throw a JDOException.
   */
  void cancelAll();

  /**
   * Method to cancel an executing query in the specified thread. If the underlying datastore
   * doesn't support cancellation of queries this will throw JDOUnsupportedOptionException. If the
   * cancellation fails (e.g in the underlying datastore) then this will throw a JDOException.
   *
   * @param thread The thread to cancel
   */
  void cancel(Thread thread);

  /**
   * Method to close the specified query result.
   *
   * @param result The result
   */
  void close(Object result);

  /** Method to close all query results from this query. */
  void closeAll();

  /**
   * Method to return the equivalent String form of this query (if applicable for the query
   * language).
   *
   * @return The single-string form of this query
   */
  String toString();
}
