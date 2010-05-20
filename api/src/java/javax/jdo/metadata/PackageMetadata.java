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

import javax.jdo.annotations.SequenceStrategy;

/**
 * Represents a package within a JDOMetadata.
 * @since 3.0
 */
public interface PackageMetadata extends Metadata {
    /**
     * Accessor for the name of this package (set on construction).
     * 
     * @return The name
     */
    String getName();

    /**
     * Method to set the catalog (ORM) to apply to all classes in this package.
     * 
     * @param catalog Catalog name
     */
    PackageMetadata setCatalog(String catalog);

    /**
     * Accessor for the catalog (ORM) that all classes in this package default
     * to.
     * 
     * @return The catalog
     */
    String getCatalog();

    /**
     * Method to set the schema (ORM) to apply to all classes in this package.
     * 
     * @param schema Schema name
     */
    PackageMetadata setSchema(String schema);

    /**
     * Accessor for the schema (ORM) that all classes in this package default to.
     * 
     * @return The schema
     */
    String getSchema();

    /**
     * Accessor for all classes defined in this package.
     * 
     * @return The classes
     */
    ClassMetadata[] getClasses();

    /**
     * Add a new class to this package.
     * 
     * @param name Name of the class
     * @return The ClassMetadata
     */
    ClassMetadata newClassMetadata(String name);

    /**
     * Add a new class to this package.
     * 
     * @param cls The class
     * @return The ClassMetadata
     */
    ClassMetadata newClassMetadata(Class cls);

    /**
     * Accessor for the number of classes defined in this package.
     * 
     * @return The number of classes.
     */
    int getNumberOfClasses();

    /**
     * Accessor for all interfaces defined in this package.
     * 
     * @return The interfaces
     */
    InterfaceMetadata[] getInterfaces();

    /**
     * Add a new interface to this package.
     * 
     * @param name The interface name
     * @return The InterfaceMetadata
     */
    InterfaceMetadata newInterfaceMetadata(String name);

    /**
     * Add a new interface to this package.
     * 
     * @param cls The class
     * @return The ClassMetadata
     */
    InterfaceMetadata newInterfaceMetadata(Class cls);

    /**
     * Accessor for the number of interfaces defined in this package.
     * 
     * @return The number of interfaces.
     */
    int getNumberOfInterfaces();

    /**
     * Accessor for any sequences defined on the package.
     * 
     * @return The sequences
     */
    SequenceMetadata[] getSequences();

    /**
     * Add a new sequence to this package.
     * 
     * @param name Name of the sequence
     * @param strategy Strategy for the sequence
     * @return The SequenceMetadata
     */
    SequenceMetadata newSequenceMetadata(String name, SequenceStrategy strategy);

    /**
     * Accessor for the number of sequences defined for this package.
     * 
     * @return The number of sequences.
     */
    int getNumberOfSequences();
}
