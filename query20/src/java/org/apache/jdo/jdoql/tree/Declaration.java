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
 * This node represents a declaration expression. Examples of
 * declarations expressions are
 * <code>ParameterDeclarationExpression</code> and
 * <code>VariableDeclarationExpression</code>. Declaration expressions
 * do not have any children.
 *
 * @author Michael Watzek
 */
public interface Declaration extends Node
{
    /**
     * Returns the name of the specialized declaration.
     * @return the name
     */
    public String getName();

    /**
     * Returns the Java type name of the specialized declaration.
     * @return the Java type name
     */
    public String getTypeName();
}
