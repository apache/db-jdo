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
package javax.jdo;

import java.lang.instrument.ClassFileTransformer;
import java.util.Properties;

import javax.jdo.metadata.JDOMetadata;

/**
 * Interface for a JDO Enhancer.
 * @since 2.3
 */
public interface JDOEnhancer extends ClassFileTransformer
{
    /**
     * Return non-configurable properties of this JDOEnhancer.
     * Properties with keys "VendorName" and "VersionNumber" are required. Other keys are optional.
     * @return the non-configurable properties of this JDOEnhancer.
     */
    Properties getProperties();

    /**
     * Whether to provide verbose output
     * @param flag Verbose?
     * @return The enhancer
     */
    JDOEnhancer setVerbose(boolean flag);

    /**
     * Mutator to set the location where enhanced classes are written.
     * Mutator to set the location where enhanced classes are written.
     * If this method is not called, classes will be enhanced in place, 
     * overwriting the existing classes. If overwriting classes in a jar file,
     * the existing files in the jar file will be written unchanged except
     * for the enhanced classes. The directory name can be absolute or relative.
     * @param dirName Name of the directory
     * @return The enhancer
     */
    JDOEnhancer setOutputDirectory(String dirName);

    /**
     * Mutator to set the class loader to use for loading classes.
     * @param loader ClassLoader to use
     * @return The enhancer
     */
    JDOEnhancer setClassLoader(ClassLoader loader);

    /**
     * Add a persistence-unit to the items to be enhanced.
     * @param persistenceUnit Name of the persistence unit
     * @return The enhancer
     */
    JDOEnhancer addPersistenceUnit(String persistenceUnit);

    /**
     * Add an in-memory class to the items to be enhanced.
     * The class name should be of the form "mydomain.MyClass".
     * @param className Name of the class
     * @param bytes The bytes of the class
     * @return The enhancer
     */
    JDOEnhancer addClass(String className, byte[] bytes);

    /**
     * Add class(es) to the items to be enhanced.
     * The class names can be absolute file names, relative file names, or
     * names of CLASSPATH resources.
     * @param classNames Names of the classes
     * @return The enhancer
     */
    JDOEnhancer addClasses(String... classNames);

    /**
     * Add metadata file(s) to the items to be enhanced.
     * The metadata file names can be absolute file names, relative file names, or
     * names of CLASSPATH resources. They should be JDO XML metadata files.
     * @param metadataFiles Names of the files
     * @return The enhancer
     */
    JDOEnhancer addFiles(String... metadataFiles);

    /**
     * Add a jar file to the items to be enhanced.
     * The jar file name can be absolute, or relative or a CLASSPATH resource.
     * @param jarFileName Name of the jar file
     * @return The enhancer
     */
    JDOEnhancer addJar(String jarFileName);

    /**
     * Method to enhance the items specified using addJar, addFiles, addClasses, addClass,
     * addPersistenceUnit.
     * @return Number of classes enhanced
     * @throws JDOEnhanceException if an error occurs during enhancement. If multiple
     * errors occur then the nested exceptions provides this detail.
     */
    int enhance();

    /**
     * Method to validate the items specified using addJar, addFiles, addClasses, addClass,
     * addPersistenceUnit.
     * @return Number of classes validated
     * @throws JDOEnhanceException if an error occurs during validation. If multiple
     * errors occur then the nested exceptions provides this detail.
     */
    int validate();

    /**
     * Method to retrieve the (enhanced) bytes of the specified class.
     * Only applies to the classes enhanced in the most recent enhance() call.
     * If no enhance has yet been performed will throw a JDOEnhanceException.
     * If the specified class hasn't been enhanced then will throw a JDOEnhanceException.
     * @param className Name of the class (of the form "mydomain.MyClass")
     * @return Enhanced bytes
     */
    byte[] getEnhancedBytes(String className);

    /**
     * Method to register metadata with the enhancement process managed by this
     * <code>JDOEnhancer</code>.
     * Metadata can be created using the method {@link #newMetadata}.
     * If there is already metadata registered for a class contained in this metadata
     * object then a JDOUserException will be thrown.
     * @param metadata The Metadata to register.
     * @since 2.3
     */
    void registerMetadata(JDOMetadata metadata);

    /**
     * Method to return a new metadata object that can be subsequently modified
     * and registered with the enhancement process using the method {@link #registerMetadata}.
     * @return The metadata
     * @since 2.3
     */
    JDOMetadata newMetadata();
}