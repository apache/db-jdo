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
 * Represents a fetch plan for a class.
 * @since 3.0
 */
public interface FetchPlanMetadata extends Metadata {
    /**
     * Accessor for the fetch plan name (set on construction).
     * @return The fetch plan name
     */
    String getName();

    /**
     * Method to set the max fetch depth for this plan.
     * @param depth The max fetch depth
     * @return This metadata object
     */
    FetchPlanMetadata setMaxFetchDepth(int depth);

    /**
     * Accessor for the max fetch depth.
     * @return The max fetch depth
     */
    int getMaxFetchDepth();

    /**
     * Method to set the fetch size.
     * @param size The fetch size
     * @return This metadata object
     */
    FetchPlanMetadata setFetchSize(int size);

    /**
     * Accessor for the max fetch depth.
     * @return The max fetch depth
     */
    int getFetchSize();

    /**
     * Accessor for all fetch groups defined for this fetch plan.
     * @return The fetch groups
     */
    FetchGroupMetadata[] getFetchGroups();

    /**
     * Add a new fetch group for this fetch plan.
     * @param name Name of fetch group.
     * @return The FetchGroupMetadata
     */
    FetchGroupMetadata newFetchGroupMetadata(String name);

    /**
     * Accessor for the number of fetch groups defined for this fetch plan.
     * @return The number of fetch groups
     */
    int getNumberOfFetchGroups();
}
