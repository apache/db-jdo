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

import javax.jdo.annotations.IdGeneratorStrategy;

/**
 * Represents the datastore identity of a class.
 * @since 3.0
 */
public interface DatastoreIdentityMetadata extends Metadata {
    /**
     * Method to set the datastore identity column name.
     * 
     * @param column Name of the datastore identity column
     */
    DatastoreIdentityMetadata setColumn(String column);

    /**
     * Accessor for the datastore identity column name
     * 
     * @return The column name
     */
    String getColumn();

    /**
     * Method to set the identity generation strategy.
     * 
     * @param strategy The strategy
     */
    DatastoreIdentityMetadata setStrategy(IdGeneratorStrategy strategy);

    /**
     * Accessor for the identity generation strategy.
     * 
     * @return The strategy
     */
    IdGeneratorStrategy getStrategy();

    /**
     * Method to set the custom identity generation strategy.
     * 
     * @param strategy The strategy
     */
    DatastoreIdentityMetadata setCustomStrategy(String strategy);

    /**
     * Accessor for the custom strategy (overriding "strategy").
     * 
     * @return The strategy
     */
    String getCustomStrategy();

    /**
     * Method to set the sequence key (when using "sequence" strategy)
     * 
     * @param seq Sequence key
     */
    DatastoreIdentityMetadata setSequence(String seq);

    /**
     * Accessor for the sequence key (when using "sequence" strategy)
     * 
     * @return The sequence
     */
    String getSequence();

    /**
     * Accessor for all column(s) defined on the datastore identity.
     * 
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a new column for this datastore identity.
     * 
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this datastore identity.
     * 
     * @return The number of columns
     */
    int getNumberOfColumns();
}
