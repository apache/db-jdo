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

package org.apache.jdo.jdoql.tree;

import java.io.Serializable;

/**
 * This is the base interface of all nodes. Examples of nodes are
 * <CandidateClass</code>, <code>Declaration</code>, <code>Expression</code>
 * and <code>OrderingExpression<code>.
 *
 * @author Michael Watzek
 */
public interface Node extends Serializable
{
    /**
     * Returns the user object.
     * @return the ouser object
     */
    public Object getObject();

    /**
     * Sets the user object.
     * @param object the user object
     */
    public void setObject(Object object);

    /**
     * Returns this node's parent node.
     * @return the parent node
     */
    public Node getParent();

    /**
     * Sets the parent of this node.
     * @param parent the parent node
     */
    public void setParent(Node parent);

    /**
     * Returns this node's children.
     * @return the children
     */
    public Node[] getChildren();

    /**
     * Returns the Java type of this node.
     * @return the Java type
     */
    public Class getJavaClass();

    /**
     * Returns the token type of this node.
     * @return the token type
     */
    public int getTokenType();

    /**
     * This method is called by the tree walker when this node is walked
     * but its children have not been walked yet.
     * It delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     */
    public void arrive(NodeVisitor visitor);

    /**
     * This method is called by the tree walker when this node is walked
     * and all of its children have been walked.
     * It delegates to the argument <code>visitor</code>.
     * The argument <code>results</code> contains the result instances
     * returned by all <code>leave</code> methods of this node's children.
     * This method returns the result instance of the delegation call
     * of the argument <code>visitor</code>.
     * @param visitor the node visitor
     * @param results the result array containing result instances of
     * this node's children
     * @return the result instance of the delegation call
     * of the argument <code>visitor</code>
     */
    public Object leave(NodeVisitor visitor, Object[] results);

    /**
     * This method is called by the tree walker after walking each child
     * except the last child. 
     * It delegates to the argument <code>visitor</code>. The argument
     * <code>resultOfPreviousChild</code> contains the result instance
     * returned by the <code>leave</code> method of the last walked
     * child. The argument <code></code> indicates the index of the
     * next child in the children array returned by method
     * <code>getChildren</code>. If this method returns
     * <code>false</code> then the tree walker does not walk any more
     * children of this node. Instead, it calls method
     * <code>leave</code> immediately.
     * @param visitor the node visitor
     * @param resultOfPreviousChild the result computed by leaving the
     * previous child node
     * @param indexOfNextChild the index in the children array of the
     * next child to walk
     * @return <code>false</code> if remaining children should not be walked
     */
    public boolean walkNextChild(NodeVisitor visitor,
                                 Object resultOfPreviousChild,
                                 int indexOfNextChild);

}
