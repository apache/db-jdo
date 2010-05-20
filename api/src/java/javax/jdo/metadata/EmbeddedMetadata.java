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
 * Represents embedding details of a field/property in a class.
 * @since 3.0
 */
public interface EmbeddedMetadata extends Metadata {
    /**
     * Method to set the name of the owner field/property.
     * 
     * @param member Name of the owner member
     */
    EmbeddedMetadata setOwnerMember(String member);

    /**
     * Accessor for the owner field/property name.
     * 
     * @return The owner member name
     */
    String getOwnerMember();

    /**
     * Method to set any column that indicates a null embedded object
     * 
     * @param col Null indicator column
     */
    EmbeddedMetadata setNullIndicatorColumn(String col);

    /**
     * Accessor for any column indicating a null embedded object
     * 
     * @return Whether to call post-load
     */
    String getNullIndicatorColumn();

    /**
     * Method to set the value of a null indicator column to signify null object
     * 
     * @param val Null indicator value
     */
    EmbeddedMetadata setNullIndicatorValue(String val);

    /**
     * Accessor for a null indicator value
     * 
     * @return Null indicator value
     */
    String getNullIndicatorValue();

    /**
     * Accessor for all fields/properties defined on the fetch group.
     * 
     * @return The members
     */
    MemberMetadata[] getMembers();

    /**
     * Accessor for the number of fields/properties defined for embedding
     * 
     * @return The number of members
     */
    int getNumberOfMembers();

    /**
     * Add a new field to be embedded.
     * 
     * @param name Name of the field
     * @return The FieldMetadata
     */
    FieldMetadata newFieldMetadata(String name);

    /**
     * Add a new property for embedding
     * 
     * @param name Name of the property
     * @return The PropertyMetadata
     */
    PropertyMetadata newPropertyMetadata(String name);
}
