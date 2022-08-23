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
 * Represents a unique constraint.
 *
 * @since 3.0
 */
public interface UniqueMetadata extends Metadata {
  /**
   * Method to set the name of the constraint.
   *
   * @param name Name of the constraint
   * @return This metadata object
   */
  UniqueMetadata setName(String name);

  /**
   * Accessor for the constraint name.
   *
   * @return The constraint name
   */
  String getName();

  /**
   * Method to set the table name.
   *
   * @param table Table name
   * @return This metadata object
   */
  UniqueMetadata setTable(String table);

  /**
   * Accessor for the name of the table.
   *
   * @return The name
   */
  String getTable();

  /**
   * Method to set whether the constraint is deferred.
   *
   * @param def Deferred?
   * @return This metadata object
   */
  UniqueMetadata setDeferred(boolean def);

  /**
   * Accessor for whether deferred.
   *
   * @return Deferred?
   */
  Boolean getDeferred();

  /**
   * Accessor for all column(s) defined on the unique constraint.
   *
   * @return The column(s)
   */
  ColumnMetadata[] getColumns();

  /**
   * Add a new column for this unique constraint.
   *
   * @return The ColumnMetadata
   */
  ColumnMetadata newColumnMetadata();

  /**
   * Accessor for the number of columns defined for this unique constraint.
   *
   * @return The number of columns
   */
  int getNumberOfColumns();

  /**
   * Accessor for all fields/properties defined on the unique constraint.
   *
   * @return The members
   */
  MemberMetadata[] getMembers();

  /**
   * Accessor for the number of fields/properties defined for this unique constraint.
   *
   * @return The number of members
   */
  int getNumberOfMembers();

  /**
   * Add a new field for this unique constraint.
   *
   * @param name Name of the field
   * @return The FieldMetadata
   */
  FieldMetadata newFieldMetadata(String name);

  /**
   * Add a new property for this unique constraint.
   *
   * @param name Name of the property
   * @return The PropertyMetadata
   */
  PropertyMetadata newPropertyMetadata(String name);
}
