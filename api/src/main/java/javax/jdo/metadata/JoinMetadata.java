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
 * Represents join information.
 * @since 3.0
 */
public interface JoinMetadata extends Metadata {
    /**
     * Method to set the join column.
     * @param column Name of the join column
     * @return This metadata object
     */
    JoinMetadata setColumn(String column);

    /**
     * Accessor for the join column name.
     * @return The column name
     */
    String getColumn();

    /**
     * Method to set the table name.
     * @param table Table name
     * @return This metadata object
     */
    JoinMetadata setTable(String table);

    /**
     * Accessor for the name of the table.
     * @return The name
     */
    String getTable();

    /**
     * Method to set whether to use an outer join.
     * @param outer Outer join?
     * @return This metadata object
     */
    JoinMetadata setOuter(boolean outer);

    /**
     * Accessor for whether to use an outer join.
     * @return Outer join?
     */
    boolean getOuter();

    /**
     * Method to set the delete action of the FK
     * @param action Delete action of the FK
     * @return This metadata object
     */
    JoinMetadata setDeleteAction(ForeignKeyAction action);

    /**
     * Accessor for the delete action of the FK.
     * @return The FK delete-action
     */
    ForeignKeyAction getDeleteAction();

    /**
     * Method to set whether indexed.
     * @param indexed Whether indexed (true | false | unique)
     * @return This metadata object
     */
    JoinMetadata setIndexed(Indexed indexed);

    /**
     * Accessor for whether indexed (true|false|unique).
     * @return Indexed?
     */
    Indexed getIndexed();

    /**
     * Method to set whether it is unique.
     * @param unique Unique?
     * @return This metadata object
     */
    JoinMetadata setUnique(boolean unique);

    /**
     * Accessor for whether unique.
     * @return Unique?
     */
    Boolean getUnique();

    /**
     * Method to set new index metadata for the join.
     * @return The IndexMetadata
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata on this join.
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();

    /**
     * Method to set new unique constraint metadata for the join.
     * @return The UniqueMetadata
     */
    UniqueMetadata newUniqueMetadata();

    /**
     * Accessor for any unique constraint metadata on this join.
     * @return The UniqueMetadata
     */
    UniqueMetadata getUniqueMetadata();

    /**
     * Method to set new foreign key metadata for the join.
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata newForeignKeyMetadata();

    /**
     * Accessor for any foreign key metadata on this join.
     * 
     * @return The ForeignKeyMetadata
     */
    ForeignKeyMetadata getForeignKeyMetadata();

    /**
     * Method to set new primary key metadata for the join.
     * @return The PrimaryKeyMetadata
     */
    PrimaryKeyMetadata newPrimaryKeyMetadata();

    /**
     * Accessor for any primary key metadata on this join.
     * @return The PrimaryKeyMetadata
     */
    PrimaryKeyMetadata getPrimaryKeyMetadata();

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
}
