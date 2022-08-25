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

import javax.jdo.annotations.VersionStrategy;

/**
 * Represents versioning of a class.
 * @since 3.0
 */
public interface VersionMetadata extends Metadata {
    /**
     * Method to set the version strategy.
     * @param strategy The strategy
     * @return This metadata object
     */
    VersionMetadata setStrategy(VersionStrategy strategy);

    /**
     * Accessor for the version strategy.
     * @return The strategy
     */
    VersionStrategy getStrategy();

    /**
     * Method to set the version column name.
     * @param column Name of the version column
     * @return This metadata object
     */
    VersionMetadata setColumn(String column);

    /**
     * Accessor for the version column name
     * @return The version column name
     */
    String getColumn();

    /**
     * Method to set whether indexed.
     * @param indexed Whether indexed (true | false | unique)
     * @return This metadata object
     */
    VersionMetadata setIndexed(Indexed indexed);

    /**
     * Accessor for whether indexed (true|false|unique).
     * @return Indexed?
     */
    Indexed getIndexed();

    /**
     * Accessor for all column(s) defined on the version.
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this version.
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this version.
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Method to set index metadata for the version.
     * @return The IndexMetadata
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata on this version.
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();
}
