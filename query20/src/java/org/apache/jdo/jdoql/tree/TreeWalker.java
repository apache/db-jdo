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

package org.apache.jdo.jdoql.tree;

/**
 * An instance of this class is used to walk any query node. It provides
 * a walk method which takes a node and a node visitor as arguments. It delegates
 * to methods <code>Node.arrive</code>, <code>Node.leave/code> and
 * <code>Node.walkNextChild</code>. Node implementations are required
 * to delegate these calls to corresponding methods of the supplied node
 * visitor.
 *
 * @author Michael Watzek
 */
public final class TreeWalker
{
    /**
     * Walks the tree specified by the argument <code>node</code>
     * implementing a depth first algorithm.
     * Executes the visitor instance callback <code>arrive</code>
     * when starting to walk the argument <code>node</code>.
     * Subsequently iterates that node's children and
     * executes the visitor instance callback <code>walkNextChild</code>
     * before the current child is walked. For the case that
     * <code>walkNextChild</code> returns <code>true</code>,
     * this tree walker walkes the next child.
     * Otherwise, the next child and all remaining childs are not walked.
     * In both cases, this tree walker
     * executes the visitor instance callback <code>leave</code> indicating
     * that the argument <code>node</code> and its children have been walked.
     * The result of <code>leave</code> is returned.
     * when starting to traverse a node.
     * @param node the tree instance
     * @param visitor the node visitor instance
     * @return the object returned by the visitor instance callback <code>leave</code>
     */
    public Object walk(Node node, NodeVisitor visitor)
    {
        if( node!=null )
        {   node.arrive( visitor );
            Object[] results = null;
            Node[] children = node.getChildren();
            if( children!=null )
            {   results = new Object[children.length];
                for( int i=0; i<children.length; i++ )
                {   Object resultOfPrevChild = i==0 ? null : results[i-1];
                    if( !node.walkNextChild(visitor, resultOfPrevChild, i) )
                        break;
                    results[i] = walk( children[i], visitor );
            }   }
            return node.leave( visitor, results );
        }
        return null;
    }
}
