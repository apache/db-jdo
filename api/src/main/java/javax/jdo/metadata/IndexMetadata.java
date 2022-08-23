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

/**
 * Represents an index.
 *
 * @since 3.0
 */
public interface IndexMetadata extends Metadata {
  /**
   * Method to set the name of the index.
   *
   * @param name Name of the index
   * @return This metadata object
   */
  IndexMetadata setName(String name);

  /**
   * Accessor for the index name.
   *
   * @return The index name
   */
  String getName();

  /**
   * Method to set the table name.
   *
   * @param table Table name
   * @return This metadata object
   */
  IndexMetadata setTable(String table);

  /**
   * Accessor for the name of the table.
   *
   * @return The name
   */
  String getTable();

  /**
   * Method to set whether it is unique.
   *
   * @param unique Unique?
   * @return This metadata object
   */
  IndexMetadata setUnique(boolean unique);

  /**
   * Accessor for whether unique.
   *
   * @return Unique?
   */
  boolean getUnique();

  /**
   * Accessor for all column(s) defined on the index.
   *
   * @return The column(s)
   */
  ColumnMetadata[] getColumns();

  /**
   * Add a new column for this index.
   *
   * @return The ColumnMetadata
   */
  ColumnMetadata newColumn();

  /**
   * Accessor for the number of columns defined for this index.
   *
   * @return The number of columns
   */
  int getNumberOfColumns();

  /**
   * Accessor for all member(s) defined on the index.
   *
   * @return The fields/properties
   */
  MemberMetadata[] getMembers();

  /**
   * Accessor for the number of fields/properties defined for this index.
   *
   * @return The number of members
   */
  int getNumberOfMembers();

  /**
   * Add a new field for this index.
   *
   * @param name Name of the field
   * @return The FieldMetadata
   */
  FieldMetadata newFieldMetadata(String name);

  /**
   * Add a new property for this index.
   *
   * @param name Name of the property
   * @return The PropertyMetadata
   */
  PropertyMetadata newPropertyMetadata(String name);
}
