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
 * JDOQL.g
 *
 * Created on August 28, 2001
 */

header
{
    package org.apache.jdo.impl.jdoql.jdoqlc;

    import antlr.MismatchedTokenException;
    import antlr.MismatchedCharException;
    import antlr.NoViableAltException;
    import antlr.NoViableAltForCharException;
    import antlr.TokenStreamRecognitionException;
    
    import org.apache.jdo.jdoql.JDOQueryException;
    import org.apache.jdo.impl.jdoql.tree.*;
    import org.apache.jdo.util.I18NHelper;
}

//===== Lexical Analyzer Class Definitions =====

/**
 * This class defines the lexical analysis for the JDOQL compiler.
 *
 * @author  Michael Bouschen
 */
class JDOQLLexer extends Lexer;
options
{
    k = 2;
    exportVocab = JDOQL;
    charVocabulary = '\u0000'..'\uFFFE'; //NOI18N
}

tokens {

    IMPORT = "import"         <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    IMPORT_ON_DEMAND          <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>;
    THIS = "this"             <AST=org.apache.jdo.impl.jdoql.tree.ThisExpr>; //NOI18N
    ASCENDING = "ascending"   <AST=org.apache.jdo.impl.jdoql.tree.AscendingOrderingExpr>; //NOI18N
    DESCENDING = "descending" <AST=org.apache.jdo.impl.jdoql.tree.DescendingOrderingExpr>; //NOI18N
    
    // types
    
    BOOLEAN = "boolean"       <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    BYTE = "byte"             <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    CHAR = "char"             <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    SHORT = "short"           <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    INT = "int"               <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    FLOAT = "float"           <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    LONG = "long"             <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    DOUBLE = "double"         <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>; //NOI18N
    
    // literals
    
    NULL = "null"   <AST=org.apache.jdo.impl.jdoql.tree.ConstantExpr>; //NOI18N
    TRUE = "true"   <AST=org.apache.jdo.impl.jdoql.tree.BooleanLiteralExpr>; //NOI18N
    FALSE = "false" <AST=org.apache.jdo.impl.jdoql.tree.BooleanLiteralExpr>; //NOI18N

    // other tokens

    EQUAL     <AST=org.apache.jdo.impl.jdoql.tree.EqualsExpr>;
    NOT_EQUAL <AST=org.apache.jdo.impl.jdoql.tree.NotEqualsExpr>;
    GE        <AST=org.apache.jdo.impl.jdoql.tree.GreaterThanEqualsExpr>;
    GT        <AST=org.apache.jdo.impl.jdoql.tree.GreaterThanExpr>;
    LE        <AST=org.apache.jdo.impl.jdoql.tree.LessThanEqualsExpr>;
    LT        <AST=org.apache.jdo.impl.jdoql.tree.LessThanExpr>;

    PLUS      <AST=org.apache.jdo.impl.jdoql.tree.PlusExpr>;
    MINUS     <AST=org.apache.jdo.impl.jdoql.tree.MinusExpr>;
    STAR      <AST=org.apache.jdo.impl.jdoql.tree.TimesExpr>;
    DIV       <AST=org.apache.jdo.impl.jdoql.tree.DivideExpr>;

    BOR       <AST=org.apache.jdo.impl.jdoql.tree.OrExpr>;
    OR        <AST=org.apache.jdo.impl.jdoql.tree.ConditionalOrExpr>;
    BAND      <AST=org.apache.jdo.impl.jdoql.tree.AndExpr>;
    AND       <AST=org.apache.jdo.impl.jdoql.tree.ConditionalAndExpr>;
    LNOT      <AST=org.apache.jdo.impl.jdoql.tree.NotExpr>;
    BNOT      <AST=org.apache.jdo.impl.jdoql.tree.ComplementExpr>;

    INT_LITERAL    <AST=org.apache.jdo.impl.jdoql.tree.IntLiteralExpr>;
    LONG_LITERAL   <AST=org.apache.jdo.impl.jdoql.tree.IntLiteralExpr>;
    FLOAT_LITERAL  <AST=org.apache.jdo.impl.jdoql.tree.FloatLiteralExpr>;
    DOUBLE_LITERAL <AST=org.apache.jdo.impl.jdoql.tree.DoubleLiteralExpr>;
    CHAR_LITERAL   <AST=org.apache.jdo.impl.jdoql.tree.CharLiteralExpr>;
    STRING_LITERAL <AST=org.apache.jdo.impl.jdoql.tree.ConstantExpr>;
    IDENT          <AST=org.apache.jdo.impl.jdoql.tree.IdentifierExpr>;

    DOT <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>;

    // lexer only

    LPAREN;
    RPAREN;
    COMMA;
    SEMI;
    WS;
    NEWLINE;
    ESC; 
    HEX_DIGIT;
    EXPONENT;
    FLOATINGPOINT_SUFFIX;
    UNICODE_ESCAPE;
    UNICODE_CHAR;
}

{
    /** The error message support class. */
    protected ErrorMsg errorMsg;
    
    /** The width of a tab stop. */
    protected static final int TABSIZE = 4;

    /** */
    protected static final int EOF_CHAR = 65535; // = (char) -1 = EOF

    /** I18N support. */
    protected static final I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", JDOQLLexer.class.getClassLoader()); //NOI18N
    
    /**
     *
     */
    public void init(ErrorMsg errorMsg)
    {
        this.errorMsg = errorMsg;
    }
    
    /**
     *
     */
    public void tab() 
    {
        int column = getColumn();
        int newColumn = (((column-1)/TABSIZE)+1)*TABSIZE+1;
        setColumn(newColumn);
    }

    /**
     *
     */
    public void reportError(int line, int column, String s)
    {
        errorMsg.error(line, column, s);
    }

    /**
     * Report lexer exception errors caught in nextToken()
     */
    public void reportError(RecognitionException e)
    {
        handleANTLRException(e, errorMsg);
    }

    /**
     * Lexer error-reporting function
     */
    public void reportError(String s)
    {
        errorMsg.error(0, 0, s);
    }

    /**
     * Lexer warning-reporting function
     */
    public void reportWarning(String s)
    {
        throw new JDOQueryException(s);
    }

    /**
     *
     */
    public static void handleANTLRException(ANTLRException ex, ErrorMsg errorMsg)
    {
        if (ex instanceof MismatchedCharException) {
            MismatchedCharException mismatched = (MismatchedCharException)ex;
            if (mismatched.mismatchType == MismatchedCharException.CHAR) {
                if (mismatched.foundChar == EOF_CHAR) {
                    errorMsg.error(mismatched.getLine(), mismatched.getColumn(), 
                        msg.msg("EXC_UnexpectedEOF")); //NOI18N
                }
                else {
                    errorMsg.error(mismatched.getLine(), mismatched.getColumn(), 
                        msg.msg("EXC_ExpectedCharFound", //NOI18N
                            String.valueOf((char)mismatched.expecting), 
                            String.valueOf((char)mismatched.foundChar)));
                }
                return;
            }
        }
        else if (ex instanceof MismatchedTokenException) {
            MismatchedTokenException mismatched = (MismatchedTokenException)ex;
            Token token = mismatched.token;
            if ((mismatched.mismatchType == MismatchedTokenException.TOKEN) &&
                (token != null)) {
                if (token.getType() == Token.EOF_TYPE) {
                    errorMsg.error(token.getLine(), token.getColumn(), 
                        msg.msg("EXC_UnexpectedEOF")); //NOI18N
                }
                else {
                    errorMsg.error(token.getLine(), token.getColumn(), 
                        msg.msg("EXC_SyntaxErrorAt", token.getText())); //NOI18N
                }
                return;
            }
        }
        else if (ex instanceof NoViableAltException) {
            Token token = ((NoViableAltException)ex).token;
            if (token != null) {
                if (token.getType() == Token.EOF_TYPE) {
                    errorMsg.error(token.getLine(), token.getColumn(), 
                        msg.msg("EXC_UnexpectedEOF")); //NOI18N
                }
                else {
                    errorMsg.error(token.getLine(), token.getColumn(), 
                        msg.msg("EXC_UnexpectedToken", token.getText())); //NOI18N
                }
                return;
            }
        }
        else if (ex instanceof NoViableAltForCharException) {
            NoViableAltForCharException noViableAlt = (NoViableAltForCharException)ex;
            errorMsg.error(noViableAlt.getLine(), noViableAlt.getColumn(), 
                msg.msg("EXC_UnexpectedChar", String.valueOf((char)noViableAlt.foundChar)));//NOI18N
        }
        else if (ex instanceof TokenStreamRecognitionException) {
            handleANTLRException(((TokenStreamRecognitionException)ex).recog, errorMsg);
        }

        // no special handling from aboves matches the exception if this line is reached =>
        // make it a syntax error
        int line = 0;
        int column = 0;
        if (ex instanceof RecognitionException) {
            line = ((RecognitionException)ex).getLine();
            column = ((RecognitionException)ex).getColumn();
        }
        errorMsg.error(line, column, msg.msg("EXC_SyntaxError")); //NOI18N
    }
}

// OPERATORS

// The following Java operators are included although not supported by JDOQL:
//   ASSIGN, DIV_ASSIGN, PLUS_ASSIGN, INC, MINUS_ASSIGN, DEC, STAR_ASSIGN, 
//   SR, SL, BOR_ASSIGN, BAND_ASSIGN
// The lexer recognizes them, but the parser will result in a syntax error.
// This gurantees an error message. Othewise the java operator pre-decrement
// is mapped to two unray minus. Two unray minus are valid in JDOQL, but not 
// indented.

LPAREN          :   '('     ;
RPAREN          :   ')'     ;
COMMA           :   ','     ;
//DOT           :   '.'     ;
ASSIGN          :   "="     ; //NOI18N
EQUAL           :   "=="    ; //NOI18N
LNOT            :   '!'     ;
BNOT            :   '~'     ;
NOT_EQUAL       :   "!="    ; //NOI18N
DIV             :   '/'     ;
DIV_ASSIGN      :   "/="    ; //NOI18N
PLUS            :   '+'     ;
PLUS_ASSIGN     :   "+="    ; //NOI18N
INC             :   "++"    ; //NOI18N
MINUS           :   '-'     ;
MINUS_ASSIGN    :   "-="    ; //NOI18N
DEC             :   "--"    ; //NOI18N
STAR            :   '*'     ;
STAR_ASSIGN     :   "*="    ; //NOI18N
SR              :   ">>"    ; //NOI18N
GE              :   ">="    ; //NOI18N
GT              :   ">"     ; //NOI18N
SL              :   "<<"    ; //NOI18N
LE              :   "<="    ; //NOI18N
LT              :   '<'     ;
BOR             :   '|'     ;
BOR_ASSIGN      :   "|="    ; //NOI18N
OR              :   "||"    ; //NOI18N
BAND            :   '&'     ;
BAND_ASSIGN     :   "&="    ; //NOI18N
AND             :   "&&"    ; //NOI18N
SEMI            :   ';'     ;

// Whitespace -- ignored
WS
    :   (   ' '
        |   '\t'
        |   '\f'
        )+
        { _ttype = Token.SKIP; }
    ;

NEWLINE
    :   (   "\r\n"  //NOI18N
        |   '\r'
        |   '\n'
        )
        { 
            newline(); 
            _ttype = Token.SKIP; 
        }
    ;

// character literals
CHAR_LITERAL
    :   '\'' ( ESC | ~'\'' ) '\'' 
    ;

// string literals
STRING_LITERAL
    :  '"' ( ESC | ~'"')* '"' //NOI18N
    ;

protected
ESC
    :   '\\'
        (   'n'
        |   'r'
        |   't'
        |   'b'
        |   'f'
        |   '"' //NOI18N
        |   '\''
        |   '\\'
        |   ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT 
        |   ('0'..'3')
            (
                options {
                    warnWhenFollowAmbig = false;
                }
            :   ('0'..'7')
                (   
                    options {
                        warnWhenFollowAmbig = false;
                    }
                :   '0'..'7'
                )?
            )?
        |   ('4'..'7')
            (
                options {
                    warnWhenFollowAmbig = false;
                }
            :   ('0'..'9')
            )?
        )
    ;

protected
HEX_DIGIT
    :   ('0'..'9'|'A'..'F'|'a'..'f')
    ;

INT_LITERAL
    {   
        boolean isDecimal=false;
        int tokenType = DOUBLE_LITERAL; 
    }
    :   '.' {_ttype = DOT;}
            (('0'..'9')+ {tokenType = DOUBLE_LITERAL;}
             (EXPONENT)? 
             (tokenType = FLOATINGPOINT_SUFFIX)? 
            { _ttype = tokenType; })?
    |   (   '0' {isDecimal = true;} // special case for just '0'
            (   ('x'|'X')
                (
                    options {
                        warnWhenFollowAmbig=false;
                    }
                :   HEX_DIGIT
                )+
            |   ('0'..'7')+
            )?
        |   ('1'..'9') ('0'..'9')*  {isDecimal=true;}
        )
        (   ('l'|'L') { _ttype = LONG_LITERAL; }
        |   {isDecimal}?
            {tokenType = DOUBLE_LITERAL;} 
            (   '.' ('0'..'9')* (EXPONENT)? 
                (tokenType = FLOATINGPOINT_SUFFIX)?
            |   EXPONENT (tokenType = FLOATINGPOINT_SUFFIX)?
            |   tokenType = FLOATINGPOINT_SUFFIX
            )
            { _ttype = tokenType; }
        )?
    ;

protected
EXPONENT
    :   ('e'|'E') ('+'|'-')? ('0'..'9')+
    ;

protected
FLOATINGPOINT_SUFFIX returns [int tokenType]
    : 'f' { tokenType = FLOAT_LITERAL; } 
    | 'F' { tokenType = FLOAT_LITERAL; } 
    | 'd' { tokenType = DOUBLE_LITERAL; } 
    | 'D' { tokenType = DOUBLE_LITERAL; } 
    ;

IDENT
    options {paraphrase = "an identifier"; testLiterals=true;} //NOI18N
    :   (   'a'..'z'
        |   'A'..'Z'
        |   '_'
        |   '$'
        |   UNICODE_ESCAPE
        |   c1:'\u0080'..'\uFFFE'
            { 
                if (!Character.isJavaIdentifierStart(c1)) {
                    errorMsg.error(getLine(), getColumn(), 
                        msg.msg("EXC_UnexpectedChar", String.valueOf(c1)));//NOI18N
                }
            }
        ) 
        (   'a'..'z'
        |   'A'..'Z'
        |   '_'
        |   '$'
        |   '0'..'9'
        |   UNICODE_ESCAPE
        |   c2:'\u0080'..'\uFFFE'
            {   
                if (!Character.isJavaIdentifierPart(c2)) {
                    errorMsg.error(getLine(), getColumn(), 
                        msg.msg("EXC_UnexpectedChar", String.valueOf(c2)));//NOI18N
                }
            }
        )*
    ;

protected
UNICODE_ESCAPE
    : '\\' ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
        {
            try {
                String tmp = text.toString();
                char c  = (char)Integer.parseInt(tmp.substring(tmp.length() - 4, tmp.length()), 16);
                // problems using ANTLR feature $setText => use generated code
                text.setLength(_begin); 
                text.append(new Character(c).toString());
            }
            catch (NumberFormatException ex) {
                errorMsg.fatal(msg.msg("ERR_UnexpectedExceptionUnicode"), ex); //NOI18N
            }
        }
    ;

//===== Parser Class Definitions =====

/**
 * This class defines the syntax analysis (parser) of the JDOQL compiler.
 *
 * @author  Michael Bouschen
 */
class JDOQLParser extends Parser;

options {
    k = 2;                   // two token lookahead
    exportVocab = JDOQL;
    buildAST = true;
    ASTLabelType = "JDOQLAST"; // NOI18N
}

tokens
{
    // "imaginary" tokens, that have no corresponding real input

    QUERY_TREE             <AST=org.apache.jdo.impl.jdoql.tree.Tree>;
    CANDIDATE_CLASS        <AST=org.apache.jdo.impl.jdoql.tree.CandidateClassImpl>;
    PARAMETER_DECL         <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>;
    VARIABLE_DECL          <AST=org.apache.jdo.impl.jdoql.tree.VariableDecl>;

    // operators
    OBJECT_EQUAL           <AST=org.apache.jdo.impl.jdoql.tree.EqualsExpr>;
    OBJECT_NOT_EQUAL       <AST=org.apache.jdo.impl.jdoql.tree.NotEqualsExpr>;
    COLLECTION_EQUAL       <AST=org.apache.jdo.impl.jdoql.tree.EqualsExpr>;
    COLLECTION_NOT_EQUAL   <AST=org.apache.jdo.impl.jdoql.tree.NotEqualsExpr>;
    UNARY_MINUS            <AST=org.apache.jdo.impl.jdoql.tree.UnaryMinusExpr>;
    UNARY_PLUS             <AST=org.apache.jdo.impl.jdoql.tree.UnaryPlusExpr>;
    CONCAT                 <AST=org.apache.jdo.impl.jdoql.tree.PlusExpr>;
    CAST                   <AST=org.apache.jdo.impl.jdoql.tree.CastExpr>;

    // special dot expressions
    FIELD_ACCESS           <AST=org.apache.jdo.impl.jdoql.tree.FieldAccessExpr>;
    STATIC_FIELD_ACCESS    <AST=org.apache.jdo.impl.jdoql.tree.FieldAccessExpr>;
    NAVIGATION             <AST=org.apache.jdo.impl.jdoql.tree.FieldAccessExpr>;
    CONTAINS               <AST=org.apache.jdo.impl.jdoql.tree.ContainsCallExpr>;
    STARTS_WITH            <AST=org.apache.jdo.impl.jdoql.tree.StartsWithCallExpr>;
    ENDS_WITH              <AST=org.apache.jdo.impl.jdoql.tree.EndsWithCallExpr>;
    IS_EMPTY               <AST=org.apache.jdo.impl.jdoql.tree.IsEmptyCallExpr>;
    
    // identifier types
    VARIABLE_ACCESS        <AST=org.apache.jdo.impl.jdoql.tree.VariableAccessExpr>;
    PARAMETER_ACCESS       <AST=org.apache.jdo.impl.jdoql.tree.ParameterAccessExpr>;

    // types
    TYPE                   <AST=org.apache.jdo.impl.jdoql.tree.TypeImpl>;

    // constant value
    CONSTANT               <AST=org.apache.jdo.impl.jdoql.tree.ConstantExpr>;

    // Query Tree nodes
    BOOLEAN_LITERAL        <AST=org.apache.jdo.impl.jdoql.tree.BooleanLiteralExpr>;
    BYTE_LITERAL           <AST=org.apache.jdo.impl.jdoql.tree.ByteLiteralExpr>;
    SHORT_LITERAL          <AST=org.apache.jdo.impl.jdoql.tree.ShortLiteralExpr>;

    // temporary node removed by Semantic
    ARG_LIST               <AST=org.apache.jdo.impl.jdoql.tree.NodeImpl>;
}

{
    /** The error message support class. */
    protected ErrorMsg errorMsg;
    
    /** I18N support. */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", JDOQLParser.class.getClassLoader()); //NOI18N
    
    /**
     *
     */
    public void init(ErrorMsg errorMsg)
    {
        this.errorMsg = errorMsg;
    }
    
    /**
     * ANTLR method called when an error was detected.
     */
    public void reportError(RecognitionException ex)
    {
        JDOQLLexer.handleANTLRException(ex, errorMsg);
    }

    /**
     * ANTLR method called when an error was detected.
     */
    public void reportError(String s)
    {
        errorMsg.error(0, 0, s);
    }

    /**
     *
     */
    public void reportError(int line, int column, String s)
    {
        errorMsg.error(line, column, s);
    }

    /**
     * ANTLR method called when a warning was detected.
     */
    public void reportWarning(String s)
    {
        throw new JDOQueryException(s);
    }

}

// ----------------------------------
// rules: import declaration
// ----------------------------------

parseImports
{   
    errorMsg.setContext("declareImports");  //NOI18N
}
    :   ( declareImport ( SEMI! declareImport )* )? ( SEMI! )? EOF!
    ;

declareImport
    :   i:IMPORT^ q:qualifiedNameStar
        {
            if (#q.getType() == STAR) {
                #i.setType(IMPORT_ON_DEMAND);
                #i.setFirstChild(#q.getFirstChild());
            }
        }
    ;

// ----------------------------------
// rules: parameter declaration
// ----------------------------------

parseParameters
{   
    errorMsg.setContext("declareParameters"); //NOI18N
}
    :   ( declareParameter ( COMMA! declareParameter )* )? ( COMMA! )? EOF!
    ;

declareParameter
    :   type IDENT
        { 
            ParameterDecl paramDecl = 
                new ParameterDecl(new Token(PARAMETER_DECL,"parameterDecl")); //NOI18N
            paramDecl.setFirstChild(#declareParameter);
            #declareParameter = paramDecl;
    }
    ;

// ----------------------------------
// rules: variables declaration
// ----------------------------------

parseVariables
{   
    errorMsg.setContext("declareVariables");  //NOI18N
}
    :   ( declareVariable ( SEMI! declareVariable )* )? ( SEMI! )? EOF!
    ;

declareVariable
    :   type IDENT
        {  
            VariableDecl variableDecl = 
                new VariableDecl(new Token(VARIABLE_DECL,"variableDecl")); //NOI18N
            variableDecl.setFirstChild(#declareVariable);
            #declareVariable = variableDecl;
        }
    ;

// ----------------------------------
// rules ordering specification
// ----------------------------------

parseOrdering
{   
    errorMsg.setContext("setOrdering");  //NOI18N
}
    :   ( orderSpec ( COMMA! orderSpec )* )? ( COMMA! )? EOF!
    ;

orderSpec
    :   e:expression ( ASCENDING^ | DESCENDING^ )
    ; 

// ----------------------------------
// rules filer expression
// ----------------------------------

parseFilter
{  
    errorMsg.setContext("setFilter");  //NOI18N
}
    :   e:expression EOF!
    ;

// This is a list of expressions.
expressionList
    :   expression (COMMA! expression)*
    ;

expression
    :   invalidExpression
    ;

// assignment expression: not supported => syntax error
invalidExpression
    :   conditionalOrExpression 
        ( 
            op:invalidOperator
            {
                errorMsg.error(#op.getLine(), #op.getColumn(), 
                    msg.msg("EXC_UnexpectedToken", #op.getText())); //NOI18N
            }
            invalidExpression
        )?
    ;

invalidOperator
    :   ASSIGN | PLUS_ASSIGN | MINUS_ASSIGN | STAR_ASSIGN | 
        DIV_ASSIGN | BAND_ASSIGN | BOR_ASSIGN | SR | SL
    ;

// conditional or ||
conditionalOrExpression
    :   conditionalAndExpression (OR^ conditionalAndExpression)*
    ;

// conditional and &&
conditionalAndExpression
    :   inclusiveOrExpression (AND^ inclusiveOrExpression)*
    ;

// bitwise or logical or |
inclusiveOrExpression
    :   andExpression (BOR^ andExpression)*
    ;

// bitwise or logical and &
andExpression
    :   equalityExpression (BAND^ equalityExpression)*
    ;

// equality/inequality ==/!=
equalityExpression
    :   relationalExpression ((NOT_EQUAL^ | EQUAL^) relationalExpression)*
    ;
// boolean relational expressions
relationalExpression
    :   additiveExpression
        (   (   LT^
            |   GT^
            |   LE^
            |   GE^
            )
            additiveExpression
        )*
    ;

// binary addition/subtraction
additiveExpression
    :   multiplicativeExpression ((PLUS^ | MINUS^) multiplicativeExpression)*
    ;
// multiplication/division/modulo
multiplicativeExpression
    :   unaryExpression ((STAR^ | DIV^ ) unaryExpression)*
    ;

unaryExpression
    :   MINUS^ {#MINUS.setType(UNARY_MINUS);} unaryExpression
    |   PLUS^  {#PLUS.setType(UNARY_PLUS);} unaryExpression
    |   i:INC^ unaryExpression
        {
            errorMsg.error(#i.getLine(), #i.getColumn(), 
                msg.msg("EXC_UnexpectedToken", #i.getText())); //NOI18N
        }
    |   d:DEC^ unaryExpression
        {
            errorMsg.error(#d.getLine(), #d.getColumn(), 
                msg.msg("EXC_UnexpectedToken", #d.getText())); //NOI18N
        }
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus
    :   BNOT^ unaryExpression
    |   LNOT^ unaryExpression
    |   ( LPAREN type RPAREN unaryExpression )=>
          lp:LPAREN^ {#lp.setType(CAST);} type RPAREN! unaryExpression
    |   postfixExpression
    ;

// qualified names, field access, method invocation
postfixExpression
    :   primary
        (   DOT^ IDENT ( argList )? )*
    ;

argList
    :   LPAREN!
        (   expressionList
            {
                NodeImpl argListRoot = new NodeImpl(new Token(ARG_LIST,"argList")); //NOI18N
                argListRoot.setFirstChild(#argList);
                #argList = argListRoot;
            }
        |   /* empty list */
            {
                NodeImpl argListRoot = new NodeImpl(new Token(ARG_LIST,"argList")); //NOI18N
                #argList = argListRoot;
            }
        )
        RPAREN!
    ;

// the basic element of an expression
primary
    :   IDENT
    |   literal
    |   THIS
    |   LPAREN! expression RPAREN!
    ;

literal
    :   TRUE
    |   FALSE
    |   INT_LITERAL
    |   LONG_LITERAL
    |   FLOAT_LITERAL
    |   DOUBLE_LITERAL
    |   c:CHAR_LITERAL
        {
            // strip quotes from the token text
            String text = #c.getText();
            #c.setText(text.substring(1,text.length()-1));
        }
    |   s:STRING_LITERAL
        {
            // strip quotes from the token text
            String text = #s.getText();
            #s.setText(text.substring(1,text.length()-1));
        }
    |   NULL
    ;

qualifiedName
    :   IDENT ( DOT^ IDENT )*
    ;

qualifiedNameStar
    :   IDENT ( DOT^ IDENT )* ( DOT! STAR^ )?
    ;

type
    :   qualifiedName
    |   primitiveType
    ;

// The primitive types.
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
