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
 * Represents a fetch group for a class.
 * @since 3.0
 */
public interface FetchGroupMetadata extends Metadata {
    /**
     * Accessor for the fetch group name (set on construction).
     * @return The fetch group name
     */
    String getName();

    /**
     * Method to set whether to call post load with this fetch group
     * @param load Call post load
     * @return This metadata object
     */
    FetchGroupMetadata setPostLoad(boolean load);

    /**
     * Accessor for whether to call post load for this fetch group.
     * @return Whether to call post-load
     */
    Boolean getPostLoad();

    /**
     * Accessor for all fields/properties defined on the fetch group.
     * @return The members
     */
    MemberMetadata[] getMembers();

    /**
     * Accessor for the number of fields/properties defined for this fetch group.
     * @return The number of members
     */
    int getNumberOfMembers();

    /**
     * Add a new field for this fetch group.
     * @param name Name of field
     * @return The FieldMetadata
     */
    FieldMetadata newFieldMetadata(String name);

    /**
     * Add a new property for this fetch group.
     * @param name Name of property
     * @return The PropertyMetadata
     */
    PropertyMetadata newPropertyMetadata(String name);
}
