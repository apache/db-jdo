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

import java.io.PrintStream;

import org.apache.jdo.jdoql.tree.AbstractNodeVisitor;
import org.apache.jdo.jdoql.tree.AndExpression;
import org.apache.jdo.jdoql.tree.AscendingOrderingExpression;
import org.apache.jdo.jdoql.tree.BinaryExpression;
import org.apache.jdo.jdoql.tree.CastExpression;
import org.apache.jdo.jdoql.tree.ComplementExpression;
import org.apache.jdo.jdoql.tree.ConditionalAndExpression;
import org.apache.jdo.jdoql.tree.ConditionalOrExpression;
import org.apache.jdo.jdoql.tree.ConstantExpression;
import org.apache.jdo.jdoql.tree.DescendingOrderingExpression;
import org.apache.jdo.jdoql.tree.DivideExpression;
import org.apache.jdo.jdoql.tree.EqualsExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.FieldAccessExpression;
import org.apache.jdo.jdoql.tree.GreaterThanEqualsExpression;
import org.apache.jdo.jdoql.tree.GreaterThanExpression;
import org.apache.jdo.jdoql.tree.IdentifierExpression;
import org.apache.jdo.jdoql.tree.LessThanEqualsExpression;
import org.apache.jdo.jdoql.tree.LessThanExpression;
import org.apache.jdo.jdoql.tree.MethodCallExpression;
import org.apache.jdo.jdoql.tree.MinusExpression;
import org.apache.jdo.jdoql.tree.NotEqualsExpression;
import org.apache.jdo.jdoql.tree.NotExpression;
import org.apache.jdo.jdoql.tree.OrExpression;
import org.apache.jdo.jdoql.tree.OrderingExpression;
import org.apache.jdo.jdoql.tree.PlusExpression;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.jdoql.tree.TimesExpression;
import org.apache.jdo.jdoql.tree.UnaryMinusExpression;

/**
 * An instance of this class is used to print a query tree
 * into a print stream. The tree is printed as a string representation.
 * The filter of this representation can be used as argument for method
 * <code>Query.setFilter</code>.
 * As this class inherits from <code>AbstractNodeVisitor</code>, it only
 * overwrites those methods that need to dump and relies on the delegation
 * mechanism for the remaining methods implemented in
 * <code>AbstractNodeVisitor</code> .
 *
 * @author Michael Watzek
 */
public class FilterExpressionDumper extends AbstractNodeVisitor
{
    final PrintStream out;

    /**
     * Constructs an instance of this class. The print stream used to
     * dump the tree is <code>System.out</code>.
     */
    public FilterExpressionDumper()
    {   this.out = System.out;
    }

    /**
     * Constructs an instance of this class. The print stream used to
     * dump the tree is the argument <code>out</code>.
     * @param out the print stream to dump to
     */
    public FilterExpressionDumper(PrintStream out)
    {   this.out = out;
    }

    //protected methods

    /**
     * Binary expressions are dumped in parenthesized manner. Thus, this
     * method dumps the left parenthesis and it's corresponding leave method
     * dumps the right parenthesis.
     * @param node the node to dump
     */
    protected void arrive(BinaryExpression node)
    {   out.print( "(" ); //NOI18N
    }

    /**
     * Binary expressions are dumped in parenthesized manner. Thus, this
     * method dumps the right parenthesis and it's corresponding leave method
     * dumps the left parenthesis.
     * @param node the node to dump
     * @param results an array of <code>null</code> instances
     * @return <code>null</code>
     */
    protected Object leave(BinaryExpression node, Object[] results)
    {   out.print( ")" ); //NOI18N
        return null;
    }

    /**
     * Method call expressions are dumped like
     * <code>target.methodName(arguments)</code>.
     * @param node the node to dump
     * @param results an array of <code>null</code> instances
     * @return <code>null</code>
     */
    protected Object leave(MethodCallExpression node, Object[] results)
    {   if( node.getArguments()==null )
            out.print( "."+node.getMethodName()+"(" ); //NOI18N
        out.print( ")" ); //NOI18N
        return null;
    }

    /**
     * Ordering expressions are dumped like
     * <code>Ascending/Descending(expression)</code>.
     * @param node the node to dump
     * @param results an array of <code>null</code> instances
     * @return <code>null</code>
     */
    protected Object leave(OrderingExpression node, Object[] results)
    {   out.print( ")" ); //NOI18N
        return null;
    }

    /**
     * Method call expressions are dumped like
     * <code>target.methodName(arguments)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild the result of the previsous child node
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    protected boolean walkNextChild(MethodCallExpression node, 
                                    Object resultOfPreviousChild, 
                                    int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( "."+node.getMethodName()+"(" ); //NOI18N
        else if( indexOfNextChild>1 )
            out.print( ", " ); //NOI18N
        return true;
    }

    //public methods

    /**
     * Ascending ordering expressions are dumped like
     * <code>Ascending(expression)</code>.
     * @param node the node to dump
     */
    public void arrive(AscendingOrderingExpression node)
    {   out.print( "Ascending(" ); //NOI18N
    }

    /**
     * Descending ordering expressions are dumped like
     * <code>Descending(expression)</code>.
     * @param node the node to dump
     */
    public void arrive(DescendingOrderingExpression node)
    {   out.print( "Descending(" ); //NOI18N
    }

    /**
     * Dumps the value of the argument <code>node</code>. If that node holds
     * a String instance, then the string value is dumped in double quotes.
     * @param node the node to dump
     */
    public void arrive(ConstantExpression node)
    {   Object value = node.getValue();
        if( value instanceof String )
            out.print( "\""+value+"\"" ); //NOI18N
        else
            out.print( value );
    }

    /**
     * Cast expressions are dumped like <code>(type)expression</code>.
     * @param node the node to dump
     */
    public void arrive(CastExpression node)
    {   out.print( "("+node.getTypeName()+")" ); //NOI18N
    }

    /**
     * Field access expressions are dumped like <code>target.fieldName</code>.
     * @param node the node to dump
     */
    public void arrive(FieldAccessExpression node)
    {}

    /**
     * Complement expressions are dumped like <code>~expression</code>.
     * @param node the node to dump
     */
    public void arrive(ComplementExpression node)
    {   out.print( "~" ); //NOI18N
    }

    /**
     * Dumps the name of an identifier expression.
     * @param node the node to dump
     */
    public void arrive(IdentifierExpression node)
    {   out.print( node.getName() );
    }

    /**
     * Dumps a not expression.
     * It is dumped like <code>!expression</code>.
     * @param node the node to dump
     */
    public void arrive(NotExpression node)
    {   out.print( "!" ); //NOI18N
    }

    /**
     * Unary minus expressions are dumped like <code>-expression</code>.
     * @param node the node to dump
     */
    public void arrive(UnaryMinusExpression node)
    {   out.print( "-" ); //NOI18N
    }

    /**
     * Field access expressions are dumped like <code>target.fieldName</code>.
     * @param node the node to dump
     */
    public Object leave(FieldAccessExpression node, Object[] results)
    {   out.print( "."+node.getName() ); //NOI18N
        return null;
    }

    /**
     * Query trees are dumped like 
     * <code>orderingExpressions, filterExpression</code>.
     * @param node the node to dump
     */
    public Object leave(QueryTree node, Object[] results)
    {   out.println();
        return null;
    }


    /**
     * A logical and expression is dumped like
     * <code>(leftExpression & rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(AndExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " & " ); //NOI18N
        return true;
    }

    /**
     * A conditional and expression is dumped like
     * <code>(leftExpression && rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(ConditionalAndExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " && " ); //NOI18N
        return true;
    }

    /**
     * A logical or expression is dumped like
     * <code>(leftExpression || rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(ConditionalOrExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " || " ); //NOI18N
        return true;
    }

    /**
     * A divide expression is dumped like
     * <code>(leftExpression / rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(DivideExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " / " ); //NOI18N
        return true;
    }

    /**
     * An equals expression is dumped like
     * <code>(leftExpression == rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(EqualsExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " == " ); //NOI18N
        return true;
    }

    /**
     * A greater than equals expression is dumped like
     * <code>(leftExpression >= rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(GreaterThanEqualsExpression node, 
                                 Object resultOfPreviousChild,
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " >= " ); //NOI18N
        return true;
    }

    /**
     * A greater than expression is dumped like
     * <code>(leftExpression > rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(GreaterThanExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " > " ); //NOI18N
        return true;
    }

    /**
     * A less than equals expression is dumped like
     * <code>(leftExpression <= rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(LessThanEqualsExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " <= " ); //NOI18N
        return true;
    }

    /**
     * A less than expression is dumped like
     * <code>(leftExpression < rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(LessThanExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " < " ); //NOI18N
        return true;
    }

    /**
     * A minus expression is dumped like
     * <code>(leftExpression - rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(MinusExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " - " ); //NOI18N
        return true;
    }

    /**
     * Dumps a not equals expression.
     * It is dumped like <code>(leftExpression != rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(NotEqualsExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " != " ); //NOI18N
        return true;
    }

    /**
     * A logical or expression is dumped like
     * <code>(leftExpression | rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(OrExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " | " ); //NOI18N
        return true;
    }

    /**
     * Query trees are dumped like <code>orderingExpressions, 
     * filterExpression</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(QueryTree node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   Object[] children = node.getChildren();
        if( indexOfNextChild!=0 &&
            (children[indexOfNextChild-1] instanceof OrderingExpression &&
             (children[indexOfNextChild] instanceof OrderingExpression ||
              children[indexOfNextChild] instanceof Expression)) )
            out.print( ", " ); //NOI18N
        return true;
    }

    /**
     * A plus expression is dumped like
     * <code>(leftExpression + rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(PlusExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " + " ); //NOI18N
        return true;
    }

    /**
     * A times expression is dumped like
     * <code>(leftExpression * rightExpression)</code>.
     * @param node the parent node of the children currently dumped
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>true</code>
     */
    public boolean walkNextChild(TimesExpression node, 
                                 Object resultOfPreviousChild, 
                                 int indexOfNextChild)
    {   if( indexOfNextChild==1 )
            out.print( " * " ); //NOI18N
        return true;
    }
}
