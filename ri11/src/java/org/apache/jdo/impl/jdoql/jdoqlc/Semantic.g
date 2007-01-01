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
 * Semantic.g
 *
 * Created on August 28, 2001
 */

header
{
    package org.apache.jdo.impl.jdoql.jdoqlc;
    
    import java.util.Collection;
    
    import org.apache.jdo.model.java.JavaType;
    import org.apache.jdo.model.java.JavaField;
    import org.apache.jdo.impl.model.java.ErrorType;
    import org.apache.jdo.impl.model.java.NullType;
    import org.apache.jdo.impl.model.java.PrimitiveType;
    import org.apache.jdo.impl.model.java.WrapperClassType;
    import org.apache.jdo.impl.model.java.PredefinedType;

    import org.apache.jdo.impl.jdoql.tree.*;
    import org.apache.jdo.impl.jdoql.scope.ParameterTable;
    import org.apache.jdo.impl.jdoql.scope.SymbolTable;
    import org.apache.jdo.impl.jdoql.scope.TypeNames;
    import org.apache.jdo.impl.jdoql.scope.VariableTable;

    import org.apache.jdo.util.I18NHelper; 
}

/**
 * This class defines the semantic analysis of the JDOQL compiler.
 * Input of this pass is the AST as produced by the parser,
 * that consists of JDOQLAST nodes.
 * The result is a typed JDOQLAST tree.
 * <p>
 * TBD:
 * <ul>
 * <li> Check for non portable contains queries
 * </ul> 
 *
 * @author  Michael Bouschen
 */
class Semantic extends TreeParser;

options
{
    importVocab = JDOQL;
    buildAST = true;
    defaultErrorHandler = false;
    ASTLabelType = "JDOQLAST"; //NOI18N
}

{
    /** The error message support class. */
    protected ErrorMsg errorMsg;
    
    /** Symbol table handling names of variables and parameters. */
    protected SymbolTable symtab;
    
    /** Table of type names handling imports. */
    protected TypeNames typeNames;

    /** The type support. */
    protected TypeSupport typeSupport;
    
    /** The query parameter table */
    protected ParameterTable paramtab;
    
    /** The variable table. */
    protected VariableTable vartab;
    
    /** The variable checker. */
    protected VariableChecker varChecker;

    /** Candidate class. */
    protected JavaType candidateClass; 
    
    /** I18N support */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", Semantic.class.getClassLoader()); //NOI18N
    
    /**
     *
     */
    public void init(TypeSupport typeSupport, ParameterTable paramtab, 
                     VariableTable vartab, ErrorMsg errorMsg)
    {
        this.errorMsg = errorMsg;
        this.symtab = new SymbolTable();
        this.typeNames = new TypeNames(typeSupport);
        this.vartab = vartab;
        this.typeSupport = typeSupport;
        this.paramtab = paramtab;
        this.varChecker = new VariableChecker();
    }

    /**
     *
     */
    public void reportError(RecognitionException ex) {
        errorMsg.fatal(msg.msg("ERR_SemanticError"), ex); //NOI18N
    }

    /**
     *
     */
    public void reportError(String s) {
        errorMsg.fatal(msg.msg("ERR_SemanticError") + s); //NOI18N
    }
    
    /**
     * Combines partial ASTs into one query AST.
     */
    public JDOQLAST createQueryTree(Class candidateClass, JDOQLAST importsAST, 
                                    JDOQLAST paramsAST, JDOQLAST varsAST, 
                                    JDOQLAST orderingAST, JDOQLAST filterAST)
    {
        CandidateClassImpl candidateClassAST = new CandidateClassImpl();
        candidateClassAST.setType(CANDIDATE_CLASS);
        candidateClassAST.setText(candidateClass.getName());
        candidateClassAST.setCandidateClass(candidateClass);
        JDOQLAST query = new NodeImpl();
        query.setType(QUERY_TREE);
        query.setText("query"); //NOI18N
        query.addChild(candidateClassAST);
        if (importsAST != null)
            query.addChild(importsAST);
        if (paramsAST != null)
            query.addChild(paramsAST);
        if (varsAST != null)
            query.addChild(varsAST);
        if (orderingAST != null)
            query.addChild(orderingAST);
        if (filterAST != null)
            query.addChild(filterAST);
        return query;
    }
    
    /**
     * This method analyses the expression of a single ordering definition.
     * It checks whether the expression
     * - is of a orderable type
     * @param expr the expression of an ordering definition
     */
    protected void analyseOrderingExpression(JDOQLAST expr)
    {
        JavaType exprType = expr.getTypeInfo();
        if (!exprType.isOrderable())
        {
            errorMsg.error(expr.getLine(), expr.getColumn(),
                msg.msg("EXC_NotSortableInOrdering", //NOI18N 
                    exprType.getName()));
            expr.setTypeInfo(ErrorType.errorType);
        }
    }

    /**
     * This method analyses a dot expression of the form expr.ident or
     * expr.ident(params) where expr itself can again be a dot expression.
     * It checks whether the dot expression is 
     * - part of a qualified class name specification
     * - field access,
     * - a method call
     * The method returns a temporary single AST node that is defined with a
     * specific token type (field access, method call, etc.). This node also
     * contains the type of the dot expression.
     * @param expr the left hand side of the dot expression
     * @param ident the right hand side of the dot expression
     * @param args arguments (in the case of a call)
     * @return AST node representing the specialized dot expr
     */
    protected JDOQLAST analyseDotExpr(JDOQLAST dot, JDOQLAST expr, 
                                      JDOQLAST ident, JDOQLAST args)
    {
        JavaType exprType = expr.getTypeInfo();
        String name = ident.getText();
        dot.setText(expr.getText() + '.' + name);
        if (!exprType.isPrimitive()) {
            // left expression is of a class type
            if (args == null) {
                // no paranethesis specified => field access
                JavaField javaField = exprType.getJavaField(name);
                if (javaField == null) {
                    errorMsg.error(ident.getLine(), ident.getColumn(),
                        msg.msg("EXC_UnknownField",  //NOI18N
                            ident.getText(), exprType.getName()));
                    dot.setTypeInfo(ErrorType.errorType);
                    ident.setTypeInfo(ErrorType.errorType);
                    return dot;
                }
                else if (expr.getType() == TYPE) {
                    // access of the form: className.staticField
                    JDOQLAST fieldAccess = analyseStaticFieldAccess(
                        expr, ident, exprType, javaField);
                    fieldAccess.setLine(#dot.getLine());
                    fieldAccess.setColumn(#dot.getColumn());
                    return fieldAccess;
                }
                else {
                    // access of the form: object.field
                    JDOQLAST fieldAccess = 
                        analyseFieldAccess(expr, ident, exprType, javaField);
                    fieldAccess.setLine(#dot.getLine());
                    fieldAccess.setColumn(#dot.getColumn());
                    return fieldAccess;
                }
            }
            else {
                // parenthesis specified => method call
                if (exprType.isJDOSupportedCollection()) {
                    JDOQLAST call = 
                        analyseCollectionCall(dot, expr, ident, args);
                    call.setLine(#dot.getLine());
                    call.setColumn(#dot.getColumn());
                    return call;
                }
                else if (exprType.equals(PredefinedType.stringType)) {
                    JDOQLAST call = analyseStringCall(dot, expr, ident, args);
                    call.setLine(#dot.getLine());
                    call.setColumn(#dot.getColumn());
                    return call;
                }
                errorMsg.error(dot.getLine(), dot.getColumn(),  
                               msg.msg("EXC_InvalidMethodCall")); //NOI18N
                dot.setTypeInfo(ErrorType.errorType);
                return dot;
            }
        }
        else {
            errorMsg.error(expr.getLine(), expr.getColumn(),
                msg.msg("EXC_ClassTypeExpressionExpected", //NOI18N
                    ident.getText(), exprType.getName()));
            dot.setTypeInfo(ErrorType.errorType);
            return dot;
        }
    }

    /**
     * 
     */
    protected JDOQLAST analyseFieldAccess(
        JDOQLAST objectExpr, JDOQLAST ident, JavaType classType, 
        JavaField javaField)
    {
        FieldAccessExpr fieldAccess = new FieldAccessExpr();
        String name = ident.getText();
        JavaType fieldType = javaField.getType();
        int tokenType = (classType.isPersistenceCapable() && 
                         fieldType.isPersistenceCapable()) ? 
                         NAVIGATION : FIELD_ACCESS;
        fieldAccess.initialize(tokenType, objectExpr.getText() + '.' + name, 
                               fieldType);
        fieldAccess.setName(name);
        fieldAccess.setFirstChild(objectExpr);
        objectExpr.setNextSibling(null);
        return fieldAccess;
    }

    /**
     * 
     */
    protected JDOQLAST analyseStaticFieldAccess(
        JDOQLAST typename, JDOQLAST ident, JavaType classType, 
        JavaField javaField)
    {
        String name = ident.getText();
        if (!typeSupport.isStaticField(javaField)) {
            errorMsg.error(ident.getLine(), ident.getColumn(),  
                msg.msg("EXC_InvalidStaticReference", //NOI18N 
                    name, classType.getName()));
        }
        StaticFieldAccessExpr fieldAccess = new StaticFieldAccessExpr();
        fieldAccess.initialize(STATIC_FIELD_ACCESS,
                               typename.getText() + '.' + name, 
            javaField.getType());
        fieldAccess.setName(name);
        fieldAccess.setFirstChild(typename);
        typename.setNextSibling(null);
        return fieldAccess;
    }

    /**
     * This method analyses an identifier defined in the current scope which
     * is a field, variable or parameter defined in the symbol table.
     * @param ident the identifier AST
     * @return AST node representing a defined identifier 
     */
    protected JDOQLAST analyseDefinedIdentifier(JDOQLAST ident)
    {
        JDOQLAST ast = null;
        String name = ident.getText();
        Decl decl = (Decl)symtab.getDeclaration(name);
        if (decl != null) {
            if (decl instanceof VariableDecl) {
                ast = new VariableAccessExpr();
                ast.initialize(VARIABLE_ACCESS, name, decl.getTypeInfo());
                ast.setLine(ident.getLine());
                ast.setColumn(ident.getColumn());
            }
            else if (decl instanceof ParameterDecl) {   
                ast = new ParameterAccessExpr();
                ast.initialize(PARAMETER_ACCESS, name, decl.getTypeInfo());
                ast.setLine(ident.getLine());
                ast.setColumn(ident.getColumn());
            }
        }
        else {
            JavaField javaField = candidateClass.getJavaField(name);
            if (javaField != null) {
                ThisExpr thisAST = new ThisExpr();
                thisAST.initialize(THIS, "this", candidateClass); //NOI18N
                ast = analyseFieldAccess(thisAST, ident, candidateClass, 
                                         javaField);
                ast.setLine(ident.getLine());
                ast.setColumn(ident.getColumn());
            }
        }
        return ast;
    }
    
    /**
     * Analyses a call for an object that implements Collection. 
     * Currently, only contains and isEmpty are supported.
     */
    protected JDOQLAST analyseCollectionCall(JDOQLAST dot, JDOQLAST collection, 
                                             JDOQLAST method, JDOQLAST args)
    {
        String methodName = method.getText();
        JDOQLAST call = null;
        JDOQLAST firstArg = (JDOQLAST)args.getFirstChild();
        if (methodName.equals("contains")) { //NOI18N
            call = new ContainsCallExpr();
            call.initialize(CONTAINS, methodName, PredefinedType.booleanType);
            checkContainsArgs(collection, method, firstArg);
            call.setFirstChild(collection);
            collection.setNextSibling(firstArg);
        }
        else if (methodName.equals("isEmpty")) { //NOI18N
            call = new IsEmptyCallExpr();
            call.initialize(IS_EMPTY, methodName, PredefinedType.booleanType);
            checkIsEmptyArgs(firstArg);
            call.setFirstChild(collection);
            collection.setNextSibling(null);
        }
        else {
            errorMsg.error(dot.getLine(), dot.getColumn(),  
                msg.msg("EXC_InvalidMethodCall"));  //NOI18N
            call = new IsEmptyCallExpr();
            call.initialize(IS_EMPTY, methodName, ErrorType.errorType);
        }
        return call;
    }
    
    /**
     * Check the arguments of a contains call.
     */
    protected void checkContainsArgs(JDOQLAST collection, JDOQLAST method, 
                                     JDOQLAST args)
    {
        JDOQLAST firstArg = args;
        if (firstArg == null) {
            errorMsg.error(method.getLine(), method.getColumn(),
                msg.msg("EXC_WrongNumberOfArgs")); //NOI18N
        }
        else if (firstArg.getNextSibling() != null) {
            JDOQLAST nextArg = (JDOQLAST)firstArg.getNextSibling();
            errorMsg.error(nextArg.getLine(), nextArg.getColumn(),
                msg.msg("EXC_WrongNumberOfArgs")); //NOI18N
        }
        else {
            JavaType elementType = PredefinedType.objectType;
            JavaField collectionJavaField = getCollectionField(collection);
            if (collectionJavaField != null) {
                elementType = TypeSupport.getElementType(collectionJavaField);
            }
            JavaType argumentType = firstArg.getTypeInfo();
            JavaType testElementType = elementType.isPrimitive() ?
                ((PrimitiveType)elementType).getWrapperClassType() : elementType;
            JavaType testArgumentType = argumentType.isPrimitive() ?
                ((PrimitiveType)argumentType).getWrapperClassType() : argumentType;
            // elementType compatible with argumentType => OK
            // argumentType compatible with elementType => OK
            // otherwise error
            if (!testElementType.isCompatibleWith(testArgumentType) &&
                !testArgumentType.isCompatibleWith(testElementType)) {
                errorMsg.error(collection.getLine(), collection.getColumn(),
                    msg.msg("EXC_CollectionElementTypeMismatch", //NOI18N
                        elementType.getName(), argumentType.getName()));
            }
        }
    }

    /**
     *
     */
    protected JavaField getCollectionField(JDOQLAST expr)
    {
        JDOQLAST child = (JDOQLAST)expr.getFirstChild();
        switch (expr.getType()) {
        case FIELD_ACCESS:
        case NAVIGATION:
            if (child != null) {
                JavaType classType = child.getTypeInfo();
                String fieldName = null;
                if (child.getNextSibling() != null) {
                    fieldName = child.getNextSibling().getText();
                }
                else {
                    fieldName = ((FieldAccessExpr)expr).getName();
                }
                return classType.getJavaField(fieldName);
            }
            errorMsg.fatal(msg.msg("ERR_MissingChildren", expr)); //NOI18N
            break;
        case CAST:
            if ((child != null) && (child.getNextSibling() != null)) {
                return getCollectionField((JDOQLAST)child.getNextSibling());
            }
            errorMsg.fatal(msg.msg("ERR_MissingChildren", expr)); //NOI18N
            break;
        }
        return null;
    }

    /**
     * Check the arguments of a isEmpty call.
     */
    protected void checkIsEmptyArgs(JDOQLAST args)
    {
        if (args != null) {
            // isEmpty does not take parameters
            errorMsg.error(args.getLine(), args.getColumn(),
                msg.msg("EXC_WrongNumberOfArgs")); //NOI18N
        }
    }

    /**
     * Analyses a call for an object of type String.
     * Currently startsWith and endsWith are the only valid String methods
     * in a query filter.
     */
    protected JDOQLAST analyseStringCall(JDOQLAST dot, JDOQLAST string, 
                                         JDOQLAST method, JDOQLAST args)
    {
        String methodName = method.getText();
        JDOQLAST call = null;
        JDOQLAST firstArg = (JDOQLAST)args.getFirstChild();
        if (methodName.equals("startsWith")) { //NOI18N
            call = new StartsWithCallExpr();
            call.initialize(STARTS_WITH, methodName, PredefinedType.booleanType);
            checkStringCallArgs(method, firstArg);
            call.setFirstChild(string);
            string.setNextSibling(firstArg);
        }
        else if (methodName.equals("endsWith")) { //NOI18N
            call = new EndsWithCallExpr();
            call.initialize(ENDS_WITH, methodName, PredefinedType.booleanType);
            checkStringCallArgs(method, firstArg);
            call.setFirstChild(string);
            string.setNextSibling(firstArg);
        }
        else
        {
            errorMsg.error(dot.getLine(), dot.getColumn(),  
                msg.msg("EXC_InvalidMethodCall"));  //NOI18N
            call = new StartsWithCallExpr();
            call.initialize(STARTS_WITH, methodName, ErrorType.errorType);
        }
        return call;
    }

    /**
     * Check the arguments of a startWith or endWith call.
     */
    protected void checkStringCallArgs(JDOQLAST method, JDOQLAST args)
    {
        JDOQLAST firstArg = args;
        if (firstArg == null) {
            errorMsg.error(method.getLine(), method.getColumn(),
                msg.msg("EXC_WrongNumberOfArgs")); //NOI18N
        }
        else if (firstArg.getNextSibling() != null) {
            JDOQLAST nextArg = (JDOQLAST)firstArg.getNextSibling();
            errorMsg.error(nextArg.getLine(), nextArg.getColumn(),
                msg.msg("EXC_WrongNumberOfArgs")); //NOI18N
        }
        else {
            JavaType argType = firstArg.getTypeInfo();
            if (!argType.equals(PredefinedType.stringType)) {
                errorMsg.error(firstArg.getLine(), firstArg.getColumn(),
                    msg.msg("EXC_ArgumentTypeMismatch", //NOI18N
                        argType.getName(), PredefinedType.stringType.getName()));
            }
        }
    }

    /**
     * Analyses a bitwise/logical operation (&, |, ^)
     * @param op the bitwise/logical operator
     * @param leftAST left operand 
     * @param rightAST right operand
     * @return the type info of the operator 
     */
    protected JavaType analyseBitwiseExpr(JDOQLAST op, JDOQLAST leftAST, 
                                          JDOQLAST rightAST)
    {
        JavaType left = leftAST.getTypeInfo();
        JavaType right = rightAST.getTypeInfo();
        
        // handle error type
        if (left.equals(ErrorType.errorType) || 
            right.equals(ErrorType.errorType))
            return ErrorType.errorType;
        
        switch(op.getType()) {
        case BAND:
        case BOR:
            if (TypeSupport.isBooleanType(left) && 
                TypeSupport.isBooleanType(right)) {
                JavaType common = PredefinedType.booleanType;
                ((BinaryExpr)op).setCommonOperandType(
                    TypeSupport.getJavaClass(common));
                return common;
            }
            break;
        }

        // if this code is reached a bitwise operator was used with 
        // invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments",  op.getText())); //NOI18N
        return ErrorType.errorType;
    }
    
    /**
     * Analyses a boolean conditional operation (&&, ||)
     * @param op the conditional operator
     * @param leftAST left operand 
     * @param rightAST right operand
     * @return the type info of the operator 
     */
    protected JavaType analyseConditionalExpr(JDOQLAST op, JDOQLAST leftAST, 
                                              JDOQLAST rightAST)
    {
        JavaType left = leftAST.getTypeInfo();
        JavaType right = rightAST.getTypeInfo();

        // handle error type
        if (left.equals(ErrorType.errorType) || 
            right.equals(ErrorType.errorType))
            return ErrorType.errorType;

        switch(op.getType()) {
        case AND:
        case OR:
            if (TypeSupport.isBooleanType(left) && 
                TypeSupport.isBooleanType(right)) {
                JavaType common = PredefinedType.booleanType;
                ((BinaryExpr)op).setCommonOperandType(
                    TypeSupport.getJavaClass(common));
                return common;
            }
            break;
        }
        
        // if this code is reached a conditional operator was used 
        // with invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments", op.getText())); //NOI18N
        return ErrorType.errorType;
    }

    /**
     * Analyses a relational operation.
     * A relational operation contains one of <, <=, >, >=, ==, or !=.
     * @param op the relational operator
     * @param leftAST left operand 
     * @param rightAST right operand
     * @return the node representing the relational expr
     */
    protected JDOQLAST analyseRelationalExpr(JDOQLAST op, JDOQLAST leftAST, 
                                             JDOQLAST rightAST)
    {
        JavaType left = leftAST.getTypeInfo();
        JavaType right = rightAST.getTypeInfo();

        // handle error type
        if (left.equals(ErrorType.errorType) || 
            right.equals(ErrorType.errorType)) {
            op.setTypeInfo(ErrorType.errorType);
            return op;
        }

        // special check for <, <=, >, >=
        // left and right hand types must be orderable
        switch(op.getType()) {
        case LT:
        case LE:
        case GT:
        case GE:
            if (!left.isOrderable()) {
                errorMsg.error(op.getLine(), op.getColumn(),
                    msg.msg("EXC_NotSortableType", //NOI18N
                        left.getName(), op.getText()));
                op.setTypeInfo(ErrorType.errorType);
                return op;
            }
            if (!right.isOrderable()) {
                errorMsg.error(op.getLine(), op.getColumn(),
                    msg.msg("EXC_NotSortableType", //NOI18N 
                        right.getName(), op.getText()));
                op.setTypeInfo(ErrorType.errorType);
                return op;
            }
            break;
        case EQUAL:
            if (left.isPersistenceCapable() ||
                right.isPersistenceCapable()) {
                op.setType(OBJECT_EQUAL);
            }
            else if (left.isJDOSupportedCollection() || 
                     right.isJDOSupportedCollection()) {
                op.setType(COLLECTION_EQUAL);
            }
            break;
        case NOT_EQUAL:
            if (left.isPersistenceCapable() || 
                right.isPersistenceCapable()) {
                op.setType(OBJECT_NOT_EQUAL);
            }
            else if (left.isJDOSupportedCollection() || 
                     right.isJDOSupportedCollection()) {
                op.setType(COLLECTION_NOT_EQUAL);
            }
            break;
        }
        
        JavaType common = getCommonOperandType(left, right);
        if (common != ErrorType.errorType) {
            ((BinaryExpr)op).setCommonOperandType(
                TypeSupport.getJavaClass(common));
            op.setTypeInfo(PredefinedType.booleanType);
            // check for operands of type char or Character;
            // they need to be explictly cast to the common operand type.
            leftAST = addCharacterCast(leftAST, common);
            rightAST = addCharacterCast(rightAST, common);
            op.setFirstChild(leftAST);
            leftAST.setNextSibling(rightAST);
            return op;
        }
        
        // if this code is reached a conditional operator was used with 
        // invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments",  op.getText())); //NOI18N 
        op.setTypeInfo(ErrorType.errorType);
        return op;
    }
    
    /**
     * Analyses a binary arithmetic expression +, -, *, /.
     * @param op the  operator
     * @param leftAST left operand 
     * @param rightAST right operand
     * @return the node representing the binary arithmetic op
     */
    protected JDOQLAST analyseBinaryArithmeticExpr(JDOQLAST op, JDOQLAST leftAST,
                                                   JDOQLAST rightAST)
    {
        JavaType left = leftAST.getTypeInfo();
        JavaType right = rightAST.getTypeInfo();

        // handle error type
        if (left.equals(ErrorType.errorType) || 
            right.equals(ErrorType.errorType)) {
            op.setTypeInfo(ErrorType.errorType);
            return op;
        }

        if (TypeSupport.isNumberType(left) && TypeSupport.isNumberType(right)) {
            JavaType common = getCommonOperandType(left, right);
            if (common != ErrorType.errorType) {
                ((BinaryExpr)op).setCommonOperandType(
                    TypeSupport.getJavaClass(common));
                op.setTypeInfo(common);
                // Check for operands of type char or Character;
                // they need to be explictly cast to the common operand type.
                leftAST = addCharacterCast(leftAST, common);
                rightAST = addCharacterCast(rightAST, common);
                op.setFirstChild(leftAST);
                leftAST.setNextSibling(rightAST);
                return op;
            }
        }
        else if (op.getType() == PLUS) {
            // handle + for strings
            if (left.equals(PredefinedType.stringType) && 
                right.equals(PredefinedType.stringType)) {
                JavaType common = PredefinedType.stringType;
                ((BinaryExpr)op).setCommonOperandType(
                    TypeSupport.getJavaClass(common));
                op.setTypeInfo(common);
                // change the token type to CONCAT
                op.setType(CONCAT);
                return op;
            }
        }

        // if this code is reached a conditional operator was used 
        // with invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments",  op.getText())); //NOI18N
        op.setTypeInfo(ErrorType.errorType);
        return op;
    }
    
    /**
     * The query runtime has a problem with binary or relational expressions 
     * having an operand of type char or Character. The query runtime treats the
     * value of the expression to be a value of the promoted type (see binary 
     * numeric promotion). The value will be of type Character which is not 
     * compatible to java.lang.Number. 
     * In the current query runtime only the CastExpression includes the 
     * conversion code from the Character to Number. This keeps the code for
     * binary and relational expressions free from any special treatement of 
     * char or Character values.
     * As a consequence the semantic analsis needs to insert a cast node 
     * whenever binary numeric promotion converts a char or Character into a 
     * numeric or Number type.
     * Method addCharacterCast checks whether the specified ast is of type 
     * char or Character. If so it wraps it into a cast expression ast using
     * the specified type. If not the ast is reured as it is.
     * @param ast the ast to be checked
     * @param common the type to be used inside the cast
     * @return a cast node that wraps the specified ast node, if the ast is 
     * of type char or Character; the ast itself otherwise.
     */
    protected JDOQLAST addCharacterCast(JDOQLAST ast, JavaType common)
    {
        JDOQLAST node = ast;
        if (TypeSupport.isCharType(ast.getTypeInfo())) {
            CastExpr cast = new CastExpr();
            cast.initialize(CAST, "CAST", common); //NOI18N
            cast.setLine(ast.getLine());
            cast.setColumn(ast.getColumn());
            TypeImpl typeNode = new TypeImpl();
            typeNode.initialize(TYPE, common.getName(), common);
            cast.setFirstChild(typeNode);
            typeNode.setNextSibling(ast);
            node = cast;
        }
        return node;
    }
    
    /**
     * Returns the common type info for the specified operand types. 
     * This includes binary numeric promotion as specified in Java.
     * @param left type info of left operand 
     * @param right type info of right operand
     * @return the common type info
     */
    protected JavaType getCommonOperandType(JavaType left, JavaType right)
    {
        if (TypeSupport.isNumberType(left) && TypeSupport.isNumberType(right)) {
            // handle java.math.BigDecimal
            if (left.isCompatibleWith(PredefinedType.bigDecimalType))
                return left;
            if (right.isCompatibleWith(PredefinedType.bigDecimalType))
                return right;
            
            // handle java.math.BigInteger
            if (left.isCompatibleWith(PredefinedType.bigIntegerType)) {
                // if right is floating point return BigDecimal, 
                // otherwise return BigInteger
                if (right.isWrapperClass())
                    right = ((WrapperClassType)right).getWrappedPrimitiveType();
                return right.isFloatingPoint() ? 
                       PredefinedType.bigDecimalType : left;
            }
            if (right.isCompatibleWith(PredefinedType.bigIntegerType)) {
                // if left is floating point return BigDecimal, 
                // otherwise return BigInteger
                if (left.isWrapperClass())
                    left = ((WrapperClassType)left).getWrappedPrimitiveType();
                return left.isFloatingPoint() ? 
                       PredefinedType.bigDecimalType : right;
            }       

            boolean wrapper = false;
            if (left.isWrapperClass()) {
                left = ((WrapperClassType)left).getWrappedPrimitiveType();
                wrapper = true;
            }
            if (right.isWrapperClass()) {
                right = ((WrapperClassType)right).getWrappedPrimitiveType();
                wrapper = true;
            }
            
            // handle numeric types with arbitrary arithmetic operator
            if (TypeSupport.isNumericType(left) && 
                TypeSupport.isNumericType(right)) {
                JavaType promotedType = 
                    TypeSupport.binaryNumericPromotion(left, right);
                if (wrapper && TypeSupport.isNumericType(promotedType)) {   
                    promotedType = 
                        ((PrimitiveType)promotedType).getWrapperClassType();
                }
                return promotedType;
            }
        }
        else if (TypeSupport.isBooleanType(left) && 
                 TypeSupport.isBooleanType(right)) {
            // check for boolean wrapper class: if one of the operands has the 
            // type Boolean return Boolean, otherwise return boolean.
            if (left instanceof WrapperClassType)
               return left;
            else if (right instanceof WrapperClassType)
               return right;
            else
               return PredefinedType.booleanType;
        }
        else if (left.isCompatibleWith(right)) {
            return right;
        }
        else if (right.isCompatibleWith(left)) {
            return left;
        }

        // not compatible types => return errorType
        return ErrorType.errorType;
    }

    /**
     * Analyses a unary expression + and -
     * @param op the operator
     * @param argAST the opreand
     * @return the node representing the unary expression
     */
    protected JDOQLAST analyseUnaryArithmeticExpr(JDOQLAST op, JDOQLAST argAST)
    {
        JDOQLAST expr = null;
        JavaType type = analyseUnaryArithmeticExprType(op, argAST);

        switch(op.getType()) {
        case UNARY_PLUS:
            // create the correct node type here, 
            // the lexer create Binary plus
            expr = new UnaryPlusExpr();
            expr.initialize(UNARY_PLUS, "+", type); //NOI18N
            break;
        case UNARY_MINUS:
            // create the correct node type here, 
            // the lexer create Binary plus
            expr = new UnaryMinusExpr();
            expr.initialize(UNARY_MINUS, "-", type); //NOI18N
            break;
        }
        expr.setFirstChild(addCharacterCast(argAST, type));
        expr.setLine(op.getLine());
        expr.setColumn(op.getColumn());

        return expr;
    }

    /**
     * Analyses a unary expression + and -
     * @param op the operator
     * @param argAST the operand
     * @return the type info of the operator 
     */
    protected JavaType analyseUnaryArithmeticExprType(JDOQLAST op, 
                                                      JDOQLAST argAST)
    {
        JavaType arg = argAST.getTypeInfo();

        // handle error type
        if (arg.equals(ErrorType.errorType))
            return ErrorType.errorType;
        
        // handle java.math.BigDecimal and java.math.BigInteger
        if (arg.isCompatibleWith(PredefinedType.bigDecimalType))
            return arg;

        // handle java.math.BigInteger
        if (arg.isCompatibleWith(PredefinedType.bigIntegerType))
            return arg;

        boolean wrapper = false;
        if (arg.isWrapperClass()) {
            arg = ((WrapperClassType)arg).getWrappedPrimitiveType();
            wrapper = true;
        }

        if (TypeSupport.isNumericType(arg)) {
            JavaType promotedType = TypeSupport.unaryNumericPromotion(arg);
            if (wrapper && TypeSupport.isNumericType(promotedType)) {
                promotedType = 
                    ((PrimitiveType)promotedType).getWrapperClassType();
            }
            return promotedType;
        }
        
        // if this code is reached a conditional operator was used 
        // with invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments",  op.getText())); //NOI18N
        return ErrorType.errorType;
    }

    /**
     * Analyses a complement expression.
     * A complement expression contains one of ! and ~
     * @param op the operator
     * @param argAST the operand
     * @return the node representing the complement expression
     */
    protected JDOQLAST analyseComplementExpr(JDOQLAST op, JDOQLAST argAST)
    {
        JDOQLAST expr = null;
        JavaType type = analyseComplementExprType(op, argAST);

        switch(op.getType()) {
        case BNOT:
            // create the correct node type here, 
            // the lexer create Binary plus
            expr = new ComplementExpr();
            expr.initialize(BNOT, "~", type); //NOI18N
            break;
        case LNOT:
            // create the correct node type here, 
            // the lexer create Binary plus
            expr = new NotExpr();
            expr.initialize(LNOT, "!", type); //NOI18N
            break;
        }
        expr.setFirstChild(addCharacterCast(argAST, type));
        expr.setLine(op.getLine());
        expr.setColumn(op.getColumn());

        return expr;
    }

    /**
     * Analyses a complement expression.
     * A complement expression contains one of ! and ~
     * @param op the operator
     * @param argAST the operand
     * @return the type info of the operator 
     */
    protected JavaType analyseComplementExprType(JDOQLAST op, JDOQLAST argAST)
    {
        JavaType arg = argAST.getTypeInfo();

        // handle error type
        if (arg.equals(ErrorType.errorType))
            return ErrorType.errorType;

        switch(op.getType()) {
        case BNOT:
            if (TypeSupport.isIntegralType(arg)) {
                boolean wrapper = false;
                if (arg.isWrapperClass()) {
                    arg = ((WrapperClassType)arg).getWrappedPrimitiveType();
                    wrapper = true;
                }

                JavaType promotedType = TypeSupport.unaryNumericPromotion(arg);
                if (wrapper) {
                    promotedType = 
                        ((PrimitiveType)promotedType).getWrapperClassType();
                }
                return promotedType;
            }
            break;
        case LNOT:
            if (TypeSupport.isBooleanType(arg)) {
                return arg;
            }
            break;
        }
        
        // if this code is reached a conditional operator was used with 
        // invalid arguments
        errorMsg.error(op.getLine(), op.getColumn(), 
            msg.msg("EXC_InvalidArguments", op.getText())); //NOI18N 
        return ErrorType.errorType;
    }
    
    /**
     *
     */
    protected void checkConstraints(JDOQLAST ast, VariableChecker tab)
    {
        checkConstraints(ast, null, tab);
    }

    /**
     *
     */
    protected void checkConstraints(JDOQLAST ast, String dependentVariable, 
                                    VariableChecker tab)
    {
        if (ast == null) return;
        switch (ast.getType()) {
        case VARIABLE_ACCESS:  
            tab.markUsed(ast, dependentVariable);
            break;
        case CONTAINS:
            JDOQLAST expr = (JDOQLAST)ast.getFirstChild();
            JDOQLAST var = (JDOQLAST)expr.getNextSibling();
            if (var.getType() == VARIABLE_ACCESS) {
                checkConstraints(expr, var.getText(), tab);
                tab.markConstraint(var, expr);
            }
            else {
                checkConstraints(expr, dependentVariable, tab);
            }
            break;
        case BOR:
        case OR:
            JDOQLAST left = (JDOQLAST)ast.getFirstChild();
            JDOQLAST right = (JDOQLAST)left.getNextSibling();
            // prepare tab copy for right hand side and merge 
            // the right hand side copy into vartab
            VariableChecker copy = new VariableChecker(tab);
            checkConstraints(left, dependentVariable, tab);
            checkConstraints(right, dependentVariable, copy);
            tab.merge(copy);
            break;
        default:
            for (JDOQLAST node = (JDOQLAST)ast.getFirstChild(); 
                 node != null; node = (JDOQLAST)node.getNextSibling())  {
                checkConstraints(node, dependentVariable, tab);
            }
            break;
        }
    }

}

// rules

query
    :   #(  QUERY_TREE
            candidateClass
            {
                typeNames.init(candidateClass.getName());
            }
            imports
            parameters
            variables
            ordering
            filter
        )
    ;

// ----------------------------------
// rules: candidate class
// ----------------------------------

candidateClass
{   
    errorMsg.setContext("setClass"); //NOI18N
}
    :   #( c:CANDIDATE_CLASS t:type )
        {
            candidateClass = #t.getTypeInfo();
            if (candidateClass == null) {
                errorMsg.fatal(msg.msg("EXC_UnknownCandidateClass", //NOI18N
                        #t.getText()));
            }
            #c.setTypeInfo(candidateClass);
        }
    ;

// ----------------------------------
// rules: import declaration
// ----------------------------------

imports!
{   
    errorMsg.setContext("declareImports"); //NOI18N
}
    :   ( declareImport )*
    ;

declareImport
    {  String name = null; }
    :   #( i1:IMPORT name = qualifiedName )
        {
            JavaType type = typeSupport.checkType(name);
            if (type == null) {
                errorMsg.error(#i1.getLine(), #i1.getColumn(),
                    msg.msg("EXC_UnknownType", name)); //NOI18N
            }

            String old = typeNames.declareImport(name);
            if (old != null) {
                errorMsg.error(#i1.getLine(), #i1.getColumn(),
                    msg.msg("EXC_MultipleImport", name)); //NOI18N
            }
        }
    |   #( i2:IMPORT_ON_DEMAND name = qualifiedName )
        {
            typeNames.declareImportOnDemand(name);
        }
    ;

// ----------------------------------
// rules: parameter declaration
// ----------------------------------

parameters
{   
    errorMsg.setContext("declareParameters"); //NOI18N
}
    :   ( declareParameter )*
    ;

declareParameter
    :   #( p:PARAMETER_DECL t:type ( i:IDENT )? )
        {
            ParameterDecl paramDecl = (ParameterDecl)#p;
            String name = (#i != null) ? #i.getText() : paramDecl.getName();
            JavaType type = #t.getTypeInfo();
            if (symtab.declare(name, paramDecl) != null) {
                errorMsg.error(#i.getLine(), #i.getColumn(),
                    msg.msg("EXC_MultipleDeclaration", name)); //NOI18N
            }
            paramDecl.setTypeInfo(type);
            paramDecl.setName(name);
            paramDecl.setFirstChild(#t);
            paramtab.declare(paramDecl);
            #t.setNextSibling(null);
        }
    ;

// ----------------------------------
// rules: variable declaration
// ----------------------------------

variables 
{ 
    errorMsg.setContext("declareVariables"); //NOI18N
}
    :   ( declareVariable )*
    ;

declareVariable
    :   #( v:VARIABLE_DECL t:type ( i:IDENT )? )
        {
            VariableDecl varDecl = (VariableDecl)#v;
            String name = (#i != null) ? #i.getText() : varDecl.getName();
            JavaType type = #t.getTypeInfo();
            if (symtab.declare(name, varDecl) != null) {
                errorMsg.error(#i.getLine(), #i.getColumn(),
                    msg.msg("EXC_MultipleDeclaration", name)); //NOI18N
            }
            varDecl.setTypeInfo(type);
            varDecl.setName(name);
            varDecl.setFirstChild(#t);
            #t.setNextSibling(null);
            vartab.declare(varDecl);
            varChecker.add(name);
        }
    ;

// ----------------------------------
// rules: ordering specification
// ----------------------------------

ordering 
{   
    errorMsg.setContext("setOrdering"); //NOI18N
}
    :   ( orderSpec )*
    ;

orderSpec
    :   #( ASCENDING e1:expression )
        {   analyseOrderingExpression(#e1); }
    |   #( DESCENDING e2:expression )
        {   analyseOrderingExpression(#e2); }
    ;

// ----------------------------------
// rules: filer expression
// ----------------------------------

filter
{   
    errorMsg.setContext("setFilter"); //NOI18N
}
        // There is always a filter defined and it is the last node of the query
        // tree. Otherwise all the remaining subtrees after the CANDIDATE_CLASS 
        // subtree are empty which results in a ClassCastException 
        // antlr.ASTNullType during analysis of the (non existsent) subtrees
    :   e:expression
        {
            JavaType exprType = #e.getTypeInfo();
            if (!(TypeSupport.isBooleanType(exprType) || 
                    exprType.equals(ErrorType.errorType))) {
                // filter expression must have the type boolean or Boolean
                errorMsg.error(#e.getLine(), #e.getColumn(),
                    msg.msg("EXC_BooleanFilterExpected", exprType)); //NOI18N
            }
            checkConstraints(#e, varChecker);
            varChecker.checkConstraints();
        }
    ;

expression
    {   String repr; }
    :   repr = e:exprNoCheck[false]
        {
            if (repr != null) {
               #e.setTypeInfo(ErrorType.errorType);
               errorMsg.error(#e.getLine(), #e.getColumn(),
                    msg.msg("EXC_UndefinedExpression", repr)); //NOI18N
            }
        }
    ;

exprNoCheck [boolean insideDotExpr] returns [String repr]
    {   repr = null; }  // repr is used to get the text of identifier
                        // inside a dot expression
    :   bitwiseExpr
    |   conditionalExpr
    |   relationalExpr
    |   binaryArithmeticExpr
    |   unaryArithmeticExpr
    |   complementExpr
    |   repr = primary[insideDotExpr]
    ;

bitwiseExpr
    :   #( op1:BAND left1:expression right1:expression )
        {
            #op1.setTypeInfo(analyseBitwiseExpr(#op1, #left1, #right1));
        }
    |   #( op2:BOR  left2:expression right2:expression )
        {
            #op2.setTypeInfo(analyseBitwiseExpr(#op2, #left2, #right2));
        }
    ;

conditionalExpr
    :   #( op1:AND left1:expression right1:expression )
        {
            #op1.setTypeInfo(analyseConditionalExpr(#op1, #left1, #right1));
        }
    |   #( op2:OR  left2:expression right2:expression )
        {
            #op2.setTypeInfo(analyseConditionalExpr(#op2, #left2, #right2));
        }
    ;

relationalExpr
{
    JavaType left = null;
    JavaType right = null;
}
    :   #( op1:EQUAL left1:expression right1:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op1, #left1, #right1);
        }
    |   #(  op2:NOT_EQUAL left2:expression right2:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op2, #left2, #right2);
        }
    |   #(  op3:LT left3:expression right3:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op3, #left3, #right3);
        }
    |   #(  op4:GT left4:expression right4:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op4, #left4, #right4);
        }
    |   #(  op5:LE left5:expression right5:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op5, #left5, #right5);
        }
    |   #(  op6:GE left6:expression right6:expression )
        {
            #relationalExpr = analyseRelationalExpr(#op6, #left6, #right6);
        }
    ;

binaryArithmeticExpr
    :   #( op1:PLUS left1:expression right1:expression )
        {
            #binaryArithmeticExpr = analyseBinaryArithmeticExpr(#op1, #left1, 
                                                                #right1);
        }
    |   #( op2:MINUS left2:expression right2:expression )
        {
            #binaryArithmeticExpr = analyseBinaryArithmeticExpr(#op2, #left2,
                                                                #right2);
        }
    |   #( op3:STAR left3:expression right3:expression )
        {
            #binaryArithmeticExpr = analyseBinaryArithmeticExpr(#op3, #left3,
                                                                #right3);
        }
    |   #( op4:DIV left4:expression right4:expression )
        {
            #binaryArithmeticExpr = analyseBinaryArithmeticExpr(#op4, #left4,
                                                                #right4);
        }
    ;

unaryArithmeticExpr
    :   #( op1:UNARY_PLUS arg1:expression )
        {
            #unaryArithmeticExpr = analyseUnaryArithmeticExpr(#op1, #arg1);
        }
    |   #( op2:UNARY_MINUS arg2:expression )
        {
            #unaryArithmeticExpr = analyseUnaryArithmeticExpr(#op2, #arg2);
        }
    ;

complementExpr
    :   #( op1:BNOT arg1:expression )
        {
            #complementExpr = analyseComplementExpr(#op1, #arg1);
        }
    |   #( op2:LNOT arg2:expression )
        {
            #complementExpr = analyseComplementExpr(#op2, #arg2);
        }
    ;

primary [boolean insideDotExpr] returns [String repr]
{   repr = null; } 
    :   #( c:CAST t:type e:expression )
        {
            JavaType type = #t.getTypeInfo();
            JavaType exprType = #e.getTypeInfo();
            if (! ((TypeSupport.isNumberType(type) && 
                    TypeSupport.isNumberType(exprType)) ||
                    type.isCompatibleWith(exprType) || 
                    exprType.isCompatibleWith(type))) {
                errorMsg.error(#c.getLine(), #c.getColumn(),
                    msg.msg("EXC_InvalidCast", //NOI18N
                        exprType.getName(), type.getName()));
                type = ErrorType.errorType;
            }
            CastExpr cast = new CastExpr();
            cast.initialize(#c.getType(), "CAST", type); //NOI18N
            cast.setLine(#c.getLine());
            cast.setColumn(#c.getColumn());
            cast.setFirstChild(#t);
            #t.setNextSibling(#e);
            #primary = cast;
        }
    |   literal
    |   i:THIS
        { #i.setTypeInfo(candidateClass); }
    |   repr = dotExpr
    |   repr = identifier [insideDotExpr]
    |   parameterAccess
    |   variableAccess
    |   fieldAccess
    |   navigation
    |   contains
    |   isEmpty
    |   startsWith
    |   endsWith
    ;
 
literal
    :   b1:TRUE          { #b1.setTypeInfo(PredefinedType.booleanType); }
    |   b2:FALSE         { #b2.setTypeInfo(PredefinedType.booleanType); }
    |   b3:BOOLEAN_LITERAL { #b3.setTypeInfo(PredefinedType.booleanType); }
    |   i:INT_LITERAL    { #i.setTypeInfo(PredefinedType.intType); }
    |   l:LONG_LITERAL   { #l.setTypeInfo(PredefinedType.longType); }
    |   f:FLOAT_LITERAL  { #f.setTypeInfo(PredefinedType.floatType); }
    |   d:DOUBLE_LITERAL { #d.setTypeInfo(PredefinedType.doubleType); }
    |   c:CHAR_LITERAL   { #c.setTypeInfo(PredefinedType.charType); }
    |   s:STRING_LITERAL { #s.setTypeInfo(PredefinedType.stringType); }
    |   n:NULL           { #n.setTypeInfo(NullType.nullType); }
    |   by:BYTE_LITERAL  { #by.setTypeInfo(PredefinedType.byteType); }
    |   sh:SHORT_LITERAL { #sh.setTypeInfo(PredefinedType.shortType); }
    |   con:CONSTANT     
        {
            Object value = ((ConstantExpr)#con).getValue();
            #con.setTypeInfo((value == null) ? NullType.nullType :
                             typeSupport.checkType(value.getClass()));
        }
    ;

dotExpr returns [String repr]
    {
        repr = null;
    }
    :   #( dot:DOT 
           repr = expr:exprNoCheck[true] ident:IDENT ( args:argList )?
         )
        {
            JavaType type = null;
            if (repr != null) { // possible package name
                String qualifiedName = repr + '.' + #ident.getText();
                type = typeSupport.checkType(qualifiedName);
                if (type == null) {
                    // name does not define a valid class => return qualifiedName
                    repr = qualifiedName;
                }
                else if (#args == null) {
                    // found valid class name and NO arguments specified
                    // => use of the class name
                    repr = null;
                    TypeImpl typeNode = new TypeImpl();
                    typeNode.initialize(TYPE, qualifiedName, type);
                    #dotExpr = typeNode;
                }
                else {
                    // found valid class name and arguments specified =>
                    // looks like constructor call
                    repr = null;
                    errorMsg.error(dot.getLine(), dot.getColumn(),  
                        msg.msg("EXC_InvalidMethodCall")); //NOI18N
                }
                #dot.setTypeInfo(type);
                #dot.setText(#expr.getText() + '.' + #ident.getText());
            }
            else { // no string repr of left hand side => expression is defined
                #dotExpr = analyseDotExpr(#dot, #expr, #ident, #args);
            }
        }
    ;

argList
    :   #(ARG_LIST args)
    ;

identifier [boolean insideDotExpr] returns [String repr]
    {
        repr = null;   // repr is set when ident is part of a package name spec
    }
    :   ident:IDENT ( args:argList )?
        {
            String name = #ident.getText();

            // check args, if defined => invalid method call
            if (#args != null) {
                #ident.setTypeInfo(ErrorType.errorType);
                errorMsg.error(#ident.getLine(), #ident.getColumn(),  
                    msg.msg("EXC_InvalidMethodCall")); //NOI18N
            }
            JDOQLAST ast = analyseDefinedIdentifier(#ident);
            if (ast != null) {
                #identifier = ast;
            }
            else if (insideDotExpr) {
                JavaType resolved = typeNames.resolve(name);
                if (resolved != null) {
                    // type name
                    TypeImpl typeNode = new TypeImpl();
                    typeNode.initialize(TYPE, resolved.getName(), resolved);
                    #identifier = typeNode;
                }
                else {
                    repr = #ident.getText();
                }
            }
            else {
                #ident.setTypeInfo(ErrorType.errorType);
                errorMsg.error(ident.getLine(), ident.getColumn(),
                    msg.msg("EXC_UndefinedIdentifier", //NOI18N
                        ident.getText()));
            }
        }
    ;

parameterAccess
    :   p:PARAMETER_ACCESS
        {
            String name = #p.getText();
            Decl decl = (Decl)symtab.getDeclaration(name);
            if (decl instanceof ParameterDecl) {
                #p.setTypeInfo(decl.getTypeInfo());
            }
            else {
                errorMsg.error(#p.getLine(), #p.getColumn(),
                    msg.msg("EXC_InvalidParameterAccess", name)); //NOI18N
            }
        }
    ;

variableAccess
    :   #( v:VARIABLE_ACCESS ( expression )? )
        {
            String name = #v.getText();
            Decl decl = (Decl)symtab.getDeclaration(name);
            if (decl instanceof VariableDecl) {
                #v.setTypeInfo(decl.getTypeInfo());
            }
            else {
                errorMsg.error(#v.getLine(), #v.getColumn(),
                    msg.msg("EXC_InvalidVariableAccess", name)); //NOI18N
            }
        }
    ;

staticFieldAccess
    :   #( s:STATIC_FIELD_ACCESS t:type ( i:IDENT )? )
        {
            String fieldName = ((StaticFieldAccessExpr)#s).getName();
            JavaType type = #t.getTypeInfo();
            JavaType fieldType = ErrorType.errorType;
            if (!type.isPrimitive()) {
                // left expression is of a class type
                JavaField javaField = type.getJavaField(fieldName);
                if (javaField == null) {
                    errorMsg.error(#s.getLine(), #s.getColumn(),
                        msg.msg("EXC_UnknownField",  //NOI18N
                            fieldName, type.getName()));
                }
                else {
                    if (typeSupport.isStaticField(javaField)) {
                        errorMsg.error(#s.getLine(), #s.getColumn(),  
                            msg.msg("EXC_InvalidStaticReference", //NOI18N 
                                fieldName, type.getName()));
                    }
                }
                fieldType = javaField.getType();
            }
            else {
                errorMsg.error(#s.getLine(), #s.getColumn(),
                    msg.msg("EXC_ClassTypeExpressionExpected")); //NOI18N
            }
            #s.setTypeInfo(fieldType);
        }
    ;

fieldAccess
    :   #( f:FIELD_ACCESS o:expression ( i:IDENT )? )
        {
            String fieldName = ((FieldAccessExpr)#f).getName();
            JavaType exprType = #o.getTypeInfo();
            JavaType fieldType = ErrorType.errorType;
            if (!exprType.isPrimitive()) {
                // left expression is of a class type
                JavaField javaField = exprType.getJavaField(fieldName);
                if (javaField == null) {
                    errorMsg.error(#f.getLine(), #f.getColumn(),
                        msg.msg("EXC_UnknownField",  //NOI18N
                            fieldName, exprType.getName()));
                }
                fieldType = javaField.getType();
            }
            else {
                errorMsg.error(#o.getLine(), #o.getColumn(),
                    msg.msg("EXC_ClassTypeExpressionExpected")); //NOI18N
            }
            #f.setTypeInfo(fieldType);
            if (fieldType.isPersistenceCapable()) {
                #f.setType(NAVIGATION);
            }
        }
    ;

navigation
    :   #(  n:NAVIGATION o:expression ( i:IDENT )? )
        {
            String fieldName = ((FieldAccessExpr)#n).getName();
            JavaType exprType = #o.getTypeInfo();
            JavaType fieldType = ErrorType.errorType;
            if (!exprType.isPrimitive()) {
                // left expression is of a class type
                JavaField javaField = exprType.getJavaField(fieldName);
                if (javaField == null) {
                    errorMsg.error(#n.getLine(), #n.getColumn(),
                        msg.msg("EXC_UnknownField",  //NOI18N
                            fieldName, exprType.getName()));
                }
                fieldType = javaField.getType();
            }
            else {
                errorMsg.error(#o.getLine(), #o.getColumn(),
                    msg.msg("EXC_ClassTypeExpressionExpected")); //NOI18N
            }
            #n.setTypeInfo(fieldType);
        }
    ;

contains
    :   #(c:CONTAINS collection:expression args:args )
        {
            // check type of collection expression
            JavaType type = #collection.getTypeInfo();
            if (!type.isJDOSupportedCollection()) {
                errorMsg.error(#collection.getLine(), #collection.getColumn(),
                    msg.msg("EXC_CollectionTypeExpected", type.getName())); //NOI18N
            }
            checkContainsArgs(#collection, #c, #args);
            #c.setTypeInfo(PredefinedType.booleanType);
        }
    ;

isEmpty
    :   #(i:IS_EMPTY collection:expression args:args )
        {
            // check type of collection expression
            JavaType type = #collection.getTypeInfo();
            if (!type.isJDOSupportedCollection()) {
                errorMsg.error(#collection.getLine(), #collection.getColumn(),
                    msg.msg("EXC_CollectionTypeExpected", type.getName())); //NOI18N
            }
            checkIsEmptyArgs(#args);
            #i.setTypeInfo(PredefinedType.booleanType);
        }
    ;

startsWith
    :   #(s:STARTS_WITH string:expression args:args )
        {
            // check type of string expression
            JavaType type = #string.getTypeInfo();
            if (!PredefinedType.stringType.equals(type)) {
                errorMsg.error(#string.getLine(), #string.getColumn(),
                    msg.msg("EXC_StringTypeExpected", type.getName())); //NOI18N
            }
            checkStringCallArgs(#s, #args);
            #s.setTypeInfo(PredefinedType.booleanType);
        }
    ;

endsWith
    :   #(e:ENDS_WITH string:expression args:args )
        {
            // check type of string expression
            JavaType type = #string.getTypeInfo();
            if (!PredefinedType.stringType.equals(type)) {
                errorMsg.error(#string.getLine(), #string.getColumn(),
                    msg.msg("EXC_StringTypeExpected", type.getName())); //NOI18N
            }
            checkStringCallArgs(#e, #args);
            #e.setTypeInfo(PredefinedType.booleanType);
        }
    ;

args
    : (expression)*
    ;

qualifiedName returns [String name]
    {   name = null; }
    :   id1:IDENT
        {
            name = #id1.getText();
        }
    |   #(  d:DOT
            name = qualifiedName
            id2:IDENT
            {
                name += (#d.getText() + #id2.getText());
            }
        )
    ;

// ----------------------
// types
// ----------------------

type
    { String name = null; }
    :   name = qn:qualifiedName
        {
            // First check type name as it is
            JavaType type = typeSupport.checkType(name);
            if (type == null)
                // Check type imports
                type = typeNames.resolve(name);
            if (type == null) {
                // unknown type
                errorMsg.error(#qn.getLine(), #qn.getColumn(),
                    msg.msg("EXC_UnknownType", name)); //NOI18N
            }
            TypeImpl typeNode = new TypeImpl();
            typeNode.initialize(TYPE, type.getName(), type);
            #type = typeNode;
        }
    |   t:TYPE
        {
            Class clazz = ((NodeImpl)#t).getJavaClass();
            if (clazz != null)
                #t.setTypeInfo(typeSupport.checkType(clazz));
            else
                #t.setTypeInfo(typeSupport.checkType(#t.getText()));
        }
    |   p:primitiveType
        {
            name = #p.getText();
            TypeImpl typeNode = new TypeImpl();
            typeNode.initialize(TYPE, name, typeSupport.checkType(name));
            #type = typeNode;
        }
    ;

primitiveType
    :   BOOLEAN
    |   BYTE
    |   CHAR
    |   SHORT
    |   INT
    |   FLOAT
    |   LONG
    |   DOUBLE
    ;
