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

/*
 * VariableTable.java
 *
 * Created on September 11, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Declaration;



/**
 * The variable table.
 *
 * @author  Michael Bouschen
 * @version 0.1
 */
public class VariableTable
    extends AbstractValueTable
{
    /**
     * Returns a copy of this VariableTable.
     * @return a copy of this VariableTable.
     */
    public VariableTable getCopy()
    {
        try {
            return (VariableTable)clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_UnexpectedCloneProblems", ex)); //NOI18N
        }
    }
 
    /**
     * Internal method to check whether the specified identifier is declared. 
     */
    protected void checkDeclaredIdentifier(String name, Declaration decl)
    {
        if (decl == null)
            throw new JDOQueryException(
                msg.msg("EXC_UndefinedQueryVariable", name)); //NOI18N
    }
}

