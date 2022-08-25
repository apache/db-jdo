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
 * Representation of an Optional type in a query.
 */
public interface OptionalExpression<T> extends ComparableExpression<java.util.Optional<T>>
{
    /**
     * Accessor for the object within the Optional.
     * @return Expression for the object
     */
    Expression<T> get();

    /**
     * Accessor for whether there is an object within the Optional.
     * @return Whether there is an expression
     */
    BooleanExpression isPresent();
}