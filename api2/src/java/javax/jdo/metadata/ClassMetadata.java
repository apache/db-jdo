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
 * Represents a class.
 * @since 2.3
 */
public interface ClassMetadata extends ComponentMetadata {
    /**
     * Method to define the persistence modifier.
     * @param mod persistence modifier
     */
    ClassMetadata setPersistenceModifier(ClassPersistenceModifier mod);

    /**
     * Accessor for the persistence modifier.
     * @return persistence modifier
     */
    ClassPersistenceModifier getPersistenceModifier();

    /**
     * Accessor for all fields defined on the class.
     * @return The fields
     */
    FieldMetadata[] getFields();

    /**
     * Add a new field to this class.
     * @param name Name of the field
     * @return The FieldMetadata
     */
    FieldMetadata newFieldMetadata(String name);

    /**
     * Accessor for the number of fields defined for this class.
     * @return The number of fields
     */
    int getNumberOfFields();
}