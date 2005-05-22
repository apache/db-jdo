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


import java.util.Collection;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLTokenTypes;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.IsEmptyCallExpression;
import org.apache.jdo.jdoql.tree.NodeVisitor;


/**
 * This node represents the method call expression
 * <code>Collection.isEmpty</code>. This node's child is a target
 * expression (e.g. an instance of <code>FieldAccessExpression</code>).
 *
 * @author Michael Watzek
 */
public final class IsEmptyCallExpr
    extends MethodCallExpr implements IsEmptyCallExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public IsEmptyCallExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public IsEmptyCallExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param target the target expression of this method call
     * @exception JDOQueryException if the result type of target is
     * not a collection type
     */
    IsEmptyCallExpr(Expression target)
    {   super( JDOQLTokenTypes.IS_EMPTY, "isEmpty", Boolean.class,
               target, null ); //NOI18N
        if( target.getJavaClass()!=null &&
            !Collection.class.isAssignableFrom(target.getJavaClass()) )
            throw new JDOQueryException(
                msg.msg("EXC_NoCollectionType", target, this) ); //NOI18N
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
