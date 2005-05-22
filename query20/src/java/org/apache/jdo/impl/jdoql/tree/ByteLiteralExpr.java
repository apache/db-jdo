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
import org.apache.jdo.jdoql.tree.ByteLiteralExpression;
import org.apache.jdo.jdoql.tree.NodeVisitor;


/**
 * This node represents a byte literal. It does not have any children.
 *
 * @author Michael Watzek
 */
public final class ByteLiteralExpr
    extends ConstantExpr implements ByteLiteralExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ByteLiteralExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public ByteLiteralExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param b the byte value
     */
    ByteLiteralExpr(Byte b)
    {   super( JDOQLTokenTypes.BYTE_LITERAL, b.toString(), b ); //NOI18N
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param b the byte value
     */
    ByteLiteralExpr(byte b)
    {   this( new Byte(b) );
    }

    /**
     * Returns the byte value represented by this expression.
     * @return the byte value
     */
    public byte getByte()
    {   return ((Byte)this.value).byteValue();
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
