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
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.jdoql.tree.Type;


/**
 * This node represents a type instance. A type instance wraps a
 * <code>java.lang.Class</code> instance which has been supplied by
 * the application. The following nodes have type instances as children:
 * <LI><code>CandidateClass</code>
 * <LI><code>CastExpr</code>
 * <LI><code>Decl</code>
 * <LI><code>StaticFieldAccessExpr</code>
 * The result type of a type instance is the wrapped <code>java.lang.Class</code>
 * instance. Type instances are not visible in query tree factory methods and
 * expression factory methods. They are internally created by in implementation
 * and are walked by a node visitor.
 *
 * @author Michael Watzek
 */
public final class TypeImpl extends NodeImpl implements Type
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public TypeImpl()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public TypeImpl(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param clazz the Java class which is wrapped by this instance
     */
    TypeImpl(Class clazz)
    {   super( JDOQLTokenTypes.TYPE, clazz.getName(), clazz );
    }

    /**
     * Returns the string representation of the Java class,
     * which is wrapped by this instance.
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
