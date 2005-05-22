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
import org.apache.jdo.jdoql.tree.MethodCallExpression;
import org.apache.jdo.jdoql.tree.Node;

/**
 * This node represents a method call expression.
 * Examples of method call expressions are
 * <code>ContainsCallExpression</code>, <code>IsEmptyCallExpression</code>,
 * <code>EndsWithCallExpression</code> and
 * <code>StartsWithCallExpression</code>.
 *
 * @author Michael Watzek
 */
public abstract class MethodCallExpr extends Expr implements MethodCallExpression
{
    Expression[] args;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public MethodCallExpr()
    {}

    /**
     * This constructor is called by specialized nodes.
     * It calls <code>setChildren</code> in order to initialize the node's
     * child <code>expr</code>.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     * @param clazz the Java type of this node
     * @param target the target expression of this method call
     * @param args the arguments of this method call
     */
    MethodCallExpr(int tokenType, String tokenName, Class clazz, Expression target, Expression[] args)
    {   super( tokenType, tokenName, clazz );
        this.args = args;
        int nrOfChildren = 1;
        if( args!=null )
            nrOfChildren += args.length;
        Node[] children = new Node[nrOfChildren];
        children[0] = target;
        if( args!=null )
            System.arraycopy( args, 0, children, 1, args.length );
        setChildren( children );
    }

    /**
     * Creates and returns a copy of this object.
     * @return the copy
     * @exception CloneNotSupportedException thrown by <code>super.clone()</code>
     */
    protected Object clone() throws CloneNotSupportedException
    {   MethodCallExpr copy = (MethodCallExpr) super.clone();
        copy.args = null;
        return copy;
    }

    /**
     * Returns the target expression of this method call.
     * The target expression can be an instance of
     * <code>ThisExpression</code> or an instance of an arbitrary other
     * <code>Expression</code>, e.g. <code>FieldAccessExpression</code>.
     * @return the target expression
     */
    public Expression getTarget()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return (Expression) this.children[0];
    }

    /**
     * Returns the method name.
     * @return the method name
     */
    public String getMethodName()
    {   return getText();
    }

    /**
     * Returns the argument array of this method call.
     * @return the argument array
     */
    public Expression[] getArguments()
    {   ASTToChildren();
        if( args==null &&
            this.children.length>1 )
        {   this.args = new Expression[this.children.length-1];
            System.arraycopy( this.children, 1, this.args, 0, this.args.length );
        }
        return args;
    }
}
