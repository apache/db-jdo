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

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLTokenTypes;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.CastExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.jdoql.tree.Type;



/**
 * This node represents a cast expression. It has a result type and a child
 * which corresponds with the expression to cast.
 *
 * @author Michael Watzek
 */
public final class CastExpr extends Expr implements CastExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public CastExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public CastExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param type the type instance wrapping the Java class to which
     * the argument <code>expr</code> is casted
     * @param expr the expression to cast
     * @exception JDOQueryException if the result type of expr cannot
     * be casted to clazz
     */
    CastExpr(Type type, Expression expr)
    {   super( JDOQLTokenTypes.CAST, "Cast", type.getJavaClass() ); //NOI18N
        setChildren( new Node[] {type, expr} );
        if( expr.getJavaClass()!=null &&
            !clazz.isAssignableFrom(expr.getJavaClass()) &&
            !expr.getJavaClass().isAssignableFrom(clazz) )
            throw new JDOQueryException( 
                msg.msg("EXC_IllegalCast", expr.getJavaClass().getName(), 
                        clazz.getName(), this) ); //NOI18N
    }

    /**
     * Returns the string representation of the Java class,
     * to which this node's expression is casted.
     * @return the Java type name
     */
    public String getTypeName()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return ((Type)this.children[0]).getTypeName();
    }

    /**
     * Returns the node's cast expression.
     * @return the node's cast expression
     */
    public Expression getExpression()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<2 )
            return null;
        return (Expression) this.children[1];
    }

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     */
    public void arrive(NodeVisitor visitor)
    {   visitor.arrive( this );
    }

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     * @param results the result array
     * @return the object returned by the visitor instance
     */
    public Object leave(NodeVisitor visitor, Object[] results)
    {   return visitor.leave( this, results );
    }
}
