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

package org.apache.jdo.jdoql.tree;

/**
 * This interface provides methods for factorizing expression nodes.
 * Expression nodes are used as filter expressions for query trees.
 *
 * @author Michael Watzek
 */
public interface ExpressionFactory
{
    /**
     * The implementation may decide to create an instance
     * of <code>FieldAccessExpression</code>,
     * <code>VariableAccessExpression</code> or
     * <code>ParameterAccessExpression</code> depending on the fact
     * whether the argument corresponds with a parameter, variable or
     * a field of the candidate class. Optionally, the implementation
     * may return an instance of <code>IdentifierExpression</code>
     * which later may be replaced with its specialized counterpart
     * semantically analysing a query tree.
     * @param identifier the name of the identifier access expression
     * @return the identifier access expression instance
     */
    public IdentifierExpression newIdentifier(String identifier);

    /**
     * Returns an instance of <code>FieldAccessExpression</code>.
     * @param target the target expression of the field access expression
     * @param fieldName the name of the field to access
     * @return the field access expression instance
     */
    public FieldAccessExpression newFieldAccess(Expression target,
                                                String fieldName);

    /**
     * Returns an instance of <code>StaticFieldAccessExpression</code>.
     * @param clazz the class instance defining the field
     * @param fieldName the name of the field to access
     * @return the static field access expression instance
     */
    public StaticFieldAccessExpression newFieldAccess(Class clazz,
                                                      String fieldName);

    /**
     * The implementation may decide to create a specialized instance
     * of <code>MethodCallExpression</code> (for example,
     * <code>ContainsCallExpression</code>) 
     * depending on the argument <code>methodName</code>.
     * Optionally, the implementation may return an instance of
     * <code>MethodCallExpression</code>
     * which later may be replaced with its specialized counterpart
     * semantically analysing a query tree.
     * @param target the target expression of the method call expression
     * @param methodName the name of the method
     * @param arguments the array of arguments
     * @return the specialized method call expression instance
     */
    public MethodCallExpression newMethodCall(
        Expression target, String methodName, Expression[] arguments);

    /**
     * Returns an instance of <code>CastExpression</code>.
     * @param clazz the Java class to cast the argument
     * <code>expression</code> to
     * @param expression the expression to cast
     * @return the cast expression instance
     */
    public CastExpression newCast(Class clazz, Expression expression);

    //////////////////////////////////////////////////////////
    //methods for factorizing specialized constant expressions
    //////////////////////////////////////////////////////////

    /**
     * Returns an instance of <code>BooleanLiteralExpression</code>.
     * @param b the value wrapped by the boolean expression
     * @return the boolean expression instance
     */
    public ConstantExpression newConstant(boolean b);

    /**
     * Returns an instance of <code>ByteLiteralExpression</code>.
     * @param b the value wrapped by the byte expression
     * @return the byte expression instance
     */
    public ConstantExpression newConstant(byte b);

    /**
     * Returns an instance of <code>CharLiteralExpression</code>.
     * @param c the value wrapped by the char expression
     * @return the char expression instance
     */
    public ConstantExpression newConstant(char c);

    /**
     * Returns an instance of <code>DoubleLiteralExpression</code>.
     * @param d the value wrapped by the double expression
     * @return the double expression instance
     */
    public ConstantExpression newConstant(double d);

    /**
     * Returns an instance of <code>FloatLiteralExpression</code>.
     * @param f the value wrapped by the float expression
     * @return the float expression instance
     */
    public ConstantExpression newConstant(float f);

    /**
     * Returns an instance of <code>IntLiteralExpression</code>.
     * @param i the value wrapped by the int expression
     * @return the int expression instance
     */
    public ConstantExpression newConstant(int i);

    /**
     * Returns an instance of <code>LongLiteralExpression</code>.
     * @param l the value wrapped by the long expression
     * @return the long expression instance
     */
    public ConstantExpression newConstant(long l);

    /**
     * Returns an instance of <code>ShortLiteralExpression</code>.
     * @param s the value wrapped by the short expression
     * @return the short expression instance
     */
    public ConstantExpression newConstant(short s);

    /**
     * Returns an instance of <code>ConstantExpression</code>.
     * This method handles <code>null</code> as a constant expression.
     * @param value the object wrapped by the constant expression
     * @return the constant expression instance
     */
    public ConstantExpression newConstant(Object value);

    ///////////////////////////////////////////////////////
    //methods for factorizing specialized unary expressions
    ///////////////////////////////////////////////////////

    /**
     * Returns a complement expression for the argument
     * <code>expr</code>.
     * @param expr the expression argument for the operation
     * @return the complement expression instance
     */
    public ComplementExpression newComplement(Expression expr);

    /**
     * Returns a unary minus expression for the argument
     * <code>expr</code>.
     * @param expr the expression argument for the operation
     * @return the unary minus expression instance
     */
    public UnaryMinusExpression newMinus(Expression expr);

    /**
     * Returns a not expression for the argument
     * <code>expr</code>.
     * @param expr the expression argument for the operation
     * @return the not expression instance
     */
    public NotExpression newNot(Expression expr);

    /**
     * Returns a plus expression for the argument
     * <code>expr</code>.
     * @param expr the expression argument for the operation
     * @return the plus expression instance
     */
    public UnaryPlusExpression newPlus(Expression expr);

    ////////////////////////////////////////////////////////
    //methods for factorizing specialized binary expressions
    ////////////////////////////////////////////////////////

    /**
     * Returns an and expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the and expression instance
     */
    public AndExpression newAnd(Expression left, Expression right);

    /**
     * Returns a conditional and expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the conditional and expression instance
     */
    public ConditionalAndExpression newConditionalAnd(Expression left,
                                                      Expression right);

    /**
     * Returns a conditional or expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the conditional or expression instance
     */
    public ConditionalOrExpression newConditionalOr(Expression left,
                                                    Expression right);

    /**
     * Returns a divide expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of
     * the arguments <code>left</code> or <code>right</code> are
     * <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the divide expression instance
     */
    public DivideExpression newDivide(Expression left, Expression right);

    /**
     * Returns an equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the equals expression instance
     */
    public EqualsExpression newEquals(Expression left, Expression right);

    /**
     * Returns a greater than expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the greater than expression instance
     */
    public GreaterThanExpression newGreaterThan(Expression left,
                                                Expression right);

    /**
     * Returns a greater than equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the greater than equals expression instance
     */
    public GreaterThanEqualsExpression newGreaterThanEquals(Expression left,
                                                            Expression right);

    /**
     * Returns a less than expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the less than expression instance
     */
    public LessThanExpression newLessThan(Expression left, Expression right);

    /**
     * Returns a less than equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the less than equals expression instance
     */
    public LessThanEqualsExpression newLessThanEquals(Expression left,
                                                      Expression right);

    /**
     * Returns a minus expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the minus expression instance
     */
    public MinusExpression newMinus(Expression left, Expression right);

    /**
     * Returns a not equals expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the not equals expression instance
     */
    public NotEqualsExpression newNotEquals(Expression left, Expression right);

    /**
     * Returns a plus expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the plus expression instance
     */
    public PlusExpression newPlus(Expression left, Expression right);

    /**
     * Returns an or expression for the arguments
     * <code>left</code> and <code>right</code>.
     * This method throws <code>NullPointerException</code> if one of
     * the arguments <code>left</code> or <code>right</code> are
     * <code>null</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the or expression instance
     */
    public OrExpression newOr(Expression left, Expression right);

    /**
     * Returns a times expression for the arguments
     * <code>left</code> and <code>right</code>.
     * @param left the left expression argument for the operation
     * @param right the right expression argument for the operation
     * @return the times expression instance
     */
    public TimesExpression newTimes(Expression left, Expression right);
}
