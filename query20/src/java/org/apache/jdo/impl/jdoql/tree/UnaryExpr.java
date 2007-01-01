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

package org.apache.jdo.impl.jdoql.tree;

import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.UnaryExpression;

/**
 * This node represents a unary operator. All unary operators have exactly
 * one children. Examples of binary operators
 * are <code>ComplementExpression</code> and <code>NotExpression</code>.
 *
 * @author Michael Watzek
 */
public abstract class UnaryExpr extends Expr implements UnaryExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public UnaryExpr()
    {}

    /**
     * This constructor is called by specialized nodes.
     * It calls <code>setChildren</code> in order to initialize the node's
     * child <code>expr</code>.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     * @param expr the first child
     */
    UnaryExpr(int tokenType, String tokenName, Expression expr)
    {   super( tokenType, tokenName, expr.getJavaClass() );
        setChildren( new Node[] {expr} );
    }

    /**
     * Returns the node's expression.
     * @return the node's expression
     */
    public Expression getExpression()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return (Expression) this.children[0];
    }
}
