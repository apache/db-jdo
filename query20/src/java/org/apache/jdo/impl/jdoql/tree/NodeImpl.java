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

import java.io.*;
import java.util.HashMap;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLAST;
import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.util.I18NHelper;

import antlr.collections.AST;


/**
 * This is the base class of all nodes. Examples of nodes are
 * <CandidateClass</code>, <code>Declaration</code>, <code>Expression</code>
 * and <code>OrderingExpression<code>. This class is not defined
 * <code>abstract</code> to allow the syntactical analysis to
 * construct general nodes, which are replaced by the semantic analysis
 * with their specialized counterparts.
 *
 * @author Michael Watzek
 */
public class NodeImpl extends JDOQLAST implements Node
{
    /** I18N support */
    final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", NodeImpl.class.getClassLoader()); //NOI18N

    /** RuntimeJavaModelFactory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    Node parent = null;
    Node[] children = null;
    Object object = null;
    transient Class clazz = null;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public NodeImpl()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public NodeImpl(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by specialized nodes.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     * @param clazz the Java type of this node
     */
    NodeImpl(int tokenType, String tokenName, Class clazz)
    {   initialize( tokenType, tokenName );
        this.clazz = clazz;
    }

    /**
     * Creates and returns a copy of this object nullifying fields
     * <code>parent</code>, <code>children</code> and <code>clazz</code>.
     * @return the copy
     * @exception CloneNotSupportedException thrown by <code>super.clone()</code>
     */
    protected Object clone() throws CloneNotSupportedException
    {   NodeImpl copy = (NodeImpl) super.clone();
        copy.parent = null;
        copy.children = null;
        return copy;
    }

    public String toString()
    {   if( getTypeInfo()==null )
            return getText();
        else
            return super.toString();
    }


    /**
     * Returns the Java type of this node.
     * @return the Java type
     */
    public Class getJavaClass()
    {   if( this.clazz==null &&
            getTypeInfo()!=null )
            this.clazz = javaModelFactory.getJavaClass(getTypeInfo());
        return this.clazz;
    }

    /**
     * Returns the token type of this node.
     * @return the token type
     */
    public int getTokenType()
    {   return getType();
    }

    /**
     * Returns the user object.
     * @return the ouser object
     */
    public Object getObject()
    {   return this.object;
    }

    /**
     * Sets the user object.
     * @param object the user object
     */
    public void setObject(Object object)
    {   this.object = object;
    }

    /**
     * Returns this node's parent node.
     * @return the parent node
     */
    public Node getParent()
    {   return this.parent;
    }

    /**
     * Sets the parent of this node.
     * @param parent the parent node
     */
    public void setParent(Node parent)
    {   this.parent = parent;
    }

    /**
     * Returns this node's children.
     * Ensures that this node's children corresponds with the underlying
     * ANTLR tree structure.
     * @return the children
     */
    public Node[] getChildren()
    {   ASTToChildren();
        return this.children;
    }

    /**
     * Implements a noop as a default implementation.
     * @param visitor the node visitor
     */
    public void arrive(NodeVisitor visitor)
    {}

    /**
     * Returns <code>null</code> as a default implementation.
     * @param visitor the node visitor
     * @param results the result array containing result instances of
     * this node's children
     */
    public Object leave(NodeVisitor visitor, Object[] results)
    {   return null;
    }

    /**
     * Returns <code>true</code> as a default implementation.
     * @param visitor the node visitor
     * @param resultOfPreviousChild the result computed by leaving the
     * previous child node
     * @param indexOfNextChild the index in the children array of the
     * next child to walk
     * @return <code>true</code>
     */
    public boolean walkNextChild(NodeVisitor visitor,
                                 Object resultOfPreviousChild,
                                 int indexOfNextChild)
    {   return true;
    }

    /**
     * Sets the children of this node specified by the argument
     * <code>children</code>.
     * It is called in constructors of specialized nodes. These
     * constructors are called by factory methods
     * when you build the tree yourself.
     * @param children the child nodes
     * @exception JDOQueryException if an instance in the children
     * array is <code>null</code> or already has a parent node
     */
    void setChildren(Node[] children)
    {   if( children!=null )
        {   setFirstChild( null );
            for( int i=0; i<children.length; i++ )
            {   Node current = children[i];
                if( current==null )
                    throw new JDOQueryException(
                        msg.msg("EXC_CannotProcessNullNodes", this) ); //NOI18N
                if( current.getParent()!=null )
                    throw new JDOQueryException(
                        msg.msg("EXC_CannotReuseNodes", current, 
                                current.getParent()) ); //NOI18N
                current.setParent( this );
                if( current instanceof AST )
                    addChild( (AST) current );
        }   }
        this.children = children;
        this.parent = null;
    }

    /**
     * This method initializes the children of this node
     * based on underlying ANTLR tree structure.
     * It is called in method <code>getChildren</code> of this class.
     */
    void ASTToChildren()
    {   if( this.children==null )
        {   int nrOfChildren;
            AST current = getFirstChild();
            for( nrOfChildren=0; current!=null; nrOfChildren++ )
                current = current.getNextSibling();
            if( nrOfChildren!=0 )
            {   this.children = new Node[nrOfChildren];
                current = getFirstChild();
                for( nrOfChildren=0; current!=null; nrOfChildren++ )
                {   this.children[nrOfChildren] = (Node) current;
                    current = current.getNextSibling();
        }   }   }
    }
}
