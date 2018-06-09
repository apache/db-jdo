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
 * Represents details of a map in a field/property in a class.
 * @since 3.0
 */
public interface MapMetadata extends Metadata {
    /**
     * Method to set the name of the key type.
     * @param type Name of the key type
     * @return This metadata object
     */
    MapMetadata setKeyType(String type);

    /**
     * Accessor for the key type.
     * @return The key type
     */
    String getKeyType();

    /**
     * Method to set whether the key is embedded.
     * @param val Whether it is embedded
     * @return This metadata object
     */
    MapMetadata setEmbeddedKey(boolean val);

    /**
     * Accessor for whether the key is embedded.
     * @return whether the key is embedded
     */
    Boolean getEmbeddedKey();

    /**
     * Method to set whether the key is serialised.
     * @param val Whether it is serialised
     * @return This metadata object
     */
    MapMetadata setSerializedKey(boolean val);

    /**
     * Accessor for whether the key is serialised.
     * @return whether the key is serialised
     */
    Boolean getSerializedKey();

    /**
     * Method to set whether the key is dependent.
     * @param val Whether it is dependent
     * @return This metadata object
     */
    MapMetadata setDependentKey(boolean val);

    /**
     * Accessor for whether the key is dependent.
     * @return whether the key is dependent
     */
    Boolean getDependentKey();

    /**
     * Method to set the name of the value type.
     * @param type Name of the value type
     * @return This metadata object
     */
    MapMetadata setValueType(String type);

    /**
     * Accessor for the value type.
     * @return The value type
     */
    String getValueType();

    /**
     * Method to set whether the value is embedded.
     * @param val Whether it is embedded
     * @return This metadata object
     */
    MapMetadata setEmbeddedValue(boolean val);

    /**
     * Accessor for whether the value is embedded.
     * @return whether the value is embedded
     */
    Boolean getEmbeddedValue();

    /**
     * Method to set whether the value is serialised.
     * @param val Whether it is serialised
     * @return This metadata object
     */
    MapMetadata setSerializedValue(boolean val);

    /**
     * Accessor for whether the value is serialised.
     * @return whether the value is serialised
     */
    Boolean getSerializedValue();

    /**
     * Method to set whether the value is dependent.
     * @param val Whether it is dependent
     * @return This metadata object
     */
    MapMetadata setDependentValue(boolean val);

    /**
     * Accessor for whether the value is dependent.
     * @return whether the value is dependent
     */
    Boolean getDependentValue();
}
