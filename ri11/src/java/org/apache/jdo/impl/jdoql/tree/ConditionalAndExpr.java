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
import org.apache.jdo.jdoql.tree.ConditionalAndExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.NodeVisitor;


/**
 * This node represents a conditional and operator.
 * A conditional and operator is a binary expression.
 * The string representation of this operator is <code>&&</code>.
 *
 * @author Michael Watzek
 */
public final class ConditionalAndExpr 
    extends BinaryExpr implements ConditionalAndExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ConditionalAndExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ConditionalAndExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param left the left operand
     * @param right the right operand
     */
    ConditionalAndExpr(Expression left, Expression right)
    {   super( JDOQLTokenTypes.AND, "ConditionalAnd", Boolean.class, 
               left, right ); //NOI18N
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

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     * @param resultOfPreviousChild the result computed by leaving the 
     * previous child node
     * @param indexOfNextChild the index in the children array of the 
     * next child to walk 
     * @return the boolean value returned by the visitor instance
     */
    public boolean walkNextChild(NodeVisitor visitor, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   return visitor.walkNextChild( this, resultOfPreviousChild, 
                                      indexOfNextChild );
    }
}
