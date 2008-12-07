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

import javax.jdo.annotations.ForeignKeyAction;

/**
 * Represents a value in a map.
 * @since 2.3
 */
public interface ValueMetadata extends Metadata {
    /**
     * Method to set the column name.
     * 
     * @param column Column name
     */
    void setColumn(String column);

    /**
     * Accessor for the name of the column.
     * 
     * @return The name
     */
    String getColumn();

    /**
     * Method to set the table name.
     * 
     * @param table Table name
     */
    void setTable(String table);

    /**
     * Accessor for the name of the table.
     * 
     * @return The name
     */
    String getTable();

    /**
     * Method to set the delete action of the FK
     * 
     * @param action Delete action of the FK
     */
    void setDeleteAction(ForeignKeyAction action);

    /**
     * Accessor for the delete action of the FK
     * 
     * @return The FK delete-action
     */
    ForeignKeyAction getDeleteAction();

    /**
     * Method to set the update action of the FK
     * 
     * @param action Update action of the FK
     */
    void setUpdateAction(ForeignKeyAction action);

    /**
     * Accessor for the update action of the FK
     * 
     * @return The FK update-action
     */
    ForeignKeyAction getUpdateAction();

    /**
     * Method to set new embedded metadata for the value.
     * 
     * @return The EmbeddedMetadata
     */
    EmbeddedMetadata setEmbeddedMetadata();

    /**
     * Accessor for any embedded metadata on this value
     * 
     * @return The EmbeddedMetadata
     */
    EmbeddedMetadata getEmbeddedMetadata();

    /**
     * Method to set new index metadata for the value.
     * 
     * @return The IndexMetadata
     */
    IndexMetadata setIndexMetadata();

    /**
     * Accessor for any index metadata on this value
     * 
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();

    /**
     * Method to set new unique constraint metadata for the value
     * 
     * @return The UniqueMetadata
     */
    UniqueMetadata setUniqueMetadata();

    /**
     * Accessor for any unique constraint metadata on this value.
     * 
     * @return The UniqueMetadata
     */
    UniqueMetadata getUniqueMetadata();

    /**
     * Method to set new foreign key metadata for the value
     * 
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata setForeignKeyMetadata();

    /**
     * Accessor for any foreign key metadata on this value.
     * 
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata getForeignKeyMetadata();
}