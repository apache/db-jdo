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
 * This interface defines three categories of methods for each query tree node.
 * These methods are called during walking the tree:
 * <code>arrive</code>: This method is meant for initialization purposes
 * and is called before walking a node's children.
 * <code>leave</code>: This method is meant for calculation purposes
 * and is called after walking a node's children.
 * <code>walkNextChild</code>: This method is called before walking
 * each child of a node. It can be used to control whether
 * the a node's remaining children should be walked.
 *
 * @author Michael Watzek
 */
public interface NodeVisitor
{
    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(AndExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(AscendingOrderingExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(BooleanLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ByteLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(CandidateClass node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(CastExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(CharLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ComplementExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ConditionalAndExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ConditionalOrExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ConstantExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ContainsCallExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(DescendingOrderingExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(DivideExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(DoubleLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(EndsWithCallExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(EqualsExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(FieldAccessExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(StaticFieldAccessExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(FloatLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(GreaterThanEqualsExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(GreaterThanExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(IdentifierExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(IntLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(IsEmptyCallExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(LessThanEqualsExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(LessThanExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(LongLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(MinusExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(NotEqualsExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(NotExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(OrExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ParameterAccessExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ParameterDeclaration node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(PlusExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(QueryTree node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ShortLiteralExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(StartsWithCallExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(ThisExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(TimesExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(Type node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(UnaryMinusExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(UnaryPlusExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(VariableAccessExpression node);

    /**
     * This method is called before walking any children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to perform any initialization tasks it needs for walking the node's
     * children.
     * @param node the node to be walked
     */
    public void arrive(VariableDeclaration node);


    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(AndExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(AscendingOrderingExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(BooleanLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ByteLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(CandidateClass node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(CastExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(CharLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ComplementExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ConditionalAndExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ConditionalOrExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ConstantExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ContainsCallExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(DescendingOrderingExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(DivideExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(DoubleLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(EqualsExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(EndsWithCallExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(FieldAccessExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(StaticFieldAccessExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(FloatLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(GreaterThanEqualsExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(GreaterThanExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(IdentifierExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(IntLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(IsEmptyCallExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(LessThanEqualsExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(LessThanExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(LongLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(MinusExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(NotEqualsExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(NotExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(OrExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ParameterAccessExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ParameterDeclaration node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(PlusExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(QueryTree node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ShortLiteralExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(StartsWithCallExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(ThisExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(TimesExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(Type node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(UnaryMinusExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(UnaryPlusExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(VariableAccessExpression node, Object[] results);

    /**
     * This method is called after walking the children of the argument
     * <code>node</code>. A node visitor instance uses this method
     * to compute the result of walking the argument <code>node</code>
     * and it's children. This result is returned by the tree walker's
     * <code>walk</code> method. The argument <code>results</code> holds
     * the results of walking the children of the argument <code>node</code>.
     * Usually, the result of the argument <code>node</code> is computed in
     * consideration of the results of its' children.
     * @param node the node having been walked
     * @param results the results of walking the node's children
     * @return the result of walking the node and it's children
     */
    public Object leave(VariableDeclaration node, Object[] results);


    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(AndExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(AscendingOrderingExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(CastExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(ComplementExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(ConditionalAndExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(ConditionalOrExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(ContainsCallExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(DescendingOrderingExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(DivideExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(EqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(EndsWithCallExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(FieldAccessExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(GreaterThanEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(GreaterThanExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(IsEmptyCallExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(LessThanEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(LessThanExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(MinusExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(NotEqualsExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(NotExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(OrExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(PlusExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(QueryTree node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(StartsWithCallExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(StaticFieldAccessExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(TimesExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(UnaryMinusExpression node, Object resultOfPreviousChild, int indexOfNextChild);

    /**
     * This method is called before walking each child of the argument
     * <code>node</code>. The return value of this method determines if
     * the next child of the argument <code>node</code> should be walked.
     * In case of returning <code>false</code>, none of the remaining
     * children are walked. Instead, the node's leave method is called
     * immediately. The argument <code>resultOfPreviousChild</code>
     * holds the result of walking the previous child of the argument
     * <code>node</code>. Usually, it is used to determine the return value
     * of this method. The argument <code>indexOfNextChild</code>
     * determines the index of the next child to be walked. This index
     * determines the position in the children array of the argument <code>node</code>.
     * Note: The index of the first child is 0.
     * @param node the parent node of the children currently walked
     * @param resultOfPreviousChild the result of walking the node's previous child
     * @param indexOfNextChild the index of the next child to be walked
     * @return <code>false</code>, if no more childs should be walked, else <code>true</code>
     */
    public boolean walkNextChild(UnaryPlusExpression node, Object resultOfPreviousChild, int indexOfNextChild);

}
