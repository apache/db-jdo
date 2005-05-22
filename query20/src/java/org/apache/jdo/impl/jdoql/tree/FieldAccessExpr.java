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

import javax.jdo.PersistenceManager;

import org.apache.jdo.impl.jdoql.jdoqlc.JDOQLTokenTypes;
import org.apache.jdo.impl.jdoql.jdoqlc.TypeSupport;
import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.FieldAccessExpression;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.pm.PersistenceManagerInternal;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * This node represents a field access expression. Field access expressions
 * have exactly one child, the target expression.
 * This expression can be an arbitrary expression.
 *
 * @author Michael Watzek
 */
public final class FieldAccessExpr 
    extends IdentifierExpr implements FieldAccessExpression
{
    String fieldName;
    transient JavaField javaField;
    transient Field field;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public FieldAccessExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public FieldAccessExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by <code>StaticFieldAccessExpr</code>.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     * @param tokenType the token type
     * @param name the name of this identifier
     * @param clazz the Java type of this identifier
     */
    FieldAccessExpr(int tokenType, String name, Class clazz)
    {   super( tokenType, name, clazz );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param target the target expression
     * @param fieldName the field name of this field access expression
     */
    FieldAccessExpr(Expression target, String fieldName)
    {   super( JDOQLTokenTypes.FIELD_ACCESS, fieldName, null );
        init( target, fieldName );
    }

    /**
     * Returns the name of the accessed field.
     * Please note, that this name does not contain any information
     * about the target object of this field access.
     * @return the field name
     */
    public String getName()
    {   return this.fieldName;
    }

    /**
     * Sets the name of the accessed field.
     * Please note, that this name must not contain any information
     * about the target object of this field access.
     * This method is used by semantic analysis only.
     * @param fieldName the field name
     */
    public void setName(String fieldName)
    {   this.fieldName = fieldName;
    }

    /**
     * Returns the target expression of this field access.
     * The target expression can be an instance of
     * <code>ThisExpression</code> or an instance of an arbitrary other
     * <code>Expression</code>, e.g. <code>FieldAccessExpression</code>.
     * @return the target expression
     */
    public Expression getTarget()
    {   ASTToChildren();
        if( this.children==null ||
            this.children.length<1 )
            return null;
        return (Expression) this.children[0];
    }

    /**
     * Returns the value of the field corresponding with this
     * field access expression for the argument <code>object</code>.
     * Note: If the field value is obtained via reflection and
     * the reflection call throws an <code>IllegalAccessException</code>,
     * then undefined is returned.
     * @param pm the persistence manager of the query
     * @param object the instance for which to return the field value
     * @return the field value for <code>object</code>
     * @exception JDOQueryException if the access to the desired field is denied
     */
    public Object getFieldValue(PersistenceManager pm, Object object)
    {   JavaField javaField = getFieldInfo();
        int fieldNumber = TypeSupport.getFieldNumber( javaField.getJDOField(), 
                                                      pm, object );
        if (fieldNumber == -1) {
            return TypeSupport.getFieldValue( getField(), object );
        }
        else {
            return TypeSupport.getFieldValue(
                fieldNumber, (PersistenceManagerInternal)pm, object );
        }
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
     * Initializes fields of <code>this</code>.
     * @param target the target expression of this field access
     * @param fieldName the name of the field to access
     */
    void init(Expression target, String fieldName)
    {   setChildren( new Node[] {target} );
        this.fieldName = fieldName;
    }

    /**
     * Returns the model's field object assciated with this instance.
     * If that field object is <code>null</code>,
     * then it is computed by this method.
     * @return the model's field object
     * @exception JDOQueryException if the access to the desired field is denied
     */
    JavaField getFieldInfo()
    {   if( this.javaField==null )
        {   Expr target = (Expr) getTarget();
            this.javaField = target.getTypeInfo().getJavaField(this.fieldName);
        }
        return this.javaField;
    }

    /** */
    private Field getField()
    {   if( this.field==null )
            this.field = (Field) AccessController.doPrivileged(
               new PrivilegedAction() {
                   public Object run () {
                       return TypeSupport.getAccessibleField(getFieldInfo());
                   }});
        return this.field;
    }
    
}

