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

/**
 * Expression representing the ordering using an expression and a direction.
 *
 * @param <T> Java type of the expression being represented here
 */
public interface OrderExpression<T> {
  public enum OrderDirection {
    ASC,
    DESC
  }

  public enum OrderNullsPosition {
    FIRST,
    LAST
  }

  /**
   * Accessor for the direction of the ordering with this expression.
   *
   * @return The direction
   */
  OrderDirection getDirection();

  /**
   * Accessor for the expression being used for ordering.
   *
   * @return Ordering expression
   */
  Expression<T> getExpression();

  /**
   * Accessor for the position of nulls with this expression.
   *
   * @return The nulls position (or null if not defined)
   */
  OrderNullsPosition getNullsPosition();

  /**
   * Method to set nulls to be ordered BEFORE non-nulls.
   *
   * @return The order expression
   */
  OrderExpression<T> nullsFirst();

  /**
   * Method to set nulls to be ordered AFTER non-nulls.
   *
   * @return The order expression
   */
  OrderExpression<T> nullsLast();
}
