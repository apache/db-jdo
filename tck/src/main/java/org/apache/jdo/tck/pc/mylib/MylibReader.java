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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

/**
 * Utility class to create a mylib instances from an xml
 * representation. 
 */
public class MylibReader extends XmlBeanFactory {

    /** The format of date values in the xml representation */
    public static final String DATE_PATTERN = "d/MMM/yyyy";

    /** The name of the root list bean. */
    public static final String ROOT_LIST_NAME = "root";

    /** Teardown classes 
     */
    private static final Class[] tearDownClasses = new Class[] {
        PrimitiveTypes.class, PCClass.class
    };
    
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
     */
    public MylibReader(String resourceName, ClassLoader classLoader) {
        super(new ClassPathResource(resourceName, classLoader));
        configureFactory();
    }

    /**
     * Create a MylibReader for the specified InputStream.
     * @param stream the input stream
     */
    public MylibReader(InputStream stream) {
        super(new InputStreamResource(stream));
        configureFactory();
    }

    /** 
     * Returns a list of root objects. The method expects to find a bean
     * called "root" of type list in the xml and returns it.
     * @return a list of root instances
     */
    public List getRootList() {
        return (List)getBean(ROOT_LIST_NAME);
    }
    
    /** 
     * Configure the MylibReader, e.g. register CustomEditor classes
     * to convert the string representation of a property into an instance
     * of the right type.
     */
    private void configureFactory() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        CustomDateEditor dateEditor =
            new CustomDateEditor(formatter, true);
        registerCustomEditor(Date.class, dateEditor);
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
        return (PrimitiveTypes)getBean(name, PrimitiveTypes.class);
    }

    /**
     * @return Returns the tearDownClasses.
     */
    public static Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}

