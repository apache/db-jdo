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


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.BinaryExpression;
import org.apache.jdo.jdoql.tree.EqualsExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.NotEqualsExpression;

/**
 * This node represents a binary operator. All binary operators have exactly
 * two children. Examples of binary operators
 * are <code>AndExpression</code> and <code>EqualsExpression</code>.
 *
 * @author Michael Watzek
 */
public abstract class BinaryExpr extends Expr implements BinaryExpression
{
    transient Class commonOperandType;
    String commonOperandTypeName; // needed for serialization support only

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public BinaryExpr()
    {}

    /**
     * This constructor is called by specialized nodes.
     * It calls <code>setChildren</code> in order to initialize the node's
     * children <code>left</code> and <code>right</code>.
     * @param tokenType the token type
     * @param tokenName the name of this node
     * @param clazz the Java type of this node
     * @param left the first child
     * @param right the second child
     */
    BinaryExpr(int tokenType, String tokenName, Class clazz, Expression left, Expression right)
    {   super( tokenType, tokenName, clazz );
        this.commonOperandType = computeJavaClass( left.getJavaClass(), right.getJavaClass() );
        setChildren( new org.apache.jdo.jdoql.tree.Node[] {left, right} );
    }

    /**
     * Returns the first child of this node.
     * @return the first child
     */
    public Expression getLeftExpression()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return (Expression) this.children[0];
    }

    /**
     * Returns the second child of this node.
     * @return the second child
     */
    public Expression getRightExpression()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<2 )
            return null;
        return (Expression) this.children[1];
    }

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
    public Class getCommonOperandType()
    {   return this.commonOperandType;
    }

    /**
     * Sets the common operand type for this binary expression.
     * @param clazz the common operand type
     */
    public void setCommonOperandType(Class clazz)
    {   this.commonOperandType = clazz;
    }

    Class computeJavaClass( Class left, Class right )
    {   Class clazz = null;
        if( left!=null && right!=null )
        {   if( (BigDecimal.class.isAssignableFrom(left) &&
                 Number.class.isAssignableFrom(right)) ||
                (BigDecimal.class.isAssignableFrom(right) &&
                 Number.class.isAssignableFrom(left)) ||
                (BigInteger.class.isAssignableFrom(left) &&
                 (right==Float.class || right==Double.class)) ||
                (BigInteger.class.isAssignableFrom(right) &&
                 (left==Float.class || left==Double.class)) )
                clazz = BigDecimal.class;
            else if( (BigInteger.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (BigInteger.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) )
                clazz = BigInteger.class;
            else if( (Double.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (Double.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) )
                clazz = Double.class;
            else if( (Float.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (Float.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) )
                clazz = Float.class;
            else if( (Long.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (Long.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) )
                clazz = Long.class;
            else if( (Integer.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (Integer.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) ||
                     (Short.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) ||
                     (Short.class.isAssignableFrom(right) &&
                      Number.class.isAssignableFrom(left)) ||
                     (Byte.class.isAssignableFrom(left) &&
                      Number.class.isAssignableFrom(right)) )
                clazz = Integer.class;
            else if( Boolean.class.isAssignableFrom(left) &&
                     Boolean.class.isAssignableFrom(right) )
                clazz = Boolean.class;
            else if( this instanceof EqualsExpression ||
                     this instanceof NotEqualsExpression )
                clazz = Object.class;
            else
                throw new JDOQueryException( msg.msg("EXC_IncompatibleTypes", left, right, this) ); //NOI18N
        }
        return clazz;
    }
}
