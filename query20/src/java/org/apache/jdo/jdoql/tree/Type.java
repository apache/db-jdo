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

/**
 * This node represents a type instance. A type instance wraps a <code>java.lang.Class</code>
 * instance which has been supplied by the application. The following nodes
 * have type instances as children:
 * <LI><code>CandidateClass</code>
 * <LI><code>CastExpression</code>
 * <LI><code>Declaration</code>
 * The result type of a type instance is the wrapped <code>java.lang.Class</code>
 * instance. Type instances are not visible in query tree factory methods and
 * expression factory methods. They are internally created by in implementation
 * and are walked by a node visitor.
 *
 * @author Michael Watzek
 */
public interface Type extends Node
{
    /**
     * Returns the string representation of the Java class,
     * which is wrapped by this instance.
     * @return the Java type name
     */
    public String getTypeName();
}
