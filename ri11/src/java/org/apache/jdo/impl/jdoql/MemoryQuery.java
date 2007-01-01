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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

import org.apache.jdo.impl.jdoql.scope.ParameterTable;
import org.apache.jdo.impl.jdoql.scope.UNDEFINED;
import org.apache.jdo.impl.jdoql.scope.VariableTable;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.AbstractNodeVisitor;
import org.apache.jdo.jdoql.tree.AndExpression;
import org.apache.jdo.jdoql.tree.CastExpression;
import org.apache.jdo.jdoql.tree.ComplementExpression;
import org.apache.jdo.jdoql.tree.ConditionalAndExpression;
import org.apache.jdo.jdoql.tree.ConditionalOrExpression;
import org.apache.jdo.jdoql.tree.ConstantExpression;
import org.apache.jdo.jdoql.tree.ContainsCallExpression;
import org.apache.jdo.jdoql.tree.DivideExpression;
import org.apache.jdo.jdoql.tree.EndsWithCallExpression;
import org.apache.jdo.jdoql.tree.EqualsExpression;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.FieldAccessExpression;
import org.apache.jdo.jdoql.tree.GreaterThanEqualsExpression;
import org.apache.jdo.jdoql.tree.GreaterThanExpression;
import org.apache.jdo.jdoql.tree.IsEmptyCallExpression;
import org.apache.jdo.jdoql.tree.LessThanEqualsExpression;
import org.apache.jdo.jdoql.tree.LessThanExpression;
import org.apache.jdo.jdoql.tree.MinusExpression;
import org.apache.jdo.jdoql.tree.NotEqualsExpression;
import org.apache.jdo.jdoql.tree.NotExpression;
import org.apache.jdo.jdoql.tree.OrExpression;
import org.apache.jdo.jdoql.tree.OrderingExpression;
import org.apache.jdo.jdoql.tree.ParameterAccessExpression;
import org.apache.jdo.jdoql.tree.PlusExpression;
import org.apache.jdo.jdoql.tree.StartsWithCallExpression;
import org.apache.jdo.jdoql.tree.StaticFieldAccessExpression;
import org.apache.jdo.jdoql.tree.ThisExpression;
import org.apache.jdo.jdoql.tree.TimesExpression;
import org.apache.jdo.jdoql.tree.TreeWalker;
import org.apache.jdo.jdoql.tree.UnaryMinusExpression;
import org.apache.jdo.jdoql.tree.UnaryPlusExpression;
import org.apache.jdo.jdoql.tree.VariableAccessExpression;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.util.I18NHelper;

/**
 * An instance of this class is used to evaluate a
 * query tree in memory. For this purpose this class keeps references
 * to a parameter/variable table and to the current object corresponding
 * with a <code>ThisExpression</code>.
 * It extends <code>AbstractNodeVisitor</code>.
 * To evaluate a query tree, you need to pass the query tree instance and
 * an instance of this class to method <code>walk</code> of a tree walker
 * instance.
 *
 * @author Michael Watzek
 */
public class MemoryQuery extends AbstractNodeVisitor
{
    /** I18N support */
    final static I18NHelper msg = I18NHelper.getInstance(MemoryQuery.class);
    final static UNDEFINED  undefined = UNDEFINED.getInstance();

    final TreeWalker            walker = new TreeWalker();
    final List                  boundVariables = new ArrayList();
    VariableAccessExpression    unboundVariableAccess = null;
    BoundVariable               removedBoundVariable = null;

    final PersistenceManagerInternal pm;
    final ParameterTable        parameters;
    final VariableTable         variables;
    Object                      current;

    /**
     * Constructs an instance of this class for the specified paramter table
     * and variable table. This query evaluator uses reflection for
     * field accesses.
     * @param parameters the parameter table
     * @param variables the variable table
     */
    public MemoryQuery(ParameterTable parameters, VariableTable variables)
    {   this( null, parameters, variables );
    }

    /**
     * Constructs an instance of this class for the specified paramter table
     * and variable table.
     * @param pm the persistence manager
     * @param parameters the parameter table
     * @param variables the variable table
     */
    public MemoryQuery(PersistenceManagerInternal pm, ParameterTable parameters, VariableTable variables)
    {   this.pm = pm;
        this.parameters = parameters;
        this.variables = variables;
    }

    /**
     * Sets the instance returned by leaving an instance if
     * <code>ThisExpression</code>.
     * @param current the instance to set
     */
    public void setCurrent(Object current)
    {   this.current = current;
    }

    //public methods of NodeVisitor

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non boolean types
     */
    public Object leave(AndExpression node, Object[] results)
    {   Class clazz = node.getJavaClass();
        if( clazz==Boolean.class || clazz==boolean.class )
            return logicalAnd( results[0], results[1] );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "&", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is the same as the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by the node's child
     * @return the result evaluated for node
     * @exception JDOQueryException if the result evaluated by the node's
     * child is not an instance of the type to cast to
     */
    public Object leave(CastExpression node, Object[] results)
    {   Object value = results[1];
        if( value==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( value!=null &&
            !clazz.isInstance(value) )
        {   
            if( clazz==Byte.class || clazz==byte.class ) {
                if( value instanceof Character )
                    return new Byte( (byte)((Character)value).charValue() );
                else
                    return new Byte( ((Number)value).byteValue() );
            }
            else if( clazz==Double.class || clazz==double.class ) {
                if( value instanceof Character )
                    return new Double( ((Character)value).charValue() );
                else
                    return new Double( ((Number)value).doubleValue() );
            }
            else if( clazz==Float.class || clazz==float.class ) {
                if( value instanceof Character )
                    return new Float( ((Character)value).charValue() );
                else
                    return new Float( ((Number)value).floatValue() );
            }
            else if( clazz==Integer.class || clazz==int.class ) {
                if( value instanceof Character )
                    return new Integer( ((Character)value).charValue() );
                else
                    return new Integer( ((Number)value).intValue() );
            }
            else if( clazz==Long.class || clazz==long.class ) {
                if( value instanceof Character )
                   return new Long( ((Character)value).charValue() );
                else
                    return new Long( ((Number)value).longValue() );
            }
            else if( clazz==Short.class || clazz==short.class ) {
                if( value instanceof Character )
                    return new Short( (short) ((Character)value).charValue() );
                else
                    return new Short( ((Number)value).shortValue() );
            }
            else if( clazz==BigInteger.class ) {
                if( value instanceof Character )
                   return BigInteger.valueOf( (long) ((Character)value).charValue() );
                else
                    return BigInteger.valueOf( ((Number)value).longValue() );
            }
            else if( clazz==BigDecimal.class ) {
                if( value instanceof Character )
                    return new BigDecimal( (double) ((Character)value).charValue() );
                else
                    return new BigDecimal( ((Number)value).doubleValue() );
            }
            else if( clazz==Character.class || clazz==char.class ) {
                if (value instanceof Number )
                    return new Character((char)((Number)value).intValue());
                else
                    return value;
            }
            // value is not null and is not an instance of the type
            // specified in the cast expression => return undefined
            return undefined;
        }
        return value;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is based on the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by the node's child
     * @return the result evaluated for node
     * @exception JDOQueryException if the result evaluated by the node's
     * child is not a boolean or integral type.
     */
    public Object leave(ComplementExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( ~((Number)results[0]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( ~((Number)results[0]).longValue() );
        else if( clazz==BigInteger.class )
            return ((BigInteger)results[0]).not();
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) ~((Number)results[0]).byteValue() );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) ~((Character)results[0]).charValue() );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) ~((Number)results[0]).shortValue() );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "~", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non boolean types
     */
    public Object leave(ConditionalAndExpression node, Object[] results)
    {   Class clazz = node.getJavaClass();
        if( clazz==Boolean.class || clazz==boolean.class )
            return logicalAnd( results[0], results[1] );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "&&", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non boolean types
     */
    public Object leave(ConditionalOrExpression node, Object[] results)
    {   Class clazz = node.getJavaClass();
        if( clazz==Boolean.class || clazz==boolean.class )
            return logicalOr( results[0], results[1] );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "||", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> is <code>null</code> as the argument
     * <code>node</code> does not have any children. The result
     * returned by this method is the object wrapped by the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results <code>null</code>
     * @return the object wrapped by node
     */
    public Object leave(ConstantExpression node, Object[] results)
    {   return node.getValue();
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(ContainsCallExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
        {   // bind an empty set to the variable, if collection evaluates
            // to null or undefined, otherwise the variable access results
            // in an exception that unbound variables are not supported.
            if( this.unboundVariableAccess!=null )
            {   this.boundVariables.add(  new BoundVariable(this.unboundVariableAccess.getName(), this.unboundVariableAccess.getJavaClass(), Collections.EMPTY_SET) );
            }
            this.unboundVariableAccess = null;
            return undefined;
        }
        Collection collection = (Collection) results[0];
        boolean result;
        if( this.unboundVariableAccess!=null )
        {   this.boundVariables.add(  new BoundVariable(this.unboundVariableAccess.getName(), this.unboundVariableAccess.getJavaClass(), collection) );
            result = !collection.isEmpty();
        }
        else
            result = ((Collection)results[0]).contains( results[1] );
        this.unboundVariableAccess = null;
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(DivideExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( ((Number)results[0]).intValue() / ((Number)results[1]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( ((Number)results[0]).longValue() / ((Number)results[1]).longValue() );
        else if( clazz==Double.class || clazz==double.class )
            return new Double( ((Number)results[0]).doubleValue() / ((Number)results[1]).doubleValue() );
        else if( clazz==Float.class || clazz==float.class )
            return new Float( ((Number)results[0]).floatValue() / ((Number)results[1]).floatValue() );
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            return left.divide( right, BigDecimal.ROUND_HALF_UP );
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            return left.divide( right );
        }
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) (((Number)results[0]).byteValue() / ((Number)results[1]).byteValue()) );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) (((Character)results[0]).charValue() / ((Character)results[1]).charValue()) );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) (((Number)results[0]).shortValue() / ((Number)results[1]).shortValue()) );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "/", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(EndsWithCallExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        boolean result = ((String)results[0]).endsWith( (String)results[1] );
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(EqualsExpression node, Object[] results)
    {   if( results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        if( results[0]==null && results[1]==null )
            result = true;
        else if( results[0]==null || results[1]==null )
            result = false;
        else
        {   Class clazz = node.getCommonOperandType();
            if( clazz==Integer.class || clazz==int.class )
                result = ((Number)results[0]).intValue() == ((Number)results[1]).intValue();
            else if( clazz==Long.class || clazz==long.class )
                result = ((Number)results[0]).longValue() == ((Number)results[1]).longValue();
            else if( clazz==Double.class || clazz==double.class )
                result = ((Number)results[0]).doubleValue() == ((Number)results[1]).doubleValue();
            else if( clazz==Float.class || clazz==float.class )
                result = ((Number)results[0]).floatValue() == ((Number)results[1]).floatValue();
            else if( clazz==BigDecimal.class  )
            {   BigDecimal left, right;
                if( results[0] instanceof BigDecimal )
                    left = (BigDecimal) results[0];
                else
                    left = new BigDecimal( results[0].toString() );
                if( results[1] instanceof BigDecimal )
                    right = (BigDecimal) results[1];
                else
                    right = new BigDecimal( results[1].toString() );
                result = left.compareTo( right ) == 0;
            }
            else if( clazz==BigInteger.class  )
            {   BigInteger left, right;
                if( results[0] instanceof BigInteger )
                    left = (BigInteger) results[0];
                else
                    left = new BigInteger( results[0].toString() );
                if( results[1] instanceof BigInteger )
                    right = (BigInteger) results[1];
                else
                    right = new BigInteger( results[1].toString() );
                result = left.compareTo( right ) == 0;
            }
            else if( Date.class.isAssignableFrom(clazz) )
                result = ((Date)results[0]).getTime() == ((Date)results[1]).getTime();
            else if( clazz==Byte.class || clazz==byte.class )
                result = ((Number)results[0]).byteValue() == ((Number)results[1]).byteValue();
            else if( clazz==Character.class || clazz==char.class )
                result = ((Character)results[0]).charValue() == ((Character)results[1]).charValue();
            else if( clazz==Short.class || clazz==short.class )
                result = ((Number)results[0]).shortValue() == ((Number)results[1]).shortValue();
            else 
            {   PersistenceManager leftPM = JDOHelper.getPersistenceManager(results[0]);
                PersistenceManager rightPM = JDOHelper.getPersistenceManager(results[1]);
                if( leftPM!=null && rightPM!=null )
                    // use == for persistence instances
                    result = results[0] == results[1];
                else if( leftPM==null && rightPM==null )
                    // use equals for non persistent instances
                    result = results[0].equals( results[1] );
                else 
                    // comparing persistent with non persistent instance
                    result = false;
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children is undefined
     */
    public Object leave(FieldAccessExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        return node.getFieldValue( pm, results[0] );
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(GreaterThanEqualsExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        Class clazz = node.getCommonOperandType();
        if( clazz==Integer.class || clazz==int.class )
            result = ((Number)results[0]).intValue() >= ((Number)results[1]).intValue();
        else if( clazz==Long.class || clazz==long.class )
            result = ((Number)results[0]).longValue() >= ((Number)results[1]).longValue();
        else if( clazz==Double.class || clazz==double.class )
            result = ((Number)results[0]).doubleValue() >= ((Number)results[1]).doubleValue();
        else if( clazz==Float.class || clazz==float.class )
            result = ((Number)results[0]).floatValue() >= ((Number)results[1]).floatValue();
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            result = left.compareTo(right) >= 0;
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            result = left.compareTo(right) >= 0;
        }
        // Comparable covers Date and String
        else if( Comparable.class.isAssignableFrom(clazz) )
            result = ((Comparable)results[0]).compareTo((Comparable)results[1]) >= 0;
        else if( clazz==Byte.class || clazz==byte.class )
            result = ((Number)results[0]).byteValue() >= ((Number)results[1]).byteValue();
        else if( clazz==Character.class || clazz==char.class )
            result = ((Character)results[0]).charValue() >= ((Character)results[1]).charValue();
        else if( clazz==Short.class || clazz==short.class )
            result = ((Number)results[0]).shortValue() >= ((Number)results[1]).shortValue();
        else
            throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", ">=", clazz.getName()) ); //NOI18N
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(GreaterThanExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        Class clazz = node.getCommonOperandType();
        if( clazz==Integer.class || clazz==int.class )
            result = ((Number)results[0]).intValue() > ((Number)results[1]).intValue();
        else if( clazz==Long.class || clazz==long.class )
            result = ((Number)results[0]).longValue() > ((Number)results[1]).longValue();
        else if( clazz==Double.class || clazz==double.class )
            result = ((Number)results[0]).doubleValue() > ((Number)results[1]).doubleValue();
        else if( clazz==Float.class || clazz==float.class )
            result = ((Number)results[0]).floatValue() > ((Number)results[1]).floatValue();
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            result = left.compareTo(right) > 0;
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            result = left.compareTo(right) > 0;
        }
        // Comparable covers Date and String
        else if( Comparable.class.isAssignableFrom(clazz) )
            result = ((Comparable)results[0]).compareTo((Comparable)results[1]) > 0;
        else if( clazz==Byte.class || clazz==byte.class )
            result = ((Number)results[0]).byteValue() > ((Number)results[1]).byteValue();
        else if( clazz==Character.class || clazz==char.class )
            result = ((Character)results[0]).charValue() > ((Character)results[1]).charValue();
        else if( clazz==Short.class || clazz==short.class )
            result = ((Number)results[0]).shortValue() > ((Number)results[1]).shortValue();
        else
            throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", ">", clazz.getName()) ); //NOI18N
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(IsEmptyCallExpression node, Object[] results)
    {   if( results[0]==null )
            return Boolean.TRUE;
        else if( results[0]==undefined )
            return undefined;
        boolean result = ((Collection)results[0]).isEmpty();
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(LessThanEqualsExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        Class clazz = node.getCommonOperandType();
        if( clazz==Integer.class || clazz==int.class )
            result = ((Number)results[0]).intValue() <= ((Number)results[1]).intValue();
        else if( clazz==Long.class || clazz==long.class )
            result = ((Number)results[0]).longValue() <= ((Number)results[1]).longValue();
        else if( clazz==Double.class || clazz==double.class )
            result = ((Number)results[0]).doubleValue() <= ((Number)results[1]).doubleValue();
        else if( clazz==Float.class || clazz==float.class )
            result = ((Number)results[0]).floatValue() <= ((Number)results[1]).floatValue();
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            result = left.compareTo(right) <= 0;
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            result = left.compareTo(right) <= 0;
        }
        // Comparable covers Date and String
        else if( Comparable.class.isAssignableFrom(clazz) )
            result = ((Comparable)results[0]).compareTo((Comparable)results[1]) <= 0;
        else if( clazz==Byte.class || clazz==byte.class )
            result = ((Number)results[0]).byteValue() <= ((Number)results[1]).byteValue();
        else if( clazz==Character.class || clazz==char.class )
            result = ((Character)results[0]).charValue() <= ((Character)results[1]).charValue();
        else if( clazz==Short.class || clazz==short.class )
            result = ((Number)results[0]).shortValue() <= ((Number)results[1]).shortValue();
        else
            throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "<=", clazz.getName()) ); //NOI18N
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(LessThanExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        Class clazz = node.getCommonOperandType();
        if( clazz==Integer.class || clazz==int.class )
            result = ((Number)results[0]).intValue() < ((Number)results[1]).intValue();
        else if( clazz==Long.class || clazz==long.class )
            result = ((Number)results[0]).longValue() < ((Number)results[1]).longValue();
        else if( clazz==Double.class || clazz==double.class )
            result = ((Number)results[0]).doubleValue() < ((Number)results[1]).doubleValue();
        else if( clazz==Float.class || clazz==float.class )
            result = ((Number)results[0]).floatValue() < ((Number)results[1]).floatValue();
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            result = left.compareTo(right) < 0;
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            result = left.compareTo(right) < 0;
        }
        // Comparable covers Date and String
        else if( Comparable.class.isAssignableFrom(clazz) )
            result = ((Comparable)results[0]).compareTo((Comparable)results[1]) < 0;
        else if( clazz==Byte.class || clazz==byte.class )
            result = ((Number)results[0]).byteValue() < ((Number)results[1]).byteValue();
        else if( clazz==Character.class || clazz==char.class )
            result = ((Character)results[0]).charValue() < ((Character)results[1]).charValue();
        else if( clazz==Short.class || clazz==short.class )
            result = ((Number)results[0]).shortValue() < ((Number)results[1]).shortValue();
        else
            throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "<", clazz.getName()) ); //NOI18N
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(MinusExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( ((Number)results[0]).intValue() - ((Number)results[1]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( ((Number)results[0]).longValue() - ((Number)results[1]).longValue() );
        else if( clazz==Double.class || clazz==double.class )
            return new Double( ((Number)results[0]).doubleValue() - ((Number)results[1]).doubleValue() );
        else if( clazz==Float.class || clazz==float.class )
            return new Float( ((Number)results[0]).floatValue() - ((Number)results[1]).floatValue() );
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            return left.subtract( right );
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            return left.subtract( right );
        }
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) (((Number)results[0]).byteValue() - ((Number)results[1]).byteValue()) );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) (((Character)results[0]).charValue() - ((Character)results[1]).charValue()) );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) (((Number)results[0]).shortValue() - ((Number)results[1]).shortValue()) );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "-", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(NotEqualsExpression node, Object[] results)
    {   if( results[0]==undefined || results[1]==undefined )
            return undefined;
        boolean result;
        if( results[0]==null && results[1]==null )
            result = false;
        else if( results[0]==null || results[1]==null )
            result = true;
        else
        {   Class clazz = node.getCommonOperandType();
            if( clazz==Integer.class || clazz==int.class )
                result = ((Number)results[0]).intValue() != ((Number)results[1]).intValue();
            else if( clazz==Long.class || clazz==long.class )
                result = ((Number)results[0]).longValue() != ((Number)results[1]).longValue();
            else if( clazz==Double.class || clazz==double.class )
                result = ((Number)results[0]).doubleValue() != ((Number)results[1]).doubleValue();
            else if( clazz==Float.class || clazz==float.class )
                result = ((Number)results[0]).floatValue() != ((Number)results[1]).floatValue();
            else if( clazz==BigDecimal.class  )
            {   BigDecimal left, right;
                if( results[0] instanceof BigDecimal )
                    left = (BigDecimal) results[0];
                else
                    left = new BigDecimal( results[0].toString() );
                if( results[1] instanceof BigDecimal )
                    right = (BigDecimal) results[1];
                else
                    right = new BigDecimal( results[1].toString() );
                result = left.compareTo( right ) != 0;
            }
            else if( clazz==BigInteger.class  )
            {   BigInteger left, right;
                if( results[0] instanceof BigInteger )
                    left = (BigInteger) results[0];
                else
                    left = new BigInteger( results[0].toString() );
                if( results[1] instanceof BigInteger )
                    right = (BigInteger) results[1];
                else
                    right = new BigInteger( results[1].toString() );
                result = left.compareTo( right ) != 0;
            }
            else if( Date.class.isAssignableFrom(clazz) )
                result = ((Date)results[0]).getTime() != ((Date)results[1]).getTime();
            else if( clazz==Byte.class || clazz==byte.class )
                result = ((Number)results[0]).byteValue() != ((Number)results[1]).byteValue();
            else if( clazz==Character.class || clazz==char.class )
                result = ((Character)results[0]).charValue() != ((Character)results[1]).charValue();
            else if( clazz==Short.class || clazz==short.class )
                result = ((Number)results[0]).shortValue() != ((Number)results[1]).shortValue();
            else 
            {   PersistenceManager leftPM = JDOHelper.getPersistenceManager( results[0] );
                PersistenceManager rightPM = JDOHelper.getPersistenceManager( results[1] );
                if( leftPM!=null && rightPM!=null )
                    // use == for persistence instances
                    result = results[0] != results[1];
                else if( leftPM==null && rightPM==null )
                    // use equals for non persistent instances
                    result = !results[0].equals( results[1] );
                else
                    // comparing persistent with non persistent instance
                    result = true;
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is based on the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by child of node
     * @return the result evaluated for node
     */
    public Object leave(NotExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        boolean result = !((Boolean)results[0]).booleanValue();
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is based on the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by child of node
     * @return the result evaluated for node
     */
    public Object leave(OrderingExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        return results[0];
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non boolean types
     */
    public Object leave(OrExpression node, Object[] results)
    {   Class clazz = node.getJavaClass();
        if( clazz==Boolean.class || clazz==boolean.class )
            return logicalOr( results[0], results[1] );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "|", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> is <code>null</code> as the argument
     * <code>node</code> does not have any children. The result
     * returned by this method is the instance obtained by the parameter table
     * for for the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results <code>null</code>
     * @return the instance obtained by the parameter table for node
     */
    public Object leave(ParameterAccessExpression node, Object[] results)
    {   return this.parameters.getValue( node.getName() );
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(PlusExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( ((Number)results[0]).intValue() + ((Number)results[1]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( ((Number)results[0]).longValue() + ((Number)results[1]).longValue() );
        else if( clazz==Double.class || clazz==double.class )
            return new Double( ((Number)results[0]).doubleValue() + ((Number)results[1]).doubleValue() );
        else if( clazz==Float.class || clazz==float.class )
            return new Float( ((Number)results[0]).floatValue() + ((Number)results[1]).floatValue() );
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            return left.add( right );
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            return left.add( right );
        }
        else if( clazz==String.class )
            return (String)results[0] + (String)results[1];
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) (((Number)results[0]).byteValue() + ((Number)results[1]).byteValue()) );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) (((Character)results[0]).charValue() + ((Character)results[1]).charValue()) );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) (((Number)results[0]).shortValue() + ((Number)results[1]).shortValue()) );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "+", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(StartsWithCallExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        boolean result = ((String)results[0]).startsWith( (String)results[1] );
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     */
    public Object leave(StaticFieldAccessExpression node, Object[] results)
    {   return node.getFieldValue( pm );
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> is <code>null</code> as the argument
     * <code>node</code> does not have any children. The result
     * returned by this method is the current instance stored in this node visitor
     * by method <code>setCurrent</code>.
     * @param node the node to be evaluated
     * @param results <code>null</code>
     * @return the current instance stored in this node visitor
     * by method <code>setCurrent</code>
     */
    public Object leave(ThisExpression node, Object[] results)
    {   return this.current;
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the results evaluated
     * by the children of the argument <code>node</code>. The result
     * returned by this method is based on the results evaluated by the children
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the results evaluated by children of node
     * @return the result evaluated for node
     * @exception JDOQueryException if the results evaluated by the node's
     * children are non integral types
     */
    public Object leave(TimesExpression node, Object[] results)
    {   if( results[0]==null || results[1]==null ||
            results[0]==undefined || results[1]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( ((Number)results[0]).intValue() * ((Number)results[1]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( ((Number)results[0]).longValue() * ((Number)results[1]).longValue() );
        else if( clazz==Double.class || clazz==double.class )
            return new Double( ((Number)results[0]).doubleValue() * ((Number)results[1]).doubleValue() );
        else if( clazz==Float.class || clazz==float.class )
            return new Float( ((Number)results[0]).floatValue() * ((Number)results[1]).floatValue() );
        else if( clazz==BigDecimal.class  )
        {   BigDecimal left, right;
            if( results[0] instanceof BigDecimal )
                left = (BigDecimal) results[0];
            else
                left = new BigDecimal( results[0].toString() );
            if( results[1] instanceof BigDecimal )
                right = (BigDecimal) results[1];
            else
                right = new BigDecimal( results[1].toString() );
            return left.multiply( right );
        }
        else if( clazz==BigInteger.class  )
        {   BigInteger left, right;
            if( results[0] instanceof BigInteger )
                left = (BigInteger) results[0];
            else
                left = new BigInteger( results[0].toString() );
            if( results[1] instanceof BigInteger )
                right = (BigInteger) results[1];
            else
                right = new BigInteger( results[1].toString() );
            return left.multiply( right );
        }
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) (((Number)results[0]).byteValue() * ((Number)results[1]).byteValue()) );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) (((Character)results[0]).charValue() * ((Character)results[1]).charValue()) );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) (((Number)results[0]).shortValue() * ((Number)results[1]).shortValue()) );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "*", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is based on the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by the node's child
     * @return the result evaluated for node
     * @exception JDOQueryException if the result evaluated by the node's
     * child is not an integral type.
     */
    public Object leave(UnaryMinusExpression node, Object[] results)
    {   if( results[0]==null ||
            results[0]==undefined )
            return undefined;
        Class clazz = node.getJavaClass();
        if( clazz==Integer.class || clazz==int.class )
            return new Integer( -((Number)results[0]).intValue() );
        else if( clazz==Long.class || clazz==long.class )
            return new Long( -((Number)results[0]).longValue() );
        else if( clazz==Double.class || clazz==double.class )
            return new Double( -((Number)results[0]).doubleValue() );
        else if( clazz==Float.class || clazz==float.class )
            return new Float( -((Number)results[0]).floatValue() );
        else if( clazz==BigInteger.class )
            return ((BigInteger)results[0]).negate();
        else if( clazz==BigDecimal.class )
            return ((BigDecimal)results[0]).negate();
        else if( clazz==Byte.class || clazz==byte.class )
            return new Byte( (byte) -((Number)results[0]).byteValue() );
        else if( clazz==Character.class || clazz==char.class )
            return new Character( (char) -((Character)results[0]).charValue() );
        else if( clazz==Short.class || clazz==short.class )
            return new Short( (short) -((Number)results[0]).shortValue() );
        throw new JDOQueryException( msg.msg("EXC_IllegalResultTypeForExpression", "-", clazz.getName()) ); //NOI18N
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> contains the result evaluated
     * by the child of the argument <code>node</code>. The result
     * returned by this method is the same as the result evaluated by the child
     * of the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results the result evaluated by the node's child
     * @return the result evaluated for node
     */
    public Object leave(UnaryPlusExpression node, Object[] results)
    {   return results[0];
    }

    /**
     * Returns the result evaluated for the argument <code>node</code>.
     * The argument <code>results</code> is <code>null</code> as the argument
     * <code>node</code> does not have any children. The result
     * returned by this method is the instance obtained by the variable table
     * for for the argument <code>node</code>.
     * @param node the node to be evaluated
     * @param results <code>null</code>
     * @return the instance obtained by the variable table for node
     */
    public Object leave(VariableAccessExpression node, Object[] results)
    {   Object result = this.variables.getValue( node.getName() );
        if( result==undefined )
            this.unboundVariableAccess = node;
        return result;
    }

    /**
     * Returns <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater
     * than 0.
     * @param node the parent node of the children currently evaluated
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater
     * than 0
     */
    public boolean walkNextChild(AndExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   if( indexOfNextChild>0 )
        {   BoundVariable boundVariable = removeBoundVariable();
            if( isIteration(boundVariable, node.getRightExpression()) )
                return false;
        }
        return true;
    }

    /**
     * Returns <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is <code>null</code>, undefined or <code>false</code> and the argument <code>indexOfNextChild</code> is greater
     * than 0.
     * @param node the parent node of the children currently evaluated
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is <code>null</code>, undefined or <code>false</code> and the argument <code>indexOfNextChild</code> is greater
     * than 0
     */
    public boolean walkNextChild(ConditionalAndExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   if( indexOfNextChild>0 )
        {   BoundVariable boundVariable = removeBoundVariable();
            if ( isIteration(boundVariable, node.getRightExpression()) )
                return false;
            else if ( resultOfPreviousChild==undefined ) 
                // evaluate second operand, if first is undefined
                return true;

            Class clazz = node.getJavaClass();
            if( ((clazz==Boolean.class || clazz==boolean.class) &&
                 resultOfPreviousChild!=null &&
                 !((Boolean)resultOfPreviousChild).booleanValue()) )
                return false;
        }
        return true;
    }

    /**
     * Returns <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is <code>null</code>, undefined or <code>true</code> and the argument <code>indexOfNextChild</code> is greater
     * than 0.
     * @param node the parent node of the children currently evaluated
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is <code>null</code>, undefined or <code>true</code> and the argument <code>indexOfNextChild</code> is greater
     * than 0
     */
    public boolean walkNextChild(ConditionalOrExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   if( indexOfNextChild>0 )
        {   BoundVariable boundVariable = removeBoundVariable();
            if( resultOfPreviousChild==undefined ) 
                // evaluate second operand, if first is undefined
                return true;

            Class clazz = node.getJavaClass();
            if ( ((clazz==Boolean.class || clazz==boolean.class) &&
                  resultOfPreviousChild!=null &&
                  ((Boolean)resultOfPreviousChild).booleanValue()) )
                return false;
        }
        return true;
    }

    /**
     * Returns <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater
     * than 0.
     * @param node the parent node of the children currently evaluated
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater than 0
     */
    public boolean walkNextChild(ContainsCallExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   return true;
    }

    /**
     * Returns <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater
     * than 0.
     * @param node the parent node of the children currently evaluated
     * @param resultOfPreviousChild an array of <code>null</code> instances
     * @param indexOfNextChild the index of the next child to be dumped
     * @return <code>false</code> if the argument <code>resultOfPreviousChild</code>
     * is undefined and the argument <code>indexOfNextChild</code> is greater
     * than 0
     */
    public boolean walkNextChild(OrExpression node, Object resultOfPreviousChild, int indexOfNextChild)
    {   if( indexOfNextChild>0 )
        {   BoundVariable boundVariable = removeBoundVariable();
        }
        return true;
    }

    //private members
    private class BoundVariable
    {   String name;
        Class clazz;
        Collection collection;
        boolean result = false;

        BoundVariable(String name, Class clazz, Collection collection)
        {   this.name = name;
            this.clazz = clazz;
            this.collection = collection;
        }
        public boolean equals(Object obj)
        {   return name.equals( obj );
        }
        public int hashCode()
        {   return name.hashCode();
        }
        public String toString()
        {   return name.toString();
        }
    }
    private BoundVariable removeBoundVariable()
    {   if( this.unboundVariableAccess!=null )
            throw new JDOQueryException( msg.msg("EXC_CannotAccessUnboundVariables", this.unboundVariableAccess.getName()) ); //NOI18N
        int nrOfBoundVariables = this.boundVariables.size();
        if( nrOfBoundVariables>1 )
            throw new JDOQueryException( msg.msg("EXC_CannotProcessMultipleContainsClauses") ); //NOI18N
        BoundVariable boundVariable;
        if( nrOfBoundVariables>0 )
            boundVariable = (BoundVariable) this.boundVariables.remove( nrOfBoundVariables-1 );
        else
            boundVariable = null;
        return boundVariable;
    }
    private boolean isIteration(BoundVariable boundVariable, Expression expr)
    {   boolean result = false;
        if( boundVariable!=null )
        {   result = true;
            boundVariable.result = false;
            int nrOfBoundVariables = this.boundVariables.size();
            for( Iterator i=boundVariable.collection.iterator(); i.hasNext(); )
            {   Object value = i.next();
                if( boundVariable.clazz.isInstance(value) )
                {   this.variables.setValue( boundVariable.name, value );
                    Object object = walker.walk( expr, this );
                    while( nrOfBoundVariables<this.boundVariables.size() )
                        removeBoundVariable();
                    if( object!=null &&
                        object instanceof Boolean &&
                        ((Boolean)object).booleanValue() )
                    {   boundVariable.result = true;
                        break;
            }   }   }
            this.variables.setValue( boundVariable.name, undefined );
            this.removedBoundVariable = boundVariable;
        }
        return result;
    }
    private Object logicalAnd(Object left, Object right)
    {   Object result;
        //if the expression has been an iteration then return the iteration result
        if( right==null )
        {   if( this.removedBoundVariable!=null )
            {   result = this.removedBoundVariable.result ? Boolean.TRUE : Boolean.FALSE;
                this.removedBoundVariable = null;
            }
            else
                result = left;
        }
        else if( right==undefined ) 
        {   // right hand side is undefined =>
            // T AND U -> U
            // F AND U -> F
            // U AND U -> U
            if( (left==undefined) || ((Boolean)left).booleanValue() )
                result = undefined;
            else 
                result = Boolean.FALSE;
        }
        else if( left==undefined )
        {   // left hand side is undefined
            // U AND T -> U
            // U AND F -> F
            // U AND U -> U (handled above)
            if( ((Boolean)right).booleanValue() )
                result = undefined;
            else
                result = Boolean.FALSE;
        }
        else 
        {   result = ((Boolean)left).booleanValue() && ((Boolean)right).booleanValue() ?
                Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }
    private Object logicalOr(Object left, Object right)
    {   Object result;
        //if the expression has been an iteration then return the iteration result
        if( right==null )
        {   if( this.removedBoundVariable!=null )
            {   result = this.removedBoundVariable.result ? Boolean.TRUE : Boolean.FALSE;
                this.removedBoundVariable = null;
            }
            else
                result = left;
        }
        else if( right==undefined )
        {   // right hand side is undefined =>
            // T OR U -> T
            // F OR U -> U
            // U OR U -> U
            if( (left==undefined) || !((Boolean)left).booleanValue() )
                result = undefined;
            else
                result =  Boolean.TRUE;
        }
        else if( left==undefined )
        {   // left hand side is undefined
            // U OR T -> T
            // U OR F -> U
            // U OR U -> U (handled above)
            if ( ((Boolean)right).booleanValue() )
                result = Boolean.TRUE;
            else
                result = undefined; 
        }
        else 
        {   result = ((Boolean)left).booleanValue() || ((Boolean)right).booleanValue() ?
                Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }
    private void log(String s)
    {   System.out.print( s );
    }
    private void logln(String s)
    {   System.out.println( s );
    }
}
