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

import antlr.collections.AST;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLTokenTypes;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.AndExpression;
import org.apache.jdo.jdoql.tree.CandidateClass;
import org.apache.jdo.jdoql.tree.CastExpression;
import org.apache.jdo.jdoql.tree.ComplementExpression;
import org.apache.jdo.jdoql.tree.ConditionalAndExpression;
import org.apache.jdo.jdoql.tree.ConditionalOrExpression;
import org.apache.jdo.jdoql.tree.ConstantExpression;
import org.apache.jdo.jdoql.tree.Declaration;
import org.apache.jdo.jdoql.tree.DivideExpression;
import org.apache.jdo.jdoql.tree.EqualsExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.ExpressionFactory;
import org.apache.jdo.jdoql.tree.FieldAccessExpression;
import org.apache.jdo.jdoql.tree.GreaterThanEqualsExpression;
import org.apache.jdo.jdoql.tree.GreaterThanExpression;
import org.apache.jdo.jdoql.tree.IdentifierExpression;
import org.apache.jdo.jdoql.tree.LessThanEqualsExpression;
import org.apache.jdo.jdoql.tree.LessThanExpression;
import org.apache.jdo.jdoql.tree.MethodCallExpression;
import org.apache.jdo.jdoql.tree.MinusExpression;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.jdoql.tree.NotEqualsExpression;
import org.apache.jdo.jdoql.tree.NotExpression;
import org.apache.jdo.jdoql.tree.OrExpression;
import org.apache.jdo.jdoql.tree.ParameterDeclaration;
import org.apache.jdo.jdoql.tree.PlusExpression;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.jdoql.tree.StaticFieldAccessExpression;
import org.apache.jdo.jdoql.tree.TimesExpression;
import org.apache.jdo.jdoql.tree.UnaryMinusExpression;
import org.apache.jdo.jdoql.tree.UnaryPlusExpression;
import org.apache.jdo.jdoql.tree.VariableDeclaration;


/**
 * This node represents the root of a query tree.
 * You can use it to factorize this node's children, as there are
 * candidate class, declarations, filter expression and
 * ordering expressions.
 *
 * @author Michael Watzek
 */
public final class Tree extends NodeImpl implements QueryTree, ExpressionFactory
{
    static final JavaKeyWords           javaKeyWords =          new JavaKeyWords();

    CandidateClass                      candidateClass;
    Map                                 parameterMap;
    List                                parameterList;
    Map                                 variableMap;
    List                                orderings;
    Expression                          filter;

    String                              serializedCandidateClassName; // serialization support

    /**
     * The noarg constructor is called by persistence manager internal.
     */
    public Tree()
    {   super( JDOQLTokenTypes.QUERY_TREE, "QueryTree", null ); //NOI18N
        init();
    }

    /**
     * This constructor is called by semantic analysis only.
     * @param candidateClass the candidate class
     * @param parameterDeclarations the antlr node containing all
     * parameter declaration nodes as siblings 
     * @param variableDeclarations the antlr node containing all
     * variable declaration nodes as siblings 
     * @param orderingExpressions the antlr node containing all
     * ordering nodes as siblings 
     * @param filter the filter expression
     */
    public Tree(CandidateClass candidateClass, 
                ParameterDecl parameterDeclarations, 
                VariableDecl variableDeclarations, 
                OrderingExpr orderingExpressions, 
                Expr filter)
    {   this();
        Class clazz = candidateClass.getJavaClass();
        this.clazz = clazz;
        this.candidateClass = candidateClass;
        for( ParameterDecl current=parameterDeclarations; current!=null; current = (ParameterDecl)current.getNextSibling() )
        {   this.parameterMap.put( current.getName(), current );
            this.parameterList.add( current );
        }
        for( VariableDecl current=variableDeclarations; current!=null; current = (VariableDecl)current.getNextSibling() )
            this.variableMap.put( current.getName(), current );
        for( OrderingExpr current=orderingExpressions; current!=null; current = (OrderingExpr)current.getNextSibling() )
            this.orderings.add( current );
        this.filter = filter;
        initANTLRAST();
    }

    /**
     * Creates and returns a copy of this object.
     * @return the copy
     * @exception CloneNotSupportedException thrown by <code>super.clone()</code>
     */
    protected Object clone() throws CloneNotSupportedException
    {   Tree copy = (Tree) super.clone();
        init();
        return copy;
    }

    /**
     * Sets the candidate class for this query tree.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>clazz</code> is <code>null</code>.
     * Otherwise this method invalidates this tree:
     * Parameters, variables, orderings and the filter are nullified.
     * @param clazz the candidate class
     * @exception <code>NullPointerException</code> if the argument <code>clazz</code> is null
     */
    public void setCandidateClass(Class clazz)
    {   if( clazz==null )
            throw new NullPointerException();
        if( this.clazz!=null )
            reset();
        this.clazz = clazz;
        this.candidateClass = new CandidateClassImpl( new TypeImpl(clazz) );
    }

    /**
     * Declares a parameter for this query tree.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>type</code> or <code>parameter</code> are <code>null</code>.
     * If a parameter is already declared having the same name
     * as the argument <code>parameter</code>, then that declaration is
     * replaced by this declaration.
     * Once you have declared a parameter, you can access it using method
     * <code>newIdentifier</code>.
     * Please note: You can not declare a parameter and a variable having the same name.
     * @param clazz the instance of a Java class which is the type of the declared parameter
     * @param parameter the name of the declared parameter
     * @exception NullPointerException if type or parameter are null
     * @exception JDOQueryException if a variable has been declared with the same name
     */
    public void declareParameter(Class clazz, String parameter)
    {   if( clazz==null ||
            parameter==null )
            throw new NullPointerException();
        if( this.variableMap.get(parameter)!=null )
            throw new JDOQueryException( msg.msg("EXC_ParameterVariableCollision", parameter) ); //NOI18N
        ParameterDeclaration decl = new ParameterDecl( new TypeImpl(clazz), parameter );
        this.parameterMap.put( parameter, decl );
        this.parameterList.add( decl );
    }

    /**
     * Declares a variable for this query tree.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>type</code> or <code>variable</code> are <code>null</code>.
     * If a variable is already declared having the same name
     * as the argument <code>variable</code>, then that declaration is
     * replaced by this declaration.
     * Once you have declared a variable, you can access it using method
     * <code>newIdentifier</code>.
     * Please note: You can not declare a parameter and a variable having the same name.
     * @param clazz the instance of a Java class which is the type of the declared variable
     * @param variable the name of the declared variable
     * @exception NullPointerException if type or variable are null
     * @exception JDOQueryException if a parameter has been declared with the same name
     */
    public void declareVariable(Class clazz, String variable)
    {   if( clazz==null ||
            variable==null )
            throw new NullPointerException();
        if( this.parameterMap.get(variable)!=null )
            throw new JDOQueryException( msg.msg("EXC_VariableParameterCollision", variable) ); //NOI18N
        VariableDeclaration decl = new VariableDecl( new TypeImpl(clazz), variable );
        this.variableMap.put( variable, decl );
    }

    /**
     * Adds an ascending ordering expression to this query tree.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expression</code> is <code>null</code>.
     * The order of adding ascending and descending ordering expressions defines
     * the order of instances in the result collection of an executed query
     * instance corresponding with this query tree.
     * @param expression the order expression
     * @exception NullPointerException if expression is null
     */
    public void addAscendingOrdering(Expression expression)
    {   if( expression==null )
            throw new NullPointerException();
        this.orderings.add( new AscendingOrderingExpr(expression) );
    }

    /**
     * Adds an descending ordering expression to this query tree.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expression</code> is <code>null</code>.
     * The order of adding ascending and descending ordering expressions defines
     * the order of instances in the result collection of an executed query
     * instance corresponding with this query tree.
     * @param expression the order expression
     * @exception NullPointerException if expression is null
     */
    public void addDescendingOrdering(Expression expression)
    {   if( expression==null )
            throw new NullPointerException();
        this.orderings.add( new DescendingOrderingExpr(expression) );
    }

    /**
     * Sets the filter expression for this query tree.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>filter</code> is <code>null</code>.
     * @param filter the filter expression
     * @exception NullPointerException if filter is null
     * @exception JDOQueryException if the result type of filter is a non boolean type
     */
    public void setFilter(Expression filter)
    {   if( filter==null )
            throw new NullPointerException();
        if( filter.getJavaClass()!=Boolean.class &&
            filter.getJavaClass()!=boolean.class )
            throw new JDOQueryException( msg.msg("EXC_IllegalTypeForFilterExpression", filter) ); //NOI18N
        this.filter = filter;
    }

    /**
     * Returns the candidate class.
     * @return the candidate class
     */
    public Class getCandidateClass()
    {   if( this.candidateClass==null )
            throw new JDOQueryException( msg.msg("EXC_MissingCandidateClass") ); //NOI18N
        return this.candidateClass.getJavaClass();
    }

    /**
     * Returns a map containing all declared variables.
     * This map contains variable names as keys and instances of
     * <code>VariableDeclaration</code> as values.
     * @return the map of declared variables
     */
    public Map getDeclaredVariables()
    {   return this.variableMap;
    }

    /**
     * Returns a map containing all declared parameters.
     * This map contains parameter names as keys and instances of
     * <code>ParameterDeclaration</code> as values.
     * @return the map of declared parameters
     */
    public Map getDeclaredParameters()
    {   return this.parameterMap;
    }

    /**
     * Returns a list of all declared parameters. The order of entries is
     * defined by the order of calls <code>declareParameter</code>.
     * This list contains instances of
     * <code>ParametersDeclaration</code> as entries.
     * @return the list of declared parameters
     */
    public List getDeclaredParametersAsList()
    {   return this.parameterList;
    }

    /**
     * Returns the filter expression of this query tree.
     * @return the filter or null.
     */
    public Expression getFilter()
    {   return (Expression) this.filter;
    }

    /**
     * Returns a list of all added ordering expressions.
     * The order of entries is defined by the order of calls
     * <code>addAscendingOrdering</code> and <code>addDescendingOrdering</code>.
     * This list contains instances of
     * <code>OrderingExpression</code> as entries.
     * @return the list of declared parameters
     */
    public List getOrderingExpressions()
    {   return this.orderings;
    }

    /**
     * Returns an instance of either <code>ThisExpression</code> or
     * <code>VariableAccessExpression</code> or
     * <code>ParameterAccessExpression</code> or <code>FieldAccessExpression</code>
     * depending on the fact which of the classes the argument
     * <code>identifier</code> maps to.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>identifier</code> is <code>null</code> or the candidate class is not set.
     * Note: If you pass <code>"this"</code> as an identifier name, then
     * an instance of <code>ThisExpression</code> is returned. If you pass any
     * other java key word as an identifier name, then an instance of
     * <code>JDOQueryException</code> is thrown.
     * @param identifier the name of the identifier access expression
     * @return the specialized identifier access expression instance
     * @exception NullPointerException if identifier is null
     * @exception JDOQueryException if identifier is a java key word.
     */
    public IdentifierExpression newIdentifier(String identifier)
    {   if( identifier==null )
            throw new NullPointerException();
        IdentifierExpression identifierExpr;
        if( identifier.equals("this") ) //NOI18N
            identifierExpr = new ThisExpr( this.candidateClass==null?null:this.candidateClass.getJavaClass() );
        else
        {   if( javaKeyWords.isJavaKeyWord(identifier) )
                throw new JDOQueryException( msg.msg("EXC_IllegalIdentifier", identifier) ); //NOI18N
            Declaration decl = (Declaration) this.variableMap.get( identifier );
            if( decl==null )
            {   decl = (Declaration) this.parameterMap.get( identifier );
                if( decl==null )
                    //identifierExpr = new FieldAccessExpr( new ThisExpr(this.candidateClass.getJavaClass()), identifier );
                    identifierExpr = new IdentifierExpr( JDOQLTokenTypes.IDENT, identifier, null );
                else
                    identifierExpr = new ParameterAccessExpr( decl.getJavaClass(), identifier );
            }
            else
                identifierExpr = new VariableAccessExpr( decl.getJavaClass(), identifier );
        }
        return identifierExpr;
    }

    /**
     * Returns an instance of <code>FieldAccessExpression</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>target</code> or <code>fieldName</code> are <code>null</code>.
     * @param target the target expression of the field access expression
     * @param fieldName the name of the field to access
     * @return the field access expression instance
     * @exception NullPointerException if target or fieldName are null
     */
    public FieldAccessExpression newFieldAccess(Expression target, String fieldName)
    {   if( target==null ||
            fieldName==null )
            throw new NullPointerException();
        return new FieldAccessExpr( target, fieldName );
    }

    /**
     * Returns an instance of <code>StaticFieldAccessExpression</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>clazz</code> or <code>fieldName</code> are <code>null</code>.
     * @param clazz the class instance defining the field
     * @param fieldName the name of the field to access
     * @return the static field access expression instance
     * @exception NullPointerException if clazz or fieldName are null
     */
    public StaticFieldAccessExpression newFieldAccess(Class clazz, String fieldName)
    {   if( clazz==null ||
            fieldName==null )
            throw new NullPointerException();
        return new StaticFieldAccessExpr( new TypeImpl(clazz), fieldName );
    }

    /**
     * Returns an instance of <code>MethodCallExpression</code>.
     * Note: If the method corresponding with methodName does not have any
     * arguments, then the argument <code>arguments</code> is ignored.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>target</code> or <code>methodName</code>
     * are <code>null</code>.
     * @param target the target expression of the method call expression
     * @param methodName the name of the method
     * @param arguments the array of arguments
     * @return the specialized method call expression
     * @exception NullPointerException if target or methodName are null
     * @exception JDOQueryException if methodName is not one of
     * <code>"contains"</code>, <code>"endsWith"</code>, <code>"isEmpty"</code>
     * or <code>"startsWith"</code>.
     */
    public MethodCallExpression newMethodCall(Expression target, String methodName, Expression[] arguments)
    {   if( target==null ||
            methodName==null )
            throw new NullPointerException();
        MethodCallExpression methodCall;
        if( methodName.equals("contains") ) //NOI18N
            methodCall = new ContainsCallExpr( target, arguments );
        else if( methodName.equals("endsWith") ) //NOI18N
            methodCall = new EndsWithCallExpr( target, arguments );
        else if( methodName.equals("isEmpty") ) //NOI18N
            methodCall = new IsEmptyCallExpr( target );
        else if( methodName.equals("startsWith") ) //NOI18N
            methodCall = new StartsWithCallExpr( target, arguments );
        else
            throw new JDOQueryException( msg.msg("EXC_NonSupportedMethodCall", methodName) ); //NOI18N
        return methodCall;
    }

    /**
     * Returns an instance of <code>CastExpression</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>clazz</code> or <code>expression</code>
     * are <code>null</code>.
     * @param clazz the Java class to cast the argument <code>expression</code> to
     * @param expression the expression to cast
     * @return the cast expression instance
     * @exception NullPointerException if expression is null
     */
    public CastExpression newCast(Class clazz, Expression expression)
    {   if( clazz==null ||
            expression==null )
            throw new NullPointerException();
        return new CastExpr( new TypeImpl(clazz), expression );
    }

    /**
     * Returns an instance of <code>ConstantExpression</code>.
     * This method handles <code>null</code> as a constant expression.
     * @param value the object wrapped by the constant expression
     * @return the constant expression instance
     */
    public ConstantExpression newConstant(Object value)
    {   return ConstantExpr.newConstant( value );
    }

    /**
     * Returns an instance of <code>BooleanLiteralExpression</code>.
     * @param b the value wrapped by the boolean expression
     * @return the boolean expression instance
     */
    public ConstantExpression newConstant(boolean b)
    {   return new BooleanLiteralExpr( b );
    }

    /**
     * Returns an instance of <code>ByteLiteralExpression</code>.
     * @param b the value wrapped by the byte expression
     * @return the byte expression instance
     */
    public ConstantExpression newConstant(byte b)
    {   return new ByteLiteralExpr( b );
    }

    /**
     * Returns an instance of <code>CharLiteralExpression</code>.
     * @param c the value wrapped by the char expression
     * @return the char expression instance
     */
    public ConstantExpression newConstant(char c)
    {   return new CharLiteralExpr( c );
    }

    /**
     * Returns an instance of <code>DoubleLiteralExpression</code>.
     * @param d the value wrapped by the double expression
     * @return the double expression instance
     */
    public ConstantExpression newConstant(double d)
    {   return new DoubleLiteralExpr( d );
    }

    /**
     * Returns an instance of <code>FloatLiteralExpression</code>.
     * @param f the value wrapped by the float expression
     * @return the float expression instance
     */
    public ConstantExpression newConstant(float f)
    {   return new FloatLiteralExpr( f );
    }

    /**
     * Returns an instance of <code>IntLiteralExpression</code>.
     * @param i the value wrapped by the int expression
     * @return the int expression instance
     */
    public ConstantExpression newConstant(int i)
    {   return new IntLiteralExpr( i );
    }

    /**
     * Returns an instance of <code>LongLiteralExpression</code>.
     * @param l the value wrapped by the long expression
     * @return the long expression instance
     */
    public ConstantExpression newConstant(long l)
    {   return new LongLiteralExpr( l );
    }

    /**
     * Returns an instance of <code>ShortLiteralExpression</code>.
     * @param s the value wrapped by the short expression
     * @return the short expression instance
     */
    public ConstantExpression newConstant(short s)
    {   return new ShortLiteralExpr( s );
    }

    /**
     * Returns a complement expression for the argument
     * <code>expr</code>.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expr</code> is <code>null</code>.
     * @param expr the expression argument for the operation
     * @return the complement expression instance
     * @exception NullPointerException if expr is null
     */
    public ComplementExpression newComplement(Expression expr)
    {   if( expr==null )
            throw new NullPointerException();
        return new ComplementExpr( expr );
    }

    /**
     * Returns a unary minus expression for the argument
     * <code>expr</code>.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expr</code> is <code>null</code>.
     * @param expr the expression argument for the operation
     * @return the unary minus expression instance
     * @exception NullPointerException if expr is null
     */
    public UnaryMinusExpression newMinus(Expression expr)
    {   if( expr==null )
            throw new NullPointerException();
        return new UnaryMinusExpr( expr );
    }

    /**
     * Returns a not expression for the argument
     * <code>expr</code>.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expr</code> is <code>null</code>.
     * @param expr the expression argument for the operation
     * @return the not expression instance
     * @exception NullPointerException if expr is null
     */
    public NotExpression newNot(Expression expr)
    {   if( expr==null )
            throw new NullPointerException();
        return new NotExpr( expr );
    }

    /**
     * Returns a plus expression for the argument
     * <code>expr</code>.
     * This method throws <code>NullPointerException</code> if the argument
     * <code>expr</code> is <code>null</code>.
     * @param expr the expression argument for the operation
     * @return the plus expression instance
     * @exception NullPointerException if expr is null
     */
    public UnaryPlusExpression newPlus(Expression expr)
    {   if( expr==null )
            throw new NullPointerException();
        return new UnaryPlusExpr( expr );
    }

    /**
     * Returns an and expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the and expression instance
     * @exception NullPointerException if left or right are null
     */
    public AndExpression newAnd(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new AndExpr( left, right );
    }

    /**
     * Returns a conditional and expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the conditional and expression instance
     * @exception NullPointerException if left or right are null
     */
    public ConditionalAndExpression newConditionalAnd(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new ConditionalAndExpr( left, right );
    }

    /**
     * Returns a conditional or expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the conditional or expression instance
     * @exception NullPointerException if left or right are null
     */
    public ConditionalOrExpression newConditionalOr(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new ConditionalOrExpr( left, right );
    }

    /**
     * Returns a divide expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the divide expression instance
     * @exception NullPointerException if left or right are null
     */
    public DivideExpression newDivide(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new DivideExpr( left, right );
    }

    /**
     * Returns an equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the equals expression instance
     * @exception NullPointerException if left or right are null
     */
    public EqualsExpression newEquals(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new EqualsExpr( left, right );
    }

    /**
     * Returns a greater than expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the greater than expression instance
     * @exception NullPointerException if left or right are null
     */
    public GreaterThanExpression newGreaterThan(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new GreaterThanExpr( left, right );
    }

    /**
     * Returns a greater than equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the greater than equals expression instance
     * @exception NullPointerException if left or right are null
     */
    public GreaterThanEqualsExpression newGreaterThanEquals(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new GreaterThanEqualsExpr( left, right );
    }

    /**
     * Returns a less than expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the less than expression instance
     * @exception NullPointerException if left or right are null
     */
    public LessThanExpression newLessThan(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new LessThanExpr( left, right );
    }

    /**
     * Returns a less than equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the less than equals expression instance
     * @exception NullPointerException if left or right are null
     */
    public LessThanEqualsExpression newLessThanEquals(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new LessThanEqualsExpr( left, right );
    }

    /**
     * Returns a minus expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the minus expression instance
     * @exception NullPointerException if left or right are null
     */
    public MinusExpression newMinus(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new MinusExpr( left, right );
    }

    /**
     * Returns a not equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the not equals expression instance
     * @exception NullPointerException if left or right are null
     */
    public NotEqualsExpression newNotEquals(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new NotEqualsExpr( left, right );
    }

    /**
     * Returns a plus expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the plus expression instance
     * @exception NullPointerException if left or right are null
     */
    public PlusExpression newPlus(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new PlusExpr( left, right );
    }

    /**
     * Returns an or expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the or expression instance
     * @exception NullPointerException if left or right are null
     */
    public OrExpression newOr(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new OrExpr( left, right );
    }

    /**
     * Returns a times expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of the arguments
     * <code>left</code> or <code>right</code> are <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the times expression instance
     * @exception NullPointerException if left or right are null
     */
    public TimesExpression newTimes(Expression left, Expression right)
    {   if( left==null ||
            right==null )
            throw new NullPointerException();
        return new TimesExpr( left, right );
    }

    /**
     * Returns the children of this node.
     * This method ensures that the children array corresponds with
     * the children nodes of the underlying ANTLR tree structure.
     * @return the children
     * @exception JDOQueryException if the candidate class is not set
     */
    public Node[] getChildren()
    {   initANTLRAST();
        return super.getChildren();
    }

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     */
    public void arrive(NodeVisitor visitor)
    {   visitor.arrive( this );
    }

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     * @param results the result array
     * @return the object returned by the visitor instance
     */
    public Object leave(NodeVisitor visitor, Object[] results)
    {   return visitor.leave( this, results );
    }

    /**
     * Delegates to the argument <code>visitor</code>.
     * @param visitor the node visitor
     * @param resultOfPreviousChild the result computed by leaving the 
     * previous child node.
     * @param indexOfNextChild the index of the next child node
     * @return the boolean value returned by the visitor instance
     */
    public boolean walkNextChild(NodeVisitor visitor, Object resultOfPreviousChild, int indexOfNextChild)
    {   return visitor.walkNextChild( this, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * Ensures that this node has a corresponding ANTLR tree structure.
     * @exception JDOQueryException if the candidate class is not set
     */
    public void initANTLRAST()
    {   if( getFirstChild()==null )
        {   if( this.candidateClass==null )
                throw new JDOQueryException( msg.msg("EXC_MissingCandidateClass") ); //NOI18N

            setFirstChild( (AST) this.candidateClass );
            AST current = (AST) candidateClass;
            for( Iterator i=this.parameterList.iterator(); i.hasNext(); )
            {   current.setNextSibling( (AST) i.next() );
                current = current.getNextSibling();
            }
            for( Iterator i=this.variableMap.values().iterator(); i.hasNext(); )
            {   current.setNextSibling( (AST) i.next() );
                current = current.getNextSibling();
            }
            for( Iterator i=this.orderings.iterator(); i.hasNext(); )
            {   current.setNextSibling( (AST) i.next() );
                current = current.getNextSibling();
            }
            current.setNextSibling( (AST) this.filter );
        }
    }

    /**
     * Nullifies the declared fields in this node as well as the underlying
     * ANTLR tree structure.
     */
    private void reset()
    {   this.candidateClass = null;
        this.parameterMap.clear();
        this.parameterList.clear();
        this.variableMap.clear();
        this.orderings.clear();
        this.filter = null;
        this.children = null;

        this.parent = null;
        setFirstChild( null );
        setChildren( null );
    }

    private void init()
    {   this.candidateClass = null;
        this.parameterMap = new HashMap();
        this.parameterList = new LinkedList();
        this.variableMap = new HashMap();
        this.orderings = new LinkedList();
        this.filter = null;
    }

    static Class toWrapperClass(Class clazz)
    {   if( clazz==boolean.class )
            clazz = Boolean.class;
        if( clazz==byte.class )
            clazz = Byte.class;
        if( clazz==char.class )
            clazz = Character.class;
        if( clazz==double.class )
            clazz = Double.class;
        if( clazz==float.class )
            clazz = Float.class;
        if( clazz==int.class )
            clazz = Integer.class;
        if( clazz==long.class )
            clazz = Long.class;
        if( clazz==short.class )
            clazz = Short.class;
        return clazz;
    }

    static class JavaKeyWords
    {   Collection javaKeyWords = new HashSet();
        JavaKeyWords()
        {   javaKeyWords.add( "abstract" ); //NOI18N
            javaKeyWords.add( "boolean" ); //NOI18N
            javaKeyWords.add( "break" ); //NOI18N
            javaKeyWords.add( "byte" ); //NOI18N
            javaKeyWords.add( "case" ); //NOI18N
            javaKeyWords.add( "catch" ); //NOI18N
            javaKeyWords.add( "char" ); //NOI18N
            javaKeyWords.add( "class" ); //NOI18N
            javaKeyWords.add( "const" ); //NOI18N
            javaKeyWords.add( "continue" ); //NOI18N
            javaKeyWords.add( "default" ); //NOI18N
            javaKeyWords.add( "do" ); //NOI18N
            javaKeyWords.add( "double" ); //NOI18N
            javaKeyWords.add( "else" ); //NOI18N
            javaKeyWords.add( "extends" ); //NOI18N
            javaKeyWords.add( "final" ); //NOI18N
            javaKeyWords.add( "finally" ); //NOI18N
            javaKeyWords.add( "float" ); //NOI18N
            javaKeyWords.add( "for" ); //NOI18N
            javaKeyWords.add( "goto" ); //NOI18N
            javaKeyWords.add( "if" ); //NOI18N
            javaKeyWords.add( "implements" ); //NOI18N
            javaKeyWords.add( "import" ); //NOI18N
            javaKeyWords.add( "instanceof" ); //NOI18N
            javaKeyWords.add( "int" ); //NOI18N
            javaKeyWords.add( "interface" ); //NOI18N
            javaKeyWords.add( "long" ); //NOI18N
            javaKeyWords.add( "native" ); //NOI18N
            javaKeyWords.add( "new" ); //NOI18N
            javaKeyWords.add( "package" ); //NOI18N
            javaKeyWords.add( "private" ); //NOI18N
            javaKeyWords.add( "protected" ); //NOI18N
            javaKeyWords.add( "public" ); //NOI18N
            javaKeyWords.add( "return" ); //NOI18N
            javaKeyWords.add( "short" ); //NOI18N
            javaKeyWords.add( "static" ); //NOI18N
            javaKeyWords.add( "strictfp" ); //NOI18N
            javaKeyWords.add( "super" ); //NOI18N
            javaKeyWords.add( "switch" ); //NOI18N
            javaKeyWords.add( "synchronized" ); //NOI18N
            javaKeyWords.add( "this" ); //NOI18N
            javaKeyWords.add( "throw" ); //NOI18N
            javaKeyWords.add( "throws" ); //NOI18N
            javaKeyWords.add( "transient" ); //NOI18N
            javaKeyWords.add( "try" ); //NOI18N
            javaKeyWords.add( "void" ); //NOI18N
            javaKeyWords.add( "volatile" ); //NOI18N
            javaKeyWords.add( "while" ); //NOI18N
        }
        /**
         * Checks if the argument <code>identifier</code> equals a Java key word.
         * @param identifier the identifier to check
         * @return <code>true</code>, if <code>identifier</code> is a Java key word,
         * else <code>false</code>
         */
        private boolean isJavaKeyWord(String identifier)
        {   return javaKeyWords.contains(identifier);
        }
    }

    // Serialization support

    /** 
     * Returns the candidate class name calculated during serialization of
     * this query tree instance. 
     * @return serialized candidate class name
     */
    public String getSerializedCandidateClassName()
    {   
        return serializedCandidateClassName;
    }

    /** Deserialization support. */
    private void writeObject(java.io.ObjectOutputStream out)
        throws java.io.IOException
    {
        if ((candidateClass != null) && (getCandidateClass() != null))
            this.serializedCandidateClassName = getCandidateClass().getName();
        out.defaultWriteObject();
    }
}

