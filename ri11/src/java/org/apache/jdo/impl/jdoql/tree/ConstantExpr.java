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

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLTokenTypes;
import org.apache.jdo.jdoql.tree.ConstantExpression;
import org.apache.jdo.jdoql.tree.NodeVisitor;


/**
 * This node represents a constant expression.
 * Examples of constant expressions are <code>BooleanLiteralExpression</code> or
 * <code>ByteLiteralExpression</code>. It does not have any children.
 *
 * @author Michael Watzek
 */
public class ConstantExpr extends Expr implements ConstantExpression
{
    /**
     * Returns an instance of <code>ConstantExpression</code>.
     * This method handles <code>null</code> as a constant expression.
     * @param value the object wrapped by the constant expression
     * @return the constant expression
     */
    public static ConstantExpr newConstant(Object value)
    {   ConstantExpr constant;
        if( value instanceof Boolean )
            constant = new BooleanLiteralExpr( (Boolean)value );
        else if( value instanceof Byte )
            constant = new ByteLiteralExpr( (Byte)value );
        else if( value instanceof Character )
            constant = new CharLiteralExpr( (Character)value );
        else if( value instanceof Double )
            constant = new DoubleLiteralExpr( (Double)value );
        else if( value instanceof Float )
            constant = new FloatLiteralExpr( (Float)value );
        else if( value instanceof Integer )
            constant = new IntLiteralExpr( (Integer)value );
        else if( value instanceof Long )
            constant = new LongLiteralExpr( (Long)value );
        else if( value instanceof Short )
            constant = new ShortLiteralExpr( (Short)value );
        else
            constant = new ConstantExpr( value );
        return constant;
    }

    Object value = null;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ConstantExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ConstantExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by specialized nodes.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     */
    ConstantExpr(int tokenType, String tokenName, Object value)
    {   super( tokenType, tokenName, value==null?null:value.getClass() );
        this.value = value;
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param value the value represented by this expression
     */
    ConstantExpr(Object value)
        {   this( JDOQLTokenTypes.CONSTANT, "Constant", value ); // NOI18N
    }

    /**
     * Returns the value represented by this expression.
     * @return the value
     */
    public Object getValue()
    {   return this.value;
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
