/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */
package javax.jdo.query;

import java.util.Map;

/**
 * Representation of a map in a query.
 *
 * @param <T> Java type being represented here
 * @param <K> Key type of the map being represented here
 * @param <V> Value type of the map being represented here
 */
public interface MapExpression<T extends Map<K, V>, K, V> extends Expression<T> {
  /**
   * Method returning an expression for the value of the specified key in the map.
   *
   * @param key The key expression
   * @return the value for the specified key
   */
  Expression<V> get(Expression<K> key);

  /**
   * Method returning an expression for the value of the specified key in the map.
   *
   * @param key The key expression
   * @return the value for the specified key
   */
  Expression<V> get(K key);

  /**
   * Method returning whether the specified key expression is contained in this map.
   *
   * @param expr The key expression
   * @return Whether it is contained here
   */
  BooleanExpression containsKey(Expression<K> expr);

  /**
   * Method returning whether the specified key is contained in this map.
   *
   * @param key The key
   * @return Whether it is contained here
   */
  BooleanExpression containsKey(K key);

  /**
   * Method returning whether the specified value expression is contained in this map.
   *
   * @param expr The value expression
   * @return Whether it is contained here
   */
  BooleanExpression containsValue(Expression<V> expr);

  /**
   * Method returning whether the specified value is contained in this map.
   *
   * @param value The value
   * @return Whether it is contained here
   */
  BooleanExpression containsValue(V value);

  /**
   * Method returning whether the specified entry expression is contained in this map.
   *
   * @param expr The entry expression
   * @return Whether it is contained here
   */
  BooleanExpression containsEntry(Expression<Map.Entry<K, V>> expr);

  /**
   * Method returning whether the specified entry is contained in this map.
   *
   * @param entry The entry expression
   * @return Whether it is contained here
   */
  BooleanExpression containsEntry(Map.Entry<K, V> entry);

  /**
   * Method returning whether the map is empty.
   *
   * @return Whether it is empty
   */
  BooleanExpression isEmpty();

  /**
   * Method returning an expression for the size of the map
   *
   * @return The size
   */
  NumericExpression<Integer> size();
}
