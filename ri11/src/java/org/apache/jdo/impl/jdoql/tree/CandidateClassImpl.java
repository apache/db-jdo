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
import org.apache.jdo.jdoql.tree.CandidateClass;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;


/**
 * This node represents the candidate class of a query.
 * The candidate class defines the type of instances in the
 * candidate collection on which the filter expression is applied.
 *
 * @author Michael Watzek
 */
public final class CandidateClassImpl extends NodeImpl implements CandidateClass
{
    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public CandidateClassImpl()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public CandidateClassImpl(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor and
     * sets the specified candidate class.
     * @param type the type instance wrapping the candidate class
     */
    CandidateClassImpl(TypeImpl type)
    {   super( JDOQLTokenTypes.CANDIDATE_CLASS, "CandidateClass", 
               type.getJavaClass() ); //NOI18N
        setChildren( new Node[] {type} );
    }

    /**
     * Sets the candidate class for this instance.
     * This method is used by semantic analysis only.
     * @param clazz the candidate clazz
     */
    public void setCandidateClass(Class clazz)
    {   ASTToChildren();
        if( this.children==null )
            setChildren( new Node[] {new TypeImpl(clazz)} );
        TypeImpl type = (TypeImpl) this.children[0];
        type.clazz = clazz;
        this.clazz = clazz;
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
