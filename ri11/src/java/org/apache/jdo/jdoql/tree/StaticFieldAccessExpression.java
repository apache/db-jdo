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

import javax.jdo.PersistenceManager;

import org.apache.jdo.jdoql.JDOQueryException;

/**
 * This node represents a static field access expression.
 * It inherits from <code>IdentifierExpression</code>.
 * Static Field access expressions have exactly one child, the type of the clazz
 * containing the static field.
 *
 * @author Michael Watzek
 */
public interface StaticFieldAccessExpression extends IdentifierExpression
{
    /**
     * Returns the value of the field corresponding with this static
     * field access expression.
     * @param pm the persistence manager of the query
     * @return the field value
     * @exception JDOQueryException if access to the corresponding field of this
     * expression is denied
     */
    public Object getFieldValue(PersistenceManager pm);
}

