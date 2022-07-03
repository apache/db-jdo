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
 
package org.apache.jdo.tck.pc.mylib;

import java.util.Date;
import java.util.List;

import org.apache.jdo.tck.util.JDOCustomDateEditor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Utility class to create a mylib instances from an xml
 * representation. 
 */
public class MylibReader extends DefaultListableBeanFactory {

    /** The name of the root list bean. */
    public static final String ROOT_LIST_NAME = "root";

    /** Teardown classes 
     */
    private static final Class<?>[] tearDownClasses = new Class[] {
        PrimitiveTypes.class, PCClass.class
    };

    /** Bean definition reader  */
    private final XmlBeanDefinitionReader reader;

    /** 
     * Create a MylibReader for the specified resourceName. 
     * @param resourceName the name of the resource
     */
    public MylibReader(String resourceName) {
        // Use the class loader of the PrimitiveTypes class to find the resource
        this(resourceName, PrimitiveTypes.class.getClassLoader());
    }

    /** 
     * Create a MylibReader for the specified resourceName. 
     * @param resourceName the name of the resource
     * @param classLoader the ClassLoader for the lookup
     */
    public MylibReader(String resourceName, ClassLoader classLoader) {
        super();
        configureFactory();
        this.reader = new XmlBeanDefinitionReader(this);
        this.reader.loadBeanDefinitions(new ClassPathResource(resourceName, classLoader));
    }

    /** 
     * Returns a list of root objects. The method expects to find a bean
     * called "root" of type list in the xml and returns it.
     * @return a list of root instances
     */
    public List<Object> getRootList() {
        return (List<Object>)getBean(ROOT_LIST_NAME);
    }
    
    /** 
     * Configure the MylibReader, e.g. register CustomEditor classes
     * to convert the string representation of a property into an instance
     * of the right type.
     */
    private void configureFactory() {
        registerCustomEditor(Date.class, JDOCustomDateEditor.class);
    }
    
    // Convenience methods

    /** 
     * Convenience method returning an Address instance for the specified
     * name. The method returns <code>null</code> if there is no Address
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Address bean.
     */
    public PrimitiveTypes getPrimitiveTypes(String name) {
        return getBean(name, PrimitiveTypes.class);
    }

    /**
     * @return Returns the tearDownClasses.
     */
    public static Class<?>[] getTearDownClasses() {
        return tearDownClasses;
    }
}

