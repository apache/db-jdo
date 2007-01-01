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

import org.apache.jdo.jdoql.tree.Declaration;
import org.apache.jdo.jdoql.tree.IdentifierExpression;
import org.apache.jdo.jdoql.tree.NodeVisitor;

/**
 * This node represents an identifier expression.
 * Examples of identifier expressions are
 * <code>FieldAccessExpression</code>, <code>ParameterAccessExpression</code>,
 * <code>ThisExpression</code>or <code>VariableAccessExpression</code>.
 * This class is not defined
 * <code>abstract</code> to allow the syntactical analysis to
 * construct general nodes, which are replaced by the semantic analysis
 * with their specialized counterparts.
 *
 * @author Michael Watzek
 */
public class IdentifierExpr extends Expr implements IdentifierExpression
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public IdentifierExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public IdentifierExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by specialized nodes.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     * @param tokenType the token type
     * @param name the name of this identifier
     * @param clazz the Java type of this identifier
     */
    IdentifierExpr(int tokenType, String name, Class clazz)
    {   super( tokenType, name, clazz );
    }

    /**
     * Returns the name of the specialized identifier.
     * @return the name
     */
    public String getName()
    {   return getText();
    }

    /**
     * Returns the Java type name of the specialized identifier.
     * @return the Java type name
     */
    public String getTypeName()
    {   return getJavaClass().getName();
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
