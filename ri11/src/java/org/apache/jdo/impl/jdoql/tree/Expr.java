/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.impl.jdoql.tree;

import org.apache.jdo.jdoql.tree.Expression;

/**
 * This node represents a general expression. Examples of general expressions
 * are <code>UnaryExpression</code>, <code>BinaryExpression</code>,
 * <code>FieldAccessExpression</code>, <code>CastExpression</code>
 * and <code>MethodCallExpression</code>.
 *
 * @author Michael Watzek
 */
public abstract class Expr extends NodeImpl implements Expression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public Expr()
    {}

    /**
     * This constructor is called by specialized nodes.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     * @param clazz the Java type of this node
     */
    Expr(int tokenType, String tokenName, Class clazz)
    {   super( tokenType, tokenName, clazz );
    }
}
