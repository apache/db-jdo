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
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.NodeVisitor;
import org.apache.jdo.jdoql.tree.StaticFieldAccessExpression;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.pm.PersistenceManagerInternal;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * This node represents a static field access expression.
 * It inherits from <code>FieldAccessExpr</code>.
 * Static Field access expressions have exactly one child, the target expression.
 * That target expression is an identifier expression.
 *
 * @author Michael Watzek
 */
public final class StaticFieldAccessExpr
    extends IdentifierExpr implements StaticFieldAccessExpression
{
    String fieldName;
    transient JavaField javaField;
    transient Field field;

    /**
     * The noarg constructor is needed for ANTLR support and deserialization.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public StaticFieldAccessExpr()
    {}

    /**
     * The noarg constructor is needed for ANTLR support.
     * The caller must make sure to set the ANTLR tree structure himself
     * or, call <code>setChildren</code> optionally.
     */
    public StaticFieldAccessExpr(antlr.Token token)
    {   initialize( token );
    }

    /**
     * This constructor is called by the query tree instance.
     * It delegates to the super class constructor.
     * @param type the type of the instance wrapping the Java clazz
     * defining the static field
     * @param fieldName the field name of this static field access expression
     * @exception JDOQueryException if the field cannot be found in the
     * the result type of target.
     */
    StaticFieldAccessExpr(TypeImpl type, String fieldName)
    {   super( JDOQLTokenTypes.STATIC_FIELD_ACCESS, fieldName, null );
        setChildren( new Node[] {type} );
        this.fieldName = fieldName;
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
     * Returns the value of the field corresponding with this static
     * field access expression.
     * @param pm the persistence manager of the query
     * @return the field value
     * @exception JDOQueryException if access to the corresponding field of this
     * expression is denied
     */
    public Object getFieldValue(PersistenceManager pm)
    {   JavaField javaField = getFieldInfo();
        return TypeSupport.getFieldValue( getField(), null );
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
     * Returns the model's field object assciated with this instance.
     * If that field object is <code>null</code>,
     * then it is computed by this method.
     * @return the model's field object
     * @exception JDOQueryException if the access to the desired field is denied
     */
    JavaField getFieldInfo()
    {   if( this.javaField==null )
        {   ASTToChildren();
            if( this.children==null ||
                this.children.length<1 )
                return null;
            TypeImpl type = (TypeImpl) this.children[0];
            JavaType classType = (JavaType) type.getTypeInfo();
            if( classType!=null )
                this.javaField = classType.getJavaField( this.fieldName );
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

