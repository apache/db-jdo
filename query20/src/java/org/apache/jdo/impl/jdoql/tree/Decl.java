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

package org.apache.jdo.impl.jdoql.tree;

import org.apache.jdo.jdoql.tree.Declaration;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.Type;

/**
 * This node represents a declaration expression. Examples of
 * declarations expressions are
 * <code>ParameterDeclarationExpression</code> and
 * <code>VariableDeclarationExpression</code>. Declaration expressions
 * do not have any children.
 *
 * @author Michael Watzek
 */
public abstract class Decl extends NodeImpl implements Declaration
{
    String  name;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public Decl()
    {}

    /**
     * This constructor is called by specialized nodes.
     * @param tokenType the token tpye
     * @param tokenName the name of this node
     * @param type the type instance wrapping the Java class
     * @param name the name of the specialized declaration
     */
    Decl(int tokenType, String tokenName, Type type, String name)
    {   super( tokenType, tokenName, type.getJavaClass() );
        setChildren( new Node[] {type} );
        this.name = name;
    }

    /**
     * Returns the name of the specialized declaration.
     * @return the name
     */
    public String getName()
    {   return this.name;
    }


    /**
     * Sets the name of the specialized declaration.
     * This method is used by semantic analysis only.
     * @param name the name
     */
    public void setName(String name)
    {   this.name = name;
    }

    /**
     * Returns the Java type name of the specialized declaration.
     * @return the Java type name
     */
    public String getTypeName()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return ((Type)this.children[0]).getTypeName();
    }
}

