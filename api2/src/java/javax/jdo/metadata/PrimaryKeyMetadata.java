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
 * Represents the primary key definition of a class.
 * @since 2.3
 */
public interface PrimaryKeyMetadata extends Metadata {
    /**
     * Method to set the name of the PK constraint.
     * 
     * @param name Name of the PK constraint
     */
    void setName(String strategy);

    /**
     * Accessor for the name of the PK constraint.
     * 
     * @return The PK constraint name
     */
    String getName();

    /**
     * Method to set the PK column name.
     * 
     * @param column Name of the PK column
     */
    void setColumn(String column);

    /**
     * Accessor for the PK column name
     * 
     * @return The column name
     */
    String getColumn();

    /**
     * Accessor for all column(s) defined on the PK.
     * 
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this PK
     * 
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this PK
     * 
     * @return The number of columns
     */
    int getNumberOfColumns();
}