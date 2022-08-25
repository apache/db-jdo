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

import javax.jdo.AttributeConverter;
import javax.jdo.annotations.ForeignKeyAction;

/**
 * Represents a value in a map.
 *
 * @since 3.0
 */
public interface ValueMetadata extends Metadata {
  /**
   * Method to set the column name.
   *
   * @param column Column name
   * @return This metadata object
   */
  ValueMetadata setColumn(String column);

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
   * @return This metadata object
   */
  ValueMetadata setTable(String table);

  /**
   * Accessor for the name of the table.
   *
   * @return The name
   */
  String getTable();

  /**
   * Method to set the delete action of the FK.
   *
   * @param action Delete action of the FK
   * @return This metadata object
   */
  ValueMetadata setDeleteAction(ForeignKeyAction action);

  /**
   * Accessor for the delete action of the FK.
   *
   * @return The FK delete-action
   */
  ForeignKeyAction getDeleteAction();

  /**
   * Method to set the update action of the FK.
   *
   * @param action Update action of the FK
   * @return This metadata object
   */
  ValueMetadata setUpdateAction(ForeignKeyAction action);

  /**
   * Accessor for the update action of the FK.
   *
   * @return The FK update-action
   */
  ForeignKeyAction getUpdateAction();

  /**
   * Accessor for all column(s) defined on the value.
   *
   * @return The column(s)
   */
  ColumnMetadata[] getColumns();

  /**
   * Add a new column for this value.
   *
   * @return The ColumnMetadata
   */
  ColumnMetadata newColumnMetadata();

  /**
   * Method to set new embedded metadata for the value.
   *
   * @return The EmbeddedMetadata
   */
  EmbeddedMetadata newEmbeddedMetadata();

  /**
   * Accessor for any embedded metadata on this value.
   *
   * @return The EmbeddedMetadata
   */
  EmbeddedMetadata getEmbeddedMetadata();

  /**
   * Method to set new index metadata for the value.
   *
   * @return The IndexMetadata
   */
  IndexMetadata newIndexMetadata();

  /**
   * Accessor for any index metadata on this value.
   *
   * @return Index metadata
   */
  IndexMetadata getIndexMetadata();

  /**
   * Method to set new unique constraint metadata for the value.
   *
   * @return The UniqueMetadata
   */
  UniqueMetadata newUniqueMetadata();

  /**
   * Accessor for any unique constraint metadata on this value.
   *
   * @return The UniqueMetadata
   */
  UniqueMetadata getUniqueMetadata();

  /**
   * Method to set new foreign key metadata for the value.
   *
   * @return The ForeignKeyMetadata
   */
  ForeignKeyMetadata newForeignKeyMetadata();

  /**
   * Accessor for any foreign key metadata on this value.
   *
   * @return The ForeignKeyMetadata
   */
  ForeignKeyMetadata getForeignKeyMetadata();

  /**
   * Accessor for the attribute converter for this value (if any).
   *
   * @return The converter
   */
  AttributeConverter<?, ?> getConverter();

  /**
   * Method to set the attribute converter to use for this value.
   *
   * @param conv Converter
   * @return This value metadata
   */
  ValueMetadata setConverter(AttributeConverter<?, ?> conv);

  /**
   * Accessor for whether any AttributeConverter for this value type is explicitly disabled and
   * should use the implementation default.
   *
   * @return Whether it is disabled
   */
  Boolean getUseDefaultConversion();

  /**
   * Method to explicitly set whether to disable use of AttributeConverter for this type (either
   * here, class-level or PMF level).
   *
   * @param flag Whether to disable
   * @return This metadata
   */
  ValueMetadata setUseDefaultConversion(Boolean flag);
}
