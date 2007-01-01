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
 * JDOQLAST.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import org.apache.jdo.model.java.JavaType;

import antlr.Token;
import antlr.CommonAST;
import antlr.collections.AST;


/** 
 * This class represents a node in the intermediate representation (AST) 
 * used by the query compiler. 
 * It provides
 * - line info
 * - column info
 * - type info (JavaType instance): the semantic analysis calculates 
 *   the type of an expression and adds this info to each node.
 * @author  Michael Bouschen
 */
public class JDOQLAST
    extends CommonAST
    implements Cloneable
{
    /** The line info */
    protected int line = 0;

    /** The column info */
    protected int column = 0;

    /** The type info */
    protected transient JavaType typeInfo;

    /** */
    public JDOQLAST()
    {
    }

    /** */
    public JDOQLAST(int type, String text, JavaType typeInfo)
    {
        initialize(type, text, typeInfo);
    }

    /** */
    public JDOQLAST(JDOQLAST ast)
    {
        initialize(ast);
    }
    
    /** */
    public void initialize(Token t)
    {
        setType(t.getType());
        setText(t.getText());
        setLine(t.getLine());
        setColumn(t.getColumn());
    }

    /** */
    public void initialize(int type, String text, JavaType typeInfo)
    {
        setType(type);
        setText(text);
        setTypeInfo(typeInfo);
    }

    /** */
    public void initialize(AST _ast)
    {
        JDOQLAST ast = (JDOQLAST)_ast;
        setType(ast.getType());
        setText(ast.getText());
        setLine(ast.getLine());
        setColumn(ast.getColumn());
        setTypeInfo(ast.getTypeInfo());
    }
    
    /** */
    public void setLine(int line)
    {
        this.line = line;
    }

    /** */
    public int getLine()
    {
        return line;
    }

    /** */
    public void setColumn(int column)
    {
        this.column = column;
    }

    /** */
    public int getColumn()
    {
        return column;
    }

    /** */
    public void setTypeInfo(JavaType typeInfo)
    {
        this.typeInfo = typeInfo;
    }

    /** */
    public JavaType getTypeInfo()
    {
        return typeInfo;
    }

    /** 
     * Returns a string representation of this JDOQLAST.
     * @return a string representation of the object.
     */
    public String toString()
    {
        JavaType typeInfo = getTypeInfo();
        StringBuffer repr = new StringBuffer();
        // token text
        repr.append((getText() == null ? "null" : getText())); //NOI18N
        repr.append(" ["); //NOI18N
        // token type
        repr.append(getType());
        // line/column info
        repr.append(", ("); //NOI18N
        repr.append(getLine() + "/" + getColumn()); //NOI18N
        repr.append("), "); //NOI18N
        // type info
        repr.append(typeInfo);
        repr.append(", "); //NOI18N
        // node class info
        String className = getClass().getName();
        int index = className.lastIndexOf('.');
        if (index > -1)
            className = className.substring(index+1);
        repr.append(className);
        repr.append("]"); //NOI18N
        return repr.toString();
    }

    /**
     * Returns a string representation of this JDOQLAST including 
     * all child nodes.
     * @return a string representation of this JDOQLAST including all
     * child nodes.
     */
    public String treeToString() 
    {
        StringBuffer repr = new StringBuffer();
        treeToString(0, repr);
        return repr.toString();
    }

    /**
     * Creates and returns a copy of this object.
     * The returned JDOQLAST shares the same state as this object, meaning 
     * the fields type, text, line, column, and typeInfo have the same values. 
     * But it is not bound to any tree structure, thus the child is null 
     * and the sibling is null.
     * @return a clone of this instance.
     */
    protected Object clone()
        throws CloneNotSupportedException
    {
        JDOQLAST clone = (JDOQLAST)super.clone();
        clone.setFirstChild(null);
        clone.setNextSibling(null);
        return clone;
    }
    
    // Internal helper method

    /** */
    private void treeToString(int indent, StringBuffer repr) 
    {
        // indent
        for (int i = 0; i < indent; i++) {
            repr.append("   "); //NOI18N
        }

        // append string representation of current node
        repr.append(toString());

        // child nodes in new line
        repr.append("\n");

        // append string representation(s) of child nodes
        for (JDOQLAST node = (JDOQLAST)this.getFirstChild(); 
             node != null; 
             node = (JDOQLAST)node.getNextSibling()) {
            node.treeToString(indent+1, repr);
        }
    }
}

