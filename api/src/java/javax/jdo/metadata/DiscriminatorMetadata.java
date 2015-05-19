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

import javax.jdo.annotations.DiscriminatorStrategy;

/**
 * Represents the discriminator for inheritance purposes for this class.
 * @since 3.0
 */
public interface DiscriminatorMetadata extends Metadata {
    /**
     * Method to set the discriminator column.
     * @param column Name of the discriminator column
     * @return This metadata object
     */
    DiscriminatorMetadata setColumn(String column);

    /**
     * Accessor for the discriminator column name
     * @return The column name
     */
    String getColumn();

    /**
     * Method to set the discriminator value (when using "value-map" strategy).
     * @param val Value for the discriminator for this class
     * @return This metadata object
     */
    DiscriminatorMetadata setValue(String val);

    /**
     * Accessor for the discriminator value (when using "value-map" strategy).
     * @return The value
     */
    String getValue();

    /**
     * Method to set the discriminator strategy.
     * @param strategy The strategy
     * @return This metadata object
     */
    DiscriminatorMetadata setStrategy(DiscriminatorStrategy strategy);

    /**
     * Accessor for the discriminator strategy.
     * @return The strategy
     */
    DiscriminatorStrategy getStrategy();

    /**
     * Method to set whether indexed.
     * @param indexed Whether indexed (true | false | unique)
     * @return This metadata object
     */
    DiscriminatorMetadata setIndexed(Indexed indexed);

    /**
     * Accessor for whether indexed (true|false|unique)
     * @return Indexed?
     */
    Indexed getIndexed();

    /**
     * Accessor for all column(s) defined on the discriminator.
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this discriminator.
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this discriminator.
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Method to set the index metadata for the discriminator.
     * @return The IndexMetadata
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata for the discriminator.
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();
}
