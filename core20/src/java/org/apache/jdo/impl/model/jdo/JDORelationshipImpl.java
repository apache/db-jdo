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

package org.apache.jdo.impl.model.jdo;

import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDORelationship;

/**
 * JDORelationship is the super interface for all interfaces representing 
 * JDO relationship metadata of a managed field of a persistence capable class.
 * 
 * @author Michael Bouschen
 */
public abstract class JDORelationshipImpl extends JDOElementImpl
    implements JDORelationship {
    
    /** Property lowerBound. No default. */
    private int lowerBound;

    /** Property upperBound. No default. */
    private int upperBound;

    /** Relationship JDOField<->JDORelationship. */
    private JDOField declaringField;

    /** Relationship JDORelationship<->JDORelationship. */
    private JDORelationship inverse;

    /** 
     * Get the lower cardinality bound for this relationship element.
     * @return the lower cardinality bound
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /** 
     * Set the lower cardinality bound for this relationship element.
     * @param lowerBound an integer indicating the lower cardinality bound
     */
    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    /** 
     * Get the upper cardinality bound for this relationship element.
     * @return the upper cardinality bound
     */
    public int getUpperBound() {
        return upperBound;
    }

    /** 
     * Set the upper cardinality bound for this relationship element.
     * @param upperBound an integer indicating the upper cardinality bound
     */
    public void setUpperBound(int upperBound)
    {
        this.upperBound = upperBound;
    }

    /** 
     * Get the declaring field of this JDORelationship.
     * @return the field that owns this JDORelationship, or <code>null</code>
     * if the element is not attached to any field
     */
    public JDOField getDeclaringField() {
        return declaringField;
    }

    /** 
     * Set the declaring field of this JDORelationship.
     * @param declaringField the declaring field of this relationship element
     */
    public void setDeclaringField(JDOField declaringField) {
        this.declaringField = declaringField;
    }
    
    /**
     * Get the inverse JDORelationship in the case of a managed relationship.
     * @return the inverse relationship
     */
    public JDORelationship getInverseRelationship() {
        return inverse;
    }

    /**
     * Set the inverse JDORelationship in the case of a managed relationship.
     * @param inverseRelationship the inverse relationship
     */
    public void setInverseRelationship(JDORelationship inverseRelationship) {
        this.inverse = inverseRelationship;
    }

    /**
     * Determines whether this JDORelationship represents a reference
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOReference instance.
     * @return <code>true</code> if this JDORelationship represents a
     * reference relationship; <code>false</code> otherwise.
     */
    public boolean isJDOReference() {
        return false;
    }
    
    /**
     * Determines whether this JDORelationship represents a collection
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOCollection instance.
     * @return <code>true</code> if this JDORelationship represents a
     * collection relationship; <code>false</code> otherwise.
     */
    public boolean isJDOCollection() {
        return false;
    }

    /**
     * Determines whether this JDORelationship represents an array
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOArray instance.
     * @return <code>true</code> if this JDORelationship represents an 
     * array relationship; <code>false</code> otherwise.
     */
    public boolean isJDOArray() {
        return false;
    }

    /**
     * Determines whether this JDORelationship represents a map 
     * relationship or not. A return of <code>true</code> means this
     * JDORelationship is a JDOMap instance.
     * @return <code>true</code> if this JDORelationship represents a
     * map relationship; <code>false</code> otherwise.
     */
    public boolean isJDOMap() {
        return false;
    }

}
