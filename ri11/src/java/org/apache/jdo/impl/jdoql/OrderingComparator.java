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

package org.apache.jdo.impl.jdoql;


import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.jdo.impl.jdoql.scope.UNDEFINED;
import org.apache.jdo.jdoql.tree.DescendingOrderingExpression;
import org.apache.jdo.jdoql.tree.OrderingExpression;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.jdoql.tree.TreeWalker;

/**
 * An instance of this class is used to compare two instances of
 * <code>Comparable</code> based on the ordering expressions
 * defined by a query tree. This instance holds references to a query tree,
 * a tree walker and a memory query instance for this purpose.
 *
 * @author Michael Watzek
 */
public class OrderingComparator implements Comparator
{
    private static final UNDEFINED  undefined = UNDEFINED.getInstance();

          List          orderings;
    final TreeWalker    walker;
    final MemoryQuery   nodeVisitor;

    /**
     * Constructs an ordering comparator for a tree walker and a node visitor.
     * This comparator is not bound to a query tree.
     * It can be bound to a query tree by calling method
     * <code>setQueryTree</code>.
     * @param walker the tree walker
     * @param nodeVisitor the node visitor evaluating the ordering expressions
     */
    public OrderingComparator(TreeWalker walker, MemoryQuery nodeVisitor)
    {   this( walker, nodeVisitor, null );
    }

    /**
     * Constructs an ordering comparator for a tree walker, a node visitor
     * and a query tree. This comparator is bound to that query tree.
     * @param walker the tree walker
     * @param nodeVisitor the node visitor evaluating the ordering expressions
     * @param queryTree the query tree containing the ordering expressions
     */
    public OrderingComparator(TreeWalker walker, MemoryQuery nodeVisitor, QueryTree queryTree)
    {   this.walker = walker;
        this.nodeVisitor = nodeVisitor;
        setQueryTree( queryTree );
    }

    /**
     * Sets the query tree for this ordering comparator. Each ordering comparator
     * can be bound to a query tree. The ordering expression list inside
     * that query tree determines the ordering semantics of this comparator.
     * @param queryTree the query tree containing the ordering expression list
     */
    public void setQueryTree(QueryTree queryTree)
    {   if( queryTree!=null )
            this.orderings = queryTree.getOrderingExpressions();
        else
            this.orderings = null;
    }

    /**
     * Required method for implementing the interface <code>Comparator</code>.
     * This method returns a negative integer, zero, or a positive integer
     * as the first argument is less than, equal to, or greater than the second
     * argument.
     * If this instance is not bound to a query tree or,
     * if the query tree bound to this instance
     * does not have any ordering expression, then argument <code>o1</code>
     * is less than argument <code>o2</code> by definition.
     * @param o1 the first object to be compared
     * @param o2 the second object to be compared
     * @return a negative integer, zero, or a positive integer
     * as the first argument is less than, equal to, or greater than the second
     * @exception ClassCastException if the arguments are not instances of
     * <code>Comparable</code>
     */
    public int compare(Object o1, Object o2)
    {   int cmp = -1;
        if( this.orderings!=null )
            for( Iterator i=this.orderings.iterator(); i.hasNext(); )
            {   OrderingExpression orderExpr = (OrderingExpression) i.next();
                nodeVisitor.setCurrent( o1 );
                Object result1 = walker.walk( orderExpr, nodeVisitor );
                nodeVisitor.setCurrent( o2 );
                Object result2 = walker.walk( orderExpr, nodeVisitor );
                if( result1!=null ||
                    result2!=null )
                {   if( !(result1 instanceof Comparable) )
                        break;
                    else if( !(result2 instanceof Comparable) )
                    {   cmp = 1;
                        break;
                    }
                    cmp = ((Comparable)result1).compareTo( result2 );
                    if( cmp!=0 )
                    {   if( orderExpr instanceof DescendingOrderingExpression )
                            cmp = -cmp;
                        break;
            }   }   }
        return cmp;
    }
}