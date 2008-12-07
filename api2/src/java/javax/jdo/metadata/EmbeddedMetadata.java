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
 * @since 2.3
 */
public interface EmbeddedMetadata extends Metadata {
    /**
     * Method to set the name of the owner field.
     * 
     * @param fld Name of the owner field
     */
    void setOwnerField(String fld);

    /**
     * Accessor for the owner field name.
     * 
     * @return The owner field name
     */
    String getOwnerField();

    /**
     * Method to set any column that indicates a null embedded object
     * 
     * @param col Null indicator column
     */
    void setNullIndicatorColumn(String col);

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
    void setNullIndicatorValue(String val);

    /**
     * Accessor for a null indicator value
     * 
     * @return Null indicator value
     */
    String getNullIndicatorValue();

    /**
     * Accessor for all fields defined on the fetch group.
     * 
     * @return The fields
     */
    FieldMetadata[] getFields();

    /**
     * Add a new field to be embedded
     * 
     * @param name Name of the field
     * @return The FieldMetadata
     */
    FieldMetadata newFieldMetadata(String name);

    /**
     * Accessor for the number of fields defined for embedding
     * 
     * @return The number of fields
     */
    int getNumberOfFields();

    /**
     * Accessor for all properties defined for embedding
     * 
     * @return The properties
     */
    PropertyMetadata[] getProperties();

    /**
     * Add a new property for embedding
     * 
     * @param name Name of the property
     * @return The PropertyMetadata
     */
    PropertyMetadata newPropertyMetadata(String name);

    /**
     * Accessor for the number of properties defined for embedding
     * 
     * @return The number of properties
     */
    int getNumberOfProperties();
}