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
 * JDOQLASTFactory.java
 *
 * Created on September 3, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import antlr.collections.AST;
import antlr.ASTFactory;

import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.util.I18NHelper;

/** 
 * Factory to create and connect JDOQLAST nodes.
 *
 * @author  Michael Bouschen
 */
public class JDOQLASTFactory
    extends ASTFactory
{
    /** The singleton JDOQLASTFactory instance. */    
    private static JDOQLASTFactory factory = new JDOQLASTFactory();

    /** I18N support. */
    private final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", JDOQLASTFactory.class.getClassLoader()); //NOI18N
    
    /** 
     * Get an instance of JDOQLASTFactory.
     * @return an instance of JDOQLASTFactory
     */    
    public static JDOQLASTFactory getInstance()
    {
        return factory;
    }
    
    /**
     *
     */
    protected JDOQLASTFactory()
    {
        this.theASTNodeTypeClass = JDOQLAST.class;
        this.theASTNodeType = this.theASTNodeTypeClass.getName();
    }
    
    /**
     *
     */
    public AST create() 
    {
        return new JDOQLAST();
    }

    /**
     *
     */
    public AST create(AST tr) 
    { 
        return create((JDOQLAST)tr);
    }

    /**
     *
     */
    public JDOQLAST create(JDOQLAST tr) 
    { 
        try {
            return (tr==null) ? null : (JDOQLAST)tr.clone();
        }
        catch(CloneNotSupportedException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_UnexpectedExceptionClone"), ex); //NOI18N
        }
    }
}

