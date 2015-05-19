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
package javax.jdo.metadata;

import javax.jdo.AttributeConverter;
import javax.jdo.annotations.ForeignKeyAction;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceModifier;

/**
 * Represents a field/property in a class/persistent-interface.
 * @since 3.0
 */
public interface MemberMetadata extends Metadata {
    /**
     * Method to set the name.
     * @param name name
     * @return This metadata object
     */
    MemberMetadata setName(String name);

    /**
     * Accessor for the name of the field/property.
     * @return The name
     */
    String getName();

    /**
     * Method to set the table name.
     * @param table Table name
     * @return This metadata object
     */
    MemberMetadata setTable(String table);

    /**
     * Accessor for the name of the table.
     * @return The name
     */
    String getTable();

    /**
     * Method to set the column name.
     * @param col Column name
     * @return This metadata object
     */
    MemberMetadata setColumn(String col);

    /**
     * Accessor for the name of the column.
     * @return The column name
     */
    String getColumn();

    /**
     * Method to set the field type(s). For defining where we want to restrict
     * what type is stored in a field
     * @param type Type of field
     * @return This metadata object
     */
    MemberMetadata setFieldType(String type);

    /**
     * Accessor for the type storable in the field.
     * @return The field type
     */
    String getFieldType();

    /**
     * Method to set the delete action of the FK
     * @param action Delete action of the FK
     * @return This metadata object
     */
    MemberMetadata setDeleteAction(ForeignKeyAction action);

    /**
     * Accessor for the delete action of the FK.
     * @return The FK delete-action
     */
    ForeignKeyAction getDeleteAction();

    /**
     * Method to set the persistence-modifier of the field/property.
     * @param mod persistence modifier
     * @return This metadata object
     */
    MemberMetadata setPersistenceModifier(PersistenceModifier mod);

    /**
     * Accessor for the persistence modifier of the field/property.
     * @return The persistence modifier
     */
    PersistenceModifier getPersistenceModifier();

    /**
     * Method to set the behaviour of a null value
     * @param val Null value behaviour
     * @return This metadata object
     */
    MemberMetadata setNullValue(NullValue val);

    /**
     * Accessor for the behaviour of a null value
     * @return The null value behaviour
     */
    NullValue getNullValue();

    /**
     * Method to set whether it is in the DFG.
     * @param dfg DFG?
     * @return This metadata object
     */
    MemberMetadata setDefaultFetchGroup(boolean dfg);

    /**
     * Accessor for whether part of the DFG.
     * @return dfg?
     */
    Boolean getDefaultFetchGroup();

    /**
     * Method to set whether it is unique.
     * @param unique Unique?
     * @return This metadata object
     */
    MemberMetadata setDependent(boolean unique);

    /**
     * Accessor for whether unique.
     * @return Unique?
     */
    Boolean getDependent();

    /**
     * Method to set whether it is embedded.
     * @param emb Embedded?
     * @return This metadata object
     */
    MemberMetadata setEmbedded(boolean emb);

    /**
     * Accessor for whether it is embedded.
     * @return embedded?
     */
    Boolean getEmbedded();

    /**
     * Method to set whether it is serialized.
     * @param ser serialized?
     * @return This metadata object
     */
    MemberMetadata setSerialized(boolean ser);

    /**
     * Accessor for whether it is serialized.
     * @return serialized?
     */
    Boolean getSerialized();

    /**
     * Method to set whether it is part of the pk
     * @param pk PK?
     * @return This metadata object
     */
    MemberMetadata setPrimaryKey(boolean pk);

    /**
     * Accessor for whether it is part of the pk.
     * @return pk?
     */
    boolean getPrimaryKey();

    /**
     * Method to set whether it is indexed.
     * @param index Indexed?
     * @return This metadata object
     */
    MemberMetadata setIndexed(boolean index);

    /**
     * Accessor for whether it is indexed.
     * @return Indexed?
     */
    Boolean getIndexed();

    /**
     * Method to set whether it is unique.
     * @param unique Unique?
     * @return This metadata object
     */
    MemberMetadata setUnique(boolean unique);

    /**
     * Accessor for whether unique.
     * @return Unique?
     */
    Boolean getUnique();

    /**
     * Method to set whether this is cacheable
     * @param cacheable Cacheable?
     * @return This metadata object
     */
    MemberMetadata setCacheable(boolean cacheable);

    /**
     * Accessor for whether this is cacheable.
     * @return Detachable?
     */
    boolean getCacheable();

    /**
     * Method to set the recursion depth (when used in a fetch group).
     * @param depth Recursion depth
     * @return This metadata object
     */
    MemberMetadata setRecursionDepth(int depth);

    /**
     * Accessor for the recursion depth (when part of a fetch group).
     * @return Recursion depth?
     */
    int getRecursionDepth();

    /**
     * Method to set the load fetch group.
     * @param grp Load fetch group
     * @return This metadata object
     */
    MemberMetadata setLoadFetchGroup(String grp);

    /**
     * Accessor for the name of the load fetch group
     * @return The load fetch group
     */
    String getLoadFetchGroup();

    /**
     * Method to set the value strategy
     * @param str Value strategy
     * @return This metadata object
     */
    MemberMetadata setValueStrategy(IdGeneratorStrategy str);

    /**
     * Accessor for the value strategy
     * @return Value strategy
     */
    IdGeneratorStrategy getValueStrategy();

    /**
     * Method to set the custom identity generation strategy.
     * @param strategy The strategy
     * @return This metadata object
     */
    MemberMetadata setCustomStrategy(String strategy);

    /**
     * Accessor for the custom strategy (overriding "strategy").
     * @return The strategy
     */
    String getCustomStrategy();

    /**
     * Method to set the sequence (when using value-strategy of "sequence")
     * @param seq Sequence key
     * @return This metadata object
     */
    MemberMetadata setSequence(String seq);

    /**
     * Accessor for the sequence (when using value-strategy of "sequence")
     * @return Sequence key
     */
    String getSequence();

    /**
     * Method to set the field on the other side of a bidirectional relation
     * (this side is owner).
     * @param map  mapped-by field/property
     * @return This metadata object
     */
    MemberMetadata setMappedBy(String map);

    /**
     * Accessor for the mapped-by field/property
     * @return mapped-by field/property
     */
    String getMappedBy();

    /**
     * Method to define the array details (if the field/property is an array)
     * @return The ArrayMetadata
     */
    ArrayMetadata newArrayMetadata();

    /**
     * Accessor for the array details.
     * @return array details
     */
    ArrayMetadata getArrayMetadata();

    /**
     * Method to define the array details (if the field/property is an array).
     * @return The ArrayMetadata
     */
    CollectionMetadata newCollectionMetadata();

    /**
     * Accessor for the array details.
     * @return array details
     */
    CollectionMetadata getCollectionMetadata();

    /**
     * Method to define the map details (if the field/property is an map).
     * @return The MapMetadata
     */
    MapMetadata newMapMetadata();

    /**
     * Accessor for the map details.
     * @return map details
     */
    MapMetadata getMapMetadata();

    /**
     * Method to define the join details.
     * @return The JoinMetadata
     */
    JoinMetadata newJoinMetadata();

    /**
     * Accessor for the join details.
     * @return join details
     */
    JoinMetadata getJoinMetadata();

    /**
     * Method to define the embedded details.
     * @return The EmbeddedMetadata
     */
    EmbeddedMetadata newEmbeddedMetadata();

    /**
     * Accessor for the embedded metadata.
     * @return embedded metadata
     */
    EmbeddedMetadata getEmbeddedMetadata();

    /**
     * Method to define the new element details.
     * @return The ElementMetadata
     */
    ElementMetadata newElementMetadata();

    /**
     * Accessor for the element details.
     * @return element details
     */
    ElementMetadata getElementMetadata();

    /**
     * Method to define the key details.
     * @return The KeyMetadata
     */
    KeyMetadata newKeyMetadata();

    /**
     * Accessor for the key details.
     * @return key details
     */
    KeyMetadata getKeyMetadata();

    /**
     * Method to define the value details.
     * @return The ValueMetadata
     */
    ValueMetadata newValueMetadata();

    /**
     * Accessor for the value details.
     * @return value details
     */
    ValueMetadata getValueMetadata();

    /**
     * Method to set index metadata for the field/property.
     * @return The metadata for any index
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata for the field/property.
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();

    /**
     * Method to set new unique constraint metadata for the field/property.
     * @return The UniqueMetadata
     */
    UniqueMetadata newUniqueMetadata();

    /**
     * Accessor for any unique constraint metadata on this field/property.
     * @return The UniqueMetadata
     */
    UniqueMetadata getUniqueMetadata();

    /**
     * Method to set new foreign key metadata for the field/property.
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata newForeignKeyMetadata();

    /**
     * Accessor for any foreign key metadata on this field/property.
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata getForeignKeyMetadata();

    /**
     * Method to define the order details.
     * @return The OrdeMetadata
     */
    OrderMetadata newOrderMetadata();

    /**
     * Accessor for the order metadata.
     * @return order metadata
     */
    OrderMetadata getOrderMetadata();

    /**
     * Accessor for all column(s) defined on the join.
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this join.
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this join.
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Accessor for the attribute converter for this member (if any).
     * @return The converter
     */
    AttributeConverter<?, ?> getConverter();

    /**
     * Method to set the attribute converter to use for this member.
     * @param conv Converter
     * @return This metadata
     */
    MemberMetadata setConverter(AttributeConverter<?, ?> conv);

    /**
     * Accessor for whether the PMF defined converter for this member type is disabled.
     * @return Whether it is disabled
     */
    boolean getDisableConverter();

    /**
     * Method to enable/disable the PMF defined converter for this member (if present).
     * If a converter is defined on this metadata element then this flag will be ignored.
     * @param disable Whether to disable
     * @return This metadata
     */
    MemberMetadata setDisableConverter(boolean disable);
}
