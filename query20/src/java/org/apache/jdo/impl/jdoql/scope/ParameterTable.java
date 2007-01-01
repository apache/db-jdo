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

/*
 * ParameterTable.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

import java.util.*;

import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.impl.jdoql.QueryResultHelperImpl;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Declaration;
import org.apache.jdo.pm.PersistenceManagerInternal;


/**
 * The query parameter table.
 *
 * @author  Michael Bouschen
 */
public class ParameterTable
    extends AbstractValueTable
{
    /**
     * List of query parameter names. The query parameter tables stores the 
     * query parameter names in an extra list to presever the order of 
     * parameter declarations.
     */
    List names = new ArrayList();

    /**
     * Adds a new declaration. The value is initially set to UNDEFINED.
     * @param decl the declaration node
     */
    public void declare(Declaration decl)
    {
        super.declare(decl);
        names.add(decl.getName());
    }
    
    /**
     * Returns a copy of this ParameterTable.
     * @return a copy of this ParameterTable.
     */
    public ParameterTable getCopy()
    {
        try {
            return (ParameterTable)clone();
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
                msg.msg("EXC_UndefinedQueryParameter", name)); //NOI18N
    }

    //========= PatameterTable convenience methods ==========

    /**
     * Sets all query parameter values. The order of declarations in 
     * declareParameters defines the order in the specified array of parameter 
     * values. The method checks the type compatibility of the query parameter 
     * values.
     * @param paramValues the parameter values
     */
    public void setValues(PersistenceManagerInternal queryPM, Object[] paramValues)
    {
        if (paramValues != null)
        {
            Iterator i = names.iterator();
            for (int index = 0; index < paramValues.length; index++) {
                Object value = paramValues[index];
                if (!i.hasNext()) {
                    throw new JDOQueryException(
                        msg.msg("EXC_WrongNumberOfQueryParameters")); //NOI18N
                }
                String name = (String)i.next();
                QueryResultHelperImpl.checkPM(queryPM, value);
                checkCompatibility(name, value);
                setValue(name, value);
            }
        }
    }

    /**
     * Sets all query parameter values. The values are taken from the specified 
     * map. The method assumes the key in the map to be the parameter name and 
     * the value in the map to be the parameter value. 
     * @param paramValues the parameter values
     */
    public void setValues(PersistenceManagerInternal queryPM, Map paramValues)
    {
        if (paramValues != null)
        {
            for (Iterator i = paramValues.entrySet().iterator(); i.hasNext();)
            {
                Map.Entry actualParam = (Map.Entry)i.next();
                String name = (String)actualParam.getKey();
                Object value = actualParam.getValue();
                QueryResultHelperImpl.checkPM(queryPM, value);
                checkCompatibility(name, value);
                setValue(name, value);
            }
        }
    }

    /**
     * Checks whether all query parameters are bound. 
     * If not a JDOQueryException is thrown.
     */
    public void checkUnboundParams()
    {
        if (valueMap == null)
            throw new JDOFatalInternalException(
                msg.msg("ERR_InvalidTableForExecution", //NOI18N
                        this.getClass().getName()));
        for (Iterator i = valueMap.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry valueEntry = (Map.Entry)i.next();
            if (valueEntry.getValue() instanceof UNDEFINED)
            {
                throw new JDOQueryException(
                    msg.msg("EXC_UnboundQueryParameter", (String)valueEntry.getKey())); //NOI18N
            }
        }
    }

    //========= Internal helper methods ==========
   
    /**
     * Checks the type compatibility of the specified value and throws a
     * JDOQueryException if the value has an incompatible type.
     */
    private void checkCompatibility(String name, Object value)
    {
        if (!isCompatibleValue(name, value)) {
            Declaration decl = (Declaration)declMap.get(name);
            String formalType = decl.getJavaClass().getName();
            String actualType = (value == null) ? "null" : value.getClass().getName(); //NOI18N
            throw new JDOQueryException(
                msg.msg("EXC_IncompatibleTypeOfQueryParameter", actualType, formalType)); //NOI18N
        }
    }
}

