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

/** Representation of a boolean expression. */
public interface BooleanExpression extends ComparableExpression<Boolean> {
  /**
   * Method to return the AND of this expression and the other expression.
   *
   * @param expr The other expression
   * @return The resultant (boolean) expression
   */
  BooleanExpression and(BooleanExpression expr);

  /**
   * Method to return the OR of this expression and the other expression.
   *
   * @param expr The other expression
   * @return The resultant (boolean) expression
   */
  BooleanExpression or(BooleanExpression expr);

  /**
   * Method to negate this expression.
   *
   * @return The negated expression
   */
  BooleanExpression not();

  /**
   * Method to return an expression that is the current expression negated.
   *
   * @return The negated expression
   */
  BooleanExpression neg();
}
