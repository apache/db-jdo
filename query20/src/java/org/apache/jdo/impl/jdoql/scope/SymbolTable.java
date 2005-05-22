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
 * SymbolTable.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

import java.util.Map;
import java.util.HashMap;

import org.apache.jdo.jdoql.tree.Declaration;


/**
 * The symbol table handling declared identifies.
 *
 * @author  Michael Bouschen
 */
public class SymbolTable
{
    /**
     * The table of declared identifier (symbols).
     */
    protected Map symbols = new HashMap();

	/**
	 * This method adds the specified identifier to this SymbolTable. 
     * The specified declaration node provides details anbout the declaration. 
     * If this SymbolTable already defines an identifier with the same name, 
     * the SymbolTable is not changed and the existing declaration is returned. 
     * Otherwise <code>null</code> is returned.
     * @param   ident   identifier to be declared
     * @param   def     new definition of identifier
     * @return  the old definition if the identifier was already declared; 
     * <code>null</code> otherwise
	 */
    public Declaration declare(String ident, Declaration def)
    {
        Declaration old = (Declaration)symbols.get(ident);
        if (old == null) {
            symbols.put(ident, def);
        }
        return old;
    }

    /**
     * Checks whether the specified identifier is declared.  
     * @param ident the name of identifier to be tested
     * @return <code>true</code> if the identifier is declared; 
     * <code>false</code> otherwise.
     */
    public boolean isDeclared(String ident)
    {
        return (getDeclaration(ident) != null);
    }

    /**
     * Checks the symbol table for the actual declaration of the specified 
     * identifier. The method returns the declaration node if available or
     * <code>null</code> for an undeclared identifier. 
     * @param ident the name of identifier
     * @return the declaration node if ident is declared;
     * <code>null</code> otherise.
     */
    public Declaration getDeclaration(String ident)
    {
        return (Declaration)symbols.get(ident);
    }
	
}
