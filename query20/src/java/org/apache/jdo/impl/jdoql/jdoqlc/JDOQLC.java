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
 * JDOQLC.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.jdoql.scope.ParameterTable;
import org.apache.jdo.impl.jdoql.scope.VariableTable;
import org.apache.jdo.impl.jdoql.tree.Tree;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.util.I18NHelper;

import antlr.TokenBuffer;
import antlr.ANTLRException;

/** 
 * This class controls the JDOQL compiler passes.
 *
 * @author  Michael Bouschen
 */
public class JDOQLC
{
    /** The persistence manager of the query instance. */
    protected PersistenceManagerInternal pm;

    /** Type support. */
    protected TypeSupport typeSupport;

    /** Error message helper. */
    protected ErrorMsg errorMsg;    
    
    /** The candidate class. */
    protected Class candidateClass;
    
    /** The AST representing the filter expression. */
    protected JDOQLAST filterAST = null;
    
    /** The AST representing the import declarations. */
    protected JDOQLAST importsAST = null;
    
    /** The AST representing the parameter declarations. */
    protected JDOQLAST paramsAST = null;
    
    /** The AST representing the variable declarations. */
    protected JDOQLAST varsAST = null;
    
    /** The AST representing the ordering specification. */
    protected JDOQLAST orderingAST = null;

    /** The complete query tree. */
    protected JDOQLAST queryTree = null;
    
    /** I18N support */
    private final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", //NOI18N
        JDOQLC.class.getClassLoader());

    /** Logger */
    private static Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.jdoql.jdoqlc"); //NOI18N
    
    /**
     *
     */
    public JDOQLC(PersistenceManagerInternal pm)
    {
        this.pm = pm;
        this.typeSupport = new TypeSupport();
        this.errorMsg = new ErrorMsg();
    }
    
    /**
     *
     */
    public void setClass(Class candidateClass)
    {
        // Method semanticCheck checks for valid candidateClass
        this.candidateClass = candidateClass;
    }

    /**
     *
     */
    public void declareImports(String imports)
    {
        if (imports == null) {
            importsAST = null;
            return;
        }

        try {
            JDOQLParser parser = createStringParser(imports);
            parser.parseImports();
            importsAST = (JDOQLAST)parser.getAST();        
        }
        catch (ANTLRException ex) {
            JDOQLLexer.handleANTLRException(ex, errorMsg);
        }
    }
    
    /**
     *
     */
    public void declareParameters(String parameters)
    {
        if (parameters == null) { 
            paramsAST = null;
            return;
        }
        
        try {
            JDOQLParser parser = createStringParser(parameters);
            parser.parseParameters();
            paramsAST = (JDOQLAST)parser.getAST();        
        }
        catch (ANTLRException ex) {
            JDOQLLexer.handleANTLRException(ex, errorMsg);
        }
    }
    
    /**
     *
     */
    public void declareVariables(String variables)
    {
        if (variables == null) {
            varsAST = null;
            return;
        }
        
        try {
            JDOQLParser parser = createStringParser(variables);
            parser.parseVariables();
            varsAST = (JDOQLAST)parser.getAST();        
        }
        catch (ANTLRException ex) {
            JDOQLLexer.handleANTLRException(ex, errorMsg);
        }
    }
    
    /**
     *
     */
    public void setOrdering(String ordering)
    {
        if (ordering == null) {
            orderingAST = null;
            return;
        }
        
        try {
            JDOQLParser parser = createStringParser(ordering);
            parser.parseOrdering();
            orderingAST = (JDOQLAST)parser.getAST();        
        }
        catch (ANTLRException ex) {
            JDOQLLexer.handleANTLRException(ex, errorMsg);
        }
    }
    
    /**
     *
     */
    public void setFilter(String filter)
    {
        if ((filter == null) || (filter.trim().length() == 0)) {
            // If there is no filter specified use "true" as filter.
            // This is the case if 
            // - setFilter is not called at all (filter == null)
            // - the filter is empty or contians whitespecace only.
            // Internally the filter has to be specified, 
            // otherwise semantic analysis has problems with empty AST.
            filter = "true"; //NOI18N
        }
        
        try {
            JDOQLParser parser = createStringParser(filter);
            parser.parseFilter();
            filterAST = (JDOQLAST)parser.getAST();        
        }
        catch (ANTLRException ex) {
            JDOQLLexer.handleANTLRException(ex, errorMsg);
        }
     }

    /**
     *
     */
    public void setQueryTree(JDOQLAST queryTree)
    {
        this.queryTree = queryTree;
    }

    /**
     *
     */
    public JDOQLAST semanticCheck(ParameterTable paramtab, VariableTable vartab)
    {
        boolean trace = logger.isTraceEnabled();
        ClassLoader applClassLoader = null;

        Semantic semantic = new Semantic();
        semantic.init(typeSupport, paramtab, vartab, errorMsg);
        semantic.setASTFactory(JDOQLASTFactory.getInstance());

        if (queryTree == null) {
            // check candidate class
            if (candidateClass == null) {
                throw new JDOQueryException(
                    msg.msg("EXC_MissingCandidateClass")); //NOI18N
            }
            applClassLoader = candidateClass.getClassLoader();
            // create complete tree representation
            queryTree = semantic.createQueryTree(
                candidateClass, importsAST, paramsAST, 
                varsAST, orderingAST, filterAST);
        }
        else {
            Tree tree = (Tree)queryTree;
            Class clazz = tree.getCandidateClass();
            if (clazz != null)
                applClassLoader = clazz.getClassLoader();
            tree.initANTLRAST();
        }

        // set the candidateClass class loader
        typeSupport.initApplicationJavaModel(applClassLoader);

        if (trace)
            logger.trace("AST\n" + queryTree.treeToString()); //NOI18N

        try {
            if (logger.isDebugEnabled())
                logger.debug("Start semantic analysis"); //NOI18N
            semantic.query(queryTree);
            queryTree = (JDOQLAST)semantic.getAST();
            if (trace)
                logger.trace("Typed AST\n" + queryTree.treeToString()); //NOI18N
        }
        catch (ANTLRException ex) {
            ex.printStackTrace();
            errorMsg.fatal(
                msg.msg("ERR_UnexpectedExceptionSemantic"), ex); //NOI18N
        }
        return queryTree;
    }

    /**
     *
     */
    public JDOQLAST optimize(JDOQLAST tree)
    {
        // Do not include parameters into optimization => paramtab == null
        return optimize(tree, null);
    }

    /**
     *
     */
    public JDOQLAST optimize(JDOQLAST tree, ParameterTable paramtab)
    {
        Tree ast = null;
        Optimizer optimizer= new Optimizer();
        optimizer.init(pm, paramtab, errorMsg);
        optimizer.setASTFactory(JDOQLASTFactory.getInstance());
        
        try {
            if (logger.isDebugEnabled())
                logger.debug("START optimizer"); //NOI18N
            optimizer.query(tree);
            ast = (Tree)optimizer.getAST();
            if (logger.isTraceEnabled())
                logger.trace("Optimized AST\n" + queryTree.treeToString()); //NOI18N
        }
        catch (ANTLRException ex) {
            ex.printStackTrace();
            errorMsg.fatal(
                msg.msg("ERR_UnexpectedExceptionOptimizer"), ex); //NOI18N
        }
        return ast;
    }

    //========= Internal helper methods ==========
    
    /**
     *
     */
    private JDOQLParser createStringParser(String text)
    {
        Reader in = new StringReader(text);
        JDOQLLexer lexer = new JDOQLLexer(in);
        lexer.init(errorMsg);
        TokenBuffer buffer = new TokenBuffer(lexer);
        JDOQLParser parser = new JDOQLParser(buffer);
        parser.init(errorMsg);
        parser.setASTFactory(JDOQLASTFactory.getInstance());
        return parser;
    }
    
}
