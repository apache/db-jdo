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
 * This node expression represents a binary operator. All binary operators have exactly
 * two children. Examples of binary operators
 * are <code>AndExpression</code> and <code>EqualsExpression</code>.
 *
 * @author Michael Watzek
 */
public interface BinaryExpression extends Expression
{
    /**
     * Returns the first child of this node.
     * @return the first child
     */
    public Expression getLeftExpression();

    /**
     * Returns the second child of this node.
     * @return the second child
     */
    public Expression getRightExpression();

    /**
     * Returns the class instance suiteable for implementing the result
     * of this expression. In case of integral binary expressions
     * that class instance is also the result type of the operation retrieved
     * by method <code>getJavaClass</code>. In case of relational binary
     * expressions, that class instance differs from the type retrieved by
     * <code>getJavaClass</code>, because relational binary expressions
     * have a boolean result type which does not depend of the operand types.
     * @return the common operand type
     */
    public Class getCommonOperandType();
}

