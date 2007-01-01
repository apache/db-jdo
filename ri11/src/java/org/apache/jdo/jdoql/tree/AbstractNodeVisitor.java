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

package org.apache.jdo.jdoql.tree;

/**
 * This class provides a default implementation for the node visitor pattern.
 * Methods having derived nodes as arguments delegate to those methods
 * having least derived nodes as arguments. As some visitors do not need
 * to know each specific operations,
 * they do not need to provide code for those operations.
 * In addition to methods defined in the interface <code>NodeVisitor</code>
 * this class defines protected methods taking non-leaf nodes in terms of the
 * node hierarchy of interfaces.
 *
 * @author Michael Watzek
 */
public abstract class AbstractNodeVisitor implements NodeVisitor
{
    //protected methods

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    protected void arrive(BinaryExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    protected void arrive(Declaration node)
    {   arrive( (Node) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    protected void arrive(Expression node)
    {   arrive( (Node) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    protected void arrive(MethodCallExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    protected void arrive(OrderingExpression node)
    {   arrive( (Node) node );
    }

    /**
     * This method defines the default implementation for all
     * <code>arrive</code> methods: It immediately returns without executing
     * any other instruction.
     * @param node the node to be walked
     */
    protected void arrive(Node node)
    {   return;
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    protected void arrive(UnaryExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(BinaryExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(Declaration node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(Expression node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(MethodCallExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(OrderingExpression node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method defines the default implementation for all
     * <code>leave</code> methods: It immediately returns <code>null</code>
     * without executing any other instruction.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return <code>null</code>
     */
    protected Object leave(Node node, Object[] results)
    {   return null;
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    protected Object leave(UnaryExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }


    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(BinaryExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Expression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(Expression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Node) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(IdentifierExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Expression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(MethodCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Expression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method defines the default implementation for all
     * <code>walkNextChild</code> methods: It immediately returns <code>true</code>
     * without executing any other instruction.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>true</code>
     */
    protected boolean walkNextChild(Node node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return true;
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(OrderingExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Node) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    protected boolean walkNextChild(UnaryExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Expression) node, resultOfPreviousChild, indexOfNextChild );
    }

    //public methods

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(AndExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(AscendingOrderingExpression node)
    {   arrive( (OrderingExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(BooleanLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ByteLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    public void arrive(CandidateClass node)
    {   arrive( (Node) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    public void arrive(CastExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(CharLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ComplementExpression node)
    {   arrive( (UnaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ConditionalAndExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ConditionalOrExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    public void arrive(ConstantExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ContainsCallExpression node)
    {   arrive( (MethodCallExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(DescendingOrderingExpression node)
    {   arrive( (OrderingExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(DivideExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(DoubleLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(EqualsExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(EndsWithCallExpression node)
    {   arrive( (MethodCallExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(FieldAccessExpression node)
    {   arrive( (IdentifierExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(FloatLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(GreaterThanEqualsExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(GreaterThanExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Expression</code>.
     * @param node the node to be walked
     */
    public void arrive(IdentifierExpression node)
    {   arrive( (Expression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(IntLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(IsEmptyCallExpression node)
    {   arrive( (MethodCallExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(LessThanEqualsExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(LessThanExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(LongLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(MinusExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(NotEqualsExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(NotExpression node)
    {   arrive( (UnaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(OrExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ParameterAccessExpression node)
    {   arrive( (IdentifierExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Declaration</code>.
     * @param node the node to be walked
     */
    public void arrive(ParameterDeclaration node)
    {   arrive( (Declaration) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(PlusExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    public void arrive(QueryTree node)
    {   arrive( (Node) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ShortLiteralExpression node)
    {   arrive( (ConstantExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(StartsWithCallExpression node)
    {   arrive( (MethodCallExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>FieldAccessExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(StaticFieldAccessExpression node)
    {   arrive( (FieldAccessExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(ThisExpression node)
    {   arrive( (IdentifierExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(TimesExpression node)
    {   arrive( (BinaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Node</code>.
     * @param node the node to be walked
     */
    public void arrive(Type node)
    {   arrive( (Node) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(UnaryMinusExpression node)
    {   arrive( (UnaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(UnaryPlusExpression node)
    {   arrive( (UnaryExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>.
     * @param node the node to be walked
     */
    public void arrive(VariableAccessExpression node)
    {   arrive( (IdentifierExpression) node );
    }

    /**
     * This method delegates to <code>arrive</code> casting the argument
     * <code>node</code> to <code>Declaration</code>.
     * @param node the node to be walked
     */
    public void arrive(VariableDeclaration node)
    {   arrive( (Declaration) node );
    }


    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(AndExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(AscendingOrderingExpression node, Object[] results)
    {   return leave( (OrderingExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(BooleanLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ByteLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(CandidateClass node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(CastExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(CharLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ComplementExpression node, Object[] results)
    {   return leave( (UnaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ConditionalAndExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ConditionalOrExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ConstantExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ContainsCallExpression node, Object[] results)
    {   return leave( (MethodCallExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(DescendingOrderingExpression node, Object[] results)
    {   return leave( (OrderingExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(DivideExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(DoubleLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(EndsWithCallExpression node, Object[] results)
    {   return leave( (MethodCallExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(EqualsExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(FieldAccessExpression node, Object[] results)
    {   return leave( (IdentifierExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(FloatLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(GreaterThanEqualsExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(GreaterThanExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(IdentifierExpression node, Object[] results)
    {   return leave( (Expression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(IntLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(IsEmptyCallExpression node, Object[] results)
    {   return leave( (MethodCallExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(LessThanEqualsExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(LessThanExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(LongLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(MinusExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(NotEqualsExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(NotExpression node, Object[] results)
    {   return leave( (UnaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(OrExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ParameterAccessExpression node, Object[] results)
    {   return leave( (IdentifierExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Declaration</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ParameterDeclaration node, Object[] results)
    {   return leave( (Declaration) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(PlusExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(QueryTree node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>ConstantExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ShortLiteralExpression node, Object[] results)
    {   return leave( (ConstantExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(StartsWithCallExpression node, Object[] results)
    {   return leave( (MethodCallExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>FieldAccessExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(StaticFieldAccessExpression node, Object[] results)
    {   return leave( (FieldAccessExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(ThisExpression node, Object[] results)
    {   return leave( (IdentifierExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(TimesExpression node, Object[] results)
    {   return leave( (BinaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(Type node, Object[] results)
    {   return leave( (Node) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(UnaryMinusExpression node, Object[] results)
    {   return leave( (UnaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(UnaryPlusExpression node, Object[] results)
    {   return leave( (UnaryExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(VariableAccessExpression node, Object[] results)
    {   return leave( (IdentifierExpression) node, results );
    }

    /**
     * This method delegates to <code>leave</code> casting the argument
     * <code>node</code> to <code>Declaration</code>. It returns the value
     * of that method.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of the delegation call
     */
    public Object leave(VariableDeclaration node, Object[] results)
    {   return leave( (Declaration) node, results );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(AndExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(AscendingOrderingExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (OrderingExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Expression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(CastExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Expression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(ComplementExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (UnaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(ConditionalAndExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(ConditionalOrExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(ContainsCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (MethodCallExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>OrderingExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(DescendingOrderingExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (OrderingExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(DivideExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(EndsWithCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (MethodCallExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(EqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>IdentifierExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(FieldAccessExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (IdentifierExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(GreaterThanEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(GreaterThanExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(IsEmptyCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (MethodCallExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(LessThanEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(LessThanExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(MinusExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(NotEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(NotExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (UnaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(OrExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(PlusExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>Node</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(QueryTree node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (Node) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>MethodCallExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(StartsWithCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (MethodCallExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>FieldAccessExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(StaticFieldAccessExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (FieldAccessExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>BinaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(TimesExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (BinaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(UnaryMinusExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (UnaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }

    /**
     * This method delegates to <code>walkNextChild</code> casting the argument
     * <code>node</code> to <code>UnaryExpression</code>. It returns the value
     * of that method.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return the result of the delegation call
     */
    public boolean walkNextChild(UnaryPlusExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return walkNextChild( (UnaryExpression) node, resultOfPreviousChild, indexOfNextChild );
    }
}
