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
 
package org.apache.jdo.tck.pc.order;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.jdo.tck.util.ConversionHelper;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

/**
 * Utility class to create a graph of order model instances from an xml
 * representation. 
 *
 */
public class OrderModelReader extends XmlBeanFactory {

    /** The format of date values in the xml representation */
    public static final String DATE_PATTERN = "d/MMM/yyyy";

    /** The name of the root list bean. */
    public static final String ROOT_LIST_NAME = "root";

    /** The bean-factory name in the xml input files.
     */
    public static final String BEAN_FACTORY_NAME = "orderFactory";

    /** The order factory instance. */
    private OrderFactory orderFactory;

    /** 
     * Create a OrderModelReader for the specified resourceName. 
     * @param resourceName the name of the resource
     */
    public OrderModelReader(String resourceName) {
        // Use the class loader of the Order class to find the resource
        this(resourceName, Order.class.getClassLoader());
    }

    /** 
     * Create a OrderModelReader for the specified resourceName. 
     * @param resourceName the name of the resource
     */
    public OrderModelReader(String resourceName, ClassLoader classLoader) {
        super(new ClassPathResource(resourceName, classLoader));
        configureFactory();
    }

    /**
     * Create a OrderModelReader for the specified InputStream.
     * @param stream the input stream
     */
    public OrderModelReader(InputStream stream) {
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
     * Configure the OrderModelReader, e.g. register CustomEditor classes
     * to convert the string representation of a property into an instance
     * of the right type.
     */
    private void configureFactory() {
        SimpleDateFormat formatter =
            new SimpleDateFormat(DATE_PATTERN, Locale.US);
        CustomDateEditor dateEditor =
            new CustomDateEditor(formatter, true);
        registerCustomEditor(Date.class, dateEditor);
        orderFactory = OrderFactoryRegistry.getInstance();
        addSingleton(BEAN_FACTORY_NAME, orderFactory);
    }
    
    // Convenience methods

    /** 
     * Convenience method returning a Order instance for the specified 
     * name. The method returns <code>null</code> if there is no Order
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Order bean.
     */
    public Order getOrder(String name) {
        return (Order)getBean(name, Order.class);
    }

    /** 
     * Convenience method returning a OrderItem instance for the specified
     * name. The method returns <code>null</code> if there is no Department
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Department bean.
     */
    public OrderItem getOrderItem(String name) {
        return (OrderItem)getBean(name, OrderItem.class);
    }

    /**
     * @return Returns the tearDownClasses.
     */
    public Class[] getTearDownClassesFromFactory() {
        return orderFactory.getTearDownClasses();
    }
    
    /**
     * @return Returns the tearDownClasses.
     */
    public static Class[] getTearDownClasses() {
        return OrderFactoryConcreteClass.tearDownClasses;
    }

    public static Date stringToUtilDate(String value) {
        return ConversionHelper.toUtilDate(DATE_PATTERN, Locale.US, value);
    }
}

