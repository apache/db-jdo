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


import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static javax.jdo.Constants.PROPERTY_ENHANCER_VENDOR_NAME;
import static javax.jdo.Constants.PROPERTY_ENHANCER_VERSION_NUMBER;

import javax.jdo.metadata.JDOMetadata;


/**
 * Tests class javax.jdo.Enhancer (main class).
 * <p>
 */
public class MockEnhancer implements JDOEnhancer {

    static final Properties props = new Properties();
    static {
        props.put(PROPERTY_ENHANCER_VENDOR_NAME, "Mock Enhancer");
        props.put(PROPERTY_ENHANCER_VERSION_NUMBER, "2.3.0");
        props.put("MockKey", "MockValue");
    }
    @SuppressWarnings("unused")
    private boolean verbose;
    private int numberOfElements;
    private final List<String> classNames = new ArrayList<>();
    private final List<String> jarNames = new ArrayList<>();
    private final List<String> jdoNames = new ArrayList<>();
    private final List<String> puNames = new ArrayList<>();
    @SuppressWarnings("unused")
    private String outputDirectory = null;

    public MockEnhancer(){
    }

    public Properties getProperties() {
        return props;
    }

    public JDOEnhancer setVerbose(boolean flag) {
        this.verbose = flag;
        return this;
    }

    public JDOEnhancer setOutputDirectory(String dirName) {
        outputDirectory = dirName;
        return this;
    }

    public JDOEnhancer setClassLoader(ClassLoader loader) {
        // check to see if JDOHelper is loadable from the loader
        try {
        loader.loadClass("javax.jdo.JDOHelper");
        } catch (ClassNotFoundException ex) {
            // bad
            throw new JDOFatalInternalException("Should be able to load JDOHelper from the class loader");
        }
        return this;
    }

    public JDOEnhancer addPersistenceUnit(String persistenceUnit) {
        numberOfElements++;
        this.puNames.add(persistenceUnit);
        return this;
    }

    public JDOEnhancer addClass(String className, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JDOEnhancer addClasses(String... classNames) {
        numberOfElements += classNames.length;
        this.classNames.addAll(Arrays.asList(classNames));
        return this;
    }

    public JDOEnhancer addFiles(String... metadataFiles) {
        numberOfElements += metadataFiles.length;
        this.jdoNames.addAll(Arrays.asList(metadataFiles));
        return this;
    }

    public JDOEnhancer addJar(String jarFileName) {
        numberOfElements++;
        this.jarNames.add(jarFileName);
        return this;
    }

    public int enhance() {
        return numberOfElements;
    }

    public int validate() {
        return numberOfElements;
    }

    public byte[] getEnhancedBytes(String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void registerMetadata(JDOMetadata metadata) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JDOMetadata newMetadata() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

