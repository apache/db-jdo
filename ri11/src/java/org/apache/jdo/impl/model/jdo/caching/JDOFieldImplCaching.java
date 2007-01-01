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

package org.apache.jdo.impl.model.jdo.caching;

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.jdo.JDOArray;
import org.apache.jdo.model.jdo.JDOCollection;
import org.apache.jdo.model.jdo.JDOMap;
import org.apache.jdo.model.jdo.JDOReference;
import org.apache.jdo.model.jdo.JDORelationship;
import org.apache.jdo.model.jdo.PersistenceModifier;
import org.apache.jdo.impl.model.jdo.JDOFieldImplDynamic;

/**
 * An instance of this class represents the JDO metadata of a managed
 * field of a persistence capable class. This caching implementation
 * caches any calulated value to avoid re-calculating it if it is
 * requested again. 
 * <p>
 * Please note, this implementation does not support
 * changing the relationship property once it is defined (either
 * explicitly by the setter or internally calculated by the
 * getter). The second attempt to define the relationship will result
 * in an exception.
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOFieldImplCaching extends JDOFieldImplDynamic {

    /** Relative field number. */
    private int relativeFieldNumber = -1;

    /**
     * Get the persistence modifier of this JDOField.
     * @return the persistence modifier, one of 
     * {@link PersistenceModifier#NONE}, 
     * {@link PersistenceModifier#PERSISTENT},
     * {@link PersistenceModifier#TRANSACTIONAL}, or
     * {@link PersistenceModifier#POSSIBLY_PERSISTENT}.
     */
    public int getPersistenceModifier() {
        if (persistenceModifier == PersistenceModifier.UNSPECIFIED) {
            persistenceModifier = super.getPersistenceModifier();
        }
        return persistenceModifier;
    }

    /**
     * Returns the relative field number of this JDOField.
     * @return the relative field number
     */
    public int getRelativeFieldNumber() {
        ((JDOClassImplCaching)getDeclaringClass()).calculateFieldNumbers();
        return relativeFieldNumber;
    }

    /**
     * Determines whether this JDOField is part of the default fetch group or 
     * not.
     * @return <code>true</code> if the field is part of the default fetch 
     * group, <code>false</code> otherwise
     */
    public boolean isDefaultFetchGroup() {
        if (defaultFetchGroup == null) {
            defaultFetchGroup = 
                super.isDefaultFetchGroup() ? Boolean.TRUE : Boolean.FALSE;
        }
        return (defaultFetchGroup == null) ? 
            false : defaultFetchGroup.booleanValue();
    }

    /**
     * Determines whether the field should be stored if possible as part of
     * the instance instead of as its own instance in the datastore.
     * @return <code>true</code> if the field is stored as part of the instance;
     * <code>false</code> otherwise
     */
    public boolean isEmbedded() {
        if (embedded == null) {
            embedded = super.isEmbedded() ? Boolean.TRUE : Boolean.FALSE;
        }
        return (embedded == null) ? false : embedded.booleanValue();
    }
    
    /**
     * Get the relationship information for this JDOField. The method 
     * returns null if the field is not part of a relationship 
     * (e.g. it is a primitive type field).
     * @return relationship info of this JDOField or <code>null</code> if 
     * this JDOField is not a relationship
     */
    public JDORelationship getRelationship() {
        if (relationship == null) {
            relationship = super.getRelationship();
        }
        return relationship;
    }
    
    /**
     * Creates and returns a new JDOReference instance. 
     * This method automatically binds the new JDOReference to this JDOField. 
     * It throws a ModelException, if this JDOField is already bound to 
     * another JDORelationship instance. Otherwise the following holds true:
     * <ul>
     * <li> Method {@link #getRelationship} returns the new created instance
     * <li> <code>this.getRelationship().getDeclaringField() == this</code>
     * </ul> 
     * @return a new JDOReference instance bound to this JDOField
     * @exception ModelException if impossible
     */
    public JDOReference createJDOReference() throws ModelException {
        if (relationship != null)
            throw new ModelException(
                msg.msg("EXC_RelationshipAlreadyDefined", //NOI18N
                        getName(), relationship));
        return super.createJDOReference();
    }

    /**
     * Creates and returns a new JDOCollection instance. 
     * This method automatically binds the new JDOCollection to this JDOField. 
     * It throws a ModelException, if this JDOField is already bound to 
     * another JDORelationship instance. Otherwise the following holds true:
     * <ul>
     * <li> Method {@link #getRelationship} returns the new created instance
     * <li> <code>this.getRelationship().getDeclaringField() == this</code>
     * </ul> 
     * @return a new JDOCollection instance bound to this JDOField
     * @exception ModelException if impossible
     */
    public JDOCollection createJDOCollection() throws ModelException {
        if (relationship != null)
            throw new ModelException(
                msg.msg("EXC_RelationshipAlreadyDefined", //NOI18N
                        getName(), relationship));
        return super.createJDOCollection();
    }

    /**
     * Creates and returns a new JDOArray instance. 
     * This method automatically binds the new JDOArray to this JDOField. 
     * It throws a ModelException, if this JDOField is already bound to 
     * another JDORelationship instance. Otherwise the following holds true:
     * <ul>
     * <li> Method {@link #getRelationship} returns the new created instance
     * <li> <code>this.getRelationship().getDeclaringField() == this</code>
     * </ul> 
     * @return a new JDOArray instance bound to this JDOField
     * @exception ModelException if impossible
     */
    public JDOArray createJDOArray() throws ModelException {
        if (relationship != null)
            throw new ModelException(
                msg.msg("EXC_RelationshipAlreadyDefined", //NOI18N
                        getName(), relationship));
        return super.createJDOArray();
    }

    /**
     * Creates and returns a new JDOMap instance. 
     * This method automatically binds the new JDOMap to this JDOField. 
     * It throws a ModelException, if this JDOField is already bound to 
     * another JDORelationship instance. Otherwise the following holds true:
     * <ul>
     * <li> Method {@link #getRelationship} returns the new created instance
     * <li> <code>this.getRelationship().getDeclaringField() == this</code>
     * </ul> 
     * @return a new JDOMap instance bound to this JDOField
     * @exception ModelException if impossible
     */
    public JDOMap createJDOMap() throws ModelException {
        if (relationship != null)
            throw new ModelException(
                msg.msg("EXC_RelationshipAlreadyDefined", //NOI18N
                        getName(), relationship));
        return super.createJDOMap();
    }

    //========= Internal helper methods ==========

    /**
     * Creates and returns a new JDOCollection instance. 
     * This method automatically this JDOField as the declarinmg field of 
     * the returned instance.
     * @return a new JDOCollection instance bound to this JDOField
     */
    protected JDOCollection createJDOCollectionInternal() {
        JDOCollectionImplCaching collection = new JDOCollectionImplCaching();
        // update relationship JDORelationship->JDOField
        collection.setDeclaringField(this);
        return collection;
    }

    /**
     * Creates and returns a new JDOArray instance. 
     * This method automatically this JDOField as the declarinmg field of 
     * the returned instance.
     * @return a new JDOArray instance bound to this JDOField
     */
    protected JDOArray createJDOArrayInternal() {
        JDOArrayImplCaching array = new JDOArrayImplCaching();
        // update relationship JDORelationship->JDOField
        array.setDeclaringField(this);
        return array;
    }

    /**
     * Creates and returns a new JDOMap instance. 
     * This method automatically this JDOField as the declarinmg field of 
     * the returned instance.
     * @return a new JDOMap instance bound to this JDOField
     */
    protected JDOMap createJDOMapInternal() {
        JDOMapImplCaching map = new JDOMapImplCaching();
        // update relationship JDORelationship->JDOField
        map.setDeclaringField(this);
        return map;
    }

    /**
     * Sets the relative field number of this JDOField.
     * @param number the relative field number
     */
    void setRelativeFieldNumber(int number) {
        this.relativeFieldNumber = number;
    }
}
