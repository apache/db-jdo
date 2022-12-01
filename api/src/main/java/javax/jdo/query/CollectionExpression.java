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

import java.util.Collection;

/**
 * Representation of a collection in a query.
 *
 * @param <T> Java type being represented here
 * @param <E> Element type of the collection being represented here
 */
public interface CollectionExpression<T extends Collection<E>, E> extends Expression<T> {
  /**
   * Method returning whether the specified element expression is contained in this collection.
   *
   * @param expr The element expression
   * @return Whether it is contained here
   */
  BooleanExpression contains(Expression<E> expr);

  /**
   * Method returning whether the specified element is contained in this collection.
   *
   * @param elem The element
   * @return Whether it is contained here
   */
  BooleanExpression contains(E elem);

  /**
   * Method returning whether the collection is empty.
   *
   * @return Whether it is empty
   */
  BooleanExpression isEmpty();

  /**
   * Method returning an expression for the size of the collection
   *
   * @return The size
   */
  NumericExpression<Integer> size();
}
