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
 * Representation of an IF-THEN-ELSE expression.
 *
 * @param <T> Java type
 */
public interface IfThenElseExpression<T> extends ComparableExpression<T> {

    /**
     * Method to add an "IF (...) ..." clause.
     * If called multiple times, will add extra "IF (...) ..." or "ELSE IF (...) ..."
     * @param cond The if expression
     * @param thenValueExpr Expression to return when the if expression is met
     * @return This expression
     */
    IfThenElseExpression<T> ifThen(BooleanExpression cond, Expression<T> thenValueExpr);

    /**
     * Method to add an "IF (...) ..." clause.
     * If called multiple times, will add extra "IF (...) ..." or "ELSE IF (...) ..."
     * @param cond The if condition
     * @param thenValue Value to return when the if expression is met
     * @return This expression
     */
    IfThenElseExpression<T> ifThen(BooleanExpression cond, T thenValue);

    /**
     * Method to add the "ELSE ..." clause.
     * If called multiple times will replace the previous else clause
     * @param elseValueExpr Expression for value to return when the if expression is not met
     * @return This expression
     */
    IfThenElseExpression<T> elseEnd(Expression<T> elseValueExpr);

    /**
     * Method to add the "ELSE ..." clause.
     * If called multiple times will replace the previous else clause
     * @param elseValue Value to return when the if expression is not met
     * @return This expression
     */
    IfThenElseExpression<T> elseEnd(T elseValue);
    
}
