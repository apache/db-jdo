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

package org.apache.jdo.model.jdo;

import org.apache.jdo.model.ModelException;

/**
 * JDORelationship is the super interface for all interfaces representing 
 * JDO relationship metadata of a managed field of a persistence-capable class.
 * 
 * @author Michael Bouschen
 */
public interface JDORelationship 
    extends JDOElement 
{
    /**
     * Constant representing the cardinality zero used for lower and upper 
     * bounds.
     */
    public static final int CARDINALITY_ZERO = 0;

    /**
     * Constant representing the cardinality one used for lower and upper bounds.
     */
    public static final int CARDINALITY_ONE = 1;

    /**
     * Constant representing the cardinality n used for lower and upper bounds.
     */
    public static final int CARDINALITY_N = java.lang.Integer.MAX_VALUE;

    /** 
     * Get the lower cardinality bound for this relationship element.
     * @return the lower cardinality bound
     */
    public int getLowerBound();

    /** 
     * Set the lower cardinality bound for this relationship element.
     * @param lowerBound an integer indicating the lower cardinality bound
     * @exception ModelException if impossible
     */
    public void setLowerBound(int lowerBound)
        throws ModelException;
    
    /** 
     * Get the upper cardinality bound for this relationship element.
     * @return the upper cardinality bound
     */
    public int getUpperBound();

    /** 
     * Set the upper cardinality bound for this relationship element.
     * @param upperBound an integer indicating the upper cardinality bound
     * @exception ModelException if impossible
     */
    public void setUpperBound(int upperBound)
        throws ModelException;

    /** 
     * Get the declaring field of this JDORelationship.
     * @return the field that owns this JDORelationship, or <code>null</code>
     * if the element is not attached to any field
     */
    public JDOField getDeclaringField();

    /** 
     * Set the declaring field of this JDORelationship.
     * @param declaringField the declaring field of this relationship element
     * @exception ModelException if impossible
     */
    public void setDeclaringField(JDOField declaringField)
        throws ModelException;

    /**
     * Get the inverse JDORelationship in the case of a managed relationship.
     * @return the inverse relationship
     */
    public JDORelationship getInverseRelationship();

    /**
     * Set the inverse JDORelationship in the case of a managed relationship.
     * @param inverseRelationship the inverse relationship
     * @exception ModelException if impossible
     */
    public void setInverseRelationship(JDORelationship inverseRelationship)
        throws ModelException;

    /**
     * Determines whether this JDORelationship represents a reference
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOReference instance.
     * @return <code>true</code> if this JDORelationship represents a
     * reference relationship; <code>false</code> otherwise.
     */
    public boolean isJDOReference();
    
    /**
     * Determines whether this JDORelationship represents a collection
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOCollection instance.
     * @return <code>true</code> if this JDORelationship represents a
     * collection relationship; <code>false</code> otherwise.
     */
    public boolean isJDOCollection();

    /**
     * Determines whether this JDORelationship represents an array
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOArray instance.
     * @return <code>true</code> if this JDORelationship represents an 
     * array relationship; <code>false</code> otherwise.
     */
    public boolean isJDOArray();

    /**
     * Determines whether this JDORelationship represents a map 
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOMap instance.
     * @return <code>true</code> if this JDORelationship represents a
     * map relationship; <code>false</code> otherwise.
     */
    public boolean isJDOMap();

}
