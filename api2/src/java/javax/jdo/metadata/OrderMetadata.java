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

/**
 * Represents ordering of a collection field/property.
 * @since 2.3
 */
public interface OrderMetadata extends Metadata {
    /**
     * Method to set the version column name.
     * 
     * @param column Name of the version clumn
     */
    OrderMetadata setColumn(String column);

    /**
     * Accessor for the version column name
     * 
     * @return The version column name
     */
    String getColumn();

    /**
     * Method to set mapped-by information whether the order is present in the element class.
     * 
     * @param mappedBy Field/property name in which to store the ordering in the element
     */
    OrderMetadata setMappedBy(String mappedBy);

    /**
     * Accessor for the mapped-by field/property name in the element class.
     * 
     * @return Name of field/property in element class
     */
    String getMappedBy();

    /**
     * Accessor for all column(s) defined on the ordering.
     * 
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a column for this ordering.
     * 
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this ordering.
     * 
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Method to set index metadata for the ordering
     * 
     * @return The metadata for any index
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata for the ordering
     * 
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();
}