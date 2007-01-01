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
 * AbstractValueTable.java
 *
 * Created on September 11, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;

import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Declaration;
import org.apache.jdo.jdoql.tree.ValueTable;
import org.apache.jdo.util.I18NHelper;


/**
 * This method is the abstract super class for ParameterTable and VariableTable.
 * It provides common implementation for managing parameter and variable values.
 *
 * @author  Michael Bouschen
 */
abstract class AbstractValueTable
    implements ValueTable, Serializable, Cloneable
{
    /**
     * Map of declarations. Key is the the name of the declared identifier, 
     * value is the Declaration node.
     */
    Map declMap = new HashMap();
    
    /**
     * Map of values. This map includes a values entry for each declared 
     * identifier. Default value is UNDEFINED.
     */
    transient Map valueMap;
    
    /** I18N support */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", //NOI18N
        AbstractValueTable.class.getClassLoader()); 
 
    /**
     * Adds a new declaration. The value is set to UNDEFINED.
     * @param decl the declaration node
     */
    public void declare(Declaration decl)
    {
        String name = decl.getName();
        declMap.put(name, decl);
    }
    
    /**
     * This method initializes the map of values for this ValueTable. 
     * It needs to be called prior to any use of an AbstractValueTable
     * at query execution time.
     */
    public void initValueHandling()
    {
        valueMap = new HashMap();
        for (Iterator i = declMap.keySet().iterator(); i.hasNext();) {
            String name = (String)i.next();
            valueMap.put(name, UNDEFINED.getInstance());
        }
    }

    /**
     * Sets the value for the specified identifier.
     * @param name the name of the identifier
     * @param value the current value of the identifier
     */
    public void setValue(String name, Object value)
    {
        if (valueMap == null)
            throw new JDOFatalInternalException(
                msg.msg("ERR_InvalidTableForExecution", //NOI18N
                        this.getClass().getName()));
        Declaration decl = (Declaration)declMap.get(name);
        checkDeclaredIdentifier(name, decl);
        valueMap.put(name, value);
    }
    
    /**
     * Returns the current value for the specified identifier.
     * @param name the name of the identifier
     * @return the current value of the identifier
     */
    public Object getValue(String name)
    {
        if (valueMap == null)
            throw new JDOFatalInternalException(
                msg.msg("ERR_InvalidTableForExecution", //NOI18N
                        this.getClass().getName()));
        checkDeclaredIdentifier(name, (Declaration)declMap.get(name));
        return valueMap.get(name);
    }
    
    /**
     * Checks whether the type of the specified value is compatible of the type
     * of the identifier from its declaration.
     * @param name the name of the identifier
     * @param value the value to be checked
     * @return <code>true</code> if the type of the value is compatible with the 
     * type of the identifier; <code>false</code> otherwise.
     */
    public boolean isCompatibleValue(String name, Object value)
    {
        Declaration decl = (Declaration)declMap.get(name);
        checkDeclaredIdentifier(name, decl);

        boolean isCompatible = true;

        // check type compatibility of actual and formal parameter
        Class formalType = decl.getJavaClass();

        // handle value == null
        if (value == null) {
            isCompatible = !formalType.isPrimitive();
        }
        else {
            Class actualType = value.getClass();
            Class type = formalType;
            if (formalType.isPrimitive()) {
                if (formalType == int.class)
                    type = Integer.class;
                else if (formalType == long.class)
                    type = Long.class;
                else if (formalType == short.class)
                    type = Short.class;
                else if (formalType == byte.class)
                    type = Byte.class;
                else if (formalType == double.class)
                    type = Double.class;
                else if (formalType == float.class)
                    type = Float.class;
                else if (formalType == boolean.class)
                    type = Boolean.class;
                else if (formalType == char.class)
                    type = Character.class;
            }
            isCompatible = type.isAssignableFrom(actualType);
        }
        return isCompatible;
    }
 
    /**
     * Internal method to check whether the specified identifier is declared. 
     * Allows subclasses of AbstractValueTable to use specific error messages.
     */
    protected abstract void checkDeclaredIdentifier(String name, 
                                                    Declaration decl);

}

