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

import javax.jdo.PersistenceManager;

import org.apache.jdo.jdoql.JDOQueryException;

/**
 * This node represents a field access expression. Field access expression
 * have exactly one child, the target expression.
 * This expression can be an arbitrary expression.
 *
 * @author Michael Watzek
 */
public interface FieldAccessExpression extends IdentifierExpression
{
    /**
     * Returns the target expression of this field access.
     * The target expression can be an instance of
     * <code>ThisExpression</code> or an instance of an arbitrary other
     * <code>Expression</code>, e.g. <code>FieldAccessExpression</code>.
     * @return the target expression
     */
    public Expression getTarget();

    /**
     * Returns the value of the field corresponding with this
     * field access expression for the argument <code>object</code>.
     * @param pm the persistence manager of the query
     * @param object the instance for which to return the field value
     * @return the field value for <code>object</code>
     * @exception JDOQueryException if access to the corresponding field of this
     * expression is denied
     */
    public Object getFieldValue(PersistenceManager pm, Object object);
}

