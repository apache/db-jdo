/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.jdo.metadata;

import javax.jdo.annotations.ForeignKeyAction;

/**
 * Represents a FK constraint in an ORM context.
 * @since 3.0
 */
public interface ForeignKeyMetadata extends Metadata {
    /**
     * Method to set the name of the constraint
     * @param name Name of the constraint
     * @return This metadata object
     */
    ForeignKeyMetadata setName(String name);

    /**
     * Accessor for the constraint name.
     * @return The constraint name
     */
    String getName();

    /**
     * Method to set the table name.
     * @param table Table name
     * @return This metadata object
     */
    ForeignKeyMetadata setTable(String table);

    /**
     * Accessor for the name of the table.
     * @return The name
     */
    String getTable();

    /**
     * Method to set whether it is unique.
     * @param unique Unique?
     * @return This metadata object
     */
    ForeignKeyMetadata setUnique(boolean unique);

    /**
     * Accessor for whether unique.
     * @return Unique?
     */
    Boolean getUnique();

    /**
     * Method to set whether it is deferred.
     * @param def Deferred?
     * @return This metadata object
     */
    ForeignKeyMetadata setDeferred(boolean def);

    /**
     * Accessor for whether the constraint can be deferred.
     * @return Deferred?
     */
    Boolean getDeferred();

    /**
     * Method to set the delete action of the FK
     * @param action Delete action of the FK
     * @return This metadata object
     */
    ForeignKeyMetadata setDeleteAction(ForeignKeyAction action);

    /**
     * Accessor for the delete action of the FK.
     * @return The FK delete-action
     */
    ForeignKeyAction getDeleteAction();

    /**
     * Method to set the update action of the FK.
     * @param action Update action of the FK
     * @return This metadata object
     */
    ForeignKeyMetadata setUpdateAction(ForeignKeyAction action);

    /**
     * Accessor for the update action of the FK.
     * @return The FK update-action
     */
    ForeignKeyAction getUpdateAction();

    /**
     * Accessor for all column(s) defined on the FK.
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this FK.
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this FK.
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Accessor for all fields/properties defined on the FK.
     * @return The members
     */
    MemberMetadata[] getMembers();

    /**
     * Accessor for the number of fields/properties defined for this FK.
     * @return The number of members
     */
    int getNumberOfMembers();

    /**
     * Add a new field for this FK.
     * 
     * @param name Name of the field
     * @return The FieldMetadata
     */
    FieldMetadata newFieldMetadata(String name);

    /**
     * Add a new property for this FK.
     * 
     * @param name Name of the property
     * @return The PropertyMetadata
     */
    PropertyMetadata newPropertyMetadata(String name);
}
