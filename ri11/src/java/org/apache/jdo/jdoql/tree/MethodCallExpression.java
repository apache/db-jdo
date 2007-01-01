/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.jdoql.tree;

/**
 * This node represents a method call expression.
 * Examples of method call expressions are
 * <code>ContainsCallExpression</code>, <code>IsEmptyCallExpression</code>,
 * <code>EndsWithCallExpression</code> and
 * <code>StartsWithCallExpression</code>.
 *
 * @author Michael Watzek
 */
public interface MethodCallExpression extends Expression
{
    /**
     * Returns the target expression of this method call.
     * The target expression can be an instance of
     * <code>ThisExpression</code> or an instance of an arbitrary other
     * <code>Expression</code>, e.g. <code>FieldAccessExpression</code>.
     * @return the target expression
     */
    public Expression getTarget();

    /**
     * Returns the method name.
     * @return the method name
     */
    public String getMethodName();

    /**
     * Returns the argument array of this method call.
     * @return the argument array
     */
    public Expression[] getArguments();
}
